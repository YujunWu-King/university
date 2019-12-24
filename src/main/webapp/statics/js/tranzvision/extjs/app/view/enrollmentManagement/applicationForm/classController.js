﻿Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.classController', {
    extend: 'Ext.app.ViewController',
    requires:['Ext.ux.IFrame'],
    alias: 'controller.appFormClass',
    queryClass:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.TZ_BMBSH_ECUST_VW',
            condition:{
                TZ_DLZH_ID:TranzvisionMeikecityAdvanced.Boot.loginUserId,
                TZ_JG_ID:Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    queryStudents:function(btn){
        var store = btn.findParentByType("grid").store;

        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.TZ_APP_LIST_VW',
            condition:{
                TZ_CLASS_ID:store.classID,
                TZ_BATCH_ID:store.batchID
            },
            callback: function(seachCfg){
                var tzStoreParams = Ext.decode(seachCfg);
                tzStoreParams.classID = store.classID;
                tzStoreParams.batchID = store.batchID;
                store.tzStoreParams = Ext.encode(tzStoreParams);
                var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_UM_GETSQL_STD","OperateType":"getQuerySQL","comParams":' + seachCfg + '}';

                Ext.tzLoadAsync(tzParams, function (responseData) {


                    var getedSQL = responseData.SQL;

                    window.getedSQL2 = responseData.SQL;
                    console.log(getedSQL2);

               
            });
                store.load();
        }
        });
    },
    onStuInfoSave: function(btn){
        //学生信息列表
        var grid = this.getView().child("grid");
        //学生信息数据
        var store = grid.getStore();
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取学生列表参数
            var tzParams = this.getStuInfoParams();
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    store.commitChanges();
                },"",true,this);
            }
        }
    },
    onStuInfoEnsure: function(btn){
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取学生列表参数
            var tzParams = this.getStuInfoParams();
            var comView = this.getView();
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    comView.close();
                },"",true,this);
            }else{
                comView.close();
            }
        }
    },
    getStuInfoParams: function(){
        //更新操作参数
        var comParams = "";
        //学生信息列表
        var grid = this.getView().child("grid");
        //学生信息数据
        var store = grid.getStore();
        //修改json字符串
        var editJson = "";
        var mfRecs = store.getModifiedRecords();
        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        //提交参数
        var tzParams="";
        if(comParams!=""){
            tzParams= '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"U","comParams":{'+comParams+'}}';
        }
        return tzParams;
    },
    onStuInfoClose: function(btn){
        //关闭窗口
        this.getView().close();
    },
    viewApplicants:function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_STU_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        //alert(ViewClass)
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
        window.classID2=record.data.classID;
        window.batchID2=record.data.batchID;
        var render = function(initialData){
        	
            cmp = new ViewClass({
                    initialData:initialData,
                    classID:classID,
                    batchID:batchID
                }
            );
            cmp.on('afterrender',function(panel){
                var form = panel.child('form').getForm();
                var panelGrid = panel.child('grid');
                panelGrid.getView().on('expandbody', function (rowNode, record, expandRow, eOpts){
                    if(!record.get('moreInfo')){
                        var appInsID = record.get('appInsID');
                        var tzExpandParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzLoadExpandData","comParams":{"classID":"'+classID+'","appInsID":"'+appInsID+'"}}';
                        Ext.tzLoad(tzExpandParams,function(respData){
                                if(panelGrid.getStore().getModifiedRecords().length>0){
                                    record.set('moreInfo',respData);
                                }else{
                                    record.set('moreInfo',respData);
                                    panelGrid.getStore().commitChanges( );
                                }
                            },panelGrid
                        );
                    }
                });
                var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
                Ext.tzLoad(tzParams,function(respData){
                    var formData = respData.formData;
                    form.setValues(formData);

                    var tzStoreParams = {
                        "classID":classID,
                        "batchID":batchID,
                        "cfgSrhId": "TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.TZ_APP_LIST_VW",
                        "condition":{
                            "TZ_CLASS_ID-operator": "01",
                            "TZ_CLASS_ID-value": classID,
                            "TZ_BATCH_ID-operator": "01",
                            "TZ_BATCH_ID-value": batchID
                        }
                    };
                    panelGrid.store.classID=classID;
                    panelGrid.store.batchID=batchID;
                    panelGrid.store.tzStoreParams = Ext.encode(tzStoreParams);
                    panelGrid.store.load();
                });
            });

            tab = contentPanel.add(cmp);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
        };


        var submitStateStore = new KitchenSink.view.common.store.appTransStore("TZ_APPFORM_STATE"),
            auditStateStore = new KitchenSink.view.common.store.appTransStore("TZ_AUDIT_STATE"),
            interviewResultStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_RESULT"),
            orgColorSortStore = new KitchenSink.view.common.store.comboxStore({
                recname:'TZ_ORG_COLOR_V',
                condition:{
                    TZ_JG_ID:{
                        value:Ext.tzOrgID,
                        operator:'01',
                        type:'01'
                    }},
                result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
            });

        //下拉项过滤器数据
        var colorSortFilterOptions=[],
            submitStateFilterOptions=[],
            auditStateFilterOptions=[],
            interviewResultFilterOptions=[];

        //颜色类别初始化数据-学生颜色类别列渲染数据
        var initialColorSortData=[];

        //4个下拉控件Store加载完毕之后打开页面
        var times = 4;
        var beforeRender = function(){
            times--;
            if(times==0){
                render({
                    submitStateStore:submitStateStore,
                    auditStateStore:auditStateStore,
                    interviewResultStore:interviewResultStore,
                    orgColorSortStore:orgColorSortStore,
                    colorSortFilterOptions:colorSortFilterOptions,
                    submitStateFilterOptions:submitStateFilterOptions,
                    auditStateFilterOptions:auditStateFilterOptions,
                    interviewResultFilterOptions:interviewResultFilterOptions,
                    initialColorSortData:initialColorSortData
                });
            }
        };

        orgColorSortStore.on("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                initialColorSortData.push(records[i].data);
                colorSortFilterOptions.push([records[i].data.TZ_COLOR_SORT_ID,records[i].data.TZ_COLOR_NAME]);
            }
            beforeRender();
        });
        submitStateStore.on("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                submitStateFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
            }
            beforeRender();
        });
        auditStateStore.on("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                auditStateFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
            }
            beforeRender();
        });
        interviewResultStore.on("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                interviewResultFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
            }
            beforeRender();
        });

    },
    //报名流程结果公布（LZ添加）
    publishResult:function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_FB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
                                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
    //添加结束LZ

    auditApplicationForm:function(grid, rowIndex, colIndex){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_AUDIT_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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

        if(this.getView().name=="applicationFormWindow"){
            classID = this.getView().classID;
            oprID = this.getView().oprID;
            appInsID= this.getView().appInsID;
            record= this.getView().gridRecord;
            this.getView().close();
        }else{
            record = grid.store.getAt(rowIndex);
            classID = record.data.classID;
            oprID = record.data.oprID;
            appInsID= record.data.appInsID;
        }

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
                    cmp = new ViewClass({appInsID:appInsID,applicationFormTagStore:applicationFormTagStore,gridRecord:record});
                    cmp.on('afterrender',function(panel){
                        var form = panel.child('form').getForm();
                        var tabpanel = panel.child("tabpanel");
                        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_AUDIT_STD","OperateType":"QF","comParams":{"classID":"'+classID+'","oprID":"'+oprID+'"}}';
                        Ext.tzLoad(tzParams,function(respData){
                            var formData = respData.formData;
                            form.setValues(formData);
                            tabpanel.down('form[name=remarkForm]').getForm().setValues(formData);
                            tabpanel.down('form[name=contactInfoForm]').getForm().setValues(formData);
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
    
    viewApplicationForm: function(grid, rowIndex,colIndex){
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }

        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        var classID=record.get("classID");
        var oprID=record.get("oprID");
        var appInsID=record.get("appInsID");
        console.log(appInsID);
        if(appInsID!=""){
            var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appInsID+'","isEdit":"Y"}}';
            var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+encodeURIComponent(tzParams);
            var mask ;
            var win = new Ext.Window({
                name:'applicationFormWindow',
                title : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.viewApplicationForm","查看报名表"),
                maximized : true,
                controller:'appFormClass',
                classID :classID,
                oprID :oprID,
                appInsID : appInsID,
                gridRecord:record,
                width : Ext.getBody().width,
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
                buttons: [ {
                    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.audit","审批"),
                    iconCls:"send",
                    handler: "auditApplicationForm"
                },
                    {
                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
                        iconCls:"close",
                        handler: function(){
                            win.close();
                        }
                    }]
            })
            win.show();
        }else{
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.cantFindAppForm","找不到该报名人的报名表"));
        }
    },
    /*材料打包和下载*/
    packageAndDownload:function(btn){
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        if(selList.length<1) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
            return;
        }else{
            var strAppId;
            for (var i = 0; i < selList.length; i++) {
                if (i>0) {
                    strAppId=selList[i].get('appInsID')+";"+strAppId
                }else{
                    strAppId=selList[i].get('appInsID')+";"
                }

            }
        }
        // Ext.MessageBox.alert('测试',strAppId);
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_DBDL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('cldbForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "add";
        var form = win.child("form").getForm();
        form.reset();
        form.setValues({appInsID:"'"+strAppId+"'"});
        win.show();

    },
    /*将搜索结果考生材料批量打包*/
    packageLetAndDownload:function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var data = store.getData();
        var dataLen = data.length;

        if(dataLen.length<1) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
            return;
        }else{
            var strAppId;
            var totalCount = store.totalCount;

            var classId = this.getView().classID;
            var batchId = this.getView().batchID;

            var tzStoreParams = store.tzStoreParams;
            var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzExportAll","comParams":{"classId":"' + classId + '","batchId":"' + batchId + '","tzStoreParams":' + Ext.JSON.encode(tzStoreParams) + ',"totalCount":"' + totalCount + '"}}';
            Ext.tzLoadAsync(tzParams,function(responseData){
                strAppId = responseData.result;
            });
        }
        // Ext.MessageBox.alert('测试',strAppId);
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_DBDL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('cldbForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "add";
        var form = win.child("form").getForm();
        form.reset();
        form.setValues({appInsID:"'"+strAppId+"'",packageType:"B"});
        win.show();
    },
    /*将搜索结果考生材料批量打包*/
    packageAllAndDownload:function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var data = store.getData();
        var dataLen = data.length;

        if(dataLen.length<1) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
            return;
        }else{
            var strAppId;
            var totalCount = store.totalCount;

            var classId = this.getView().classID;
            var batchId = this.getView().batchID;

            var tzStoreParams = store.tzStoreParams;
            var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzExportAll","comParams":{"classId":"' + classId + '","batchId":"' + batchId + '","tzStoreParams":' + Ext.JSON.encode(tzStoreParams) + ',"totalCount":"' + totalCount + '"}}';
            Ext.tzLoadAsync(tzParams,function(responseData){
                strAppId = responseData.result;
            });
        }
        // Ext.MessageBox.alert('测试',strAppId);
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_DBDL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('cldbForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "add";
        var form = win.child("form").getForm();
        form.reset();
        form.setValues({appInsID:"'"+strAppId+"'",packageType:"A"});
        win.show();
    },
    /*将搜索结果考生复试材料批量打包*/
    packageFSZL:function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var data = store.getData();
        var dataLen = data.length;

        if(dataLen.length<1) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
            return;
        }else{
            var strAppId;
            var totalCount = store.totalCount;

            var classId = this.getView().classID;
            var batchId = this.getView().batchID;

            var tzStoreParams = store.tzStoreParams;
            var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzExportAll","comParams":{"classId":"' + classId + '","batchId":"' + batchId + '","tzStoreParams":' + Ext.JSON.encode(tzStoreParams) + ',"totalCount":"' + totalCount + '"}}';
            Ext.tzLoadAsync(tzParams,function(responseData){
                strAppId = responseData.result;
            });
        }
        // Ext.MessageBox.alert('测试',strAppId);
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_DBDL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('cldbForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为新增
        win.actType = "add";
        var form = win.child("form").getForm();
        form.reset();
        form.setValues({appInsID:"'"+strAppId+"'",packageType:"C"});
        win.show();
    },
    viewAndDownload:function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_DBDL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_MSGL_MSFZ_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('cldbForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        //操作类型设置为更新
        win.actType = "update";
        //var comRegParams = this.getView().child("form").getForm().getValues();
        var tabPanel = win.lookupReference("packageTabPanel");
        tabPanel.setActiveTab(1);
        win.show();
    },
    /*导出到Excel or 下载导出结果*/
    exportExcelOrDownload:function(btn){
        var btnName = btn.name;
        var selList = [];

        //导出选中的人员信息到Excel
        if(btnName=='exportExcel'){
            var selection = btn.findParentByType("grid").getSelectionModel().getSelection();
            if(selection.length<1) {
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
                return;
            }else{
                Ext.each(selection,function(item,index){
                    selList.push(item.get("appInsID"));
                })
            };
        }

        //导出搜索的人员信息到Excel
        if(btnName=='exportSearch'){
            var store = btn.findParentByType("grid").getStore();
            if(store.totalCount<1) {
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.noRecordsNeedsExport","没有需要导出的数据！"));
                return;
            }else{
                var classId = this.getView().classID;
                var batchId = this.getView().batchID;

                var tzStoreParams = store.tzStoreParams;
                var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzExportAll","comParams":{"classId":"' + classId + '","batchId":"' + batchId + '","tzStoreParams":' + Ext.JSON.encode(tzStoreParams) + ',"totalCount":"' + store.totalCount + '"}}';
                Ext.tzLoadAsync(tzParams,function(responseData){
                    selList = responseData.result&&responseData.result.split(";");
                });
            };
        }


        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_EXP_EXCEL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限") );
            return;
        }

        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var win = this.lookupReference('exportExcelForm');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            var modalID =btn.findParentByType('auditClassInfo').child('form').getForm().findField('modalID').getValue();
            win = new ViewClass(modalID);
            this.getView().add(win);
        };
        win.selList=selList;

        //
        if(btnName=='downloadExcel'){
            var tabPanel = win.lookupReference("exportExcelTabPanel");
            tabPanel.setActiveTab(1);
        }
        var form = win.child("form").getForm();
        form.reset();
        win.show();
    },
    printAppForm:function(obj,rowIndex,colIndex){
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_PRINT_ADMIN_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        var appInsID;
        if(!obj.store){
            var selList = obj.findParentByType("grid").getSelectionModel().getSelection();

            if(selList.length<1) {
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
                return;
            }
            if(selList.length>1) {
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.canSelectOneOnly","只能选中一条记录"));
                return;
            }
            appInsID =selList[0].get('appInsID');
        }else{
            appInsID = obj.getStore().getAt(rowIndex).get('appInsID');
        }

        if(appInsID!=""){
            //var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_PRINT_ADMIN_STD","OperateType":"HTML","comParams":{"appInsID":"'+appInsID+'"}}';
            //var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+tzParams;

            //html转pdf的;
            //var viewUrl = TzUniversityContextPath+"/admission/expform/"+appInsID;
            //window.location.href=viewUrl;

            //var url = TzUniversityContextPath + "/DownPdfServlet?instanceID="+appInsID;
            //window.open(url, '_blank');

            var url = TzUniversityContextPath + "/PrintPdfServlet?instanceID="+appInsID;
            window.open(url, '_blank');
        }else{
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.cantFindAppForm","找不到该报名人的报名表"));
        }

    },
    /*黑名单管理*/
    addHmd:function(btn){
        var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
        if(selList.length<1) {
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要將所选账户加入黑名单吗?', function(btnId){
                if(btnId == 'yes'){
                    var strOprId;
                    for (var i = 0; i < selList.length; i++) {
                        strOprId = selList[i].get('oprID');
                        if (i>0) {
                            updateJson = updateJson + ','+'{"OPRID":"' + strOprId + '"}';
                        }else{
                            updateJson = '{"OPRID":"' + strOprId + '"}';
                        }

                    }
                    var comParams = "";
                    if(updateJson != ""){
                        comParams = '"update":[' + updateJson + "]";
                    }
                    //提交参数
                    var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERMG_STD","OperateType":"U","comParams":{'+comParams+'}}';
                    //保存数据
                    Ext.tzSubmit(tzParams,function(){
                        //无须刷新页面数据
                        //store.reload();
                    },"",true,this);
                }
            });
        }

    },
    /*搜索结果另存为听众*/
    saveAsDynAud: function () {


        var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{"add":[{"audJG":"ADMIN","audID":"NEXT","audName":"","audStat":"1","audType":"2","audMS":"","audSQL":"","audLY":"ZCYH"}]}}';

        //后台执行插入表操作
        var AudID = "";
        Ext.tzLoadAsync(tzParams, function (resp) {
            AudID = resp;

        }, "", true, this);


        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_UM_AUDDYN_STD"];

        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];


        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_UM_AUDDYN_STD，请检查配置。');
            return;
        }

        var JGID = Ext.tzOrgID;


   //     var OriSQL = "SELECT OPRID FROM PS_TZ_APP_LIST_VW where TZ_JG_ID='" + JGID + "'";
        var OriSQL = "SELECT OPRID FROM PS_TZ_APP_LIST_VW where TZ_CLASS_ID='" + classID2 + "' and TZ_BATCH_ID='" +batchID2+ "'";
        console.log(OriSQL);
        if ((typeof getedSQL2) == "undefined") {

            getedSQL2 = OriSQL;
        }


        var win = this.lookupReference('pageRegWindow');

        if (!win) {

            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
        console.log(win);
        win.actType = "update";

        var audId = AudID;


        var audName = "";
        var audStat = "1";
        var audType = "1";
        var audMS = "";
        var audSQL = getedSQL2;


        //参数
        var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"' + audId + '","audName":"' + audName + '","audStat":"' + audStat + '","audType":"' + audType + '","audMS":"' + audMS + '","audSQL":"' + audSQL + '"}}';
        //页面注册信息表单
        console.log(tzParams);
        var form1 = win.child("form");
        console.log(form1);
        var form=form1.getForm();

        var gridStore = win.child("form").child("grid").getStore();
        var tzStoreParams = '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "' + audId + '"}}';

        Ext.tzLoad(tzParams, function (responseData) {

            console.log(responseData);
            form.setValues(responseData);
            gridStore.tzStoreParams = tzStoreParams;
            gridStore.reload();

        });


        win.show();

    },

    /*关闭窗口*/
    onPageRegClose: function (btn) {
        var win = btn.findParentByType("window");
        win.close();
    },
    onPageRegSave1: function (btn) {
        //获取窗口
        var win = btn.findParentByType("window");
        //页面注册信息表单
        var form = win.child("form").getForm();
        if (form.isValid()) {
            /*保存页面注册信息*/

            this.savePageRegInfo1(win);
        }
    },
    savePageRegInfo1: function (win, view) {

        //信息表单
        var form = win.child("form").getForm();

        var formParams = form.getValues();
        var audSQL = formParams["audSQL"];
        var audID = formParams["audID"];


        var gridStore = win.child("form").child("grid").getStore();
        var selList = win.child("form").child("grid").getSelectionModel().getSelection();

        var removeJson = "";
        //删除记录
        var removeRecs = gridStore.getRemovedRecords();

        for (var i = 0; i < removeRecs.length; i++) {
            if (removeJson == "") {
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            } else {
                removeJson = removeJson + ',' + Ext.JSON.encode(removeRecs[i].data);
            }
        }
        ;

        var comParams = "";
        if (removeJson != "") {
            comParams = '"delete":[' + removeJson + "]";
        }

        var tzParams2 = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{' + comParams + '}}';

        //保存数据
        if (comParams != "") {
            Ext.tzSubmit(tzParams2, function () {

            }, "", true, this);
        }


        //表单数据
        var comParamsALL = '"update":[{"typeFlag":"FORM","data":' + Ext.JSON.encode(form.getValues()) + '}]';

        var actType = win.actType;


        //表格数据
        var updateJson = "";
        var updateRecs = gridStore.getUpdatedRecords();


        for (var i = 0; i < updateRecs.length; i++) {
            if (updateJson == "") {

                updateJson = '{"typeFlag":"GRID","data":' + Ext.JSON.encode(updateRecs[i].data) + '}';

            } else {

                updateJson = updateJson + ',{"typeFlag":"GRID","data":' + Ext.JSON.encode(updateRecs[i].data) + '}';

            }
        }
        ;

        var comParams3 = "";
        if (updateJson != "") {

            comParamsALL = comParamsALL + ',"update":[' + updateJson + "]";

        }


        //提交参数

        var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{' + comParamsALL + '}}';

        var pageGrid = this.getView();

        Ext.tzSubmit(tzParams, function (resp) {
            win.actType = "update";

        }, "", true, this);
    },
    
    onPageRegEnsure1: function (btn) {
        //获取窗口
        var win = btn.findParentByType("window");
        //页面注册信息表单
        var form = win.child("form").getForm();
        if (form.isValid()) {
            /*保存页面注册信息*/
            this.savePageRegInfo1(win);
            //重置表单
            form.reset();
            //关闭窗口
            win.close();
        }
    },

    /*保存并关闭窗口*/
    onPageRegEnsure: function (btn) {
        //获取窗口
        var win = btn.findParentByType("window");
        //页面注册信息表单
        var form = win.child("form").getForm();
        if (form.isValid()) {
            /*保存页面注册信息*/
            this.savePageRegInfo(win);
            //重置表单
            form.reset();
            //关闭窗口
            win.close();
        }
    },
    savePageRegInfo: function (win, view) {

        //信息表单
        var form = win.child("form").getForm();

        var formParams = form.getValues();
        var audSQL = formParams["audSQL"];
        var audID = formParams["audID"];

        if (audSQL != "") {

            var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"tzOther","comParams":{"audSQL":"' + audSQL + '","audID":"' + audID + '"}}';

            Ext.tzLoad(tzParams, function (responseData) {
            });
        }


        var gridStore = win.child("form").child("grid").getStore();
        var selList = win.child("form").child("grid").getSelectionModel().getSelection();

        var removeJson = "";
        //删除记录
        var removeRecs = gridStore.getRemovedRecords();

        for (var i = 0; i < removeRecs.length; i++) {
            if (removeJson == "") {
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            } else {
                removeJson = removeJson + ',' + Ext.JSON.encode(removeRecs[i].data);
            }
        }
        

        var comParams = "";
        if (removeJson != "") {
            comParams = '"delete":[' + removeJson + "]";
        }

        var tzParams2 = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{' + comParams + '}}';

        //保存数据
        if (comParams != "") {
            Ext.tzSubmit(tzParams2, function () {

            }, "", true, this);
        }


        //表单数据
        var comParamsALL = '"update":[{"typeFlag":"FORM","data":' + Ext.JSON.encode(form.getValues()) + '}]';

        var actType = win.actType;


        //表格数据
        var updateJson = "";
        var updateRecs = gridStore.getUpdatedRecords();


        for (var i = 0; i < updateRecs.length; i++) {
            if (updateJson == "") {

                updateJson = '{"typeFlag":"GRID","data":' + Ext.JSON.encode(updateRecs[i].data) + '}';

            } else {

                updateJson = updateJson + ',{"typeFlag":"GRID","data":' + Ext.JSON.encode(updateRecs[i].data) + '}';

            }
        }
        ;

        var comParams3 = "";
        if (updateJson != "") {

            comParamsALL = comParamsALL + ',"update":[' + updateJson + "]";

        }


        var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{' + comParamsALL + '}}';

        var pageGrid = this.getView();

        Ext.tzSubmit(tzParams, function (resp) {
            win.actType = "update";

        }, "", true, this);
    },
    /*清除过滤条件*/
    onClearFilters: function (btn) {
        btn.findParentByType('grid').filters.clearFilters();
    },
    onComRegClose: function(btn){
        //关闭窗口
        this.getView().close();
    },
    /*查看邮件发送历史*/
    viewMailHistory: function(btn){
        var grid=btn.findParentByType("grid");
        var store = grid.getStore();
        var selList = grid.getSelectionModel().getSelection();

        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.znxzytjl","只能选择一条记录"));
            return;
        }
        var appInsID = selList[0].get("appInsID");

        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzGetEmail","comParams":{"appInsID":"'+appInsID+'"}}';

        Ext.tzLoad(tzParams,function(respData){
            if(respData.email!=undefined){
                Ext.tzSearchMailHistory(respData.email);
            }else{
                Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nxzdrymytjzyyx","您选中的人员没有添加邮箱"));
                return;
            }
        });
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
        
        var appInsID = selList[0].get("appInsID");
        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzGetSms","comParams":{"appInsID":"'+appInsID+'"}}';
        Ext.tzLoad(tzParams,function(respData){
    		if(respData.mobile!=undefined){
    			cmp = new ViewClass(respData.mobile);
		        cmp.on('afterrender',function(panel){
					var store  = panel.lookupReference("smsHistoryGrid").store;
		    		var tzStoreParams ='{"mobile":"'+respData.mobile+'"}';
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
        });
       
    },
    /*给选中人发送邮件*/
    sendEmlSelPers:function(btn) {
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var selList = grid.getSelectionModel().getSelection();

        //选中行长度
        var checkLen = selList.length;
        if (checkLen == 0) {
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt", "提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing", "您没有选中任何记录"));
            return;
        }

        var personList = [];
        for (var i = 0; i < checkLen; i++) {
            var oprID = selList[i].get('oprID');
            var appInsID = selList[i].get('appInsID');
            personList.push({"oprID": oprID, "appInsID": appInsID});
        };
        var params = {
            "ComID": "TZ_BMGL_BMBSH_COM",
            "PageID": "TZ_BMGL_YJDX_STD",
            "OperateType": "U",
            "comParams": {"add": [
                {"type": 'MULTI', "personList": personList}
            ]}
        };
        Ext.tzLoad(Ext.JSON.encode(params), function (responseData) {
            Ext.tzSendEmail({
                //发送的邮件模板;
                "EmailTmpName": ["TZ_EML_N"],
                //创建的需要发送的听众ID;
                "audienceId": responseData,
                //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "N"
            });
        });
    },
    sendEmlSelPersAll:function(btn) {
//        var grid = btn.findParentByType("grid");
//        var store = grid.getStore();
//        var selList = grid.getSelectionModel().getSelection();
//
//        //选中行长度
//        var checkLen = selList.length;
//        if (checkLen == 0) {
//            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt", "提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing", "您没有选中任何记录"));
//            return;
//        }

        var personList = [];
 
      
        if ((typeof getedSQL2) == "undefined") {
        	  personList.push({"classID2": classID2, "batchID2": batchID2});
        	  var params = {
        	            "ComID": "TZ_BMGL_BMBSH_COM",
        	            "PageID": "TZ_BMGL_DXYJ_STD",
        	            "OperateType": "U",
        	            "comParams": {"add": [
        	                {"type": 'MULTI', "personList": personList}
        	            ]}
        	        };     
        }else{
          personList.push({"getedSQL2":getedSQL2});
      	  var params = {
      	            "ComID": "TZ_BMGL_BMBSH_COM",
      	            "PageID": "TZ_BMGL_DXYJ_STD",
      	            "OperateType": "U",
      	            "comParams": {"add": [
      	                {"type": 'MULT', "personList": personList}
      	            ]}
      	        }; 
        }
      
        Ext.tzLoad(Ext.JSON.encode(params), function (responseData) {
            Ext.tzSendEmail({
                //发送的邮件模板;
                "EmailTmpName": ["TZ_EML_N"],
                //创建的需要发送的听众ID;
                "audienceId": responseData,
                //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "N"
            });
        });
    },
    /*评审进度管理*/
    clReviewScheduleMg:function(grid,rowIndex){
        Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_CLPS_SCHE_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有管理评审进度的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLPS_SCHE_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }

        ViewClass = Ext.ClassManager.get(className);
        //ViewClass = new KitchenSink.view.materialsReview.materialsReview.materialsReviewSchedule();
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
        var classID = record.get('classID');
        var batchID = record.get('batchID');
        cmp = new ViewClass(classID,batchID);

        cmp.on('afterrender',function(panel){
            var judgeStore =panel.down('tabpanel').child("form[name=judgeInfoForm]").child('grid').store,
                judgeParams = '{"type":"judgeInfo","classID":"'+classID+'","batchID":"'+batchID+'"}',
                form = panel.child('form').getForm();
            var stuListStore = panel.down('tabpanel').child('grid[name=materialsStudentGrid]').store,
                stuListParams = '{"type":"stuList","classID":"'+classID+'","batchID":"'+batchID+'"}';
            var tzParams ='{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD",' +
                '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                respData.className = record.data.className;
                respData.batchName = record.data.batchName;
                form.setValues(respData);
                var formButton =panel.child('form');
                var btnStartNewReview=formButton.down('button[name=startNewReview]'),
                    btnCloseReview=formButton.down('button[name=closeReview]'),
                    btnReStartReview=formButton.down('button[name=reStartReview]');
                if(respData.status=='进行中'){
                    btnStartNewReview.flagType='positive';
                    btnCloseReview.flagtype='positive';
                    btnReStartReview.flagType='negative';
                    btnStartNewReview.setDisabled(true);
                    btnReStartReview.setDisabled(true);
                }
                if(respData.status=='已关闭'){
                    btnStartNewReview.flagType='positive';
                    btnCloseReview.flagtype='negative';
                    btnReStartReview.flagType='positive';
                    btnCloseReview.setDisabled(true);
                }
                if(respData.status=='未开始'){
                    btnStartNewReview.flagType='negative';
                    btnCloseReview.flagtype='negative';
                    btnReStartReview.flagType='positive';
                    btnCloseReview.setDisabled(true);
                }
                if(respData.delibCount==0){
                    btnStartNewReview.flagType='positive';
                    btnCloseReview.flagtype='negative';
                    btnReStartReview.flagType='negative';
                    btnCloseReview.setDisabled(true);
                    btnReStartReview.setDisabled(true);
                }
            });
            judgeStore.tzStoreParams = judgeParams;
            judgeStore.load();
            stuListStore.tzStoreParams = stuListParams;
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    /*材料评审--设置评审规则*/
    setMaterialReviewRule:function(grid,rowIndex){
        Ext.tzSetCompResourses("TZ_REVIEW_CL_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_CL_COM"]["TZ_CLPS_RULE_STD"];

        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLPS_RULE_STD，请检查配置。');
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
        var classId = record.data.classID;
        var className = record.data.className;
        var batchId = record.data.batchID;
        var batchName = record.data.batchName;
        var applicantsNumber = record.data.applicantsNumber;

        var judgeGroupParams =  '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD",' +
            '"OperateType":"tzGetJudgeGroup","comParams":{"classId":"'+classId+'","batchId":"'+batchId+'"}}';
        Ext.tzLoad(judgeGroupParams,function(responseData){
            var judgeGroupData = responseData.groupData;

            cmp = new ViewClass({
                classId:classId,
                batchId:batchId,
                judgeGroupData:judgeGroupData
            });

            cmp.on('afterrender',function(panel){

                panel.judgeGroupData=judgeGroupData;

                var form = panel.child('form').getForm();
                var statisticsNumForm = panel.down("form[name=statisticsNumForm]").getForm();

                var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD",' +
                    '"OperateType":"QF","comParams":{"classId":"'+classId+'","batchId":"'+batchId+'"}}';

                Ext.tzLoad(tzParams,function(respData){
                    var formData = respData.formData;
                    if(formData!="" && formData!=undefined) {
                        panel.actType="update";
                        form.setValues(formData);

                        if(formData.dqpsStatus=="A") {
                            form.findField("judgeNumSet").setDisabled(true);
                        }

                        statisticsNumForm.findField("clpsksNum").setValue(formData.clpsksNum);
                        //要求评审人次更新
                        statisticsNumForm.findField("reviewNumSet").setValue(parseInt(formData.clpsksNum)*(formData.judgeNumSet));
                    } else {
                        panel.actType="add";
                        form.findField("classId").setValue(classId);
                        form.findField("className").setValue(className);
                        form.findField("batchId").setValue(batchId);
                        form.findField("batchName").setValue(batchName);
                        form.findField("bkksNum").setValue(applicantsNumber);
                        form.findField("clpsksNum").setValue(0);
                        form.findField("dqpsStatus").setValue("N");
                        form.findField("dqpsStatusDesc").setValue("未开始");
                        form.findField("judgeNumSet").setValue(2);

                        var statisticsForm = panel.down("form[name=statisticsNumForm]").getForm();
                        statisticsForm.findField("clpsksNum").setValue(0);
                        statisticsForm.findField("reviewNumSet").setValue(0);
                    }

                });
            });

            tab = contentPanel.add(cmp);

            contentPanel.setActiveTab(tab);

            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
        });


        /*
        var judgeGroupData = [];
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
            listeners: {
                load: function (store, records, successful, eOpts) {
                    for (i = 0; i < records.length; i++) {
                        judgeGroupData.push(records[i].data);
                    }

                    cmp = new ViewClass({
                        classId:classId,
                        batchId:batchId
                    });

                    cmp.on('afterrender',function(panel){

                        panel.judgeGroupData=judgeGroupData;

                        var form = panel.child('form').getForm();
                        var statisticsNumForm = panel.down("form[name=statisticsNumForm]").getForm();

                        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_RULE_STD",' +
                            '"OperateType":"QF","comParams":{"classId":"'+classId+'","batchId":"'+batchId+'"}}';

                        Ext.tzLoad(tzParams,function(respData){
                            var formData = respData.formData;
                            if(formData!="" && formData!=undefined) {
                                panel.actType="update";
                                form.setValues(formData);

                                statisticsNumForm.findField("clpsksNum").setValue(formData.clpsksNum);
                                //要求评审人次更新
                                statisticsNumForm.findField("reviewNumSet").setValue(parseInt(formData.clpsksNum)*(formData.judgeNumSet));
                            } else {
                                panel.actType="add";
                                form.findField("classId").setValue(classId);
                                form.findField("className").setValue(className);
                                form.findField("batchId").setValue(batchId);
                                form.findField("batchName").setValue(batchName);
                                form.findField("bkksNum").setValue(applicantsNumber);
                                form.findField("clpsksNum").setValue(0);
                                form.findField("dqpsStatus").setValue("N");
                                form.findField("dqpsStatusDesc").setValue("未开始");

                                var statisticsForm = panel.down("form[name=statisticsNumForm]").getForm();
                                statisticsForm.findField("clpsksNum").setValue(0);
                                statisticsForm.findField("reviewNumSet").setValue(0);
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
        */
    },
    /*材料评审--材料评审考生名单*/
    viewMaterialStuApplicants:function(grid,rowIndex){
        Ext.tzSetCompResourses("TZ_REVIEW_CL_COM");
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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }


        var record = grid.store.getAt(rowIndex);
        var classId = record.data.classID;
        var className = record.data.className;
        var batchId = record.data.batchID;
        var batchName = record.data.batchName;
        var applicantsNumber = record.data.applicantsNumber;

        cmp = new ViewClass({
            classId:classId,
            batchId:batchId
        });

        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();
            var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_KS_STD",' + '"OperateType":"QF","comParams":{"classId":"'+classId+'","batchId":"'+batchId+'"}}';

            Ext.tzLoad(tzParams,function(respData){
                var examineeGrid = panel.down('grid');
                var tzStoreParams = '{"cfgSrhId": "TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.TZ_CLPS_KS_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "' + classId + '","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value": "' + batchId + '"}}';
                examineeGrid.store.tzStoreParams = tzStoreParams;

                var formData = respData.formData;
                if(formData!="" && formData!=undefined) {
                    panel.actType="update";
                    formData.className = record.data.className;
                    formData.batchName = record.data.batchName;
                    form.setValues(formData);
                    examineeGrid.store.load();
                } else {
                    panel.actType="add";
                    form.findField("classId").setValue(classId);
                    form.findField("className").setValue(className);
                    form.findField("batchId").setValue(batchId);
                    form.findField("batchName").setValue(batchName);
                    form.findField("bkksNum").setValue(applicantsNumber);
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
    /*评审进度管理*/
    msReviewScheduleMg:function(grid,rowIndex){

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_MSPS_SCHE_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有管理评审进度的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CLPS_SCHE_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }

        ViewClass = Ext.ClassManager.get(className);
        //ViewClass = new KitchenSink.view.materialsReview.materialsReview.materialsReviewSchedule();
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
        var transValue = this.transValues();
        var record = grid.store.getAt(rowIndex);
        var classID = record.get('classID');
        var batchID = record.get('batchID');
        cmp = new ViewClass(classID,batchID,transValue);
        cmp.on('afterrender',function(panel){
            var judgeStore =panel.down('tabpanel').child("form[name=judgeFormInfo]").child('grid[name=interviewJudgeGrid]').store,
                judgeParams = '{"type":"judgeInfo","classID":"'+classID+'","batchID":"'+batchID+'"}',
                form = panel.child('form'),
                tzParams ='{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_SCHE_STD",' +
                    '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}',
                stuListStore = panel.down('tabpanel').child('grid[name=interviewStudentGrid]').getStore(),
                stuListParams = '{"type":"stuList","classID":"'+classID+'","batchID":"'+batchID+'"}';

            Ext.tzLoad(tzParams,function(respData){

                transValue.set(["TZ_PWEI_ZHZT","TZ_GENDER","TZ_LUQU_ZT","TZ_MSPS_ZT","TZ_MSPS_KSZT"],function(){
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

            });
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    onInterviewClose: function(btn){
        //关闭窗口
    	var panel = btn.findParentByType("appFormClass");
		panel.commitChanges(panel);
		panel.close();
		
    },
    //查看面试情况
    interviewCheck: function(grid, rowIndex){
    	var record = grid.store.getAt(rowIndex);
        var classID = record.get('classID');
        var batchID = record.get('batchID');
    	Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_INTERVIEW_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_INTERVIEW_STD，请检查配置。');
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
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
		}

		cmp = new ViewClass(classID,batchID);
		cmp.on('afterrender',function(panel){
			
		});
		
		tab = contentPanel.add(cmp);     
		
		contentPanel.setActiveTab(tab);

		Ext.resumeLayouts(true);

		if (cmp.floating) {
			cmp.show();
		}
	},
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

    //自动初筛BASE版
    automaticScreenBase: function(grid,rowIndex){
        Ext.tzSetCompResourses("TZ_AUTO_BASE_COM");

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUTO_BASE_COM"]["TZ_AUTO_SCREEN_BS"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUTO_SCREEN_BS，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }

        ViewClass = Ext.ClassManager.get(className);

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        var batchID = record.data.batchID;
        var batchName = record.data.batchName;
        var className = record.data.className;

        cmp = new ViewClass({
            classId: classID,
            batchId: batchID,
            className:className,
            batchName: batchName
        });

        cmp.on('afterrender',function(gridPanel){});

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
  //自动初筛
    automaticScreen: function(grid,rowIndex){
        Ext.tzSetCompResourses("TZ_AUTO_SCREEN_COM");

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AUTO_SCREEN_COM"]["TZ_AUTO_SCREEN_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AUTO_SCREEN_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }

        ViewClass = Ext.ClassManager.get(className);

        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        var batchID = record.data.batchID;
        var batchName = record.data.batchName;
        var className = record.data.className;

        cmp = new ViewClass({
            classId: classID,
            batchId: batchID,
            className:className,
            batchName: batchName
        });

        cmp.on('afterrender',function(gridPanel){});

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    /*面试评审--设置评审规则*/
    setInterviewReviewRule:function(grid,rowIndex){
        Ext.tzSetCompResourses("TZ_REVIEW_MS_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_RULE_STD"];

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
        var classId = record.data.classID;
        var className = record.data.className;
        var batchId = record.data.batchID;
        var batchName = record.data.batchName;
        var applicantsNumber = record.data.applicantsNumber;
        cmp = new ViewClass({
            classId:classId,
            batchId:batchId
        });
        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();


            var pwgridform = panel.down("form[name=pwlbgrid]").getForm();
            var kspwnum=panel.down('grid').down('numberfield[name=ksRevedpwnum]');
            var pwTeamnum=panel.down('grid').down('numberfield[name=countTeamnum]');


            // var statisticsNumForm = panel.down("form[name=statisticsNumForm]").getForm();

            var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD",' +
                '"OperateType":"QF","comParams":{"classId":"'+classId+'","batchId":"'+batchId+'"}}';

            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                if(formData!="" && formData!=undefined) {
                    panel.actType="update";
                    form.setValues(formData);


                    // pwgridform.findField("ksRevedpwnum").
                    //  kspwnum.setValue(formData.kspwnum);
                    //  pwTeamnum.setValue(formData.pwTeamnum);



                    //  statisticsNumForm.findField("clpsksNum").setValue(formData.clpsksNum);
                    //要求评审人次更新
                    // statisticsNumForm.findField("reviewNumSet").setValue(parseInt(formData.clpsksNum)*(formData.judgeNumSet));
                } else {
                    panel.actType="add";
                    form.findField("classId").setValue(classId);
                    form.findField("className").setValue(className);
                    form.findField("batchId").setValue(batchId);

                    form.findField("ksNum").setValue(applicantsNumber);
                    //form.findField("reviewClpsKsNum").setValue(0);
                    form.findField("reviewKsNum").setValue(0);
                    form.findField("batchName").setValue(batchName);
                    kspwnum.setValue(0);
                    pwTeamnum.setValue(0);
                    // form.findField("bkksNum").setValue(applicantsNumber);
                    //form.findField("clpsksNum").setValue(0);
                    // form.findField("dqpsStatus").setValue("N");
                    //form.findField("dqpsStatusDesc").setValue("未开始");

                    // var statisticsForm = panel.down("form[name=statisticsNumForm]").getForm();
                    // statisticsForm.findField("clpsksNum").setValue(0);
                    // statisticsForm.findField("reviewNumSet").setValue(0);
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
    /*面试评审--面试评审考生名单*/
    viewInterviewStuApplicants:function(grid,rowIndex){
        Ext.tzSetCompResourses("TZ_REVIEW_MS_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_REVIEW_MS_COM"]["TZ_MSPS_KS_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MSPS_KS_STD，请检查配置。');
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
        var classId = record.data.classID;
        var className = record.data.className;
        var batchId = record.data.batchID;
        var batchName = record.data.batchName;
        var applicantsNumber = record.data.applicantsNumber;

        cmp = new ViewClass({
            classId:classId,
            batchId:batchId
        });

        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();
            var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_RULE_STD",' + '"OperateType":"QF","comParams":{"classId":"'+classId+'","batchId":"'+batchId+'"}}';
            var examineeGrid = panel.down('grid');

            var tzStoreParams = '{"cfgSrhId":"TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.TZ_MSPS_KS_VW","condition":{"TZ_CLASS_ID-operator": "01","TZ_CLASS_ID-value": "' + classId + '","TZ_APPLY_PC_ID-operator": "01","TZ_APPLY_PC_ID-value": "' + batchId + '"}}';
            examineeGrid.store.tzStoreParams = tzStoreParams;

            Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                if(formData!="" && formData!=undefined) {
                    panel.actType="update";
                    formData.className = className;
                    formData.batchName = batchName;
                    form.setValues(formData);
                    examineeGrid.store.load();

                } else {
                    panel.actType="add";

                    form.setValues({classId:classId,className:className,batchId:batchId,batchName:batchName,ksNum:applicantsNumber,reviewKsNum:0});
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
    //查看申请人详情
    viewSqrInfo:function(btn){
        var grid=btn.findParentByType("grid");
        var store = grid.getStore();
        //选中行
        var selList = grid.getSelectionModel().getSelection();

        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要查看的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要查看的记录");
            return;
        }

        var OPRID = selList[0].get("oprID");

        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        var cmp = this.createUserInfoClass();

        cmp.on('afterrender',function(){
            var msgForm = this.lookupReference('userMgForm');
            var form = this.lookupReference('userMgForm').getForm();
            var userInfoForm =this.lookupReference('userMgForm').down('form[name=userInfoForm]').getForm();
            var processInfoForm =this.lookupReference('userMgForm').down('form[name=processInfoForm]').getForm();
            //var ksdrInfoForm =this.lookupReference('userMgForm').down('form[name=ksdrInfoForm]').getForm();

            var tzParams = '{"ComID":"TZ_UM_USERMG_COM","PageID":"TZ_UM_USERINFO_STD","OperateType":"QF","comParams":{"OPRID":"'+OPRID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //用户账号信息数据
                var formData = responseData.formData;

                form.setValues(formData);

                //考生导入信息;
                //ksdrInfoForm.setValues(formData.ksdrInfo);
                //考生个人信息
                userInfoForm.setValues(formData.perInfo)
                //录取流程
                processInfoForm.setValues(formData.lqlcInfo);
                /*var userInfoItems = [];

                var fields = formData.column;
                var size = fields.length;
                typeField = {};*/
                /*for(var i = 0;i < size;i++){
                    var field = fields[i];
                    var fieldLabel,name,value;
                    for(var fieldName in field){
                        if(fieldName == "desc"){
                            fieldLabel = field["desc"];
                        }else{
                            name = fieldName;
                            value = field[fieldName];
                        }
                    }
                    typeField = {
                        xtype: 'textfield',
                        fieldLabel: fieldLabel,
                        readOnly:true,
                        name: name,
                        value: value,
                        fieldStyle:'background:#F4F4F4',
                    }
                    userInfoForm.add(typeField);
                }*/
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
    createUserInfoClass: function(){
        //是否有访问权限
    	 Ext.tzSetCompResourses("TZ_UM_USERMG_COM");// 设置组件
    	var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_UM_USERMG_COM"]["TZ_UM_USERINFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有访问或修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_YHZHXX_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        //contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        //contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.security.user.userInfoPanel';
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
    /*给选中人发送短信*/
    sendSmsSelPers:function(btn) {
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var selList = grid.getSelectionModel().getSelection();

        //选中行长度
        var checkLen = selList.length;
        if (checkLen == 0) {
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt", "提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing", "您没有选中任何记录"));
            return;
        }

        var personList = [];
        for (var i = 0; i < checkLen; i++) {
            var oprID = selList[i].get('oprID');
            var appInsID = selList[i].get('appInsID');
            personList.push({"oprID": oprID, "appInsID": appInsID});
        };
        var params = {
            "ComID": "TZ_BMGL_BMBSH_COM",
            "PageID": "TZ_BMGL_YJDX_STD",
            "OperateType": "U",
            "comParams": {"add": [
                {"type": 'MULTI', "personList": personList}
            ]}
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
    },
    /*给选中人发送短信*/
    sendSmsSelPersAll:function(btn) {
       

    	 var personList = [];
    	 
         
         if ((typeof getedSQL2) == "undefined") {
         	  personList.push({"classID2": classID2, "batchID2": batchID2});
         	  var params = {
         	            "ComID": "TZ_BMGL_BMBSH_COM",
         	            "PageID": "TZ_BMGL_DXYJ_STD",
         	            "OperateType": "U",
         	            "comParams": {"add": [
         	                {"type": 'MULTI', "personList": personList}
         	            ]}
         	        };     
         }else{
           personList.push({"getedSQL2":getedSQL2});
       	  var params = {
       	            "ComID": "TZ_BMGL_BMBSH_COM",
       	            "PageID": "TZ_BMGL_DXYJ_STD",
       	            "OperateType": "U",
       	            "comParams": {"add": [
       	                {"type": 'MULT', "personList": personList}
       	            ]}
       	        }; 
         }
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
    },
    /*给搜索结果发送短信*/
    sendSmsSelPersOfAll:function(btn) {

    	var strAppId;   
    	var store = btn.findParentByType("grid").store;
		var strConfSearCond=btn.findParentByType('classInfo');
		if (strConfSearCond.length==0) {
			strConfSearCond="{\"cfgSrhId\":\"TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.TZ_APP_LIST_VW\",\"condition\": {\"TZ_CLASS_ID-operator\": \"01\", \"TZ_CLASS_ID-value\":\""+store.classID +"\"}}";
			}
		
        //顾贤达 2017年6月26日 11:08:54 修改SQL提交逻辑
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_STU_STD","OperateType":"tzGetAppIdAndOprID","comParams":'+strConfSearCond+'}';

		Ext.tzLoadAsync(tzParams,function(resp){
			 if(resp.AppID!=undefined){
					strAppId =resp.AppID;
	            }
	    },"",true,this);
		console.log(strAppId);
    	
    	
		var personList = [];
		var strs= new Array(); 
		strs=strAppId.split(";"); 
		
		for (i=0;i<strs.length ;i++ ){ 
			var strs2= new Array();
			strs2=strs[i].split("+");
				var oprID = strs2[1];
		        var appInsID = strs2[0];
		        personList.push({"oprID": oprID, "appInsID": appInsID});
		}  
     
        var params = {
            "ComID": "TZ_BMGL_BMBSH_COM",
            "PageID": "TZ_BMGL_YJDX_STD",
            "OperateType": "U",
            "comParams": {"add": [
                {"type": 'MULTI', "personList": personList}
            ]}
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
        
    },
    saveAsStaAud: function (btn) {

    	 var grid = btn.findParentByType("grid");
      //   var store = grid.getStore();
         var selList = grid.getSelectionModel().getSelection();

         //选中行长度
         var checkLen = selList.length;
         if (checkLen == 0) {
             Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt", "提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing", "您没有选中任何记录"));
             return;
         }

        var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"U","comParams":{"add":[{"audJG":"ADMIN","audID":"NEXT","audName":"","audStat":"1","audType":"2","audMS":"","audSQL":"","audLY":"ZCYH"}]}}';

        //后台执行插入表操作
        var AudID = "";
        Ext.tzLoadAsync(tzParams, function (resp) {
            AudID = resp;

            var oprIDJson = "";
            for (var i = 0; i < selList.length; i++) {
                var OPRID = selList[i].get("oprID");

                if (oprIDJson == "") {
                    oprIDJson = '{"OPRID":"' + OPRID + '","AudID":"' + AudID + '"}';
                } else {
                    oprIDJson = oprIDJson + ',' + '{"OPRID":"' + OPRID + '","AudID":"' + AudID + '"}';
                }
            }
            var comParamsOPRID = "";
            if (oprIDJson != "") {
                comParamsOPRID = '"add":[' + oprIDJson + "]";
            }
            var tzParams2 = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_LIST_STD","OperateType":"U","comParams":{' + comParamsOPRID + '}}';


            Ext.tzSubmit(tzParams2, function (resp) {

            }, "", true, this, AudID);


        }, "", true, this);


        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_UM_AUDNEW_STD"];

        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];


        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_UM_AUDNEW_STD，请检查配置。');
            return;
        }

        var win = this.lookupReference('pageRegWindow');

        if (!win) {

            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
        win.actType = "update";

        var audId = AudID;


        var audName = "";
        var audStat = "1";
        var audType = "2";
        var audMS = "";
        var audSQL = "";


        //参数
        var tzParams = '{"ComID":"TZ_AUD_COM","PageID":"TZ_AUD_NEW_STD","OperateType":"QF","comParams":{"audId":"' + audId + '","audName":"' + audName + '","audStat":"' + audStat + '","audType":"' + audType + '","audMS":"' + audMS + '","audSQL":"' + audSQL + '"}}';
        //页面注册信息表单

        var form = win.child("form").getForm();

        var gridStore = win.child("form").child("grid").getStore();
        var tzStoreParams = '{"cfgSrhId":"TZ_AUD_COM.TZ_AUD_NEW_STD.PS_TZ_AUDCY_VW","condition":{"TZ_AUD_ID-operator": "01","TZ_AUD_ID-value": "' + audId + '"}}';

        Ext.tzLoad(tzParams, function (responseData) {

            form.setValues(responseData);
            gridStore.tzStoreParams = tzStoreParams;
            gridStore.reload();

        });


        win.show();


    },
    //查看招生线索
    clueDetailForm:function(view,rowIndex){
    	var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        var clueID = selRec.get("clueID");
        this.editClueInfoByID(clueID);
    },
    //根据线索ID进行编辑
    editClueInfoByID:function(clueId) {
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

