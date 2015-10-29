Ext.define('KitchenSink.view.website.set.siteSet', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.website.set.siteSetModel',
        'KitchenSink.view.website.set.siteSetStore'
    ],
    xtype: 'websitSet',
	store: {
        type: 'siteSetStore'
    },
    columnLines: true,
   /* selModel: {
        type: 'checkboxmodel'
    },
	*/
	style:"margin:8px",
    multiSelect: true,
    title: '消息集合管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true
    /*dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"task-folder"}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls:"query"},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add"},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls: "edit"},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove"}
		]
	}],
	
    initComponent: function () {    
        Ext.apply(this, {
            columns: [{ 
                text: '转换值集合编号',
                dataIndex: 'transSetID',
				width: 180
            },{
                text: '描述',
				dataIndex: 'transSetDesc',
                minWidth: 160,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   width:40,
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑'},
			   	  {iconCls: 'remove',tooltip: '删除'}
			   ]
            }],
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store: {
					type: 'siteSetStore'
				},
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
	*/
});
