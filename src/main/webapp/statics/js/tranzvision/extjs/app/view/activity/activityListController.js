Ext.define('KitchenSink.view.activity.activityListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.activityMg', 
    /*
		requires: [
       'KitchenSink.view.activity.activityInfoPanel'
    ],
    */
    viewPhoneActivity: function(view, rowIndex) {
  	  //组件注册表单
			var store = view.findParentByType("grid").store;
			 var selRec = store.getAt(rowIndex);
			 //活动ID
		   var activityId = selRec.get("activityId");
		   var siteId = selRec.get("siteId");
		   var columnId = selRec.get("columnId");
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_HD_MANAGER_COM"]["TZ_HD_SJ_VIEW"];
			if( pageResSet == "" || typeof(pageResSet) == "undefined" ){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || typeof(className) == "undefined"  ){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_MENUADD_STD，请检查配置。');
				return;
			}
			
			var win = this.lookupReference('activityCodePanel');
	        
	    if (!win) {
				Ext.syncRequire(className);
				ViewClass = Ext.ClassManager.get(className);
			  //新建类
	      win = new ViewClass();
	      this.getView().add(win);
	      win.title = "手机版活动详情页发布地址";    
	    }
	    win.on('afterrender',function(panel){
	    	var codeForm = panel.child("form").getForm();
	    	 //参数
				var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_SJ_VIEW","OperateType":"QF","comParams":{"activityId":"'+activityId+'","siteId":"'+siteId+'","coluId":"'+columnId+'"}}';
				
				//加载数据
				Ext.tzLoad(tzParams,function(responseData){
					var formData = responseData.formData;
					codeForm.setValues(formData);
					panel.down('image[name=codeImage]').setSrc(TzUniversityContextPath + formData.codeImage);	
					
				});
	    });
      win.show();
  	},
    addActivity: function() {
    	
    	//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_HD_MANAGER_COM"]["TZ_HD_INFO_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_HD_INFO_STD，请检查配置。');
				return;
			}
		
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
      contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
      contentPanel.body.addCls('kitchensink-example');

      //className = 'KitchenSink.view.activity.activityInfoPanel';
      if(!Ext.ClassManager.isCreated(className)){
				Ext.syncRequire(className);
			}	
      ViewClass = Ext.ClassManager.get(className);
      clsProto = ViewClass.prototype;

      if (clsProto.themes) {
          clsProto.themeInfo = clsProto.themes[themeName];

          if (themeName === 'gray') {
              clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
          }else if (themeName !== 'neptune' && themeName !== 'classic') {
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
			
    cmp.on('afterrender',function(panel){
    	
    	/*隐藏发布对象*/
		var fbSet = panel.down('fieldset[name=fbSet]');
		fbSet.hide();
				//附件列表
				/*
				var attachmentGrid = this.lookupReference('attachmentGrid');
				var attachmentToolBar = this.lookupReference('attachmentToolBar');
				attachmentGrid.store.load();
				attachmentToolBar.store.load();	
				*/			
				//信息项列表
				//var applyItemGrid = this.lookupReference('applyItemGrid');
				//var tzStoreParams = "{'activityId':''}";
				//applyItemGrid.store.tzStoreParams = tzStoreParams;
				//applyItemGrid.store.load();
				
				var cmpForm = panel.child("form").getForm();
				//新建时，隐藏听众列表
				cmpForm.findField("AudList").hide(); 
				var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_INFO_STD","OperateType":"QF","comParams":{"activityId":""}}';
				//var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_INFO_STD","OperateType":"QF","comParams":{"activityId":"","siteId":"","coluId":""}}';
				//加载数据
				/**/
				Ext.tzLoad(tzParams,function(responseData){
						var formData = responseData.formData;
						cmpForm.setValues(formData);
						//cmpForm.findField("siteIds").setValue(formData.siteids);
						//cmpForm.findField("columsHide").setValue(formData.columsHide);
						//cmpForm.findField("saveImageAccessUrl").setValue(formData.saveImageAccessUrl);
						//cmpForm.findField("saveAttachAccessUrl").setValue(formData.saveAttachAccessUrl);
				});
				
				var applyItemGrid = this.lookupReference('applyItemGrid');
				var r = Ext.create('KitchenSink.view.activity.applyItemModel', {
    			activityId: "",
        	applyItemId: "TZ_CYR_NAME",
					applyItemNum: 1,
					applyItemName: '姓名',
					applyItemNameEng:'Name',
					applyItemRequired: 'Y',
					applyItemType: '1'
    		});
    	 applyItemGrid.store.insert(0,r);
    	 
    	 var r = Ext.create('KitchenSink.view.activity.applyItemModel', {
    			activityId: "",
        	applyItemId: "TZ_ZY_SJ",
					applyItemNum: 2,
					applyItemName: '手机',
					applyItemNameEng:'Phone',
					applyItemRequired: 'Y',
					applyItemType: '1'
    		});
    	 applyItemGrid.store.insert(1,r);
    	 
    	 var r = Ext.create('KitchenSink.view.activity.applyItemModel', {
    			activityId: "",
        	applyItemId: "TZ_ZY_EMAIL",
					applyItemNum: 3,
					applyItemName: '邮箱',
					applyItemNameEng:'Email',
					applyItemRequired: 'Y',
					applyItemType: '1'
    		});
    	 applyItemGrid.store.insert(2,r);

		});
			
      tab = contentPanel.add(cmp);     
	  tab.on(Ext.tzTabOn(tab,this.getView(),cmp));	
      contentPanel.setActiveTab(tab);
	
      Ext.resumeLayouts(true);

     	if (cmp.floating) {
         cmp.show();
      }
    },
    editActivity: function() {
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
	   //活动ID
	   var activityId= selList[0].get("activityId");
	   //var siteId= selList[0].get("siteId");
	   //var columnId= selList[0].get("columnId");
	 
	   //显示活动信息编辑页面
	   this.editActivityIdByID(activityId);
    },
		editSelActivityInfo: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //活动ID
	   var activityId = selRec.get("activityId");
	   //var siteId = selRec.get("siteId");
	   //var columnId = selRec.get("columnId");
	   //显示活动信息编辑页面
	   this.editActivityIdByID(activityId);
		},
		editActivityIdByID: function(activityId){
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_HD_MANAGER_COM"]["TZ_HD_INFO_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_HD_INFO_STD，请检查配置。');
				return;
			}
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
			contentPanel.body.addCls('kitchensink-example');
	
			//className = 'KitchenSink.view.activity.activityInfoPanel';
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
			//操作类型设置为更新
			cmp.actType = "update";
			
			cmp.on('afterrender',function(panel){
				/*隐藏发布对象*/
				var fbSet = panel.down('fieldset[name=fbSet]');
				fbSet.hide();
				//活动表单信息;
				var form = panel.child('form').getForm();
				//form.findField("activityId").setReadOnly(true);
				//附件集;
				//var attachGrid = Ext.getCmp('attachmentGrid');
				var attachGrid = panel.down('grid[name=attachmentGrid]');
				//报名信息项列表
				//var grid = panel.child('grid');
			  //var grid = Ext.getCmp('applyItemGrid');
				var grid =panel.down('grid[name=applyItemGrid]');
				
				//预览发布列表
				var viewArtGrid =panel.down('grid[name=viewArtGrid]');
				
				//参数
				//var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_INFO_STD","OperateType":"QF","comParams":{"activityId":"'+activityId+'","siteId":"'+siteId+'","coluId":"'+columnId+'"}}';
				var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_INFO_STD","OperateType":"QF","comParams":{"activityId":"'+activityId+'"}}';
				
				//加载数据
				Ext.tzLoad(tzParams,function(responseData){
					//活动基本信息
					var formData = responseData.formData;
					form.setValues(formData);
					
										
					//听众赋值20170209
				     var audIDList=formData.AudID;
		             var audNameList=formData.AudName;
		             var oprIdArray=new Array();
		             var i=0,j=0;
		                for(j=0;j<audIDList.length;j++){
		                    var TagModel=new KitchenSink.view.activity.tagModel();
		                    var audId = audIDList[j];
		                    var audName=audNameList[j];
		                    TagModel.set('tagId',audId);
		                    TagModel.set('tagName',audName);
		                    oprIdArray[i]=TagModel;
		                    i++;
		                }
		                form.findField("AudList").setValue(oprIdArray);
		                
		             //根据发布对象判断是否因此听众
		             var str_limit=formData.limit;
		             if ("B"==str_limit){
		            	 //听众
		            	 form.findField("AudList").show(); 
		             }else{
		            	 //无限制
		            	 form.findField("AudList").hide(); 
		             }
		             
		             
					
					/*
					var publishStatus = form.findField("publishStatus").getValue();
					var siteId = form.findField("siteId").getValue();
					var coluId = form.findField("coluId").getValue();
					if (publishStatus == "Y"){
						form.findField("publishStatusDesc").setValue("已发布");
						var viewUrl = formData.publishUrl;
				  		form.findField("publishUrl").setValue(viewUrl);
					}
					
					if (publishStatus == "N"){
						form.findField("publishStatusDesc").setValue("未发布");
					}
					*/
						//Ext.getCmp( "titileImage").setSrc(Ext.getCmp( "titleImageUrl").getValue());	
						var titleImageUrl = panel.down('hiddenfield[name=titleImageUrl]').getValue();
						if(titleImageUrl!=""){
							panel.down('image[name=titileImage]').setSrc(TzUniversityContextPath + titleImageUrl);	
						}
						//附件集
						var tzStoreParams = '{"activityId":"'+activityId+'","gridTyp":"FJ"}';
						attachGrid.store.tzStoreParams = tzStoreParams;
						attachGrid.store.load();			
					  //报名信息项列表
						var tzStoreParams = '{"activityId":"'+activityId+'","gridTyp":"BMX"}';
						grid.store.tzStoreParams = tzStoreParams;
						grid.store.load();	
						
						//图片集;
						var picDataView = panel.down('dataview[name=picView]');
						var tzStoreParams = '{"activityId":"'+activityId+'","gridTyp":"TPJ"}';
						picDataView.store.tzStoreParams = tzStoreParams;
						picDataView.store.load();	
						
						//预览发布列表;
						var tzStoreParams = '{"activityId":"'+activityId+'","gridTyp":"VIEWART"}';
						viewArtGrid.store.tzStoreParams = tzStoreParams;
						viewArtGrid.store.load();		
				});
				
			});
			
			tab = contentPanel.add(cmp);     
			tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
			contentPanel.setActiveTab(tab);
	
			Ext.resumeLayouts(true);
	
			if (cmp.floating) {
				cmp.show();
			}
		},
		
/*********************************
报名人管理
**********************************/		
	actApplicantsMg: function(view, rowIndex, colIndex, item, e, record, row){
		
		Ext.tzSetCompResourses("TZ_GD_BMRGL_COM");
		
			//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BMRGL_COM"]["TZ_GD_BMRGL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_BMRGL_STD，请检查配置。');
			return;
		}
		
		var actId = record.data.activityId;
		var me = this;
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		//className = 'KitchenSink.view.activity.applicants.applicantsList';
		
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
		
		/***********************************************************************/
		var tzColParam = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_BMRGL_STD","OperateType":"creaColumn","comParams":{"activityId":"'+ actId +'"}}';
		//动态加载报名信息项
		Ext.tzLoad(tzColParam,function(respData){
			appItems = respData.appColumn;	

			var appClomn = me.createColumnsArr(appItems);
			var colStore = me.getComboColumnStore(appItems);
			cmp = new ViewClass(appClomn,colStore);
			
			cmp.on('afterrender',function(gridPanel){
			
				//var tzParams = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_BMRGL_STD","OperateType":"QG","comParams":{"activityId":"'+actId+'","cfgSrhId":"TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.TZ_NAUDLIST_G_V"}}';
				
				//var tzStoreParams = '{"activityId":"'+actId+'"}';
				
				var tzStoreParams = '{"cfgSrhId":"TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.TZ_NAUDLIST_G_V","condition":{"TZ_ART_ID-operator": "01","TZ_ART_ID-value": "'+ actId+'"}}';
				gridPanel.store.tzStoreParams=tzStoreParams;
				gridPanel.store.reload();
				
				var form = gridPanel.child('form').getForm();
				form.setValues([{id:'activityId', value:actId}]);
								
				
				//Ext.tzLoad(tzStoreParams,function(responseData){
				
					//var formData = responseData.formData;
					//form.setValues(formData);
		
				//});
			});
			
			tab = contentPanel.add(cmp);     
			
			contentPanel.setActiveTab(tab);
	
			Ext.resumeLayouts(true);
	
			if (cmp.floating) {
				cmp.show();
			}
		});
		
		/************************************************************************/
		
		//cmp = new ViewClass(actId);
		
		
	},
	
	/*********************************
报名人管理
**********************************/		
	actApplicants: function(view,t,rowIndex){
		var record = view.findParentByType("grid").store.getAt(rowIndex);
		Ext.tzSetCompResourses("TZ_GD_BMRGL_COM");
		
			//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BMRGL_COM"]["TZ_GD_BMRGL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_BMRGL_STD，请检查配置。');
			return;
		}
		
		var actId = record.data.activityId;
		var me = this;
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
		contentPanel.body.addCls('kitchensink-example');

		//className = 'KitchenSink.view.activity.applicants.applicantsList';
		
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
		
		/***********************************************************************/
		var tzColParam = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_BMRGL_STD","OperateType":"creaColumn","comParams":{"activityId":"'+ actId +'"}}';
		//动态加载报名信息项
		Ext.tzLoad(tzColParam,function(respData){
			appItems = respData.appColumn;	

			var appClomn = me.createColumnsArr(appItems);
			var colStore = me.getComboColumnStore(appItems);
			cmp = new ViewClass(appClomn,colStore);
			
			cmp.on('afterrender',function(gridPanel){
			
				//var tzParams = '{"ComID":"TZ_GD_BMRGL_COM","PageID":"TZ_GD_BMRGL_STD","OperateType":"QG","comParams":{"activityId":"'+actId+'","cfgSrhId":"TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.TZ_NAUDLIST_G_V"}}';
				
				//var tzStoreParams = '{"activityId":"'+actId+'"}';
				
				var tzStoreParams = '{"cfgSrhId":"TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.TZ_NAUDLIST_G_V","condition":{"TZ_ART_ID-operator": "01","TZ_ART_ID-value": "'+ actId+'","TZ_NREG_STAT-operator":"10","TZ_NREG_STAT-value":["1","4"]}}';
				gridPanel.store.tzStoreParams=tzStoreParams;
				gridPanel.store.reload();
				
				var form = gridPanel.child('form').getForm();
				form.setValues([{id:'activityId', value:actId}]);
								
				
				//Ext.tzLoad(tzStoreParams,function(responseData){
				
					//var formData = responseData.formData;
					//form.setValues(formData);
		
				//});
			});
			
			tab = contentPanel.add(cmp);     
			
			contentPanel.setActiveTab(tab);
	
			Ext.resumeLayouts(true);
	
			if (cmp.floating) {
				cmp.show();
			}
		});
		
		/************************************************************************/
		
		//cmp = new ViewClass(actId);
		
		
	},
	
	createColumnsArr: function(appItems){
		var colItems=[];
		for (var i=0; i<appItems.length; i++){
			var columnItem;
			if (appItems[i].editType == "1"){
				//输入框
				if (appItems[i].columnId == "TZ_CYR_NAME"){
					columnItem	= {
						text: appItems[i].columnName,
						draggable:false,
						dataIndex: appItems[i].columnId,
						width:120
					};
				}else{
					columnItem	= {
						text: appItems[i].columnName,
						//draggable:false,
						dataIndex: appItems[i].columnId,
						width:120,
						editor:{
							xtype:'textfield'
						}
					};
				}
			} else {
				//下拉框
				columnItem	= {
					text: appItems[i].columnName,
					//draggable:false,
					dataIndex: appItems[i].columnId,
					width:120,
					editor: {
						xtype: 'combo', 
						forceSelection: true,
						valueField: 'transID',
						displayField: 'transName',
						store: new Ext.data.Store({		
							fields: ['transID', 'transName'],
							data:appItems[i].transVal
						})
					},
					renderer: 'selectOptionHandler'
				};
			}
			colItems.push(columnItem);	
		}
		return colItems;
	},
	
	getComboColumnStore: function(appItems){
		var storeArr = [];
		for (var i=0; i<appItems.length; i++){
			var columnItem;
			
			if (appItems[i].editType == "1"){
				storeArr.push(null);
			} else {
				var comboStore = new Ext.data.Store({		
							fields: ['transID', 'transName'],
							data:appItems[i].transVal
						});
				
				storeArr.push(comboStore);
			}
		}
		return storeArr;
	},
	//可配置搜索
	cfgSearchAct: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_HD_MANAGER_COM.TZ_HD_MANAGER_STD.TZ_GD_HDCFG_VW',
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
	//最近活动
	showCurrentActivity: function(btn){
			var dt = Ext.Date.add(new Date(), Ext.Date.DAY, -7);
		  var dtStr = Ext.Date.format(dt, 'Y-m-d');
			var store = btn.findParentByType("grid").store;
			store.tzStoreParams = '{"cfgSrhId":"TZ_HD_MANAGER_COM.TZ_HD_MANAGER_STD.TZ_GD_HDCFG_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'","TZ_START_DT-operator": "04","TZ_START_DT-value": "'+ dtStr+'"}}';
			store.load();
	},
	//历史活动
	showHistoryActivity: function(btn){
		  var dt = new Date();
		  var dtStr = Ext.Date.format(dt, 'Y-m-d');
			var store = btn.findParentByType("grid").store;
			store.tzStoreParams = '{"cfgSrhId":"TZ_HD_MANAGER_COM.TZ_HD_MANAGER_STD.TZ_GD_HDCFG_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'","TZ_START_DT-operator": "05","TZ_START_DT-value": "'+ dtStr+'"}}';
			store.load();
	},
	//全部活动
	showAllActivity: function(btn){
			var store = btn.findParentByType("grid").store;
			store.tzStoreParams = '{"cfgSrhId":"TZ_HD_MANAGER_COM.TZ_HD_MANAGER_STD.TZ_GD_HDCFG_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}';
			store.load();
	},
  //发布选中的内容
  releaseSelList:function(btn){
       //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
				Ext.Msg.alert("提示","请选择要发布的记录");   
				return;
	   }else{
			Ext.MessageBox.confirm('确认', '您将发布选中的内容?', function(btnId){
				if(btnId == 'yes'){
					for(var i = 0;i < selList.length;i++){
				   		selList[i].set("releaseOrUndo","Y");
				  }
				  var tzParams = this.submitContentParams("P","发布成功");
				}
			},this);
	   }
  },
  //撤销发布选中的内容
  UndoSelList:function(btn){
       //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要撤销发布的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您将撤销发布选中的内容?', function(btnId){
				if(btnId == 'yes'){
					for(var i = 0;i < selList.length;i++){
				   		selList[i].set("releaseOrUndo","N");
				   	}
				   	var tzParams = this.submitContentParams("P","撤销发布成功");
				}
			},this);
	   }
  },
    //置顶或者取消置顶
  topOrUndo:function(view,t,rowIndex){
  	var msg="";
		var record = view.findParentByType("grid").store.getAt(rowIndex);
		if(record.get("topOrUndo") != "0"){
			record.set("topOrUndo", "0");
			msg = "撤销置顶成功";
    }else{
					record.set("topOrUndo", "TOP");
					msg = "置顶成功";
    }
    var tzParams = this.submitContentParams("T",msg);
        //record.commit();
	},
	//发布或者撤销发布
	releaseOrUndo:function(view,t,rowIndex){
		var msg="";
		var record = view.findParentByType("grid").store.getAt(rowIndex);
		if(record.get("releaseOrUndo") == "Y"){
			record.set("releaseOrUndo", "N");
			msg = "撤销发布成功";
    }else{
				record.set("releaseOrUndo", "Y");
				msg = "发布成功";
    }
    var tzParams = this.submitContentParams("P",msg);
    //record.commit();
  },
  //获取修改记录
  submitContentParams: function(clickTyp,msg){
		var comParams = "";
		var editJson = "";
		var store = this.getView().getStore();
		//修改记录
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				//editJson = Ext.JSON.encode(mfRecs[i].data);
				editJson = '{"ClickTyp":"'+clickTyp+'","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}else{
				//editJson = editJson + ','+Ext.JSON.encode(mfRecs[i].data);
				editJson = editJson + ',{"ClickTyp":"'+clickTyp+'","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}
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
		var removeRecs = store.getRemovedRecords();
		for(var i=0;i<removeRecs.length;i++){
			if(removeJson == ""){
				removeJson = Ext.JSON.encode(removeRecs[i].data);
			}else{
				removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
			}
		}
		if(removeJson != ""){
			if(comParams == ""){
				comParams = '"delete":[' + removeJson + "]";
			}else{
				comParams = comParams + ',"delete":[' + removeJson + "]";
			}
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_MANAGER_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(){
			store.reload();		   
		},msg,true,this);
	},
	onComRegClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
	onComRegSave:function(btn){
		var dataGrid = btn.findParentByType("grid");
		var store = dataGrid.getStore();
        if(store.getRemovedRecords().length>0 || store.getModifiedRecords().length>0){
            var tzParams = this.submitContentParams("D","保存成功");
        };
        if(btn.name=="ensure"){
            this.getView().close();
        }
    },
});
