Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewSchedule', {
    extend: 'Ext.panel.Panel',
    xtype: 'materialsReviewSchedule',
    controller: 'materialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'Ext.ux.MaximizeTool',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.materialsReview.materialsReview.materialsReviewScheduleJudgeStore',
        'KitchenSink.view.materialsReview.materialsReview.materialsReviewScheduleAppJudgeWindow',
        'KitchenSink.view.materialsReview.materialsReview.materialsReviewScheduleAppsStore'
    ],
    title: '进度管理',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    constructor: function (classID,batchID){
        this.classID=classID;
        this.batchID=batchID;
        this.callParent();
    },
    initComponent:function(){
        var classID =this.classID;
        var batchID = this.batchID;
        var statisticsGridDataModel;
        var msyrtislReviewScheduleChartStore=new Ext.data.JsonStore({
            fields:['name','data1'],
            data:[]
        });

        var msyrtislReviewScheduleChartStore2=new Ext.data.JsonStore({
            fields:[],
            data:[]
        });
        var materialsReviewScheduleAppsStore=new KitchenSink.view.materialsReview.materialsReview.materialsReviewScheduleAppsStore();
        var materialsReviewScheduleJudgeStore=new KitchenSink.view.materialsReview.materialsReview.materialsReviewScheduleJudgeStore();


        //柱状图chart和曲线图chart
        var columnChart;
        var lineChart;

        columnChart = new Ext.chart.Chart({
            width: 860,
            height: 400,
            animate: true,
            store: msyrtislReviewScheduleChartStore,
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
            store: msyrtislReviewScheduleChartStore2,
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

        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD",' +
            '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'","getPwDfData":"true"}}';
        console.log(tzParams);
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
        var applicantsColumns =[
            {
                text: "报名表编号",
                dataIndex: 'insID',
                align:'center',
                minWidth: 100
            },{
                text: "姓名",
                dataIndex: 'name',
                align:'center',
                minWidth: 100,
                xtype:'linkcolumn',
                handler:'viewThisApplicationForm',
                renderer:function(v){
                    this.text=v;
                    return ;
                }

            },{
                text: "性别",
                dataIndex: 'gender',
                align:'center',
                minWidth: 30,
                renderer: function (v) {
                    switch (v) {
                        case 'M':
                            return '男';
                        case 'F':
                            return '女';
                    }
                }
            },{
                text: "面试资格",
                dataIndex: 'viewQUA',
                align:'center',
				 minWidth: 80,
                width: 100

            },
            {
                text: "评委信息",
                dataIndex: 'judgeInfo',
                align:'center',
                name:'judgeInfo',
                //id:'judgeInfo',
                width: 100,
                flex:1,
                renderer:function(v) {
                    if (v) {
                        return '<a class="tz_materialsReview_app" href = "javaScript:void(0)" >' + v + '</a>';
                    } else {
                        return "";
                    }},
                listeners:{
                    click:'viewJudge'
                }
            },
            {
                text: "评审状态",
                dataIndex: 'judgeStatus',
                align:'center',
                width: 200
            }
        ];
        var dockedItems;


        var tzAppColsParams ='{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD",' +
            '"OperateType":"isJiSuanFenZhi","comParams":{"type":"isJiSuanFenZhi","classID":"'+classID+'","batchID":"'+batchID+'"}}';
        Ext.tzLoadAsync(tzAppColsParams,function(respData){
            var transScoreValue=respData.ZFZ;
            if(transScoreValue=='Y'){
                applicantsColumns.push({
                    text: "评委间偏差",
                    dataIndex: 'judgePC',
                    align:'center',
                    width: 130,
                    flex:1
                });
                applicantsColumns.push({
                    text: "平均分",
                    dataIndex: 'aveScore',
                    align:'center',
                    width: 100,
                    flex:1
                });
                dockedItems = [
                    {
                        xtype: "toolbar",
                        items: [
                            {text: "计算偏差", tooltip: "计算偏差", handler: "calDeviation"}
                        ]
                    }
                ]
            }
        });
        Ext.apply(this,{
            items: [
                {
                    xtype: 'form',
                    reference: 'materialsProgressForm',
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
                            fieldStyle: 'background:#F4F4F4',
                            readOnly: true,
                            labelWidth:150
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: "批次",
                            name: 'batchName',
                            fieldStyle: 'background:#F4F4F4',
                            readOnly: true,
                            labelWidth:150
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: "报考考生数量",
                            name: 'totalStudents',
                            fieldStyle: 'background:#F4F4F4',
                            readOnly: true,
                            labelWidth:150
                        },
                        {
                            xtype: 'numberfield',
                            fieldLabel: "已选择材料评审考生",
                            name: 'materialStudents',
                            fieldStyle: 'background:#F4F4F4',
                            readOnly: true,
                            labelWidth:150
                        }, {
                            xtype: 'hiddenfield',
                            fieldLabel: "是否合格",
                            name: 'strWaring'
                        },
                        {
                            xtype: 'fieldset',
                            title: '阶段状态',
                            collapsible: true,
                            defaults: {
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
                                    {
                                        margin:'8px',
                                        xtype: 'displayfield',
                                        value: "<b>目前是第</b>",
                                        labelWidth:1
                                    },
                                    {
                                        xtype: 'displayfield',
                                        margin:'8px',
                                        readOnly: true,
                                        name: 'delibCount',
                                        minValue:0,
                                        style:'margin-left:5hpx;margin-right:2px;border:none',
                                        negativeText:'评审次数必须为正整数',
                                        nanText:'{0}不是有效的数字',
                                        hideLabel: true,
                                        width:20
//                                        listeners:{
//                                            change:function(field, newValue, oldValue, eOpts ){
//                                                var form = field.findParentByType("setMaterialsReviewRule").lookupReference("CountForm").getForm();
//                                                var reviewApplicantsCount = form.findField("materialsReviewApplicantsNumber").getValue();
//                                                var reviewCountAll = reviewApplicantsCount* parseInt(newValue);
//                                                form.findField("reviewCountAll").setValue(reviewCountAll);
//                                            }
//                                        }
                                    },{
                                        margin:'8px',
                                        xtype: 'displayfield',
                                        //style:'margin-left:1px',
                                        value: "<b>次评审，当前评审状态是：</b>",
                                        //hideLabel: true,
                                        labelWidth:70
                                    },{
                                        margin:'8px',
                                        xtype: 'displayfield',
                                        name: 'status',
                                        style:'display:inline-block',
                                        readOnly: true
//                                        renderer: function (v) {
//                                            if (v == 'A') {
//                                                return '进行中'
//                                            }
//                                            if (v == 'B') {
//                                                return '已结束'
//                                            }
//                                            if (v == 'N') {
//                                                return '未开始'
//                                            }
//                                        }
                                    },{
                                        margin:'8px 0 0 10px',
                                        xtype: 'displayfield',
                                        value: "<b>本次评审的评审进度是</b>",
                                        labelWidth:1
                                    },
                                    {
                                        xtype: 'displayfield',
                                        fieldLabel: "",
                                        margin:'8px',
                                        name: 'progress',
                                        style:'display:inline-block',
                                        readOnly: true,
                                        hideLabel:true,
                                        width:'40px'
                                    },
                                    {
                                        margin:'8px',
                                        xtype: 'displayfield',
                                        value: "<b>人次</b>",
                                        labelWidth:1
                                    }
                                ]},{
                                layout: {
                                    type: 'column'
                                } ,
                                padding:'0 0 8px 0',
                                items:[{
                                    style:'margin-left:5px',
                                    xtype: 'button',
                                    flagType:'positive',
                                    name:'startNewReview',
                                    defaultColor:'',
                                    setType:0,
                                    text: '开启新一轮评审',
                                    handler: 'startNewReview',
                                    width: 150
                                },{
                                    style:'margin-left:10px',
                                    xtype:'button',
                                    text:'关闭',
                                    defaultColor:'',
                                    name:'closeReview',
                                    flagType:'positive',
                                    setType:0,
                                    handler:'closeReview',
                                    width:90
                                },{
                                    style:'margin-left:10px',
                                    xtype:'button',
                                    text:'重新开启本轮评审',
                                    defaultColor:'',
                                    name:'reStartReview',
                                    flagType:'positive',
                                    setType:0,
                                    handler:'reStartReview',
                                    width:150
                                },{
                                    xtype: 'checkboxfield',
                                    fieldLabel: '评委可见统计表',
                                    name: 'judgeTJB',
                                    hidden:true
                                },
                                    {
                                        xtype: 'checkboxfield',
                                        fieldLabel: '评委可见分布图',
                                        name: 'judgeFBT',
                                        hidden:true
                                    }]
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
                                    if (newCard.name == "materialsStudentGrid"){
                                        var form = tabPanel.findParentByType('materialsReviewSchedule').child('form').getForm();
                                        var store = newCard.store;
                                        console.log(store);
                                        var classID = form.findField('classID').getValue();
                                        var batchID = form.findField('batchID').getValue();
                                        if(store.isLoaded()==false) {
                                            var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD","OperateType":"QG",' +
                                                '"comParams":{"type":"stuList","classID":"' + classID + '","batchID":"' + batchID + '"}}';
                                            Ext.tzLoad(tzParams, function (respData) {
                                              // store.loadData(respData.root);
                                            });
                                            this.doLayout();
                                       }
                                    };
                                    if (newCard.name == "judgeInfoForm"){
                                        var form = tabPanel.findParentByType('materialsReviewSchedule').child('form').getForm();
                                        var store = newCard.child('grid').store;
                                        console.log(store);
                                        var classID = form.findField('classID').getValue();
                                        var batchID = form.findField('batchID').getValue();
                                       if(store.isLoaded()==false) {
                                            var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD","OperateType":"QG",' +
                                                '"comParams":{"type":"judgeInfo","classID":"' + classID + '","batchID":"' + batchID + '"}}';
                                            Ext.tzLoad(tzParams, function (respData) {
                                               // store.loadData(respData.root);
                                            });

                                            this.doLayout();
                                        }
                                    }
                                }
                            },
                            items: [
                                {
                                    title: '评委信息',
                                    xtype: 'form',
                                    name:'judgeInfoForm',
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

                                                xtype: 'displayfield',
                                                margin:'8px',
                                                readOnly: true,
                                                name: 'reviewCount',
                                                style:'display:inline-block'
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
                                            name:'materialsJudgeGrid',
                                            border:false,
                                            plugins: {
                                                ptype: 'cellediting',
                                                clicksToEdit: 1
                                            },
                                            store:{
                                                type:'materialsReviewScheduleJudgeStore'
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
                                                        {text: "设置评委状态为正常", tooltip: "设置评委状态为正常", handler: "setNoaml"},
                                                        "-",
                                                        {text: "提交评委数据", tooltip: "提交评委数据",  handler: "submitData"},
                                                        "-",
                                                        {text: "设置提交状态为未提交", tooltip: "设置提交状态为未提交", handler: "setNoSubmit"},
                                                        "-",
                                                        {text: "撤销评议数据", tooltip: "撤销评议数据", handler: "revokeData"},
                                                        "-",
                                                        {text: "打印评分总表", tooltip: "打印评分总表", handler: "printChart"}

                                                    ]
                                                }
                                            ],
                                            columns: [
                                               {
                                                    width:1
                                                }, {
                                                    text: "选择",
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
                                                    text: "描述",
                                                    dataIndex: 'judgeName',
                                                    minWidth: 100,
                                                    align:'center',
                                                    flex: 1
                                                },
                                                {
                                                    text: "评委组",
                                                    dataIndex: 'judgeGroup',
                                                    minWidth: 100,
                                                    align:'center',
                                                    flex: 1
                                                },{
                                                    text: "评审考生人数",
													 align:'center',
                                                    dataIndex: 'lower',
                                                    width:130
                                                }
//                                                ,{
//                                                    text: "评审考生上限",
//                                                    dataIndex: 'upper',
//                                                    width:130
//                                                }
                                                , {
                                                    text: "抽取数量/已评审数量",
                                                    dataIndex: 'hasSubmited',
                                                    align:'center',
                                                    minWidth: 100,
                                                    flex: 1
                                                },
                                                {
                                                    text: "提交状态",
                                                    dataIndex: 'submitYN',
                                                    minWidth: 80,
                                                    align:'center',
                                                    flex: 1,
                                                     renderer:function(v){
                                                        if (v == 'Y') {
                                                            return '已提交'
                                                        } if (v == 'N') {
                                                             return '未提交'
                                                         }if (v == 'C') {
                                                            return '被撤销'
                                                        } if (v=='') {
                                                             return '未提交'
                                                         }
                                                     }
//                                                    renderer: function (v) {
//                                                        if (v == 'N') {
//                                                            return '未提交'
//                                                        } if (v == 'Y') {
//                                                            return '已评审'
//                                                        }
//                                                        if (v == 'C') {
//                                                            return '被撤销'
//                                                        }
//                                                    }
                                                },
                                                {
                                                    text: "评委状态",
                                                    dataIndex: 'accountStatus',
                                                    minWidth: 80,
                                                    align:'center',
                                                    flex: 1,
                                                    renderer: function (v) {
                                                        if (v == 'A') {
                                                            return '正常'
                                                        }
                                                        if (v == 'B') {
                                                            return '暂停'
                                                        }
                                                        if (v == 'N'){
                                                            return '新建'
                                                        }
                                                    },
                                                    editable: false
                                                }
                                            ]
                                        } ]},
                                {
                                    xtype: 'grid',
                                    title: '考生名单',
                                    minHeight: 260,
                                    name:'materialsStudentGrid',
                                    reference:'materialsReviewAppsGrid',
                                    columnLines: true,
                                    autoHeight: true,
                                    selModel: {
                                        type: 'checkboxmodel'
                                    },
                                    listeners:{
                                        activate:'stuListActive'
                                    },
                                    store:{
                                        type:'materialsReviewScheduleAppsStore'
                                    },
                                    plugins: [{
                                        ptype: 'cellediting',
                                        pluginId: 'judgeCellEditing',
                                        clicksToEdit: 1,
                                        listeners:{
                                            beforeedit:function( editor, context, eOpts ){
                                                if(context.field=="judgeID"&&context.value.length>0&&!context.record.isModified('judgeID')){
                                                    return false;
                                                }
                                            }
                                        }
                                    }],
                                    dockedItems: dockedItems,
                                    columns:applicantsColumns
                                },
                                {
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
                                                    {text:"刷新图表",tooltip:"刷新图表",iconCls: "reset",handler:function(btn){
                                                        var form = btn.findParentByType('materialsReviewSchedule').child('form').getForm();
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


                                                        var tzParams='{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD","OperateType":"QG",' +
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
                                                            msyrtislReviewScheduleChartStore2.fields=[chartfields];

                                                            msyrtislReviewScheduleChartStore.loadData(respData.columnChart);

                                                            //计算最大值
                                                            var arrData = respData.columnChart;
                                                            for (i = 0; i < arrData.length; i++) {
                                                                if(columnMaxCount < arrData[i].data1){
                                                                    columnMaxCount = arrData[i].data1;
                                                                }
                                                            }
                                                            // 统计分布图;
                                                            msyrtislReviewScheduleChartStore2.loadData(respData.lineChart);

                                                            columnChart = new Ext.chart.Chart({
                                                                width: 860,
                                                                height: 400,
                                                                animate: true,
                                                                store: msyrtislReviewScheduleChartStore,
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
                                                                store: msyrtislReviewScheduleChartStore2,
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
                                }
                            ]
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

