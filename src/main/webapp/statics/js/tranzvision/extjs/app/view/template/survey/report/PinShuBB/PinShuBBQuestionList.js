Ext.define('KitchenSink.view.template.survey.report.PinShuBB.PinShuBBQuestionList', {
    extend: 'Ext.panel.Panel',
    xtype: 'PinShuBBQuestionList',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.template.survey.report.PinShuBB.PinShuBBQuestionListModel',
        'KitchenSink.view.template.survey.report.PinShuBB.PinShuBBQuestionListStore',
         'KitchenSink.view.template.survey.report.PinShuBB.PinShuBBQuestionListController',
        'KitchenSink.view.common.store.comboxStore',
        'tranzvision.extension.grid.column.Link'
    ],
    title: "频数报表",
    controller: 'PinShuBBQuestionListController',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    // reference: 'interviewArrangeSelStuPanel'
    style:"margin:8px",
    ignoreChangesFlag: true,
//    listeners:{
//        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
//            var buttonHeight = 44;/*button height plus panel body padding*/
//            var grid = panel.child('grid');
//            if(grid)grid.setHeight( height -buttonHeight -96-8);
//        }
//    }，


    initComponent: function (){
        var PinShuBBQuestionListGridStore = new KitchenSink.view.template.survey.report.PinShuBB.PinShuBBQuestionListStore();
        var PinShuBBWCZT = new KitchenSink.view.common.store.appTransStore("TZ_PSBB_WCZT");
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'PinShuBBQuestionListForm',
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
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_STD.wjID","问卷ID") ,
                    name: 'onlinedcId',
                    fieldStyle:'background:#F4F4F4',
                    hidden:true,
                    ignoreChangesFlag: true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_STD.wjTitle","标题") ,
                    name: 'onlinedcTitle',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true,
                    ignoreChangesFlag: true
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_STD.onlinedcState","发布状态") ,
                    name: 'onlinedcState',
                    fieldStyle:'background:#F4F4F4',
                    readOnly:true,
                    ignoreChangesFlag: true
                },{
                    xtype: 'combo',
                    fieldLabel: "完成状态",
                    name: 'WCZT',
                    store: PinShuBBWCZT,
                    //value:datas.LUQUZT,
                    displayField: 'TSDesc',
                    valueField: 'TValue',
                    queryMode: 'local',
                    editable: false,
                    ignoreChangesFlag: true
                }]
            },{
                xtype: 'grid',
                height: 350,
                frame: true,
                name: 'PinShuBBQuestionListGrid',
                reference: 'PinShuBBQuestionListGrid',
                store: PinShuBBQuestionListGridStore,
                columnLines: true,    //显示纵向表格线
                selModel:{
                    type: 'checkboxmodel'
                },

                dockedItems:[{
                    xtype:"toolbar",
                    items:[{
                        xtype:"displayfield",
                        fieldLabel:Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_STD.selectQuestions","请选择生成交叉报表的问题:"),
                        labelWidth:200,
                        height:30
                        //tooltip:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBLB_STD.generateReportTip","根据选择问题生成交叉报表"),
                        //iconCls:"",
                        //handler:'generateReport'
                    }]
                }],
                columns: [{
                    text: Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_STD.rowNum","序号"),
                    xtype: 'rownumberer',
                    width:50
                },{
                    text:Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_STD.questionID","问题编号") ,
                    dataIndex: 'questionID',
                    minWidth:125,
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_STD.questionDesc","问题描述") ,
                    dataIndex: 'questionDesc',
                    minWidth: 300,
                    flex:3
                },{
                    text:Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_STD.childQuestionDesc","子问题描述") ,
                    dataIndex: 'childQuestionDesc',
                    minWidth: 300,
                    flex:3
                },{
                    header: '查看报表',
                    xtype:'linkcolumn',
                    sortable: false,
                    flex:2,
                    value:'查看报表',
                    minWidth: 150,
                    handler:'viewBaoBiao'

                }], bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: PinShuBBQuestionListGridStore,
                    plugins: new Ext.ux.ProgressBarPager()
                }
            }]
        });
        this.callParent();
    },
    buttons: [{
        text:Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_STD.butNext","下一步") ,
        //iconCls:"",
        handler:'generateReport'
    },{
        text:Ext.tzGetResourse("TZ_ZXDC_PSBB_COM.TZ_ZXDC_PSBB_STD.butClose","关闭") ,
        iconCls:"close",
        handler:'onPanelClose'
    }]
});
