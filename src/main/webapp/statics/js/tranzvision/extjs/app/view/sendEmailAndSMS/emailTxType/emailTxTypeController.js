Ext.define('KitchenSink.view.sendEmailAndSMS.emailTxType.emailTxTypeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.emailTxTypeController', 

    addTxType: function(btn) {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EMLTX_TYPE_COM"]["TZ_TXTYPE_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
		return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_TXTYPE_DEFN_STD，请检查配置。');
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

        var grid = btn.findParentByType('emailTxTypeList');
        cmp = new ViewClass({
        	actType: 'add',
        	reloadGrid: function(){
        		grid.getStore().reload();
        	}
        });
		
        tab = contentPanel.add(cmp);    
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    
    editCurrentTxType: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //退信条件ID
	   	 var txTypeId = selRec.get("txTypeId");
	     //显示组件注册信息编辑页面
	     this.editTxTypeDefnByKey(txTypeId,view);
	},
	
	editTxType: function() {
		var grid = this.getView();
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
        //退信条件ID
        var txTypeId = selList[0].get("txTypeId");
        //显示邮箱服务器编辑页面
        this.editTxTypeDefnByKey(txTypeId,grid);
    },
    
    
    editTxTypeDefnByKey: function(txTypeId,grid) {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EMLTX_TYPE_COM"]["TZ_TXTYPE_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_TXTYPE_DEFN_STD，请检查配置。');
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

        cmp = new ViewClass({
        	actType: 'update',
        	reloadGrid: function(){
        		grid.getStore().reload();
        	}
        });
        
		cmp.on('afterrender',function(panel){
			//表单信息;
			var form = panel.child('form').getForm();
			form.findField("txTypeId").setReadOnly(true);
			form.findField("txTypeId").addCls("lanage_1");
			
			//参数
			var tzParams = '{"ComID":"TZ_EMLTX_TYPE_COM","PageID":"TZ_TXTYPE_DEFN_STD","OperateType":"QF","comParams":{"txTypeId":"'+txTypeId+'"}}';
			
			Ext.tzLoad(tzParams,function(responseData){
				
				form.setValues(responseData);							
			});
			
			var ruleGridStore = panel.down('grid[reference=txTypeRuleGrid]').getStore();
			ruleGridStore.tzStoreParams = '{"txTypeId":"'+ txTypeId +'"}';
			ruleGridStore.reload();
		});
		
        tab = contentPanel.add(cmp);   
        
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    
    searchTxType: function(btn){     //searchComList为各自搜索按钮的handler event;

        Ext.tzShowCFGSearch({            

            cfgSrhId: 'TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.TZ_TX_TYPE_TBL',    
			condition:{
                //"TZ_JG_ID_1": Ext.tzOrgID          //设置搜索字段的默认值，没有可以不设置condition;
            },			
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });    
    },
    
    
    deleteTxType: function(){
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
				   var ruleStore = this.getView().store;
				   ruleStore.remove(selList);
				}												  
			},this);   
	   }
	},
	
	delCurrentTxType: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    
    saveTxType: function(btn){
        //资源集合列表
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
        }else{
            return;
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_EMLTX_TYPE_COM","PageID":"TZ_EMLTX_TYPE_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    
    ensureTxType: function(btn){
        //资源集合列表
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
			var tzParams = '{"ComID":"TZ_EMLTX_TYPE_COM","PageID":"TZ_EMLTX_TYPE_STD","OperateType":"U","comParams":{'+comParams+'}}';
			//保存数据
			Ext.tzSubmit(tzParams,function(){
				//关闭窗口
				grid.close();
			},"",true,this);
        }else{
			//关闭窗口
			grid.close();
        }        
    },
	closeEmailServer: function(btn){
        var grid = btn.findParentByType("emailTxTypeList");
		//关闭窗口
		grid.close();
    },

    
    addTxRule: function(btn){
    	var form = btn.up('emailTxTypePanel').child('form').getForm();
		var grid = btn.findParentByType('grid');
		var gridStore = grid.getStore();
		var rowCount = gridStore.getCount();
		
		var txTypeId = form.getValues().txTypeId;
		
		var CellEditing = grid.getPlugin('txTypeRuleCellediting');	
		
		var row = Ext.create('KitchenSink.view.sendEmailAndSMS.emailTxType.txTypeRuleModel', {
			txTypeId: txTypeId,
			txRuleId: '',
			txRuleName: '',
			matchType: ''
    	});
		
		gridStore.insert(rowCount,row);
		
		CellEditing.startEditByPosition({
	       	row: rowCount,
	       	column: 1
    	});
    },
    
    
    onFormSave: function(btn){
    	var panel = btn.findParentByType("emailTxTypePanel");
    	this.formSaveHandler(panel,function(){
    		if(panel.reloadGrid){
    			panel.reloadGrid();
    		}
    	});
    },
    
    onFormEnsure: function(btn){
    	var panel = btn.findParentByType("emailTxTypePanel");
    	this.formSaveHandler(panel,function(){
    		if(panel.reloadGrid){
    			panel.reloadGrid();
    		}
    		panel.close();
    	});
    },
    
    formSaveHandler: function(panel,callback){
    	var form = panel.child('form');
    	var actType = panel.actType;
    	if(form.isValid()){
    		var formRec = form.getForm().getValues();
    		var addArr = [];
    		var updateArr = [];
    		var removeArr = [];
    		var txTypeId = formRec.txTypeId;
    		
    		if(actType == "add"){
    			addArr.push({
    				type: 'FORMINFO',
    				data: formRec
    			});
    		}
    		
    		if(actType == "update"){
    			updateArr.push({
    				type: 'FORMINFO',
    				data: formRec
    			});
    		}
    		
    		var grid = form.down('grid[reference=txTypeRuleGrid]');
    		var CellEditingPlugin = grid.getPlugin('txTypeRuleCellediting');
    		var ModifyRec = grid.getStore().getModifiedRecords();
			var RemoveRec = grid.getStore().getRemovedRecords(); 
			
			
			//检查grid数据
			var GridAllRecs = grid.getStore().getRange();
			var ItemsIdArr = [];
			for(var i=0; i<GridAllRecs.length; i++){
				var txRuleId = GridAllRecs[i].get("txRuleId");
				if(txRuleId == "" || txRuleId == null){
					Ext.MessageBox.alert('提示', '请选择退信条件！',
						function(e){
							if(e == "ok"|| e == "OK" || e == "确定"){
								for (var j=0; j<grid.columns.length; j++) {
									if ("txRuleId"==grid.columns[j].dataIndex){
										CellEditingPlugin.startEdit(GridAllRecs[i], grid.columns[j]);
									}
								}
							}
						}
					);
					return;
				}
				
				if(ItemsIdArr.indexOf(txRuleId)>=0){
					Ext.MessageBox.alert('提示', '退信条件不能重复！');
					return;
				}else{
					ItemsIdArr.push(txRuleId);
				}
			}
			
			var ModifyData = [];
			for(var i=0; i<ModifyRec.length; i++){
				ModifyData.push(ModifyRec[i].data);
			}
			if(ModifyData.length > 0){
				updateArr.push({
					type: 'GRIDINFO',
					data: ModifyData
				});
			}
			
			var RemoveData = [];
			for(var i=0; i<RemoveRec.length; i++){
				RemoveData.push(RemoveRec[i].data);
			}
			if(RemoveData.length > 0){
				removeArr.push({
					type: 'GRIDINFO',
					data: RemoveData
				});
			}
    		
    		
    		var tzParamsObj = {
    			ComID: 'TZ_EMLTX_TYPE_COM',
    			PageID: 'TZ_TXTYPE_DEFN_STD',
    			OperateType: 'U',
    			comParams:{
    				add: addArr,
    				update: updateArr,
    				delete: removeArr
    			}
    		};

    		//提交参数
    		var tzParams = Ext.JSON.encode(tzParamsObj);
           
    		Ext.tzSubmit(tzParams,function(respData){
    			if(actType == "add"){
    				panel.actType = "update";	
        			
        			form.getForm().findField("txTypeId").setReadOnly(true);
    				form.getForm().findField("txTypeId").addCls("lanage_1");
    				
    				grid.setHidden(false);
    				var ruleGridStore = grid.getStore();
    				ruleGridStore.tzStoreParams = '{"txTypeId":"'+ txTypeId +'"}';
    				ruleGridStore.reload();
    			}
				callback();
			},"",true,this);
    	}
    },
    
    onFormClose: function(btn){
    	var panel = btn.findParentByType("emailTxTypePanel");
    	panel.close();
    },
    
    
    selectTzRule: function(btn){
    	var grid = btn.up('grid');
		var formRec = grid.up('emailTxTypePanel').child('form').getForm().getValues();
		var record = grid.getSelectionModel().getSelection()[0];
		
		var txTypeId = formRec.txTypeId;
		
		Ext.tzShowPromptSearch({
            recname: 'PS_TZ_TX_RULE_TBL',
            searchDesc: '查询退信条件',
            maxRow:50,
            condition:{
                presetFields:{
                	TZ_IS_USED:{
                        value: 'Y',
                        type: '01'
                    }
                },
                srhConFields:{
                	TZ_TX_RULE_ID:{
                        desc:'退信条件ID',
                        operator:'07',
                        type:'01'
                    },
                    TZ_RULE_DESC:{
                        desc:'退信条件名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
            	TZ_TX_RULE_ID: '成绩项ID',
            	TZ_RULE_DESC: '成绩项名称'
            },
            multiselect: false, 
            callback: function(selection){
                record.set("txRuleId",selection[0].data.TZ_TX_RULE_ID);
                record.set("txRuleName",selection[0].data.TZ_RULE_DESC);
            }
        });
    },
    
    
    removeTxRule: function(btn){
    	var grid = btn.findParentByType('grid');
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
 				   var ruleStore = grid.getStore();
 				   ruleStore.remove(selList);
 				}												  
 			},this);   
 	   }
    },
    
    
    deleteCurrentRow: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    }
});