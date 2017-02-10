Ext.define('KitchenSink.view.qklZsmb.zsmbGlList',{
	extend: 'Ext.grid.Panel',
	requires:[
		'Ext.data.*',
		'Ext.grid.*',
		'Ext.util.*',
		'Ext.toolbar.Paging',
		'Ext.ux.ProgressBarPager',
		'KitchenSink.view.qklZsmb.zsmbListStore',
		'KitchenSink.view.qklZsmb.zsmbController'
	],
	xtype: 'zsmbGlList',
	columnLines: true,
	selModel: {
		type: 'checkboxmodel'
	},
	controller:'zsmbController',
	style:"margin:8px",
	multiSelect: true,
	title: Ext.tzGetResourse("TZ_ZHENGSHU_COM.TZ_MOBAN_LIST_STD.zsmbGl","证书模板管理") ,
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
			{minWidth:80,text:"保存",iconCls:"save",handler:'saveMbList'},
			{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureMbList'},
{minWidth:80,text:"关闭",iconCls:"close",handler:'closeMbList'}
]
},{
	xtype:"toolbar",
		items:[
		{text:"查询",tooltip:"查询数据",iconCls:"query",handler:'searchCertTmpl'},"-",
		{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addCertTmpl'},"-",
		{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'eidtCertTmpl'},"-",
		{text:"删除",tooltip:"删除数据",iconCls:"delete",handler:'deleteCertTmpl'}
	]
}],
initComponent: function () {
	var store = new KitchenSink.view.qklZsmb.zsmbListStore();
	Ext.apply(this, {
		columns:[{
			text: Ext.tzGetResourse("TZ_ZHENGSHU_COM.TZ_MOBAN_LIST_STD.JgId","机构编号"),
			dataIndex: 'JgId',
			width: 200,
			hidden:true
		},{
			text: Ext.tzGetResourse("TZ_ZHENGSHU_COM.TZ_MOBAN_LIST_STD.certTmpl","证书模板编号"),
			dataIndex: 'certTmpl',
			width: 200
		},{
			text: Ext.tzGetResourse("TZ_ZHENGSHU_COM.TZ_MOBAN_LIST_STD.certType","证书类型"),
			dataIndex: 'certType',
			minWidth: 200,
			flex:1
		},{
			text:  Ext.tzGetResourse("TZ_ZHENGSHU_COM.TZ_MOBAN_LIST_STD.certJGID","证书颁发机构"),
			dataIndex: 'certJGID',
			minWidth: 200,
			flex:1
		},{
			text:  Ext.tzGetResourse("TZ_ZHENGSHU_COM.TZ_MOBAN_LIST_STD.useFlag","启用状态"),
			dataIndex: 'useFlag',
			width: 100,
			renderer : function(value, metadata, record) {
				if (value=="Y"){
					return "启用";
				}else if(value=="N"){
					return "不启用";
				}
			}
		},{
			menuDisabled: true,
			sortable: false,
			width:60,
			align:'center',
			xtype: 'actioncolumn',
			items:[
				{iconCls: 'edit',tooltip: '编辑',handler:'editCurrTmpl'},
				{iconCls: 'remove',tooltip: '删除',handler:'deleteCurrTmpl'}
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
