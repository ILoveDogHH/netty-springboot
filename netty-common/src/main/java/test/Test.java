package test;

import com.haoxy.common.logger.JLogger;
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
        JLogger.gameLog("E:/github/netty-springboot/netty-common/cc","dddd");

    }

}
