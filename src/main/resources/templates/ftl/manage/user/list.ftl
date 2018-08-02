<!DOCTYPE html>
<html class="ui-page-login">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>用户管理</title>
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
			<h1 class="mui-title">用户管理</h1>
		</header>
		<div class="mui-content">
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
		</div>
		<script src="/js/mui-min.js"></script>
		<script src="/js/jquery-3.3.1.min.js"></script>
		<script src="/js/project/app.js"></script>
		<script src="/js/vue.min.js"></script>
		<script>
			(function(Page) {
				Page.userVm = new Vue({
					el: '#user-list',
					data: {
						users: []
					}
				});
				
				Page.openUser = function(id){
					window.location.href = app.getServerUrl('/manage/user/view?id='+id);
				}
				Page.openRole = function(id,ev){
					ev.preventDefault();
					//console.log(ev.target);
					console.log(id);
				}
				Page.searchParam = {};
				
				$.ajax({
					url:app.getServerUrl('/user/search.do'),
					type:'POST',
					contentType:'application/json;charset=UTF-8',
					dataType:'json',
					data: JSON.stringify(Page.searchParam),
					success: function (data) {
						Page.userVm.users = data.data;
						/* $("#account").val(Page.user.name);
						$("#mobile").val(Page.user.mobile);
						$("#description").val(Page.user.description);
						$("#type").val(Page.user.type);
						$("#open-id").val(Page.user.openId); */
						mui.toast(data.msg);
					},
					error: function(data){ mui.toast(data.msg); }
				});
				
			}(Page={}));
		</script>
	</body>
</html>