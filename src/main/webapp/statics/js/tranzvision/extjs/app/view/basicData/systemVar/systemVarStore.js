Ext.define('KitchenSink.view.basicData.systemVar.systemVarStore', {
    extend: 'Ext.data.Store',
    alias: 'store.systemVarStore',
    model: 'KitchenSink.view.basicData.systemVar.systemVarModel',
	autoLoad: true,
	pageSize: 10,
	/*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/basicData/systemVar/systemVar.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}	
	*/
	comID: 'TZ_GD_SYSVARGL_COM',
	pageID: 'TZ_GD_SYSVARGL_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARGL_STD.TZ_SYSVAR_VW"}',
	proxy: Ext.tzListProxy()
});
