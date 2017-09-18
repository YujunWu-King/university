Ext.define('KitchenSink.view.weChat.weChatUser.weChatTagListStore',{
    extend: 'Ext.data.Store',
    alias: 'store.weChatTagListStore',
    model: 'KitchenSink.view.weChat.weChatUser.weChatTagListModel',
    autoLoad:false,
    comID: 'TZ_WX_USER_COM',
    pageID: 'TZ_WX_TAG_STD',
    tzStoreParams:'',
    pageSize:1000,
    proxy: Ext.tzListProxy()
});