Ext.define('KitchenSink.view.weChatLog.weChatLogListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.weChatLogListController',
//    requires: [
//        'KitchenSink.view.weChatLog.weChatLogInfo'
//     ],
    
    getLogInfo: function(view, rowIndex) {
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 var orgId = selRec.get("jgId");
	   	 var wxAppId = selRec.get("appId");
	   	 var xH = selRec.get("XH");
	   	 alert(orgId+'jia'+wxAppId+'jia'+xH);
	     this.getLogInfoById(orgId, wxAppId, xH);
	     },
	     
	getLogInfoById: function(orgId,wxAppId,xH){
	    //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WXSERVICE_COM"]["TZ_GD_LOGINFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_LOGINFO_STD，请检查配置。');
            return;
        }

        var win = this.lookupReference('comPageWindow');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";

        var form = win.child("form").getForm();
        var pageGrid = Ext.getCmp('pageGrid');
        var processGrid = Ext.getCmp('processGrid');
        var grid = form.child('grid');
//        form.setValues(
//            [
//                {id:'comID', value:orgId},
//                {id:'comName', value:wxAppId},
//                {id:'comName', value:xH}
//            ]
//        );
        var tzStoreParams = '{"jgId":"'+orgId+'","appId":"'+wxAppId+'","XH":"'+xH+'"}';

        pageGrid.store.tzStoreParams = tzStoreParams;
        pageGrid.store.load();
        grid.store.tzStoreParams = '{"cfgSrhId":"TZ_GD_WXSERVICE_COM.TZ_GD_LOGINFO_STD.TZ_PROCESS_DF_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'","TZ_ZCZJ_ID-operator": "01","TZ_ZCZJ_ID-value": "' + comID + '"}}';;;
        grid.store.load();

        win.show();
	}
    
});