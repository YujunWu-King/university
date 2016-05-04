/**
 * Created by admin on 2015/9/7.
 */
Ext.define('KitchenSink.view.enrollmentManagement.examStats.studentsExamStore', {
    extend: 'Ext.data.Store',
    alias: 'store.studentsExamStore',
    model: 'KitchenSink.view.enrollmentManagement.examStats.studentsExamModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_EXAM_COUNT_COM',
    pageID: 'TZ_EXAM_COUNT_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy(),
    constructor: function(config){
    	this.tzStoreParams = '{"statsId":"' + config.statsId + '"}';
    	this.callParent();
    }
});