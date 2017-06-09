Ext.define('KitchenSink.view.processDispatchMg.processDispatchMgList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.processDispatchMg.processDispatchController',
        'KitchenSink.view.processServer.processServerStore',
        'KitchenSink.view.processDispatchMg.processDispatchStore'
    ],
    xtype: 'processDispatchCon',
    controller: 'processDispatchCon',
    reference: 'processDispatchGrid',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style: "margin:8px",
    multiSelect: true,
    title: '进程调度管理',
    viewConfig: {
        enableTextSelection: true
    },
    plugins: {
        ptype: 'cellediting',
        pluginId: 'artHdListCellEditing'
    },
    header: false,
    frame: true,
    dockedItems: [{
        xtype: "toolbar",
        dock: "bottom",
        ui: "footer",
        items: [
            '->', {minWidth: 80, text: "关闭", iconCls: "close", handler: "onComRegClose"}
        ]
    }, {
        xtype: "toolbar",
        items: [
            {text: "查询", tooltip: "查询数据", iconCls: "query", handler: 'cfgSearchAct'}, "-",
            {text: "打开进程监视器", tooltip: "打开进程监视器", iconCls: "", handler: 'openProcessMonitor'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.processDispatchMg.processDispatchStore();

        Ext.apply(this, {
            columns: [{
                text: '所属机构',
                dataIndex: 'orgId',
                flex: 1
            },{
                text: '进程名称',
                dataIndex: 'processName',
                flex: 1
            }, {
                text: '进程描述',
                sortable: true,
                dataIndex: 'processDesc',
                flex: 1
            }, {
                menuDisabled: true,
                sortable: false,
                flex: 1,
                xtype: 'actioncolumn',
                items: [
                    {icon: 'statics/images/tranzvision/start.png', tooltip: '打开进程调度', handler: 'openProcessBL'}
                ]
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });

        this.callParent();
    }
});
