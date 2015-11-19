Ext.define('KitchenSink.view.enrollProject.projectMg.Zyfx.ZyfxWindowsController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.ZyfxWindowsController',
	onFormSave: function(btn){
		var form = this.getView().child("form").getForm();
		var actType = this.getView().actType;
		var comParams = "";
		if(actType == "add"){
			comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		}
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
		};

		var comView = this.getView();
		var templateGridStore = comView.findParentByType('projectInfo').lookupReference('professionGrid').store;
		if(actType=='add'&&templateGridStore.find("pro_zyfx_id",form.findField('pro_zyfx_id').getValue(),0,false,true,false)>-1){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_ZYFX_STD.prompt","提示"),Ext.tzGetResourse("TZ_PRJ_PROMG_COM.TZ_PRJ_ZYFX_STD.zyfxidycz","专业方向ID已经存在"));
			return;
		}

		var tzParams = '{"ComID":"TZ_PRJ_PROMG_COM","PageID":"TZ_PRJ_ZYFX_STD","OperateType":"U","comParams":{'+comParams+'}}';

		if(form.isValid()){
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";
				if(actType == "add"){
					form.findField('pro_zyfx_id').setReadOnly(true);
				}
				templateGridStore.reload();
			},"",true,this);
		}
	},
	onFormEnsure: function(btn){
	 this.onFormSave(btn);
	 this.getView().close();
	 },
	onFormClose: function(){
		this.getView().close();
	}
});