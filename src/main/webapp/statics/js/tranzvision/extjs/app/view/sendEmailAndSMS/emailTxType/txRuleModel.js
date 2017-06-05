Ext.define('KitchenSink.view.sendEmailAndSMS.emailTxType.txRuleModel', {
    extend: 'Ext.data.Model',
    fields: [
		{name: 'ruleId'},
        {name: 'ruleName'},
        {name: 'compareType'},
		{name: 'status'},
        {name: 'keyword'}
	]
});
