Ext.define('KitchenSink.view.security.plst.plstDef', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.security.plst.plstModel',
        'KitchenSink.view.security.plst.plstStore',
        'KitchenSink.view.security.plst.plstController'
    ],
    xtype: 'plstDef',
    controller: 'plstMg',
	/*store: {
        type: 'plstStore'
    },*/
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '许可权定义',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[
    {
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',
            {minWidth:80,text:"保存",iconCls:"save",handler:"savePlst"},
            {minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensurePlst'},
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closePlst'}]
		},
        {
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryPermission'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addPermission'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editPermission'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deletePermissions'}
		]
	}],
    initComponent: function () {
        var store = new KitchenSink.view.security.plst.plstStore();
        Ext.apply(this, {
            columns: [{ 
                text: '许可权编号',
                dataIndex: 'permID',
				width: 260
            },{
                text: '描述',
                sortable: true,
                dataIndex: 'permDesc',
                minWidth: 100,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
               align:'center',
			   width:60,
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editCurrPermission'},
			   	  {iconCls: 'remove',tooltip: '删除',handler:'deleteCurrPermission'}
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
