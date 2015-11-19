/**
 * Created by luyan on 2015/11/19.
 */

Ext.define('KitchenSink.view.tzLianxi.LY.DBRWHome.dbrwHomeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.dbrwHomeStore',
    model: 'KitchenSink.view.tzLianxi.LY.DBRWHome.dbrwHomeModel',
    comID: 'TZ_DBRW_HOME_COM',
    pageID: 'TZ_DBRW_HOME_STD',
    tzStoreParams:'',
    autoLoad: true,
    pageSize: 10,
    tzStoreParams:'{"cfgSrhId":"TZ_DBRW_HOME_COM.TZ_DBRW_HOME_STD.TZ_WF_DBRW_T"}',
    proxy: Ext.tzListProxy()
});
