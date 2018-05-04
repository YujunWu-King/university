Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.stuModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'oprID'},
        {name: 'appInsID'},
        {name: 'interviewApplicationID'},
        {name: 'nationalID'},
        {name: 'stuName'},
        {name: 'submitState'},
        {name: 'submitDate',type:'date'},
        {name: 'auditState'},
		{name: 'interviewResult'},
        {name: 'colorType'},
        {name: 'moreInfo'},
        {name: 'group_name'},
        {name: 'group_date'},
        ]
});
