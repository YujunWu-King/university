Ext.define('KitchenSink.view.content.artMg.artStore', {
    extend: 'Ext.data.Store',
    alias: 'store.artStore',
    model: 'KitchenSink.view.content.artMg.artModel',
	comID: 'TZ_ART_MG_COM',
	pageID: 'TZ_ART_LIST_STD',
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
		this.tzStoreParams = '{"cfgSrhId":"TZ_ART_MG_COM.TZ_ART_LIST_STD.TZ_GD_CONTENT_V","condition":{"TZ_COLU_ID-operator":"01","TZ_COLU_ID-value":"'+columnId+'"}}';
        this.callParent();
    },
	proxy: Ext.tzListProxy()
});