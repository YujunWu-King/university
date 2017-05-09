Ext.define('KitchenSink.view.processServer.processServerModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'orgId'},
        {name: 'processName'},
        {name: 'processDesc'},
        {name: 'runningStatus'}
	]
});
