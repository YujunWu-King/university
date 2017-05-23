Ext.define('KitchenSink.view.processServer.processServerWindow', {
    extend: 'Ext.window.Window',
    xtype: 'processServerWindow',
    controller: 'processServerCon',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.processServer.processServerController'
    ],
    title: '进程服务器定义',
    reference: 'processServerWindow',
    width: 700,
    height: 400,
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
            labelWidth: 150,
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
                fieldLabel: '进程服务器名称',
                name: 'processName',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'textfield',
                fieldLabel: '服务器IP地址',
                name: 'serverIP',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'textfield',
                fieldLabel: '进程服务器描述',
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
                xtype: 'textfield',
                fieldLabel: '任务循环读取间隔时间',
                name: 'intervalTime',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            }, {
                xtype: 'textfield',
                fieldLabel: '最大并行任务数',
                name: 'parallelNum',
                afterLabelTextTpl: [
                    '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                ],
                allowBlank:false
            },{
                xtype: 'textarea',
                fieldLabel: '备注信息',
                name: 'remark'
            }

        ],
    }],
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onProcessServerWinSave'
    }, {
        text: '确定',
        iconCls: "ensure",
        handler: 'onProcessServerWinEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: function (btn) {
            var win = btn.findParentByType("window");
            var form = win.child("form").getForm();
            win.close();
        }
    }]
});
