Ext.define('KitchenSink.view.enrollProject.submitDtMdlMg.backMsgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.backMsgStore',
    model: 'KitchenSink.view.enrollProject.submitDtMdlMg.backMsgModel',
	//autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_SMTDTMDL_COM',
	pageID: 'TZ_GD_BAKEMSG_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});