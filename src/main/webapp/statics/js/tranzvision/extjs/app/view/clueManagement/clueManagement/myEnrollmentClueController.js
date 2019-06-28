Ext.define('KitchenSink.view.clueManagement.clueManagement.myEnrollmentClueController',{
    extend:'Ext.app.ViewController',
    alias:'controller.myEnrollmentClueController',
    //查询
    searchMyEnrollmentClue:function(btn){
        var grid=btn.findParentByType("grid");
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_XSXS_MYXS_COM.TZ_XSXS_MYXS_STD.TZ_XSXS_INFO_VW',
            condition:{
                "TZ_JG_ID": Ext.tzOrgID,
                "TZ_LEAD_STATUS":"G"
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                btn.findParentByType('grid').getStore().clearFilter();//查询基于可配置搜索，清除预设的过滤条件
                store.tzStoreParams = seachCfg;
                grid.searchCond = seachCfg;
                store.load();

                var tzParams = '{"ComID":"TZ_XSXS_DRDC_COM","PageID":"TZ_XSXS_DRDC_STD","OperateType":"getSearchSql","comParams":'+seachCfg+'}';
                Ext.tzLoad(tzParams,function(responseData){
                    var getedSQL = responseData.searchSql;
                    grid.getedSQL = getedSQL;
                });
            }
        });
    },
    //新建线索
    addMyEnrollmentClue:function(btn) {
        Ext.tzSetCompResourses("TZ_XSXS_INFO_COM");     
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_XSXS_DETAIL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_XSXS_DETAIL_STD，请检查配置。');
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

        //类别
        var colorSortData = [];
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
		                        fromType:"MYXS",
		                        colorSortData:colorSortData,
		                        clueTagStore: clueTagStore,
                                otherZrrStore: otherZrrStore
		                    });
		
		                    //操作标志
		                    cmp.actType="add";
		
		                    cmp.on('afterrender',function(){
		                        //线索信息表单
		                        var form = cmp.child('form').getForm();
		                        var currentOprid,currentName,currentLocalId,currentLocalName;
		                        var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzGetCurrentName","comParams":{}}';
		                        Ext.tzLoad(tzParams,function(responseData){
		                        	
		                            currentOprid=responseData.currentOprid;
		                            currentName=responseData.currentName;
		                            currentLocalId=responseData.currentLocalId;
		                            currentLocalName=responseData.currentLocalName;
		                            
		                            form.findField('chargeOprid').setValue(currentOprid);
		                            form.findField('chargeName').setValue(currentName);
		
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
    },
    //Grid中每一行的编辑
    editSelClueInfo: function (view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var clueId = selRec.get("clueId");
        this.editClueInfoByID(clueId);
    },
    //根据线索ID进行编辑
    editClueInfoByID:function(clueId) {
        Ext.tzSetCompResourses("TZ_XSXS_INFO_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_XSXS_DETAIL_STD"];
        if(pageResSet==""||pageResSet==undefined) {
            Ext.MessageBox.alert('提示','您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className==""||className==undefined) {
            Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID为：TZ_XSXS_DETAIL_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp,className,ViewClass,clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        if(!Ext.ClassManager.isCreated(className)) {
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        
        //获取线索信息中的下拉框值
        var backReasonId,backReasonName,closeReasonId,closeReasonName,colorTypeId,colorTypeName,colorTypeCode,zxlbId,zxlbName;
        var backReasonFlag,closeReasonFlag,colorTypeFlag,zxlbFlag;
        var i, j, m,n;
        var backReasonData = [],
            closeReasonData = [],
            colorSortData = [],
            customerNameData = [],
            companyNameData = [];
        var tzDropDownParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzGetDropDownInfo","comParams":{"clueId":"'+clueId+'"}}';
        Ext.tzLoad(tzDropDownParams,function(respData){
            backReasonId=respData.backReasonId;
            backReasonName=respData.backReasonName;
            closeReasonId=respData.closeReasonId;
            closeReasonName=respData.closeReasonName;
            colorTypeId=respData.colorTypeId;
            colorTypeName=respData.colorTypeName;
            colorTypeCode=respData.colorTypeCode;


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
                        value:Ext.tzOrgID,
                        operator: '01',
                        type: '01'
                    }
                },
                result: 'TZ_THYY_ID,TZ_LABEL_NAME',
                listeners:{
                    load:function(store,records,successful,eOpts) {
                        for(i=0;i<records.length;i++) {
                            backReasonData.push(records[i].data);
                            if(backReasonId.length==0||records[i].data["TZ_THYY_ID"]==backReasonId){
                                backReasonFlag="Y";
                            }
                        }
                        if(backReasonFlag=="Y") {
                        } else {
                            backReasonData.push({
                                TZ_THYY_ID:backReasonId,
                                TZ_LABEL_NAME:backReasonName
                            });
                        }
                        
                        //关闭原因
                        var validCloseReasonStore = new KitchenSink.view.common.store.comboxStore({
                            recname: 'TZ_XS_GBYY_V',
                            condition: {
                            	TZ_JG_ID: {
                                    value:Ext.tzOrgID,
                                    operator: '01',
                                    type: '01'
                                }
                            },
                            result: 'TZ_GBYY_ID,TZ_LABEL_NAME',
                            listeners:{
                                load:function(store,records,successful,eOpts){
                                    for(j=0;j<records.length;j++) {
                                        closeReasonData.push(records[j].data);
                                        if(closeReasonId.length==0||records[j].data["TZ_GBYY_ID"]==closeReasonId){
                                            closeReasonFlag="Y";
                                        }
                                    }
                                    if(closeReasonFlag=="Y") {

                                    } else {
                                        closeReasonData.push({
                                            TZ_GBYY_ID:closeReasonId,
                                            TZ_LABEL_NAME:closeReasonName
                                        });
                                    }

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
                                                    if (colorTypeId.length == 0 || records[m].data["TZ_COLOUR_SORT_ID"] == colorTypeId) {
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
                                                                    otherZrrStore: otherZrrStore
				                                                });
				
				
				                                                cmp.on('afterrender', function (panel) {
				                                                    var form = panel.child('form').getForm();
				                                                    var store = panel.child('tabpanel').child("grid").store;
				                                                    var glBmbBut = panel.down("button[name=glBmbBut]");
				                                                    //参数
				                                                    var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"QF","comParams":{"clueId":"' + clueId + '"}}';
				                                                    Ext.tzLoad(tzParams, function (respData) {
				                                                        var formData = respData.formData;
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
				
				                                                tab = contentPanel.add(cmp);
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
        });
    },
    //更多操作-导出查询结果
    exportSearchExcel:function(btn) {
        var panel = btn.findParentByType('grid');

        //构造搜索sql
        if((typeof panel.getedSQL) == "undefined"){
            searchSql = "default";
        }else{
            searchSql = panel.getedSQL;
        }

        var comParamsObj = {
            ComID: "TZ_XSXS_DRDC_COM",
            PageID: "TZ_XSXS_DRDC_STD",
            OperateType: "EXPORT",
            comParams: {
                exportType:'MYXS',
                searchSql: searchSql
            }
        };


        var className = 'KitchenSink.view.clueManagement.clueManagement.export.exportExcelWindow';
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass({
            exportType:'MYXS',
            exportObj: comParamsObj
        });

        win.show();
    },
    //更多操作-查看历史导出并下载
    downloadHisExcel:function(btn) {
        var panel = btn.findParentByType('enrollmentCluePanel');

        var className = 'KitchenSink.view.clueManagement.clueManagement.export.exportExcelWindow';

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        var ViewClass = Ext.ClassManager.get(className);
        var win = new ViewClass({
            exportType:'MYXS',
            type: 'download'
        });

        var tabPanel = win.lookupReference("packageTabPanel");
        tabPanel.setActiveTab(1);

        win.show();
    },
    //更多操作-批量导入线索列表
    importExcel:function(btn) {
        Ext.tzImport({
            /* importType导入类型 A-传Excel;B-粘贴Excel数据 */
            importType:'A',
            /* 导入模板编号 */
            tplResId:'TZ_MYXS_IMP',
            /* businessHandler 预览导入的数据之后点击下一步执行的函数，根据页面的需求自由编写，columnArray为解析Excel后的标题行数组（如果未勾选首行是标题行columnArray=[]） */
            /* dataArray为解析后的Excel二维数组数据（勾选了首行是标题行则dataArray不包含首行数据）*/
            businessHandler:function(columnArray,dataArray) {
            	console.log(columnArray);
            	console.log(dataArray);
                Ext.tzSetCompResourses("TZ_XSXS_DRDC_COM");
                //是否有访问权限
                var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_DRDC_COM"]["TZ_XSXS_IMP_STD"];
                if (pageResSet == "" || pageResSet == undefined) {
                    Ext.MessageBox.alert('提示','您没有修改数据的权限');
                    return;
                }

                //该功能对应的JS类
                var className = pageResSet["jsClassName"];
                if (className == "" || className == undefined) {
                    Ext.MessageBox.alert('提示','为找到该功能页面对应的JS类，页面ID为：TZ_XSXS_IMP_STD，请检查配置。');
                    return;
                }

                var win = this.lookupReference('clueImportWindow');
                if (!win) {
                    Ext.syncRequire(className);
                    ViewClass = Ext.ClassManager.get(className);
                    //新建类
                    win = new ViewClass({
                        impType:'MYXS'
                    });
                }

                var windowgrid=win.child('grid');

                windowgrid.store.loadData(dataArray);

                var winform = win.child("form").getForm();
                var strtmp = "共 ";
                strtmp = strtmp+"<span style='color:red;font-size:22px'>"+dataArray.length+"</span>";
                strtmp = strtmp+" 条数据";
                var formpara = {ImpClueCount:strtmp};
                winform.setValues(formpara);
                win.show();
            }
        });
    },
    //更多操作-批量发送邮件
    sendEnrollClueEmails:function(btn) {
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var selList = grid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen==0){
            Ext.MessageBox.alert('提示','您没有选中任何记录');
            return;
        } else {
            var noEmailName = "";
            var noEmailCount = 0;
            var personList = [];
            for (var i = 0; i < checkLen; i++) {
            	var name = selList[i].get('cusName');
                var email = selList[i].get('email');
                var clueId=selList[i].get("clueId");
                var mobile = selList[i].get("cusMobile");

                personList.push({"name": name, "email": email,"clueId":clueId,"mobile":mobile});
                //判断用户有没有邮箱
                if(email!=null && email!="" && email!=undefined) {
                } else {
                    noEmailCount ++;
                    if(noEmailName!="") {
                        noEmailName += "、" + name;
                    } else {
                        noEmailName = name;
                    }
                }
            }

            if(noEmailCount==checkLen) {
                //不存在有邮箱的数据
                Ext.MessageBox.alert('提示','您选中的记录没有邮箱');
                return;
            } else {

                if(noEmailName!="") {
                    Ext.MessageBox.alert('提示', noEmailName + '，没有邮箱');
                }

                var params = {
                    "ComID": "TZ_XSXS_MYXS_COM",
                    "PageID": "TZ_XSXS_MYXS_STD",
                    "OperateType": "U",
                    "comParams": {
                        "add": [
                            {"type": 'MULTI', "personList": personList}
                        ]
                    }
                };
                Ext.tzLoad(Ext.JSON.encode(params), function (responseData) {
                    Ext.tzSendEmail({
                        //发送的邮件模板
                        "EmailTmpName": ["TZ_EML_N_001"],
                        //创建的需要发送的听众ID
                        "audienceId": responseData,
                        //是否可以发送附件：Y表示可以发送附件，N表示无附件
                        "file": "N"
                    }, "", false);
                });
            }
        }
    },
    //更多操作-查看邮件发送历史
    viewEnrollClueEmailsHis:function(btn){
        var grid=btn.findParentByType("grid");
        var store = grid.getStore();
        var selList = grid.getSelectionModel().getSelection();

        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","您没有选中任何记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条记录");
            return;
        }
        var cusEmail = selList[0].get("email");
        if(cusEmail!=""){
            Ext.tzSearchMailHistory(cusEmail);
        }else{
            Ext.Msg.alert("提示","您选中的记录没有邮箱");
            return;
        }
    },
    //更多操作-查看短信发送历史
    viewSmsHistory: function(btn) {
    	Ext.tzSetCompResourses("TZ_XSXS_ZSXS_COM");
        var grid=btn.findParentByType("grid");
        var store = grid.getStore();
        var selList = grid.getSelectionModel().getSelection();

        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","您没有选中任何记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条记录");
            return;
        }
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_ZSXS_COM"]["TZ_XSXS_SMSHIS_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_XSXS_SMSHIS_STD，请检查配置。');
            return;
        }
        var cmp, ViewClass,contentPanel,clsProto;
        
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
        
        var mobile = selList[0].get("cusMobile");
        if(mobile!=""){
        	cmp = new ViewClass(mobile);
            cmp.on('afterrender',function(panel){
            	var store  = panel.lookupReference("smsHistoryGrid").store;
//            	var store=panel.getStore();
                var tzStoreParams ='{"mobile":"'+mobile+'"}';
                store.tzStoreParams = tzStoreParams;
                store.load({
                	
                });
            });

            tab = contentPanel.add(cmp);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
        }else{
            Ext.Msg.alert("提示","您选中的记录没有手机号码");
            return;
        }
    },
    //更多操作-快速处理线索-过往状态
    viewClueOldState: function(btn) {
        var grid = btn.findParentByType("grid");
        var selectRecords = grid.getSelectionModel().getSelection();
        var selectLength = selectRecords.length;

        if(selectLength==0) {
            Ext.Msg.alert("提示","您没有选中任何记录");
            return;
        } else if(selectLength>1) {
            Ext.Msg.alert("提示","只能选择一条记录");
            return;
        } else {
            var clueId = selectRecords[0].data.clueId;

            Ext.tzSetCompResourses("TZ_STATE_PAST_COM");

            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_STATE_PAST_COM"]["TZ_STATE_PAST_STD"];
            if( pageResSet == "" || pageResSet == undefined){
                Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                return;
            }
            //该功能对应的JS类
            var className = pageResSet["jsClassName"];
            if(className == "" || className == undefined){
                Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_STATE_PAST_STD，请检查配置。');
                return;
            }
            var contentPanel, cmp, ViewClass, clsProto;

            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
            contentPanel.body.addCls('kitchensink-example');

            if(!Ext.ClassManager.isCreated(className)){
                Ext.syncRequire(className);
            }
            ViewClass = Ext.ClassManager.get(className);
            var tzParams = '{"ComID":"TZ_STATE_PAST_COM","PageID":"TZ_STATE_PAST_STD","OperateType":"QG","comParams":{"TZ_LEAD_ID":"'+clueId+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                var transValue = new KitchenSink.view.common.store.appTransStore("TZ_LEAD_STATUS");
                transValue.load({
                    callback:function(){
                        cmp = new ViewClass(respData,transValue);
                        var tab = contentPanel.add(cmp);

                        contentPanel.setActiveTab(tab);

                        Ext.resumeLayouts(true);

                        if (cmp.floating) {
                            cmp.show();
                        }
                    }
                });
            });
        }
    },
    //更多操作-快速处理线索-退回
    dealWithBack: function(btn) {
        var me = this,
            view = me.getView();

        var grid = btn.findParentByType("grid");
        var selectRecords = grid.getSelectionModel().getSelection();
        var selectLength = selectRecords.length;

        if(selectLength==0) {
            Ext.Msg.alert("提示","您没有选中任何记录");
            return;
        } else if(selectLength>1) {
            Ext.Msg.alert("提示", "只能选择一条记录");
            return;
        } else {
            //线索编号
            var clueId = selectRecords[0].data.clueId;
            //线索责任人
            var chargeOprid = selectRecords[0].data.chargeOprid;
            //线索状态
            var clueState = selectRecords[0].data.clueState;


            Ext.tzSetCompResourses("TZ_XSXS_INFO_COM");
            //是否有访问权限
            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_XSXS_THYY_STD"];
            if(pageResSet==""||pageResSet==undefined) {
                Ext.MessageBox.alert('提示','您没有修改数据的权限');
                return;
            }
            //该功能对应的JS类
            var className = pageResSet["jsClassName"];
            if(className==""||className==undefined) {
                Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID为：TZ_XSXS_THYY_STD，请检查配置。');
                return;
            }

            var win = me.lookupReference('clueBackWindow');
            if(!win) {
                Ext.syncRequire(className);
                ViewClass = Ext.ClassManager.get(className);

                //获取退回原因下拉框值
                var backReasonId, backReasonName, backReasonFlag, i;
                var backReasonData = [];

                var backPersonOprid,backPersonName;
                var tzDropDownParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzGetDropDownInfo","comParams":{"clueId":"' + clueId + '"}}';
                Ext.tzLoad(tzDropDownParams, function (respData) {
                    backReasonId = respData.backReasonId;
                    backReasonName = respData.backReasonName;
                    //去掉默认值，自行选择
                    //backPersonOprid = respData.backPersonOprid;
                    //backPersonName = respData.backPersonName;
                    backPersonOprid = "";
                    backPersonName = "";

                    var myMask = new Ext.LoadMask(
                        {
                            msg    : TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00022"),
                            target : Ext.getCmp('tranzvision-framework-content-panel')
                        });

                    myMask.show();

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

                                myMask.hide();

                                win = new ViewClass({
                                    clueId: clueId,
                                    chargeOprid:chargeOprid,
                                    clueState:clueState,
                                    backReasonData: backReasonData,
                                    backPersonOprid: backPersonOprid,
                                    backPersonName: backPersonName,
                                    fromType: 'MYXS'
                                });

                                view.add(win);

                                win.show();
                            }
                        }
                    });
                });
            }
        }
    },
    //更多操作-快速处理线索-关闭
    dealWithClose: function (btn) {
        var me = this,
            view = me.getView();

        var grid = btn.findParentByType("grid");
        var selectRecords = grid.getSelectionModel().getSelection();
        var selectLength = selectRecords.length;

        if(selectLength==0) {
            Ext.Msg.alert("提示","您没有选中任何记录");
            return;
        } else if(selectLength>1) {
            Ext.Msg.alert("提示", "只能选择一条记录");
            return;
        } else {
            //线索编号
            var clueId = selectRecords[0].data.clueId;
            //线索责任人
            var chargeOprid = selectRecords[0].data.chargeOprid;
            //线索状态
            var clueState = selectRecords[0].data.clueState;


            Ext.tzSetCompResourses("TZ_XSXS_INFO_COM");
            //是否有访问权限
            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_XSXS_GBYY_STD"];
            if (pageResSet == "" || pageResSet == undefined) {
                Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                return;
            }
            //该功能对应的JS类
            var className = pageResSet["jsClassName"];
            if (className == "" || className == undefined) {
                Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_XSXS_GBYY_STD，请检查配置。');
                return;
            }

            var win = me.lookupReference('clueCloseWindow');
            if (!win) {
                Ext.syncRequire(className);
                ViewClass = Ext.ClassManager.get(className);

                //获取关闭原因下拉框值
                var closeReasonId, closeReasonName, closeReasonFlag, i;
                var closeReasonData = [];

                var tzDropDownParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzGetDropDownInfo","comParams":{"clueId":"' + clueId + '"}}';
                Ext.tzLoad(tzDropDownParams, function (respData) {
                    closeReasonId = respData.closeReasonId;
                    closeReasonName = respData.closeReasonName;

                    var myMask = new Ext.LoadMask(
                        {
                            msg    : TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00022"),
                            target : Ext.getCmp('tranzvision-framework-content-panel')
                        });

                    myMask.show();

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
                                for (i = 0; i < records.length; i++) {
                                    closeReasonData.push(records[i].data);
                                    if (closeReasonId.length == 0 || records[i].data["TZ_GBYY_ID"] == closeReasonId) {
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

                                myMask.hide();

                                win = new ViewClass({
                                    clueId: clueId,
                                    chargeOprid: chargeOprid,
                                    clueState: clueState,
                                    closeReasonData: closeReasonData,
                                    fromType: 'ZSXS'
                                });

                                view.add(win);

                                win.show();
                            }
                        }
                    });
                });
            }
        }
    },
    //更多操作-快速处理线索-转交
    dealWithGive: function(btn) {
        var me = this,
            view = me.getView();

        var grid = btn.findParentByType("grid");
        var selectRecords = grid.getSelectionModel().getSelection();
        var selectLength = selectRecords.length;

        if(selectLength==0) {
            Ext.Msg.alert("提示","您没有选中任何记录");
            return;
        } else if(selectLength>1) {
            Ext.Msg.alert("提示", "只能选择一条记录");
            return;
        } else {
            //线索编号
            var clueId = selectRecords[0].data.clueId;
            //线索责任人
            var chargeOprid = selectRecords[0].data.chargeOprid;
            //线索状态
            var clueState = selectRecords[0].data.clueState;


            Ext.tzSetCompResourses("TZ_XSXS_INFO_COM");
            //是否有访问权限
            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_XSXS_FPZRR_STD"];
            if(pageResSet==""||pageResSet==undefined) {
                Ext.MessageBox.alert('提示','您没有修改数据的权限');
                return;
            }
            //该功能对应的JS类
            var className = pageResSet["jsClassName"];
            if(className==""||className==undefined) {
                Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID为：TZ_XSXS_FPZRR_STD，请检查配置。');
                return;
            }

            var win = me.lookupReference('clueAssignResponsibleWindow');
            if(!win) {
                Ext.syncRequire(className);
                ViewClass = Ext.ClassManager.get(className);
                win = new ViewClass({
                    clueId:clueId,
                    chargeOprid:chargeOprid,
                    clueState:clueState,
                    fromType: 'ZSXS'
                });

                view.add(win);
            }

            win.show();
        }
    },
    //更多操作-快速处理线索-延迟联系
    dealWithDelayContact:function(btn) {
        var me = this,
            view = me.getView();

        var grid = btn.findParentByType("grid");
        var selectRecords = grid.getSelectionModel().getSelection();
        var selectLength = selectRecords.length;

        if(selectLength==0) {
            Ext.Msg.alert("提示","您没有选中任何记录");
            return;
        } else if(selectLength>1) {
            Ext.Msg.alert("提示", "只能选择一条记录");
            return;
        } else {
            //线索编号
            var clueId = selectRecords[0].data.clueId;
            //线索责任人
            var chargeOprid = selectRecords[0].data.chargeOprid;
            //线索状态
            var clueState = selectRecords[0].data.clueState;


            Ext.tzSetCompResourses("TZ_XSXS_INFO_COM");
            //是否有访问权限
            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_XSXS_YCLX_STD"];
            if(pageResSet==""||pageResSet==undefined) {
                Ext.MessageBox.alert('提示','您没有修改数据的权限');
                return;
            }
            //该功能对应的JS类
            var className = pageResSet["jsClassName"];
            if(className==""||className==undefined) {
                Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID为：TZ_XSXS_YCLX_STD，请检查配置。');
                return;
            }

            var win = this.lookupReference('clueDelayContactWindow');
            if(!win) {
                Ext.syncRequire(className);
                ViewClass = Ext.ClassManager.get(className);
                win = new ViewClass({
                    clueId:clueId,
                    chargeOprid:chargeOprid,
                    clueState:clueState,
                    fromType: 'ZSXS'
                });

                view.add(win);
            }

            win.show();
        }
    },
  //添加联系报告
    addContactReport: function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var clueID = selRec.get("clueId");
    	
        var winClass = "KitchenSink.view.clueManagement.clueManagement.viewOrAddLxReportWindow";
        Ext.syncRequire(winClass);
        var ViewClass = Ext.ClassManager.get(winClass);
        
        var win = new ViewClass({
        	clueID: clueID
        });
        
        win.show();
    },
    //关闭
    closeMyEnrollmentClue:function() {
        this.getView().close();
    },
    /*批量发送短信*/
    sendSmsSelPers:function(btn) {
    	var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var selList = grid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen==0){
            Ext.MessageBox.alert('提示','您没有选中任何记录');
            return;
        } else {
            var noMobileName = "";
            var noMobileCount = 0;
            var personList = [];
            for (var i = 0; i < checkLen; i++) {
                var name = selList[i].get('cusName');
                var email = selList[i].get('email');
                var clueId=selList[i].get("clueId");
                var mobile = selList[i].get("cusMobile");

                personList.push({"name": name, "email": email,"clueId":clueId,"mobile":mobile});

                //判断用户有没有电话
                if(mobile!=null && mobile!="" && mobile!=undefined) {
                } else {
                    noMobileCount ++;
                    if(noMobileName!="") {
                    	noMobileName += "、" + name;
                    } else {
                    	noMobileName = name;
                    }
                }
            }

            if(noMobileCount==checkLen) {
                //不存在有短信的数据
                Ext.MessageBox.alert('提示','您选中的记录没有手机号码');
                return;
            } else {

                if(noMobileName!="") {
                    Ext.MessageBox.alert('提示',noMobileName + '，没有手机号码');
                }

                var params = {
                    "ComID": "TZ_XSXS_MYXS_COM",
                    "PageID": "TZ_XSXS_MYXS_STD",
                    "OperateType": "U",
                    "comParams": {
                        "add": [
                            {"type": 'DX', "personList": personList}
                        ]
                    }
                };
                Ext.tzLoad(Ext.JSON.encode(params), function (responseData) {
                	Ext.tzSendSms({
                        //发送的短信模板;
                        "SmsTmpName": ["TZ_SMS_N_002"],
                        //创建的需要发送的听众ID;
                        "audienceId": responseData,
                        //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                        "file": "N"
                    });
                });
            }
        }
    },
  //查看活动
    seeActivity: function(view,rowIndex) {
    	var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var clueId = selRec.get("clueId");
        this.editClueInfoByID2(clueId);
        
    },
    //根据线索ID进行编辑
    editClueInfoByID2:function(clueId) {
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
                                                                                                /*var clueId = form.findField("clueId").getValue();
                                                                                                store.tzStoreParams = '{"clueId":"' + clueId + '"}';
                                                                                                store.load();*/
                                                                                                tabpanel.setActiveTab(2);
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

