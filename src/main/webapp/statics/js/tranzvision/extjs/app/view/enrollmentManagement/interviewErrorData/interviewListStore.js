//面试异常数据处理-班级批次列表  默认首页GRID
Ext.define('KitchenSink.view.enrollmentManagement.interviewErrorData.interviewListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewListStore',
    model: 'KitchenSink.view.enrollmentManagement.interviewErrorData.interviewListModel',
	autoLoad: false,
    pageSize: 30,
    comID: 'TZ_MSYCSJ_COM',
    pageID: 'TZ_MSYCSJLIST_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_MSYCSJ_COM.TZ_MSYCSJLIST_STD.TZ_BMBSH_ECUST_VW","condition":{"TZ_DLZH_ID-operator": "01","TZ_DLZH_ID-value": "'+TranzvisionMeikecityAdvanced.Boot.loginUserId+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'","TZ_IS_APP_OPEN-operator":"01","TZ_IS_APP_OPEN-value":"Y"}}',
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
