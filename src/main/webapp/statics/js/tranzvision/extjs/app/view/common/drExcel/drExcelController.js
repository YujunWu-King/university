Ext.define('KitchenSink.view.common.drExcel.drExcelController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.drExcel',
    ondrExcelInfoClose: function(){
		this.getView().close();
	},
	//查询
	cfgSearchExcel: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_GD_FILTER_COM.TZ_IMPORTEXCEL_STD.TZ_EXCEL_DRXX_V',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
});