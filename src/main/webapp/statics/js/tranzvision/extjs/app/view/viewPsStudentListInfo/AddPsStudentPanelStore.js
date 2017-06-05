Ext.define('KitchenSink.view.viewPsStudentListInfo.AddPsStudentPanelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.mspsksstore',
    model: 'KitchenSink.view.viewPsStudentListInfo.AddPsStudentPanelModel',
    pageSize:2000,
    autoLoad:true,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_ADDKS_STD',     
    tzStoreParams: '{"cfgSrhId":"TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.TZ_CLPS_KSH_VW","condition":{}}',
    proxy: Ext.tzListProxy()
});