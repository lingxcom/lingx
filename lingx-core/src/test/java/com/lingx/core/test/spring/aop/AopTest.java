package com.lingx.core.test.spring.aop;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.lingx.core.test.spring.JUnit4ClassRunner;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月11日 下午11:30:27 
 * 类说明 
 */

@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:aop-test2.xml"})
public class AopTest {
	@Resource//(name = "lingxProxy")  
	private IPople pople;
	@Test
	public void test(){
		this.pople.say().in();
	}
	//@Autowired(name="")
	public void setPople(IPople pople) {
		this.pople = pople;
	}
}
