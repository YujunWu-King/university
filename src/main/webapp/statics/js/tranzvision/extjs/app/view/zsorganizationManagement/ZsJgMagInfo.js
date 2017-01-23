Ext.define('KitchenSink.view.zsorganizationManagement.ZsJgMagInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'zsbfjgMgInfo',
    controller: 'zsbfjgMgController',
    actType:'add',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'KitchenSink.view.zsorganizationManagement.ZsJgMagListController'
    ],
    title: Ext.tzGetResourse("TZ_ZSBF_JG_COM.TZ_ZSBF_INFO_STD.schoolLibInfo","证书颁发机构定义"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'zsbfjgInform',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 130,
            labelStyle: 'font-weight:bold'
        },

        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZSBF_JG_COM.TZ_ZSBF_INFO_STD.zhjgID","机构ID"),
            name: 'zhjgID',
            readOnly: true,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            cls:'lanage_1'

        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZSBF_JG_COM.TZ_ZSBF_INFO_STD.zhjgName","证书颁发机构名称"),
            name: 'zhjgName',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZSBF_JG_COM.TZ_ZSBF_INFO_STD.zhjgLinkedIn","LinkedIn编号"),
            name: 'zhjgLinkedIn',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }]

    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onschoolSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'ensureonschoolSave'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onSchoolClose'
    }]
});
