Ext.define('KitchenSink.view.sendEmailAndSMS.smsServer.smsServerInfoMth', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.smsServerInfoMth',
	/*********************************************************
	+功能描述：保存短信服务器配置信息 							+
	+开发人：韩守玉											+
	********************************************************/
	
	loadSmsServInfo: function() {
			
			//表单信息;
			var form = this.getView().child("form").getForm();
			//参数
			var tzParams = '{"ComID":"TZ_SMSSER_MG_COM","PageID":"TZ_SMSSER_STD","OperateType":"QF","comParams":{}}';
			
			Ext.tzLoad(tzParams,function(responseData){
					//组件注册信息数据
					//console.log(responseData);
					//var formData = responseData.formData;
					form.findField("smssevid").setReadOnly(true);
					form.setValues(responseData);							
				});
    },
	
	onFormSave: function(){
	
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			
			var tzParams = this.getSmsSerInfoParams();
			
			Ext.tzSubmit(tzParams,function(responseData){

			},"",true,this);
		}
	},
	/*********************************************************
	+功能描述：保存配置信息并关闭窗口 							+
	+开发人：韩守玉											+
	********************************************************/
	onFormEnsure: function(){
		//获取窗口
		var win = this.getView();
		//信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面信息*/
			this.onFormSave();
			//重置表单
			form.reset();
			//关闭窗口
			win.close();
		}		
	},
	/*********************************************************
	+功能描述：关闭窗口 										+
	+开发人：韩守玉											+
	********************************************************/
	onFormClose: function(){
		this.getView().close();
	},
	getSmsSerInfoParams: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";
	
		//修改json字符串
		var editJson = "";
		
		editJson = Ext.JSON.encode(form.getValues());
		
		if(editJson != ""){
			comParams = '"update":[' + editJson + "]";
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_SMSSER_MG_COM","PageID":"TZ_SMSSER_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	}
});