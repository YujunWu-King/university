Ext.define('KitchenSink.view.clmsHtpz.bqzManagement.bqzController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.bqzManage', 
    addbqzManageInfo: function() {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BIAOQZ_COM"]["TZ_BIAOQZ_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能标签对应的JS类，页面ID为：TZ_BIAOQZ_DEFN_STD，请检查配置。');
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
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
    },
    editbqzManageInfo: function() {
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
	   //标签组ID
	   var bqzID = selList[0].get("bqzID");
	   //显示标签组注册信息编辑标签
	   this.editbqzManageIntoByID(bqzID);
    },
	deletebqzManageInfo: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	deletebqzManageInfos: function(){
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
	editSelbqzManageInfo: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //标签组ID
	   	 var bqzID = selRec.get("bqzID");
	     //显示标签组注册信息编辑标签
	     this.editbqzManageIntoByID(bqzID);
	},
	editbqzManageIntoByID: function(bqzID){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BIAOQZ_COM"]["TZ_BIAOQZ_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能标签对应的JS类，页面ID为：TZ_BIAOQZ_DEFN_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		//className = 'KitchenSink.view.clmsHtpz.bqzManagement.bqzInfoPanel';
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
			//标签组注册表单信息;
			var form = panel.child('form').getForm();
			form.findField("bqzID").setReadOnly(true);
			form.findField("bqzID").setFieldStyle('background:#F4F4F4');
			//标签注册信息列表
			var grid = panel.child('grid');
			//参数
			var tzParams = '{"ComID":"TZ_BIAOQZ_COM","PageID":"TZ_BIAOQZ_DEFN_STD","OperateType":"QF","comParams":{"bqzID":"'+bqzID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//标签组注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				//标签注册信息列表数据
				var roleList = responseData.listData;	

				var tzStoreParams = '{"bqzID":"'+bqzID+'"}';
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
	/*标签组列表保存*/
	savebqzManageInfos: function(btn){
		//标签组列表
		var grid = btn.findParentByType("grid");
		//标签组数据
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
		var tzParams = '{"ComID":"TZ_BIAOQZ_COM","PageID":"TZ_BIAOQZ_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        if(comParams!=""){
            Ext.tzSubmit(tzParams,function(){
                store.reload();
            },"",true,this);
        }

	},
	/*新增标签*/
	addbqDefnInfo: function(btn){
		if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存标签组信息后，再新增标签。");
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BIAOQZ_COM"]["TZ_BIAOQ_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能标签对应的JS类，页面ID为：TZ_BIAOQ_DEFN_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('bqDefnWindow');
        
        if (!win) {
			//className = 'KitchenSink.view.clmsHtpz.bqzManagement.bqDefnWindow';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为新增
		win.actType = "add";
		//标签组注册信息
		var bqzManageParams = this.getView().child("form").getForm().getValues();
		//标签组ID
		var bqzID = bqzManageParams["bqzID"];
        //标签注册信息表单
		var form = win.child("form").getForm();
		form.reset();
		form.findField("bqzID").setValue(bqzID);
		//form.findField("bqID").setReadOnly(false);
        win.show();
	},
	/*编辑标签*/
	editbqDefnInfo: function(btn){
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
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BIAOQZ_COM"]["TZ_BIAOQ_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能标签对应的JS类，页面ID为：TZ_BIAOQ_DEFN_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('bqDefnWindow');
        
        if (!win) {
			//className = 'KitchenSink.view.clmsHtpz.bqzManagement.bqDefnWindow';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		//标签组ID
		var bqzID = selList[0].get("bqzID");
		//标签ID
		var bqID = selList[0].get("bqID");
		//参数
		var tzParams = '{"ComID":"TZ_BIAOQZ_COM","PageID":"TZ_BIAOQ_DEFN_STD","OperateType":"QF","comParams":{"bqzID":"'+bqzID+'","bqID":"'+bqID+'"}}';
		//标签注册信息表单
		var form = win.child("form").getForm();
		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData);
			form.findField("bqID").setReadOnly(true);
			form.findField("bqID").setFieldStyle('background:#F4F4F4');
		});
        win.show();
	},
	deletebqDefnInfos: function(btn){
	   //标签注册信息列表
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
	/*标签组定义页面保存*/
	onbqzManageSave: function(btn){
		//标签组表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取标签组标签信息参数
			var tzParams = this.getbqzManageInfoParams();
			var bqzView = this.getView();
			//标签列表
			var grid = bqzView.child("grid");
			//标签列表数据
			var store = grid.getStore();
			Ext.tzSubmit(tzParams,function(responseData){
				bqzView.actType = "update";	
				form.findField("bqzID").setReadOnly(true);
                if(store.isLoaded()){
                    store.reload();
                }
			},"",true,this);
		}
	},
	onbqzManageEnsure: function(btn){
		//标签组注册表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取标签组页面信息参数
			var tzParams = this.getbqzManageInfoParams();
			var bqzView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				//关闭窗口						   
				bqzView.close();	
			},"",true,this);
		}
	},
	/*标签组定义页面保存-获取标签组页面各信息参数*/
	getbqzManageInfoParams: function(){
		//标签组注册表单
		var form = this.getView().child("form").getForm();
		//标签组信息标志
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
		
		//标签列表
		var grid = this.getView().child("grid");
		//标签数据
		var store = grid.getStore();
		//获取标签列表修改记录
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
		//获取标签列表删除记录
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
		var tzParams = '{"ComID":"TZ_BIAOQZ_COM","PageID":"TZ_BIAOQZ_DEFN_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	onbqzManageClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
	onbqDefnSave: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//标签注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存标签注册信息*/
			this.savebqDefnInfo(win);
		}
	},
	onbqDefnEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//标签注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存标签注册信息*/
			this.savebqDefnInfo(win);
			//重置表单
			form.reset();
			//关闭窗口
			win.close();
		}	
	},
	savebqDefnInfo: function(win){
        //标签信息表单
        var form = win.child("form").getForm();

		//表单数据
		var formParams = form.getValues();
		/*var transSetID = selRec.get("transSetID");
		var transID = selRec.get("transID");
		var language = this.getView().lanageType;
		var tzParams = '{"ComID":"TZ_GD_TRANSLATE_COM","PageID":"TZ_GD_TRANSXX_STD","OperateType":"QF","comParams":{"transSetID":"'+transSetID+'","transID":"'+transID+'","language":"'+language+'"}}';*/

		if(formParams.csOut != "Y"){
			formParams.csOut = "N"
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_BIAOQZ_COM","PageID":"TZ_BIAOQ_DEFN_STD","OperateType":"U","comParams":{"'+win.actType+'":['+Ext.JSON.encode(formParams)+']}}';
		var tzStoreParams = '{"bqzID":"'+formParams["bqzID"]+'"}';
		var bqGrid = this.getView().child("grid");
		Ext.tzSubmit(tzParams,function(resp){
			win.actType = "update";
			//form.findField("bqID").setReadOnly(true);
			//form.findField("bqID").setFieldStyle('background:#F4F4F4');
			bqGrid.store.tzStoreParams = tzStoreParams;
			bqGrid.store.reload();
	    },"",true,this);
	},
	onbqDefnClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//标签注册信息表单
		var form = win.child("form").getForm();
		win.close();
	},
	/*searchbqzList: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_BIAOQZ_COM.TZ_BIAOQZ_MG_STD.TZ_GD_COMZC_VW',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},*/
	closebqzManageInfos: function(btn){
		//关闭
		var grid = btn.findParentByType("grid");
		grid.close();
	},
	//确定
	ensurebqzManageInfos:function(btn) {
		this.savebqzManageInfos(btn);
		this.closebqzManageInfos(btn);
	},
	//bqzInfoPanel.js 中grid 每行中的编辑
	editbqDefnInfoOne: function(view, rowIndex){

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BIAOQZ_COM"]["TZ_BIAOQ_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能标签对应的JS类，页面ID为：TZ_BIAOQ_DEFN_STD，请检查配置。');
			return;
		}

		var win = this.lookupReference('bqDefnWindow');

		if (!win) {
			//className = 'KitchenSink.view.clmsHtpz.bqzManagement.bqDefnWindow';
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
		//标签组ID
		var bqzID = selRec.get("bqzID");
		//标签ID
		var bqID = selRec.get("bqID");
		//参数
		var tzParams = '{"ComID":"TZ_BIAOQZ_COM","PageID":"TZ_BIAOQ_DEFN_STD","OperateType":"QF","comParams":{"bqzID":"'+bqzID+'","bqID":"'+bqID+'"}}';
		//标签表单
		var form = win.child("form").getForm();
		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData);
			//form.findField("bqID").setReadOnly(true);
		});
		win.show();
	},
	//bqzInfoPanel.js 中grid 每行中的删除
	deletebqDefnInfoOne: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){
				var store = view.findParentByType("grid").store;
				store.removeAt(rowIndex);
			}
		},this);
	}
});