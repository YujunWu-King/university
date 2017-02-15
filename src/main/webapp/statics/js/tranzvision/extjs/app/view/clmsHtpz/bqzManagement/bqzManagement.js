Ext.define('KitchenSink.view.clmsHtpz.bqzManagement.bqzManagement', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.clmsHtpz.bqzManagement.bqzModel',
        'KitchenSink.view.clmsHtpz.bqzManagement.bqzStore',
		'KitchenSink.view.clmsHtpz.bqzManagement.bqzController'
    ],
    xtype: 'bqzManage',
	controller: 'bqzManage',
	/*store: {
        type: 'bqzStore'
    },*/
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '标签组管理',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:"savebqzManageInfos"},
            {minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensurebqzManageInfos'},
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closebqzManageInfos'}]
		},{
		xtype:"toolbar",
		items:[
			//{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchbqzList"},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addbqzManageInfo"},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editbqzManageInfo"},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deletebqzManageInfos"}
		]
	}],
	initComponent: function () { 
	    //标签组列表
	    var store = new KitchenSink.view.clmsHtpz.bqzManagement.bqzStore();
        Ext.apply(this, {
            columns: [{ 
                text: '标签组编号',
                dataIndex: 'bqzID',
				width: 240
            },{
                text: '标签组名称',
                sortable: true,
                dataIndex: 'bqzName',
                minWidth: 100,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   width:60,
               align:'center',
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler: 'editSelbqzManageInfo'},
			   	  {iconCls: 'remove',tooltip: '删除',handler: 'deletebqzManageInfo'}
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
