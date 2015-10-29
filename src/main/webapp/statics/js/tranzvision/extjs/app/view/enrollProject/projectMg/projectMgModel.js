Ext.define('KitchenSink.view.enrollProject.projectMg.projectMgModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'projectId'},
        {name: 'projectName'},
        {name: 'projectDesc'},
		{name: 'projectType'},
		{name: 'usedStatus'},
		{name: 'statusDesc'}
	]
});
