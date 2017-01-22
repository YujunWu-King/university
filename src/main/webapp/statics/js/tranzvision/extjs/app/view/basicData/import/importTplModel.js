Ext.define('KitchenSink.view.basicData.import.importTplModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'tplId'},
        {name: 'tplName'},
        {name: 'targetTbl'},
        {name: 'javaClass'},
        {name: 'excelTpl'}
	]
});
