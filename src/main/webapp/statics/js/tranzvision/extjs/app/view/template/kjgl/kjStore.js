Ext.define('KitchenSink.view.template.kjgl.kjStore', {
    extend: 'Ext.data.Store',
    alias: 'store.kjStore',
    model: 'KitchenSink.view.template.kjgl.kjModel',
    pageSize:10,
    autoLoad:true,
    comID: 'TZ_KJGL_COM',
    pageID: 'TZ_KJGL_LIST_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_KJGL_COM.TZ_KJGL_LIST_STD.TZ_KJ_VW"}',
    proxy: Ext.tzListProxy()
});
