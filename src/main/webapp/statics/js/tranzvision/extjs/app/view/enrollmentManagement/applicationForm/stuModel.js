Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.stuModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'oprID'},
        {name: 'appInsID'},
        {name: 'stuName'},
        {name: 'submitState'},
        {name: 'submitDate',type:'date'},
        {name: 'auditState'},
        {name: 'colorType'},
        {name: 'moreInfo'}
    ]
});
