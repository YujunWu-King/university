Ext.define('KitchenSink.view.clueManagement.clueManagement.clueDealWithController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.clueDealWithController',
    //线索退回、转交-选择责任人
    searchUser:function(btn) {
        var me = this,
            view = me.getView();
        var currentWin = btn.findParentByType("window");
        var form = btn.findParentByType("form").getForm();

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_ZRR_XZ_STD"];
        if(pageResSet==""||pageResSet==undefined) {
            Ext.MessageBox.alert('提示','您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet['jsClassName'];
        if(className==""||className==undefined) {
            Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID为：TZ_ZRR_XZ_STD，请检查配置。');
            return;
        }

        var win = me.lookupReference("personChooseWindow");
        if(!win) {
            Ext.syncRequire(className);
            var ViewClass = Ext.ClassManager.get(className);

            win = new ViewClass(currentWin);

            view.add(win);
            win.show();
        }
    },
    //线索退回-清除责任人
    clearUser:function(field) {
        field.setValue("");
        //人员ID也要置空
        var form = field.findParentByType("form").getForm();
        form.findField("backPersonOprid").setValue("");
    },
    //选择责任人-查询
    queryPerson:function(btn) {
        var grid=btn.findParentByType("grid");
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_XSXS_INFO_COM.TZ_ZRR_XZ_STD.TZ_XSXS_ZRR_VW',
            condition:{},
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                btn.findParentByType('grid').getStore().clearFilter();//查询基于可配置搜索，清除预设的过滤条件
                store.tzStoreParams = seachCfg;
                grid.searchCond=seachCfg;
                store.load();
            }
        });
    },
    //选择责任人-确定
    personChooseEnsure:function(btn) {
        var panel = btn.findParentByType("panel");
        var firstWin = panel.firstWin;

        if(firstWin.reference=="clueProblemPanel") {
            //问题线索
        } else {
            if(firstWin.reference=="clueImportWindow") {
                //招生线索管理导入线索，选择责任人
                var firstGrid = firstWin.down("grid");
            } else {
                var firstForm = firstWin.child("form").getForm();
            }
        }

        var grid = panel.child("grid");
        var selectRecords = grid.getSelectionModel().getSelection();
        var selectLength = selectRecords.length;

        if(selectLength==0) {
            Ext.Msg.alert("提示","您没有选中任何记录");
            return;
        } else if(selectLength>1) {
            Ext.Msg.alert("提示","只能选择一条记录");
            return;
        } else {
            var oprid = selectRecords[0].data.oprid;
            var name = selectRecords[0].data.name;

            if(firstWin.reference == "clueBackWindow") {
                firstForm.findField("backPersonOprid").setValue(oprid);
                firstForm.findField("backPersonName").setValue(name);
            }
            if(firstWin.reference == "clueAssignResponsibleWindow") {
                firstForm.findField("chargeOprid").setValue(oprid);
                firstForm.findField("chargeName").setValue(name);
            }
            if(firstWin.reference=="clueImportWindow") {
                //招生线索管理导入线索
                var selList = firstGrid.getSelectionModel().getSelection();
                selList[0].set('chargeOprid',oprid);
                selList[0].set('chargeName',name);
            }
            if(firstWin.reference == "clueProblemPanel") {
                //问题线索
                var selList = firstWin.getSelectionModel().getSelection();
                selList[0].set('chargeOprid',oprid);
                selList[0].set('chargeName',name);
            }

            panel.close();
        }
    },
    //线索退回-保存
    onClueBackReasonSave:function(btn) {
        var panel = this.getView();
        var fromType = panel.fromType;
        var form = panel.child("form").getForm();
        if(form.isValid()){
            //主要表单
            var form = this.getView().child('form').getForm();
            //表单数据
            var formParams = form.getValues();
            var formJson =  Ext.JSON.encode(formParams);
            //更新操作参数
            var comParams = "";
            //修改json字符串
            var  editJson = '{"typeFlag":"BACK","data":'+Ext.JSON.encode(formParams)+'}';
            comParams = '"update":[' + editJson + "]";
            //提交参数
            var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"U","comParams":{'+comParams+'}}';
            if(tzParams!="") {
                Ext.tzSubmit(tzParams,function(responseData){
                    var detailPanel = panel.findParentByType("panel");
                    //重新加载线索详情页面数据
                    if(btn.name=="clueBackReasonEnsureBtn"){
                        panel.close();
                        if(fromType!="" && fromType!=undefined) {

                        } else {
                            //线索详情页面进入的，关闭
                            detailPanel.close();
                        }
                    }else{
                        var detailForm=panel.findParentByType("panel").down("form").getForm();
                        var clueId = detailForm.findField('clueId').getValue();
                        var tzParamsDetail = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"QF","comParams":{"clueId":"'+clueId+'"}}';
                        Ext.tzLoad(tzParamsDetail,function(respData){
                            var formData = respData.formData;
                            //基本信息
                            detailForm.setValues(formData);

                            //退回原因
                            detailForm.findField('backReasonId').setVisible(true);
                            //关闭原因
                            detailForm.findField('closeReasonId').setVisible(false);
                            //建议跟进日期
                            detailForm.findField('contactDate').setVisible(false);
                            //修改页面状态
                            var clueState = detailForm.findField('clueState').getValue();
                            form.findField('clueStatePage').setValue(clueState);
                        });
                    }

                    /*刷新我的线索列表或者线索管理列表*/
                    var panelMyEnrollMentClue;
                    var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
                    panelMyEnrollMentClue = contentPanel.child("myEnrollmentClue");
                    if(panelMyEnrollMentClue!=undefined && panelMyEnrollMentClue!=null)
                    {
                        panelMyEnrollMentClue.store.reload();
                    }
                    var panelEnrollMentClue = contentPanel.child("enrollmentClue");
                    if(panelEnrollMentClue!=undefined && panelEnrollMentClue!=null)
                    {
                        panelEnrollMentClue.store.reload();
                    }
                },"",true,this);
            }
        }
    },
    //线索关闭-保存
    onClueCloseReasonSave:function(btn){
        var panel = this.getView();
        var fromType = panel.fromType;
        var form = panel.child("form").getForm();
        if(form.isValid()){
            //主要表单
            var form = this.getView().child('form').getForm();
            //表单数据
            var formParams = form.getValues();
            var formJson =  Ext.JSON.encode(formParams);
            //更新操作参数
            var comParams = "";
            //修改json字符串
            var  editJson = '{"typeFlag":"CLOSE","data":'+Ext.JSON.encode(formParams)+'}';
            comParams = '"update":[' + editJson + "]";
            //提交参数
            var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"U","comParams":{'+comParams+'}}';
            if(tzParams!="") {
                Ext.tzSubmit(tzParams,function(responseData){
                        //重新加载线索详情页面数据
                        var detailPanel = panel.findParentByType("panel");
                        if(btn.name=="clueCloseReasonEnsureBtn"){
                            panel.close();
                            if(fromType!="" && fromType!=undefined) {

                            } else {
                                //线索详情页面进入的，关闭
                                detailPanel.close();
                            }
                        }
                        /*刷新我的线索列表或者线索管理列表*/
                        var panelMyEnrollMentClue;
                        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
                        panelMyEnrollMentClue = contentPanel.child("myEnrollmentClue");
                        if(panelMyEnrollMentClue!=undefined && panelMyEnrollMentClue!=null)
                        {
                            panelMyEnrollMentClue.store.reload();
                        }
                        var panelEnrollMentClue = contentPanel.child("enrollmentClue");
                        if(panelEnrollMentClue!=undefined && panelEnrollMentClue!=null)
                        {
                            panelEnrollMentClue.store.reload();
                        }

                },"",true,this);
            }
        }
    },
    //线索转交-确定
    onClueAssignSave:function(btn) {
        var panel = this.getView();
        var fromType = panel.fromType;
        var form = panel.child("form").getForm();
        if(form.isValid()){
            //主要表单
            var form = this.getView().child('form').getForm();
            //表单数据
            var formParams = form.getValues();
            var formJson =  Ext.JSON.encode(formParams);
            //更新操作参数
            var comParams = "";
            //修改json字符串
            var  editJson = '{"typeFlag":"GIVE","data":'+Ext.JSON.encode(formParams)+'}';
            comParams = '"update":[' + editJson + "]";
            //提交参数
            var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"U","comParams":{'+comParams+'}}';
            if(tzParams!="") {
                Ext.tzSubmit(tzParams,function(responseData){

                    var detailPanel=panel.findParentByType("panel");
                    if(btn.name=="clueAssignEnsureBtn"){
                        panel.close();
                        if(fromType!="" && fromType!=undefined) {

                        } else {
                            //线索详情页面进入的，关闭
                            detailPanel.close();
                        }
                    }else{
                        //重新加载线索详情页面数据
                        var detailForm=panel.findParentByType("panel").down("form").getForm();
                        var clueId = detailForm.findField('clueId').getValue();
                        var tzParamsDetail = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"QF","comParams":{"clueId":"'+clueId+'"}}';
                        Ext.tzLoad(tzParamsDetail,function(respData){
                            var formData = respData.formData;
                            //基本信息
                            detailForm.setValues(formData);

                            var clueState = detailForm.findField('clueState').getValue();
                            var chargeOprid = detailForm.findField('chargeOprid').getValue();

                            detailForm.findField('backReasonId').setVisible(false);
                            detailForm.findField('closeReasonId').setVisible(false);
                            detailForm.findField('contactDate').setVisible(false);
                            //退回原因
                            if(clueState=="F") {
                                detailForm.findField('backReasonId').setVisible(true);
                            }
                            //关闭原因
                            if(clueState=="G") {
                                detailForm.findField('closeReasonId').setVisible(true);
                            }
                            //建议跟进日期
                            if(clueState=="D") {
                                detailForm.findField('contactDate').setVisible(true);
                            }
                            form.findField('chargeOpridPage').setValue(chargeOprid);
                            form.findField('clueStatePage').setValue(clueState);
                        });
                    }

                    /*刷新我的线索列表或者线索管理列表*/
                    var panelMyEnrollMentClue;
                    var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
                    panelMyEnrollMentClue = contentPanel.child("myEnrollmentClue");
                    if(panelMyEnrollMentClue!=undefined && panelMyEnrollMentClue!=null)
                    {
                        panelMyEnrollMentClue.store.reload();
                    }
                    var panelEnrollMentClue = contentPanel.child("enrollmentClue");
                    if(panelEnrollMentClue!=undefined && panelEnrollMentClue!=null)
                    {
                        panelEnrollMentClue.store.reload();
                    }
                },"",true,this);
            }
        }
    },
    //延迟联系-保存确定
    onClueDelayContactSave:function(btn){
        var panel = this.getView();
        var fromType = panel.fromType;
        var form = panel.child("form").getForm();
        if(form.isValid()){
            //表单数据
            var formParams = form.getValues();
            var formJson =  Ext.JSON.encode(formParams);
            //更新操作参数
            var comParams = "";
            //修改json字符串
            var  editJson = '{"typeFlag":"DELAY","data":'+Ext.JSON.encode(formParams)+'}';
            comParams = '"update":[' + editJson + "]";
            //提交参数
            var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"U","comParams":{'+comParams+'}}';
            if(tzParams!="") {
                Ext.tzSubmit(tzParams,function(responseData){

                    var detailPanel=panel.findParentByType("panel");
                    if(btn.name=="clueDelayContactEnsureBtn"){
                        panel.close();
                        if(fromType!="" && fromType!=undefined) {

                        } else {
                            //线索详情页面进入的，关闭
                            detailPanel.close();
                        }
                    } else {
                        //重新加载线索详情页面数据
                        var detailForm=panel.findParentByType("panel").down("form").getForm();
                        var clueId = detailForm.findField('clueId').getValue();
                        var tzParamsDetail = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"QF","comParams":{"clueId":"' + clueId + '"}}';
                        Ext.tzLoad(tzParamsDetail, function (respData) {
                            var formData = respData.formData;
                            //基本信息
                            detailForm.setValues(formData);
                            /*修改页面状态*/
                            var clueState = detailForm.findField('clueState').getValue();
                            if (btn.name == "clueDelayContactSaveBtn") {
                                form.findField('clueStatePage').setValue(clueState);
                            }
                        });
                    }
                    /*刷新我的线索列表或者线索管理列表*/
                    var panelMyEnrollMentClue;
                    var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
                    panelMyEnrollMentClue = contentPanel.child("myEnrollmentClue");
                    if(panelMyEnrollMentClue!=undefined && panelMyEnrollMentClue!=null)
                    {
                        panelMyEnrollMentClue.store.reload();
                    }
                    var panelEnrollMentClue = contentPanel.child("enrollmentClue");
                    if(panelEnrollMentClue!=undefined && panelEnrollMentClue!=null)
                    {
                        panelEnrollMentClue.store.reload();
                    }

                },"",true,this);
            }
        }
    },
    //线索处理关闭
    onClueDealWithClose:function(btn){
        btn.findParentByType("window").close();
    },
    //查询可关联的报名表
    queryClueBmb:function(btn){
        var grid=btn.findParentByType("clueRelBmbWindow").child("grid");
        var form=btn.findParentByType("clueRelBmbWindow").child("form").getForm();
        var resultDesc=form.findField("resultDesc");
        var store=grid.store;
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_XSXS_INFO_COM.TZ_XSXS_BMB_STD.TZ_CLUE_BMB_VW', //这里面的组件页面视图需要换成自己的
            condition:
            {
                "TZ_JG_ID":Ext.tzOrgID
            },
            callback: function(seachCfg){
                store.tzStoreParams = seachCfg;
                store.load({
                    callback:function(records, operation, success){
                        var count=store.getTotalCount();
                        if(count==0){
                            grid.setHidden(true);
                            resultDesc.setValue(Ext.tzGetResourse("TZ_XSXS_INFO_COM.TZ_XSXS_BMB_STD.notExistBmb","系统按上述客户信息，没有查到可关联的报名表！您可以点击【查询】继续查找"));
                        }else{
                            grid.setHidden(false);
                            resultDesc.setValue(Ext.tzGetResourse("TZ_XSXS_INFO_COM.TZ_XSXS_BMB_STD.existBmb","系统按上述客户信息，查询到以下报名表可以关联！"));
                        }
                        Ext.resumeLayouts(true);
                    }
                });

            }
        });
    },
    //选择关联的报名表
    clueRelBmbWindowEnsure:function(btn){
        var grid=btn.findParentByType("clueRelBmbWindow").child("grid");
        var selList=grid.getSelectionModel().getSelection();
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要关联的报名表");
            return;
        }else{
            if(checkLen>1){
                Ext.Msg.alert("提示","只能关联一条报名表");
                return;
            }
        }
        //点击确定关联报名表
        var bmbId=selList[0].data.bmbId;
        var bmbClueId=selList[0].data.bmbClueId;
        if(bmbClueId!=null && bmbClueId!="") {
            Ext.Msg.alert("提示","此报名表已经关联了销售线索");
            return;
        } else {
            var form = btn.findParentByType("clueRelBmbWindow").child("form").getForm();
            var clueId = form.findField("clueId").getValue();
            var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_BMB_STD","OperateType":"U","comParams":{"add":[{"bmbId":"' + bmbId + '","clueId":"' + clueId + '"}]}}';
            Ext.tzSubmit(tzParams, function (response) {
                btn.findParentByType("clueRelBmbWindow").close();
                //返回报考状态
                var bkStatus = response.bkStatus;
                //父页面刷新
                var clueDetailPanel;
                var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
                clueDetailPanel = contentPanel.child("clueDetailPanel");
                if (clueDetailPanel != undefined && clueDetailPanel != null) {
                    clueDetailPanel.child("grid").store.reload();
                    var glBmbBut = clueDetailPanel.down("button[name=glBmbBut]");
                    glBmbBut.setVisible(false);
                    //更新页面报考状态
                    clueDetailPanel.child("form").getForm().findField("bkStatus").setValue(bkStatus);
                }
            }, "", true, this);
        }
    },
    //查看报名表
    viewBmbDetail:function(grid,rowIndex){
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }

        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        var classID=record.get("classId");
        var oprID=record.get("oprid");
        var appInsID=record.get("bmbId");

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
