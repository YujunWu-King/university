Ext.define('KitchenSink.view.weChat.weChatUser.weChatUserMgStore',{
    extend: 'Ext.data.Store',
    alias: 'store.weChatUserMgStore',
    model: 'KitchenSink.view.weChat.weChatUser.weChatUserMgModel',
    autoLoad:false,
    comID: 'TZ_WX_USER_COM',
    pageID: 'TZ_WX_USER_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_WX_USER_COM.TZ_WX_USER_STD.TZ_WX_USER_VW","condition":{}}',
    pageSize:20,
    proxy: Ext.tzListProxy(),
    listeners:{
        load:function(records, successful, operation, eOpts) {
            //默认展开rowexpander
            var grid = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab();
            var store = grid.getStore();
            var expander = grid.getPlugin();
            var storeData = store.data;
            for(var i=0;i< storeData.length;i++){
                var record = storeData.items[i];
                expander.toggleRow(i,record);
            }
        }
    }
});