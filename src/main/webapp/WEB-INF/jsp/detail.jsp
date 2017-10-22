<%@page import="com.fasterxml.jackson.annotation.JsonInclude.Include"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
   <head>
      <title>秒杀详情页</title>
		<%@include file="common/head.jsp" %>
		 <%@include file="common/tag.jsp" %>
   </head>
   <body>
 		<div class="container">
 			<div class="panel panel-default text-center">
 				<div class="pannel-heading">
 					<h1>${seckill.name }</h1>
 				</div>
 			</div>
 			<div class="panel-body">
 				<h2 class="text-center text-danger">
 					<!-- 显示time图标 -->
 					<span class="glyphicon glyphicon-time"></span>
 					<!-- 展示倒计时 -->
 					<span class="glyphicon" id="seckill-box"></span>
 				</h2>
 			</div>
 		</div>
 		<!-- 登陆弹出层输入电话 -->
 		<div id="killPhoneModal" class="mdal fade">
 			<div class="modal-dialog">
 				<div class="modal-content">
 					<div class="modal-header">
 						<h3 class="model-title text-center">
 							<span class="glyphicon glyphicon-phone"></span>秒杀电话：
 						</h3>
 					</div>
 					<div class="modal-body">
 						<div class="row">
 							<div class="col-xs-8 col-xs-offset-2">
 								<input type="text" id="killPhoneKey" name="kilPhone" class="form-control" placeholder="请填手机号"	>
 							</div>
 						</div>
 					</div>
 					<div class="model-fotter text-right">
	 					<!-- 验证信息 -->
 						<span id="killPhoneMessage" class="glyphicon"></span>
 						<button type="button" id="killPhoneBtn" class="btn btn-success">
 							<span class="glyphicon glyphicon-phone"></span>
 							Submit
 						</button>
 					</div>
 				</div>
 			</div>
 		
 		</div>
 		
   </body>
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	
	<!-- 使用CDN 获取公共js  	http://www.bootcdn.cn/ -->
	<!-- jquery cookie 插件 -->
	<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
	<!-- jquery countdown 使用倒计时插件 -->
	<script src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.min.js" ></script>

	<script src="/seckill/resources/script/seckill.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(function(){
		//使用EL表达式传入参数
		seckill.detail.init({
			seckillId : "${seckill.seckillId}",
			startTime : "${seckill.startTime.time}",
			endTime : "${seckill.endTime.time}"
		});
		
	});
	
	</script>
	
</html>