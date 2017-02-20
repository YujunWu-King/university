Ext.define('KitchenSink.view.distributionTable.distributionDetailStore', {
    extend: 'Ext.data.Store',
    alias: 'store.distributionDetailStore',
    model: 'KitchenSink.view.distributionTable.distributionDetailModel',
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_DISTRI_TAB_COM',
	pageID: 'TZ_DISTRI_INFO_STD ',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
