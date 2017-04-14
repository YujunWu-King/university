Ext.define('KitchenSink.view.content.artMg.artTreeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.artTreeController',
    //关闭按钮
    onCloseButton:function(btn){
        var panel=btn.findParentByType("panel");
        panel.close();
    },
	
	copySelList:function(btn){
		//选中行
	   var refs = this.getReferences(),
			dataGrid = refs.artListGrid;
	   var selList = dataGrid.getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要复制的记录");   
			return;
	   }else{
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ART_MG_COM"]["TZ_CHAN_SEL_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CHAN_SEL_STD，请检查配置。');
				return;
			}
			var win = this.lookupReference('channelTreeWindow');
			if (!win) {
				Ext.syncRequire(className);
				ViewClass = Ext.ClassManager.get(className);
				//新建类
				win = new ViewClass();
				this.getView().add(win);
			}
			//操作类型设置为新增
			win.actType = "update";
			//console.log(selList);
			win.artRecs = selList;
			//模板信息表单
			win.show();
		}
	},
	
	onShooseChannelEnsure:function(btn){
		var msg = "复制成功";
		var panel = btn.findParentByType("panel");
		console.log(panel.artRecs);
		var channelId = panel.channelId;
		if(channelId == "" || channelId == undefined){
			Ext.MessageBox.alert('提示', '请先选择栏目。');
			return;
		}else{
			//复制信息
			var copyJson = "";
			var comParams = "";
			//删除记录
			var artRecs = panel.artRecs;
			for(var i=0;i<artRecs.length;i++){
				if(copyJson == ""){
					copyJson = Ext.JSON.encode(artRecs[i].data);
					copyJson = '{"channelId":"'+channelId+'","data":'+Ext.JSON.encode(artRecs[i].data)+'}';
				}else{
					//copyJson = copyJson + ','+Ext.JSON.encode(artRecs[i].data);
					copyJson = copyJson + ',{"channelId":"'+channelId+'","data":'+Ext.JSON.encode(artRecs[i].data)+'}';
				}
			}
			if(copyJson != ""){
				if(comParams == ""){
					comParams = '"copy":[' + copyJson + "]";
					
				}else{
					comParams = comParams + ',"copy":[' + copyJson + "]";
				}
			}
			//提交参数
			var tzParams = '{"ComID":"TZ_ART_MG_COM","PageID":"TZ_ART_LIST_STD","OperateType":"copy","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(){
				//store.reload();		   
			},msg,true,this);
		}
		this.getView().close();
	},
	
	onShooseChannelClose:function(btn){
		this.getView().close();
	},
    treeItemClick: function( view , record, item, index, e, eOpts ){
        var refs = this.getReferences(),
		dataPanel = refs.artListGridPanel,
		dataGrid = refs.artListGrid,
		columnId=record.data.id,
		title = record.data.text;
		
		dataPanel.columnId = columnId;
		//----------过滤 "活动内容"
		/*
		var coluType=record.data.coluType;
		var btn=dataPanel.down("toolbar").down("button[name='add']");
		if(coluType!=undefined&&coluType=="D"){
			btn.setDisabled(true);
		}
		else{
			btn.setDisabled(false);
		}*/
		//--------------
        if(record.parentNode){
            title = record.parentNode.data.text + " - " + title;
        }

        dataPanel.setTitle(title);
        dataGrid.store.columnId=columnId;
        dataGrid.store.tzStoreParams = '{"cfgSrhId":"TZ_ART_MG_COM.TZ_ART_LIST_STD.TZ_GD_CONTENT_V","condition":{"TZ_COLU_ID-operator":"01","TZ_COLU_ID-value":"'+columnId+'"}}';
        dataGrid.store.load();
    },
	channelTreeItemClick: function( view , record, item, index, e, eOpts ){
		var panel = view.findParentByType("panel").findParentByType("panel");
		panel.channelId = record.data.id;
    },
	//查询
    cfgSearch: function(btn){
    	//var columId = btn.findParentByType('toolbar').child('combobox').getValue();
		var panel = btn.findParentByType("panel").findParentByType("panel");
		console.log(panel);
		var columnId = panel.columnId;
		if(columnId == "" || columnId == undefined){
			Ext.MessageBox.alert('提示', '请先选择栏目。');
			return;
		}
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_ART_MG_COM.TZ_ART_LIST_STD.TZ_GD_CONTENT_V',
			condition:
			{
				"TZ_COLU_ID": columnId
			},
			callback: function(seachCfg){
				var store = panel.child("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
	//添加内容;
    addArt: function(btn) {
    	//var contentGrid = this.getView();
		var refs = this.getReferences()
		dataGrid = refs.artListGrid;
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ART_MG_COM"]["TZ_ART_INFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ART_INFO_STD，请检查配置。');
			return;
		}
		  
	    //栏目;
	    var panel = btn.findParentByType("panel").findParentByType("panel");
		var columnId = panel.columnId;

	    if(columnId == "" || columnId == undefined){
			Ext.MessageBox.alert('提示', '请先选择栏目。');
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
		
		cmp = new ViewClass({"coluId":columnId});

    	cmp.on('afterrender',function(panel){
				var form = panel.child("form").getForm();
				
				//参数
				var tzParams = '{"ComID":"TZ_ART_MG_COM","PageID":"TZ_ART_INFO_STD","OperateType":"QF","comParams":{"artId":"","coluId":"'+columnId+'"}}';
				
				//加载数据
				Ext.tzLoad(tzParams,function(responseData){
						var formData = responseData.formData;
						var siteId = formData.siteId;
						form.findField("siteId").setValue(formData.siteId);
						form.findField("coluId").setValue(formData.coluId);
						form.findField("colus").setValue(formData.colus);
						form.findField("siteType").setValue(formData.siteType);
						form.findField("saveImageAccessUrl").setValue(formData.saveImageAccessUrl);
						form.findField("saveAttachAccessUrl").setValue(formData.saveAttachAccessUrl);
						
						/*控制自定义字段的隐藏显示*/
						var tztxt1Enabled = formData.tztxt1Enabled;
						var tztxt1Label = formData.tztxt1Label;
						if(tztxt1Enabled=="Y"){
							form.findField("tztxt1").show();
							form.findField("tztxt1").setFieldLabel(tztxt1Label);
						}else{
							form.findField("tztxt1").hide();
						}
						
						var tztxt2Enabled = formData.tztxt2Enabled;
						var tztxt2Label = formData.tztxt2Label;
						if(tztxt2Enabled=="Y"){
							form.findField("tztxt2").show();
							form.findField("tztxt2").setFieldLabel(tztxt2Label);
						}else{
							form.findField("tztxt2").hide();
						}
						
						var tztxt3Enabled = formData.tztxt3Enabled;
						var tztxt3Label = formData.tztxt3Label;
						if(tztxt3Enabled=="Y"){
							form.findField("tztxt3").show();
							form.findField("tztxt3").setFieldLabel(tztxt3Label);
						}else{
							form.findField("tztxt3").hide();
						}
						
						var tztxt4Enabled = formData.tztxt4Enabled;
						var tztxt4Label = formData.tztxt4Label;
						if(tztxt4Enabled=="Y"){
							form.findField("tztxt4").show();
							form.findField("tztxt4").setFieldLabel(tztxt4Label);
						}else{
							form.findField("tztxt4").hide();
						}
						
						var tzlong1Enabled = formData.tzlong1Enabled;
						var tzlong1Label = formData.tzlong1Label;
						if(tzlong1Enabled=="Y"){
							form.findField("tzlong1").show();
							form.findField("tzlong1").setFieldLabel(tzlong1Label);
						}else{
							form.findField("tzlong1").hide();
						}
						
						var tzlong2Enabled = formData.tzlong2Enabled;
						var tzlong2Label = formData.tzlong2Label;
						if(tzlong2Enabled=="Y"){
							form.findField("tzlong2").show();
							form.findField("tzlong2").setFieldLabel(tzlong2Label);
						}else{
							form.findField("tzlong2").hide();
						}
						
						var tzlong3Enabled = formData.tzlong3Enabled;
						var tzlong3Label = formData.tzlong3Label;
						if(tzlong3Enabled=="Y"){
							form.findField("tzlong3").show();
							form.findField("tzlong3").setFieldLabel(tzlong3Label);
						}else{
							form.findField("tzlong3").hide();
						}
						
						var tzdate1Enabled = formData.tzdate1Enabled;
						var tzdate1Label = formData.tzdate1Label;
						if(tzdate1Enabled=="Y"){
							form.findField("tzdate1").show();
							form.findField("tzdate1").setFieldLabel(tzdate1Label);
						}else{
							form.findField("tzdate1").hide();
						}
						
						var tzdate2Enabled = formData.tzdate2Enabled;
						var tzdate2Label = formData.tzdate2Label;
						if(tzdate2Enabled=="Y"){
							form.findField("tzdate2").show();
							form.findField("tzdate2").setFieldLabel(tzdate2Label);
						}else{
							form.findField("tzdate2").hide();
						}
						
						//新建时，隐藏听众列表
						form.findField("AudList").hide(); 
						
						//发布对象
						var siteType = form.findField("siteType").getValue();
					
						if(siteType=="A" || siteType == "B"){
							var pubAud = panel.down('fieldset[name=pubAud]');
//							pubAud.setHidden(true);
						}
						//栏目类型
						var coluType = formData.coluType;
						if(coluType=="D"){	
							var actEdit = panel.down('button[name=editAct]');
							actEdit.setHidden(false);
						}else{
							
						}
				});
			});
    	
    	cmp.on('close',function(panel){
            try{
                dataGrid.store.reload();
            }catch (e){
                /*do nothing*/
            }
        });
			
		tab = contentPanel.add(cmp);     
		tab.on(Ext.tzTabOn(tab,this.getView(),cmp));	
		contentPanel.setActiveTab(tab);
	
		Ext.resumeLayouts(true);
		if (cmp.floating) {
         cmp.show();
		}
    },
    //删除选中的内容
    deleteSelList:function(btn){
       //选中行
	   var refs = this.getReferences(),
			dataGrid = refs.artListGrid;
	   var selList = dataGrid.getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var userStore = dataGrid.store;
				   userStore.remove(selList);
				}
			},this);
	   }
    },
    saveContentList:function(btn){
		var refs = this.getReferences(),
			dataGrid = refs.artListGrid;
		var store = dataGrid.getStore();
        if(store.getRemovedRecords().length>0 || store.getModifiedRecords().length>0){
            var tzParams = this.submitContentParams("D","保存成功");
        };
        if(btn.name=="ensure"){
            this.getView().close();
        }
    },
    //发布选中的内容
    releaseSelList:function(btn){
       //选中行
	   var refs = this.getReferences(),
			dataGrid = refs.artListGrid;
	   var selList = dataGrid.getSelectionModel().getSelection();
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
				  var tzParams = this.publishContentParams("P","发布成功");
				}
			},this);
	   }
    },
    //撤销发布选中的内容
    UndoSelList:function(btn){
       //选中行
	   var refs = this.getReferences(),
			dataGrid = refs.artListGrid;
	   var selList = dataGrid.getSelectionModel().getSelection();
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
    //编辑内容
	editSelArt:function(){
	
		var refs = this.getReferences(),
			dataGrid = refs.artListGrid;
		 //选中行
	    var selList = dataGrid.getSelectionModel().getSelection();
	    //选中行长度
	    var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	    }
		var articleId= selList[0].get("articleId");
	    var coluId= selList[0].get("columnId");
	    //显示活动信息编辑页面
	    this.editArticleByID(dataGrid,articleId,coluId);
	},
	//编辑内容
	editArt:function(view, rowIndex){
		var contentGrid = view.findParentByType("grid");
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		 //内容ID
	    var articleId = selRec.get("articleId");
	    //栏目;
		var columnId =selRec.get("columnId");
	    //显示活动信息编辑页面
	    this.editArticleByID(contentGrid,articleId,columnId);
	   
	},
	editArticleByID: function(contentGrid,articleId,coluId){
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ART_MG_COM"]["TZ_ART_INFO_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ART_INFO_STD，请检查配置。');
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
	
			cmp = new ViewClass({"coluId":coluId});
			//操作类型设置为更新
			cmp.actType = "update";
			
			cmp.on('afterrender',function(panel){
				//内容表单信息;
				var form = panel.child('form').getForm();
				//附件集;
				var attachGrid = panel.down('grid[name=attachmentGrid]');
				
				//参数
				var tzParams = '{"ComID":"TZ_ART_MG_COM","PageID":"TZ_ART_INFO_STD","OperateType":"QF","comParams":{"artId":"'+articleId+'","coluId":"'+coluId+'"}}';
				
				//加载数据
				Ext.tzLoad(tzParams,function(responseData){
					//活动基本信息
					var formData = responseData.formData;
					form.setValues(formData);
					var titleStyle = form.findField("titleStyleView").getValue();
					if (titleStyle == "HOT"){
						var artTitle = form.findField("artTitle").getValue();
						var styleTitle = artTitle+"<span><font color ='#6633CC'> HOT</font></span>";
						panel.down('#titleView').getEl().setHtml(styleTitle);
					}
					if (titleStyle == "NEW"){
						var artTitle = form.findField("artTitle").getValue();
						var styleTitle = artTitle+"<span><font color ='#bb1914'> NEW</font></span>";
						panel.down('#titleView').getEl().setHtml(styleTitle);
					}
					var publishStatus = form.findField("publishStatus").getValue();
					var siteId = form.findField("siteId").getValue();
					if (publishStatus == "Y"){
						form.findField("publishStatusDesc").setValue("已发布");
						
						form.findField("publishStatusDesc").setValue("已发布");
				  	var viewUrl = formData.publishUrl;
				  	form.findField("publishUrl").setValue(viewUrl);
					}
					
					if (publishStatus == "N"){
						form.findField("publishStatusDesc").setValue("未发布");
					}
					/*控制自定义字段的隐藏显示*/
					var tztxt1Enabled = formData.tztxt1Enabled;
					var tztxt1Label = formData.tztxt1Label;
					if(tztxt1Enabled=="Y"){
						form.findField("tztxt1").show();
						form.findField("tztxt1").setFieldLabel(tztxt1Label);
					}else{
						form.findField("tztxt1").hide();
					}
					
					var tztxt2Enabled = formData.tztxt2Enabled;
					var tztxt2Label = formData.tztxt2Label;
					if(tztxt2Enabled=="Y"){
						form.findField("tztxt2").show();
						form.findField("tztxt2").setFieldLabel(tztxt2Label);
					}else{
						form.findField("tztxt2").hide();
					}
					
					var tztxt3Enabled = formData.tztxt3Enabled;
					var tztxt3Label = formData.tztxt3Label;
					if(tztxt3Enabled=="Y"){
						form.findField("tztxt3").show();
						form.findField("tztxt3").setFieldLabel(tztxt3Label);
					}else{
						form.findField("tztxt3").hide();
					}
					
					var tztxt4Enabled = formData.tztxt4Enabled;
					var tztxt4Label = formData.tztxt4Label;
					if(tztxt4Enabled=="Y"){
						form.findField("tztxt4").show();
						form.findField("tztxt4").setFieldLabel(tztxt4Label);
					}else{
						form.findField("tztxt4").hide();
					}
					
					var tzlong1Enabled = formData.tzlong1Enabled;
					var tzlong1Label = formData.tzlong1Label;
					if(tzlong1Enabled=="Y"){
						form.findField("tzlong1").show();
						form.findField("tzlong1").setFieldLabel(tzlong1Label);
					}else{
						form.findField("tzlong1").hide();
					}
					
					var tzlong2Enabled = formData.tzlong2Enabled;
					var tzlong2Label = formData.tzlong2Label;
					if(tzlong2Enabled=="Y"){
						form.findField("tzlong2").show();
						form.findField("tzlong2").setFieldLabel(tzlong2Label);
					}else{
						form.findField("tzlong2").hide();
					}
					
					var tzlong3Enabled = formData.tzlong3Enabled;
					var tzlong3Label = formData.tzlong3Label;
					if(tzlong3Enabled=="Y"){
						form.findField("tzlong3").show();
						form.findField("tzlong3").setFieldLabel(tzlong3Label);
					}else{
						form.findField("tzlong3").hide();
					}
					
					var tzdate1Enabled = formData.tzdate1Enabled;
					var tzdate1Label = formData.tzdate1Label;
					if(tzdate1Enabled=="Y"){
						form.findField("tzdate1").show();
						form.findField("tzdate1").setFieldLabel(tzdate1Label);
					}else{
						form.findField("tzdate1").hide();
					}
					
					var tzdate2Enabled = formData.tzdate2Enabled;
					var tzdate2Label = formData.tzdate2Label;
					if(tzdate2Enabled=="Y"){
						form.findField("tzdate2").show();
						form.findField("tzdate2").setFieldLabel(tzdate2Label);
					}else{
						form.findField("tzdate2").hide();
					}
					
					//听众赋值20170209
				     var audIDList=formData.AudID;
		             var audNameList=formData.AudName;
		             var oprIdArray=new Array();
		             var i=0,j=0;
		                for(j=0;j<audIDList.length;j++){
		                    var TagModel=new KitchenSink.view.content.artMg.tagModel();
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
					
					var siteType = form.findField("siteType").getValue();
				
					if(siteType=="A" || siteType == "B"){
						var pubAud = panel.down('fieldset[name=pubAud]');
						pubAud.setHidden(true);
					}
					
					//栏目类型
					var coluType = formData.coluType;
					if(coluType=="D"){	
						var actEdit = panel.down('button[name=editAct]');
						actEdit.setHidden(false);
					}else{
					}
					
					panel.down('image[name=titileImage]').setSrc(TzUniversityContextPath + panel.down('hiddenfield[name=titleImageUrl]').getValue());	
					//附件集
					var tzStoreParams = '{"artId":"'+articleId+'","gridTyp":"FJ"}';
					attachGrid.store.tzStoreParams = tzStoreParams;
					attachGrid.store.load();			

					//图片集;
					var picDataView = panel.down('dataview[name=picView]');
					var tzStoreParams = '{"artId":"'+articleId+'","gridTyp":"TPJ"}';
					picDataView.store.tzStoreParams = tzStoreParams;
					picDataView.store.load();
					
				});
				
			});
			
			cmp.on('close',function(panel){
	            try{
	                contentGrid.store.reload();
	            }catch (e){
	                /*do nothing*/
	            }
	        });
			
			tab = contentPanel.add(cmp);     
			tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
			contentPanel.setActiveTab(tab);
	
			Ext.resumeLayouts(true);
	
			if (cmp.floating) {
				cmp.show();
			}
		},
	//编辑内容
	deleteArt:function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '确定要删除改内容吗?', function(btnId){
			if(btnId == 'yes'){
				view.findParentByType("grid").store.removeAt(rowIndex);
			}
		},this);
	},
	//发布或者撤销发布
	releaseOrUndo:function(view,t,rowIndex){
		var msg = "";
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
	//置顶或者取消置顶
	topOrUndo:function(view,t,rowIndex){
  	var msg;
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
    //获取修改记录
	submitContentParams: function(clickTyp,msg){
		var comParams = "";
		var editJson = "";
		var refs = this.getReferences(),
			dataGrid = refs.artListGrid;
		var store = dataGrid.getStore();
		//修改记录
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				//editJson = Ext.JSON.encode(mfRecs[i].data);
				editJson = '{"ClickTyp":"'+clickTyp+'","data":'+ Ext.JSON.encode(mfRecs[i].data)+'}';
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
		var tzParams = '{"ComID":"TZ_ART_MG_COM","PageID":"TZ_ART_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
		Ext.tzSubmit(tzParams,function(){
			store.reload();		   
		},msg,true,this);
	},
	//获取修改记录
	publishContentParams: function(clickTyp,msg){
		var comParams = "";
		var editJson = "";
		var refs = this.getReferences(),
			dataGrid = refs.artListGrid;
		var store = dataGrid.getStore();
		//选中的记录
		var mfRecs = dataGrid.getSelectionModel().getSelection();
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				//editJson = Ext.JSON.encode(mfRecs[i].data);
				editJson = '{"ClickTyp":"'+clickTyp+'","data":'+ Ext.JSON.encode(mfRecs[i].data)+'}';
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
		var tzParams = '{"ComID":"TZ_ART_MG_COM","PageID":"TZ_ART_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
		Ext.tzSubmit(tzParams,function(){
			store.reload();		   
		},msg,true,this);
	},
	onPageClose: function(btn){
		//关闭窗口
		this.getView().close();
	}
});