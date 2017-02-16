Ext.define('KitchenSink.view.common.store.batchProcessLogModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'orderNum'},
        {name: 'level'},
        {name: 'datetime'},
        {name: 'logContent'}
	]
});
