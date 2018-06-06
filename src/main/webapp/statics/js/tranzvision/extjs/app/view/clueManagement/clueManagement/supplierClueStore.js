Ext.define('KitchenSink.view.clueManagement.clueManagement.supplierClueStore',{
    extend:'Ext.data.Store',
    alias:'store.supplierClueStore',
    model:'KitchenSink.view.clueManagement.clueManagement.myEnrollmentClueModel',
    pageSize:20,
    autoLoad: true,
    comID:'TZ_GYS_CLUE_COM',
    pageID:'TZ_GYS_CLUE_STD',
    tzStoreParams:'{"cfgSrhId":"TZ_GYS_CLUE_COM.TZ_GYS_CLUE_STD.TZ_GYSXS_INFO_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID +'","TZ_LEAD_STATUS-operator": "02","TZ_LEAD_STATUS-value": "G"}}',
    proxy:Ext.tzListProxy()
});