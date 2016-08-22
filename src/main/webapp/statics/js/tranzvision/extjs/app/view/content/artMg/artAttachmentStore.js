Ext.define('KitchenSink.view.content.artMg.artAttachmentStore', {
    extend: 'Ext.data.Store',
    alias: 'store.artAttachmentStore',
    model: 'KitchenSink.view.content.artMg.artAttachmentModel',
    comID: 'TZ_ART_MG_COM',
	pageID: 'TZ_ART_INFO_STD',
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
