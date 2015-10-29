Ext.define('KitchenSink.view.sendEmailAndSMS.paraMg.paraInfoMth', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.paraInfoMth', 
	onFormSave: function(){
	
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var tzParams = this.getParaInfoParams();
			
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
				form.findField("paraid").setReadOnly(true);
				form.findField("paraid").addCls("lanage_1");
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("paraMg").store.reload();
			},"",true,this);
		}
	},
	onFormEnsure: function(){
		//获取窗口
		var comView = this.getView();
		//信息表单
		var form = comView.child("form").getForm();
		if (form.isValid()) {
			/*保存页面信息*/
			var tzParams = this.getParaInfoParams();

			Ext.tzSubmit(tzParams,function(responseData){
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("paraMg").store.reload();
				comView.actType = "update";	
				//重置表单
				form.reset();
				//关闭窗口
				comView.close();

			},"",true,this);

		}
	},
	onFormClose: function(){
		this.getView().close();
	},
	getParaInfoParams: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";
		
		//新增
		if(actType == "add"){
			
			comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			editJson = Ext.JSON.encode(form.getValues());
		}
		
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_PARA_MG_COM","PageID":"TZ_PARA_SET_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	}
	
	
});