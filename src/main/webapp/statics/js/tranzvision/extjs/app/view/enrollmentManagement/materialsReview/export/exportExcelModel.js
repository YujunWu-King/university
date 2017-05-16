Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.export.exportExcelModel', {
    extend: 'Ext.data.Model',
    fields: [
    	{name: 'procInsId'},
    	{name: 'classBatch'},
        {name: 'fileName'},
        {name: 'czPerName'},
        {name: 'bgTime'},
        {name: 'procStaDescr'},
        {name: 'procState'}
    ]
});
