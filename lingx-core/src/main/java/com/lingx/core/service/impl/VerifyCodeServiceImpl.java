package com.lingx.core.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.lingx.core.Constants;
import com.lingx.core.engine.IContext;
import com.lingx.core.service.ILingxService;
import com.lingx.core.service.IVerifyCodeService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月11日 下午10:31:10 
 * 类说明 
 */
@Component(value="lingxVerifyCodeService")
public class VerifyCodeServiceImpl implements IVerifyCodeService {
	public static Logger logger = LogManager.getLogger(VerifyCodeServiceImpl.class);
	public VerifyCodeServiceImpl(){
	}
	@Resource
	private ILingxService lingxService;
	public static final Random random = new Random();
	public static final Font[] dFont = new Font[] {
			new Font("nyala", Font.BOLD, 24),
			new Font("nyala", Font.PLAIN, 24),
			new Font("Bell MT", Font.BOLD, 24),
			new Font("Bell MT", Font.PLAIN, 24),
			new Font("Credit valley", Font.BOLD, 24),
			new Font("Credit valley", Font.PLAIN, 24)  };


	public Color getRandomColor() {
		return new Color(random.nextInt(255), random.nextInt(255),
				random.nextInt(255));
	}

	public Font getRandomFont() {
		return dFont[random.nextInt(dFont.length)];
	}
	
	@Override
	public BufferedImage getVerifyCodeImage(String code,int width,int height) {
		BufferedImage image = new BufferedImage(100, 32,
				BufferedImage.TYPE_INT_BGR);
		// 获取图片处理对象
		Graphics graphics = image.getGraphics();
		// 填充背景色
		graphics.setColor(new Color(255, 255, 255));
		graphics.fillRect(0, 0, width, height);
		// 设置干扰线
		graphics.setColor(getRandomColor());
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int x1 = random.nextInt(width - 1);
			int y1 = random.nextInt(height - 1);
			graphics.drawLine(x, y, x1, y1);
		}
		// 写入文字
		graphics.setColor(getRandomColor());
		graphics.setFont(getRandomFont());
		graphics.drawString(code, 16, 24);

		// 释放资源
		graphics.dispose();

		return image;
	}

	@Override
	public boolean verify(IContext context) {
		if("true".equals(this.lingxService.getConfigValue("lingx.login.verifycode","false"))){
			try {
				String reqYzm=context.getRequest().getParameter("verifycode");
				String sesYzm=context.getSession().get(Constants.SESSION_YZM).toString();
				return sesYzm.equals(reqYzm);
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	public void setLingxService(ILingxService lingxService) {
		this.lingxService = lingxService;
	}


}
