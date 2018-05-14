//面试现场分组  评委组分组 Model
Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.interviewGroupModel', {
    extend: 'Ext.data.Model',
    fields: [
    	{name: 'tz_group_id'},
    	{name: 'tz_group_name'},
        {name: 'interviewers'}
    ]
});
