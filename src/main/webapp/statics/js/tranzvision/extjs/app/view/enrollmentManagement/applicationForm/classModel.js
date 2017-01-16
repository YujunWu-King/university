Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID',type:'number'},
        {name: 'className'},
		{name: 'batchID'},
		{name: 'batchName'},
		{name: 'applyStatus'},
		{name: 'applyStatusDesc'},
        {name: 'applicantsNumber',type:'number'},
        {name: 'expectedNumber',type:'number'},
		{name: 'firstChoiceNumber',type:'number'}
    ]
});
