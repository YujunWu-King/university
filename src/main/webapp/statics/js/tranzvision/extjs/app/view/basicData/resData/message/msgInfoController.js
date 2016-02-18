Ext.define('KitchenSink.view.basicData.resData.message.msgInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.msgInfoController', 
    //新增消息定义
    addMsgDefine:function(btn){
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存消息集合定义，再新增消息定义。");
			return;
		}
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_MESSAGE_COM"]["TZ_GD_MSGDEF_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_MSGDEF_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('msgDefine');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		//操作类型设置为新增
		win.actType = "add";
		//消息集合定义表单
		var msgFormParams = this.getView().child("form").getForm().getValues();
		//消息集合id
		var msgSetID = msgFormParams["msgSetID"];
		//语言类型
		var lanageType = this.getView().lanageType;
		var orgId = this.getView().orgId;
        //消息定义表单
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({msgSetID:msgSetID});
		form.setValues({msgLanage:lanageType});
		form.setValues({orgId:orgId});
        win.show();
    },
    //编辑消息
    editMsgDefine:function(btn){
    	var grid = this.getView().child("grid");
    	//选中行
	   	var selList = grid.getSelectionModel().getSelection();
	   	//选中行长度
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
				Ext.Msg.alert("提示","请选择一条要修改的记录");
				return;
	   	}else if(checkLen >1){
		   	Ext.Msg.alert("提示","只能选择一条要修改的记录");
		  	return;
	   	}
	   	var msgSetID = selList[0].get("msgSetID");
	   	var messageId = selList[0].get("messageId");
	   	//显示消息集合信息编辑页面
			this.msgDefineEditById(msgSetID,messageId);
    },
    //删除消息
    delMsgDefine:function(btn){
    	var grid = this.getView().child("grid");
    	//选中行
	    var selList = grid.getSelectionModel().getSelection();
	    //选中行长度
	   	var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录。");   
			return;
	    }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){
				   grid.store.remove(selList);
				}												  
			},this);
	   	}
    },
    //编辑消息
    msgDefineEdit:function(view,t,rowIndex){
    	//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_MESSAGE_COM"]["TZ_GD_MSGDEF_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_MSGDEF_STD，请检查配置。');
				return;
			}
			var win = this.lookupReference('msgDefine');
	        
	    if (!win) {
				Ext.syncRequire(className);
				ViewClass = Ext.ClassManager.get(className);
			    //新建类
	        win = new ViewClass();
	        this.getView().add(win);
	    }
			
			//操作类型设置为更新
			win.actType = "update";
			//语言类型
			var lanageType = this.getView().lanageType;
			//对应机构
			var orgId = this.getView().orgId;
			//选中行的数据
			var record = view.findParentByType("grid").store.getAt(rowIndex);
			var msgSetID = record.get("msgSetID");
			var messageId = record.get("messageId");
			//消息表单
			var form = win.child('form').getForm();
			form.findField("msgId").setReadOnly(true);
			form.findField("msgId").addCls("lanage_1");
			//参数
			var tzParams = '{"ComID":"TZ_GD_MESSAGE_COM","PageID":"TZ_GD_MSGDEF_STD","OperateType":"QF","comParams":{"msgSetID":"'+msgSetID+'","messageId":"'+messageId+'","messageLanguage":"'+lanageType+'","orgId":"'+orgId+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				var formData = responseData.formData;
				form.setValues(formData);
			});
      win.show();
    },
  //编辑消息
  msgDefineEditById:function(msgSetID,messageId){
  		//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_MESSAGE_COM"]["TZ_GD_MSGDEF_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_MSGDEF_STD，请检查配置。');
				return;
			}
			var win = this.lookupReference('msgDefine');
	        
	    if (!win) {
				Ext.syncRequire(className);
				ViewClass = Ext.ClassManager.get(className);
			    //新建类
	        win = new ViewClass();
	        this.getView().add(win);
	    }
			
			//操作类型设置为更新
			win.actType = "update";
			//语言类型
			var lanageType = this.getView().lanageType;
			//对应机构
			var orgId = this.getView().orgId;
			//消息表单
			var form = win.child('form').getForm();
			form.findField("msgId").setReadOnly(true);
			form.findField("msgId").addCls("lanage_1");
			//参数
			var tzParams = '{"ComID":"TZ_GD_MESSAGE_COM","PageID":"TZ_GD_MSGDEF_STD","OperateType":"QF","comParams":{"msgSetID":"'+msgSetID+'","messageId":"'+messageId+'","messageLanguage":"'+lanageType+'","orgId":"'+orgId+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				var formData = responseData.formData;
				form.setValues(formData);
			});
      win.show();
  },
	//编辑当前行消息记录
	editCurrentMsg:function(view,rowIndex){
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		//消息集合ID
		var msgSetID = selRec.get("msgSetID");
    var messageID = selRec.get("messageId");
    //显示消息集合信息编辑页面
		this.msgDefineEditById(msgSetID,messageID);
	},
	//删除当前行消息记录
	deleteCurrentMsg:function(view,rowIndex){
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
			if(btnId == 'yes'){
				var store = view.findParentByType("grid").store;
				store.removeAt(rowIndex);
			}
		},this);
	},
	//保存数据
    onMsgInfoSave:function(btn){
    	//消息表单
		var form = this.getView().child("form").getForm();
		if(form.isValid()){
			//消息定义标志
			var actType = this.getView().actType;
	
			//更新操作参数
			var comParams = "";
			//新增
			if(actType == "add"){
				comParams = '"add":[' + Ext.JSON.encode(form.getValues()) + ']';
			}
			//修改json字符串
			var editJson = "";
			if(actType == "update"){
				editJson = Ext.JSON.encode(form.getValues());
			}
			if(editJson != ""){
				if(comParams == ""){
					comParams = '"update":[' + editJson + ']';
				}else{
					comParams = comParams + ',"update":[' + editJson + ']';
				}
			}
			
			//列表信息
			var grid = this.getView().child("grid");
			//删除json字符串
			var removeJson = "";
			//删除记录
			var removeRecs = grid.store.getRemovedRecords();
			for(var i=0;i<removeRecs.length;i++){
				if(removeJson == ""){
					removeJson = Ext.JSON.encode(removeRecs[i].data);
				}else{
					removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
				}
			}
			if(removeJson != ""){
				if(comParams == ""){
					comParams = '"delete":[' + removeJson + ']';
				}else{
					comParams = comParams + ',"delete":[' + removeJson + ']';
				}
			}
			//提交参数
			var tzParams = '{"ComID":"TZ_GD_MESSAGE_COM","PageID":"TZ_GD_MSGINFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
			var comView = this.getView();
			
			Ext.tzSubmit(tzParams,function(responseData){
                var msgSetID = form.findField("msgSetID").getValue();
                if(comView.actType=="add"){
                    var tzStoreParams = '{"cfgSrhId": "TZ_GD_MESSAGE_COM.TZ_GD_MSGINFO_STD.TZ_PT_XXDY_VW","condition":{"TZ_XXJH_ID-operator": "01","TZ_XXJH_ID-value": "'+msgSetID+'","TZ_LANGUAGE_ID-operator": "01","TZ_LANGUAGE_ID-value": "'+TranzvisionMeikecityAdvanced.Boot.language+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "ADMIN"}}';
                    grid.store.tzStoreParams = tzStoreParams;
                    grid.store.load();
                };
                if(btn != "but_ensure"){
                    form.findField("msgSetID").setReadOnly(true);
                    form.findField("msgSetID").addCls("lanage_1");
                    if(comView.actType=='update'){
                        grid.store.reload();
                    }
                }
				comView.actType = "update";

				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
				contentPanel.child("msgSetMg").store.reload();
			},"",true,this);
            return true;
		};
        return false;
    },
    //确定
    onMsgInfoEnsure:function(btn){
    	if(this.onMsgInfoSave("but_ensure")){
            this.getView().close();
        };
    },
    //关闭
    onMsgInfoClose:function(btn){
    	this.getView().close();
    },
    //切换语言
    changeLanguage:function(btn){
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存消息集合定义，再做操作。");
			return;
		}
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_MESSAGE_COM"]["TZ_GD_CHANGE_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_CHANGE_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('changeLanguage');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		//消息集合定义表单
		var msgFormParams = this.getView().child("form").getForm().getValues();
		//消息集合id
		var msgSetID = msgFormParams["msgSetID"];
		//语言类型
		var lanageType = this.getView().lanageType;
        //消息定义表单
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({msgSetID:msgSetID});
		form.setValues({msgLanage:lanageType});
        win.show();
    },
    //切换语言确定
    onLanguageFormEnsure:function(btn){
    	var valuesForm = this.getView().child('form').getForm().getValues();
    	var msgSetID = valuesForm["msgSetID"];
    	var msgLanage = valuesForm["msgLanage"];
    	this.getView().close();

	    var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
	    var panel = contentPanel.child("messageInfo");
	    panel.lanageType = msgLanage;
		var orgId = panel.orgId;/*对应机构*/
		var grid = panel.child('grid');
        var tzStoreParams = '{"cfgSrhId": "TZ_GD_MESSAGE_COM.TZ_GD_MSGINFO_STD.TZ_PT_XXDY_VW","condition":{"TZ_XXJH_ID-operator": "01","TZ_XXJH_ID-value": "'+msgSetID+'","TZ_LANGUAGE_ID-operator": "01","TZ_LANGUAGE_ID-value": "'+msgLanage+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+orgId+'"}}';
		grid.store.tzStoreParams = tzStoreParams;
		grid.store.reload();
    },
    //切换语言关闭
    onLanguageFormClose:function(btn){
    	this.getView().close();
    },
    //同步资源
    synchrLanguage:function(btn){
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存消息集合定义，再做操作。");
			return;
		}
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_MESSAGE_COM"]["TZ_GD_SYNCHR_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_SYNCHR_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('synchrLanguage');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		//消息集合定义表单
		var msgFormParams = this.getView().child("form").getForm().getValues();
        //机构编号
        var orgId = this.getView().orgId;
		//消息集合id
		var msgSetID = msgFormParams["msgSetID"];
		//语言类型
		var lanageType = this.getView().lanageType;
        //消息定义表单
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({msgSetID:msgSetID,sourceLanage:lanageType,orgId:orgId});
        win.show();
    },
    //同步资源确定
    onSynchrFormEnsure:function(btn){
    	var view = this.getView();
    	//消息表单
		var form = view.child("form").getForm();
		if(form.isValid()){
			var valuesForm = form.getValues();
			var sourceLanage = valuesForm["sourceLanage"];
			var targetLanage = valuesForm["targetLanage"];
			if(sourceLanage == targetLanage){
				Ext.Msg.alert("提示","请选择不同的源语言和目标语言。");
			}else{
				var comParams = '"update":[' + Ext.JSON.encode(form.getValues()) + ']';
		    	//提交参数
				var tzParams = '{"ComID":"TZ_GD_MESSAGE_COM","PageID":"TZ_GD_SYNCHR_STD","OperateType":"U","comParams":{'+comParams+'}}';
				Ext.tzSubmit(tzParams,function(responseData){
					var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
				    var grid = contentPanel.child("messageInfo").child('grid');
					grid.store.reload();
		
					Ext.Msg.alert("提示","同步成功！");
					view.close();
				},"",true,this);
			}
		}
    },
    //同步资源关闭
    onSynchrFormClose:function(btn){
    	this.getView().close();
    },
    queryMsg:function(btn){
        var me = this;
        if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存消息集合信息并添加消息信息之后，再查询消息信息。");
            return;
        }

        var panel = this.getView();
        var form = panel.child("form").getForm();
        var msgSetID = form.findField("msgSetID").getValue();
        var msgLanage = panel.lanageType;
        var orgId = panel.orgId;/*对应机构*/

        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_GD_MESSAGE_COM.TZ_GD_MSGINFO_STD.TZ_PT_XXDY_VW',
            condition :{
                TZ_XXJH_ID:msgSetID,
                TZ_LANGUAGE_ID:msgLanage,
                TZ_JG_ID:orgId
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    }
});