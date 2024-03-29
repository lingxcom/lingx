# 灵犀模驱 - 开源的低代码模型驱动开发框架

![image](http://www.lingx.com/logo.png)
 
官方网站：https://www.lingx.com

技术文档：http://docs.lingx.com

视频教程：https://space.bilibili.com/482658194

成品示例：https://gps.lingx.com 

账号:admin,密码:123456

## 框架目标
简化信息管理软件开发；抛开编码细节、专注业务逻辑。

## 框架简介
灵犀的核心是模型驱动开发，有别于一般的代码生成系统。我们不生成代码、没有MVC、没有分层、没有前后端分离，非专业人员便可以做出一般信息管理系统。可以用于所有的Web应用程序，如网站管理后台，网站会员中心，CMS，CRM，OA。

## 主要功能
> * 在线开发：Web版的开发工具，可以进行模型的创建与编辑
> * 在线预览：模型创建与编辑后，可立即预览；方便查看效果
> * 功能权限：细粒度的功能管理，可管控用户管理的添加按钮是否给某人使用
> * 数据权限：横向/纵向数据权限动态配置，用于不同人查看不同数据
> * 远程提交：假如系统已经部署到服务器，可以通过此功能一键提交更新
> * 远程更新：类似于window的系统更新，该系统不断维护，不定时发布新功能
> * 导出模型：可以将开发好的模型、字典导出为ZIP压缩包
> * 导入模型：可以将以上导出的ZIP压缩包进行导入
> * 字典管理：研发过程中对数据字典的统一管理
> * 权限管理：常规的用户、角色、功能、菜单管理
> * 定时任务：可以指定某个时间或定时执行某项功能
> * 操作日志：常规的操作日志
> * 登录日志：常规的登录日志


## 快速入门
### 一. 下载免安装一键启动版

右击以下链接，选择“另存为”下载

http://mdd.lingx.com/20210115/lingx-mdd.zip

下载后解压到路径中  **没有中文且没有空格**的文件夹下

1、双击“启动灵犀.bat”

2、等到Tomcat的窗口不再增加消息时为启动成功

3、打开谷歌浏览器chrome或者360浏览器，输入网址：http://127.0.0.1:8080

4、账号：admin，密码：123456

注意：《免安装一键启动版》只支持64位Windows系统；linux与mac系统需要下载war包部署到tomcat中，数据库需要用mysql5.7，JDK需要1.8。