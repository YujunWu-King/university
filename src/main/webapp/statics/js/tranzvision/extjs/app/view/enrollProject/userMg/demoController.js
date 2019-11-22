Ext.define('KitchenSink.view.enrollProject.userMg.demoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.demoController',
    requires: [
        'Ext.ux.IFrame'
    ],

    searchComList: function(btn){
		
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_UM_USERMG_COM.TZ_DEMO_STD.CLASS_USER_BM',
			condition:
			{
				"TZ_JG_ID": Ext.tzOrgID
			},
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});
	},

});