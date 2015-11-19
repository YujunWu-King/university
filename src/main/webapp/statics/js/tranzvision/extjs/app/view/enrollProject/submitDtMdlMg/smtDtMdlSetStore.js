Ext.define('KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlSetStore', {
    extend: 'Ext.data.Store',
    alias: 'store.smtDtMdlSetStore',
    model: 'KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlSetModel',
	//autoLoad: true,
	pageSize: 50,		
	comID: 'TZ_GD_SMTDTMDL_COM',
	pageID: 'TZ_GD_SMTDTSET_STD',
    tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
