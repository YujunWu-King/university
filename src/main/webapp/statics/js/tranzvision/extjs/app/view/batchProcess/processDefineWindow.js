Ext.define('KitchenSink.view.batchProcess.processDefineWindow', {
    extend: 'Ext.window.Window',
    xtype: 'processDefineWindow',
    controller: 'processDefineCon',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.batchProcess.processDefineController'
    ],
    title: '新增进程定义',
    reference: 'processDefineWindow',
    width: 700,
    height: 350,
    minWidth: 200,
    minHeight: 100,
    layout: 'fit',
    resizable: true,
    modal: true,
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
            labelWidth: 120,
            labelStyle: 'font-weight:bold'
        },
        items: [
            {
                xtype: 'combobox',
                editable:false,
                fieldLabel: '归属机构',
                forceSelection: true,
                valueField: 'orgId',
                displayField: 'orgName',
                store: new KitchenSink.view.orgmgmt.orgListStore(),
                queryMode: 'local',
                name: 'orgId',
                emptyText:'请选择机构',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'textfield',
                fieldLabel: '进程名称',
                name: 'processName',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'textfield',
                fieldLabel: '进程描述',
                name: 'processDesc',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'combobox',
                editable:false,
                fieldLabel: '运行平台类型',
                forceSelection: true,
                valueField: 'TValue',
                displayField: 'TSDesc',
                store: new KitchenSink.view.common.store.appTransStore("TZ_PLAT_TYPE"),
                queryMode: 'remote',
                name: 'runPlatType',
                emptyText:'Windows/Unix/其他',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                bodyStyle:'padding:0 0 10px 0',
                xtype: 'textfield',
                fieldLabel: '注册到组件',
                name: 'ComID',
                editable: false,
                triggers: {
                    search: {
                        cls: 'x-form-search-trigger',
                        handler: "pmtSearchComIDTmp"
                    }
                },
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            }, {
                xtype: 'textfield',
                fieldLabel: 'JavaClass类路径',
                name: 'className',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'textarea',
                fieldLabel: '备注信息',
                name: 'remark'
            }
        ]
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onProcessWinSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onProcessWinEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: function(btn){
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            win.close();
        }
    }]
});
