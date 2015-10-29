Ext.define('KitchenSink.view.tzLianxi.ytt.bugMg.bugInfoPanel', {
    requires: [
        'Ext.data.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.ytt.bugMg.bugAttachmentStore'
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
                    name: 'bugName'
                },{
                    xtype: 'datefield',
                    fieldLabel: '录入日期',
                    name: 'recDate',
                    allowBlank: false,
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
                        name: 'recOprID',
                        allowBlank: false,
                        triggers: {
                            search: {
                                cls: 'x-form-search-trigger',
                                handler: "selectOprID"
                            }
                        }
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
                        name: 'resOprID',
                        allowBlank: false,
                        triggers: {
                            search: {
                                cls: 'x-form-search-trigger',
                                handler: "selectOprID"
                            }
                        }
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
                                selModel: {
                                    type: 'checkboxmodel'
                                },
                                tbar: [
                                    {
                                        xtype: 'form',
                                        bodyStyle:'padding:10px 0px 0px 0px',
                                        items:[{
                                            xtype: 'fileuploadfield',
                                            buttonText: '上传附件',
                                            name: 'attachmentUpload',
                                            buttonOnly:true,
                                            width: 88,
                                            listeners:{
                                                change:'addAttach',
                                                keydown:'checkAtta'
                                                /*function(file, value, eOpts){
                                                 addAttach(file, value, "ATTACHMENT");
                                                 }*/
                                            }
                                        }]},"-",
                                    {iconCls: 'remove',text: '删除',tooltip:"删除选中的数据",handler: 'deleteArtAttenments'}],
                                columns: [{
                                    text: 'BUG编号',
                                    dataIndex: 'bugID',
                                    sortable: false,
                                    hidden: true
                                },{
                                    text: '附件编号',
                                    dataIndex: 'attachmentID',
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
                                },{
                                    menuDisabled: true,
                                    sortable: false,
                                    width:60,
                                    xtype: 'actioncolumn',
                                    items:[
                                        {iconCls: 'remove',tooltip: '删除', handler: 'deleteArtAttenments'}
                                    ]
                                }]
                            }]
                    }]
            }],
            buttons:[{
                text: '保存',
                handler:'bugInfoSave',
                iconCls:'save'
            },{
                text:'确定',
                handler:'bugInfoEnsure',
                iconCls:'ensure'
            },{
                text:'关闭',
                iconCls:'close',
                handler:'bugInfoClose'
            }]
        });
        this.callParent();
    }
});
