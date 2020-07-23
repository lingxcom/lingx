<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="renderer" content="webkit" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<title>灵犀 - 开源的轻量级模型驱动开发平台</title>
<script type="text/javascript" src="<%=basePath %>js/jquery.js"></script>
<script type="text/javascript" src="<%=basePath %>js/md5-min.js"></script>
<script type="text/javascript" src="<%=basePath %>lingx/js/lingx.js"></script>
<script type="text/javascript">
function login(){
	encodePassword();
	cookieSet();
	$.post("d",{c:"login",userid:$("#userid").val(),password:$("#password").val(),verifycode:$("#code").val()},function(json){
		if(json.code==1){
			window.location.href=json.page;
		}else{
			alert(json.message);
			$("#yzm")[0].src='verifyCodeImage?random='+Math.random();
		}
	},"json");
}

function encodePassword(){
	var password=""+$("#password").val();
	if(password&&password.length!=32){
	var pwd=Lingx.javascriptPasswordEncode($("#password").val(), $("#userid").val());
	$("#password").val(pwd);
	}
}

$(function(){
	var userid=getCookie("userid");
	var password=getCookie("password");
	if(userid||password){
		document.getElementById("checkbox").checked=true;
		$("#userid").val(userid);
		$("#password").val(password);
	}
	$('body').bind('keyup', function(event){
		   if (event.keyCode=="13"){
			   login();
		   }
		});

});
function cookieSet(){
	if(document.getElementById("checkbox").checked){
		setCookie("userid",$("#userid").val(),365);
		setCookie("password",$("#password").val(),365);
	}else{
		setCookie("userid","");
		setCookie("password","");
	}
	
}
function getCookie(c_name)
{
if (document.cookie.length>0)
  {
  c_start=document.cookie.indexOf(c_name + "=");
  if (c_start!=-1)
    { 
    c_start=c_start + c_name.length+1 ;
    c_end=document.cookie.indexOf(";",c_start);
    if (c_end==-1) c_end=document.cookie.length;
    return unescape(document.cookie.substring(c_start,c_end));
    } 
  }
return "";
}

function setCookie(c_name,value,expiredays)
{
var exdate=new Date();
exdate.setDate(exdate.getDate()+expiredays);
document.cookie=c_name+ "=" +escape(value)+
((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
}

</script>
<style type="text/css">
*{
border:0px none;
padding:0px;
margin:0px;
font-family:"Microsoft Yahei","Hiragino Sans GB","Helvetica Neue",Helvetica,tahoma,arial,"WenQuanYi Micro Hei",Verdana,sans-serif,"\5B8B\4F53"
}
.field-label{
padding:0px;
font-size:18px;
color:#777;
text-align:right;
}
.ipt{
border:1px solid #c9d4d6;
height:30px;
width:180px;
font-size:14px;
padding-left:5px;
}

.yj10{
 		border-radius:10px;
        -webkit-border-radius:10px;
        -moz-border-radius:10px;
        }
        
.yj05{
 		border-radius:5px;
        -webkit-border-radius:5px;
        -moz-border-radius:5px;
        }
 ul li{
 color:#fff;
 font-size:16px;
 line-height:36px;
 }
</style>
</head>
<body>
<table width="100%" border="0" cellSpacing="0" cellPadding="0">
<tr>
<td style="" align="center">
<div style="width:1000px;height:80px;line-height:80px;text-align:left;">
<h1>
灵犀 - 开源的轻量级模型驱动开发平台</h1>
</div>

</td>

</tr>
<tr>
<td style="background-color:#01316e;" align="center" valign="middle">
<div style="width:1000px;height:400px;">

<table width="100%" border="0" cellSpacing="0" cellPadding="0">
<tr>
<td>
<ul>
<li>模型驱动：基于数据库表直接生成数据的增删改查，非源代码生成。</li>
<li>控件丰富：数据输入控件丰富，无需二次开发整合，一键配置。</li>
<li>数据查询：重新定义数据查询方式，可动态配置、简化多重关连。</li>
<li>基于配置：用简单的配置可完成系统80%的开发，快速便捷。</li>
<li>权限管理：强大的、可动态配置数据权限及细粒度的功能权限。</li>
<li>级联展示：列表级联、树型联级、最多支持三重级联。</li>
<li>在线开发：WEB在线配置开发、在线预览、在线部署、立即使用。</li>
</ul>
</td>
<td width="400" valign="middle">
<div style="width:300px;height:300px;background-color:#fff;margin:30px;padding:10px;" class="yj10">

<table border="0" cellSpacing="0" cellPadding="0" width="280" height="280" >
<tr>
<td height="58" colspan="2" align="center"> <h2 style="color:#666;">用户登录&nbsp;</h2></td>
</tr>
<tr>
<td height="50"  class="field-label">账&nbsp;号：</td><td align="left"><input id="userid" class="ipt" type="text" name="userid"> </td>
</tr>
<tr>
<td height="50"  class="field-label">密&nbsp;码：</td><td align="left"><input id="password" class="ipt" type="password" name="password"></td>
</tr>
<tr>
<td height="50"  class="field-label">验证码：</td><td align="left"><input id="code" class="ipt" type="text" name="code" style="width:80px;"> <img id="yzm" align="absmiddle" title="看不清，请单击" style="cursor:pointer" src="verifyCodeImage" onclick="javascript:this.src='verifyCodeImage?random='+Math.random()"> </td>
</tr>
<tr>
<td height="30" colspan="2" align="right"> <input id="checkbox" type="checkbox" /><label for="checkbox" style="font-size:14px;color:#999"> 记住账号和密码</label>

 </td>
</tr>
<tr>
<td height="50" colspan="2" align="center" valign="middle">
<button onclick="login();" style="width:260px;height:40px;line-height:40px;background-color:#01316e;color:#fff;font-size:20px;cursor:pointer;" class="yj05">登  录</button>
 </td>
</tr>
<tr>
<td height="120" colspan="2" align="center" valign="middle" style="line-height:24px;">
<br/>
</td>
</tr>
<tr>
<td>&nbsp;</td>
</tr>
</table>
</div>
</td>
</tr>
</table>

</div>
</td>
</tr>
<tr>
<td style="" align="center">
<div style="width:1000px;height:100px;font-size:12px;color:#aaa"><br/>
为了得到最佳的体验效果，建议使用谷歌浏览器(chrome)。<br/><br/>

<br/>
© 2012-2020 lingx.com All rights reserved. 
</div>
</td>
</tr>


</table>
<!-- FORM -->
<!-- END FORM -->
</body>
</html> 