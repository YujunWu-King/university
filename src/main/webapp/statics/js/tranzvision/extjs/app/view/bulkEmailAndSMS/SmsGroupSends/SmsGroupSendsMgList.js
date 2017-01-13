Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsMgList', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.grid.plugin.Clipboard',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsMgStore',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsMgController',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsReceiverStore',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsSmsTmplStore',
        'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsSmsItemStore',
		'KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsTransmitStore'
    ],
    xtype: 'SmsGroupSendsMg',
    title:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.panelTitle","短信发送管理"),
    controller: 'SmsGroupSendsMgController',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;/*button height plus panel body padding*/
            var grid = panel.child('grid');
            if(grid) grid.setHeight( height -buttonHeight -8);
        }
    },

    initComponent: function () {
        var SmsGroupSendsMgrStore = new KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.SmsGroupSendsMgStore();
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
                            text:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.tbarQuery","查询"),
                            tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.tbarTipQuery","查询数据"),
                            iconCls:"query",
                            handler:'querySmsGroupSends'
                        },"-",{
                            text:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.tbarAdd","新增"),
                            tooltip:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.tbarTipAdd","新增数据"),
                            iconCls:"add",
                            handler:'addSmsGroupSends'
                        }
                    ]
                }],
                columns: [{
                        text: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.rowNum","序号"),
                        xtype: 'rownumberer',
                        width:50
                    },
                    {
                        text:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.smsQfDesc","群发任务名称") ,
                        dataIndex: 'smsQfDesc',
						minWidth:160,
                        flex:1
                    }/*,{
                        text:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.dept","所属部门"),
                        dataIndex: 'dept',
                        width:150
                    }*/,{
                        text:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.crePer","创建人") ,
                        dataIndex: 'crePer',
                        width:110
                    },{
                        text:Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.creDt","创建时间"),
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
                                tooltip: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.editSmsGsend","编辑"),
                                handler:'editSmsTask'
                            },{
                                iconCls: 'view',
                                tooltip: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.viewHistory","查看邮件发送历史"),
                                handler:'viewSmsHistory'
                            }
                        ]
                    }],
                store: SmsGroupSendsMgrStore,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: SmsGroupSendsMgrStore,
					/*
                    displayInfo: true,
                    displayMsg:"显示{0}-{1}条，共{2}条",
                    beforePageText:"第",
                    afterPageText:"页/共{0}页",
                    emptyMsg: "没有数据显示",
					*/
                    plugins: new Ext.ux.ProgressBarPager()
                }
            }]
        });
        this.callParent();
    },
    buttons: [ {
        text: Ext.tzGetResourse("TZ_SMSQ_COM.TZ_SMSQ_MGR_STD.close","关闭"),
        iconCls:"close",
        handler: 'onPanelClose'
    }]
});

