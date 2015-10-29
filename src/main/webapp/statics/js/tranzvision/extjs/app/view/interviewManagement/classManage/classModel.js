Ext.define('KitchenSink.view.interviewManagement.classManage.classModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'className'},
        {name: 'projectName'},
        {name: 'projectType'},
        {name: 'applicantsNumber',type:'number'},
        {name: 'noauditNumber',type:'number'}
    ]
});
