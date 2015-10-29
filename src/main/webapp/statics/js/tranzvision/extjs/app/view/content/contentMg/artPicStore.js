Ext.define('KitchenSink.view.content.contentMg.artPicStore', {
    extend: 'Ext.data.Store',
    alias: 'store.artPicStore',
    model: 'KitchenSink.view.content.contentMg.artPicModel',
    comID: 'TZ_CONTENT_MG_COM',
		pageID: 'TZ_CONTENT_INF_STD',
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