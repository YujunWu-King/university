Ext.define('KitchenSink.view.template.survey.question.export.exportExcelModel', {
    extend: 'Ext.data.Model',
    fields: [
    	{name: 'procInsId'},
        {name: 'fileName'},
        {name: 'czPerName'},
        {name: 'bgTime'},
        {name: 'loginId'},
        {name: 'procState'}
    ]
});
