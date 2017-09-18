Ext.define('KitchenSink.view.signIn.signStore', {
    extend: 'Ext.data.Store',
    alias: 'store.cardStore',
    model: 'KitchenSink.view.signIn.signModel',
    autoLoad: true,
    pageSize: 30,
    comID:'TZ_QDGL_COM',
    pageID:'TZ_QDGL_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_QDGL_COM.TZ_QDGL_STD.SIGN_INFO","condition":{"TZ_JG_ID-operator": "01"}}',
    proxy: Ext.tzListProxy()
})