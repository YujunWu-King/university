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
        'KitchenSink.view.template.survey.report.PinShuBB.PinShuBBQuestionListController',
        'KitchenSink.view.template.survey.report.PinShuBB.PinShuBBAnswerTuBiaoChartStore',
        'KitchenSink.view.template.survey.report.PinShuBB.PinShuBBAnswerTuBiaoGridStore'
    ],
    title: '频数报表图表',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',

    initComponent:function(){

          //图表区store
        var ChartStore = new KitchenSink.view.template.survey.report.PinShuBB.PinShuBBAnswerTuBiaoChartStore();
        //列表store
        var GridStore =  new KitchenSink.view.template.survey.report.PinShuBB.PinShuBBAnswerTuBiaoGridStore( );


        Ext.apply(this,{
            items: [{
                xtype: 'form',
                //reference: 'jygzInfoForm',
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
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_PSBB_W_STD.wjID","问卷ID") ,
                    name: 'onlinedcId',
                    fieldStyle:'background:#F4F4F4',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_PSBB_W_STD.wjTitle","问卷标题") ,
                    name: 'onlinedcTitle',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_PSBB_W_STD.questionDesc","问题描述") ,
                    name: 'questionDesc',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_PSBB_W_STD.childQuestionDesc","子问题描述") ,
                    name: 'childQuestionDesc',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                },{

                    xtype:'chart',
                    name: 'PinShuBBAnswerTuBiaoChart',
                    title: {
                        text:'完成情况'
                    },
                    //  title:'完成情况',
                    store:ChartStore,
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
                            fields: [ 'answerPercent'],
                            title: '比例(百分比)'
                        },{
                            type:'Category',
                            position:'bottom',
                            fields:['answer'],
                            title:'答案'
                        }],

                    /*  legend:{
                     position:'bottom'
                     },*/
                    series:[{
                        type:'column',

                        axis:'left',
                        xField:'answer',
                        yField:'answerPercent',
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
                            field:['answer'],
                            display:'answerPercent',
                            font:'18px "Lucida Grande"'

                        }
                    }]
                },{
                    xtype: 'grid',
                    autoHeight:true,
                    frame: true,
                    minHeight: 260,
                    name: 'PinShuBBAnswerTuBiaoGrid',
                    reference: 'PinShuBBAnswerTuBiaoGrid',
                    store: GridStore,
                    columnLines: true,    //显示纵向表格线
                    selModel:{
                        type: 'checkboxmodel'
                    },

                    dockedItems:[{
                        xtype:"toolbar",
                        items:[]
                    }],
                    columns: [{
                        text: Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_W_STD.rowNum","序号"),
                        xtype: 'rownumberer',
                        width:50
                    },{
                        text:Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_W_STD.questionID","选项") ,
                        dataIndex: 'answer',
                        minWidth:125,
                        width:200
                    },{
                        text     : Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_W_STD.questionDesc","比例图") ,
                        xtype    : 'widgetcolumn',
                        width    : 120,
                        dataIndex: 'answerPercent',
                        widget: {
                            xtype: 'progressbarwidget',
                            textTpl: [
                                '{percent:number("0")} '
                            ]
                        }
                    }, {
                        text:Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_W_STD.childQuestionDesc","百分比") ,
                        dataIndex: 'answerPercent',
                        minWidth: 300,
                        flex:1
                    },{
                            text: Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_W_STD.childQuestionDesc", "响应数"),
                            dataIndex: 'answerFraction',
                            minWidth: 300,
                            flex: 1
                        } ]
                }]
            }]
        })
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


