Ext.define('KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlStore', {
    extend: 'Ext.data.Store',
    alias: 'store.transStore',
    model: 'KitchenSink.view.enrollProject.submitDtMdlMg.smtDtMdlModel',
	autoLoad: true,
    pageSize: 10,
	comID: 'TZ_GD_SMTDTMDL_COM',
	pageID: 'TZ_GD_SMTDTLST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_GD_SMTDTMDL_COM.TZ_GD_SMTDTSET_STD.TZ_SBMINF_TMP_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
});
