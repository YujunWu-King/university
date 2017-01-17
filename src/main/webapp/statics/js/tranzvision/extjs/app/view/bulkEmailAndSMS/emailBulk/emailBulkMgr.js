Ext.define('KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkMgr', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.grid.plugin.Clipboard',
        'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkMgrStore',
        'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkMgrController',
        'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetSenderStore',
        'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetReceiverStore',
        'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetCCStore',
        'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetEmlTmplStore',
        'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkAttaStore',
        'KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkDetEmlItemStore'
    ],
    xtype: 'emailBulkMgr',
    title:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.panelTitle","邮件发送管理"),
    controller: 'emailBulkMgrController',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;/*button height plus panel body padding*/
            var grid = panel.child('grid');
            if(grid) grid.setHeight( height -buttonHeight -8);
        }
    },

    initComponent: function () {
        var emailBulkMgrStore = new KitchenSink.view.bulkEmailAndSMS.emailBulk.emailBulkMgrStore();
        Ext.apply(this, {
            items: [{
                xtype: 'grid',
                columnLines: true,
                style:"margin:8px",
                viewConfig: {
                    enableTextSelection: true
                },
                header:false,
                frame: true,
                dockedItems:[{
                    xtype:"toolbar",
                    items:[
                        {
                            text:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.tbarQuery","查询"),
                            tooltip:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.tbarTipQuery","查询数据"),
                            iconCls:"query",
                            handler:'queryEmlTsks'
                        },"-",{
                            text:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.tbarAdd","新增"),
                            tooltip:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.tbarTipAdd","新增数据"),
                            iconCls:"add",
                            handler:'addEmlTsks'
                        }
                    ]
                }],
                columns: [{
                        text: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.rowNum","序号"),
                        xtype: 'rownumberer',
                        width:50
                    },
                    {
                        text:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.emlQfDesc","群发任务名称") ,
                        dataIndex: 'emlQfDesc',
                        flex:1
                    }/*,{
                        text:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.dept","所属部门"),
                        dataIndex: 'dept',
                        width:150
                    }*/,{
                        text:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.emlSubj","邮件主题"),
                        dataIndex: 'emlSubj',
                        flex:1
                    },{
                        text:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.crePer","创建人") ,
                        dataIndex: 'crePer',
                        width:110
                    },{
                        text:Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.creDt","创建时间"),
                        dataIndex: 'creDt',
                        width:150
                    },{
                        xtype: 'actioncolumn',
                        width:100,
                        header:'操作',
                        menuDisabled: true,
                        sortable: false,
                        items:[
                            {
                                iconCls: 'edit',
                                tooltip: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.editEmlTsk","编辑"),
                                handler:'editEmialTask'
                            },{
                                iconCls: 'view',
                                tooltip: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.vwHistory","查看邮件发送历史"),
                                handler:'viewEmailHistory'
                            },{
                                iconCls: 'frequency_report',
                                tooltip: Ext.tzGetResourse("TZ_EMLQ_COM.TZ_EMLQ_MGR_STD.vwEmlCou","查看邮件发送统计"),
                                handler:'viewEmailCount',
								isDisabled:function(view ,rowIndex ,colIndex ,item,record ){
									var edmFlag = record.get('edmFlag');
									if(edmFlag == "Y"){
										return false;
									}else{
										return true;
									}
								}
                            }
                        ]
                    }],
                store: emailBulkMgrStore,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: emailBulkMgrStore,
                    displayInfo: true,
                    displayMsg:"显示{0}-{1}条，共{2}条",
                    beforePageText:"第",
                    afterPageText:"页/共{0}页",
                    emptyMsg: "没有数据显示",
                    plugins: new Ext.ux.ProgressBarPager()
                }
            }]
        });
        this.callParent();
    },
    buttons: [ {
        text: '关闭',
        iconCls:"close",
        handler: 'onPanelClose'
    }]
});

