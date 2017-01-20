Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryDetailGridModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'status'},
        {name: 'sendNum'},
        {name: 'sendSucNum'},
        {name: 'sendFailNum'},
        {name: 'sendDt'},
        {name: 'operator'}
    ]
});
