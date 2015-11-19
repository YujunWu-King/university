/**
 * Created by carmen on 2015/11/11.
 */
Ext.define('KitchenSink.view.ZNX.Message', {
    extend: 'Ext.container.Container',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',

        'KitchenSink.view.ZNX.Window',

        'KitchenSink.view.ZNX.MessageModel',

        'KitchenSink.view.ZNX.MsgController',

        'KitchenSink.view.ZNX.MsgRecListModel',
        'KitchenSink.view.ZNX.MsgRecListStore',
        'KitchenSink.view.ZNX.RecList',

       // ' KitchenSink.view.ZNX.MsgRecInfoModel',
       // 'KitchenSink.view.ZNX.MsgRecInfoStore',
        'KitchenSink.view.ZNX.RecMsgInfo',


        'KitchenSink.view.ZNX.Menu',
        'KitchenSink.view.ZNX.MenuModel',

        'KitchenSink.view.ZNX.AddMsgModel',
        'KitchenSink.view.ZNX.AddMsgStore',
        'KitchenSink.view.ZNX.AddMsg',

        'KitchenSink.view.ZNX.MsgSentListModel',
        'KitchenSink.view.ZNX.MsgSentListStore',
       'KitchenSink.view.ZNX.SentList',
   //    'KitchenSink.view.ZNX.SentMsgInfo',

        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'Message',
    controller: 'MsgController',
    title:'站内信',
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    style:'background-color:#f6f6f6',
   //margin: '10 10 10 10',
    items: [
        {
            xtype: 'container',
            itemId: 'navigationPanel',
            width: '20%',
            minWidth: 180,
            maxWidth: 240,
            defaults: {
                cls: 'navigation-email',
              margin: '10 10 10 10'
            },
            items: [
                {
                    xtype: 'Msgmenu',
                    listeners: {
                        click: 'onMenuClick'
                    }

                }]
        },
        {
            xtype: 'container',
            itemId: 'ZNXContentPanel',
          // margin: '0 20 20 0',
            autoScroll : true,
           margin:  '10 10 10 10',
            flex: 1,
            layout: {
                type : 'anchor',
                anchor : '100%'
            }

        }

    ]
});
