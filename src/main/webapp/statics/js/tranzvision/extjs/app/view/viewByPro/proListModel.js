Ext.define('KitchenSink.view.viewByPro.proListModel', {
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
