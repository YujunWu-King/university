//面试现场分组  考生列表 MODEL
Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.stuModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'ksOprId'},
        {name: 'ksName'},
		{name: 'mshId'},
		{name: 'appInsId'},
		{name: 'gender'},
		{name: 'pwgroup_name'},
        {name: 'group_name'},
        {name: 'order'},
        {name: 'group_date'}
    ]
});
