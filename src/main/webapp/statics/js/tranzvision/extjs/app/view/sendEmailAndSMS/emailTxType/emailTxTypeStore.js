Ext.define('KitchenSink.view.sendEmailAndSMS.emailTxType.emailTxTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.emailTxTypeStore',
    model: 'KitchenSink.view.sendEmailAndSMS.emailTxType.emailTxTypeModel',
	autoLoad: true,
	pageSize: 20,
	comID: 'TZ_EMLTX_TYPE_COM',
    pageID: 'TZ_EMLTX_TYPE_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.TZ_TX_TYPE_TBL","condition":{}}',
	proxy: Ext.tzListProxy()
});
