Ext.define('KitchenSink.view.basicData.resData.resource.resourceSet', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.basicData.resData.resource.resourceSetModel',
        'KitchenSink.view.basicData.resData.resource.resourceSetStore',
        'KitchenSink.view.basicData.resData.resource.resourceSetController'
    ],
    xtype: 'resSetMg',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    controller: 'resSet',
	style:"margin:8px",
    multiSelect: true,
    title: '资源集合管理',
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
			{minWidth:80,text:"保存",iconCls:"save",handler:'saveResSets'},
			{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureResSets'},
			{minWidth:80,text:"关闭",iconCls:"close",handler:'closeResSets'}
		]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls:"query",handler:'queryResSet'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addResSet'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls: "edit",handler:'editResSet'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteResSets'}
		]
	}],
    initComponent: function () {
        var store = new KitchenSink.view.basicData.resData.resource.resourceSetStore();
        Ext.apply(this, {
            columns: [{ 
                text: '资源集合编号',
                dataIndex: 'resSetID',
				width: 300
            },{
                text: '资源集合名称',
				dataIndex: 'resSetDesc',
                minWidth: 160,
				flex:1
            },{
                menuDisabled: true,
                sortable: false,
                width:60,
                align:'center',
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',handler:'editCurrResSet'},
                    {iconCls: 'remove',tooltip: '删除',handler:'deleteCurrResSet'}
                ]
            }
            ],
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
