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
	if(session.getAttribute("SESSION_LANGUAGE")!=null){
		lanuage=session.getAttribute("SESSION_LANGUAGE").toString();
	}
	II18NService i18n=spring.getBean(II18NService.class);
	List<Map<String,Object>> list=i18n.getLanuages();
	String select="<select style=\"border:0px;none;\" onchange=\"setLanuage(this.value)\">";
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
<link rel="stylesheet" type="text/css" href="lingx/js/resources/css/style.css">
<script type="text/javascript" src="lingx/js/rootApi.js"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/avalon.js"></script>
<style type="text/css">
.view-link {

    float: left;
    width: 68px;
    height: 33px;
    font-size: 14px;
    font-family: 宋体;
    white-space: nowrap;
    background-image: url("lingx/images/link.gif");
    display: block;
    cursor: default;
    padding: 9px 0px 0px;
    background-position: -2px -33px;
}
.x-border-panel{
position: absolute;
}
.view-link-bgtext {
    color: #005F8D;
    margin: 0 0 0 18px;
    position: absolute;
}


.menu-over{
background-position:0px -66px;
}

.menu-active{
background-position:0px 0px;
}




.ui-tab-bar{  
    padding-top:1px;  
}  
.ui-tab-bar .x-tab-bar{  
    background:rgba(0,0,0,0) !important;  
    border:0 !important;  
}  
  
.ui-tab-bar .x-tab-bar-strip {  
    top: 25px !important; /* Default value is 20*/  
}  
  
.ui-tab-bar .x-tab-bar .x-tab-bar-body .x-box-inner .x-tab {  
    height: 25px !important; /* Default value is 21*/ 
	padding-top: 5px !important;
}  

.x-tab-wrap .x-tab-button .x-tab-inner {
	font-weight:100 !important;
}
.x-tab-active{
	border:1px solid #99bce8 !important;
	border-bottom: 1px solid #deecfd !important;
	background-image: -webkit-linear-gradient(top,#fff,#f5f9fe 25%,#deecfd 45%) !important;
}
.x-tab-active .x-tab-wrap .x-tab-button .x-tab-inner {

/*     border-top:2px solid #00CCFF;  */
    margin: -3px -2px -2px -2px; 
    padding: 3px 5px 4px 5px !important;
    font-weight:100 !important;
}
.ui-tab-bar .x-tab-bar .x-tab-bar-body .x-box-inner .x-tab {  
color:black !important;
border:1px solid rgba(1,1,1,0);/* 
border-left:1px solid #fff;
border-right:1px solid rgb(153,193,232); */
margin-left:0px;
margin-right:0px;

padding:2px;
background: rgba(1,1,1,0);
box-shadow:none;
-webkit-box-shadow:none;
} 

.btn1{
background-position: 0px 0px;
}
.btn1:HOVER{
 background: rgba(255,255,255,0.2);
}
</style>
<script type="text/javascript">
setInterval("keepSession()", 5*60*1000); //隔5分钟访问一次
var logo='${SESSION_USER.app.logo}';
var logoIMG='';
var cacheMenu=[];
var model=avalon.define({
	$id:"body",
	menus:[],
	over:function(el){
		$(el).addClass("menu-over");
	},
	out:function(el){
		$(el).removeClass("menu-over");
	},
	showMenu:function(id,el){
		$(".view-link").removeClass("menu-active");
		$(el).addClass("menu-active");
		//console.log(cacheMenu);
		Ext.getCmp("tabpanel").removeAll();
		for(var i=0;i<cacheMenu.length;i++){
			if(id==cacheMenu[i].text){
				//alert(cacheMenu[i].menu.length);
				for(var j=0;j<cacheMenu[i].menu.length;j++){
					var obj=cacheMenu[i].menu[j];
					if(obj=="-")continue;
					if(obj.uri!="e?e=public&m=public")
					addTab(obj.itemId,obj.text,obj.uri);
					if(obj.menu){
						for(var k=0;k<obj.menu.length;k++){
							var obj1=obj.menu[k];
							if(obj1.uri!="e?e=public&m=public")
							addTab(obj1.itemId,obj1.text,obj1.uri);
						}
					}

				}
			}
		}
		Ext.getCmp("tabpanel").setActiveTab(0);
	}
});
if(logo){
	logoIMG='<img src="'+logo+'" height="40" style="margin-top:10px;"/>';
}
function keepSession(){
	Lingx.post("d",{c:"getNewMessageCount"},function(json){
		$("#ext-gen81").html(json.count);
	});
}
function setLanuage(lanuage){
	Lingx.post("lingx/common/handler.jsp",{c:"lanuage",lanuage:lanuage},function(json){
		window.location.href="d?c=i";
	});
}
Ext.onReady(function(){
	Lingx.post("d",{c:"menu"},function(json){
		Lingx.post("d",{c:"menu"},function(json22){
			json22.unshift({name:"首页",short_name:"首页",selected:true,menu:[{itemId:"indexPage",text:"默认",uri:"${SESSION_USER.app.indexPage}"}]});
			cacheMenu=json22;
			$("#aaa首页").addClass("menu-active");;
		});
		
		json.unshift({name:"首页",short_name:"首页",selected:true,menu:[{itemId:"indexPage",text:"默认",uri:"${SESSION_USER.app.indexPage}"}]});
		//console.log(json);
		model.menus=json;
		
    Ext.create('Ext.Viewport', {
    	id:'viewport',
    	layout: 'border',
    	items:[{
    			region:'north',
               // margins: '0 0 5 0',
    			//height:100,
    			height:63,
    			border:false,
    			layout: 'border',
    			contentEl:"headerNew"
    		},
    	       {
    			region:'center',
    			margins: '0 0 0 0',
    			id: 'tabpanel',
    			xtype: 'tabpanel',
    			region: 'center', // a center region is ALWAYS required for border layout
    			tabPosition:'top',
    			cls:"ui-tab-bar",
                bodyCls:"ui-tab-body",
                collapsedCls:"tab_title_none",
//                 style:"color:red",
               // bodyStyle: 'background:red; padding:10px;',
               // deferredRender: false,
                activeTab: 0,     // first tab initially active
                items: [{
                	id:"<%=i18n.text("默认")%>",
                	html: '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="${SESSION_USER.app.indexPage}"> </iframe>',
                    title: '&nbsp;&nbsp;&nbsp;&nbsp;<%=i18n.text("默认")%>&nbsp;&nbsp;&nbsp;&nbsp;',
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
            title: '&nbsp;&nbsp;'+title+'&nbsp;&nbsp;',

            closable: false,
            autoScroll: true
		});
	}
}

function openMessageWindow(){
	openReadonlyWindow("消息中心","e?e=tlingx_message&m=grid");
}
</script>
</head>
<body ms-controller="body">
	<div id="headerNew"
		style="height: 86px; background: url(lingx/images/head-bg.gif) repeat-x; color: #fff;">
		<div id="index-logo-top-left" class="x-border-panel"
			style="width: 172px; left: 0px; top: 0px; height: 63px; background-image: url(${SESSION_USER.app.logo});"></div>

		<div id="ext-comp-1011" class=" x-border-panel"
			style="position: absolute; left: 172px; top: 0px; width:100%; height: 63px;">
			<div id="ext-comp-1012" class=" x-border-panel"
				style="height: 30px; left: 0px; top: 0px; width: 100%;">
				<div id="ext-comp-1013" class=" x-border-panel"
					style="padding: 2px 0px 0px; left: 0px; top: 0px; width: 60%; height: 24px;">
					<%-- <span id="ext-comp-1014" class=" view-info">您好</span> <span
						id="loginInfo-1" class=" view-info"
						style="color: rgb(249, 246, 6);">${SESSION_USER.name}[${SESSION_USER.account}]</span> --%>
				</div>
				
			</div>
			<div id="ext-comp-1016" class=" x-border-panel"
				style="height: 33px; left: 0px; top: 30px; width: 100%;">
				<div id="view-tabs" class=" x-border-panel"
					style="left: 0px; top: 0px; width:100%; height: 33px;">
					
					
					<div ms-attr-id="'aaa'+el.short_name" ms-repeat="menus" class="view-link" ms-mouseover="over(this)" ms-mouseout="out(this)" ms-click="showMenu(el.text,this)"
						>
						<span class="view-link-bgtext" id="ext-gen98"
							style="color: rgb(0, 95, 141);">{{el.short_name}}</span><span
							class="view-link-text" id="ext-gen99">{{el.short_name}}</span>
					</div>
				</div>

				<div id="ext-comp-1017" class=" x-border-panel"
					style="position: fixed;padding: 6px 8px 0px 0px; width: 92px; right: 10px; top: 0px; height: 27px;">
					<a onclick="openMessageWindow()" href="javascript:" id="view-messenger"
						class="btn1 ux-form-button-bottom"
						style="float: right; background-position: 0px 0px; cursor:pointer;"><div
							class="ux-form-button-bottom-left" id="ext-gen78"
							style="padding-left: 5px; background-position: 0px 0px;">
							<div class="ux-form-button-bottom-right" id="ext-gen79"
								style="padding-top: 0px; padding-right: 5px; background-position: right 0px;">
								<div class="icon-messenger" id="ext-gen80">
									<div class="ux-form-button-bottom-label" id="ext-gen81">${msg_count }</div>
								</div>
							</div>
						</div></a>
				</div>


				<div id="ext-comp-1015" class=" x-border-panel "
					style="position: fixed;padding: 0px 0px 0px 0px; width: 292px; right:10px; top: 32px; height: 24px;">
					<a href="${logoutUrl}" id="view-exit" class="btn1"
						
						style="float: right; background-position: 0px 0px;">
						<div class="ux-form-button-top-left" id="ext-gen63"
							style="padding-left: 5px; background-position: 0px 0px;">
							<div class="ux-form-button-top-right " id="ext-gen64"
								style="padding-top: 0px; padding-right: 5px; background-position: right 0px;">
								<div class="icon-exit" id="ext-gen65"  >
									<div class="ux-form-button-top-label" id="ext-gen66"  style="line-height:15px;">注销</div>
								</div>
							</div>
						</div>
					</a> 
					<a href="javascript:"  id="view-menu"
						class=" ux-form-button-top btn1"  onclick="javascript:openWindow('修改用户信息','e?e=tlingx_user&m=editPassword&id=${SESSION_USER.id}');"
						style="float: right; background-position: 0px 0px;cursor: auto;">
						<div class="ux-form-button-top-left " id="ext-gen67"
							style="padding-left: 5px; background-position: 0px 0px;">
							<div class="ux-form-button-top-right " id="ext-gen68"
								style="padding-top: 0px; padding-right: 5px; background-position: right 0px;">
								<div class="icon-menu " id="ext-gen69">
									<div class="ux-form-button-top-label" id="ext-gen70" style="line-height:15px;">修改密码</div>
								</div>
							</div>
						</div></a>
						<a href="javascript:"  id="view-record"  onclick="javascript:openWindow('修改用户信息','e?e=tlingx_user&m=editSelf&id=${SESSION_USER.id}');"
						class=" ux-form-button-top btn1" style="float: right;cursor: auto;"><div
							class="ux-form-button-top-left" id="ext-gen71">
							<div class="ux-form-button-top-right" id="ext-gen72">
								<div  id="ext-gen73">
									<div class="ux-form-button-top-label" id="ext-gen74" style="line-height:15px;" >${SESSION_USER.name}[${SESSION_USER.account}]</div>
								</div>
							</div>
						</div></a>
				</div>
			</div>
		</div>
	</div>
</body>
</html> 