Ext.define('KitchenSink.view.activity.activityInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.activityInfo', 
	requires: [
	           'KitchenSink.view.activity.tagModel'
	       ],
  previewPhoneArt: function() {
  	  //组件注册表单
			var form = this.getView().child("form").getForm();
		  var activityId = form.findField("activityId").getValue();
		  var siteId = form.findField("siteId").getValue();
		  var columnId = form.findField("coluId").getValue();
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_HD_MANAGER_COM"]["TZ_HD_SJ_PREVIEW"];
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
	      win.title = "手机版活动详情页预览地址";    
	    }
	    win.on('afterrender',function(panel){
	    	var codeForm = panel.child("form").getForm();
	    	 //参数
				var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_SJ_PREVIEW","OperateType":"QF","comParams":{"activityId":"'+activityId+'","siteId":"'+siteId+'","coluId":"'+columnId+'"}}';
				
				//加载数据
				Ext.tzLoad(tzParams,function(responseData){
					var formData = responseData.formData;
					
					codeForm.setValues(formData);
					panel.down('image[name=codeImage]').setSrc(TzUniversityContextPath + formData.codeImage);	
					
				});
	    });
      win.show();
  },
  onPreviewPhoneArtClose: function(){
  	this.getView().close();
  },
	onActivitySave: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var msArrInfoPanelArr=Ext.ComponentQuery.query("grid[reference=activityListGridPanal]");
			//获取组件注册信息参数
			var tzParams = this.getActivityInfoParams();
			var comView = this.getView();
			
			//预览发布列表
			var viewArtGrid =comView.down('grid[name=viewArtGrid]');
			
			var actType = comView.actType;
			var activityId = form.findField("activityId").getValue();
			if(actType=="update" && (activityId=="" || typeof(activityId) == "undefined" )){
					Ext.Msg.alert("提示","保存出错");
			}else{
				Ext.tzSubmit(tzParams,function(responseData){
					if(actType=="add"){
						comView.actType = "update";
						form.findField("activityId").setValue(responseData.activityId);
						
					}
					
					 //预览发布列表;
					var tzStoreParams = '{"activityId":"'+responseData.activityId+'","gridTyp":"VIEWART"}';
					viewArtGrid.store.tzStoreParams = tzStoreParams;
					viewArtGrid.store.load();		
					
					comView.commitChanges(comView);
					
					for(var i=0;i<msArrInfoPanelArr.length;i++){
						msArrInfoPanelArr[i].store.load();
					}
					
				},"",true,this);
			}
	
		}else{
				Ext.Msg.alert("提示","必填项未填或者填写内容长度超过字数限制");
		}
	},
	onActivityEnsure: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var msArrInfoPanelArr=Ext.ComponentQuery.query("grid[reference=activityListGridPanal]");
			//获取组件注册信息参数
			var tzParams = this.getActivityInfoParams();
			var comView = this.getView();
			var actType = comView.actType;
			var activityId = form.findField("activityId").getValue();
			if(actType=="update" && (activityId=="" || typeof(activityId) == "undefined")){
					Ext.Msg.alert("提示","保存出错");
			}else{
					Ext.tzSubmit(tzParams,function(responseData){
						comView.close();
							
						for(var i=0;i<msArrInfoPanelArr.length;i++){
							msArrInfoPanelArr[i].store.load();
						}
					},"",true,this);
			}
		}else{
				Ext.Msg.alert("提示","必填项未填或者填写内容长度超过字数限制");
		}
		
	},
	publishArt:function(){
		//内容表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var msArrInfoPanelArr=Ext.ComponentQuery.query("grid[reference=activityListGridPanal]");
			//获取内容信息参数
			var comView = this.getView();
			
			//预览发布列表
			var viewArtGrid =comView.down('grid[name=viewArtGrid]');
			
			var actType = comView.actType;
			var activityId = form.findField("activityId").getValue();
			if(actType=="update" && (activityId=="" || typeof(activityId) == "undefined" )){
					Ext.Msg.alert("提示","保存出错");
			}else{
				  form.findField("publishStatus").setValue("Y");
				  form.findField("publishClick").setValue("Y");
				  var tzParams = this.getActivityInfoParams();
				  form.findField("publishClick").setValue("");
					Ext.tzSubmit(tzParams,function(responseData){
					
					  if(actType=="add"){
						comView.actType = "update";	
					  	form.findField("activityId").setValue(responseData.activityId);
					  }
				  	
					  /*	
					  	if(responseData.activityId != ""){
					  		form.findField("publishStatusDesc").setValue("已发布");
					  		var viewUrl = responseData.publishUrl;
					  		form.findField("publishUrl").setValue(viewUrl);
					  	}
					  */
					  //预览发布列表;
						var tzStoreParams = '{"activityId":"'+responseData.activityId+'","gridTyp":"VIEWART"}';
						viewArtGrid.store.tzStoreParams = tzStoreParams;
						viewArtGrid.store.load();		
						
				  	  comView.commitChanges(comView);
					
						for(var i=0;i<msArrInfoPanelArr.length;i++){
							msArrInfoPanelArr[i].store.load();
						}
				  	
					},"发布成功",true,this);
			}
	
		}else{
				Ext.Msg.alert("提示","必填项未填或者填写内容长度超过字数限制");
		}
	},
	unpublishArt:function(){
		//内容表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var msArrInfoPanelArr=Ext.ComponentQuery.query("grid[reference=activityListGridPanal]");
			//获取内容信息参数
			var comView = this.getView();
			
			//预览发布列表
			var viewArtGrid =comView.down('grid[name=viewArtGrid]');
			
			var actType = comView.actType;
			var activityId = form.findField("activityId").getValue();
			if(actType=="update" && (activityId=="" || typeof(activityId) == "undefined" )){
					Ext.Msg.alert("提示","保存出错");
			}else{
				
				  form.findField("publishStatus").setValue("N");
				  form.findField("publishClick").setValue("Y");
				  var tzParams = this.getActivityInfoParams();
				  form.findField("publishClick").setValue("");
					Ext.tzSubmit(tzParams,function(responseData){
					
					  if(actType=="add"){
							comView.actType = "update";	
					  	    form.findField("activityId").setValue(responseData.activityId);
					  }
					  
					  /*
					  if(responseData.activityId != ""){
				  		form.findField("publishStatusDesc").setValue("未发布");
				  		form.findField("publishUrl").setValue("");
				  	  }
				  	*/
					//预览发布列表;
						var tzStoreParams = '{"activityId":"'+responseData.activityId+'","gridTyp":"VIEWART"}';
						viewArtGrid.store.tzStoreParams = tzStoreParams;
						viewArtGrid.store.load();	
						
				  	comView.commitChanges(comView);
					
						for(var i=0;i<msArrInfoPanelArr.length;i++){
							msArrInfoPanelArr[i].store.load();
						}
				  	
					},"撤销成功",true,this);
			}
	
		}else{
				Ext.Msg.alert("提示","必填项未填或者填写内容长度超过字数限制");
		}
	},
	upArt:function(){
		//内容表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var msArrInfoPanelArr=Ext.ComponentQuery.query("grid[reference=activityListGridPanal]");
			//获取内容信息参数
			var comView = this.getView();
			
			//预览发布列表
			var viewArtGrid =comView.down('grid[name=viewArtGrid]');
			
			var actType = comView.actType;
			var activityId = form.findField("activityId").getValue();
			if(actType=="update" && (activityId=="" || typeof(activityId) == "undefined" )){
					Ext.Msg.alert("提示","保存出错");
			}else{
				  form.findField("upArtClick").setValue("Y");
				  var tzParams = this.getActivityInfoParams();
				  form.findField("upArtClick").setValue("");
					Ext.tzSubmit(tzParams,function(responseData){
					
						if(actType=="add"){
							comView.actType = "update";	
							form.findField("activityId").setValue(responseData.activityId);
					  	
						}
						
						//预览发布列表;
						var tzStoreParams = '{"activityId":"'+responseData.activityId+'","gridTyp":"VIEWART"}';
						viewArtGrid.store.tzStoreParams = tzStoreParams;
						viewArtGrid.store.load();	
						
						comView.commitChanges(comView);
						
					  
						for(var i=0;i<msArrInfoPanelArr.length;i++){
							msArrInfoPanelArr[i].store.load();
						}
			},"置顶成功",true,this);
			}
	
		}else{
				Ext.Msg.alert("提示","必填项未填或者填写内容长度超过字数限制");
		}
	},
	viewArt:function(){
		//内容表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取内容信息参数
			var tzParams = this.getActivityInfoParams();
			var comView = this.getView();
			
			//预览发布列表
			var viewArtGrid =comView.down('grid[name=viewArtGrid]');
			
			var actType = comView.actType;
			var activityId = form.findField("activityId").getValue();
			if(actType=="update" && (activityId=="" || typeof(activityId) == "undefined" )){
					Ext.Msg.alert("提示","保存出错");
			}else{
				Ext.tzSubmit(tzParams,function(responseData){
					
					if(actType=="add"){
						comView.actType = "update";	
						form.findField("activityId").setValue(responseData.activityId);
						
					}
					
					//预览发布列表;
					var tzStoreParams = '{"activityId":"'+responseData.activityId+'","gridTyp":"VIEWART"}';
					viewArtGrid.store.tzStoreParams = tzStoreParams;
					viewArtGrid.store.load();	
					
					comView.commitChanges(comView);
					
					var viewUrl = responseData.viewUrl;
					if(!isNaN(viewUrl)){
						if(viewUrl > 1){
							Ext.Msg.alert("提示","活动对应多个栏目,请在上方列表中选择对应栏目预览");
						}
						
						if(viewUrl == 0){
							Ext.Msg.alert("提示","未选择栏目");
						}
					}else{
						window.open(viewUrl);
					}
					
				  
			},"",true,this);
			}
	
		}else{
				Ext.Msg.alert("提示","必填项未填或者填写内容长度超过字数限制");
		}
	},
	onActivityClose: function(){
		this.getView().close();
	},
	getActivityInfoParams: function(){
		//活动信息表单
		var form = this.getView().child("form").getForm();
		//活动信息标志
		var actType = this.getView().actType;
		//活动ID;
		var activityId = form.findField("activityId").getValue();
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
		//报名信息项数据
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
				removeJson = '{"typeFlag":"ARTATTACHINFO","activityId":"'+activityId+'","data":'+Ext.JSON.encode(attachmentGridRemoveRecs[i].data)+'}';
			}else{
				removeJson = removeJson + ',{"typeFlag":"ARTATTACHINFO","activityId":"'+activityId+'","data":'+Ext.JSON.encode(attachmentGridRemoveRecs[i].data)+'}';
	  	}
		}
		//结束附件集;
		
		//开始报名信息项;
		//var grid = Ext.getCmp('applyItemGrid');
		var grid = this.getView().down('grid[name=applyItemGrid]');
		//报名信息项数据
		var store = grid.getStore();
		
		//修改记录
		var mfRecs = store.getRange(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"ACTAPPLYINFO","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"ACTAPPLYINFO","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}
		}
		
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
		
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		
		//删除json字符串
		//删除记录
		var removeRecs = store.getRemovedRecords();
		for(var i=0;i<removeRecs.length;i++){
			
			if(removeJson == ""){
				removeJson = '{"typeFlag":"ACTAPPLYINFO","activityId":"'+activityId+'","data":'+Ext.JSON.encode(removeRecs[i].data)+'}';
			}else{
				removeJson = removeJson + ',{"typeFlag":"ACTAPPLYINFO","activityId":"'+activityId+'","data":'+Ext.JSON.encode(removeRecs[i].data)+'}';
	  	}
		}
		if(removeJson != ""){
			if(comParams == ""){
				comParams = '"delete":[' + removeJson + "]";
			}else{
				comParams = comParams + ',"delete":[' + removeJson + "]";
			}
		}
		//结束报名信息项;

		//提交参数
		var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
			var dataView = win.findParentByType("activityInfo").down('dataview[name=picView]');
			var picStore = dataView.getStore();
			var i = 0;
			for(i=0; i<picStore.getCount(); i++){
				var sFileName = picStore.getAt(i).data.sysFileName;
				if (sFileName == sysFileName ){
					picStore.getAt(i).data.caption = caption;
					picStore.getAt(i).data.picURL = picURL;
				}
			}
			//关闭窗口
			win.ignoreChangesFlag = true;
			win.close();
			dataView.refresh();
		}	
	},
	addApplyItem: function(){
		//var applyItemGrid = Ext.getCmp('applyItemGrid');
		var applyItemGrid = this.lookupReference('applyItemGrid');
		var applyItemCellEditing = applyItemGrid.getPlugin('applyItemCellediting');	
		var applyItemstore =  applyItemGrid.getStore();
		var rowCount = applyItemstore.getCount();

		if(rowCount===23){
			 Ext.Msg.alert('提示','最多自定义20个信息项');
		}else{
			
			var i = 0;
			var applyInfoStr=""; 
			for(i = 0; i < rowCount; i++){	
				applyInfoStr=applyInfoStr+applyItemGrid.getStore().getAt(i).get("applyItemId");
			}
		
		  var strApplyItemId="";
			for(i = 0; i < 20; i++){	
				if(i>8){
					strApplyItemId = "TZ_ZXBM_XXX_0"+(i+1);
				}else{
					strApplyItemId = "TZ_ZXBM_XXX_00"+(i+1);
				}
				if(applyInfoStr.indexOf(strApplyItemId)<0){
					break;
				}
			}
		
	
			applyItemCellEditing.cancelEdit();
			// Create a model instance
    	var r = Ext.create('KitchenSink.view.activity.applyItemModel', {
    		activityId: "",
        applyItemId: strApplyItemId,
				applyItemNum: rowCount + 1,
				applyItemName: '',
				applyItemRequired: 'Y',
				applyItemType: '1'
    	});

 
 
    	applyItemstore.insert(rowCount,r);
    	applyItemCellEditing.startEditByPosition({
       	row: rowCount,
       	column: 1
    	});
    }
	},
	deleteApplyItem: function(view, rowIndex){
		var store = view.findParentByType("grid").store;
		
		var applyItemId = store.getAt(rowIndex).data.applyItemId;
		if(applyItemId =="TZ_CYR_NAME" || applyItemId =="TZ_ZY_EMAIL" || applyItemId =="TZ_ZY_SJ"){
        Ext.Msg.alert("提示","【姓名】、【邮箱】、【手机】不可删除");
        return;
    }
		
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					      
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	deleteApplyItems: function(){
	   //选中行
	   var applyItemGrid = this.lookupReference('applyItemGrid');
	   var selList = applyItemGrid.getSelectionModel().getSelection();
	  
	   
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
	   	 var i = 0;
		 var bl = false;
	   	 for(i = checkLen; i>0; i--){
	   	 	 var applyItemId = selList[i-1].data.applyItemId;
	   	 	 if(applyItemId =="TZ_CYR_NAME" || applyItemId =="TZ_ZY_EMAIL" || applyItemId =="TZ_ZY_SJ"){
	   	 	 	  bl = true;
        		Ext.Array.splice( selList, i-1,1 ) 
    		 }			 
	   	 }
				Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				
					if(btnId == 'yes'){					   
						var store = applyItemGrid.store;
						store.remove(selList);
						if(bl){
							Ext.MessageBox.alert('提示', '【姓名】、【邮箱】、【手机】不可删除');
						}
					}												  
				},this);   
	   }
	},
	editorApplyItemOptions: function(grid, rowIndex){
		
		var rec = grid.getStore().getAt(rowIndex);
		var applyItemId = rec.data.applyItemId;
		
		if(applyItemId =="TZ_CYR_NAME" || applyItemId =="TZ_ZY_EMAIL" || applyItemId =="TZ_ZY_SJ"){
	        Ext.Msg.alert("提示","【姓名】、【邮箱】、【手机】不可修改");
	        return;
	    }
		
		if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存当前页面信息。");
			return;
		}		
		
		//是否修改过;
		//报名信息项选项数据
		var store = grid.getStore();
		//修改记录
		var mfRecs = store.getModifiedRecords(); 
		//删除记录
		var removeRecs = store.getRemovedRecords();
		if(removeRecs.length > 0 || mfRecs.length > 0){
			Ext.MessageBox.alert("提示","请先保存当前页面信息。");
			return;
		}
		
		var activityId = this.getView().child("form").getForm().findField("activityId").getValue();
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_HD_MANAGER_COM"]["TZ_HD_OPTIONS_STD"];
		if( pageResSet == "" || typeof(pageResSet) == "undefined" ){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || typeof(className) == "undefined"  ){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_HD_OPTIONS_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('applyOptionsWindow');
        
    if (!win) {
			//className = 'KitchenSink.view.activity.applyOptionsWindow';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
        win = new ViewClass();
        this.getView().add(win);
    }
		
		//操作类型设置为更新
		//win.actType = "update";
		//参数
		//var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_OPTIONS_STD","OperateType":"QF","comParams":{"activityId":"'+activityId+'"}}';
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({"activityId":activityId,"applyItemId":applyItemId});
		
		grid = this.lookupReference('applyItemOptionsGrid');
				
		var tzStoreParams = '{"activityId":"'+activityId+'","applyItemId":"'+applyItemId+'"}';
		grid.store.tzStoreParams = tzStoreParams;
		grid.store.load();		
		//Ext.tzLoad(tzParams,function(responseData){
		
			  //grid = this.lookupReference('applyItemOptionsGrid');
				
				//var tzStoreParams = "{'activityId':'"+activityId+"','applyItemId':'"+applyItemId+"'}";
				//grid.store.tzStoreParams = tzStoreParams;
				//grid.store.load();		
		//});
		
    win.show();
	},
	onItemOptionsClose: function(btn){
		
	
		//this.getView().close();
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	},
	addApplyItemOption: function(){
		var applyItemOptionsGrid = this.lookupReference('applyItemOptionsGrid');
		var applyItemOptionsCellediting = applyItemOptionsGrid.getPlugin('applyItemOptionsCellediting');	
		var applyItemOptionsStore =  applyItemOptionsGrid.getStore();
		var rowCount = applyItemOptionsStore.getCount();
			
			var i = 0;
			var maxNum = 1; 
			for(i = 0; i < rowCount; i++){	
				var num = applyItemOptionsStore.getAt(i).get("transId");
				if (num != "" && maxNum <= parseInt(num)){
					maxNum = parseInt(num)+1;
				}
			}
		
			
			applyItemOptionsCellediting.cancelEdit();
			// Create a model instance
    	var r = Ext.create('KitchenSink.view.activity.applyItemOptionsModel', {
    		transId: maxNum,
        transPxXh: rowCount + 1,
				transName: ""
    	});

    	applyItemOptionsStore.insert(rowCount,r);
    	applyItemOptionsCellediting.startEditByPosition({
       	row: rowCount,
       	column: 1
    	});
	},
	deleteApplyItemOption: function(view, rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	deleteApplyItemOptions: function(){
	   //选中行
	   var applyItemOptionsGrid = this.lookupReference('applyItemOptionsGrid');
	   var selList = applyItemOptionsGrid.getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var store = applyItemOptionsGrid.store;
				   store.remove(selList);
				}												  
			},this);   
	   }
	},
	onItemOptionsSave: function(btn){

		//获取窗口
		var win = btn.findParentByType("window");
	//报名信息项选项表单;
		var form = win.child("form").getForm();

			//获取信息项选项参数
			var tzParams = this.getItmeOptionsParams(win);

			Ext.tzSubmit(tzParams,function(responseData){

			},"",true,this);
	
	},
	onItemOptionsEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面注册信息*/
			var tzParams = this.getItmeOptionsParams(win);

			Ext.tzSubmit(tzParams,function(responseData){
				//关闭窗口
				win.close();
			},"",true,this);
			
		}	
	},
	getItmeOptionsParams: function(win){
		 //活动信息表单
		var form = win.child("form").getForm();
		
		//活动ID;
		var activityId = form.findField("activityId").getValue();
		//活动项ID;
		var applyItemId = form.findField("applyItemId").getValue();
		
		//更新操作参数
		var comParams = "";
		
		//报名信息项选项grid;
		//var grid = Ext.getCmp('applyItemOptionsGrid');
		var grid = win.down('grid[name=applyItemOptionsGrid]');
		//报名信息项选项数据
		var store = grid.getStore();
	
		//修改记录
		var editJson="";
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"ACTOPTIONS","activityId":"'+activityId+'","applyItemId":"'+applyItemId+'","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"ACTOPTIONS","activityId":"'+activityId+'","applyItemId":"'+applyItemId+'","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
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
				removeJson = '{"typeFlag":"ACTOPTIONS","activityId":"'+activityId+'","applyItemId":"'+applyItemId+'","data":'+Ext.JSON.encode(removeRecs[i].data)+'}';
			}else{
				removeJson = removeJson +',{"typeFlag":"ACTOPTIONS","activityId":"'+activityId+'","applyItemId":"'+applyItemId+'","data":'+Ext.JSON.encode(removeRecs[i].data)+'}';
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
		
		var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_OPTIONS_STD","OperateType":"U","comParams":{'+comParams+'}}';
		
        return tzParams;
        
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
	viewArtContent:  function(view, rowIndex){
		var store = view.findParentByType("grid").store;
		
		var publicUrl = store.getAt(rowIndex).data.publicUrl;
		window.open(publicUrl);
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
                TZ_AUD_NAM: '听众名称',
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
                    
                    var TagModel=new KitchenSink.view.activity.tagModel();
                    var audName = listData.data.tagName;
                    var audId=listData.data.tagId;
                    TagModel.set('tagId',audId);
                    TagModel.set('tagName',audName);
                    oprIdArray[i]=TagModel;
                    i++;
                }
                for(j=0;j<selection.length;j++){
                    var TagModel=new KitchenSink.view.activity.tagModel();
                    var audName = selection[j].data.TZ_AUD_NAM;
                    var audId=selection[j].data.TZ_AUD_ID;
                    TagModel.set('tagId',audId);
                    TagModel.set('tagName',audName);
                    oprIdArray[i]=TagModel;
                    i++;
                };
                btn.findParentByType("form").getForm().findField("AudList").setValue(oprIdArray);
            }
        })
    }
});