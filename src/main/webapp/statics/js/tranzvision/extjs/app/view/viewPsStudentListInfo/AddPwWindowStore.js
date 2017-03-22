Ext.define('KitchenSink.view.viewPsStudentListInfo.AddPwWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.addpwstore',
    model: 'KitchenSink.view.viewPsStudentListInfo.AddPwWindowModel',
    pageSize:10,
    autoLoad:true,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_JUDGES_STD',
    tzStoreParams:'{"cfgSrhId":"TZ_REVIEW_MS_COM.TZ_MSPS_JUDGES_STD.TZ_MSJU_REL_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "' + Ext.tzOrgID+ '"}}',
			
    proxy: Ext.tzListProxy()
});