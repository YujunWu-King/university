Ext.define('KitchenSink.view.clueManagement.clueManagement.supplierClueController',{
    extend:'Ext.app.ViewController',
    alias:'controller.supplierClueController',
    //查询
    searchClue:function(btn){
        var grid=btn.findParentByType("grid");
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_GYS_CLUE_COM.TZ_GYS_CLUE_STD.TZ_GYSXS_INFO_VW',
            condition:{
                "TZ_JG_ID":Ext.tzOrgID,
                "TZ_LEAD_STATUS":"G"
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                btn.findParentByType('grid').getStore().clearFilter();//查询基于可配置搜索，清除预设的过滤条件
                
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //新建线索
    addClue:function(btn) {
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GYS_CLUE_COM"]["TZ_GYS_XSINFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GYS_XSINFO_STD，请检查配置。');
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
        
        var grid = btn.findParentByType('supplierClueMgr');
        cmp = new ViewClass({
            actType: "add",
            reloadClueGrid: function(){
            	grid.getStore().reload();
            }
        });

        cmp.on('afterrender',function(){
            //线索信息表单
            var form = cmp.child('form').getForm();
            
            //隐藏退回原因、关闭原因、建议跟进日期
            form.findField("backReasonId").setHidden(true);
            form.findField("closeReasonId").setHidden(true);
            form.findField("contactDate").setHidden(true);
        });

        var tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    //Grid中每一行的编辑
    editSelClueInfo: function (view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var clueId = selRec.get("clueId");
        this.editClueInfoByID(clueId,store);
    },
    //根据线索ID进行编辑
    editClueInfoByID:function(clueId,panelGridStore) {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GYS_CLUE_COM"]["TZ_GYS_XSINFO_STD"];
        if(pageResSet==""||pageResSet==undefined) {
            Ext.MessageBox.alert('提示','您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className==""||className==undefined) {
            Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID为：TZ_GYS_XSINFO_STD，请检查配置。');
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
        var backReasonId,backReasonName,closeReasonId,closeReasonName,zxlbId,zxlbName;
        var backReasonFlag,closeReasonFlag,colorTypeFlag,zxlbFlag;
        var i, j, m,n;
        var backReasonData = [],
            closeReasonData = [],
            customerNameData = [],
            companyNameData = [];
        
        var tzDropDownParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzGetDropDownInfo","comParams":{"clueId":"'+clueId+'"}}';
        Ext.tzLoad(tzDropDownParams,function(respData){
            backReasonId=respData.backReasonId;
            backReasonName=respData.backReasonName;
            closeReasonId=respData.closeReasonId;
            closeReasonName=respData.closeReasonName;



            var myMask = new Ext.LoadMask({
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

                                    
            						myMask.hide();

                                    cmp = new ViewClass({
                                    	clueID: clueId,
                                    	backReasonData: backReasonData,
                                        closeReasonData: closeReasonData,
                                        customerNameData: customerNameData,
                                        companyNameData: companyNameData,
                                        actType: 'update',
                                        reloadClueGrid: function(){
                                        	panelGridStore.reload();
                                        }
                                    });


                                    cmp.on('afterrender', function (panel) {
                                        var form = panel.child('form').getForm();

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
                                        });
                                    });

                                    tab = contentPanel.add(cmp);
                                    contentPanel.setActiveTab(tab);
                                    Ext.resumeLayouts(true);
                                    if (cmp.floating) {
                                        cmp.show();
                                    }
                                }
                            }
                        });
                    }
                }
            });
        });
    },

    
    saveClue: function(btn){
    	var me=this;
    	var panel = btn.findParentByType("supplierClueDetailPanel");
        var form = panel.child("form").getForm();
        if(!form.isValid()) return;

        var tzParams="";
        //如果新增，根据手机号码判断是否存在未关闭的线索
        var existFlag = "";
        var clueId = form.findField("clueId").getValue();
        if (clueId == "" || clueId == null) {
            var cusMobile = form.findField("cusMobile").getValue();
            if(cusMobile!="" && cusMobile!=null) {
                var tzParamsExist = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzIsExist","comParams":{"cusMobile":"' + cusMobile + '"}}';
                Ext.tzLoad(tzParamsExist, function (responseData) {
                    existFlag = responseData.existFlag;
                    if (existFlag == "Y") {
                        Ext.Msg.alert("提示", "已有此人");
                    }
                    me.saveClueDetail(btn);
                });
            } else {
                me.saveClueDetail(btn);
            }
        } else {
            me.saveClueDetail(btn);
        }
    },
    
  //获取保存线索的参数并保存
    saveClueDetail:function(btn){
        var panel = btn.findParentByType("supplierClueDetailPanel");
        var form = panel.child("form").getForm();
        if(!form.isValid) return;

        var formParams = form.getValues();
        var tzParamsObj = {
        	ComID: 'TZ_GYS_CLUE_COM',
        	PageID: 'TZ_GYS_XSINFO_STD',
        	OperateType: 'saveClueInfo',
        	comParams: formParams
        };

        //提交参数
        var tzParams = Ext.JSON.encode(tzParamsObj);

        Ext.tzSubmit(tzParams, function (response) {
            form.setValues({"clueId": response.clueId});
            
            var existName = response.existName;

            if(existName=="Y") {
                Ext.MessageBox.alert("提示","已存在姓名相同的线索");
            }
            
            //如果为确认按钮
            if (btn.name == "ensureBtn") {
                //刷新父窗口
                panel.close();
            }
            
            //刷新线索列表
            if(typeof(panel.reloadClueGrid) == "function"){
            	panel.reloadClueGrid();
            }
        }, "", true, this);
    }
});

