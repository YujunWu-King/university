Ext.define('KitchenSink.view.security.user.userNbStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userStore',
    model: 'KitchenSink.view.security.user.userModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_AQ_NB_YHZHGL_COM',
	pageID: 'TZ_NB_YHZHGL_STD',
//	tzStoreParams: '{"cfgSrhId":"TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.TZ_YHZH_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	tzStoreParams: '{"cfgSrhId":"TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHGL_STD.TZ_YHZH_NB_VW"}',
	proxy: Ext.tzListProxy(),
	constructor: function () {
		this.tzStoreParams ='{"cfgSrhId":"TZ_AQ_NB_YHZHGL_COM.TZ_NB_YHZHGL_STD.TZ_YHZH_NB_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}';
		this.callParent();
	}
});
