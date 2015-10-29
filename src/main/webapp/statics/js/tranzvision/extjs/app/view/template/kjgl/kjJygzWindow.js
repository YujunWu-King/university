Ext.define('KitchenSink.view.template.kjgl.kjJygzWindow', {
    extend: 'Ext.window.Window',
    xtype: 'kjJygzWindow',
    controller: 'kjJygzController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.kjgl.kjJygzController'
    ],
    title: '控件校验规则信息',
    reference:'kjJygzWindow',
    width:500,
    actType: 'add',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        items: [
            {
                xtype: 'textfield',
                fieldLabel: '控件编号',
                hidden:true,
                name: 'kjID',
                allowBlank:false,
                ignoreChangesFlag: true,
                blankText: '控件编号必填'
            },
            {
                xtype: 'textfield',
                fieldLabel: '控件名称',
                name: 'kjName',
                allowBlank:false,
                editable:false,
                disabled:true,
                ignoreChangesFlag: true,
                blankText: '控件名称必填'
            },
            {
                xtype: 'textfield',
                fieldLabel: '序号',
                name: 'tz_order',
                hidden:true,
                ignoreChangesFlag: true,
                blankText: '序号'
            },
            {
                layout: {
                    type: 'column'
                },
                items:[{
                    columnWidth:.6,
                    xtype: 'textfield',
                    fieldLabel: "校验规则ID",
                    name: 'kjJygzID',
                    ignoreChangesFlag: true,
                    editable: false	,
                    triggers: {
                        search: {
                            cls: 'x-form-search-trigger',
                            handler: "pmtSearchJygz"
                        }
                    }
                },{
                    columnWidth:.4,
                    xtype: 'displayfield',
                    hideLabel: true,
                    style:'margin-left:5px',
                    name: 'kjJygzMx',
                    ignoreChangesFlag: true
                }]
            },
            {
                xtype: 'combobox',
                fieldLabel: '是否启用',
                editable:false,
                emptyText:'请选择',
                queryMode: 'remote',
                name: 'isQy',
                valueField: 'TValue',
                displayField: 'TSDesc',
                store: new KitchenSink.view.common.store.appTransStore("TZ_QY_BZ"),
                allowBlank:false,
                ignoreChangesFlag: true,
                value:'Y'
            }
        ]
    }],
    buttons: [
        {
            text: '保存',
            iconCls:"save",
            handler: "doSaveKjJygz"
        },
        {
            text: '确定',
            iconCls:"ensure",
            handler: "sureKjJygz"
        },
        {
            text: '关闭',
            iconCls:"close",
            handler: "closeKjJygz"
        }
    ]
});
