<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>我的预约</title>
		<link href="/css/mui-min.css" rel="stylesheet" />
		<link href="/css/mui.picker.min.css?v=1" rel="stylesheet" />
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
			<h1 class="mui-title">我的预约管理</h1>
		</header>
		<div class="mui-content">
			<div id="appoint-page">
				<div class="pro-row">
					<button id="list-appoint" class="mui-btn mui-btn-primary">查看预约</button>
				</div>
				<div id="appoint-list">
					<div v-for="appoint in appoints" class="mui-card">
						<div class="mui-card-header">
							预约时间：{{ appoint.startDate }} —— {{ appoint.endDate }}
						</div>
						<div class="mui-card-content">
							<div class="mui-card-content-inner">
								<p>客户名称：{{ appoint.customer.description }}</p>
								<p>服务技师：{{ appoint.attendant.description }}</p>
								<p>预约状态：{{ getStatusText(appoint.status) }}</p>
								<p>留言：{{ appoint.comment }}</p>
							</div>
						</div>
						<!--页脚，放置补充信息或支持的操作-->
						<div class="mui-card-footer">
							发起预约时间：{{ appoint.createDate }}
							<div>
								<button class="mui-btn mui-btn-primary" v-show="appoint.status == 1" @click.stop="confirmAppoint(appoint)">确认</button>
								<button class="mui-btn mui-btn-primary" v-show="appoint.status == 1" @click.stop="cancelAppoint(appoint)">取消</button>
								<button class="mui-btn mui-btn-primary" v-show="user.type == 1" @click.stop="deleteAppoint(appoint)">删除</button>
							</div>
						</div>
					</div>
				</div>
				<div class="pro-row">
					<button id="load-more" class="mui-btn mui-btn-primary" style="display: none;">加载更多...</button>
				</div>
			</div>
			<div id="create-page" style="display: none;">
				<div class="title">创建预约</div>
				<form id='appoint-form' class="mui-input-group">
					<div class="mui-input-row">
						<label>技师</label>
						<a id="pick-attendant" class="mui-btn mui-btn-primary">选择技师</a>
						<input id="aname" type="text" class="mui-input-clear mui-input" style="width:50%" readonly="readonly">
					</div>
					<div class="mui-input-row">
						<label>预约时间</label>
						<a id="pick-start" class="mui-btn mui-btn-primary">选择时间</a>
						<input id="startDate" type="text" class="mui-input-clear mui-input" style="width:50%" readonly="readonly">
					</div>
					<div class="mui-input-row">
						<label>预约时长</label>
						<input id="time" type="number" min="1" max="10" class="mui-input-clear mui-input" placeholder="请输入整数">
					</div>
					<div class="mui-input-row">
						<label>留言</label>
						<input id="comment" type="text" class="mui-input-clear mui-input"></input>
					</div>
					<input id="attendant" type="hidden">
				</form>
				
				<div id="role-popover" class="mui-popover">
					<ul v-for="user in users" class="mui-table-view">
						<li class="mui-table-view-cell"><a @click.stop="pickattendant(user)">{{ user.description }}</a></li>
					</ul>
				</div>
				
				<div class="pro-row" style="margin-top:10px;background-color: #fff;">
					<button id="appoint-confirm" class="mui-btn mui-btn-primary">确认</button>
					<button id="back-list" class="mui-btn mui-btn-primary">返回</button>
				</div>
			</div>
		</div>
		<script src="/js/mui-min.js"></script>
		<script src="/js/mui.picker.min.js?v=2"></script>
		<script src="/js/jquery-3.3.1.min.js"></script>
		<script src="/js/project/app.js?v=1"></script>
		<script src="/js/date.js"></script>
		<script src="/js/vue.min.js"></script>
		<script>
			(function(Page) {
				Page.attendantVm = new Vue({
					el: '#role-popover',
					data: {
						users: []
					},
					methods:{
						pickattendant : function(user){
							$('#aname').val(user.description);
							$('#attendant').val(user.id);
							mui('#role-popover').popover('toggle');
							console.log(user.id);
						}
					}
				});
				
				var dtPicker = new mui.DtPicker({'type':'date'});
				
				Page.appointVm = new Vue({
					el: '#appoint-list',
					data:{
						user:{},
						appoints: []
					},
					methods:{
						getStatusText : function(status){
							if (status==1)
								return "等待确认";
							if (status==10)
								return "预约已确认";
							if (status==99)
								return "已取消";
						},
						cancelAppoint : function(appoint){
							$.ajax({
								url:app.getServerUrl('/customer/appoint/cancel'),
								dataType:'json',
								data: {id:appoint.id},
								success: function (data) {
									Page.postList();
									console.log(data.msg);
								},
								error: function(data){
									obj = JSON.parse(data.responseText);
									console.log(obj.msg);
								}
							});
						},
						confirmAppoint : function(appoint){
							$.ajax({
								url:app.getServerUrl('/appoint/attendant/confirm.do'),
								dataType:'json',
								data: {id:appoint.id},
								success: function (data) {
									Page.postList();
									console.log(data.msg);
								},
								error: function(data){
									obj = JSON.parse(data.responseText);
									console.log(obj.msg);
								}
							});
						},
						deleteAppoint : function(appoint){
							$.ajax({
								url:app.getServerUrl('/appoint/attendant/delete.do'),
								dataType:'json',
								data: {id:appoint.id},
								success: function (data) {
									Page.postList();
									console.log(data.msg);
								},
								error: function(data){
									obj = JSON.parse(data.responseText);
									console.log(obj.msg);
								}
							});
						}
					}
				});
				
				Page.setDefaultPageParam = function(){
					this.no=0;
					this.limit=10;
				};
				
				$.ajax({
					url:app.getServerUrl("/system/getuser"),
					type:'GET',
					dataType:'json',
					success: function (data) {
						mui.toast(data.msg);
						if (data.success){
							app.user=data.data;
							Page.appointVm.user=data.data;
						}
					},
					error: function(data){
						mui.toast(data.msg);
						console.log(data);
					}
				});
				
				Page.postList = function(more){
					if (more){
						Page.no++;
					}else{
						Page.setDefaultPageParam();
					}
					$.ajax({
						url:app.getServerUrl('/appoint/attendant/mylist.do'),
						dataType:'json',
						data: {page:Page.no,size:Page.limit,date:Page.dateStr},
						success: function (data) {
							if (data.success){
								if (more){
									$.each(data.data,function(i,item){
										Page.appointVm.appoints.push(item);
									});
								}else{
									Page.appointVm.appoints=data.data;
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
				
				// 选择开始时间
				var startPicker = new mui.DtPicker({'type':'hour'});
				$('#pick-start').click(function(){
					startPicker.show(function(item){
						var timeStr = item.y.text+'-'+item.m.text+'-'+item.d.text+' '+item.h.text;
						console.log(timeStr);
						var date = DateUtils.Date.fromString(timeStr,'yyyy-MM-dd hh');
						$('#startDate').val(DateUtils.Date.toString(date,DateUtils.Date.DEFAULT_FORMAT1));
					});
				});
				
				// 选择技师
				$('#pick-attendant').click(function(){
					$.ajax({
						url:app.getServerUrl('/customer/appoint/list-attendant'),
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						data: JSON.stringify(Page.searchParam),
						success: function (data) {
							Page.attendantVm.users = data.data;
							mui('#role-popover').popover('toggle',document.getElementById("pick-attendant"));
							mui.toast(data.msg);
						},
						error: function(data){ mui.toast(data.msg); }
					});
				});
				
				$('#appoint-confirm').click(function(){
					var time = app.toInt($('#time').val());
					if (time <= 0){
						mui.toast("预约时长不正确");
						return;
					}
					$('#time').val(time);
					var startDateStr = $('#startDate').val();
					var comment = $('#comment').val();
					var attendantId = $('#attendant').val();
					var startDate = DateUtils.Date.fromString(startDateStr,'yyyy-MM-dd hh');
					var endDate = DateUtils.Date.increase(startDate,time,'h');
					var endDateStr = DateUtils.Date.toString(endDate,DateUtils.Date.DEFAULT_FORMAT1);
					var data = {attendant:{id:attendantId},comment:comment,startDate:startDateStr,endDate:endDateStr};
					console.log(data);
					$.ajax({
						url:app.getServerUrl('/customer/appoint/start'),
						type:'POST',
						contentType:'application/json;charset=UTF-8',
						dataType:'json',
						data: JSON.stringify(data),
						success: function (data) {
							console.log(data.msg);
							$('#appoint-page').toggle();
							$('#create-page').toggle();
						},
						error: function(data){
							obj = JSON.parse(data.responseText);
							console.log(obj.msg);
						}
					});
				});
				$('#back-list').click(function(){
					$('#appoint-page').toggle();
					$('#create-page').toggle();
				});
				$('#load-more').click(function(){
					Page.postList(true);
				});
				$('#create-appoint').click(function(){
					$('#appoint-page').toggle();
					$('#create-page').toggle();
				});
				$('#list-appoint').click(function(){
					dtPicker.show(function(item){
						Page.dateStr = item.y.text+'-'+item.m.text+'-'+item.d.text;
						console.log(Page.dateStr);
						Page.date = DateUtils.Date.fromString(Page.dateStr,'yyyy-MM-dd');
						Page.postList();
					});
				});
				
			}(Page={}));
		</script>
	</body>
</html>