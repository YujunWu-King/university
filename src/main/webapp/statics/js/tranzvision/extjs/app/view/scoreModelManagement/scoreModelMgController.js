Ext.define('KitchenSink.view.scoreModelManagement.scoreModelMgController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.scoreModelMgController',
	
	//新增
	addNewScoreModel: function(btn){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SCORE_MOD_COM"]["TZ_SCRMOD_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.prompt","提示"), Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.prompt","提示"),Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
			btn.findParentByType('scoreModelMgList').getStore().reload();
		});

		cmp.on('afterrender',function(panel){
			var form = panel.child('form').getForm();
			form.findField('status').setValue('Y');//默认有效
		});

		tab = contentPanel.add(cmp);
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	//编辑
	editScoreModel: function(grid, rowIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var orgId = rec.data.orgId;
		var modeId = rec.data.modeId;
		
		this.editScoreModelHandler(grid, orgId, modeId);
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
		var modeId = selList[0].get("modeId");

		this.editScoreModelHandler(this.getView(), orgId, modeId);
	},
	
	editScoreModelHandler: function(grid, orgId, modeId){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SCORE_MOD_COM"]["TZ_SCRMOD_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.prompt","提示"), Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.prompt","提示"),Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
			var dfScoreItemsGridStore = panel.down("tabpanel").child('grid[name=dfScoreItemsGrid]').getStore();
			dfScoreItemsGridStore.tzStoreParams = '{"orgId":"'+orgId+'","modeId":"'+modeId+'","type":"A"}';
			
			var pwScoreItemsGridStore = panel.down("tabpanel").child('grid[name=pwScoreItemsGrid]').getStore();
			pwScoreItemsGridStore.tzStoreParams = '{"orgId":"'+orgId+'","modeId":"'+modeId+'","type":"B"}';
			
			var tzParams = '{"ComID":"TZ_SCORE_MOD_COM","PageID":"TZ_SCORE_MG_STD","OperateType":"QF","comParams":{"orgId":"'+orgId+'","modeId":"'+modeId+'"}}';
			Ext.tzLoad(tzParams,function(respData){
				var formData = respData;
				form.setValues(formData);
				
				dfScoreItemsGridStore.load();
				pwScoreItemsGridStore.load();
			});
		});

		tab = contentPanel.add(cmp);
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	
	//复制成绩模型
	copyScoreModel: function(grid, rowIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var orgId = rec.data.orgId;
		var modeId = rec.data.modeId;
		
		var className = 'KitchenSink.view.scoreModelManagement.copyScoreModelWin';
        if(!Ext.ClassManager.isCreated(className)){
             Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass();
        win.orgId = orgId;
        win.modeId = modeId;
        win.modelGrid = grid;
        
        win.show();
	},
	
	//确认复制
	copyScoreModelEnsure: function(btn){
		var win = btn.findParentByType('copyScoreModelWin');
		var form = win.child('form');
		var formRec = form.getForm().getValues();

		if(form.isValid()){
			var orgId = win.orgId;
			var modeId = win.modeId;
			var copyModeId = formRec.copyModeId;
			var copyTreeName = formRec.copyTreeName;
			
			var comParamsObj = {
				ComID: 'TZ_SCORE_MOD_COM',
				PageID: 'TZ_SCORE_MG_STD',
				OperateType: 'tzCopyScoreModel',
				comParams:{
					orgId: orgId,
					modeId: modeId,
					copyModeId: copyModeId,
					copyTreeName: copyTreeName
				}
			}
			var tzParams = Ext.JSON.encode(comParamsObj);
			
			Ext.tzSubmit(tzParams,function(respData){
				win.modelGrid.getStore().reload();
				win.close();				
			},"复制成功",true,this);
		}
	},
	
	
	//保存
	onScoreModelSave: function(btn){
		var closePanel = btn.closePanel;
		var panel = btn.findParentByType("scoreModelInfo");
		var actType = panel.actType;
		var form = panel.child('form');
		
		if(form.isValid()){
			var formObj;
			var addArr = [];
			var updateArr = [];
			var removeArr = [];

			//form表单数据
			var formRec = form.getForm().getValues();
			if(actType == "Add"){
				formObj = {
					type: 'modeInfo',
					data: formRec
				}
				addArr.push(formObj);
			}else if(actType == "Update"){
				formObj = {
					type: 'modeInfo',
					data: formRec
				}
				updateArr.push(formObj);
			}
			
			var dfScoreItemsGrid = panel.down("tabpanel").child('grid[name=dfScoreItemsGrid]');
			var dfScoreItemsGridStore = dfScoreItemsGrid.getStore();
			var dfGridModifyRec = dfScoreItemsGridStore.getModifiedRecords();
			var dfGridRemoveRec = dfScoreItemsGridStore.getRemovedRecords(); 
			var dfCellEditingPlugin = dfScoreItemsGrid.getPlugin('scoreItemsCellediting');
			
			//检查打分页面显示成绩项grid数据
			var dfGridAllRecs = dfScoreItemsGridStore.getRange();
			var dfItemsIdArr = [];
			for(var i=0; i<dfGridAllRecs.length; i++){
				var itemId = dfGridAllRecs[i].get("itemId");
				var viewName = dfGridAllRecs[i].get("viewName");
				if(itemId == "" || itemId == null){
					Ext.MessageBox.alert('提示', '打分页面成绩项ID不能为空！',
						function(e){
							if(e == "ok"|| e == "OK" || e == "确定"){
								for (var j=0; j<dfScoreItemsGrid.columns.length; j++) {
									if ("itemId"==dfScoreItemsGrid.columns[j].dataIndex){
										dfCellEditingPlugin.startEdit(dfGridAllRecs[i], dfScoreItemsGrid.columns[j]);
									}
								}
							}
						}
					);
					return;
				}
				if(viewName == "" || viewName == null){
					Ext.MessageBox.alert('提示', '打分页面显示名称不能为空！',
						function(e){
							if(e == "ok"|| e == "OK" || e == "确定"){
								for (var j=0; j<dfScoreItemsGrid.columns.length; j++) {
									if ("viewName"==dfScoreItemsGrid.columns[j].dataIndex){
										dfCellEditingPlugin.startEdit(dfGridAllRecs[i], dfScoreItemsGrid.columns[j]);
									}
								}
							}
						}
					);
					return;
				}
				
				if(dfItemsIdArr.indexOf(itemId)>=0){
					Ext.MessageBox.alert('提示', '打分页面信息项编号不能重复！');
					return;
				}else{
					dfItemsIdArr.push(itemId);
				}
			}
			
			//打分grid修改
			var dfModifyData = [];
			for(var i=0; i<dfGridModifyRec.length; i++){
				dfModifyData.push(dfGridModifyRec[i].data);
			}
			if(dfModifyData.length > 0){
				updateArr.push({
					type: 'dfItemsInfo',
					data: dfModifyData
				});
			}
			
			//打分grid删除
			var dfRemoveData = [];
			for(var i=0; i<dfGridRemoveRec.length; i++){
				dfRemoveData.push(dfGridRemoveRec[i].data);
			}
			if(dfRemoveData.length > 0){
				removeArr.push({
					type: 'dfItemsInfo',
					data: dfRemoveData
				});
			}
			
			
			var pwScoreItemsGrid = panel.down("tabpanel").child('grid[name=pwScoreItemsGrid]');
			var pwScoreItemsGridStore = pwScoreItemsGrid.getStore();
			var pwGridModifyRec = pwScoreItemsGridStore.getModifiedRecords(); 
			var pwGridRemoveRec = pwScoreItemsGridStore.getRemovedRecords(); 
			var pwCellEditingPlugin = pwScoreItemsGrid.getPlugin('scoreItemsCellediting');
			
			//检查打分页面显示成绩项grid数据
			var pwGridAllRecs = pwScoreItemsGridStore.getRange();
			var pwItemsIdArr = [];
			for(var i=0; i<pwGridAllRecs.length; i++){
				var itemId = pwGridAllRecs[i].get("itemId");
				var viewName = pwGridAllRecs[i].get("viewName");
				if(itemId == "" || itemId == null){
					Ext.MessageBox.alert('提示', '评委打印页成绩项ID不能为空！',
						function(e){
							if(e == "ok"|| e == "OK" || e == "确定"){
								for (var j=0; j<pwScoreItemsGrid.columns.length; j++) {
									if ("itemId"==pwScoreItemsGrid.columns[j].dataIndex){
										pwCellEditingPlugin.startEdit(pwGridAllRecs[i], pwScoreItemsGrid.columns[j]);
									}
								}
							}
						}
					);
					return;
				}
				if(viewName == "" || viewName == null){
					Ext.MessageBox.alert('提示', '评委打印页显示名称不能为空！',
						function(e){
							if(e == "ok"|| e == "OK" || e == "确定"){
								for (var j=0; j<pwScoreItemsGrid.columns.length; j++) {
									if ("viewName"==pwScoreItemsGrid.columns[j].dataIndex){
										pwCellEditingPlugin.startEdit(pwGridAllRecs[i], pwScoreItemsGrid.columns[j]);
									}
								}
							}
						}
					);
					return;
				}
				
				if(pwItemsIdArr.indexOf(itemId)>=0){
					Ext.MessageBox.alert('提示', '评委打印页信息项编号不能重复！');
					return;
				}else{
					pwItemsIdArr.push(itemId);
				}
			}
			
			
			//评委grid修改
			var pwModifyData = [];
			for(var i=0; i<pwGridModifyRec.length; i++){
				pwModifyData.push(pwGridModifyRec[i].data);
			}
			if(pwModifyData.length > 0){
				updateArr.push({
					type: 'pwItemsInfo',
					data: pwModifyData
				});
			}
			
			//评委grid删除
			var pwRemoveData = [];
			for(var i=0; i<pwGridRemoveRec.length; i++){
				pwRemoveData.push(pwGridRemoveRec[i].data);
			}
			if(pwRemoveData.length > 0){
				removeArr.push({
					type: 'dfItemsInfo',
					data: pwRemoveData
				});
			}
			
			var comParamsObj = {
				ComID: 'TZ_SCORE_MOD_COM',
				PageID: 'TZ_SCRMOD_DEFN_STD',
				OperateType: 'U',
				comParams:{
					add: addArr,
					delete: removeArr,
					update: updateArr
				}
			}
			
			var tzParams = Ext.JSON.encode(comParamsObj);
			//console.log(tzParams);
			
			Ext.tzSubmit(tzParams,function(respData){
				if(respData.result == "success"){
					form.getForm().setValues(respData.formData);
					if(actType=="Add"){
						panel.actType = "Update";
						//设置成绩模型ID只读
						var modelIdField = form.down("textfield[name=modelId]");
						modelIdField.setReadOnly(true);
						modelIdField.addCls("lanage_1");
						//显示tabpanel
						panel.down("tabpanel").setHidden(false);
					}
					
					panel.reloadGrid();//刷新成绩模型管理grid
				}
				
				if(closePanel == "Y"){
					panel.close();
				}
			},"保存成功",true,this);
		}
	},
	
	//确定
	onScoreModelEnsure: function(btn){
		this.onScoreModelSave(btn);
	},
	//关闭
	onScoreModelClose: function(btn){
		var panel = btn.findParentByType("scoreModelInfo");
		panel.close();
	},
	
	//选择成绩模型树
	pmtSearchScoreModelTree: function(field){
		Ext.tzShowPromptSearch({
            recname: 'TZ_TREEDEFN',
            searchDesc: '选择成绩模型树',
            maxRow:20,
            condition:{
                presetFields:{
                	TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    },
                    TZ_TREE_TYPE:{
                        value: 'A', //成绩模型树
                        type: '01'
                    },
                    TZ_EFFECT:{
                    	value: 'Y',
                        type: '01'
                    }
                },
                srhConFields:{
                	TREE_NAME:{
                        desc:'成绩树',
                        operator:'01',
                        type:'01'
                    },
                    DESCR:{
                        desc:'树描述',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
            	TREE_NAME: '成绩树',
            	DESCR: '树描述'
            },
            multiselect: false, 
            callback: function(selection){
            	field.setValue(selection[0].data.TREE_NAME);
            }
        });
	},
	
	
	addScoreItem: function(btn){
		var form = btn.up('scoreModelInfo').child('form').getForm();
		var grid = btn.findParentByType('grid');
		var gridStore = grid.getStore();
		var type = grid.tabType;
		var rowCount = gridStore.getCount();
		
		var modeId = form.getValues().modelId;
		
		var CellEditing = grid.getPlugin('scoreItemsCellediting');	
		
		var row = Ext.create('KitchenSink.view.scoreModelManagement.scoreItemsModel', {
			orgId: Ext.tzOrgID,
			modeId: modeId,
			itemId: "",
			itemType: type,
			sortNum: rowCount + 1,
			viewName:""
    	});
		
		gridStore.insert(rowCount,row);
		
		CellEditing.startEditByPosition({
	       	row: rowCount,
	       	column: 2
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
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.confirm","确认"),Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.nqdyscsxjlm","您确定要删除所选记录吗"), function(btnId){
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
	
	
	
	//设置树结构
	setTreeManager: function(btn){
		var me = this;
		var panel = btn.findParentByType("scoreModelInfo");
		var formRec = panel.child("form").getForm().getValues();
		
		var treeName = formRec["treeName"];
		
		if(treeName == "" || treeName == null){
			Ext.Msg.alert("提示","请选择或填写树名称");  
		}else{
			var tzParams ='{"ComID":"TZ_SCORE_MOD_COM","PageID":"TZ_SCRMOD_DEFN_STD","OperateType":"queryScoreModelTree","comParams":{"treeName":"'+ treeName +'"}}';
			Ext.tzLoadAsync(tzParams,function(respData){
				treeExists = respData.treeExists;
				if(treeExists == "Y"){
					me.viewTreeManager(treeName);
				}else if(treeExists == "YN"){
					Ext.Msg.alert("提示","系统已存在这棵树，请重新命名！"); 
				}else{
					Ext.MessageBox.confirm("提示","系统没有此树，是否创建一棵新树？",function(btnId){
		                if(btnId == 'yes'){
		                	
		                	var tzTreeParams ='{"ComID":"TZ_SCORE_MOD_COM","PageID":"TZ_SCRMOD_DEFN_STD","OperateType":"createScoreModelTree","comParams":{"treeName":"'+ treeName +'"}}';
		                	Ext.tzSubmit(tzTreeParams,function(respData){
		                		var result = respData.result;
		                		if(result == "success"){
		                			me.viewTreeManager(treeName);
		                		}else{
		                			Ext.Msg.alert("提示","创建树失败！");  
		                		}
		                	});
		                }
					});  
				}
			});
		}
	},
	
	//查看树管理
	viewTreeManager: function(treeName){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SCORE_MOD_COM"]["TZ_TREE_MG_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.prompt","提示"), Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.prompt","提示"),Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_SCRMOD_DEFN_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
			treeName: treeName
		},
		cmp = new ViewClass(config,function(){
			//回调函数
			
		});

		cmp.on('afterrender',function(panel){
			
		});

		tab = contentPanel.add(cmp);
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	
	//选择成绩项
	selectScoreModelItemId: function(btn){
		var grid = btn.up('grid');
		var formRec = grid.up('scoreModelInfo').child('form').getForm().getValues();
		var record = grid.getSelectionModel().getSelection()[0];
		
		var orgId = formRec.orgId;
		var treeName = formRec.treeName;
		
		Ext.tzShowPromptSearch({
            recname: 'PS_TZ_MODAL_DT_TBL',
            searchDesc: '查询成绩项',
            maxRow:20,
            condition:{
                presetFields:{
                	TZ_JG_ID:{
                        value: orgId,
                        type: '01'
                    },
                    TREE_NAME:{
                        value: treeName,
                        type: '01'
                    }
                },
                srhConFields:{
                	TZ_SCORE_ITEM_ID:{
                        desc:'成绩项ID',
                        operator:'01',
                        type:'01'
                    },
                    DESCR:{
                        desc:'成绩项名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
            	TZ_SCORE_ITEM_ID: '成绩项ID',
            	DESCR: '成绩项名称'
            },
            multiselect: false, 
            callback: function(selection){
                record.set("itemId",selection[0].data.TZ_SCORE_ITEM_ID);
            }
        });
	},
	
	//可配置搜索
	cfgSearchScoreModel: function(btn){    
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.TZ_RS_MODAL_TBL',
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
	
	onGridPanelClose: function(btn){
		//关闭
		var grid = btn.findParentByType("grid");
		grid.close();
	},
	
	
	testAutoScreen: function(){
		Ext.tzSetCompResourses("TZ_AUTO_SCREEN_COM");
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUTO_SCREEN_COM"]["TZ_AUTO_SCREEN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUTO_SCREEN_STD，请检查配置。');
			return;
		}

		var contentPanel,cmp, className, ViewClass, clsProto;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');
	
		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}	
		
		ViewClass = Ext.ClassManager.get(className);

		cmp = new ViewClass({
			classId:'105',
			batchId:'45'
		});
		
		cmp.on('afterrender',function(gridPanel){
			

		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}

	}
	
	
	
});