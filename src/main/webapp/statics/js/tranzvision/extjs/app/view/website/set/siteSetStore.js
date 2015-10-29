Ext.define('KitchenSink.view.website.set.siteSetStore', {
    extend: 'Ext.data.Store',
    alias: 'store.siteSetStore',
    model: 'KitchenSink.view.website.set.siteSetModel',
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/basicData/resData/translate/trans.json',
				reader: {
					type: 'json',
					rootProperty: ''
				}
			}	
});
