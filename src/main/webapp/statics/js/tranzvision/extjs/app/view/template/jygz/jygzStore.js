Ext.define('KitchenSink.view.template.jygz.jygzStore', {
    extend: 'Ext.data.Store',
    alias: 'store.jygzStore',
    model: 'KitchenSink.view.template.jygz.jygzModel',
    pageSize:10,
    autoLoad:true,
    comID: 'TZ_JYGZ_COM',
    pageID: 'TZ_JYGZ_LIST_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_JYGZ_COM.TZ_JYGZ_LIST_STD.TZ_JYGZ_VW"}',
    proxy: Ext.tzListProxy()
});
