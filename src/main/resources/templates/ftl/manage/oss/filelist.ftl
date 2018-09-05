<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<title>文件列表</title>
<link href="/css/mui-min.css" rel="stylesheet" />
<link href="/css/mui.picker.min.css?v=1" rel="stylesheet" />
<link href="/css/project.css?v=1" rel="stylesheet" />
<style type="text/css">
.mui-input-group {
	margin-top: 10px;
}
</style>
</head>
<body>
	<header class="mui-bar mui-bar-nav">
		<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
		<a href="/manage/index" class="mui-icon mui-icon-home" style="color: #999;"></a>
		<h1 class="mui-title">文件列表</h1>
		<a id="file-add" class="mui-icon mui-icon-plus mui-pull-right"></a>
	</header>
	<div class="mui-content">
		<div id="file-page">
			<div id="file-list"></div>
			<template id="file-list-template">
				<div>
					<ul v-for="file in files" class="mui-table-view">
						<li class="mui-table-view-cell" @click="viewFiles(file)">{{ file.descName }}.{{ file.ext }}
							<span class="mui-badge mui-badge-warning" @click.stop="fileActions(file,$event)">操作</span>
						</li>
					</ul>
				</div>
			</template>
			<!-- 加载更多 -->
			<div class="pro-row">
				<button id="load-more" class="mui-btn mui-btn-primary"
					style="display: none;">加载更多...</button>
			</div>
		</div>
		<div id="file-sheet" class="mui-popover mui-popover-bottom mui-popover-action ">
		    <!-- 可选择菜单 -->
		    <ul class="mui-table-view">
		      <li class="mui-table-view-cell">
		        <a id="file-edit">编辑</a>
		      </li>
		      <li class="mui-table-view-cell">
		        <a id="file-download">下载</a>
		      </li>
		      <li class="mui-table-view-cell">
		        <a id="file-delete">删除</a>
		      </li>
		    </ul>
		    <!-- 取消菜单 -->
		    <ul class="mui-table-view">
		      <li class="mui-table-view-cell">
		        <a id="action-cancel"><b>取消</b></a>
		      </li>
		    </ul>
		</div>
	</div>
	<script src="/js/mui-min.js"></script>
	<script src="/js/jquery-3.3.1.min.js"></script>
	<script src="/js/project/app.js?v=1"></script>
	<script src="/js/vue.min.js"></script>
	<script>
			(function(Page) {
				Page.params = app.parseParams(window.location.href);
				if (Page.params.type==='bucket'){
					Page.searchParam = {parentId:"bucket",bucketId:Page.params.id};
				}else if (Page.params.type==='file'){
					Page.searchParam = {parentId:Page.params.id};
				}else{
					mui.toast("parent is undefined.");
				}
				
				Page.fileVm = new Vue({
					el: '#file-list',
					data: {
						files: []
					},
					template: '#file-list-template',
					methods: {
						viewFiles: function(file){
							//window.location.href = app.getServerUrl('/manage/oss/filelist?id='+bucket._id+'&type=bucket');
						},
						fileActions: function(file,ev){
							ev.preventDefault();
							Page.curFile = file;
							mui('#file-sheet').popover('show');
						}
					}
				});
				
				Page.setDefaultPageParam = function(){
					this.no=0;
					this.limit=10;
				};
				
				Page.postList = function(more){
					if (more){
						Page.no++;
					}else{
						Page.setDefaultPageParam();
					}
					$.ajax({
						url:app.getServerUrl('/oss/file/search.do?size='+Page.limit+'&page='+Page.no),
						dataType:'json',
						type:'POST',
						data: JSON.stringify(Page.searchParam),
						contentType:'application/json;charset=UTF-8',
						success: function (data) {
							if (data.success){
								if (more){
									$.each(data.data,function(i,item){
										Page.fileVm.files.push(item);
									});
								}else{
									Page.fileVm.files=data.data;
								}
								if ((Page.no+1)<data.totalPage){
									$('#load-more').show();
								}else{
									$('#load-more').hide();
								}
							}else{
								mui.toast(data.msg);
							}
							console.log(data.msg);
						},
						error: function(data){
							obj = JSON.parse(data.responseText);
							console.log(obj.msg);
						}
					});
				};
				
				// 初始化列表
				Page.setDefaultPageParam();
				Page.postList();
				
				$('#load-more').click(function(){
					Page.postList(true);
				});
				$('#file-add').click(function(){
					if (Page.params.type==='bucket'){
						window.location.href = app.getServerUrl('/manage/oss/fileview?bucketId='+Page.params.id);
					}else if (Page.params.type==='file'){
						Page.searchParam = {parentId:Page.params.id};
						window.location.href = app.getServerUrl('/manage/oss/fileview?parentId='+Page.params.id);
					}
				});
				$('#file-edit').click(function(){
					window.location.href = app.getServerUrl('/manage/oss/fileview?id='+Page.curFile._id);
				});
				$('#file-download').click(function(){
					window.location.href = app.getServerUrl('/oss/file/download.do?id='+Page.curFile._id);
					mui('#file-sheet').popover('toggle');
				});
				$('#file-delete').click(function(){
					$.ajax({
						url:app.getServerUrl('/oss/file.do?id='+Page.curFile._id),
						dataType:'json',
						type:'DELETE',
						//data: '',
						contentType:'application/json;charset=UTF-8',
						success: function (data) {
							mui.toast(data.msg);
						},
						error: function(data){
							obj = JSON.parse(data.responseText);
							console.log(obj.msg);
							mui.toast(obj.msg);
						}
					});
					mui('#file-sheet').popover('toggle');
				});
				$('#action-cancel').click(function(){
					mui('#file-sheet').popover('toggle');
				});
				
			}(Page={}));
		</script>
</body>
</html>