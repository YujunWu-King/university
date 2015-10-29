Ext.define('KitchenSink.view.template.survey.report.PinShuBB.PinShuBBAnswerTuBiao', {
    extend: 'Ext.panel.Panel',
    xtype: 'PinShuBBAnswerTuBiao',
    controller: 'PinShuBBQuestionListController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.template.survey.report.PinShuBB.PinShuBBQuestionListController'
    ],
    title: '频数报表图表',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',

    initComponent:function(){

        var msyrtislReviewScheduleChartStore=new Ext.data.JsonStore({
            fields:['name','data1'],
            data:[]
        });

        var msyrtislReviewScheduleChartStore2=new Ext.data.JsonStore({
            fields:[],
            data:[]
        });


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
                            xtype: 'numberfield',
                            fieldLabel: "报考考生数量",
                            name: 'totalStudents',
                            fieldStyle: 'background:#F4F4F4',
                            readOnly: true,
                            value:'123',
                            labelWidth:150
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
                            items: [
                                {
                                    xtype: 'form',
                                    title: '统计信息',
                                    autoHeight:true,
                                    name:'statisticalInfoForm',
                                    items:[
                                        Ext.create('Ext.grid.Panel', {
                                            store: Ext.create('Ext.data.ArrayStore', {
                                                fields:  ['name','data2'],
                                                data: []
                                            }),
                                            minHeight:80,
                                            //stateful: true,
                                            selModel: {
                                                type: 'checkboxmodel'
                                            },
                                            columns: [{
                                                text     : '1',
                                                sortable : false,

                                                flex:1
                                            },{
                                                text     : '2',
                                                sortable : false,
                                                flex:1
                                            }],
                                            header: false,
                                            border:false,
                                            dockedItems:[{
                                                xtype:"toolbar",
                                                dock: 'bottom',
                                                items:[
                                                    {text:"刷新图表",tooltip:"刷新图表",iconCls: "reset",handler:function(btn){


                                                        //统计分布图表series
                                                        var seriesArray = [{
                                                            type: 'line',
                                                            highlight: {size: 7,radius: 7},
                                                            axis: 'left',
                                                            smooth: true,
                                                            xField: 'fbName',
                                                            yField: tips,
                                                            markerConfig: {type: 'circle',size: 4,radius: 4,'stroke-width': 0},
                                                            title: '第一个标题'/*,
                                                             tips: {
                                                             trackMouse: true,
                                                             width: 180,
                                                             renderer: function(storeItem, item)
                                                             {
                                                             this.setTitle('分布区间[' + storeItem.get('fbName') + ']<br>分布比率: '+item.data+'-' + Ext.util.Format.number(storeItem.get(fieldName),'000.00'));
                                                             }
                                                             }*/
                                                        },{
                                                            type: 'line',
                                                            highlight: {size: 7,radius: 7},
                                                            axis: 'left',
                                                            smooth: true,
                                                            xField: 'fbName',
                                                            yField: 'tips',
                                                            markerConfig: {type: 'circle',size: 4,radius: 4,'stroke-width': 0},
                                                            title: '第二个标题'/*,
                                                             tips: {
                                                             trackMouse: true,
                                                             width: 180,
                                                             renderer: function(storeItem, item)
                                                             {
                                                             this.setTitle('分布区间[' + storeItem.get('fbName') + ']<br>分布比率: '+item.data+'-' + Ext.util.Format.number(storeItem.get(fieldName),'000.00'));
                                                             }
                                                             }*/
                                                        }];


                                                        columnChart = new Ext.chart.Chart({
                                                            width: 860,
                                                            height: 400,
                                                            animate: true,

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

                                                        mainPageFrame.add(columnChart);
                                                        mainPageFrame.add(lineChart);
                                                        mainPageFrame.expand(true);
                                                        mainPageFrame.doLayout();

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

