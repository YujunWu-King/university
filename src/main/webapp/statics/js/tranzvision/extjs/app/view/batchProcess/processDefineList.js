Ext.define('KitchenSink.view.batchProcess.processDefineList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.batchProcess.processDefineController',
        'KitchenSink.view.orgmgmt.orgListStore',
        'KitchenSink.view.batchProcess.processDefineStore'
    ],
    xtype: 'processDefineCon',
    controller: 'processDefineCon',
    reference: 'processDefineGrid',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '进程定义列表',
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
        items:['->',
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveProcess"},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"ensureProcess"},
            {minWidth:80,text:"关闭",iconCls:"close",handler:"onComRegClose"}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'cfgSearchAct'},"-",
            {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addProcess'},"-",
            {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editProcess'},"-",
            {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteProcess'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.batchProcess.processDefineStore();

        Ext.apply(this, {
            columns: [{
                text: '进程名称',
                dataIndex: 'processName',
                flex: 1
            },{
                text: '进程描述',
                sortable: true,
                dataIndex: 'processDesc',
                flex: 1
            },{
                menuDisabled: true,
                sortable: false,
                minWidth:100,
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',handler: 'editProcessBL'},
                    {iconCls: 'remove',tooltip: '删除',handler: 'deleteProcessBL'}
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
