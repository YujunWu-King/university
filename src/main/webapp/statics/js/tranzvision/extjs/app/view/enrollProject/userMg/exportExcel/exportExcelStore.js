Ext.define('KitchenSink.view.enrollProject.userMg.exportExcel.exportExcelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userMgExportExcelStore',
    model: 'KitchenSink.view.enrollProject.userMg.exportExcel.exportExcelModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_UM_USERMG_COM',
    pageID: 'TZ_EXP_EXCEL_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_UM_USERMG_COM.TZ_EXP_EXCEL_STD.TZ_GD_DCEXCEL_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'+ TranzvisionMeikecityAdvanced.Boot.loginUserId+'"}}',
    proxy: Ext.tzListProxy()
});

