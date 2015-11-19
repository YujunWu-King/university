Ext.define('KitchenSink.view.basicData.systemVar.systemVarController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.systemVarMg', 
	
	addData: function(){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SYSVARGL_COM"]["TZ_GD_SYSVARDY_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_SYSVARDY_STD，请检查配置。');
			return;
		}
		
		var contentPanel, cmp, ViewClass, clsProto;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
       	contentPanel.body.addCls('kitchensink-example');
			
		//className = 'KitchenSink.view.basicData.systemVar.systemVarInfoPanel';
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
		cmp.actType="add";
		
		cmp.on('afterrender',function(panel){
			console.log(panel);
			var form = panel.child('form').getForm();
			var _type = form.findField("getValType").getGroupValue();	
			
			if (_type=="SQL"){
				form.findField("sqlValue").setDisabled(false);
				form.findField("appClass").setDisabled(true);
				form.findField("constant").setDisabled(true);
			}else if(_type=="APP"){
				form.findField("sqlValue").setDisabled(true);
				form.findField("appClass").setDisabled(false);
				form.findField("constant").setDisabled(true);
			}else if(_type=="CON"){
				form.findField("sqlValue").setDisabled(true);
				form.findField("appClass").setDisabled(true);
				form.findField("constant").setDisabled(false);
			}
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	
	editBySysVarIdHandler: function(sysVarId){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SYSVARGL_COM"]["TZ_GD_SYSVARDY_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_SYSVARDY_STD，请检查配置。');
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		//className = 'KitchenSink.view.basicData.systemVar.systemVarInfoPanel';
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
		cmp.actType="edit";
		
		cmp.on('afterrender',function(panel){
			//用户账号信息表单
			//var form = this.lookupReference('systemVarForm').getForm();
			var form = panel.child('form').getForm();
			form.findField("systemVarId").setReadOnly(true);
			form.findField("systemVarId").addCls("lanage_1");
			//var sysVarId = record.data.systemVarId;
			
			var tzParams = '{"ComID":"TZ_GD_SYSVARGL_COM","PageID":"TZ_GD_SYSVARDY_STD","OperateType":"QF","comParams":{"systemVarId":"'+sysVarId+'"}}';
			
			Ext.tzLoad(tzParams,function(responseData){
				//系统变量信息数据
				form.setValues(responseData);
				
				var _type = form.findField("getValType").getGroupValue();	
			
				if (_type=="SQL"){
					form.findField("sqlValue").setDisabled(false);
					form.findField("appClass").setDisabled(true);
					form.findField("constant").setDisabled(true);
				}else if(_type=="APP"){
					form.findField("sqlValue").setDisabled(true);
					form.findField("appClass").setDisabled(false);
					form.findField("constant").setDisabled(true);
				}else if(_type=="CON"){
					form.findField("sqlValue").setDisabled(true);
					form.findField("appClass").setDisabled(true);
					form.findField("constant").setDisabled(false);
				}					
			});
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},

	editData: function(view, rowIndex, colIndex, item, e, record, row){
		var sysVarId = record.data.systemVarId;
		this.editBySysVarIdHandler(sysVarId);
	},
	
	//编辑
	editSelData: function(btn){
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
	   //系统变量ID
	   var sysVarID = selList[0].get("systemVarId");
	   this.editBySysVarIdHandler(sysVarID);
	},
	
	getSysVarParams: function(panel){
		var form = panel.child("form").getForm();
		//操作类型，add-添加，edit-编辑
		var _actType = panel.actType;
		
		if(form.isValid()){
			var formParams = form.getValues();
			var tzParams = '{"ComID":"TZ_GD_SYSVARGL_COM","PageID":"TZ_GD_SYSVARDY_STD","OperateType":"U","comParams":{';
			
			if(_actType=="add"){
				tzParams = tzParams + '"add":[';
			}else if(_actType=="edit"){
				tzParams = tzParams + '"update":['
			}
			
			if (formParams['isValid'] == undefined){
				formParams['isValid'] = "N";
			}
			if(form.findField("sqlValue").isDisabled()){
				formParams['sqlValue'] = form.findField("sqlValue").getValue();
			}
			if(form.findField("appClass").isDisabled()){
				formParams['appClass'] = form.findField("appClass").getValue();
			}
			if(form.findField("constant").isDisabled()){
				formParams['constant'] = form.findField("constant").getValue();
			}
			
			tzParams += Ext.JSON.encode(formParams) + ']}}';
			//console.log(tzParams);
			return tzParams;
		}
	},

	onFormClose: function(btn){
		this.getView().close();	
	},
	
	onFormSave: function(btn){
		var panel = btn.findParentByType("panel");
		var form = panel.child("form").getForm();
		var tzParams = this.getSysVarParams(panel);
		
		Ext.tzSubmit(tzParams,function(responseData){
			form.findField('systemVarId').setReadOnly(true);
			form.findField('systemVarId').addCls("lanage_1");
			panel.actType = "edit";	
		},"",true,this);
	},
	
	onFormEnsure: function(btn){ 
		var panel = btn.findParentByType("panel");
		var tzParams = this.getSysVarParams(panel);
		
		var sysVarPanel = this.getView();
		Ext.tzSubmit(tzParams,function(responseData){
			sysVarPanel.close();
		},"",true,this);
	},
	
	deleteCurrData: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	
	deleteSelData: function(btn){
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
	//保存删除
	onSaveRemoveData: function(btn){
		//系统变量列表
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
		var comParams = "";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_SYSVARGL_COM","PageID":"TZ_GD_SYSVARGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		},"保存成功！",true,this);
	},
	onEnsureRemoveData:function(btn){
		//保存数据
		this.onSaveRemoveData(btn);
		//关闭窗口
		this.view.close();
	},
	onCloseRemoveData:function(btn){
		//关闭窗口
		this.view.close();
	},
	//可配置搜索
	searchSysVarList: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_GD_SYSVARGL_COM.TZ_GD_SYSVARGL_STD.TZ_SYSVAR_VW',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	}
});