Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.viewHistorySms.SmsReplyModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'taskId'},
        {name: 'smsId'},
        {name: 'mobile'},
        {name: 'name'},
        {name: 'replyContent'}
	]
});
