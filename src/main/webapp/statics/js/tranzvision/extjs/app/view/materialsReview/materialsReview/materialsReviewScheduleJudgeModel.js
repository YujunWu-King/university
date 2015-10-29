Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewScheduleJudgeModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'batchID'},
        {name: 'judgeID'},
        {name: 'descript'},
        {name: 'judgeGroup'},
        {name: 'submitNum'},
        {name:'submitYN'},
        {name:'accountStatus'}
    ]
});