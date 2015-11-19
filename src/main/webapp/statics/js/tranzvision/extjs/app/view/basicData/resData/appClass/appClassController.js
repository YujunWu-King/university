Ext.define('KitchenSink.view.basicData.resData.appClass.appClassController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.appClassController', 

    addAppClassDfn: function() {
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_APP_CLS_COM"]["TZ_APP_CLSINF_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_APP_CLSINF_STD，请检查配置。');
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
	editAppClassDfn: function() {
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
	   var appClassId = selList[0].get("appClassId");
	   this.editAppClassInfoByID(appClassId);
    },
	editSelAppClassDfn: function(view, rowIndex) {
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
	   	 var appClassId = selRec.get("appClassId");
	     this.editAppClassInfoByID(appClassId);
    },
	
	editAppClassInfoByID: function(appClassId){
	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_APP_CLS_COM"]["TZ_APP_CLSINF_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_APP_CLSINF_STD，请检查配置。');
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
			var appClassIdField = form.findField("appClassId");
            appClassIdField.setReadOnly(true);
            appClassIdField.addCls('lanage_1');/*灰掉应用程序类ID输入框*/
			//参数
			var tzParams = '{"ComID":"TZ_APP_CLS_COM","PageID":"TZ_APP_CLSINF_STD","OperateType":"QF","comParams":{"appClassId":"'+appClassId+'"}}';
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
	
	onAppClassClose: function(btn){
		var panel = btn.findParentByType("panel");
		panel.close();
	},
	
	onAppClassSave: function(btn){
		var panel = btn.findParentByType("panel");
		//操作类型，add-添加，edit-编辑
		var actType = panel.actType;
		var form = panel.child("form").getForm();
		if (form.isValid()) {
			
			//新增
			var comParams="";
			if(actType == "add"){
				comParams = '"add":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}else{
				//修改
				comParams = '"update":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}
			var tzParams = '{"ComID":"TZ_APP_CLS_COM","PageID":"TZ_APP_CLSINF_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData){
					panel.actType = "update";	
					form.findField("appClassId").setReadOnly(true);
                    form.findField("appClassId").addCls('lanage_1');
			},"",true,this);
		}
	},
	
	onAppClassEsure: function(btn){ 
		var panel = btn.findParentByType("panel");
		//操作类型，add-添加，edit-编辑
		var actType = panel.actType;
		var form = panel.child("form").getForm();
		if (form.isValid()) {
		
			//新增
			var comParams="";
			if(actType == "add"){
				comParams = '"add":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}else{
				//修改
				comParams = '"update":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}
			var tzParams = '{"ComID":"TZ_APP_CLS_COM","PageID":"TZ_APP_CLSINF_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData){
					panel.actType = "update";
					form.reset();
					panel.close();
			},"",true,this);
		}
	},
	//可配置搜索
	cfgSearchAppCls: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_APP_CLS_COM.TZ_APP_CLSLIST_STD.TZ_APPCLS_TBL',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
	deleteSelAppClassDfn: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	deleteAppClassDfns: function(){
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
	saveAppClass: function(btn){
		var grid = btn.findParentByType("grid");
		var store = grid.getStore();
		//删除json字符串
		var removeJson = "";
		//删除记录
		var removeRecs = store.getRemovedRecords();

        var comParams="";

		for(var i=0;i<removeRecs.length;i++){
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
			}
		}
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_APP_CLS_COM","PageID":"TZ_APP_CLSLIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			if(btn.name=="save"){
				store.reload();	
			}else{
				grid.close();
			}
		},"",true,this);
	}
});