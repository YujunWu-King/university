Ext.define('KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfoMth', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.emailServerInfoMth', 
	onFormSave: function(btn){
		//组件注册表单
		
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var tzParams = this.getEmlSerInfoParams();
			
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
				form.findField("emailservid").setReadOnly(true);
				form.findField("emailorg").setReadOnly(true);
				form.findField("emailservid").addCls("lanage_1");
				form.findField("emailorg").addCls("lanage_1");

				//form.findField("emailservid").setValue(responseData.emailservid);//修改保存数据失败
				form.setValues({emailservid:responseData.emailservid});
				
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("emailSet").store.reload();
			},"",true,this);
		}
	},
	onFormEnsure: function(){
		//获取窗口
		var win = this.getView();
		//信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面信息*/
			var tzParams = this.getEmlSerInfoParams();	
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("emailSet").store.reload();
				comView.actType = "update";	
				//重置表单
				form.reset();
				//关闭窗口
				win.close();

			},"",true,this);

		}		
	},
	onFormClose: function(){
		this.getView().close();
	},
	getEmlSerInfoParams: function(){
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
		var tzParams = '{"ComID":"TZ_EMLSER_MG_COM","PageID":"TZ_EMLSER_SET_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	}
});