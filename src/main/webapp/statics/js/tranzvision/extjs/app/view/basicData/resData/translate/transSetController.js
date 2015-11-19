Ext.define('KitchenSink.view.basicData.resData.translate.transSetController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.tranSetMg' ,

    addTranslate: function() {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_TRANSLATE_COM"]["TZ_GD_TRANSDY_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_TRANSDY_STD，请检查配置。');
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
			cmp.actType = "add";
            tab = contentPanel.add(cmp);
            contentPanel.setActiveTab(tab);
            Ext.resumeLayouts(true);
            if (cmp.floating) {
                cmp.show();
            }

    },
	editTranslate: function() {
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
	   var transSetID = selList[0].get("transSetID");
	   //显示组件注册信息编辑页面
	   this.editTranslateByID(transSetID);
    },
	editSelTranslate: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //组件ID
	   	 var transSetID = selRec.get("transSetID");
	     //显示组件注册信息编辑页面
	     this.editTranslateByID(transSetID);
	},
	deleteTranslates: function(){
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
	deleteSelTranslate: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}
		},this);
	},

	editTranslateByID: function(transSetID){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_TRANSLATE_COM"]["TZ_GD_TRANSDY_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_TRANSDY_STD，请检查配置。');
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
			form.findField("transSetID").setReadOnly(true);
			form.findField("transSetID").addCls("lanage_1");
			//页面注册信息列表
			var grid = panel.child('grid');
			//参数
			var tzParams = '{"ComID":"TZ_GD_TRANSLATE_COM","PageID":"TZ_GD_TRANSDY_STD","OperateType":"QF","comParams":{"transSetID":"'+transSetID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				var roleList = responseData.listData;
				var tzStoreParams = '{"transSetID":"'+transSetID+'","language":""}';
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
	saveTranslates: function(btn){
		var grid = btn.findParentByType("grid");
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
		var comParams="";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
			//提交参数
			var tzParams = '{"ComID":"TZ_GD_TRANSLATE_COM","PageID":"TZ_GD_TRANSET_STD","OperateType":"U","comParams":{'+comParams+'}}';
			//保存数据
			Ext.tzSubmit(tzParams,function(){
				store.reload();
			},"",true,this);
		}
	},
	//保存转换值并关闭窗口
	ensureTranslates:function(btn){
		//保存
		this.saveTranslates(btn);
		//关闭窗口
		this.getView().close();
	},
	//关闭窗口
	closeTranslates:function(btn){
		this.getView().close();
	},
    addTransValInfo: function(btn){

		if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存转换值集合信息后，再新增转换值信息。");
			return;
		}
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_TRANSLATE_COM"]["TZ_GD_TRANSXX_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_TRANSXX_STD，请检查配置。');
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

		var language = this.getView().lanageType;
		//操作类型设置为新增
		win.actType = "add";
		//组件注册信息
		var comRegParams = this.getView().child("form").getForm().getValues();
		//组件IDtransSetID
		var transSetID = comRegParams["transSetID"];
        //页面注册信息表单
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({transSetID:transSetID});
		form.setValues({language:language});
		form.findField("transID").setReadOnly(false);
        win.show();
	},
	editTransValInfo: function(btn){
		var pannel = btn.findParentByType("transDefine");
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
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_TRANSLATE_COM"]["TZ_GD_TRANSXX_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_TRANSXX_STD，请检查配置。');
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
		var transSetID = selList[0].get("transSetID");
	    var transID = selList[0].get("transID");
	    var language = pannel.lanageType;
		//参数
		var tzParams = '{"ComID":"TZ_GD_TRANSLATE_COM","PageID":"TZ_GD_TRANSXX_STD","OperateType":"QF","comParams":{"transSetID":"'+transSetID+'","transID":"'+transID+'","language":"'+language+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();

		Ext.tzLoad(tzParams,function(responseData){
			form.findField("transID").setReadOnly(true);
			form.findField("transID").addCls("lanage_1");
			form.setValues(responseData);
		});

        win.show();

	},
	deleteSelTransValInfo: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}
		},this);
	},
	editCurrTransVal:function(view,rowIndex){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_TRANSLATE_COM"]["TZ_GD_TRANSXX_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_TRANSXX_STD，请检查配置。');
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
		//转换值集合ID
		var transSetID = selRec.get("transSetID");
		//转换值ID
		var transID = selRec.get("transID");
		var language = this.getView().lanageType;
		//参数
		var tzParams = '{"ComID":"TZ_GD_TRANSLATE_COM","PageID":"TZ_GD_TRANSXX_STD","OperateType":"QF","comParams":{"transSetID":"'+transSetID+'","transID":"'+transID+'","language":"'+language+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();

		Ext.tzLoad(tzParams,function(responseData){
			form.findField("transID").setReadOnly(true);
			form.findField("transID").addCls("lanage_1");
			form.setValues(responseData);
		});

		win.show();
	},
	deleteCurrTransVal:function(view,rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){
				var store = view.findParentByType("grid").store;
				store.removeAt(rowIndex);
			}
		},this);
	},
	deleteTransValInfos: function(btn){
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
	onTranslateSave: function(btn){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var tzParams = this.getTranslateParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";
				form.findField("transSetID").setReadOnly(true);
				form.findField("transSetID").addCls("lanage_1");
			},"",true,this);
		}
	},
	onTranslateEnsure: function(btn){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getTranslateParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				//关闭窗口
				comView.close();
			},"",true,this);
		}
	},
	onTranslateClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
	getTranslateParams: function(){
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
		var tzParams = '{"ComID":"TZ_GD_TRANSLATE_COM","PageID":"TZ_GD_TRANSDY_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	onTransValSave: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面注册信息*/
			this.saveTransValInfo(win);
		}
	},
	onTransValEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面注册信息*/
			this.saveTransValInfo(win);
			//关闭窗口
			win.close();
		}
	},
	saveTransValInfo: function(win){
		//页面注册信息表单
		var form = win.child("form").getForm();
		//表单数据
		var formParams = form.getValues();

		//提交参数
		var tzParams = '{"ComID":"TZ_GD_TRANSLATE_COM","PageID":"TZ_GD_TRANSXX_STD","OperateType":"U","comParams":{"'+win.actType+'":['+Ext.JSON.encode(formParams)+']}}';


		var tzStoreParams = '{"transSetID":"'+formParams["transSetID"]+'","language":"'+formParams["language"]+'"}';
		var pageGrid = this.getView().child("grid");
		Ext.tzSubmit(tzParams,function(){
			win.actType = "update";
			form.findField("transID").setReadOnly(true);
			form.findField("transID").addCls("lanage_1");
			pageGrid.store.tzStoreParams = tzStoreParams;
			pageGrid.store.reload();
	    },"",true,this);
	},
	onTransValClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	},
  queryTranslate:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_GD_TRANSLATE_COM.TZ_GD_TRANSET_STD.TZ_PT_ZHZJH_TBL',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
  },
  //切换语言
  changeLanguage:function(btn){
    	if(this.getView().actType == "add"){
				Ext.MessageBox.alert("提示","请先保存消息集合定义，再做操作。");
				return;
			}
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_TRANSLATE_COM"]["TZ_GD_CHANGE_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_CHANGE_STD，请检查配置。');
				return;
			}

			var win = this.lookupReference('changeLanguage');
			if(!win) {
			  	Ext.syncRequire(className);
					ViewClass = Ext.ClassManager.get(className);
			    //新建类;
	        win = new ViewClass();
	        this.getView().add(win);
	    }

	    //trans定义表单
			var transFormParams = this.getView().child("form").getForm().getValues();
			//transid;
			var transSetID = transFormParams["transSetID"];
			//语言类型
			var lanageType = this.getView().lanageType;
	        //消息定义表单
			var form = win.child("form").getForm();
			form.reset();
			form.setValues({transSetID:transSetID});
			form.setValues({language:lanageType});
	    win.show();
	},
  //切换语言确定
  onLanguageFormEnsure:function(btn){
    	var valuesForm = this.getView().child('form').getForm().getValues();
    	var transSetID = valuesForm["transSetID"];
    	var language = valuesForm["language"];

    	var panel = btn.findParentByType("transDefine");
    	this.getView().close();
    	panel.lanageType = language;
    	var grid = panel.child('grid');
    	var tzStoreParams = '{"transSetID":"'+transSetID+'","language":"'+language+'"}';
			grid.store.tzStoreParams = tzStoreParams;
			grid.store.reload();
  },
  //切换语言关闭
  onLanguageFormClose:function(btn){
    	this.getView().close();
  }
});