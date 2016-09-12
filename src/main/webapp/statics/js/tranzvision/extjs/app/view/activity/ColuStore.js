Ext.define('KitchenSink.view.activity.ColuStore',{
    extend: 'Ext.data.Store',
    alias: 'store.ColuStore',
    model: 'KitchenSink.view.activity.ColuModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_HD_MANAGER_COM',
  	pageID: 'TZ_HD_INFO_STD',
    tzStoreParams:'{"gridTyp":"COLU"}',
    //tzStoreParams:'{}',

    proxy: Ext.tzListProxy()
});


