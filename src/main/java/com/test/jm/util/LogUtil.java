package com.test.jm.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.test.jm.keys.TaskType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.appender.rolling.action.DeleteAction;
import org.apache.logging.log4j.core.appender.rolling.action.Duration;
import org.apache.logging.log4j.core.appender.rolling.action.IfFileName;
import org.apache.logging.log4j.core.appender.rolling.action.IfLastModified;
import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class LogUtil {
    //日志打印目录
    /**日志打印的目录*/
    private static final String datalogDir = "task";

    private static final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
    private static final Configuration config = ctx.getConfiguration();
    private LogUtil(){}

    /**启动一个动态的logger*/
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void start(String loggerName) {

        //创建一个展示的样式：PatternLayout，   还有其他的日志打印样式。
        Layout layout = PatternLayout.newBuilder()
                .withConfiguration(config).withPattern("%msg%n").build();
        //单个日志文件大小
        TimeBasedTriggeringPolicy tbtp = TimeBasedTriggeringPolicy.createPolicy(null, null);
        TriggeringPolicy tp = SizeBasedTriggeringPolicy.createPolicy("10M");
        CompositeTriggeringPolicy policyComposite = CompositeTriggeringPolicy.createPolicy(tbtp, tp);

        String loggerDir = datalogDir + File.separator + loggerName + File.separator + "task.log";
        //删除日志的条件
        IfFileName ifFileName = IfFileName.createNameCondition(null, loggerName + "\\.\\d{4}-\\d{2}-\\d{2}.*");
        IfLastModified ifLastModified = IfLastModified.createAgeCondition(Duration.parse("1d"));
        DeleteAction deleteAction = DeleteAction.createDeleteAction(
                loggerDir, false, 1, false, null,
                new PathCondition[]{ifLastModified,ifFileName}, null, config);
        Action[] actions = new Action[]{deleteAction};

        DefaultRolloverStrategy strategy = DefaultRolloverStrategy.createStrategy(
                "7", "1", null, null, actions, false, config);

        RollingFileAppender appender = RollingFileAppender.newBuilder()
                .withFileName(loggerDir)
                .withFilePattern(loggerDir + ".%d{yyyy}.%i.log")
                .withAppend(false)
                .withStrategy(strategy)
                .withName("task")
                .withPolicy(policyComposite)
                .withLayout(layout)
                .withConfiguration(config)
                .build();
        appender.start();
        config.addAppender(appender);

        AppenderRef ref = AppenderRef.createAppenderRef(loggerName, null, null);
        AppenderRef[] refs = new AppenderRef[]{ref};
        LoggerConfig loggerConfig = LoggerConfig.createLogger(false,
                Level.ALL, loggerName, "true", refs, null, config, null);
        loggerConfig.addAppender(appender, null, null);
        config.addLogger(loggerName, loggerConfig);
        ctx.updateLoggers();
    }

    /**使用完之后记得调用此方法关闭动态创建的logger，避免内存不够用或者文件打开太多*/
    public static void stop(String loggerName) {
        synchronized (config){
            config.getAppender(loggerName).stop();
            config.getLoggerConfig(loggerName).removeAppender(loggerName);
            config.removeLogger(loggerName);
            ctx.updateLoggers();
        }
    }

    /**获取Logger*/
    public static Logger getLogger(String log_type, String loggerName) {
        if(log_type.equals(TaskType.WRITE)){
            String logname = new File("").getAbsolutePath() + "/task/" + loggerName + "/task.log";
            File resultfile = new File(logname);
            if(resultfile.exists()){
                try {
                    FileWriter fileWriter =new FileWriter(resultfile);
                    fileWriter.write("");
                    fileWriter.flush();
                    fileWriter.close();
                    System.out.println("ok");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        synchronized (config) {
            if (!config.getLoggers().containsKey(loggerName)) {
                start(loggerName);
            }
        }
        return LogManager.getLogger(loggerName);
    }

//    public static void main(String[] args) throws IOException, InterruptedException {
//        for(int i = 0; i < 2; i++){
//            String name = "s" + String.valueOf(i);
//            Logger logger = getLogger(name);
//            logger.info("asdfasdf");
//            stop(name);
//        }
//    }

}
