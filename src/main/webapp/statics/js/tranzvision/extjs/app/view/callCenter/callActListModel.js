Ext.define('KitchenSink.view.callCenter.callActListModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'oprid'},
		{name: 'artId'},
        {name: 'artName'},
        {name: 'startDt'},
        {name: 'startTime'},
        {name: 'endDt'},
        {name: 'endTime'},
        {name: 'artAddr'},
	]
});
