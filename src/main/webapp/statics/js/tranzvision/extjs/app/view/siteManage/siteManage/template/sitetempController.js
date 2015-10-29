Ext.define('KitchenSink.view.siteManage.siteManage.template.sitetempController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.siteTemplateInfoI', 
	onFormSave: function(btn){
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
		var tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_GD_MBSZ_STD","OperateType":"U","comParams":{'+comParams+'}}';

		var comView = this.getView();

		Ext.tzSubmit(tzParams,function(responseData){
			comView.actType = "update";
			if(actType == "add" && btn != "but_ensure"){
				var templateId = responseData.templateId;
				form.findField('templateId').setValue(templateId);
			}
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
			var templateGrid = contentPanel.child("siteTemplateInfoGL").child("form").child("tabpanel").getActiveTab();
			templateGrid.store.reload();
		},"",true,this);
	},
	onFormEnsure: function(){
		this.onFormSave("but_ensure");
		this.getView().close();
	},
	onFormClose: function(){
		this.getView().close();
	}
});