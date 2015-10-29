Ext.define('KitchenSink.view.basicData.resData.message.msgDefineController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.msgDefineController', 
	//保存数据
    onFormSave:function(btn){
    	//消息表单
		var form = this.getView().child("form").getForm();
		if(form.isValid()){
			//消息定义标志
			var actType = this.getView().actType;
	
			//更新操作参数
			var comParams = "";
			//新增
			if(actType == "add"){
				comParams = '"add":[' + Ext.JSON.encode(form.getValues()) + ']';
			}
			//修改json字符串
			var editJson = "";
			if(actType == "update"){
				editJson = Ext.JSON.encode(form.getValues());
			}
			if(editJson != ""){
				if(comParams == ""){
					comParams = '"update":[' + editJson + ']';
				}else{
					comParams = comParams + ',"update":[' + editJson + ']';
				}
			}
			//提交参数
			var tzParams = '{"ComID":"TZ_GD_MESSAGE_COM","PageID":"TZ_GD_MSGDEF_STD","OperateType":"U","comParams":{'+comParams+'}}';
			var comView = this.getView();
            var store = this.getView().findParentByType("messageInfo").lookupReference('msgInfoListGrid').store;
			var panel = this.getView().findParentByType("messageInfo");
			
			Ext.tzSubmit(tzParams,function(responseData){
				if (comView.actType = "add") {
					var orgId = panel.orgId;
					var msgLanage = panel.lanageType;
					var msgSetID = form.findField("msgSetID").getValue();
                    var tzStoreParams = '{"cfgSrhId": "TZ_GD_MESSAGE_COM.TZ_GD_MSGINFO_STD.TZ_PT_XXDY_VW","condition":{"TZ_XXJH_ID-operator": "01","TZ_XXJH_ID-value": "'+msgSetID+'","TZ_LANGUAGE_ID-operator": "01","TZ_LANGUAGE_ID-value": "'+msgLanage+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+orgId+'"}}';
					store.tzStoreParams=tzStoreParams;
				}
				comView.actType = "update";
				if(btn != "but_ensure"){
					form.findField("msgId").setReadOnly(true);
					form.findField("msgId").addCls("lanage_1");
				}
                store.reload();
			},"",true,this);
            return true;
		};
        return false;
    },
    onFormEnsure:function(btn){
        if(this.onFormSave("but_ensure")){
            this.getView().close();
        };
    },
    onFormClose:function(btn){
    	this.getView().close();
    }
});