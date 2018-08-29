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
			<h1 class="mui-title">用户管理</h1>
			<a id="user-add" class="mui-icon mui-icon-plus mui-pull-right"></a>
		</header>
		<div class="mui-content">
			<div id="user-page">
				<div class="mui-input-row mui-search">
					<input id="user-param" type="search" class="mui-input-clear" placeholder="">
					<span class="mui-icon mui-icon-clear mui-hidden"></span>
					<span class="mui-placeholder"><span class="mui-icon mui-icon-search"></span>
						<span>搜索用户</span>
					</span>
				</div>
				<div id="user-list">
					<div v-for="user in users" class="mui-card" @click="Page.openUser(user.id)">
						<div class="mui-card-header">
							{{ user.description }} | {{ user.name }}
						</div>
						<div class="mui-card-content">
							<div class="mui-card-content-inner">
								<p>手机号：{{ user.mobile }}</p>
								<p>类型：{{ user.type }}</p>
								<p>open id：{{ user.openId }}</p>
							</div>
						</div>
						<!--页脚，放置补充信息或支持的操作-->
						<div class="mui-card-footer">
							创建时间：{{ user.createDate }}<br>
							修改时间：{{ user.modifyDate }}
							<button class="mui-btn mui-btn-primary" @click.stop="Page.openRole(user.id,$event)">角色</button>
						</div>
					</div>
				</div>
				<div class="pro-row">
					<button id="load-more" class="mui-btn mui-btn-primary" style="display: none;">加载更多...</button>
				</div>
			</div>
			<div id="role-page" style="display: none;">
				<div class="title">用户角色列表</div>
				<div id="user-role">
					<ul v-for="role in roles" class="mui-table-view">
					    <li class="mui-table-view-cell mui-media">
					        <a @click="chooseRole(role)">
					        	<span v-show="role.selected" class="mui-pull-right mui-icon mui-icon-checkmarkempty"></span>
					            <div class="mui-media-body">
					               	 {{ role.description }}
					                <p class='mui-ellipsis'>{{ role.name }}</p>
					            </div>
					        </a>
					    </li>
					</ul>
				</div>
				<div class="pro-row" style="margin:10px auto;background-color: #fff;">
					<a id="add-role" class="mui-icon mui-icon-arrowup" style="color: #fff;background-color: #008080;"></a>
					<a id="remove-role" class="mui-icon mui-icon-arrowdown" style="color: #fff;background-color: #008080;"></a>
				</div>
				<div class="title">所有角色列表</div>
				<div id="all-role">
					<ul v-for="role in roles" class="mui-table-view">
					    <li class="mui-table-view-cell mui-media">
					        <a @click="chooseRole(role)">
					        	<span v-show="role.selected" class="mui-pull-right mui-icon mui-icon-checkmarkempty"></span>
					            <div class="mui-media-body">
					               	 {{ role.description }}
					                <p class='mui-ellipsis'>{{ role.name }}</p>
					            </div>
					        </a>
					    </li>
					</ul>
				</div>
				
				<div class="pro-row" style="margin-top:10px;background-color: #fff;">
					<button id="role-confirm" class="mui-btn mui-btn-primary">确认</button>
					<button id="back-user" class="mui-btn mui-btn-primary">返回</button>
				</div>
			</div>
		</div>
		<script src="/js/mui-min.js"></script>
		<script src="/js/jquery-3.3.1.min.js"></script>
		<script src="/js/project/app.js?v=1"></script>
		<script src="/js/vue.min.js"></script>
		<script>
			(function(Page) {
				Page.userVm = new Vue({
					el: '#user-list',
					data: {
						users: []
					}
				});
				
				Page.roleVm = new Vue({
					el: '#user-role',
					data:{
						roles: []
					},
					methods:{
						chooseRole : function(role){
							role.selected = !role.selected;
						}
					}
				});
				
				Page.allRoleVm = new Vue({
					el: '#all-role',
					data: {
						roles: []
					},
					methods:{
						chooseRole : function(role){
							role.selected = !role.selected;
						}
					}
				});
				
				Page.openUser = function(id){
					window.location.href = app.getServerUrl('/manage/user/view?id='+id);
				}
				Page.openRole = function(id,ev){
					ev.preventDefault();
					//console.log(ev.target);
					$('#user-page').toggle();
					$('#role-page').toggle();
					Page.getUserRoles(id);
					Page.getAllRoles();
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
						url:app.getServerUrl('/user/search.do?size='+Page.limit+'&page='+Page.no),
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						data: JSON.stringify(Page.searchParam),
						success: function (data) {
							if (more){
								$.each(data.data,function(i,item){
									Page.userVm.users.push(item);
								});
							}else{
								Page.userVm.users = data.data;
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
				Page.getUserRoles = function(userId){
					$.ajax({
						url:app.getServerUrl('/auth/role/listbyuser.do?userId='+userId),
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						//data: JSON.stringify(Page.searchParam),
						success: function (data) {
							Page.currentUserId = userId;
							$.each(data.data,function(i,item){
								item.selected=false;
							});
							Page.roleVm.roles = data.data;
							mui.toast(data.msg);
						},
						error: function(data){ mui.toast(data.msg); }
					});
				};
				Page.getAllRoles = function(){
					$.ajax({
						url:app.getServerUrl('/auth/role/list.do?page=0&size=20'),
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						data: JSON.stringify({}),
						success: function (data) {
							$.each(data.data,function(i,item){
								item.selected=false;
							});
							Page.allRoleVm.roles = data.data;
							mui.toast(data.msg);
						},
						error: function(data){ mui.toast(data.msg); }
					});
				};
				
				// 初始化列表
				Page.setDefaultPagePram();
				Page.postSearch();
				
				$('#user-search').click(function(){
					Page.setDefaultPagePram();
					Page.postSearch();
				});
				$('#user-add').click(function(){
					window.location.href = app.getServerUrl('/manage/user/view');
				});
				$('#load-more').click(function(){
					Page.postSearch(true);
				});
				$('#user-param').keypress(function(e){
					if(event.keyCode == "13") {
						document.activeElement.blur();
						Page.setDefaultPagePram();
						Page.postSearch();
						event.preventDefault();
					}
				});
				$('#back-user').click(function(){
					$('#user-page').toggle();
					$('#role-page').toggle();
					Page.currentUserId = null;
				});
				$('#add-role').click(function(){
					var addlist=[];
					$.each(Page.allRoleVm.roles,function(i,item){
						if (item.selected){
							item.selected = false;
							addlist.push(item);
						}
					});
					$.each(addlist,function(i,item){
						var exist = app.containInList(Page.roleVm.roles,'id',item.id);
						if (!exist){
							var newItem = $.extend({}, item);
							Page.roleVm.roles.push(newItem);
						}
					});
				});
				$('#remove-role').click(function(){
					var roleList=[];
					$.each(Page.roleVm.roles,function(i,item){
						if (!item.selected){
							roleList.push(item);
						}
					});
					Page.roleVm.roles=roleList;
				});
				$('#role-confirm').click(function(){
					if (Page.currentUserId){
						var data = {id:Page.currentUserId,roles:[]};
						$.each(Page.roleVm.roles,function(i,item){
							var role = {id:item.id};
							data.roles.push(role);
						});
						$.ajax({
							url:app.getServerUrl('/user/save.do'),
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
						mui.toast('当前用户不存在!');
					}
				});
				
			}(Page={}));
		</script>
	</body>
</html>