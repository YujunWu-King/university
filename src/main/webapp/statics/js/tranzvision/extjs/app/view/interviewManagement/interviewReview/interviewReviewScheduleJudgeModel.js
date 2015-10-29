Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewScheduleJudgeModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'batchID'},
        {name: 'judgeID'},
        {name: 'judgeName'},
        {name: 'judgeGroup'},
        {name: 'submitNum'},
        {name:'submitYN'},
        {name:'accountStatus'},
        {name:'hasSubmited'}
    ]
});