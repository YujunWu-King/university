Ext.define('KitchenSink.view.template.proTemplate.probackMsgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.probackMsgStore',
    model: 'KitchenSink.view.template.proTemplate.probackMsgModel',
	//autoLoad: true,
	pageSize: 10,
	comID: 'TZ_PM_BMLCMBGL_COM',
	pageID: 'TZ_GD_BACKMSG_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});