/**
 * Created by carmen on 2015/11/11.
 */
Ext.define('KitchenSink.view.ZNX.Menu', {
    extend: 'Ext.menu.Menu',
    alias: 'widget.Msgmenu',
    requires: [
        'KitchenSink.view.ZNX.MenuModel'
        ],
        viewModel: {
            type: 'MenuModel'
        },
        //title: '站内信',
        iconCls: 'x-fa fa-group',
       //  renderTo: Ext.getBody(),
         minHeight:311,
          floating: false,

    items: [
        {
            routeId: 'AddMsg', //xtype and used for url routing
            params: {
                openWindow: true, // Let the controller know that we want this component in the window,
                targetCfg: {
                    //put any extra configs for your view here
                },
                windowCfg: {
                    // Any configs that you would like to apply for window popup goes here
                    title: '写站内信'
                }
            },
            iconCls: 'x-fa fa-edit',
            text: '写站内信'
        },

        {
            routeId: 'RecList',
            iconCls: 'x-fa fa-inbox',
            params: {
                openWindow: false,
                targetCfg: {
                },
                windowCfg: {
                    title: '收件箱'
                }
            },
            text: '收件箱'

        },
        {
            routeId: 'SentList',
            iconCls: 'x-fa fa-check-circle',
            params: {
                openWindow: false,
                targetCfg: {
                },
                windowCfg: {
                    title: '发件箱'
                }
            },
            text: '发件箱'

        }
    ]
});
