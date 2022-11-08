package com.haoxy.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JLogger {

    private static Logger DEBUG = LoggerFactory.getLogger("DEBUG");
    private static Logger INFO = LoggerFactory.getLogger("INFO");
    private static Logger ERROR = LoggerFactory.getLogger("ERROR");







    public static void debug(String msg, Object... args){
        DEBUG.debug(msg, args);
    }


    public static void info(String msg, Object... args){
        INFO.info(msg, args);
    }

    public static void error(String msg, Throwable e){
        ERROR.error(msg, e);
    }


    public static void gameLog(String fileName, String msg, Object... args){
        JLoggerManager.gameLog(fileName, msg, args);
    }








}
