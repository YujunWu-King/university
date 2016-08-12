Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsSendMemberModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'taskId'},
        {name: 'rwInsId'},
        {name: 'name'},
        {name: 'addresseePhone'},
        {name: 'sendDt'},
		{name: 'sendStatus'} 
	]
});
