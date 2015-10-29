Ext.define('KitchenSink.view.activity.activityInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.activityInfo', 
	onActivitySave: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getActivityInfoParams();
			var comView = this.getView();
			var actType = comView.actType;
			var activityId = form.findField("activityId").getValue();
			if(actType=="update" && (activityId=="" || activityId==undefined)){
					Ext.Msg.alert("提示","保存出错");
			}else{
					Ext.tzSubmit(tzParams,function(responseData){
					if(actType=="add"){
						comView.actType = "update";	
				  	form.findField("activityId").setValue(responseData.activityId);
				  }
			},"",true,this);
			}
	
		}else{
				Ext.Msg.alert("提示","请填写必填项");
		}
	},
	onActivityEnsure: function(){
		alert("保存信息并关闭");
		this.getView().close();
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
		
		//报名信息项;
		var grid = Ext.getCmp('applyItemGrid');
		//报名信息项数据
		var store = grid.getStore();
		
		
		
		//修改记录
		var mfRecs = store.getModifiedRecords(); 
		for(var i=0;i<mfRecs.length;i++){
			if(editJson == ""){
				editJson = '{"typeFlag":"ACTAPPLYINFO","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"ACTAPPLYINFO","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
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
			console.log(removeRecs[i].data.applyItemId);
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
		//提交参数
		
		var tzParams = '{"ComID":"TZ_HD_MANAGER_COM","PageID":"TZ_HD_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	editorApplyItem: function(){
		Ext.create('Ext.window.Window', {
	    title: 'Hello',
	    height: 200,
	    width: 400,
	    id: 'applyItemWindow',
	    layout: 'fit',
	    items: {  // Let's put an empty grid in just to illustrate fit layout
	        
	    },
	    buttons: [{
	    	text: '确定',
	    	handler: function() {
        		Ext.getCmp('applyItemWindow').close();
    		}
	    },{
	    	text: '取消',
	    	handler: function() {
        		Ext.getCmp('applyItemWindow').close();
	    	}
	    }]
		}).show();
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
		
			//console.log(applyItemGrid);
			applyItemCellEditing.cancelEdit();
			// Create a model instance
    	var r = Ext.create('KitchenSink.view.activity.applyItemModel', {
    		activityId: "",
        applyItemId: strApplyItemId,
				applyItemNum: rowCount + 1,
				applyItemName: '',
				applyItemRequired: false,
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
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
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
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var store = applyItemGrid.store;
				   store.remove(selList);
				}												  
			},this);   
	   }
	},
});