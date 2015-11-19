Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.exportExcel.exportExcelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.appFormExportExcelStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.exportExcel.exportExcelModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_EXP_EXCEL_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_BMGL_BMBSH_COM.TZ_EXP_EXCEL_STD.TZ_GD_DCEXCEL_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'+ TranzvisionMeikecityAdvanced.Boot.loginUserId+'"}}',
    proxy: Ext.tzListProxy()
});

