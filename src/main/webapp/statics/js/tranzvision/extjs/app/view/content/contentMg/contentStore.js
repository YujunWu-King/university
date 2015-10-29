Ext.define('KitchenSink.view.content.contentMg.contentStore', {
    extend: 'Ext.data.Store',
    alias: 'store.contentStore',
    model: 'KitchenSink.view.content.contentMg.contentModel',
	comID: 'TZ_CONTENT_MG_COM',
	pageID: 'TZ_CONTENT_STD',
	tzStoreParams: '',
	autoLoad: true,
	pageSize: 50,
	/*
	proxy: {
		type: 'ajax',
		url : '/tranzvision/kitchensink/app/view/content/contentMg/contents.json',
		reader: {
			type: 'json',
			rootProperty: 'root'
		}
	}*/
	constructor: function (columnId) {
        //this.tzStoreParams = '{"columnId":"' + columnId + '"}';
        this.tzStoreParams = '{"cfgSrhId":"TZ_CONTENT_MG_COM.TZ_CONTENT_STD.TZ_GD_CONTENT_V","condition":{"TZ_COLU_ID-operator":"01","TZ_COLU_ID-value":"'+columnId+'"}}';
        this.callParent();
    },
	proxy: Ext.tzListProxy()
});