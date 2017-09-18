Ext.define('KitchenSink.view.sendEmailAndSMS.emailTxType.txTypeRuleModel', {
    extend: 'Ext.data.Model',
    fields: [
    	{name: 'txTypeId'},
		{name: 'txRuleId'},
		{name: 'txRuleName'},
		{name: 'matchType'}
	]
});
