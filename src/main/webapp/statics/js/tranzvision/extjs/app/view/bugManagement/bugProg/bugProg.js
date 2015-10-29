Ext.define('KitchenSink.view.bugManagement.bugProg.bugProg', {
    extend: 'Ext.window.Window',
    xtype: 'bugProg',
    reference: 'bugProg',
    controller: 'bugProgController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.ProgressBarWidget',
        'Ext.toolbar.Paging',
        'KitchenSink.view.bugManagement.bugProg.bugProgController',
        'KitchenSink.view.bugManagement.bugProg.bugProgStore'
    ],
    xtype: 'bugProg',
    width:700,
    minWidth: 300,
    controller: 'bugProgController',
    columnLines: true,
    title: '查看任务进度日志',
    y:10,
    layout: 'fit',
    resizable: true,
    modal: true,
    items: [{
        xtype: 'form',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,

        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 150,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'grid',
            bodyStyle:'overflow-y:auto;overflow-x:hidden;',
            minHeight: 200,
            maxHeight: 250,
            name: 'bugProgGrid',
            attrEnabledStore:'',
            columnLines: true,
            reference: 'bugProgGrid',
            store: {
                type: 'bugProgStore'
            },
            columns: [
                {
                    text:'编号',
                    width:80,
                    dataIndex:'bugID'
                },{
                    text:'责任人',
                    width:90,
                    dataIndex:'resOpr'
                },
                {
                    text:'预计结束日期',
                    width:120,
                    dataIndex:'estDate',
                    xtype: 'datecolumn',
                    format:'Y-m-d'
                },
                {
                    text: '进度百分比',
                    xtype: 'widgetcolumn',
                    width: 200,
                    dataIndex: 'bugPercent',
                    widget: {
                        xtype: 'progressbarwidget',
                        textTpl: [
                            '{percent:number("0")}% done'
                        ]
                    }
                },
                {
                    text: '进度更新时间',
                    width: 180,
                    dataIndex: 'bugUpdateDTTM'
/*                    ,
                    xtype: 'datecolumn',
                    format: 'Y-m-d H:i:s'*/
                }
            ]
        }]
    }],
    buttons: [ {
        text: '关闭',
        iconCls:"close",
        handler: 'onClose'
    }]
});
