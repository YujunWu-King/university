Ext.define('KitchenSink.view.bulkEmailAndSMS.SmsGroupSends.pasteFromExcelWinSMS', {
    extend: 'Ext.window.Window',
    reference: 'pasteFromExcelWinSMS',
    xtype: 'pasteFromExcelWinSMS',
    controller:'SmsGroupSendsDefnController',
    requires: [
        'Ext.data.*',
        'Ext.util.*'
    ],
    width: 600,
    minHeight:250,
    maxHeight:500,
    ignoreChangesFlag: true,
    style:"margin:8px",
    title: '从Excel粘贴收件人',
    layout: 'fit',
    resizable: false,
    modal: true,
    initComponent: function () {
        Ext.apply(this, {
            items:[{
                xtype: 'form',
                layout: {
                    type: 'vbox',      // Arrange child items vertically
                    align: 'stretch'    // 控件横向拉伸至容器大小
                },
                border: false,
                bodyPadding: 8,
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 120,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype:'label',
                    style:{
                        padding:'8px 0 0 0',
                        'font-weight':'bold'
                    },
                    text:"手机："
                },{
                    xtype: 'textarea',
                    name: 'excelText',
                    msgTarget: 'side',
                    grow      : true,
                    growMax :500,
                    height:200,
                    anchor    : '100%',
                    allowBlank: false
                }]
            }]
        });
        this.callParent();
    },
    buttons: [ {
        text: '确定',
        iconCls:"ensure",
        handler: 'onWinEnsure'
    },{
        text: '关闭',
        iconCls:"close",
        handler: 'onWinClose'
    }]
});


