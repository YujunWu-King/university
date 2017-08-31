Ext.define('KitchenSink.view.weChat.weChatMaterial.picAndWordStore',{
    extend: 'Ext.data.Store',
    alias: 'store.picAndWordStore',
    model: 'KitchenSink.view.weChat.weChatMaterial.picAndWordModel',
    autoLoad:false,
    comID: '',
    pageID: '',
    tzStoreParams:'',
    pageSize:1000,
    proxy: Ext.tzListProxy()
});