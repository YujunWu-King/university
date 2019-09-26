//面试异常数据处理-班级批次列表  默认首页 MODEL
Ext.define('KitchenSink.view.enrollmentManagement.interviewErrorData.interviewListModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'className'},
		{name: 'batchID'},
		{name: 'batchName'}
    ]
});
