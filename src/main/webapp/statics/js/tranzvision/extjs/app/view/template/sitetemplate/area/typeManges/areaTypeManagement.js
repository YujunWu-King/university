Ext.define('KitchenSink.view.template.sitetemplate.area.typeManges.areaTypeManagement', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.sitetemplate.area.typeManges.areaController',//调用控制器
		'KitchenSink.view.template.sitetemplate.area.typeManges.areaTypeModel',//json格式
        'KitchenSink.view.template.sitetemplate.area.typeManges.areaTypeStore'//json路径
    ],
    xtype: 'areaTypeManagement',
	controller: 'areaTypeInfoTemplate',
	siteId:'',
	store: {
        type: 'areaTypeStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '区域类型管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{xtype:"toolbar",dock:"bottom",ui:"footer",items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:"saveAreaInfos"}]},{
		xtype:"toolbar",
		items:[
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addType'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls: 'edit',handler:'editType'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteTyle'}
		]
	}],
    initComponent: function () {    
        Ext.apply(this, {
            columns: [{ 
                text: '站点模板编号',
                dataIndex: 'siteId',
                hidden: true,
				width: 100
            },{
                text: '区域类型编号',
                dataIndex: 'areatypeid',
				width: 120
            },{
                text: '区域类型名称',
                sortable: true,
                dataIndex: 'areatypename',
                minWidth: 150,
                flex: 1
            },{
                text: '设置区域时的处理程序',
                sortable: true,
                dataIndex: 'areasetcode',
                width: 250
            },{
                text: '区域代码展示的处理程序',
                sortable: true,
                dataIndex: 'areahtmlcode',
                width: 250
            },{
               menuDisabled: true,
               sortable: false,
               width:60,
			   xtype: 'actioncolumn',
			   items:[{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteArea'},'-',
					  {iconCls: 'remove',tooltip: '删除',handler: 'deleteSiteArea'}
			   ]
            }],
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                /*
				store: {
					type: 'areaTypeStore'
				},*/
				listeners:{
					afterrender: function(pbar){
						var grid = pbar.findParentByType("grid");
						pbar.setStore(grid.store);
					}
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
});
