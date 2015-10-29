Ext.define('KitchenSink.view.activity.applicants.channelTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.channelTypeStore',
    fields: ['channelId','channelName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/activity/applicants/channelType.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
