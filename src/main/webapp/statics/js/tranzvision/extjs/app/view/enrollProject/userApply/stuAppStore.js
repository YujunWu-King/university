Ext.define('KitchenSink.view.enrollProject.userApply.stuAppStore', {
    extend: 'Ext.data.Store',
    alias: 'store.stuAppStore',
    model: 'KitchenSink.view.enrollProject.userApply.stuAppModel',
	autoLoad: false,
	pageSize: 20,
	comID: 'TZ_UM_USERMG_COM',
	pageID: 'TZ_UM_STUAPPL_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
