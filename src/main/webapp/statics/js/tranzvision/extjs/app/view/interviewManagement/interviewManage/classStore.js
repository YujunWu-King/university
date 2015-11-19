Ext.define('KitchenSink.view.interviewManagement.interviewManage.classStore', {
    extend: 'Ext.data.Store',
    alias: 'store.msMgrClassStore',
    model: 'KitchenSink.view.interviewManagement.interviewManage.classModel',
	autoLoad: true,
    pageSize: 10,
    comID: 'TZ_MS_MGR_COM',
    pageID: 'TZ_MS_MGR_CLS_STD',
    tzStoreParams: '{"cfgSrhId": "TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.TZ_CLASS_OPR_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'+TranzvisionMeikecityAdvanced.Boot.loginUserId+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy(),
    sorters:[
        {
            direction:'DESC',
            property:'classID'
        }
    ]
});
