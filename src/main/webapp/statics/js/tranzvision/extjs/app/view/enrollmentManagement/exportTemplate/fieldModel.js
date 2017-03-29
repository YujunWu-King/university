Ext.define('KitchenSink.view.enrollmentManagement.exportTemplate.fieldModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'tplID'},
        {name: 'fieldID'},
        {name: 'fieldName'},
        {name: 'appClass'},
        {name: 'fieldSeq',type:'number'},
        {name: 'separator'},
        {name: 'columnWidth',type:'number'},
        {name: 'filter'},
        {name: 'appFormField'}
    ]
});
