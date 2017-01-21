Ext.define('KitchenSink.view.scoreModelManagement.scoreItemsModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'orgId'},
        {name: 'modeId'},
        {name: 'itemId'},
		{name: 'itemType'},
		{name: 'sortNum'},
		{name: 'viewName'}
	]
});
