<%@ page import="java.net.*,java.io.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	JdbcTemplate jdbc=spring.getBean(JdbcTemplate.class);
	UserBean userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);
	String logoutUrl="d?c=logout";
	try{
		ISsoCasService ssocas=spring.getBean(ISsoCasService.class);
		logoutUrl=ssocas.getLogoutUrl();
	}catch(Exception e){}
	request.setAttribute("logoutUrl", logoutUrl);
	
	

	String lanuage="";
	if(session.getAttribute("LINGX_LANUAGE")!=null){
		//lanuage=session.getAttribute("LINGX_LANUAGE").toString();
	}
	II18NService i18n=spring.getBean(II18NService.class);
	List<Map<String,Object>> list=i18n.getLanuages();
	String select="<select style=\"border:0px;none;\" onchange=\"setLanuage(this.value)\">";
	select+="<option  value=\"\">语言/lanuage</option>";
	for(Map<String,Object> map:list){
		select+="<option "+(lanuage.equals(map.get("lanuage").toString())?"selected":"")+" value=\""+map.get("lanuage")+"\">"+map.get("name")+"</option>";
	}
	select+="</select>";
	if(list.size()<=1)select="";
	request.setAttribute("select", select);
	

	int msg_count=jdbc.queryForInt("select count(*) from tlingx_message where to_user_id=? and status=1",userBean.getId());
	request.setAttribute("msg_count",msg_count);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>${SESSION_USER.app.name }</title>
<%@ include file="/lingx/include/include_JavaScriptAndCss.jsp"%> 
<script type="text/javascript" src="lingx/js/rootApi.js"></script>
<script type="text/javascript">
setInterval("keepSession()", 5*60*1000); //隔5分钟访问一次
var logo='${SESSION_USER.app.logo}';
var logoIMG='';
if(logo){
	logoIMG='<img src="'+logo+'" height="40" style="margin-top:10px;"/>';
}
function keepSession(){
	Lingx.post("d",{},function(json){
	});
}
function setLanuage(lanuage){
	Lingx.post("lingx/common/handler.jsp",{c:"lanuage",lanuage:lanuage},function(json){
		window.location.href="d?c=i";
	});
}
Ext.onReady(function(){
	Lingx.post("d",{c:"menu"},function(json){
    Ext.create('Ext.Viewport', {
    	id:'viewport',
    	layout: 'border',
    	items:[{
    			region:'north',
               // margins: '0 0 5 0',
    			//height:100,
    			height:54,
    			border:false,
    			layout: 'border',
    			items:[{
        			border:false,
    				region:'center',
    				bodyStyle: 'background:url(lingx/images/top.jpg) no-repeat left top #515cfc;',//89b9e1 images/obd/top.jpg 347cc6
    				html:'<div style="position:absolute; left:0; bottom:0;color:#fff;font-size:14px;line-height:24px;height:24px;vertical-align:top;font:"Microsoft Yahei","Hiragino Sans GB","Helvetica Neue",Helvetica,tahoma,arial,"WenQuanYi Micro Hei",Verdana,sans-serif,"\5B8B\4F53"><div >&nbsp; ${SESSION_USER.app.name}</div></div>'// 
    				
    				+'<div style="position:absolute; right:0; bottom:0;color:#fff;font-size:12px;line-height:24px;height:24px;">'
    				+'${select}&nbsp;&nbsp;'
    				+'<a style="color:#fff;font-size:12px;" href="javascript:;" onclick="openReadonlyWindow(\'消息中心\',\'e?e=tlingx_message&m=grid\');">消息(${msg_count})</a>&nbsp;|&nbsp;'
    				
    				+'<%=i18n.text("当前用户")%>：<a style="color:#fff;font-size:12px;" href="javascript:;" onclick="javascript:openWindow(\'<%=i18n.text("修改用户信息")%>\',\'e?e=tlingx_user&m=editSelf&id=${SESSION_USER.id}\');";>${SESSION_USER.name}</a>[${SESSION_USER.account}] '
    				+'&nbsp;|&nbsp;'
    				+'<a style="color:#fff;font-size:12px;" href="javascript:;" onclick="javascript:openWindow(\'<%=i18n.text("修改密码")%>\',\'e?e=tlingx_user&m=editPassword\');";><%=i18n.text("修改密码")%></a>'
    				+'&nbsp;|&nbsp;'
    				+'<a style="color:#fff;font-size:12px;" href="${logoutUrl}"><%=i18n.text("退出系统")%></a>&nbsp;</div>'
    				+'</div>'
    			},{
    				height:26,
        			border:false,
    				region:"south",
    				items:[{
                    	xtype: 'toolbar',
    					border:false,
    					items: json
    				}]
    			}]
    		},
    	       {
    			region:'center',
    			margins: '0 0 0 0',
    			id: 'tabpanel',
    			xtype: 'tabpanel',
    			region: 'center', // a center region is ALWAYS required for border layout
    			tabPosition:'bottom',
               // deferredRender: false,
                activeTab: 0,     // first tab initially active
                items: [{
                	id:"<%=i18n.text("首页")%>",
                	html: '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="${SESSION_USER.app.indexPage}"> </iframe>',
                    title: '<%=i18n.text("首页")%>',
                    autoScroll: true
                }]
    		}]
       
    }).show();

	});
});

function addTab(id,title,url){
	var tab=Ext.getCmp("tabpanel").queryById(id);
	if(tab){
		tab.show();
	}else{
		Ext.getCmp("tabpanel").add({
			id:id,
			html: '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+url+'"> </iframe>',
            title: title,

            closable: true,
            autoScroll: true
		}).show();
	}
}
</script>
</head>
<body>
	
</body>
</html> 