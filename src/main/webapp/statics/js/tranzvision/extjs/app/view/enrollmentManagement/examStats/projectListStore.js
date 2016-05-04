/**
 * 学生报考统计--添加报告项目--项目列表Store
 */
Ext.define('KitchenSink.view.enrollmentManagement.examStats.projectListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.projectListStore',
    model: 'KitchenSink.view.enrollmentManagement.examStats.projectListModel',
	autoLoad: true,
	pageSize: 5,
	comID: 'TZ_EXAM_COUNT_COM',
	pageID: 'TZ_JG_CLASSES_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.PS_TZ_JG_CLASS_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
