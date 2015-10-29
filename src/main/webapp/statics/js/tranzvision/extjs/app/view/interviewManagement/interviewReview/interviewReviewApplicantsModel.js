Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicantsModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'batchID'},
        {name: 'oprID'},
        {name: 'realName'},
        {name: 'appInsID'},
        {name: 'gender'},
        {name:'judgeGroup'},
        {name:'reviewStatus'},
        {name:'remark'}
    ]
});
