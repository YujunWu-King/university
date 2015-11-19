/**
 * Created by luyan on 2015/11/16.
 */

Ext.define('KitchenSink.view.tzLianxi.LY.DBRWList.dbrwListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.dbrwListStore',
    model: 'KitchenSink.view.tzLianxi.LY.DBRWList.dbrwListModel',
    comID: 'TZ_DBRW_LIST_COM',
    pageID: 'TZ_DBRW_LIST_STD',
    tzStoreParams:'',
    autoLoad: true,
    pageSize: 10,
    constructor:function(dbrwTypeId) {
        this.tzStoreParams = '{"cfgSrhId":"TZ_DBRW_LIST_COM.TZ_DBRW_LIST_STD.TZ_WF_DBRW_T","condition":' +
            '{"TZ_CUSQRYID-operator":"07","TZ_CUSQRYID-value":"'+dbrwTypeId+'"}}';
        this.callParent();
    },

    proxy: Ext.tzListProxy()
});
