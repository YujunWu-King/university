Ext.define('KitchenSink.view.weChatLog.weChatLogListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.weChatLogListStore',
    model: 'KitchenSink.view.weChatLog.weChatLogListModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_WXSERVICE_COM',
	pageID: 'TZ_GD_LOGLIST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_GD_WXSERVICE_COM.TZ_GD_LOGLIST_STD.TZ_WXMSG_LOG_T","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
//    pageSize: 0,
    proxy: Ext.tzListProxy()
});
