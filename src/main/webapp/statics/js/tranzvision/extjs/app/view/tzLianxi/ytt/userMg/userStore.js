Ext.define("KitchenSink.view.tzLianxi.ytt.userMg.userStore",{
    extend : "Ext.data.Store",
    alias:'store.userStore',
    model : "KitchenSink.view.tzLianxi.ytt.userMg.userModel",
    autoLoad:true,
    pageSize:10,
    comID:"LX_YTT_BUGUSER_COM",
    pageID:"LX_YTT_BUGUSER_STD",
    tzStoreParams:'{"cfgSrhId": "LX_YTT_BUGUSER_COM.LX_YTT_BUGUSER_STD.YTT_BUGMG_PER_V"}',
    proxy:Ext.tzListProxy()
});