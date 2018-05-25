Ext.define('KitchenSink.view.clueManagement.clueManagement.clueProblemStore',{
    extend:'Ext.data.Store',
    alias:'store.clueProblemStore',
    model:'KitchenSink.view.clueManagement.clueManagement.clueProblemModel',
    autoLoad:true,
    comID:'TZ_XSXS_ZSXS_COM',
    pageID:'TZ_XSXS_WTXS_STD',
    tzStoreParams:'',
    pageSize:1000,
    proxy:Ext.tzListProxy(),
    groupField: 'clueDesc',
    sorters: {property: 'createDttm', direction: 'DESC'}
});