Ext.define('KitchenSink.view.judgesManagement.secretaryAccount.secretaryAccModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'accountNo'},//秘书账号
        {name: 'judgeName'},//秘书名称
        {name: 'judgeType'},//秘书类型
		{name: 'judTypeDesc'},//秘书类型名称
		{name: 'oprId'},
		{name: 'orgId'},
        {name: 'judPhoneNumber'},//秘书手机号
        {name: 'judEmail'},//秘书邮箱
//        {name: 'judOAID'},//秘书OA账号
//        {name: 'judDepart'}//秘书所属系


	]
});
