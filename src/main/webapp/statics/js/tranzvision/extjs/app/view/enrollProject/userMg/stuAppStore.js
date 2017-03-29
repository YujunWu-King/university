Ext.define('KitchenSink.view.enrollProject.userMg.stuAppStore', {
    extend: 'Ext.data.Store',
    alias: 'store.stuAppStore',
    model: 'KitchenSink.view.enrollProject.userMg.stuAppModel',
	autoLoad: false,
	pageSize: 20,
	comID: 'TZ_UM_USERMG_COM',
	pageID: 'TZ_UM_STUAPPL_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
