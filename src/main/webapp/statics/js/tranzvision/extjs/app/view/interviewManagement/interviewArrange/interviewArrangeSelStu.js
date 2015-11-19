Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSelStu', {
    extend: 'Ext.panel.Panel',
    xtype: 'interviewArrangeSelStu',
    controller: 'interviewArrangeSelStuController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSelStuModel',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSelStuStore',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSelStuController'
    ],
    title: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.panelTitle","考生选择"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    reference: 'interviewArrangeSelStuPanel',
    //style:"margin:8px",
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;/*button height plus panel body padding*/
            var grid = panel.child('grid');
            if(grid)grid.setHeight( height -buttonHeight -16);
        }
    },
    initComponent: function (){
        var me = this;
        //材料评审状态（有无面试资格）
        var mszgFlagStore = new KitchenSink.view.common.store.appTransStore("TZ_MSHI_ZGFLG");
        //为filter添加值
        var mszgFlagSortFilterOptions=[];
        mszgFlagStore.addListener("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                mszgFlagSortFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
            };
        });
        mszgFlagStore.load();

        //预约状态
        var msArrStateStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_ARR_STATE");
        msArrStateStore.load();
        //为filter添加值
        var msArrStateFilterOptions=[];
        msArrStateStore.addListener("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                msArrStateFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
            };
        });

        //确认状态
        var msConfStateStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_CONF_STA");
        msConfStateStore.load();
        //为filter添加值
        var msConfStateFilterOptions=[];
        msConfStateStore.addListener("load",function(store, records, successful, eOpts){
            for(var i=0;i<records.length;i++){
                msConfStateFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
            };
        });


        //gridStore添加filterchange监听
        var interviewArrangeSelStuGridStore = new KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSelStuStore({
            listeners:{
                filterchange:function( store, filters, eOpts ){
                    var clearFiltersBtn=me.lookupReference('msArrSelStuClearFiltersBtn');
                    if(filters.length>0){
                        clearFiltersBtn.setDisabled( false );
                    }else{
                        clearFiltersBtn.setDisabled( true );
                    }
                }
            }
        });
        Ext.util.CSS.createStyleSheet(" .x-grid-cell.msjysjstyle {background-color: #66cc66;}","msArrSelStuJysj");
        Ext.util.CSS.createStyleSheet(" .x-grid-cell.nomsjysjstyle {background-color: #ff0000;}","msArrSelStuNoJysj");
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'interviewArrangeSelStuForm',
                layout: {
                    type: 'vbox',       // Arrange child items vertically
                    align: 'stretch'    // 控件横向拉伸至容器大小
                },
                border: false,
                bodyPadding: 4,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 120,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'textfield',
                    name: 'msArrGridRowIndex',
                    hidden:true
                }]
            },{
                xtype: 'grid',
                //minHeight: 340,
                frame: true,
                name: 'interviewArrangeSelStuGrid',
                reference: 'interviewArrangeSelStuGrid',
                store: interviewArrangeSelStuGridStore,
                columnLines: true,    //显示纵向表格线
                plugins:[
                    {
                        ptype: 'gridfilters'
                    }
                ],
                selModel:new Ext.selection.CheckboxModel({
                    checkOnly:true,
                    listeners: {
                        select: function( t, record, index, eOpts ){
                            //当前考生选择panel-grid中数据
                            var selStuGridRowClassId,selStuGridRowBatchId,selStuGridRowArrState,selStuGridRowConfState,selStuGridRowOprid,selStuGridRowAppid,msArrInfoGridRowIndex;
                            selStuGridRowClassId=record.data.classId;
                            selStuGridRowBatchId=record.data.batchId;
                            selStuGridRowArrState=record.data.arrState;
                            selStuGridRowConfState=record.data.confState;
                            selStuGridRowOprid=record.data.oprId;
                            selStuGridRowAppid=record.data.appId;
                            msArrInfoGridRowIndex=record.data.msArrInfoGridRowIndex;
                            var localStartDateFormat = Ext.util.Format.date(record.data.localStartDate, 'Y-m-d');
                            var localFinishDateFormat = Ext.util.Format.date(record.data.localFinishDate, 'Y-m-d');
                            var moreInfo={"city":record.data.city,"country":record.data.country,"timezone":record.data.timezone,"timezoneDiff":record.data.timezomeDiff,"localStartDate":localStartDateFormat,"localFinishDate":localFinishDateFormat,"lxEmail":record.data.lxEmail};

                            //面试安排panel相关
                            var msArrInfoPanelArr=Ext.ComponentQuery.query("panel[reference=interviewArrangeInfoPanel]");
                            var msArrInfoForm,msArrInfoFormRec;
                            var msArrInfoGridStore,msArrInfoGridStoreRec;
                            var msArrInfoFormRecClassId,msArrInfoFormRecBatchId;

                            //考生选择panel数组
                            var selStuPanelArr=Ext.ComponentQuery.query("panel[reference=interviewArrangeSelStuPanel]");
                            var selStuGridStore,selStuGridMsArrInfoGridRowIndex;
                            //待删除index
                            var index;

                            //selStuGridRowConfState='C';
                            if (selStuGridRowConfState=='C'||selStuGridRowArrState=="B") {
                                var tzParams = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_ARR_CSTU_STD","OperateType":"getMsArrInfo","comParams":{"classID":"'+selStuGridRowClassId+'","batchID":"'+selStuGridRowBatchId+'","oprid":"'+selStuGridRowOprid+'"}}';
                                Ext.tzLoad(tzParams,function(responseData) {
                                    var curMsArrTime=record.data.bjDate+" "+record.data.bjTime;
                                    var infoText="";
                                    if(selStuGridRowConfState=='C'){
                                        infoText="学生 "+record.data.stuName+" 当前被安排在 "+responseData.msArrTime+" 面试，且学生已确认，确认将该考生的面试时间重新调整到 "+curMsArrTime+" 吗?";
                                    }else if(selStuGridRowArrState=="B"){
                                        infoText="学生 "+record.data.stuName+" 当前被安排在 "+responseData.msArrTime+" 面试，确认将该考生的面试时间重新调整到 "+curMsArrTime+" 吗?";
                                    };

                                    Ext.MessageBox.confirm('确认',infoText , function(btnId){
                                        if(btnId == 'yes'){
                                            //清除当前考生面试安排信息
                                            var tzParams1 = '{"ComID":"TZ_MS_ARR_MG_COM","PageID":"TZ_MS_ARR_CSTU_STD","OperateType":"delCurStuMsArrInfo","comParams":{"classID":"'+selStuGridRowClassId+'","batchID":"'+selStuGridRowBatchId+'","oprid":"'+selStuGridRowOprid+'"}}';
                                            Ext.tzLoad(tzParams1,function(responseData) {
                                                if(responseData.success='success'){
                                                    //面试安排panel相关
                                                    if(msArrInfoPanelArr.length>0){
                                                        for(var i=0;i<msArrInfoPanelArr.length;i++){
                                                            msArrInfoForm = msArrInfoPanelArr[i].child('form');
                                                            msArrInfoFormRec = msArrInfoForm.getForm().getFieldValues();
                                                            msArrInfoGridStore =msArrInfoForm.child('grid').store;
                                                            msArrInfoFormRecClassId = msArrInfoFormRec["classID"];
                                                            msArrInfoFormRecBatchId = msArrInfoFormRec["batchID"];
                                                            if(msArrInfoFormRecClassId==selStuGridRowClassId && msArrInfoFormRecBatchId==selStuGridRowBatchId){
                                                                index = msArrInfoGridStore.find('msOprId',record.data.oprId);
                                                                if(index!=-1){
                                                                    msArrInfoGridStoreRec = msArrInfoGridStore.getAt(index);
                                                                    msArrInfoGridStoreRec.set("localStartTime","");
                                                                    msArrInfoGridStoreRec.set("localFinishTime","");
                                                                    msArrInfoGridStoreRec.set("skypeId","");
                                                                    msArrInfoGridStoreRec.set("msOprId","");
                                                                    msArrInfoGridStoreRec.set("msOprName","");
                                                                    msArrInfoGridStoreRec.set("msOrderState","");
                                                                    msArrInfoGridStoreRec.set("msConfirmState","");
                                                                    msArrInfoGridStoreRec.set("sort","");
                                                                    msArrInfoGridStoreRec.set("releaseOrUndo","");
                                                                    msArrInfoGridStoreRec.set('moreInfo',"{}");
                                                                }
                                                                msArrInfoGridStoreRec = msArrInfoGridStore.getAt(msArrInfoGridRowIndex);
                                                                msArrInfoGridStoreRec.set("localStartTime",record.data.localStartTime);
                                                                msArrInfoGridStoreRec.set("localFinishTime",record.data.localFinishTime);
                                                                msArrInfoGridStoreRec.set("skypeId",record.data.skype);
                                                                msArrInfoGridStoreRec.set("msOprId",record.data.oprId);
                                                                msArrInfoGridStoreRec.set("msOprName",record.data.stuName);
                                                                msArrInfoGridStoreRec.set("msOrderState","");
                                                                msArrInfoGridStoreRec.set("msConfirmState","");
                                                                msArrInfoGridStoreRec.set("sort","");
                                                                msArrInfoGridStoreRec.set("releaseOrUndo","");
                                                                msArrInfoGridStoreRec.set('moreInfo',moreInfo);
                                                            }
                                                        }
                                                    };
                                                    //考生选择panel数组
                                                    for(var i=0;i<selStuPanelArr.length;i++){
                                                        selStuGridStore = selStuPanelArr[i].child('grid').store;
                                                        if (selStuGridStore.getCount()>0){
                                                            selStuGridMsArrInfoGridRowIndex=selStuGridStore.getAt(0).data.msArrInfoGridRowIndex;
                                                            if (selStuGridMsArrInfoGridRowIndex==msArrInfoGridRowIndex) {
                                                                selStuPanelArr[i].close();
                                                            }
                                                        }
                                                    }
                                                }else{
                                                    Ext.Msg.alert("提示","清除考生面试安排信息失败,请重试.");
                                                }
                                            });
                                        }else{
                                            //考生选择panel数组
                                            var selectedRecs,selStuGridSelectionModel;
                                            var selStuGridUnselecteRecs=new Array();
                                            for(var i=0;i<selStuPanelArr.length;i++){
                                                selStuGridStore = selStuPanelArr[i].child('grid').store;
                                                selStuGridSelectionModel=selStuPanelArr[i].child('grid').getSelectionModel();
                                                selectedRecs = selStuGridSelectionModel.getSelection();
                                                if (selectedRecs.length>0) {
                                                    for (var i = 0; i < selectedRecs.length; i++) {
                                                        if (selStuGridRowAppid==selectedRecs[i].data.appId) {

                                                        } else {
                                                            selStuGridUnselecteRecs.push(selectedRecs[i]);
                                                        }
                                                    }
                                                    selStuGridSelectionModel.deselect(selStuGridUnselecteRecs);
                                                }
                                            }
                                        }
                                    },this);
                                });
                            }else{
                                //面试安排panel相关
                                if(msArrInfoPanelArr.length>0){
                                    for(var i=0;i<msArrInfoPanelArr.length;i++){
                                        msArrInfoForm = msArrInfoPanelArr[i].child('form');
                                        msArrInfoFormRec = msArrInfoForm.getForm().getFieldValues();
                                        msArrInfoGridStore =msArrInfoForm.child('grid').store;
                                        msArrInfoFormRecClassId = msArrInfoFormRec["classID"];
                                        msArrInfoFormRecBatchId = msArrInfoFormRec["batchID"];
                                        if(msArrInfoFormRecClassId==selStuGridRowClassId && msArrInfoFormRecBatchId==selStuGridRowBatchId){
                                            index = msArrInfoGridStore.find('msOprId',record.data.oprId);
                                            if(index!=-1){
                                                msArrInfoGridStoreRec = msArrInfoGridStore.getAt(index);
                                                msArrInfoGridStoreRec.set("localStartTime","");
                                                msArrInfoGridStoreRec.set("localFinishTime","");
                                                msArrInfoGridStoreRec.set("skypeId","");
                                                msArrInfoGridStoreRec.set("msOprId","");
                                                msArrInfoGridStoreRec.set("msOprName","");
                                                msArrInfoGridStoreRec.set("msOrderState","");
                                                msArrInfoGridStoreRec.set("msConfirmState","");
                                                msArrInfoGridStoreRec.set("sort","");
                                                msArrInfoGridStoreRec.set("releaseOrUndo","");
                                                msArrInfoGridStoreRec.set('moreInfo',"{}");
                                            }

                                            msArrInfoGridStoreRec = msArrInfoGridStore.getAt(msArrInfoGridRowIndex);
                                            msArrInfoGridStoreRec.set("localStartTime",record.data.localStartTime);
                                            msArrInfoGridStoreRec.set("localFinishTime",record.data.localFinishTime);
                                            msArrInfoGridStoreRec.set("skypeId",record.data.skype);
                                            msArrInfoGridStoreRec.set("msOprId",record.data.oprId);
                                            msArrInfoGridStoreRec.set("msOprName",record.data.stuName);
                                            msArrInfoGridStoreRec.set("msOrderState","");
                                            msArrInfoGridStoreRec.set("msConfirmState","");
                                            msArrInfoGridStoreRec.set("sort","");
                                            msArrInfoGridStoreRec.set("releaseOrUndo","");
                                            msArrInfoGridStoreRec.set('moreInfo',moreInfo);
                                        }
                                    }
                                };
                                //考生选择panel数组
                                for(var i=0;i<selStuPanelArr.length;i++){
                                    selStuGridStore = selStuPanelArr[i].child('grid').store;
                                    if (selStuGridStore.getCount()>0){
                                        selStuGridMsArrInfoGridRowIndex=selStuGridStore.getAt(0).data.msArrInfoGridRowIndex;
                                        if (selStuGridMsArrInfoGridRowIndex==msArrInfoGridRowIndex) {
                                            selStuPanelArr[i].close();
                                        }
                                    }
                                }
                            }
                        }
                    }

                }),
                dockedItems:[{
                    xtype:"toolbar",
                    items:[	{
                        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.tbarClearFilters","清除筛选条件"),
                        tooltip:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.tbarClearFiltersTip","清除筛选条件"),
                        iconCls:"reset",
                        reference:'msArrSelStuClearFiltersBtn',
                        handler:'onClearFilters',
                        disabled:true
                    }]
                }],
                columns: [{
                    text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.rowNum","序号"),
                    xtype: 'rownumberer',
                    width:50
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.appId","报名表编号") ,
                    dataIndex: 'appId',
                    filter: {
                        type: 'number'
                    },
                    minWidth:125,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.stuName","姓名") ,
                    dataIndex: 'stuName',
                    filter: {
                        type: 'string'
                    },
                    minWidth: 100,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.msCLPSPC","材料评审批次") ,
                    dataIndex: 'msCLPSPC',
                    filter: {
                        type: 'string'
                    },
                    minWidth: 125,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.msZGFlag","初筛结果"),
                    dataIndex: 'msZGFlag',
                    filter: {
                        type: 'list',
                        options: mszgFlagSortFilterOptions
                    },
                    renderer : function(value, metadata, record) {
                        //alert("render"+value);
                        var index = mszgFlagStore.find('TValue',value);
                        if(index!=-1){
                            return mszgFlagStore.getAt(index).data.TSDesc;
                        }
                        return record.get('msZGFlag');
                    },
                    minWidth:120,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.arrState","预约状态") ,
                    filter: {
                        type: 'list',
                        options: msArrStateFilterOptions
                    },
                    sortable: true,
                    dataIndex: 'arrState',
                    minWidth: 105,
                    flex:1,
                    renderer : function(value, metadata, record) {
                        if (value=="B"){
                            metadata.tdCls = 'msArrangeArrStateYesStyle';
                        }else{
                            metadata.tdCls = 'msArrangeArrStateNoStyle';
                        }
                        //alert("render"+value);
                        var index = msArrStateStore.find('TValue',value);
                        if(index!=-1){
                            return msArrStateStore.getAt(index).data.TSDesc;
                        }
                        return record.get('msZGFlag');
                    }
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.confState","确认状态") ,
                    filter: {
                        type: 'list',
                        options: msConfStateFilterOptions
                    },
                    sortable: true,
                    dataIndex: 'confState',
                    minWidth: 105,
                    flex:1,
                    renderer : function(value, metadata, record) {
                        if (value=="C"){
                            metadata.tdCls = 'msArragneConfigStateYesStyle';
                        }else if (value=="NA"){
                            metadata.tdCls = 'msArragneConfigStateNcStyle';
                        }else{
                            metadata.tdCls = 'msArragneConfigStateNoStyle';
                        }
                        //alert("render"+value);
                        var index = msConfStateStore.find('TValue',value);
                        if(index!=-1){
                            return msConfStateStore.getAt(index).data.TSDesc;
                        }
                        return record.get('msZGFlag');
                    }
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.timeSort","时间类型") ,
                    sortable: true,
                    dataIndex: 'timeSort',
                    minWidth: 110,
                    flex:1,
                    renderer:function(value, metadata, record){
                        if (value=="Y"){
                            metadata.tdCls = 'msjysjstyle';
                            return "建议时间";
                        }else  if(value=="N"){
                            metadata.tdCls = 'nomsjysjstyle';
                            return "不建议时间";
                        }
                    }
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.city","所在城市") ,
                    filter: {
                        type: 'string'
                    },
                    sortable: true,
                    dataIndex: 'city',
                    minWidth: 105,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.country","所在国家") ,
                    filter: {
                        type: 'string'
                    },
                    sortable: true,
                    dataIndex: 'country',
                    minWidth: 105,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.timezone","所属时区") ,
                    filter: {
                        type: 'string'
                    },
                    sortable: true,
                    dataIndex: 'timezone',
                    minWidth: 107,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.timezomeDiff","时差（同北京）") ,
                    filter: {
                        type: 'string'
                    },
                    sortable: true,
                    dataIndex: 'timezomeDiff',
                    minWidth: 125,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.bjTime","北京时间") ,
                    sortable: true,
                    dataIndex: 'bjTime',
                    minWidth: 110,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.localStartDate","当地开始日期") ,
                    xtype:'datecolumn',
                    format:'Y-m-d',
                    filter: {
                        type: 'date',
                        dateFormat: 'Y-m-d'
                    },
                    sortable: true,
                    dataIndex: 'localStartDate',
                    minWidth: 125,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.localStartTime","当地开始时间") ,
                    sortable: true,
                    dataIndex: 'localStartTime',
                    minWidth: 125,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.localFinishDate","当地结束日期") ,
                    xtype:'datecolumn',
                    format:'Y-m-d',
                    filter: {
                        type: 'date',
                        dateFormat: 'Y-m-d'
                    },
                    sortable: true,
                    dataIndex: 'localFinishDate',
                    minWidth: 125,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.localFinishTime","当地结束时间") ,
                    filter: {
                        type: 'string'
                    },
                    sortable: true,
                    dataIndex: 'localFinishTime',
                    minWidth: 125,
                    flex:1
                }]
            }]
        });
        this.callParent();
    },
    buttons: [{
        text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_CSTU_STD.butClose","关闭") ,
        iconCls:"close",
        handler:'onPanelClose'
    }]
});
