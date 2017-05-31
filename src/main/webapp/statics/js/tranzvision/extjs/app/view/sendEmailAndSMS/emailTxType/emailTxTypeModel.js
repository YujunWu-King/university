Ext.define('KitchenSink.view.sendEmailAndSMS.emailTxType.emailTxTypeModel', {
    extend: 'Ext.data.Model',
    fields: [
		{name: 'txTypeId'},
        {name: 'txTypeName'},
        {name: 'txType'},
		{name: 'txTypeDesc'},
        {name: 'isValid'}
	]
});
