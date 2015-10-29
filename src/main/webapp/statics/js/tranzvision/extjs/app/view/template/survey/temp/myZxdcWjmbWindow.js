Ext.syncRequire("KitchenSink.view.template.survey.temp.WjmbSideNavigationTabs");
Ext.define('KitchenSink.view.template.survey.temp.myZxdcWjmbWindow', {
    extend: 'Ext.window.Window',
    xtype: 'myZxdcWjmbWindow',
    reference: 'myZxdcWjmbWindow',
    title: '新建问卷',
    closable: true,
    modal: true,
    autoScroll: true,
    bodyStyle: 'padding: 5px;',
    actType: 'add',
    items: [{
        xtype:'wjmb-side-navigation-tabs'
    }],
    buttons: [{
        text: '确定',
        iconCls: "ensure",
        handler: 'onNewEnsure'
    },
      {
            text: '关闭',
            iconCls: "close",
            handler: 'onNewClose'
      }]

});
