//面试异常数据处理  异常数据详情Model
Ext.define('KitchenSink.view.enrollmentManagement.interviewErrorData.interviewInfoModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'appInsID'},
        {name: 'pwOprID'},
		{name: 'classID'},
		{name: 'batchID'},
		{name: 'stuName'},
		{name: 'mshID'},
		{name: 'groupName'},
        {name: 'msOrder'},
        {name: 'groupDate'},
        {name: 'pwGroupName'},
		{name: 'pwName'}
    ]
});
