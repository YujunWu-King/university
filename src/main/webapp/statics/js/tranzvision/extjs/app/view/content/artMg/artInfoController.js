Ext.define('KitchenSink.view.content.artMg.artInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.artInfoController', 
	requires: [
	           'KitchenSink.view.content.artMg.tagModel'
	       ],
	onArtSave: function(){
		//内容表单
		var form = this.getView().child("form").getForm();
		
		if (form.isValid()) {
			//获取内容信息参数
			var tzParams = this.getArtInfoParams();
			var comView = this.getView();
			var actType = comView.actType;
			var artId = form.findField("artId").getValue();
			if(actType=="update" && (artId=="" || typeof(artId) == "undefined" )){
					Ext.Msg.alert("提示","保存出错");
			}else{
					Ext.tzSubmit(tzParams,function(responseData){
					
					if(actType=="add"){
						comView.actType = "update";	
				  	form.findField("artId").setValue(responseData.artId);
				  	comView.commitChanges(comView);
				  }
			},"",true,this);
			}
	
		}else{
				Ext.Msg.alert("提示","信息填写不完整或填写有误");
		}
		
	},
	onArtEnsure: function(){
		//内容表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取内容信息参数
			var tzParams = this.getArtInfoParams();
			var comView = this.getView();
			var actType = comView.actType;
			var artId = form.findField("artId").getValue();
			if(actType=="update" && (artId=="" || typeof(artId) == "undefined")){
					Ext.Msg.alert("提示","保存出错");
			}else{
					Ext.tzSubmit(tzParams,function(responseData){
							comView.close();
					},"",true,this);
			}
		}else{
				Ext.Msg.alert("提示","请填写必填项");
		}
		
	},
	editAct:function(){
		//内容表单
		var me = this;
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取内容信息参数
			var tzParams = this.getArtInfoParams();
			var comView = this.getView();
			var actType = comView.actType;
			var artId = form.findField("artId").getValue();
			if(actType=="update" && (artId=="" || typeof(artId) == "undefined")){
					Ext.Msg.alert("提示","保存出错");
			}else{
					Ext.tzSubmit(tzParams,function(responseData){
							/*打开活动窗口*/
							
							me.editActivityIdByID(responseData.artId);
							comView.close();
					},"",true,this);
			}
		}else{
				Ext.Msg.alert("提示","请填写必填项");
		}
	},
	publishArt:function(){
		//内容表单
		var form = this.getView().child("form").getForm();
		var fbDt = form.findField("artNewsDT").getValue();
		var dt = new Date();
		var dtStr = Ext.Date.format(dt, "Y-m-d H:i"); 
		if( fbDt == "" ){
			form.findField("artNewsDT").setValue(dtStr);
		}

		if (form.isValid()) {
			//获取内容信息参数
			var comView = this.getView();
			var actType = comView.actType;
			var artId = form.findField("artId").getValue();
			var siteId = form.findField("siteId").getValue();
			var columnId = form.findField("coluId").getValue();
			
			var editType=form.findField("type").getValue();
			
			fbDt = form.findField("artNewsDT").getValue();
			
			if(actType=="update" && (artId=="" || typeof(artId) == "undefined" )){
					Ext.Msg.alert("提示","保存出错");
			}else if(fbDt == ""){
				  Ext.Msg.alert("提示","发布时间必填");
			}else{
				  form.findField("publishStatus").setValue("Y");
				  form.findField("publishClick").setValue("Y");
				  var tzParams = this.getArtInfoParams();
				  form.findField("publishClick").setValue("");
					Ext.tzSubmit(tzParams,function(responseData){
					
						if(actType=="add"){
							comView.actType = "update";	
					  	form.findField("artId").setValue(responseData.artId);
					  	
					  }
				  	
				  	if(responseData.artId != ""){
				  		form.findField("publishStatusDesc").setValue("已发布");
				  		var viewUrl = responseData.publishUrl;
				  		form.findField("publishUrl").setValue(viewUrl);
				  	}
				  	
				  	comView.commitChanges(comView);
				  	
					},"发布成功",true,this);
			}
	
		}else{
				Ext.Msg.alert("提示","请填写必填项");
		}
	},
	unpublishArt:function(){
		//内容表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取内容信息参数
			
			var comView = this.getView();
			var actType = comView.actType;
			var artId = form.findField("artId").getValue();
			if(actType=="update" && (artId=="" || typeof(artId) == "undefined" )){
					Ext.Msg.alert("提示","保存出错");
			}else{
				
				  form.findField("publishStatus").setValue("N");
				  form.findField("publishClick").setValue("Y");
				  var tzParams = this.getArtInfoParams();
				  form.findField("publishClick").setValue("");
					Ext.tzSubmit(tzParams,function(responseData){
					
						if(actType=="add"){
							comView.actType = "update";	
					  	form.findField("artId").setValue(responseData.artId);
					  }
					  
					  if(responseData.artId != ""){
				  		form.findField("publishStatusDesc").setValue("未发布");
				  		form.findField("publishUrl").setValue("");
				  	}
				  	
				  	comView.commitChanges(comView);
				  	
					},"撤销成功",true,this);
			}
	
		}else{
				Ext.Msg.alert("提示","请填写必填项");
		}
	},
	upArt:function(){
		//内容表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取内容信息参数
			
			var comView = this.getView();
			var actType = comView.actType;
			var artId = form.findField("artId").getValue();
			if(actType=="update" && (artId=="" || typeof(artId) == "undefined" )){
					Ext.Msg.alert("提示","保存出错");
			}else{
				  form.findField("upArtClick").setValue("Y");
				  var tzParams = this.getArtInfoParams();
				  form.findField("upArtClick").setValue("");
					Ext.tzSubmit(tzParams,function(responseData){
					
						if(actType=="add"){
							comView.actType = "update";	
					  	form.findField("artId").setValue(responseData.artId);
					  	comView.commitChanges(comView);
					  }
			},"置顶成功",true,this);
			}
	
		}else{
				Ext.Msg.alert("提示","请填写必填项");
		}
	},
	viewArt:function(){
		//内容表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取内容信息参数
			var tzParams = this.getArtInfoParams();
			var comView = this.getView();
			var actType = comView.actType;
			var artId = form.findField("artId").getValue();
			var siteId = form.findField("siteId").getValue();
			var columnId = form.findField("coluId").getValue();
			if(actType=="update" && (artId=="" || typeof(artId) == "undefined" )){
					Ext.Msg.alert("提示","保存出错");
			}else{
					Ext.tzSubmit(tzParams,function(responseData){
					
					if(actType=="add"){
						comView.actType = "update";	
				  	form.findField("artId").setValue(responseData.artId);
				  	comView.commitChanges(comView);
				  }
				  
				  window.open(responseData.viewUrl);

			},"",true,this);
			}
	
		}else{
				Ext.Msg.alert("提示","请填写必填项");
		}
	}, 
	onArtClose: function(){
		this.getView().close();
	},
	getArtInfoParams: function(){
		//活动信息表单
		var form = this.getView().child("form").getForm();
		//活动信息标志
		var actType = this.getView().actType;
		//活动ID;
		var artId = form.findField("artId").getValue();
		
		//编辑类型
		var editType=form.findField("type").getValue();
		
		//更新操作参数
		var comParams = "";
		//新增
		if(actType == "add"){
			comParams = '"add":[{"typeFlag":"ACTINFO","data":'+Ext.JSON.encode(form.getValues())+'}]';
		}
		//修改json字符串
	
		var editJson = "";
		if(actType == "update"){
			editJson = '{"typeFlag":"ACTINFO","data":'+Ext.JSON.encode(form.getValues())+'}';
		}
		
		
    
		
		//开始附件集;
		//var attachmentGrid = Ext.getCmp('attachmentGrid');
		var attachmentGrid =this.getView().down('grid[name=attachmentGrid]');
		//附件集stroe
		var attachmentGridstore = attachmentGrid.getStore();
		
		//修改记录
		var mfAttachmentGridRecs = attachmentGridstore.getModifiedRecords(); 
		for(var i=0;i<mfAttachmentGridRecs.length;i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"ARTATTACHINFO","data":'+Ext.JSON.encode(mfAttachmentGridRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"ARTATTACHINFO","data":'+Ext.JSON.encode(mfAttachmentGridRecs[i].data)+'}';
			}
		}

		
		//删除json字符串
		var removeJson = "";
		//删除记录
		var attachmentGridRemoveRecs = attachmentGridstore.getRemovedRecords();
		for(var i=0;i<attachmentGridRemoveRecs.length;i++){
			if(removeJson == ""){
				removeJson = '{"typeFlag":"ARTATTACHINFO","artId":"'+artId+'","data":'+Ext.JSON.encode(attachmentGridRemoveRecs[i].data)+'}';
			}else{
				removeJson = removeJson + ',{"typeFlag":"ARTATTACHINFO","artId":"'+artId+'","data":'+Ext.JSON.encode(attachmentGridRemoveRecs[i].data)+'}';
	  	}
		}
		//结束附件集;
		
		
		
		//图片集;
		var dataStr="";
		var numberLen = 0;
		var picsDataView =this.getView().down('dataview[name=picView]');
		var picsDataViewStore = picsDataView.getStore();
		var picDataViewArr = [];
		picsDataViewStore.each( function(mdRec) {
      // picDataViewArr.push(mdRec.data);
      numberLen = numberLen + 1;
      
      if(dataStr == ""){
      	dataStr = '"'+("data"+ numberLen) +'":'+Ext.JSON.encode(mdRec.data);
				//editJson = '{"typeFlag":"ARTTPJ","data":'+Ext.JSON.encode(mdRec.data)+'}';
			}else{
				dataStr = dataStr+',"'+("data"+ numberLen) +'":'+Ext.JSON.encode(mdRec.data);
				//editJson = editJson + ',{"typeFlag":"ARTTPJ","data":'+Ext.JSON.encode(mdRec.data)+'}';
			}
    });
    
    if(dataStr == ""){
			dataStr = '"data0": "deleteAll"';
		}
		
    if(dataStr != ""){
	    if(editJson == ""){
					editJson = '{"typeFlag":"ARTTPJ",'+dataStr+'}';
			}else{
					editJson = editJson + ',{"typeFlag":"ARTTPJ",'+dataStr+'}';
			}
		}
		
		
		//update;
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		
		
		//delete;
		if(removeJson != ""){
			if(comParams == ""){
				comParams = '"delete":[' + removeJson + "]";
			}else{
				comParams = comParams + ',"delete":[' + removeJson + "]";
			}
		}
		//结束报名信息项;
		
		//提交参数
		var tzParams = '{"ComID":"TZ_ART_MG_COM","PageID":"TZ_ART_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	onPicEditClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	},
	onPicEditEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			var sysFileName = form.findField("sysFileName").getValue();
			var caption = form.findField("caption").getValue();
			var picURL = form.findField("picURL").getValue();
			var dataView = win.findParentByType("artInfo").down('dataview[name=picView]');
			var picStore = dataView.getStore();
			var i = 0;
			for(i=0; i<picStore.getCount(); i++){
				var sFileName = picStore.getAt(i).data.sysFileName;
				if (sFileName == sysFileName ){
					picStore.getAt(i).data.caption = caption;
					picStore.getAt(i).data.picURL = picURL;
				}
			}
			//重置表单
			form.reset();
			//关闭窗口
			win.close();
			dataView.refresh();
		}	
	},
	deleteArtAttenment: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	deleteArtAttenments: function(){
	   //选中行
	   var attachmentGrid = this.lookupReference('attachmentGrid');
	   var selList = attachmentGrid.getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var store = attachmentGrid.store;
				   store.remove(selList);
				}												  
			},this);   
	   }
	},
	addHotStyle:function(btn){
		var form = this.getView().child("form").getForm();
		var artTitle = form.findField("artTitle").getValue();
		var styleTitle = artTitle+"<span><font color ='#6633CC'> HOT</font></span>";
		form.findField("titleStyleView").setValue("HOT");
		btn.findParentByType('form').findParentByType('panel').down('#titleView').getEl().setHtml(styleTitle);
	},
	addNewStyle:function(btn){
		var form = this.getView().child("form").getForm();
		var artTitle = form.findField("artTitle").getValue();
		var styleTitle = artTitle+"<span><font color ='#bb1914'> NEW</font></span>";
		form.findField("titleStyleView").setValue("NEW");
		btn.findParentByType('form').findParentByType('panel').down('#titleView').getEl().setHtml(styleTitle);
	},
	clearStyle:function(btn){
		var form = this.getView().child("form").getForm();
		form.findField("titleStyleView").setValue("");
		btn.findParentByType('form').findParentByType('panel').down('#titleView').getEl().setHtml("");
	},
	addAudience:function(btn){
        var arrAddAudience=[];
        var addAudirec;
        var arrAddAudiValue=[];
        Ext.tzShowPromptSearch({
            recname: 'TZ_AUDIENCE_VW',
            searchDesc: '选择听众',
            maxRow:50,
            condition:{
                presetFields:{
                	TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    }
                },
                srhConFields:{
                    TZ_AUD_NAME:{
                        desc:'听众名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_AUD_ID:'听众ID',
                TZ_AUD_NAME: '听众名称',
                //TZ_ORG_CODE:'所属部门',
                ROW_ADDED_DTTM:'创建时间'
                // ROW_LASTMANT_DTTM:'修改时间'
            },
            multiselect: true,
            callback: function(selection){
                if(selection.length>0){
                    for(j=0;j<selection.length;j++){
                        addAudirec="";
                        addAudirec = {"id":selection[j].data.TZ_AUD_ID,"desc":selection[j].data.TZ_AUD_NAME};
                        arrAddAudience.push(addAudirec);
                        arrAddAudiValue.push(selection[j].data.TZ_AUD_ID);
                    };
//                    var znxBulkDetForm = btn.findParentByType('panel').child('form');
//                    //var storereceive=znxBulkDetForm.child('tagfield[reference="recever"]').getStore();
//                    //storereceive.add(arrAddAudience);
//                    //znxBulkDetForm.down('tagfield[reference="recever"]').removeListener('change','receverChange');
//                    znxBulkDetForm.child('tagfield[reference="recever"]').addValue(arrAddAudiValue);
//                    //znxBulkDetForm.down('tagfield[reference="recever"]').addListener('change','receverChange');
//
//                    znxBulkDetForm.down('button[reference=clearAllBtn]').disabled=false;
//                    znxBulkDetForm.down('button[reference=clearAllBtn]').removeCls('x-item-disabled x-btn-disabled')
                }
            }
        })
    },
  //查看听众
    searchListeners:function(btn){
        Ext.tzShowPromptSearch({
            recname: 'TZ_AUDIENCE_VW',
            searchDesc: '选择听众',
            maxRow:20,
            condition:{
                presetFields:{
                	TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    }
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
                TZ_AUD_NAME: '听众名称',
                //TZ_ORG_CODE:'所属部门',
                ROW_ADDED_DTTM:'创建时间'
                // ROW_LASTMANT_DTTM:'修改时间'
            },
            multiselect: true,
            callback: function(selection){
                var oprIdArray=new Array();
                var i=0;
                var listenersList=btn.findParentByType("form").getForm().findField("AudList");
                var j= 0,k=0;
                for (k=0;k<listenersList.valueCollection.items.length;k++){
                    var listData=listenersList.valueCollection.items[k];
                    
                    var TagModel=new KitchenSink.view.content.artMg.tagModel();
                    var audName = listData.data.tagName;
                    var audId=listData.data.tagId;
                    TagModel.set('tagId',audId);
                    TagModel.set('tagName',audName);
                    oprIdArray[i]=TagModel;
                    i++;
                }
                for(j=0;j<selection.length;j++){
                    var TagModel=new KitchenSink.view.content.artMg.tagModel();
                    var audName = selection[j].data.TZ_AUD_NAME;
                    var audId=selection[j].data.TZ_AUD_ID;
                    TagModel.set('tagId',audId);
                    TagModel.set('tagName',audName);
                    oprIdArray[i]=TagModel;
                    i++;
                };
                btn.findParentByType("form").getForm().findField("AudList").setValue(oprIdArray);
            }
        })
    },
	editActivityIdByID: function(activityId){
		Ext.tzSetCompResourses("TZ_HD_MANAGER_COM");
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
	}
});