Ext.define('KitchenSink.view.website.set.js.siteSetStore', {
    extend: 'Ext.data.Store',
    alias: 'store.siteSetStore',
    model: 'KitchenSink.view.website.set.js.siteSetModel',
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
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_SITEI_SETED_COM',
	pageID: 'TZ_SITEI_SETED_STD',
	tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
