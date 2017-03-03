Ext.define('KitchenSink.view.classManage.Pcgl.PcglController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.PcglDetail', 
	onFormSave: function(btn){
		var me=this;
		var form = this.getView().child("form").getForm();
		//判断必填		
		if(form.isValid()==false){
			return;
		}
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
		}
		var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_PCGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
		var comView = this.getView();

		Ext.tzSubmit(tzParams,function(responseData){
			comView.actType = "update";
			if(actType == "add"){
				var templateId = responseData.pc_id;
				form.setValues({pc_id:templateId});
			}
            var templateGrid = comView.findParentByType('ClassInfo').lookupReference('pcgl_grid');
			templateGrid.store.reload();
		},"",true,this);
	},
    onFormEnsure:function(btn){
        this.onFormSave(btn);
        this.getView().close();
    },
	onFormClose: function(){
		this.getView().close();
	}
});