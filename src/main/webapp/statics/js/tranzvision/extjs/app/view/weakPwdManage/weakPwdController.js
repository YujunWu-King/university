Ext.define('KitchenSink.view.weakPwdManage.weakPwdController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.weakPwdController',
	//可配置搜索
	cfgSearchAct: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_WEAK_PWD_COM.TZ_WEAK_PWD_STD.TZ_WEAK_PASSWORD',
			condition:{},
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});
	},

	//查询
	searchWeakPwdList: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_WEAK_PWD_COM.TZ_WEAK_PWD_STD.TZ_WEAK_PASSWORD',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
	
	//新增
	addWeakPwdList: function() {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_WEAK_PWD_COM"]["TZ_PWD_INFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_WEAK_PWD_STD，请检查配置。');
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
    
    //弱密码信息保存
    saveWeakPwdInfo: function(btn){
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var tzParams = this.getWeakPwdInfoParams();
			var comView = this.getView();
			var weakPwdGrid = Ext.getCmp('weakPwdGrid');
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
				form.findField("TZ_PWD_ID").setReadOnly(true);
				form.findField("TZ_PWD_ID").setFieldStyle('background:#F4F4F4');
				weakPwdGrid.store.reload();
			},"",true,this);
		}
	},
	
	//获取弱密码信息数据参数
	getWeakPwdInfoParams: function(){
		var form = this.getView().child("form").getForm();
		var actType = this.getView().actType;
		var comParams = "";
		//新增
		if(actType == "add"){
			comParams = '"add":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
		}
		//修改
		if(actType == "update"){
			comParams = '"update":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
		}
		
		//提交参数
		var tzParams = '{"ComID":"TZ_WEAK_PWD_COM","PageID":"TZ_PWD_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	
	//弱密码信息关闭
	closeWeakPwdInfo: function(btn){
		//关闭
		var panel = btn.findParentByType("panel");
		panel.close();
	},
	
	//弱密码信息确定
	ensureWeakPwdInfo:function(btn) {
		this.saveWeakPwdInfo(btn);
		this.closeWeakPwdInfo(btn);
	},
	
	//上方编辑
	editWeakPwdLists: function() {
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
	   var tzPwdId = selList[0].get("TZ_PWD_ID");
	   //显示组件注册信息编辑页面
	   this.editWeakPwdByID(tzPwdId);
    },
    
    //单行编辑
    editWeakPwdList: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //组件ID
	   	 var tzPwdId = selRec.get("TZ_PWD_ID");
	     //显示组件注册信息编辑页面
	     this.editWeakPwdByID(tzPwdId);
	},
    
    //编辑方法
    editWeakPwdByID: function(tzPwdId){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_WEAK_PWD_COM"]["TZ_PWD_INFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_PWD_INFO_STD，请检查配置。');
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
			var form = panel.child('form').getForm();
			form.findField("TZ_PWD_ID").setReadOnly(true);
			form.findField("TZ_PWD_ID").setFieldStyle('background:#F4F4F4');
			var pageGrid = Ext.getCmp('pageRegGrid');
			//参数
			var tzParams = '{"ComID":"TZ_WEAK_PWD_COM","PageID":"TZ_PWD_INFO_STD","OperateType":"QF","comParams":{"tzPwdId":"'+tzPwdId+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
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
	
	//弱密码列表批量删除
	deleteWeakPwdLists: function(){
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
	
	//弱密码列表当行删除
	deleteWeakPwdList: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	
	//弱密码列表保存
	saveWeakPwdList: function(btn){
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
		};
        var comParams = "";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_WEAK_PWD_COM","PageID":"TZ_WEAK_PWD_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        if(comParams!=""){
            Ext.tzSubmit(tzParams,function(){
                store.reload();
            },"",true,this);
        }

	},
	
	//弱密码列表确定
	ensureWeakPwdList:function(btn) {
		this.saveWeakPwdList(btn);
		this.closeWeakPwdList(btn);
	},
	
	//弱密码列表关闭
	closeWeakPwdList: function(btn){
		var grid = btn.findParentByType("grid");
		grid.close();
	}
});