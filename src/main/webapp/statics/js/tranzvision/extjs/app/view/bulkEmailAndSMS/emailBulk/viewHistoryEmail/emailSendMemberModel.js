Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.viewHistoryEmail.emailSendMemberModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'taskId'},
        {name: 'rwInsId'},
        {name: 'name'},
        {name: 'email'},
        {name: 'sendDt'},
		{name: 'sendStatus'} 
	]
});
