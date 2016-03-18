Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.GSMmaterialsReview',
    queryClassBatch:function(btn){Ext.tzShowCFGSearch({
        cfgSrhId: 'TZ_REVIEW_GSMCL_COM.TZ_GSMCL_LIST_STD.TZ_GSM_CL_BAT_V',
        condition :{TZ_JG_ID:Ext.tzOrgID},
        callback: function(seachCfg){
            var store = btn.findParentByType("grid").store;
            store.tzStoreParams = seachCfg;
            store.load();
        }
    });
    },
    selectProject:function(trigger){
        Ext.tzShowPromptSearch({
            recname: 'TZ_CLASS_V',
            searchDesc: '选择项目',
            maxRow:20,
            condition:{
                presetFields:
                {
                    TZ_JG_ID:{
                        value:Ext.tzOrgID,
                        type:'01'
                    }
                },
                srhConFields:{
                    TZ_CLASS_ID:{
                        desc:'项目编号',
                        operator:'07',
                        type:'01'
                    },
                    TZ_CLASS_NAME:{
                        desc:'项目名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_CLASS_ID: '项目编号',
                TZ_CLASS_NAME: '项目名称'
            },
            multiselect: false,
            callback: function(selection){
                var projectID = selection[0].data.TZ_CLASS_ID;
                var projectName = selection[0].data.TZ_CLASS_NAME;
                var form = trigger.findParentByType('form');
                form.getForm().findField("classID").setValue(projectID);
                form.getForm().findField("className").setValue(projectName);
            }
        })
    },
    addClassBatch:function(btn){
        var grid=btn.findParentByType("grid");
        var win = Ext.create('Ext.window.Window', {
            title: '批次管理',
            width: 500,
            height: 235,
            modal: true,
            frame: true,
            items: [
                new Ext.form.Panel({
                    xtype: 'form',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    border: false,
                    bodyPadding: 10,
                    bodyStyle: 'overflow-y:auto;overflow-x:hidden',

                    fieldDefaults: {
                        msgTarget: 'side',
                        labelWidth: 140,
                        labelStyle: 'font-weight:bold'
                    },
                    items: [  {
                        xtype: 'textfield',
                        fieldLabel: '招聘项目ID',
                        name: 'classID',
                        hidden:true,
                        ignoreChangesFlag: true
                    },
                        {
                            xtype: 'textfield',
                            fieldLabel: '招聘项目',
                            name: 'className',
                            editable: false,
                            triggers: {
                                search: {
                                    cls: 'x-form-search-trigger',
                                    handler:this.selectProject
                                }
                            },
                            ignoreChangesFlag: true
                        }, {
                            xtype: 'textfield',
                            fieldLabel: '材料评审批次名称',
                            name: 'batchName',
                            ignoreChangesFlag: true
                        }
                    ]
                    //items END
                })],
            buttons: [{
                text: '确定',
                iconCls: "ensure",
                handler: function (btn) {
                    var form = btn.findParentByType("panel").child("form").getForm(),
                        formParams=form.getValues();
                    formParams.type="add";

                    var result = {update:[formParams]},
                        comParams = Ext.JSON.encode(result);


                    var tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_LIST_STD","OperateType":"U","comParams":' + comParams + '}';
                    Ext.tzSubmit(tzParams,function(responseData){
                        //alert(responseData);
                        if(responseData!="您输入的批次名在当前项目中已存在，请重新输入")
                        {

                            grid.getStore().reload();
                            btn.findParentByType("panel").close();}
                        else {
                            return;
                        }

                    },"",true,this);

                }
            }, {
                text: '关闭',
                iconCls: "close",
                handler: function (btn) {
                    btn.findParentByType("panel").close();
                }
            }]
        });
        win.show();
    },
    editClassBatch:function(btn,rowIndex) {
        var grid= btn.findParentByType('grid');
        var selList =  grid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要编辑的记录");
            return;
        }
        if (checkLen>=2) {
            Ext.Msg.alert("提示", "请您仅选择一条记录进行编辑");
            return;
        } else{
            var rec=selList[0];
            var data = {
                classID:rec.data.classID,
                className:rec.data.className,
                batchID:rec.data.batchID,
                batchName:rec.data.batchName
            };
            var win = Ext.create('Ext.window.Window', {
                title: '批次管理',
                width: 500,
                height: 235,
                modal: true,
                frame: true,
                items: [{
                    xtype: 'form',
                    id:'projectForm',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    border: false,
                    bodyPadding: 10,
                    bodyStyle: 'overflow-y:auto;overflow-x:hidden',

                    fieldDefaults: {
                        msgTarget: 'side',
                        labelWidth: 140,
                        labelStyle: 'font-weight:bold'
                    },
                    items: [{
                        xtype: 'textfield',
                        fieldLabel: '招聘项目ID',
                        name: 'classID',
                        hidden:true,
                        ignoreChangesFlag: true
                    },
                        {
                            xtype: 'textfield',
                            fieldLabel: '招聘项目',
                            name: 'className',
                            editable: false,
                            ignoreChangesFlag: true
                        },{
                            xtype: 'textfield',
                            fieldLabel: '材料评审批次ID',
                            name: 'batchID',
                            ignoreChangesFlag: true,
                            hidden:true,
                            editable: false
                        },{
                            xtype: 'textfield',
                            fieldLabel: '材料评审批次名称',
                            name: 'batchName',
                            ignoreChangesFlag: true
                        }
                    ]
                    //items END
                }],
                buttons: [{
                    text: '确定',
                    iconCls: "ensure",
                    handler: function (btn) {
                        var form = btn.findParentByType("panel").child("form").getForm(),
                            formParams=form.getValues();
                        formParams.type="update";
                        var result = {update:[formParams]},
                            comParams = Ext.JSON.encode(result);

                        var tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_LIST_STD","OperateType":"U","comParams":' + comParams + '}';
                        Ext.tzSubmit(tzParams,function(responseData){
//alert(responseData);
                            if(responseData!="您输入的批次名在当前项目中已存在，请重新输入")
                            { grid.getStore().reload();
                                btn.findParentByType("panel").close();}

                        },"",true,this);

                    }
                }, {
                    text: '关闭',
                    iconCls: "close",
                    handler: function (btn) {
                        btn.findParentByType("panel").close();
                    }
                }]
            });
            Ext.getCmp('projectForm').getForm().setValues(data);
            win.show();
        }
    },

    setReviewRule:function(grid,rowIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_GSMCL_COM"]["TZ_GSMCL_RULE_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GSMCL_RULE_STD，请检查配置。');
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

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        var batchID = record.data.batchID;
        cmp = new ViewClass(classID,batchID);
        cmp.classID=classID;
        cmp.batchID=batchID;
        var appStore=cmp.child('form').down("grid[name=GSMmaterialsReviewApplicantsGrid]").getStore();
        console.log(appStore);
        appStore.tzStoreParams='{"classID":"'+classID+'","batchID":"'+batchID+'"}',
            cmp.on('afterrender',function(panel){
                var form = panel.child('form').getForm();
                var tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_RULE_STD",' +
                    '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
                Ext.tzLoad(tzParams,function(respData){
                    console.log('dsa');
                    var formData = respData.formData;
                    formData.batchName = record.data.batchName;
                    formData.className = record.data.className;
                    var startReviewButton=panel.child('form').down("button[name=startReview]");
                    var closeReviewButton=panel.child('form').down("button[name=closeReview]");

                    if (formData.reviewStatus=='已结束'|| formData.reviewStatus=='未开始')
                    {

                        closeReviewButton.setDisabled(true);
                        closeReviewButton.setType = 0;
                        closeReviewButton.flagType = 'negative';}
                    if (formData.reviewStatus=='进行中')
                    {

                        startReviewButton.setDisabled(true);
                        startReviewButton.setType = 0;
                        startReviewButton.flagType = 'negative';}

                    form.setValues(formData);

                });
//读取申请人 grid

            });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    sendEmail: function(btn) {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_GSMCL_COM"]["TZ_GSMCL_MAIL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有发送邮件的权限');
            return;
        }
        var grid=btn.findParentByType("form").child('grid');
        var store = grid.getStore();
        var modifiedRecs = store.getModifiedRecords();
        if(modifiedRecs.length>0){
            Ext.MessageBox.alert("提示","请先保存列表数据之后再发送邮件！");
            return;
        };

        var datas = grid.getSelectionModel().getSelection(),
            arr = [];
        if (datas.length==0){
            Ext.Msg.alert('提示','请选择发送邮件的评委');
            return;
        }
        for(var x=datas.length-1;x>=0;x--){
            arr.push(datas[x].data.judgeID);
        }
        var params = {
            ComID:"TZ_REVIEW_GSMCL_COM",
            PageID:"TZ_GSMCL_MAIL_STD",
            OperateType:"ReLis",
            comParams:{type:"ReLis",oprArr:arr}
        };
        Ext.Ajax.request({
            url:Ext.tzGetGeneralURL,
            params:{tzParams:Ext.JSON.encode(params)},
            success:function(responseData){

                var audID = Ext.JSON.decode(responseData.responseText).comContent;

                Ext.tzSendEmail({
                    //发送的邮件模板;
                    "EmailTmpName": ["TZ_GSM_CLPS_EMA"],
                    //创建的需要发送的听众ID;
                    "audienceId": audID,
                    //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                    "file": "N"
                });
            }
        })
    },
    setJudgeForStudent:function(btn){

        var grid = btn.findParentByType('grid'),
            form=grid.findParentByType('form'),
            datas = form.getForm().getValues();
        if(form.getForm().findField('reviewStatus').getValue()=='进行中')
        { Ext.Msg.alert('提示','当前评审状态为进行中，不可为申请人设置评委');
            return ;}
        var classID=form.getForm().findField('classID').getValue(),
            batchID=form.getForm().findField('batchID').getValue();
        var appStore=grid.getStore();
        if (appStore.getCount()==0){
            Ext.Msg.alert('注意','未添加申请人，不可设置评委')
            return;
        }
        var modiefyGrid=appStore.getModifiedRecords(),
            removeGrid=appStore.getRemovedRecords();
        if (modiefyGrid!=0||removeGrid!=0)
        {
            Ext.Msg.alert('提示','请您先保存申请人页面数据，再为申请人设置评委');
            return;
        }

        tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_APPS_STD","OperateType":"BUTTON","comParams":{"type":"setJudgeForStudent","classID":"' + classID + '","batchID":"' + batchID + '"}}';
        Ext.tzLoad(tzParams, function (respData) {

            appStore.reload()

        });

    },
    viewGSMApplicationForm : function(btn){

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_GSMCL_COM"]["TZ_GSMCL_APPS_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        //var className = 'KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicants';
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GSMCL_APPS_STD，请检查配置。');
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


        var form = this.findCmpParent(btn.target).findParentByType('form').getForm(),
            classID = form.findField('classID').getValue(),
            batchID = form.findField('batchID').getValue(),
            className = form.findField('className').getValue(),
            batchName = form.findField('batchName').getValue();
        cmp = new ViewClass();
        cmp.classID=classID;
        cmp.batchID=batchID;

        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();

            var tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_APPS_STD",' +
                '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';

            var tzStoreParams ='{"classID":"'+classID+'","batchID":"'+batchID+'"}';



            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                formData.className = className;
                formData.batchName = batchName;
                form.setValues(formData);
                var store = panel.child('form').child("grid").store;
                store.tzStoreParams = tzStoreParams;
                store.load({
                    scope: this,
                    callback: function(records, operation, success) {
                    }
                });
            });
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    findCmpParent : function(ele){
        //根据当前DOM节点，向上查找最近的包含EXT节点对象的节点并返回该EXT节点对象
        if(ele){
            while(!Ext.getCmp(ele.parentNode.id)){
                ele = ele.parentNode;
            }
            return Ext.getCmp(ele.parentNode.id);
        }else{
            return false;
        }
    },
    addJudgeSave:function(btn){

        //由于新增弹出窗口会是当前页面不能操作，弹出窗口对应的tab即是当前活动的Tab
        var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),
            targetStore = activeTab.down("grid[name=GSMmaterialsReviewJudgeGrid]").getStore(),
            select = btn.findParentByType("panel").child('grid').getSelectionModel().getSelection();
        for(var x =0;x<select.length;x++){

            if(targetStore.find('judgeID',select[x].data.judgeID)<0) {
                if (select[x].data.judgeType=="材料评委")
                { var judgeGroup="A";}
                if (select[x].data.judgeType=="系主任")
                {  var judgeGroup="B";}
                var judgeModel = new KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewJudgeAccountModel({
                    classID:select[x].data.classID,
                    batchID:select[x].data.batchID,
                    judgeID:select[x].data.judgeID,
                    judgeName:select[x].data.judgeName,
                    judgeDepart:select[x].data.judgeDepart,
                    judgeType:select[x].data.judgeType,
                    judgePhoneNumber:select[x].data.judgePhoneNumber,
                    judgeEmail:select[x].data.judgeEmail,
                    judgeOAID:select[x].data.judgeOAID,
                    judgeGroup:judgeGroup
                });

                targetStore.add(judgeModel);
            }else{
                var isExist = true;
            }
//            if(isExist){
//                Ext.Msg.alert("提示","在您所选的记录中，有评委已经存在于列表中");
//            }

        }
        var store=btn.findParentByType("panel").child('grid').getStore();

    },
    addJudgeEnsure:function(btn){

        //由于新增弹出窗口会是当前页面不能操作，弹出窗口对应的tab即是当前活动的Tab
        var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),
            targetStore = activeTab.down("grid[name=GSMmaterialsReviewJudgeGrid]").getStore(),
            select = btn.findParentByType("panel").child('grid').getSelectionModel().getSelection();
        for(var x =0;x<select.length;x++){

            if(targetStore.find('judgeID',select[x].data.judgeID)<0,[anyMatch=false],[exactMatch =true]) {

                var judgeModel = new KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewJudgeAccountModel({
                    classID:select[x].data.classID,
                    batchID:select[x].data.batchID,
                    judgeID:select[x].data.judgeID,
                    judgeName:select[x].data.judgeName,
                    judgeDepart:select[x].data.judgeDepart,
                    judgeType:select[x].data.judgeType,
                    judgePhoneNumber:select[x].data.judgePhoneNumber,
                    judgeEmail:select[x].data.judgeEmail,
                    judgeOAID:select[x].data.judgeOAID,
                    judgeGroup:select[x].data.judgeGroup
                });

                targetStore.add(judgeModel);
            }else{
                var isExist = true;
            }
            if(isExist){
                Ext.Msg.alert("提示","在您所选的记录中，有评委已经存在于列表中");
            }

        }
        var store=btn.findParentByType("panel").child('grid').getStore();
        btn.findParentByType("panel").close();
    },
    addJudgeClose:function(btn){ btn.findParentByType("panel").close();},
    onReviewRuleSave:function(btn){
        var me =this;
        var form = this.getView().child("form").getForm();
        var grid= me.getView().lookupReference("GSMmaterialsReviewJudgeGrid");
        var AppGrid= me.getView().lookupReference("GSMmaterialsReviewApplicantsGrid");
        //var store=grid.store;
        var store = grid.getStore();
        var AppStore=AppGrid.getStore();


        if (form.isValid()) {
            var tzParams = this.getRuleParams();


            Ext.tzSubmit(tzParams,function(responseData){
                var grid = me.getView().lookupReference("GSMmaterialsReviewJudgeGrid");
                var store = grid.getStore();
//
//                if(tzParams.indexOf('"typeFlag":"JUDGE"')>-1||tzParams.indexOf("delete")>-1){
//
//                    store.reload();
//                }

            },"",true,this);
        }
    },
    onReviewRuleEnsure: function(btn){
        var me =this;
        var form = this.getView().child("form").getForm();
        var grid= me.getView().lookupReference("GSMmaterialsReviewJudgeGrid");
        var store = grid.getStore();

        if (form.isValid()) {
            var tzParams = this.getRuleParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                comView.close();
            },"",true,this);
        }
    },
//    getStudentRuleParams:function(btn){
//        var form = this.getView().child("form").getForm();
//        var Appgrid = this.getView().lookupReference("GSMmaterialsReviewApplicantsGrid");
//        var AppStore = Appgrid.getStore();
//        var studentComParams,studentJson;
//        if (AppStore.getRemovedRecords()!=""||AppStore.getModifiedRecords()!="")
//        {
//
//            var removedData = AppStore.getRemovedRecords(),
//                updateData = AppStore.getModifiedRecords(),
//
//                JSONData={},
//                classID = form.findField('classID').getValue(),
//                batchID =form.findField('batchID').getValue();
//            //console.log(removedData,updateData,newData)
//
//            if(removedData.length !== 0){
//                JSONData.delete = [];
//                for(var x =removedData.length-1;x>=0;x--){
//                    JSONData.delete[x] = {};
//                    JSONData.delete[x].appInsID = removedData[x].data.appInsID;
//                    JSONData.delete[x].classID = classID;
//                    JSONData.delete[x].batchID = batchID;
//                    JSONData.delete[x].intentID = removedData[x].data.intentID;;
//
//                }
//            }
//            //更新数据
//            if(updateData.length !== 0){
//                JSONData.update =[];
//                for(var x = updateData.length-1;x>=0;x--){
//                    JSONData.update[x] ={};
//                    delete updateData[x].data.id;
//                    JSONData.update[x].classID = classID;
//                    JSONData.update[x].batchID = batchID;
//                    // JSONData.update[x].batchID = updateData[x].data.batchID;
//                    JSONData.update[x].intentID = updateData[x].data.intentID;
//                    JSONData.update[x].appInsID = updateData[x].data.appInsID;
//
//                    JSONData.update[x].interviewQualification = updateData[x].data.interviewQualification;
//
//                }
//            }
//            studentComParams=Ext.JSON.encode(JSONData);
//            //提交参数
//            var tzStudentParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_APPS_STD","OperateType":"U","comParams":' + studentComParams + '}';
//        }
//        return tzStudentParams;
//    },
    getRuleParams:function(){
        var form = this.getView().child("form").getForm();
        var formParams = form.getValues();


        //更新操作参数
        var comParams = "";

        //修改json字符串
        var editJson = "";
        var studentJson="";

        editJson = '{"typeFlag":"RULE","data":'+Ext.JSON.encode(formParams)+'}';

        var AppGrid= this.getView().lookupReference("GSMmaterialsReviewApplicantsGrid");
        var AppStore = AppGrid.getStore();
        var studentComParams,studentJson;

        var editStudentRecs = AppStore.getModifiedRecords();
        for(var t=0;t<editStudentRecs.length;t++){
            if(studentJson=="")
            {studentJson='{"typeFlag":"STUDENT","data":'+Ext.JSON.encode(editStudentRecs[t].data)+'}'}
            else{
                studentJson = studentJson + ','+'{"typeFlag":"STUDENT","data":'+Ext.JSON.encode(editStudentRecs[t].data)+'}';}
        }

        var grid = this.getView().lookupReference("GSMmaterialsReviewJudgeGrid");
        var store = grid.getStore();

        var editRecs = store.getModifiedRecords();
        for(var i=0;i<editRecs.length;i++){
            editJson = editJson + ','+'{"typeFlag":"JUDGE","data":'+Ext.JSON.encode(editRecs[i].data)+'}';
        }

        if(studentJson==""){
            comParams = '"update":[' + editJson +"]";
        }else{
            comParams = '"update":[' + editJson +','+studentJson+ "]";
        }


        var removeStudentJson = "";
        //删除记录
        var removeStundentRecs = AppStore.getRemovedRecords();

        for(var i=0;i<removeStundentRecs.length;i++){
            if(removeStudentJson == ""){
                removeStudentJson = Ext.JSON.encode(removeStundentRecs[i].data);
            }else{
                removeStudentJson = removeStudentJson + ','+Ext.JSON.encode(removeStundentRecs[i].data);
            }
        }


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

        if(removeJson == ""){
            if(removeStudentJson != ""){ removeJson=removeStudentJson}

        }
        if(removeJson != ""){
            if(removeStudentJson != ""){ removeJson=removeJson+','+removeStudentJson}

        }
        if(removeJson != "") {
            if (comParams == "") {
                comParams = '"delete":[' + removeJson + "]";
            } else {
                comParams = comParams + ',"delete":[' + removeJson + "]";
            }
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_RULE_STD","OperateType":"U","comParams":{'+comParams+'}}';

        return tzParams;
    },
    onReviewRuleClose:function(btn){
        this.getView().close();
    },



    addJudge:function(btn){
        var form = btn.findParentByType("form").findParentByType("form").getForm();
        if (form.findField('reviewStatus').getValue()=='进行中')
        { Ext.Msg.alert('提示','当前评审状态为进行中，不可添加评委');
            return ;}
        var classID = form.findField("classID").getValue(),
            batchID = form.findField("batchID").getValue();
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_GSMCL_COM"]["TZ_GSMCL_ADDJD_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GSMCL_ADDJD_STD，请检查配置。');
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
        //cmp = new ViewClass();
        cmp = new ViewClass();
        cmp.on('afterrender',function(win){
            var store = win.child('grid').getStore();
            tzStoreParams = '{"classID":"'+classID+'","batchID":"'+batchID+'"}';
            store.tzStoreParams = tzStoreParams;
            store.load();
//                store.filterBy(function(record) {
//                    return  record.get('judgeType') == "材料评委"||record.get('judgeType') == "系主任"})
        });

        cmp.show();

    },
    removeJudge:function(view,rowIndex){
        var form = view.findParentByType("form").findParentByType("form").getForm();

        if (form.findField('reviewStatus').getValue()=='进行中')
        { Ext.Msg.alert('提示','当前评审状态为进行中，不可删除评委');

            return ;}
        Ext.MessageBox.confirm('警告', '您确定要删除所选评委及与其相关的数据吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    viewApplicants:function(grid,rowIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_GSMCL_COM"]["TZ_GSMCL_APPS_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GSMCL_APPS_STD，请检查配置。');
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

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        var batchID = record.data.batchID;
        cmp = new ViewClass();
        cmp.classID=classID;
        cmp.batchID=batchID;

//        cmp.on('afterrender',function(panel){
//            var form = panel.child('form').getForm();
//
//            var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_APPS_STD",' +
//                '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
//
//            var tzStoreParams ='{"cfgSrhId": "TZ_REVIEW_CL_COM.TZ_CLPS_APPS_STD.TZ_CLPSKS_VW",' +
//                '"condition":{"TZ_CLASS_ID-operator":"01","TZ_CLASS_ID-value":"'+classID+'","TZ_APPLY_PC_ID-operator":"01","TZ_APPLY_PC_ID-value":"'+batchID+'"}}'
//
//            Ext.tzLoad(tzParams,function(respData){
//                var formData = respData.formData;
//                form.setValues(formData);
//                form.setValues(
//                    {className:record.data.className,batchName:record.data.batchName}
//                );
//                var store = panel.child('form').child("grid").store;
//                store.tzStoreParams = tzStoreParams;
//                store.load({
//                    scope: this,
//                    callback: function(records, operation, success) {
//                    }
//                });
//            });
//        }
        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();

            var tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_APPS_STD",' +
                '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';


            var tzStoreParams ='{"classID":"'+classID+'","batchID":"'+batchID+'"}';

            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;

                formData.className = record.data.className;
                formData.batchName = record.data.batchName;
                form.setValues(formData);
                if (formData.status=='B')
                {
                    var closeReviewButton=panel.child('form').down("button[name=closeReview]");
                    closeReviewButton.setDisabled(true);
                    closeReviewButton.setType = 0;
                    closeReviewButton.flagType = 'negative';}
                if (formData.status=='A')
                {
                    var startReviewButton=panel.child('form').down("button[name=startReview]");
                    startReviewButton.setDisabled(true);
                    startReviewButton.setType = 0;
                    startReviewButton.flagType = 'negative';}
                var store = panel.child('form').child("grid").store;
                store.tzStoreParams = tzStoreParams;
                store.load({
                    scope: this,
                    callback: function(records, operation, success) {
                    }
                });
            });
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    queryApplicants:function(btn){
        var form = btn.findParentByType("form").getForm();
        var classID=form.findField("classID").getValue();
        var batchID=form.findField("batchID").getValue();

        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_REVIEW_GSMCL_COM.TZ_GSMCL_APPS_STD.TZ_GSM_KS_VW',
            condition :{"TZ_CLASS_ID":classID,"TZ_APPLY_PC_ID":batchID},
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    queryAddApplicants:function(btn){
        var form = btn.findParentByType("form").getForm();
        var classID=form.findField("classID").getValue();
        var batchID=form.findField("batchID").getValue();

        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_REVIEW_GSMCL_COM.TZ_GSMCL_ADDAP_STD.TZ_GSM_ADD_KS_V',
            condition :{"TZ_REALNAME":classID,"TZ_APP_SUB_DTTM":batchID,"TZ_APP_FORM_STA":classID,"TZ_APP_L_TEXT":batchID},
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    addApplicants : function(btn){
        var form=btn.findParentByType('grid').findParentByType('form').getForm();

        if (form.findField('reviewStatus').getValue()=='进行中')
        { Ext.Msg.alert('提示','当前评审状态为进行中，不可添加申请人 ');
            return ;}
        var classID = form.findField('classID').value,
            batchID =form.findField('batchID').value,
            className=form.findField('className').value,
            config = {type:'GSMmaterialsStudent'};
        var data = {
            classID:classID,
            batchID:batchID,
            className:className
        };
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_GSMCL_COM"]["TZ_GSMCL_ADDAP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLPS_ADDAP_STD，请检查配置。');
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
        cmp=new ViewClass();
        cmp.on('afterrender',function(win){
            var store = win.child('form').child('grid').getStore(),
                tzStoreParams = '{"type":"'+config.type+'","classID":"'+classID+'","batchID":"'+batchID+'"}';
            store.tzStoreParams = tzStoreParams;
            store.load();
            win.child('form').getForm().setValues(data);


        });
        cmp.show();

    },
    addApplicantSave : function(btn,event){
        //由于新增弹出窗口会是当前页面不能操作，弹出窗口对应的tab即是当前活动的Tab
        var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),

            targetStore = activeTab.down("grid[name=GSMmaterialsReviewApplicantsGrid]").getStore();
        var form=btn.findParentByType("panel").child('form'),
            batchID=form.getForm().findField('batchID').getValue();

        var   select = form.child('grid').getSelectionModel().getSelection();
        var  selectApps="";


        if(select.length=='0')
        {
            btn.findParentByType("panel").close();
            return;
        }
        for (var i=0;i<select.length;i++){
            var appINSID=select[i].data.appInsID;
            if(targetStore.find('appInsID',select[i].data.appInsID)<0) {
                var applicantModel = new KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewApplicantsModel({
                    classID:select[i].data.classID,
                    batchID:select[i].data.batchID,
                    realName:select[i].data.realName,
                    gender:select[i].data.gender,
                    appInsID:select[i].data.appInsID,
                    oprID:"",
                    judgeList:"",
                    applyPosition:select[i].data.applyPosition,
                    intentOrder:"",
                    intentID:"",
                    intentDepart:"",
                    interviewQualification:""
                });
                targetStore.add(applicantModel);
                if (selectApps==""){
                    selectApps=appINSID;
                }else{
                    selectApps=selectApps+","+appINSID;
                }

            }else{
                var isExist = true;
            }
//            if(isExist){
//                Ext.Msg.alert("提示","在您所选的记录中，有申请人 已经存在于名单中");
//            }
        }


        var classID=select[0].data.classID;


        var tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_ADDAP_STD","OperateType":"ADDAPP","comParams":{"type":"ADDAPP","classID":"' + classID + '","batchID":"' + batchID + '","selectApps":"' + selectApps + '"}}';
        Ext.tzLoad(tzParams, function (respData) {
            //          targetStore.reload();
        });


    },
    addApplicantEnsure : function(btn,event){
        //由于新增弹出窗口会是当前页面不能操作，弹出窗口对应的tab即是当前活动的Tab
        var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),
            targetStore = activeTab.down("grid[name=GSMmaterialsReviewApplicantsGrid]").getStore();
        var form=btn.findParentByType("panel").child('form'),
            batchID=form.getForm().findField('batchID').getValue();

        var   select = form.child('grid').getSelectionModel().getSelection();
        var  selectApps="";


        if(select.length=='0')
        {
            btn.findParentByType("panel").close();
            return;
        }
        for (var i=0;i<select.length;i++){

            var appINSID=select[i].data.appInsID;
            if (selectApps==""){
                selectApps=appINSID;
            }else{
                selectApps=selectApps+","+appINSID;
            }

        }
        var classID=select[0].data.classID;
        var  newRecord = [];

        var tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_ADDAP_STD","OperateType":"ADDAPP","comParams":{"type":"ADDAPP","classID":"' + classID + '","batchID":"' + batchID + '","selectApps":"' + selectApps + '"}}';

        Ext.tzLoad(tzParams, function (respData) {
            console.log(respData);
            for(var dd=0;dd<respData.length;dd++) {
                var gg = 0;
                for (var xxx = 0; xxx < targetStore.getCount(); xxx++) {
                    if (targetStore.getAt(xxx).data.appInsID == respData[dd].appInsID
                        && targetStore.getAt(xxx).data.intentID == respData[dd].intentID
                        ) {
                        gg = gg + 1;
                    }
                }
                if
                    (gg == 0) {
//
//
//                    var applicantModel = new KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewApplicantsModel({
//                        classID: respData[dd].classID,
//                        batchID: respData[dd].batchID,
//                        realName: respData[dd].realName,
//                        gender: respData[dd].gender,
//                        appInsID: respData[dd].appInsID,
//                        oprID: respData[dd].oprID,
//                        judgeList: respData[dd].judgeList,
//                        applyPosition: respData[dd].applyPosition,
//                        intentOrder: respData[dd].intentOrder,
//                        intentID: respData[dd].intentID,
//                        intentDepart: respData[dd].intentDepart,
//                        interviewQualification: respData[dd].interviewQualification
//                    });
//                    targetStore.add(applicantModel);

                    console.log(respData[dd])
                    newRecord.push(respData[dd]);
                    console.log(newRecord);
                }
                else {
                    var isExist = true;
                }

            }
//            if(isExist){
//                Ext.Msg.alert("提示","在您所选的记录中，有申请人 已经存在于名单中");
//            }

            //       targetStore.reload();
            targetStore.add(newRecord);
        });
//        var store=btn.findParentByType("panel").child('grid').getStore();

        btn.findParentByType("panel").close();

    },
    addApplicantClose : function(btn){
        btn.findParentByType("panel").close();
    },
    dumpTerm: function(btn){

        btn.findParentByType("grid").filters.clearFilters(true);
       // btn.findParentByType("grid").getStore().clearFilter();
    },

    editApplicants :function(btn,rowIndex){
        var form=btn.findParentByType('grid').findParentByType('form').getForm();

        //获得选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要编辑的记录");
            return;
        }
        if (checkLen>=2) {
            Ext.Msg.alert("提示", "请您仅选择一条记录进行编辑");
            return;
        } else{
            var form = btn.findParentByType("form").getForm(),
                grid = btn.findParentByType("grid"),
                classID = form.findField("classID").getValue(),
                batchID = form.findField("batchID").getValue(),
                datas =selList[0].data;
            if (datas.interviewQualification=="")
            {datas.interviewQualification="待定"}
            var win = Ext.create('Ext.window.Window', {
                title: '编辑申请人 ',
                width: 600,
                height: 285,
                modal: true,
                frame: true,
                items: [{
                    xtype: 'form',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    border: false,
                    bodyPadding: 10,
                    bodyStyle: 'overflow-y:auto;overflow-x:hidden',

                    fieldDefaults: {
                        msgTarget: 'side',
                        labelWidth: 110,
                        labelStyle: 'font-weight:bold'
                    },
                    items: [
                        {
                            xtype: 'textfield',
                            fieldLabel: '姓名',
                            name: 'realName',
                            editable: false,
                            value:datas.realName,
                            ignoreChangesFlag: true
                        }, {
                            xtype: 'textfield',
                            fieldLabel: '报名表编号',
                            name: 'appInsID',
                            editable: false,
                            value:datas.appInsID,
                            ignoreChangesFlag: true
                        }, {
                            xtype: 'combo',
                            fieldLabel: "面试资格",
                            name: 'interviewQualification',
                            store: {
                                fields: ["status", "desc"],
                                data: [
                                    {status: '有面试资格', desc: '有面试资格'},
                                    {status: '待定', desc: '待定'},
                                    {status: '无面试资格', desc: '无面试资格'}
                                ]
                            },
                            displayField: 'desc',
                            valueField: 'status',
                            queryMode: 'local',
                            editable: false,
                            value:datas.interviewQualification,
                            ignoreChangesFlag: true
                        }
                    ]
                    //items END
                }],
                buttons: [{
                    text: '确定',
                    iconCls: "ensure",
                    handler: function (btn) {
                        var form = btn.findParentByType("panel").child("form").getForm(),
                            interviewQualification = form.findField("interviewQualification").getValue(),
                            record = selList[0];
                        record.set("interviewQualification",interviewQualification||'');
                        btn.findParentByType("panel").close();
                    }
                }, {
                    text: '关闭',
                    iconCls: "close",
                    handler: function (btn) {
                        btn.findParentByType("panel").close();
                    }
                }]
            });
            win.show();
        }
    },
    editCurrentApplicant:function(btn,rowIndex){
        var self=this,
            form = btn.findParentByType("grid").findParentByType("form").getForm(),
            grid = btn.findParentByType("grid"),
            classID = form.findField("classID").getValue(),
            batchID = form.findField("batchID").getValue(),
            datas = grid.getStore().getAt(rowIndex).data;

        var interviewQualification=datas.interviewQualification;
        console.log(interviewQualification);
        if (interviewQualification=="")
        {  interviewQualification="W"}
        var win = Ext.create('Ext.window.Window', {
            title: '编辑申请人 ',
            width: 600,
            height: 285,
            modal: true,
            frame: true,
            items: [{
                xtype: 'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },
                items: [
                    {
                        xtype: 'textfield',
                        fieldLabel: '姓名',
                        name: 'realName',
                        editable: false,
                        value:datas.realName,
                        ignoreChangesFlag: true
                    }, {
                        xtype: 'textfield',
                        fieldLabel: '报名表编号',
                        name: 'appInsID',
                        editable: false,
                        value:datas.appInsID,
                        ignoreChangesFlag: true
                    }, {
                        xtype: 'combo',
                        fieldLabel: "面试资格",
                        name: 'interviewQualification',
                        store: {
                            fields: ["status", "desc"],
                            data: [
                                {status: '有面试资格', desc: '有面试资格'},
                                {status: '待定', desc: '待定'},
                                {status: '无面试资格', desc: '无面试资格'}
                            ]
                        },
                        displayField: 'desc',
                        valueField: 'status',
                        queryMode: 'local',
                        editable: false,
                        value:interviewQualification,
                        ignoreChangesFlag: true
                    }
                ]
                //items END
            }],
            buttons: [{
                text: '确定',
                iconCls: "ensure",
                handler: function (btn) {
                    var form = btn.findParentByType("panel").child("form").getForm(),
                        interviewQualification = form.findField("interviewQualification").getValue(),
                        record = grid.getStore().getAt(rowIndex);
                    console.log(interviewQualification);
                    record.set("interviewQualification",interviewQualification||'');

                    btn.findParentByType("panel").close();
                }
            }, {
                text: '关闭',
                iconCls: "close",
                handler: function (btn) {
                    btn.findParentByType("panel").close();
                }
            }]
        });
        win.show();
    },
    removeApplicants:function(btn){

        if(btn.findParentByType('grid').findParentByType('form').getForm().findField('reviewStatus').getValue()=='进行中')
        { Ext.Msg.alert('提示','当前评审状态为进行中，不可删除申请人 ');
            return ;}
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var resSetStore =  btn.findParentByType("grid").store;
        var checkLen = selList.length;
        var tiShi=0;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
//
                    for ( var i=0;i<checkLen;i++)
                    {
                        if (selList[i].data.judgeList!=0)
                        {
                            console.log(selList)
                            tiShi=tiShi+1;
                            selList.splice(i,"1"," ");
                            }

                    }
                    if(tiShi>0)
                    {
                        Ext.Msg.alert('提示','选中申请人中存在已被指定评委的申请人 ，不可删除');

                    }
                    resSetStore.remove(selList);
                }

            },"",true,this);
        }
    },
    setOwnQuary:function(btn){
        var win = Ext.create('Ext.window.Window', {
            title: '设置面试资格',
            width: 400,
            height: 150,
            modal: true,
            frame: true,
            items: [{
                xtype:'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                items:[{
                    xtype: 'combo',
                    fieldLabel: "面试资格",
                    name: 'interviewQualification',
                    store: {
                        fields: ["status", "desc"],
                        data: [
                            {status: '有面试资格', desc: '有面试资格'},
                            {status: '待定', desc: '待定'},
                            {status: '无面试资格', desc: '无面试资格'}
                        ]
                    },
                    displayField: 'desc',
                    valueField: 'status',
                    queryMode: 'local',
                    editable: false,
                    ignoreChangesFlag: true
                }]

                //items END
            }],
            buttons: [{
                text: '确定',
                iconCls: "ensure",
                handler:function(btn){
                    var form = btn.findParentByType("window").child("form").getForm(),
                        interviewQualification = form.findField("interviewQualification").getValue();
                    for(var x = 0;x<selList.length;x++) {
                        selList[x].set('interviewQualification',interviewQualification);
                    }
                    btn.findParentByType("panel").close();
                }
            }, {
                text: '关闭',
                iconCls: "close",
                handler: function (btn) {
                    btn.findParentByType("panel").close();
                }
            }]
        });
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection(),
            store=btn.findParentByType("grid").getStore();
        if(selList.length == 0){
            Ext.Msg.alert("提示","请选择要操作的记录");
            return;
        }else{
            win.show();

        }
    },
    setAppJudge:function(btn,rowIndex){
        var grid= this.getView().lookupReference("GSMmaterialsReviewApplicantsGrid");
        var modiefyGrid=grid.getStore().getModifiedRecords(),
            removeGrid=grid.getStore().getRemovedRecords();
        if (modiefyGrid!=0||removeGrid!=0)
        {
            Ext.Msg.alert('提示','请您先保存页面申请人 数据，再指定评委');
            return;
        }
        var form=btn.findParentByType('form').getForm();
        var classID = form.findField('classID').value;
        var batchID =form.findField('batchID').value;

        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_GSMCL_COM"]["TZ_GSMCL_APJUG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
//该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GSMCL_APJUG_STD，请检查配置。');
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
        cmp = new ViewClass(
            appGrid=grid,
            appRowIndex=rowIndex
        );
        cmp.on('afterrender',function(win){
            var store = win.child('grid').getStore(),
                tzStoreParams = '{"classID":"'+classID+'","batchID":"'+batchID+'"}';
            store.tzStoreParams = tzStoreParams;
            store.load();
        });
        cmp.show();
    },
    onApplicantsSave:function(btn){
        var store = btn.findParentByType('panel').down('grid[name=GSMmaterialsReviewApplicantsGrid]').store;
        var me =this;
        var form = this.getView().child("form").getForm();


        if (form.isValid()) {
            var tzParams = this.getApplicantsParams();
            var comView = this.getView();

            Ext.tzSubmit(tzParams, function (responseData) {
                var grid = btn.findParentByType('panel').down("grid[name=GSMmaterialsReviewApplicantsGrid]");
                var store = grid.getStore();
                if (tzParams.indexOf("add") > -1 || tzParams.indexOf("delete") > -1) {
                    store.reload();
                }

            }, "", true, this);
        }
    },

    onApplicantsClose:function(btn){
        this.getView().close();
    },
    onApplicantsEnsure: function(btn){
        var store = btn.findParentByType('panel').down('grid[name=GSMmaterialsReviewApplicantsGrid]').store;
        var me =this;
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            var tzParams = this.getApplicantsParams();
            var comView = this.getView();

            Ext.tzSubmit(tzParams,function(responseData){
                var grid = btn.findParentByType('panel').down("grid[name=GSMmaterialsReviewApplicantsGrid]");
                var store = grid.getStore();
                if(tzParams.indexOf("delete")>-1){
                    store.reload();
                }
                comView.close()
                //btn.findParentByType('panel').close();
            },"",true,this);


        }
    },
    viewJudge:function(grid,record,rowIndex){
        var gridData=grid.getStore().getAt(rowIndex).data;
        if(gridData.judgeList==""){
            return;
        }
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_GSMCL_COM"]["TZ_GSMCL_VWJUD_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
//该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GSMCL_VWJUD_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        var win = this.lookupReference('materialsReviewAppJugDetail');
        var panel = this.getView();

        var form = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab().child('form').getForm(),
            JSONData={
                appInsID:gridData.appInsID,
                classID  :form.findField('classID').getValue(),
                batchID : form.findField('batchID').getValue(),
                judgeList : gridData.judgeList.split(',')
            };

        var comParams = Ext.JSON.encode(JSONData);

        var params = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_VWJUD_STD","OperateType":"QG","comParams":'+comParams+'}';
        Ext.tzLoad(params, function(resp){

            var scoreItems=[];
//返回json的root
            var value=resp.root;
            console.log(value)
            //因为考虑到无评委不会提交请求，故返回的root内必有数据
            //所有数据的打分类型一致，取第一组数据打分类型
            var scoreTypes=value[0].score;
            var GridData,judgeScore,scoreItem;
            if(scoreTypes.length>0){
                for (var x =0 ; x <scoreTypes.length; x++) {
                    scoreItem = scoreTypes[x].name;
                    scoreItems.push(scoreItem);

                }

            }

            if (!win) {
                Ext.syncRequire(className);
                ViewClass = Ext.ClassManager.get(className);
                //新建类
                win = new ViewClass(scoreType=scoreItems);
                //this.getView().add(win);
                panel.add(win);
            }
            //设置操作类型

            var store=win.child('grid').getStore();
            store.tzStoreParams = comParams;
            store.load({  })
            Ext.resumeLayouts(true);

            if (win.floating) {
                win.show();
            }
        });

//        if(!Ext.ClassManager.isCreated(className)){
//            Ext.syncRequire(className);
//        }
//        store.tzStoreParams = tzStoreParams;
//
//        store.load({
//            callback:function(value){

//
//
//                }
//
//            }
//        });
//        cmp.show();
    },
    getApplicantsParams:function() {
        var form = this.getView().child("form").getForm();
        var grid = this.getView().lookupReference("GSMmaterialsReviewApplicantsGrid");
        var store = grid.getStore(),
            storeNumber=store.getCount();

        var removedData = store.getRemovedRecords(),
            updateData = store.getModifiedRecords(),

            comParas,JSONData={},
            classID = form.findField('classID').getValue(),
            batchID =form.findField('batchID').getValue();


        if(removedData.length !== 0){
            JSONData.deleted = [];
            for(var x =removedData.length-1;x>=0;x--){
                JSONData.deleted[x] = {};
                JSONData.deleted[x].appInsID = removedData[x].data.appInsID;
                JSONData.deleted[x].classID = classID;
                JSONData.deleted[x].batchID = batchID;
                JSONData.deleted[x].intentID = removedData[x].data.intentID;;

            }
        }
        //更新数据
        if(updateData.length !== 0){
            JSONData.update =[];
            for(var x = updateData.length-1;x>=0;x--){
                JSONData.update[x] ={};
                delete updateData[x].data.id;
                JSONData.update[x].classID = classID;
                JSONData.update[x].batchID = batchID;
                // JSONData.update[x].batchID = updateData[x].data.batchID;
                JSONData.update[x].intentID = updateData[x].data.intentID;
                JSONData.update[x].appInsID = updateData[x].data.appInsID;

                JSONData.update[x].interviewQualification = updateData[x].data.interviewQualification;

            }
        }
        comParas=Ext.JSON.encode(JSONData);
        //提交参数
        var tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_APPS_STD","OperateType":"U","comParams":' + comParas + '}';
        return tzParams;
    },
    startReview:function(btn){
        var form = btn.findParentByType('form'),
            datas = form.getForm().getValues();

        var classID=form.getForm().findField('classID').getValue(),
            batchID=form.getForm().findField('batchID').getValue();
        var appStore=form.down("grid[name=GSMmaterialsReviewApplicantsGrid]").getStore();
        if (appStore.getCount()==0){
            Ext.Msg.alert('注意','未添加申请人 ，不可开启评审')
            return;
        }
        var modiefyGrid=appStore.getModifiedRecords(),
            removeGrid=appStore.getRemovedRecords();
        if (modiefyGrid!=0||removeGrid!=0)
        {
            Ext.Msg.alert('提示','请您先保存页面申请人 数据，再开启评审');
            return;
        }




        if(btn.flagType === 'positive'){
            tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_APPS_STD","OperateType":"BUTTON","comParams":{"type":"startReviewOnclick","classID":"' + classID + '","batchID":"' + batchID + '"}}';
            Ext.tzLoad(tzParams, function (respData) {
                console.log(respData);
                if(respData==1){
                    appStore.reload();
                    Ext.Msg.alert( "注意","报考申请人中，有申请人所报志愿不存在相应系所评委，或不同时存在材料评委和系主任")
                }else {
                    appStore.reload();
                    form.getForm().findField('reviewStatus').setValue("进行中");
                    //可点击状态，设置setType值为1,当前按钮已点击
                    btn.setType = 1;
                    //获取重新开始本轮评审按钮
                    btn.setDisabled(true);
                    btn.flagType = 'negative';
                    var closeReviewButton = btn.findParentByType("form").down("button[name=closeReview]");
                    closeReviewButton.setDisabled(false);
                    closeReviewButton.setType = 0;
                    closeReviewButton.flagType = 'positive';
                }
            });

        }
    },
    closeReview:function(btn){
        var form = btn.findParentByType('form'),
            datas = form.getForm().getValues();
        var classID=form.getForm().findField('classID').getValue(),
            batchID=form.getForm().findField('batchID').getValue();
        if(btn.flagType === 'positive'){
            //可点击状态，设置setType值为1,当前按钮已点击
            tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_APPS_STD","OperateType":"BUTTON","comParams":{"type":"closeReviewOnclick","classID":"' + classID + '","batchID":"' + batchID + '"}}';
            Ext.tzLoad(tzParams, function (respData) {
            });
            form.getForm().findField('reviewStatus').setValue("已结束");
            btn.setType = 1;
            //获取重新开始本轮评审按钮
            btn.setDisabled(true);
            btn.flagType = 'negative';
            var reStartReviewButton=btn.findParentByType("form").down("button[name=startReview]");
            reStartReviewButton.setDisabled(false);
            reStartReviewButton.setType = 0;
            reStartReviewButton.flagType = 'positive';

        }

    },

    exportExcelApplicants: function(btn) {
        var appsGrid=btn.findParentByType('grid');
        appsGrid.saveDocumentAs({
            type: 'excel',
            title: '光华材料评审申请人 列表',
            fileName: '光华材料评审申请人 .xls'
        });
    },
    expsortExcelApplicants: function(btn){
        //Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");

        var appsGrid=btn.findParentByType('grid'),
            selList = appsGrid.getSelectionModel().getSelection();
        var form=appsGrid.findParentByType('form').getForm();

        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要打印申请人 表的记录");
            return;
        }
        else {
            //拼装评委信息
            var appsIDJson="";
            var classID=selList[0].data.classID,
                batchID=selList[0].data.batchID;


            if (selList.length==1){
                var appsIDJson= '"' + selList[0].data.appInsID + '"';

            }
            else{
                for (var i=0;i<selList.length-1;i++)
                {
                    if(appsIDJson=="")
                    {
                        appsIDJson= '"' + selList[i].data.appInsID;}
                    else{appsIDJson=appsIDJson+','+selList[i].data.appInsID}
                }
                appsIDJson=appsIDJson+','+selList[selList.length-1].data.appInsID+'"';
            }
            var comParams = '"appsARRAY":' + appsIDJson ;


            var tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_KSMD_STD","OperateType":"HTML","comParams":{"PF_TYPE":"KSMD","classID":"' + classID + '","batchID":"' + batchID + '",' + comParams + '}}';


            var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+tzParams;

            window.open(viewUrl, "下载","status=no,menubar=yes,toolbar=no,location=no");


        }
    },
    sendStudentEmail: function(btn) {

        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_GSMCL_COM"]["TZ_GSMCL_MAIL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有发送邮件的权限');
            return;
        }
        var classID=btn.findParentByType("grid").findParentByType("form").getForm().findField('classID').getValue(),
            batchID=btn.findParentByType("grid").findParentByType("form").getForm().findField('batchID').getValue();
        var grid=btn.findParentByType("grid");
        var store = grid.getStore();
        var modifiedRecs = store.getModifiedRecords();
        if(modifiedRecs.length>0){
            Ext.MessageBox.alert("提示","请先保存列表数据之后再发送邮件！");
            return;
        };

        var datas = grid.getSelectionModel().getSelection(),
            arr = [];
        if (datas.length==0){
            Ext.Msg.alert('提示','请选择发送邮件的申请人');
            return;
        }

        for(var x=datas.length-1;x>=0;x--){
            arr.push(datas[x].data.appInsID);
        }
        var arr_ZY=[];
        for(var x=datas.length-1;x>=0;x--){
            arr_ZY.push(datas[x].data.intentID);
        }
        var params = {
            ComID:"TZ_REVIEW_GSMCL_COM",
            PageID:"TZ_GSMCL_MAIL_STD",
            OperateType:"ReStudentLis",
            comParams:{type:"ReStudentLis",classID:classID,batchID:batchID,stuArr:arr,stuZYID:arr_ZY}
        };
 //       var tzParams=Ext.JSON.encode(params);
//        Ext.tzLoad(tzParams,function(responseData){
//            //alert(responseData['EmailTmpName']);
//            var emailTmpName = responseData['EmailTmpName'];
//            var arrEMLTmpls = new Array();
//            arrEMLTmpls=emailTmpName.split(",");
//            var audienceId = responseData['audienceId'];
//            Ext.tzSendEmail({
//                //发送的邮件模板;
//                "EmailTmpName":arrEMLTmpls,
//                //创建的需要发送的听众ID;
//                "audienceId": audienceId,
//                //是否可以发送附件: Y 表示可以发送附件,"N"表示无附件;
//                "file": "N"
//            });
//        });

        Ext.Ajax.request({
            url:Ext.tzGetGeneralURL,
            params:{tzParams:Ext.JSON.encode(params)},
            success:function(responseData){

                var audID = Ext.JSON.decode(responseData.responseText).comContent;

                Ext.tzSendEmail({
                    //发送的邮件模板;
                    "EmailTmpName": ["TZ_GSM_STU_EMA"],
                    //创建的需要发送的听众ID;
                    "audienceId": audID,
                    //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                    "file": "N"
                });
            }
        })



    }


});
