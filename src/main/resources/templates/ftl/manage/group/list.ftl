<!DOCTYPE html>
<html class="ui-page-login">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>用户管理</title>
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
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
			<a href="/manage/index" class="mui-icon mui-icon-home" style="color: #999;"></a>
			<h1 class="mui-title">机构管理</h1>
			<a href="javascript:void" id="group-add" class="mui-icon mui-icon-plus mui-pull-right"></a>
		</header>
		<div class="mui-content">
			<div id="group-page">
				<div class="mui-input-row mui-search">
					<input id="group-param" type="search" class="mui-input-clear" placeholder="">
					<span class="mui-icon mui-icon-clear mui-hidden"></span>
					<span class="mui-placeholder"><span class="mui-icon mui-icon-search"></span>
						<span>搜索机构</span>
					</span>
				</div>
				<div id="group-list">
					<div v-for="group in groups" class="mui-card" @click="Page.openGroup(group.id)">
						<div class="mui-card-header">
							{{ group.description }}
						</div>
						<div class="mui-card-content">
							<div class="mui-card-content-inner">
								<p>机构ID：{{ group.id }}</p>
								<p>机构名称：{{ group.name }}</p>
								<p>机构号：{{ group.no }}</p>
								<p>类型：{{ group.type }}</p>
							</div>
						</div>
						<!--页脚，放置补充信息或支持的操作-->
						<div class="mui-card-footer">
							创建时间：{{ group.createDate }}<br>
							修改时间：{{ group.modifyDate }}
						</div>
					</div>
				</div>
				<div class="pro-row">
					<button id="load-more" class="mui-btn mui-btn-primary" style="display: none;">加载更多...</button>
				</div>
			</div>
		</div>
		<script src="/js/mui-min.js"></script>
		<script src="/js/jquery-3.3.1.min.js"></script>
		<script src="/js/project/app.js?v=1"></script>
		<script src="/js/vue.min.js"></script>
		<script>
			(function(Page) {
				Page.groupVm = new Vue({
					el: '#group-list',
					data: {
						groups: []
					}
				});
				
				Page.openGroup = function(id){
					window.location.href = app.getServerUrl('/manage/group/view?id='+id);
				}
				Page.searchParam = {};
				Page.setDefaultPagePram = function(){
					this.no=0;
					this.limit=10;
				};
				Page.postSearch = function(more){
					var param=$('#user-param').val();
					if (param){
						Page.searchParam = {namesearch:param};
					}else{
						Page.searchParam = {};
					}
					if (more){
						Page.no++;
					}
					$.ajax({
						url:app.getServerUrl('/group/list.do?size='+Page.limit+'&page='+Page.no),
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						data: JSON.stringify(Page.searchParam),
						success: function (data) {
							if (more){
								$.each(data.data,function(i,item){
									Page.groupVm.groups.push(item);
								});
							}else{
								Page.groupVm.groups = data.data;
							}
							if ((Page.no+1)<data.totalPage){
								$('#load-more').show();
							}else{
								$('#load-more').hide();
							}
							mui.toast(data.msg);
						},
						error: function(data){ mui.toast(data.msg); }
					});
				};
				
				// 初始化列表
				Page.setDefaultPagePram();
				Page.postSearch();
				
				$('#group-search').click(function(){
					Page.setDefaultPagePram();
					Page.postSearch();
				});
				$('#group-add').click(function(){
					window.location.href = app.getServerUrl('/manage/group/view');
				});
				$('#load-more').click(function(){
					Page.postSearch(true);
				});
				$('#group-param').keypress(function(e){
					if(event.keyCode == "13") {
						document.activeElement.blur();
						Page.setDefaultPagePram();
						Page.postSearch();
						event.preventDefault();
					}
				});
			}(Page={}));
		</script>
	</body>
</html>