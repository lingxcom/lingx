# 灵犀 - 开源的轻量级模型驱动开发平台

技术文档：http://docs.lingx.com

视频教程：https://space.bilibili.com/482658194

联系QQ：283853318

技术支持：泉州市领新信息科技有限公司

示例地址：http://www.longyaniot.com
账号:admin,密码:123456

## 快速入门
### 一. 安装环境
1. JDK 1.8

2. Tomcat 8.5

3. MySQL 5.7

4. Eclipse JEE版

5. lingx-web.war

### 二. 创建项目
1. 创建Web项目，选择Sevlet 2.5版

2. 解压lingx-web.war,并复制到项目根目录下。如：webapp

3. 将resoures文件移到项目的配置目录下

4. 创建数据库并导入初始化脚本lingx.sql(该脚本在lingx-web-1.0.0\WEB-INF\classes\db)

5. 修改配置目录下的db.propties文件

6. 配置Web容器并启动；如：Tomcat

### 三. 进入后台

1. 打开Web浏览器，这里建议用chrome。因为javascript代码比较多

2. 在浏览器中输入本机地址+端口+项目名称

3. 进入登录界面，账号:lingx，密码：123456

### 四. 在线开发
1. 进入菜单：开发 -> 开发工具

2. 点击左上角的“创建”按钮

3. 在对话框中选择数据库表“ttest”，点击“确定”，输入对象名称与隶属应用，再点“确定”

4. 点击工具栏的“刷新”，在左侧选中“ttest”

5. 点击工具栏的“写入功能树”，打勾“添加”、“修改”、“删除”、“查看”、“列表查看”

6. 点击工具栏的“对象预览”