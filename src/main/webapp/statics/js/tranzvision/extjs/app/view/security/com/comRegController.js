Ext.define('KitchenSink.view.security.com.comRegController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.comReg', 
    addComRegInfo: function() {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_COMREG_COM"]["TZ_AQ_COMREG_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_COMREG_STD，请检查配置。');
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
	deleteComRegInfo: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	deleteComRegInfos: function(){
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
	editSelComRegInfo: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //组件ID
	   	 var comID = selRec.get("comID");
	     //显示组件注册信息编辑页面
	     this.editComRegIntoByID(comID);
	},
	editComRegIntoByID: function(comID){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_COMREG_COM"]["TZ_AQ_COMREG_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_COMREG_STD，请检查配置。');
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
			form.findField("comID").setReadOnly(true);
			form.findField("comID").setFieldStyle('background:#F4F4F4');
			//页面注册信息列表
			var grid = panel.child('grid');
			//参数
			var tzParams = '{"ComID":"TZ_AQ_COMREG_COM","PageID":"TZ_AQ_COMREG_STD","OperateType":"QF","comParams":{"comID":"'+comID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				var roleList = responseData.listData;	

				var tzStoreParams = '{"comID":"'+comID+'"}';
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
	},
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
		var tzParams = '{"ComID":"TZ_AQ_COMREG_COM","PageID":"TZ_AQ_COMGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_COMREG_COM"]["TZ_AQ_PAGEREG_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_PAGEREG_STD，请检查配置。');
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
		
		//操作类型设置为新增
		win.actType = "add";
		//组件注册信息
		var comRegParams = this.getView().child("form").getForm().getValues();
		//组件ID
		var comID = comRegParams["comID"];
        //页面注册信息表单
		var form = win.child("form").getForm();
		form.reset();
		form.findField("comID").setValue(comID);
		form.findField("pageID").setReadOnly(false);
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
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_COMREG_COM"]["TZ_AQ_PAGEREG_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_PAGEREG_STD，请检查配置。');
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
		//组件ID
		var comID = selList[0].get("comID");
		//页面ID
		var pageID = selList[0].get("pageID");
		//参数
		var tzParams = '{"ComID":"TZ_AQ_COMREG_COM","PageID":"TZ_AQ_PAGEREG_STD","OperateType":"QF","comParams":{"comID":"'+comID+'","pageID":"'+pageID+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();
		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData);
			form.findField("pageID").setReadOnly(true);
			form.findField("pageID").setFieldStyle('background:#F4F4F4');
		});
        win.show();
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
				form.findField("comID").setReadOnly(true);
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
			comParams = '"add":[{"typeFlag":"COM","data":'+Ext.JSON.encode(form.getValues())+'}]';
		}
		//修改json字符串
		var editJson = "";
		if(actType == "update"){
			editJson = '{"typeFlag":"COM","data":'+Ext.JSON.encode(form.getValues())+'}';
		}
		
		//页面注册信息列表
		var grid = this.getView().child("grid");
		//页面注册信息数据
		var store = grid.getStore();
		//修改记录
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
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
		var tzParams = '{"ComID":"TZ_AQ_COMREG_COM","PageID":"TZ_AQ_COMREG_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
	savePageRegInfo: function(win){
        //页面注册信息表单
        var form = win.child("form").getForm();

		//表单数据
		var formParams = form.getValues();
		//是否外部URL
		if(formParams["isExURL"] == undefined){
			formParams["isExURL"] = "N";
		}
		//默认首页
		if(formParams["isDefault"] == undefined){
			formParams["isDefault"] = "N";
		}
		//新开窗口
		if(formParams["isNewWin"] == undefined){
			formParams["isNewWin"] = "N";
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_AQ_COMREG_COM","PageID":"TZ_AQ_PAGEREG_STD","OperateType":"U","comParams":{"'+win.actType+'":['+Ext.JSON.encode(formParams)+']}}';
		var tzStoreParams = '{"comID":"'+formParams["comID"]+'"}';
		var pageGrid = this.getView().child("grid");
		Ext.tzSubmit(tzParams,function(resp){
			win.actType = "update";
			form.findField("pageID").setReadOnly(true);
			form.findField("pageID").setFieldStyle('background:#F4F4F4');
			pageGrid.store.tzStoreParams = tzStoreParams;
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
	searchComList: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_AQ_COMREG_COM.TZ_AQ_COMGL_STD.TZ_GD_COMZC_VW',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
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
	//comInfoPanel.js 中grid 每行中的编辑
	editPageRegInfoOne: function(view, rowIndex){

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_COMREG_COM"]["TZ_AQ_PAGEREG_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_PAGEREG_STD，请检查配置。');
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
		var comID = selRec.get("comID");
		//页面ID
		var pageID = selRec.get("pageID");
		//参数
		var tzParams = '{"ComID":"TZ_AQ_COMREG_COM","PageID":"TZ_AQ_PAGEREG_STD","OperateType":"QF","comParams":{"comID":"'+comID+'","pageID":"'+pageID+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();
		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData);
			form.findField("pageID").setReadOnly(true);
		});
		win.show();
	},
	//comInfoPanel.js 中grid 每行中的删除
	deletePageRegInfoOne: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){
				var store = view.findParentByType("grid").store;
				store.removeAt(rowIndex);
			}
		},this);
	}
});