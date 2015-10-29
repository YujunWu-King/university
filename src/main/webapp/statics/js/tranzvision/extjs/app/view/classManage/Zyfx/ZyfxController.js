Ext.define('KitchenSink.view.classManage.Zyfx.ZyfxController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ZyfxDetail', 
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
        var templateGridStore = comView.findParentByType('ClassInfo').lookupReference('Zyfx_Grid').store;
        if(actType=='add'&&templateGridStore.find("fx_id",form.findField('fx_id').getValue(),0,false,true,false)>-1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.zyfxidycz","专业方向ID已经存在"));
            return;
        }

		var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_ZYFX_STD","OperateType":"U","comParams":{'+comParams+'}}';

        if(form.isValid()){
            Ext.tzSubmit(tzParams,function(responseData){
                comView.actType = "update";
                if(actType == "add" && btn != "but_ensure"){
                    form.findField('fx_id').setReadOnly(true);
                }
                templateGridStore.reload();
            },"",true,this);
        }
	},
	onFormEnsure: function(){
		this.onFormSave("but_ensure");
		this.getView().close();
	},
	onFormClose: function(){
		this.getView().close();
	}
});