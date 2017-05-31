Ext.define('KitchenSink.view.enrollmentManagement.interviewReview.interviewReviewController', {
    extend: 'Ext.app.ViewController',
    requires:['Ext.ux.IFrame'],
    alias: 'controller.interviewReview',
    transValues:function(){
        var transvalueCollection = {},
            self = this;
        return {
            set:function(sets,callback){
                if(sets instanceof Array || typeof sets === 'string'){  
                    //可以传入一个数组或者单个字符串作为参数
                    if(sets instanceof Array){
                        var unload = [];
                        //遍历查找还未加载的数据
                        for(var x = sets.length-1;
                            x>=0&&!transvalueCollection[sets[x]]||(transvalueCollection[sets[x]]&&transvalueCollection[sets[x]].isLoaded());
                            x--){
                            unload.push(sets[x]);
                        }

                        var finishCount = self.isAllFinished(unload);
                        //加载未加载的数据
                        if(unload.length>0){
                            for(var x = unload.length-1;x>=0;x--){
                                transvalueCollection[unload[x]] = new KitchenSink.view.common.store.appTransStore(unload[x]);
                                transvalueCollection[unload[x]].load({
                                    callback:function(){
                                        finishCount(callback);
                                    }
                                });sets[x]
                            }
                        }else{
                            if(callback instanceof Function){
                                callback();
                            }
                        }
                    }else{
                        if(transvalueCollection[sets]&&transvalueCollection[sets].isLoaded()){
                            //当前store已经加载
                            if(callback instanceof Function){
                                callback();
                            }
                        }else{
                            var finishCount = self.isAllFinished(sets);
                            transvalueCollection[sets] = new KitchenSink.view.common.store.appTransStore(sets);
                            transvalueCollection[sets].load({
                                callback:function(){
                                    finishCount(callback);
                                }
                            });
                        }
                    }

                }else{
                    Ext.MessageBox.alert("传入参数有误");
                }
            },
            get:function(name){
                return transvalueCollection[name];
            }
        }
    },
    isAllFinished:function(sets){
        var len = sets instanceof Array ? sets.length : 1;
        return function(callback){
            len--;
            if(len===0){
                if(callback instanceof Function){
                    callback();
                }
            }
        }
    },
    //KitchenSink.view.interviewManagement.interviewReview.interviewProgress
    queryClassBatch:function(btn){
        Ext.tzShowCFGSearch({
        cfgSrhId: 'TZ_REVIEW_MS_COM.TZ_MSPS_LIST_STD.TZ_CLS_BATCH_V',
        condition :{TZ_JG_ID:Ext.tzOrgID},
        callback: function(seachCfg){
            var store = btn.findParentByType("grid").store;
            store.tzStoreParams = seachCfg;
            store.load();
        }
    });
    },
    //关闭
    /*==========================================================*/
    /*    关闭班级列表页面  LYY  2015-10-26                     */
    /*==========================================================*/
    onGridPanelClose:function(btn){
        btn.up('grid').close();
    },
    setReviewRule:function(grid,rowIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_RULE_STD"];
        // var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_SCHE_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_RULE_STD，请检查配置。');
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
        var transValue = this.transValues();
        transValue.set(["TZ_MSPS_STAGE","TZ_JUGACC_STATUS"],function(){
            cmp = new ViewClass(transValue);
            cmp.classID=classID;
            cmp.batchID=batchID;

            cmp.on('afterrender',function(panel){
                var form = panel.child('form').getForm();
                var countForm = panel.lookupReference("CountForm").getForm();

                var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD",' +
                    '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
                Ext.tzLoad(tzParams,function(respData){
                    var formData = respData.formData;
                    formData.batchName = record.data.batchName;
                    formData.className = record.data.className;
                    if(!formData.reviewStatus){
                        formData.reviewStatus='N';
                    }
                    form.setValues(formData);
                    countForm.findField("interviewReviewApplicantsNumber").setValue(formData.interviewReviewApplicantsNumber);
                    countForm.findField("reviewCountAll").setValue(parseInt(formData.interviewReviewApplicantsNumber)*(formData.reviewCount));

                });
            });

            tab = contentPanel.add(cmp);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
        });
    },
    onReviewRuleSave:function(btn){
        var me =this;
        var form = this.getView().child("form").getForm();
        var grid= this.getView().lookupReference("interviewReviewJudgeGrid");
        //var store=grid.store;
        var store = grid.getStore();
        for (var i=0;i<store.getCount();i++){
            //var group=Ext.JSON.encode(store.getAt(i).get('judgeGroup').data);
            var group=store.getAt(i).get('judgeGroup');
            if (group=="")
            {
                var string1="第"+i+"行";
                Ext.Msg.alert(string1,"评委组编号不可为空");
                return;
            }

        }
        if (form.isValid()) {
            var tzParams = this.getRuleParams();
            Ext.tzSubmit(tzParams,function(responseData){
                var grid = me.getView().lookupReference("interviewReviewJudgeGrid");
                var store = grid.getStore();
                if(tzParams.indexOf('"typeFlag":"JUDGE"')>-1||tzParams.indexOf("delete")>-1){
                    store.reload();
                }
            },"",true,this);
        }
    },
    onReviewRuleClose:function(btn){
        this.getView().close();
    },
    onReviewRuleEnsure: function(btn){
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            var tzParams = this.getRuleParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                comView.close();
            },"",true,this);
        }
    },
    getRuleParams:function(){
        var form = this.getView().child("form").getForm();
        var formParams = form.getValues();

        //更新操作参数
        var comParams = "";

        //修改json字符串
        var editJson = "";

        editJson = '{"typeFlag":"RULE","data":'+Ext.JSON.encode(formParams)+'}';

        var grid = this.getView().lookupReference("interviewReviewJudgeGrid");
        var store = grid.getStore();
        var editRecs = store.getModifiedRecords()||store.getNewRecords();
        for(var i=0;i<editRecs.length;i++){
            editJson = editJson + ','+'{"typeFlag":"JUDGE","data":'+Ext.JSON.encode(editRecs[i].data)+'}';
        }

        comParams = '"update":[' + editJson + "]";
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
            if(comParams == ""){
                comParams = '"delete":[' + removeJson + "]";
            }else{
                comParams = comParams + ',"delete":[' + removeJson + "]";
            }
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    selectJudgeOprID:function(trigger){
        Ext.tzShowPromptSearch({
            recname: 'TZ_JUSR_REL_V',
            searchDesc: '选择评委',
            maxRow:20,
            condition:{
                presetFields:
                {
                    TZ_JG_ID:{
                        value:Ext.tzOrgID,
                        type:'01'
                    },
                    TZ_JUGTYP_ID:{
                        value:'002',
                        type:'01'
                    }
                },
                srhConFields:{
                    OPRID:{
                        desc:'评委帐号',
                        operator:'07',
                        type:'01'
                    },
                    TZ_REALNAME:{
                        desc:'评委姓名',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_DLZH_ID: '评委帐号',
                TZ_REALNAME: '评委姓名',
                TZ_JUGTYP_NAME:'评委类型'
            },
            multiselect: false,
            callback: function(selection){
                var oprID = selection[0].data.TZ_DLZH_ID;
                var oprName = selection[0].data.TZ_REALNAME;
                var form = trigger.findParentByType('form');
                form.getForm().findField("judgeID").setValue(oprID);
                form.getForm().findField("judgeName").setValue(oprName);
            }
        })
    },
    addJudge:function(btn){
        var self=this,
            form = btn.findParentByType("form").findParentByType("form").getForm(),
            grid = btn.findParentByType("grid"),
            classID = form.findField("classID").getValue(),
            batchID = form.findField("batchID").getValue(),
            status = form.findField("interviewStage").getValue();
        var judgeGroupStore = new KitchenSink.view.common.store.comboxStore({
                recname:'TZ_PWZDY_T',
                condition:{
                    TZ_JUGTYP_STAT:{
                        value:'Y',
                        operator:'01',
                        type:'01'
                    }},
                result:'TZ_PWZBH,TZ_PWZMS'
            }),
            jugaccStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_JUGACC_STATUS");
        var win = Ext.create('Ext.window.Window',{
            title:'新增评委',
            width: 600,
            height: 265,
            modal:true,
            frame:true,
            items:[{
                xtype:'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },
                items:[
                    {
                        xtype: 'textfield',
                        fieldLabel: '评委账号',
                        name: 'judgeID',
                        allowBlank: false,
                        editable:false,
                        triggers: {
                            search: {
                                cls: 'x-form-search-trigger',
                                handler:self.selectJudgeOprID
                            }
                        },
                        ignoreChangesFlag: true
                    },{
                        xtype:'textfield',
                        fieldLabel: '评委姓名',
                        name: 'judgeName',
                        allowBlank: false,
                        editable:false,
                        ignoreChangesFlag: true
                    },{
                        xtype:'combo',
                        fieldLabel: "账户状态",
                        name: 'judgeStatus',
                        value:'A',/*默认有效*/
                        store:jugaccStatusStore,
                        displayField:'TSDesc',
                        valueField:'TValue',
                        queryMode:'local',
                        editable:false,
                        renderer:function(v){
                            var x;
                            if((x = jugaccStatusStore.find('TValue',v,0,false,false,false))>=0){
                                return jugaccStatusStore.getAt(x).data.TSDesc;
                            }else{
                                return v;
                            }
                        },
                        ignoreChangesFlag: true
                    },{
                        fieldLabel: "所属评委组",
                        name: 'judgeGroup',
                        minWidth:100,
                        allowBlank: false,
                        flex:1,
                        xtype:'combo',
                        store:judgeGroupStore,
                        displayField:'desc',
                        valueField:'group',
                        queryMode:'local',
                        editable:false,
                        listeners:{
                            focus:function(combo){
                                var form = btn.findParentByType("form").getForm();
                                var reviewCount = form.findField("reviewTeamCount").getValue();

                                var groupData = "";
                                for(var i=1;i<=reviewCount;i++){
                                    var groupID =String.fromCharCode(64+i);
                                    if(groupData==""){
                                        groupData=Ext.JSON.encode({group:groupID,desc:groupID})
                                    }else{
                                        groupData=groupData+","+Ext.JSON.encode({group:groupID,desc:groupID})
                                    }
                                }
                                groupData="["+groupData+"]";
                                combo.store.loadData(Ext.JSON.decode(groupData));
                            }
                        },
                        ignoreChangesFlag: true
                    }
                ]
                //items END
            }],
            buttons: [ {
                text: '确定',
                iconCls:"ensure",
                handler: function(btn){
                    var form = btn.findParentByType("panel").child("form").getForm(),
                        judgeID = form.findField("judgeID").getValue(),
                        judgeName = form.findField("judgeName").getValue(),
                        judgeStatus = form.findField("judgeStatus").getValue(),
                        judgeGroup = form.findField("judgeGroup").getValue();
                    if(form.findField("judgeID").isValid() && form.findField("judgeGroup").isValid()) {
                        if (grid.store.find('judgeID', judgeID, 0, false, false, true) == -1) {
                            var data = {
                                classID: classID,
                                batchID: batchID,
                                judgeID: judgeID,
                                judgeName: judgeName,
                                judgeStatus: judgeStatus,
                                judgeGroup: judgeGroup
                            };
                            grid.getStore().insert(grid.getStore().length, data);
                            btn.findParentByType("panel").close();
                        } else {
                            Ext.Msg.alert("提示", "ID为“" + judgeID + "”的评委已经存在");
                            return false;
                        }
                    }

                }
            }, {
                text: '关闭',
                iconCls:"close",
                handler: function(btn){
                    btn.findParentByType("panel").close();
                }
            }]
        });
        var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"IFP","comParams":{"type":"IFP","classID":"'+classID+'","batchID":"'+batchID+'"}}';
        this.isProgress(tzParams,function(status){
            switch(status) {
                case 'A':
                    Ext.MessageBox.alert('提示', '评审正在进行中，不能新增评委');
                    break;
                case 'B':
                case 'N':
                default:
                    win.show();
                    break;
            }
        });
    },
    editJudge : function(btn,rowIndex){
        var form = btn.findParentByType("form").findParentByType("form").getForm(),
            grid = btn.findParentByType("grid"),
            judgeID = grid.getStore().getAt(rowIndex).data.judgeID,
            judgeName = grid.getStore().getAt(rowIndex).data.judgeName,
            accStatus = grid.getStore().getAt(rowIndex).data.judgeStatus,
            judgeGroup = grid.getStore().getAt(rowIndex).data.judgeGroup,
            classID = form.findField("classID").getValue(),
            batchID = form.findField("batchID").getValue();
        var judgeGroupStore = new KitchenSink.view.common.store.comboxStore({
                recname:'TZ_PWZDY_T',
                condition:{
                    TZ_JUGTYP_STAT:{
                        value:'Y',
                        operator:'01',
                        type:'01'
                    }},
                result:'TZ_PWZBH,TZ_PWZMS'
            }),
            jugaccStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_JUGACC_STATUS");
        var win = Ext.create('Ext.window.Window',{
            title:'编辑评委',
            width: 600,
            height: 265,
            modal:true,
            frame:true,
            items:[{
                xtype:'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },
                items:[
                    {
                        xtype: 'textfield',
                        fieldLabel: '评委账号',
                        value:judgeID,
                        editable:false,
                        ignoreChangesFlag: true
                    },{
                        xtype:'textfield',
                        fieldLabel: '评委姓名',
                        value:judgeName,
                        editable:false,
                        ignoreChangesFlag: true
                    },{
                        xtype:'combo',
                        fieldLabel: "账户状态",
                        name: 'judgeStatus',
                        value:accStatus,/*默认有效*/
                        store:jugaccStatusStore,
                        displayField:'TSDesc',
                        valueField:'TValue',
                        queryMode:'local',
                        editable:false,
                        renderer:function(v){
                            var x;
                            if((x = jugaccStatusStore.find('TValue',v,0,false,false,false))>=0){
                                return jugaccStatusStore.getAt(x).data.TSDesc;
                            }else{
                                return v;
                            }
                        },
                        ignoreChangesFlag: true
                    },{
                        fieldLabel: "所属评委组",
                        name: 'judgeGroup',
                        minWidth:100,
                        value:judgeGroup,
                        flex:1,
                        xtype:'combo',
                        store:judgeGroupStore,
                        displayField:'desc',
                        valueField:'group',
                        queryMode:'local',
                        editable:false,
                        listeners:{
                            focus:function(combo){
                                var form = btn.findParentByType("form").getForm();
                                var reviewCount = form.findField("reviewTeamCount").getValue();

                                var groupData = "";
                                for(var i=1;i<=reviewCount;i++){
                                    var groupID =String.fromCharCode(64+i);
                                    if(groupData==""){
                                        groupData=Ext.JSON.encode({group:groupID,desc:groupID})
                                    }else{
                                        groupData=groupData+","+Ext.JSON.encode({group:groupID,desc:groupID})
                                    }
                                }
                                groupData="["+groupData+"]";
                                combo.store.loadData(Ext.JSON.decode(groupData));
                            }
                        },
                        ignoreChangesFlag: true
                    }
                ]
                //items END
            }],
            buttons: [{
                text: '确定',
                iconCls:"ensure",
                handler: function(btn){
                    var form = btn.findParentByType("panel").child("form").getForm(),
                        judgeStatus = form.findField("judgeStatus").getValue(),
                        judgeGroup = form.findField("judgeGroup").getValue();
                    grid.store.getAt(rowIndex).set("judgeStatus",judgeStatus);
                    grid.store.getAt(rowIndex).set("judgeGroup",judgeGroup);
                    btn.findParentByType("panel").close();
                }
            }, {
                text: '关闭',
                iconCls:"close",
                handler: function(btn){
                    btn.findParentByType("panel").close();
                }
            }]
        });
        var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"IFP","comParams":{"type":"IFP","classID":"'+classID+'","batchID":"'+batchID+'"}}';
        this.isProgress(tzParams,function(status){
            switch(status) {
                case 'A':
                    Ext.MessageBox.alert('提示', '评审正在进行中，不能编辑评委');
                    break;
                case 'B':
                case 'N':
                default:
                    win.show();
                    break;
            }
        });
    },
    removeJudge:function(view,rowIndex){
        var classID = view.findParentByType("form").findParentByType("form").getForm().findField("classID").getValue(),
            batchID = view.findParentByType("form").findParentByType("form").getForm().findField("batchID").getValue(),
            tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"IFP","comParams":{"type":"IFP","classID":"'+classID+'","batchID":"'+batchID+'"}}';
        this.isProgress(tzParams,function(status){
            switch(status) {
                case 'A':
                    Ext.MessageBox.alert('提示', '评审正在进行中，不能删除评委');
                    break;
                case 'B':
                case 'N':
                default:
                    Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function (btnId) {
                        if (btnId == 'yes') {
                            var store = view.store;
                            store.removeAt(rowIndex);
                        }
                    }, this);
                    break;
            }
        });
    },

    viewApplicants:function(grid,rowIndex){
        //是否有访问权限
        var self = this;
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_APPS_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        //var className = 'KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicants';
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_APPS_STD，请检查配置。');
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
        var transValue = self.transValues();
        cmp = new ViewClass(transValue);
        cmp.classID=classID;
        cmp.batchID=batchID;
        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();

            var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_APPS_STD",' +
                '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';

            var tzStoreParams ='{"classID":"'+classID+'","batchID":"'+batchID+'"}';

            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                formData.className = record.data.className;
                formData.batchName = record.data.batchName;
                form.setValues(formData);
                var store = panel.child('form').child("grid").store;
                store.tzStoreParams = tzStoreParams;
                transValue.set(["TZ_GENDER","TZ_LUQU_ZT"],function(){
                    store.load();
                })
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
            cfgSrhId: 'TZ_REVIEW_MS_COM.TZ_MSPS_APPS_STD.TZ_MSPSKS_VW',
            condition :{"TZ_CLASS_ID":classID,"TZ_APPLY_PC_ID":batchID},
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    queryInterviewer:function(btn){
        var form = btn.findParentByType("grid").findParentByType('panel').child('form').getForm();
        var classID=form.findField("classID").getValue();
        var batchID=form.findField("batchID").getValue();

        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_REVIEW_MS_COM.TZ_MSPS_ADDAPP_STD.TZ_MSPS_ADD_VW',
            // condition :{"TZ_CLASS_ID":classID,"TZ_APPLY_PC_ID":batchID},
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    removeApplicant:function(view, rowIndex){
        var classID = view.findParentByType("grid").findParentByType('form').getForm().findField("classID").getValue(),
            batchID = view.findParentByType("grid").findParentByType('form').getForm().findField("batchID").getValue(),
            tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"IFP","comParams":{"type":"IFP","classID":"'+classID+'","batchID":"'+batchID+'"}}',
            self=this;
        this.isProgress(tzParams,function(status){
            switch(status) {
                case 'A':
                    Ext.MessageBox.alert('提示', '评审正在进行中，不能删除考生');
                    break;
                case 'B':
                case 'N':
                default:
                    Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function (btnId) {
                        if (btnId == 'yes') {
                            var resSetStore = view.findParentByType("grid").store;
                            resSetStore.removeAt(rowIndex);
                            self.deleteApplicantsAtOnce(view.findParentByType("grid"));
                        }
                    }, this);
                    break;
            }
        });
    },
    removeApplicants : function(btn,rowIndex){
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection(),
            classID = btn.findParentByType("grid").findParentByType("form").getForm().findField('classID').getValue(),
            batchID = btn.findParentByType("grid").findParentByType("form").getForm().findField('batchID').getValue(),
            tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"IFP","comParams":{"type":"IFP","classID":"'+classID+'","batchID":"'+batchID+'"}}';
        //选中行长度
            var checkLen = selList.length,
                self = this;
        this.isProgress(tzParams,function(status){
            switch(status) {
                case 'A':
                    Ext.MessageBox.alert('提示', '评审正在进行中，不能删除考生');
                    break;
                case 'B':
                case 'N':
                default:
                    if (checkLen == 0) {
                        Ext.Msg.alert("提示", "请选择要删除的记录");
                        return;
                    } else {
                        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function (btnId) {
                            if (btnId == 'yes') {
                                var resSetStore = btn.findParentByType("grid").store;
                                resSetStore.remove(selList);
                                self.deleteApplicantsAtOnce(btn.findParentByType('grid'));
                            }
                        }, this);
                    }
                    break;
            }
        })

    },
    deleteApplicantsAtOnce : function(grid){
        var removedRecord = grid.getStore().getRemovedRecords(),
            result = [];
        for(var x=removedRecord.length-1;x>=0;x--){
            result.push({
                appINSID:removedRecord[x].data.appINSID,
                classID:removedRecord[x].data.classID,
                batchID:removedRecord[x].data.batchID
            });
        }
        var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"U","comParams":{"delete":'+
                Ext.JSON.encode(result)+"}}";
        Ext.tzLoad(tzParams,function(respData) {
           if(respData.msg === "reviewing"){
               Ext.MessageBox.alert("提示","已经处于评审状态的考生不能删除");
           }
            grid.getStore().reload();
        });
    },
    searchFromAll : function(btn){
        Ext.tzShowCFGSearch({
        cfgSrhId: 'TZ_REVIEW_MS_COM.TZ_MSPS_ADDSTU_STD.TZ_MSPS_ADDS_VW',
        condition :{TZ_JG_ID:Ext.tzOrgID},
        callback: function(seachCfg){
            var store = btn.findParentByType("grid").store;
            store.tzStoreParams = seachCfg;
            store.load();
        }
        });
    },
    addApplicants : function(btn,config){
        var classID = config.classID;
        var batchID = config.batchID;
        var self = this;
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_ADDSTU_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_SCHE_STD，请检查配置。');
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
            var store = win.child('grid').getStore(),
                tzStoreParams = '{"cfgSrhId": "TZ_REVIEW_MS_COM.TZ_MSPS_ADDSTU_STD.TZ_MSPS_ADDS_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+Ext.tzOrgID+'","TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value":"'+classID+'"}}';
            store.tzStoreParams = tzStoreParams;
            store.load({
                callback:function(){
                    store.filterBy(function(record){
                        if(record.data.isInterviewed=='是'){
                            return true;
                        }
                    });
                    store.on('filterchange',self.ifClearCondition(store));
                }
            });
        });
        cmp.show();

    },
    reviewScheduleMg:function(grid,rowIndex){
        var self = this;
//是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_SCHE_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
//该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_SCHE_STD，请检查配置。');
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
        //cmp = new ViewClass();
        var transValue = self.transValues();
        cmp = new ViewClass(classID,batchID,transValue);
        cmp.classID=classID;
        cmp.batchID=batchID;
        cmp.transValue = transValue;

        cmp.on('afterrender',function(panel){
            var judgeStore =panel.down('tabpanel').child("form[title=评委信息]").child('grid').store,
                judgeParams = '{"type":"judgeInfo","classID":"'+classID+'","batchID":"'+batchID+'"}',
            //standStore = panel.down('tabpanel').child("grid[title=评审标准]").store,
            //standParams = '{"type":"standard","classID":"'+classID+'","batchID":"'+batchID+'"}',
                form = panel.child('form'),
                tzParams ='{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SCHE_STD",' +
                    '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}',
                stuListStore = panel.down('tabpanel').child('grid[name=interviewStudentGrid]').getStore(),
                stuListParams = '{"type":"stuList","classID":"'+classID+'","batchID":"'+batchID+'"}';
            Ext.tzLoad(tzParams,function(respData){
                
                transValue.set(["TZ_JUGACC_STATUS","TZ_GENDER","TZ_LUQU_ZT","TZ_MSPS_ZT","TZ_MSPS_KSZT"],function(){
                    respData.className = record.data.className;
                    respData.batchName = record.data.batchName;
                    form.getForm().setValues(respData);
                    //根据加载的数据勾选复选框
                    if(respData.judgeTJB === 'Y'){
                        form.getForm().findField('judgeTJB').setValue(true);
                    }
                    if(respData.judgeFBT === 'Y'){
                        form.getForm().findField('judgeFBT').setValue(true);
                    }
                    //设置按钮,评议状态的状态
                    var finishbtn =form.down('button[name=finish]'),
                        startbtn = form.down('button[name=startup]'),
                        statusField = form.getForm().findField("interviewStatus");
                    startbtn.defaultColor = startbtn.el.dom.style['background-color'];
                    finishbtn.defaultColor = finishbtn.el.dom.style['background-color'];
                    switch(respData.status){
                        case 'A':
                            //进行中
                            startbtn.flagType='negative';
                            finishbtn.flagType='positive';
                            startbtn.setDisabled(true);
                            statusField.setValue("进行中");
                            break;
                        case 'B':
                            //已结束
                            startbtn.flagType='positive';
                            finishbtn.flagType='negative';
                            finishbtn.setDisabled(true);
                            statusField.setValue("已结束");
                            break;
                        case 'N':
                        default:
                            //初始状态和未开始相同
                            startbtn.flagType='positive';
                            finishbtn.flagType='negative';
                            finishbtn.setDisabled(true);
                            statusField.setValue("未开始");
                            break;
                    }
                    judgeStore.tzStoreParams = judgeParams;
                    judgeStore.load();
                    stuListStore.tzStoreParams = stuListParams;
                });
                
                //stuListStore.load();
                //standStore.tzStoreParams = standParams;
                //standStore.load();
                });

        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
        
    },
    startInterview : function(btn){
        if(btn.flagType === 'positive') {
            var classID = btn.findParentByType('form').getForm().findField('classID').getValue(),
                batchID = btn.findParentByType('form').getForm().findField('batchID').getValue(),
                tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SCHE_STD","OperateType":"CK","comParams":{"type":"startClick","classID":"' + classID + '","batchID":"' + batchID + '"}}';
            Ext.tzLoad(tzParams, function (responseData) {
                if (responseData.isPass === 'Y') {

                    //可点击状态,设置setType值为1,当前按钮已点击
                    var finishButton;
                    btn.setType = 1;
                    //更改当前按钮样式
                    btn.setDisabled(true);
                    //启动在可点击的情况下被点击后，设置关闭按钮为可点击状态
                    finishButton = btn.findParentByType("form").down("button[name=finish]");
                    finishButton.flagType = 'positive';
                    btn.flagType = 'negative';
                    //设置关闭按钮样式
                    finishButton.setDisabled(false);
                    btn.findParentByType("form").getForm().findField("interviewStatus").setValue("进行中");

                } else {
                    Ext.MessageBox.alert('启动失败','请设置正确的评审规则');
                }
            });
        }

    },
    endInterview : function(btn){
        if (btn.flagType === 'positive') {
            var classID = btn.findParentByType('form').getForm().findField('classID').getValue(),
                batchID = btn.findParentByType('form').getForm().findField('batchID').getValue(),
                tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SCHE_STD","OperateType":"CK","comParams":{"type":"finishClick","classID":"' + classID + '","batchID":"' + batchID + '"}}';
            Ext.tzLoad(tzParams, function (respData) {
                //可点击状态，设置setType值为1,当前按钮已点击
                btn.setType = 1;
                //更改当前按钮样式
                btn.setDisabled(true);
                //关闭后设置启动按钮卫可点击状态
                var startBtn = btn.findParentByType("form").down("button[name=startup]");
                startBtn.flagType = 'positive';
                btn.flagType = 'negative';
                //更新启动按钮的样式
                startBtn.setDisabled(false);
                btn.findParentByType("form").getForm().findField("interviewStatus").setValue("已结束");
            });
        }

    },
    onScheduleSave : function(btn){
        var form = btn.findParentByType('panel').child('form'),
            datas = form.getForm().getValues(),
            grid = btn.findParentByType('panel').down("grid[name=interviewJudgeGrid]"),
            modified = grid.getStore().getModifiedRecords(),
            judgeFormValue = btn.findParentByType('panel').down("form[name=judgeFormInfo]").getForm().findField('judgeCount').value,
            stuGrid = btn.findParentByType('panel').down("grid[name=interviewStudentGrid]"),
            stuModified = stuGrid.getStore().getModifiedRecords();
        
        var panel = btn.findParentByType('panel');
        
        //删除不需要想后台传输的数据
        delete datas.batchName;
        delete datas.className;
        delete datas.totalStudents;
        delete datas.interviewStudents;
        delete datas.materialStudents;
        delete datas.progress;
        delete datas.reviewCount;//每位考生被多少位评委审批
        //数字文本框中的数字
        if(judgeFormValue){
            datas.judgesCount = judgeFormValue.toString();
        }
        //获取复选框中的选中状态
        if(!datas.judgeFBT){
            datas.judgeFBT = 'off';
        }
        if(!datas.judgeTJB){
            datas.judgeTJB = 'off';
        }
        //获取启动和关闭按钮的点击状态
        datas.buttonStartClicked = form.down('button[name=startup]').setType.toString();
        datas.buttonEndClicked = form.down('button[name=finish]').setType.toString();
        //评委信息grid的修改项
        if(modified.length !== 0){
            datas.judgeInfoUpdate = [];
            for(var x =modified.length-1;x>=0;x--){
                var thisdata = modified[x].data;
                //删除不需要的数据
                delete thisdata.id;
                delete thisdata.classID;
                delete thisdata.batchID;
                datas.judgeInfoUpdate.push(thisdata);
            }
        }
        //获取考生表单中更新了的评委间偏差信息
        if(stuModified.length !==0){
            datas.studentInfo = [];
            for(var x = stuModified.length-1;x>=0;x--){
                var thisdata = {};
                thisdata.pweiPC = stuModified[x].data.judgePC;
                thisdata.appInsId = stuModified[x].data.insID;
                thisdata.judgeList = stuModified[x].data.judgeInfo;
                datas.studentInfo.push(thisdata);
            }
        }
        var btName = btn.name;
        var result = {update:[datas]},
            comParams = Ext.JSON.encode(result),
            tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SCHE_STD","OperateType":"U","comParams":'+comParams+'}';
        Ext.tzSubmit(tzParams,function(responseData) {
        	if(btName=="save"){
        		//更新FORM表单中的数据
                var classID = btn.findParentByType('panel').child('form').getForm().findField('classID').getValue(),
                    batchID = btn.findParentByType('panel').child('form').getForm().findField('batchID').getValue();
                var tzParams ='{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SCHE_STD",' +
                    '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
                Ext.tzLoad(tzParams,function(respData){
                    respData.className = btn.findParentByType('panel').child('form').getForm().findField('className').getValue();
                    respData.batchName = btn.findParentByType('panel').child('form').getForm().findField('batchName').getValue();
                    form.getForm().setValues(respData);
                    //根据加载的数据勾选复选框
                    if(respData.judgeTJB === 'Y'){
                        form.getForm().findField('judgeTJB').setValue(true);
                    }
                    if(respData.judgeFBT === 'Y'){
                        form.getForm().findField('judgeFBT').setValue(true);
                    }
                });
                //更新grid中的数据
                grid.store.reload();
                stuGrid.store.reload();
        	}else if(btName=="ensure"){        		
                panel.close();
        	}
            
        },"",true,this);
    },
    onScheduleEnsure : function(btn){
    	this.onScheduleSave(btn);        
    },
    onScheduleClose : function(btn){
        var panel = btn.findParentByType('panel');
        panel.close();
    },
    judgeInfoController : function(btn,arr){
        var selList =  btn.findParentByType("grid").getSelectionModel();
        if(selList.getSelection().length == 0){
            Ext.Msg.alert("提示","请选择要操作的记录");
            return;
        }else{
            for(var x = 0;x<selList.getSelection().length;x++) {
                var select = btn.findParentByType("grid").getSelection(),
                    index = btn.findParentByType("grid").getStore().indexOf(select[x]),
                    record = btn.findParentByType("grid").getStore().getAt(index);
                for(var y = arr.length-1;y>=0;y--){
                    record.set(arr[y].name,arr[y].value);
                }
            }
        }

    },
    pause : function(btn){
        this.judgeInfoController(btn,[{name:'accountStatus',value:'B'}]);
    },
    setUsual : function(btn){
        this.judgeInfoController(btn,[{name:'accountStatus',value:'A'}]);
    },
    /*submitData : function(btn){
        var selection = btn.findParentByType('grid').getSelectionModel().getSelection();
        // data[0].value = str.replace(/\/\d+$/,'/'+str.match(/^(\d+)\//)[1]);
        for(var x = selection.length-1;x>=0;x--) {
            var select = btn.findParentByType("grid").getSelection(),
                index = btn.findParentByType("grid").getStore().indexOf(select[x]),
                record = btn.findParentByType("grid").getStore().getAt(index);
            str = selection[x].data.hasSubmited;
            if (str.match(/^\d+\//)[0].replace(/\//, '') == str.match(/\/\d+$/)[0].replace(/\//, '') && !str.match(/^0\//)) {
                //抽取数量与已提交的数量相同同时抽取数量不为空=>当前评委抽取的所有考生都已评审
                //btn.findParentByType('grid').getSelectionModel().getSelection()[0].data.isChange = 'submit';
                record.set('submitYN','Y');
            }else{
                Ext.Msg.alert("提示","有评委没有评审完所有考生");
                record.set('submitYN','N');
            }
        }
    },*/
    submitData : function(btn){
    	var judgeGrid = btn.findParentByType("grid");
        var selection = judgeGrid.getSelectionModel().getSelection();
        if(selection.length == 0){
            Ext.Msg.alert("提示","请选择要操作的记录");
            return;
        }
        var view = this.getView();
        var datas = view.child('form[name=interviewProgressForm]').getValues();
        var classID = datas.classID;
        var batchID = datas.batchID;       
    
        var judgeOprIdList = "";
        for(var x = selection.length-1;x>=0;x--) {
            var select = judgeGrid.getSelection(),
                index = judgeGrid.getStore().indexOf(select[x]),
                record = judgeGrid.getStore().getAt(index);
            judgeOprIdList = judgeOprIdList + record.data.judgeOprID + ";";
        }
        tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SCHE_STD","OperateType":"BUTTON","comParams":{"type":"submitPweiData","classID":"' + classID + '","batchID":"' + batchID + '","pwOpridList":"' + judgeOprIdList + '"}}';
        Ext.tzLoad(tzParams, function (respData) {
        	if(respData=="success"){
        		for(var x = selection.length-1;x>=0;x--) {
                    var select = btn.findParentByType("grid").getSelection(),
                        index = btn.findParentByType("grid").getStore().indexOf(select[x]),
                        record = btn.findParentByType("grid").getStore().getAt(index);
                    record.set('submitYN','Y');
                }
        	}else{
        		Ext.Msg.alert("提示","在您选择提交数据的评委中，发现有未提交考生数据。");
        	}        	
        });

    },
    setNoSubmit : function(btn){
        this.judgeInfoController(btn,[{name:'submitYN',value:'N'}]);
    },
    revokeData : function(btn){
        /* var str = btn.findParentByType('grid').getSelectionModel().getSelection();
         for(var x=0;x<str.length;x++) {
         var select = btn.findParentByType("grid").getSelection(),
         index = btn.findParentByType("grid").getStore().indexOf(select[x]),
         record = btn.findParentByType("grid").getStore().getAt(index),
         data;
         data = str[x].data.hasSubmited;
         data = data.replace(/\/\d+$/,'\/0');
         record.set("hasSubmited",data);
         str[x].data.isChange = 'revoke';
         }*/
        Ext.Msg.confirm('警告','撤销面试数据会将该评委的所有数据都撤销，该操作不可恢复，是否继续？',function(e) {
            if (e === 'yes') {
                var str = btn.findParentByType('grid').getSelectionModel().getSelection(),
                    JSONData = [];
                for (var x = 0; x < str.length; x++) {
                    var select = btn.findParentByType("grid").getSelection(),
                        index = btn.findParentByType("grid").getStore().indexOf(select[x]),
                        record = btn.findParentByType("grid").getStore().getAt(index);
                    JSONData[x] = {};
                    JSONData[x].classID = record.data.classID;
                    JSONData[x].batchID = record.data.batchID;
                    JSONData[x].judgeID = record.data.judgeID;
                }
                var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SCHE_STD","OperateType":"RJD","comParams":{"type":"rjd","remove":'+Ext.JSON.encode(JSONData)+'}}';
                Ext.tzSubmit(tzParams,function(responseData) {
                    btn.findParentByType('grid').getStore().reload();
                },"成功删除",true,this);
            }
        } );
    },
    judgeStatusChange : function(combo){
        if(combo.value === 'Y'){
            var select = combo.findParentByType("grid").getSelection(),
                index = combo.findParentByType("grid").getStore().indexOf(select[0]),
                record = combo.findParentByType("grid").getStore().getAt(index),
                schedule = select[0].data.hasSubmited;
            if(schedule.match(/^\d+\//)[0].replace(/\//, '') !== schedule.match(/\/\d+$/)[0].replace(/\//, '') || schedule.match(/^0\//)){
                //抽取数量与已提交的数量相同同时抽取数量不为空=>当前评委抽取的所有考生都已评审
                Ext.Msg.alert("提示","当前评委没有评审完所有考生");
                record.set('submitYN',combo.originalValue);
            }

        }
    },
    calculate : function(btn){
        var select = btn.findParentByType("grid").getSelectionModel().getSelection(),
            classID = btn.findParentByType("grid").findParentByType("form").getValues().classID,
            batchID = btn.findParentByType("grid").findParentByType("form").getValues().batchID,
            datas={};
        if(select.length == 0) {
            Ext.Msg.alert("提示", "请选择要操作的记录");
            return;
        }else{
            datas.type = 'calculate';
            datas.classID = classID;
            datas.batchID = batchID;
            datas.appID =[];
            for(var x = 0;x<select.length;x++){
                datas.appID.push(select[x].data.insID);
            }
            var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SCHE_STD","OperateType":"CA","comParams":' + Ext.JSON.encode(datas) + '}';
            Ext.tzLoad(tzParams, function (responseData) {
                //btn.findParentByType("grid").getStore().reload();
            	for(var x=select.length-1;x>=0;x--) {
                    var index = btn.findParentByType("grid").getStore().indexOf(select[x]),
                        record = btn.findParentByType("grid").getStore().getAt(index);
                    for(var y=responseData.root.length-1;y>=0;y--){
                        if(responseData.root[y].appInsID === record.data.insID){
                            record.set('judgePC',responseData.root[y].standardDeviation);
                        }
                    }
                }
            });
        }
    },
    stuListActive : function(grid){
        var stuListStore = grid.getStore(),
            classID = grid.findParentByType("form").getValues().classID,
            batchID = grid.findParentByType("form").getValues().batchID,
            self = this;
        if(!stuListStore.isLoaded()) {
            stuListStore.load({
                scope: this,
                callback: function (records, operation, success) {
                    /*for (var x = records.length - 1; x >= 0; x--) {*/
                	for(var x = 0 ; x < records.length; x ++) {
                        var viewRecord = grid.getView().getRow(x).querySelector(".tz_lzh_interviewReview_app");
                        (function (thisrecord,col) {
                            if (col && !col.onclick) {
                                //为没有注册事件的链接注册事件
                                if(col.addEventListener) {
                                    col.addEventListener('click', function (e) {
                                        //阻止事件向上冒泡
                                        e.stopPropagation();
                                        //打开评委页面
                                        self.viewJudgeReviewInfo(thisrecord);
                                    }, false);
                                }else{
                                    //兼容IE
                                    col.attachEvent("onclick",function(){
                                        e= window.event;
                                        //阻止事件向上冒泡
                                        e.stopPropagation();
                                        //打开评委页面
                                        self.viewJudgeReviewInfo(thisrecord);
                                    });
                                }
                            }
                        })(records[x],viewRecord);
                    }
                }
            });
        }else{
            grid.view.on('refresh',function(){
                records = stuListStore.getRange();
                for (var x = records.length - 1; x >= 0; x--) {
                    var viewRecord = grid.getView().getRow(x).querySelector(".tz_lzh_interviewReview_app");
                    (function (thisrecord,col) {
                        if (col && !col.onclick) {
                            //为没有注册事件的链接注册事件
                            if(col.addEventListener) {
                                col.addEventListener('click', function (e) {
                                    //阻止事件向上冒泡
                                    e.stopPropagation();
                                    //打开评委页面
                                    self.viewJudgeReviewInfo(thisrecord);
                                }, false);
                            }else{
                                //兼容IE
                                col.attachEvent("onclick",function(){
                                    e= window.event;
                                    //阻止事件向上冒泡
                                    e.stopPropagation();
                                    //打开评委页面
                                    self.viewJudgeReviewInfo(thisrecord);
                                });
                            }
                        }
                    })(records[x],viewRecord);
                }
                grid.view.removeListener('refresh',arguments.callee);
            });
            
        }
    },
    addApplicantEnsure : function(btn,event){
        //由于新增弹出窗口会使当前页面不能操作，弹出窗口对应的tab即是当前活动的Tab
        var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),
            targetStore = activeTab.down("grid[name=interviewReviewStudentGrid]").getStore(),
            select = btn.findParentByType("panel").child('grid').getSelectionModel().getSelection(),
            hasInterviewed = false,
            newRecord = [];
        //循环中将非重复数据保存到中间变量，避免之后的循环会额外查询到之前的循环插入的数据
        for(var x =0;x<select.length;x++){
            if(targetStore.find('appINSID',select[x].data.appINSID)<0) {
                delete select[x].data.isInterviewed;
                delete select[x].data.id;
                newRecord.push(select[x].data);
            }else{
                hasInterviewed = true;
            }
        }
        //循环完毕后再向store中添加数据
        targetStore.add(newRecord);
        if(hasInterviewed){
            Ext.Msg.alert("提示","有考生已经存在于面试阶段");
        }
        btn.findParentByType("panel").close();
    },
    addApplicantClose : function(btn){
        btn.findParentByType("panel").close();
    },
    selectOprForStudent : function(trigger){
        var classID = trigger.findParentByType('grid').findParentByType('form').getForm().findField('classID').value;
        Ext.tzShowPromptSearch({
            recname: 'TZ_MSPS_PW_VW',
            searchDesc: '选择评委',
            maxRow:20,
            condition:{
                presetFields:
                {
                    TZ_JG_ID:{
                        value:Ext.tzOrgID,
                        type:'01'
                    },
                    TZ_CLASS_ID:{
                        value:classID,
                        type:'01'
                    }
                },
                srhConFields:{
                    TZ_DLZH_ID:{
                        desc:'评委帐号',
                        operator:'07',
                        type:'01'
                    },
                    TZ_REALNAME:{
                        desc:'评委姓名',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_DLZH_ID: '评委帐号',
                TZ_REALNAME: '评委姓名',
                TZ_JUGTYP_NAME:'评委类型'
            },
            multiselect: false,
            callback: function(selection) {
                var oprID = selection[0].data.TZ_DLZH_ID,
                    select = trigger.findParentByType('grid').getSelectionModel().getSelection(),
                    index = trigger.findParentByType("grid").getStore().indexOf(select[0]),
                    record = trigger.findParentByType("grid").getStore().getAt(index);
                if(trigger.value.indexOf(oprID) == -1){
                    if(trigger.value != '') {
                        record.set('judgeList', trigger.value + "," + oprID);
                    }else{
                        record.set('judgeList',oprID);
                    }
                }else{
                    Ext.Msg.alert('提示','当前评委已存在该考生的评委列表中');
                }
            }
        })
    },
    //查看评委评分信息
    viewJudgeReviewInfo:function(record){
    	Ext.tzSetCompResourses("TZ_REVIEW_MS_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_APPJUG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_APPJUG_STD，请检查配置。');
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
        
        var JSONData = {};
        JSONData.classID=Ext.JSON.decode(record.store.tzStoreParams).classID;
        JSONData.batchID=Ext.JSON.decode(record.store.tzStoreParams).batchID;
        JSONData.appINSID = record.data.insID;
        JSONData.oprID = record.data.oprID;        
        var tzStoreParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_APPJUG_STD","OperateType":"QF","comParams":'+Ext.JSON.encode(JSONData)+'}}';
        Ext.tzLoad(tzStoreParams,function(respData){
            var score = typeof respData.root[0].score === 'object'?respData.root[0].score:Ext.JSON.decode(respData.root[0].score)
            cmp = new ViewClass(score);
            var fields = [{name: 'classID'},
                {name: 'batchID'},
                {name: 'judgeRealName'},
                {name: 'appInsID'},
                {name: 'studentRealName'}];
            for(var x=0;x<score.length;x++){
                fields.push({
                    name:x
                });
            }
            var newStore = Ext.create('Ext.data.Store',{
                    alias: 'store.interviewReviewAppJudgeStore',
                    autoLoad:false,
                    comID: 'TZ_REVIEW_MS_COM',
                    pageID: 'TZ_MSPS_APPJUG_STD',
                    pageSize:0,
                    tzStoreParams:Ext.JSON.encode(JSONData),
                    proxy: Ext.tzListProxy(),
                    model:Ext.create('Ext.data.Model',{
                        fields:fields
                    })
                });
            //console.log(newStore.getData());
            cmp.child('grid').setStore(newStore);
            cmp.child('grid').store.load();
            cmp.show();
        });

    },
    clearCondition : function(btn){
        btn.findParentByType("grid").filters.clearFilters(true);
    },
    ifClearCondition:function(store){
        var times = 1;
        return function(){
            times--;
            if(store.isFiltered() && times>=0){
                store.clearFilter();
            }
        }
    },
    editApplicant : function(btn,rowIndex){
        var self=this,
            form = btn.findParentByType("grid").findParentByType("form").getForm(),
            grid = btn.findParentByType("grid"),
            classID = form.findField("classID").getValue(),
            batchID = form.findField("batchID").getValue(),
            datas = grid.getStore().getAt(rowIndex).data,
            jugaccStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_LUQU_ZT");
        if(!jugaccStatusStore.isLoaded()){
            jugaccStatusStore.load();
        }
        var win = Ext.create('Ext.window.Window', {
            title: '编辑考生',
            width: 600,
            height: 265,
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
                        name: 'AppName',
                        editable: false,
                        value:datas.realName,
                        ignoreChangesFlag: true
                    }, {
                        xtype: 'textfield',
                        fieldLabel: '报名表编号',
                        name: 'AppInsID',
                        editable: false,
                        value:datas.appINSID,
                        ignoreChangesFlag: true
                    }, {
                        xtype: 'combo',
                        fieldLabel: "录取状态",
                        name: 'LUQUZT',
                        store: jugaccStatusStore,
                        value:datas.LUQUZT,
                        displayField: 'TSDesc',
                        valueField: 'TValue',
                        queryMode: 'local',
                        editable: false,
                        ignoreChangesFlag: true
                    },{
                        xtype:'textfield',
                        fieldLabel:'备注',
                        value:datas.remark,
                        name:'remark',
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
                        recordStatus = form.findField("LUQUZT").getValue(),
                        remark = form.findField("remark").getValue(),
                        record = grid.getStore().getAt(rowIndex);
                    record.set("LUQUZT",recordStatus||'');
                    record.set("remark",remark||'');
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
    viewApplicationForm : function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_APPS_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        //var className = 'KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicants';
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_APPS_STD，请检查配置。');
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

            var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_APPS_STD",' +
                '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';

            var tzStoreParams ='{"classID":"'+classID+'","batchID":"'+batchID+'"}';

            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                formData.className = className;
                formData.batchName = batchName;
                form.setValues(formData);
                var store = panel.child('form').child("grid").store;
                store.tzStoreParams = tzStoreParams;
                store.load();
            });
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
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_MAIL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有发送邮件的权限');
            return;
        }

        var store = btn.findParentByType('grid').store;
        var modifiedRecs = store.getModifiedRecords();
        if(modifiedRecs.length>0){
            Ext.MessageBox.alert("提示","请先保存列表数据之后再发送邮件！");
            return;
        };
        var datas = btn.findParentByType('grid').getSelectionModel().getSelection(),
            arr = [];
        if(datas.length !== 0) {
            for (var x = datas.length - 1; x >= 0; x--) {
                arr.push(datas[x].data.oprID);
            }
            var params = {
                ComID: "TZ_REVIEW_MS_COM",
                PageID: "TZ_MSPS_MAIL_STD",
                OperateType: "GETL",
                comParams: {type: "getL", oprArr: arr}
            };
            Ext.Ajax.request({
                url: Ext.tzGetGeneralURL,
                params: {tzParams: Ext.JSON.encode(params)},
                success: function (responseData) {
                    var audID = Ext.JSON.decode(responseData.responseText).comContent;

                    Ext.tzSendEmail({
                        //发送的邮件模板;
                        "EmailTmpName": ["TZ_MAIL_MSPSJUG", "TZ_MAILYC_MSPSJ"],
                        //创建的需要发送的听众ID;
                        "audienceId": audID,
                        //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                        "file": "N"
                    });
                }
            })
        }else{
            Ext.MessageBox.alert("提示","请先选择评委");
        }
    },
    addApplicantsFromWJ : function(btn){
        var classID = btn.findParentByType("grid").findParentByType("form").getForm().findField("classID").getValue(),
            batchID = btn.findParentByType("grid").findParentByType("form").getForm().findField("batchID").getValue(),
            config = {classID:classID,batchID:batchID,type:'WJ'},
            tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"IFP","comParams":{"type":"IFP","classID":"'+classID+'","batchID":"'+batchID+'"}}',
            self = this;
        this.isProgress(tzParams,function(status){
            switch(status) {
                case 'A':
                    Ext.MessageBox.alert('提示', '评审正在进行中，不能添加考生');
                    break;
                case 'B':
                case 'N':
                default:
                    self.addApplicants(btn, config);
                    break;
            }
        });
    },
    addApplicantsFromBM : function(btn){
        var classID = btn.findParentByType("grid").findParentByType("form").getForm().findField("classID").getValue(),
            batchID = btn.findParentByType("grid").findParentByType("form").getForm().findField("batchID").getValue(),
            config = {classID:classID,batchID:batchID,type:'BM'},
            tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"IFP","comParams":{"type":"IFP","classID":"'+classID+'","batchID":"'+batchID+'"}}',
            self = this;
        this.isProgress(tzParams,function(status){
            switch(status) {
                case 'A':
                    Ext.MessageBox.alert('提示', '评审正在进行中，不能添加考生');
                    break;
                case 'B':
                case 'N':
                default:
                    self.addApplicants(btn, config);
                    break;
            }
        });
    },
    printPFZB: function(btn){
        //Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");

        var judgeGrid=btn.findParentByType('grid'),
            selList = judgeGrid.getSelectionModel().getSelection();
        var form=judgeGrid.findParentByType('form').findParentByType('form').getForm();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要打印总分表的记录");
            return;
        }
        else {
            //拼装评委信息
            var JudgeJson="";
            var classID=selList[0].data.classID,
                batchID=selList[0].data.batchID;
            if (selList.length==1){
                var JudgeJson= '"' + selList[0].data.judgeOprID + '"';
            }
            else{
                for (var i=0;i<selList.length-1;i++)
                {
                    if(JudgeJson=="")
                    {
                        JudgeJson= '"' + selList[i].data.judgeOprID;}
                    else{JudgeJson=JudgeJson+'='+selList[i].data.judgeOprID}
                }
                JudgeJson=JudgeJson+'='+selList[selList.length-1].data.judgeOprID+'"';
            }
            
            
            var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_MSPYSJ_STD","OperateType":"DCPY","comParams":{"TZ_CLASS_ID":"' + classID + '","TZ_APPLY_PC_ID":"' + batchID + '","TZ_PWEI_OPRIDS":' + JudgeJson + '}}';
            //面试导出 var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_MSPYSJ_STD","OperateType":"DCPY","comParams":{"TZ_CLASS_ID":"' + classID + '","TZ_APPLY_PC_ID":"' + batchID + '","TZ_PWEI_OPRIDS":' + JudgeJson + '}}';
            
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
            	if( responseData.url == "" || responseData.url == undefined){
                     Ext.MessageBox.alert('提示', '下载失败，请与管理员联系。');
                     return;
                }else{
                	  window.open(responseData.url, '_blank');
                }
            });
        }
        
    },
    setJudgeGroup:function(btn){
        var grid = btn.findParentByType("grid"),
            selections = grid.getSelectionModel().getSelection();
        if(selections.length > 0){
            var judgeGroupStore = new KitchenSink.view.common.store.comboxStore({
                    recname:'TZ_PWZDY_T',
                    condition:{
                        TZ_JUGTYP_STAT:{
                            value:'Y',
                            operator:'01',
                            type:'01'
                        }},
                    result:'TZ_PWZBH,TZ_PWZMS'
                }),
                gridBtn = btn,
                classID = btn.findParentByType('grid').findParentByType('form').getForm().findField('classID').getValue(),
                batchID = btn.findParentByType('grid').findParentByType('form').getForm().findField('batchID').getValue(),
                comParas = {
                    classID:classID,
                    batchID:batchID,
                    type:"getG"
                }; 
            var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"GC","comParams":' + Ext.JSON.encode(comParas) + '}';
            Ext.tzLoad(tzParams,function(respData){
                win.child('form').getForm().findField('judgeGroup').addListener('focus',function(combo){
                        var form = btn.findParentByType("form").getForm();
                        var reviewCount = respData.groupCount;

                        var groupData = "";
                        for(var i=1;i<=reviewCount;i++){
                            var groupID =String.fromCharCode(64+i);
                            if(groupData==""){
                                groupData=Ext.JSON.encode({group:groupID,desc:groupID})
                            }else{
                                groupData=groupData+","+Ext.JSON.encode({group:groupID,desc:groupID})
                            }
                        }
                        groupData="["+groupData+"]";
                        combo.store.loadData(Ext.JSON.decode(groupData));
                });
                win.show();
            });

            var win = Ext.create('Ext.window.Window', {
                title: '分配评委组',
                width: 400,
                height: 150,
                modal: true,
                frame: true,
                items:[{
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
                    items:[
                        {
                            fieldLabel: "所属评委组",
                            name: 'judgeGroup',
                            minWidth:100,
                            flex:1,
                            xtype:'combo',
                            store:judgeGroupStore,
                            displayField:'desc',
                            valueField:'group',
                            queryMode:'local',
                            editable:false,
                            ignoreChangesFlag: true,
                            triggers:{
                                clear: {
                                    cls: 'x-form-clear-trigger',
                                    handler: function(field){
                                        field.setValue("");
                                    }
                                }
                            }
                        }
                    ]
                }],
                buttons:[
                    {
                        text: '确定',
                        iconCls: "ensure",
                        //handler: 'onApplicantsClose'
                        handler: function (btn) {
                            var records = gridBtn.findParentByType('grid').getSelectionModel().getSelection(),
                                value = btn.findParentByType('panel').child('form').getForm().findField('judgeGroup').getValue();
                            for(var x = records.length-1;x>=0;x--){
                                records[x].set('judgeGroup',value);
                            }
                            btn.findParentByType('panel').close();
                        }
                    },
                    {
                        text: '关闭',
                        iconCls: "close",
                        //handler: 'onApplicantsClose'
                        handler: function (btn) {
                            btn.findParentByType('panel').close();
                        }
                    }
                ]
            });
        }else{
            Ext.MessageBox.alert("提示","请先选择考生");
        }
    },
    isProgress : function(params,callback){
        Ext.tzLoad(params,function(respData){
            callback(respData.progress);
        });
    },
    /*viewThisApplicationForm: function(view, rowIndex,colIndex){
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        var store = view.grid.getStore();
        var record = store.getAt(rowIndex);
        var appInsID=record.get("insID");
        var classID=view.grid.findParentByType('tabpanel').findParentByType('form').getForm().findField('classID').getValue();
        
        var oprID=record.get("oprID");

        if(appInsID!=""){
            var tzParams="{ComID:'TZ_ONLINE_REG_COM',PageID:'TZ_ONLINE_APP_STD',OperateType:'HTML',comParams:{TZ_APP_INS_ID:'"+appInsID+"'}}";
            var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+tzParams;
            var mask ;
            var win = new Ext.Window({
                name:'applicationFormWindow',
                title :"查看报名表",
                maximized : true,
                classID :classID,
                oprID :oprID,
                appInsID : appInsID,
                gridRecord:record,
                width : Ext.getBody().width,
                height : Ext.getBody().height,
                autoScroll : true,
                border:false,
                bodyBorder : false,
                isTopContainer : true,
                modal : true,
                resizable : false,
                items:[
                    new Ext.ux.IFrame({
                        xtype: 'iframepanel',
                        layout: 'fit',
                        style : "border:0px none;scrollbar:true",
                        border: false,
                        src : viewUrl,
                        height : "100%",
                        width : "100%"
                    })
                ],
                buttons: [{
                        text:"关闭",
                        iconCls:"close",
                        handler: function(){
                            win.close();
                        }
                    }]
            })
            win.show();
        }else{
            Ext.MessageBox.alert("提示","找不到该报名人的报名表");
        }
    },*/
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
		
		var classID = record.get("classID");
		var oprID = record.get("oprID");
		var appInsID = record.get("insID");	
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
						controller : 'interviewReview',
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
			Ext.MessageBox
					.alert(
							Ext
									.tzGetResourse(
											"TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt",
											"提示"),
							Ext
									.tzGetResourse(
											"TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.cantFindAppForm",
											"找不到该报名人的报名表"));
		}
	},
    findCmpParent : function(ele){
        //根据当前DOM节点，向上查找最近的包含EXT节点对象的节点并返回该EXT节点对象
        if(ele){
            while(ele.parentNode && !Ext.getCmp(ele.parentNode.id)){
                ele = ele.parentNode;
            }
            return Ext.getCmp(ele.parentNode.id);
        }else{
            return false;
        }
    },
    calcuScoreDist:function(btn){
    	var selList =  btn.findParentByType("grid").getSelectionModel();
        if(selList.getSelection().length == 0){
            Ext.Msg.alert("提示","请选择要操作的记录");
            return;
        }else{
        	var win = btn.findParentByType("interviewReviewSchedule");
        	
        	var pwIds = "";
        	var selectedIndex = [];
        	var selectedRecords = [];
        	var selectTmp = btn.findParentByType("grid[name=statisticalGrid]").getSelection();
            for(var x = 0;x<selList.getSelection().length;x++) {
                var select = btn.findParentByType("grid[name=statisticalGrid]").getSelection(),
                    index = btn.findParentByType("grid[name=statisticalGrid]").getStore().indexOf(select[x]),
                    record = btn.findParentByType("grid[name=statisticalGrid]").getStore().getAt(index);
                selectedIndex.push(index);
                selectedRecords.push(record);
                var data = record.data;
                pwIds = pwIds + data.col01 + ",";
            }
            //已有选中评委，提交到后台处理
            var view = this.getView();
            var datas = view.child('form[name=interviewProgressForm]').getValues();
            var classID = datas.classID;
            var batchID = datas.batchID;
            var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SCHE_STD","OperateType":"QG","comParams":{"type":"getPwDfData" , "classID":"' + classID + '","batchID":"' + batchID + '","pwIds":"' + pwIds + '"}}';
            Ext.tzLoadAsync(tzParams, function(respData) {
            	//统计信息
                var tmpArray = respData.pw_dfqk_grid;
               
                statisticsGridDataModel = {
                    gridFields: [],
                    gridColumns: [],
                    gridData: []
                };          
                
                var tmpObject = {
                    columns: []
                };
                for (var i = 0; i < tmpArray.length; i++) {
                    var colName = '00' + (i + 1);
                    colName = 'col' + colName.substr(colName.length - 2);

                    var tmpColumn = {
                        text: tmpArray[i][colName],
                        sortable: false,
                        dataIndex: colName,
                        flex: 1
                    };

                    statisticsGridDataModel['gridColumns'].push(tmpColumn);
                    statisticsGridDataModel['gridFields'].push({
                        name: colName
                    });
                }

                tmpArray = respData.pw_dfqk_grid_data;

                for (var i = 0; i < tmpArray.length; i++) {
                    var tmpdataArray = tmpArray[i].field_value;
                    var dataRow = [];
                    for (var j = 0; j < tmpdataArray.length; j++) {
                        var colName = '00' + (j + 1);
                        colName = 'col' + colName.substr(colName.length - 2);
                        dataRow.push(tmpdataArray[j][colName]);
                    }                    
                    statisticsGridDataModel['gridData'].push(dataRow);
                }

                var gridData = statisticsGridDataModel['gridData'];
                var gridStore = btn.findParentByType("grid").getStore();
                gridStore.setData(gridData);

                var getSle = btn.findParentByType("grid").getSelectionModel();
                for(var m=0;m<selectedIndex.length;m++){
                	var selIndex = selectedIndex[m];
                	getSle.select(selIndex,true);
                }
            });
        }
    },
    showMSTB : function(btn){
    	
    	//先计算标准评分分布（调用于得实的方法）;
    	var bzfbtmp = this.calcuScoreDist(btn);
    	// 柱状图参数
    	var coluObj = [];
    	// 曲线图参数
    	var lineObj = [];
    	/*
		// 曲线图参数
		lineObj = [ 
		                {'pw' : 'zhangsan',	'n1' :10,'n2' : 20,'n3' : 30,'n4' : 40,'n5' : 40,'n6' : 40}, 
		                {'pw' : 'lisi',     'n1' :15,'n2' : 25,'n3' : 35,'n4' : 45,'n5' : 40,'n6' : 40},
		                {'pw' : 'wangwu',   'n1' :40,'n2' : 30,'n3' : 20,'n4' : 10,'n5' : 40,'n6' : 40},
		                {'pw' : '平均',     'n1' :22,'n2' : 25,'n3' : 30,'n4' : 33,'n5' : 40,'n6' : 40}
		              ];
		// 柱状图参数
		
		coluObj = [
		               {'pw' : 'zhangsan', 'pj' : 10},
		               {'pw' : 'lisi',     'pj' : 15},
		               {'pw' : 'wangwu',   'pj' : 40}, 
		               {'pw' : '平均',     'pj' : 22} 
		               ];
		*/
		//console.log('-------------');
		//console.log(this.getView().statisticsGrid);
		//console.log(btn.ownerCt.ownerCt);
        var PWIDs ="";//存放选择的评委;
		var grid = btn.ownerCt.ownerCt;//获取grid;
		var selList = grid.getSelectionModel().getSelection();//获取选中的数据;
        var checkLen = selList.length;
        //取得选择的评委串;
        if(checkLen == 0){
            Ext.MessageBox.alert("提示","请选择需要的评委！");
            return;
        }else{
            for (var i=0;i<selList.length;i++){
            	if(PWIDs==""){
            		PWIDs = selList[i].get("col01");	
            	}else{
            		PWIDs = PWIDs+"="+selList[i].get("col01");
            	}
            }
        }
        //console.log(PWIDs);
        //循环grid（所有，包括没有选择的，目的是取得汇总的数据）;
		for(var i=0;i<grid.getStore().data.length;i++){
			var pwdata = grid.getStore().getAt(i).data;//pwdata就是对应record的一个一个的对象;
			var pw = pwdata.col01;// 获取每行的评委;
			if (pw !== null && pw !== undefined && pw !== '') {
				//如果非空，判断是否为选择项;
				if(PWIDs.indexOf(pw) >= 0){
					//对选择的评委进行处理;
					//处理柱状图
					var coltmpobj = {};
					coltmpobj["pw"] = pw;
					var tmppj = pwdata.col05;
                	//处理值为非数字的情况
                	if (tmppj !== null && tmppj !== undefined && tmppj !== '' && isNaN(tmppj)==false) {
    					coltmpobj["pj"] = parseFloat(tmppj);
                	}else{
                		coltmpobj["pj"] = 0;
                	}
					coluObj.push(coltmpobj);
					
					//处理曲线图部分;
					var linetmpobj = {};
					linetmpobj["pw"] = pw;
					for(var j=5;j<this.getView().statisticsGrid.gridColumns.length;j++){
						var colname = this.getView().statisticsGrid.gridColumns[j].text;
						var colid =   this.getView().statisticsGrid.gridColumns[j].dataIndex;
						var colvalue =pwdata[colid];
						//处理非数字的情况(取百分比数据)
						var tmppwpyqjdata = colvalue.split('（')[1].replace('）','').replace('%','');
           			 	if(isNaN(tmppwpyqjdata)){
           			 		linetmpobj[colname] = 0;
           			 	}else{
           			 		linetmpobj[colname] = parseFloat(tmppwpyqjdata);
           			 	}

					}
					lineObj.push(linetmpobj);
					
				}else{
					//未选择的不进行处理;
					//console.log("未选上");
				}
			}else{
				//取得汇总行的数据;
				//console.log("汇总");
				//处理柱状图
				var coltmpobj = {};
				coltmpobj["pw"] = "标准";
				
				var tmppj = pwdata.col05;
            	//处理值为非数字的情况
            	if (tmppj !== null && tmppj !== undefined && tmppj !== '' && isNaN(tmppj)==false) {
					coltmpobj["pj"] = parseFloat(tmppj);
            	}else{
            		coltmpobj["pj"] = 0;
            	}
				coluObj.push(coltmpobj);
				
				//处理曲线图部分;
				var linetmpobj = {};
				linetmpobj["pw"] = "标准";
				for(var j=5;j<this.getView().statisticsGrid.gridColumns.length;j++){
					var colname = this.getView().statisticsGrid.gridColumns[j].text;
					var colid =   this.getView().statisticsGrid.gridColumns[j].dataIndex;
					var colvalue =pwdata[colid];
					//处理非数字的情况(取百分比数据)
					var tmppwpyqjdata = colvalue.split('（')[1].replace('）','').replace('%','');
       			 	if(isNaN(tmppwpyqjdata)){
       			 		linetmpobj[colname] = 0;
       			 	}else{
       			 		linetmpobj[colname] = parseFloat(tmppwpyqjdata);
       			 	}
				}
				lineObj.push(linetmpobj);
				
			}
			
		}
    
        //是否有访问权限
    	Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_MSTB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_BMGL_MSTB_STD，请检查配置。');
            return;
        }
        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className.toString());
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

        cmp = new ViewClass({"coluObj":coluObj,"lineObj":lineObj});
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    }

});
