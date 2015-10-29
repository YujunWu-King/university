Ext.define('KitchenSink.view.content.contentMg.artAttachmentStore', {
    extend: 'Ext.data.Store',
    alias: 'store.artAttachmentStore',
    model: 'KitchenSink.view.content.contentMg.artAttachmentModel',
    comID: 'TZ_CONTENT_MG_COM',
		pageID: 'TZ_CONTENT_INF_STD',
		tzStoreParams: '',
    pageSize: 5,
	  //autoLoad: true,
	  /*
		proxy: {
			type: 'ajax',
			url : '/tranzvision/kitchensink/app/view/activity/attachments.json',
			reader: {
				type: 'json',
				totalProperty: 'total',
				rootProperty: 'root'
			}
		}	
		*/
		proxy: Ext.tzListProxy()
});
