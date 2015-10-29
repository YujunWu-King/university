Ext.define('KitchenSink.view.orgmgmt.orgRoleModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name:'orgId'},
        {name: 'roleName'},
        {name: 'roleDesc'},
		{name: 'roleType'},
        {name: 'roleTypeDesc'}
	]
});
