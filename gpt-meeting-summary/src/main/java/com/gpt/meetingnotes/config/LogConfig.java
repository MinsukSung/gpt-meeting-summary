package com.gpt.meetingnotes.config;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class LogConfig {
	
	@Value("${log.level}")
	private String level;
	
	@Value("${log.file}")
	private String file;
	
	@Value("${log.pattern}")
	private String pattern;
	
	@PostConstruct
	public void init() {
		log.debug("LEVEL >>>>>>>>>>>>>>>>>>>>>>>>>> {}", level);
		System.setProperty("log.level", level);
		System.setProperty("log.file", file);
		System.setProperty("log.pattern", pattern);
		
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		context.reconfigure();
		
		Configurator.setRootLevel(Level.toLevel(level.toUpperCase()));
	}
}
