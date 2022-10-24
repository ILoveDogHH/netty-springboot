package com.haoxy.common.cmd;

public class RequestExecuteResult<T> {
	public static final int CODE_NO_EXECUTOR = 0;
	public static final int CODE_ERROR = -1;
	public static final int CODE_ERROR2 = -2;
	public static final int CODE_SYNC = 1;
	public static final int CODE_ASNYC = 2;

	public final int code;
	public final T result;

	private RequestExecuteResult(int code, T result) {
		this.code = code;
		this.result = result;
	}

	/**
	 * 发生错误
	 * 
	 * @param error
	 * @return
	 */
	public static RequestExecuteResult<Throwable> error(Throwable error) {
		return new RequestExecuteResult<>(CODE_ERROR, error);
	}
	

	public static RequestExecuteResult<String> error2(String errorMsg) {
		return new RequestExecuteResult<>(CODE_ERROR2, errorMsg);
	}

	/**
	 * 成功的异步请求
	 * 
	 * @return
	 */
	public static RequestExecuteResult<Void> async() {
		return new RequestExecuteResult<>(CODE_ASNYC, null);
	}

	/**
	 * 成功的同步请求
	 * 
	 * @param data
	 * @return
	 */
	public static <T> RequestExecuteResult<T> sync(T data) {
		return new RequestExecuteResult<>(CODE_SYNC, data);
	}

	/**
	 * 成功的同步请求
	 * 
	 * @return
	 */
	public static <T> RequestExecuteResult<T> noexecutor() {
		return new RequestExecuteResult<>(CODE_NO_EXECUTOR, null);
	}
}
