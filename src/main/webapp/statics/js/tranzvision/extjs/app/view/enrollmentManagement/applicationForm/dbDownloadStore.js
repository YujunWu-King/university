Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.dbDownloadStore', {
    extend: 'Ext.data.Store',
    alias: 'store.dbDownloadStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.dbDownloadModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_BMGL_DBDL_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_BMGL_BMBSH_COM.TZ_BMGL_DBDL_STD.TZ_GD_ZLDL_V","condition":{"OPRID-operator": "01","OPRID-value": "'+ TranzvisionMeikecityAdvanced.Boot.loginUserId+'"}}',
    proxy: Ext.tzListProxy()
});

