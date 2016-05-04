Ext.define('KitchenSink.view.batch.batchServerDefnMngController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.batchServerDefnMngController', 
    addBatchServerDefnInfo: function() {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BATCH_SERVER_COM"]["TZ_BATCH_SERVD_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_BATCH_SERVD_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		//className = 'KitchenSink.view.security.com.comInfoPanel';
		console.log(className);
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
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
    },
    editBatchServerDefnInfo: function() {
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
	   //机构id，进程名称
	   var orgId = selList[0].get("orgId");
	   var batchServerName = selList[0].get("batchServerName");
	   //显示组件注册信息编辑页面
	   this.editBatchServerDefnInfoByID(orgId,batchServerName);
    },
	deleteBatchServerDefnInfo: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	deleteBatchServerDefnInfos: function(){
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
	editSelBatchServerDefnInfo: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //组件ID
	   	 var orgId = selRec.get("orgId");
	   	 var batchServerName = selRec.get("batchServerName");
	     //显示组件注册信息编辑页面
	     this.editBatchServerDefnInfoByID(orgId,batchServerName);
	},
	editBatchServerDefnInfoByID: function(orgId,batchServerName){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BATCH_SERVER_COM"]["TZ_BATCH_SERVD_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_BATCH_SERVD_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, ViewClass, clsProto;
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
			form.findField("orgId").setReadOnly(true);
			form.findField("orgId").setFieldStyle('background:#F4F4F4');
			form.findField("batchServerName").setReadOnly(true);
			form.findField("batchServerName").setFieldStyle('background:#F4F4F4');

			//参数
			var tzParamsJson = {
				"ComID":"TZ_BATCH_SERVER_COM",
				"PageID":"TZ_BATCH_SERVD_STD",
				"OperateType":"QF",
				"comParams":{
					"orgId": orgId,
					"batchServerName": batchServerName
				}
			};
			
			//参数
			var tzParams = Ext.JSON.encode(tzParamsJson);
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);				
			});
			
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	saveDeleteBatchServerDefnInfos: function(btn){
		//进程信息列表
		var grid = btn.findParentByType("grid");
		//进程信息数据
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
        var comParams = '"delete":[' + removeJson + "]";

		//提交参数
		var tzParams = '{"ComID":"TZ_BATCH_SERVER_COM","PageID":"TZ_BATCH_SERVD_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        if(comParams!=""){
            Ext.tzSubmit(tzParams,function(){
                store.reload();
            },"",true,this);
        }

	},
	onBatchServerDefnSave: function(btn){
		//进程服务器表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取进程服务器表单参数
			var tzParams = this.getBatchDefnInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
				form.findField("orgId").setReadOnly(true);
				form.findField("orgId").setFieldStyle('background:#F4F4F4');
				form.findField("batchServerName").setReadOnly(true);
				form.findField("batchServerName").setFieldStyle('background:#F4F4F4');
			},"",true,this);
		}
	},
	onBatchServerDefnEnsure: function(btn){
		//进程服务器表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取进程服务器表单参数
			var tzParams = this.getBatchDefnInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				//关闭窗口						   
				comView.close();	
			},"",true,this);
		}
	},
	getBatchDefnInfoParams: function(){
		//进程服务器表单
		var form = this.getView().child("form").getForm();
		//进程服务器表单标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";
		//新增
		if(actType == "add"){
			comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		}else{
			comParams = '"update":['+Ext.JSON.encode(form.getValues())+']';
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_BATCH_SERVER_COM","PageID":"TZ_BATCH_SERVM_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	onBatchServerDefnClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
	searchBatchServerDefnList: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_BATCH_SERVER_COM.TZ_BATCH_SERVM_STD.TZ_JC_FWQDX_T',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
	closeBatchServerDefnMng: function(btn){
		//关闭
		var grid = btn.findParentByType("grid");
		grid.close();
	},
	//确定
	ensureDeleteBatchServerDefnInfos:function(btn) {
		this.saveDeleteBatchServerDefnInfos(btn);
		this.closeBatchServerDefnMng(btn);
	},
	startBatchServer: function(bt){
		//进程服务器表单
		var form = this.getView().child("form").getForm();
		form.findField("yxzt").setValue("STARTING");
		if (form.isValid()) {
			//进程服务器表单参数
			var tzParams = this.getBatchDefnInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
				form.findField("orgId").setReadOnly(true);
				form.findField("orgId").setFieldStyle('background:#F4F4F4');
				form.findField("batchServerName").setReadOnly(true);
				form.findField("batchServerName").setFieldStyle('background:#F4F4F4');
				form.findField("yxzt").setValue("RUNNING");
				comView.commitChanges(comView);
			},"",true,this);
		}
	},
	stopBatchServer: function(bt){
		//进程服务器表单
		var form = this.getView().child("form").getForm();
		form.findField("yxzt").setValue("STOPPING");
		if (form.isValid()) {
			//进程服务器表单参数
			var tzParams = this.getBatchDefnInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
				form.findField("orgId").setReadOnly(true);
				form.findField("orgId").setFieldStyle('background:#F4F4F4');
				form.findField("batchServerName").setReadOnly(true);
				form.findField("batchServerName").setFieldStyle('background:#F4F4F4');
				form.findField("yxzt").setValue("STOPPED");
				comView.commitChanges(comView);
			},"",true,this);
		}
	},
	startSelBatchServer: function(view, rowIndex){
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		//组件ID
		var orgId = selRec.get("orgId");
		var batchServerName = selRec.get("batchServerName");
		
		//参数
		var tzParamsJson = {
			"ComID":"TZ_BATCH_SERVER_COM",
			"PageID":"TZ_BATCH_SERVD_STD",
			"OperateType":"startSelBatchServer",
			"comParams":{
				"orgId": orgId,
				"batchServerName": batchServerName
			}
		};
		
		//参数
		var tzParams = Ext.JSON.encode(tzParamsJson);
		//加载数据
		Ext.tzSubmit(tzParams,function(responseData){
			store.reload();
        },"启动成功",true,this);
	},
	stopSelBatchServer: function(view, rowIndex){
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		//组件ID
		var orgId = selRec.get("orgId");
		var batchServerName = selRec.get("batchServerName");
		
		//参数
		var tzParamsJson = {
			"ComID":"TZ_BATCH_SERVER_COM",
			"PageID":"TZ_BATCH_SERVD_STD",
			"OperateType":"stopSelBatchServer",
			"comParams":{
				"orgId": orgId,
				"batchServerName": batchServerName
			}
		};
		
		//参数
		var tzParams = Ext.JSON.encode(tzParamsJson);
		//加载数据
		Ext.tzSubmit(tzParams,function(responseData){
			store.reload();
        },"停止成功",true,this);
	}
});