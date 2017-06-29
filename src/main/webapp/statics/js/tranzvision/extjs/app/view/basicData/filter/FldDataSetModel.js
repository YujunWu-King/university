Ext.define('KitchenSink.view.basicData.filter.FldDataSetModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'ComID'},
        {name: 'PageID'},
        {name: 'ViewMc'},
		{name: 'orderNum'},
        {name: 'dataSetFld'},
        {name: 'searchRec'},
        {name: 'fldDstDesc'},
		{name: 'fldDstStatus'},
		{name: 'fldDstDefault'}
    ]
});