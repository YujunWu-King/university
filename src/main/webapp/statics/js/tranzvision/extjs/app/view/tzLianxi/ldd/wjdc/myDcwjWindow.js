Ext.syncRequire("KitchenSink.view.tzLianxi.ldd.wjdc.dcwjSideNavigationTabs");
Ext.define('KitchenSink.view.tzLianxi.ldd.wjdc.myDcwjWindow', {
    extend: 'Ext.window.Window',
    xtype: 'myDcwjWindow',
   // controller:'wjdcController',
    reference: 'myDcwjWindow',
    title: '新建调查',
    closable: true,
    modal: true,
    autoScroll: true,
    bodyStyle: 'padding: 5px;',
    actType: 'add',
    items: [{
        xtype:'dcwj-side-navigation-tabs'
    }],
    buttons: [{
        text: '确定',
        iconCls: "ensure",
        handler: 'onNewWjEnsure'
    },
        {
            text: '关闭',
            iconCls: "close",
            handler: 'onNewClose'
        }]


});
