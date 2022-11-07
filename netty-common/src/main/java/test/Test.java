package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Test {



    private static Logger loggerDebug = LoggerFactory.getLogger("DEBUG");
    private static Logger loggerINFO = LoggerFactory.getLogger("INFO");


    public static void main(String[] args){
        SpringApplication.run(Test.class,args);
//        logger.error("测试错误error========================{}");
//        logger.info("测试日志info========================{}", 1);
        loggerDebug.debug("debug234234234");
        loggerINFO.info("cdscwerwrwrwrw");
    }

}
