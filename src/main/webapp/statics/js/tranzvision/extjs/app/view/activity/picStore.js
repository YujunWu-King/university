Ext.define('KitchenSink.view.activity.picStore', {
    extend: 'Ext.data.Store',
    alias: 'store.picStore',
    model: 'KitchenSink.view.activity.picModel',
    comID: 'TZ_HD_MANAGER_COM',
		pageID: 'TZ_HD_INFO_STD',
		tzStoreParams: '',
    pageSize: 5,
    proxy: Ext.tzListProxy()
		//autoLoad: true,
		//proxy: {
		//	type: 'ajax',
			//url : '/tranzvision/kitchensink/app/view/template/sitetemplate/pics.json',
		//	url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_getAtt',
		//	reader: {
		//		type: 'json',
		//		rootProperty: 'picList'
		//	}
		//}
});