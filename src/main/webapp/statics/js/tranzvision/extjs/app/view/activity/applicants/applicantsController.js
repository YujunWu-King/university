Ext.define('KitchenSink.view.activity.applicants.applicantsController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.applicantsMg', 
	requires: [
	   		'KitchenSink.view.activity.applicants.sendTicketsFormWin'
	   		
	   	],
	closeWin: function(btn){
		//关闭窗口
		//var win = this.lookupReference('setStatusWindow')
		var win = btn.findParentByType('window');
		win.close();
	},

/*********************************************************
+功能描述：关闭当前窗口    									+
+开发人：张浪												+
********************************************************/
	onFormClose: function(){
		this.getView().close();
	},
	
/*********************************************************
+功能描述：弹出发送函件窗口									+
+开发人：张浪												+
********************************************************/
	showSendWindow: function(){
		//选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择需要发送函件的报名人记录");   
			return;
	   }
		
		var win = this.lookupReference('sendFormWindow');
        
        if (!win) {
            win = new KitchenSink.view.activity.applicants.sendFormWin();

            this.getView().add(win);
        }
        win.recArray = selList;
		//console.log(checkLen);
        win.show();
	},
	
/*********************************************************
+功能描述：弹出设置签到状态窗口								+
+开发人：张浪												+
********************************************************/
	showSetStatusWindow: function(){
		//选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择批量设置签到状态的报名人记录");   
			return;
	   }
		
		var win = this.lookupReference('setStatusWindow');
        
        if (!win) {
            win = new KitchenSink.view.activity.applicants.setStatusBatWin();
			
            this.getView().add(win);
        }
        
        win.show();
	},

/*********************************************************
+功能描述：查看发送历史										+
+开发人：张浪												+
********************************************************/
	onCheckHistory: function(btn){
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
		//发送类型
		var sType = btn.findParentByType('panel').sendType;
		//活动编号
		var activetyId = btn.findParentByType('panel').child('form').getForm().getValues()['activetyId'];
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BMRGL_COM"]["TZ_GD_HJFSLS_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_HJFSLS_STD，请检查配置。');
			return;
		}
		
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		//className = 'KitchenSink.view.activity.applicants.sendHistoryGrid';
		
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
		cmp.sendType = sType;
		cmp.actID = activetyId;

		cmp.on('afterrender',function(grid){
			
			var tzStoreParams = '{"cfgSrhId":"TZ_GD_BMRGL_COM.TZ_GD_HJFSLS_STD.TZ_HDHJFSLS_VW","condition":{"TZ_HUOD_ID-operator": "01","TZ_HUOD_ID-value": "'+ activetyId+'","TZ_TASK_LX-operator": "01","TZ_TASK_LX-value": "'+ sType+'"}}';
			grid.store.tzStoreParams = tzStoreParams;
			grid.store.reload();
			
			/*
			var tzParams = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_HJFSLS_STD","OperateType":"QG","comParams":{"activetyId":"'+activetyId+'","sendType":"'+sType+'"}}';
			var tzStoreParams = '{"activetyId":"'+activetyId+'","sendType":"'+sType+'"}';
			Ext.tzLoad(tzParams,function(respDate){
				grid.store.tzStoreParams = tzStoreParams;
				grid.store.reload();
			});
			*/
			
		});
		
		tab = contentPanel.add(cmp);     
		contentPanel.setActiveTab(tab);
		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}	
	},

/*********************************************************
+功能描述：保存报名人信息修改 								+
+开发人：张浪												+
********************************************************/
	saveData: function(){
		var grid = this.getView();
		
		var records = null;
		var jsonData = "";
		
		records = grid.store.getModifiedRecords();
		if (records.length <= 0)
    	{ return; }

		jsonData = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_BMRGL_STD","OperateType":"U","comParams":{"update":[';//Json数据
		
    	for (var i = 0; i < records.length; i++) {//遍历集合,将所有修改的拼接成Json数据
			if (i == 0){
				jsonData += Ext.JSON.encode(records[i].data);
			} else {
				jsonData += ',' + Ext.JSON.encode(records[i].data);
			} 
		}
		jsonData += "]}}";
		
		Ext.tzSubmit(jsonData,function(respData){
			grid.store.reload();	
		},"",true,this);
	},
	onEnsure:function(){
		this.saveData();
		this.getView().close();
	},
/*********************************************************
+功能描述：批量设置报名人的参与状态							+
+开发人：张浪												+
********************************************************/
	setStatusBatch: function(btn){
		
	   var jsonData = "";
	   var bmrIds = "";
	   var grid = this.getView();
		//选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   //获取签到状态设置值
	   var form = btn.findParentByType('window').child('form');
	   
	   if(form.isValid()){
		   var formValues = form.getForm().getValues();
		   var setStatus = formValues['signStatus'];
			
		   jsonData = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_BMRGL_STD","OperateType":"setStatus","comParams":{"activityId":"'+selList[0].data.activityId+'","signStatus":"'+setStatus+'","bmrIds":[';
		   
		   for(var i=0;i<checkLen;i++){
			   if(i==0){
				   bmrIds += '"' + selList[i].data.applicantsId + '"';
			   }else{
				   bmrIds += ',"' + selList[i].data.applicantsId + '"';
			   }
			   
			}
			jsonData += bmrIds + "]}}";
	
			/********************数据提交***************************/
			Ext.tzSubmit(jsonData,function(respData){
				grid.store.reload();
			},"",true,this);
			this.closeWin(btn);
	   }
	},
	
/*********************************************************
+功能描述：给活动报名人发送函件								+
+开发人：张浪												+
********************************************************/
	onSendMsg: function(btn){
		var tzParams;
		var bmrStr;
		
		var panel = btn.findParentByType('panel');
		var form = panel.child('form').getForm();	
		var formVals = form.getValues();
		//报名人ID数组
		var bmrIds = panel.bmrIdArr;
		
		var params = Ext.JSON.encode(formVals);
		if (params.indexOf('{')==0 && params.indexOf('}')==params.length-1) { 
			params = params.substr(1, params.lastIndexOf('}')-1); 
		}
		var sType = panel.sendType;
		
		if(form.isValid()){
			if(sType=="EMAIL"){
				if(formVals['sendEmailAddr']==undefined){
					params += ',"sendEmailAddr":"' + form.findField('sendEmailAddr').getValue() + '"';	 
				}
				tzParams = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_EMAIL_STD","OperateType":"EMAIL","comParams":{'+params+',"bmrIds":[';
				
			}else if(sType=="SMS"){
				tzParams = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_SMS_STD","OperateType":"SMS","comParams":{'+params+',"bmrIds":[';
				
			}else if(sType=="WECHAT"){
				/*
				var wechatCon = formVals['weChatCont'] + formVals['editContent'];
				
				tzParams = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_WECHAT_STD","OperateType":"WECHAT","comParams":{"activetyId":"'+activityId+'","content":"'+wechatCon+'","bmrIds":[';
				*/
				tzParams = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_WECHAT_STD","OperateType":"WECHAT","comParams":{'+params+',"bmrIds":[';
			}
			
			for(i=0;i<bmrIds.length;i++){
				if(i==0){
					bmrStr = '"' + bmrIds[i] + '"';
				}else{
					bmrStr = bmrStr + ',"' + bmrIds[i] + '"';	
				}
			}
			tzParams += bmrStr + ']}}';
			console.log(tzParams);
			//以下发送处理
			Ext.tzSubmit(tzParams,function(response){
					
			},"",true,this);
		}
	},
	
	//报名信息项下拉框值显示
	selectOptionHandler: function(value ,metaData ,record ,rowIndex ,colIndex ,store ,view){
			
			var colStoreArr = this.getView().colStore;
			var columnStore = colStoreArr[colIndex-2];

			for (var i = 0; i < columnStore.data.items.length; i++) {
				  if (columnStore.data.items[i].data.transID == value) {
					   return columnStore.data.items[i].data.transName;
				  }
			 }
				
	},
	
	
	/*********************************************************
	+功能描述：查询											+
	+开发人：马炳春											+
	********************************************************/
	searchComList: function(btn){     //searchComList为各自搜索按钮的handler event;
		var appGrid = btn.findParentByType('applicantsMg');
		var activetyId = btn.findParentByType('panel').child('form').getForm().getValues()['activityId'];
		//console.log(activetyId);
        Ext.tzShowCFGSearch({            
           cfgSrhId: 'TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.TZ_NAUDLIST_G_V', 
		   condition:
            {
                "TZ_ART_ID": activetyId          //设置搜索字段的默认值，没有可以不设置condition;
            },   
           callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
                
                //console.log(seachCfg);
                var tzParams = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_BMRGL_STD","OperateType":"getSearchSql","comParams":'+seachCfg+'}';
				Ext.tzLoad(tzParams,function(responseData){
					var getedSQL = responseData.SQL;
					appGrid.getedSQL = getedSQL;
				});
            }
        });    
    },
	
	cfgSearchSendHistory: function(btn){
		var grid = btn.findParentByType("grid");
		var sType = grid.sendType;
		var actID = grid.actID;
		Ext.tzShowCFGSearch({            
           cfgSrhId: 'TZ_GD_BMRGL_COM.TZ_GD_HJFSLS_STD.TZ_HDHJFSLS_VW', 
		   condition:
            {
                "TZ_HUOD_ID": actID,          //设置搜索字段的默认值，没有可以不设置condition;
				"TZ_TASK_LX": sType
            },   
           callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });    
	},
	onComRegClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
	
	/*********************************************************
	+功能描述：导出选中报名人信息									+
	+开发人：张浪												+
	********************************************************/
	exportApplyInfo:function(btn){
		var appGrid = btn.findParentByType('applicantsMg');
		var activityId = appGrid.child('form').getForm().getValues()['activityId'];
		
		var jsonData = "";
		var bmrIds = "";
		//选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择需要导出的报名人记录");   
			return;
	   }
	   
	   /*
	   jsonData = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_BMRGL_STD","OperateType":"EXPORT","comParams":{"activityId":"'+ selList[0].data.activityId +'","bmrIds":['
	   
	   for(var i=0;i<checkLen;i++){
		   if(i==0){
			   bmrIds += '"' + selList[i].data.applicantsId + '"';
		   }else{
			   bmrIds += ',"' + selList[i].data.applicantsId + '"';
		   }
		}
		jsonData += bmrIds + "]}}";
		
		Ext.tzSubmit(jsonData,function(respDate){
			var fileUrl = respDate.fileUrl;
			window.location.href=fileUrl;
		},"导出报名人信息成功",true,this);
		*/
	   var bmrIds = [];
	   for(var i=0;i<checkLen;i++){
		   bmrIds.push(selList[i].data.applicantsId);
		}
	   var comParamsObj = {
			ComID: "TZ_GD_BMRGL_COM",
			PageID: "TZ_GD_BMRGL_STD",
			OperateType: "EXPORT",
			comParams: {
				activityId: activityId,
				bmrIds: bmrIds
			}
	   };
	   
	   var className = 'KitchenSink.view.activity.applicants.exportExcelWindow';
       if(!Ext.ClassManager.isCreated(className)){
           Ext.syncRequire(className);
       }
       var ViewClass = Ext.ClassManager.get(className);
       var win = new ViewClass({
    	   activityId: activityId,
       	   expBmr: comParamsObj
       });
       
       win.show();
	},
	
	//导出搜索结果报名人信息
	exportSearchApplyInfo: function(btn){
		var appGrid = btn.findParentByType('applicantsMg');
		var activityId = appGrid.child('form').getForm().getValues()['activityId'];
		var searchSql = "";
		
		if((typeof appGrid.getedSQL) == "undefined"){
			searchSql = "SELECT TZ_HD_BMR_ID FROM PS_TZ_NAUDLIST_T WHERE TZ_ART_ID='"+ activityId +"' AND TZ_NREG_STAT IN('1','4')";
		}else{
			searchSql = appGrid.getedSQL;
		}
		
		/*
		jsonData = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_BMRGL_STD","OperateType":"EXPORT","comParams":{"activityId":"'+ activetyId +'","searchSql":"'+ searchSql +'"}}';
		
		Ext.tzSubmit(jsonData,function(respDate){
			var fileUrl = respDate.fileUrl;
			window.location.href=fileUrl;
		},"导出报名人信息成功",true,this);
		*/
		
		
		var comParamsObj = {
			ComID: "TZ_GD_BMRGL_COM",
			PageID: "TZ_GD_BMRGL_STD",
			OperateType: "EXPORT",
			comParams: {
				activityId: activityId,
				searchSql: searchSql
			}
		};
		
		
		var className = 'KitchenSink.view.activity.applicants.exportExcelWindow';
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass({
        	activityId: activityId,
        	expBmr: comParamsObj
        });
        
        win.show();
	},
	
	//下载导出结果
	downloadExportFile: function(btn){
		var appGrid = btn.findParentByType('applicantsMg');
		var activityId = appGrid.child('form').getForm().getValues()['activityId'];
		
		var className = 'KitchenSink.view.activity.applicants.exportExcelWindow';
    	
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass({
        	activityId: activityId,
        	type: 'download'
        });
        
        var tabPanel = win.lookupReference("packageTabPanel");
        tabPanel.setActiveTab(1);
        
        win.show();
	},
	
	ensureExport: function(btn){
		var win = btn.findParentByType('bmrExportExcelWin');
		var form = btn.findParentByType('form');
		var formRec = form.getForm().getValues();
		
		if(form.isValid()){
			var exportObj = win.exportObj;
			exportObj.comParams.fileName = formRec.FileName;
			
			var jsonData = Ext.JSON.encode(exportObj);
			Ext.tzSubmit(jsonData,function(respDate){
				var tabPanel = win.lookupReference("packageTabPanel");
				tabPanel.child('grid').getStore().reload();
		        tabPanel.setActiveTab(1);
		        
			},"导出报名人信息成功",true,this);
		}
	},
	
	
	exportQuery: function(btn){
		var win = btn.findParentByType('bmrExportExcelWin');
		var activityId = win.activityId;
		
		Ext.tzShowCFGSearch({            
           cfgSrhId: 'TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.TZ_HD_BMRDC_V', 
		   condition:
            {
                "TZ_ART_ID": activityId,
                "TZ_DLZH_ID": TranzvisionMeikecityAdvanced.Boot.loginUserId
            },   
           callback: function(seachCfg){
	        	var searchObj = eval('(' + seachCfg + ')');
	        	searchObj.type="expHistory";
	
	        	seachCfg = Ext.JSON.encode(searchObj);

                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });   
	},
	
	exportDelete: function(btn){
        var me=this;
        var win = btn.findParentByType("window");
        var tabPanel = win.lookupReference("packageTabPanel");
        //选中行
        var selList = tabPanel.child("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示", "请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm("确认", "您确定要删除所选记录吗?", function(btnId){
                if(btnId == 'yes'){
                    var store = btn.findParentByType('grid').store;
                    store.remove(selList);
                }
            },this);
        }
    },
	
	downloadFile: function(grid, rowIndex){
        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        var procInsId=record.get("procInsId");

        if(procInsId != ""){
        	/*
        	var viewUrl = TzUniversityContextPath+"/admission/exprar/"+procInsId;
            window.open(viewUrl, "download","status=no,menubar=yes,toolbar=no,location=no");
            */
        	 var tzParams = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_BMRGL_STD","OperateType":"DOWNLOAD","comParams":{"procInsId":"'+ procInsId +'"}}';
             //保存数据
             Ext.tzLoad(tzParams,function(respData){
                 var filePath = respData.filePath;
                 //window.open(filePath, "download","status=no,menubar=yes,toolbar=no,location=no");
                 if(filePath == ""){
                	 Ext.Msg.alert("提示","文件不存在");
                 }else{
                	 window.location.href=filePath;
                 }
             });
        	
        }else{
            Ext.MessageBox.alert("提示", "找不到附件");
        }
    },
    
    
    exportGridSave: function(btn){
        //组件注册信息列表
         var grid = btn.findParentByType("grid");
         //组件注册信息数据
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
         var comParams ="";
         if(removeJson != ""){
              comParams = '"delete":[' + removeJson + "]";
         }

         //提交参数
         var tzParams = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_BMRGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
         //保存数据
         Ext.tzSubmit(tzParams,function(){
             store.reload();
         },"",true,this);
     },
 	/*********************************************************
	 +功能描述：弹出发送电子门票窗口									+
	 +开发人：张浪												+
	 ********************************************************/
	showSendTicketsWindow: function(btn){
		var actId=btn.findParentByType('applicantsMg').child('form').getForm().getValues()['activityId'];
		//选中行
		var selList = this.getView().getSelectionModel().getSelection();
		//选中行长度
		var checkLen = selList.length;
		/*如果没有选择，默认发送所有“已报名”的记录
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择需要发送电子门票的报名人记录");
			return;
		}
		*/
		/*
		var win = this.lookupReference('sendTicketsFormWindow');
		if (!win) {*/
			win = new KitchenSink.view.activity.applicants.sendTicketsFormWin();
			this.getView().add(win);
		//}
		win.recArray = selList;
		win.checkLen = checkLen;
		win.actId = actId;
		//console.log(checkLen);
		win.show();
	},

	
	//给选中报名人创建线索
	createClue: function(btn){
		var actId=btn.findParentByType('applicantsMg').child('form').getForm().getValues()['activityId'];
		
		//选中行长度
		var selList = [];
		var selModel = this.getView().getSelectionModel().getSelection();
		var gridStore = this.getView().getStore();
		if(gridStore.isFiltered()){ //如果使用filter过滤，过滤后的勾选行不发送
			 for(var i=0;i<selModel.length; i++){
				var index = gridStore.indexOf(selModel[i]);
				if(index != -1){
					selList.push(selModel[i]);	 
				}
			}
		}else{
			selList = selModel;
		}
		
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择创建线索的报名人记录");   
			return;
		}
		
		var bmrIds = [];
		var existClueFlag="";
		for(var i=0;i<checkLen;i++){
			if(selList[i].data.leadId != null && selList[i].data.leadId !=""){
				existClueFlag = "Y";
			}
			bmrIds.push(selList[i].data.applicantsId);
		}
		
		if(existClueFlag=="Y") {
			Ext.Msg.alert("提示","选择的报名人记录中存在已创建线索的数据，请重新选择");
			return;
		}
		
		var tzParamsObj={
			ComID: 	"TZ_GD_BMRGL_COM",
			PageID: "TZ_GD_BMRGL_STD",
			OperateType: 'tzCreateClue',
			comParams: {
				activityId: actId,
				bmrIds: bmrIds
			}
		};
		
		 var tzParams = Ext.JSON.encode(tzParamsObj);
         Ext.tzSubmit(tzParams,function(){
        	 var existsLead = result.existsLead;
	    	 if(existsLead != ""){
	    		 Ext.Msg.alert("提示",existsLead + "线索已存在，没有重复创建");
	    	 }
	    	 
        	 gridStore.reload();
         },"创建线索成功",true,this);
	},
	
	//给选中报名人创建其他机构线索
	createClueForOtherOrg: function(btn){
		var actId=btn.findParentByType('applicantsMg').child('form').getForm().getValues()['activityId'];
		
		//选中行长度
		var selList = [];
		var selModel = this.getView().getSelectionModel().getSelection();
		var gridStore = this.getView().getStore();
		if(gridStore.isFiltered()){ //如果使用filter过滤，过滤后的勾选行不发送
			 for(var i=0;i<selModel.length; i++){
				var index = gridStore.indexOf(selModel[i]);
				if(index != -1){
					selList.push(selModel[i]);	 
				}
			}
		}else{
			selList = selModel;
		}
		
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","请选择创建线索的报名人记录");   
			return;
		}
		
		var bmrIds = [];
		for(var i=0;i<checkLen;i++){
			bmrIds.push(selList[i].data.applicantsId);
		}
		
        var win = new KitchenSink.view.activity.applicants.chooseClueOrgWindow({
        	actId: actId,
        	bmrIds: bmrIds,
        	callback: function(){
        		gridStore.reload();
        	}
        });
        //this.getView().add(win);
        win.show();
	},
	
	createClueEnsure: function(btn){
		var win = btn.findParentByType('window');
		var form = win.child('form').getForm();
		
		if(!form.isValid()) return;
		
		var actId = win.actId,
			bmrIds = win.bmrIds,
			orgId = form.getValues()['orgId'];
		
		var tzParamsObj={
			ComID: 	"TZ_GD_BMRGL_COM",
			PageID: "TZ_GD_BMRGL_STD",
			OperateType: 'tzCreateClue',
			comParams: {
				activityId: actId,
				bmrIds: bmrIds,
				orgId: orgId
			}
		};

		 var tzParams = Ext.JSON.encode(tzParamsObj);
	     Ext.tzSubmit(tzParams,function(result){
	    	 var existsLead = result.existsLead;
	    	 if(existsLead != ""){
	    		 Ext.Msg.alert("提示",existsLead + "线索已存在，没有重复创建");
	    	 }
	    	 
	    	 //刷新报名人列表
	    	 if(typeof(win.callback) == "function") win.callback();
	    	 win.close();
	     },"创建线索成功",true,this);
	}
});