Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsMgModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'smsQfId'},
        {name: 'smsQfDesc'},
        /*{name: 'dept'},*/
		{name: 'oprid'},
        {name: 'crePer'},
        {name: 'creDt'}
    ]
});
