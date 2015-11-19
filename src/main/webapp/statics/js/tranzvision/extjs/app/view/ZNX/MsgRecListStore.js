Ext.define('KitchenSink.view.ZNX.MsgRecListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.MsgRecListStore',
    model: 'KitchenSink.view.ZNX.MsgRecListModel',
    pageSize:10,
    autoLoad:true,
    comID: 'TZ_ZNX_COM',
    pageID: 'TZ_ZNX_INBOX_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_ZNX_COM.TZ_ZNX_INBOX_STD.TZ_RECLIST_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'","TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value":"'+TranzvisionMeikecityAdvanced.Boot.loginUserId+'"}}',

    // tzStoreParams:'{}',
    proxy: Ext.tzListProxy()
});
