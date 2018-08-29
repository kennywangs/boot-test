<!DOCTYPE html>
<html class="ui-page-login">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>机构管理</title>
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
			<h1 class="mui-title">机构管理</h1>
		</header>
		<div class="mui-content">
			<form id='login-form' class="mui-input-group">
				<div class="mui-input-row">
					<label>机构名称</label>
					<input id='name' type="text" class="mui-input-clear mui-input" placeholder="请输入名称">
				</div>
				<div class="mui-input-row">
					<label>机构显示名称</label>
					<input id='description' type="text" class="mui-input-clear mui-input" placeholder="请输入英文名称或机构号">
				</div>
				<div class="mui-input-row">
					<label>机构号</label>
					<input id='no' type="text" class="mui-input-clear mui-input" placeholder="请输入用户昵称">
				</div>
				<div class="mui-input-row">
					<label>机构类型</label>
					<input id='type' type="text" class="mui-input-clear mui-input" placeholder="请输入用户类型">
				</div>
			</form>
			<div class="mui-content-padded">
				<button id='group-save' class="mui-btn mui-btn-block mui-btn-primary">保存机构</button>
			</div>
		</div>
		<script src="/js/mui-min.js"></script>
		<script src="/js/jquery-3.3.1.min.js"></script>
		<script src="/js/project/app.js?v=1"></script>
		<script>
			(function(Page) {
				Page.params = app.parseParams(window.location.href);
				Page.groupId=Page.params.id;
				
				if (Page.groupId){
					$.ajax({
						url:app.getServerUrl('/group/view.do?id='+Page.groupId),
						type:'GET',
						dataType:'json',
						success: function (data) {
							Page.group = data.data;
							$("#name").val(Page.group.name);
							$("#description").val(Page.group.description);
							$("#type").val(Page.group.type);
							$("#no").val(Page.group.no);
							mui.toast(data.msg);
						},
						error: function(data){ mui.toast(data.msg); }
					});
				}
				
				$("#group-save").click(function() {
					var name = $("#name").val();
					var no = $("#no").val();
					var description = $("#description").val();
					var type = $("#type").val();
					if (!name||!no||!description||!type){
						mui.toast("请把信息填写完整");
						return;
					}
					var data = {'id':Page.groupId,'name':name,'description':description,'type':type,'no':no};
					$.ajax({
						url:app.getServerUrl('/group/save.do'),
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						data: JSON.stringify(data),
						success: function (data) {
							window.location.href = app.getServerUrl('/manage/group/list');
							mui.toast(data.msg);
						},
						error: function(data){ mui.toast(data.msg); }
					});
				});
				
			}(Page={}));
		</script>
	</body>
</html>