/**
 * Created by tzhjl on 2017/1/12.
 */
Ext.define('KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageStore', {
    extend: 'Ext.data.Store',
    alias: 'store.resourceStore',
    model: 'KitchenSink.view.schoolLibManagement.schoolLibMannage.schoolLibManageModel',
    pageSize: 5,
    autoLoad:false,
    comID: 'TZ_SCH_LIB_COM',
    pageID: 'TZ_SCH_LIST_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
