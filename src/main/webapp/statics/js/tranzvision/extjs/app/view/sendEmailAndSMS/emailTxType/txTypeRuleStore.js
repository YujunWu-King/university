Ext.define('KitchenSink.view.sendEmailAndSMS.emailTxType.txTypeRuleStore', {
    extend: 'Ext.data.Store',
    alias: 'store.txTypeRuleStore',
    model: 'KitchenSink.view.sendEmailAndSMS.emailTxType.txTypeRuleModel',
	autoLoad: false,
	pageSize: 100,
	comID: 'TZ_EMLTX_TYPE_COM',
    pageID: 'TZ_TXTYPE_DEFN_STD',
    tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
