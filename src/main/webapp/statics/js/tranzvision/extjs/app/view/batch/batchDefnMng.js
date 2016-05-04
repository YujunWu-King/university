Ext.define('KitchenSink.view.batch.batchDefnMng', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.batch.batchDefnModel',
        'KitchenSink.view.batch.batchDefnStore',
		'KitchenSink.view.batch.batchDefnMngController'
    ],
    xtype: 'batchDefnMng',
	controller: 'batchDefnMngController',
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
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveDeleteBatchDefnInfos"},
            {minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureDeleteBatchDefnInfos'},
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeBatchDefnMng'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchBatchDefnList"},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addBatchDefnInfo"},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editBatchDefnInfo"},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deleteBatchDefnInfos"}
		]
	}],
    initComponent: function () { 
	    //进程定义列表
	    var store = new KitchenSink.view.batch.batchDefnStore();
        Ext.apply(this, {
            columns: [{ 
                text: '所属机构',
                dataIndex: 'orgId',
				width: 240
            },{ 
                text: '进程名称',
                dataIndex: 'batchName',
				width: 240
            },{
                text: '进程描述',
                sortable: true,
                dataIndex: 'batchDecs',
                minWidth: 100,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   width:60,
               align:'center',
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler: 'editSelBatchDefnInfo'},
			   	  {iconCls: 'remove',tooltip: '删除',handler: 'deleteBatchDefnInfo'}
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
