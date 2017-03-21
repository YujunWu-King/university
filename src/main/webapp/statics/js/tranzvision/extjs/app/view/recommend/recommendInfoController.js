Ext.define('KitchenSink.view.recommend.recommendInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.recommendCon', 

	//可配置搜索
	cfgSearchAct: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.TZ_GD_TJRINF_VW',
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
	viewLetter: function(view,rowIndex){
		
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var letterId = selRec.get("refLetterId");
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_HD_MANAGER_COM"]["TZ_HD_SJ_VIEW"];
		if( pageResSet == "" || typeof(pageResSet) == "undefined" ){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || typeof(className) == "undefined"  ){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_MENUADD_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('activityCodePanel');
	    if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			//新建类
			win = new ViewClass();
			this.getView().add(win);
			win.title = "手机版活动详情页发布地址";    
	    }
	    win.on('afterrender',function(panel){
	    	var codeForm = panel.child("form").getForm();
	    	 //参数
				var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_SJ_VIEW","OperateType":"QF","comParams":{"activityId":"'+activityId+'","siteId":"'+siteId+'","coluId":"'+columnId+'"}}';
				
				//加载数据
				Ext.tzLoad(tzParams,function(responseData){
					var formData = responseData.formData;
					codeForm.setValues(formData);
					panel.down('image[name=codeImage]').setSrc(TzUniversityContextPath + formData.codeImage);	
					
				});
	    });
	    win.show();
	}
	onComRegClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
});
