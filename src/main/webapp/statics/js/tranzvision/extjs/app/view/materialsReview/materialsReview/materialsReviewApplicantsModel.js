Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsModel', {
    extend: 'Ext.data.Model',
    field:[
        {name: 'classID'},
        {name: 'batchID'},
        {name: 'appInsID'},
        {name: 'oprID'},
        {name: 'realName'},
        {name: 'gender'},
        {name: 'judgeList'},
        {name: 'reviewStatus'},
        {name: 'interviewQualification'},
        {name: 'colorType'},
        {name: 'remark'},
        {name: 'adminRemark'}
    ]
});
