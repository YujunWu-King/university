Ext.define('KitchenSink.view.template.sitetemplate.siteModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'usAccNum'},
        {name: 'usName'},
        {name: 'mobile'},
        {name: 'email'},
        {name: 'birthday', type: 'date'}
	]
});
