Ext.define('KitchenSink.view.template.sitetemplate.column.siteLMController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.siteLMInfo', 
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
		var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_GD_ZDLM_STD","OperateType":"U","comParams":{'+comParams+'}}';

		var comView = this.getView();

		Ext.tzSubmit(tzParams,function(responseData){
			comView.actType = "update";
			if(actType == "add" && btn != "but_ensure"){
				var templateId = responseData.lm_id;
				form.findField('lm_id').setValue(templateId);
			}
			
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
			var columnGrid = contentPanel.child("siteTemplateInfo").child("form").child("tabpanel").getActiveTab();
			columnGrid.store.reload();
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