Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.interviewArrangeController',
	//弹出自动安排面试时间页面
	SetInterviewTime: function(btn) {
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_MS_SJ_SET_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_SJ_SET_STD，请检查配置。');
			return;
		}

		var msArrForm = btn.up('grid').up('form');
		var msArrFormRec = msArrForm.getForm().getFieldValues();
		var clearAllTimeArr = msArrFormRec["clearAllTimeArr"];
		if("ALL"==clearAllTimeArr){
			Ext.MessageBox.alert('提示', '页面有尚未保存的数据，请先保存.');
			return;
		}

		var win = this.lookupReference('interviewArrangeTimeSet');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
		var comSiteParams = this.getView().child("form").getForm().getValues();
		var classID = comSiteParams["classID"];
		var batchID = comSiteParams["batchID"];
		var batchStartDate = comSiteParams["batchStartDate"];
		var batchEndDate = comSiteParams["batchEndDate"];
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({classID:classID,batchID:batchID,startDate:batchStartDate,endDate:batchEndDate});
        win.show();
    },
	//建议时间内自动安排考生
	msJYSJAutoArrStus:function(btn){
		//alert('--');
		var msArrGrid = btn.up('grid');
		var msArrGridAllRecs = msArrGrid.store.getRange();
		var msArrGridModifiedRecs =  msArrGrid.store.getModifiedRecords();
		var msArrGridRemovedRecs= msArrGrid.store.getRemovedRecords();
		var msArrForm = msArrGrid.up('form');
		var msArrFormRec = msArrForm.getForm().getFieldValues();
		var msArrFormclassID = msArrFormRec["classID"];
		var msArrFormbatchId = msArrFormRec["batchID"];

		//alert(msArrGridAllRecs.length+"__"+msArrGridModifiedRecs.length+"__"+msArrGridRemovedRecs.length+"__"+msArrFormclassID+"__"+msArrFormbatchId);
		if(msArrGridModifiedRecs.length>0 ||msArrGridRemovedRecs.length>0  ){
			Ext.Msg.alert("提示","页面有尚未保存的数据，请先保存.");
			return;
		};
		if(msArrGridAllRecs.length==0){
			Ext.Msg.alert("提示","尚无面试安排计划，请先生成面试安排计划.");
			return;
		};
		var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_CAL_ARR_STD","OperateType":"queryCurBatIfArr","comParams":{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'"}}';
		Ext.tzLoad(tzParams,function(responseData){
			if(responseData['arrFlag']=='YES'){
				Ext.MessageBox.confirm('确认', '当批次下已存在考生安排记录，是否清除？', function(btnId){
					if(btnId == 'yes'){
						tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_CAL_ARR_STD","OperateType":"autoArrStu","comParams":{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'","clearFlag":"YES"}}';
						Ext.tzLoad(tzParams,function(responseData){
							if (responseData['arrsta']=='2'){
								Ext.Msg.alert("提示",responseData['desc']);
								return; 
							}
							if (responseData['arrsta']=='0'){
								Ext.Msg.alert("提示",responseData['desc']);
							}
							var Params= '{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'"}';
							msArrGrid.store.tzStoreParams = Params;
							msArrGrid.store.reload();
						});
					}else{
						tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_CAL_ARR_STD","OperateType":"autoArrStu","comParams":{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'","clearFlag":"NO"}}';
						Ext.tzLoad(tzParams,function(responseData){
							if (responseData['arrsta']=='2'){
								Ext.Msg.alert("提示",responseData['desc']);
								return;
							}
							if (responseData['arrsta']=='0'){
								Ext.Msg.alert("提示",responseData['desc']);
							}
							var Params= '{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'"}';
							msArrGrid.store.tzStoreParams = Params;
							msArrGrid.store.reload();
						});
					}
				},this);
			}else if(responseData['arrFlag']=='NO'){
				tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_CAL_ARR_STD","OperateType":"autoArrStu","comParams":{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'","clearFlag":"YES"}}';
				Ext.tzLoad(tzParams,function(responseData){
					if (responseData['arrsta']=='2'){
						Ext.Msg.alert("提示",responseData['desc']);
						return;
					}
					if (responseData['arrsta']=='0'){
						Ext.Msg.alert("提示",responseData['desc']);
					}
					var Params= '{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'"}';
					msArrGrid.store.tzStoreParams = Params;
					msArrGrid.store.reload();
				});
			}
		});
	},
	//弹出面试建议时间定义页面
	ms_jytime: function(btn) {
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_MS_JYSJ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_JYSJ_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('addClass');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
			var form = win.child('form').getForm();
			win.on('afterrender',function(panel){
				var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_JYSJ_STD","OperateType":"QF","comParams":{}}';
				Ext.tzLoad(tzParams,function(responseData){
					var formData = responseData.msjysj;
					form.setValues(formData);
				});
			});
            this.getView().add(win);
        }
        win.show();
    },
	//批量清除考生安排
	ms_cleanAp: function(){
	   //选中行
	   var mssj_grid=this.getView().down('grid[name=msjh_grid]');
	   var selList=mssj_grid.getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要清除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要清除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				    //var userStore = mssj_grid.store;
				    //userStore.remove(selList);
					var selRecord;
					for(var i=0;i<selList.length;i++){
						selRecord=selList[i];
						selRecord.set("localStartTime","");
						selRecord.set("localFinishTime","");
						selRecord.set("skypeId","");
						selRecord.set("msClearOprId",selRecord.data.msOprId);
						selRecord.set("msOprId","");
						selRecord.set("msOprName","");
						selRecord.set("msOrderState","");
						selRecord.set("msConfirmState","");
						selRecord.set("sort","");
						selRecord.set("releaseOrUndo","");
						selRecord.set('moreInfo',"{}");
					}
				}												  
			},this);   
	   }
	},
	//批量清除时间安排
	ms_cleanTimeArr:function(btn){
		var msArrGrid = btn.up('grid');
		var msArrGridSelectedRecs=msArrGrid.getSelectionModel().getSelection();
		//选中行长度
		var checkLen = msArrGridSelectedRecs.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要清除的记录");
			return;
		}else{
			Ext.MessageBox.confirm('确认', '您确定要清除选中时间安排吗?', function(btnId){
				if(btnId == 'yes'){
					msArrGrid.store.remove(msArrGridSelectedRecs);
				}
			},this);
		}
	},
	//清除所有时间安排
	ms_cleanAllTimeArr:function(btn){
		var msArrGrid = btn.up('grid');
		var msArrForm = this.getView().child("form").getForm();
		var msArrFormRec =	msArrForm.getFieldValues();
		Ext.MessageBox.confirm('确认', '您确定要清除所有时间安排吗?', function(btnId){
			if(btnId == 'yes'){
				var classID = msArrFormRec["classID"];
				var batchID = msArrFormRec["batchID"];
				msArrForm.setValues({classID:classID,batchID:batchID,clearAllTimeArr:"ALL"});
				var msArrGridSelectedRecs=msArrGrid.store.getRange();
				msArrGrid.store.removeAll();
			}
		},this);

	},
	//设置参与本批次面试考生
	setInterviewApplicant:function(btn){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_MS_ARR_SSTU_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_ARR_SSTU_STD，请检查配置。');
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

		var itwArrInfGrid = btn.up('grid');
		var itwArrInfForm = itwArrInfGrid.up('form');
		var itwArrInfRec = itwArrInfForm.getForm().getFieldValues();
		var classID = itwArrInfRec["classID"];
		var batchID = itwArrInfRec["batchID"];
		var className = itwArrInfRec["className"];
		var batchName = itwArrInfRec["batchName"];


		cmp.on('afterrender',function(panel){
			//听众store
			var audStore = new KitchenSink.view.interviewManagement.interviewArrange.interviewAudienceStore();
			audStore.tzStoreParams = '{"TYPE":"AUD","classID":"'+classID+'","batchID":"'+batchID+'"}';
			audStore.load();
			
			var audForm = panel.down('grid').down('form[reference=audienceForm]');
			audForm.child('tagfield[name="audTag"]').setStore(audStore);
			
			var setStuListForm = panel.child('form').getForm();
			var setStuListGrid = panel.child('grid');

			var setStuListFormRec = {"itwArrInfFormData":{
				"classID":classID,
				"className":className,
				"batchID":batchID,
				"batchName":batchName,
				"stuCount":0
			}};
			setStuListForm.setValues(setStuListFormRec.itwArrInfFormData);
			
			
			//听众
			var audParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_CAL_ARR_STD","OperateType":"queryAudIDsArr","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
			Ext.tzLoad(audParams,function(respData){
				if(respData.result == "success"){
					var audform = panel.down("form[reference=audienceForm]");
	                audform.down('tagfield[name="audTag"]').setValue(respData.audIDs);
				}
			});
			

			Params= '{"TYPE":"STULIST","classID":"'+classID+'","batchID":"'+batchID+'"}';
			setStuListGrid.store.tzStoreParams = Params;
			setStuListGrid.store.load({
				callback : function(records, operation, success) {
					if (success == success) {
						var setStuListGridStoreCount = setStuListGrid.store.getRange().length;
						setStuListFormRec = {"itwArrInfFormData":{
							"classID":classID,
							"className":className,
							"batchID":batchID,
							"batchName":batchName,
							"stuCount":setStuListGridStoreCount
						}};
						setStuListForm.setValues(setStuListFormRec.itwArrInfFormData);
					}
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
	//选择考生
	selInterviwStu:function(grid, rowIndex, colIndex){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_MS_ARR_CSTU_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_ARR_CSTU_STD，请检查配置。');
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

		var msArrGridRowRecord = grid.store.getAt(rowIndex);
		var msOrderState = msArrGridRowRecord.data.msOrderState;
		var classID = msArrGridRowRecord.data.classID;
		var batchID = msArrGridRowRecord.data.batchID;
		var msOprId = msArrGridRowRecord.data.msOprId;

		var msDate = Ext.util.Format.date(msArrGridRowRecord.data.msDate,'Y-m-d');
		var bjMsStartTime = Ext.util.Format.date(msArrGridRowRecord.data.bjMsStartTime, 'H:i');
		var bjMsEndTime = Ext.util.Format.date(msArrGridRowRecord.data.bjMsEndTime, 'H:i');
		var msArrInfoGridRowIndex = rowIndex;

		//面试日期不能为空
		if(msDate==""||msDate==null){
			Ext.MessageBox.alert('提示', '面试日期不能为空！'	);
			return;
		}
		//开始时间不能为空
		if(bjMsStartTime==""||bjMsStartTime==null){
			Ext.MessageBox.alert('提示', '开始时间不能为空！');
			return;
		}
		//结束时间不能为空
		if(bjMsEndTime==""||bjMsEndTime==null){
			Ext.MessageBox.alert('提示', '结束时间不能为空！');
			return;
		}

		if (msOrderState=="B"){
			var infoText="当前时间段已安排考生，若重新安排将清除当前考生的面试安排信息，确认安排其他考生吗？";
			Ext.MessageBox.confirm('确认',infoText , function(btnId) {
				if (btnId == 'yes') {
					msArrGridRowRecord.set("localStartTime","");
					msArrGridRowRecord.set("localFinishTime","");
					msArrGridRowRecord.set("skypeId","");
					msArrGridRowRecord.set("msClearOprId","");
					msArrGridRowRecord.set("msOprId","");
					msArrGridRowRecord.set("msOprName","");
					msArrGridRowRecord.set("msOrderState","");
					msArrGridRowRecord.set("msConfirmState","");
					msArrGridRowRecord.set("sort","");
					msArrGridRowRecord.set('moreInfo',"{}");

					var tzParams1 = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_ARR_CSTU_STD","OperateType":"delCurStuMsArrInfo","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'","oprid":"'+msOprId+'"}}';
					Ext.tzLoad(tzParams1,function(responseData) {
						if (responseData.success == 'success') {
							cmp.on('afterrender',function(panel){
								var selStuForm = panel.child('form').getForm();
								var selStuGrid = panel.child('grid');

								var selStuFormRec = {"msArrFormData":{
									"msArrGridRowIndex":rowIndex
								}};
								selStuForm.setValues(selStuFormRec.msArrFormData);

								Params= '{"classID":"'+classID+'","batchID":"'+batchID+'","msDate":"'+msDate+'","bjMsStartTime":"'+bjMsStartTime+'","bjMsEndTime":"'+bjMsEndTime+'","msArrInfoGridRowIndex":"'+rowIndex+'"}';
								selStuGrid.store.tzStoreParams = Params;
								selStuGrid.store.load();
							});

							tab = contentPanel.add(cmp);

							contentPanel.setActiveTab(tab);

							Ext.resumeLayouts(true);

							if (cmp.floating) {
								cmp.show();
							}
						}else{
							Ext.MessageBox.alert('提示', '清除考生信息失败，请重试.');
						}
					});
				}
			});
		}else{
			cmp.on('afterrender',function(panel){
				var selStuForm = panel.child('form').getForm();
				var selStuGrid = panel.child('grid');

				var selStuFormRec = {"msArrFormData":{
					"msArrGridRowIndex":rowIndex
				}};
				selStuForm.setValues(selStuFormRec.msArrFormData);

				Params= '{"classID":"'+classID+'","batchID":"'+batchID+'","msDate":"'+msDate+'","bjMsStartTime":"'+bjMsStartTime+'","bjMsEndTime":"'+bjMsEndTime+'","msArrInfoGridRowIndex":"'+rowIndex+'"}';
				selStuGrid.store.tzStoreParams = Params;
				selStuGrid.store.load();
			});

			tab = contentPanel.add(cmp);

			contentPanel.setActiveTab(tab);

			Ext.resumeLayouts(true);

			if (cmp.floating) {
				cmp.show();
			}
		}
	},
	//增加
	addMsCalRow:function(grid, rowIndex, colIndex){
		var clsformrec = grid.up('form').getForm().getFieldValues();
		var classID = clsformrec["classID"];
		var batchID = clsformrec["batchID"];

		//var tagCellEditing = grid.getPlugin('tagCellEditing');
		//var tagStore =  grid.getStore();
		var rowCount = rowIndex+1;
		//console.log(applyItemGrid);
		//tagCellEditing.cancelEdit();
		// Create a model instance
		var r = Ext.create('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeModel', {
			classID:classID,
			batchID:batchID,
			msJxNo:"NEXT",
			msDate:"",
			bjMsStartTime:"",
			bjMsEndTime:"",
			msGroupId:"",
			msGroupSn:"NEXT",
			msOprId:"",
			msClearOprId:"",
			msXxBz:""
		});

		grid.store.insert(rowCount,r);
		//alert(grid.store.getAt(rowCount).data.classID);
		//tagCellEditing.startEdit(r, 1);
	},
	//删除
	deleteMsCalRow:function(grid, rowIndex, colIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){
				grid.store.removeAt(rowIndex);
			}
		},this);
	},
	//skype
	transferSkype:function(grid, rowIndex, colIndex){
		var msArrGridRowRecord = grid.store.getAt(rowIndex);
		var skypeId = msArrGridRowRecord.data.skypeId;
		if (skypeId=="" || skypeId==null){
			Ext.MessageBox.alert('提示', 'Skype账号为空.');
			return;
		};
		var url="skype:"+skypeId+"?call";
		window.location.href=url;

	},
	//发送面试确认邮件
	sendInterviewArrConfirmEmail:function(btn){
		//面试信息列表
		var stuListGrid =btn.up("grid");

		var clsformrec = stuListGrid.up('form').getForm().getFieldValues();
		var classID = clsformrec["classID"];
		//面试信息选中行数据
		var stuListRecs = stuListGrid.getSelectionModel().getSelection();

		//提交参数
		var comParams="";

		//编辑JSON
		var editJson="";

		if(stuListRecs.length==0){
			Ext.MessageBox.alert('提示', '请选择需要发送邮件的记录！');
			return;
		}else{
			for(var i=0;i<stuListRecs.length;i++){
				if(stuListRecs[i].data.msOrderState != "B" ){
					Ext.MessageBox.alert('提示', '请确保所选记录的【预约状态】均为"已安排"！');
					return;
				}

				if(editJson == ""){
					editJson = stuListRecs[i].data.msOprId;
				}else{
					editJson = editJson + ','+stuListRecs[i].data.msOprId;
				}
			}
		}

		var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_CAL_ARR_STD","OperateType":"getEmailInfo","comParams":{"oprids":"'+editJson+'","classID":"'+classID+'"}}';

		Ext.tzLoad(tzParams,function(responseData){
			var emailTmpName = responseData['EmailTmpName'];
			var arrEMLTmpls = new Array();
			arrEMLTmpls=emailTmpName.split(",");

			var audienceId = responseData['audienceId'];

			Ext.tzSendEmail({
				//发送的邮件模板;
				"EmailTmpName":arrEMLTmpls,
				//创建的需要发送的听众ID;
				"audienceId": audienceId,
				//是否可以发送附件: Y 表示可以发送附件,"N"表示无附件;
				"file": "N"
			});
		});
	},
	//发布、撤销
	releaseOrUndo:function(grid, rowIndex, colIndex){
		var editRec = grid.store.getAt(rowIndex);
		
		var editJson="";
		var infoText="";
		if(editRec.data.releaseOrUndo=="Y"){
			infoText="撤销发布成功."
			editRec.data.releaseOrUndo="N";
		}else{
			infoText="发布成功.";
			editRec.data.releaseOrUndo="Y";
		}
		editJson = Ext.JSON.encode(editRec.data);
		var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_CAL_ARR_STD","OperateType":"publish","comParams":'+editJson+'}';
		var msArrFormRec = grid.up('form').getForm().getFieldValues();
		var msArrFormclassID = msArrFormRec["classID"];
		var msArrFormbatchId = msArrFormRec["batchID"];
		Ext.tzSubmit(tzParams,function(responseData){
			if(responseData.success=='success'){
				Params= '{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'"}';
				grid.store.tzStoreParams = Params;
				grid.store.reload();
			};
		},infoText,true,this);
	},
	//批量发布
	releaseSelList:function(btn){
		var msArrGrid = btn.up('grid');
		var msArrGridSelectedRecs=msArrGrid.getSelectionModel().getSelection();
		//选中行长度
		var checkLen = msArrGridSelectedRecs.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要发布的记录");
			return;
		}else{
			Ext.MessageBox.confirm('确认', '您确定要发布选中记录吗?', function(btnId){
				if(btnId == 'yes'){
					var editJson="";
					for(var i=0;i<msArrGridSelectedRecs.length;i++){
						msArrGridSelectedRecs[i].data.releaseOrUndo="Y";
						if(editJson == ""){
							editJson = Ext.JSON.encode(msArrGridSelectedRecs[i].data);
						}else{
							editJson = editJson + ','+Ext.JSON.encode(msArrGridSelectedRecs[i].data);
						}
					}
					var comParams="";
					if(editJson != ""){
						comParams = '"pubrecs":[' + editJson + "]";
					}else{
						comParams = '"pubrecs":[]';
					}
					var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_CAL_ARR_STD","OperateType":"publishpl","comParams":{'+comParams+'}}';
					var msArrFormRec = msArrGrid.up('form').getForm().getFieldValues();
					var msArrFormclassID = msArrFormRec["classID"];
					var msArrFormbatchId = msArrFormRec["batchID"];
					Ext.tzSubmit(tzParams,function(responseData){
						if(responseData.success=='success'){
							Params= '{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'"}';
							msArrGrid.store.tzStoreParams = Params;
							msArrGrid.store.reload();
						};
					},"发布成功.",true,this);
				}
			},this);
		}
	},
	//批量撤销
	UndoSelList:function(btn){
		var msArrGrid = btn.up('grid');
		var msArrGridSelectedRecs=msArrGrid.getSelectionModel().getSelection();
		//选中行长度
		var checkLen = msArrGridSelectedRecs.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要撤销的记录");
			return;
		}else{
			Ext.MessageBox.confirm('确认', '您确定要撤销选中记录吗?', function(btnId){
				if(btnId == 'yes'){
					var editJson="";
					for(var i=0;i<msArrGridSelectedRecs.length;i++){
						msArrGridSelectedRecs[i].data.releaseOrUndo="N";
						if(editJson == ""){
							editJson = Ext.JSON.encode(msArrGridSelectedRecs[i].data);
						}else{
							editJson = editJson + ','+Ext.JSON.encode(msArrGridSelectedRecs[i].data);
						}
					}
					var comParams="";
					if(editJson != ""){
						comParams = '"revrecs":[' + editJson + "]";
					}else{
						comParams = '"revrecs":[]';
					}
					var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_CAL_ARR_STD","OperateType":"revokepl","comParams":{'+comParams+'}}';
					var msArrFormRec = msArrGrid.up('form').getForm().getFieldValues();
					var msArrFormclassID = msArrFormRec["classID"];
					var msArrFormbatchId = msArrFormRec["batchID"];
					Ext.tzSubmit(tzParams,function(responseData){
						if(responseData.success=='success'){
							Params= '{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'"}';
							msArrGrid.store.tzStoreParams = Params;
							msArrGrid.store.reload();
						};
					},"撤销发布成功.",true,this);
				}
			},this);
		}
	},

	onFormSave:function(btn){
		var msArrPanel = btn.up('panel');
		var msArrForm = msArrPanel.child('form');
		var msArrFormRec = msArrForm.getForm().getValues();

		var msArrFormclassID = msArrFormRec["classID"];
		var msArrFormbatchId = msArrFormRec["batchID"];
		var clearAllTimeArr = msArrFormRec["clearAllTimeArr"];
		if(msArrFormRec["frontView"] == undefined){
			msArrFormRec["frontView"] = "N";
		}
		
		var msArrGrid = msArrForm.child('grid');
		var msArrGridAllRecs = msArrGrid.store.getRange();
		var msArrGridModifiedRecs,msArrGridRemovedRecs;
		msArrGridModifiedRecs =  msArrGrid.store.getModifiedRecords();
		msArrGridRemovedRecs= msArrGrid.store.getRemovedRecords();

		var msDate,bjMsStartTime,bjMsEndTime,msGroupId,msGroupSn;
		var msArrCellEditingPlugin = msArrGrid.getPlugin('msArrCellEditingPlugin');
		var i,j;
		var compbjMsStartTime,compbjMsEndTime;
		
		//检查
		for ( i = 0; i < msArrGridAllRecs.length; i++) {
			msDate = msArrGridAllRecs[i].get("msDate");
			bjMsStartTime = msArrGridAllRecs[i].get("bjMsStartTime");
			bjMsEndTime = msArrGridAllRecs[i].get("bjMsEndTime");
			maxPerson = msArrGridAllRecs[i].get("maxPerson");
			
			compbjMsStartTime=new Date("January 01, 1900 "+Ext.util.Format.date(bjMsStartTime, 'H:i')+":00");
			compbjMsEndTime=new Date("January 01, 1900 "+Ext.util.Format.date(bjMsEndTime, 'H:i')+":00");
			//面试日期不能为空
			if(msDate==""||msDate==null){
				Ext.MessageBox.alert('提示', '面试日期不能为空！',
					function(e){
						if(e == "ok"|| e == "OK" || e == "确定"){
							for ( j = 0; j < msArrGrid.columns.length; j++) {
								if ("msDate"==msArrGrid.columns[j].dataIndex){
									msArrCellEditingPlugin.startEdit(msArrGridAllRecs[i], msArrGrid.columns[j]);
								}

							}
						}
					}
				);
				return;
			}
			//最多预约人数不能为空
			if(maxPerson==""||maxPerson==null||maxPerson==0){
				Ext.MessageBox.alert('提示', '最多预约人数必须大于0！',
					function(e){
						if(e == "ok"|| e == "OK" || e == "确定"){
							for ( j = 0; j < msArrGrid.columns.length; j++) {
								if ("maxPerson"==msArrGrid.columns[j].dataIndex){
									msArrCellEditingPlugin.startEdit(msArrGridAllRecs[i], msArrGrid.columns[j]);
								}
							}
						}
					}
				);
				return;
			}
			//开始时间不能为空
			if(bjMsStartTime==""||bjMsStartTime==null){
				Ext.MessageBox.alert('提示', '面试开始时间不能为空！',
					function(e){
						if(e == "ok"|| e == "OK" || e == "确定"){
							for ( j = 0; j < msArrGrid.columns.length; j++) {
								if ("bjMsStartTime"==msArrGrid.columns[j].dataIndex){
									msArrCellEditingPlugin.startEdit(msArrGridAllRecs[i], msArrGrid.columns[j]);
								}

							}
						}
					}
				);
				return;
			}
			//结束时间不能为空
			if(bjMsEndTime==""||bjMsEndTime==null){
				Ext.MessageBox.alert('提示', '面试结束时间不能为空！',
					function(e){
						if(e == "ok"|| e == "OK" || e == "确定"){
							for ( j = 0; j < msArrGrid.columns.length; j++) {
								if ("bjMsEndTime"==msArrGrid.columns[j].dataIndex){
									msArrCellEditingPlugin.startEdit(msArrGridAllRecs[i], msArrGrid.columns[j]);
								}

							}
						}
					}
				);
				return;
			}
			//开始时间不能大于结束时间
			if(compbjMsStartTime>compbjMsEndTime){
				Ext.MessageBox.alert('提示', '面试结束时间不能小于开始时间！',
					function(e){
						if(e == "ok"|| e == "OK" || e == "确定"){
							for ( j = 0; j < msArrGrid.columns.length; j++) {
								if ("bjMsEndTime"==msArrGrid.columns[j].dataIndex){
									msArrCellEditingPlugin.startEdit(msArrGridAllRecs[i], msArrGrid.columns[j]);
								}

							}
						}
					}
				);
				return;
			}
		}
		
		var comParams="";
		//修改记录
		var msDateTemp,bjMsStartTimeTemp,bjMsEndTimeTemp;
		var editJson="";
		for(var i=0;i<msArrGridModifiedRecs.length;i++){
			msDateTemp=msArrGridModifiedRecs[i].data.msDate;
			bjMsStartTimeTemp=msArrGridModifiedRecs[i].data.bjMsStartTime;
			bjMsEndTimeTemp=msArrGridModifiedRecs[i].data.bjMsEndTime;
			msArrGridModifiedRecs[i].data.msDate=Ext.util.Format.date(msArrGridModifiedRecs[i].data.msDate, 'Y-m-d');
			msArrGridModifiedRecs[i].data.bjMsStartTime=Ext.util.Format.date(msArrGridModifiedRecs[i].data.bjMsStartTime, 'H:i');
			msArrGridModifiedRecs[i].data.bjMsEndTime=Ext.util.Format.date(msArrGridModifiedRecs[i].data.bjMsEndTime, 'H:i');
			
			if(editJson == ""){
				editJson = Ext.JSON.encode(msArrGridModifiedRecs[i].data);
			}else{
				editJson = editJson + ','+Ext.JSON.encode(msArrGridModifiedRecs[i].data);
			}
			
			msArrGridModifiedRecs[i].data.msDate=msDateTemp;
			msArrGridModifiedRecs[i].data.bjMsStartTime=bjMsStartTimeTemp;
			msArrGridModifiedRecs[i].data.bjMsEndTime=bjMsEndTimeTemp;
		}
		if(editJson != ""){
			comParams = '"updaterecs":[' + editJson + "]";
		}else{
			comParams = '"updaterecs":[]';
		}

		//删除记录
		var removeJson="";
		for(var i=0;i<msArrGridRemovedRecs.length;i++){
			msDateTemp=msArrGridRemovedRecs[i].data.msDate;
			bjMsStartTimeTemp=msArrGridRemovedRecs[i].data.bjMsStartTime;
			bjMsEndTimeTemp=msArrGridRemovedRecs[i].data.bjMsEndTime;
			msArrGridRemovedRecs[i].data.msDate=Ext.util.Format.date(msArrGridRemovedRecs[i].data.msDate, 'Y-m-d');
			msArrGridRemovedRecs[i].data.bjMsStartTime=Ext.util.Format.date(msArrGridRemovedRecs[i].data.bjMsStartTime, 'H:i');
			msArrGridRemovedRecs[i].data.bjMsEndTime=Ext.util.Format.date(msArrGridRemovedRecs[i].data.bjMsEndTime, 'H:i');
			
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(msArrGridRemovedRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(msArrGridRemovedRecs[i].data);
			}
			msArrGridRemovedRecs[i].data.msDate=msDateTemp;
			msArrGridRemovedRecs[i].data.bjMsStartTime=bjMsStartTimeTemp;
			msArrGridRemovedRecs[i].data.bjMsEndTime=bjMsEndTimeTemp;
		}

		if(removeJson != ""){
			comParams =comParams+ ',"removerecs":[' + removeJson + "]";
		}else{
			comParams =comParams+ ',"removerecs":[]';
		}

		var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_CAL_ARR_STD","OperateType":"saveMsArrInfo","comParams":{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'","clearAllTimeArr":"'+clearAllTimeArr+'","formData":'+ Ext.JSON.encode(msArrFormRec) +','+comParams+'}}';
		
		console.log(tzParams);
		
		var formDataSet={classID:msArrFormclassID,batchID:msArrFormbatchId,clearAllTimeArr:""}
		Ext.tzSubmit(tzParams,function(responseData){
			console.log(responseData)
			if(responseData.success=='success'){
				msArrForm.getForm().setValues(formDataSet);
				Params= '{"classID":"'+msArrFormclassID+'","batchID":"'+msArrFormbatchId+'"}';
				msArrGrid.store.tzStoreParams = Params;
				msArrGrid.store.reload();
			};
		},"",true,this);
	},
	onFormEnsure: function(btn){
		this.onFormSave(btn);
		this.onFormClose();
	},
	onFormClose: function(){
		this.getView().close();
	},

	//考生安排情况一览表  LYY  2015-08-10
	ms_msArrPreview:function(btn){
		//alert("ms_msArrPreview");
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_MS_ARR_PRE_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_ARR_PRE_STD，请检查配置。');
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

		var panelform = btn.up('grid').up('panel').getForm();

		var panelformrec = panelform.getFieldValues();
		var classID = panelformrec["classID"];
		//alert(classID);

		cmp = new ViewClass();
		cmp.on('afterrender',function(panel){
			var prePanelGrid = panel.child('grid');
			var tzParams = '{"classID":"'+classID+'"}';
			prePanelGrid.store.tzStoreParams = tzParams;
			prePanelGrid.store.load();
		});

		tab = contentPanel.add(cmp);

		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	
	//查看预约考生
	viewArrangeStuList: function(btn){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_MSKS_VIEW_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSKS_VIEW_STD，请检查配置。');
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

		cmp = new ViewClass();

		var itwArrInfGrid = btn.up('grid');
		var itwArrInfForm = itwArrInfGrid.up('form');
		var itwArrInfRec = itwArrInfForm.getForm().getFieldValues();
		var classID = itwArrInfRec["classID"];
		var batchID = itwArrInfRec["batchID"];
		var className = itwArrInfRec["className"];
		var batchName = itwArrInfRec["batchName"];


		cmp.on('afterrender',function(panel){
			var setStuListForm = panel.child('form').getForm();
			var setStuListGrid = panel.down('grid');

			var setStuListFormRec = {
				"classID":classID,
				"className":className,
				"batchID":batchID,
				"batchName":batchName
			};
			setStuListForm.setValues(setStuListFormRec);

			Params= '{"classID":"'+classID+'","batchID":"'+batchID+'"}';
			
			setStuListGrid.store.tzStoreParams = Params;
			setStuListGrid.store.load({
				callback : function(records, operation, success) {
					
				}
			});
			
		});

		tab = contentPanel.add(cmp);

		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	}
});