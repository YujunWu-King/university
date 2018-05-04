Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.intervieweeGroupStore', {
    extend: 'Ext.data.Store',
    alias: 'store.intervieweeGroupStore',
    model: 'KitchenSink.view.enrollmentManagement.interviewGroup.intervieweeGroupModel',
    autoLoad: true,
    pageSize: 5,
    comID: 'TZ_MSXCFZ_COM',
    pageID: 'TZ_MSGL_MSFZ_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_MSXCFZ_COM.TZ_MSGL_MSFZ_STD.TZ_GD_ZLDL_V"}',
    proxy: Ext.tzListProxy()
});

