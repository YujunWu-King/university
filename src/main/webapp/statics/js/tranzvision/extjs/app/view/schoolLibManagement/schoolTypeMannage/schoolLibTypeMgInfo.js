Ext.define('KitchenSink.view.schoolLibManagement.schoolTypeMannage.schoolLibTypeMgInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'schoolTypeMgInfo',
    controller: 'schoolMgTypeConter',
    actType:'add',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'KitchenSink.view.schoolLibManagement.schoolTypeMannage.schoolLibTypeMgController',
        'KitchenSink.view.common.store.appTransStore'
    ],
    title: Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_INFO_STD.schoolLibInfo","院校库详情"),
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'schoolTypeInform',
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
            fieldLabel: Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_INFO_STD.typeID","编号"),
            name: 'typeID',
            readOnly: true,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            cls:'lanage_1'

        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_INFO_STD.typeName","院校类型名称"),
            name: 'typeName',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_INFO_STD.typedec","备注"),
            name: 'typedec',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        },
            {
                xtype: 'combobox',
                fieldLabel:Ext.tzGetResourse("TZ_SCH_TYPE_COM.TZ_SCTYE_INFO_STD.typeFlag","状态"),
                forceSelection: true,
                allowBlank: false,
                valueField: 'TValue',
                displayField: 'TSDesc',
                store: new KitchenSink.view.common.store.appTransStore("TZ_SCHOOL_FLAG"),
                typeAhead: true,
                queryMode: 'local',
                name: 'typeFlag'

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
