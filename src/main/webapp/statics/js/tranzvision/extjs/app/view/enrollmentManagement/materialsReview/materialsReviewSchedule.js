Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewSchedule', {
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
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewController', 
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleJudgeStore', 
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleAppJudgeWindow', 
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleAppsStore'
    ],
    title: '进度管理',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    constructor: function(classID, batchID) {
        this.classID = classID;
        this.batchID = batchID;
        this.callParent();
    },
    initComponent: function() {
        var classID = this.classID;
        var batchID = this.batchID;
        var statisticsGridDataModel;
        var statisticsGoalGridDataModel;
        var msyrtislReviewScheduleChartStore = new Ext.data.JsonStore({
            fields: ['name', 'data1'],
            data: []
        });

        var msyrtislReviewScheduleChartStore2 = new Ext.data.JsonStore({
            fields: [],
            data: []
        });
        var materialsReviewScheduleAppsStore = new KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleAppsStore();
        var materialsReviewScheduleJudgeStore = new KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewScheduleJudgeStore();

        // 柱状图chart和曲线图chart
        var columnChart;
        var lineChart;

        columnChart = new Ext.chart.Chart({
            width: 1300,
            height: 230,
            animate: true,
            store: msyrtislReviewScheduleChartStore,
            shadow: true,
            axes: [{
                type: 'Numeric',
                position: 'left',
                fields: ['data1'],
                minimum: 0,
                maximum: 100,
                label: {
                    renderer: function(value) {
                        return Ext.util.Format.number(value, '00');
                    }
                },
                title: '平均分',
                grid: true,
                minimum: 0
            },
            {
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
            width: 1300,
            height: 230,
            style: 'background:#fff',
            animate: true,
            store: msyrtislReviewScheduleChartStore2,
            shadow: true,
            theme: 'Category1',
            legend: {
                position: 'top'
            },
            axes: [{
                type: 'Numeric',
                position: 'left',
                fields: [chartfields],
                label: {
                    renderer: function(value) {
                        return Ext.util.Format.number(value, '00');
                    }
                },
                title: '分布比率（%）',
                grid: true,
                maximum: 100,
                minimum: 0
            },
            {
                type: 'Category',
                position: 'bottom',
                fields: ['fbName'],
                title: '分布区间',
                label: {
                    rotate: {
                        degrees: 270
                    }
                }
            }]
        });
        // 添加两个空的图表，解决没有内容时伸缩和点击放大后添加不进数据的问题
        var mainPageFrame = Ext.create('Ext.Panel', {
            xtype: 'form',
            title: '图表区',
            collapsible: true,
            collapsed: true,
            plugins: {
                ptype: 'maximize'
            },
            name: 'averageChart',
            items: [columnChart,lineChart]
        });

        // 柱状图和曲线图最大值最小值
        var columnMaxCount = 100;
        var columnMinCount = 0;
        var lineMaxCount = 100;
        var lineMinCount = 0;
        var chartfields;

        var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD","OperateType":"QG","comParams":{"type":"getPwDfData" , "classID":"' + classID + '","batchID":"' + batchID + '"}}';

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
                console.log(tmpdataArray);
                statisticsGridDataModel['gridData'].push(dataRow);
            }
            //分布指标
            var tmpArray2 = respData.pw_fbzb_grid;
            statisticsGoalGridDataModel = {
                gridFields: [],
                gridColumns: [],
                gridData: []
            };

            var tmpObject2 = {
            	columns: []
            };
            
            for (i = 0; i < tmpArray2.length; i++) {
                var colName = '00' + (i + 1);
                colName = 'col' + colName.substr(colName.length - 2);

                var tmpColumn = {
                    text: tmpArray2[i][colName],
                    sortable: false,
                    dataIndex: colName,
                    editor: {
                        xtype: 'numberfield',
                        allowBlank: false,
                        emptyText:"不允许为空！"
                    },
                    flex: 1
                };

                if(i>0){
                	statisticsGoalGridDataModel['gridColumns'].push(tmpColumn);
                }
                statisticsGoalGridDataModel['gridFields'].push({
                    name: colName
                });
            }
            
            tmpArray2 = respData.pw_fbzb_grid_data;

            for (i = 0; i < tmpArray2.length; i++) {
                var tmpdataArray = tmpArray2[i].field_value;
                var dataRow = [];
                for (var j = 0; j < tmpdataArray.length; j++) {
                    var colName = '00' + (j + 1);
                    colName = 'col' + colName.substr(colName.length - 2);
                    dataRow.push(tmpdataArray[j][colName]);
                }
                statisticsGoalGridDataModel['gridData'].push(dataRow);
            }
        });

        var applicantsColumns = [{
            text: "报名表编号",
            dataIndex: 'insID',
            align: 'center',
            minWidth: 100
        },
        {
            text: "姓名",
            dataIndex: 'name',
            align: 'center',
            minWidth: 100,
            xtype: 'linkcolumn',
            handler: 'viewThisApplicationForm',
            renderer: function(v) {
                this.text = v;
                return;
            }

        },
        {
            text: "性别",
            dataIndex: 'gender',
            align: 'center',
            minWidth: 30,
            renderer: function(v) {
                switch (v) {
                case 'M':
                    return '男';
                case 'F':
                    return '女';
                }
            }
        },
        {
            text: "面试资格",
            dataIndex: 'viewQUA',
            align: 'center',
            minWidth: 80,
            width: 100

        },
        {
            text: "评委信息",
            dataIndex: 'judgeInfo',
            align: 'center',
            name: 'judgeInfo',
            // id:'judgeInfo',
            width: 100,
            flex: 1,
            renderer: function(v) {
                if (v) {
                    return '<a class="tz_materialsReview_app" href = "javaScript:void(0)" >' + v + '</a>';
                } else {
                    return "";
                }
            },
            listeners: {
                click: 'viewJudge'
            }
        },
        {
            text: "评审状态",
            dataIndex: 'judgeStatus',
            align: 'center',
            width: 200
        }];
        var dockedItems;

        var tzAppColsParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD",' + '"OperateType":"isJiSuanFenZhi","comParams":{"type":"isJiSuanFenZhi","classID":"' + classID + '","batchID":"' + batchID + '"}}';
        Ext.tzLoadAsync(tzAppColsParams,
        function(respData) {
            var transScoreValue = respData.ZFZ;
            if (transScoreValue == 'Y') {
                applicantsColumns.push({
                    text: "评委间偏差",
                    dataIndex: 'judgePC',
                    align: 'center',
                    width: 130,
                    flex: 1
                });
                applicantsColumns.push({
                    text: "平均分",
                    dataIndex: 'aveScore',
                    align: 'center',
                    width: 100,
                    flex: 1
                });
                dockedItems = [{
                    xtype: "toolbar",
                    items: [{
                        text: "计算偏差",
                        tooltip: "计算偏差",
                        handler: "calDeviation"
                    }]
                }]
            }
        });
        Ext.apply(this, {
            items: [{
                xtype: 'form',
                name: 'materialsProgressForm',
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

                items: [{
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
                    labelWidth: 150
                },
                {
                    xtype: 'textfield',
                    fieldLabel: "批次",
                    name: 'batchName',
                    fieldStyle: 'background:#F4F4F4',
                    readOnly: true,
                    labelWidth: 150
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: "报考考生数量",
                    name: 'totalStudents',
                    fieldStyle: 'background:#F4F4F4',
                    readOnly: true,
                    labelWidth: 150
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: "已选择材料评审考生",
                    name: 'materialStudents',
                    fieldStyle: 'background:#F4F4F4',
                    readOnly: true,
                    labelWidth: 150
                },
                {
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
                            defaultMargins: {
                                top: 0,
                                right: 5,
                                bottom: 0,
                                left: 0
                            }
                        }
                    },
                    items: [{
                        layout: {
                            type: 'column'
                        },
                        padding: '0 0 8px 0',
                        items: [{
                            margin: '8px',
                            xtype: 'displayfield',
                            value: "<b>目前是第</b>",
                            labelWidth: 1
                        },
                        {
                            xtype: 'displayfield',
                            margin: '8px',
                            readOnly: true,
                            name: 'delibCount',
                            minValue: 0,
                            style: 'margin-left:5hpx;margin-right:2px;border:none',
                            negativeText: '评审次数必须为正整数',
                            nanText: '{0}不是有效的数字',
                            hideLabel: true,
                            width: 20
                            // listeners:{
                            // change:function(field,
                            // newValue,
                            // oldValue,
                            // eOpts
                            // ){
                            // var
                            // form
                            // =
                            // field.findParentByType("setMaterialsReviewRule").lookupReference("CountForm").getForm();
                            // var
                            // reviewApplicantsCount
                            // =
                            // form.findField("materialsReviewApplicantsNumber").getValue();
                            // var
                            // reviewCountAll
                            // =
                            // reviewApplicantsCount*
                            // parseInt(newValue);
                            // form.findField("reviewCountAll").setValue(reviewCountAll);
                            // }
                            // }
                        },
                        {
                            margin: '8px',
                            xtype: 'displayfield',
                            // style:'margin-left:1px',
                            value: "<b>次评审，当前评审状态是：</b>",
                            // hideLabel:
                            // true,
                            labelWidth: 70
                        },
                        {
                            margin: '8px',
                            xtype: 'displayfield',
                            name: 'status',
                            style: 'display:inline-block',
                            readOnly: true
                        },
                        {
                            margin: '8px 0 0 10px',
                            xtype: 'displayfield',
                            value: "<b>本次评审的评审进度是</b>",
                            labelWidth: 1
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: "",
                            margin: '8px',
                            name: 'progress',
                            style: 'display:inline-block',
                            readOnly: true,
                            hideLabel: true,
                            width: '40px'
                        },
                        {
                            margin: '8px',
                            xtype: 'displayfield',
                            value: "<b>人次</b>",
                            labelWidth: 1
                        }]
                    },
                    {
                        layout: {
                            type: 'column'
                        },
                        padding: '0 0 8px 0',
                        items: [{
                            style: 'margin-left:5px',
                            xtype: 'button',
                            flagType: 'positive',
                            name: 'startNewReview',
                            defaultColor: '',
                            setType: 0,
                            text: '开启新一轮评审',
                            handler: 'startNewReview',
                            width: 150
                        },
                        {
                            style: 'margin-left:10px',
                            xtype: 'button',
                            text: '关闭',
                            defaultColor: '',
                            name: 'closeReview',
                            flagType: 'positive',
                            setType: 0,
                            handler: 'closeReview',
                            width: 90
                        },
                        {
                            style: 'margin-left:10px',
                            xtype: 'button',
                            text: '重新开启本轮评审',
                            defaultColor: '',
                            name: 'reStartReview',
                            flagType: 'positive',
                            setType: 0,
                            handler: 'reStartReview',
                            width: 150
                        },
                        {
                            xtype: 'checkboxfield',
                            fieldLabel: '评委可见统计表',
                            name: 'judgeTJB',
                            hidden: true
                        },
                        {
                            xtype: 'checkboxfield',
                            fieldLabel: '评委可见分布图',
                            name: 'judgeFBT',
                            hidden: true
                        }]
                    }]
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
                    listeners: {
                        tabchange: function(tabPanel, newCard, oldCard) {
                            if (newCard.name == "materialsStudentGrid") {
                                var form = tabPanel.findParentByType('materialsReviewSchedule').child('form').getForm();
                                var store = newCard.store;
                                var classID = form.findField('classID').getValue();
                                var batchID = form.findField('batchID').getValue();
                                if (store.isLoaded() == false) {
                                    var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD","OperateType":"QG",' + '"comParams":{"type":"stuList","classID":"' + classID + '","batchID":"' + batchID + '"}}';
                                    Ext.tzLoad(tzParams,
                                    function(respData) {
                                        // store.loadData(respData.root);
                                    });
                                    this.doLayout();
                                }
                            };
                            if (newCard.name == "judgeInfoForm") {
                                var form = tabPanel.findParentByType('materialsReviewSchedule').child('form').getForm();
                                var store = newCard.child('grid').store;
                                console.log(store);
                                var classID = form.findField('classID').getValue();
                                var batchID = form.findField('batchID').getValue();
                                if (store.isLoaded() == false) {
                                    var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_SCHE_STD","OperateType":"QG",' + '"comParams":{"type":"judgeInfo","classID":"' + classID + '","batchID":"' + batchID + '"}}';
                                    Ext.tzLoad(tzParams,
                                    function(respData) {
                                        // store.loadData(respData.root);
                                    });

                                    this.doLayout();
                                }
                            }
                        }
                    },
                    items: [{
                        title: '评委信息',
                        xtype: 'form',
                        name: 'judgeInfoForm',
                        style: 'margin-left:0',
                        layout: {
                            type: 'vbox',
                            align: 'stretch'
                        },
                        items: [{
                            layout: {
                                type: 'column'
                            },
                            items: [{
                                margin: '8px 8px 0px 8px',
                                xtype: 'displayfield',
                                value: "<b>要求每位考生被</b>",
                                labelWidth: 1
                            },
                            {

                                xtype: 'displayfield',
                                margin: '8px 8px 0px 8px',
                                readOnly: true,
                                name: 'reviewCount',
                                style: 'display:inline-block'
                            },
                            {
                                margin: '8px 8px 0px 8px',
                                xtype: 'displayfield',
                                // style:'margin-left:1px',
                                value: "<b>个评委审批</b>",
                                // hideLabel:
                                // true,
                                labelWidth: 70
                            }]
                        },
                        {
                            layout: {
                                type: 'column'
                            },
                            items: [{
                                margin: '0px 8px',
                                xtype: 'displayfield',
                                value: "<b>要求评审人次：</b>",
                                labelWidth: 1
                            },
                            {

                                xtype: 'displayfield',
                                margin: '0px 8px',
                                readOnly: true,
                                name: 'requiredCount',
                                value: "0",
                                style: 'display:inline-block'
                            },
                            {
                                margin: '0px 8px',
                                xtype: 'displayfield',
                                // style:'margin-left:1px',
                                value: "<b>人次</b>",
                                // hideLabel:
                                // true,
                                labelWidth: 70
                            }]
                        },
                        {
                            layout: {
                                type: 'column'
                            },
                            items: [{
                                margin: '0px 8px',
                                xtype: 'displayfield',
                                value: "<b>当前选择评委评议人次：</b>",
                                labelWidth: 1
                            },
                            {

                                xtype: 'displayfield',
                                margin: '0px 8px',
                                readOnly: true,
                                name: 'reviewCount',
                                value: "0",
                                style: 'display:inline-block'
                            },
                            {
                                margin: '0px 8px',
                                xtype: 'displayfield',
                                // style:'margin-left:1px',
                                value: "<b>人次</b>",
                                // hideLabel:
                                // true,
                                labelWidth: 70
                            }]
                        },
                        {
                            xtype: 'grid',
                            minHeight: 150,
                            autoHeight: true,
                            name: 'materialsJudgeGrid',
                            border: false,
                            plugins: {
                                ptype: 'cellediting',
                                clicksToEdit: 1
                            },
                            store: {
                                type: 'materialsReviewScheduleJudgeStore'
                            },
                            multiSelect: true,
                            selModel: {
                                selType: 'checkboxmodel'
                            },
                            dockedItems: [{
                                xtype: "toolbar",
                                items: [{
                                    text: "暂停",
                                    tooltip: "暂停",
                                    handler: "pause"
                                },
                                "-", {
                                    text: "设置评委状态为正常",
                                    tooltip: "设置评委状态为正常",
                                    handler: "setNoaml"
                                },
                                "-", {
                                    text: "提交评委数据",
                                    tooltip: "提交评委数据",
                                    handler: "submitData"
                                },
                                "-", {
                                    text: "设置提交状态为未提交",
                                    tooltip: "设置提交状态为未提交",
                                    handler: "setNoSubmit"
                                },
                                "-", {
                                    text: "撤销评议数据",
                                    tooltip: "撤销评议数据",
                                    handler: "revokeData"
                                },
                                "-", {
                                    text: "打印评分总表",
                                    tooltip: "打印评分总表",
                                    handler: "printPFZB"
                                }

                                ]
                            }],
                            columns: [{
                                width: 1
                            },
                            {
                                text: "选择",
                                xtype: 'rownumberer',
                                width: 50,
                                align: 'center'
                            },
                            {
                                text: "评委帐号",
                                dataIndex: 'judgeID',
                                minWidth: 100,
                                align: 'center',
                                flex: 1
                            },
                            {
                                text: "描述",
                                dataIndex: 'judgeName',
                                minWidth: 100,
                                align: 'center',
                                flex: 1
                            },
                            {
                                text: "评委组",
                                dataIndex: 'judgeGroup',
                                minWidth: 100,
                                align: 'center',
                                flex: 1
                            },
                            {
                                text: "评审考生人数",
                                align: 'center',
                                dataIndex: 'lower',
                                width: 130
                            }
                            // ,{
                            // text:
                            // "评审考生上限",
                            // dataIndex:
                            // 'upper',
                            // width:130
                            // }
                            , {
                                text: "抽取数量/已评审数量",
                                dataIndex: 'hasSubmited',
                                align: 'center',
                                minWidth: 100,
                                flex: 1
                            },
                            {
                                text: "提交状态",
                                dataIndex: 'submitYN',
                                minWidth: 80,
                                align: 'center',
                                flex: 1,
                                renderer: function(v) {
                                    if (v == 'Y') {
                                        return '已提交'
                                    }
                                    if (v == 'N') {
                                        return '未提交'
                                    }
                                    if (v == 'C') {
                                        return '被撤销'
                                    }
                                    if (v == '') {
                                        return '未提交'
                                    }
                                }
                            },
                            {
                                text: "评委状态",
                                dataIndex: 'accountStatus',
                                minWidth: 80,
                                align: 'center',
                                flex: 1,
                                renderer: function(v) {
                                    if (v == 'A') {
                                        return '正常'
                                    }
                                    if (v == 'B') {
                                        return '暂停'
                                    }
                                    if (v == 'N') {
                                        return '新建'
                                    }
                                },
                                editable: false
                            }]
                        }]
                    },
                    {
                        xtype: 'grid',
                        title: '考生名单',
                        minHeight: 260,
                        name: 'materialsStudentGrid',
                        reference: 'materialsReviewAppsGrid',
                        columnLines: true,
                        autoHeight: true,
                        selModel: {
                            type: 'checkboxmodel'
                        },
                        listeners: {
                            activate: 'stuListActive'
                        },
                        store: {
                            type: 'materialsReviewScheduleAppsStore'
                        },
                        plugins: [{
                            ptype: 'cellediting',
                            pluginId: 'judgeCellEditing',
                            clicksToEdit: 1,
                            listeners: {
                                beforeedit: function(editor, context, eOpts) {
                                    if (context.field == "judgeID" && context.value.length > 0 && !context.record.isModified('judgeID')) {
                                        return false;
                                    }
                                }
                            }
                        }],
                        dockedItems: dockedItems,
                        columns: applicantsColumns
                    },                    
                    {
                        xtype: 'form',
                        title: '统计信息',
                        autoHeight: true,
                        name: 'statisticalInfoForm',
                        items: [
                        	Ext.create('Ext.grid.Panel', {
	                            store: Ext.create('Ext.data.ArrayStore', {
	                                fields: statisticsGridDataModel['gridFields'],
	                                data: statisticsGridDataModel['gridData']
	                            }),
	                            minHeight: 180,
	                            margin:'5 0',
	                            selModel: {
	                                type: 'checkboxmodel'
	                            },
	                            columns: statisticsGridDataModel['gridColumns'],
	                            header: false,
	                            border: false,
	                            dockedItems: [{
	                                xtype: "toolbar",
	                                items: [{
	                                    text: "计算选中评委的标准评分分布",
	                                    tooltip: "计算选中评委的标准评分分布",
	                                    handler:'calcuScoreDist'
	                                }, "-", {
	                                    text: "使用计算结果设置分布标准",
	                                    tooltip: "使用计算结果设置分布标准",
	                                    handler:'userCalcuScoreDist'
	                                }, "-", {
	                                    text: "保存评议标准",
	                                    tooltip: "保存评议标准",
	                                    handler:'saveEvaStandard'
	                                }, "-", {
	                                    text: "刷新图表",
	                                    name:"toolbarShowTB",
	                                    tooltip: "刷新图表",
	                                    iconCls: "reset",
	                                    handler: function(obj) {
	                                    	
	                                    	//开始	                                    	
	                                    	var coluObj = [];//用于存放柱状图数据
	                                    	var lineObj = [];//用于存放曲线图数据
	                                    	//console.log(statisticsGridDataModel);
	                                    	//console.log(statisticsGoalGridDataModel);
	                                    	
	                                        var selList = obj.ownerCt.ownerCt.getSelectionModel().getSelection();
	                                        var checkLen = selList.length;
	                                        var pw = "";
	                                        var pw2 = "";
	                                            if(checkLen == 0){
	                                               Ext.MessageBox.alert("提示","请选择需要的评委！");
	                                               return;
	                                            }else{
	                                        	//循环选中的评委
	                                            	for (var i=0;i<selList.length;i++){
	                                            	 pw = selList[i].get("col01");	
	                                            	 if (pw !== null && pw !== undefined && pw !== '') {
		                                            	 //与总的评委比较，取选中评委的数据
			                                             for (var j=0;j<statisticsGridDataModel.gridData.length;j++){
			                                            	 pw2 = statisticsGridDataModel.gridData[j][0];
			                                            	 if(pw==pw2){
			                                            		 //处理柱状图
			                                            		 var coltmpobj = {};
			                                            		 coltmpobj["pw"] = statisticsGridDataModel.gridData[j][1];
			                                            		 //处理非数字的情况
			                                            		 if(isNaN(statisticsGridDataModel.gridData[j][4])){
				                                            		 coltmpobj["pj"] = 0;
			                                            		 }else{
				                                            		 coltmpobj["pj"] = parseFloat(statisticsGridDataModel.gridData[j][4]);
			                                            		 }
			                                            		 coluObj.push(coltmpobj);
			                                            		 //处理曲线图部分
			                                            		 var linetmpobj = {};
			                                            		 linetmpobj["pw"] = statisticsGridDataModel.gridData[j][1];
			                                            		 //循环动态区间
			                                            		 for (var k=5;k<statisticsGridDataModel.gridData[j].length;k++){
			                                            			 //取得区间名称
			                                            			 var colname = statisticsGridDataModel.gridColumns[k].text;
			                                            			 //处理非数字的情况
			                                            			 if(isNaN(statisticsGridDataModel.gridData[j][k])){
			                                            				 linetmpobj[colname] = 0;
			                                            			 }else{
			                                            				 linetmpobj[colname] = parseFloat(statisticsGridDataModel.gridData[j][k]);
			                                            			 }
			                                            		 }
			                                            		 lineObj.push(linetmpobj);
			                                            	 }
			                                             }
	                                             	 }
	                                            	}
	                                            }
	                                        // 添加标准数据
	                                        var bzline = {};
	                                        bzline["pw"] = "标准";
	                                        var bzcoln = {};
	                                        bzcoln["pw"] = "标准";
	                                        
	                                    	//取页面数据
	                                    	var evaluationStandardGrid = Ext.ComponentQuery.query('grid[name=evaluationStandardGrid]')[0];
                                       
	                                        for(var i=1;i<statisticsGoalGridDataModel.gridData[0].length;i++){
	                                        	
	                                        	//从页面取值
	                                        	var colname = statisticsGoalGridDataModel.gridColumns[i-1].text;
	                                        	var colid = statisticsGoalGridDataModel.gridColumns[i-1].dataIndex;
	                                        	var colvalue =evaluationStandardGrid.store.data.items[0].data[colid];
	                                        	//处理值为非数字的情况
	                                        	if (colvalue !== null && colvalue !== undefined && colvalue !== '' && isNaN(colvalue)==false) {
	                                        		bzline[colname] = colvalue;
	                                        	}else{
	                                        		bzline[colname] = 0;
	                                        	}
	                                        	if(i==statisticsGoalGridDataModel.gridData[0].length-1){
	                                        		//处理非数字的情况
	                                        		if (colvalue !== null && colvalue !== undefined && colvalue !== '' && isNaN(colvalue)==false) {
	                                        			bzcoln["pj"] = colvalue;
		                                        	}else{
		                                        		bzcoln["pj"] = 0;
		                                        	}
	                                        	}

	                                        	/* 从后台取数据
	                                        	var colname = statisticsGoalGridDataModel.gridColumns[i-1].text;
	                                        	//处理值为非数字的情况
	                                        	if (statisticsGoalGridDataModel.gridData[0][i] !== null && statisticsGoalGridDataModel.gridData[0][i] !== undefined && statisticsGoalGridDataModel.gridData[0][i] !== '' && isNaN(statisticsGoalGridDataModel.gridData[0][i])==false) {
	                                        		bzline[colname] = statisticsGoalGridDataModel.gridData[0][i];
	                                        	}else{
	                                        		bzline[colname] = 0;
	                                        	}
	                                        	if(i==statisticsGoalGridDataModel.gridData[0].length-1){
	                                        		//处理非数字的情况
	                                        		if (statisticsGoalGridDataModel.gridData[0][i] !== null && statisticsGoalGridDataModel.gridData[0][i] !== undefined && statisticsGoalGridDataModel.gridData[0][i] !== '' && isNaN(statisticsGoalGridDataModel.gridData[0][i])==false) {
	                                        			bzcoln["pj"] = statisticsGoalGridDataModel.gridData[0][i];
		                                        	}else{
		                                        		bzcoln["pj"] = 0;
		                                        	}
	                                        	}
	                                        	*/
	                                        }
	                                        lineObj.push(bzline);
	                                        coluObj.push(bzcoln);
	                                        //以上为参数处理
	                                        //以下为处理图标部分-开始
	                                		// 曲线图参数
	                                		var lineObj2 = [ 
	                                		                {'pw' : 'zhangsan',	'n1' :10,'n2' : 20,'n3' : 30,'n4' : 40,'n5' : 40,'n6' : 40}, 
	                                		                {'pw' : 'lisi',     'n1' :15,'n2' : 25,'n3' : 35,'n4' : 45,'n5' : 40,'n6' : 40},
	                                		                {'pw' : 'wangwu',   'n1' :40,'n2' : 30,'n3' : 20,'n4' : 10,'n5' : 40,'n6' : 40},
	                                		                {'pw' : '标准曲线',     'n1' :22,'n2' : 25,'n3' : 30,'n4' : 33,'n5' : 40,'n6' : 40}
	                                		              ];
	                                		// 柱状图参数
	                                		var coluObj2 = [
	                                		               {'pw' : 'zhangsan', 'pj' : 10},
	                                		               {'pw' : 'lisi',     'pj' : 15},
	                                		               {'pw' : 'wangwu',   'pj' : 40}, 
	                                		               {'pw' : '标准平均分','pj' : 22} 
	                                		               ];
	                                		// 评委数组
	                                		// var pwArr = [ 'data1', 'data2', 'data3' ];
	                                		var pwArr = [];
	                                		// 区间数组
	                                		// var qjArr = [ 'n1', 'n2', 'n3', 'n4' ];
	                                		var qjArr = [];
	                                		//生成评委数组、区间数组
	                                		for (var i = 0; i < lineObj.length; i++) {
	                                			// 生成选择的评委数组
	                                			var person = lineObj[i];
	                                			var personCols = Object.keys(person);
	                                			pwArr.push(person['pw']);
	                                			// 生成区间数组
	                                			if (i == 0) {
	                                				for (var j = 0; j < personCols.length; j++) {
	                                					var col = personCols[j];
	                                					if (col != 'pw') {
	                                						qjArr.push(col);
	                                					}
	                                				}
	                                			}
	                                		}
	                                		// 变量定义 - 曲线图部分
	                                		var arrayLines = []; // 创建一个空数组,用于存放曲线的图例;
	                                		var arrayNameLines = []; // 创建一个空数组,用于存放曲线的图例（包括x坐标）;
	                                		arrayNameLines.push("name");
	                                		var arrayDatas = []; // 创建一个空数组,用于存放曲线数据（包括x坐标）;
	                                		var arraySeries = []; // 创建一个空数组,用于存放曲线;
	                                		// 变量定义 - 柱状图部分
	                                		var arrayColumDatas = []; // 柱状图数据;
	                                		// -----------------根据评委个数、区间个数设置页面的高度、宽度-----------------------------
	                                		var pwNum = pwArr.length;// 评委个数
	                                		var qjNum = qjArr.length;// 指标区间个数
	                                		var columnWidth = 1300;// 柱状图宽度;
	                                		if (((pwNum + 1) * 100) > 1300) {
	                                			columnWidth = (pwNum + 1) * 100;
	                                		}
	                                		var columnHeight = 400;// 柱状图高度;
	                                		var lineWidth = 1300; // 曲线图宽度;
	                                		if ((qjNum * 150) > 1300) {
	                                			lineWidth = qjNum * 200;
	                                		}
	                                		var lineHeigth = 400; // 曲线图高度;
	                                		lineHeigth = (pwNum + 1) * 20 + 400;
	                                		// 保持柱状图和线状图宽度一致
	                                		if (columnWidth > lineWidth) {
	                                			lineWidth = columnWidth;
	                                		} else {
	                                			columnWidth = lineWidth;
	                                		}
	                                		// ------------------------------------柱状图开始---------------------------
	                                		// 1、生产柱状图数据
	                                		for (var i = 0; i < pwArr.length; i++) {
	                                			// 循环每个评委
	                                			var pw = pwArr[i];
	                                			var datatmp = {};
	                                			datatmp['graphName'] = pw;
	                                			// 从传递过来的数字里面取评委对应的数据;
	                                			var pjs = 0;
	                                			for (var j = 0; j < coluObj.length; j++) {
	                                				if (pw == coluObj[j]['pw']) {
	                                					pjs = coluObj[j]['pj']
	                                				}
	                                			}
	                                			datatmp['graphData'] = pjs;
	                                			arrayColumDatas.push(datatmp);
	                                		}
	                                		// 2、曲线图数据集
	                                		var graphDataStore = Ext.create('Ext.data.Store', {
	                                			fields : [ 'graphName', 'graphData' ],
	                                			data : arrayColumDatas
	                                		});
	                                		// var colors = ['#6E548D','#94AE0A','#FF7348','#3D96AE'];
	                                		// 动态定义曲线图id
	                                		var id1 = "" + Math.ceil(Math.random() * 100);
	                                		// 定义曲线图
	                                		var columnChart = Ext.create('Ext.chart.Chart', {
	                                			xtype : 'chart',
	                                			id : 'columnChart' + id1,
	                                			width : columnWidth,
	                                			height : columnHeight,
	                                			animate : true,// 使用动画
	                                			store : graphDataStore,
	                                			shadow : true,// 使用阴影
	                                			axes : [ {// x轴与y轴的声明
	                                				type : 'Numeric',
	                                				position : 'left',
	                                				title : '平均分',
	                                				minimum : 0,
	                                				maximum : 100,
	                                				grid : true
	                                			}, {
	                                				type : 'Category',
	                                				position : 'bottom',
	                                				fields : 'graphName',
	                                				title : '评委列表'
	                                			} ],
	                                			series : [ {
	                                				type : 'column',
	                                				axis : 'bottom',
	                                                label: {
	                                                    display: 'insideEnd',
	                                                    'text-anchor': 'middle',
	                                                      field: 'graphData',
	                                                      renderer: Ext.util.Format.numberRenderer('0.00'),
	                                                      //orientation: 'vertical',控制数字横着还是竖着
	                                                      color: '#333'
	                                                },
	                                				style: { width: 200 },
	                                				xField : 'graphName',
	                                				yField : 'graphData'// x与y轴的数据声明
	                                			} ]
	                                		});
	                                		// ------------------------------------柱状图结束---------------------------
	                                		// ------------------------------------曲线图开始---------------------------
	                                		// 处理曲线图的数据-开始
	                                		// 1、获取每个区间的数据-开始
	                                		for (var i = 0; i < qjArr.length; i++) {
	                                			// 循环每个区间
	                                			var qj = qjArr[i];// 区间
	                                			var lineData = {};
	                                			lineData["name"] = qjArr[i];
	                                			for (var j = 0; j < pwArr.length; j++) {
	                                				// 循环每个评委
	                                				var pw = pwArr[j];
	                                				// 从传递过来的数组里面取评委对应的数据;
	                                				var qjdata = 0;
	                                				for (var k = 0; k < lineObj.length; k++) {
	                                					if (pw == lineObj[k]['pw']) {
	                                						qjdata = lineObj[k][qj]
	                                					}
	                                				}
	                                				lineData[pw] = qjdata;
	                                			}
	                                			arrayDatas.push(lineData);
	                                		};
	                                		// 获取每个区间的数据-结束
	                                		// 2、循环生成曲线-开始
	                                		for (var k = 0; k < pwArr.length; k++) {
	                                			// 循环每个评委
	                                			var pwTmp = pwArr[k];
	                                			var SerieTmp = {
	                                				type : 'line',
	                                				highlight : {
	                                					size : 7,
	                                					radius : 7
	                                				},
	                                				tips : {
	                                					trackMouse : true,
	                                					width : 430,
	                                					height : 28,
	                                					renderer : function(storeItem, item) {
	                                						this.setTitle('评委：' + item.series.yField
	                                								+ '，分布区间：' + storeItem.get('name')
	                                								+ '，分布比率 ' + item.value[1]);
	                                					}
	                                				},
	                                				axis : 'left',
	                                				fill : false,
	                                				xField : 'name',
	                                				yField : pwTmp,
	                                				markerConfig : {
	                                					type : 'circle',
	                                					size : 4,
	                                					radius : 4,
	                                					'stroke-width' : 0
	                                				},
	                                				smooth : true
	                                			};
	                                			arraySeries.push(SerieTmp);
	                                			arrayLines.push(pwTmp);
	                                			arrayNameLines.push(pwTmp);
	                                		};
	                                		// 循环生成曲线-结束
	                                		// 处理曲线图的数据-开始
	                                		// 曲线图定义部分-开始
	                                		// 1、曲线图的数据源
	                                		var mystore = Ext.create('Ext.data.JsonStore', {
	                                			fields : arrayLines,
	                                			data : arrayDatas
	                                		});
	                                		// 2、动态定义曲线图的id
	                                		var id2 = "" + Math.ceil(Math.random() * 100);
	                                		// 3、生成曲线图
	                                		var lineChart = Ext.create('Ext.chart.Chart', {
	                                			xtype : 'chart',
	                                			style : 'background:#fff',
	                                			height : lineHeigth,
	                                			// id: 'linechart',
	                                			id : 'lineChart' + id2,
	                                			width : lineWidth,
	                                			animate : true,
	                                			// insetPadding: 20,
	                                			store : mystore,
	                                			shadow : true,
	                                			theme : 'Category1',
	                                			layout : 'fit',
	                                			axes : [ {
	                                				type : 'Numeric',
	                                				minimum : 0,
	                                				position : 'left',
	                                				fields : arrayLines,
	                                				title : '分布比率（%）',
	                                				minimum : 0,
	                                				maximum : 100,
	                                				minorTickSteps : 1,
	                                				grid : {
	                                					odd : {
	                                						opacity : 1,
	                                						fill : '#ddd',
	                                						stroke : '#bbb',
	                                						'stroke-width' : 0.5
	                                					}
	                                				}
	                                			}, {
	                                				type : 'Category',
	                                				position : 'bottom',
	                                				fields : [ 'name' ],
	                                				title : '分布区间'
	                                			} ],
	                                			series : arraySeries,
	                                			legend : {
	                                				position : 'top'
	                                			}
	                                		});
	                                		// 曲线图定义部分-结束
	                                		// ------------------------------------曲线图结束---------------------------
	                                		// 填充页面部分-开始
	                                        mainPageFrame.removeAll();
	                                        mainPageFrame.add(columnChart);
	                                        mainPageFrame.add(lineChart);
	                                        mainPageFrame.expand(true);
	                                        mainPageFrame.doLayout();
	                                		// 填充页面部分-结束
	                                    	//结束
	                                    }
	                                }]
	                            }],
	                            viewConfig: {
	                                enableTextSelection: true
	                            }
                        	}),
                        	Ext.create('Ext.grid.Panel', {
	                            store: Ext.create('Ext.data.ArrayStore', {
	                                fields: statisticsGoalGridDataModel['gridFields'],
	                                data: statisticsGoalGridDataModel['gridData']
	                            }),
	                            minHeight: 80,
	                            margin:'10 0',
	                            name:"evaluationStandardGrid",
	                            columnLines: true,
	                            viewConfig: {
	                                enableTextSelection: true
	                            },	       
	                            plugins: [{
	                                ptype: 'cellediting',
	                            }],
	                            columns:[
	                            	{
	                                    text:'指标名称',	                                    
	                                    dataIndex:'col01',
	                                    width:'10%'                                    
	                                },
	                                {
	                                    text:'总分',
	                                    lockable   : false,
	                                    menuDisabled: true,
	                                    columns:statisticsGoalGridDataModel['gridColumns']
	                                }
	                            ],
	                            //columns: statisticsGoalGridDataModel['gridColumns'],
	                            header: false,
	                            border: false,
		                        viewConfig: {
		                            enableTextSelection: true
		                        }
                        	}),mainPageFrame]
                    }]
                }]
            }]

        });
        this.callParent();
    },
    buttons: [{
        text: '保存',
        iconCls: "save",
        handler: 'onScheduleSave'
    },
    {
        text: '确定',
        iconCls: "ensure",
        handler: 'onScheduleEnsure'
    },
    {
        text: '关闭',
        iconCls: "close",
        handler: 'onScheduleClose'
    }]
});