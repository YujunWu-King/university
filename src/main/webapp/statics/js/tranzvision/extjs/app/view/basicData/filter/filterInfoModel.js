Ext.define('KitchenSink.view.basicData.filter.filterInfoModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'ComID'},
        {name: 'PageID'},
        {name: 'ViewMc'},
        {name: 'FieldMc'},
        {name: 'fieldDesc'},
        {name: 'fldReadonly',type:'int'},
        {name: 'fldHide',type:'int'},
        {name: 'promptTab'},
        {name: 'promptFld'},
        {name: 'orderNum'},
        {name: 'deepQueryFlgDesc'},
        {name: 'deepQueryFlg'},
        {name: 'deepQueryView'},
        {name: 'deepQueryFld'}
    ]
});
