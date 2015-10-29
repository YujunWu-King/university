Ext.define('KitchenSink.view.basicData.filter.fldYsfModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'ComID'},
        {name: 'PageID'},
        {name: 'ViewMc'},
        {name: 'FieldMc'},
        {name: 'orderNum'},
        {name: 'FieldYsfID'},
        {name: 'FieldYsf'},
        {name: 'isQy', type: 'boolean', defaultValue: false},
        {name: 'isOprt'}
    ]
});
