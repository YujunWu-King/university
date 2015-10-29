Ext.define('KitchenSink.view.template.survey.report.JDBB.jdbbInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'jdbbInfo',
  // store: 'wcztStore',
    controller: 'jdbbController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.survey.report.JDBB.wcztStore',
        'KitchenSink.view.template.survey.report.JDBB.weekStore',
        'KitchenSink.view.template.survey.report.JDBB.jdbbController',
        'Ext.chart.series.Column'
    ],

    initComponent:function(){

        var wcztStore = new KitchenSink.view.template.survey.report.JDBB.wcztStore(
//            {
//            fields:['wczt','pepNum'],
//            data:[]
//        }
        );
        var weekStore = new KitchenSink.view.template.survey.report.JDBB.weekStore(
//            {
//            fields:['date','pepNum'],
//            data:[]
//        }
        );

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
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JDBB_STD.wjID","问卷ID") ,
                    name: 'onlinedcId',
                    fieldStyle:'background:#F4F4F4',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JDBB_STD.wjTitle","问卷标题") ,
                    name: 'onlinedcName',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JDBB.publishState","发布状态") ,
                    name: 'onlinedcState',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                }]
            },{

                xtype:'chart',
                name: 'chart1',
                title: {
                    text:'完成情况'
                },
              //  title:'完成情况',
                store:wcztStore,
                width: 860,
                height: 400,
                layout: 'fit',
                renderTo:Ext.getBody(),
                axes:[
                    {
                        type: 'Numeric',
                        position: 'left',
                        minimum: 0,
                        maximum:40,
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

                    }
                }]
            },{
                xtype:'chart',
                name: 'chart2',
                title:'最近一周进入情况',
                store:weekStore,
                width:860,
                height:400,
                layout: 'fit',
                renderTo:Ext.getBody(),
                axes:[
                    {
                        type: 'Numeric',
                        position: 'left',
                        minimum: 0,
                        maximum: 40,
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
                }]

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