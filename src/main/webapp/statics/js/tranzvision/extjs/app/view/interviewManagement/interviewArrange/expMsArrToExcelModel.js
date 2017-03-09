Ext.define('KitchenSink.view.interviewManagement.interviewArrange.expMsArrToExcelModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'fileName'},
        {name: 'oprName'},
        {name: 'oprTime'},
        {name: 'processInstance'},
        {name: 'engineStatus'},
        {name: 'fileUrl'}
    ]
});
