Ext.define('KitchenSink.view.weChat.weChatMaterial.picStore',{
    extend: 'Ext.data.Store',
    alias: 'store.picStore',
    model: 'KitchenSink.view.weChat.weChatMaterial.picModel',
    autoLoad:false,
    comID: '',
    pageID: '',
    tzStoreParams:'',
    pageSize:1000,
    proxy: Ext.tzListProxy()
});