package com.zimbra.log4j;

import java.util.Map;
import java.util.zip.Deflater;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class LogMain {

    private static final Logger logger = LogManager.getLogger(LogMain.class);

    public static void main(String[] args) {

        LoggerContext lc = (LoggerContext) LogManager.getContext(false);
        final Configuration config = lc.getConfiguration();

        Map<String, Appender> appenders2 = lc.getConfiguration().getAppenders();
        for (Map.Entry<String, Appender> entry : appenders2.entrySet()) {
            if (entry.getValue() instanceof RollingFileAppender) {
                entry.getValue().getLayout().getContentFormat().replace("format",
                        "%highlight{%d [%t] %-5level: %msg%n%throwable}{FATAL=white, ERROR=red, WARN=blue, INFO=black, DEBUG=green, TRACE=blue}");

            }

        }

        lc.updateLoggers();
        Map<String, Appender> appenders3 = lc.getConfiguration().getAppenders();
        
        

        logger.debug("Hello from Log4j 2");
        logger.debug("This is a Debug Message!");
        logger.info("This is an Info Message!");
        try {
            System.out.println(100 / 0);
        } catch (Exception e) {
            logger.error("Error Occured", e);
        }
        // logger.error("And here comes the Error Message!", new RuntimeException("Run
        // Run Run"));

    }

    public static void initializeYourLogger(String fileName, String pattern) {

        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        builder.setStatusLevel(Level.DEBUG);
        builder.setConfigurationName("DefaultLogger");

        // create a console appender
        AppenderComponentBuilder appenderBuilder = builder.newAppender("Console", "CONSOLE").addAttribute("target",
                ConsoleAppender.Target.SYSTEM_OUT);
        appenderBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", pattern));
        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.DEBUG);
        rootLogger.add(builder.newAppenderRef("Console"));

        builder.add(appenderBuilder);

        // create a rolling file appender
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout").addAttribute("pattern", pattern);
        ComponentBuilder<?> triggeringPolicy = builder.newComponent("Policies")
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "1KB"));
        appenderBuilder = builder.newAppender("LogToRollingFile", "RollingFile").addAttribute("fileName", fileName)
                .addAttribute("filePattern", fileName + "-%d{MM-dd-yy-HH-mm-ss}.log.").add(layoutBuilder)
                .addComponent(triggeringPolicy);
        builder.add(appenderBuilder);
        rootLogger.add(builder.newAppenderRef("LogToRollingFile"));
        builder.add(rootLogger);
        Configurator.reconfigure(builder.build());

        // Logger applicationLogger = LogManager.getLogger(LogMain.class);
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();

        // LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
        // .addAttribute("pattern", logLayoutPattern);
        Map<String, Appender> appenders = config.getAppenders();

        LoggerContext lc = (LoggerContext) LogManager.getContext(false);
        lc.getConfiguration().getAppenders();
        lc.updateLoggers();

        /*
         * if (applicationAppender instanceof RollingFileAppender) {
         * 
         * // Configure max properties of rolling file appender here if (maxLogFiles !=
         * null) {
         * 
         * LoggerContext context = (LoggerContext) LogManager.getContext(false);
         * Configuration config = context.getConfiguration(); // Copied code from lib
         * DefaultRolloverStrategy newStrategy =
         * DefaultRolloverStrategy.createStrategy(String.valueOf(maxLogFiles), null,
         * null, String.valueOf(Deflater.DEFAULT_COMPRESSION), null, true, config);
         * 
         * // How to modify the existing appender?
         * 
         * }
         * 
         * }
         */

    }

}
