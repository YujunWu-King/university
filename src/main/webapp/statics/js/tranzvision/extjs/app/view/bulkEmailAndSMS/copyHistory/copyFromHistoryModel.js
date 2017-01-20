Ext.define('KitchenSink.view.bulkEmailAndSMS.copyHistory.copyFromHistoryModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'qfRwId'},
        {name: 'qfRwName'},
        {name: 'deptDesc'},
        {name: 'sendModal'},
		{name: 'createDttm'},
		{name: 'emlTheme'}
	]
});
