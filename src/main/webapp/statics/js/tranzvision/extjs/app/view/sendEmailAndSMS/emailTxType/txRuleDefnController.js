Ext.define('KitchenSink.view.sendEmailAndSMS.emailTxType.txRuleDefnController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.txRuleDefnController', 

    addTxRule: function(btn) {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EMLTX_RULE_COM"]["TZ_TXRULE_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
		return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_TXRULE_DEFN_STD，请检查配置。');
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

        var grid = btn.findParentByType('txRuleDefnList');
        cmp = new ViewClass();
		cmp.actType = "add";
		cmp.RuleGrid = grid;
		
        tab = contentPanel.add(cmp);    
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    
    editCurrentTxRule: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //退信条件ID
	   	 var ruleId = selRec.get("ruleId");
	     //显示组件注册信息编辑页面
	     this.editTxRuleDefnByKey(ruleId,view);
	},
	
	editTxRule: function() {
		var grid = this.getView();
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
        //退信条件ID
        var ruleId = selList[0].get("ruleId");
        //显示邮箱服务器编辑页面
        this.editTxRuleDefnByKey(ruleId,grid);
    },
    
    
    editTxRuleDefnByKey: function(ruleId,grid) {
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_EMLTX_RULE_COM"]["TZ_TXRULE_DEFN_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_TXRULE_DEFN_STD，请检查配置。');
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

        cmp = new ViewClass();
		//操作类型设置为更新
		cmp.actType = "update";
		cmp.RuleGrid = grid;
        
		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			form.findField("ruleId").setReadOnly(true);
			form.findField("ruleId").addCls("lanage_1");
			
			//参数
			var tzParams = '{"ComID":"TZ_EMLTX_RULE_COM","PageID":"TZ_TXRULE_DEFN_STD","OperateType":"QF","comParams":{"ruleId":"'+ruleId+'"}}';
			
			Ext.tzLoad(tzParams,function(responseData){
				
				form.setValues(responseData);							
			});
		});
		
        tab = contentPanel.add(cmp);   
        
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    
    searchTxRule: function(btn){     //searchComList为各自搜索按钮的handler event;

        Ext.tzShowCFGSearch({            

            cfgSrhId: 'TZ_EMLTX_RULE_COM.TZ_EMLTX_RULE_STD.TZ_TX_RULE_TBL',    
			condition:{
                //"TZ_JG_ID_1": Ext.tzOrgID          //设置搜索字段的默认值，没有可以不设置condition;
            },			
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });    
    },
    
    
    deleteTxRule: function(){
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
				   var ruleStore = this.getView().store;
				   ruleStore.remove(selList);
				}												  
			},this);   
	   }
	},
	
	delCurrentTxRule: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    
    saveTxRule: function(btn){
        //资源集合列表
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
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
            comParams = '"delete":[' + removeJson + "]";
        }else{
            return;
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_EMLTX_RULE_COM","PageID":"TZ_EMLTX_RULE_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    
    ensureTxRule: function(btn){
        //资源集合列表
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
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
            comParams = '"delete":[' + removeJson + "]";
			var tzParams = '{"ComID":"TZ_EMLTX_RULE_COM","PageID":"TZ_EMLTX_RULE_STD","OperateType":"U","comParams":{'+comParams+'}}';
			//保存数据
			Ext.tzSubmit(tzParams,function(){
				//关闭窗口
				grid.close();
			},"",true,this);
        }else{
			//关闭窗口
			grid.close();
        }        
    },
	closeEmailServer: function(btn){
        var grid = btn.findParentByType("txRuleDefnList");
		//关闭窗口
		grid.close();
    },

    
    
    onFormSave: function(btn){
    	var panel = btn.findParentByType("txRuleDefnPanel");
    	this.formSaveHandler(panel,function(){
    		if(panel.RuleGrid){
    			panel.RuleGrid.getStore().reload();
    		}
    	});
    },
    
    onFormEnsure: function(btn){
    	var panel = btn.findParentByType("txRuleDefnPanel");
    	this.formSaveHandler(panel,function(){
    		if(panel.RuleGrid){
    			panel.RuleGrid.getStore().reload();
    		}
    		panel.close();
    	});
    },
    
    formSaveHandler: function(panel,callback){
    	var form = panel.child('form');
    	var actType = panel.actType;
    	if(form.isValid()){
    		var formRec = form.getForm().getValues();
    		var comParams = "";
    		//新增
    		if(actType == "add"){
    			comParams = '"add":[' + Ext.JSON.encode(formRec) + ']';
    		}
    		
    		if(actType == "update"){
    			comParams = '"update":[' + Ext.JSON.encode(formRec) + "]";
    		}
    		
    		//提交参数
    		var tzParams = '{"ComID":"TZ_EMLTX_RULE_COM","PageID":"TZ_TXRULE_DEFN_STD","OperateType":"U","comParams":{'+comParams+'}}';
           
    		Ext.tzSubmit(tzParams,function(respData){
    			if(actType == "add"){
    				panel.actType = "update";	
        			form.getForm().setValues({ruleId:respData.ruleId});
        			
        			form.getForm().findField("ruleId").setReadOnly(true);
    				form.getForm().findField("ruleId").addCls("lanage_1");
    			}
				callback();
			},"",true,this);
    	}
    },
    
    onFormClose: function(btn){
    	var panel = btn.findParentByType("txRuleDefnPanel");
    	panel.close();
    }
});