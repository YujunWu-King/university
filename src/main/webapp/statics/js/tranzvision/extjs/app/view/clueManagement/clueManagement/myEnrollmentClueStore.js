Ext.define('KitchenSink.view.clueManagement.clueManagement.myEnrollmentClueStore',{
    extend:'Ext.data.Store',
    alias:'store.myEnrollmentClueStore',
    model:'KitchenSink.view.clueManagement.clueManagement.myEnrollmentClueModel',
    pageSize:20,
    autoLoad: true,
    comID:'TZ_XSXS_MYXS_COM',
    pageID:'TZ_XSXS_MYXS_STD',
    tzStoreParams:'{"cfgSrhId":"TZ_XSXS_MYXS_COM.TZ_XSXS_MYXS_STD.TZ_XSXS_INFO_VW","condition":{}}',
    proxy:Ext.tzListProxy()
});