Ext.define('KitchenSink.view.distributionTable.distributionMgController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.distributionMgController',
	
	//新增
	addNewScoreModel: function(btn){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DISTRI_TAB_COM"]["TZ_DISTRI_INFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.prompt","提示"), Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.prompt","提示"),Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}

		var contentPanel, cmp, ViewClass, clsProto;

		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		contentPanel.body.addCls('kitchensink-example');

		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}
		ViewClass = Ext.ClassManager.get(className);
		clsProto = ViewClass.prototype;

		var config = {
			actType: "Add"	
		},
		cmp = new ViewClass(config,function(){
			//回调函数
			btn.getParentByType('distributionMgList').getStore().reload();
		});

		/*cmp.on('afterrender',function(panel){
			var form = panel.child('form').getForm();
			form.findField('status').setValue('Y');//默认有效
		});*/

		tab = contentPanel.add(cmp);
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	//编辑
	editSelData: function(grid, rowIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var orgId = rec.data.orgId;
		var distrId = rec.data.distrId;
		this.editDataHandler(grid, orgId, distrId);
	},
	//选中编辑
	editSelScoreModel: function(btn){
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
		var orgId = selList[0].get("orgId");
		var modeId = selList[0].get("distrId");
		this.editDataHandler(this.getView(), orgId, distrId);
	},
	
	editDataHandler: function(grid, orgId, distrId){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_DISTRI_TAB_COM"]["TZ_DISTRI_INFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.prompt","提示"), Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.prompt","提示"),Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var contentPanel, cmp, ViewClass, clsProto;
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		contentPanel.body.addCls('kitchensink-example');
		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}
		ViewClass = Ext.ClassManager.get(className);
		clsProto = ViewClass.prototype;
		var config = {
				actType: "Update"	
			},
		cmp = new ViewClass(config,function(){
			grid.getStore().reload();
		});
		cmp.on('afterrender',function(panel){
			var form = panel.child('form').getForm();
			var detailItemsGridStore = panel.down("grid").getStore();
			detailItemsGridStore.tzStoreParams = '{"orgId":"'+orgId+'","distrId":"'+distrId+'","type":"A"}';
			
			var tzParams = '{"ComID":"TZ_DISTRI_TAB_COM","PageID":"TZ_DISTRI_GL_STD","OperateType":"QF","comParams":{"orgId":"'+orgId+'","distrId":"'+distrId+'"}}';
//			console.log(tzParams);
			Ext.tzLoad(tzParams,function(respData){
				var formData = respData;
				form.setValues(formData);
				
				detailItemsGridStore.load();
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
	onFormSave: function(btn){
		var closePanel = btn.closePanel;
		var panel = btn.findParentByType("distributionInfo");
		var actType = panel.actType;
		var form = panel.child('form');
		if(form.isValid()){
			var formObj;
			var addArr = [];
			var updateArr = [];
			var removeArr = [];

			//form表单数据
			var formRec = form.getForm().getValues();
//			console.log(formRec);
			if(actType == "Add"){
				formObj = {
					type: 'tableInfo',
					data: formRec
				}
				addArr.push(formObj);
			}else if(actType == "Update"){
				formObj = {
					type: 'tableInfo',
					data: formRec
				}
				updateArr.push(formObj);
			}
//			var detailGrid = panel.down("tabpanel").child('grid[name=detailGrid]');
			var detailGrid = panel.down("grid");
			var detailGridGridStore = detailGrid.getStore();
			var GridModifyRec = detailGridGridStore.getModifiedRecords();
			var GridRemoveRec = detailGridGridStore.getRemovedRecords(); 
			var CellEditingPlugin = detailGrid.getPlugin('detailCellediting');
			//分布对照表明细grid数据
			var GridAllRecs = detailGridGridStore.getRange();
//			console.log(GridAllRecs);
			var dfItemsIdArr = [];
			for(var i=0; i<GridAllRecs.length; i++){
//				var itemId = GridAllRecs[i].get("itemId");
				var sortNum = GridAllRecs[i].get("sortNum");
			}
			//grid修改
			var ModifyData = [];
			for(var i=0; i<GridModifyRec.length; i++){
				ModifyData.push(GridModifyRec[i].data);
			}
			if(ModifyData.length > 0){
				updateArr.push({
					type: 'ItemsInfo',
					data: ModifyData
				});
			}
			
			//打分grid删除
			var RemoveData = [];
			for(var i=0; i<GridRemoveRec.length; i++){
				RemoveData.push(GridRemoveRec[i].data);
			}
			if(RemoveData.length > 0){
				removeArr.push({
					type: 'ItemsInfo',
					data: RemoveData
				});
			}
			
			
			var comParamsObj = {
				ComID: 'TZ_DISTRI_TAB_COM',
				PageID: 'TZ_DISTRI_INFO_STD',
				OperateType: 'U',
				comParams:{
					add: addArr,
					delete: removeArr,
					update: updateArr
				}
			}
			
			var tzParams = Ext.JSON.encode(comParamsObj);
//			console.log(tzParams);
			Ext.tzSubmit(tzParams,function(respData){
				if(respData.result == "success"){
					form.getForm().setValues(respData.formData);
					if(actType=="Add"){
						panel.actType = "Update";
						var distrIdField = form.findField("distrId");
						distrIdField.setReadOnly(true);
						distrIdField.addCls("lanage_1");
					}
					
					panel.reloadGrid();//刷新grid
				}
				
				if(closePanel == "Y"){
					panel.close();
				}
			},"保存成功",true,this);
		}
	},
	
	//确定
	onFormEnsure: function(btn){
		this.onFormSave(btn);
	},
	//关闭
	onFormClose: function(btn){
//		var panel = btn.findParentByType("distributionInfo");
//		panel.close();
		this.view.close();
	},
	
	
	addItem: function(btn,rowIndex){
		var form = btn.up('distributionInfo').child('form').getForm();
		var grid = btn.findParentByType('grid');
		var gridStore = grid.getStore();
		var type = grid.tabType;
		var rowCount = rowIndex + 1;
		var distrId = form.getValues().distrId;
		var CellEditing = grid.getPlugin('detailCellediting');	
		
		var row = Ext.create('KitchenSink.view.distributionTable.distributionDetailModel', {
			orgId: Ext.tzOrgID,
			distrId: distrId,
			distrMXId: "",
			itemType: type,
			sortNum: rowCount,
    	});
		
		gridStore.insert(rowCount,row);
		
		CellEditing.startEditByPosition({
	       	row: rowCount,
	       	column: 4
    	});
	},
	
	addItem1: function(btn){
		var form = btn.up('distributionInfo').child('form').getForm();
		var grid = btn.findParentByType('grid');
		var gridStore = grid.getStore();
		var type = grid.tabType;
		var rowCount = gridStore.getCount();
		var distrId = form.getValues().distrId;
		var CellEditing = grid.getPlugin('detailCellediting');	
		
		var row = Ext.create('KitchenSink.view.distributionTable.distributionDetailModel', {
			orgId: Ext.tzOrgID,
			distrId: distrId,
			distrMXId: "",
			itemType: type,
			sortNum: rowCount + 1,
    	});
		
		gridStore.insert(rowCount,row);
		
		CellEditing.startEditByPosition({
	       	row: rowCount,
	       	column: 4
    	});
	},
	
	
	deleteScoreItems: function(btn){
		var grid = btn.findParentByType('grid');
		var gridStore = grid.getStore();
		
		var selList = grid.getSelectionModel().getSelection();
	    //选中行长度
	    var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	    }else{
	    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
					gridStore.remove(selList);
					
					var items = gridStore.data.items;
					for(var i = 0; i< items.length; i++){
						items[i].set('sortNum',i+1);
					}
				}												  
			},this);  
	    }
	},
	
	
	deleteCurrentRow: function(grid, rowIndex){
		var store = grid.store;
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.confirm","确认"),Ext.tzGetResourse("TZ_DISTRI_TAB_COM.TZ_DISTRI_INFO_STD.nqdyscsxjlm","您确定要删除所选记录吗"), function(btnId){
			var removeRec = store.getAt(rowIndex);
			if(btnId == 'yes'){
				store.remove(removeRec);
				var items = store.data.items;
				for(var i = rowIndex; i< items.length; i++){
					items[i].set('sortNum',i+1);
				}
			}
		},this);
	},
	
	
	//可配置搜索
	cfgSearchDistribution: function(btn){    
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_DISTRI_TAB_COM.TZ_DISTRI_GL_STD.TZ_FBDZ_TBL',
			condition:
			{
				"TZ_JG_ID": Ext.tzOrgID
			},
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});
	},
	deleteSelData: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
	saveList: function(btn){
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
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
            //提交参数
            var tzParams = '{"ComID":"TZ_DISTRI_TAB_COM","PageID":"TZ_DISTRI_GL_STD","OperateType":"U","comParams":{'+comParams+'}}';
            //保存数据
            Ext.tzSubmit(tzParams,function(){
                store.reload();
                grid.commitChanges(grid);
            },"",true,this);
        }else{
        	Ext.Msg.alert("提示","保存成功");
        }
    },
    ensureList: function(btn){
    	this.saveList(btn);
    	this.getView().close();
    },
    ensureList1: function(btn){
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
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
          //提交参数
            var tzParams = '{"ComID":"TZ_DISTRI_TAB_COM","PageID":"TZ_DISTRI_GL_STD","OperateType":"U","comParams":{'+comParams+'}}';
            //保存数据
            Ext.tzSubmit(tzParams,function(){
            	
              grid.commitChanges(grid);
              grid.close();
     
            },"",true,this);
        }else{
        	grid.close();
        }
        
        
    },
	onGridPanelClose: function(btn){
		//关闭
		var grid = btn.findParentByType("grid");
		grid.close();
	},

});