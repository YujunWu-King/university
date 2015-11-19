Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicantsWindowModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'batchID'},
        {name: 'realName'},
        {name: 'appINSID'},
        {name: 'gender'},
        {name:'isInterviewed'},
        {name:'joinedBatchs'}
    ]
});