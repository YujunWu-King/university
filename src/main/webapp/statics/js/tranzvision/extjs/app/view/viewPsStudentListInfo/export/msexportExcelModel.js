Ext.define('KitchenSink.view.viewPsStudentListInfo.export.msexportExcelModel', {
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
