package com.lingx.core.test.spring;

import java.io.FileNotFoundException;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

/**
 * @author www.lingx.com
 * @version 创建时间：2015年4月9日 下午9:59:36 类说明
 */
public class JUnit4ClassRunner extends SpringJUnit4ClassRunner {
	static {
		try {
			Log4jConfigurer.initLogging("classpath:log4j.properties");
		} catch (FileNotFoundException ex) {
			System.err.println("Cannot Initialize log4j");
		}
	}

	public JUnit4ClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}
}