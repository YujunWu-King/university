Ext.define('KitchenSink.view.judgesManagement.judgesGroupMg.jugClGroupMg', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.judgesManagement.judgesGroupMg.jugGroupMgModel',
        'KitchenSink.view.judgesManagement.judgesGroupMg.jugClGroupMgStore',
        'KitchenSink.view.judgesManagement.judgesGroupMg.jugClGroupMgController'
    ],
    xtype: 'jugClMg',
    controller: 'jugClMg',
    reference: "jugClMgPanel",
    columnLines: true,
    selModel: {
        type: 'rowmodel'
    },
    style: "margin:8px",
    multiSelect: false,
    title: '材料评审评委组管理',
    viewConfig: {
        enableTextSelection: false
    },
    header: false,
    frame: true,
    dockedItems:[{xtype:"toolbar",dock:"bottom",ui:"footer",items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveList'},{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureList'},{minWidth:80,text:"关闭",iconCls:"close",handler:'closeList'}]},{
        xtype: "toolbar",
        items: [
            {text: "查询", tooltip: "查询数据", iconCls: "query", handler: 'searchDataList'}, "-",
            {text: "新增", tooltip: "新增数据", iconCls: "add", handler: 'addDataList'}, '->'
        ]
    }],
    initComponent: function () {
        //材料评审评委组
        var store = new KitchenSink.view.judgesManagement.judgesGroupMg.jugClGroupMgStore();
        Ext.apply(this, {
            store: store,
            columns: [{
                text: '材料评审组ID',
                sortable: true,
                dataIndex: 'jugGroupId',
                width: 305
            }, {
                text: '材料评审组名称',
                sortable: true,
                dataIndex: 'jugGroupName',
                width: 305,
                flex: 1
            }, {
                menuDisabled: true,
                sortable: false,
                width: 50,
                xtype: 'actioncolumn',
                items: [
                    {iconCls: 'edit', tooltip: '编辑', handler: 'editSelData'},
                    {iconCls: 'remove', tooltip: '删除', handler: 'deleteSelData'}
                ]
            }],
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                displayInfo: true,
                displayMsg: '显示{0}-{1}条，共{2}条',
                beforePageText: '第',
                afterPageText: '页/共{0}页',
                emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});
