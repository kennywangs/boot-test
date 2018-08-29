<!DOCTYPE html>
<html class="ui-page-login">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>角色管理</title>
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
			<h1 class="mui-title">角色管理</h1>
		</header>
		<div class="mui-content">
			<form id='login-form' class="mui-input-group">
				<div class="mui-input-row">
					<label>角色名称</label>
					<input id='name' type="text" class="mui-input-clear mui-input" placeholder="请输入角色名称">
				</div>
				<div class="mui-input-row">
					<label>角色描述</label>
					<input id='description' type="text" class="mui-input-clear mui-input" placeholder="请输入角色描述">
				</div>
				<div class="mui-input-row">
					<label>类型</label>
					<input id='type' type="text" class="mui-input-clear mui-input" placeholder="请输入权限类型">
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
				Page.roleId=Page.params.id;
				
				if (Page.roleId){
					$.ajax({
						url:app.getServerUrl('/auth/role/view.do?id='+Page.roleId),
						type:'GET',
						dataType:'json',
						success: function (data) {
							Page.role = data.data;
							$("#name").val(Page.role.name);
							$("#description").val(Page.role.description);
							$("#type").val(Page.role.roleType);
							mui.toast(data.msg);
						},
						error: function(data){ mui.toast(data.msg); }
					});
				}
				
				$("#reg").click(function() {
					var name = $("#name").val();
					var description = $("#description").val();
					var type = $("#type").val();
					if (!name||!type||!description){
						mui.toast("请把信息填写完整");
						return;
					}
					var data = {'id':Page.roleId,'name':name,'description':description,'roleType':type};
					$.ajax({
						url:app.getServerUrl('/auth/role/save.do'),
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