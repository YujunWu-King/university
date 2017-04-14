Ext.define('KitchenSink.view.autoScoringRulerManagement.autoScoringRulerListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.resourceStore',
    model: 'KitchenSink.view.autoScoringRulerManagement.autoScoringRulerListModel',
    pageSize: 10,
    autoLoad:true,
    comID: 'TZ_ZDCS_DFGZ_COM',
    pageID: 'TZ_DFGZ_LIST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.TZ_ZDCS_DFGZ_T","condition":{}}',
    proxy: Ext.tzListProxy()
});
