Ext.define('KitchenSink.view.basicData.resData.message.msgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.msgController',
    //查询
    msgQuery:function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_GD_MESSAGE_COM.TZ_GD_MSGlIST_STD.TZ_MESSAGES_V',
			condition:{},
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
    },
    //新增
    msgAdd:function() {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_MESSAGE_COM"]["TZ_GD_MSGINFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_MSGINFO_STD，请检查配置。');
            return;
        }

    var contentPanel,cmp, className, ViewClass, clsProto;
    var themeName = Ext.themeName;

    contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
    contentPanel.body.addCls('kitchensink-example');

    //className = 'KitchenSink.view.basicData.resData.resource.resourceSetInfoPanel';
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

    tab = contentPanel.add(cmp);
    //设置tab页签的beforeactivate事件的监听方法
    tab.on(Ext.tzTabOn(tab,this.getView(),cmp));

    contentPanel.setActiveTab(tab);

    Ext.resumeLayouts(true);

    if (cmp.floating) {
        cmp.show();
    }
},

    //删除
    msgRemove:function(btn){
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
				   var msgStore = this.getView().store;
				   msgStore.remove(selList);
				}												  
			},this);   
	   	}
    },
    //保存
    msgSave:function(btn){
    	//消息集合列表数据
		var store = btn.findParentByType("grid").getStore();
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
		var comParams = "";
		if(removeJson != ""){
			comParams = '"delete":[' + removeJson + "]";
		}
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_MESSAGE_COM","PageID":"TZ_GD_MSGlIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
		Ext.tzSubmit(tzParams,function(){
			store.reload();			   
		},"",true,this);
    },
    //确定
    msgEnsure:function(btn){
        this.msgSave(btn);
        //关闭窗口
        var comView = this.getView();
        comView.close();
    },
    //关闭
    msgClose:function(btn){
        //关闭窗口
        var comView = this.getView();
        comView.close();
    },
    currMsgEdit: function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //资源集合ID
        var msgSetID = selRec.get("msgSetID");
        //显示资源集合信息编辑页面
        this.editMsgSetByID(msgSetID);
    },
    msgEdit: function() {
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
        var msgSetID = selList[0].get("msgSetID");
        this.editMsgSetByID(msgSetID);
    },
    editMsgSetByID:function(msgSetID){
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_MESSAGE_COM"]["TZ_GD_MSGINFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_MSGINFO_STD，请检查配置。');
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
			//消息集合定义表单;
			var form = panel.child('form').getForm();
			form.findField("msgSetID").setReadOnly(true);
			form.findField("msgSetID").addCls("lanage_1");
			var grid = panel.child('grid');
			
			var tzParams = '{"ComID":"TZ_GD_MESSAGE_COM","PageID":"TZ_GD_MSGINFO_STD","OperateType":"QF","comParams":{"msgSetID":"'+msgSetID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				var formData = responseData.formData;
				form.setValues(formData);
			});
            var tzStoreParams = '{"cfgSrhId": "TZ_GD_MESSAGE_COM.TZ_GD_MSGINFO_STD.TZ_PT_XXDY_VW","condition":{"TZ_XXJH_ID-operator": "01","TZ_XXJH_ID-value": "'+msgSetID+'","TZ_LANGUAGE_ID-operator": "01","TZ_LANGUAGE_ID-value": "'+TranzvisionMeikecityAdvanced.Boot.language+'","TZ_JG_ID-operator": "01","TZ_JG_ID-value": "ADMIN"}}';
			grid.store.tzStoreParams = tzStoreParams;
            grid.store.load();
		});
		
        tab = contentPanel.add(cmp);
        //设置tab页签的beforeactivate事件的监听方法
        tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    currMsgDelete: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    }
});