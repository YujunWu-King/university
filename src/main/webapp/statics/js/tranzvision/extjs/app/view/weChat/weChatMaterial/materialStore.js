Ext.define('KitchenSink.view.weChat.weChatMaterial.materialStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialStore',
    model: 'KitchenSink.view.weChat.weChatMaterial.materialModel',
    autoLoad:false,
    comID: 'TZ_WX_SCGL_COM',
	pageID: 'TZ_WX_SCGL_STD',
	tzStoreParams: '',
    pageSize: 10, 
    proxy: Ext.tzListProxy()
});