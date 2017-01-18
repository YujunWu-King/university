Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewAddStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewStuViewAddStore',
    model: 'KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewModel',
	autoLoad: true,
    pageSize: 10,
    comID: 'TZ_MS_ARR_MG_COM',
    pageID: 'TZ_MSYY_ADD_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy(),
    
    constructor: function(config){
		var classID = config.classID;
		var batchID = config.batchID;

		this.tzStoreParams = '{"cfgSrhId": "TZ_MS_ARR_MG_COM.TZ_MSYY_ADD_STD.TZ_MS_STU_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "'+classID+'","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value": "'+batchID+'"}}';
		this.callParent();	
	},
});
