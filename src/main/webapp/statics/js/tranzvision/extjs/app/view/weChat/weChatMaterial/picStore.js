Ext.define('KitchenSink.view.weChat.weChatMaterial.picStore',{
    extend: 'Ext.data.Store',
    alias: 'store.picStore',
    model: 'KitchenSink.view.weChat.weChatMaterial.picModel',
    autoLoad:false,
    comID: 'TZ_WX_SCGL_COM',
    pageID: 'TZ_WX_TPSC_STD',
   // tzStoreParams:'',
    proxy: Ext.tzListProxy()
});