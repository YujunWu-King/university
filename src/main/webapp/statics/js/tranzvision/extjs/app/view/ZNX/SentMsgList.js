/**
 * Created by carmen on 2015/11/12.
 */
/**
 * Created by carmen on 2015/11/11.
 */
Ext.define('KitchenSink.view.ZNX.SentMsgList', {
    extend: 'Ext.container.Container',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.ZNX.MsgSentListModel',
        'KitchenSink.view.ZNX.MsgSentListStore',
        'KitchenSink.view.ZNX.MsgController',
        'KitchenSink.view.ZNX.Menu',
        'KitchenSink.view.ZNX.MenuModel',
        'KitchenSink.view.ZNX.MessageModel',
        'KitchenSink.view.ZNX.SentList',
        'KitchenSink.view.ZNX.AddMsgModel',
        'KitchenSink.view.ZNX.AddMsg',

        'tranzvision.extension.grid.column.Link'
    ],

    xtype: 'SentMsgList',

    controller: 'MsgController',

    viewModel: {
        type: 'MessageModel'
    },
    title:'发件箱',
    itemId: 'emailMainContainer',
    layout: {
        type: 'hbox',
        align: 'stretch'
    },

    style:'background-color:#f6f6f6',
    // margin: '20 0 0 20',

    items: [
        {
            xtype: 'container',
            itemId: 'navigationPanel',
            width: '20%',
            minWidth: 180,
            maxWidth: 240,

            defaults: {
                cls: 'navigation-email'
                // margin: '0 20 20 0'
            },

            items: [
                {
                    xtype: 'Msgmenu'

                }

            ]
        },
        {
            xtype: 'container',
            itemId: 'contentPanel',
            // margin: '0 20 20 0',
            flex: 1,
            layout: {
                type : 'anchor',
                anchor : '100%'
            },
            items: [
                {
                    xtype: 'SentList',
                    flex: 1
                }]
        }
    ]
});
