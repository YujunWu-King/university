Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewController', {
    requires: [
        "KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateController"
    ],

    extend: 'Ext.app.ViewController',
    alias: 'controller.GSMinterviewReview',
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
    queryClassBatch: function (btn) {
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_GSM_REVIEW_MS_COM.TZ_GSMS_LIST_STD.TZ_GSMS_BATCH_V',
            condition: {TZ_JG_ID: Ext.tzOrgID},
            callback: function (seachCfg) {
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    addClassBatch:function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GSM_REVIEW_MS_COM"]["TZ_MSPS_ADDBAT_STD"];
        // var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_PLAN_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_ADDBAT_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        var store = btn.findParentByType('tabpanel').down('grid').getStore(),
            batchNames = {};
        for(var x = store.getRange().length-1;x>=0;x--){
            var data = store.getAt(x).data;
            if(batchNames[data.classID]){
                batchNames[data.classID].push(data.batchName);
            }else{
                batchNames[data.classID] = [];
                batchNames[data.classID].push(data.batchName);
            }
        }
        cmp = new ViewClass(batchNames);

        cmp.on('afterrender',function(panel){

        });

        cmp.show();
    },
    deleteClassBatch:function(btn){
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var resSetStore =  btn.findParentByType("grid").store;
                    resSetStore.remove(selList);
                }
            },this);
        }
    },
    selectClass:function(trigger){
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
                        desc:'项目ID',
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
                TZ_CLASS_ID: '项目ID',
                TZ_CLASS_NAME: '项目名称'
            },
            multiselect: false,
            callback: function(selection){
                var classID = selection[0].data.TZ_CLASS_ID;
                var className = selection[0].data.TZ_CLASS_NAME;
                var tzParams = '{"ComID":"TZ_GSM_REVIEW_MS_COM","PageID":"TZ_MSPS_ADDBAT_STD","OperateType":"search","comParams":{"classID":"'+classID+'"}}';
                Ext.tzLoad(tzParams,function(responseData) {
                    var form = trigger.findParentByType('form'),
                        batchNames = responseData.root;
                    form.getForm().findField("classID").setValue(classID);
                    form.getForm().findField("className").setValue(className);
                    if(batchNames.length>0) {
                        var batchNameField = form.getForm().findField("batchName");
                        //如果选择的班级以及存在已列表中，则需要验证用户输入的批次名不能与其他改班级下的批次重名
                        //否则在客户端将无法区分同一班级下的两个同名批次
                        var parttern = "^(?!";
                        for (var x = batchNames.length - 1; x >= 0; x--) {
                            var mid = "";
                            //由于汉字不是正则可识别字符，所以先将所有需要校验的项转为Unicode码
                            for (var y = 0; y < batchNames[x].length; y++) {
                                mid += "\\u" + ("00" + batchNames[x].charCodeAt(y).toString(16)).slice(-4);
                            }
                            parttern += mid + '|';
                        }
                        parttern = parttern.replace(/\|$/, ')');
                        var reg = new RegExp(parttern, 'i');
                        var cofObj = {
                            value: '',
                            fieldStyle: 'color:black',
                            readOnly: false
                        };
                        batchNameField.setConfig(cofObj);
                        batchNameField.allowBlank = false;
                        batchNameField.regex = reg;
                        batchNameField.regexText = "当前项目已含有此批次";
                        batchNameField.reset();
                        batchNameField.setValue("");
                    }else{
                        var batchNameField = form.getForm().findField("batchName");
                        var cofObj = {
                            value: '',
                            fieldStyle: 'color:black',
                            readOnly: false
                        };
                        batchNameField.setConfig(cofObj);
                        batchNameField.allowBlank = false;
                        batchNameField.reset();
                        batchNameField.setValue("");
                    }
                });

            }
        })
    },
    newClassEnsure:function(btn){
        var form = btn.findParentByType('panel').child('form').getForm(),
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel'),
            grid = contentPanel.getActiveTab().down('grid');
        if(form.findField('classID').isValid() && form.findField('batchName').isValid()){
            grid.getStore().add(form.getValues());
            btn.findParentByType('panel').close();
        }

    },
    onClassBatchSave:function(btn){
        var store = btn.findParentByType('panel').down('grid').getStore(),
            datas = {},newRecords,removedRecords;
        newRecords = store.getNewRecords();
        removedRecords=store.getRemovedRecords();
        //如果有新建的记录
        if(newRecords.length !== 0){
            datas.add = [];
           for(var x = newRecords.length-1;x>=0;x--){
               delete newRecords[x].data.id;
               datas.add.push(newRecords[x].data);
           }
        }
        //如果有删除的记录
        if(removedRecords.length!==0){
            datas.delete = [];
            for(var x = removedRecords.length-1;x>=0;x--){
                delete removedRecords[x].data.id;
                datas.delete.push(removedRecords[x].data);
            }
        }
        datas = Ext.JSON.encode(datas);
        var tzParams = '{"ComID":"TZ_GSM_REVIEW_MS_COM","PageID":"TZ_GSMS_LIST_STD","OperateType":"U","comParams":'+datas+'}';
    
        Ext.tzSubmit(tzParams,function(responseData){
            btn.findParentByType('panel').down('grid').getStore().commitChanges();
        },"",true,this)
    },
    onClassBatchEnsure:function(btn){
        this.onClassBatchSave(btn);
        btn.findParentByType('panel').close();
    },
    onClassBatchClose:function(btn){
        btn.findParentByType('panel').close();
    },
    viewLDTeam:function(tabview,rowIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GSM_REVIEW_MS_COM"]["TZ_MSPS_RULE_STD"];
        // var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_PLAN_STD"];
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
        var contentPanel, cmp, ViewClass;
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }

        ViewClass = Ext.ClassManager.get(className);
        var store = tabview.grid.getStore();
            classID = store.getAt(rowIndex).data.classID,
            batchID = store.getAt(rowIndex).data.batchID,
            transValue = this.transValues();
        //必须先保存后数据库，才能分配到批次ID
        if(batchID) {
            //当能取到batchID后。说明已经保存过了
            transValue.set("TZ_GSM_JUG_GRP",function(){
                cmp = new ViewClass(transValue);
                cmp.on('afterrender', function (panel) {
                    var tzParams = '{"ComID":"TZ_GSM_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD",' +
                        '"OperateType":"QF","comParams":{"classID":"' + classID + '","batchID":"' + batchID + '"}}';
                    var params = '{"classID":"' + classID + '","batchID":"' + batchID + '","orgID":"' + Ext.tzOrgID + '"}';
                    Ext.tzLoad(tzParams, function (respData) {
                        var judgeStore = panel.lookupReference('GSMinterviewReviewJudgeGrid').getStore();
                        panel.lookupReference('GSMinterviewReviewDesc').getForm().setValues(respData.formData);
                        judgeStore.tzStoreParams = params;
                        judgeStore.reload();
                    });
                });
                var tab = contentPanel.add(cmp);

                contentPanel.setActiveTab(tab);

                Ext.resumeLayouts(true);

                if (cmp.floating) {
                    cmp.show();
                }
            });
        }else{
            //没有批次ID,提醒用户先保存
            Ext.MessageBox.alert("提示","请先保存后再继续操作");
        }

    },
    viewXSTeam:function(tabview,rowIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GSM_REVIEW_MS_COM"]["TZ_MSPS_APPS_STD"];
        // var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_PLAN_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_APPS_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass;
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }

        ViewClass = Ext.ClassManager.get(className);
        var store = tabview.grid.getStore();
            classID = store.getAt(rowIndex).data.classID,
            batchID = store.getAt(rowIndex).data.batchID,
            transValue = this.transValues();
        //必须先保存后数据库，才能分配到批次ID
        if(batchID) {
            //当能取到batchID后。说明已经保存过了
            transValue.set("TZ_GSM_JUG_GRP",function(){
                cmp = new ViewClass(transValue);
                cmp.on('afterrender', function (panel) {
                    var tzParams = '{"ComID":"TZ_GSM_REVIEW_MS_COM","PageID":"TZ_MSPS_APPS_STD",' +
                        '"OperateType":"QF","comParams":{"classID":"' + classID + '","batchID":"' + batchID + '"}}';
                    Ext.tzLoad(tzParams, function (respData) {
                        var judgeStore = panel.lookupReference('GSMinterviewReviewJudgeGrid').getStore(),
                            thisbatchID = respData.formData.batchID;
                        var params = '{"classID":"' + classID + '","batchID":"' + thisbatchID + '","orgID":"' + Ext.tzOrgID + '"}';
                        panel.lookupReference('GSMinterviewReviewDesc').getForm().setValues(respData.formData);
                        judgeStore.tzStoreParams = params;
                        judgeStore.reload();
                    });
                });
                var tab = contentPanel.add(cmp);

                contentPanel.setActiveTab(tab);

                Ext.resumeLayouts(true);

                if (cmp.floating) {
                    cmp.show();
                }
            });
            
        }else{
            //没有批次ID,提醒用户先保存
            Ext.MessageBox.alert("提示","请先保存后再继续操作");
        }

    },
    viewCandidateApplicants:function(grid,rowIndex){
        //是否有访问权限
        Ext.tzSetCompResourses("TZ_GSM_CANDIDATE_COM");
         var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GSM_CANDIDATE_COM"]["TZ_MSPS_APPS_STD"];
         if( pageResSet == "" || pageResSet == undefined){
         Ext.MessageBox.alert('提示', '您没有修改数据的权限');
         return;
         }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        //className='KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateApplicants';
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_APPS_STD，请检查配置。');
            return;
        }

        //var className='KitchenSink.view.GSMinterviewReview.interviewReview.candidateApplicants';
        //var classname='KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewApplicants';
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
        transValue.set(["TZ_GENDER","TZ_LUQU_ZT","TZ_MSPS_STAGE"],function(){
            cmp = new ViewClass(transValue);
            cmp.classID=classID;
            cmp.batchID=batchID;


            cmp.on('afterrender',function(panel){
                var interviewMgrPanel=panel.child('form').down('grid');
                //var store=interviewMgrPanel[0].getStore();
                var  store = interviewMgrPanel.getStore();
                //alert(panel.getXType());


                //var panel11=Ext.ComponentQuery.query("panel[reference=candidateApplicantsPanel]");
               // var form =panel11[0].child('form').getForm();
                var tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_APPS_STD",' +
                    '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';

                var tzStoreParams ='{"classID":"'+classID+'","batchID":"'+batchID+'"}';

                Ext.tzLoad(tzParams,function(respData){
                    var form = panel.child('form').getForm();
                    var formData = respData.formData;
                    form.setValues(formData);
                    form.findField("className").setValue(record.data.className);
                    form.findField("batchName").setValue(record.data.batchName);

                    //设置按钮,评议状态的状态
                   //form.down('button[name=finish]'),form.down('button[name=startup]'),
                    var finishbtn =panel.down('button[name=finish]'),
                        startbtn = panel.down('button[name=startup]'),
                        statusField = panel.down('displayfield[name=interviewStatus]');

                    var finishbtn2 =panel.down('button[name=finish2]'),
                         startbtn2 = panel.down('button[name=startup2]'),
                         statusField2 = panel.down('displayfield[name=interviewStatus2]');

                    startbtn.defaultColor = startbtn.style['background-color'];
                    finishbtn.defaultColor = finishbtn.style['background-color'];

                      startbtn2.defaultColor = startbtn.style['background-color'];
                      finishbtn2.defaultColor = finishbtn.style['background-color'];
                    //alert(respData.formData.status);
                    switch(respData.formData.status){
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

                    switch(respData.formData.status1){
                          case 'A':

                              //进行中
                              startbtn2.flagType='negative';
                              finishbtn2.flagType='positive';
                              startbtn2.setDisabled(true);
                              statusField2.setValue("进行中");
                              break;
                        case 'B':
                            startbtn2.flagType='positive';
                            finishbtn2.flagType='negative';
                            finishbtn2.setDisabled(true);
                            statusField2.setValue("已结束");
                             break;
                        case 'N':
                        default:
                            //初始状态和未开始相同
                            startbtn2.flagType='positive';
                            finishbtn2.flagType='negative'
                            finishbtn2.setDisabled(true);
                            statusField2.setValue("未开始");
                            break;

                     }
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
        });
    },
    addJudge:function(btn){
        var form = btn.findParentByType('grid').findParentByType('panel').down('form').getForm(),
            classID = form.findField("classID").getValue(),
            batchID = form.findField("batchID").getValue();
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GSM_REVIEW_MS_COM"]["TZ_MSPS_ADDJUG_STD"];
        // var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_PLAN_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_ADDJUG_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        var store = btn.findParentByType('tabpanel').down('grid').getStore(),
            batchNames = {};
        for(var x = store.getRange().length-1;x>=0;x--){
            var data = store.getAt(x).data;
            if(batchNames[data.classID]){
                batchNames[data.classID].push(data.batchName);
            }else{
                batchNames[data.classID] = [];
                batchNames[data.classID].push(data.batchName);
            }
        }
        cmp = new ViewClass(batchNames);

        cmp.on('afterrender',function(panel){
            var tzStoreParams = '{"classID":"'+classID+'","orgID":"'+Ext.tzOrgID+'","jugTyp":"004"}',
                store = panel.child('grid').getStore();
            store.tzStoreParams = tzStoreParams;
            store.load();
        });
        var tzParams = '{"ComID":"TZ_GSM_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"IFP","comParams":{"type":"IFP","classID":"'+classID+'","batchID":"'+batchID+'"}}';
        this.isProgress(tzParams,function(status) {
            switch (status) {
                case 'A':
                    Ext.MessageBox.alert("提示", "当前评审正在进行中，不能添加评委");
                    break;
                case'B':
                case'N':
                default:
                    cmp.show();
                    break;
            }
        });
    },
    addXSJudge:function(btn){
        var self=this,
            form = btn.findParentByType('grid').findParentByType('panel').down('form').getForm(),
            grid = btn.findParentByType("grid"),
            classID = form.findField("classID").getValue(),
            batchID = form.findField("batchID").getValue();
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GSM_REVIEW_MS_COM"]["TZ_MSPS_ADDJUG_STD"];
        // var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_PLAN_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_ADDJUG_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        var store = btn.findParentByType('tabpanel').down('grid').getStore(),
            batchNames = {};
        for(var x = store.getRange().length-1;x>=0;x--){
            var data = store.getAt(x).data;
            if(batchNames[data.classID]){
                batchNames[data.classID].push(data.batchName);
            }else{
                batchNames[data.classID] = [];
                batchNames[data.classID].push(data.batchName);
            }
        }
        cmp = new ViewClass(batchNames);

        cmp.on('afterrender',function(panel){
            var tzStoreParams = '{"classID":"'+classID+'","orgID":"'+Ext.tzOrgID+'","jugTyp":"005"}',
                store = panel.child('grid').getStore();
            store.tzStoreParams = tzStoreParams;
            store.load();
        });
        var tzParams = '{"ComID":"TZ_GSM_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"IFP","comParams":{"type":"IFP","classID":"'+classID+'","batchID":"'+batchID+'"}}';
        this.isProgress(tzParams,function(status){
            switch(status){
                case 'A':
                    Ext.MessageBox.alert("提示","当前评审正在进行中，不能添加评委");
                    break;
                case'B':
                case'N':
                default:
                    cmp.show();
                    break;
            }
        })
    },
    clearCondition : function(btn){
        btn.findParentByType("grid").filters.clearFilters(true);
    },
    onReviewRuleSave:function(btn){
        var comParams = this.getRuleParams(),
            me=this;
        var tzParams = '{"ComID":"TZ_GSM_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
            var grid = me.getView().lookupReference("GSMinterviewReviewJudgeGrid");
            var store = grid.getStore();
            if(tzParams.indexOf('"typeFlag":"JUDGE"')>-1||tzParams.indexOf("delete")>-1){
                store.reload();
            }
        },"",true,this);
    },
    onReviewRuleEnsure:function(btn){
        this.onReviewRuleSave(btn);
        this.getView().close();
    },
    onReviewRuleClose:function(){
        this.getView().close();
    },
    recommendationSave:function(btn){
        var values = btn.findParentByType('panel').lookupReference("GSMCommendationForm").getForm().getValues(),
            comParams = '"add":'+Ext.JSON.encode(values);
            me=this;
        var tzParams = '{"ComID":"TZ_GSM_REVIEW_MS_COM","PageID":"TZ_MSPS_ADDSTU_STD","OperateType":"SAVE","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
            btn.findParentByType('panel').lookupReference("GSMCommendationForm").getForm().setValues(responseData);
        },"",true,this);
    },
    recommendationEnsure:function(btn){
        this.recommendationSave(btn);
        this.getView().close();
    },
    recommendationClose:function(){
        this.getView().close();
    },
    onReviewXSSave:function(btn){
        var comParams = this.getRuleParams(),
            me=this;
        var tzParams = '{"ComID":"TZ_GSM_REVIEW_MS_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
            var grid = me.getView().lookupReference("GSMinterviewReviewJudgeGrid");
            var store = grid.getStore();
            if(tzParams.indexOf('"typeFlag":"JUDGE"')>-1||tzParams.indexOf("delete")>-1){
                store.reload();
            }
        },"",true,this);
    },
    onReviewXSEnsure:function(btn){
        this.onReviewXSSave(btn);
        this.getView().close();
    },
    onReviewXSClose:function(){
        this.getView().close();
    },
    getRuleParams:function(){
        var form = this.getView().lookupReference("GSMinterviewReviewDesc").getForm();
        var formParams = form.getValues();
        formParams.classID = formParams.classID?formParams.classID:form.findField('classID').getValue();
        formParams.batchID = formParams.batchID?formParams.batchID:form.findField('batchID').getValue();

        //更新操作参数
        var comParams = "";

        //修改json字符串
        var editJson = "";

        editJson = '{"typeFlag":"RULE","data":'+Ext.JSON.encode(formParams)+'}';

        var grid = this.getView().lookupReference("GSMinterviewReviewJudgeGrid");
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
        return comParams;
    },
    removeJudge:function(view,rowIndex){
        var classID = view.grid.findParentByType("panel").child("form").getForm().findField("classID").getValue(),
            batchID = view.grid.findParentByType("panel").child("form").getForm().findField("batchID").getValue(),
            tzParams = '{"ComID":"TZ_GSM_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD","OperateType":"IFP","comParams":{"type":"IFP","classID":"'+classID+'","batchID":"'+batchID+'"}}';
        this.isProgress(tzParams,function(status){
            switch(status) {
                case 'A':
                    Ext.MessageBox.alert('提示', '评审正在进行中，不能删除评委');
                    break;
                case 'B':
                case 'N':
                default:
                    Ext.MessageBox.alert('提示', '您确定要删除所选记录吗?', function (btnId) {
                        if (btnId == 'ok') {
                            var store = view.store;
                            store.removeAt(rowIndex);
                        }
                    }, this);
                    break;
            }
        });
    },
    sendEmail: function(btn) {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GSM_REVIEW_MS_COM"]["TZ_MSPS_MAIL_STD"];
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
        for(var x=datas.length-1;x>=0;x--){
            arr.push(datas[x].data.oprID);
        }
        if(arr.length!==0) {
            var params = {
                ComID: "TZ_GSM_REVIEW_MS_COM",
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
                        "EmailTmpName": ["TZ_MAIL_MSPSJUG", "TZ_MAILYC_MSPSJ", "TZ_MAILGSM_MSPS"],
                        //创建的需要发送的听众ID;
                        "audienceId": audID,
                        //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                        "file": "N"
                    });
                }
            })
        }else{
            Ext.MessageBox.alert("提示","请选择收件人");
        }
    },
    isProgress : function(params,callback){
        Ext.tzLoad(params,function(respData){
            callback(respData.progress);
        });
    },
    addJudgeClose:function(btn){
        btn.findParentByType("panel").close();
    },
    addJudgeEnsure:function(btn){
        var grid = btn.findParentByType("panel").child('grid'),
            select = grid.getSelectionModel().getSelection(),
        //由于新增弹出窗口会使当前页面不能操作，弹出窗口对应的tab即是当前活动的Tab
            activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),
            store = activeTab.child('form').down('grid').getStore(),
            hasInterviewed = false,
            classID = activeTab.child('form').down('form').getForm().findField("classID").getValue(),
            batchID = activeTab.child('form').down('form').getForm().findField("batchID").getValue();
        for(var x =0;x<select.length;x++){
            if(store.find('judgeID',select[x].data.judgeID)<0) {
                delete select[x].data.id;
                select[x].data.classID=classID;
                select[x].data.batchID = batchID;
                store.add(select[x].data);
            }else{
                hasInterviewed = true;
            }
        }
        if(hasInterviewed){
            Ext.Msg.alert("提示","部分评委已添加");
        }
        btn.findParentByType("panel").close();
        //btn.findParentByType("grid").findParentByType("panel").close();
    }
});