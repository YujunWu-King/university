Ext.define('KitchenSink.view.basicData.import.importTplFieldStore', {
    extend: 'Ext.data.Store',
    alias: 'store.importTplFieldStore',
    model: 'KitchenSink.view.basicData.import.importTplFieldModel',
	autoLoad: false,
	pageSize: 0,
	comID: 'TZ_IMP_TPL_COM',
	pageID: 'TZ_TPL_INF_STD',
	proxy: Ext.tzListProxy(),
	listeners:{
		load:function(store, records){
			Ext.each(records,function(record){
				record.formatCheckbox();
			})
		}
	}
});
