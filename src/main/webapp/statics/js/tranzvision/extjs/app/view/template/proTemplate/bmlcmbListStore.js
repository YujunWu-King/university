Ext.define('KitchenSink.view.template.proTemplate.bmlcmbListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.bmlcmbListStore',
    model: 'KitchenSink.view.template.proTemplate.bmlcmbModel',
    autoLoad: true,
	pageSize: 10,
    comID: 'TZ_PM_BMLCMBGL_COM',
    pageID: 'TZ_PM_BMLCMBGL_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_PM_BMLCMBGL_COM.TZ_PM_BMLCMBGL_STD.TZ_APPPRO_TMP_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()
	/*
    proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/proTemplate/bmlcmbInfo.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}
			*/

});
