Ext.define('KitchenSink.view.interviewManagement.interviewManage.interviewMgrStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewMgrStore',
    model: 'KitchenSink.view.interviewManagement.interviewManage.interviewMgrModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_MS_MGR_COM',
    pageID: 'TZ_MS_IVWMGR_STD',
    //tzStoreParams: '{"cfgSrhId":"TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.TZ_MS_ITWMGRL_V"}',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});
