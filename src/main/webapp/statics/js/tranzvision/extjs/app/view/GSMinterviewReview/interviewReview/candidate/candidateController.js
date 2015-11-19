Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateController', {

    extend: 'Ext.app.ViewController',
    alias: 'controller.candidateController',

    TZ_MSPS_STAGE:new KitchenSink.view.common.store.appTransStore("TZ_MSPS_STAGE"),
   //学术委员会审议
    editApplicant24 : function(btn,rowIndex){
        var self=this,

            grid = btn.findParentByType("grid"),

            datas = grid.getStore().getAt(rowIndex).data,
            classID = datas.classID,
            batchID =datas.batchID,
            whichColnums = btn.findParentByType("window").whichColnums,
            jugaccStatusStore = grid.findParentByType('window').scoreStore;
        if(!jugaccStatusStore.isLoaded()){
            jugaccStatusStore.load();
        }
        var win = Ext.create('Ext.window.Window', {
            title: '编辑评委成员评议信息',
            width: 600,
            height: 245,
            modal: true,
            frame: true,
            whichColnums:whichColnums,
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
                        name: 'pwName',
                        editable: false,
                        value:datas.pwName,
                        ignoreChangesFlag: true
                    }, {
                        xtype: 'textfield',
                        fieldLabel: '报名表编号',
                        name: 'appInsID',
                        hidden:true,
                        editable: false,
                        value:datas.appInsID,
                        ignoreChangesFlag: true
                    }, {
                        xtype: 'combo',
                        fieldLabel: "审议结果",
                        name: whichColnums,
                        store: jugaccStatusStore,
                        value:datas[whichColnums],
                        displayField: 'TSDesc',
                        valueField: 'TValue',
                        queryMode: 'local',
                        editable: false,
                        ignoreChangesFlag: true
                    }
                ]
                //items END
            }],
            buttons: [{
                text: '确定',
                iconCls: "ensure",
                handler: function (btn) {
                    var form = btn.findParentByType("window").child("form").getForm(),
                        x = btn.findParentByType("window").whichColnums,
                        recordStatus = form.findField(x).getValue(),
                        record = grid.getStore().getAt(rowIndex);
                        record.set(x,recordStatus||'');
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
    //编辑领导小组审议时的状态信息
    editApplicant23 : function(btn,rowIndex){
        var self=this,
            grid = btn.findParentByType("grid"),
            datas = grid.getStore().getAt(rowIndex).data,
            classID = datas.classID,
            batchID =datas.batchID,
            jugaccStatusStore = grid.findParentByType('window').scoreStore,
            whichColnums = btn.findParentByType("window").whichColnums;
        if(!jugaccStatusStore.isLoaded()){
            jugaccStatusStore.load();
        }
        var win = Ext.create('Ext.window.Window', {
            title: '编辑评委成员评议信息',
            width: 600,
            height: 245,
            modal: true,
            frame: true,
            whichColnums:whichColnums,
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
                        name: 'pwName',
                        editable: false,
                        value:datas.pwName,
                        ignoreChangesFlag: true
                    }, {
                        xtype: 'textfield',
                        fieldLabel: '报名表编号',
                        name: 'appInsID',
                        hidden:true,
                        editable: false,
                        value:datas.appInsID,
                        ignoreChangesFlag: true
                    }, {
                        xtype: 'combo',
                        fieldLabel: "审议结果",
                        name: whichColnums,
                        store: jugaccStatusStore,
                        value:datas[whichColnums],
                        displayField: 'TSDesc',
                        valueField: 'TValue',
                        queryMode: 'local',
                        editable: false,
                        ignoreChangesFlag: true
                    }
                ]
                //items END
            }],
            buttons: [{
                text: '确定',
                iconCls: "ensure",
                handler: function (btn) {
                        var form = btn.findParentByType("window").child("form").getForm(),
                        x = btn.findParentByType("window").whichColnums,
                        recordStatus = form.findField(x).getValue(),
                        record = grid.getStore().getAt(rowIndex);
                        record.set(x,recordStatus||'');
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


    editApplicant22 : function(btn,rowIndex){
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
        var appInsID = datas.appINSID,
            tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"CK","comParams":{"type":"major","classID":"'+classID+'","batchID":"'+batchID+'","appInsID":"'+appInsID+'"}}';
        Ext.tzLoad(tzParams, function (respData) {
            var majorStore = Ext.create('Ext.data.Store',{
                autoLoad:false,
                pageSize:0,
                data:respData.root,
                model:Ext.create('Ext.data.Model',{
                    fiels:['TValue','TSDesc']
                })
            });
            var win = Ext.create('Ext.window.Window', {
            title: '编辑申请人',
            width: 600,
            height: 300,
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
                        xtype: 'combo',
                        fieldLabel: "拟录取系所",
                        name: 'nlqxs',
                        store: majorStore,
                        value:datas.nlqxs,
                        displayField: 'TSDesc',
                        valueField: 'TValue',
                        queryMode: 'local',
                        editable: false,
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
                            nlqxs = form.findField("nlqxs").getValue(),
                            record = grid.getStore().getAt(rowIndex);
                        record.set("LUQUZT",recordStatus||'');
                        record.set("nlqxs",nlqxs||'');
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
        });
    },

    endInterview : function(btn){
        if (btn.flagType === 'positive') {
            var classID = btn.findParentByType('form').getForm().findField('classID').getValue(),
                batchID = btn.findParentByType('form').getForm().findField('batchID').getValue(),
                tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_PLAN_STD","OperateType":"CK","comParams":{"type":"finishClick","classID":"' + classID + '","batchID":"' + batchID + '"}}';
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
    endInterview2 : function(btn){
        if (btn.flagType === 'positive') {
            var classID = btn.findParentByType('form').getForm().findField('classID').getValue(),
                batchID = btn.findParentByType('form').getForm().findField('batchID').getValue(),
                tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_PLAN_STD","OperateType":"CK","comParams":{"type":"finishClick2","classID":"' + classID + '","batchID":"' + batchID + '"}}';
            Ext.tzLoad(tzParams, function (respData) {
                //可点击状态，设置setType值为1,当前按钮已点击
                btn.setType = 1;
                //更改当前按钮样式
                btn.setDisabled(true);
                //关闭后设置启动按钮卫可点击状态
                var startBtn = btn.findParentByType("form").down("button[name=startup2]");
                startBtn.flagType = 'positive';
                btn.flagType = 'negative';
                //更新启动按钮的样式
                startBtn.setDisabled(false);
                btn.findParentByType("form").getForm().findField("interviewStatus2").setValue("已结束");
            });
        }

    },
    startInterview : function(btn){
        var self = this;
        if(btn.flagType === 'positive') {
            var classID = btn.findParentByType('form').getForm().findField('classID').getValue(),
                batchID = btn.findParentByType('form').getForm().findField('batchID').getValue(),
                tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_PLAN_STD","OperateType":"CK","comParams":{"type":"startClick","classID":"' + classID + '","batchID":"' + batchID + '"}}';
            Ext.tzLoad(tzParams, function (responseData) {
                if (responseData.isPass === 'Y') {

                    //可点击状态,设置setType值为1,当前按钮已点击
                    var finishButton,
                        enduring = self.TZ_MSPS_STAGE.getAt(self.TZ_MSPS_STAGE.find('TValue','A')).data.TSDesc;    
                    btn.setType = 1;
                    //更改当前按钮样式
                    btn.setDisabled(true);
                    //启动在可点击的情况下被点击后，设置关闭按钮为可点击状态
                    finishButton = btn.findParentByType("form").down("button[name=finish]");
                    finishButton.flagType = 'positive';
                    btn.flagType = 'negative';
                    //设置关闭按钮样式
                    finishButton.setDisabled(false);
                    btn.findParentByType("form").getForm().findField("interviewStatus").setValue(enduring);

                } else {
                    Ext.MessageBox.alert('启动失败','该批次下还没有评委');
                }
            });
        }

    },
    startInterview2 : function(btn){
        var self=this;
        if(btn.flagType === 'positive') {
            var classID = btn.findParentByType('form').getForm().findField('classID').getValue(),
                batchID = btn.findParentByType('form').getForm().findField('batchID').getValue(),
                tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_PLAN_STD","OperateType":"CK","comParams":{"type":"startClick2","classID":"' + classID + '","batchID":"' + batchID + '"}}';
            Ext.tzLoad(tzParams, function (responseData) {
                if (responseData.isPass === 'Y') {

                    //可点击状态,设置setType值为1,当前按钮已点击
                    var finishButton,
                        enduring = self.TZ_MSPS_STAGE.getAt(self.TZ_MSPS_STAGE.find('TValue','A')).data.TSDesc;
                    btn.setType = 1;
                    //更改当前按钮样式
                    btn.setDisabled(true);
                    //启动在可点击的情况下被点击后，设置关闭按钮为可点击状态
                    finishButton = btn.findParentByType("form").down("button[name=finish2]");
                    finishButton.flagType = 'positive';
                    btn.flagType = 'negative';
                    //设置关闭按钮样式
                    finishButton.setDisabled(false);
                    btn.findParentByType("form").getForm().findField("interviewStatus2").setValue(enduring);

                }else if(responseData.isPass === 'P'){
                    Ext.MessageBox.alert('启动失败','必须先完成招聘领导小组审议');
                } else {
                    Ext.MessageBox.alert('启动失败','该批次下还没有评委');
                }
            });
        }

    },
    //提交
    submitMS: function(btn,rowIndex){
        var self=this,
            grid = btn.findParentByType("grid"),
            datas = grid.getStore().getAt(rowIndex).data,
            classID = datas.classID,
            batchID =datas.batchID,
            appInsID=datas.appInsID,
            pwID=datas.pwID,
            tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_ZPLDTEAM_STD","OperateType":"CK","comParams":{"type":"submit","classID":"' + classID + '","batchID":"' + batchID + '","appInsID":"' + appInsID + '","pwID":"' + pwID + '"}}';
            Ext.tzLoad(tzParams, function (responseData) {
                if (responseData.TZ_PSHEN_ZT === 'Y') {
                    Ext.Msg.alert("提示",'提交成功');
                } else {

                    if (responseData.TZ_PSHEN_ZT === 'C') {

                    Ext.Msg.alert("提示", '已提交过，不需要重复提交');
                 }else{
                    Ext.Msg.alert("提示", '评委还未评议数据');
                    }
                }
            });


    },
    submitSX:function(btn,rowIndex){
        var self=this,
            grid = btn.findParentByType("grid"),
            datas = grid.getStore().getAt(rowIndex).data,
            classID = datas.classID,
            batchID =datas.batchID,
            appInsID=datas.appInsID,
            pwID=datas.pwID,
            tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_ZPLDTEAM_STD","OperateType":"CK","comParams":{"type":"submitXS","classID":"' + classID + '","batchID":"' + batchID + '","appInsID":"' + appInsID + '","pwID":"' + pwID + '"}}';
            Ext.tzLoad(tzParams, function (responseData) {
                if (responseData.TZ_PSHEN_ZT === 'Y') {
                    Ext.Msg.alert("提示",'提交成功');
                } else {

                    if (responseData.TZ_PSHEN_ZT === 'C') {

                    Ext.Msg.alert("提示", '已提交过，不需要重复提交');
                 }else{
                    Ext.Msg.alert("提示", '评委还未评议数据');
                    }
                }
            });
    },
    //退回
    viewCancel: function(btn,rowIndex){
        var self=this,
            grid = btn.findParentByType("grid"),
            datas = grid.getStore().getAt(rowIndex).data,
            classID = datas.classID,
            batchID =datas.batchID,
            appInsID=datas.appInsID,
            pwID=datas.pwID,
            tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_ZPLDTEAM_STD","OperateType":"CK","comParams":{"type":"reversion","classID":"' + classID + '","batchID":"' + batchID + '","appInsID":"' + appInsID + '","pwID":"' + pwID + '"}}';
            Ext.tzLoad(tzParams, function (responseData) {
                if (responseData.TZ_PSHEN_ZT === 'Y') {
                    Ext.Msg.alert("提示",'撤回成功');
                } else {
                    Ext.Msg.alert("提示", '评委还未提交评议数据');
                }
            });


    },
    //退回
    viewXSCancel: function(btn,rowIndex){
        var self=this,
            grid = btn.findParentByType("grid"),
            datas = grid.getStore().getAt(rowIndex).data,
            classID = datas.classID,
            batchID =datas.batchID,
            appInsID=datas.appInsID,
            pwID=datas.pwID,
            tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_ZPLDTEAM_STD","OperateType":"CK","comParams":{"type":"reversionXS","classID":"' + classID + '","batchID":"' + batchID + '","appInsID":"' + appInsID + '","pwID":"' + pwID + '"}}';
            Ext.tzLoad(tzParams, function (responseData) {
                if (responseData.TZ_PSHEN_ZT === 'Y') {
                    Ext.Msg.alert("提示",'撤回成功');
                } else {
                    Ext.Msg.alert("提示", '评委还未提交评议数据');
                }
            });


    },

    //KitchenSink.view.interviewManagement.interviewReview.interviewProgress
    addApplicantEnsure : function(btn,event){
        var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),
            targetStore = activeTab.down("grid[name=candidateStudentGrid]").getStore(),
            select = btn.findParentByType("window").down("grid[name=candidateBRMapplyGrid]").getSelectionModel().getSelection(),
            hasInterviewed = false,
            hasNewApp = false,
            mid = [];
        for(var x =0;x<select.length;x++){
            if(targetStore.find('appINSID',select[x].data.appINSID)<0) {
                delete select[x].data.isInterviewed;
                delete select[x].data.id;
                hasNewApp=true;
                mid.push(select[x].data);
            }else{
                hasInterviewed = true;
            }
        }
        if(hasNewApp){
            targetStore.add(mid);
        }
        if(hasInterviewed){
            Ext.Msg.alert("提示","有申请人已经存在于面试阶段");
        }
        btn.findParentByType("panel").close();
    },
    addApplicantClose : function(btn){
        btn.findParentByType("panel").close();
    },
    removeApplicants:function(btn){
        var interviewStatus=btn.findParentByType('form').getForm().findField('interviewStatus').value;
        var interviewStatus2=btn.findParentByType('form').getForm().findField('interviewStatus2').value;
        var enduring = this.TZ_MSPS_STAGE.getAt(this.TZ_MSPS_STAGE.find('TValue','A')).data.TSDesc;        //alert(interviewStatus+"=="+interviewStatus2);
        if (interviewStatus==enduring||interviewStatus2==enduring){
            Ext.MessageBox.alert("提示","评审正在进行中，不能删除申请人");
            return;
        }

        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var resSetStore =  btn.findParentByType("grid").store;
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var mid = [];
                    for ( var i=0;i<checkLen;i++)
                    {
                        mid.push(selList[i]);
                    }
                    resSetStore.remove(mid);
                }

            },"",true,this);
        }
    },
    addApplicantsBMR : function(btn){
        var classID = btn.findParentByType('form').getForm().findField('classID').value;
        var batchID = btn.findParentByType('form').getForm().findField('batchID').value;

        if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存组件注册信息后，再新增页面注册信息。");
            return;
        }
        /*状态为进行中的时候，禁止添加申请人*/

        var interviewStatus=btn.findParentByType('form').getForm().findField('interviewStatus').value;
        var interviewStatus2=btn.findParentByType('form').getForm().findField('interviewStatus2').value;
        var enduring = this.TZ_MSPS_STAGE.getAt(this.TZ_MSPS_STAGE.find('TValue','A')).data.TSDesc;
        //alert(interviewStatus+"=="+interviewStatus2);
        if (interviewStatus==enduring||interviewStatus2==enduring){
            Ext.MessageBox.alert("提示","评审正在进行中，不能添加申请人");
            return;
        }

        /*
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_PROBACKMSG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];*/
        var className="KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateWindow";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_SMTDTXX_STD，请检查配置。');
            return;
        }
        var win = this.lookupReference('candidateWindow');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "add";
        //cmp = new ViewClass();

        //页面注册信息表单
        var form = win.child("form").getForm();
        //form.reset();

        var formParams = form.getValues();
        formParams["classID"] = classID;
        formParams["batchID"] = batchID;
        form.setValues(formParams);
        win.show();
    },
    ViewTeamReview : function(grid,rowIndex){
        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        var batchID = record.data.batchID;
        var appINSID=record.data.appINSID;
        var realName=record.data.realName;
        var zpldxzsytgl=record.data.zpldxzsytgl;

        if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存组件注册信息后，再设置评议小组信息信息。");
            return;
        }
        /*
         var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_PROBACKMSG_STD"];
         if( pageResSet == "" || pageResSet == undefined){
         Ext.MessageBox.alert('提示', '您没有修改数据的权限');
         return;
         }
         //该功能对应的JS类
         var className = pageResSet["jsClassName"];*/
        var className="KitchenSink.view.GSMinterviewReview.interviewReview.candidate.zpldteamReviewWindow";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_SMTDTXX_STD，请检查配置。');
            return;
        }
        var params = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"CK","comParams":{"type":"searchScore","classID":"'+classID+'"}}';
        Ext.tzLoad(params,function(respData){
             //操作类型设置为新增
            var contentPanel;
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
            contentPanel.body.addCls('kitchensink-example');
            
            var ScoreStore = Ext.create('Ext.data.Store',{
                autoLoad:false,
                pageSize:0,
                data:respData.root,
                model:Ext.create('Ext.data.Model',{
                    fields:['TValue','TSDesc']
                })
            });
            var params = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_ZPLDTEAM_STD","OperateType":"SS","comParams":{"type":"CJX","classID":"'+classID+'","batchID":"'+batchID+'","appINSID":"'+appINSID+'"}}';
            Ext.tzLoad(params,function(respDate){
                var params = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_PLAN_STD","OperateType":"CK","comParams":{"type":"isAdmin"}}';
                Ext.tzLoad(params,function(flag){
                    var apps = respDate.root[0],
                        gridStore;
                    var fields = [{name: 'classID'},
                                    {name: 'batchID'},
                                    {name: 'pwID'},
                                    {name: 'pwName'},
                                    {name: 'appInsID'},
                                    {name: 'ifVote'}];
                    for(var x=0;apps&&x<apps.CJX.length;x++){
                        fields.push({
                            name:apps.CJX[x].ID
                        });
                    }
                    gridStore = Ext.create('Ext.data.Store',{
                        alias: 'store.zpldteamReviewWindowStore',
                        autoLoad:false,
                        comID: 'TZ_GSM_CANDIDATE_COM',
                        pageID: 'TZ_ZPLDTEAM_STD',
                        pageSize:0,
                        tzStoreParams:'{"classID":"'+classID+'","batchID":"'+batchID+'","appINSID":"'+appINSID+'"}',
                        proxy: Ext.tzListProxy(),
                        fields:fields
                    });

                    Ext.syncRequire(className);
                    ViewClass = Ext.ClassManager.get(className);
                    //新建类
                    win = new ViewClass(ScoreStore,fields.length===6?[]:apps.CJX,flag);
                    contentPanel.add(win);
                    win.actType = "add";
                    win.on('afterrender',function(panel) {

                        var form = panel.child("form").getForm();
                        var formParams={};
                        formParams["classID"] = classID;
                        formParams["batchID"] = batchID;
                        formParams["realName"] = realName;
                        formParams["zpldxzsytgl"] = zpldxzsytgl;
                        form.setValues(formParams);

                        panel.child("form").child('grid').setStore(gridStore);
                        panel.child("form").child('grid').store.load();
                        
                        /*
                        var tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_APPS_STD",' +
                            '"OperateType":"QF","comParams":{"classID":"' + classID + '","batchID":"' + batchID + '"}}';

                        var tzStoreParams = '{"classID":"' + classID + '","batchID":"' + batchID + '","appINSID":"' + appINSID + '"}';
                        var store=win.lookupReference('zpldteamReviewGrid').store;
                        store.tzStoreParams = tzStoreParams;
                        store.load();
                        */

                    });
                    win.show();
                });
                
            });
        });
    },
    ViewXxwyReview : function(grid,rowIndex){
        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        var batchID = record.data.batchID;
        var appINSID=record.data.appINSID;
        var realName=record.data.realName;
        var xswyhsytgl=record.data.xswyhsytgl;

        if(this.getView().actType == "add"){
            Ext.MessageBox.alert("提示","请先保存组件注册信息后，再设置评议小组信息信息。");
            return;
        }
        /*
         var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_PROBACKMSG_STD"];
         if( pageResSet == "" || pageResSet == undefined){
         Ext.MessageBox.alert('提示', '您没有修改数据的权限');
         return;
         }
         //该功能对应的JS类
         var className = pageResSet["jsClassName"];*/
        var className="KitchenSink.view.GSMinterviewReview.interviewReview.candidate.xswyhReviewWindow";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_XSWYH_STD，请检查配置。');
            return;
        }
         
        var params = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"CK","comParams":{"type":"searchScore","classID":"'+classID+'"}}';
        Ext.tzLoad(params,function(respData){
             //操作类型设置为新增
            var contentPanel;
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
            contentPanel.body.addCls('kitchensink-example');
            
            var ScoreStore = Ext.create('Ext.data.Store',{
                autoLoad:false,
                pageSize:0,
                data:respData.root,
                model:Ext.create('Ext.data.Model',{
                    fields:['TValue','TSDesc']
                })
            });
            var params = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_ZPLDTEAM_STD","OperateType":"SS","comParams":{"type":"CJX","classID":"'+classID+'","batchID":"'+batchID+'","appINSID":"'+appINSID+'"}}';
            Ext.tzLoad(params,function(respDate){
                var params = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_PLAN_STD","OperateType":"CK","comParams":{"type":"isAdmin"}}';
                Ext.tzLoad(params,function(flag){
                    var apps = respDate.root[0],
                        gridStore;
                    var fields = [{name: 'classID'},
                                    {name: 'batchID'},
                                    {name: 'pwID'},
                                    {name: 'pwName'},
                                    {name: 'appInsID'},
                                    {name: 'ifVote'}];
                    for(var x=0;apps&&x<apps.CJX.length;x++){
                        fields.push({
                            name:apps.CJX[x].ID
                        });
                    }
                    gridStore = Ext.create('Ext.data.Store',{
                        alias: 'store.xswyhReviewWindowStore',
                        autoLoad:false,
                        comID: 'TZ_GSM_CANDIDATE_COM',
                        pageID: 'TZ_XSWYH_STD',
                        pageSize:0,
                        tzStoreParams:'{"classID":"'+classID+'","batchID":"'+batchID+'","appINSID":"'+appINSID+'"}',
                        proxy: Ext.tzListProxy(),
                        fields:fields
                    });

                    Ext.syncRequire(className);
                    ViewClass = Ext.ClassManager.get(className);
                    //新建类
                    win = new ViewClass(ScoreStore,fields.length===6?[]:apps.CJX,flag);
                    contentPanel.add(win);
                    win.actType = "add";
                    win.on('afterrender',function(panel) {
                        var form = panel.child("form").getForm();
                        var formParams = form.getValues();
                        formParams["classID"] = classID;
                        formParams["batchID"] = batchID;
                        formParams["realName"] = realName;
                        formParams["xswyhsytgl"] = xswyhsytgl;

                        form.setValues(formParams);
                        panel.child("form").child('grid').setStore(gridStore);
                        panel.child("form").child('grid').store.load();
                    });
                    win.show();
                });
                
            });
        });
    },

    viewCandidateTJBG:function(tabview,rowIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GSM_REVIEW_MS_COM"]["TZ_MSPS_ADDSTU_STD"];
        // var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_PLAN_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_ADDSTU_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass;
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        var record = tabview.grid.getStore().getAt(rowIndex).data;
        ViewClass = Ext.ClassManager.get(className);
        cmp = new ViewClass();
        cmp.on('afterrender',function(panel){
            var comParams = '"classID":"'+record.classID+'","batchID":"'+record.batchID+'","appInsID":"'+record.appINSID+'"',
                tzParams = '{"ComID":"TZ_GSM_REVIEW_MS_COM","PageID":"TZ_MSPS_ADDSTU_STD","OperateType":"QG","comParams":{'+comParams+'}}';
            Ext.tzLoad(tzParams,function(respData) {
                panel.lookupReference("GSMCommendationForm").getForm().setValues(respData);
            });
        });
        cmp.show();
    },
    sendEmail:function(btn){
        var store = btn.findParentByType('grid').store;
        var classID = btn.findParentByType('grid').findParentByType('form').getForm().findField('classID').getValue();
        var batchID = btn.findParentByType('grid').findParentByType('form').getForm().findField('batchID').getValue()
        var modifiedRecs = store.getModifiedRecords();
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GSM_REVIEW_MS_COM"]["TZ_MSPS_MAIL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有发送邮件的权限');
            return;
        }
        if(modifiedRecs.length>0){
            Ext.MessageBox.alert("提示","请先保存列表数据之后再发送邮件！");
            return;
        };
        var datas = btn.findParentByType('grid').getSelectionModel().getSelection(),
            hasNull = false;
            arr = [];
        for(var x=datas.length-1;x>=0;x--){
            if(datas[x].data.LUQUZT){
                arr.push(datas[x].data.oprID);
            }else{
                hasNull = true;
                break;
            }
        }
        if(!hasNull){
            if(arr.length > 0) {
            
                var params = {
                    ComID: "TZ_GSM_REVIEW_MS_COM",
                    PageID: "TZ_MSPS_MAIL_STD",
                    OperateType: "GETL",
                    comParams: {type: "getL", oprArr: arr,classID:classID,batchID:batchID}
                };
                Ext.Ajax.request({
                    url: Ext.tzGetGeneralURL,
                    params: {tzParams: Ext.JSON.encode(params)},
                    success: function (responseData) {
                        var audID = Ext.JSON.decode(responseData.responseText).comContent;

                        Ext.tzSendEmail({
                            //发送的邮件模板;
                            "EmailTmpName": ["TZ_MSPS_RESULT"],
                            //创建的需要发送的听众ID;
                            "audienceId": audID,
                            //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                            "file": "N"
                        });
                    }
                });
            }else{
                Ext.MessageBox.alert("提示","至少选择一位申请人发送邮件");
            }
        }else{
            Ext.MessageBox.alert("提示","没有评审完所有选择的申请人");
        }
    },
    publishResult:function(grid, rowIndex, colIndex){
        //是否有访问权限
        /*var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_FB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }*/
        //该功能对应的JS类
       // var className = pageResSet["jsClassName"];
        var className = "KitchenSink.view.enrollmentManagement.bmb_lc.studentsList";
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_BMSH_FB_STD，请检查配置。');
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
        cmp = new ViewClass();
        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        cmp.on('afterrender',function(panel){
            var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_FB_STD","OperateType":"tzLoadGridColumns","comParams":{"classID":"'+classID+'"}}';
            Ext.tzLoad(tzParams,function(responseData){
                var msResGridColumns = [{
                    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.bmbbh","报名表编号"),
                    dataIndex: 'bmb_id',
                    filter: {
                        type: 'number',
                        fields:{gt: {iconCls: Ext.baseCSSPrefix + 'grid-filters-gt', margin: '0 0 3px 0'}, lt: {iconCls: Ext.baseCSSPrefix + 'grid-filters-lt', margin: '0 0 3px 0'}, eq: {iconCls: Ext.baseCSSPrefix + 'grid-filters-eq', margin: 0}}
                    },
                    minWidth: 125,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.name","姓名"),
                    dataIndex: 'ry_name',
                    minWidth: 100,
                    flex:1,
                    filter: {
                        type: 'string'
                    }
                },{
                    text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.country","国籍"),
                    dataIndex: 'country',
                    minWidth: 100,
                    flex:1,
                    filter: {
                        type: 'string'
                    }
                },{
                    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.ckqtgbnr","查看前台公布内容"),
                    align: 'center',
                    groupable: false,
                    width: 150,
                    renderer: function(v) {
                        return '<a href="javascript:void(0)">'+Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.view","查看")+'</a>';
                    },
                    listeners:{
                        click:'sec_frontDeac'
                    }
                }];
                var bmlc_zd=responseData['bmlc_zd'];

                //LYY  2015-08-14  流程发布更改为单元格点击事件，不使用actioncolumn
                var lcfbLCJDdataIndexId = "";
                for(var i=0;i<bmlc_zd.length;i++){
                    lcfbLCJDdataIndexId= bmlc_zd[i]['bmlc_id']+"#^"+bmlc_zd[i]['bmlc_name'];
                    msResGridColumns.push({
                        text:bmlc_zd[i]['bmlc_name'] ,
                        dataIndex: lcfbLCJDdataIndexId,
                        width: 170,
                        editable:false,
                        renderer:function(value){
                            var _url1=value.split(",");
                            if(value!=""){
                                return "<div class='x-colorpicker-field-swatch-inner' style='width:20%;height:50%;background-color: #"+_url1[0]+"'></div><div style='width:70%;margin-left:auto;'>"+_url1[1]+"</div>";
                            }
                        }
                    });
                }
                var bmb_lcStudentsStore = new KitchenSink.view.enrollmentManagement.bmb_lc.studentsStore();
                var msResGrid = Ext.create("Ext.grid.Panel",{
                    height: panel.getHeight()-58,
                    frame: true,
                    name: 'student_listGr',
                    reference: 'student_listGr',
                    store:bmb_lcStudentsStore,
                    columnLines: true,    //显示纵向表格线
                    selModel:{
                        type: 'checkboxmodel'
                    },
                    plugins:[
                        {
                            ptype: 'gridfilters',
                            controller: 'appFormClass'
                        },
                        {
                            ptype: 'cellediting',
                            clicksToEdit: 1
                        }
                    ],
                    dockedItems:[{
                        xtype: "toolbar",
                        items: [{
                            text:Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.plfbjg","批量发布结果"),
                            handler: 'onPLPublisResult'
                        }]
                    }],
                    columns: msResGridColumns,
                    bbar: {
                        xtype: 'pagingtoolbar',
                        pageSize: 1000,
                        store: bmb_lcStudentsStore,
                        plugins: new Ext.ux.ProgressBarPager()
                    }
                });

                var onClickColumnsIndex=0;
                for(var i=0;i<msResGrid.columns.length;i++){
                    if(msResGrid.columns[i].dataIndex!=null||msResGrid.columns[i].dataIndex!=undefined){
                        if(msResGrid.columns[i].dataIndex.indexOf("#^")!=-1){
                            onClickColumnsIndex=i;
                            break;
                        }
                    }
                }
                msResGrid.on("cellclick",function( table,td, cellIndex, record, tr, rowIndex, e, eOpts ){
                    if(onClickColumnsIndex>0){
                        if(cellIndex>onClickColumnsIndex){
                            var lciddataindexarr = msResGrid.columns[cellIndex-1].dataIndex.split("#^");
                            var lciddataindex=lciddataindexarr[0];
                            var classId = msResGrid.store.getAt(rowIndex).data.bj_id;
                            var appId = msResGrid.store.getAt(rowIndex).data.bmb_id;
                            var bmlcId = lciddataindex;
                            Ext.tzSetCompResourses("TZ_BMGL_LCJGPUB_COM");
                            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_LCJGPUB_COM"]["TZ_PER_LCJGPUB_STD"];

                            if( pageResSet == "" || pageResSet == undefined){
                                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
                                return;
                            }
                            var className = pageResSet["jsClassName"];
                            if(className == "" || className == undefined){
                                Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，请检查配置。');
                                return;
                            }

                            var win = this.lookupReference('perLcjgPubWindow');
                            if (!win) {
                                Ext.syncRequire(className);
                                ViewClass = Ext.ClassManager.get(className);
                                win = new ViewClass({
                                    perLcjgCallBack:function(){
                                        msResGrid.store.reload();
                                    }
                                });
                                var record = grid.store.getAt(rowIndex);
                                var classID = classId;
                                var bmlc_id = bmlcId;
                                var bmb_id = appId;
                                var form = win.child('form').getForm();
                                var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
                                    recname: 'TZ_CLS_BMLCHF_T',
                                    condition:{
                                        TZ_CLASS_ID:{
                                            value:classID,
                                            operator:"01",
                                            type:"01"
                                        },
                                        TZ_APPPRO_ID:{
                                            value:bmlc_id,
                                            operator:"01",
                                            type:"01"
                                        }
                                    },
                                    result:'TZ_APPPRO_HF_BH,TZ_CLS_RESULT'
                                });
                                form.findField("jg_id").setStore(lm_mbStore);
                                win.on('afterrender',function(panel){
                                    var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGPUB_STD","OperateType":"QF","comParams":{"bj_id":"'+classID+'","bmlc_id":"'+bmlc_id+'","bmb_id":"'+bmb_id+'"}}';
                                    //"callback"'+this.testLqlc2()+'"
                                    Ext.tzLoad(tzParams,function(responseData){
                                        var formData = responseData.formData;
                                        form.setValues(formData);
                                    });
                                });
                                //this.getView().add(win);
                            }
                            win.show();
                        }
                    }
                })

                cmp.add(msResGrid);

                var lcfbStuListFormData={'classID':classID};
                var lcfbStuListForm = panel.child('form').getForm();
                lcfbStuListForm.setValues(lcfbStuListFormData);

                var Params= '{"bj_id":"'+classID+'"}';
                var panelGrid = panel.down('grid');
                panelGrid.store.tzStoreParams = Params;
                panelGrid.store.reload();
            });
        });
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    publishResult2:function(grid, rowIndex, colIndex) {
        //是否有访问权限
        /*var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_FB_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];*/

        var className = "KitchenSink.view.enrollmentManagement.bmb_lc.studentsList";
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_BMSH_FB_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        if (!Ext.ClassManager.isCreated(className)) {
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
                Ext.log.warn('Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        cmp = new ViewClass();
        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;


        cmp.on('afterrender', function (panel) {
            var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_FB_STD","OperateType":"tzLoadGridColumns","comParams":{"classID":"' + classID + '"}}';
             alert("zgw2");
             Ext.tzLoad(tzParams, function (responseData) {
                var msResGridColumns = [{
                    text: '报名表编号',
                    dataIndex: 'bmb_id',
                    filter: {
                        type: 'number',
                        fields: {
                            gt: {
                                iconCls: Ext.baseCSSPrefix + 'grid-filters-gt',
                                margin: '0 0 3px 0',
                                emptyText: '输入数字'
                            },
                            lt: {
                                iconCls: Ext.baseCSSPrefix + 'grid-filters-lt',
                                margin: '0 0 3px 0',
                                emptyText: '输入数字'
                            },
                            eq: {iconCls: Ext.baseCSSPrefix + 'grid-filters-eq', margin: 0, emptyText: '输入数字'}
                        }
                    },
                    minWidth: 125,
                    flex: 1
                }, {
                    text: '姓名',
                    dataIndex: 'ry_name',
                    minWidth: 100,
                    flex: 1,
                    filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: '搜索...'
                        }
                    }
                }, {
                    text: '国籍',
                    dataIndex: 'country',
                    minWidth: 100,
                    flex: 1,
                    filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: '搜索...'
                        }
                    }
                }, {
                    text: '查看前台公布内容',
                    align: 'center',
                    groupable: false,
                    width: 150,
                    renderer: function (v) {
                        return '<a href="javascript:void(0)">查看</a>';
                    },
                    listeners: {
                        click: 'sec_frontDeac'
                    }
                }];
                var bmlc_zd = responseData['bmlc_zd'];

                //LYY  2015-08-14  流程发布更改为单元格点击事件，不使用actioncolumn
                var lcfbLCJDdataIndexId = "";
                for (var i = 0; i < bmlc_zd.length; i++) {
                    lcfbLCJDdataIndexId = bmlc_zd[i]['bmlc_id'] + "#^" + bmlc_zd[i]['bmlc_name'];
                    msResGridColumns.push({
                        text: bmlc_zd[i]['bmlc_name'],
                        dataIndex: lcfbLCJDdataIndexId,
                        width: 170,
                        editable: false,
                        renderer: function (value) {
                            var _url1 = value.split(",");
                            if (value != "") {
                                return "<div class='x-colorpicker-field-swatch-inner' style='width:20%;height:50%;background-color: #" + _url1[0] + "'></div><div style='width:70%;margin-left:auto;'>" + _url1[1] + "</div>";
                            }
                        }
                    });
                }
                var bmb_lcStudentsStore = new KitchenSink.view.enrollmentManagement.bmb_lc.studentsStore();
                var msResGrid = Ext.create("Ext.grid.Panel", {
                    height: panel.getHeight() - 58,
                    frame: true,
                    name: 'student_listGr',
                    reference: 'student_listGr',
                    store: bmb_lcStudentsStore,
                    columnLines: true,    //显示纵向表格线
                    selModel: {
                        type: 'checkboxmodel'
                    },
                    plugins: [
                        {
                            ptype: 'gridfilters',
                            controller: 'appFormClass'
                        },
                        {
                            ptype: 'cellediting',
                            clicksToEdit: 1
                        }
                    ],
                    dockedItems: [{
                        xtype: "toolbar",
                        items: [{
                            text: "批量发布结果",
                            tooltip: "批量发布结果",
                            handler: 'onPLPublisResult'
                        }]
                    }],
                    columns: msResGridColumns,
                    bbar: {
                        xtype: 'pagingtoolbar',
                        pageSize: 1000,
                        store: bmb_lcStudentsStore,
                        plugins: new Ext.ux.ProgressBarPager()
                    }
                });

                var onClickColumnsIndex = 0;
                for (var i = 0; i < msResGrid.columns.length; i++) {
                    if (msResGrid.columns[i].dataIndex != null || msResGrid.columns[i].dataIndex != undefined) {
                        if (msResGrid.columns[i].dataIndex.indexOf("#^") != -1) {
                            onClickColumnsIndex = i;
                            break;
                        }
                    }
                }
                msResGrid.on("cellclick", function (table, td, cellIndex, record, tr, rowIndex, e, eOpts) {
                    if (onClickColumnsIndex > 0) {
                        if (cellIndex > onClickColumnsIndex) {
                            var lciddataindexarr = msResGrid.columns[cellIndex - 1].dataIndex.split("#^");
                            var lciddataindex = lciddataindexarr[0];
                            var classId = msResGrid.store.getAt(rowIndex).data.bj_id;
                            var appId = msResGrid.store.getAt(rowIndex).data.bmb_id;
                            var bmlcId = lciddataindex;
                            Ext.tzSetCompResourses("TZ_BMGL_LCJGPUB_COM");
                            var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_LCJGPUB_COM"]["TZ_PER_LCJGPUB_STD"];

                            if (pageResSet == "" || pageResSet == undefined) {
                                Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                                return;
                            }
                            var className = pageResSet["jsClassName"];
                            if (className == "" || className == undefined) {
                                Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，请检查配置。');
                                return;
                            }

                            var win = this.lookupReference('perLcjgPubWindow');
                            if (!win) {
                                Ext.syncRequire(className);
                                ViewClass = Ext.ClassManager.get(className);
                                win = new ViewClass({
                                    perLcjgCallBack: function () {
                                        msResGrid.store.reload();
                                    }
                                });
                                var record = grid.store.getAt(rowIndex);
                                var classID = classId;
                                var bmlc_id = bmlcId;
                                var bmb_id = appId;
                                var form = win.child('form').getForm();
                                var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
                                    recname: 'TZ_CLS_BMLCHF_T',
                                    condition: {
                                        TZ_CLASS_ID: {
                                            value: classID,
                                            operator: "01",
                                            type: "01"
                                        },
                                        TZ_APPPRO_ID: {
                                            value: bmlc_id,
                                            operator: "01",
                                            type: "01"
                                        }
                                    },
                                    result: 'TZ_APPPRO_HF_BH,TZ_CLS_RESULT'
                                });
                                form.findField("jg_id").setStore(lm_mbStore);
                                win.on('afterrender', function (panel) {
                                    var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGPUB_STD","OperateType":"QF","comParams":{"bj_id":"' + classID + '","bmlc_id":"' + bmlc_id + '","bmb_id":"' + bmb_id + '"}}';
                                    //"callback"'+this.testLqlc2()+'"
                                    Ext.tzLoad(tzParams, function (responseData) {
                                        var formData = responseData.formData;
                                        form.setValues(formData);
                                    });
                                });
                                //this.getView().add(win);
                            }
                            win.show();
                        }
                    }
                })

                cmp.add(msResGrid);

                var lcfbStuListFormData = {'classID': classID};
                var lcfbStuListForm = panel.child('form').getForm();
                lcfbStuListForm.setValues(lcfbStuListFormData);

                var Params = '{"bj_id":"' + classID + '"}';
                var panelGrid = panel.down('grid');
                panelGrid.store.tzStoreParams = Params;
                panelGrid.store.reload();
            });
        });
    }

});