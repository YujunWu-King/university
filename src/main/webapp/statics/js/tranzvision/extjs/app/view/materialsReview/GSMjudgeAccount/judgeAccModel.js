Ext.define('KitchenSink.view.materialsReview.GSMjudgeAccount.judgeAccModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'accountNo'},//评委账号
        {name: 'judgeName'},//评委名称
        {name: 'judgeType'},//评委类型
		{name: 'judTypeDesc'},//评委类型名称
		{name: 'oprId'},
		{name: 'orgId'},
        {name: 'judPhoneNumber'},//评委手机号
        {name: 'judEmail'},//评委邮箱
        {name: 'judOAID'},//评委OA账号
        {name: 'judDepart'}//评委所属系
//        {name: 'classID'},//评委所属班级ID
//        {name: 'className'}//评委所属班级
	]
});
