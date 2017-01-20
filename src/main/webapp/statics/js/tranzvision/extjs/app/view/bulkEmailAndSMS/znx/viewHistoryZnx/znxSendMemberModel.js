Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxSendMemberModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'taskId'},
        {name: 'rwInsId'},
        {name: 'name'},
        /*{name: 'email'},*/
        {name: 'sendDt'},
		{name: 'sendStatus'} 
	]
});
