Ext.define('KitchenSink.view.template.survey.report.CrosstabReport.jcbbBatWin', {
    extend: 'Ext.window.Window',
    xtype: 'jcbbBatWin',
    controller: 'jcbbBatWinController',
    reference: 'jcbbBatWin',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.survey.report.CrosstabReport.jcbbBatWinStore',
        'KitchenSink.view.template.survey.report.CrosstabReport.jcbbBatWinController'
    ],
    modal:true,//背景遮罩
    header:false,
    minHeight: 150,
    maxHeight: 400,
    resizable:false,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    y:25,
    initComponent: function(){
        var jcbbListStore = new KitchenSink.view.template.survey.report.CrosstabReport.jcbbBatWinStore();
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'jcbbBatForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                //heigth: 600,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelStyle: 'font-weight:bold'
                },

                items: [
                    {
                        xtype: 'tabpanel',
                        reference:'jcbbBatTabPanel',
                        activeTab: 0,
                        frame: false,
                        header:false,
                        width: 650,
                        minHeight: 200,
                        maxHeight: 400,
                        resizeTabs: true,
                        defaults: {
                            autoScroll: false
                        },
                        listeners: {
                            tabchange: function (tp, p) {
                                if (p.reference== 'viewJcbbGrid') {
                                    this.doLayout();
                                }
                            }
                        },
                        items: [
                            {
                                title: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.genJcbb","生成报表"),
                                xtype: 'form',
                                frame:false,
                                minHeight:150,
                                autoHeight:true,
                                reference: 'genJcbbForm',
                                border: false,
                                bodyPadding: 10,
                                buttons: [{
                                    text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.genJcbbBtn","生成报表"),
                                    //iconCls:"close",
                                    handler: 'batGenJcbb'
                                },{
                                    text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.closeBtn","关闭"),
                                    iconCls:"close",
                                    handler: 'onWindowClose'
                                }],
                                fieldDefaults: {
                                    msgTarget: 'side',
                                    labelStyle: 'font-weight:bold'
                                },
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                items: [
                                    {
                                        xtype: 'label',
                                        style:'margin-top:20px',
                                        text: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.inputJcbbName","请输入生成报表名称：")
                                    },{
                                        xtype: 'textfield',
                                        name: 'jcbbName',
                                        allowBlank: false
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'onlinedcId',
                                        hidden: true
                                    },
                                    {
                                        xtype: 'textfield',
                                        name: 'wtIds',
                                        hidden: true
                                    }
                                ]
                            },
                            {
                                title: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.viewJcbb","查看报表"),
                                xtype: 'grid',
                                autoHeight: true,
                                frame:false,
                                minHeight:200,
                                columnLines: true,
                                reference: 'viewJcbbGrid',
                                multiSelect: true,
                                store: jcbbListStore,
                                dockedItems: [{
                                    xtype: "toolbar",
                                    items: [
                                        {text: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.queryBtn","查询"),  iconCls: "query",handler:"viewJcbbQuery"},
                                        '-',
                                        {text: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.deleteBtn","删除"),  iconCls: "remove",handler:"viewJcbbDelete"}
                                    ]
                                },{
                                    xtype:"toolbar",
                                    dock:"bottom",
                                    ui:"footer",
                                    items:['->',
                                        {minWidth:80,text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.saveBtn","保存"),iconCls:"save",handler:'viewJcbbSave'}
                                        ,{minWidth:80,text:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.closeBtn","关闭"),iconCls:"close",handler:'onWindowClose'}]
                                }],
                                selModel: {
                                    type: 'checkboxmodel'
                                },
                                columns: [
                                    {text: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.jcbbName", "报表名称"),
                                        dataIndex: 'jcbbName',
                                        minWidth:100,
                                        flex:1
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.oprName", "操作人"),
                                        dataIndex: 'oprName',
                                        width: 110
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.genTime", "生成时间"),
                                        dataIndex: 'genTime',
                                        width: 146
                                    },
                                    {
                                        text: Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.AEState", "状态"),
                                        dataIndex: 'AEState',
                                        width: 100
                                    },
                                    {
                                        xtype:'actioncolumn',
                                        header:Ext.tzGetResourse("TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.viewAct", "查看"),
                                        align:'center',
                                        items:[
                                            {
                                                iconCls:'view',
                                                sortable:false,
                                                handler: "viewJcbb",
                                                isDisabled:function(view ,rowIndex ,colIndex ,item,record ){
                                                    if(record.get('AEState')=='成功'){
                                                        return false;
                                                    }else{
                                                        return true;
                                                    }
                                                }
                                            }
                                        ],
                                        width:60
                                    }
                                ],
                                bbar: {
                                    xtype: 'pagingtoolbar',
                                    pageSize: 5,
                                    store: jcbbListStore,
                                    displayInfo: true,
                                    displayMsg: '显示{0}-{1}条，共{2}条',
                                    beforePageText: '第',
                                    afterPageText: '页/共{0}页',
                                    emptyMsg: '没有数据显示',
                                    plugins: new Ext.ux.ProgressBarPager()
                                }
                            }
                        ]
                    }
                ]


            }]
        });
        this.callParent();
    }
});
