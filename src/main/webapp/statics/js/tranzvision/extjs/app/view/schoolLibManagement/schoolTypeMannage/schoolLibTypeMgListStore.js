/**
 * Created by tzhjl on 2017/1/19.
 */
Ext.define('KitchenSink.view.schoolLibManagement.schoolTypeMannage.schoolLibTypeMgListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.resourceStore',
    model: 'KitchenSink.view.schoolLibManagement.schoolTypeMannage.schoolLibTypeMgListModel',
    pageSize: 10,
    autoLoad:true,
    comID: 'TZ_SCH_TYPE_COM',
    pageID: 'TZ_SCTYE_LIST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.TZ_SCHOOL_TYPE_VW","condition":{}}',
    proxy: Ext.tzListProxy()
});
