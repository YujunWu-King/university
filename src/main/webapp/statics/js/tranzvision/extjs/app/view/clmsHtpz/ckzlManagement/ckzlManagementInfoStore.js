Ext.define('KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.ckzlManagementInfoStore',
    model: 'KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementInfoModel',
	comID: 'TZ_ZDCS_CKZL_COM',
	pageID: 'TZ_CKZL_MG_STD',
	tzStoreParams:  '{"ckzlid":""}',
	pageSize: 5,
	proxy: Ext.tzListProxy()
});
