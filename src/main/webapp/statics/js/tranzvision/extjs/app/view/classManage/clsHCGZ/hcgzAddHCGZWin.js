Ext.define('KitchenSink.view.classManage.clsHCGZ.hcgzAddHCGZWin', {
    extend: 'Ext.window.Window',
    reference: 'hcgzAddHCGZWin',
    xtype: 'hcgzAddHCGZWin',
    controller:'hcgzListController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*'
    ],
    width: 600,
    height:250,
    columnLines: true,
    //ignoreChangesFlag: true,
    title:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.hcgzAddHCGZWinTitle","新增规则") ,
    resizable: false,
    modal: true,
    closeAction: 'hide',
    initComponent: function () {
        Ext.apply(this, {
            items:[{
                xtype: 'form',
                border: false,
                bodyPadding: 10,
                fieldDefaults:{
                    labelWidth:80,
                    labelAlign:"left"
                },
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items:[{
                    xtype:"displayfield",
                    fieldLabel:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.hcgzId","规则编号"),
                    name:"hcgzId",
                    value:'NEXT'
                },{
                    xtype: 'textfield',
                    fieldLabel:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.hcgzName", '规则名称'),
                    name: 'hcgzName'
                }]
            }]

        });

        this.callParent();
    },
    buttons: [{
        text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.butEnsure","确定") ,
        iconCls:"ensure",
        handler: 'onWinConfirm'
    }, {
        text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZ_STD.butClose","关闭") ,
        iconCls:"close",
        handler: 'onWinClose'
    }]
});


