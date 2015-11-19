Ext.define('KitchenSink.view.ZNX.RecMsgInfo', {
    extend: 'Ext.form.Panel',
    xtype: 'RecMsgInfo',
    itemID:'RecForm',

    requires: [
        'KitchenSink.view.ZNX.MsgRecInfoModel',
        'Ext.container.Container',
        'Ext.form.field.HtmlEditor',
        'Ext.layout.container.Anchor',
        'Ext.layout.container.HBox'
    ],

    cls: 'shadow-panel',
    bodyPadding: 10,
    layout : {
        type : 'anchor',
        anchor : '100%'
    },
  //  autoScroll : true,

    listeners: {
        beforerender: 'beforeMsgRender'
    },
    tbar: [
        {
            iconCls: 'x-fa fa-angle-left',
            listeners: {
                click: 'onBackBtnClick'
            }
        },

        {
                iconCls: 'x-fa fa-trash',
                handler: 'deleteCurrentZNXMsg'
        },
        {

            iconCls: 'x-fa fa-forward',
           handler:'getNextMsg'

        }

    ],

    bbar: {
        cls: 'single-mail-action-button',
        defaults: {
            margin:'0 15 0 0'
        },
        overflowHandler: 'menu',
        items: [
            '->',
            {
                text: '发送',
                iconCls: "ensure",
                handler: 'onMsgReply'
            }, {
                text: '关闭',
                iconCls: "close",
                handler: 'onBackBtnClick'
            }
        ]
    },

    items: [
        {
            xtype: 'container',
            height: 82,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [

                {
                    xtype: 'component',
                    flex: 1,
                    cls: 'single-mail-email-subject',
                    data: {},
                    itemId: 'emailSubjectContainer',
                    padding: 10,
                    tpl: [
                        '<div class="user-name">{SendID}</div>',
                        '<div class="user-info">{MsgSub}</div>'
                    ]
                }
            ]
        },
        {
            xtype: 'box',
            cls: 'mail-body',
            itemId: 'mailBody',
            data: {},
            tpl: [
                '<div class="user-name">{MsgText}</div>'
            ]
        },
        {
            xtype: 'box',
            itemId: 'MsgID',
            data: {},
            tpl: [
                '<div class="user-name">{MsgID}</div>'
            ]
        },
        {
            xtype: 'box',
            itemId: 'attachments',
            cls:'attachment-container',
            data: null,
            tpl: [
                '<tpl for=".">',
                '<img class="single-mail-attachment" src="resources/images/{.}" ',
                'alt="profile image">',
                '</tpl>'
            ]
        },
        {
            xtype: 'ueditor',
            fieldLabel: '回复',
            data: {},
            itemId:'MsgReply',
            zIndex: 999,
            height: 300,
            allowBlank: true,
            labelAlign: 'top',
            labelSeparator: ''

        }

    ]
});
