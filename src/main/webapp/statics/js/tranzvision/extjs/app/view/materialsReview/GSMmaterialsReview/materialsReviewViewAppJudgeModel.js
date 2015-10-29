Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewViewAppJudgeModel', {
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