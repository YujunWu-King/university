Ext.define('KitchenSink.view.clueManagement.clueManagement.clueRelBmbWindow', {
    extend: 'Ext.window.Window',
    xtype: 'clueRelBmbWindow',
    title: '线索关联报名表',
    reference: 'clueRelBmbWindow',
    controller:'clueDealWithController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.clueManagement.clueRelBmbStore',
        'KitchenSink.view.clueManagement.clueManagement.clueDealWithController'
    ],
    height: 400,
    width: 1000,
    modal: true,
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    constructor:function(config){
        this.submitStateStore=config.submitStateStore;
        this.callParent();
    },
    initComponent: function () {
        var me=this;
        var submitStateStore=me.submitStateStore;
        Ext.apply(this, {
            items: [
                {
                    xtype: 'form',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    fieldDefaults: {
                        msgTarget: 'side',
                        labelWidth: 100,
                        labelStyle: 'font-weight:bold'
                    },
                    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                    border: false,
                    bodyPadding: 10,
                    items: [{
                            layout: {
                                type: 'column'
                            },
                            items: [
                                {
                                    columnWidth: 0.10,
                                    xtype: 'button',
                                    name:'queryMoreBmb',
                                    handler: 'queryClueBmb',
                                    text: '查询'
                                },
                                {   columnWidth: 0.55,
                                    xtype: 'displayfield',
                                    fieldLabel: '线索编号',
                                    name: 'clueId',
                                    hidden:true
                                }
                            ]
                        },
                        {   xtype: 'displayfield',
                            fieldLabel: '姓名',
                            name: 'name'
                        },
                        { xtype: 'displayfield',
                            fieldLabel: '手机',
                            name: 'mobile'
                        },
                        {
                            xtype:'displayfield',
                            fieldLabel:'描述',
                            hideLabel:true,
                            name:'resultDesc',
                            fieldStyle:'color:red'
                        }
                    ]
                },
                {
                    xtype: 'grid',
                    hidden:true,
                    reference: 'relBmbGrid',
                    columnLines: true,
                    minHeight: 150,
                    selModel: {
                        type: 'checkboxmodel'
                    },
                    columns: [
                        {
                            text: '报名表编号',
                            dataIndex: 'bmbId',
                            xtype:'linkcolumn',
                            handler:'viewBmbDetail',
                            renderer:function(v) {
                                this.text=v;
                                this.tooltip=v;
                                return;
                            },
                            width:90
                        },
                        {
                            text: '提交状态',
                            dataIndex: 'bmbSubStatus',
                            renderer: function (v) {
                                if (v) {
                                    var index = submitStateStore.find('TValue', v, 0, false, true, true);
                                    if (index > -1) {
                                        return submitStateStore.getAt(index).get("TSDesc");
                                    }

                                    return "";
                                }
                            },
                            width:90
                        },
                        {
                            text: '姓名',
                            dataIndex: 'bmbName',
                            width:80
                        },
                        {
                            text: '公司',
                            dataIndex: 'bmbCompany',
                            width:120
                        },
                        {
                            text: '手机',
                            dataIndex: 'bmbPhone',
                            width:100
                        },
                        {
                            text: '邮箱',
                            dataIndex: 'bmbEmail',
                            width:150
                        },
                        {
                            text: '报考班级',
                            dataIndex: 'bmbClass',
                            flex:1
                        },
                        {
                            text: '报名时间',
                            dataIndex: 'bmbTime',
                            width:120
                        }
                    ],
                    store: {
                        type: 'clueRelBmbStore'
                    },
                    bbar: {
                        xtype: 'pagingtoolbar',
                        pageSize: 10,
                        listeners: {
                            afterrender: function (pbar) {
                                var grid = pbar.findParentByType("grid");
                                pbar.setStore(grid.store);
                            }
                        },
                        plugins: new Ext.ux.ProgressBarPager()
                    }
                }
            ],
            buttons: [
                {
                    text: '确定',
                    iconCls: "ensure",
                    handler: 'clueRelBmbWindowEnsure'
                },
                {
                    text: '关闭',
                    iconCls: "close",
                    handler: function (btn) {
                        var win = btn.findParentByType("window");
                        win.close();
                    }
                }
            ]
        });
        this.callParent();
    }
});



