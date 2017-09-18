Ext.define('KitchenSink.view.audienceManagement.audienceManagementController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.audienceManagementController', 
//新增听众
addComRegInfo: function() {
				//是否有访问权限
				var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUD_COM"]["TZ_AUD_PANEL_STD"];
				if( pageResSet == "" || pageResSet == undefined){
					Ext.MessageBox.alert('提示', '您没有修改数据的权限');
					return;
				}
				//该功能对应的JS类
				var className = pageResSet["jsClassName"];
				if(className == "" || className == undefined){
					Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUD_NEW_STD，请检查配置。');
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
			    cmp.on('afterrender',function(panel){
			    	
			    	
							var cmpForm = panel.child("form").getForm();
							//新建时，隐藏保存按钮
							var cmpB3=panel.down("button[reference=audSave]").setVisible(false);
						
					});
				
				
				tab = contentPanel.add(cmp);     
				
				contentPanel.setActiveTab(tab);

				Ext.resumeLayouts(true);

				if (cmp.floating) {
					cmp.show();
				}
		    },

    
//修改听众   
    editComRegInfo: function() {
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
	   //组件ID
	   var comID = selList[0].get("comID");
	   //显示组件注册信息编辑页面
	   this.editComRegIntoByID(comID);
    },
//删除一条听众    
	deleteComRegInfo: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
//删除选中听众  	
	deleteComRegInfos: function(btn){
	   //选中行
		
		var win = this.lookupReference('newAudWindow');
		
				if (!win) {
					className = 'KitchenSink.view.security.com.pageRegWindow';
					Ext.syncRequire(className);
					ViewClass = Ext.ClassManager.get(className);
					//新建类
					win = new ViewClass();
					this.getView().add(win);
				}
	
	   var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
	   

	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){		
				var gridStore =btn.findParentByType("grid").getStore();
				   var store = this.getView().store;
				   gridStore.remove(selList);
				}												  
			},this);   
	   }
	},
	
	editSelComRegInfo: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //ID
	   	var audId = selRec.get("audId");
	   	var audName = selRec.get("audName");
		var audStat = selRec.get("audStat");
		var audType = selRec.get("audType");
		var audMS = selRec.get("audMS");
		var audSQL = selRec.get("audSQL");
		var audLY = selRec.get("audLY");
	     //显示组件注册信息编辑页面
	     this.editComRegIntoByID(audId,audName,audStat,audType,audMS,audSQL,audLY);
	},
	editComRegIntoByID: function(audId,audName,audStat,audType,audMS,audSQL,audLY){
		//是否有访问权限
		
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUD_COM"]["TZ_AUD_PANEL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUD_PANEL_STD，请检查配置。');
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
			// <debug warn>
			// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
			// </debug>
		}

		cmp = new ViewClass();
		//操作类型设置为更新
		cmp.actType = "update";
		
		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			//页面注册信息列表
			var gridStore =panel.child('grid').getStore();
			//参数
			var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"'+audId+'","audName":"'+audName+'","audStat":"'+audStat+'","audType":"'+audType+'","audMS":"'+audMS+'","audSQL":"'+audSQL+'","audLY":"'+audLY+'"}}';

			//加载数据
			var tzStoreParams =  '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "'+ audId+'"}}';
			
			Ext.tzLoad(tzParams,function(responseData){
			
				form.setValues(responseData);
			
				//页面注册信息列表数据
				var roleList = responseData.listData;
				gridStore.tzStoreParams = tzStoreParams;
				gridStore.reload();
				
			});
			
			
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},

//保存	
	saveComRegInfos: function(btn){
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
		};
		
        var comParams = "";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
		
		//保存数据
        if(comParams!=""){
            Ext.tzSubmit(tzParams,function(){
                store.reload();
            },"",true,this);
        }

	},
	
	
	addPageRegInfo: function(btn){
		
		if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存组件注册信息后，再新增页面注册信息。");
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUD_COM"]["TZ_AUD_NEW_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUD_NEW_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('newAudWindow');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为新增
		win.actType = "add";
		
		//组件注册信息
		
		var form = win.child("form").getForm();
	
		var comRegParams = win.child("form").getForm().getValues();
		
		
		//组件ID
		var comID = comRegParams["comID"];
		var audID = comRegParams["audID"];
		var audStat = comRegParams["audStat"];
		
		
        //页面注册信息表单
		var form = win.child("form").getForm();
		form.reset();
		form.findField("audID").setValue(audID);
        win.show();
	},
	
	
	editPageRegInfo: function(btn){
		
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
			
			//组件ID
			var audId = selList[0].get("audId");
			var audName = selList[0].get("audName");
			var audStat = selList[0].get("audStat");
			var audType = selList[0].get("audType");
			var audMS = selList[0].get("audMS");
			var audSQL = selList[0].get("audSQL");
			var audLY = selList[0].get("audLY");
			
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUD_COM"]["TZ_AUD_PANEL_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUD_PANEL_STD，请检查配置。');
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
				// <debug warn>
				// Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
				if (!clsProto.themeInfo) {
					Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
						themeName + '\'. Is this intentional?');
				}
				// </debug>
			}

			cmp = new ViewClass();
			//操作类型设置为更新
			cmp.actType = "update";
			
			cmp.on('afterrender',function(panel){
				//组件注册表单信息;
				var form = panel.child('form').getForm();

				//页面注册信息列表
				var gridStore =panel.child('grid').getStore();
				//参数
				var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"'+audId+'","audName":"'+audName+'","audStat":"'+audStat+'","audType":"'+audType+'","audMS":"'+audMS+'","audSQL":"'+audSQL+'","audLY":"'+audLY+'"}}';

				//加载数据
				var tzStoreParams =  '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "'+ audId+'"}}';
				
				Ext.tzLoad(tzParams,function(responseData){
					//组件注册信息数据
				
					form.setValues(responseData);
				
					//页面注册信息列表数据
					var roleList = responseData.listData;	
					gridStore.tzStoreParams = tzStoreParams;
					gridStore.reload();
					
				});
				
				
			});
			
			tab = contentPanel.add(cmp);     
			
			contentPanel.setActiveTab(tab);

			Ext.resumeLayouts(true);

			if (cmp.floating) {
				cmp.show();
			}
		
	},
	deletePageRegInfos: function(btn){
	   //页面注册信息列表
	   var grid = this.getView().child("grid");
		//选中行
	   var selList = grid.getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var store = grid.store;
				   store.remove(selList);
				}												  
			},this);   
	   }
	},
	onComRegSave: function(btn){
		//组件注册表单
		var form = this.getView().child("form").getForm();
	
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getComRegInfoParams();
			var comView = this.getView();
			//页面注册信息列表
			var grid = comView.child("grid");
			//页面注册信息数据
			var store = grid.getStore();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
                if(store.isLoaded()){
                    store.reload();
                }
			},"",true,this);
			
		}
	},
	onComRegEnsure: function(btn){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getComRegInfoParams();
			var comView = this.getView();
		
			Ext.tzSubmit(tzParams,function(responseData){
				//关闭窗口						   
				comView.close();
			
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
			    contentPanel.child("audienceManagementDa").store.reload();
		
			},"",true,this);
		}
		
	},
	getComRegInfoParams: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		
		//更新操作参数
		var comParams = "";
		//新增
		if(actType == "add"){
			comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			editJson = '{"typeFlag":"FORM","data":'+Ext.JSON.encode(form.getValues())+'}';
		}
		
		//页面注册信息列表
		var grid = this.getView().child("grid");
		//页面注册信息数据
		var store = grid.getStore();
		//修改记录
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"GRID","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"GRID","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
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
		var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{'+comParams+'}}';
    
		return tzParams;
	},
	onComRegClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
	onPageRegSave: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面注册信息*/
		
			this.savePageRegInfo(win);
		}
	},
	onPageRegEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面注册信息*/
			this.savePageRegInfo(win);
			//重置表单
			form.reset();
			//关闭窗口
			win.close();
		}	
	},
	savePageRegInfo: function(win,view){
	
        //信息表单
        var form = win.child("form").getForm();
        var gridStore =win.child("form").child("grid").getStore();
		var selList = win.child("form").child("grid").getSelectionModel().getSelection();
	
		var removeJson = "";
		//删除记录
		var removeRecs = gridStore.getRemovedRecords();
							  	
		for(var i=0;i<removeRecs.length;i++){
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
			}
		};
		
		 var comParams = "";
			if(removeJson != ""){
				comParams = '"delete":[' + removeJson + "]";
			}
			
			var tzParams2 = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{'+comParams+'}}';
		
			//保存数据
	        if(comParams!=""){
	            Ext.tzSubmit(tzParams2,function(){
	            	gridStore.reload();
	            },"",true,this);
	        }

		var	comParamsALL = '"update":[{"typeFlag":"FORM","data":'+Ext.JSON.encode(form.getValues())+'}]';
			
		var actType=win.actType;
		
		
		 //表格数据
		var updateJson = "";
		var updateRecs = gridStore.getUpdatedRecords();
		
		
		for(var i=0;i<updateRecs.length;i++){
			if(updateJson == ""){
				updateJson = '{"typeFlag":"GRID","data":'+Ext.JSON.encode(updateRecs[i].data)+'}';
			}else{	
				updateJson = updateJson + ',{"typeFlag":"GRID","data":'+Ext.JSON.encode(updateRecs[i].data)+'}';
			}
		};

		 var comParams3 = "";
			if(updateJson != ""){
		
				comParamsALL = comParamsALL+',"update":[' + updateJson + "]";
			
			}

		var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{'+comParamsALL+'}}';

		var pageGrid = this.getView();

		Ext.tzSubmit(tzParams,function(resp){
			win.actType = "update";

			pageGrid.store.reload();
	    },"",true,this);
	},
	
	
	
	onPageRegClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	},
//查询窗口	
	searchComList: function(btn){
		Ext.tzShowCFGSearch({
		
			cfgSrhId: 'TZ_AUD_COM.TZ_AUD_LIST_STD.PS_TZ_AUDCX_VW',
			condition:
			{
				"TZ_JG_ID": Ext.tzOrgID
			},
			
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load(				
				);
			}
		});	
	},
	
	searchList: function(btn){
		
		var form = this.getView().child("form").getForm();
		//表单数据
			var formParams = form.getValues();
			
			var audChildID =formParams["audID"];
		
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW',
			condition:
			{
				"TZ_AUD_ID": audChildID
			},
		
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load(				
				);
			}
		});	
	},
	
	
	
	closeComRegInfos: function(btn){
		//关闭
		var grid = btn.findParentByType("grid");
		grid.close();
	},
	//确定
	ensureComRegInfos:function(btn) {
		this.saveComRegInfos(btn);
		this.closeComRegInfos(btn);
	},
	
	editPageRegInfoOne: function(view, rowIndex){

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUD_COM"]["TZ_AUD_NEW_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUD_NEW_STD，请检查配置。');
			return;
		}

		var win = this.lookupReference('pageRegWindow');

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
		//组件ID
		
		var audId = selRec.get("audId");
		var audName = selRec.get("audName");
		var audStat = selRec.get("audStat");
		var audType = selRec.get("audType");
		var audMS = selRec.get("audMS");
		var audSQL = selRec.get("audSQL");



		//参数
		var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"'+audId+'","audName":"'+audName+'","audStat":"'+audStat+'","audType":"'+audType+'","audMS":"'+audMS+'","audSQL":"'+audSQL+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();

		
		var gridStore =win.child("form").child("grid").getStore();

		var tzStoreParams =  '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "'+ audId+'"}}';

		
		Ext.tzLoad(tzParams,function(responseData){

			form.setValues(responseData);

			gridStore.tzStoreParams = tzStoreParams;
			gridStore.reload();
			
		});
		

		win.show();
	},

	editPageRegInfoTwo: function(view, rowIndex){

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUD_COM"]["TZ_AUD_NEW_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUD_NEW_STD，请检查配置。');
			return;
		}

		var win = this.lookupReference('pageRegWindow');

		if (!win) {
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
		//组件ID
	
		var audId = selRec.get("audId");
		var audName = selRec.get("audName");
		var audStat = selRec.get("audStat");
		var audType = selRec.get("audType");
		var audMS = selRec.get("audMS");
		var audSQL = selRec.get("audSQL");

		//参数
		var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"'+audId+'","audName":"'+audName+'","audStat":"'+audStat+'","audType":"'+audType+'","audMS":"'+audMS+'","audSQL":"'+audSQL+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();

		var gridStore =win.child("form").child("grid").getStore();
		
		var tzStoreParams =  '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "'+ audId+'"}}';


		
		Ext.tzLoad(tzParams,function(responseData){

			form.setValues(responseData);
			gridStore.tzStoreParams = tzStoreParams;
			gridStore.reload();
			
		});
	
		win.show();
	},
	
	
//重新产生听众	
	freshAudMember: function(btn){
		

		//页面注册信息表单
		var form = this.getView().child("form").getForm();
		//表单数据
			var formParams = form.getValues();
			var audSQL =formParams["audSQL"];
			var audID =formParams["audID"];

			if(audSQL==""){
				Ext.MessageBox.alert("提示","请先填写SQL信息后，再执行操作。");
			}else{
			 var removeAud =Ext.JSON.encode(audID); 
			 var comParams = "";
				if(removeAud != ""){
					//		 	 "delete":[{"audID":"4","audDxzt":"1"}]	
					comParams = '"delete":[{"audID":' + removeAud + ',"audDxzt":"A"}]';
				}


	 var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"tzOther","comParams":{"audSQL":"'+audSQL+'","audID":"'+audID+'"}}';
 
	 var tzStoreParams =audSQL;
		 

		var gridStore = this.getView().child("grid").getStore();	
	
		Ext.tzLoad(tzParams,function(responseData){
			
			gridStore.reload();
					
				});
		
			}
}
	
})