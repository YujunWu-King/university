Ext.define('KitchenSink.view.classManage.clsHCGZ.hcgzClassListWin', {
    extend: 'Ext.window.Window',
    reference: 'hcgzClassListWin',
    xtype: 'hcgzClassListWin',
    controller:'hcgzClassListController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.classManage.clsHCGZ.hcgzClassListStore',
        'KitchenSink.view.classManage.clsHCGZ.hcgzClassListController'
    ],
    width: 800,
    height:500,
    columnLines: true,
    //ignoreChangesFlag: true,
    title:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZCLS_STD.hcgzClassListWinTitle","班级列表") ,
    resizable: false,
    modal: true,
    //closeAction: 'hide',
    initComponent: function () {
        var hcgzClassListStore = new KitchenSink.view.classManage.clsHCGZ.hcgzClassListStore();
        Ext.apply(this, {
            items:[{
                xtype: 'form',
                border: false,
                items:[{
                    xtype:"displayfield",
                    fieldLabel:"规则编号",
                    name:"hcgzId",
                    hidden:true
                }]
            },{
                xtype: 'grid',
                columnLines: true,
                height:392,
                selModel: {
                    type: 'checkboxmodel'
                },
                multiSelect: true,
                viewConfig: {
                    enableTextSelection: true
                },
                header:false,
                columns: [{
                    text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZCLS_STD.hcgzClassId","班级编号") ,
                    dataIndex: 'hcgzClassId',
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZCLS_STD.hcgzClassName","班级名称"),
                    dataIndex: 'hcgzClassName',
                    flex:2
                }],
                store: hcgzClassListStore
            }]

        });
        this.callParent();
    },
    buttons: [{
        text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZCLS_STD.butEnsure","确定") ,
        iconCls:"ensure",
        handler: 'onWinConfirm'
    }, {
        text:Ext.tzGetResourse("TZ_CLS_HCGZ_COM.TZ_CLS_HCGZCLS_STD.butClose","关闭") ,
        iconCls:"close",
        handler: 'onWinClose'
    }]
});


