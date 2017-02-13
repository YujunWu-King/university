/**
 * Created by tzhjl on 2017/1/12.
 */
Ext.define('KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageStore', {
    extend: 'Ext.data.Store',
    alias: 'store.resourceStore',
    model: 'KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageModel',
    pageSize: 10,
    autoLoad:true,
    comID: 'TZ_SCH_LIB_COM',
    pageID: 'TZ_SCH_LIST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_SCH_LIB_COM.TZ_SCH_LIST_STD.TZ_SCH_LIB_VW","condition":{}}',
    proxy: Ext.tzListProxy()
});
