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
				<div id="user-" class="mui-card" onclick="Page.openUser('2c9ad0b6-de25-4b2f-a671-3c41025744dc')">
					<div class="mui-card-header">
						张三 | zhangsan
					</div>
					<div class="mui-card-content">
						<div class="mui-card-content-inner">
							<p>手机号：13987654123</p>
							<p>类型：普通用户</p>
							<p>open id：u1542334</p>
						</div>
					</div>
					<!--页脚，放置补充信息或支持的操作-->
					<div class="mui-card-footer">
						创建时间：2018-04-04 12:12:31<br>
						修改时间：2018-04-04 12:12:31
					</div>
				</div>
			</div>
		</div>
		<script src="/js/mui-min.js"></script>
		<script src="/js/jquery-3.3.1.min.js"></script>
		<script src="/js/project/app.js"></script>
		<script>
			(function(Page) {
				
				Page.openUser = function(id){
					window.location.href = app.getServerUrl('/manage/user/view?id='+id);
				}
				
			}(Page={}));
		</script>
	</body>
</html>