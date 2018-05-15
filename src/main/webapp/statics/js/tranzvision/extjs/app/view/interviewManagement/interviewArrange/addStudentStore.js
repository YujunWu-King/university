Ext.define('KitchenSink.view.interviewManagement.interviewArrange.addStudentStore', {
	extend: 'Ext.data.Store',
    alias: 'store.addStudentStore',
    model: 'KitchenSink.view.interviewManagement.interviewArrange.addStudentModel',
    autoLoad: true,
    pageSize: 200,
    comID: 'TZ_MS_ARR_MG_COM',
    pageID: 'TZ_MS_ARR_ASTU_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy(),
    constructor: function(config){
		var classID = config.classID;
		var batchID = config.batchID;

		//this.tzStoreParams = '{"cfgSrhId":"TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.TZ_MSSZ_STU_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "'+ classID +'"}}';
		this.tzStoreParams = '{"classID":"'+ classID +'","batchID":"'+ batchID +'","cfgSrhId":"TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.TZ_MSSZ_STU_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}';
		
		this.callParent();	
	},
});