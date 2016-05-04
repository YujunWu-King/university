/**
 * 学生报考统计--添加报告项目--项目列表Model
 */
Ext.define('KitchenSink.view.enrollmentManagement.examStats.projectListModel', {
    extend: 'Ext.data.Model',
    fields: [
		{name: 'classId'},
        {name: 'className'},
        {name: 'classStatus'},
		{name: 'orgId'}
	]
});