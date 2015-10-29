Ext.define('KitchenSink.view.template.sitetemplate.area.typeManges.areaTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.areaTypeStore',
    model: 'KitchenSink.view.template.sitetemplate.area.typeManges.areaTypeModel',//json格式
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_ZDMB_COM',
	pageID: 'TZ_ZD_QYLXSZ_STD',
	tzStoreParams: '{}',
	/*proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/area/typeManges/areaTypes.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}*/
	proxy: Ext.tzListProxy()			
});
