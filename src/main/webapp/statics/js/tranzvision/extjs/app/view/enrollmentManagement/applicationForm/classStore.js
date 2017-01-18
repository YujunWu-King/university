Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classStore', {
    extend: 'Ext.data.Store',
    alias: 'store.appFormClassStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.classModel',
	autoLoad: false,
    pageSize: 30,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_BMGL_CLASS_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.TZ_CLASS_OPR_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'+TranzvisionMeikecityAdvanced.Boot.loginUserId+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'","TZ_IS_APP_OPEN-operator":"01","TZ_IS_APP_OPEN-value":["Y"]}}',
    proxy: Ext.tzListProxy(),
    sorters:[
        {
            direction:'DESC',
            property:'classID'
        },
		{
            direction:'ASC',
            property:'batchID'
        }
    ]
});
