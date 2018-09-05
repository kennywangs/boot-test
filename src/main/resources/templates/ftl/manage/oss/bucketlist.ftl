<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<title>文件仓库</title>
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
		<h1 class="mui-title">文件仓库</h1>
		<a id="bucket-add" class="mui-icon mui-icon-plus mui-pull-right"></a>
	</header>
	<div class="mui-content">
		<div id="bucket-page">
			<div id="bucket-list"></div>
			<template id="bucket-list-template">
				<div>
					<ul v-for="bucket in buckets" class="mui-table-view">
						<li class="mui-table-view-cell" @click="viewFiles(bucket)">{{ bucket.name }}
							<span class="mui-badge mui-badge-warning" @click.stop="bucketActions(bucket,$event)">操作</span>
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
		<div id="bucket-sheet" class="mui-popover mui-popover-bottom mui-popover-action ">
		    <!-- 可选择菜单 -->
		    <ul class="mui-table-view">
		      <li class="mui-table-view-cell">
		        <a id="bucket-edit">编辑</a>
		      </li>
		      <li class="mui-table-view-cell">
		        <a href="#">查看path</a>
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
				Page.bucketVm = new Vue({
					el: '#bucket-list',
					data: {
						buckets: []
					},
					template: '#bucket-list-template',
					methods: {
						viewFiles: function(bucket){
							window.location.href = app.getServerUrl('/manage/oss/filelist?id='+bucket._id+'&type=bucket');
						},
						bucketActions: function(bucket,ev){
							ev.preventDefault();
							Page.curBucket = bucket;
							mui('#bucket-sheet').popover('show');
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
						url:app.getServerUrl('/oss/bucket/search.do?size='+Page.limit+'&page='+Page.no),
						dataType:'json',
						type:'POST',
						data: JSON.stringify(Page.searchParam),
						contentType:'application/json;charset=UTF-8',
						success: function (data) {
							if (data.success){
								if (more){
									$.each(data.data,function(i,item){
										Page.bucketVm.buckets.push(item);
									});
								}else{
									Page.bucketVm.buckets=data.data;
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
				Page.searchParam = {};
				Page.postList();
				
				$('#load-more').click(function(){
					Page.postList(true);
				});
				$('#bucket-add').click(function(){
					window.location.href = app.getServerUrl('/manage/oss/bucketview');
				});
				$('#bucket-edit').click(function(){
					window.location.href = app.getServerUrl('/manage/oss/bucketview?id='+Page.curBucket._id);
				});
				$('#action-cancel').click(function(){
					mui('#bucket-sheet').popover('toggle');
				});
				
			}(Page={}));
		</script>
</body>
</html>