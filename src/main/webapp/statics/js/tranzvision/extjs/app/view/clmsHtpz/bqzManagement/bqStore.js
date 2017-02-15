Ext.define('KitchenSink.view.clmsHtpz.bqzManagement.bqStore', {
    extend: 'Ext.data.Store',
    alias: 'store.bqStore',
    model: 'KitchenSink.view.clmsHtpz.bqzManagement.bqModel',
	comID: 'TZ_BIAOQZ_COM',
	pageID: 'TZ_BIAOQZ_DEFN_STD',
	tzStoreParams:  '{"bqzID":""}',
	pageSize: 5,
	proxy: Ext.tzListProxy()
});
