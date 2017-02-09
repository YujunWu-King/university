Ext.define('KitchenSink.view.template.survey.testQuestion.backXXXStore', {
    extend: 'Ext.data.Store',
    alias: 'store.backXXXStore',
    model: 'KitchenSink.view.template.survey.testQuestion.backXXXModel',
	autoLoad: true,
	pageSize:20,
	comID: 'TZ_CSWJ_LIST_COM',
	pageID: 'TZ_CSWJ_XXX_STD',
	//tzStoreParams: '{"TZ_CS_WJ_ID":""}',
	proxy: Ext.tzListProxy()
});