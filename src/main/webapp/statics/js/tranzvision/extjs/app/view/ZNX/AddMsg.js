
Ext.define('KitchenSink.view.ZNX.AddMsg', {
    extend: 'Ext.form.Panel',
    alias: 'widget.AddMsg',
    requires: [
        'KitchenSink.view.ZNX.AddMsgModel',
        'KitchenSink.view.ZNX.AddMsgStore',
        'KitchenSink.view.ZNX.MsgAttachmentModel',
        'KitchenSink.view.ZNX.MsgAttachmentStore',
        'Ext.button.Button',
        'Ext.form.field.Text',
        'Ext.form.field.File',
        'Ext.form.field.HtmlEditor'
    ],


   /* viewModel: {
        type: 'AddMsgModel'
    },*/

    controller: 'MsgController',
    actType:'add',
    cls: 'email-compose',

    layout: {
        type:'vbox',
        align:'stretch'
    },

    bodyPadding: 10,
    scrollable: true,

    defaults: {
        labelWidth: 60,
        labelSeparator: ''
    },

    initComponent:function(){
        Ext.apply(this,{
            items:[
                {
                    xtype: 'form',
                    frame: true,
                    fieldDefaults: {
                        msgTarget: 'side',
                        labelWidth: 100,
                        labelStyle: 'font-weight:bold'
                    },
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    margin: '8px',
                    style: 'border:0px',


                    bodyPadding: 10,
                    scrollable: true,

                    defaults: {
                        labelWidth: 60,
                        labelSeparator: ''
                    },

                    items: [
                        {
                            xtype: 'textfield',
                            fieldLabel: '信息编号',
                            name: 'MsgID',
                            editable: false,
                            readOnly: true,
                            cls: 'lanage_1',
                            // hidden:true,
                            value: 'NEXT'

                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '信息类别',
                            name: 'MsgKind',
                            value: '0',
                            hidden:true
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '发件人删除状态',
                            name: 'MsgDelStatus',
                            value: '1',
                             hidden:true
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '收件人',
                            name: 'RecID'
                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '发送人',
                            name: 'SendID',
                            allowBlank: false,
                            editable: true,
                            value: TranzvisionMeikecityAdvanced.Boot.loginUserId

                        },
                        {
                            xtype: 'textfield',
                            fieldLabel: '主题',
                            name: 'MsgSub'

                        },
                   /*     {
                            xtype: 'datefield',
                            fieldLabel: '日期',
                            name: 'MsgDate',
                            // hidden:true,
                           // maxValue: new Date(),
                            value:new Date(),
                          //  value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.MONTH, 0), "Y-m-d"),
//                        value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d"),

                            format: 'Y-m-d H:i:s '
                        },*/

                        {
                            fieldLabel: '内容',
                            xtype: 'form',
                            name: 'MsgText',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            height: 415,
                            style: 'border:0',
                            items: [
                                {
                                    xtype: 'ueditor',
                                    name: 'descript',
                                    zIndex: 999,
                                    height: 415,
                                    allowBlank: true

                                }
                            ]
                        },
                        {
                            xtype: 'grid',
                            title: '附件',
                            height: 100,
                            frame: true,
                            columnLines: true,
                            name: 'attachmentGrid',
                            reference: 'attachmentGrid',
                            style: "border:0px",
                            store: {
                                type: 'MsgAttachmentStore'
                            },
                            selModel: {
                                type: 'checkboxmodel'
                            },
                            tbar: [
                                {
                                    xtype: 'form',
                                    bodyStyle: 'padding:10px 0px 0px 0px',
                                    items: [
                                        {
                                            xtype: 'fileuploadfield',
                                            buttonText: '上传附件',
                                            name: 'attachmentUpload',
                                            buttonOnly: true,
                                            width: 88,
                                            listeners: {
                                                change: 'addZNXAttach'
                                                //keydown: 'checkAtta'
                                                /*function(file, value, eOpts){
                                                 addAttach(file, value, "ATTACHMENT");
                                                 }*/
                                            }
                                        }
                                    ]},
                                "-",
                                {iconCls: 'remove', text: '删除', tooltip: "删除选中的数据", handler: 'deleteArtAttenments'}
                            ],
                            columns: [
                                {
                                    text: '附件名称',
                                    dataIndex: 'attachmentName',
                                    sortable: false,
                                    minWidth: 100,
                                    flex: 1,
                                    renderer: function (v, d) {
                                        return '<a href="' + d.record.data.attachmentUrl + '" target="_blank">' + v + '</a>';
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
                                }
                            ]
                        }
                    ],
                    bbar: {
                        overflowHandler: 'menu',
                        items: [

                            '->',
                            /*  {
                             text: '保存',
                             iconCls: "save",
                             handler: 'onMsgSave'
                             },*/ {
                                text: '发送',
                                iconCls: "ensure",
                                handler: 'onMsgSend'
                            }, {
                                text: '关闭',
                                iconCls: "close",
                                handler: 'onMsgClose'
                            }
                        ]


                    }
                }]
        })
        this.callParent();
    }

});
