Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewRuleController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.materialsReviewRuleController',
    //设置评审规则-查看考生
    viewApplicationForm : function(btn){
        var me=this,
            view=me.getView();

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_CL_COM"]["TZ_CLPS_KS_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLPS_KS_STD，请检查配置。');
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

            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }


        var form = view.child("form").getForm(),
            classId = form.findField('classId').getValue(),
            batchId = form.findField('batchId').getValue(),
            className = form.findField('className').getValue(),
            batchName = form.findField('batchName').getValue(),
            bkksNum = form.findField('bkksNum').getValue();

        cmp = new ViewClass({
            classId:classId,
            batchId:batchId
        });

        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();

            var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_KS_STD",' +
                '"OperateType":"QF","comParams":{"classId":"'+classId+'","batchId":"'+batchId+'"}}';

            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                if(formData!="" && formData!=undefined) {
                    panel.actType="update";
                    formData.className = className;
                    formData.batchName = batchName;
                    form.setValues(formData);

                    var examineeGrid = panel.down('grid');
                    var tzStoreParams = '{"cfgSrhId": "TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.TZ_CLPS_KS_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "' + classId + '","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value": "' + batchId + '"}}';
                    examineeGrid.store.tzStoreParams = tzStoreParams;
                    examineeGrid.store.load();
                } else {
                    panel.actType="add";
                    form.findField("classId").setValue(classId);
                    form.findField("className").setValue(className);
                    form.findField("batchId").setValue(batchId);
                    form.findField("batchName").setValue(batchName);
                    form.findField("bkksNum").setValue(bkksNum);
                    form.findField("clpsksNum").setValue(0);
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
    //设置评审规则-新增评委
    addJudge:function(btn) {
        var me=this,
            form = btn.findParentByType("form").findParentByType("form").getForm();

        if(form.findField('dqpsStatus').getValue()=='A'){
            Ext.Msg.alert('提示','当前评审状态为进行中，不可添加评委');
            return ;
        }

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_CL_COM"]["TZ_CLPS_ADDPW_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        //var className = 'KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicants';
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLPS_ADDPW_STD，请检查配置。');
            return;
        }

        var classId = form.findField("classId").getValue();
        var batchId = form.findField("batchId").getValue();
        var judgeNumSet = form.findField("judgeNumSet").getValue();

        Ext.syncRequire(className);
        ViewClass = Ext.ClassManager.get(className);

        var judgeGroupParams =  '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD",' +
            '"OperateType":"tzGetJudgeGroup","comParams":{"classId":"'+classId+'","batchId":"'+batchId+'","judgeNumSet":"'+judgeNumSet+'"}}';

        Ext.tzLoad(judgeGroupParams,function(responseData) {
            var judgeGroupData = responseData.groupData;

            var win = new ViewClass({
                judgeGroupData:judgeGroupData
            });
            me.getView().add(win);
            win.down("grid").getStore().load();
            win.show();
        });


    },
    //设置评审规则-新增评委-批量设置选中教师评委组
    setJudgeGroupBatch:function(btn) {
        var window = btn.findParentByType("window");
        var grid = window.down("grid");
        var selList = grid.getSelectionModel().getSelection();

        if(selList.length<1) {
            Ext.MessageBox.alert("提示", "您没有选中任何记录");
            return;
        } else {

            var form = window.down("form[name=setGroupForm]").getForm();
            var judgeGroupSet = form.findField("judgeGroupSet").getValue();
            if(judgeGroupSet==null) {
                Ext.MessageBox.alert("提示", "请选择要设置的评委组");
                return;
            } else {
                for (var i = 0; i < selList.length; i++) {
                    selList[i].set("judgeGroup",judgeGroupSet);
                }
            }


            /*var win = new Ext.window.Window({
             title:'批量设置 评委组',
             width:400,
             modal:true,
             ignoreChangesFlag:true,
             layout: {
             type: 'vbox',
             align: 'stretch'
             },
             items:[{
             xtype:'combobox',
             margin:10,
             labelStyle:'font-weight:bold',
             labelWidth:120,
             fieldLabel:"评委组",
             store: new KitchenSink.view.common.store.comboxStore({
             recname:'TZ_CLPS_GR_TBL',
             condition:{
             TZ_JG_ID:{
             value:Ext.tzOrgID,
             operator:'01',
             type:'01'
             }
             },
             result:'TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME'
             }),
             displayField:"TZ_CLPS_GR_NAME",
             valueField:"TZ_CLPS_GR_ID",
             editable:false,
             queryMode:'local',
             listeners:{
             change:function( combo , newValue , oldValue , eOpts){
             for (var i = 0; i < selList.length; i++) {
             selList[i].set("judgeGroup",newValue);
             }
             Ext.defer(win.destroy,100,win,[]);
             }
             }
             }
             ]
             });
             win.show();*/
        }
    },
    //设置评审规则-grid操作列删除
    removeJudge:function(view,rowIndex){
        var form=view.findParentByType("form").findParentByType("form");

        if(form.getForm().findField('dqpsStatus').getValue()=='A'){
            Ext.Msg.alert('提示','当前评审状态为进行中，不可删除评委');
            return ;
        }
        Ext.MessageBox.confirm('警告', '您确定要删除所选评委及与其相关的数据吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    //设置评审规则-更多操作-批量重置选中评委密码
    resetPassword:function(btn) {
        var me = this,
            view = me.getView();
        var grid = view.down("grid[name=materialJudgeGrid]");
        var selList = grid.getSelectionModel().getSelection();

        var selectJudgeOprid="";
        var noMobileJudge="";

        var checkLen = selList.length;
        if(checkLen==0) {
            Ext.MessageBox.alert('提示','您没有选中任何记录');
            return;
        } else {
            for(var i=0;i<checkLen;i++) {
                if(selList[i].data.judgeMobile!="") {
                    if(selectJudgeOprid=="") {
                        selectJudgeOprid=selList[i].data.judgeOprid;
                    } else {
                        selectJudgeOprid+=","+selList[i].data.judgeOprid;
                    }
                } else {
                    noMobileJudge = "Y";
                }
            }
        }

        var msg = "";
        if(noMobileJudge=="Y") {
            msg = "没有手机的评委无法重置密码，其他评委密码重置成功"
        } else {
            msg = "评委密码重置成功";
        }

        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD","OperateType":"tzResetPassword","comParams":{"selectJudgeOprid":"'+selectJudgeOprid+'"}}';
        Ext.tzSubmit(tzParams,function(){
        },msg,true,this);

    },
    //设置评审规则-更多操作-给选中评委发送邮件
    sendEmail:function(btn) {
        var me = this,
            view = me.getView();
        var grid = view.down("grid[name=materialJudgeGrid]");
        var selList = grid.getSelectionModel().getSelection();

        var selectJudgeOprid="";

        var checkLen = selList.length;
        if(checkLen==0) {
            Ext.MessageBox.alert('提示','您没有选中任何记录');
            return;
        } else {
            for(var i=0;i<checkLen;i++) {
                if(selectJudgeOprid=="") {
                    selectJudgeOprid='"'+selList[i].data.judgeOprid+'"';
                } else {
                    selectJudgeOprid+=',"'+selList[i].data.judgeOprid+'"';
                }
            }
        }

        var comParams = '"selectJudgeOprid":['+selectJudgeOprid+']';

        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD","OperateType":"tzSendEmail","comParams":{'+comParams+'}}';
        Ext.tzLoad(tzParams,function(responseData){
            Ext.tzSendEmail({
                //发送的邮件模板;
                "EmailTmpName": ["TZ_CLPS_PW_M"],
                //创建的需要发送的听众ID;
                "audienceId": responseData.audienceId,
                //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "N"
            })
        });

    },
    //设置评审规则-更多操作-给选中评委发送短信
    sendMessage:function(btn) {
        var me = this,
            view = me.getView();
        var grid = view.down("grid[name=materialJudgeGrid]");
        var selList = grid.getSelectionModel().getSelection();

        var selectJudgeOprid="";

        var checkLen = selList.length;
        if(checkLen==0) {
            Ext.MessageBox.alert('提示','您没有选中任何记录');
            return;
        } else {
            for(var i=0;i<checkLen;i++) {
                if(selectJudgeOprid=="") {
                    selectJudgeOprid='"'+selList[i].data.judgeOprid+'"';
                } else {
                    selectJudgeOprid+=',"'+selList[i].data.judgeOprid+'"';
                }
            }
        }

        var comParams = '"selectJudgeOprid":['+selectJudgeOprid+']';

        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD","OperateType":"tzSendMessage","comParams":{'+comParams+'}}';
        Ext.tzLoad(tzParams,function(responseData){
            Ext.tzSendSms({
                //发送的短信模板;
                "SmsTmpName": ["TZ_CLPS_PW_M"],
                //发送的听众;
                "audienceId": responseData.audienceId
            })
        });
    },
    //设置评审规则-更多操作-批量导出评委
    exportJudge:function(btn) {
        var me = this,
            view = me.getView();
        var form = view.child("form").getForm();
        var classId = form.findField("classId").getValue();
        var batchId = form.findField("batchId").getValue();

        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD","OperateType":"tzExportJudge","comParams":{"classId":"'+classId+'","batchId":"'+batchId+'"}}';
        Ext.tzSubmit(tzParams,function(respDate){
            var fileUrl = respDate.fileUrl;
            window.open(fileUrl);
        },"导出评委成功",true,this);
    },
    //设置评审规则-保存
    onRuleSave:function(btn) {
        var me = this,
            view = me.getView();
        var form = view.child("form").getForm();
        var clpsksNum = form.findField("clpsksNum").getValue();
        var classId = form.findField("classId").getValue();
        var batchId = form.findField("batchId").getValue();

        var grid = view.down("grid[name=materialJudgeGrid]");
        var judgeStore = grid.getStore();
        var tzParams = '{"classId":"'+classId+'","batchId":"'+batchId+'"}';
        judgeStore.tzStoreParams = tzParams;

        var actType = view.actType;

        if(form.isValid()) {

            var saveFlag = true;
            for(var i=0;i<judgeStore.getCount();i++) {
                var judgeGroup = judgeStore.getAt(i).get("judgeGroup");
                if(judgeGroup=="" || judgeGroup==null) {
                    saveFlag=false;
                    break;
                }
            }

            if(saveFlag==false) {
                Ext.Msg.alert('提示', '请给评委设置评委组');
                return;
            } else {

                //校验评委各组评议人数合是否等于考生人数
                var tzParamsNum = me.getCheckNumParams();
                Ext.tzLoad(tzParamsNum, function (responseData) {
                    if (responseData.success == true) {
                    } else {
                        Ext.Msg.alert('提示', '评委各组评议人数和不等于考生人数');
                    }
                });
                    
                var tzParams = me.getRuleParams(actType);
                Ext.tzSubmit(tzParams, function (responseData) {
                    judgeStore.reload();

                    if (actType == "add") {
                        view.actType = "update";
                    }
                    if (btn.name == 'onRuleEnsure') {
                        view.close();
                    }


                    /* var judgeNumTotal = 0;
                     var records = judgeStore.getRange(0,judgeStore.getCount()-1);
                     for(var i=0;i<records.length;i++) {
                     judgeNumTotal = parseInt(judgeNumTotal)+parseInt(records[i].data.judgeExamineeNum);
                     }
                     var statisticsForm = view.down("form[name=statisticsNumForm]").getForm();
                     form.findField("judgeNumTotal").setValue(judgeNumTotal);*/

                }, "", true, this);

            }

            /*var judgeGroupData = [];

            var judgeGroupStore = new KitchenSink.view.common.store.comboxStore({
                recname:'TZ_CLPS_GR_TBL',
                condition:{
                    TZ_JG_ID:{
                        value:Ext.tzOrgID,
                        operator:'01',
                        type:'01'
                    }
                },
                result:'TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME',
                listeners:{
                    load:function(store,records,successful,eOpts) {
                        for(i=0;i<records.length;i++) {
                            judgeGroupData.push(records[i].data);
                        }

                        var judgeGroup,judgeExamineeNum;
                        var judgeExamineeTotal=0;
                        var judgeExamineeTotalTmp=0;
                        var arrayTotal = [];

                        for(var i=0;i<judgeGroupData.length;i++) {
                            var rec = judgeGroupData[i];
                            judgeGroup = rec.TZ_CLPS_GR_ID;

                            for(var j=0;j<judgeStore.getCount();j++) {
                                var record = judgeStore.getAt(j);
                                if(judgeGroup == record.get("judgeGroup")) {
                                    judgeExamineeNum = record.get("judgeExamineeNum");
                                    judgeExamineeTotal+=judgeExamineeNum;
                                }
                            }

                            arrayTotal.push(judgeExamineeTotal);
                            judgeExamineeTotalTmp+=judgeExamineeTotal;
                            judgeExamineeTotal=0;
                        }

                        var checkFlag=true;
                        for(var k=0;k<arrayTotal.length;k++) {
                            if(clpsksNum!=arrayTotal[k]) {
                                checkFlag=false;
                                break;
                            }
                        }

                        if(checkFlag==true) {
                            var statisticsNumForm = view.down("form[name=statisticsNumForm]").getForm();
                            statisticsNumForm.findField("judgeNumTotal").setValue(judgeExamineeTotalTmp);

                            var tzParams = me.getRuleParams(actType);
                            Ext.tzSubmit(tzParams,function(responseData) {
                                if(actType=="add") {
                                    view.actType="update";
                                }
                                if(btn.name=='onRuleEnsure') {
                                    view.close();
                                }
                            },"",true,this);
                        } else {
                            Ext.Msg.alert('提示','评委各组评议人数合不等于考生人数');
                            return ;
                        }

                    }
                }
            });*/
        }
    },
    //设置评审规则-关闭
    onRuleClose:function() {
        this.getView().close();
    },
    //设置评审规则，校验评委各组评议人数合是否等于考生人数
    checkJudgeExamineeTotal:function() {
        var me = this,
            view = me.getView();

        var form = view.child("form").getForm();
        var clpsksNum = form.findField("clpsksNum").getValue();

        var grid = view.down("grid[name=materialJudgeGrid]");
        var judgeStore = grid.getStore();
        if(!judgeStore.isLoaded()) {
            var classId = form.findField("classId").getValue();
            var batchId = form.findField("batchId").getValue();
            var tzParams = '{"classId":"'+classId+'","batchId":"'+batchId+'"}';
            judgeStore.tzStoreParams = tzParams;
            judgeStore.load();
        }

       /* var judgeGroupStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_CLPS_GR_TBL',
            condition:{
                TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                }
            },
            result:'TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME'
        });
        judgeGroupStore.load();*/

        var judgeGroupData = view.judgeGroupData;

        var judgeGroup,judgeExamineeNum;
        var judgeExamineeTotalTmp=0;
        var arrayTotal = [];

        for(var i=0;i<judgeGroupData.length;i++) {
            var judgeExamineeTotal=0;
            var rec = judgeGroupData[i];
            judgeGroup = rec.TZ_CLPS_GR_ID;

            for(var j=0;j<judgeStore.getCount();j++) {
                var record = judgeStore.getAt(j);
                if(judgeGroup == record.get("judgeGroup")) {
                    judgeExamineeNum = record.get("judgeExamineeNum");
                    judgeExamineeTotal = parseInt(judgeExamineeTotal)+parseInt(judgeExamineeNum);
                }
            }

            arrayTotal.push(judgeExamineeTotal);
            judgeExamineeTotalTmp+=judgeExamineeTotal;
        }

        var checkFlag=true;
        for(var k=0;k<arrayTotal.length;k++) {
            if(clpsksNum!=arrayTotal[k]) {
                checkFlag=false;
                break;
            }
        }

        if(checkFlag==true) {
            var statisticsNumForm = view.down("form[name=statisticsNumForm]").getForm();
            statisticsNumForm.findField("judgeNumTotal").setValue(judgeExamineeTotalTmp);
        }

        return checkFlag;

    },
    //设置评审规则-保存参数
    getRuleParams:function(actType) {
        var me = this,
            view = me.getView();

        var actType = view.actType;

        var form = view.child("form").getForm();
        var formValues = form.getValues();
        if(form.findField("judgeNumSet").disabled==true) {
        	formValues.judgeNumSet = form.findField("judgeNumSet").getValue();
        }
        //formValues.startTime = form.findField("startTime").getValue();
        //formValues.endTime = form.findField("endTime").getValue();

        var comParams="";

        var editJson="";
        editJson = '{"typeFlag":"RULE","data":'+Ext.JSON.encode(formValues)+'}';

        var grid = view.down("grid[name=materialJudgeGrid]");
        var store = grid.getStore();
        var editRecs = store.getModifiedRecords();
        for(var i=0;i<editRecs.length;i++) {
            editJson = editJson + ',' + '{"typeFlag":"JUDGE","data":' + Ext.JSON.encode(editRecs[i].data) + '}';
        }

        if (actType == "add") {
            comParams = '"add":[' + editJson + ']';
        } else {
            comParams = '"update":[' + editJson + ']';
        }

        var removeJson = "";
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
        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD","OperateType":"U","comParams":{' + comParams + '}}';

        return tzParams;

    },
    //设置评审规则-校验评委各组评议人数合是否等于考生人数参数
    getCheckNumParams:function() {
        var me = this,
            view = me.getView();

        var actType = view.actType;

        var form = view.child("form").getForm();
        var clpsksNum = form.findField("clpsksNum").getValue();
        var judgeNumSet = form.findField("judgeNumSet").getValue();


        var comParams= '"clpsksNum":"'+ clpsksNum+ '","judgeNumSet":"' + judgeNumSet +'"';

        var editJson="";

        var grid = view.down("grid[name=materialJudgeGrid]");
        var store = grid.getStore();
        var records = store.getRange(0,store.getCount()-1);
        for(var i=0;i<records.length;i++) {
            if(editJson!="") {
                editJson = editJson + ',' + Ext.JSON.encode(records[i].data);
            } else {
                editJson = Ext.JSON.encode(records[i].data);
            }
        }

        if(editJson!="") {
            comParams = comParams + ',"data":[' + editJson + "]";
        }

        //提交参数
        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD","OperateType":"tzCheckNum","comParams":{'+comParams+'}}';
        return tzParams;

    }
});
