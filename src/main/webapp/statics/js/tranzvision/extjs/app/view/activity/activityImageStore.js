Ext.define('KitchenSink.view.activity.activityImageStore', {
    extend: 'Ext.data.Store',
    alias: 'store.activityImageStore',
    model: 'KitchenSink.view.activity.activityImageModel',
    //pageSize: 5,
	  autoLoad: true,
		proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/activity/activityImages.json',
				reader: {
					type: 'json',
					//totalProperty: 'total',
					rootProperty: 'root'
				}
		}	
});
