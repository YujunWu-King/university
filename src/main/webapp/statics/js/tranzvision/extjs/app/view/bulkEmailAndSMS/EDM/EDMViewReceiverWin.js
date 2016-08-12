Ext.define('KitchenSink.view.bulkEmailAndSMS.EDM.EDMViewReceiverWin', {
    extend: 'Ext.window.Window',
    xtype: 'EDMViewReceiver',
    controller:'EDMSetController',
    reference:'EDMViewReceiverWin',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bulkEmailAndSMS.EDM.EDMViewRecevierModel',
        'KitchenSink.view.bulkEmailAndSMS.EDM.EDMViewRecevierStore'
    ],
    title: '收件人列表',
    layout: 'fit',
    modal: true,
    autoScroll: true,
    header:true,
    height: 400,
    width: 800,
    items: [
        {
            xtype: 'grid',
            columnLines: true,
            viewConfig: {
                enableTextSelection: true
            },
            store: {
                type: 'EDMViewRecevierStore'
            },
            columns: [
                {
                    text: '收件人姓名',
                    dataIndex: 'personName',
                    width: 200
                },
                {
                    text: '邮箱',
                    dataIndex: 'personEmail',
                    width: 300
                },
                {
                    text: '手机',
                    dataIndex: 'personMobile',
                    flex:1
                }
            ],
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                listeners:{
                    afterrender: function(pbar){
                        var grid = pbar.findParentByType("grid");
                        pbar.setStore(grid.store);
                    }
                },
                plugins: new Ext.ux.ProgressBarPager()
            }
        }],
        buttons: [
        {
            text: '关闭',
            iconCls: "close",
            handler:function(btn){
                var win=btn.findParentByType("window");
                win.close();
            }
        }
    ]
});