Ext.define('KitchenSink.view.basicData.filter.filterDataSetController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.filterDataSetController', 
	//放大镜搜索Prompt字段名称
	searchDataSetFld: function(btn){
		var form = this.getView().child("form").getForm();
		var formValues = form.getValues();
		var promptTab = formValues['ViewMc'];
		
		if(promptTab == ""){
			Ext.Msg.alert("提示","获取记录出错。");
			return;
		}
				
		Ext.tzShowPromptSearch({
			recname: 'TZ_FIELDRECD_VW',
			searchDesc: '搜索字段名称',
			maxRow:20,
			condition:{
				presetFields:{
					RECNAME:{
						value:promptTab,
						operator:'07',
						type: '01'
					}
				},
				srhConFields:{
					FIELDNAME:{
						desc:'字段名称',
						operator:'07',
						type:'01'		
					},
					LONGNAME:{
						desc:'字段描述',
						operator:'07',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				FIELDNAME: '字段名称',
				LONGNAME: '字段描述'
			},
			multiselect: false,
			callback: function(selection){
				form.findField("dataSetFld").setValue(selection[0].data.FIELDNAME);
			}
		});	
	},
	//放大镜搜索Prompt字段名称
	searchDataSetRec: function(btn){
		var form = this.getView().child("form").getForm();
		var formValues = form.getValues();
				
		Ext.tzShowPromptSearch({
			recname: 'TZ_SCHEMA_TABLES_VW',
			searchDesc: '搜索记录名称',
			maxRow:20,
			condition:{
				srhConFields:{
					TABLE_NAME:{
						desc:'记录名称',
						operator:'07',
						type:'01'		
					},
					TABLE_COMMENT:{
						desc:'记录描述',
						operator:'07',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TABLE_NAME: '记录名称',
				TABLE_COMMENT: '记录描述'
			},
			multiselect: false,
			callback: function(selection){
				form.findField("searchRec").setValue(selection[0].data.TABLE_NAME);
			}
		});	
	},
	searchDataSetCondFld: function(btn){
		var win = btn.findParentByType("window");
		var form = win.child("form").getForm();
		var formValues = form.getValues();
		var promptTab = formValues['searchRec'];
		
		if(promptTab == ""){
			Ext.Msg.alert("提示","获取记录出错。");
			return;
		}
				
		Ext.tzShowPromptSearch({
			recname: 'TZ_FIELDRECD_VW',
			searchDesc: '搜索字段名称',
			maxRow:20,
			condition:{
				presetFields:{
					RECNAME:{
						value:promptTab,
						operator:'07',
						type: '01'
					}
				},
				srhConFields:{
					FIELDNAME:{
						desc:'字段名称',
						operator:'07',
						type:'01'		
					},
					LONGNAME:{
						desc:'字段描述',
						operator:'07',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				FIELDNAME: '字段名称',
				LONGNAME: '字段描述'
			},
			multiselect: false,
			callback: function(selection){
				form.findField("dstCondFld").setValue(selection[0].data.FIELDNAME);
			}
		});	
	},
    addCond: function() {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存页面信息后，再新增查询条件信息。");
			return;
		}
        //是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_FLDDST_CON_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_FLDDST_CON_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('filterDataSetCondWindow');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		var form = this.getView().child("form").getForm();
		var formParams = form.getValues();
		var ComID = formParams['ComID'];
   	 	var PageID = formParams['PageID'];
   	 	var ViewMc = formParams['ViewMc'];
		var fieldOrder = formParams['fieldOrder'];
   	 	var winform = win.child("form").getForm();
   	 	var grid = win.child("form").child("grid");
   	 	winform.setValues(formParams);

        win.show();
    },
    editSelCond: function(){
    	//var store = view.findParentByType("grid").store;
	 		//var selRec = store.getAt(rowIndex);
		 	//选中行
	    var panel = this.getView();
	    var grid = panel.down('grid[name=filterCondGrid]');
	    var selList = grid.getSelectionModel().getSelection();
	   //选中行长度
	    var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		    return;
	    }
	    selRec = selList[0];
	   
   	 	var ComID = selRec.get("ComID");
   	 	var PageID = selRec.get("PageID");
   	 	var ViewMc = selRec.get("ViewMc");
   	 	var fieldOrder = selRec.get("fieldOrder");
		var condOrder = selRec.get("orderCond");
   	 	
     	//显示编辑页面
     	this.editFilterCondByID(ComID,PageID,ViewMc,fieldOrder,condOrder);
    },
    editCond: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	
   	 	var ComID = selRec.get("ComID");
   	 	var PageID = selRec.get("PageID");
   	 	var ViewMc = selRec.get("ViewMc");
   	 	var fieldOrder = selRec.get("fieldOrder");
		var condOrder = selRec.get("orderCond");
   	 	
     	//显示编辑页面
     	this.editFilterCondByID(ComID,PageID,ViewMc,fieldOrder,condOrder);
    },
    editFilterCondByID: function(ComID,PageID,ViewMc,fieldOrder,condOrder){
        if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存页面信息后，再新增查询条件信息。");
			return;
		}
        //是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_FLDDST_CON_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_FLDDST_CON_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('filterDataSetCondWindow');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_FLDDST_CON_STD","OperateType":"QF","comParams":{"ComID":"'+ComID+'","PageID":"'+PageID+'","ViewMc":"'+ViewMc+'","fieldOrder":"'+fieldOrder+'","orderCond":"'+condOrder+'"}}';
		
		//页面注册信息表单
		var form = win.child("form").getForm();
		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData.formData);
			//form.findField("condOrder").setReadOnly(true);
			//form.findField("condOrder").setFieldStyle('background:#F4F4F4');
		});
		win.show();
    },
    deleteConds: function(btn){
        //资源信息列表
        var panel = this.getView();
		var grid = panel.down('grid[name=filterCondGrid]');
		console.log(grid);
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
    onFilterDataSetFldSave: function(){
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getFilterInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";	
				//资源集合信息数据
                form.setValues(responseData);
			},"",true,this);
		}
    },
    onFilterDataSetFldEnsure: function(){
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getFilterInfoParams();
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				//关闭窗口						   
				comView.close();	
			},"",true,this);
		}
    },
    onFilterDataSetFldClose: function(){
		this.getView().close();
	},
	getFilterInfoParams: function(){
        //资源集合表单
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();
		
        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"FILTER","data":'+Ext.JSON.encode(formParams)+'}]';
        }
        //修改json字符串
        var editJson = "";
        var editfldJson="";
        //资源信息列表
//		var grid = this.getView().child('grid');
//        //资源信息数据
//        var store = grid.getStore();
        
        var tabpanel = this.getView().child("tabpanel");
		var grid1 = tabpanel.down('grid[name=filterCondGrid]');
		var grid2 = tabpanel.down('grid[name=filterRoleGrid]');
		var store1 = grid1.getStore();
		var store2 = grid2.getStore();
        
        
        //列表中修改的记录
		var mfRecs = store1.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editfldJson == ""){
				editfldJson = Ext.JSON.encode(mfRecs[i].data);
			}else{
				editfldJson = editfldJson + ','+Ext.JSON.encode(mfRecs[i].data);
			}
		}
		
        if(actType == "update"){
            editJson = '{"typeFlag":"FILTER","data":'+Ext.JSON.encode(formParams)+',"updateList":[' + editfldJson + ']}';
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
		if(actType == "update"){
			var removeRecs = store1.getRemovedRecords();
			for(var i=0;i<removeRecs.length;i++){
				if(removeJson == ""){
					removeJson = Ext.JSON.encode(removeRecs[i].data);
				}else{
					removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
				}
			}
			
			
			var removeJsonRole = "";
			var removeRoles = store2.getRemovedRecords();
			for(var i=0;i<removeRoles.length;i++){
				if(removeJsonRole == ""){
					removeJsonRole = Ext.JSON.encode(removeRoles[i].data);
				}else{
					removeJsonRole = removeJsonRole + ','+Ext.JSON.encode(removeRoles[i].data);
				}
			}
			var removeJsonData = '{"typeFlag":"Condition","removeList":[' + removeJson + ']}';
			removeJsonData = removeJsonData + "," + '{"typeFlag":"Roles","removeList":[' + removeJsonRole + ']}';
			console.log(removeJsonData);
			
	//        if(removeJson != ""){
	//            if(comParams == ""){
	//                comParams = '"delete":[' + removeJson + "]";
	//            }else{
	//                comParams = comParams + ',"delete":[' + removeJson + "]";
	//            }
	//        }
			if(removeJsonData != ""){
				if(comParams == ""){
					comParams = '"delete":[' + removeJsonData + "]";
				}else{
					comParams = comParams + ',"delete":[' + removeJsonData + "]";
				}
        }
		}
        
        //提交参数
        var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_FLDDST_FLD_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    deleteCond: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
	onFilterDataSetCondWinEnsure: function(btn){
		var panel = this.getView();
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		//表单数据
        var formParams = form.getValues();
		//组件信息标志
        var actType = win.actType;
        //更新操作参数
        var comParams = "";

        comParams = '"update":[{"data":'+Ext.JSON.encode(formParams)+'}]';

		//提交参数
        var tzParams = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_FLDDST_CON_STD","OperateType":"U","comParams":{'+comParams+'}}';
        console.log(tzParams);
		if (form.isValid()) {
			//获取组件注册信息参数

			Ext.tzSubmit(tzParams,function(responseData){
				//关闭窗口		
				var grid = panel.down('grid[name=filterCondGrid]');
				grid.store.reload();
				win.close();	
			},"",true,this);
		}
	},
	onFilterDataSetCondWinClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	},
	previewCondSql: function() {
        //是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_FILTER_COM"]["TZ_FLDDST_SQL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_FLDDST_SQL_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('filterDataSetCondWindow');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		var panel = this.getView();
		var form = this.getView().child("form").getForm();
		var formParams = form.getValues();
		var dataSetFld = formParams["dataSetFld"];
		var searchRec = formParams["searchRec"];
		var grid = panel.down('grid[name=filterCondGrid]');
		var store = grid.store;
		var condSql = "SELECT " + dataSetFld + " FROM " + searchRec;
		//列表中修改的记录
		var recs = store.getRange(); 
		for(var i=0;i<recs.length;i++){
			if(i==0){
				condSql = condSql + " WHERE ";
			}
			if(recs[i].data.dstAndOr!=""){
				condSql = condSql + recs[i].data.dstAndOr + " ";
			}
			if(recs[i].data.leftParen!=""){
				condSql = condSql + recs[i].data.leftParen;
			}
			if(recs[i].data.dstCondFld!=""){
				condSql = condSql + recs[i].data.dstCondFld + " ";
			}
			var dstCondValue = "";
			if(recs[i].data.dstCondValueType=="A"){
				dstCondValue = TranzvisionMeikecityAdvanced.Boot.loginUserId;
			}else if(recs[i].data.dstCondValueType=="B"){
				///获取当前日期(到秒)
				var curDate = new Date()
				//格式化;
				var curDateFormat = Ext.Date.format(curDate, 'Y-m-d');
				dstCondValue = curDateFormat;
			}else{
				dstCondValue = recs[i].data.dstCondFldValue;
			}
			switch(recs[i].data.dstOperator)
			{
				case "01":
				  dstOperator = "=";
				  condSql = condSql + "= " + "'" + dstCondValue +"' ";
				  break;
				case "02":
				  condSql = condSql + "<> " + "'" + dstCondValue +"' ";
				  break;
				case "03":
				  condSql = condSql + "> " + "'" + dstCondValue +"' ";
				  break;
				case "04":
				  condSql = condSql + ">= " + "'" + dstCondValue +"' ";
				  break;
				case "05":
				  condSql = condSql + "< " + "'" + dstCondValue +"' ";
				  break;
				case "06":
				  condSql = condSql + "<= " + "'" + dstCondValue +"' ";
				  break;
				case "07":
				  condSql = condSql + "LIKE " + "'%" + dstCondValue +"%' ";
				  break;
				default:
			}
			if(recs[i].data.rightParen!=""){
				condSql = condSql + recs[i].data.rightParen;
			}
		}

		formParams["previewsql"] = condSql;

   	 	var winform = win.child("form").getForm();
		winform.setValues(formParams);
        win.show();
    },
	onFilterDataSetCondSqlWinClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		win.close();
	},
    
    
    addRole:function(btn){
	        var arrAddRoleValue=[];
	        
	        var form = this.getView().child("form").getForm();
    		var formParams = form.getValues();
    		var CompID = formParams['ComID'];
    		var PageID = formParams['PageID'];
       	 	var ViewMc = formParams['ViewMc'];
       	 	var fieldOrder = formParams['fieldOrder'];
       	 	
       	 	var tabpanel = this.getView().child("tabpanel");
    	 	var grid = tabpanel.down('grid[name=filterRoleGrid]');
			var store = grid.getStore();
       	 	
	        
    	 Ext.tzShowPromptSearch({
	            recname: 'PSROLEDEFN_VW',
	            searchDesc: '新增数据集角色',
	            condition:{
//	                presetFields:{
//	                	TZ_JG_ID:{
//	                        value: Ext.tzOrgID,
//	                        type: '01'
//	                    }
//	                },
	                srhConFields:{
	                	ROLENAME:{
	                        desc:'角色名称',
	                        operator:'07',
	                        type:'01'
	                    },
	                    DESCR:{
	                        desc:'描述',
	                        operator:'07',
	                        type:'01'
	                    }
	                }
	            },
	            srhresult:{
	            	ROLENAME:'角色名称',
	            	DESCR: '描述'
	            },
	            multiselect: true,
	            callback: function(selection){
	                if (selection.length>0){
	                    for(j=0;j<selection.length;j++){
	                    	arrAddRoleValue.push(selection[j].data.ROLENAME);	                       
	                    };
	                    
	      
	            		var roleId = arrAddRoleValue;
	            		RoleID =roleId;
	            		
	            		var roleNum=arrAddRoleValue.length;
	            		
	        			var oprIDJson = "";
	        			oprIDJson = '{"CompID":"' + CompID + '","PageID":"' + PageID + '","ViewMc":"' + ViewMc + '","fieldOrder":"' + fieldOrder + '","RoleID":"' + RoleID + '"}';

	        			
	        			var comParamsOPRID = "";
	        			if(oprIDJson != ""){
	        				comParamsOPRID = '"add":[' + oprIDJson + "]";
	        			}
	        			var tzParams2 = '{"ComID":"TZ_GD_FILTER_COM","PageID":"TZ_FLDDST_CON_STD","OperateType":"U","comParams":{'+comParamsOPRID+'}}';
	        			console.log(tzParams2);
	        		
	        				            		
//	        			Ext.tzSubmit(tzParams2,function(responseData){
//	                        if(store.isLoaded()){
//	                            store.reload();
//	                        }
//	        			},"",true,this);
	        			
	        			Ext.tzSubmit(tzParams2,function(resp){
	        				 if(store.isLoaded()){
		                            store.reload();
		                        }
	        			},"",true,this,RoleID);
	        			
	            		
	                }
	            }
	        
    	 })
    },
    
    
//    deleteConds: function(btn){
    deleteRole: function(btn){
        //资源信息列表
    	var tabpanel = this.getView().child("tabpanel");
	 	var grid = tabpanel.down('grid[name=filterRoleGrid]');
		console.log(grid);
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
    
//    deleteCond: function(view, rowIndex){
    deleteSelectedRole: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
	
	
});