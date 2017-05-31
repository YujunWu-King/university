Ext.define('KitchenSink.view.sendEmailAndSMS.emailTxType.txRuleStore', {
    extend: 'Ext.data.Store',
    alias: 'store.tzRuleStore',
    model: 'KitchenSink.view.sendEmailAndSMS.emailTxType.txRuleModel',
	autoLoad: true,
	pageSize: 20,
	comID: 'TZ_EMLTX_RULE_COM',
    pageID: 'TZ_EMLTX_RULE_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_EMLTX_RULE_COM.TZ_EMLTX_RULE_STD.TZ_TX_RULE_TBL","condition":{}}',
	proxy: Ext.tzListProxy()
});
