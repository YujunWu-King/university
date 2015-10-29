Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.exportExcel.exportExcelModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'fileName'},
        {name: 'oprName'},
        {name: 'oprTime'},
        {name: 'processInstance'},
        {name: 'applicationEngineStatus'},
        {name: 'fileUrl'}
    ]
});
