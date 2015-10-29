Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewApplicantsWindowModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'batchID'},
        {name: 'realName'},
        {name: 'appInsID'},
        {name: 'gender'},
        {name:'isInterviewed'},
        {name:'joinedBatchs'}
    ]
});