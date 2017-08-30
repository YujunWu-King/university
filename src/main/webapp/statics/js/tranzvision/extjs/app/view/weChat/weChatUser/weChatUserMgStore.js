Ext.define('KitchenSink.view.weChat.weChatUser.weChatUserMgStore',{
    extend: 'Ext.data.Store',
    alias: 'store.weChatUserMgStore',
    model: 'KitchenSink.view.weChat.weChatUser.weChatUserMgModel',
    autoLoad:true,
    comID: 'TZ_WX_USER_COM',
    pageID: 'TZ_WX_USER_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_WX_USER_COM.TZ_WX_USER_STD.TZ_WX_USER_VW","condition":{}}',
    pageSize:20,
    proxy: Ext.tzListProxy()
});