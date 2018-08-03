<!DOCTYPE html>
<html class="ui-page-login">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>用户管理</title>
		<link href="/css/mui-min.css" rel="stylesheet" />
		<link href="/css/project.css" rel="stylesheet" />
		<style type="text/css">
			.mui-input-group{
				margin-top: 10px;
			}
		</style>
	</head>
	<body>
		<header class="mui-bar mui-bar-nav">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
			<h1 class="mui-title">用户管理</h1>
		</header>
		<div class="mui-content">
			<form id='login-form' class="mui-input-group">
				<div class="mui-input-row">
					<label>账号</label>
					<input id='account' type="text" class="mui-input-clear mui-input" placeholder="请输入账号">
				</div>
				<div class="mui-input-row">
					<label>密码</label>
					<input id='password' type="password" class="mui-input-clear mui-input" placeholder="请输入密码">
				</div>
				<div class="mui-input-row">
					<label>手机号</label>
					<input id='mobile' type="text" class="mui-input-clear mui-input" placeholder="请输入手机号">
				</div>
				<div class="mui-input-row">
					<label>昵称</label>
					<input id='description' type="text" class="mui-input-clear mui-input" placeholder="请输入用户昵称">
				</div>
				<div class="mui-input-row">
					<label>类型</label>
					<input id='type' type="text" class="mui-input-clear mui-input" placeholder="请输入用户类型">
				</div>
				<div class="mui-input-row">
					<label>open id</label>
					<input id='open-id' type="text" class="mui-input-clear mui-input" placeholder="微信openid">
				</div>
			</form>
			<div class="mui-content-padded">
				<button id='reg' class="mui-btn mui-btn-block mui-btn-primary">保存用户</button>
			</div>
		</div>
		<script src="/js/mui-min.js"></script>
		<script src="/js/jquery-3.3.1.min.js"></script>
		<script src="/js/project/app.js?v=1"></script>
		<script>
			(function(Page) {
				Page.params = app.parseParams(window.location.href);
				Page.userId=Page.params.id;
				
				$.ajax({
					url:app.getServerUrl('/user/view.do?id='+Page.userId),
					type:'GET',
					dataType:'json',
					success: function (data) {
						Page.user = data.data;
						$("#account").val(Page.user.name);
						$("#mobile").val(Page.user.mobile);
						$("#description").val(Page.user.description);
						$("#type").val(Page.user.type);
						$("#open-id").val(Page.user.openId);
						mui.toast(data.msg);
					},
					error: function(data){ mui.toast(data.msg); }
				});
				
				$("#reg").click(function() {
					var name = $("#account").val();
					var password = $("#password").val();
					var repassword = $("#repassword").val();
					var mobile = $("#mobile").val();
					var description = $("#description").val();
					var type = $("#type").val();
					var openId = $("#open-id").val();
					if (!name||!mobile||!description){
						mui.toast("请把信息填写完整");
						return;
					}
					var data = {'id':Page.userId,'name':name,'mobile':mobile,'description':description,'type':type,'openId':openId};
					if (password){
						data.password = password;
					}
					$.ajax({
						url:app.getServerUrl('/user/save.do'),
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						data: JSON.stringify(data),
						success: function (data) {
							mui.toast(data.msg);
						},
						error: function(data){ mui.toast(data.msg); }
					});
				});
				
				$("#login").click(function() {
					window.location.href = "/login.html";
				});
				
			}(Page={}));
		</script>
	</body>
</html>