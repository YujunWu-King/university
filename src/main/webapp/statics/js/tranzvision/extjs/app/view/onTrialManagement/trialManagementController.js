Ext.define('KitchenSink.view.onTrialManagement.trialManagementController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.trialManagementController', 
    editSelTrial: function() {
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
        //序号
        var seqNum = selList[0].get("seqNum");
        this.editTrialByKey(seqNum);
    },
    editTrial: function(view, rowIndex) {
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //序号
	   	 var seqNum = selRec.get("seqNum");
	     this.editTrialByKey(seqNum);	 
    },
    editTrialByKey: function(seqNum) {
	
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_TRIAL_MNG_COM"]["TZ_TRIAL_INF_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_TRIAL_INF_STD，请检查配置。');
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
				var form = panel.child('form').getForm();
				//参数
				var tzParams = '{"ComID":"TZ_TRIAL_MNG_COM","PageID":"TZ_TRIAL_INF_STD","OperateType":"QF","comParams":{"seqNum":"'+seqNum+'"}}';
				
				Ext.tzLoad(tzParams,function(responseData){	
					form.setValues(responseData.formData);
					form.findField("orgName").setReadOnly(true);
					form.findField("website").setReadOnly(true);
					form.findField("submitTime").setReadOnly(true);
					form.findField("contactName").setReadOnly(true);
					form.findField("contactPhone").setReadOnly(true);
					form.findField("contactTel").setReadOnly(true);
					form.findField("contactEmail").setReadOnly(true);
					form.findField("startTime").setReadOnly(true);
					form.findField("endTime").setReadOnly(true);
					form.findField("hmsr").setReadOnly(true);
					
					form.findField("orgName").addCls("lanage_1");
					form.findField("website").addCls("lanage_1");
					form.findField("submitTime").addCls("lanage_1");
					form.findField("contactName").addCls("lanage_1");
					form.findField("contactPhone").addCls("lanage_1");
					form.findField("contactTel").addCls("lanage_1");
					form.findField("contactEmail").addCls("lanage_1");
					form.findField("startTime").addCls("lanage_1");
					form.findField("endTime").addCls("lanage_1");
					form.findField("hmsr").addCls("lanage_1");
					panel.commitChanges(panel);
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
    searchTrial: function(btn){     //searchComList为各自搜索按钮的handler event;
		Ext.tzShowCFGSearch({            
			cfgSrhId: 'TZ_TRIAL_MNG_COM.TZ_TRIAL_MNG_STD.PS_TZ_ON_TRIAL_V',    
			condition:{},			
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});    
    },
	closeTrialManagement: function(btn){
        var grid = btn.findParentByType("grid");
        grid.close();
        
    },
    sendEmail: function(btn){
    	var grid = btn.findParentByType("grid");
		var store = grid.getStore();
		var selList = grid.getSelectionModel().getSelection();
		
		//选中行长度
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","您没有选中任何记录");
			return ;
		}
		
		var personList = [];
		for(var i=0;i<checkLen;i++){
			var seqNum  = selList[i].get('seqNum');
			personList.push({"seqNum":seqNum});
		};
		
		var params = {
		        "ComID":"TZ_TRIAL_MNG_COM",
		         "PageID":"TZ_TRIAL_MNG_STD",
		         "OperateType":"U",
		         "comParams":{"add":[{"personList":personList}]}
		};
		
		Ext.tzLoad(Ext.JSON.encode(params),function(audID){
			 Ext.tzSendEmail({
				//发送的邮件模板;
		        "EmailTmpName": ["TZ_ON_TRIAL_TG","TZ_ON_TRIAL_GQ"],
		         //创建的需要发送的听众ID;
		         "audienceId": audID,
		         //是否有附件: Y 表示可以发送附件,"N"表示无附件;
		         "file": "N"
			 });
		});
    },
    //邮件服务器搜索
    searchDlzh: function(btn){
		var form = this.getView().child("form").getForm();
		var orgid = form.findField('orgId').value;
		if(orgid == null || orgid == ''){
			Ext.Msg.alert("提示","请先选择试用机构");
			return;
		}

		Ext.tzShowPromptSearch({
			recname: 'PS_TZ_AQ_YHXX_V',
			searchDesc: '搜索试用账号',
			maxRow:20,
			condition:{
				presetFields:{
					TZ_JG_ID:{
						value: orgid,
						type: '01'	
					}	
				},
				srhConFields:{
					TZ_DLZH_ID:{
						desc:'试用账号',
						operator:'01',
						type:'01'
					},
					TZ_REALNAME:{
						desc:'账号名称',
						operator:'01',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TZ_DLZH_ID:'试用账号',
				TZ_REALNAME: '账号名称'	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("dlzh").setValue(selection[0].data.TZ_DLZH_ID);
				form.findField("dlzhName").setValue(selection[0].data.TZ_REALNAME);
			}
		});	
	},
	onTrialClose: function(){
		this.getView().close();
	},
	onTrialSave: function(){
		var comView = this.getView();
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var comParams = '"update":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			
			var tzParams = '{"ComID":"TZ_TRIAL_MNG_COM","PageID":"TZ_TRIAL_INF_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData){
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("trialManagement").store.reload();
			},"",true,this);
		}
	},onTrialEnsure: function(){
		var comView = this.getView();
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var comParams = '"update":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			
			var tzParams = '{"ComID":"TZ_TRIAL_MNG_COM","PageID":"TZ_TRIAL_INF_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData){
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("trialManagement").store.reload();
				comView.close();
			},"",true,this);
		}
	}
});