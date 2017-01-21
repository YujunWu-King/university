Ext.define('KitchenSink.view.siteManage.siteManage.area.siteAreaController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.siteAreaInfo1', 
	onFormSave: function(btn){
		var form = this.getView().child("form").getForm();

		var actType = this.getView().actType;

		var comParams = "";
		
		var formData = form.getValues();
		
		//转换栏目值类型：array -> string
		formData["arealm"] = typeof formData["arealm"] =="object"?formData["arealm"].join(","):formData["arealm"];
		
		//新增
		if(actType == "add"){
			comParams = '"add":['+Ext.JSON.encode(formData)+']';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			editJson = Ext.JSON.encode(formData);
		}
		
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_ZD_QYSZ_STD","OperateType":"U","comParams":{'+comParams+'}}';

		var comView = this.getView();

		Ext.tzSubmit(tzParams,function(responseData){
			comView.actType = "update";
			if(actType == "add" && btn != "but_ensure"){
				var areaId = responseData.areaId;
				form.setValues({areaid:areaId});
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