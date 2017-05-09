Ext.define('KitchenSink.view.dispatchLoop.dispatchLoopList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.orgmgmt.orgListStore',
        'KitchenSink.view.dispatchLoop.dispatchLoopController',
        'KitchenSink.view.dispatchLoop.dispatchLoopStore'
    ],
    xtype: 'dispatchLoopCon',
    controller: 'dispatchLoopCon',
    reference: 'dispatchLoopGrid',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '调度循环管理',
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
            '->',{minWidth:80,text:"保存",iconCls:"save",handler:"saveDispatchLoop"},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"ensureDispatchLoop"},
            {minWidth:80,text:"关闭",iconCls:"close",handler:"onDispatchLoopClose"}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'cfgSearchAct'},"-",
            {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addDispatchLoop'},"-",
            {text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editDispatchLoop'},"-",
            {text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteDispatchLoop'}
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.dispatchLoop.dispatchLoopStore();

        Ext.apply(this, {
            columns: [{
                text: '循环名称',
                dataIndex: 'loopName',
                flex: 1
            },{
                text: '循环描述',
                sortable: true,
                dataIndex: 'loopDesc',
                flex: 1
            },{
                menuDisabled: true,
                sortable: false,
                width:50,
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',handler: 'editDispatchLoopBL'},
                    {iconCls: 'remove',tooltip: '删除',handler: 'deleteDispatchLoopBL'}
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
