Ext.define('KitchenSink.view.distributionTable.distributionDetailModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'orgId'},
        {name: 'distriId'},
        {name: 'distrMXId'},
        {name: 'itemId'},
		{name: 'itemType'},
		{name: 'sortNum'},
		{name: 'sequence'},
		{name: 'name'},
		{name: 'explain'},
		{name: 'lowScore'},
		{name: 'upScore'}
	]
});
