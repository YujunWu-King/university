Ext.define('KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.jdbbInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'jdbbInfo',
  // store: 'wcztStore',
    controller: 'jdbb',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.wcztStore',
        'KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.weekStore',
        'KitchenSink.view.tzLianxi.ZXE.ZXDCJDBB.jdbbController',
        'Ext.chart.series.Column'
    ],
 // actType: 'add',//默认新增
    initComponent:function(){
     /*   var dataStore=new Ext.data.JsonStore({
            fields: [
                {name: 'wczt'},
                {name: 'pepNum'}
            ],
            data: [
                {wczt:'完成',pepNum:30},
                {wczt:'未完成',pepNum:50}
            ]
        });

        var curDate=Ext.Date.format(new Date(), 'm-d');
        var curDate1=Ext.Date.format(new Date(new Date()-1*24*60*60*1000), 'm-d');
        var curDate2=Ext.Date.format(new Date(new Date()-2*24*60*60*1000), 'm-d');
        var curDate3=Ext.Date.format(new Date(new Date()-3*24*60*60*1000), 'm-d');
        var curDate4=Ext.Date.format(new Date(new Date()-4*24*60*60*1000), 'm-d');
        var curDate5=Ext.Date.format(new Date(new Date()-5*24*60*60*1000), 'm-d');
        var curDate6=Ext.Date.format(new Date(new Date()-6*24*60*60*1000), 'm-d');

        var dataStore1=new Ext.data.JsonStore({
            fields: [
                {name: 'date'},
                {name: 'pepNum'}
            ],
            data: [
                {date:curDate6,pepNum:30},
                {date:curDate5,pepNum:50},
                {date:curDate4,pepNum:50},
                {date:curDate3,pepNum:50},
                {date:curDate2,pepNum:50},
                {date:curDate1,pepNum:50},
                {date:curDate,pepNum:50}
            ]
        });*/
        Ext.apply(this,{

            items: [{
                xtype: 'form',
              //  reference: 'jygzInfoForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },

                border: false,
                bodyPadding: 10,
                //heigth: 600,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'textfield',
                    fieldLabel: '标题',
                    name: 'biaoti',
                    editable:false

                },
                    {
                    xtype: 'combo',
                    fieldLabel: '发布状态',
                    queryMode:"local",
                    editable:false,
                    store:{
                        fields:[
                            {name:'statusCode'},
                            {name:'statusDesc'}
                        ],
                        data:[
                            {statusCode:'Y',statusDesc:'已发布'},
                            {statusCode:'N',statusDesc:'未发布'}
                        ]
                    },
                    valueField:'statusCode',
                    displayField:'statusDesc',


                        name: 'fabuState'
                }
                ]
            },{
                xtype:'chart',
                name: 'chart1',
                title: {
                    text:'完成情况'
                },
              //  title:'完成情况',
                store:'wcztStore',
                width:400,
                height:400,
                layout: 'fit',
                renderTo:Ext.getBody(),
                axes:[
                    {
                        type: 'Numeric',
                        position: 'left',
                        minimum: 0,
                        maximum:6,
                        fields: [ 'pepNum'],
                        title: '人数'
                    },{
                        type:'Category',
                        position:'bottom',
                        fields:['wczt'],
                        title:'完成状态'
                    }],

              /*  legend:{
                    position:'bottom'
                },*/
                series:[{
                    type:'column',

                    axis:'left',
                    xField:'wczt',
                    yField:'pepNum',
                /*    tooltip: {
                        trackMouse: true,
                        width: 140,
                        height: 28,
                        renderer: function (storeItem, item) {
                            this.setHtml(storeItem.get('name') + ': ' + storeItem.get('data1') + ' views');
                        }
                    },*/
                    tooltip: {
                        valueSuffix: ''
                    },
                    label:{
                        field:['pepNum'],
                        display:'outside',
                        font:'18px "Lucida Grande"'
                       /* renderer:function(v){
                            return v + '%';
                        }*/
                    }
                }]
            },{
                xtype:'chart',
                name: 'chart2',
                title:'最近一周进入情况',
                store:'weekStore',
                width:400,
                height:400,
                layout: 'fit',
                renderTo:Ext.getBody(),
                axes:[
                    {
                        type: 'Numeric',
                        position: 'left',
                        minimum: 0,
                        maximum: 7.5,
                        fields: [ 'pepNum'],
                        title: '人数'
                    },{
                        type:'Category',
                        position:'bottom',
                        fields:['date'],

                        title:'日期'
                    }],


                series:[{
                    type:'line',
                    highlight:{size:7,
                    radius:7},
                    axis:'left',

                    xField:'date',
                    yField:'pepNum',

                    markerCfg:{
                        type:'circle',
                        radius:4
                    },
                    selectionTolerance:100,
                    showInLegend:true,
                   // smooth:true,
                    showMarkers:true
                }/* {type:'line',
                    axis:'left',
                    xField:'date',
                    yField:'pepNum',
                    showInLegend:true
                    *//* renderer:function(v){
                     return v + '%';
                     }*//*

                }*/]

            }]


        })
        this.callParent();
    },
    title: '进度报表',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    buttons: [ {
        text: '关闭',
        iconCls:"close",
        handler: 'onJdbbInfoClose'
    }]
});