Ext.define('KitchenSink.view.tranzApp.tranzAppController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.tranzAppController', 

    addTranzApp: function() {
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_TRANZAPP_COM"]["TZ_GD_TRANZAPP_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_TRANZAPP_STD，请检查配置。');
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
			
			cmp.on('afterrender',function(panel){
				var form = panel.child('form').getForm();
	            form.findField("jgId").setValue(Ext.tzOrgID);
			});
			
            tab = contentPanel.add(cmp);     
            contentPanel.setActiveTab(tab);
            Ext.resumeLayouts(true);
            if (cmp.floating) {
                cmp.show();
            }
    },

	deleteTranzApp: function(){
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
	editTranzApp: function() {
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
	   var jgId = selList[0].get("jgId");
	   var appId = selList[0].get("appId");
	   this.editTranzAppByID(jgId,appId);
    },
	
	editSelTranzApp: function(view, rowIndex) {
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 var jgId = selRec.get("jgId");
		 var appId = selRec.get("appId");
		 this.editTranzAppByID(jgId,appId);
    },
	
    editTranzAppByID: function(jgId,appId){
	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_TRANZAPP_COM"]["TZ_GD_TRANZAPP_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_TRANZAPP_STD，请检查配置。');
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
            //form.findField("appName").setReadOnly(true);
			//form.findField("appName").addCls("lanage_1");
			
			var userGrid = panel.child('grid[reference=tranzAppUserGrid]');
			
			//参数
			var tzParams = '{"ComID":"TZ_GD_TRANZAPP_COM","PageID":"TZ_GD_TRANZAPP_STD","OperateType":"QF","comParams":{"jgId":"'+jgId+'","appId":"'+appId+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				form.setValues(responseData.formData);
				
				var tzStoreParams = '{"jgId":"'+jgId+'","appId":"'+appId+'"}';
				userGrid.store.tzStoreParams = tzStoreParams;
				userGrid.store.load();
			});
			
		});
		
		tab = contentPanel.add(cmp);     
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);
		if (cmp.floating) {
			cmp.show();
		}	  
	},
	
	deleteSelTranzApp: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this); 
	},
	
	saveTanzAppInfos: function(btn){
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
		var tzParams = '{"ComID":"TZ_GD_TRANZAPP_COM","PageID":"TZ_GD_TAPP_MNG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		},"",true,this);
	},
	//确定
	ensureTanzAppInfos: function(btn){
		//保存信息
		this.saveTanzAppInfos(btn);
		//关闭窗口
		this.view.close();
	},
	//关闭
	closeTanzAppInfos:function(btn){
		this.view.close();
	},
	onTanzAppClose: function(btn){
		var panel = btn.findParentByType("panel");
		var form = panel.child("form").getForm();
		panel.close();
	},
	onTranAppSave: function(btn){
		//应用分配表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//表单参数
			var tzParams = this.getTranAppParams();
			var comView = this.getView();
			//用户关系
			var grid = comView.down('grid[reference=tranzAppUserGrid]');
			//用户关系数据
			var store = grid.getStore();
			
			Ext.tzSubmit(tzParams,function(responseData){
				if(responseData.success == "true"){
					comView.actType = "update";	
					//form.findField("appName").setReadOnly(true);
					
					var jgId = form.findField("jgId").getValue();
					var appId = form.findField("appId").getValue();
					var tzStoreParams = '{"jgId":"'+jgId+'","appId":"'+appId+'"}';
					store.tzStoreParams = tzStoreParams;
	                store.reload();
				}
			},"",true,this);
		}
	},
	onTranAppSure: function(btn){ 
		//应用分配表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取表单参数
			var tzParams = this.getTranAppParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				//关闭窗口						   
				comView.close();	
			},"",true,this);
		}
	},
	getTranAppParams: function(){
		//应用分配定义
		var form = this.getView().child("form").getForm();
		//应用分配定义标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";
		//新增
		if(actType == "add"){
			comParams = '"add":[{"typeFlag":"TRANZAPP","data":'+Ext.JSON.encode(form.getValues())+'}]';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			editJson = '{"typeFlag":"TRANZAPP","data":'+Ext.JSON.encode(form.getValues())+'}';
		}
		
		//用户关系
		var grid = this.getView().down('grid[reference=tranzAppUserGrid]');
		//用户关系数据
		var store = grid.getStore();
		//修改记录
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"TRANZUSER","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"TRANZUSER","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}
		}
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
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
		if(removeJson != ""){
			if(comParams == ""){
				comParams = '"delete":[' + removeJson + "]";
			}else{
				comParams = comParams + ',"delete":[' + removeJson + "]";
			}
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_TRANZAPP_COM","PageID":"TZ_GD_TRANZAPP_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
    queryTanzApp:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_GD_TRANZAPP_COM.TZ_GD_TAPP_MNG_STD.TZ_TRANZ_APP_VW',
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
	addTanzUserInfo: function(btn){
		if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存当前页面，再新增用户。");
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_TRANZAPP_COM"]["TZ_GD_TAPPUSER_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_TAPPUSER_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('tranzAppUserWindow');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为新增
		win.actType = "add";
		//应用分配信息
		var tranzAppParams = this.getView().child("form").getForm().getValues();
		//机构id
		var jgId = tranzAppParams["jgId"];
		//appid
		var appId = tranzAppParams["appId"];
        //页面注册信息表单
		var form = win.child("form").getForm();
		form.reset();
		form.findField("jgId").setValue(jgId);
		form.findField("jgId").setReadOnly(true);
		form.findField("appId").setValue(appId);
		form.findField("appId").setReadOnly(true);
        win.show();
	},
    pmtSearchUser: function(btn){
		var form = btn.findParentByType("tranzAppUserWindow").child("form").getForm();
		var jgId = form.findField("jgId").getValue();
		Ext.tzShowPromptSearch({
			recname: 'TZ_AQ_YHXX_TBL',
			searchDesc: '指定登录用户',
			maxRow:20,
			condition:{
				presetFields:{
                	TZ_JG_ID:{
                        value: jgId,
                        type: '01'
                    },
                    TZ_JIHUO_ZT:{
                        value: 'Y',
                        type: '01'
                    }
                },
				srhConFields:{
					TZ_DLZH_ID:{
						desc:'登录账号',
						operator:'07',
						type:'01'		
					},
					TZ_REALNAME:{
						desc:'用户姓名',
						operator:'07',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TZ_DLZH_ID: '登录账号',
				TZ_REALNAME: '用户姓名'	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("accountId").setValue(selection[0].data.TZ_DLZH_ID);
				form.findField("accountName").setValue(selection[0].data.TZ_REALNAME);
			}
		});	
	},
	clearPmtSearchUser: function(btn){
		var form = btn.findParentByType("tranzAppUserWindow").child("form").getForm();
		form.findField("accountId").setValue("");
		form.findField("accountName").setValue("");
		
	},
	ontranzAppUserClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	},ontranzAppUserSave: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//指定登录用户
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面信息*/
			this.saveTranzAppUserInfo(win);
		}
	},
	ontranzAppUserEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//指定登录用户
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面信息*/
			this.saveTranzAppUserInfo(win);
			//重置表单
			form.reset();
			//关闭窗口
			win.close();
		}	
	},saveTranzAppUserInfo: function(win){
		//指定登录用户
        var form = win.child("form").getForm();

		//表单数据
		var formParams = form.getValues();
		//是否启用
		if(formParams["isEnable"] == undefined){
			formParams["isEnable"] = "N";
		}
		
		var jgId = form.findField("jgId").getValue();
		var appId = form.findField("appId").getValue();
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_TRANZAPP_COM","PageID":"TZ_GD_TAPPUSER_STD","OperateType":"U","comParams":{"'+win.actType+'":['+Ext.JSON.encode(formParams)+']}}';
		var tzStoreParams = '{"jgId":"'+jgId+'","appId":"'+appId+'"}';

		var comView = this.getView();
		//用户关系
		var userGrid = comView.down('grid[reference=tranzAppUserGrid]');

		Ext.tzSubmit(tzParams,function(resp){
			win.actType = "update";
			form.findField("otherUserName").setReadOnly(true);
			form.findField("otherUserName").setFieldStyle('background:#F4F4F4');
			
			var tzStoreParams = '{"jgId":"'+jgId+'","appId":"'+appId+'"}';
			userGrid.store.tzStoreParams = tzStoreParams;
			userGrid.store.reload();
	    },"",true,this);
	},
	deleteTranzAppUser: function(btn){
		   //选中行
		   var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
		   //选中行长度
		   var checkLen = selList.length;
		   if(checkLen == 0){
				Ext.Msg.alert("提示","请选择要删除的记录");   
				return;
		   }else{
				Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
					if(btnId == 'yes'){					   
					   var store = btn.findParentByType("grid").store;
					   store.remove(selList);
					}												  
				},this);   
		   }
	},
	deleteSelTranzAppUser: function(view, rowIndex){
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var store = view.findParentByType("grid").store;
				   store.removeAt(rowIndex);
				}												  
			},this); 
	},
	editSelTranzAppUser: function(view, rowIndex){

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_TRANZAPP_COM"]["TZ_GD_TAPPUSER_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_TAPPUSER_STD，请检查配置。');
			return;
		}

		var win = this.lookupReference('tranzAppUserWindow');

		if (!win) {
			//className = 'KitchenSink.view.security.com.pageRegWindow';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			//新建类
			win = new ViewClass();
			this.getView().add(win);
		}

		//操作类型设置为更新
		win.actType = "update";
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		//jgId
		var jgId = selRec.get("jgId");
		//appId
		var appId = selRec.get("appId");
		//otherUserName
		var otherUserName = selRec.get("otherUserName");
		//参数
		var tzParams = '{"ComID":"TZ_GD_TRANZAPP_COM","PageID":"TZ_GD_TAPPUSER_STD","OperateType":"QF","comParams":{"jgId":"'+jgId+'","appId":"'+appId+'","otherUserName":"'+otherUserName+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();
		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData.formData);
			form.findField("otherUserName").setReadOnly(true);
			form.findField("otherUserName").setFieldStyle('background:#F4F4F4');
		});
		win.show();
	},
	editTranzAppUser: function(btn){
		//选中行
	    var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
	    //选中行长度
	    var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	    }
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_TRANZAPP_COM"]["TZ_GD_TAPPUSER_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_TAPPUSER_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('tranzAppUserWindow');
        
        if (!win) {
			//className = 'KitchenSink.view.security.com.pageRegWindow';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		//jgId
		var jgId = selList[0].get("jgId");
		//appId
		var appId = selList[0].get("appId");
		//otherUserName
		var otherUserName = selList[0].get("otherUserName");
		
		//参数
		var tzParams = '{"ComID":"TZ_GD_TRANZAPP_COM","PageID":"TZ_GD_TAPPUSER_STD","OperateType":"QF","comParams":{"jgId":"'+jgId+'","appId":"'+appId+'","otherUserName":"'+otherUserName+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();
		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData.formData);
			form.findField("otherUserName").setReadOnly(true);
			form.findField("otherUserName").setFieldStyle('background:#F4F4F4');
		});
        win.show();
	}
});