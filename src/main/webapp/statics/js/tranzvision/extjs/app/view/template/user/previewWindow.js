Ext.define('KitchenSink.view.template.user.previewWindow', {
    extend: 'Ext.window.Window',
    xtype: 'previewWindow',
    requires: [
            'Ext.data.*',
            'Ext.grid.*',
            'Ext.util.*',
            'Ext.toolbar.Paging',
            'Ext.ux.ProgressBarPager',
            'KitchenSink.view.template.user.regManageController'
    ],
    title: '注册页面预览',
    reference: 'previewWindow',
    width: 1100,
    height: 500,
    minWidth: 300,
    minHeight: 380,
    layout: 'fit',
    resizable: true,
    modal: true,
    autoScroll: true,
    closeAction: 'hide',
    actType: 'add',
    html: "<b>正文</b>",
    buttons: [{
        text: '关闭',
        iconCls: "close",
        handler: 'onOptionsClose'
    }]
});