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
					<label>确认密码</label>
					<input id='repassword' type="password" class="mui-input-clear mui-input" placeholder="请再次输入密码">
				</div>
				<div class="mui-input-row">
					<label>手机号</label>
					<input id='mobile' type="text" class="mui-input-clear mui-input" placeholder="请输入手机号">
				</div>
				<div class="mui-input-row">
					<label>昵称</label>
					<input id='description' type="text" class="mui-input-clear mui-input" placeholder="请输入一个你喜欢的名字">
				</div>
				<input id='type' type="hidden" value="4">
			</form>
			<!-- <form class="mui-input-group">
				<ul class="mui-table-view mui-table-view-chevron">
					<li class="mui-table-view-cell">
						自动登录
						<div id="autoLogin" class="mui-switch">
							<div class="mui-switch-handle"></div>
						</div>
					</li>
				</ul>
			</form> -->
			<div class="mui-content-padded">
				<button id='reg' class="mui-btn mui-btn-block mui-btn-primary">马上注册</button>
				<div class="link-area"><a id='login'>账号登陆</a>
				</div>
			</div>
			<div class="mui-content-padded oauth-area">

			</div>
		</div>
		<script src="/js/mui-min.js"></script>
		<script src="/js/jquery-3.3.1.min.js"></script>
		<script>
			(function(doc) {
				
				$("#reg").click(function() {
					var name = $("#account").val();
					var password = $("#password").val();
					var repassword = $("#repassword").val();
					var mobile = $("#mobile").val();
					var description = $("#description").val();
					var type = $("#type").val();
					if (!name||!password||!repassword||!mobile||!description){
						mui.toast("请把信息填写完整");
						return;
					}
					if (password!=repassword){
						mui.toast("两次输入的密码不一致，请一定记住密码");
						return;
					}
					var data = {'name':name,'password':password,'mobile':mobile,'description':description,'type':type};
					$.ajax({
						url:"http://localhost:8080/system/register_w",
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						data: JSON.stringify(data),
						success: function (data) {
							mui.toast(data.msg);
							window.location.href = "/login.html";
						},
						error: function(data){ console.log(data); }
					});
				});
				
				$("#login").click(function() {
					window.location.href = "/login.html";
				});
				
			}(document));
		</script>
	</body>
</html>