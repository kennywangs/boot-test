<!DOCTYPE html>
<html class="ui-page-login">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>角色管理</title>
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
			<h1 class="mui-title">角色管理</h1>
			<a id="role-add" class="mui-btn mui-btn-primary mui-pull-right">添加</a>
			<!-- <a id="role-search" class="mui-btn mui-btn-primary mui-pull-right">搜索</a> -->
		</header>
		<div class="mui-content">
			<div id="role-page">
				<div class="mui-input-row mui-search">
					<input id="role-param" type="search" class="mui-input-clear" placeholder="">
					<span class="mui-icon mui-icon-clear mui-hidden"></span>
					<span class="mui-placeholder"><span class="mui-icon mui-icon-search"></span>
						<span>搜索角色</span>
					</span>
				</div>
				<div id="role-list">
					<div v-for="role in roles" class="mui-card" @click="Page.openRole(role.id)">
						<div class="mui-card-header">
							{{ role.name }}
						</div>
						<div class="mui-card-content">
							<div class="mui-card-content-inner">
								<p>角色名：{{ role.description }}</p>
								<p>角色类型：{{ role.roleType }}</p>
							</div>
						</div>
						<!--页脚，放置补充信息或支持的操作-->
						<div class="mui-card-footer">
							创建时间：{{ role.createDate }}<br>
							修改时间：{{ role.modifyDate }}
							<button class="mui-btn mui-btn-primary" @click.stop="Page.openAuth(role.id,$event)">权限</button>
						</div>
					</div>
				</div>
				<div class="pro-row">
					<button id="load-more" class="mui-btn mui-btn-primary" style="display: none;">加载更多...</button>
				</div>
			</div>
			<div id="auth-page" style="display: none;">
				<div class="title">角色权限列表</div>
				<div id="role-auth">
					<ul v-for="auth in auths" class="mui-table-view">
					    <li class="mui-table-view-cell mui-media">
					        <a @click="chooseAuth(auth)">
					        	<span v-show="auth.selected" class="mui-pull-right mui-icon mui-icon-checkmarkempty"></span>
					            <div class="mui-media-body">
					               	 {{ auth.name }}
					                <p class='mui-ellipsis'>{{ auth.description }}</p>
					            </div>
					        </a>
					    </li>
					</ul>
				</div>
				<div class="pro-row" style="margin:10px auto;background-color: #fff;">
					<a id="add-auth" class="mui-icon mui-icon-arrowup" style="color: #fff;background-color: #008080;"></a>
					<a id="remove-auth" class="mui-icon mui-icon-arrowdown" style="color: #fff;background-color: #008080;"></a>
				</div>
				<div class="title">所有权限列表</div>
				<div id="all-auth">
					<ul v-for="auth in auths" class="mui-table-view">
					    <li class="mui-table-view-cell mui-media">
					        <a @click="chooseAuth(auth)">
					        	<span v-show="auth.selected" class="mui-pull-right mui-icon mui-icon-checkmarkempty"></span>
					            <div class="mui-media-body">
					               	 {{ auth.name }}
					                <p class='mui-ellipsis'>{{ auth.description }}</p>
					            </div>
					        </a>
					    </li>
					</ul>
				</div>
				
				<div class="pro-row" style="margin-top:10px;background-color: #fff;">
					<button id="auth-confirm" class="mui-btn mui-btn-primary">确认</button>
					<button id="back-role" class="mui-btn mui-btn-primary">返回</button>
				</div>
			</div>
		</div>
		<script src="/js/mui-min.js"></script>
		<script src="/js/jquery-3.3.1.min.js"></script>
		<script src="/js/project/app.js?v=1"></script>
		<script src="/js/vue.min.js"></script>
		<script>
			(function(Page) {
				Page.roleVm = new Vue({
					el: '#role-list',
					data: {
						roles: []
					}
				});
				
				Page.authVm = new Vue({
					el: '#role-auth',
					data:{
						auths: []
					},
					methods:{
						chooseAuth : function(auth){
							auth.selected = !auth.selected;
						}
					}
				});
				
				Page.allAuthVm = new Vue({
					el: '#all-auth',
					data: {
						auths: []
					},
					methods:{
						chooseAuth : function(auth){
							auth.selected = !auth.selected;
						}
					}
				});
				
				Page.openRole = function(id){
					window.location.href = app.getServerUrl('/manage/role/view?id='+id);
				}
				Page.openAuth = function(id,ev){
					ev.preventDefault();
					//console.log(ev.target);
					$('#role-page').toggle();
					$('#auth-page').toggle();
					Page.getRoleAuths(id);
					Page.getAllAuths();
				}
				Page.searchParam = {};
				Page.setDefaultPagePram = function(){
					this.no=0;
					this.limit=10;
				};
				Page.postSearch = function(more){
					var param=$('#role-param').val();
					if (param){
						Page.searchParam = {namesearch:param};
					}else{
						Page.searchParam = {};
					}
					if (more){
						Page.no++;
					}
					$.ajax({
						url:app.getServerUrl('/auth/role/list.do?size='+Page.limit+'&page='+Page.no),
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						data: JSON.stringify(Page.searchParam),
						success: function (data) {
							if (more){
								$.each(data.data.content,function(i,item){
									Page.roleVm.roles.push(item);
								});
							}else{
								Page.roleVm.roles = data.data.content;
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
				Page.getRoleAuths = function(roleId){
					$.ajax({
						url:app.getServerUrl('/auth/auth/listbyrole.do?roleId='+roleId),
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						//data: JSON.stringify(Page.searchParam),
						success: function (data) {
							Page.currentRoleId = roleId;
							$.each(data.data,function(i,item){
								item.selected=false;
							});
							Page.authVm.auths = data.data;
							mui.toast(data.msg);
						},
						error: function(data){ mui.toast(data.msg); }
					});
				};
				Page.getAllAuths = function(){
					$.ajax({
						url:app.getServerUrl('/auth/auth/list.do?page=0&size=30'),
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						data: JSON.stringify({}),
						success: function (data) {
							$.each(data.data.content,function(i,item){
								item.selected=false;
							});
							Page.allAuthVm.auths = data.data.content;
							mui.toast(data.msg);
						},
						error: function(data){ mui.toast(data.msg); }
					});
				};
				
				// 初始化列表
				Page.setDefaultPagePram();
				Page.postSearch();
				
				$('#role-search').click(function(){
					Page.setDefaultPagePram();
					Page.postSearch();
				});
				$('#role-add').click(function(){
					window.location.href = app.getServerUrl('/manage/role/view');
				});
				$('#load-more').click(function(){
					Page.postSearch(true);
				});
				$('#role-param').keypress(function(e){
					if(event.keyCode == "13") {
						document.activeElement.blur();
						Page.setDefaultPagePram();
						Page.postSearch();
						event.preventDefault();
					}
				});
				$('#back-role').click(function(){
					$('#role-page').toggle();
					$('#auth-page').toggle();
					Page.currentRoleId = null;
				});
				$('#add-auth').click(function(){
					var addlist=[];
					$.each(Page.allAuthVm.auths,function(i,item){
						if (item.selected){
							item.selected = false;
							addlist.push(item);
						}
					});
					$.each(addlist,function(i,item){
						var exist = app.containInList(Page.authVm.auths,'id',item.id);
						if (!exist){
							var newItem = $.extend({}, item);
							Page.authVm.auths.push(newItem);
						}
					});
				});
				$('#remove-auth').click(function(){
					var authList=[];
					$.each(Page.authVm.auths,function(i,item){
						if (!item.selected){
							authList.push(item);
						}
					});
					Page.authVm.auths=authList;
				});
				$('#auth-confirm').click(function(){
					if (Page.currentRoleId){
						var data = {id:Page.currentRoleId,auths:[]};
						$.each(Page.authVm.auths,function(i,item){
							var auth = {id:item.id};
							data.auths.push(auth);
						});
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
					}else{
						mui.toast('当前角色不存在!');
					}
				});
				
			}(Page={}));
		</script>
	</body>
</html>