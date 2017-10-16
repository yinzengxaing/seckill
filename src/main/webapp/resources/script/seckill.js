//存放主要交互逻辑的代码
//javascript模块化
//seckill.detail,init(params);

var seckill ={
		//封装秒杀相关的url
		URL:{
			
		},
		//验证信息
		validatePhone :function (phone) {
				if (phone && phone.length == 11 && !isNaN(phone) ){
					return true;
				}else{
					return false;
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
				//获取js格式的数据	
				var startTime = params['startTime'];
				var endTime = params['endTime'];
				var seckillId = params['seckillId'];
				//验证手机号
				if (!seckill.validatePhone(killPhone)){
					//绑定手机号
					//控制输出
					var  killPhoneModal = $("#killPhoneModal");
					killPhoneModal.modal({
					  	show : true,// 显示弹出层
					backdrop : false,// 禁止位置关闭《《《《《《----这里把static改为false
					keyboard : false // 关闭键盘事件
					});

					$("#killPhoneBtn").click(function(){
						var inputPhone = $("#killPhoneKey").val();
						if (seckill.validatePhone(inputPhone)){
							//将电话写入cookie中
							$.cookie('killPhone',inputPhone,{expires:7, padth:'/seckill'});
							//验证通过 刷新页面
							window.location.reload();
						}else{
							 //显示错误信息
							$("#killPhoneMessage").hide().html('<label	class="label label-danger">手机号错误！</label>').show(300);//先隐藏 300毫秒后显示 有动画效果	
						}
					});
				}
			}
		}
}