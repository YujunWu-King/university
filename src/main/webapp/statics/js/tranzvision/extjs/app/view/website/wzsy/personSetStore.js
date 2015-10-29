Ext.define('KitchenSink.view.website.wzsy.personSetStore', {
	extend: 'Ext.data.Store',
	alias: 'store.personSetStore',
	model: 'KitchenSink.view.website.wzsy.personSetModel',
	comID: 'TZ_SITE_PERSON_COM',
	pageID: 'TZ_SITE_PERSON_STD',
	pageSize: 100,
	autoLoad: true,
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});