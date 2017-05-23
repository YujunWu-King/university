Ext.define('KitchenSink.view.processDispatchMg.processMonitorModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'processInstance'},
        {name: 'processName'},
        {name: 'user'},
        {name: 'processServerName'},
        {name: 'orgId'},
        {name: 'status'}
    ]
});
