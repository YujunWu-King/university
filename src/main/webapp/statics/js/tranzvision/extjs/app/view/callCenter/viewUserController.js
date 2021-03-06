Ext.define('KitchenSink.view.callCenter.viewUserController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.viewUserController', 
	requires: [
       'Ext.ux.IFrame'
    ],
    
    createUserInfoClass: function(){
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_UM_USERMG_COM"]["TZ_UM_USERINFO_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有访问或修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_UM_USERINFO_STD，请检查配置。');
			return;
		}
		
    	var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
	
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
		
        
        
        return cmp;
    },
    viewUserBtn: function(){
    	Ext.tzSetCompResourses("TZ_UM_USERMG_COM");
		 //选中行
		var form = this.getView().lookupReference("userForm");		
		var formValues = form.getValues();
	    var OPRID = formValues.oprId;
		
		var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
  		contentPanel.body.addCls('kitchensink-example');
	
		var cmp = this.createUserInfoClass();		
	
		cmp.on('afterrender',function(){
			var msgForm = this.lookupReference('userMgForm');
			var form = this.lookupReference('userMgForm').getForm();
			var userInfoForm =this.lookupReference('userMgForm').down('form[name=userInfoForm]').getForm();
			console.log(userInfoForm);
		//	var processInfoForm =this.lookupReference('userMgForm').down('form[name=processInfoForm]').getForm();
		var ksdrInfoForm =this.lookupReference('userMgForm').down('form[name=kslcInfoForm]').getForm();

			var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERINFO_STD","OperateType":"QF","comParams":{"OPRID":"'+OPRID+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//用户账号信息数据
			var formData = responseData.formData;
		
			form.setValues(formData);
			
			//考生导入信息;
			//ksdrInfoForm.setValues(formData.ksdrInfo);
			//考生个人信息
			userInfoForm.setValues(formData.perInfo);
			//录取流程
			ksdrInfoForm.setValues(formData.lqlcInfo);
			
			if(msgForm.down('hiddenfield[name=titleImageUrl]').getValue()){
				msgForm.down('image[name=titileImage]').setSrc(TzUniversityContextPath + msgForm.down('hiddenfield[name=titleImageUrl]').getValue());	
			}else{
				msgForm.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/images/tranzvision/mrtx02.jpg");
			}
		  });
		 
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);
		
		Ext.resumeLayouts(true);
		
		if (cmp.floating) {
		    cmp.show();
		}
		 
    },
    viewThisApplicationForm : function(grid, rowIndex, colIndex) {
		Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
		// 是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}

		var store = grid.getStore();
		var record = store.getAt(rowIndex);
		
		var classID = record.get("classId");
		var oprID = record.get("oprid");
		var appInsID = record.get("appInsId");
		var clpsBmbTplId = record.get("clpsBmbTplId");
        
		if (appInsID != "") {
			var tzParams = '{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"' + appInsID + '","OPRID":"' + oprID + '","TZ_APP_TPL_ID":"' + clpsBmbTplId + '","isReview":"Y"}}';
			var viewUrl = Ext.tzGetGeneralURL() + "?tzParams="	+ encodeURIComponent(tzParams);
			var mask;
			var win = new Ext.Window(
					{
						name : 'applicationFormWindow',
						title : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.viewApplicationForm","查看报名表"),
						maximized : true,
						controller : 'viewUserController',
						classID : classID,
						oprID : oprID,
						appInsID : appInsID,
						gridRecord : record,
						width : Ext.getBody().width,
						height : Ext.getBody().height,
						autoScroll : true,
						border : false,
						bodyBorder : false,
						isTopContainer : true,
						modal : true,
						resizable : false,
						items : [ new Ext.ux.IFrame(
								{
									xtype : 'iframepanel',
									layout : 'fit',
									style : "border:0px none;scrollbar:true",
									border : false,
									src : viewUrl,
									height : "100%",
									width : "100%"
								}) 
						],
						buttons : [
								{
									text : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.audit","审批"),
									iconCls : "send",
									handler : "auditApplicationForm"
								},
								{
									text : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
									iconCls : "close",
									handler : function() {
										win.close();
									}
								} 
						]
					})
			win.show();
		} else {
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.cantFindAppForm","找不到该报名人的报名表"));
		}
	 },
	 onClose:function(){
		 this.getView().close();
	 },
	 activeAccount:function(btn){
		 //this.saveInfo(btn);
		 var form = this.getView().lookupReference("userForm");		
		 var formValues = form.getValues();
		 var oprId = formValues.oprId;
		 var callXh = formValues.receiveId;
		 var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"ACTIVE","comParams":{"OPRID":"' + oprId + '","callXh":"' + callXh + '"}}';
		 Ext.tzSubmit(tzParams,function(response){
			 if(response.success=="true"){
				 form.down("displayfield[name=bmrAccActiveStatus]").setValue("已激活");
			 }
		 });
	 },
	 confirmUpdatePsw:function(btn){
		 //this.saveInfo(btn);
		 //var form = this.getView().lookupReference("updatePswWindow");
		 //获取窗口
		 var win = btn.findParentByType("window");
		 //重置密码信息表单
		 var form = win.child("form").getForm();
			
		 if (!form.isValid()) {//表单校验未通过
			return false;
		 }
		 var formValues = form.getValues();
		 var oprId = formValues.oprId;
		 var callXh = formValues.receiveId;
		 var password = formValues.password;
		 
		 var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"UPDATEPSW","comParams":{"OPRID":"' + oprId + '","callXh":"' + callXh + '","password":"' + password + '"}}';
		 Ext.tzSubmit(tzParams,function(response){
			 win.close();
		 });
		 
	 },
	 invalidAccount:function(btn){
		 //this.saveInfo(btn);
		 var form = this.getView().lookupReference("userForm");		
		 var formValues = form.getValues();
		 var oprId = formValues.oprId;
		 var callXh = formValues.receiveId;
		 var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"INVALID","comParams":{"OPRID":"' + oprId + '","callXh":"' + callXh + '"}}';
		 Ext.tzSubmit(tzParams,function(response){
			 if(response.success=="true"){
				 form.down("displayfield[name=bmrLockStatus]").setValue("已锁定");
			 }			 
		 });
	 },
	 addBlackList:function(btn){
		 //this.saveInfo(btn);
		 var form = this.getView().lookupReference("userForm");		
		 var formValues = form.getValues();
		 var oprId = formValues.oprId;
		 var callXh = formValues.receiveId;
		 var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"ADDBLACK","comParams":{"OPRID":"' + oprId + '","callXh":"' + callXh + '"}}';
		 Ext.tzSubmit(tzParams,function(response){			 
			 if(response.success=="true"){
				 form.down("displayfield[name=bmrBlackList]").setValue("是");
			 }
		 });
	 },
	 
	//新建线索
    addEnrollmentClue:function(btn) {
    	 var form = this.getView().lookupReference("userForm");	
		 var formValues = form.getValues();
		 console.log(formValues);
		 var phone = formValues.phoneNum;
		 var name = formValues.bmrName;
    	
    	
        Ext.tzSetCompResourses("TZ_XSXS_INFO_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_XSXS_DETAIL_STD"];
        if(pageResSet=="" || pageResSet==undefined) {
            Ext.MessageBox.alert('提示','您没有访问或修改数据的权限');
        }
        //改功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className==""||className==undefined){
            Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID：TZ_XSXS_DETAIL_STD，请检查配置。');
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

        var myMask = new Ext.LoadMask(
            {
                msg    : TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00022"),
                target : Ext.getCmp('tranzvision-framework-content-panel')
            });

        myMask.show();

        var colorSortData = [],
            customerNameData = [],
            companyNameData = [];
        //类别
        var validColorSortStore =  new KitchenSink.view.common.store.comboxStore({
            recname: 'TZ_XS_XSLB_V',
            condition: {
                TZ_JG_ID: {
                    value: Ext.tzOrgID,
                    operator: '01',
                    type: '01'
                }
            },
            result: 'TZ_COLOUR_SORT_ID,TZ_COLOUR_NAME,TZ_COLOUR_CODE',
            listeners: {
                load: function (store, records, successful, eOpts) {
                    for (m = 0; m < records.length; m++) {
                        colorSortData.push(records[m].data);
                    }

                    //姓名
                    var customerNameStore = new KitchenSink.view.common.store.comboxStore({
                        pageSize: 0,
                        recname: 'TZ_XS_CUSNM_V',
                        condition: {
                            TZ_JG_ID: {
                                value: Ext.tzOrgID,
                                operator: '01',
                                type: '01'
                            }
                        },
                        result: 'TZ_KH_OPRID,TZ_REALNAME,TZ_DESCR_254',
                        listeners: {
                            load: function (store, records, successful, eOpts) {
                                for (m = 0; m < records.length; m++) {
                                    customerNameData.push(records[m].data);
                                }

                                //公司
                                var companyNameStore = new KitchenSink.view.common.store.comboxStore({
                                    pageSize: 0,
                                    recname: 'TZ_XS_COMNM_V',
                                    condition: {
                                        TZ_JG_ID: {
                                            value: Ext.tzOrgID,
                                            operator: '01',
                                            type: '01'
                                        }
                                    },
                                    result: 'TZ_COMP_CNAME',
                                    listeners: {
                                        load: function (store, records, successful, eOpts) {
                                            for (n = 0; n < records.length; n++) {
                                                companyNameData.push(records[n].data);
                                            }
                                            
                                            //标签
                                            var clueTagStore= new KitchenSink.view.common.store.comboxStore({
                                                recname:'TZ_LABEL_DFN_T',
                                                condition:{
                                                	TZ_JG_ID:{
                                                        value: Ext.tzOrgID,
                                                        operator:'01',
                                                        type:'01'
                                                    },
                                                    TZ_LABEL_STATUS:{
                                                        value: 'Y',
                                                        operator:'01',
                                                        type:'01'
                                                    }
                                                },
                                                result:'TZ_LABEL_ID,TZ_LABEL_NAME'
                                            });
                                            clueTagStore.load({
                                            	callback: function(){
                                            		
                                            		//其他责任人
                                            		var otherZrrStore= new KitchenSink.view.common.store.comboxStore({
                                                        recname:'TZ_XS_QTZRR_V',
                                                        condition:{
                                                        	TZ_LEAD_ID:{
                                                                value: "NEXT",
                                                                operator:'01',
                                                                type:'01'
                                                            }
                                                        },
                                                        result:'TZ_ZRR_OPRID,TZ_REALNAME'
                                                    });
                                            		
                                            		myMask.hide();

                                                    cmp = new ViewClass({
                                                        fromType: "ZSXS",
                                                        colorSortData: colorSortData,
                                                        customerNameData: customerNameData,
                                                        companyNameData: companyNameData,
                                                        clueTagStore: clueTagStore,
                                                        otherZrrStore: otherZrrStore,
                                                        zrrEditFalg: 'Y'
                                                    });

                                                    //操作标志
                                                    cmp.actType = "add";

                                                    cmp.on('afterrender', function () {
                                                        //线索信息表单
                                                        var form = cmp.child('form').getForm();
                                                        var currentOprid, currentName, currentLocalId, currentLocalName;
                                                        var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzGetCurrentName","comParams":{"cusMobile":"'+phone+'","name":"'+name+'"}}';
                                                        Ext.tzLoad(tzParams, function (responseData) {
                                                            currentOprid = responseData.currentOprid;
                                                            currentName = responseData.currentName;
                                                            currentLocalId = responseData.currentLocalId;
                                                            currentLocalName = responseData.currentLocalName;
                                                            
                                                            form.findField('chargeOprid').setValue(currentOprid);
                                                            form.findField('chargeName').setValue(currentName);
                                                            form.findField('cusMobile').setValue(phone);
                                                            form.findField('cusName').setValue(name);

                                                            //隐藏退回原因、关闭原因、建议跟进日期
                                                            form.findField("backReasonId").setHidden(true);
                                                            form.findField("closeReasonId").setHidden(true);
                                                            form.findField("contactDate").setHidden(true);
                                                        });
                                                    });

                                                    var tab = contentPanel.add(cmp);

                                                    //设置tab页签的beforeactivate事件的监听方法
                                                    //tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
                                                    contentPanel.setActiveTab(tab);

                                                    Ext.resumeLayouts(true);

                                                    if (cmp.floating) {
                                                        cmp.show();
                                                    }
                                            	}
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    },
	 
	 
	 //创建销售线索
	 createClue: function(btn){
		 var form = this.getView().lookupReference("userForm");	
		 var formValues = form.getValues();
		 console.log(formValues);
		 var oprId = formValues.oprId;
		 var callXh = formValues.receiveId;
		 var leadId = formValues.leadId;
		 var phone = formValues.phoneNum;
		 
		 if(leadId != null && leadId != ""){
			 Ext.Msg.alert("提示","接待单已创建过线索");
		 }else{
			 var tzParamsObj = {
				ComID: 'TZ_CALLCR_USER_COM',
				PageID: 'TZ_CALLC_USER_STD',
				OperateType: 'CREATECLUE',
				comParams: {
					OPRID: oprId,
					callXh: callXh,
					phoneNum: phone
				}
			 };
			 
			 var tzParams = Ext.JSON.encode(tzParamsObj);
			 Ext.tzSubmit(tzParams,function(result){	
				 console.log(result);
				 form.getForm().setValues(result);
			 }); 
		 }
	 },
	 saveInfo:function(btn){
		 var form = this.getView().lookupReference("userForm");		
		 var formValues = form.getValues();
		 console.log(formValues);
		 
		 var grid = form.child("grid[name=bmInfoList]");
		 var store = grid.getStore();
		 var modiRecords = store.getModifiedRecords();
		 var mRecords=[];
		 for(var i=0;i<modiRecords.length;i++){
			 console.log(modiRecords[i]);
			 mRecords.push(modiRecords[i].data);
	     }
		 
		 var oprId = formValues.oprId;
		 var callXh = formValues.receiveId;
		 var dealwithZT = formValues.dealwithZT;
		 var callDesc = formValues.callDesc;		 
		 var tzParams = {
		     ComID:"TZ_CALLCR_USER_COM",
		     PageID:"TZ_CALLC_USER_STD",
		     OperateType:"SAVEINFO",
		     comParams:{
		    	 OPRID:oprId,
		    	 callXh:callXh,
		    	 dealwithZT:dealwithZT,
		    	 callDesc:callDesc,
		    	 mRecords:mRecords
		     }
		 }
		 //var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"SAVEINFO","comParams":{"OPRID":"' + oprId + '","callXh":"' + callXh + '","dealwithZT":"' + dealwithZT + '","callDesc":"' + Ext.JSON.encode(callDesc) + '"}}';
		 Ext.tzSubmit(Ext.JSON.encode(tzParams),function(response){
			 if(modiRecords.length>0){
				 store.reload();
			 }
		 }); 
	 },
	 search:function(){
		 var me = this;
		 var form = this.getView().lookupReference("userForm");		
		 var formValues = form.getValues();
		 
		 var callXh = formValues.receiveId;
		 var searchName = formValues.searchName;
		 var searchPhone = formValues.searchPhone;
		 var searchEmail = formValues.searchEmail;
		 var searchMshId = formValues.searchMshId;
		 var callPhone = formValues.phoneNum;
		 
		 if(searchName!=""||searchPhone!=""||searchEmail!=""||searchMshId!=""){
			 var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"SEARCHUSER","comParams":{"phone":"' + searchPhone + '","email":"' + searchEmail + '","callXh":"' + callXh + '","name":"' + searchName + '","mshId":"' + searchMshId + '"}}';
			 Ext.tzLoadAsync(tzParams,function(response){
				oprid = response.OPRID;
				var count = response.PSNCOUNT;
				if(count>1){
					Ext.MessageBox.alert("提示","搜索到多个考生，请增加搜索条件");
				}else{
					//historyCount = response.viewHistoryCall;
					if(oprid!=null&&oprid!=undefined&&oprid!=""){
						tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"QF","comParams":{"OPRID":"' + oprid + '","callXh":"' + callXh + '","phone":"' + callPhone +'","type":""}}';
						var formData;
						Ext.tzLoadAsync(tzParams,function(response){
							//formData = response;
							form.getForm().setValues(response);
							if(response.titleImageUrl){
								form.down('image[name=titileImage]').setSrc(TzUniversityContextPath + response.titleImageUrl);	
							}else{
								form.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/images/tranzvision/mrtx02.jpg");
							}
							if(response.bmrBmActCount){								
								form.down("button[name=bmrBmActCount]").setText('<span style="text-decoration:underline;color:blue;">' + response.bmrBmActCount + '</span>');
							}else{
								form.down("button[name=bmrBmActCount]").setText('<span style="text-decoration:underline;color:blue;">0</span>');
							}
						});	
						var Grid = form.down("grid[name=bmInfoList]");
						var store = Grid.getStore();
						var tzStoreParams = '{"cfgSrhId":"TZ_CALLCR_USER_COM.TZ_CALLC_USER_STD.TZ_USER_CALL1_VW","condition":{"OPRID-operator": "01","OPRID-value": "'+ oprid+'"}}';

						store.tzStoreParams = tzStoreParams;
						store.load();
						
						//加载线索
						var clueGrid = form.down("grid[name=ksClueList]");
						var clueGridStore = clueGrid.getStore();
						clueGridStore.tzStoreParams = '{"type": "CLUELIST", "oprid": "' + oprid + '"}';
						clueGridStore.load();
						
						//将按钮恢复点击
						me.disabledButton(form,false);
					}else{
						Ext.MessageBox.alert("提示","搜索不到考生信息");
					}
				}
				
			 });
		 }else{
			 Ext.MessageBox.alert("提示","请输入搜索条件，搜索考生");
		 }
	 },
	 
	 //可配置搜索
	 searchCallList: function(btn){
		 Ext.tzShowCFGSearch({
			 cfgSrhId: 'TZ_CALLCR_USER_COM.TZ_CALLC_LIST_STD.TZ_CCLIST_VW',
			 condition:
	            {
	                "TZ_DLZH_ID": Ext.tzOrgID     
	            },  
			 callback: function(seachCfg){
				 var store = btn.findParentByType("grid").store;
				 store.tzStoreParams = seachCfg;
				 store.load();
			 }
		 });	
	 },
	 viewCallData:function(view, rowIndex, colIndex, item, e, record, row){
		 var me = this;
		 	var phone = record.data.callPhone;
		 	var type = record.data.callType;
		 	var callXh = record.data.receiveId;
		 	var oprid = record.data.callOprid;
		 	var historyCount = record.data.viewHistoryCall;
			var actCount = record.data.bmrBmActCount;
			
		 	//是否有访问权限
		 	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CALLCR_USER_COM"]["TZ_CALLC_USER_STD"];
		 	if( pageResSet == "" || pageResSet == undefined){
			 	Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CALLC_USER_STD，请检查配置。');
				return;
			}
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			
			contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
			contentPanel.body.addCls('kitchensink-example');

			//className = 'KitchenSink.view.basicData.systemVar.systemVarInfoPanel';
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
			cmp.actType="edit";
			
			cmp.on('afterrender',function(panel){
				//用户账号信息表单
				/*var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"GETUSER","comParams":{"phone":"' + phone + '","type":"' + type + '","callXh":"' + callXh + '"}}';
				var oprid = "";
				var historyCount;
				var actCount;
				
				Ext.tzLoadAsync(tzParams,function(response){
					oprid = response.OPRID;
					historyCount = response.viewHistoryCall;
					actCount = response.bmrBmActCount;
				});*/
				
				
				var _this = panel.child('form');
				
				var form = _this.getForm();
				var Grid = _this.down("grid[name=bmInfoList]");
				var buttonT = panel.child('form').down("button[name=historyCount]");
				var buttonAct = panel.child('form').down("button[name=bmrBmActCount]");
				var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"QF","comParams":{"OPRID":"' + oprid + '","type":"' + type + '","callXh":"' + callXh + '","phone":"' + phone +'"}}';
				
				Ext.tzLoadAsync(tzParams,function(responseData){
					//系统变量信息数据
					form.setValues(responseData);
					
					if(responseData.titleImageUrl){
						_this.down('image[name=titileImage]').setSrc(TzUniversityContextPath + responseData.titleImageUrl);	
					}else{
						_this.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/images/tranzvision/mrtx02.jpg");
					}
					if(responseData.viewHistoryCall){
						buttonT.setText('<span style="text-decoration:underline;color:blue;">查看历史来电记录（' + responseData.viewHistoryCall + '）</span>');
					}else{						
						buttonT.setText('<span style="text-decoration:underline;color:blue;">查看历史来电记录（0）</span>');
					}
					//参与的活动数
					if(responseData.bmrBmActCount){
						buttonAct.setText('<span style="text-decoration:underline;color:blue;">' + responseData.bmrBmActCount + '</span>');
					}else{
						buttonAct.setText('<span style="text-decoration:underline;color:blue;">0</span>');
					}
				});							
				
				if(oprid==null||oprid==""||oprid==undefined){
					/*禁用按钮*/
					me.disabledButton(_this,true);
					_this.down("button[name=createClue]").removeCls('x-item-disabled x-btn-disabled');
				}else{
					var store = Grid.getStore();
					var tzStoreParams = '{"cfgSrhId":"TZ_CALLCR_USER_COM.TZ_CALLC_USER_STD.TZ_USER_CALL1_VW","condition":{"OPRID-operator": "01","OPRID-value": "'+ oprid+'"}}';

					store.tzStoreParams = tzStoreParams;
					store.load();
					
					
					//加载线索
					var clueGrid = _this.down("grid[name=ksClueList]");
					var clueGridStore = clueGrid.getStore();
					clueGridStore.tzStoreParams = '{"type": "CLUELIST", "oprid": "' + oprid + '"}';
					clueGridStore.load();
				}
			});
			
			tab = contentPanel.add(cmp);     
			
			contentPanel.setActiveTab(tab);

			Ext.resumeLayouts(true);

			if (cmp.floating) {
				cmp.show();
			}
	 },
	 viewHistoryCall:function(btn){
		 Ext.tzSetCompResourses("TZ_CALLCR_USER_COM");
		 
		 var form = this.getView().lookupReference("userForm");
		 var callXh = form.down("textfield[name=receiveId]").getValue();
		 var phone = form.down("textfield[name=phoneNum]").getValue();
		 var name = form.down("displayfield[name=bmrName]").getValue();
		 var gender = form.down("displayfield[name=bmrGender]").getValue();
 		 //是否有访问权限
	     var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CALLCR_USER_COM"]["TZ_CALLC_HIST_STD"];
	     if( pageResSet == "" || pageResSet == undefined){
	         Ext.MessageBox.alert('提示', '您没有修改数据的权限');
	         return;
	     }
	     //该功能对应的JS类
	     var className = pageResSet["jsClassName"];
	     if(className == "" || className == undefined){
	         Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CALLC_HIST_STD，请检查配置。');
	         return;
	     }

	     var win = this.lookupReference('historyCallWindow');

	     if (!win) {
	    	 Ext.syncRequire(className);
	         ViewClass = Ext.ClassManager.get(className);
	         //新建类
	         win = new ViewClass();
	         this.getView().add(win);
	     }

	      //操作类型设置为更新
	      win.actType = "update";

	      var form = win.child("form").getForm();
	      var pageGrid = Ext.getCmp('historyCallGrid');

	      form.setValues(
	            [
	                {id:'callPhone', value:phone},
	                {id:'bmrName', value:name},
	                {id:'bmrGender',value:gender}
	            ]
	        );
	        
	        pageGrid.store.tzStoreParams = '{"cfgSrhId":"TZ_CALLCR_USER_COM.TZ_CALLC_LIST_STD.TZ_CCLIST_VW","condition":{"TZ_PHONE-operator": "01","TZ_PHONE-value": "'+ phone+'","TZ_XH-operator":"02","TZ_XH-value":"' + callXh + '"}}';
	        pageGrid.store.load();

	        win.show();
	 },
	 
	 disabledButton: function(form,disalbed){
		 
		 form.down("button[name=viewUserBtn]").disabled=disalbed;		 
		 form.down("button[name=activeAccount]").disabled=disalbed;		 
		 form.down("button[name=updatePsw]").disabled=disalbed;		 
		 form.down("button[name=invalidAccount]").disabled=disalbed;		 
		 form.down("button[name=addBlackList]").disabled=disalbed;
		 //form.down("button[name=createClue]").disabled=disalbed;
		 
		 if(disalbed){
			 form.down("button[name=viewUserBtn]").addCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=activeAccount]").addCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=updatePsw]").addCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=invalidAccount]").addCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=addBlackList]").addCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=createClue]").addCls('x-item-disabled x-btn-disabled');
		 }else{
			 form.down("button[name=viewUserBtn]").removeCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=activeAccount]").removeCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=updatePsw]").removeCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=invalidAccount]").removeCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=addBlackList]").removeCls('x-item-disabled x-btn-disabled');
			 form.down("button[name=createClue]").removeCls('x-item-disabled x-btn-disabled');
		 }
	 },
	 
	 sendSms: function(btn){
		 var form = this.getView().lookupReference("userForm");		
		 var formValues = form.getValues();
		 console.log(formValues);
		 var callXh = formValues.receiveId;
		 var phone = formValues.phoneNum;
		 var smsModel = formValues.smsModel;
		 var oprId = formValues.oprId;
		 
		 var comParams = '"oprId":"' + oprId + '","phone":"' + phone + '"'; 
			 
		 var tzParams = '{"ComID":"TZ_CALLCR_USER_COM","PageID":"TZ_CALLC_USER_STD","OperateType":"tzSendMessage","comParams":{'+comParams+'}}';
	        Ext.tzLoad(tzParams,function(responseData){
	            Ext.tzSendSms({
	                //发送的短信模板;
	                "SmsTmpName": [smsModel],
	                //发送的听众;
	                "audienceId": responseData.audienceId
	            })
	        });
		 
	 },
	 
	 viewHistoryAct:function(btn){
		 Ext.tzSetCompResourses("TZ_CALLCR_USER_COM");
		 
		 var form = this.getView().lookupReference("userForm");
		 var callXh = form.down("textfield[name=receiveId]").getValue();
		 var phone = form.down("textfield[name=phoneNum]").getValue();
		 var name = form.down("displayfield[name=bmrName]").getValue();
		 var gender = form.down("displayfield[name=bmrGender]").getValue();
		 var oprId = form.down("textfield[name=oprId]").getValue();
		 
 		 //是否有访问权限
	     var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CALLCR_USER_COM"]["TZ_CALLC_ACT_STD"];
	     if( pageResSet == "" || pageResSet == undefined){
	         Ext.MessageBox.alert('提示', '您没有修改数据的权限');
	         return;
	     }
	     //该功能对应的JS类
	     var className = pageResSet["jsClassName"];
	     if(className == "" || className == undefined){
	         Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CALLC_ACT_STD，请检查配置。');
	         return;
	     }

	     var win = this.lookupReference('viewHisBmActWindow');

	     if (!win) {
	    	 Ext.syncRequire(className);
	         ViewClass = Ext.ClassManager.get(className);
	         //新建类
	         win = new ViewClass();
	         this.getView().add(win);
	     }

	      //操作类型设置为更新
	      win.actType = "update";

	      var form = win.child("form").getForm();
	      var pageGrid = Ext.getCmp('historyActGrid');

	      form.setValues(
	            [
	                {id:'callPhone', value:phone},
	                {id:'bmrName', value:name},
	                {id:'bmrGender',value:gender}
	            ]
	        );
	        
	        //pageGrid.store.tzStoreParams = '{"cfgSrhId":"TZ_CALLCR_USER_COM.TZ_CALLC_ACT_STD.TZ_CCALL_HD_VW","condition":{"TZ_ZY_SJ-operator": "01","TZ_ZY_SJ-value": "'+ phone+'","TZ_JG_ID-operator":"01","TZ_JG_ID-value":"' + Ext.tzOrgID + '"}}';
	      pageGrid.store.tzStoreParams = '{"TZ_JG_ID":"' + Ext.tzOrgID + '","TZ_ZY_SJ":"' + phone + '","oprId":"' + oprId + '"}'
	        pageGrid.store.load();

	        win.show();
	 },
	 updatePsw:function(btn){
		 Ext.tzSetCompResourses("TZ_CALLCR_USER_COM");
		 
		 var form = this.getView().lookupReference("userForm");
		 var oprid = form.down("textfield[name=oprId]").getValue();
		 var callXh = form.down("textfield[name=receiveId]").getValue();
		 var phone = form.down("textfield[name=phoneNum]").getValue();
		 var name = form.down("displayfield[name=bmrName]").getValue();
		 var gender = form.down("displayfield[name=bmrGender]").getValue();
 		 //是否有访问权限
	     var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CALLCR_USER_COM"]["TZ_CALLC_UPW_STD"];
	     if( pageResSet == "" || pageResSet == undefined){
	         Ext.MessageBox.alert('提示', '您没有修改数据的权限');
	         return;
	     }
	     //该功能对应的JS类
	     var className = pageResSet["jsClassName"];
	     if(className == "" || className == undefined){
	         Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CALLC_UPW_STD，请检查配置。');
	         return;
	     }

	     var win = this.lookupReference('updatePswWindow');

	     if (!win) {
	    	 Ext.syncRequire(className);
	         ViewClass = Ext.ClassManager.get(className);
	         //新建类
	         win = new ViewClass();
	         this.getView().add(win);
	     }

	      //操作类型设置为更新
	      win.actType = "update";

	      var form = win.child("form").getForm();
	      console.log(oprid,callXh);
	      form.setValues(
	            [
	            	{id:'receiveId',value:callXh},
	            	{id:'oprId',value:oprid},
	                {id:'callPhone', value:phone},
	                {id:'bmrName', value:name},
	                {id:'bmrGender',value:gender}
	            ]
	        );

	        win.show();
	 },
	 
	 //查看线索
	 viewClueInfo: function(view, rowIndex, colIndex){
		 	var store = view.findParentByType("grid").store;
	        var selRec = store.getAt(rowIndex);
	        var clueId = selRec.get("clueId");
	        
	        Ext.tzSetCompResourses("TZ_XSXS_INFO_COM");
	        //是否有访问权限
	        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_XSXS_DETAIL_STD"];
	        if (pageResSet == "" || pageResSet == undefined) {
	            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
	            return;
	        }
	        //该功能对应的JS类
	        var className = pageResSet["jsClassName"];
	        if (className == "" || className == undefined) {
	            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_XSXS_DETAIL_STD，请检查配置。');
	            return;
	        }

	        var contentPanel, cmp, className, ViewClass, clsProto;
	        var themeName = Ext.themeName;

	        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
	        contentPanel.body.addCls('kitchensink-example');
	        if (!Ext.ClassManager.isCreated(className)) {
	            Ext.syncRequire(className);
	        }
	        ViewClass = Ext.ClassManager.get(className);

	        //获取线索信息中的下拉框值
	        var backReasonId, backReasonName, closeReasonId, closeReasonName, colorTypeId, colorTypeName, colorTypeCode;
	        var backReasonFlag, closeReasonFlag, colorTypeFlag;
	        var i, j, m, n;
	        var backReasonData = [],
	            closeReasonData = [],
	            colorSortData = [],
	            customerNameData = [],
	            companyNameData = [];
	        var tzDropDownParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzGetDropDownInfo","comParams":{"clueId":"' + clueId + '"}}';
	        Ext.tzLoad(tzDropDownParams, function (respData) {
	            backReasonId = respData.backReasonId;
	            backReasonName = respData.backReasonName;
	            closeReasonId = respData.closeReasonId;
	            closeReasonName = respData.closeReasonName;
	            colorTypeId = respData.colorTypeId;
	            colorTypeName = respData.colorTypeName;
	            colorTypeCode = respData.colorTypeCode;


	            var myMask = new Ext.LoadMask(
	                {
	                    msg    : TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00022"),
	                    target : Ext.getCmp('tranzvision-framework-content-panel')
	                });

	            myMask.show();


	            //退回原因
	            var validBackReasonStore = new KitchenSink.view.common.store.comboxStore({
	                recname: 'TZ_XS_THYY_V',
	                condition: {
	                    TZ_JG_ID: {
	                        value: Ext.tzOrgID,
	                        operator: '01',
	                        type: '01'
	                    }
	                },
	                result: 'TZ_THYY_ID,TZ_LABEL_NAME',
	                listeners: {
	                    load: function (store, records, successful, eOpts) {
	                        for (i = 0; i < records.length; i++) {
	                            backReasonData.push(records[i].data);
	                            if (backReasonId.length == 0 || records[i].data["TZ_THYY_ID"] == backReasonId) {
	                                backReasonFlag = "Y";
	                            }
	                        }
	                        if (backReasonFlag == "Y") {

	                        } else {
	                            backReasonData.push({
	                                TZ_THYY_ID: backReasonId,
	                                TZ_LABEL_NAME: backReasonName
	                            });
	                        }

	                        //关闭原因
	                        var validCloseReasonStore = new KitchenSink.view.common.store.comboxStore({
	                            recname: 'TZ_XS_GBYY_V',
	                            condition: {
	                                TZ_JG_ID: {
	                                    value: Ext.tzOrgID,
	                                    operator: '01',
	                                    type: '01'
	                                }
	                            },
	                            result: 'TZ_GBYY_ID,TZ_LABEL_NAME',
	                            listeners: {
	                                load: function (store, records, successful, eOpts) {
	                                    for (j = 0; j < records.length; j++) {
	                                        closeReasonData.push(records[j].data);
	                                        if (closeReasonId.length == 0 || records[j].data["TZ_GBYY_ID"] == closeReasonId) {
	                                            closeReasonFlag = "Y";
	                                        }
	                                    }
	                                    if (closeReasonFlag == "Y") {

	                                    } else {
	                                        closeReasonData.push({
	                                            TZ_GBYY_ID: closeReasonId,
	                                            TZ_LABEL_NAME: closeReasonName
	                                        });
	                                    }

	                                    //类别
	                                    var validColorSortStore = new KitchenSink.view.common.store.comboxStore({
	                                        recname: 'TZ_XS_XSLB_V',
	                                        condition: {
	                                            TZ_JG_ID: {
	                                                value: Ext.tzOrgID,
	                                                operator: '01',
	                                                type: '01'
	                                            }
	                                        },
	                                        result: 'TZ_COLOUR_SORT_ID,TZ_COLOUR_NAME,TZ_COLOUR_CODE',
	                                        listeners: {
	                                            load: function (store, records, successful, eOpts) {
	                                                for (k = 0; k < records.length; k++) {
	                                                    colorSortData.push(records[k].data);
	                                                    if (colorTypeId.length == 0 || records[k].data["TZ_COLOUR_SORT_ID"] == colorTypeId) {
	                                                        colorTypeFlag = "Y";
	                                                    }
	                                                }
	                                                if (colorTypeFlag == "Y") {

	                                                } else {
	                                                    colorSortData.push({
	                                                        TZ_COLOUR_SORT_ID: colorTypeId,
	                                                        TZ_COLOUR_NAME: colorTypeName,
	                                                        TZ_COLOUR_CODE: colorTypeCode
	                                                    });
	                                                }


	                                                //姓名
	                                                var customerNameStore = new KitchenSink.view.common.store.comboxStore({
	                                                    pageSize: 0,
	                                                    recname: 'TZ_XS_CUSNM_V',
	                                                    condition: {
	                                                        TZ_JG_ID: {
	                                                            value: Ext.tzOrgID,
	                                                            operator: '01',
	                                                            type: '01'
	                                                        }
	                                                    },
	                                                    result: 'TZ_KH_OPRID,TZ_REALNAME,TZ_DESCR_254',
	                                                    listeners: {
	                                                        load: function (store, records, successful, eOpts) {
	                                                            for (m = 0; m < records.length; m++) {
	                                                                customerNameData.push(records[m].data);
	                                                            }


	                                                            //公司
	                                                            var companyNameStore = new KitchenSink.view.common.store.comboxStore({
	                                                                pageSize: 0,
	                                                                recname: 'TZ_XS_COMNM_V',
	                                                                condition: {
	                                                                    TZ_JG_ID: {
	                                                                        value: Ext.tzOrgID,
	                                                                        operator: '01',
	                                                                        type: '01'
	                                                                    }
	                                                                },
	                                                                result: 'TZ_COMP_CNAME',
	                                                                listeners: {
	                                                                    load: function (store, records, successful, eOpts) {
	                                                                        for (n = 0; n < records.length; n++) {
	                                                                            companyNameData.push(records[n].data);
	                                                                        }

	                                                                        //线索标签
	                                                                        var clueTagStore= new KitchenSink.view.common.store.comboxStore({
	                                                                            recname:'TZ_LABEL_DFN_T',
	                                                                            condition:{
	                                                                            	TZ_JG_ID:{
	                                                                                    value: Ext.tzOrgID,
	                                                                                    operator:'01',
	                                                                                    type:'01'
	                                                                                },
	                                                                                TZ_LABEL_STATUS:{
	                                                                                    value: 'Y',
	                                                                                    operator:'01',
	                                                                                    type:'01'
	                                                                                }
	                                                                            },
	                                                                            result:'TZ_LABEL_ID,TZ_LABEL_NAME'
	                                                                        });
	                                                                        clueTagStore.load({
	                                                                        	callback: function(){
	                                                                        		
	                                                                        		var otherZrrStore= new KitchenSink.view.common.store.comboxStore({
	                                                                                    recname:'TZ_XS_QTZRR_V',
	                                                                                    condition:{
	                                                                                    	TZ_LEAD_ID:{
	                                                                                            value: clueId,
	                                                                                            operator:'01',
	                                                                                            type:'01'
	                                                                                        }
	                                                                                    },
	                                                                                    result:'TZ_ZRR_OPRID,TZ_REALNAME'
	                                                                                });
	                                                                        		otherZrrStore.load({
	                                                                                	callback: function(){
	                                                                                		myMask.hide();

	                                                                                        cmp = new ViewClass({
	                                                                                            clueID: clueId,
	                                                                                            backReasonData: backReasonData,
	                                                                                            closeReasonData: closeReasonData,
	                                                                                            colorSortData: colorSortData,
	                                                                                            customerNameData: customerNameData,
	                                                                                            companyNameData: companyNameData,
	                                                                                            clueTagStore: clueTagStore,
	                                                                                            otherZrrStore: otherZrrStore,
	                                                                                            zrrEditFalg: 'Y'
	                                                                                        });

	                                                                                        cmp.on('afterrender', function (panel) {
	                                                                                            var form = panel.child('form').getForm();
	                                                                                            var tabpanel = panel.child('tabpanel');
	                                                                                            var store = tabpanel.child("grid").store;
	                                                                                            var glBmbBut = panel.down("button[name=glBmbBut]");
	                                                                                            //参数
	                                                                                            var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"QF","comParams":{"clueId":"' + clueId + '"}}';
	                                                                                            Ext.tzLoad(tzParams, function (respData) {
	                                                                                                var formData = respData.formData;
	                                                                                                //基本信息
	                                                                                                form.setValues(formData);

	                                                                                                //根据显示状态显示相应的其他字段
	                                                                                                var clueState = form.findField('clueState').getValue();

	                                                                                                form.findField('backReasonId').setVisible(false);
	                                                                                                form.findField('closeReasonId').setVisible(false);
	                                                                                                form.findField('contactDate').setVisible(false);
	                                                                                                form.findField('backReasonId').allowBlank = true;
	                                                                                                form.findField('closeReasonId').allowBlank = true;
	                                                                                                form.findField('contactDate').allowBlank = true;

	                                                                                                //退回原因
	                                                                                                if (clueState == "F") {
	                                                                                                    form.findField('backReasonId').setVisible(true);
	                                                                                                    form.findField('backReasonId').allowBlank = false;
	                                                                                                }
	                                                                                                //关闭原因
	                                                                                                if (clueState == "G") {
	                                                                                                    form.findField('closeReasonId').setVisible(true);
	                                                                                                    form.findField('closeReasonId').allowBlank = false;
	                                                                                                }
	                                                                                                //建议跟进日期
	                                                                                                if (clueState == "D") {
	                                                                                                    form.findField('contactDate').setVisible(true);
	                                                                                                    form.findField('contactDate').allowBlank = false;
	                                                                                                }


	                                                                                                //线索状态为关闭或者报考状态不是未报名，关联报名表按钮隐藏
	                                                                                                var bkStatus = form.findField("bkStatus").getValue();
	                                                                                                if (clueState == "G" || bkStatus != "A") {
	                                                                                                    glBmbBut.setVisible(false);
	                                                                                                }

	                                                                                                //加载报名表信息
	                                                                                                var clueId = form.findField("clueId").getValue();
	                                                                                                store.tzStoreParams = '{"clueId":"' + clueId + '"}';
	                                                                                                store.load();
	                                                                                            });
	                                                                                        });

	                                                                                        var tab = contentPanel.add(cmp);
	                                                                                        contentPanel.setActiveTab(tab);
	                                                                                        Ext.resumeLayouts(true);
	                                                                                        if (cmp.floating) {
	                                                                                            cmp.show();
	                                                                                        }
	                                                                                	}
	                                                                                });
	                                                                        	}
	                                                                        });
	                                                                    }
	                                                                }
	                                                            });
	                                                        }
	                                                    }
	                                                });
	                                            }
	                                        }
	                                    });
	                                }
	                            }
	                        });
	                    }
	                }
	            });
	        });
	 }
});