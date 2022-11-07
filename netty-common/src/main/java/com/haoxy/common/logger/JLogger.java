package com.haoxy.common.logger;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.OptionHelper;
import org.slf4j.LoggerFactory;

public class JLogger {

    private static Logger DEBUG = LoggerFactory.getLogger("DEBUG");
    private static Logger INFO = LoggerFactory.getLogger("INFO");
    private static Logger ERROR = LoggerFactory.getLogger("ERROR");
    private static Logger FILE = LoggerFactory.getLogger("FILE");




//    /**
//     * 根据logFileName获取logger对象
//     *
//     * @param logFileName
//     * @return
//     */
//    public static Logger getLogger(String logFileName) {
//        Logger logger = logFileNameMap.get(logFileName);
//        if (logger != null) {
//            return logger;
//        }
//        logger = createLogger(logFileName);
//        logFileNameMap.put(logFileName, logger);
//        return logger;
//    }



    /**
     * 根据传入logFileName创建当前Logger对象输出日志的文件名
     *
     * @param logFileName 要创建的日志文件名称
     * @return
     */
    private static Logger createLogger(String logFileName, int day) {

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        //创建logger对象
        Logger logger = loggerContext.getLogger(logFileName);
        logger.setAdditive(false);

        //创建appender，滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件
        RollingFileAppender appender = new RollingFileAppender();
        appender.setContext(loggerContext);
        // 设置appender的名称
        appender.setName(logFileName);
        // 创建活动日志（当天日志）打印文件
        appender.setFile(OptionHelper.substVars(logFileName + "/collection.log", loggerContext));
        // 设置日志是否追加到文件结尾，true:时是,false:否
        appender.setAppend(true);
        // 日志是否线程安全写入文件，true：是，false：否，默认false
        appender.setPrudent(false);

        // 定义滚动策略，按时间及大小进行滚动
        TimeBasedRollingPolicy policy = new TimeBasedRollingPolicy();
        // 定义归档文件路径及名称
        policy.setParent(appender);
        policy.setContext(loggerContext);
        // 设置归档文件名
        policy.setFileNamePattern(logFileName);
        if(day > 0){
            // 设置归档文件保留的最大数量，这里设置30天
            policy.setMaxHistory(day);
        }
        // 设置全部日志文件最大体积
        policy.start();

        //设置输出到日志文件的格式
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        // 日志格式
        encoder.setPattern("%d#|###|#%m%n");
        encoder.start();

        //设置输出到控制台的日志文件格式
        PatternLayoutEncoder encoder1 = new PatternLayoutEncoder();
        encoder1.setContext(loggerContext);
        encoder1.setPattern("%d %p (%file:%line\\)- %m%n");
        encoder1.start();

        /*设置动态日志控制台输出*/
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setContext(loggerContext);
        consoleAppender.setEncoder(encoder1);
        consoleAppender.start();
        logger.addAppender(consoleAppender);


        // 设置appender记录日志的滚动策略
        appender.setRollingPolicy(policy);
        appender.setEncoder(encoder);
        appender.start();
        logger.addAppender(appender);

        return logger;

    }




    public static void debug(String msg, Object... args){
        DEBUG.debug(msg, args);
    }


    public static void info(String msg, Object... args){
        INFO.info(msg, args);
    }

    public static void error(String msg, Throwable e){
        ERROR.error(msg, e);
    }






}
