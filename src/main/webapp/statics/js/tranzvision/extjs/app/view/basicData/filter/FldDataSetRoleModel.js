Ext.define('KitchenSink.view.basicData.filter.FldDataSetRoleModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'ComID'},
        {name: 'PageID'},
        {name: 'ViewID'},
		{name: 'orderNum'},
        {name: 'roleID'},
        {name: 'roleDesc'}
    ]
});