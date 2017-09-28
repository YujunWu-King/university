Ext.define('KitchenSink.view.processDispatchMg.processMonitor', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.processDispatchMg.processMonitorController',
        'KitchenSink.view.processDispatchMg.processMonitorStore'
    ],
    controller: 'processMonitorCon',
    reference: 'jcMonitorGrid',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style: "margin:8px",
    multiSelect: true,
    title: '进程监视器',
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
        items:[
            '->',
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveProcessInstance"},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"ensureProcessInstance"},
            {minWidth:80,text:"关闭",iconCls:"close",handler:"onComRegClose"}
        ]
    }, {
        xtype: "toolbar",
        items: [
            {text: "查询", tooltip: "查询数据", iconCls: "query", handler: 'cfgSearchAct'}, "-",
            {text: "查看", tooltip: "查看", iconCls: "edit", handler: 'viewProcessMonitor'}, "-",
            {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteProcessInstance'}
        ]
    }],
    initComponent: function () {
    	
    	var statusStore = new KitchenSink.view.common.store.appTransStore("TZ_INSTANCE_STATUS");
        var store = new KitchenSink.view.processDispatchMg.processMonitorStore();
        Ext.apply(this, {
            columns: [{
                text: '进程实例',
                dataIndex: 'processInstance',
                flex: 1
            }, {
                text: '进程名称',
                sortable: true,
                dataIndex: 'processName',
                flex: 1
            }, {
                text: '计划运行时间',
                sortable: true,
                dataIndex: 'planExecuteTime',
                flex: 1
            }, {
                text: '服务器名称',
                sortable: true,
                dataIndex: 'processServerName',
                flex: 1
            }, {
                text: '用户',
                sortable: true,
                dataIndex: 'user',
                flex: 1
            }, {
                text: '运行状态',
                sortable: true,
                dataIndex: 'status',
                flex: 1
            }, {
                text: '计划运行时间',
                sortable: true,
                dataIndex: 'status',
                flex: 1
            }, {
                menuDisabled: true,
                sortable: false,
                flex: 1,
                xtype: 'actioncolumn',
                items: [
                    {iconCls: 'view', tooltip: '查看', handler: 'viewMonitor'},
                    {icon: 'statics/images/tranzvision/start.png', tooltip: '开启', handler: 'startInstanceBL'},
                    {icon: 'statics/images/tranzvision/stop.png', tooltip: '关闭', handler: 'stopInstanceBL'},
                    {iconCls: 'delete', tooltip: '删除', handler: 'deleteInstanceBL'}
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
