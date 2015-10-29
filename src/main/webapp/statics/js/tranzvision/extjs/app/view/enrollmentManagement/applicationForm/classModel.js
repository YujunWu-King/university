Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID',type:'number'},
        {name: 'className'},
        {name: 'projectName'},
        {name: 'projectType'},
        {name: 'applicantsNumber',type:'number'},
        {name: 'noauditNumber',type:'number'}
    ]
});
