Ext.define('KitchenSink.view.content.contentMg.contentController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.contentController', 

	//查询
    cfgSearch: function(btn){
    	var columId = btn.findParentByType('toolbar').child('combobox').getValue();
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_CONTENT_MG_COM.TZ_CONTENT_STD.TZ_GD_CONTENT_V',
			condition:
			{
				"TZ_COLU_ID": columId
			},
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
	//添加内容;
    addArt: function() {
    	var contentGrid = this.getView();
    	//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CONTENT_MG_COM"]["TZ_CONTENT_INF_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CONTENT_INF_STD，请检查配置。');
				return;
			}
		  
		  //栏目;
		  var lm = this.getView().down("combobox").getValue();
		  if(lm == "" || lm == undefined){
				Ext.MessageBox.alert('提示', '请先选择栏目。');
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
				var cmpForm = panel.child("form").getForm();
				//cmpForm.findField("coluId").setValue(lm);
				
				//参数
				var tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_CONTENT_INF_STD","OperateType":"QF","comParams":{"artId":"","coluId":"'+lm+'"}}';
				
				//加载数据
				Ext.tzLoad(tzParams,function(responseData){
						var formData = responseData.formData;
						cmpForm.findField("siteId").setValue(formData.siteId);
						cmpForm.findField("coluId").setValue(formData.coluId);
						cmpForm.findField("saveImageAccessUrl").setValue(formData.saveImageAccessUrl);
						cmpForm.findField("saveAttachAccessUrl").setValue(formData.saveAttachAccessUrl);
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
    //删除选中的内容
    deleteSelList:function(btn){
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
				   var userStore = this.getView().store;
				   userStore.remove(selList);
				}
			},this);
	   }
    },
    saveContentList:function(btn){
        var store = this.getView().store;
        if(store.getRemovedRecords().length>0){
            var tzParams = this.submitContentParams("D","保存成功");
        };
        if(btn.name=="ensure"){
            this.getView().close();
        }
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
    //编辑内容
	editSelArt:function(){
		var contentGrid = this.getView();
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
		 var articleId= selList[0].get("articleId");
	   var coluId= selList[0].get("columnId");
	   //显示活动信息编辑页面
	   this.editArticleByID(contentGrid,articleId,coluId);
	},
	//编辑内容
	editArt:function(view, rowIndex){
		var contentGrid = view.findParentByType("grid");
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //内容ID
	   var articleId = selRec.get("articleId");
	   
	   var coluId = view.findParentByType("grid").down("combobox").getValue();
		  if(coluId == "" || coluId == undefined){
				Ext.MessageBox.alert('提示', '请先选择栏目。');
				return;
			}
			
	   //显示活动信息编辑页面
	   this.editArticleByID(contentGrid,articleId,coluId);
	   
	},
	editArticleByID: function(contentGrid,articleId,coluId){
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CONTENT_MG_COM"]["TZ_CONTENT_INF_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CONTENT_INF_STD，请检查配置。');
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
				//内容表单信息;
				var form = panel.child('form').getForm();
				//附件集;
				var attachGrid = panel.down('grid[name=attachmentGrid]');
				
				//参数
				var tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_CONTENT_INF_STD","OperateType":"QF","comParams":{"artId":"'+articleId+'","coluId":"'+coluId+'"}}';
				
				//加载数据
				Ext.tzLoad(tzParams,function(responseData){
					//活动基本信息
					var formData = responseData.formData;
					form.setValues(formData);
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
		var store = this.getView().getStore();
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
		var tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_CONTENT_STD","OperateType":"U","comParams":{'+comParams+'}}';
    Ext.tzSubmit(tzParams,function(){
			store.reload();		   
		},msg,true,this);
	},
	//设置栏目，Mabc
	setChannel:function(){

		//栏目;
		  var lm = this.getView().down("combobox").getValue();
		  if(lm == "" || lm == undefined){
				Ext.MessageBox.alert('提示', '请先选择栏目。');
				return;
			}
		this.editColumnSkinByID(lm);
	},
	editColumnSkinByID: function(lm_id){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CONTENT_MG_COM"]["TZ_GD_ZDLM_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_ZDLM_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('channelWindow');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		//站点模板基本信息
		//var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		//var siteId = comSiteParams["siteId"];
		//模板集合表单信息;
		var form = win.child('form').getForm();
		
		//加载栏目模板
		/*
		var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
			recname: 'TZ_GD_SITECHL_V',
			condition:{
				TZ_JG_ID:{
					value:Ext.tzOrgID,
					operator:"01",
					type:"01"
				},
				TZ_TEMP_STATE:{
					value:'Y',
					operator:"01",
					type:"01"
				}
			},
			result:'TZ_TEMP_ID,TZ_TEMP_NAME'
		});
		form.findField("lm_mb").setStore(lm_mbStore);
		form.findField("lm_nrmb").setStore(lm_mbStore);
		*/
		//参数
		var tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_GD_ZDLM_STD","OperateType":"QF","comParams":{"templateId":"'+lm_id+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			//皮肤设置数据
			var formData = responseData.formData;
			form.setValues(formData);
			//form.setValues({siteId:siteId});
		});
        win.show();
	},
	onComRegClose: function(btn){
		//关闭窗口
		this.getView().close();
	}
});