/**
 * Created by admin on 2015/9/7.
 */
Ext.define('KitchenSink.view.enrollmentManagement.examStats.studentsInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.studentsInfoStore',
    model: 'KitchenSink.view.enrollmentManagement.examStats.studentsInfoModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_EXAM_COUNT_COM',
    pageID: 'TZ_STU_INFO_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy(),
    constructor: function(config){
    	this.tzStoreParams = '{"statsId":"' + config.statsId + '","username":"' + config.username + '","isMulti":"' + config.isMulti + '"}';
    	this.callParent();
    }
});