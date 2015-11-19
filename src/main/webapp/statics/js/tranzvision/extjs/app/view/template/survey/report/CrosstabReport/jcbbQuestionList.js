Ext.define('KitchenSink.view.template.survey.report.CrosstabReport.jcbbQuestionList', {
    extend: 'Ext.panel.Panel',
    xtype: 'jcbbQuestionList',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.template.survey.report.CrosstabReport.jcbbQuestionListModel',
        'KitchenSink.view.template.survey.report.CrosstabReport.jcbbQuestionListStore',
        'KitchenSink.view.template.survey.report.CrosstabReport.jcbbQuestionListController'
    ],
    title: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.panelTitle","交叉报表"),
    controller: 'jcbbQuestionListController',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    reference: 'interviewArrangeSelStuPanel',
    //style:"margin:8px",
    ignoreChangesFlag: true,
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;/*button height plus panel body padding*/
            var grid = panel.child('grid');
            if(grid)grid.setHeight( height -buttonHeight -96-8);
        }
    },
    initComponent: function (){
        var jcbbQuestionListGridStore = new KitchenSink.view.template.survey.report.CrosstabReport.jcbbQuestionListStore();

        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'jcbbQuestionListForm',
                ignoreChangesFlag: true,
                layout: {
                    type: 'vbox',       // Arrange child items vertically
                    align: 'stretch'    // 控件横向拉伸至容器大小
                },
                border: false,
                bodyPadding:8,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 100,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.wjID","问卷ID") ,
                    name: 'onlinedcId',
                    fieldStyle:'background:#F4F4F4',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.wjTitle","问卷标题") ,
                    name: 'onlinedcTitle',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.publishState","发布状态") ,
                    name: 'onlinedcState',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                }]
            },{
                xtype: 'grid',
                height: 350,
                frame: true,
                name: 'jcbbQuestionListGrid',
                reference: 'jcbbQuestionListGrid',
                ignoreChangesFlag: true,
                store: jcbbQuestionListGridStore,
                columnLines: true,    //显示纵向表格线
                selModel:{
                    type: 'checkboxmodel'
                },

                dockedItems:[{
                    xtype:"toolbar",
                    items:[{
                        xtype:"label",
                        text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.selectQuestions","请选择生成交叉报表的问题:")
                    }]
                }],
                columns: [{
                    text: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.rowNum","序号"),
                    xtype: 'rownumberer',
                    width:50
                },{
                    text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.questionID","问题编号") ,
                    dataIndex: 'questionID',
                    minWidth:125,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.questionDesc","问题描述") ,
                    dataIndex: 'questionDesc',
                    minWidth: 300,
                    flex:3
                }]
            }]
        });
        this.callParent();
    },
    buttons: [{
        text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.butNext","下一步") ,
        iconCls:"next",
        handler:'batchGenerateReport'
    },{
        text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.butClose","关闭") ,
        iconCls:"close",
        handler:'onPanelClose'
    }]
});
