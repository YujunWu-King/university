Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewJudgeAccountModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'batchID'},
        {name: 'judgeID'},
        {name: 'judgeName'},
        {name: 'judgeDepart'},
        {name: 'judgeType'},
        {name: 'judgePhoneNumber'},
        {name: 'judgeEmail'},
        {name: 'judgeOAID'},
        {name:'judgeGroup'}
	]
});
