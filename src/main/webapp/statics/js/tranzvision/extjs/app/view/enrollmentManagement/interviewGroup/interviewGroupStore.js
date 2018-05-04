Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.interviewGroupStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewGroupStore',
    model: 'KitchenSink.view.enrollmentManagement.interviewGroup.interviewGroupModel',
    autoLoad: false,
    pageSize: 5,
    comID: 'TZ_MSXCFZ_COM',
    pageID: 'TZ_MSGL_MSFZ_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_MSXCFZ_COM.TZ_MSGL_MSFZ_STD.TZ_GD_ZLDL_V","condition":{"OPRID-operator": "01","OPRID-value": "'+ TranzvisionMeikecityAdvanced.Boot.loginUserId+'"}}',
    proxy: Ext.tzListProxy()
});

