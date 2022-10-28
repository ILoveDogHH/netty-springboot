package com.jedigames.transport.message.request;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.jedigames.logger.JLogger;
import com.jedigames.transport.message.base.ReceivedMessage;
import com.jedigames.transport.message.base.SentMessage;
import com.jedigames.transport.message.base.SentMessageFactory;
import com.jedigames.utils.TimeManager;

public abstract class AbstractRequestFactory implements RequestFactory {
	private class RequestMsg<T> {
		// 创建者, 用于查找对应的queue
		private Object creator;
		// 消息类型
		private RequestType type;
		// 生成出来的消息
		private SentMessage<T> message;
		// 生成消息时的回调
		private CallbackOnGetMessage<T> onGetMessage;
		// 接受到消息之后的回调
		private CallbackOnResponse onResponse;
		// 消息开始处理的时间
		private long startTime = 0L;
		// 这个消息是不是处理完了
		private volatile boolean finished = false;

		private RequestMsg(Object creator, RequestType type, SentMessage<T> message, CallbackOnResponse onResponse,
				CallbackOnGetMessage<T> onGetMessage) {
			this.creator = creator;
			this.type = type;
			this.message = message;
			this.onResponse = onResponse;
			this.onGetMessage = onGetMessage;
		}

		private long getTimeoutMills() {
			return onResponse.getTimeoutMills();
		}

		private void doGetMessage() {
			onGetMessage.callback(message);
		}

		private void doTimeout() {
			if (onResponse != null) {
				onResponse.onTimeout();
			}
		}

		private void doResponse(ReceivedMessage<?> message) {
			if (onResponse != null) {
				try {
					onResponse.onResponse(message);
				} catch (Exception e) {
					JLogger.error("error on doOnResponse", e);
				}
			}
		}
	}

	private class RequestMsgQueue {
		private final Queue<RequestMsg<?>> queue = new ConcurrentLinkedQueue<>();
		private volatile boolean processingCompleted = true;
		private Thread processThread = null;

		private void interrupt() {
			if (processThread != null) {
				processThread.interrupt();
			}
		}
	}

	private class RequestMsgHandlerTask implements Runnable {
		/**
		 * 创建者
		 */
		@SuppressWarnings("unused")
		private Object creator;
		/**
		 * 消息队列
		 */
		private RequestMsgQueue msgQueue;

		/**
		 * 消息处理的线程
		 */

		public RequestMsgHandlerTask(Object creator, RequestMsgQueue msgQueue) {
			this.creator = creator;
			this.msgQueue = msgQueue;
		}

		@Override
		public void run() {
			msgQueue.processThread = Thread.currentThread();
			while (true) {
				long nextCheckTime = 0L;
				// 将队列锁住, 以免并发问题
				// 检测是不是都处理完成了
				synchronized (msgQueue) {
					if (msgQueue.queue.isEmpty()) {
						msgQueue.processingCompleted = true;
						break;
					}
				}
				for (RequestMsg<?> msg : msgQueue.queue) {
					try {
						if (msg.finished) {
							continue;
						}
						if (msg.startTime == 0) {
							// 首次处理消息
							msg.startTime = TimeManager.getSystemTimestampLong();
							// 如果有回调方法, 放入requestMap
							if (msg.onResponse != null) {
								// 如果sync方法没有onResponse方法 会被下面的超时处理了
								// TODO 如果数据循环了, 如何处理
								requestMap.putIfAbsent(msg.message.getId(), msg);
							}
							// 执行onGetMessage
							msg.doGetMessage();
						}
						// 检测超时处理
						if (msg.getTimeoutMills() > 0) {
							long timeoutTime = msg.startTime + msg.getTimeoutMills();
							if (timeoutTime < TimeManager.getSystemTimestampLong()) {
								// 执行回调方法
								requestMap.remove(msg.message.getId());
								// 从queue中移除
								msgQueue.queue.remove(msg);
								if (!msg.finished) {
									msg.finished = true;
									// 从requestMap中移除
									msg.doTimeout();
								}
							} else {
								if (nextCheckTime == 0 || nextCheckTime > timeoutTime) {
									// 设置下一次的更新时间
									nextCheckTime = timeoutTime;
								}
							}
						}
						if (msg.type == RequestType.SYNC) {
							// 同步请求, 把后续的请求阻塞掉
							break;
						}
					} catch (Throwable e) {
						JLogger.fixError(e.getMessage(), e);
						JLogger.error("error in run", e);
					}
				}
				if (nextCheckTime > 0 && msgQueue.processThread != null) {
					try{
						long needSleepTime = nextCheckTime - TimeManager.getSystemTimestampLong();
						if (needSleepTime > 0) {
							// 多睡1毫秒
							Thread.sleep(needSleepTime + 1);
						}
					} catch (InterruptedException e) {
						// 中断是正常的
					}
				}
			}
			// 清除当前的处理线程
			msgQueue.processThread = null;
		}
	}

	/**
	 * 消息处理器线程池
	 */
	private ExecutorService msgHandlerThreadPool;

	/**
	 * 设置消息处理线程池
	 * 
	 * @param threadPool
	 */
	protected void setMsgHandlerThreadPool(ExecutorService threadPool) {
		msgHandlerThreadPool = threadPool;
	}

	protected ExecutorService getMsgHandlerThreadPool() {
		if (msgHandlerThreadPool == null) {
			msgHandlerThreadPool = Executors.newCachedThreadPool();
		}
		return msgHandlerThreadPool;
	}

	/**
	 * 同步消息队列map
	 */
	private ConcurrentHashMap<Object, RequestMsgQueue> syncQueueMap = new ConcurrentHashMap<>();
	/**
	 * 请求map
	 */
	protected ConcurrentHashMap<Integer, RequestMsg<?>> requestMap = new ConcurrentHashMap<>();

	/**
	 * 自动递增序列
	 */
	private AtomicInteger index = new AtomicInteger(1);
	private Object lock = new Object();

	/**
	 * 获取唯一的index. 线程安全的</br>
	 * 当index>interger.MAX_VALUE-1000时, index会从1重新开始循化
	 */
	protected final int getIndexAndIncrement() {
		synchronized (lock) {
			int id = index.incrementAndGet();
			if (id > Integer.MAX_VALUE - 1000) {
				index.set(1);
			}
			return id;
		}
	}

	protected abstract <T> SentMessageFactory<T> getSendMessageFactory(int opcode, Class<T> claz);

	/**
	 * 生成新的请求
	 * 
	 * @param creator
	 *            创建者, 用来区分对应的threadpool
	 * @param type
	 *            消息类型RequestType:</br>
	 *            SYNC类型, 会等待消息处理完毕, 才会开始下一个message的处理;</br>
	 *            ASYNC类型, 如果当前没有SYNC类型的阻塞型请求, 将会
	 * @param uid
	 *            message的uid, 用于服务器这边做处理线程的分离
	 * @param opcode
	 * @param data
	 * @param onResponse
	 *            当消息被对面处理完回调时
	 * @param onGetMessage
	 *            消息生成之后如何处理
	 */
	@Override
	public <T> void newRequest(Object creator, RequestType type, int uid, int opcode, T data,
			CallbackOnResponse onResponse, CallbackOnGetMessage<T> onGetMessage) {
		int index = getIndexAndIncrement();
		@SuppressWarnings("unchecked")
		SentMessageFactory<T> factory = (SentMessageFactory<T>) getSendMessageFactory(opcode, data.getClass());
		if (factory == null) {
			JLogger.error("new request erro: no message-factory", new Exception());
			return;
		}
		SentMessage<T> message = factory.create(uid, index, opcode, data);
		// 启用线程池来做消息处理
		try {
			RequestMsg<T> msg = new RequestMsg<>(creator, type, message, onResponse, onGetMessage);
			if (syncQueueMap.containsKey(creator)) {
				RequestMsgQueue callbackQueue = syncQueueMap.get(creator);
				// 拿到callbackQueue的锁, 来更新processingCompleted数据
				synchronized (callbackQueue) {
					boolean msgProcessingCompleted = callbackQueue.processingCompleted;
					if (!msgProcessingCompleted) {
						// 消息队列没有处理完毕，直接添加，原来的线程会继续处理
						callbackQueue.queue.offer(msg);
						// 中断睡眠
						callbackQueue.interrupt();
					} else {
						// 之前的已经处理完了，重新分配一个线程来处理
						callbackQueue.queue.offer(msg);
						callbackQueue.processingCompleted = false;
						getMsgHandlerThreadPool().execute(new RequestMsgHandlerTask(creator, callbackQueue));
					}
				}
			} else {
				RequestMsgQueue callbackQueue = new RequestMsgQueue();
				callbackQueue.queue.offer(msg);
				callbackQueue.processingCompleted = false;
				// 锁住这个对象, 以免在其他进程中使用这个对象时发生问题
				synchronized (callbackQueue) {
					// TODO 这里有并发问题. 考虑到这个队列在后续还是会执行掉, 并且如果发生并发时不会有内存溢出问题, 就先跳过
					syncQueueMap.put(creator, callbackQueue);
					getMsgHandlerThreadPool().submit(new RequestMsgHandlerTask(creator, callbackQueue));
				}
			}
		} catch (Throwable e) {
			JLogger.fixError(e.getMessage(), e);
			JLogger.error("new request error: on handle", e);
		}
	}

	/**
	 * 执行message对应的callback
	 * 
	 * @param message
	 * @return
	 */
	@Override
	public boolean doCallback(ReceivedMessage<?> message) {
		if (!requestMap.containsKey(message.getId())) {
			return true;
		}
		boolean handled = false;
		RequestMsg<?> msg = requestMap.get(message.getId());
		requestMap.remove(message.getId());
		// 执行回调
		if (msg != null) {
			if (!msg.finished) {
				msg.finished = true;
				RequestMsgQueue msgQueue = syncQueueMap.get(msg.creator);
				if (msgQueue != null) {
					msgQueue.queue.remove(msg);
					if (msgQueue.processThread != null) {
						// 中断睡眠
						msgQueue.interrupt();
					}
				}
				msg.doResponse(message);
			}
		}
		handled = true;
		return handled;
	}
}
