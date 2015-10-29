Ext.define('KitchenSink.view.template.user.dropBoxSetStore', {
    extend: 'Ext.data.Store',
    alias: 'store.dropBoxSetStore',
    model: 'KitchenSink.view.template.user.dropBoxSetModel',
    comID: 'TZ_USER_REG_COM',
    pageID: 'TZ_DROP_BOX_STD',
    tzStoreParams: '',
    pageSize: 5,
    autoLoad: true,
    proxy: Ext.tzListProxy()
});