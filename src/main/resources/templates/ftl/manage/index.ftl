<!DOCTYPE html>
<html class="ui-page-login">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>管理中心</title>
		<link href="/css/mui-min.css" rel="stylesheet" />
		<link href="/css/project.css?v=1" rel="stylesheet" />
		<style type="text/css">
			.mui-input-group{
				margin-top: 10px;
			}
		</style>
	</head>
	<body>
		<header class="mui-bar mui-bar-nav">
			<h1 class="mui-title">管理中心</h1>
		</header>
		<div class="mui-content">
			<ul class="mui-table-view mui-grid-view mui-grid-9">
				<li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
					<a href="/index.html">
						<span class="mui-icon mui-icon-home"></span>
						<div class="mui-media-body">首页</div>
					</a>
				</li>
				<li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
					<a href="/manage/user/list">
						<span class="mui-icon mui-icon-contact"></span>
						<div class="mui-media-body">用户管理</div>
					</a>
				</li>
				<li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
					<a href="#">
						<span class="mui-icon mui-icon-flag"></span>
						<div class="mui-media-body">机构管理</div>
					</a>
				</li>
				<li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
					<a href="#">
						<span class="mui-icon mui-icon-navigate"></span>
						<div class="mui-media-body">权限管理</div>
					</a>
				</li>
				<li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
					<a href="#">
						<span class="mui-icon mui-icon-navigate"></span>
						<div class="mui-media-body">角色管理</div>
					</a>
				</li>
				<li class="mui-table-view-cell mui-media mui-col-xs-4 mui-col-sm-4">
					<a href="#">
						<span class="mui-icon mui-icon-navigate"></span>
						<div class="mui-media-body">预约管理</div>
					</a>
				</li>
			</ul>
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