Ext.define('KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ckzlManagementController', 
//新增
    addComRegInfo: function() {

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZDCS_CKZL_COM"]["TZ_CKZL_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CKZL_DEFN_STD，请检查配置。');
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
		
		cmp.on('afterrender',function(panel){
	    	
					var cmpForm = panel.child("form").getForm();
		
					var cmpB3=panel.down("button[reference=ckzlSave]").setVisible(false);
				
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
//	   var ckzlid = selList[0].get("ckzlid");
		var ckzlid = selList[0].get("ckzlid");
		var ckzlName = selList[0].get("ckzlName");
		var java = selList[0].get("java");
	   console.log(ckzlid);
	   //显示组件注册信息编辑页面
	   this.editComRegIntoByID(ckzlid,ckzlName,java);
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
	   	 var comID = selRec.get("audId");
	     //显示组件注册信息编辑页面
	   	console.log(comID);
	     this.editComRegIntoByID(comID);
	     
	     
	},
	
	editComRegIntoByID: function(ckzlid,ckzlName,java){
		//是否有访问权限
		
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZDCS_CKZL_COM"]["TZ_CKZL_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CKZL_DEFN_STD，请检查配置。');
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
//			form.findField("comID").setReadOnly(true);
//			form.findField("comID").setFieldStyle('background:#F4F4F4');
			//页面注册信息列表
//			var grid = panel.child('grid');
//			console.log(grid);
			//参数
//			var tzParams = '{"ComID":"TZ_AQ_COMREG_COM","PageID":"TZ_AQ_COMREG_STD","OperateType":"QF","comParams":{"comID":"'+comID+'"}}';
			var tzParams = '{"ComID":"TZ_ZDCS_CKZL_COM","PageID":"TZ_CKZL_MG_STD","OperateType":"QF","comParams":{"ckzlid":"'+ckzlid+'","ckzlName":"'+ckzlName+'","java":"'+java+'"}}';
			console.log(tzParams);
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				var roleList = responseData.listData;	

//				var tzStoreParams = '{"ckzlid":"'+ckzlid+'"}';
//				grid.store.tzStoreParams = tzStoreParams;
//				grid.store.load();						
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
		console.log(removeRecs);
		
        var comParams = "";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
		}
		
		console.log(comParams);
		//提交参数
		var tzParams = '{"ComID":"TZ_ZDCS_CKZL_COM","PageID":"TZ_CKZL_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
		console.log(tzParams);
		//保存数据
        if(comParams!=""){
            Ext.tzSubmit(tzParams,function(){
                store.reload();
            },"",true,this);
        }

	},
	
	
	addPageRegInfo: function(btn){
		console.log(this.getView().actType);
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
			//className = 'KitchenSink.view.security.com.pageRegWindow';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为新增
		win.actType = "add";
		console.log(win.actType);
		//组件注册信息
		
		var form = win.child("form").getForm();
	//	console.log(form);
		var comRegParams = win.child("form").getForm().getValues();
		console.log(comRegParams);
		
		//组件ID
		var comID = comRegParams["comID"];
		var audID = comRegParams["audID"];
		var audStat = comRegParams["audStat"];
		
		console.log(audID);
        //页面注册信息表单
		var form = win.child("form").getForm();
		form.reset();
		form.findField("audID").setValue(audID);
	//	form.findField("comID").setValue(comID);
	//	form.findField("pageID").setReadOnly(false);
        win.show();
	},
	
	
	editPageRegInfo1: function(btn){
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
		
	    var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZDCS_CKZL_COM"]["TZ_CKZL_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CKZL_DEFN_STD，请检查配置。');
			return;
		}
		
	/*	var win = this.lookupReference('pageRegWindow');
        
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
		var audId = selList[0].get("audId");
		var audName = selList[0].get("audName");
		var audStat = selList[0].get("audStat");
		var audType = selList[0].get("audType");
		var audMS = selList[0].get("audMS");
		var audSQL = selList[0].get("audSQL");
		
		
		//参数
		var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"'+audId+'","audName":"'+audName+'","audStat":"'+audStat+'","audType":"'+audType+'","audMS":"'+audMS+'","audSQL":"'+audSQL+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();
		
		var gridStore =win.child("form").child("grid").getStore();
		var tzStoreParams =  '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "'+ audId+'"}}';

		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData);
			form.findField("audName").setReadOnly(true);
	//		form.findField("pageID").setReadOnly(true);
	//		form.findField("pageID").setFieldStyle('background:#F4F4F4');
			
			gridStore.tzStoreParams = tzStoreParams;
			gridStore.reload();
		});
        win.show();
        */
		
var win = this.lookupReference('ckzlManagementInfoListForm');
        
        if (!win) {
			className = 'KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementInfoList';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		//组件ID
		var ckzlid = selList[0].get("ckzlid");
		var ckzlName = selList[0].get("ckzlName");
		var java = selList[0].get("java");
		
		//参数
		var tzParams = '{"ComID":"TZ_ZDCS_CKZL_COM","PageID":"TZ_CKZL_MG_STD","OperateType":"QF","comParams":{"ckzlid":"'+ckzlid+'","ckzlName":"'+ckzlName+'","java":"'+java+'"}}';
		console.log(tzParams);
		
		//页面注册信息表单
		var form = win.child("form").getForm();
		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData);
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
		//	var grid = comView.child("grid");
			//页面注册信息数据
		//	var store = grid.getStore();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
		//		form.findField("comID").setReadOnly(true);
        /*        if(store.isLoaded()){
                    store.reload();
                }
                */
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
			    contentPanel.child("ckzlManagementList").store.reload();
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
/*		var grid = this.getView().child("grid");
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
		*/
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
/*		//删除json字符串
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
		*/
		//提交参数
		var tzParams = '{"ComID":"TZ_ZDCS_CKZL_COM","PageID":"TZ_CKZL_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
		//	console.log("1");
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
	//	console.log(view);
        //信息表单
        var form = win.child("form").getForm();
       
		var gridStore =win.child("form").child("grid").getStore();
		

		var selList = win.child("form").child("grid").getSelectionModel().getSelection();
	//	console.log(selList);
		
//		var audName = selList["audName"];
	//	var audName = selList.get("audName");
	//	console.log(audName);
		
	
		
      
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
	//	console.log(removeRecs);
		
		 var comParams = "";
			if(removeJson != ""){
				comParams = '"delete":[' + removeJson + "]";
			}
			
			console.log(comParams);
			var tzParams2 = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{'+comParams+'}}';
		//	console.log(tzParams2);
			//保存数据
	        if(comParams!=""){
	            Ext.tzSubmit(tzParams2,function(){
	            	gridStore.reload();
	            },"",true,this);
	        }
		
		
		
		
	/*	var selRec = gridStore.getAt(rowIndex);
		//组件ID
		console.log(selRec);
		
		var audDxzt = selRec.get("audDxzt");
		console.log(audDxzt);
		
		"'+win.actType+'"
	*/	
		
		
		//表单数据
	//	var formParams = form.getValues();
//		var	comParamsALL = '"update":[{"typeFlag":"FORM","data":'+Ext.JSON.encode(form.getValues())+'}]';
		
		var	comParamsALL = '"+win.actType+":[{"typeFlag":"FORM","data":'+Ext.JSON.encode(form.getValues())+'}]';
		
		
		 //表格数据
		var updateJson = "";
		var updateRecs = gridStore.getUpdatedRecords();
		//console.log(updateRecs);
		
		for(var i=0;i<updateRecs.length;i++){
			if(updateJson == ""){
			//	updateJson = Ext.JSON.encode(updateRecs[i].data);
				updateJson = '{"typeFlag":"GRID","data":'+Ext.JSON.encode(updateRecs[i].data)+'}';
			//	editJson =   '{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
				
			}else{
			//	updateJson = updateJson + ','+Ext.JSON.encode(updateRecs[i].data);
				updateJson = updateJson + ',{"typeFlag":"GRID","data":'+Ext.JSON.encode(updateRecs[i].data)+'}';
			//	editJson = editJson + ',{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}
		};
console.log(updateJson);
		 var comParams3 = "";
			if(updateJson != ""){
			//	comParams3 = '"update":[' + updateJson + "]";
				comParamsALL = comParamsALL+',"update":[' + updateJson + "]";
			//	comParams = comParams + ',"update":[' + editJson + "]";
			}
console.log(comParamsALL);
		//	var tzParams3 = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{'+comParams3+'}}';
		//	console.log("tzParams3:"+tzParams3);
			//保存数据
		/*	if(comParams3!=""){
	            Ext.tzSubmit(tzParams3,function(){
	            	gridStore.reload();
	            },"",true,this);
	        }
		*/
		
		//提交参数
	//	var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{"'+win.actType+'":['+Ext.JSON.encode(formParams)+']}}';
		var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{'+comParamsALL+'}}';
		//	var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"'+audId+'","audName":"'+audName+'","audStat":"'+audStat+'","audType":"'+audType+'"}}';
	
		console.log(tzParams);
		
	//	var tzStoreParams = '{"comID":"'+formParams["comID"]+'"}';
		
	//	var pageGrid = this.parent.getView().child("grid");
		var pageGrid = this.getView();
	//	view.parent.parent.down("maintoolbar").hide();
		
	//	var pageGrid = this.findParentByType("grid");
	//	console.log(pageGrid);
		Ext.tzSubmit(tzParams,function(resp){
			win.actType = "update";
		//	form.findField("pageID").setReadOnly(true);
		//	form.findField("pageID").setFieldStyle('background:#F4F4F4');
		//	pageGrid.store.tzStoreParams = tzStoreParams;
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
		//	cfgSrhId: 'TZ_AQ_COMREG_COM.TZ_AQ_COMGL_STD.TZ_GD_COMZC_VW',
			cfgSrhId: 'TZ_ZDCS_CKZL_COM.TZ_CKZL_MG_STD.TZ_CKZL_VW',
		//	tzStoreParams: '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_LIST_STD.PS_TZ_AUDCX_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
			
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
	
	
	
	//comInfoPanel.js 中grid 每行中的编辑
	editPageRegInfoOne: function(view, rowIndex){

		var store = view.findParentByType("grid").store;
		
		var selRec = store.getAt(rowIndex);
		//组件ID
//console.log(selRec);		
		var ckzlid = selRec.get("ckzlid");
		var ckzlName = selRec.get("ckzlName");
		var java = selRec.get("java");
	
		this.editComRegIntoByID(ckzlid,ckzlName,java);
		


	},
	
	
	
//重新产生听众	
	freshAudMember: function(btn){
		
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		//表单数据
			var formParams = form.getValues();
			var audSQL =formParams["audSQL"];
			var audID =formParams["audID"];
			 console.log(audSQL);
			 console.log(audID);
			
			 var removeAud =Ext.JSON.encode(audID); 
			 var comParams = "";
				if(removeAud != ""){
					//		 	 "delete":[{"audID":"4","audDxzt":"1"}]	
					comParams = '"delete":[{"audID":' + removeAud + ',"audDxzt":"A"}]';
				}
//console.log(comParams);
//     var tzParams2 = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{'+comParams+'}}';
			 
	 var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"tzOther","comParams":{"audSQL":"'+audSQL+'"}}';
//console.log(tzParams); 
	//	 var tzStoreParams =  '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "'+ audID+'"}}'
		 
	 var tzStoreParams =audSQL;
		 
		var gridStore =win.child("form").child("grid").getStore();	
	//	gridStore.tzStoreParams = tzStoreParams;
		
/*		 if(comParams!=""){
	            Ext.tzSubmit(tzParams2,function(){
	            	gridStore.reload();
	            },"",true,this);
	        }

*/		
		Ext.tzLoad(tzParams,function(responseData){
			
			gridStore.reload();
					
				});
		

}
	
})