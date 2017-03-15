Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleJudgeModel', {
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