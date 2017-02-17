Ext.define('KitchenSink.view.automaticScreen.autoScreenModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classId'},
        {name: 'batchId'},
        {name: 'appId'},
		{name: 'name'},
		{name: 'total'},
		{name: 'ranking'},
		{name: 'status'},
		{name: 'negativeList'},
		{name: 'autoLabel'},
		{name: 'manualLabel'}
	]
});
