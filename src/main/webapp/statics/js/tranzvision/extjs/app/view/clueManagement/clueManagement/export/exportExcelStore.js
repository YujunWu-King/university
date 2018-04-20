Ext.define('KitchenSink.view.clueManagement.clueManagement.export.exportExcelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.exportExcelStore',
    model: 'KitchenSink.view.clueManagement.clueManagement.export.exportExcelModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_XSXS_DRDC_COM',
    pageID: 'TZ_XSXS_DRDC_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy(),
    constructor: function(exportType){
        this.tzStoreParams = '{"cfgSrhId":"TZ_XSXS_DRDC_COM.TZ_XSXS_DRDC_STD.TZ_EXCEL_DC_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'
            + TranzvisionMeikecityAdvanced.Boot.loginUserId
            +'","TZ_DR_LXBH-operator":"01","TZ_DR_LXBH-value":"'+exportType
            +'","TZ_COM_ID-operator": "01","TZ_COM_ID-value": "TZ_XSXS_DRDC_COM","TZ_PAGE_ID-operator": "01","TZ_PAGE_ID-value": "TZ_XSXS_DRDC_STD"}}';

        this.callParent();
    }
});

