Ext.define('KitchenSink.view.materialsReview.judgeAccount.judgeTypeModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'oprId'},//评委oprid
		{name: 'oldJudgeType'},//评委类型
        {name: 'judgeType'}//评委类型
	]
});
