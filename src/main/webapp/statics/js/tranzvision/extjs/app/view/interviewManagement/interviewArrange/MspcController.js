Ext.define('KitchenSink.view.interviewManagement.interviewArrange.MspcController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.MspcController',
	onFormEnsure: function(){
		var comView = this.getView();
		var form = comView.child("form").getForm();
		if (form.isValid()) {
			comView.close();
		}	
	},
	onFormClose: function(){
		this.getView().close();
	},
	//确认面试日程安排设置页面
	onWindowEnsure1: function(btn){
		var form = this.getView().child("form").getForm();
		var comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		if (form.isValid()) {
			var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_SJ_SET_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData){
				var win = btn.findParentByType("window");
				win.close();
				var msArrPanel = win.up('panel');
				var msArrForm = msArrPanel.child('form');
				var msArrGrid = msArrForm.child('grid');
				var msArrFormRec = msArrForm.getForm().getFieldValues();
				var msArrFormclassID = msArrFormRec["classID"];
				var msArrFormbatchId = msArrFormRec["batchID"];
				Params= '{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'"}';
				msArrGrid.store.tzStoreParams = Params;
				msArrGrid.store.reload();
			},"",true,this);
		}else{
			Ext.Msg.alert("提示","请填写必填项");
		}
	},
	
	onMsPlanSaveEnsure: function(btn){
		var win = btn.findParentByType('interviewArrPlanSet');
		var form = win.child('form').getForm();
		var actType = win.actType;
		if (form.isValid()) {
			var comParamsObj = {};
			if(actType == "A"){
				comParamsObj.add = [{dataType:"msPlan", data:form.getValues()}];
			}else{
				comParamsObj.update = [form.getValues()];
			}
			
			var tzParamsObj = {
				ComID: 	"TZ_MS_ARR_MG_COM",
				PageID:	"TZ_MSJH_SET_STD",
				OperateType: "U",
				comParams: comParamsObj
			};
			
			var tzParams = Ext.JSON.encode(tzParamsObj);
			Ext.tzSubmit(tzParams,function(respData){
				if(respData.result == "success"){
					if(actType == "A"){
						form.setValues(respData);
						win.actType = "U";
					}
					
					if(win.msArrGrid){
						win.msArrGrid.getStore().reload();
					}
					
					win.close();
				}
			},"保存成功",true,this);
		}
	},
	
	//关闭面试日程安排设置页面
	onWindowClose: function(btn){
		var win = btn.findParentByType("window");
		win.close();
	}
});