Ext.define('KitchenSink.view.automaticScreen.autoScreenController', {
	extend: 'Ext.app.ViewController',
	alias: 'controller.autoScreenController',
	
	//可配置搜索
	searchAutoScreenStu: function(btn){
		var panel = btn.findParentByType('autoScreen');
		var itemColumns = panel.itemColumns;
		var classId = panel.classId;
		var batchId = panel.batchId;
		
		var items = [];
		for(var i=0; i<itemColumns.length; i++){
			items.push(itemColumns[i].columnId);
		}
		
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.TZ_CS_STU_VW',
			condition:
			{
				"TZ_CLASS_ID": classId,
				"TZ_BATCH_ID": batchId
			},
			callback: function(seachCfg){
				var seachCfgJson = Ext.JSON.decode(seachCfg);
				seachCfgJson.items = items;
				
				seachCfg = Ext.JSON.encode(seachCfgJson);

				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});
	},

	//保存自动初筛grid
	onAutoScreenSave: function(btn){
		var closePanel = btn.closePanel;
		var panel = btn.findParentByType("autoScreen");
		var grid = panel.down('grid');
		var gridStore = grid.getStore();
		var gridModifyRec = gridStore.getModifiedRecords();
		var updateArr = [];
		
		//grid修改记录
		var modifyArr = [];
		for(var i=0; i<gridModifyRec.length; i++){
			var recObj = gridModifyRec[i].data;
			if(recObj.status){
				recObj.status = "N";
			}else{
				recObj.status = "Y";
			}
			modifyArr.push(recObj);
		}
		
		if(modifyArr.length > 0){
			updateArr.push({
				type: "GRIDSAVE",
				data: modifyArr
			});
			
			var comParamsObj = {
				ComID: 'TZ_AUTO_SCREEN_COM',
				PageID: 'TZ_AUTO_SCREEN_STD',
				OperateType: 'U',
				comParams:{
					update: updateArr
				}
			}
			var tzParams = Ext.JSON.encode(comParamsObj);
			console.log(tzParams);
			
			Ext.tzSubmit(tzParams,function(respData){
				gridStore.reload();
				
				if(closePanel == "Y"){
					panel.close();
				}
			},"保存成功",true,this);
		}else{
			if(closePanel == "Y"){
				panel.close();
			}
		}
	},
	
	//确认保存自动初筛grid
	onAutoScreenEnsure: function(btn){
		this.onAutoScreenSave(btn);
	},
	
	onAutoScreenClose: function(btn){
		var panel = btn.findParentByType("autoScreen");
		panel.close();
	},
	
	//批量设置初筛通过
	setScreenPass: function(btn){
		var grid = btn.findParentByType('grid');
		var selList = grid.getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要设置的记录");
			return;
		}
		
		this.setScreenPassStatusHandler(selList,"Y");
		grid.getStore().reload();
	},
	
	//批量设置初筛淘汰
	setScreenNoPass: function(btn){
		var grid = btn.findParentByType('grid');
		var selList = grid.getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要设置的记录");
			return;
		}
		
		this.setScreenPassStatusHandler(selList,"N");
		grid.getStore().reload();
	},
	
	//处理批量设置进入材料评审状态
	setScreenPassStatusHandler: function(setList,sta){
		var updateArr = [];
		//grid修改记录
		var modifyArr = [];
		for(var i=0; i<setList.length; i++){
			var recObj = setList[i].data;
			recObj.status = sta;

			modifyArr.push(recObj);
		}

		updateArr.push({
			type: "GRIDSAVE",
			data: modifyArr
		});
		
		var comParamsObj = {
			ComID: 'TZ_AUTO_SCREEN_COM',
			PageID: 'TZ_AUTO_SCREEN_STD',
			OperateType: 'U',
			comParams:{
				update: updateArr
			}
		}
		var tzParams = Ext.JSON.encode(comParamsObj);
		
		Ext.tzSubmit(tzParams,function(respData){

		},"保存成功",true,this);
	},
	
	//运行自动初筛引擎,如果上次运行正在进行中，显示进程运行详细信息
	runAutoScreenEngine: function(btn){
		var panel = btn.findParentByType('autoScreen');
		var classId = panel.classId;
		var batchId = panel.batchId;
		
		var comParamsObj = {
			ComID: 'TZ_AUTO_SCREEN_COM',
			PageID: 'TZ_AUTO_SCREEN_STD',
			OperateType: 'getLastEngineStatus',
			comParams:{
				classId: classId,
				batchId: batchId
			}
		}
		
		var status,processIns;
		var tzParams = Ext.JSON.encode(comParamsObj);
		Ext.tzLoadAsync(tzParams,function(respData){
			status = respData.status;
			processIns = respData.processIns;
		});
		
		if(processIns>0 && (status=="RUNNING" || status=="STARTED" || status=="QUENED")){
			Ext.tzBatchProcessDetails({
				//进程实例ID
				processIns: processIns,
				callBack:function(statusCode){
					
				}
			});
		}else{
			Ext.MessageBox.confirm('提示', '开启自动初筛后，系统会清除当前项目下的所有初筛信息重新打分。自动初筛进程可能会持续一段时间。是否确定开启自动初筛进程？', 
					function(btnId){
				if(btnId == 'yes'){
					comParamsObj.OperateType = "tzRunBatchProcess";
					tzParams = Ext.JSON.encode(comParamsObj);
					
					Ext.tzSubmit(tzParams,function(respData){
						var processIns = respData.processIns;
						Ext.tzBatchProcessDetails({
							//进程实例ID
							processIns: processIns,
							callBack:function(statusCode){
								
							}
						});
					},"运行成功",true,this);
				}
			});
		}
	},
	
	//查看打分过程
	onClickNumber: function(view,rowIndex,colIndex){
		var rec = view.getStore().getAt(rowIndex);
		
		var columns = view.grid.columns;
		var dateIndex = columns[colIndex-1]['dataIndex'];
		
		var dfgcStr = rec.get(dateIndex+"_label");
		if(dfgcStr.length > 0){
			var dfgcHtmlContent = "";
			
			var dfgcArr = dfgcStr.split("|");
			for(var i=0; i<dfgcArr.length; i++){
				dfgcHtmlContent = dfgcHtmlContent + '<span>'+ dfgcArr[i] +'</span>';
			}
		}
	},
	
	
	//查看自动初筛进程运行详情
	showBatchProcessInfo: function(btn){
		var panel = btn.findParentByType('autoScreen');
		var classId = panel.classId;
		var batchId = panel.batchId;
		
		var comParamsObj = {
			ComID: 'TZ_AUTO_SCREEN_COM',
			PageID: 'TZ_AUTO_SCREEN_STD',
			OperateType: 'getLastEngineStatus',
			comParams:{
				classId: classId,
				batchId: batchId
			}
		}
		
		var status,processIns;
		var tzParams = Ext.JSON.encode(comParamsObj);
		Ext.tzLoadAsync(tzParams,function(respData){
			status = respData.status;
			processIns = respData.processIns;
		});
		
		if(processIns > 0){
			Ext.tzBatchProcessDetails({
				//进程实例ID
				processIns: processIns,
				/**
				 * 回调函数,关闭返回时调用,statusCode-进程行回状态码
				 * QUENED -　排队中
				 * STARTED - 已启动
				 * RUNNING - 正在运行中
				 * SUCCEEDED - 成功完成
				 * ERROR - 发生错误
				 * FATAL - 发生严重错误
				 * STOPPING - 正在停止
				 * TERMINATED - 已强行终止
				 */
				callBack:function(statusCode){

				}
			});
		}else{
			Ext.Msg.alert("提示","尚未运行自动初筛引擎");
		}
	},
	
	//根据名次批量淘汰
	setWeedOutByRank: function(btn){
		var panel = btn.findParentByType('autoScreen');
		var gridStore = panel.down('grid').getStore();
		var classId = panel.classId;
		var batchId = panel.batchId;
		
		var className = 'KitchenSink.view.automaticScreen.setWeedOutWindow';
        if(!Ext.ClassManager.isCreated(className)){
             Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass({
        	classId: classId,
			batchId: batchId
        },function(){
        	gridStore.reload();
        });
        
        win.show();
	},
	
	
	//确定设置批量淘汰
	setWeedOutStuEnsure: function(btn){
		var win = btn.findParentByType('setWeedOutWindow');
		var setForm = win.child('form');
		var form = win.child('form').getForm();
		
		var classId = win.classId;
		var batchId = win.batchId;
		var screenNum = win.screenNum;
		
		if(setForm.isValid()){
			var outNum = form.findField('personNum').getValue();
			if(outNum > screenNum){
				Ext.Msg.alert("提示","淘汰人数不能超过参与初筛人数！");
				return;
			}
			
			var comParamsObj = {
				ComID: 'TZ_AUTO_SCREEN_COM',
				PageID: 'TZ_AUTO_SCREEN_STD',
				OperateType: 'setWeedOutByOrder',
				comParams:{
					classId: classId,
					batchId: batchId,
					outNum: outNum
					
				}
			}
			
			var tzParams = Ext.JSON.encode(comParamsObj);
			Ext.tzSubmit(tzParams,function(respData){
				//回调刷新
				win.reLoadGrid();
				win.close();
			},"保存成功",true,this);
		}
	},
	
	
	//编辑自动初筛详细信息
	editStuScreenDetails: function(grid,rowIndex,colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var classId = rec.get("classId");
		var batchId = rec.get("batchId");
		var appId = rec.get("appId");
		var name = rec.get("name");
		var msApplyId = rec.get("msApplyId");

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUTO_SCREEN_COM"]["TZ_ZDCS_INFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert("提示", "您没有权限");
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert("提示","未找到该功能页面对应的JS类，请检查配置。");
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

		cmp = new ViewClass({
			classId: classId,
			batchId: batchId,
			appId:appId,
			name:name,
			msApplyId:msApplyId,
			storeReload: function(){
				grid.getStore().reload();
			} 
		});

		cmp.on('afterrender',function(panel){
			var csDetailsform = panel.child('form');
			var form = csDetailsform.getForm();
			
			var comParamsObj = {
				ComID: 'TZ_AUTO_SCREEN_COM',
				PageID: 'TZ_ZDCS_INFO_STD',
				OperateType: 'QF',
				comParams:{
					classId: classId,
					batchId: batchId,
					appId: appId
				}
			}
			var tzParams = Ext.JSON.encode(comParamsObj);
			
			Ext.tzLoad(tzParams,function(respData){
				var formData = respData;
				form.setValues(formData);
				
				csDetailsform.down('tagfield[name=negativeList]').addCls('readOnly-tagfield-cls');
				csDetailsform.down('tagfield[name=autoLabel]').addCls('readOnly-tagfield-cls');
			});
		});

		tab = contentPanel.add(cmp);
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
	
	//保存自动初筛详细信息
	onAutoScreenDetailsSave: function(btn){
		var panel = btn.findParentByType('autoScreenDetails');
		var closePanel = btn.closePanel;

		var form = panel.child('form');
		var formRec = form.getForm().getValues();
		if(form.isValid()){
			var comParamsObj = {
				ComID: 'TZ_AUTO_SCREEN_COM',
				PageID: 'TZ_ZDCS_INFO_STD',
				OperateType: 'U',
				comParams:{
					update: [formRec]
				}
			}
			
			var tzParams = Ext.JSON.encode(comParamsObj);
			Ext.tzSubmit(tzParams,function(respData){
				var formDate = respData.formData;
				form.getForm().setValues(formDate);

				if(panel.storeReload){
					panel.storeReload();
				}
				if(closePanel == "Y"){
					panel.close();
				}
			},"保存成功",true,this);
		}
	},
	
	//确定保存自动初筛详细信息
	onAutoScreenDetailsEnsure: function(btn){
		this.onAutoScreenDetailsSave(btn);
	},
	//关闭自动初筛详细信息
	onAutoScreenDetailsClose: function(btn){
		var panel = btn.findParentByType('autoScreenDetails');
		if(panel) panel.close();
	},
	
	
	showApplicationForm: function(grid,rowIndex,colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var appId = rec.get("appId");

		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert("提示","您没有权限");
            return;
        }

		var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appId+'","TZ_MANAGER":"Y"}}';
        var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+encodeURIComponent(tzParams);
        var win = new Ext.Window({
            title : "查看报名表",
            maximized : true,
            width : Ext.getBody().width,
            height : Ext.getBody().height,
            autoScroll : true,
            border:false,
            bodyBorder : false,
            isTopContainer : true,
            modal : true,
            resizable : false,
            contentEl : Ext.DomHelper.append(document.body, {
                bodyBorder : false,
                tag : 'iframe',
                style : "border:0px none;scrollbar:true",
                src : viewUrl,
                height : "100%",
                width : "100%"
            }),
            buttons: [ {
                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
                iconCls:"close",
                handler: function(){
                    win.close();
                }
            }]
        });
        win.show();
	},
	
	
	tzViewApplyForm: function(btn){
		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert("提示","您没有权限");
            return;
        }
        
		var form = this.getView().child('form').getForm();
		var appInsID = form.findField('appId').getValue();

		var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appInsID+'","TZ_MANAGER":"Y"}}';
        var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+encodeURIComponent(tzParams);
        var win = new Ext.Window({
            title : "查看报名表",
            maximized : true,
            width : Ext.getBody().width,
            height : Ext.getBody().height,
            autoScroll : true,
            border:false,
            bodyBorder : false,
            isTopContainer : true,
            modal : true,
            resizable : false,
            contentEl : Ext.DomHelper.append(document.body, {
                bodyBorder : false,
                tag : 'iframe',
                style : "border:0px none;scrollbar:true",
                src : viewUrl,
                height : "100%",
                width : "100%"
            }),
            buttons: [ {
                text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
                iconCls:"close",
                handler: function(){
                    win.close();
                }
            }]
        });
        win.show();
	}
	
});