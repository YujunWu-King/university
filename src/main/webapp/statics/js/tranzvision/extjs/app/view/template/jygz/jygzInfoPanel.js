Ext.define('KitchenSink.view.template.jygz.jygzInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'jygzInfo',
    controller: 'jygz',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
    ],
    actType: 'add',//默认新增
    initComponent:function(){
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'jygzInfoForm',
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
                    fieldLabel: '校验规则编号',
                    name: 'jygzID',
                    allowBlank:false,
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }, {
                    xtype: 'textfield',
                    fieldLabel: '校验规则名称',
                    allowBlank:false,
                    name: 'jygzName',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }, {
                    xtype: 'textfield',
                    fieldLabel: '类名称',
                    allowBlank:false,
                    name: 'jygzClssName',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                } , {
                    xtype: 'textfield',
                    fieldLabel: '服务端校验',
                    allowBlank:false,
                    name: 'jygzFwdJy',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }, {
                    xtype: 'textfield',
                    fieldLabel: '提示信息',
                    allowBlank:false,
                    name: 'jygzTsxx',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },{
                    xtype: 'textfield',
                    fieldLabel: '英文提示信息',
                    allowBlank:false,
                    name: 'jygzEnTsxx',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                },
                    {
                    xtype: 'combo',
                    fieldLabel: '状态',
                    allowBlank:false,
                    queryMode:"local",
                    editable:false,
                    store:{
                        fields:[
                            {name:'statusCode'},
                            {name:'statusDesc'}
                        ],
                        data:[
                            {statusCode:'Y',statusDesc:'生效'},
                            {statusCode:'N',statusDesc:'失效'}
                        ]
                    },
                    valueField:'statusCode',
                    displayField:'statusDesc',
                    allowBlank:false,
                    value:'Y',/*默认有效*/
                    name: 'jygzState',
                    afterLabelTextTpl: [
                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                    ]
                }
                ]
            }
            ]
        })
        this.callParent();
    },
    title: '校验规则信息',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onJygzInfoSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onJygzInfoEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onJygzInfoClose'
    }]
});