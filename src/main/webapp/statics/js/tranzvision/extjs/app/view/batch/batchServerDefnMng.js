Ext.define('KitchenSink.view.batch.batchServerDefnMng', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.batch.batchServerDefnModel',
        'KitchenSink.view.batch.batchServerDefnStore',
		'KitchenSink.view.batch.batchServerDefnMngController'
    ],
    xtype: 'batchServerDefnMng',
	controller: 'batchServerDefnMngController',
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
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveDeleteBatchServerDefnInfos"},
            {minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureDeleteBatchServerDefnInfos'},
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeBatchServerDefnMng'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchBatchServerDefnList"},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addBatchServerDefnInfo"},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editBatchServerDefnInfo"},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deleteBatchServerDefnInfos"}
		]
	}],
    initComponent: function () { 
	    //进程定义列表
	    var store = new KitchenSink.view.batch.batchServerDefnStore();
        Ext.apply(this, {
            columns: [{ 
                text: '所属机构',
                dataIndex: 'orgId',
				width: 180
            },{ 
                text: '进程服务器名称',
                dataIndex: 'batchServerName',
				width: 200
            },{
                text: '进程服务器描述',
                sortable: true,
                dataIndex: 'batchServerDesc',
                minWidth: 100,
				flex:1
            },{
            	text: '运行状态',
            	sortable: true,
            	dataIndex: 'yxzt',
            	width: 150,
            	renderer: function(v){
					if(v=='STOPPED'){
						return "已停止";
					}else if(v=='STOPPING'){
						return "停止中";
					}else if(v=='RUNNING'){
						return "运行中";
					}else if(v=='STARTING'){
						return "启动中";
					}else{
						return "";
					}	
				} 
            },{
               menuDisabled: true,
               sortable: false,
			   width:120,
               align:'center',
               xtype: 'actioncolumn',
			   items:[
			      {iconCls: 'save',tooltip: '启动',handler: 'startSelBatchServer'},
			      {iconCls: 'save',tooltip: '停止',handler: 'stopSelBatchServer'},
				  {iconCls: 'edit',tooltip: '编辑',handler: 'editSelBatchServerDefnInfo'},
			   	  {iconCls: 'remove',tooltip: '删除',handler: 'deleteBatchServerDefnInfo'}
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
