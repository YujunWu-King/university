Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.znxMgr', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.grid.plugin.Clipboard',
        'KitchenSink.view.bulkEmailAndSMS.znx.znxMgrStore',
        'KitchenSink.view.bulkEmailAndSMS.znx.znxMgrModel',
        'KitchenSink.view.bulkEmailAndSMS.znx.znxDetReceiverStore',
        'KitchenSink.view.bulkEmailAndSMS.znx.znxDetZnxTmplStore',
        'KitchenSink.view.bulkEmailAndSMS.znx.znxAttaStore',
        'KitchenSink.view.bulkEmailAndSMS.znx.znxDetZnxItemStore',
        'KitchenSink.view.bulkEmailAndSMS.znx.znxMgrController'
    ],
    xtype: 'znxMgr',
    title:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.panelTitle","站内信发送管理"),
    controller: 'znxMgrController',
    listeners:{
        resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
            var buttonHeight = 44;/*button height plus panel body padding*/
            var grid = panel.child('grid');
            if(grid) grid.setHeight( height -buttonHeight -8);
        }
    },

    initComponent: function () {
        var znxMgrStore = new KitchenSink.view.bulkEmailAndSMS.znx.znxMgrStore();
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
                            text:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.tbarQuery","查询"),
                            tooltip:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.tbarTipQuery","查询数据"),
                            iconCls:"query",
                            handler:'queryZnxTsks'
                        },"-",{
                            text:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.tbarAdd","新增"),
                            tooltip:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.tbarTipAdd","新增数据"),
                            iconCls:"add",
                            handler:'addZnxTsks'
                        }
                    ]
                }],
                columns: [{
                        text: Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.rowNum","序号"),
                        xtype: 'rownumberer',
                        width:50
                    },
                    {
                        text:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.emlQfDesc","群发任务名称") ,
                        dataIndex: 'znxQfDesc',
                        flex:1
                    }/*,{
                        text:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.dept","所属部门"),
                        dataIndex: 'dept',
                        width:150
                    }*/,{
                        text:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.emlSubj","站内信标题"),
                        dataIndex: 'znxSubj',
                        flex:1
                    },{
                        text:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.crePer","创建人") ,
                        dataIndex: 'crePer',
                        width:110
                    },{
                        text:Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.creDt","创建时间"),
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
                                tooltip: Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.editZnxTask","编辑"),
                                handler:'editZnxTask'
                            },{
                                iconCls: 'view',
                                tooltip: Ext.tzGetResourse("TZ_ZNX_GL_COM.TZ_ZNX_GL_STD.vwHistory","查看站内信发送历史"),
                                handler:'viewZnxHistory'
                            }
                        ]
                    }],
                store: znxMgrStore,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: znxMgrStore,
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

