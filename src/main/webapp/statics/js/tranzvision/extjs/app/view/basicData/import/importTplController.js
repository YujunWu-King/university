Ext.define('KitchenSink.view.basicData.import.importTplController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.importTplController', 

	listSearch: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_IMP_TPL_COM.TZ_TPL_LST_STD.TZ_IMP_TPL_DFN_T',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
    deleteTpl: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	deleteTpls: function(){
	   var selList = this.getView().getSelectionModel().getSelection();
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
	//列表模板保存和确定
	listSave: function(btn){
		var grid = btn.findParentByType("grid");
		var store = grid.getStore();

		var removeJson = "";
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
			//提交参数
			var tzParams = '{"ComID":"TZ_IMP_TPL_COM","PageID":"TZ_TPL_LST_STD","OperateType":"U","comParams":{'+comParams+'}}';
	        //保存数据
			Ext.tzSubmit(tzParams,function(){
				if(btn.name=="save"){
					store.reload();	
				}else{
					grid.close();
				}
			},"",true,this);
		}else{
			if(btn.name=="save"){
				TranzvisionMeikecityAdvanced.Boot.showToast("没有需要保存的数据");
			}else{
				grid.close();
			}
		}
	},
	addTpl: function() {
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_IMP_TPL_COM"]["TZ_TPL_INF_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_TPL_INF_STD，请检查配置。');
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
    
    editTpl: function(obj,rowIndex) {
    	var tplId;
    	
    	if(obj.isXType("button",true)){
    		var selList = this.getView().getSelectionModel().getSelection();

    		   var checkLen = selList.length;
    		   if(checkLen == 0){
    				Ext.Msg.alert("提示","请选择一条要修改的记录");   
    				return;
    		   }else if(checkLen >1){
    			   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
    			   return;
    		   }
    		   tplId = selList[0].get("tplId");
    	}else{
    		var store = this.getView().store;
    		var selRec = store.getAt(rowIndex);
    		tplId = selRec.get("tplId");
    	}
	   
	   this.editTplByTplId(tplId);
    },
	
    editTplByTplId: function(tplId){
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_IMP_TPL_COM"]["TZ_TPL_INF_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_TPL_INF_STD，请检查配置。');
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
			var tplIdField = form.findField("tplId");
			tplIdField.setReadOnly(true);
			tplIdField.addCls('lanage_1');/*置灰*/
			//参数
			var tzParams = '{"ComID":"TZ_IMP_TPL_COM","PageID":"TZ_TPL_INF_STD","OperateType":"QF","comParams":{"tplId":"'+tplId+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				form.setValues(responseData);
			});
			
			panel.fieldStore.tzStoreParams=Ext.encode({
				cfgSrhId:"TZ_IMP_TPL_COM.TZ_TPL_INF_STD.TZ_IMP_TPL_FLD_T",
				condition:{
					"TZ_TPL_ID-operator":"01",
					"TZ_TPL_ID-value":tplId
					}
				}),
			panel.fieldStore.load({
				callback:function(){
					Ext.defer(function(panel){
						panel.updateLayout();
					},100,this,[panel]);
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
	
	searchTbl:function(field){
    	var me = this,
    		form = me.getView().child("form").getForm();
    	
		Ext.tzShowPromptSearch({
			recname: 'TZ_SCHEMA_TABLES_VW',
			searchDesc: '搜索所有表',
			maxRow:20,
			condition:{
				srhConFields:{
					TABLE_NAME:{
						desc:'表名',
						operator:'07',
						type:'01'
					},
					TABLE_COMMENT:{
						desc:'摘要',
						operator:'07',
						type:'01'
					}
				}
			},
			srhresult:{
				TABLE_NAME:'表名',
				TABLE_COMMENT:'摘要'
			},
			multiselect: false,
			callback: function(selection){
				var targetTblField = form.findField("targetTbl");
				
				if(targetTblField.getValue()!=selection[0].data.TABLE_NAME){
					Ext.MessageBox.confirm('确认', '更换目标表会重新加载字段，您确定要选择所选表吗?', function(btnId){
						if(btnId == 'yes'){
							targetTblField.setValue(selection[0].data.TABLE_NAME);
							var tzParams = {
									"OperateType":"COMBOX",
									"recname":"TZ_SCHEMA_COLUMNS_VW",
									"condition":{
										"TABLE_NAME":{
											"value":selection[0].data.TABLE_NAME,
											"operator":"01",
											"type":"01"
											}
									},
									"result":"COLUMN_NAME"
								};
							Ext.tzLoad(Ext.encode(tzParams),function(responseData){
								var columns = responseData["TZ_SCHEMA_COLUMNS_VW"],
									seq = 0,
									store = me.getView().fieldStore;
								
								store.removeAll();
								
								Ext.each(columns,function(column){
									seq ++;
									var record = new KitchenSink.view.basicData.import.importTplFieldModel({
										tplId:form.findField("tplId").getValue(),
										field:column["COLUMN_NAME"],
										seq:seq,
										required:false,
										cover:false,
										display:false
									});
									store.add(record);
								});
								store.clearFlag=true;
								me.getView().child("tabpanel").updateLayout();
							});
						}
					},this);
				}
			}
		});	
    },
    
    uploadExcelTpl:function(file, value, eOpts ){
		if(value != ""){
			var form = file.findParentByType("form").getForm();
			var panel = file.findParentByType("importTplInfo");
			
			//获取后缀
			var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
			if(fix.toLowerCase() == "xls" || fix.toLowerCase() == "xlsx"){
				form.submit({
					url: TzUniversityContextPath + '/UpdServlet?filePath=importExcelTpl',
					waitMsg: '正在上传，请耐心等待....',
					success: function (form, action) {
						var message = action.result.msg;
						var path = message.accessPath;
						var sysFileName = message.sysFileName;
						if(path.charAt(path.length - 1) == '/'){
							path = path + sysFileName;
						}else{
							path = path + "/" + sysFileName;
						}
						
						var excelTplField = panel.child("form").getForm().findField("excelTpl");
						excelTplField.setValue(path);

						form.reset();
					},
					failure: function (form, action) {
						form.reset();
						Ext.MessageBox.alert("错误", action.result.msg);
					}
				});
			}else{
				//重置表单
				form.reset();
				Ext.MessageBox.alert("提示", "请上传[xls,xlsx]格式的Excel文件。");
			}
		}
	},
	
	infoSave: function(btn){
		var panel = btn.findParentByType("panel");
		//操作类型，add-添加，update-更新
		var actType = panel.actType;
		var form = panel.child("form").getForm();
		if (form.isValid()) {
			var comParams = "",
				modifiedJson,
				formData = form.getValues();
			
			if(formData["enableMapping"] == undefined){
				formData["enableMapping"] = "N";
			}
			
			modifiedJson = [{type:"TPL",data:formData,targetTblChanged:panel.fieldStore.clearFlag===true}];
			
			//Store
			var modifiedRecs = panel.fieldStore.getModifiedRecords();

			for(var i=0;i<modifiedRecs.length;i++){
				modifiedJson.push({type:"FIELD",data:modifiedRecs[i].data});
			}
			
			if(actType=="add"){
				comParams="add:"+Ext.encode(modifiedJson);
			}else{
				comParams="update:"+Ext.encode(modifiedJson);
			}
			
			var tzParams = '{"ComID":"TZ_IMP_TPL_COM","PageID":"TZ_TPL_INF_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData){
				//清除store的更改目标表字段标识
				panel.fieldStore.clearFlag=true;	
				if(btn.name=="ensure"){
					panel.close();
				}else{
					panel.actType = "update";
					form.findField("tplId").setReadOnly(true);
                    form.findField("tplId").addCls('lanage_1');
				}
			},"",true,this);
		}
	}
});