package com.lingx.core.test.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lingx.core.engine.IProcessEngine;
import com.lingx.core.engine.impl.ProcessEngineImpl;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月9日 下午9:20:53 
 * 类说明 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:lingx-core.xml"})
public class SpringTest {
      
	private IProcessEngine processEngine;
	@Test
	public void test() throws Exception{//
		
		ProcessEngineImpl pe=(ProcessEngineImpl)processEngine;
		System.out.println(pe.process(null));
	}
	@Autowired 
	public void setProcessEngine(IProcessEngine processEngine) {
		this.processEngine = processEngine;
	}

}
