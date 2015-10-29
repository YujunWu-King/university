Ext.define('KitchenSink.view.tzLianxi.ytt.bugCk.bugStore',{
    extend:'Ext.data.Store',
    model:'KitchenSink.view.tzLianxi.ytt.bugCk.bugModel',
    alias:"store.bugStore",
    autoLoad: true,
    pageSize:10,
    comID:"LX_YTT_BUG_CK_COM",
    pageID:"LX_BUG_LIST_STD1",
    tzStoreParams:'{"cfgSrhId": "LX_YTT_BUG_CK_COM.LX_BUG_LIST_STD1.YTT_BUGMG_DFN_V"}',
    proxy:Ext.tzListProxy()
});
