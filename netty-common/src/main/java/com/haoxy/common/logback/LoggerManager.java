package com.haoxy.common.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.Test;

public class LoggerManager {

    private static Logger logger = LoggerFactory.getLogger(LoggerManager.class);




    public static void debug(String msg, Object... args){
        logger.debug(msg, args);
    }


    public static void error(String msg, Throwable t){
        logger.error(msg, t);
    }

    public static void info(String msg, Object... args){
        logger.info(msg, args);
    }








}
