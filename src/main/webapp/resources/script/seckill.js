//存放主要交互逻辑的代码
//javascript模块化
//seckill.detail,init(params);

var seckill ={
		//封装秒杀相关的url
		URL:{
			now : function(){
				return '/seckill/seckill/time/now';
			},
			exposer: function(seckillId){
				return '/seckill/seckill/'+seckillId+'/exposer';
			},
			execution:function(seckillId,md5){
				return '/seckill/seckill/'+seckillId+'/'+md5+'/execution';
			}
		},
		//处理秒杀逻辑
		handleSeckillkill:function(seckillId,node){
			node.hide()//操作前先隐藏节点
			.html('<button type="button" id="killBtn" class="btn btn-primary btn-lg">开始秒杀 </button>'); 
			$.post(seckill.URL.exposer(seckillId),{},function(result){
				//在回调中处理，执行交互流程
				if(result && result['success']){
					var exposer = result['data'];
					if (exposer['exposed']){ 
						//开启秒杀
						//获取秒杀地址
						var md5 = exposer['md5'];
						var killUrl =seckill.URL.execution(seckillId, md5);
						node.show();
						//console.log("killUrl"+killUrl);
						//绑定一次单击响应事件
						$('#killBtn').one('click',function(){
							//1.先 禁用秒杀按钮
							$(this).addClass('disabled');
							//2.发送秒杀请求
							$.post(killUrl,{},function(result){
								if (result && result['success']){
									var killResult = result['data']; 
									var state = result['state'];
									var stateInfo = result['stateInfo'];
									//3. 显示秒杀结果
									node.html('<span class="label label-success" >'+stateInfo+'</span>')
								}else{
									console.log('result='+result);	
								}
							},'json');
						});
					}else{
						//未开启秒杀
						var now = exposer['now'];
						var start = exposer['start'];
						var end = exposer['end'];
						//重新进入计时逻辑
						seckill.countdown(seckillId, now, start, end);
						
					}
				}else{
					console.log('result='+result);
				}
				
			},'json');
		},
		//验证手机号
		validatePhone :function (phone) {
				if (phone && phone.length == 11 && !isNaN(phone) ){
					return true;
				}else{
					return false;
				}
		},
		//秒杀倒计时
		countdown : function (seckillId,nowTime,startTime,endTime){
			var seckillBox = $("#seckill-box");
			//时间判断
			if (nowTime > endTime){
				//秒杀结束
				seckillBox.html("秒杀结束!");
			}else if (nowTime < startTime){
				//计时时间
				var killTime = new Date(startTime*1 + 1000);
				seckillBox.countdown(killTime,function (event){
					//控制时间的格式
					var format = event.strftime('秒杀倒计时：%D天 %H时 %M分%S秒')
					seckillBox.html(format);
				}).on('finish.countdown',function(){
					//获取秒杀地址 ，控制实现逻辑， 执行秒杀 seckillId执行秒杀的id  和seckillBox节点
					seckill.handleSeckillkill(seckillId,seckillBox);
				});
			}else{
				//获取秒杀地址 控制显示逻辑 执行秒杀
				seckill.handleSeckillkill(seckillId,seckillBox);
			}
		},
		//详情页秒杀逻辑
		detail:{
			//详情页初始化
			init:function(params){
				//手机验证和登录 计时交互
				//规划我们的交互流程
				//在cookie 中查找秒杀手机号
				var killPhone = $.cookie("killPhone");
				//验证手机号
				if (!seckill.validatePhone(killPhone)){
					//绑定手机号
					//控制输出
					var  killPhoneModal = $("#killPhoneModal");
					killPhoneModal.modal({
					  	show : true,// 显示弹出层
					backdrop : false,// 禁止位置关闭
					keyboard : false // 关闭键盘事件
					});

					$("#killPhoneBtn").click(function(){
						var inputPhone = $("#killPhoneKey").val();
						if (seckill.validatePhone(inputPhone)){
							//将电话写入cookie中
							$.cookie('killPhone',inputPhone,{expires:70, padth:'/seckill'});
							//验证通过 刷新页面
							window.location.reload();
						}else{
							 //显示错误信息
							$("#killPhoneMessage").hide().html('<label	class="label label-danger">手机号错误！</label>').show(300);//先隐藏 300毫秒后显示 有动画效果	
						}
					});
				}
				//用户已经登录
				//计时交互
				//获取js格式的数据	
				var startTime = params['startTime'];
				var endTime = params['endTime'];
				var seckillId = params['seckillId'];
				$.get(seckill.URL.now(),{},function(result){
					if(result && result['success']){
						var nowTime = result['data'];
						//做出时间判断
						seckill.countdown(seckillId, nowTime, startTime, endTime);
					}else{
						cosole.log('result'+result);
					}
					
				},"json");
				
			}
		}
}