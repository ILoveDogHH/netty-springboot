package com.haoxy.common.cmd;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultFunctionsTable implements FunctionsTable {
	protected ConcurrentHashMap<String, Method> functions = new ConcurrentHashMap<>();

	private ClassSearcher classSearcher;

	private final Class<?> classClass;
	private final Class<?> returnClass;
	private final Class<?>[] paramsClass;

	public DefaultFunctionsTable(Class<?> classClass, Class<?> returnClass, Class<?>[] paramsClasss) {
		this(new ClassSearcher(), classClass, returnClass, paramsClasss);
	}

	public DefaultFunctionsTable(ClassSearcher classSearcher, Class<?> classClass, Class<?> returnClass,
			Class<?>[] paramsClasss) {
		setClassSearcher(classSearcher);
		this.classClass = classClass;
		this.returnClass = returnClass;
		this.paramsClass = paramsClasss;
	}

	/**
	 * 设置classSearcher
	 * 
	 * @param classSearcher
	 */
	private void setClassSearcher(ClassSearcher classSearcher) {
		this.classSearcher = classSearcher;
	}

	/**
	 * 使用特定包名进行初始化
	 * 
	 * @param packageNames
	 * @throws CmdException
	 */
	@Override
	public void init(String[] packageNames) throws CmdException {
		List<String> classNames = new ArrayList<>();
		ConcurrentHashMap<String, Method> tmpFunctions = new ConcurrentHashMap<>();
		// 获取包里所有的类
		for (String packagaName : packageNames) {
			classNames.addAll(classSearcher.getClassNames(packagaName, true));
		}
		for (String className : classNames) {
			try {
				Class<?> cls = Class.forName(className, true, classSearcher.getClassLoader());
				// 检测是否继承CommondBase的
				if (!classClass.isAssignableFrom(cls)) {
					continue;
				}
				Method[] methods = cls.getMethods();
				for (Method method : methods) {
					String methodName = method.getName();
					// 包含"_"的为不可访问的
					if (methodName.lastIndexOf("_") != -1) {
						continue;
					}
					// 检测返回类型
					if (!returnClass.isAssignableFrom(method.getReturnType())) {
						continue;
					}
					Class<?>[] params = method.getParameterTypes();
					// 参数数量要一致
					if (params.length != paramsClass.length) {
						continue;
					}
					// 参数列表的类型一致
					boolean isParamsTypeSame = true;
					for (int i = 0; i < params.length; i++) {
						if (!paramsClass[i].isAssignableFrom(params[i])) {
							isParamsTypeSame = false;
							break;
						}
					}
					if (!isParamsTypeSame) {
						continue;
					}
					// 设置为可视的, 提高效率
					method.setAccessible(true);
					String mergedName = getMergedFuncName(className, methodName);
					if (tmpFunctions.containsKey(mergedName)) {
						throw new CmdException(
								String.format("function [%s.%s] already exists!", className, methodName));
					}
					tmpFunctions.put(mergedName, method);
				}
				functions = tmpFunctions;
			} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
				throw new CmdException("init cmd error: ", e);
			}
		}
	}

	/**
	 * className+MethodName获得唯一的方法名
	 * 
	 * @param className
	 * @param methodName
	 * @return
	 */
	protected String getMergedFuncName(String className, String methodName) {
		return methodName;
	}

	@Override
	public Method getMethodByName(String funcName) throws CmdException {
		if (!functions.containsKey(funcName)) {
			throw new CmdException("no such function:" + funcName);
		}
		return functions.get(funcName);
	}
}
