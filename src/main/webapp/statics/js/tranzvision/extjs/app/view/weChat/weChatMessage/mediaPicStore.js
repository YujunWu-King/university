Ext.define('KitchenSink.view.weChat.weChatMessage.mediaPicStore', {
    extend: 'Ext.data.Store',
    alias: 'store.mediaPicStore',
    model: 'KitchenSink.view.weChat.weChatMessage.mediaPicModel',
    autoLoad:true,
    comID: 'TZ_GD_WXMSG_COM',
	pageID: 'TZ_GD_SCGL_STD',
	tzStoreParams: '',
    pageSize: 10, 
    proxy: Ext.tzListProxy(),
    
    constructor:function(config){
        var materialType = config.materialType;
        var wxAppId = config.wxAppId;
        
        this.tzStoreParams = '{"wxAppId":"'+wxAppId+'","mediaType":"'+materialType+'"}';
        
        this.callParent();
    },
});