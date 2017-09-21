Ext.define('KitchenSink.view.security.com.processModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'orgId'},
        {name: 'processName'},
        {name: 'processDesc'},
        {name: 'platformType'},
        {name: 'isDispatch',type:'boolean'}
	]
});
