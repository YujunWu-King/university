Ext.define('KitchenSink.view.zsorganizationManagement.ZsJgMagListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.resourceStore',
    model: 'KitchenSink.view.zsorganizationManagement.ZsJgMagListModel',
    pageSize: 5,
    autoLoad:true,
    comID: 'TZ_ZSBF_JG_COM',
    pageID: 'TZ_ZSBF_LIST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_ZSBF_JG_COM.TZ_ZSBF_LIST_STD.TZ_JG_LINKEDIN_VW","condition":{}}',
    proxy: Ext.tzListProxy()
});
