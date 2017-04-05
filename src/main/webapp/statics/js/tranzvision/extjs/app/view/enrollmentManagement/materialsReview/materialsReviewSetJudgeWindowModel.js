Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewSetJudgeWindowModel',{
	 extend: 'Ext.data.Model',
    fields: [
        {name: 'classId'},
        {name: 'batchId'},
        {name: 'judgeOprid'},
        {name: 'judgeId'},
        {name: 'judgeName'},
        {name: 'judgeGroup'},
        {name: 'selectFlag'}
    ]
});