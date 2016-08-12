Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.viewHistoryEmail.emailHistoryDetailGridModel', {
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
