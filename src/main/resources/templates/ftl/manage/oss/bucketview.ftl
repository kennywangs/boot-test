<!DOCTYPE html>
<html class="ui-page-login">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>仓库管理</title>
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
			<h1 class="mui-title">仓库管理</h1>
		</header>
		<div class="mui-content">
			<form id='login-form' class="mui-input-group">
				<div class="mui-input-row">
					<label>名称</label>
					<input id="name" type="text" class="mui-input-clear mui-input" placeholder="请输入名称">
				</div>
				<div class="mui-input-row">
					<label>路径</label>
					<input id="path" type="text" class="mui-input-clear mui-input" readonly="readonly">
				</div>
				<div class="mui-input-row">
					<label>文件夹</label>
					<input id="realName" type="text" class="mui-input-clear mui-input" readonly="readonly">
				</div>
			</form>
			<div class="mui-content-padded">
				<button id="save" class="mui-btn mui-btn-block mui-btn-primary">保存</button>
			</div>
		</div>
		<script src="/js/mui-min.js"></script>
		<script src="/js/jquery-3.3.1.min.js"></script>
		<script src="/js/project/app.js?v=1"></script>
		<script>
			(function(Page) {
				Page.params = app.parseParams(window.location.href);
				Page.bucketId=Page.params.id;
				
				if (Page.bucketId){
					$.ajax({
						url:app.getServerUrl('/oss/bucket.do?id='+Page.bucketId),
						type:'GET',
						dataType:'json',
						success: function (data) {
							Page.bucket = data.data;
							$("#name").val(Page.bucket.name);
							$("#path").val(Page.bucket.filePath);
							$("#realName").val(Page.bucket.realName);
							mui.toast(data.msg);
						},
						error: function(data){
							obj = JSON.parse(data.responseText);
							mui.toast(obj.msg);
						}
					});
				}
				
				$("#save").click(function() {
					var name = $("#name").val();
					if (!name){
						mui.toast("请把信息填写完整");
						return;
					}
					var data = {'_id':Page.bucketId,'name':name};
					if (!Page.bucketId){
						$.ajax({
							url:app.getServerUrl('/oss/bucket.do'),
							type:'POST',
							contentType:'application/json;charset=UTF-8',
							dataType:'json',
							data: JSON.stringify(data),
							success: function (data) {
								mui.toast(data.msg);
							},
							error: function(data){
								obj = JSON.parse(data.responseText);
								mui.toast(obj.msg);
							}
						});
					}else{
						$.ajax({
							url:app.getServerUrl('/oss/bucket.do'),
							type:'PUT',
							contentType:'application/json;charset=UTF-8',
							dataType:'json',
							data: JSON.stringify(data),
							success: function (data) {
								mui.toast(data.msg);
							},
							error: function(data){
								obj = JSON.parse(data.responseText);
								mui.toast(obj.msg);
							}
						});
					}
				});
				
			}(Page={}));
		</script>
	</body>
</html>