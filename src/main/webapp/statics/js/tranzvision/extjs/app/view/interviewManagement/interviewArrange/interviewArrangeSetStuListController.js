Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.interviewArrangeSetStuListController',
    //清空过滤条件
    onClearFilters:function(btn){
        btn.findParentByType('grid').filters.clearFilters();

    },
    
    //添加听众
    addAudience:function(btn){
    	Ext.tzShowPromptSearch({
            recname: 'TZ_AUD_DEFN_T',
            searchDesc: '选择听众',
            maxRow:50,
            condition:{
                presetFields:{
                	TZ_JG_ID:{
                		value: Ext.tzOrgID,
                        type: '01'
                    },
                    TZ_AUD_STAT:{
                    	value: '1',//有效
                        type: '01'
                    }
                    /*
                    TZ_LXFS_LY:{
                        value: 'ZSBM',//来源类型：招生报名
                        type: '01'
                    }
                    */
                },
                srhConFields:{
                	TZ_AUD_NAM:{
                        desc:'听众名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_AUD_ID:'听众ID',
                TZ_AUD_NAM: '听众名称',
                ROW_ADDED_DTTM:'创建时间'
            },
            multiselect: true,
            callback: function(selection){
            	var arrAddAudience = [];
            	var arrAddAudiValue = [];
                if (selection.length>0){
                    for(j=0;j<selection.length;j++){
                        addAudirec="";
                        addAudirec = {"id":selection[j].data.TZ_AUD_ID,"desc":selection[j].data.TZ_AUD_NAME};
                        arrAddAudience.push(addAudirec);
                        arrAddAudiValue.push(selection[j].data.TZ_AUD_ID);
                    };
                    
                    
                    var setStuListPanel = btn.up('interviewArrangeSetStuList');
                    var setStuListForm = setStuListPanel.down('form[reference=interviewArrangeSetStuListForm]');
                    var setStuListGrid = setStuListPanel.down('grid');
                    var setStuListGridStore = setStuListGrid.getStore();
                 
                    var setStuListFormRec = setStuListForm.getForm().getFieldValues();
                    var classID = setStuListFormRec["classID"];
                    var batchID = setStuListFormRec["batchID"];
                    var className = setStuListFormRec["className"];
                    var batchName = setStuListFormRec["batchName"];
                    
                    var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_ARR_SSTU_STD","OperateType":"tzAddAudience","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'","audIDs":'+Ext.JSON.encode(arrAddAudiValue)+'}}';
                    Ext.tzSubmit(tzParams,function(){
                    	//设置听众
                    	var audform = btn.findParentByType('grid').down('form[reference=audienceForm]');
                        var audStore=audform.down('tagfield[name="audTag"]').getStore();
                        //audStore.add(arrAddAudience);
                        audStore.reload({
                        	callback: function(records, operation, success){
                        		audform.down('tagfield[name="audTag"]').addValue(arrAddAudiValue);
                        		 setStuListPanel.commitChanges(setStuListPanel);
                        	}
                        });
                    	
                        //加载开始列表
                        Params= '{"TYPE":"STULIST","classID":"'+classID+'","batchID":"'+batchID+'"}';
                        setStuListGridStore.tzStoreParams = Params;
                        setStuListGridStore.load({
                            callback : function(records, operation, success) {
                                if (success == success) {
                                    var setStuListGridStoreCount = setStuListGrid.store.getRange().length;
                                    var setStuListFormRec = {
                                        "classID":classID,
                                        "className":className,
                                        "batchID":batchID,
                                        "batchName":batchName,
                                        "stuCount":setStuListGridStoreCount
                                    };
                                    setStuListForm.getForm().setValues(setStuListFormRec);
                                    
                                    setStuListPanel.commitChanges(setStuListPanel);
                                }
                            }
                        });
                    },"添加听众成功",true,this);
                }
            }
        })
    },
    
    
    //添加
    addStudents:function(btn){
    	var setStuListPanel = btn.up('interviewArrangeSetStuList');
        var setStuListForm = setStuListPanel.down('form[reference=interviewArrangeSetStuListForm]');
        var setStuListGrid = setStuListPanel.down('grid');
        var setStuListGridStore = setStuListGrid.getStore();
     
        var setStuListFormRec = setStuListForm.getForm().getFieldValues();
        var classID = setStuListFormRec["classID"];
        var batchID = setStuListFormRec["batchID"];
        
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_MS_ARR_ASTU_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_ARR_ASTU_STD，请检查配置。');
			return;
		}
		
    	var win = this.lookupReference('addStudentWin');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass({
            	classID:classID,
            	batchID:batchID,
            	callback: function(stuInfoArr){
            		//添加考生
            		var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_ARR_ASTU_STD","OperateType":"tzAddStudents","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'","stuList":'+Ext.JSON.encode(stuInfoArr)+'}}';
                    Ext.tzSubmit(tzParams,function(respData){
                    	var result = respData.result;
                    	if(result != "success"){
                    		Ext.Msg.alert("提示",respData.message);
                    	}
                    	
                    	setStuListGridStore.tzStoreParams = '{"TYPE":"STULIST","classID":"'+classID+'","batchID":"'+batchID+'"}';
                		setStuListGridStore.reload();
                    },"保存成功",true,this);
            	}
            });
            //this.getView().add(win);
        }
        win.show();
    },
    
  //搜索考生
	searchStudents: function(btn){
		var win = btn.findParentByType("window");
		var grid = win.down("grid");
		var classID = win.classID;
		var batchID = win.batchID;
		
		var condition = {
			TZ_CLASS_ID: classID	
		};

		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_MS_ARR_MG_COM.TZ_MS_ARR_ASTU_STD.TZ_MSSZ_STU_VW',
            condition: condition,
            callback: function(seachCfg){
            	var seachCfgObj = Ext.JSON.decode(seachCfg);
            	
                var store = grid.store;
                store.tzStoreParams = Ext.JSON.encode(seachCfgObj);
                store.load();
            }
        });
	},
	
	/**
	 * 确认选择考生
	 */
	ensureAddStudent: function(btn){
		var win = btn.findParentByType("window");
        var grid = win.down("grid");
        var selList =  grid.getSelectionModel().getSelection();
        var checkLen = selList.length;
        var stuInfoArr = [];
        if(checkLen == 0){
			Ext.Msg.alert("提示","请选择考生");
            return;
        }else{
           for(var i=0;i<checkLen;i++){
               //把所有的人员信息存放在一个数组里面
        	   stuInfoArr.push(selList[i].data);
            }
        }
		if(win.callback!=undefined) win.callback(stuInfoArr);
        win.close();
	},
    
    delSelStus: function(btn){
        var stuListGrid = btn.up('grid');
        var stuListForm = stuListGrid.up('panel').child('form').getForm();
        var setStuListFormRec = stuListForm.getFieldValues();
        var classID = setStuListFormRec["classID"];
        var batchID = setStuListFormRec["batchID"];
        var className = setStuListFormRec["className"];
        var batchName = setStuListFormRec["batchName"];
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
                    var setStuListGridStoreCount = stuListGridStore.getCount();
                    var setStuListFormRec = {"itwArrInfFormData":{
                        "classID":classID,
                        "className":className,
                        "batchID":batchID,
                        "batchName":batchName,
                        "stuCount":setStuListGridStoreCount
                    }};
                    stuListForm.setValues(setStuListFormRec.itwArrInfFormData);
                }
            },this);
        }
    },
    //保存
    onPanelSave:function(btn){
        var setStuListPanel = btn.up('panel');
        var setStuListForm = setStuListPanel.child('form[reference=interviewArrangeSetStuListForm]');
        var setStuListGrid = setStuListPanel.child('grid');
        /*
        var audFormRec = setStuListGrid.down("form[reference=audienceForm]").getForm().getValues();
        var audIDs = audFormRec["audTag"];
         */
        var setStuListFormRec = setStuListForm.getForm().getFieldValues();
        var classID = setStuListFormRec["classID"];
        var batchID = setStuListFormRec["batchID"];
        var className = setStuListFormRec["className"];
        var batchName = setStuListFormRec["batchName"];

        var setStuListGridStore = setStuListGrid.getStore();
        //删除json字符串
        var removeJson = "";
        var comParams="";
        //删除记录
        var removeRecs = setStuListGridStore.getRemovedRecords();

        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }

        var audIDsJson = "[]";
        /*
        if(audIDs.length>0){
        	audIDsJson = Ext.JSON.encode(audIDs);
        }else{
        	audIDsJson = '[]';
        }
        */
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + '],"update":[{"classID":"'+classID+'","batchID":"'+batchID+'","audIDs":'+ audIDsJson+'}]';
        }else{
        	comParams = '"update":[{"classID":"'+classID+'","batchID":"'+batchID+'","audIDs":'+ audIDsJson+'}]';
        }
        
        //提交参数
        var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_ARR_SSTU_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            Params= '{"TYPE":"STULIST","classID":"'+classID+'","batchID":"'+batchID+'"}';
            setStuListGridStore.tzStoreParams = Params;
            setStuListGridStore.load({
                callback : function(records, operation, success) {
                    if (success == success) {
                        var setStuListGridStoreCount = setStuListGrid.store.getRange().length;
                        var setStuListFormRec = {"itwArrInfFormData":{
                            "classID":classID,
                            "className":className,
                            "batchID":batchID,
                            "batchName":batchName,
                            "stuCount":setStuListGridStoreCount
                        }};
                        setStuListForm.getForm().setValues(setStuListFormRec.itwArrInfFormData);
                    }
                }
            });
        },"",true,this);
    },
    //确定
    onPanelEnsure:function(btn){
        this.onPanelSave(btn);
        this.onPanelClose();
    },
    //关闭
    onPanelClose: function(){
        this.getView().close();
    },
	//给选中考生发送面试预约邮件短信
    tzSendEmailSmsToStu: function(btn){
    	var sendType = btn.sendType;
		var msStuGrid = btn.up('grid');
		var msStuGridSelRecs=msStuGrid.getSelectionModel().getSelection();
		//选中行长度
		var checkLen = msStuGridSelRecs.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要发送邮件的记录");
			return;
		}else{
			var stuJson = "";
			for(var i=0;i<msStuGridSelRecs.length;i++){
				if(stuJson == ""){
					stuJson = Ext.JSON.encode(msStuGridSelRecs[i].data);
				}else{
					stuJson = stuJson + ','+Ext.JSON.encode(msStuGridSelRecs[i].data);
				}
			}
			var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_ARR_SSTU_STD","OperateType":"tzSendEmailSmsToStu","comParams":{"sendType":"'+sendType+'","stuList":['+stuJson+']}}';
			
			Ext.tzLoad(tzParams,function(responseData){
				var tmpName = responseData['tmpName'];
				var arrTmpls = new Array();
				arrTmpls=tmpName.split(",");
	
				var audienceId = responseData['audienceId'];
	
				if(sendType == "SMS"){
					Ext.tzSendSms({
						//发送的短信模板;
						"SmsTmpName":arrTmpls,
						//创建的需要发送的听众ID;
						"audienceId": audienceId
					});
				}else{
					Ext.tzSendEmail({
						//发送的邮件模板;
						"EmailTmpName":arrTmpls,
						//创建的需要发送的听众ID;
						"audienceId": audienceId,
						//是否可以发送附件: Y 表示可以发送附件,"N"表示无附件;
						"file": "N"
					});
				}
			});	
		}
	}
});
