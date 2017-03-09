Ext.define('KitchenSink.view.interviewManagement.interviewArrange.expMsArrToExcelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.expMsArrToExcelStore',
    model: 'KitchenSink.view.interviewManagement.interviewArrange.expMsArrToExcelModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_MS_ARR_MG_COM',
    pageID: 'TZ_MS_ARR_EXP_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_MS_ARR_MG_COM.TZ_MS_ARR_EXP_STD.TZ_GD_MSARRDC_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'+ TranzvisionMeikecityAdvanced.Boot.loginUserId+'"}}',
    proxy: Ext.tzListProxy()
});
