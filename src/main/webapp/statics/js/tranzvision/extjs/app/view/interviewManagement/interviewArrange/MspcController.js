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
	//关闭面试日程安排设置页面
	onWindowClose: function(btn){
		var win = btn.findParentByType("window");
		win.close();
	}
});