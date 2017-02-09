Ext.define('KitchenSink.view.qklZsmb.certType.certTypeList',{
	extend: 'Ext.grid.Panel',
	requires:[
		'Ext.data.*',
		'Ext.grid.*',
		'Ext.util.*',
		'Ext.toolbar.Paging',
		'Ext.ux.ProgressBarPager',
		'KitchenSink.view.qklZsmb.certType.certTypeController',
		'KitchenSink.view.qklZsmb.certType.certTypeStore'
	],
	xtype: 'certTypeList',
	columnLines: true,
	selModel: {
		type: 'checkboxmodel'
	},
	controller:'certTypeController',
	multiSelect: true,
	title: Ext.tzGetResourse("TZ_CERTTYPE_COM.TZ_TYPE_LIST_STD.zslxgl","证书类型管理") ,
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
			{minWidth:80,text:"保存",iconCls:"save",handler:'saveTypeList'},
			{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureTypeList'},
			{minWidth:80,text:"关闭",iconCls:"close",handler:'closeTypeList'}
]
},{
	xtype:"toolbar",
		items:[
		{text:"查询",tooltip:"查询数据",iconCls:"query",handler:'searchCertType'},"-",
		{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addCertType'},"-",
		{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'eidtCertType'}
	]
}],
initComponent: function () {
	var store = new KitchenSink.view.qklZsmb.certType.certTypeStore();
	Ext.apply(this, {
		columns:[{
			text: Ext.tzGetResourse("TZ_CERTTYPE_COM.TZ_TYPE_LIST_STD.JgId","机构编号"),
			dataIndex: 'JgId',
			width: 200,
			hidden:true
		},{
			text: Ext.tzGetResourse("TZ_CERTTYPE_COM.TZ_TYPE_LIST_STD.certTypeId","	证书类型编号"),
			dataIndex: 'certTypeId',
			width: 200
		},{
			text: Ext.tzGetResourse("TZ_CERTTYPE_COM.TZ_TYPE_LIST_STD.certName","证书名称"),
			dataIndex: 'certName',
			minWidth: 100,
			flex:1
		},{
			menuDisabled: true,
			sortable: false,
			width:60,
			align:'center',
			xtype: 'actioncolumn',
			items:[
				{iconCls: 'edit',tooltip: '编辑',handler:'editCurrType'},
				{iconCls: 'remove',tooltip: '删除',handler:'deleteCurrType'}
			]
		}],
		store: store,
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 50,
			store: store,
			plugins: new Ext.ux.ProgressBarPager()
		}
	});
	this.callParent();
}
});
