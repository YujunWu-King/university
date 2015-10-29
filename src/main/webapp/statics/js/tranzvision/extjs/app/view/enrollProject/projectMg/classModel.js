//专业方向model
Ext.define('KitchenSink.view.enrollProject.projectMg.classModel', {
    extend: 'Ext.data.Model',
    fields: [
		{name: 'classId'},
        {name: 'className'},
        {name: 'startDate'},
		{name: 'endDate'},
		{name: 'openAppOnline'}
	]
});
