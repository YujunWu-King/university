/**
 * Created by carmen on 2015/11/9.
 */
Ext.define('KitchenSink.view.ZNX.MsgSentListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.MsgSentListStore',
    model: 'KitchenSink.view.ZNX.MsgSentListModel',
    pageSize:10,
    autoLoad:true,
    comID: 'TZ_ZNX_COM',
    pageID: 'TZ_ZNX_SENT_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_ZNX_COM.TZ_ZNX_SENT_STD.TZ_SENTLIST_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'","TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value":"'+TranzvisionMeikecityAdvanced.Boot.loginUserId+'"}}',
    proxy: Ext.tzListProxy()
});
