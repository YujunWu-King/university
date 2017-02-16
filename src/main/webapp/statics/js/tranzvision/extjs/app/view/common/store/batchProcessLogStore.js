Ext.define('KitchenSink.view.common.store.batchProcessLogStore', {
    extend: 'Ext.data.Store',
    alias: 'store.batchProcessLogStore',
    model:'KitchenSink.view.common.store.batchProcessLogModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_COMMON_JCXQ_COM',
	pageID: 'TZ_COMMON_JCXQ_STD',
	tzStoreParams:'',
	proxy: Ext.tzListProxy(),
	constructor: function (config) {
	     //交互参数
	     this.tzStoreParams = '{"processIns":"'+config.processIns+'","orgId":"'+config.orgId+'"}';
	     this.callParent();
	  }	
});
