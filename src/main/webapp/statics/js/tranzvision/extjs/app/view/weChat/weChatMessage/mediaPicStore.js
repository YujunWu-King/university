Ext.define('KitchenSink.view.weChat.weChatMessage.mediaPicStore', {
    extend: 'Ext.data.Store',
    alias: 'store.mediaPicStore',
    model: 'KitchenSink.view.weChat.weChatMessage.mediaPicModel',
    autoLoad:false,
    comID: 'TZ_GD_WXMSG_COM',
	pageID: 'TZ_GD_SCGL_STD',
	tzStoreParams: '',
    pageSize: 10, 
    proxy: Ext.tzListProxy()
});