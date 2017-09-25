Ext.define('KitchenSink.view.weChat.weChatMaterial.materialStore', {
    extend: 'Ext.data.Store',
    alias: 'store.materialStore',
    model: 'KitchenSink.view.weChat.weChatMaterial.materialModel',
    autoLoad:false,
    comID: 'TZ_WX_SCGL_COM',
	pageID: 'TZ_WX_SCGL_STD',
	tzStoreParams:'{"cfgSrhId": "TZ_WX_SCGL_COM.TZ_WX_SCGL_STD.TZ_WX_MEDIA_VW","condition":{}}',
    pageSize: 10, 
    proxy: Ext.tzListProxy()
});