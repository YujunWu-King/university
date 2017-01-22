Ext.define('KitchenSink.view.content.artMg.artColuStore',{
    extend: 'Ext.data.Store',
    alias: 'store.artColuStore',
    model: 'KitchenSink.view.content.artMg.artColuModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_ART_MG_COM',
  	pageID: 'TZ_ART_INFO_STD',
    tzStoreParams:'{"gridTyp":"COLU"}',
    //tzStoreParams:'{}',

    proxy: Ext.tzListProxy()
});


