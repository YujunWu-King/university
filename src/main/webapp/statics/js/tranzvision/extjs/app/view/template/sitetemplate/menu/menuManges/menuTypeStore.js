Ext.define('KitchenSink.view.template.sitetemplate.menu.menuManges.menuTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.menuTypeStore',
    model: 'KitchenSink.view.template.sitetemplate.menu.menuManges.menuTypeModel',//json格式
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_ZDMB_COM',
	pageID: 'TZ_ZD_CDLXSZ_STD',
	tzStoreParams: '{}',
	/*proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/menu/menuManges/menuTypes.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}	*/
	proxy: Ext.tzListProxy()			
});
