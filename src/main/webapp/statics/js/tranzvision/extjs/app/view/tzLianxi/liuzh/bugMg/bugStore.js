Ext.define('KitchenSink.view.tzLianxi.liuzh.bugMg.bugStore',{
 extend:'Ext.data.Store',
 model:'KitchenSink.view.tzLianxi.liuzh.bugMg.bugModel',
 alias:"store.bugStore",
 autoLoad: true,
 pageSize:10,
 comID:"LX_LZH_BUGMG_COM",
 pageID:"LX_BUG_LIST_STD",
 tzStoreParams:'{"cfgSrhId": "LX_LZH_BUGMG_COM.LX_BUG_LIST_STD.TZ_WY_BUG_VW"}',
 proxy:Ext.tzListProxy()
 });

