Ext.define('KitchenSink.view.tzLianxi.ytt.bugMg.bugStore',{
    extend:'Ext.data.Store',
    model:'KitchenSink.view.tzLianxi.ytt.bugMg.bugModel',
    alias:"store.bugStore",
    autoLoad: true,
    pageSize:10,
    comID:"LX_YTT_BUG_MG_COM",
    pageID:"LX_BUG_LIST_STD",
    tzStoreParams:'{"cfgSrhId": "LX_YTT_BUG_MG_COM.LX_BUG_LIST_STD.YTT_BUGMG_DFN_V"}',
    proxy:Ext.tzListProxy()
});
