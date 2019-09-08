package com.lingx.support.web.filter;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import com.lingx.core.service.impl.ContextServiceImpl;

/** 
 * @author www.lingx.com
 * @version 创建时间：2018年3月5日 下午3:00:42 
 * 类说明 
 */
public class Firewall {

	static AtomicInteger count=new AtomicInteger();
	static boolean b=true;
	
	public static boolean fire(HttpServletRequest request){
		System.out.println(ContextServiceImpl.getRequestClientIP(request)+"->"+request.getServletPath());
		count.addAndGet(1);
		if(b){
			b=false;
			PrintThread pt=new PrintThread();
			Thread t=new Thread(pt);
			t.start();
		}
		return true;
	}
}
class PrintThread implements Runnable{
	boolean b=true;
	@Override
	public void run() {
		while(b){
			System.out.println("==========Concurrent:"+Firewall.count.get()+"============");
			Firewall.count.set(0);
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	}