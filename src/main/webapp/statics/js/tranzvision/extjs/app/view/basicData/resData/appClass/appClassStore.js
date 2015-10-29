Ext.define('KitchenSink.view.basicData.resData.appClass.appClassStore', {
    extend: 'Ext.data.Store',
    alias: 'store.appClassStore',
    model: 'KitchenSink.view.basicData.resData.appClass.appClassModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_APP_CLS_COM',
		pageID: 'TZ_APP_CLSLIST_STD',
		tzStoreParams: '{"cfgSrhId":"TZ_APP_CLS_COM.TZ_APP_CLSLIST_STD.TZ_APPCLS_TBL"}',
		proxy: Ext.tzListProxy()
});
