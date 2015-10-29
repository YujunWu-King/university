Ext.define('KitchenSink.view.siteManage.siteManage.menu.menuManges.menuTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.menuTypeStoreI',
    model: 'KitchenSink.view.siteManage.siteManage.menu.menuManges.menuTypeModel',//json格式
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_ZDGL_COM',
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
