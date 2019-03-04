<!DOCTYPE html>
<html class="ui-page-login">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>权限管理</title>
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
			<h1 class="mui-title">权限管理</h1>
			<a id="auth-add" class="mui-icon mui-icon-plus mui-pull-right"></a>
		</header>
		<div class="mui-content">
			<div id="auth-page">
				<div class="mui-input-row mui-search">
					<input id="auth-param" type="search" class="mui-input-clear" placeholder="">
					<span class="mui-icon mui-icon-clear mui-hidden"></span>
					<span class="mui-placeholder"><span class="mui-icon mui-icon-search"></span>
						<span>搜索权限</span>
					</span>
				</div>
				<div id="auth-list">
					<div v-for="auth in auths" class="mui-card" @click="Page.openAuth(auth.id)">
						<div class="mui-card-header">
							{{ auth.description }}
						</div>
						<div class="mui-card-content">
							<div class="mui-card-content-inner">
								<p>权限名：{{ auth.name }}</p>
								<p>权限URL：{{ auth.authUrl }}</p>
							</div>
						</div>
						<!--页脚，放置补充信息或支持的操作-->
						<div class="mui-card-footer">
							创建时间：{{ auth.createDate }}<br>
							修改时间：{{ auth.modifyDate }}
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
				Page.authVm = new Vue({
					el: '#auth-list',
					data: {
						auths: []
					}
				});
				
				Page.openAuth = function(id){
					window.location.href = app.getServerUrl('/manage/auth/view?id='+id);
				}
				Page.searchParam = {};
				Page.setDefaultPagePram = function(){
					this.no=0;
					this.limit=10;
				};
				Page.postSearch = function(more){
					var param=$('#auth-param').val();
					if (param){
						Page.searchParam = {namesearch:param};
					}else{
						Page.searchParam = {};
					}
					if (more){
						Page.no++;
					}
					$.ajax({
						url:app.getServerUrl('/auth/auth/list.do?size='+Page.limit+'&page='+Page.no),
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						data: JSON.stringify(Page.searchParam),
						success: function (data) {
							if (more){
								$.each(data.data.content,function(i,item){
									Page.authVm.auths.push(item);
								});
							}else{
								Page.authVm.auths = data.data;
							}
							if ((Page.no+1)<data.totalPages){
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
				
				$('#auth-search').click(function(){
					Page.setDefaultPagePram();
					Page.postSearch();
				});
				$('#auth-add').click(function(){
					window.location.href = app.getServerUrl('/manage/auth/view');
				});
				$('#load-more').click(function(){
					Page.postSearch(true);
				});
				$('#auth-param').keypress(function(e){
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