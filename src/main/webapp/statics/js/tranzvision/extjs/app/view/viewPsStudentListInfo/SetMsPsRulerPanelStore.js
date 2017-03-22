Ext.define('KitchenSink.view.viewPsStudentListInfo.SetMsPsRulerPanelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.viewpwstore',
    model: 'KitchenSink.view.viewPsStudentListInfo.SetMsPsRulerPanelModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_RULE_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});