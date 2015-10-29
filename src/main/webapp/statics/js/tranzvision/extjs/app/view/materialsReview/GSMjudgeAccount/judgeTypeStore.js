Ext.define('KitchenSink.view.materialsReview.GSMjudgeAccount.judgeTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.judgeTypeStore',
    model: 'KitchenSink.view.materialsReview.GSMjudgeAccount.judgeTypeModel',
	//autoLoad: true,
	pageSize: 5,
	comID: 'TZ_GSM_JUDGE_ACC_COM',
	pageID: 'TZ_GSM_JUDINFO_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});




