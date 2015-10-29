Ext.define('KitchenSink.view.onlineSurvey.dcwj_CrosstabReport.jcbbQuestionList', {
    extend: 'Ext.panel.Panel',
    xtype: 'jcbbQuestionList',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.onlineSurvey.dcwj_CrosstabReport.jcbbQuestionListModel',
        'KitchenSink.view.onlineSurvey.dcwj_CrosstabReport.jcbbQuestionListStore',
        'KitchenSink.view.onlineSurvey.dcwj_CrosstabReport.jcbbQuestionListController'
    ],
    title: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.panelTitle","交叉报表"),
    controller: 'jcbbQuestionListController',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    reference: 'interviewArrangeSelStuPanel',
    //style:"margin:8px",
    initComponent: function (){
        var jcbbQuestionListGridStore = new KitchenSink.view.onlineSurvey.dcwj_CrosstabReport.jcbbQuestionListStore();

        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'jcbbQuestionListForm',
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
                    name: 'wjID',
                    fieldStyle:'background:#F4F4F4',
                    hidden:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.wjTitle","问卷标题") ,
                    name: 'wjTitle',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.publishState","发布状态") ,
                    name: 'publishState',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true
                }]
            },{
                xtype: 'grid',
                height: 350,
                frame: true,
                name: 'jcbbQuestionListGrid',
                reference: 'jcbbQuestionListGrid',
                store: jcbbQuestionListGridStore,
                columnLines: true,    //显示纵向表格线
                selModel:{
                    type: 'checkboxmodel'
                },
                dockedItems:[{
                    xtype:"toolbar",
                    items:[/*	{
                        text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.tbarClearFilters","清除筛选条件"),
                        tooltip:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.tbarClearFiltersTip","清除筛选条件"),
                        iconCls:"reset",
                        reference:'jcbbQuestionListClearFiltersBtn',
                        handler:'onClearFilters',
                        disabled:true
                    },*/	{
                        text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.generateReport","根据选择问题生成交叉报表"),
                        tooltip:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.generateReportTip","根据选择问题生成交叉报表"),
                        //iconCls:"",
                        handler:'generateReport'
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
        text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.butClose","关闭") ,
        iconCls:"close",
        handler:'onPanelClose'
    }]
});
