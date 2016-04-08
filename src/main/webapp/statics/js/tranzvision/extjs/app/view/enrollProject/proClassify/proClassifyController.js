Ext.define('KitchenSink.view.enrollProject.proClassify.proClassifyController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.proClassifyController', 
	
	// 在最后一行新增
	onAddInLastRow: function(btn){
		var grid = btn.findParentByType("grid");
		var row = grid.getStore().getCount();
		
        var model = new KitchenSink.view.enrollProject.proClassify.proClassifyModel({
            proTypeId: 'NEXT',
            proTypeName: '',
            proTypeDesc: ''
			//isSaved: 'N'
        });

        grid.getStore().insert(row, model);
        grid.cellEditing.startEditByPosition({
            row: row,
            column: 2
        });
    },
	//新增项目
	addNewPrjType: function(btn){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZS_XMLBSZ_COM"]["TZ_ZS_XMLBDY_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBDY_STD.prompt","提示"), Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBDY_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBDY_STD.prompt","提示"),Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBDY_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}

		var contentPanel, cmp, ViewClass, clsProto;

		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		contentPanel.body.addCls('kitchensink-example');

		//className = 'KitchenSink.view.enrollProject.projectMg.projectInfoPanel';
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
		var proGrid = btn.findParentByType('grid');
		cmp.proGrid = proGrid;

		cmp.on('afterrender',function(panel){
			var form = panel.child('form').getForm();
			form.findField('proTypeId').setValue('NEXT');
		});

		tab = contentPanel.add(cmp);
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	//批量删除
	onDeleteBat: function(btn){
		 //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.prompt","提示"),Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.qxzyscdjl","请选择要删除的记录"));
			return;
	   }else{
		  Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.confirm","确认"),Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.nqdyscsxjlm","您确定要删除所选记录吗？") , function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.getView().store;
				   store.remove(selList);
				}												  
			},this);    
	   }
	},
	
	//保存修改
	onSaveData: function(btn){
		var grid = this.getView();
		var isValid = true;
		var nameRepeat = false;

		var modifRecs = grid.store.getModifiedRecords();	
		for (var i=0; i<modifRecs.length; i++){
			//分类名称不能为空
			if (modifRecs[i].data.proTypeName == ""){
				isValid = false;
				break;
			}else {
				var loop = 0, sNum = 0, rowNum = 0;
				//用新增数据行比较，如果有两次相同则重复，重复行可能包含自己
				while (loop < 2){
					if(grid.getStore().find("proTypeName",modifRecs[i].data.proTypeName,rowNum,false,true,true) != -1){
						rowNum = grid.getStore().find("proTypeName",modifRecs[i].data.proTypeName,rowNum,false,true,true) + 1;
						sNum++;//相同数据行数量加1
					}
					loop++;
				}
				if (sNum == 2){
					nameRepeat = true;
					break;	
				}
			}	
		}
		
		if (isValid){
			if (nameRepeat){
				Ext.Msg.alert(Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.prompt","提示"),Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.xmlxmcbncf","项目类型名称不能重复！"));
			} else {
				var tzParams = this.getProClassifyParams();
				Ext.tzSubmit(tzParams,function(respData){
					grid.store.reload();
				},"",true,this);
			}
		} else {
			Ext.Msg.alert(Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.prompt","提示"),Ext.tzGetResourse("TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.xmlxmcbnwk","项目类型名称不能为空！"));
		}
	},
	
	
	getProClassifyParams: function(){
		var updateJson, delJson;
		var grid = this.getView();
		var num = 0;
		
		var tzParams = '{"ComID":"TZ_ZS_XMLBSZ_COM","PageID":"TZ_ZS_XMLBSZ_STD","OperateType":"U","comParams":{';
		//删除数据行
		var removeRecs = grid.store.getRemovedRecords();
		for (var i=0; i<removeRecs.length; i++){
			//删除行JSON报文
			if (i == 0){
				delJson = Ext.JSON.encode(removeRecs[i].data);
			} else {
				delJson += ',' + Ext.JSON.encode(removeRecs[i].data);
			}	
		}
		if (removeRecs.length > 0){
			delJson = '"delete": ['+ delJson +']';
			tzParams += delJson;
			num +=1;
		}
		
		//修改数据行
		var modifRecs = grid.store.getModifiedRecords();	
		for (var i=0; i<modifRecs.length; i++){
			//修改行JSON报文
			if (i == 0){
				updateJson = Ext.JSON.encode(modifRecs[i].data);
			} else {
				updateJson += ',' + Ext.JSON.encode(modifRecs[i].data);
			}
		}
		if (modifRecs.length > 0){
			if (num > 0){
				updateJson = ',"update":	['+ updateJson +']';
			} else {
				updateJson = '"update":	['+ updateJson +']';
			}
			tzParams += updateJson;
		}
		tzParams += '}}';
		return tzParams;
	},
	//可配置搜索
	searchProTypeList: function(btn){     //searchComList为各自搜索按钮的handler event;
        Ext.tzShowCFGSearch({            
           cfgSrhId: 'TZ_ZS_XMLBSZ_COM.TZ_ZS_XMLBSZ_STD.TZ_PRJ_TYPE_T',
           condition:
            {
                "TZ_JG_ID": Ext.tzOrgID  //设置搜索字段的默认值，没有可以不设置condition;
            },            
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });    
    },
	//保存
	onProjectSave: function(btn){
			var panel = btn.findParentByType("panel");
			var grid =panel.up('grid');

		var msArrInfoPanelArr=Ext.ComponentQuery.query("grid[reference=proClassifyLIstGridPanal]");

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
				var tzParams = '{"ComID":"TZ_ZS_XMLBSZ_COM","PageID":"TZ_ZS_XMLBDY_STD","OperateType":"U","comParams":{'+comParams+'}}';
				Ext.tzSubmit(tzParams,function(responseData){
					if(btn.name=="ensure"){
						panel.close();
					}else{
						panel.actType = "update";
						form.findField("proTypeId").setReadOnly(true);
						form.findField("proTypeId").addCls('lanage_1');
						if(responseData['prjID']){
							//form.findField("proTypeId").setValue(responseData['prjID']);
							form.setValues({proTypeId:responseData.prjID});

						};
						for(var i=0;i<msArrInfoPanelArr.length;i++){
							msArrInfoPanelArr[i].store.load();
						}
					}
				},"",true,this);
			}
		},
	closeProList: function(btn){
		//用户账号信息列表
		var grid = btn.findParentByType("grid");
		grid.close();
	},
	onProjectClose: function(btn){
		//关闭项目分类定义页面
		var proClose = btn.findParentByType("proClassifyInfoPanal");
		proClose.close();
	},
	onProjectEnsure: function(btn){
		//确定按钮
		btn.name="ensure";
		this.onProjectSave(btn);
		//this.onProjectClose(btn);
	},
	//编辑
	editPrjTypeDfn: function(btn) {
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
		var proTypeId = selList[0].get("proTypeId");
		this.editPrjTypeInfoByID(proTypeId);
	},
	editPrjTypeInfoByID: function(proTypeId){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZS_XMLBSZ_COM"]["TZ_ZS_XMLBDY_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZS_XMLBDY_STD，请检查配置。');
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

			var proTypeIdField = form.findField("proTypeId");
			proTypeIdField.setReadOnly(true);
			proTypeIdField.addCls('lanage_1');/*灰掉应用程序类ID输入框*/
			//参数
			var tzParams = '{"ComID":"TZ_ZS_XMLBSZ_COM","PageID":"TZ_ZS_XMLBDY_STD","OperateType":"QF","comParams":{"proTypeId":"'+proTypeId+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				form.setValues(responseData);

			});

		});

		tab = contentPanel.add(cmp);
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);
		if (cmp.floating) {
			cmp.show();
		}
	},
	//编辑
	onEditCurrRow: function(view, rowIndex) {
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		var proTypeId = selRec.get("proTypeId");
		this.editPrjTypeInfoByID(proTypeId);
	}
});