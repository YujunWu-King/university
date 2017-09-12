Ext.define('KitchenSink.view.weChatServiceManagement.weChatServiceController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.weChatServiceController', 

    add: function() {
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WXSERVICE_COM"]["TZ_GD_FWHINFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_FWHINFO_STD，请检查配置。');
			return;
		}
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		//className = 'KitchenSink.view.security.com.comInfoPanel';
		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}	
		ViewClass = Ext.ClassManager.get(className);
		clsProto = ViewClass.prototype;

		if (clsProto.themes) {
			clsProto.themeInfo = clsProto.themes[themeName];

			if (themeName === 'gray') {
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
			} else if (themeName !== 'neptune' && themeName !== 'classic') {
				if (themeName === 'crisp-touch') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
				}
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
			}
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
		}	
            cmp = new ViewClass();
			cmp.actType = "add";
            tab = contentPanel.add(cmp);     
            contentPanel.setActiveTab(tab);
            Ext.resumeLayouts(true);
            if (cmp.floating) {
                cmp.show();
            }
    },

    deleteMul: function(){
	   //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.getView().store;
				   store.remove(selList);
				}												  
			},this);   
	   }
	},
	edit: function() {
	  //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	   }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	   }
	   var orgId = selList[0].get("orgId");
	   var weChatId = selList[0].get("wxId");
	   this.editInfoByID(orgId, weChatId);
    },
	
	editSel: function(view, rowIndex) {
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 var orgId = selRec.get("orgId");
	   	 var weChatId = selRec.get("wxId");
	     this.editInfoByID(orgId, weChatId);
    },
	
	editInfoByID: function(orgId,weChatId){
	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WXSERVICE_COM"]["TZ_GD_FWHINFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_FWHINFO_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');
		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}	
		ViewClass = Ext.ClassManager.get(className);
		clsProto = ViewClass.prototype;

		if (clsProto.themes) {
			clsProto.themeInfo = clsProto.themes[themeName];

			if (themeName === 'gray') {
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
			} else if (themeName !== 'neptune' && themeName !== 'classic') {
				if (themeName === 'crisp-touch') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
				}
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
			}
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
		}
		cmp = new ViewClass();
		//操作类型设置为更新
		cmp.actType = "update";
		cmp.on('afterrender',function(panel){
			
			var form = panel.child('form').getForm();
            form.findField("wxId").setReadOnly(true);
			form.findField("wxId").addCls("lanage_1");
			//参数
			var tzParams = '{"ComID":"TZ_GD_WXSERVICE_COM","PageID":"TZ_GD_FWHINFO_STD","OperateType":"QF","comParams":{"orgId":"'+orgId+'","weChatId":"'+weChatId+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				form.setValues(responseData.formData);	
			});
			
		});
		
		tab = contentPanel.add(cmp);     
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);
		if (cmp.floating) {
			cmp.show();
		}	  
	},
	
	deleteSel: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this); 
	},
	
	saveList: function(btn){
		//组件注册信息列表
		var grid = btn.findParentByType("grid");
		//组件注册信息数据
		var store = grid.getStore();
		//删除json字符串
		var removeJson = "";
		//删除记录
		var removeRecs = store.getRemovedRecords();

		for(var i=0;i<removeRecs.length;i++){
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
			}
		}
		var comParams = "";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_WXSERVICE_COM","PageID":"TZ_GD_FWHLIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		},"",true,this);
	},
	//确定
	ensureList: function(btn){
		//保存信息
		this.saveList(btn);
		//关闭窗口
		this.view.close();
	},
	//关闭
	closeList:function(btn){
		this.view.close();
	},
	closeInfo: function(btn){
		var panel = btn.findParentByType("panel");
		var form = panel.child("form").getForm();
		panel.close();
	},
	
	saveInfo: function(btn){
		var panel = btn.findParentByType("panel");
		//操作类型，add-添加，edit-编辑
		var actType = panel.actType;
		var form = panel.child("form").getForm();
		if (form.isValid()){
			//新增
			var comParams="";
			if(actType == "add"){
				comParams = '"add":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}else{
				//修改
				comParams = '"update":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}
			var tzParams = '{"ComID":"TZ_GD_WXSERVICE_COM","PageID":"TZ_GD_FWHINFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData){
				panel.actType = "update";	
				form.findField("wxId").setReadOnly(true);
				form.findField("wxId").addCls('lanage_1');
				var listPanel = Ext.ComponentQuery.query("panel[reference=weChatList]");
				listPanel[0].getStore().reload();
			},"",true,this);
		}
	},
	
	esureInfo: function(btn){ 
		var panel = btn.findParentByType("panel");
		var form = panel.child("form").getForm();
		if (form.isValid()) {
			this.saveInfo(btn);
			panel.close();
		}	
	},
	query:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_GD_WXSERVICE_COM.TZ_GD_FWHLIST_STD.TZ_WX_APPSE_VW',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
	},
	//进入用户管理
	userManage:function(view,rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var jgId = selRec.get("orgId");
		var wxAppId = selRec.get("wxId");

		Ext.tzSetCompResourses("TZ_WX_USER_COM");
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_WX_USER_COM"]["TZ_WX_USER_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_WX_USER_STD，请检查配置。');
			return;
		}

		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;

		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		contentPanel.body.addCls('kitchensink-example');

		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}
		ViewClass = Ext.ClassManager.get(className.toString());
		// console.log(className,ViewClass);
		clsProto = ViewClass.prototype;

		if (clsProto.themes) {
			clsProto.themeInfo = clsProto.themes[themeName];

			if (themeName === 'gray') {
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
			} else if (themeName !== 'neptune' && themeName !== 'classic') {
				if (themeName === 'crisp-touch') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
				}
				clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
			}
			// <debug warn>
			// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
			if (!clsProto.themeInfo) {
				Ext.log.warn('Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
			// </debug>
		}

		var render = function(initialData) {

			cmp = new ViewClass({
				initialData:initialData
			});

			cmp.on('afterrender', function () {
				cmp.jgId = jgId;
				cmp.wxAppId = wxAppId;

				var userMgStore = cmp.getStore();

				//默认显示关注日期为当前日期
				var nowDate = new Date();
				var year = nowDate.getFullYear();
				var month = "00" + (nowDate.getMonth() + 1);
				month = month.substr(month.length - 2, 2);
				var day = nowDate.getDate();

				var nowDateStr = year + "-" + month + "-" + day;

				var tzStoreParams = '{"cfgSrhId": "TZ_WX_USER_COM.TZ_WX_USER_STD.TZ_WX_USER_VW",' +
					'"condition":{"TZ_JG_ID-operator":"01","TZ_JG_ID-value":"' + jgId + '","TZ_WX_APPID-operator":"01","TZ_WX_APPID-value":"' + wxAppId + '","TZ_SUBSRIBE_DATE-operator":"01","TZ_SUBSRIBE_DATE-value":"' + nowDateStr + '"}}';

				userMgStore.tzStoreParams = tzStoreParams;
				userMgStore.load();

			});

			tab = contentPanel.add(cmp);

			contentPanel.setActiveTab(tab);

			Ext.resumeLayouts(true);

			if (cmp.floating) {
				cmp.show();
			}
		};

		var preQueryForTagMenu = [];

		var time = 1;
		var beforeRender = function() {
			time--;
			if(time==0) {
				render({
					tagData:preQueryForTagMenu
				});
			}
		}

		//获取当前公众账号下的所有标签信息，用于预查询下的按类别标签查询
		var tzParams = '{"ComID":"TZ_WX_USER_COM","PageID":"TZ_WX_USER_STD","OperateType":"tzGetTag","comParams":{"jgId":"' + jgId + '","wxAppId":"' + wxAppId + '"}}';
		Ext.tzLoad(tzParams, function (responseData) {
			var tagList = responseData.tagList;
			for (var i = 0; i < tagList.length; i++) {
				var tagId = tagList[i].tagId;
				var tagName = tagList[i].tagName;

				preQueryForTagMenu.push({
					text: tagName,
					name: tagId,
					handler: 'queryForTag'
				});
			}

			beforeRender();
		});
	},
	//全量获取用户
	getUserAll:function(view,rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var jgId = selRec.get("orgId");
		var wxAppId = selRec.get("wxId");

		var tzParams = '{"ComID":"TZ_WX_USER_COM","PageID":"TZ_WX_USER_STD","OperateType":"tzGetUserAllByAe","comParams":{"jgId":"' + jgId + '","wxAppId":"' + wxAppId + '"}}';

		Ext.tzSubmit(tzParams,function(responseData){

		},"调用获取用户进程成功",true,this);
	},
	getLog: function(view, rowIndex) {
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 var orgId = selRec.get("orgId");
	   	 var wxAppId = selRec.get("wxId");
	     this.getLogById(orgId, wxAppId);
	     },
	getLogById: function(orgId,wxAppId){
		//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WXSERVICE_COM"]["TZ_GD_LOGLIST_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_LOGLIST_STD，请检查配置。');
				return;
			}
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
			contentPanel.body.addCls('kitchensink-example');
			if(!Ext.ClassManager.isCreated(className)){
				Ext.syncRequire(className);
			}	
			ViewClass = Ext.ClassManager.get(className);
			clsProto = ViewClass.prototype;
			
			if (clsProto.themes) {
				clsProto.themeInfo = clsProto.themes[themeName];

				if (themeName === 'gray') {
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
				} else if (themeName !== 'neptune' && themeName !== 'classic') {
					if (themeName === 'crisp-touch') {
						clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
					}
					clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
				}
				if (!clsProto.themeInfo) {
					Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
						themeName + '\'. Is this intentional?');
				}
			}
			   cmp = new ViewClass();
		        //操作类型设置为更新
		        cmp.actType = "update";

		        cmp.on('afterrender',function(panel){
		            //许可权表单信息;
		            var form = panel.child('form').getForm();
		            form.findField("jgId").setReadOnly(true);
		            form.findField("jgId").setFieldStyle('background:#F4F4F4');
		            form.findField("appId").setReadOnly(true);
		            form.findField("appId").setFieldStyle('background:#F4F4F4');
		            //授权组件列表
		            var grid = panel.child('grid');
		            //参数
		            var tzParams = '{"ComID":"TZ_GD_WXSERVICE_COM","PageID":"TZ_GD_LOGLIST_STD","OperateType":"QF","comParams":{"orgId":"'+orgId+'","wxAppId":"'+wxAppId+'"}}';
		            //加载数据
		            Ext.tzLoad(tzParams,function(responseData){
		            	 //资源集合信息数据
		                var formData = responseData.formData;
		                form.setValues(formData);
		                //资源集合信息列表数据
		                var roleList = responseData.listData;

		                var tzStoreParams = '{"cfgSrhId": "TZ_GD_WXSERVICE_COM.TZ_GD_LOGLIST_STD.TZ_WXMSG_LOG_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+orgId+'","TZ_WX_APPID-operator": "01","TZ_WX_APPID-value": "'+wxAppId+'"}}';
		                grid.store.tzStoreParams = tzStoreParams;
		                grid.store.load();     
		            });   

		        });

		        tab = contentPanel.add(cmp);

		        contentPanel.setActiveTab(tab);

		        Ext.resumeLayouts(true);

		        if (cmp.floating) {
		            cmp.show();
		        }
	}
});