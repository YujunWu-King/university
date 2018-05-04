Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.interviewModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'className'},
		{name: 'batchID'},
		{name: 'batchName'},
		{name: 'applyStatus'},
		{name: 'admissionDate'},
        {name: 'applicantsNumber',type:'number'},
        {name: 'expectedNumber',type:'number'},
        {name: 'submittedNumber',type:'number'},
		{name: 'firstChoiceNumber',type:'number'}
    ]
});
