Ext.define('KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlStore', {
    extend: 'Ext.data.Store',
    alias: 'store.transStore',
    model: 'KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlModel',
	autoLoad: true,
    pageSize: 10,
	comID: 'TZ_GD_SMTDTMDL_COM',
	pageID: 'TZ_GD_SMTDTLST_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
