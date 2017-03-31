Ext.define('KitchenSink.view.autoScoringRulerManagement.autoScoringRulerInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'autoScorMgInfo',
    controller: 'autScoRMgController',
    actType:'add',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'KitchenSink.view.autoScoringRulerManagement.autoScoringRulerListController'
    ],
    
    title: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_INFO_STD.schoolLibInfo","自动打分规则管理"),
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
            fieldLabel: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_INFO_STD.zhjgID","打分规则ID"),
            name: 'zhjgID',
            readOnly: true,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            cls:'lanage_1'

        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_INFO_STD.zhjgName","打分规则名称"),
            name: 'zhjgName',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_INFO_STD.zhjgLinkedIn","打分规则"),
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
