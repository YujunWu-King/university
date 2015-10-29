Ext.define('KitchenSink.view.tzLianxi.ytt.bugCk.bugInfoPanel', {
    requires: [
        'Ext.data.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.ytt.bugCk.bugAttachmentStore'
    ],
    extend : 'Ext.panel.Panel',
    controller:'bugController',
    autoScroll:false,
    actType:'add',
    name:'bugInfo',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    title:'Bug信息',
    frame:true,
    initComponent:function(){
        Ext.apply(this,{
            items:[{
                xtype:'form',
                frame:true,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 100,
                    labelStyle: 'font-weight:bold'
                },
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                margin:'8px',
                style:'border:0px',
                items: [{
                    xtype: 'textfield',
                    fieldLabel: 'BUG编号',
                    name: 'bugID',
                    editable:false,
                    value:'NEXT'
                }, {
                    xtype: 'textfield',
                    fieldLabel: '说明',
                    editable:false,
                    name: 'bugName'
                },{
                    xtype: 'datefield',
                    fieldLabel: '录入日期',
                    name: 'recDate',
                    allowBlank: false,
                    readOnly:true,
                    maxValue: new Date(),
                    format:'Y-m-d'
                }, {
                    layout: {
                        type: 'column',
                        align: 'stretch'
                    },
                    items:[{
                        xtype: 'textfield',
                        fieldLabel: '录入人',
                        editable:false,
                        name: 'recOprID',
                        allowBlank: false
                    }
                        ,{
                            columnWidth:.5,
                            xtype: 'displayfield',
                            hideLabel: true,
                            style:'margin-left:8px',
                            name: 'recOprName'
                        }
                    ]
                }, {
                    layout: {
                        type: 'column',
                        align: 'stretch'
                    },
                    items:[{
                        xtype: 'textfield',
                        fieldLabel: '责任人',
                        editable:false,
                        name: 'resOprID',
                        allowBlank: false
                    }
                        ,{
                            columnWidth:.5,
                            xtype: 'displayfield',
                            hideLabel: true,
                            style:'margin-left:8px',
                            name: 'resOprName'
                        }
                    ]
                }, {
                    xtype: 'datefield',
                    fieldLabel: '期望解决日期',
                    name: 'expDate',
                    readOnly:true,
                    minValue: new Date(),
                    format:'Y-m-d'
                }, {
                    xtype: 'combo',
                    fieldLabel: '处理状态',
                    name: 'status',
                    emptyText: '',
                    queryMode: 'local',
                    valueField: 'status',
                    displayField: 'desc',
                    editable:false,
                    triggerAction:false,
                    store: {
                        fields: ["status", "desc"],
                        data: [
                            {status: "0", desc: "新建"},
                            {status: "1", desc: "已分配"},
                            {status: "2", desc: '已修复'},
                            {status: "3", desc: '关闭'},
                            {status: "4", desc: '重新打开'},
                            {status: "5", desc: "取消"}
                        ]
                    }
                },
                    {
                        xtype: 'tabpanel',
                        frame: true,
                        activeTab: 0,
                        plain: false,
                        resizeTabs: true,
                        defaults: {
                            autoScroll: false
                        },
                        items:[
                            {
                                title:'描述信息',
                                xtype:'form',
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                style:'border:0',
                                items:[{
                                    xtype: 'ueditor',
                                    name: 'descript',
                                    zIndex:999,
                                    editable:false,
                                    readOnly:true,
                                    allowBlank: true

                                }]
                            },
                            {
                                xtype: 'grid',
                                title:'附件集',
                                height: 315,
                                frame: true,
                                columnLines: true,
                                name: 'attachmentGrid',
                                reference: 'attachmentGrid',
                                style:"border:0px",
                                store:{
                                    type:'bugAttachmentStore'
                                },
                                columns: [{
                                    text: 'BUG编号',
                                    dataIndex: 'bugID',
                                    sortable: false,
                                    hidden: true
                                },{
                                    text: '附件url',
                                    dataIndex: 'attachmentUrl',
                                    sortable: false,
                                    hidden: true
                                },{
                                    text: '附件名称',
                                    dataIndex: 'attachmentName',
                                    sortable: false,
                                    minWidth: 100,
                                    flex: 1,
                                    renderer: function(v,d){
                                        return '<a href="'+ d.record.data.attachmentUrl +'" target="_blank">'+v+'</a>';
                                    }
                                }
                                ]
                            }]
                    }]
            }],
            buttons:[{
                text:'关闭',
                iconCls:'close',
                handler:'bugInfoClose'
            }]
        });
        this.callParent();
    }
});
