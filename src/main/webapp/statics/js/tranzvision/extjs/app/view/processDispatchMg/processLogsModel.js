Ext.define('KitchenSink.view.processDispatchMg.processLogsModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'orderNum'},
        {name: 'logLevel'},
        {name: 'dateTime'},
        {name: 'logDetail'}
    ]
});
