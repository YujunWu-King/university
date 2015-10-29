Ext.define('KitchenSink.view.content.contentMg.artInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.artInfo', 
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
		var tzParams = '{"ComID":"TZ_CONTENT_MG_COM","PageID":"TZ_CONTENT_INF_STD","OperateType":"U","comParams":{'+comParams+'}}';
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
	}
});