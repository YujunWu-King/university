Ext.define('KitchenSink.view.scoreModelManagement.scoreModelMgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.scoreModelMgStore',
    model: 'KitchenSink.view.scoreModelManagement.scoreModelMgModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_SCORE_MOD_COM',
	pageID: 'TZ_SCORE_MG_STD ',
	tzStoreParams: '{"cfgSrhId":"TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.TZ_RS_MODAL_TBL","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
