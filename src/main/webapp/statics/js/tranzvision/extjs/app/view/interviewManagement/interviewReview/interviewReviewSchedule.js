Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewSchedule', {
    extend: 'Ext.panel.Panel',
    xtype: 'interviewReviewSchedule',
    controller: 'interviewReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.chart.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'Ext.ux.MaximizeTool',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.interviewManagement.interviewReview.interviewReviewScheduleJudgeStore',
        'KitchenSink.view.interviewManagement.interviewReview.interviewReviewScheduleAppsStore'
    ],
    title: '进度管理',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    constructor: function (classID,batchID,transValue){
        this.classID=classID;
        this.batchID=batchID;
        this.transValue = transValue;
        this.callParent();
    },
    initComponent:function(){
        var classID =this.classID;
        var batchID = this.batchID;
        var statisticsGridDataModel;
        var transValue = this.transValue;
        var interviewReviewScheduleChartStore=new Ext.data.JsonStore({
            fields:['name','data1'],
            data:[]
        });

        var interviewReviewScheduleChartStore2=new Ext.data.JsonStore({
            fields:[],
            data:[]
        });
        var dockedItemBtn,columnsItems;
        var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_PLAN_STD",' +
            '"OperateType":"IFS","comParams":{"type":"IFP","classID":"'+this.classID+'","batchID":"'+this.batchID+'"}}';
        Ext.tzLoadAsync(tzParams,function(respData){
            dockedItemBtn = respData.ifScore==="Y"?{
                xtype: "toolbar",
                items: [
                    {text: "计算标准差", tooltip: "计算标准差", handler: "calculate"}
                ]
            }:null;
            columnsItems = respData.ifScore==="Y"?[
                {
                    text: "报名表编号",
                    dataIndex: 'insID',
                    align:'center',
                    minWidth: 150
                },
                {
                    header: "姓名",
                    dataIndex: 'name',
                    align:'center',
                    minWidth: 100,
                    xtype:'linkcolumn',
                    handler:'viewThisApplicationForm',
                    renderer:function(v){
                        this.text=v;
                        return ;
                    }
                } ,
                {
                    text: "性别",
                    dataIndex: 'gender',
                    align:'center',
                    minWidth: 30,
                    renderer: function (v) {
                        var x;
                        var genderStore = transValue.get("TZ_GENDER");
                        if(x = genderStore.find('TValue',v)>=0){
                            x=+x;
                            return genderStore.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }
                },{
                    text: "评委间偏差",
                    dataIndex: 'judgePC',
                    align:'center',
                    minWidth: 130,
                    flex:1
                },
                {
                    text: "评委信息",
                    dataIndex: 'judgeNames',
                    name:'judgeNames',
                    align:'center',
                    minWidth: 100,
                    flex:1,
                    renderer:function(v) {
                        if (v) {
                            return '<a class="tz_lzh_interviewReview_app" href = "javaScript:void(0)">' + v + '</a>';
                        } else {
                            return "";
                        }
                    }
                },
                {
                    text: "评审状态",
                    dataIndex: 'judgeStatus',
                    align:'center',
                    minWidth: 150,
                    flex:1,
                    renderer: function (v,grid,record) {
                        var x;
                        var KSPWPSEHNZT = transValue.get("TZ_MSPS_KSZT");
                        if((x = KSPWPSEHNZT.find('TValue',v))>=0){
                            return v==='N' ? KSPWPSEHNZT.getAt(x).data.TSDesc+"("+record.data.progress+")":KSPWPSEHNZT.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }
                } ,
                {
                    text: "录取状态",
                    dataIndex: 'status',
                    align:'center',
                    minWidth: 150,
                    renderer: function (v) {
                        var x;
                        var admitStore = transValue.get("TZ_LUQU_ZT");
                        if((x = admitStore.find('TValue',v))>=0){
                            return admitStore.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }
                }
            ]:[
                {
                    text: "报名表编号",
                    dataIndex: 'insID',
                    align:'center',
                    minWidth: 150
                },
                {
                    header: "姓名",
                    dataIndex: 'name',
                    align:'center',
                    minWidth: 100,
                    xtype:'linkcolumn',
                    handler:'viewThisApplicationForm',
                    renderer:function(v){
                        this.text=v;
                        return ;
                    }
                } ,
                {
                    text: "性别",
                    dataIndex: 'gender',
                    align:'center',
                    minWidth: 30,
                    renderer: function (v) {
                        var x;
                        var genderStore = transValue.get("TZ_GENDER");
                        if(x = genderStore.find('TValue',v)>=0){
                            x=+x;
                            return genderStore.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }
                },
                {
                    text: "评委信息",
                    dataIndex: 'judgeNames',
                    name:'judgeNames',
                    align:'center',
                    minWidth: 100,
                    flex:1,
                    renderer:function(v) {
                        if (v) {
                            return '<a class="tz_lzh_interviewReview_app" href = "javaScript:void(0)">' + v + '</a>';
                        } else {
                            return "";
                        }
                    }
                },
                {
                    text: "评审状态",
                    dataIndex: 'judgeStatus',
                    align:'center',
                    minWidth: 150,
                    flex:1,
                    renderer: function (v,grid,record) {
                        var x;
                        var KSPWPSEHNZT = transValue.get("TZ_MSPS_KSZT");
                        if((x = KSPWPSEHNZT.find('TValue',v))>=0){
                            return v==='N' ? KSPWPSEHNZT.getAt(x).data.TSDesc+"("+record.data.progress+")":KSPWPSEHNZT.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }
                } ,
                {
                    text: "录取状态",
                    dataIndex: 'status',
                    align:'center',
                    minWidth: 150,
                    renderer: function (v) {
                        var x;
                        var admitStore = transValue.get("TZ_LUQU_ZT");
                        if((x = admitStore.find('TValue',v))>=0){
                            return admitStore.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }
                }
            ];
        });
        //柱状图chart和曲线图chart
       var columnChart;
        var lineChart;
        columnChart = new Ext.chart.Chart({
            width: 860,
            height: 400,
            animate: true,
            store: interviewReviewScheduleChartStore,
            shadow: true,
            axes: [{
                type: 'Numeric',
                position: 'left',
                fields: ['data1'],
                minimum: 0,
                maximum:100,
                label:{renderer: function(value){return Ext.util.Format.number(value,'000.00');}},
                title: '平均分',
                grid: true,
                minimum: 0
            }, {
                type: 'Category',
                position: 'bottom',
                fields: ['name'],
                title: '评委列表'
            }],
            series: [{
                type: 'column',
                axis: 'bottom',
                highlight: true,
                xField: 'name',
                yField: 'data1'
            }]
        });
        lineChart = new Ext.chart.Chart({
            xtype: 'chart',
            width: 860,
            height: 800,
            style: 'background:#fff',
            animate: true,
            store: interviewReviewScheduleChartStore2,
            shadow: true,
            theme: 'Category1',
            legend: {position: 'top'},
            axes: [
                {
                    type: 'Numeric',
                    position: 'left',
                    fields: [chartfields],
                    label:{renderer: function(value){return Ext.util.Format.number(value,'000.00');}},
                    title: '分布比率',
                    grid: true,
                    maximum:100,
                    minimum:0
                },
                {
                    type: 'Category',
                    position: 'bottom',
                    fields: ['fbName'],
                    title: '分布区间',
                    label: {rotate: {degrees: 270}}
                }
            ]
        });
        //添加两个空的图表，解决没有内容时伸缩和点击放大后添加不进数据的问题

        var mainPageFrame = Ext.create('Ext.Panel',
            {
                xtype:'form',
                title: '图表区',
                collapsible:true,
                collapsed:true,
                plugins: {
                    ptype:'maximize'
                },
                name:'averageChart',
                items: [
                    lineChart
                ]
            });


        //柱状图和曲线图最大值最小值
        var columnMaxCount = 100;
        var columnMinCount = 0;
        var lineMaxCount = 100;
        var lineMinCount = 0;
        var chartfields;

        var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_PLAN_STD",' +
            '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'","getPwDfData":"true"}}';

        Ext.tzLoadAsync(tzParams,function(respData) {
            var tmpArray = respData.pw_dfqk_grid;

            statisticsGridDataModel = {gridFields:[],gridColumns:[],gridData:[]};
            var tmpObject = {columns:[]};
            for(var i=0;i<tmpArray.length;i++)
            {
                var colName = '00' + (i + 1);
                colName = 'col' + colName.substr(colName.length - 2);

                var tmpColumn = {
                    text     : tmpArray[i][colName],
                    sortable : false,
                    dataIndex: colName,
                    flex:1
                };

                statisticsGridDataModel['gridColumns'].push(tmpColumn);
                statisticsGridDataModel['gridFields'].push({name:colName});
            }

            tmpArray = respData.pw_dfqk_grid_data;

            for(var i=0;i<tmpArray.length;i++)
            {
                var tmpdataArray = tmpArray[i].field_value;
                var dataRow = [];
                for(var j=0;j<tmpdataArray.length;j++) {
                    var colName = '00' + (j + 1);
                    colName = 'col' + colName.substr(colName.length - 2);
                    dataRow.push(tmpdataArray[j][colName]);
                }
                statisticsGridDataModel['gridData'].push(dataRow);
            }


        });

        Ext.apply(this,{
            items: [
                {
                    xtype: 'form',
                    reference: 'interviewProgressForm',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    border: false,
                    bodyPadding: 10,
                    bodyStyle: 'overflow-y:auto;overflow-x:hidden',

                    fieldDefaults: {
                        labelWidth: 110,
                        labelStyle: 'font-weight:bold'
                    },

                    items: [
                        {
                            xtype: 'textfield',
                            name: 'classID',
                            hidden: true
                        },
                        {
                            xtype: 'textfield',
                            name: 'batchID',
                            hidden: true
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: "报考班级",
                            name: 'className',
                            fieldStyle:'background:#F4F4F4',
                            readOnly:true
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: "批次",
                            name: 'batchName',
                            fieldStyle:'background:#F4F4F4',
                            readOnly:true
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: "报考考生数量",
                            name: 'totalStudents',
                            fieldStyle:'background:#F4F4F4',
                            readOnly:true
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: "材料评审考生",
                            name: 'materialStudents',
                            fieldStyle:'background:#F4F4F4',
                            readOnly:true
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: "面试评审考生",
                            name: 'interviewStudents',
                            fieldStyle:'background:#F4F4F4',
                            readOnly:true
                        },
                        {
                            xtype: 'fieldset',
                            title: '<span style="font-weight: bold;">阶段状态</span>',
                            collapsible: true,
                            defaults: {
                                labelWidth:120,
                                anchor: '100%',
                                layout: {
                                    type: 'hbox',
                                    defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
                                }
                            },
                            items:[{
                                layout: {
                                    type: 'column'
                                } ,
                                padding:'0 0 8px 0',
                                items:[
                                    //使用layout form嵌套以避免IE中出现错位的BUG
                                    {
                                        columnWidth:.5,
                                        layout:'form',
                                        items:[{
                                            xtype: 'datefield',
                                            fieldLabel: "开始日期",
                                            name: 'startDate',
                                            itemId: 'startDate',
                                            labelWidth:120,
                                            endDateField: 'endDate',
                                            vtype: 'daterange',
                                            repeatTriggerClick:true,
                                            format: 'Y-m-d'
                                        }]
                                    }, {
                                        columnWidth:.5,
                                        layout:'form',
                                        items:[{
                                            xtype: 'timefield',
                                            fieldLabel: "时间",
                                            labelWidth:38,
                                            style:'margin-left:5px',
                                            name: 'startTime',
                                            format: 'H:i'
                                        }]
                                    }
                                ]},{
                                layout: {
                                    type: 'column'
                                } ,
                                padding:'0 0 8px 0',
                                items:[
                                    //使用layout form嵌套以避免IE中出现错位的BUG
                                    {
                                        columnWidth:.5,
                                        layout:'form',
                                        items:[{
                                            xtype: 'datefield',
                                            fieldLabel: "结束日期",
                                            labelWidth:120,
                                            name: 'endDate',
                                            itemId: 'endDate',
                                            startDateField: 'startDate',
                                            vtype: 'daterange',
                                            format: 'Y-m-d',
                                            repeatTriggerClick:true
                                        }]
                                    },
                                    {
                                        columnWidth:.5,
                                        layout:'form',
                                        items:[{
                                            xtype: 'timefield',
                                            fieldLabel: "时间",
                                            labelWidth:38,
                                            style:'margin-left:5px',
                                            name: 'endTime',
                                            format: 'H:i'
                                        }]
                                    }
                                ]
                            },
                                {
                                    style:'margin:0px 8px 8px 0px',
                                    items:[{
                                        style:'margin-right:20px',
                                        xtype: 'button',
                                        flagType:'positive',
                                        defaultColor:'',
                                        name:'startup',
                                        setType:0,
                                        text: '启动',
                                        handler: 'startInterview',
                                        width: 100
                                    },{
                                        style:'margin-left:0px',
                                        xtype:'button',
                                        text:'关闭',
                                        name:'finish',
                                        flagType:'positive',
                                        defaultColor:'',
                                        setType:0,
                                        handler:'endInterview',
                                        width:100
                                    },{
                                        xtype:'displayfield',
                                        style:'margin-left:2em',
                                        fieldLabel:"当前评议状态",
                                        name:'interviewStatus',
                                        ignoreChangesFlag: true
                                    }]

                                },
                                {
                                    xtype: 'displayfield',
                                    fieldLabel: "评议进度",
                                    style:'display:inline-block',
                                    name:'progress',
                                    renderer:function(v){
                                        return v+' 人次';
                                    },
                                    readOnly: true
                                },{
                                    xtype: 'checkboxfield',
                                    fieldLabel: '评委可见统计表',
                                    name: 'judgeTJB',
                                    ignoreChangesFlag: true
                                },
                                {
                                    xtype: 'checkboxfield',
                                    fieldLabel: '评委可见分布图',
                                    name: 'judgeFBT',
                                    ignoreChangesFlag: true
                                }
                            ]
                        },
                        {
                            xtype: 'tabpanel',
                            frame: true,
                            activeTab: 0,
                            plain: false,
                            resizeTabs: true,
                            defaults: {
                                autoScroll: false
                            },
                            listeners:{
                                tabchange:function(tabPanel, newCard, oldCard){
                                    if (newCard.name == "statisticalInfoForm"){
                                        var form = tabPanel.findParentByType('interviewReviewSchedule').child('form').getForm();

                                    }
                                }
                            },
                            items: [
                                {
                                    title: '评委信息',
                                    xtype: 'form',
                                    name:'judgeFormInfo',
                                    style:'margin-left:0',
                                    layout: {
                                        type: 'vbox',
                                        align: 'stretch'
                                    },
                                    items: [{
                                        layout: {
                                            type: 'column'
                                        } ,
                                        //padding:'10px 0 5px 10px',
                                        items:[
                                            {
                                                margin:'8px',
                                                xtype: 'displayfield',
                                                value: "<b>要求每位考生被</b>",
                                                labelWidth:1
                                            },
                                            {
                                                xtype: 'numberfield',
                                                margin:'8px',
                                                labelWidth:1,
                                                name: 'judgeCount',
                                                minValue:0,
                                                style:'margin-left:5px;margin-right:5px;',
                                                negativeText:'评委数量不能为负数',
                                                nanText:'{0}不是有效的数字',
                                                hideLabel: true,
                                                width:130,
                                                ignoreChangesFlag:true
                                            },{
                                                margin:'8px',
                                                xtype: 'displayfield',
                                                //style:'margin-left:1px',
                                                value: "<b>个评委审批</b>",
                                                //hideLabel: true,
                                                labelWidth:70
                                            }
                                        ]},
                                        {
                                            xtype: 'grid',
                                            minHeight: 150,
                                            autoHeight:true,
                                            name:'interviewJudgeGrid',
                                            border:false,
                                            store:{
                                                type:'interviewReviewScheduleJudgeStore'
                                            },
                                            multiSelect: true,
                                            selModel: {
                                                selType: 'checkboxmodel'
                                            },
                                            dockedItems: [
                                                {
                                                    xtype: "toolbar",
                                                    items: [
                                                        {text: "暂停", tooltip: "暂停", handler: "pause"},
                                                        "-",
                                                        {text: "设置评委状态为正常", tooltip: "设置评委状态为正常", handler: "setUsual"},
                                                        "-",
                                                        {text: "提交评委数据", tooltip: "提交评委数据",  handler: "submitData"},
                                                        "-",
                                                        {text: "设置提交状态为未提交", tooltip: "设置提交状态为未提交", handler: "setNoSubmit"},
                                                        "-",
                                                        {text: "撤销面试数据", tooltip: "撤销面试数据", handler: "revokeData"},
                                                        "-",
                                                        {text: "打印评分总表",tooltip: "打印评分总表" , handler: "printChart"}

                                                    ]
                                                }
                                            ],
                                            columns: [{
                                                    width:1
                                                }, {
                                                    xtype: 'rownumberer',
                                                    width: 50,
                                                    align:'center'
                                                },
                                                {
                                                    text: "评委帐号",
                                                    dataIndex: 'judgeID',
                                                    minWidth: 100,
                                                    align:'center',
                                                    flex: 1
                                                },
                                                {
                                                    text: "姓名",
                                                    dataIndex: 'judgeName',
                                                    minWidth: 100,
                                                    align:'center',
                                                    flex: 1
                                                },
                                                {
                                                    text: "评委组编号",
                                                    dataIndex: 'judgeGroup',
                                                    minWidth: 100,
                                                    align:'center',
                                                    flex: 1
                                                },
                                                {
                                                    text: "抽取数量/已提交数量",
                                                    dataIndex: 'hasSubmited',
                                                    align:'center',
                                                    minWidth: 100,
                                                    flex: 1
                                                },
                                                {
                                                    text: "提交状态",
                                                    dataIndex: 'submitYN',
                                                    minWidth: 100,
                                                    align:'center',
                                                    flex: 1,
                                                    renderer: function (v) {
                                                        var x;
                                                        var judgeStatus = transValue.get("TZ_MSPS_ZT");
                                                        if((x = judgeStatus.find('TValue',v))>=0){
                                                            return judgeStatus.getAt(x).data.TSDesc;
                                                        }else{
                                                            return v;
                                                        }
                                                    }
                                                },
                                                {
                                                    text: "账户状态",
                                                    name:'accountStatus',
                                                    dataIndex: 'accountStatus',
                                                    minWidth: 100,
                                                    align:'center',
                                                    flex: 1,
                                                    renderer:function(v){
                                                        var x;
                                                        var jugaccStatusStore = transValue.get("TZ_JUGACC_STATUS");
                                                        if((x = jugaccStatusStore.find('TValue',v))>=0){
                                                            return jugaccStatusStore.getAt(x).data.TSDesc;
                                                        }else{
                                                            return v;
                                                        }
                                                    }
                                                }
                                            ]
                                        } ]},
                                {
                                    xtype: 'grid',
                                    title: '考生名单',
                                    minHeight: 260,
                                    name:'interviewStudentGrid',
                                    columnLines: true,
                                    autoHeight: true,
                                    listeners:{
                                        activate:'stuListActive'
                                    },
                                    selModel: {
                                        type: 'checkboxmodel'
                                    },
                                    store:{
                                        type:'interviewReviewScheduleAppsStore'
                                    },
                                    dockedItems: [dockedItemBtn]
                                    ,
                                    /*store: {
                                     type: 'interviewReviewApplicants'
                                     },*/
                                    columns: columnsItems
                                },{
                                    xtype: 'form',
                                    title: '统计信息',
                                    autoHeight:true,
                                    name:'statisticalInfoForm',
                                    items:[
                                         Ext.create('Ext.grid.Panel', {
                                            store: Ext.create('Ext.data.ArrayStore', {
                                                fields: statisticsGridDataModel['gridFields'],
                                                data: statisticsGridDataModel['gridData']
                                            }),
                                             minHeight:80,
                                            //stateful: true,
                                             selModel: {
                                                 type: 'checkboxmodel'
                                             },
                                            columns: statisticsGridDataModel['gridColumns'],
                                            header: false,
                                            border:false,
                                             dockedItems:[{
                                                 xtype:"toolbar",
                                                 dock: 'bottom',
                                                 items:[
                                                     {text:"刷新图表",tooltip:"刷新图表",iconCls: "refresh",handler:function(btn){
                                                         var form = btn.findParentByType('interviewReviewSchedule').child('form').getForm();
                                                         var classID = form.findField('classID').getValue();
                                                         var batchID = form.findField('batchID').getValue();
                                                         //评委登陆账号id用逗号分割
                                                         var pw_ids = "";
                                                         var selList = btn.findParentByType('grid').getSelectionModel().getSelection();
                                                         for(var x=0;x<selList.length;x++) {
                                                             if(pw_ids == ""){
                                                                 pw_ids = selList[x].get("col01");
                                                             }else{
                                                                 pw_ids = pw_ids + "," + selList[x].get("col01");
                                                             }
                                                         }

                                                         var tzParams='{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_PLAN_STD","OperateType":"QG",' +
                                                             '"comParams":{"type":"chart","classID":"'+classID+'","batchID":"'+batchID+'","pw_ids":"'+pw_ids+'"}}';

                                                         Ext.tzLoad(tzParams,function(respData) {

                                                             //统计分布图表series
                                                             var seriesArray = [];
                                                             //统计图表fields定义
                                                             chartfields = "'name'";
                                                             var series1 ;
                                                             tmpArray = respData.pw_dfqk_chart_field;
                                                             var tips;
                                                             for(var i=0;i<tmpArray.length;i++) {
                                                                 chartfields = chartfields + ",'" + tmpArray[i].pw_field+"'";
                                                                 tips = tmpArray[i].pw_field;
                                                                 var fieldName = tmpArray[i].pw_field;

                                                                 series1 = {
                                                                     type: 'line',
                                                                     highlight: {size: 7,radius: 7},
                                                                     axis: 'left',
                                                                     smooth: true,
                                                                     xField: 'fbName',
                                                                     yField: tips,
                                                                     markerConfig: {type: 'circle',size: 4,radius: 4,'stroke-width': 0},
                                                                     title: tmpArray[i].pw_name/*,
                                                                      tips: {
                                                                      trackMouse: true,
                                                                      width: 180,
                                                                      renderer: function(storeItem, item)
                                                                      {
                                                                      this.setTitle('分布区间[' + storeItem.get('fbName') + ']<br>分布比率: '+item.data+'-' + Ext.util.Format.number(storeItem.get(fieldName),'000.00'));
                                                                      }
                                                                      }*/
                                                                 };
                                                                 seriesArray.push(series1);
                                                             }
                                                             interviewReviewScheduleChartStore2.fields=[chartfields];

                                                             interviewReviewScheduleChartStore.loadData(respData.columnChart);

                                                             //计算最大值
                                                             var arrData = respData.columnChart;
                                                             for (i = 0; i < arrData.length; i++) {
                                                                 if(columnMaxCount < arrData[i].data1){
                                                                     columnMaxCount = arrData[i].data1;
                                                                 }
                                                             }
                                                             // 统计分布图;
                                                             interviewReviewScheduleChartStore2.loadData(respData.lineChart);

                                                             columnChart = new Ext.chart.Chart({
                                                                 width: 860,
                                                                 height: 400,
                                                                 animate: true,
                                                                 store: interviewReviewScheduleChartStore,
                                                                 shadow: true,
                                                                 axes: [{
                                                                     type: 'Numeric',
                                                                     position: 'left',
                                                                     fields: ['data1'],
                                                                     minimum: columnMinCount,
                                                                     maximum:columnMaxCount,
                                                                     label:{renderer: function(value){return Ext.util.Format.number(value,'000.00');}},
                                                                     title: '平均分',
                                                                     grid: true,
                                                                     minimum: 0
                                                                 }, {
                                                                     type: 'Category',
                                                                     position: 'bottom',
                                                                     fields: ['name'],
                                                                     title: '评委列表'
                                                                 }],
                                                                 series: [{
                                                                     type: 'column',
                                                                     axis: 'bottom',
                                                                     highlight: true,
                                                                     xField: 'name',
                                                                     yField: 'data1'
                                                                 }]
                                                             });

                                                             lineChart = new Ext.chart.Chart({
                                                                 xtype: 'chart',
                                                                 width: 860,
                                                                 height: 400,
                                                                 style: 'background:#fff',
                                                                 animate: true,
                                                                 store: interviewReviewScheduleChartStore2,
                                                                 shadow: true,
                                                                 theme: 'Category1',
                                                                 legend: {position: 'top'},
                                                                 axes: [
                                                                     {
                                                                         type: 'Numeric',
                                                                         position: 'left',
                                                                         fields: [chartfields],
                                                                         label:{renderer: function(value){return Ext.util.Format.number(value,'000.00');}},
                                                                         title: '分布比率',
                                                                         grid: true,
                                                                         maximum:lineMaxCount,
                                                                         minimum:lineMinCount
                                                                     },
                                                                     {
                                                                         type: 'Category',
                                                                         position: 'bottom',
                                                                         fields: ['fbName'],
                                                                         title: '分布区间',
                                                                         label: {rotate: {degrees: 270}}
                                                                     }
                                                                 ],
                                                                 series: seriesArray
                                                             });
                                                             mainPageFrame.removeAll();
                                                             if(respData.display_pjf == 'Y'){
                                                                 mainPageFrame.add(columnChart);
                                                             }
                                                             mainPageFrame.add(lineChart);
                                                             mainPageFrame.expand(true);
                                                             mainPageFrame.doLayout();

                                                         });

                                                         //this.doLayout();
                                                     }}
                                                 ]
                                             }],
                                            viewConfig: {
                                                enableTextSelection: true
                                            }
                                        }),
                                        mainPageFrame]
                                }]
                        }
                    ]
                }
            ]

        });
        this.callParent();

    },
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onScheduleSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onScheduleEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onScheduleClose'
    }]
});
