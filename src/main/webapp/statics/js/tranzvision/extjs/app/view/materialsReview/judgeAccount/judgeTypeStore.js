Ext.define('KitchenSink.view.materialsReview.judgeAccount.judgeTypeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.judgeTypeStore',
    model: 'KitchenSink.view.materialsReview.judgeAccount.judgeTypeModel',
	//autoLoad: true,
	pageSize: 5,
	comID: 'TZ_JUDGE_ACC_COM',
	pageID: 'TZ_JUDINFO_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
