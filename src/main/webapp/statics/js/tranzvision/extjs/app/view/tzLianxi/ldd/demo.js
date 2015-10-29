Ext.define('KitchenSink.view.tzLianxi.ldd.demo', {
    extend: 'Ext.panel.Panel',
    xtype: 'resourceSetInfo',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*'
    ],
    title: 'Demo',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        //heigth: 600,
        bodyStyle:'overflow-y:auto;overflow-x:hidden',

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 110,
            labelStyle: 'font-weight:bold'
        },

        items: [{
            xtype: 'textfield',
            fieldLabel: 'text_1111',
            maxLength: 30,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: 'text_2'
        }, {
            xtype: 'checkboxfield',
            boxLabel: '<span style="font-weight:bold;">是否启用</span>',
            margin: '0 0 0 115',
            inputValue: 'Y'
        }]
    }],
    buttons: [{
        text: '保存',
        iconCls:"save"
    }, {
        text: '确定',
        iconCls:"ensure"
    }, {
        text: '关闭',
        iconCls:"close"
    }]
});



