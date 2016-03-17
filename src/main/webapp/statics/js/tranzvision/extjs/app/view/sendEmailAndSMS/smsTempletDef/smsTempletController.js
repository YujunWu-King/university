Ext.define('KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.smsTempletController', 
	requires: [
       'KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletInfo'
    ],
    addSmsTemplet: function() {
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SMS_TMPL_MG_COM"]["TZ_SMS_TMPL_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SMS_TMPL_STD，请检查配置。');
				return;
			}
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
       		contentPanel.body.addCls('kitchensink-example');

            //className = 'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfo';
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
			
			cmp.actType = "add";
			
            tab = contentPanel.add(cmp);  

			tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
			
            contentPanel.setActiveTab(tab);
	
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
    },
	editSelSmsTemplet: function() {
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
        //邮件模版编号
        var smstempid = selList[0].get("smstempid");
		var smstemporg = selList[0].get("smstemporg");
		var restempid = selList[0].get("restempid");
        //显示短信模版编辑页面
        this.editSmsTmplInfoByKey(smstempid,smstemporg,restempid);
    },
    editSmsTemplet: function(view, rowIndex) {
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //组件ID
	   	 var smstempid = selRec.get("smstempid");
		 var smstemporg = selRec.get("smstemporg");
		 var restempid = selRec.get("restempid");
	     //显示短信模板信息编辑页面
	     this.editSmsTmplInfoByKey(smstempid,smstemporg,restempid);
    },
	editSmsTmplInfoByKey: function(smstempid,smstemporg,restempid) {
	
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SMS_TMPL_MG_COM"]["TZ_SMS_TMPL_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SMS_TMPL_STD，请检查配置。');
				return;
			}
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
       		contentPanel.body.addCls('kitchensink-example');

         //   className = 'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfo';
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
				//邮箱信息配置数据表单
				//var form = this.lookupReference('emailServerForm').getForm();
				
				//组件注册表单信息;
				var form = panel.child('form').getForm();
				form.findField("smstempid").setReadOnly(true);
				form.findField("smstemporg").setReadOnly(true);
				form.findField("smstempid").addCls("lanage_1");;
				form.findField("smstemporg").addCls("lanage_1");
				
				//参数
				var tzParams = '{"ComID":"TZ_SMS_TMPL_MG_COM","PageID":"TZ_SMS_TMPL_STD","OperateType":"QF","comParams":{"smstempid":"'+smstempid+'","smstemporg":"'+smstemporg+'"}}';
				
				Ext.tzLoad(tzParams,function(responseData){

					form.setValues(responseData);
					
					//信息项数据
				
					var	smsTmplItemGrid = panel.down('grid[name=smsTmplItemGrid]');
					var tzStoreParamsItem = '{"restempid":"'+restempid+'"}';
					smsTmplItemGrid.store.tzStoreParams = tzStoreParamsItem;
					smsTmplItemGrid.store.load();	
						
				});
			});
			
            tab = contentPanel.add(cmp);    

			tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
			
            contentPanel.setActiveTab(tab);
	
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }	
    },
	deleteSelSmsTemplet: function(){
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
				   var smsTempletStore = this.getView().store;
                    smsTempletStore.remove(selList);
				}												  
			},this);   
	   }
	},
    deleteSmsTemplet: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
	searchComList: function(btn){     //searchComList为各自搜索按钮的handler event;

		Ext.tzShowCFGSearch({           

			cfgSrhId: 'TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_MG_STD.TZ_SMSTMPL_VW',    
			condition:
            {
                "TZ_JG_ID_1": Ext.tzOrgID          //设置搜索字段的默认值，没有可以不设置condition;
            },			
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});    
    },
	saveSmsTemplet: function(btn){
        
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
        var tzParams = '{"ComID":"TZ_SMS_TMPL_MG_COM","PageID":"TZ_SMS_TMPL_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
	ensureSmsTemplet: function(btn){
        
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
			//提交参数
			var tzParams = '{"ComID":"TZ_SMS_TMPL_MG_COM","PageID":"TZ_SMS_TMPL_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
			//保存数据
			Ext.tzSubmit(tzParams,function(){
				grid.close();
			},"",true,this);
        }else{
            grid.close();
        } 
    },
	closeSmsTemplet: function(btn){
        
        var grid = btn.findParentByType("grid");
        
        grid.close();

    },
	resetAllTemplet: function(btn){
		
	   	Ext.MessageBox.confirm('确认', '“初始化设置默认模版”操作将会清除用户目前设置的所有模版数据，是否继续？', function(btnId){
			if(btnId == 'yes'){					   
				//提交参数
				var tzParams = '{"ComID":"TZ_SMS_TMPL_MG_COM","PageID":"TZ_SMS_TMPL_MG_STD","OperateType":"initializ","comParams":{}}';
				Ext.tzSubmit(tzParams,function(){
					//初始化成功
					var grid = btn.findParentByType("grid");
					var store = grid.getStore();
					store.reload();
					
				},"",true,this);
			}												  
		},this);   	   	
	}
});