Ext.define('KitchenSink.view.qklZsmb.zsmbListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.zsmbListStore',
    model:'KitchenSink.view.qklZsmb.zsmbListModel',
	autoLoad: true,
    pageSize: 10,
    comID: 'TZ_ZHENGSHU_COM',
    pageID: 'TZ_MOBAN_LIST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_ZHENGSHU_COM.TZ_MOBAN_LIST_STD.TZ_CERTTMPL_V","condition":{}}',
    proxy: Ext.tzListProxy()
});