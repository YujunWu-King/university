//面试现场分组  预约列表 MODEL
Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.yyModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'msJxNo'},
        {name: 'classID'},
		{name: 'batchID'},
		{name: 'datecolumn'},
		{name: 'maxPerson'},
		{name: 'appoPerson'},
        {name: 'bjMsStartTime'},
        {name: 'bjMsEndTime'},
        {name: 'msLocation'},
		{name: 'msXxBz'}
    ]
});
