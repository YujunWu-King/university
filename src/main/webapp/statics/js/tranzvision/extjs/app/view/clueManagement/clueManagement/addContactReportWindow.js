Ext.define('KitchenSink.view.clueManagement.clueManagement.addContactReportWindow', {
    extend: 'Ext.window.Window',
    controller:"clueContactReportController",
    title: "联系报告",
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.clueManagement.clueManagement.MsgAttachmentStore'
    ],
    width: 800,
    modal:true,
    style:'padding-top:15px',
    layout: {
        type: 'fit'
    },

    constructor:function(leadID,store,timeLineObj){
    	this.timeLineObj = timeLineObj;
    	
        Ext.apply(this,{
            items:[{
                xtype:'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold;'
                },
                items:[{
                    xtype:'textfield',
                    name:"leadID",
                    value:leadID,
                    hidden:true,
                    ignoreChangesFlag: true
                },{
                    xtype:'textfield',
                    name:"LXBGID",
                    hidden:true,
                    ignoreChangesFlag: true
                },{
                    xtype:'combo',
                    fieldLabel: "主题",
                    displayField:'TZ_LABEL_NAME',
                    valueField:'TZ_LABEL_NAME',
                    queryMode:'local',
                    store:store,
                    name:"title",
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    style:'padding-left:15px;padding-right:15px',
                    allowBlank:false
                },{
                    xtype:'datefield',
                    fieldLabel: "日期",
                    name:'date',
                    afterLabelTextTpl: ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    format: 'Y-m-d',
                    style:'padding-left:15px;padding-right:15px',
					value:new Date(),
                    allowBlank:false
                },{
                    xtype:'textareafield',
                    fieldLabel:'详细信息',
                    name:'detail',
                    style:'padding-left:15px;padding-right:15px'
                },{
                    xtype: 'panel',
                    title: '上传附件',
                    collapsible:true,
                    width:1058,
                    collapsed:false,
                    margins:'5 0 5 5',
                    layout:'fit',
                    items: [
                        {
                            xtype: 'grid',
                            name: 'attachmentGrid',
                            reference: 'attachmentGrid',
                            minHeight: 200,
                            store: {
                                type: 'MsgAttachmentStore'
                            },
                            selModel: {
                                type: 'checkboxmodel'
                            },
                            tbar: [
                                {
                                    xtype: 'form',
                                    bodyStyle: 'padding:5px 0px 0px 0px',
                                    items: [
                                        {
                                            xtype: 'fileuploadfield',
                                            buttonText: '上传附件',
                                            name: 'orguploadfile',
                                            buttonOnly: true,
                                            width: 70,
                                            listeners: {
                                                change: 'addAttach'
                                            }
                                        }
                                    ]},
                                "-",
                                {iconCls: 'remove', text: '删除', tooltip: "删除选中的数据", handler: 'deleteArtAttenments'}
                            ],
                            columns: [{
                                text: '附件名称',
                                dataIndex: 'attachmentName',
                                sortable: false,
                                minWidth: 100,
                                flex: 1,
                                renderer: function (v, d) {
                                    return '<a href="' + d.record.data.attachmentUrl + '" target="_blank" download>' + v + '</a>';
                                }
                            },
                            {
                                menuDisabled: true,
                                sortable: false,
                                width: 60,
                                xtype: 'actioncolumn',
                                items: [
                                    {iconCls: 'remove', tooltip: '删除', handler: 'deleteArtAttenments'}
                                ]
                            }],
                            bbar:{
                                xtype:'pagingtoolbar',
                                pageSize:10,
                                listeners:{
                					afterrender: function(pbar){
                						var grid = pbar.findParentByType("grid");
                						pbar.setStore(grid.store);
                					}
                				},
                                plugins: new Ext.ux.ProgressBarPager()
                            }
                        }
                    ]
                }]
            }]
        });
        this.callParent();
    },
    buttons: [ {
        text: '保存',
        iconCls:"save",
        name:'reportSaveBtn',
        handler: 'addReportSave'
    },{
        text: '确定',
        iconCls:"ensure",
        name:'reportEnsureBtn',
        handler: 'addReportSave'
    }, {
        text: '关闭',
        iconCls:"close",
        name:'reportCloseBtn',
        handler: 'addReportClose'
    }]
});