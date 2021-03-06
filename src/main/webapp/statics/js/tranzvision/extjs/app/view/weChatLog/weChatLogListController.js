Ext.define('KitchenSink.view.weChatLog.weChatLogListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.weChatLogListController',
//    requires: [
//        'KitchenSink.view.weChatLog.weChatLogInfo'
//     ],
    query:function(btn){
    	var me = this;
    	var form = this.getView().child("form").getForm();
        var wxAppId = form.findField("appId").getValue();
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_GD_WXSERVICE_COM.TZ_GD_LOGLIST_STD.TZ_WXMSG_LOG_T',
            condition:
            {
                "TZ_JG_ID": Ext.tzOrgID ,
                "TZ_WX_APPID": wxAppId
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
	},
    
    
    getLogInfo: function(view, rowIndex) {
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 var orgId = selRec.get("jgId");
	   	 var wxAppId = selRec.get("appId");
	   	 var xH = selRec.get("XH");
	   	 var sendType = selRec.get("sendType");
	     this.getLogInfoById(orgId, wxAppId, xH, sendType);
	     },
	     
	getLogInfoById: function(orgId,wxAppId,xH,sendType){
		//alert(sendType);
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WXSERVICE_COM"]["TZ_GD_LOGINFO_STD"];
		if(sendType=='模板消息'){
			pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WXSERVICE_COM"]["TZ_GD_LOGINFOM_STD"];
		}
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

        var win = this.lookupReference('weChatLogInfo');

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
        var grid = win.child('grid');
        if(sendType=='文字消息'){
        	form.findField("mediaId").hide();
        }

        var tzParams = '{"ComID":"TZ_GD_WXSERVICE_COM","PageID":"TZ_GD_LOGINFO_STD","OperateType":"QF","comParams":{"orgId":"'+orgId+'","wxAppId":"'+wxAppId+'","XH":"'+xH+'"}}';
        if(sendType=='模板消息'){
        	tzParams = '{"ComID":"TZ_GD_WXSERVICE_COM","PageID":"TZ_GD_LOGINFOM_STD","OperateType":"QF","comParams":{"orgId":"'+orgId+'","wxAppId":"'+wxAppId+'","XH":"'+xH+'"}}';
        }
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){
        	 //资源集合信息数据
            var formData = responseData.formData;
            form.setValues(formData);
            //资源集合信息列表数据
            var roleList = responseData.listData;
            var tzStoreParams = '{"cfgSrhId": "TZ_GD_WXSERVICE_COM.TZ_GD_LOGINFO_STD.TZ_WXMSG_USER_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+orgId+'","TZ_WX_APPID-operator": "01","TZ_WX_APPID-value": "'+wxAppId+'","TZ_XH-operator": "01","TZ_XH-value": "'+xH+'"}}';
            grid.store.tzStoreParams = tzStoreParams;
            grid.store.load();     
//            if(sendType != '文字消息' && sendType != '模板消息'){
//               	var  path = form.findField("mediaId").getValue();
//               	win.down("image[name=titileImage]").setSrc(TzUniversityContextPath+path);
//            }
        });   

        win.show();
	},
	 //关闭
 	closeList:function(btn){
 		this.view.close();
 	},
 	closeList: function(btn){
 		var panel = btn.findParentByType("panel");
 		var form = panel.child("form").getForm();
 		panel.close();
 	},
	    
    
});