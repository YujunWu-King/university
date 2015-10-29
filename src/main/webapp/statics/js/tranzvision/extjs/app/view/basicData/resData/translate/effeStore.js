Ext.define('KitchenSink.view.basicData.resData.translate.effeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.effeStore',
    fields: ['effeId','isEffe'],
	//autoLoad: true,
	/*proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/basicData/resData/translate/effe.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}*/	
	tzStoreParams: '{}',
	//proxy: Ext.tzListProxy()
});
