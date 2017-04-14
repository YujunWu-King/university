Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeModel', {
    extend: 'Ext.data.Model',
    fields:[
    	{name:'classId'},
    	{name:'className'},
    	{name:'batchId'},
    	{name:'batchName'},
    	{name:'name'},
    	{name:'mssqh'},
    	{name:'appinsId'},
    	{name:'sex'},
    	{name:'sexDesc'},
    	{name:'judgeList'},
    	{name:'judgeTotal'},
    	{name:'reviewStatusDesc'},
    	{name:'interviewStatus'},
    	{name:'interviewStatusDesc'}
    ]
});