package com.lingx.core;
/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午1:36:26 
 * 类说明 
 */
public interface Page {
	public static final String PAGE_TREE_CASCADE="page_tree_cascade";
	public static final String PAGE_GRID_CASCADE="page_grid_cascade";
	public static final String PAGE_EXCEPTION="page_exception";
	public static final String PAGE_NO_PERMISSION="page_no_permission";
	public static final String PAGE_TIMEOUT="page_timeout";
	public static final String PAGE_JSON="page_json";
	/**
	 * 不输出任何字符，因为response需要流输出
	 */
	public static final String PAGE_NORET="page_noret";
	public static final String PAGE_URL="page_url";
	public static final String PAGE_MAIN="page_main";
	public static final String PAGE_LOGIN="page_login";

	public static final String PAGE_LOGIN_MOBILE="page_login_mobile";
	
	public static final String PAGE_PASSWORD="page_password";
	
	//以下是工作流界面
	public static final String PAGE_WF_FORM="page_wf_form";
	//手机界面
	public static final String PAGE_WF_FORM2="page_wf_form2";
	public static final String PAGE_WF_FORM2_APPROVAL="page_wf_form2_approval";
	public static final String PAGE_WF_SUCCESS="page_wf_success";
	
	public static final String PAGE_WF_ERROR="page_wf_error";
}
