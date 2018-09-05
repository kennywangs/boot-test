<!DOCTYPE html>
<html class="ui-page-login">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>oss文件管理</title>
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
			<h1 class="mui-title">文件管理</h1>
		</header>
		<div class="mui-content">
			<form id='login-form' class="mui-input-group">
				<div class="mui-input-row">
					<label>文件名称</label>
					<input id='name' type="text" class="mui-input-clear mui-input" placeholder="请输入文件名称">
				</div>
				<div class="mui-input-row">
					<label>文件类型</label>
					<input id='type' type="text" class="mui-input-clear mui-input" readonly="readonly" placeholder="请选择文件类型">
				</div>
				<div id="file-node" class="mui-input-row" style="display: none;">
					<label>选择上传文件</label>
					<input id="file" type="file" class="mui-input-clear mui-input" onchange="Page.fileSelected(this);">
				</div>
				<div class="mui-input-row">
					<label>扩展名</label>
					<input id="ext" type="text" class="mui-input-clear mui-input" readonly="readonly">
				</div>
				<div class="mui-input-row">
					<label>路径</label>
					<input id='path' type="text" class="mui-input-clear mui-input" readonly="readonly">
				</div>
				<div class="mui-input-row">
					<label>parentId</label>
					<input id="parentId" type="text" class="mui-input-clear mui-input" readonly="readonly">
				</div>
				<div class="mui-input-row">
					<label>bucketId</label>
					<input id="bucketId" type="text" class="mui-input-clear mui-input" readonly="readonly">
				</div>
			</form>
			<div class="mui-content-padded">
				<button id="save" class="mui-btn mui-btn-block mui-btn-primary">保存</button>
			</div>
		</div>
		<div class="progressNumber" id="file_progress">
            <div class="progressContent" id="progressContent"></div>
        </div>
		<script src="/js/mui-min.js"></script>
		<script src="/js/jquery-3.3.1.min.js"></script>
		<script src="/js/project/app.js?v=1"></script>
		<script>
			(function(Page) {
				Page.params = app.parseParams(window.location.href);
				Page.fileId=Page.params.id;
				
				if (Page.fileId){
					$.ajax({
						url:app.getServerUrl('/oss/file.do?id='+Page.fileId),
						type:'GET',
						dataType:'json',
						success: function (data) {
							Page.file = data.data;
							$("#name").val(Page.file.descName);
							$("#type").val(Page.file.type);
							$("#ext").val(Page.file.ext);
							$("#path").val(Page.file.filePath);
							$("#parentId").val(Page.file.parentId);
							$("#bucketId").val(Page.file.bucketId);
							mui.toast(data.msg);
						},
						error: function(data){
							obj = JSON.parse(data.responseText);
							mui.toast(obj.msg);
						}
					});
				}else{
					$("#file-node").show();
					$("#parentId").val(Page.params.parentId);
					$("#bucketId").val(Page.params.bucketId);
				}
				
				$("#save").click(function() {
					var name = $("#name").val();
					var type = $("#type").val();
					var bucketId = $("#bucketId").val();
					var parantId = $("#parantId").val();
					if (!name||!type){
						mui.toast("请把信息填写完整");
						return;
					}
					if (!Page.fileId && type==1){
						var fileNode = document.getElementById('file');
						var file = fileNode.files[0];
						if (!file){
							mui.toast("选择要上传的文件");
							return;
						}
						var data = {bucketId:bucketId,parantId:parantId,type:type,descName:name};
						var fd = new FormData();
						fd.append("file", file);
						fd.append("params", JSON.stringify(data));
						console.log(fd);
						var xhr = new XMLHttpRequest();
						xhr.upload.addEventListener("progress", uploadProgress, false);
						xhr.addEventListener("load", uploadComplete, false);
						xhr.addEventListener("error", uploadFailed, false);
						xhr.addEventListener("abort", uploadCanceled, false);
						xhr.open("post", "/oss/file.do");
						xhr.send(fd);
					}else{
						var data = {'_id':Page.fileId,'descName':name,'type':type};
						$.ajax({
							url:app.getServerUrl('/oss/file.do'),
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
				Page.fileSelected = function(node) {
		            var file = node.files[0];
		            console.log(file);
					if (file) {
						$("#type").val(1);
						$("#name").val(file.name.split('.')[0]);
		            }
		        }
		        
		        function uploadProgress(evt) {
		            if (evt.lengthComputable) {
		                var percentComplete = Math.round(evt.loaded * 100 / evt.total);
		                document.getElementById('progressContent').style.width = percentComplete.toString()+ '%';
		                document.getElementById('progressContent').innerHTML = percentComplete.toString()+ '%';
		            } else {
		                document.getElementById('file_progress').innerHTML = '无法计算';
		            }
		        }
		
		        function uploadComplete(evt) {
		            /* 当服务器响应后，这个事件就会被触发 */
		            mui.toast("上传成功：" + evt.target.responseText);
		        }
		
		        function uploadFailed(evt) {
		            mui.toast("上传失败：" + "上传文件发生了错误");
		        }
		
		        function uploadCanceled(evt) {
		            mui.toast("上传取消：" + "上传被用户取消或者浏览器断开连接");
		        }
				
			}(Page={}));
		</script>
	</body>
</html>