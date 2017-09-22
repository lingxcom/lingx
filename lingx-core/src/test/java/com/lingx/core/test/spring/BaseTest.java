package com.lingx.core.test.spring;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月10日 下午1:58:25 
 * 类说明 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:lingx-core.xml","classpath*:lingx-database.xml"})
public class BaseTest {

}
