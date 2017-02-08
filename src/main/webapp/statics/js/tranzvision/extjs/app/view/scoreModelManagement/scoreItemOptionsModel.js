Ext.define('KitchenSink.view.scoreModelManagement.scoreItemOptionsModel', {
    extend: 'Ext.data.Model',
    fields: [
    	{name: 'orgId'},
        {name: 'treeName'},
        {name: 'itemId'},
        {name: 'optId'},
        {name: 'optName'},
        {name: 'optScore'},
		{name: 'isDefault'}
	]
});
