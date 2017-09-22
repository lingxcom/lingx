package com.lingx.support.web.action;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.lingx.core.Constants;
import com.lingx.core.action.IAction;
import com.lingx.core.action.IResponseAware;
import com.lingx.core.engine.IContext;
import com.lingx.core.service.IVerifyCodeService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月11日 下午9:51:26 
 * 类说明 
 */
public class VerifyCodeAction implements IAction,IResponseAware {

	
	private HttpServletResponse response;
	@Resource
	private IVerifyCodeService verifyCodeService;
	@Override
	public String action(IContext context) {
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		int width = 100, height = 32;
		ServletOutputStream out=null;
		try {
			out= response.getOutputStream();

			String content = new String(Utils.getRandomNumber(4));
			context.getSession().put(Constants.SESSION_YZM, content);
			BufferedImage image=verifyCodeService.getVerifyCodeImage(content, width, height);
			ImageIO.write(image, "jpg", out);
			out.flush();
		} catch (Exception e) {
		} finally {
			try {
				if(out!=null)
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void setResponse(HttpServletResponse response) {
		this.response=response;
	}

	public void setVerifyCodeService(IVerifyCodeService verifyCodeService) {
		this.verifyCodeService = verifyCodeService;
	}
}
