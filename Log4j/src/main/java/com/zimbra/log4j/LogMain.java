package com.zimbra.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogMain {
	
	static Logger logger = LogManager.getLogger(LogMain.class);
	
	public static void main(final String[] args)
	  {
	      //All>Trace>debug>info>warn>error>fatal
		  logger.trace("This is trance log msg");
	      logger.debug("Debug Message Logged !!!");
	      logger.info("Info Message Logged !!!");
	      logger.error("Error Message Logged !!!", new NullPointerException("NullError"));
	  }

}
