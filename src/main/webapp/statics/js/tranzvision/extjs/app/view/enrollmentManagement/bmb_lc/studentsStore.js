Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.studentsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.studentsStore',
    model: 'KitchenSink.view.enrollmentManagement.bmb_lc.studentsModel',
	comID: 'TZ_BMGL_BMBSH_COM',
	pageID: 'TZ_BMSH_FB_STD',
	//tzStoreParams: '{}',
	tzStoreParams:'',
	autoLoad: false,
	//autoLoad: true,
	pageSize: 1000,
	proxy: Ext.tzListProxy()	
});