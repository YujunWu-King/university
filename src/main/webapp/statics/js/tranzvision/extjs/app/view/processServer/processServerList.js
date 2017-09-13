Ext.define('KitchenSink.view.processServer.processServerList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.processServer.processServerController',
        'KitchenSink.view.orgmgmt.orgListStore',
        'KitchenSink.view.processServer.processServerStore'
    ],
    xtype: 'processServerCon',
    controller: 'processServerCon',
    reference: 'processServerGrid',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '进程服务器列表',
    viewConfig: {
        enableTextSelection: true
    },
    plugins: {
        ptype: 'cellediting',
        pluginId: 'artHdListCellEditing'
    },
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:[
            '->',
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveProcessServer"},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"ensureProcessServer"},
            {minWidth:80,text:"关闭",iconCls:"close",handler:"onComRegClose"}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'cfgSearchAct'},"-",
            {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addProcessServer'},"-",
            {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editProcessServer'},"-",
            {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteProcessServer'}
        ]
    }],
    initComponent: function () {
    	
    	var statusStore = new KitchenSink.view.common.store.appTransStore("TZ_PROCESS_STATUS");
        var store = new KitchenSink.view.processServer.processServerStore();
        Ext.apply(this, {
            columns: [{
                text: '进程服务器名称',
                dataIndex: 'processName',
                flex: 1
            },{
                text: '进程服务器描述',
                sortable: true,
                dataIndex: 'processDesc',
                flex: 1
            },{
                text: '运行状态',
                sortable: true,
                dataIndex: 'runningStatus',
                flex: 1
            },{
                menuDisabled: true,
                sortable: false,
                width:50,
                xtype: 'actioncolumn',
                items:[
                    {icon:'statics/images/tranzvision/start.png',tooltip: '开始',handler: 'startProcessServerBL'},
                    {icon:'statics/images/tranzvision/stop.png',tooltip: '停止',handler: 'stopProcessServerBL'},
                    {iconCls: 'edit',tooltip: '编辑',handler: 'editProcessServerBL'},
                    {iconCls: 'remove',tooltip: '删除',handler: 'deleteProcessServerBL'}
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
