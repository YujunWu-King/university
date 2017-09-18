Ext.define('KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewAppJudgeModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'batchID'},
        {name: 'judgeRealName'},
        {name: 'appInsID'},
        {name: 'studentRealName'},
        {name:'score'}
    ]
});