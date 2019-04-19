Ext.define('KitchenSink.view.viewByPro.classListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.classListStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.classModel',
	autoLoad: false,
    pageSize: 200,
    comID: 'TZ_BY_PRO_COM',
    pageID: 'TZ_CLASS_LIST_PAGE',
    //tzStoreParams:'{"cfgSrhId": "TZ_BY_PRO_COM.TZ_CLASS_LIST_PAGE.TZ_CLASS_OPR_V","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'+TranzvisionMeikecityAdvanced.Boot.loginUserId+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'","TZ_IS_APP_OPEN-operator":"01","TZ_IS_APP_OPEN-value":["Y"]}}',
    tzStoreParams:'{"cfgSrhId": "TZ_BY_PRO_COM.TZ_CLASS_LIST_PAGE.TZ_BMBSH_ECUST_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'"}}',
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
