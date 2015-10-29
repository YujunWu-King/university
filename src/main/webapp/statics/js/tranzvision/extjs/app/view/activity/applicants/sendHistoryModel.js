Ext.define('KitchenSink.view.activity.applicants.sendHistoryModel', {
    extend: 'Ext.data.Model',
    fields: [
		{name: 'activityId'},
		{name: 'sendType'},
        {name: 'sendStatus'},
		{name: 'sendCount'},
        {name: 'successNum'},
        {name: 'failedNum'},
		{name: 'sendDate'}
	]
});
