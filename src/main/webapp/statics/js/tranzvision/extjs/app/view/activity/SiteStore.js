Ext.define('KitchenSink.view.activity.SiteStore',{
    extend: 'Ext.data.Store',
    alias: 'store.SiteStore',
    model: 'KitchenSink.view.activity.SiteModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_HD_MANAGER_COM',
  	pageID: 'TZ_HD_INFO_STD',
    tzStoreParams:'{"gridTyp":"SITE"}',
    //tzStoreParams:'{}',

    proxy: Ext.tzListProxy()
});


