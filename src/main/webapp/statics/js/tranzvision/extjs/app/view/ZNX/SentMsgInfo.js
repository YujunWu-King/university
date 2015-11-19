/**
 * Created by carmen on 2015/11/13.
 */
/**
 * Created by carmen on 2015/11/13.
 */
Ext.define('KitchenSink.view.ZNX.SentMsgInfo', {
    extend: 'Ext.form.Panel',
    xtype: 'SentMsgInfo',

    requires: [
        'KitchenSink.view.ZNX.MsgSentListModel',
        'KitchenSink.view.ZNX.MsgSentListStore',
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

    tbar: [
        // Default item type for toolbar is button, thus we can skip it's definition in
        // the array items
        {
            iconCls: 'x-fa fa-angle-left',
            listeners: {
                click: 'onBackBtnClick'
            }
        },
        {
            iconCls: 'x-fa fa-trash'
        },
        {
            iconCls: 'x-fa fa-exclamation-circle'
        },
        {
            iconCls:'x-fa fa-print'
        },
        {
            iconCls: 'x-fa fa-forward'
        }
    ],

    bbar: {
        cls: 'single-mail-action-button',
        defaults: {
            margin:'0 15 0 0'
        },
        items: [
            '->',
            {
                ui: 'gray',
                text: 'Save'
            },
            {
                ui: 'soft-green',
                text: 'Send'
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
                        '<div class="user-name">{to}</div>',
                        '<div class="user-info">{title}</div>'
                    ]
                }
            ]
        },
        {
            xtype: 'box',
            cls: 'mail-body',
            itemId: 'mailBody'
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
        }

    ]
});
