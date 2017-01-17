Ext.define('KitchenSink.view.interviewManagement.classManage.classStore', {
    extend: 'Ext.data.Store',
    alias: 'store.msArrClassStore',
    model: 'KitchenSink.view.interviewManagement.classManage.classModel',
	autoLoad: true,
    pageSize: 10,
    comID: 'TZ_MS_ARR_MG_COM',
    pageID: 'TZ_MSGL_CLASS_STD',
    tzStoreParams: '{"cfgSrhId": "TZ_MS_ARR_MG_COM.TZ_MSGL_CLASS_STD.TZ_CLASS_OPR_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'+TranzvisionMeikecityAdvanced.Boot.loginUserId+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy(),
	/*
    sorters:[
        {
            direction:'DESC',
            property:'classID'
        }
    ]*/
});
