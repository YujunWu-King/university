Ext.define('KitchenSink.view.scoreModelManagement.scoreModelTreeManagerController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.scoreModelTreeManagerController',
	
	
	viewScoreModelTreeNode: function(config, treeManager){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SCORE_MOD_COM"]["TZ_TREE_NODE_STD"];
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

		if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}
		ViewClass = Ext.ClassManager.get(className);
		clsProto = ViewClass.prototype;

		var win = new ViewClass(config,function(){
			//回调函数
			var store = new KitchenSink.view.scoreModelManagement.scoreModelTreeStore({
	        	treeName: config.treeName
	        });
			
			treeManager.reconfigure(store);
		});
		
		var treeName = config.treeName;
		var form = win.child('form').getForm();
		//新增时默认成绩项类型为A
		if(config.actType == "A"){
			form.setValues({itemType: "A",treeName: treeName, orgId: Ext.tzOrgID});
		}else{
			var OpeItemId = config.OperatorItemId;//操作树节点
			var tzParams = '{"ComID":"TZ_SCORE_MOD_COM","PageID":"TZ_TREE_NODE_STD","OperateType":"QF","comParams":{"treeName":"'+treeName+'","OpeItemId":"'+OpeItemId+'"}}';
			Ext.tzLoad(tzParams,function(respData){
				form.setValues(respData);
				
				var itemIdField = form.findField("itemId");
				itemIdField.setReadOnly(true);
				itemIdField.addCls("lanage_1");
				
				var gridStore = win.down('grid').getStore();
				gridStore.tzStoreParams = '{"orgId":"'+respData.orgId+'","treeName":"'+treeName+'","itemId":"'+OpeItemId+'"}';
				gridStore.load();
			});
		}
		
		win.show();
	},
	
	
	/**
	 * 插入兄弟节点
	 * 参数：actType：A-新增（包括插入子节点、插入同级节点），U-编辑；
	 *		insertType：A-插入同级节点，B-插入子节点；
	 */
	insertBrotherNode: function(btn){
		var treeManager = btn.findParentByType("scoreModelTreeManager");
		var treeName = treeManager.treeName;
		var selectTreeNode = treeManager.selectTreeNode;
		
		if(selectTreeNode){
			if(selectTreeNode.root){
				//如果是根节点，不可以插入兄弟节点
				Ext.Msg.alert("提示","根节点不能插入同级节点");
			}else{
				var config = {
						treeName: treeName,
						actType: "A",
						insertType: 'A',
						OperatorItemId: selectTreeNode.itemId
					}
				this.viewScoreModelTreeNode(config, treeManager);
			}
		}else{
			Ext.Msg.alert("提示","请先选择树节点");
		}
	},
	
	/**
	 * 插入子节点
	 * 参数：actType：A-新增（包括插入子节点、插入同级节点），U-编辑；
	 *		insertType：A-插入同级节点，B-插入子节点；
	 */
	insertChildNode: function(btn){
		var treeManager = btn.findParentByType("scoreModelTreeManager");
		var treeName = treeManager.treeName;
		var selectTreeNode = treeManager.selectTreeNode;
		
		if(selectTreeNode){
			var config = {
					treeName: treeName,
					actType: "A",
					insertType: 'B',
					OperatorItemId: selectTreeNode.itemId
				}
			this.viewScoreModelTreeNode(config, treeManager);
		}else{
			Ext.Msg.alert("提示","请先选择树节点");
		}
	},
	
	/**
	 * 编辑节点
	 * 参数：actType：A-新增（包括插入子节点、插入同级节点），U-编辑；
	 *		insertType：A-插入同级节点，B-插入子节点；
	 */
	editNode: function(btn){
		var treeManager = btn.findParentByType("scoreModelTreeManager");
		var treeName = treeManager.treeName;
		var selectTreeNode = treeManager.selectTreeNode;
		
		if(selectTreeNode){
			var config = {
					treeName: treeName,
					actType: "U",
					insertType: '',
					OperatorItemId: selectTreeNode.itemId
				}
			this.viewScoreModelTreeNode(config, treeManager);
		}else{
			Ext.Msg.alert("提示","请先选择树节点");
		}
	},
	
	
	/**
	 * 删除节点
	 */
	removeNode:function(btn){
		var treeManager = btn.findParentByType("scoreModelTreeManager");
		var treeName = treeManager.treeName;
		var selectTreeNode = treeManager.selectTreeNode;
		
		if(selectTreeNode){
			Ext.MessageBox.confirm('确认', '您确定要删除所选成绩项吗?', function(btnId){
				if(btnId == 'yes'){			
					var delArr = [];
					var delObj = {
						orgId: Ext.tzOrgID,
						treeName: treeName,
						itemId: selectTreeNode.itemId
					};
					
					delArr.push(delObj);
					var comParamsObj = {
						ComID: 'TZ_SCORE_MOD_COM',
						PageID: 'TZ_TREE_MG_STD',
						OperateType: 'U',
						comParams:{
							delete: delArr
						}
					}
					var tzParams = Ext.JSON.encode(comParamsObj);
					Ext.tzSubmit(tzParams,function(respData){
						//console.log(respData);
						var store = new KitchenSink.view.scoreModelManagement.scoreModelTreeStore({
				        	treeName: treeName
				        });
						
						treeManager.reconfigure(store);
					},"删除成功",true,this);
				}												  
			},this);  
		}else{
			Ext.Msg.alert("提示","请先选择树节点");
		}
	},
	
	
	
	onScoreModelSave: function(btn){
		this.saveScoreModelHandler(btn);
	},
	
	
	onScoreModelEnsure: function(btn){
		this.saveScoreModelHandler(btn);
	},
	
	
	saveScoreModelHandler: function(btn){
		var win = btn.findParentByType("scoreModelTreeNodeWin");
		var form = win.child('form');
		var formRec = form.getForm().getValues();
		var updateArr = [];
		var AddArr = [];
		var deleteArr = [];
		var closePanel = btn.closePanel;
		if(form.isValid()){
			//自动初筛CheckBox
			if(formRec.autoScreen != "Y"){
				formRec.autoScreen = "N";
			}
			//表单信息
			var formObj = {
				type: 'nodeInfo',
				data: formRec
			}
			//下拉选项信息
			var gridStore = win.down('grid[name=comboTypeGrid]').getStore();
			var gridModifyRec = gridStore.getModifiedRecords();
			var gridRemoveRec = gridStore.getRemovedRecords(); 
			//grid修改记录
			var modifyData = [];
			for(var i=0; i<gridModifyRec.length; i++){			
				var modObj = gridModifyRec[i].data;
				if(modObj.isDefault){
					modObj.isDefault = "Y";
				}else{
					modObj.isDefault = "N";
				}
				modifyData.push(modObj);
			}
			//grid删除记录
			var removeData = [];
			for(var i=0; i<gridRemoveRec.length; i++){
				removeData.push(gridRemoveRec[i].data);
			}
			
			var config = win.opeConfig;
			if(config.actType == "U"){
				updateArr.push(formObj);
				
				if(modifyData.length>0){
					updateArr.push({
						type: 'comboOpt',
						data: modifyData
					});
				}
				if(removeData.length>0){
					deleteArr.push({
						type: 'comboOpt',
						data: removeData
					});
				}
			}else if(config.actType == "A"){
				formObj.insertType = config.insertType;
				formObj.OperatorItemId = config.OperatorItemId;
				AddArr.push(formObj);
				
				if(modifyData.length>0){
					AddArr.push({
						type: 'comboOpt',
						data: modifyData
					});
				}
			}
			
			var comParamsObj = {
				ComID: 'TZ_SCORE_MOD_COM',
				PageID: 'TZ_TREE_NODE_STD',
				OperateType: 'U',
				comParams:{
					add: AddArr,
					update: updateArr,
					delete: deleteArr
				}
			}
			var tzParams = Ext.JSON.encode(comParamsObj);
			Ext.tzSubmit(tzParams,function(respData){
				if(respData.result == "success"){
					if(config.actType == "A"){
						win.opeConfig.actType = "U";
						//设置成绩项ID只读
						var itemIdField = form.getForm().findField("itemId");
						itemIdField.setReadOnly(true);
						itemIdField.addCls("lanage_1");
					}
					
					win.treeReload();
				}
				
				if(closePanel == "Y"){
					win.close();
				}
			},"保存成功",true,this);
		}
	},
	
	
	
	onScoreModelClose: function(btn){
		btn.findParentByType("scoreModelTreeNodeWin").close();
	},
	
	
	addScoreItemOption: function(btn){
		var form = btn.up('scoreModelTreeNodeWin').child('form').getForm();
		var grid = btn.findParentByType('grid');
		var gridStore = grid.getStore();

		var rowCount = gridStore.getCount();	
		var orgId = form.getValues().orgId,
			treeName = form.getValues().treeName,
			itemId = form.getValues().itemId;
		
		var CellEditing = grid.getPlugin('TypeDCellediting');	
		
		var row = Ext.create('KitchenSink.view.scoreModelManagement.scoreItemOptionsModel', {
			orgId: Ext.tzOrgID,
			treeName: treeName,
			itemId: itemId,
			optId: "",
			optName: "",
			optScore:"",
			isDefault:""
    	});
		
		gridStore.insert(rowCount,row);
		
		CellEditing.startEditByPosition({
	       	row: rowCount,
	       	column: 0
    	});
	},
	
	deleteScoreItemOptions: function(btn){
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
				}												  
			},this);  
	    }
	},
	
	deleteCurrentRow: function(grid, rowIndex){
		var store = grid.store;
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.confirm","确认"),Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.nqdyscsxjlm","您确定要删除所选记录吗"), function(btnId){
			var removeRec = store.getAt(rowIndex);
			if(btnId == 'yes'){
				store.remove(removeRec);
			}
		},this);
	},
	
	//选择参考资料
	ckzlPmtSearch: function(field){
		var form = field.findParentByType('form').getForm();
		var formRec = form.getValues();
		var orgId = formRec.orgId;
		Ext.tzShowPromptSearch({
            recname: 'PS_TZ_CKZL_T',
            searchDesc: '查询参考资料',
            maxRow:50,
            condition:{
                presetFields:{
                	TZ_JG_ID:{
                        value: orgId,
                        type: '01'
                    }
                },
                srhConFields:{
                	TZ_CKZL_ID:{
                        desc:'参考资料ID',
                        operator:'01',
                        type:'01'
                    },
                    TZ_CKZL_NAME:{
                        desc:'参考资料名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
            	TZ_CKZL_ID: '参考资料ID',
            	TZ_CKZL_NAME: '参考资料名称'
            },
            multiselect: false, 
            callback: function(selection){
            	field.setValue(selection[0].data.TZ_CKZL_ID);
            	form.findField('refDataDescr').setValue(selection[0].data.TZ_CKZL_NAME);
            }
        });
	}
});