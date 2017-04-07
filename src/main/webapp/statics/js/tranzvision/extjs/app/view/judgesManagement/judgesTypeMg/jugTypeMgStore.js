Ext.define('KitchenSink.view.judgesManagement.judgesTypeMg.jugTypeMgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.jugTypeStore',
    model: 'KitchenSink.view.judgesManagement.judgesTypeMg.jugTypeMgModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_JUDGE_TYPE_COM',
	pageID: 'TZ_JUDGE_LIST_STD', 
	tzStoreParams: '{"cfgSrhId":"TZ_JUDGE_TYPE_COM.TZ_JUDGE_LIST_STD.TZ_JUGTYP_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
