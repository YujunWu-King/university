Ext.define('KitchenSink.view.orgmgmt.orgModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'orgId'},
        {name: 'orgName'},
        {name: 'orgYxStateId'},
		{name: 'orgYxStateDesc'},
        {name: 'orgBeizhu'},
		{name: 'orgLxrName'},
		{name: 'orgLxrPhone'},
        {name: 'orgLxrEmail'}
	]
});
