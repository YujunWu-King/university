Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.interviewStuViewController',
    
    searchInterviewStu: function(btn){
    	var win = btn.findParentByType("interviewStuViewAddWin");
    	Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_MS_ARR_MG_COM.TZ_MSYY_ADD_STD.TZ_MS_STU_VW',
            condition:{
            	TZ_CLASS_ID: win.classID,
            	TZ_APPLY_PC_ID: win.batchID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    
    //添加面试预约考生
    addInterviewAppoStu: function(btn){
    	var formRec = btn.up("form").getForm().getValues();
    	var stuGridStore = btn.findParentByType("grid").getStore();
    	//选择预算时间安排
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_MSYY_ADD_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSYY_ADD_STD，请检查配置。');
			return;
		}
    	
    	if(!Ext.ClassManager.isCreated(className)){
			Ext.syncRequire(className);
		}
		ViewClass = Ext.ClassManager.get(className);
		var config = {
				classID: formRec["classID"],
				batchID: formRec["batchID"]
			}
		var win = new ViewClass(config,{
			callback: function(){
				stuGridStore.reload();
			}
		});
		win.show();	
    },

	//批量删除预约考生
	delInterviewAppoStubat: function(btn){
		var stuListGrid = btn.findParentByType("grid");
		//选中行
        var selList = stuListGrid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var stuListGridStore =stuListGrid.store;
                    stuListGridStore.remove(selList);
                }
            },this);
        }
	},
	
	//删除预约考生
	delInterviewAppoStuRow: function(grid, rowIndex, colIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){
				grid.store.removeAt(rowIndex);
			}
		},this);
	},
	
	onFormSave: function(btn){
		var panel = btn.findParentByType("interviewStuViewList");
		var stuGrid = panel.down("grid[name=interviewAppoStuGrid]");
		//删除数据
		var stuGridRemovedRecs= stuGrid.store.getRemovedRecords();
		//修改数据
		var stuGridModifiedRecs =  stuGrid.store.getModifiedRecords();
		
		var removeRec = [];
		for(var i=0; i<stuGridRemovedRecs.length; i++){
			removeRec.push(stuGridRemovedRecs[i].data);
		}
		
		var modifyRec = [];
		for(var i=0; i<stuGridModifiedRecs.length; i++){
			modifyRec.push(stuGridModifiedRecs[i].data);
		}
		
		if(removeRec.length>0 || modifyRec.length>0){
			var tzParamsObj = {
        		ComID: 'TZ_MS_ARR_MG_COM',
        		PageID: 'TZ_MSKS_VIEW_STD',
        		OperateType: 'U',
        		comParams:{
        			delete: removeRec,
        			update: modifyRec
        		}
        	}
        	var tzParams = Ext.JSON.encode(tzParamsObj);
			Ext.tzSubmit(tzParams,function(respData){
				
			},"保存成功",true,this);
		}
	},
	
	onFormEnsure: function(btn){
		this.onFormSave(btn);
		this.onFormClose(btn);
	},
	
	onFormClose: function(btn){
		var panel = btn.findParentByType("interviewStuViewList");
		if(panel){
			panel.close();
		}
	},
	
	
	onWindowEnsure: function(btn){
		var win = btn.up("interviewStuViewAddWin");
		var form = win.child('form').getForm();
		var formRec = form.getValues();
		var stuGrid = win.down("grid");
		
		if(form.isValid()){
			var classID = win.classID;
			var batchID = win.batchID;
			var timePlan = formRec["timePlan"];
			
			var selList = stuGrid.getSelectionModel().getSelection();
			var checkLen = selList.length;
			if(checkLen == 0){
	            Ext.Msg.alert("提示","请选择要添加面试预约的学生");
	            return;
	        }else{
	        	var stuData = [];
	        	for(var i=0; i<checkLen; i++){
	        		stuData.push(selList[i].data);
	        	}

	        	var tzParamsObj = {
	        		ComID: 'TZ_MS_ARR_MG_COM',
	        		PageID: 'TZ_MSYY_ADD_STD',
	        		OperateType: 'addInterviewAppoStu',
	        		comParams:{
	        			classID: classID,
	        			batchID: batchID,
	        			timePlan: timePlan,
	        			selStuList: stuData
	        		}
	        	}
	        	var tzParams = Ext.JSON.encode(tzParamsObj);
				Ext.tzLoad(tzParams,function(respData){
					
					win.option.callback();
					win.close();
				});
	        }
		}
	},
	
	onWindowClose: function(btn){
		var win = btn.findParentByType("interviewStuViewAddWin");
		win.close();
	},
	
	
	//导出Excel
	exportToExcel:function(btn){
		var msArrGrid = btn.findParentByType("grid");
		var selList = msArrGrid.getSelectionModel().getSelection();
		if(selList.length<1) {
			Ext.MessageBox.alert("提示", "您没有选中任何记录");
			return;
		};

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_MS_ARR_EXP_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert("提示","您没有权限");
			return;
		}

		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert("提示","未找到该功能页面对应的JS类，请检查配置。");
			return;
		}
		
		if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
		ViewClass = Ext.ClassManager.get(className);

		var msArrFormRec = msArrGrid.up('form').getForm().getFieldValues();
		var msArrFormclassID = msArrFormRec["classID"];
		var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_CAL_ARR_STD","OperateType":"getModalId","comParams":{"classID":"'+msArrFormclassID+'"}}';

		Ext.tzLoadAsync(tzParams,function(responseData){
			win = new ViewClass(responseData.modalId);

			win.selList=selList;
			var form = win.child("form").getForm();
			form.reset();
			win.show();
		});
	}
})