Ext.define('KitchenSink.view.onTrialManagement.trialManagementStore', {
    extend: 'Ext.data.Store',
    alias: 'store.trialManagementStore',
    model: 'KitchenSink.view.onTrialManagement.trialManagementModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_TRIAL_MNG_COM',
    pageID: 'TZ_TRIAL_MNG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_TRIAL_MNG_COM.TZ_TRIAL_MNG_STD.PS_TZ_ON_TRIAL_V","condition":{}}',
	proxy: Ext.tzListProxy()		
});
