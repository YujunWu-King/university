Ext.define('KitchenSink.view.website.set.js.styleWinStore', {
    extend: 'Ext.data.Store',
    alias: 'store.styleWinStore',
    model: 'KitchenSink.view.website.set.js.styleWinModel',
	autoLoad: true,
	/*
	sortInfo: {
            field    : 'text',
            direction: 'ASC'
        },
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/website/set/json/siteSet.json',
				reader: {
					type: 'json',
					rootProperty: ''
				}
			}	
	*/
	//autoLoad: true,
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_SITEI_SETED_COM',
	pageID: 'TZ_STYLE_XZ_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
