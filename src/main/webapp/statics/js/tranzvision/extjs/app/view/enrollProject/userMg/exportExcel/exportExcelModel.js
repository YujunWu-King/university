Ext.define('KitchenSink.view.enrollProject.userMg.exportExcel.exportExcelModel', {
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
