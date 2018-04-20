Ext.define('KitchenSink.view.clueManagement.clueManagement.clueDetailController',{
    extend:'Ext.app.ViewController',
    alias:'controller.clueDetailController',
    //选择常住地
    searchLocal:function(btn) {
        var me = this;
        var form = btn.findParentByType("form").getForm();

        var tzRoleParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzGetPersonRole","comParams":{}}';
        Ext.tzLoad(tzRoleParams, function (respData) {
            var viewAllFlag = respData.viewAllFlag;
            if (viewAllFlag == "Y") {
                //线索管理员和一般人员能选择所有常住地
                me.showAllAddress(form);
            } else {
                //区域负责人只能选择自己的主管地区+自主开发地区
                me.showSelfAddress(form);
            }

        });
    },
    //选择所有常住地
    showAllAddress: function (form) {
        Ext.tzShowPromptSearch({
            recname: 'TZ_XSXS_YXDQ_VW',
            searchDesc: '选择常住地',
            maxRow:100,
            condition:{
                srhConFields:{
                    TZ_LABEL_DESC:{
                        desc:'常住地',
                        operator:'07',
                        type:'01'
                    },
                    TZ_JG_ID:{
                        desc:'机构编号',
                        operator:'01',
                        type:'01',
                        value:Ext.tzOrgID,
                        editable:false
                    }
                }
            },
            srhresult:{
                TZ_LABEL_NAME: '常住地编号',
                TZ_LABEL_DESC: '常住地'
            },
            multiselect: false,
            callback: function(selection){
                var localId=selection[0].data.TZ_LABEL_NAME;
                var localAddress=selection[0].data.TZ_LABEL_DESC;

                form.findField("localId").setValue(localId);
                form.findField("localAddress").setValue(localAddress);
            }
        });
    },
    //选择自己的常住地
    showSelfAddress: function (form) {
        Ext.tzShowPromptSearch({
            recname: 'TZ_PER_DQ_VW',
            searchDesc: '选择常住地',
            maxRow:100,
            condition:{
                srhConFields:{
                    TZ_LABEL_DESC:{
                        desc:'常住地',
                        operator:'07',
                        type:'01'
                    },
                    TZ_DLZH_ID:{
                        desc:'登录账号',
                        operator:'01',
                        type:'01',
                        value:TranzvisionMeikecityAdvanced.Boot.loginUserId,
                        editable:false
                    }
                }
            },
            srhresult:{
                TZ_AQDQ_LABEL: '常住地编号',
                TZ_LABEL_DESC: '常住地'
            },
            multiselect: false,
            callback: function(selection){
                var localId=selection[0].data.TZ_AQDQ_LABEL;
                var localAddress=selection[0].data.TZ_LABEL_DESC;

                form.findField("localId").setValue(localId);
                form.findField("localAddress").setValue(localAddress);
            }
        });
    },
    //清除常住地
    clearLocal:function(field) {
        field.setValue("");
        //常住地编号也要置空
        var form = field.findParentByType("form").getForm();
        form.findField("localId").setValue("");
    },
    //过往状态
    viewClueOldState:function(btn){
        Ext.tzSetCompResourses("TZ_STATE_PAST_COM");
        var clueId = btn.findParentByType('form').getForm().findField("clueId").getValue();

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
                    tab = contentPanel.add(cmp);

                    contentPanel.setActiveTab(tab);

                    Ext.resumeLayouts(true);

                    if (cmp.floating) {
                        cmp.show();
                    }
                }
            })

        });
    },
    //退回
    dealWithBack:function(btn){
        var me = this;

        //退回之前先保存当前页面数据，防止数据冲突问题
        var clueDetail = me.getView();
        var formDetail = clueDetail.child("form").getForm();
        if(formDetail.isValid()){
            var tzParams = me.getClueDetailParams(btn);
            if(tzParams!="") {
                Ext.tzSubmit(tzParams,function(responseData){
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
                    //线索编号
                    var clueId = formDetail.findField('clueId').getValue();
                    //线索责任人
                    var chargeOprid = formDetail.findField('chargeOprid').getValue();
                    //线索状态
                    var clueState = formDetail.findField('clueState').getValue();

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

                                        win = new ViewClass({
                                            clueId: clueId,
                                            chargeOprid:chargeOprid,
                                            clueState:clueState,
                                            backReasonData: backReasonData,
                                            backPersonOprid: backPersonOprid,
                                            backPersonName: backPersonName,
                                            fromType:''
                                        });

                                        var form = win.child('form').getForm();
                                        me.getView().add(win);

                                        var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"QF","comParams":{"clueId":"' + clueId + '"}}';
                                        //加载数据
                                        /**/
                                        Ext.tzLoad(tzParams, function (responseData) {
                                            var formData = responseData.formData;
                                            form.setValues(formData);
                                        });

                                        win.show();
                                    }
                                }
                            });
                        });
                    }
                });
            }
        }
    },
    //关闭
    dealWithClose:function(btn){
        var me = this;
        //关闭之前先保存当前页面数据，防止数据冲突问题
        var clueDetail = me.getView();
        var formDetail = clueDetail.child("form").getForm();
        if(formDetail.isValid()){
            var tzParams = me.getClueDetailParams(btn);
            if(tzParams!="") {
                Ext.tzSubmit(tzParams,function(responseData){
                    //是否有访问权限
                    var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_XSXS_GBYY_STD"];
                    if(pageResSet==""||pageResSet==undefined) {
                        Ext.MessageBox.alert('提示','您没有修改数据的权限');
                        return;
                    }
                    //该功能对应的JS类
                    var className = pageResSet["jsClassName"];
                    if(className==""||className==undefined) {
                        Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID为：TZ_XSXS_GBYY_STD，请检查配置。');
                        return;
                    }

                    //线索编号
                    var clueId = formDetail.findField('clueId').getValue();
                    //线索责任人
                    var chargeOprid = formDetail.findField('chargeOprid').getValue();
                    //线索状态
                    var clueState = formDetail.findField('clueState').getValue();

                    var win = me.lookupReference('clueCloseWindow');
                    if(!win) {
                        Ext.syncRequire(className);
                        ViewClass = Ext.ClassManager.get(className);

                        //获取关闭原因下拉框值
                        var closeReasonId,closeReasonName,closeReasonFlag,i;
                        var closeReasonData = [];

                        var tzDropDownParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzGetDropDownInfo","comParams":{"clueId":"' + clueId + '"}}';
                        Ext.tzLoad(tzDropDownParams, function (respData) {
                            closeReasonId=respData.closeReasonId;
                            closeReasonName=respData.closeReasonName;

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
                                        for(i=0;i<records.length;i++) {
                                            closeReasonData.push(records[i].data);
                                            if(closeReasonId.length==0||records[i].data["TZ_GBYY_ID"]==closeReasonId){
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


                                        win = new ViewClass({
                                            clueId:clueId,
                                            chargeOprid:chargeOprid,
                                            clueState:clueState,
                                            closeReasonData:closeReasonData,
                                            fromType:''
                                        });
                                        var form = win.child('form').getForm();
                                        me.getView().add(win);
                                        /**/
                                        var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"QF","comParams":{"clueId":"'+clueId+'"}}';
                                        //加载数据
                                        Ext.tzLoad(tzParams,function(responseData){
                                            var formData = responseData.formData;
                                            form.setValues(formData);
                                        });
                                        win.show();
                                    }
                                }
                            });
                        });
                    }
                },false,true,this);
            }
        }
    },
    //转交
    dealWithGive:function(btn){
        var me = this;
        //转交之前先保存当前页面数据，防止数据冲突问题
        var clueDetail = me.getView();
        var formDetail = clueDetail.child("form").getForm();
        if(formDetail.isValid()){
            var tzParams = me.getClueDetailParams(btn);
            if(tzParams!="") {
                Ext.tzSubmit(tzParams,function(responseData){
                    //保存成功后
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
                    //线索编号
                    var clueId = formDetail.findField('clueId').getValue();
                    //线索责任人
                    var chargeOprid = formDetail.findField('chargeOprid').getValue();
                    //线索状态
                    var clueState = formDetail.findField('clueState').getValue();

                    var win = me.lookupReference('clueAssignResponsibleWindow');
                    if(!win) {
                        Ext.syncRequire(className);
                        ViewClass = Ext.ClassManager.get(className);
                        win = new ViewClass({
                            clueId:clueId,
                            chargeOprid:chargeOprid,
                            clueState:clueState,
                            fromType:''
                        });
                        var form = win.child('form').getForm();
                        clueDetail.add(win);
                    }
                    /*
                    var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"QF","comParams":{"clueId":"'+clueId+'"}}';
                    //加载数据
                    Ext.tzLoad(tzParams,function(responseData){
                        var formData = responseData.formData;
                        form.setValues(formData);
                    });
                     */
                    win.show();
                },"",true,this);
            }
        }
    },
    //延迟联系
    dealWithDelayContact:function(btn){
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

        //线索详情panel
        var clueDetail = this.getView();
        //线索详情表单
        var formDetail = clueDetail.child("form").getForm();
        //线索编号
        var clueId = formDetail.findField('clueId').getValue();
        //线索责任人
        var chargeOprid = formDetail.findField('chargeOprid').getValue();
        //线索状态
        var clueState = formDetail.findField('clueState').getValue();

        var win = this.lookupReference('clueDelayContactWindow');
        if(!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass({
                clueId:clueId,
                chargeOprid:chargeOprid,
                clueState:clueState,
                fromType:''
            });
            var form = win.child('form').getForm();
            this.getView().add(win);
        }
        var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"QF","comParams":{"clueId":"'+clueId+'"}}';
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){
            var formData = responseData.formData;
            form.setValues(formData);
        });
        win.show();
    },
    //保存线索
    saveClue:function(btn){
        var me=this;
    	var panel=btn.findParentByType("clueDetailPanel");
        var form=panel.child("form").getForm();
        if(!form.isValid()){
            return false;
        }

        var glBmbBut=panel.down("button[name=glBmbBut]");

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
                    me.saveClueDetail(btn, glBmbBut);
                });
            } else {
                me.saveClueDetail(btn,glBmbBut);
            }
        } else {
            me.saveClueDetail(btn,glBmbBut);
        }

    },
    //获取保存线索的参数并保存
    saveClueDetail:function(btn,glBmbBut){
        var panel=btn.findParentByType("clueDetailPanel");
        //创建线索panel
        var clueInfo = this.getView();
        var form=clueInfo.child("form").getForm();
        var grid=clueInfo.child("grid");
        var store=grid.getStore();
        if(!form.isValid){
            return false;
        }

        var formParams = form.getValues();
        var comParams = "";
        var editJson = '{"typeFlag":"BASIC","data":' + Ext.JSON.encode(formParams) + '}';
        if (comParams == "") {
            comParams = '"update":[' + editJson + "]";
        } else {
            comParams = comParams + ',"update":[' + editJson + "]";
        }

        //删除json字符串
        var removeJson = "";
        //删除记录
        var removeRecs = store.getRemovedRecords();
        for (var i = 0; i < removeRecs.length; i++) {
            if (removeJson == "") {
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            } else {
                removeJson = removeJson + ',' + Ext.JSON.encode(removeRecs[i].data);
            }
        }
        if (removeJson != "") {
            if (comParams == "") {
                comParams = '"delete":[' + removeJson + "]";
            } else {
                comParams = comParams + ',"delete":[' + removeJson + "]";
            }
        }

        //提交参数
        var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"U","comParams":{' + comParams + '}}';

        Ext.tzSubmit(tzParams, function (response) {

            form.setValues({"clueId": response.clueId});
            var bkStatus = response.bkStatus;
            var clueState = response.clueState;
            var existName = response.existName;
            //线索状态为关闭，报考状态不是未报名，关联报名表按钮隐藏
            if (clueState == "G" || bkStatus != "A") {
                glBmbBut.setVisible(false);
            } else {
                glBmbBut.setVisible(true);
                form.findField("bkStatus").setValue(bkStatus);
            }

            if(existName=="Y") {
                Ext.MessageBox.alert("提示","已存在姓名相同的线索");
            }


            //如果为确认按钮
            if (btn.name == "ensureBtn") {
                //刷新父窗口
                panel.close();
                var fromType = panel.fromType;
                var parentPanel;
                var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
                //取得是xtype,不是reference
                if (fromType == "ZSXS") {
                    parentPanel = contentPanel.child("enrollmentClue");
                } else if (fromType == "MYXS") {
                    parentPanel = contentPanel.child("myEnrollmentClue");
                }

                if (parentPanel != undefined && parentPanel != null) {
                    parentPanel.store.reload();
                }
            }

        }, "", true, this);
    },
    //获取保存线索的参数
    getClueDetailParams:function(btn) {
        var panel=btn.findParentByType("clueDetailPanel");
        //创建线索panel
        var clueInfo = this.getView();
        var form=clueInfo.child("form").getForm();
        var grid=clueInfo.child("grid");
        var store=grid.getStore();
        if(!form.isValid){
            return false;
        }

        var formParams = form.getValues();
        var comParams = "";
        var editJson = '{"typeFlag":"BASIC","data":' + Ext.JSON.encode(formParams) + '}';
        if (comParams == "") {
            comParams = '"update":[' + editJson + "]";
        } else {
            comParams = comParams + ',"update":[' + editJson + "]";
        }

        //删除json字符串
        var removeJson = "";
        //删除记录
        var removeRecs = store.getRemovedRecords();
        for (var i = 0; i < removeRecs.length; i++) {
            if (removeJson == "") {
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            } else {
                removeJson = removeJson + ',' + Ext.JSON.encode(removeRecs[i].data);
            }
        }
        if (removeJson != "") {
            if (comParams == "") {
                comParams = '"delete":[' + removeJson + "]";
            } else {
                comParams = comParams + ',"delete":[' + removeJson + "]";
            }
        }

        //提交参数
        var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"U","comParams":{' + comParams + '}}';
        return tzParams;
    },
    //关闭线索窗口
    closeClue:function() {
        this.getView().close();
    },
    //关联报名表
    clueRelBmb:function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_XSXS_BMB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_XSXS_BMB_STD，请检查配置。');
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

            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }

        var form=btn.findParentByType("form").getForm();
        var clueId=form.findField("clueId").getValue();
        var cusName=form.findField("cusName").getValue();
        var cusMobile=form.findField("cusMobile").getValue();

        var win = this.lookupReference('clueRelBmbWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass({
                submitStateStore: new KitchenSink.view.common.store.appTransStore("TZ_APPFORM_STATE")
            });

            var winForm = win.child('form').getForm();
            //显示信息赋值
            winForm.findField("clueId").setValue(clueId);
            winForm.findField("name").setValue(cusName);
            winForm.findField("mobile").setValue(cusMobile);

            var resultDesc=winForm.findField("resultDesc");
            var grid=win.child('grid');
            var tzStoreParams = '{"cfgSrhId":"TZ_XSXS_INFO_COM.TZ_XSXS_BMB_STD.TZ_CLUE_BMB_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-operator": "'+Ext.tzOrgID+'","TZ_MOBILE-operator": "01","TZ_MOBILE-value": "'+ cusMobile +'"}}';
            grid.store.tzStoreParams = tzStoreParams;
            grid.store.load({
                scope:this,
                callback:function(rec,opetion,success){
                    var count=grid.store.getTotalCount();
                    if(count==0){
                        grid.setHidden(true);
                        resultDesc.setValue(Ext.tzGetResourse("TZ_XSXS_INFO_COM.TZ_XSXS_BMB_STD.notExistBmb","系统按上述客户信息，没有查到可关联的报名表！您可以点击【查询】继续查找"));
                    }else{
                        grid.setHidden(false);
                        resultDesc.setValue(Ext.tzGetResourse("TZ_XSXS_INFO_COM.TZ_XSXS_BMB_STD.existBmb","系统按上述客户信息，查询到以下报名表可以关联！"));
                    }
                }
            });

            this.getView().add(win);
            win.show();
        }
    },
    //删除线索和报名信息关联
    deleteBmb:function(view,rowindex){
        Ext.MessageBox.confirm('确认', '您确定要解除线索与报名表的关联关系?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowindex);
            }
        },this);
    },
    //查看报名表审核信息
    viewAuditApplication: function (grid,rowIndex) {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_BMR_AUDIT_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_BMR_AUDIT_STD，请检查配置。');
            return;
        }

        var contentPanel, cmp, ViewClass, clsProto;

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

        var classID,oprID ,appInsID,record;

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.bmrClassId;
        var oprID = record.data.bmrOprid;
        var appInsID= record.data.bmbId;

        //责任人
        var form = grid.findParentByType("clueDetailPanel").down("form").getForm();
        var chargeName = form.findField("chargeName").getValue();


        var applicationFormTagStore= new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_TAG_STORE_V',
            condition:{
                TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                },
                TZ_APP_INS_ID:{
                    value:appInsID,
                    operator:'01',
                    type:'01'
                }
            },
            result:'TZ_LABEL_ID,TZ_LABEL_NAME'
        });
        applicationFormTagStore.load(
            {
                scope:this,
                callback:function(){
                    cmp = new ViewClass({appInsID:appInsID,applicationFormTagStore:applicationFormTagStore,chargeName:chargeName});
                    cmp.on('afterrender',function(panel){
                        var form = panel.child('form').getForm();
                        var tabpanel = panel.child("tabpanel");
                        var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_BMR_AUDIT_STD","OperateType":"QF","comParams":{"classID":"'+classID+'","oprID":"'+oprID+'"}}';
                        Ext.tzLoad(tzParams,function(respData){
                            var formData = respData.formData;
                            form.setValues(formData);
                            tabpanel.down('form[name=materialCheck]').getForm().setValues(formData);

                            var store = tabpanel.down('form[name=materialCheck]').down("grid").store;
                            var tzStoreParams = '{"classID":"'+classID+'","oprID":"' + oprID + '","queryType":"FILE"}';
                            store.tzStoreParams = tzStoreParams;
                            store.load(/*{
                                scope: this,
                                callback: function(records, operation, success) {
                                    storeGroup.auditFormFileCheckBackMsgJsonData =new Array(records.length);
                                }
                            }*/);


                            //照片
                            panel.getViewModel().set("photoSrc",formData["photoUrl"]);
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
        )
    },
    //查看报名表审核信息，查看推荐信
    viewRefLetter:function(grid, rowIndex)
    {
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert("提示","您没有修改数据的权限");
            return;
        }

        var form = this.getView().child("form").getForm();
        var appInsID = grid.store.getAt(rowIndex).get('refLetterAppInsId');
        var refLetterID  = grid.store.getAt(rowIndex).get('refLetterId');


        if(appInsID!=""&&refLetterID!=""&&appInsID!="0"&&refLetterID!="0"){
            var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appInsID+'","TZ_REF_LETTER_ID":"'+refLetterID+'","TZ_MANAGER":"Y"}}';
            var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+encodeURIComponent(tzParams);
            var win = new Ext.Window({
                title : "查看推荐信",
                maximized : true,
                width : Ext.getBody().width,
                height : Ext.getBody().height,
                bodyStyle:'overflow:hidden',
                border:false,
                bodyBorder : false,
                isTopContainer : true,
                modal : true,
                resizable : false,
                contentEl : Ext.DomHelper.append(document.body, {
                    bodyBorder : false,
                    tag : 'iframe',
                    style : "border:0px none;scrollbar:true",
                    src : viewUrl,
                    height : "100%",
                    width : "100%"
                }),
                buttons: [ {
                    text: "关闭",
                    iconCls:"close",
                    handler: function(){
                        win.close();
                    }
                }]
            });
            win.show();
        }else{
            Ext.MessageBox.alert("提示", "找不到该推荐信");
        }
    },
    //查看报名表，只读
    viewApplication:function(grid,rowIndex) {
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.bmrClassId;
        var oprID = record.data.bmrOprid;
        var appInsID= record.data.bmbId;


        if(classID!=""&&oprID!=""){
            var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appInsID+'","isEdit":"N"}}';
            var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+encodeURIComponent(tzParams);
            var win = new Ext.Window({
                title : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.viewApplicationForm","查看报名表"),
                maximized : true,
                width : Ext.getBody().width,
                border:false,
                bodyBorder : false,
                isTopContainer : true,
                modal : true,
                resizable : false,
                contentEl : Ext.DomHelper.append(document.body, {
                    bodyBorder : false,
                    tag : 'iframe',
                    style : "border:0px none;scrollbar:true",
                    src : viewUrl,
                    height : "100%",
                    width : "100%"
                }),
                buttons: [ {
                    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
                    iconCls:"close",
                    handler: function(){
                        win.close();
                    }
                }]
            });
            win.show();
        }else{
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.cantFindAppForm","找不到该报名人的报名表"));
        }
    }
});