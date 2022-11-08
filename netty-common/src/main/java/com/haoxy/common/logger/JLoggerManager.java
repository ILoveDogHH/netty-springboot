package com.haoxy.common.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class JLoggerManager {




    private static String maxFileSize = "50MB";
    private static String totalSizeCap = "10GB";
    private static int maxHistory = 30;



    private static final String SYS_PATH = System.getProperty("user.dir");


    public static void gameLog(String fileName, String msg, Object... args){
        Logger logger = getLogger(SYS_PATH);
        logger.info(msg, args);
    }


    private static String getPath(String type){
        return String.format("%s/log/%s/", System.getProperty("user.dir"), type);
    }


    /**
     * description: 获取自定义的logger日志，在指定日志文件logNameEnum.getLogName()中输出日志
     * 日志中会包括所有线程及方法堆栈信息
     *
     * @return org.slf4j.Logger
     * @author Hlingoes 2020/6/10
     */
    public static Logger getLogger(String filePath) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ClassLoader.class);
        LoggerContext loggerContext = logger.getLoggerContext();
        RollingFileAppender infoAppender = createAppender(filePath, Level.INFO, loggerContext);
        // 设置不向上级打印信息
        logger.setAdditive(false);
        logger.addAppender(infoAppender);
        return logger;
    }

    /**
     * description: 创建日志文件的file appender
     *
     * @param name
     * @param level
     * @return ch.qos.logback.core.rolling.RollingFileAppender
     * @author Hlingoes 2020/6/10
     */
    private static RollingFileAppender createAppender(String name, Level level, LoggerContext loggerContext) {
        RollingFileAppender appender = new RollingFileAppender();
        // 这里设置级别过滤器
        appender.addFilter(createLevelFilter(level));
        // 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<scope="context">设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        appender.setContext(loggerContext);
        // appender的name属性
        appender.setName(name.toUpperCase() + "-" + level.levelStr.toUpperCase());
        // 读取logback配置文件中的属性值，设置文件名
        String logPath = OptionHelper.substVars( name + "-" + level.levelStr.toLowerCase() + ".log", loggerContext);
        appender.setFile(logPath);
        appender.setAppend(true);
        appender.setPrudent(false);
        // 加入下面两个节点
        appender.setRollingPolicy(createRollingPolicy(name, level, loggerContext, appender));
        appender.setEncoder(createEncoder(loggerContext));
        appender.start();
        return appender;
    }

    /**
     * description: 创建窗口输入的appender
     *
     * @param
     * @return ch.qos.logback.core.ConsoleAppender
     * @author Hlingoes 2020/6/10
     */
    private static ConsoleAppender createConsoleAppender(LoggerContext loggerContext) {
        ConsoleAppender appender = new ConsoleAppender();
        appender.setContext(loggerContext);
        appender.addFilter(createLevelFilter(Level.DEBUG));
        appender.setEncoder(createEncoder(loggerContext));
        appender.start();
        return appender;
    }

    /**
     * description: 设置日志的滚动策略
     *
     * @param name
     * @param level
     * @param context
     * @param appender
     * @return ch.qos.logback.core.rolling.TimeBasedRollingPolicy
     * @author Hlingoes 2020/6/10
     */
    private static TimeBasedRollingPolicy createRollingPolicy(String name, Level level, LoggerContext context, FileAppender appender) {
        // 读取logback配置文件中的属性值，设置文件名
        String fp = OptionHelper.substVars("${logPath}/${LOG_NAME_PREFIX}-" + name + "-" + level.levelStr.toLowerCase() + "_%d{yyyy-MM-dd}_%i.log", context);
        TimeBasedRollingPolicy rollingPolicyBase = new TimeBasedRollingPolicy<>();
        // 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<scope="context">设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        rollingPolicyBase.setContext(context);
        // 设置父节点是appender
        rollingPolicyBase.setParent(appender);
        // 设置文件名模式
        rollingPolicyBase.setFileNamePattern(fp);
        SizeAndTimeBasedFNATP sizeAndTimeBasedFNATP = new SizeAndTimeBasedFNATP();
        // 最大日志文件大小
        sizeAndTimeBasedFNATP.setMaxFileSize(FileSize.valueOf(maxFileSize));
        rollingPolicyBase.setTimeBasedFileNamingAndTriggeringPolicy(sizeAndTimeBasedFNATP);
        // 设置最大历史记录为30条
        rollingPolicyBase.setMaxHistory(maxHistory);
        // 总大小限制
        rollingPolicyBase.setTotalSizeCap(FileSize.valueOf(totalSizeCap));
        rollingPolicyBase.start();

        return rollingPolicyBase;
    }

    /**
     * description: 设置日志的输出格式
     *
     * @param context
     * @return ch.qos.logback.classic.encoder.PatternLayoutEncoder
     * @author Hlingoes 2020/6/10
     */
    private static PatternLayoutEncoder createEncoder(LoggerContext context) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        // 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<scope="context">设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        encoder.setContext(context);
        // 设置格式
        String pattern = OptionHelper.substVars("${pattern}", context);
        encoder.setPattern(pattern);
        encoder.setCharset(Charset.forName("utf-8"));
        encoder.start();
        return encoder;
    }

    /**
     * description: 设置打印日志的级别
     *
     * @param level
     * @return ch.qos.logback.core.filter.Filter
     * @author Hlingoes 2020/6/10
     */
    private static Filter createLevelFilter(Level level) {
        LevelFilter levelFilter = new LevelFilter();
        levelFilter.setLevel(level);
        levelFilter.setOnMatch(FilterReply.ACCEPT);
        levelFilter.setOnMismatch(FilterReply.DENY);
        levelFilter.start();
        return levelFilter;
    }



}
