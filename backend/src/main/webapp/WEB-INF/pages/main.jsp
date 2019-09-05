<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/common/head.jsp"%>

<div id="content" class="span10">

	<div class="row-fluid">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2>
					<i class="icon-th"></i> 系统功能说明
				</h2>
			</div>
			<div class="box-content">
				<br>IOT_DormitoryManagement系统包括硬件端，服务器端，Web端，安卓端四大模块<br>
				<br>1. 硬件端：包括出入刷卡模块与温湿度信息采集模块；<br>
				<br>2. 服务端：采用SSM框架，负责与硬件端、安卓端以及Web端的交互；<br>
				<br>3. Web端：采用Boostrap工具包渲染界面，方便系统管理员对数据库各表进行增、删、查；<br>
				<br>4. 安卓端：与宿舍职工交互，方便其查看每日学生出入以及宿舍环境情况；<br>
			</div>
		</div>
	</div>
</div>
<%@include file="/WEB-INF/pages/common/foot.jsp"%>
