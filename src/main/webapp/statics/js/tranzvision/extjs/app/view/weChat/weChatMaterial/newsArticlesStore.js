Ext.define('KitchenSink.view.weChat.weChatMaterial.newsArticlesStore',{
    extend: 'Ext.data.Store',
    alias: 'store.picStore',
    model: 'KitchenSink.view.weChat.weChatMaterial.newsArticlesModel',
    autoLoad:false,
    comID: 'TZ_WX_SCGL_COM',
    pageID: 'TZ_WX_TWSC_STD',
    tzStoreParams:'',
    pageSize:100,
    proxy: Ext.tzListProxy()
});