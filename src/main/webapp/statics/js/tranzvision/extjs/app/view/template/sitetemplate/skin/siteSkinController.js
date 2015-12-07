Ext.define('KitchenSink.view.template.sitetemplate.skin.siteSkinController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.siteSkinInfo', 
     //删除
    deleteSitePic: function(view, rowIndex){
    	Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
    onFormSave: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getSkinpicInfoParams();
			var comView = this.getView();
			var actType = comView.actType;
			var skinId = form.findField("skinId").getValue();
			if(actType=="update" && (skinId=="" || typeof(skinId) == "undefined" )){
					Ext.Msg.alert("提示","保存出错");
			}else{
				Ext.tzSubmit(tzParams,function(responseData){
					if(actType=="add"){
						comView.actType = "update";	
					  	form.findField("skinId").setValue(responseData.skinId);
				  	}
				  	contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
					var skinGrid = contentPanel.child("siteTemplateInfo").child("form").child("tabpanel").getActiveTab();
					skinGrid.store.reload();
				},"",true,this);
			}
	
		}else{
				Ext.Msg.alert("提示","请填写必填项");
		}
	},
	getSkinpicInfoParams: function(){
		//活动信息表单
		var form = this.getView().child("form").getForm();
		//活动信息标志
		var actType = this.getView().actType;
		//ID;
		var siteId = form.findField("siteId").getValue();
		var skinId = form.findField("skinId").getValue();
		//更新操作参数
		var comParams = "";
		//新增
		if(actType == "add"){
			comParams = '"add":[{"typeFlag":"SKININFO","data":'+Ext.JSON.encode(form.getValues())+'}]';
		}
		//修改json字符串
	
		var editJson = "";
		if(actType == "update"){
			editJson = '{"typeFlag":"SKININFO","data":'+Ext.JSON.encode(form.getValues())+'}';
		}
		
		//开始报名信息项;
		//var grid = Ext.getCmp('applyItemGrid');
		var grid = this.getView().down('grid[name=applyItemGrid]');
		//报名信息项数据
		var store = grid.getStore();
		
		//修改记录
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"SKINAPPLYINFO","siteId":"'+siteId+'","skinId":"'+skinId+'","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"SKINAPPLYINFO","siteId":"'+siteId+'","skinId":"'+skinId+'","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
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
				removeJson = '{"typeFlag":"SKINAPPLYINFO","siteId":"'+siteId+'","skinId":"'+skinId+'","data":'+Ext.JSON.encode(removeRecs[i].data)+'}';
			}else{
				removeJson = removeJson + ',{"typeFlag":"SKINAPPLYINFO","siteId":"'+siteId+'","skinId":"'+skinId+'","data":'+Ext.JSON.encode(removeRecs[i].data)+'}';
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
		var tzParams = '{"ComID":"TZ_GD_ZDMB_COM","PageID":"TZ_GD_ZDPF_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	onFormEnsure: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getSkinpicInfoParams();
			var comView = this.getView();
			var actType = comView.actType;
			var skinId = form.findField("skinId").getValue();
			if(actType=="update" && (skinId=="" || typeof(skinId) == "undefined" )){
					Ext.Msg.alert("提示","保存出错");
			}else{
				Ext.tzSubmit(tzParams,function(responseData){
					contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
					var skinGrid = contentPanel.child("siteTemplateInfo").child("form").child("tabpanel").getActiveTab();
					skinGrid.store.reload();
				},"",true,this);
			}

		}else{
				Ext.Msg.alert("提示","请填写必填项");
		}
		this.getView().close();
	},
	onFormClose: function(){
		this.getView().close();
	}
});