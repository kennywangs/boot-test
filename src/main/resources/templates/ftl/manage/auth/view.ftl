<!DOCTYPE html>
<html class="ui-page-login">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>权限管理</title>
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
			<h1 class="mui-title">权限管理</h1>
		</header>
		<div class="mui-content">
			<form id='auth-form' class="mui-input-group">
				<div class="mui-input-row">
					<label>权限名称</label>
					<input id='name' type="text" class="mui-input-clear mui-input" placeholder="请输入权限名称">
				</div>
				<div class="mui-input-row">
					<label>权限描述</label>
					<input id='description' type="text" class="mui-input-clear mui-input" placeholder="请输入权限描述">
				</div>
				<div class="mui-input-row">
					<label>类型</label>
					<input id='type' type="text" class="mui-input-clear mui-input" placeholder="请输入权限类型">
				</div>
				<div class="mui-input-row">
					<label>URL</label>
					<input id='authUrl' type="text" class="mui-input-clear mui-input" placeholder="请输入url">
				</div>
				<div class="mui-input-row">
					<label>需要授权</label>
					<input id='needAuth' type="text" class="mui-input-clear mui-input" placeholder="请输入是否需要授权">
				</div>
			</form>
			<div class="mui-content-padded">
				<button id='reg' class="mui-btn mui-btn-block mui-btn-primary">保存</button>
			</div>
		</div>
		<script src="/js/mui-min.js"></script>
		<script src="/js/jquery-3.3.1.min.js"></script>
		<script src="/js/project/app.js?v=1"></script>
		<script>
			(function(Page) {
				Page.params = app.parseParams(window.location.href);
				Page.authId=Page.params.id;
				
				if (Page.authId){
					$.ajax({
						url:app.getServerUrl('/auth/auth/view.do?id='+Page.authId),
						type:'GET',
						dataType:'json',
						success: function (data) {
							Page.auth = data.data;
							$("#name").val(Page.auth.name);
							$("#description").val(Page.auth.description);
							$("#type").val(Page.auth.authType);
							$("#authUrl").val(Page.auth.authUrl);
							$("#needAuth").val(Page.auth.needAuth);
							mui.toast(data.msg);
						},
						error: function(data){ mui.toast(data.msg); }
					});
				}
				
				$("#reg").click(function() {
					var name = $("#name").val();
					var description = $("#description").val();
					var type = $("#type").val();
					var authUrl = $("#authUrl").val();
					var needAuth = $("#needAuth").val();
					if (!name||!type||!description||!authUrl||!needAuth){
						mui.toast("请把信息填写完整");
						return;
					}
					var data = {'id':Page.authId,'name':name,'description':description,'authType':type,'authUrl':authUrl,'needAuth':needAuth};
					$.ajax({
						url:app.getServerUrl('/auth/auth/save.do'),
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
				
			}(Page={}));
		</script>
	</body>
</html>