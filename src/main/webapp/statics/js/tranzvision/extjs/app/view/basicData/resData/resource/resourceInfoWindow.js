Ext.define('KitchenSink.view.basicData.resData.resource.resourceInfoWindow', {
    extend: 'Ext.window.Window',
    xtype: 'resourceInfoWindow',
    title: '资源信息',
    reference: 'resourceInfoWindow',
    width: 600,
    height: 320,
    minWidth: 300,
    minHeight: 320,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'destroy',
    actType: 'add',
    items: [{
        xtype: 'form',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        //heigth: 600,

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 150,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZY_RESSET_COM.TZ_RESSET_RES_STD.resourceID","资源编号"),
            name: 'resourceID',
            maxLength: 18,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZY_RESSET_COM.TZ_RESSET_RES_STD.resourceName","资源名称"),
            name: 'resourceName'
        }, {
            xtype: 'combo',
            fieldLabel: Ext.tzGetResourse("TZ_ZY_RESSET_COM.TZ_RESSET_RES_STD.fileType","文件类型"),
            name: 'fileType',
            emptyText:'请选择',
            mode:"local",
            valueField: 'TValue',
            displayField: 'TSDesc',
            triggerAction: 'all',
            editable : false,
            store:new KitchenSink.view.common.store.appTransStore("TZ_RESSET_FILETYPE")
        },{
            xtype: 'textfield',
            fieldLabel  : Ext.tzGetResourse("TZ_ZY_RESSET_COM.TZ_RESSET_RES_STD.filePath","文件路径"),
            name      : 'filePath'
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZY_RESSET_COM.TZ_RESSET_RES_STD.fileName","文件名称"),
            name: 'fileName'
        }, {
            xtype: 'hiddenfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZY_RESSET_COM.TZ_RESSET_RES_STD.resSetID","资源集合编号"),
            name: 'resSetID'
        }]
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onResourceInfoSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onResourceInfoEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onResourceInfoClose'
    }]
});
