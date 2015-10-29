Ext.define('KitchenSink.view.siteManage.siteManage.menu.menuManges.menuTypeManagement', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.siteManage.siteManage.menu.menuManges.menuController',//调用控制器
		'KitchenSink.view.siteManage.siteManage.menu.menuManges.menuTypeModel',//json格式
        'KitchenSink.view.siteManage.siteManage.menu.menuManges.menuTypeStore'//json路径
    ],
    xtype: 'menuTypeManagement',
	controller: 'menuTypeInfo1',
	siteId:'',
	store: {
        type: 'menuTypeStoreI'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '菜单类型管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{xtype:"toolbar",dock:"bottom",ui:"footer",items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:"saveMenuInfos"}]},{
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
                hidden: true
            },{
                text: '菜单类型编号',
                dataIndex: 'menutypeid',
				width: 110
            },{
                text: '菜单类型名称',
                sortable: true,
                dataIndex: 'menutypename',
                minWidth: 140,
                flex: 1
            },{
                text: '状态',
                sortable: true,
                dataIndex: 'menutypestate',
                width: 65
            },{
	            xtype: 'checkcolumn',
				text: '是否允许添加',
				disabled:true,
	            dataIndex: 'menuisadd',
	            width: 115,
	            editor: {
	                xtype: 'checkbox',
	                cls: 'x-grid-checkheader-editor'
	            }
	        },{
                text: '业务人员设置菜单处理代码',
                sortable: true,
                dataIndex: 'menusetcode',
                width: 210
            },{
                text: '考生点击菜单处理代码',
                sortable: true,
                dataIndex: 'menushowcode',
                width: 180
            },{
               text: '操作',
               menuDisabled: true,
               sortable: false,
               width:60,
			   xtype: 'actioncolumn',
			   items:[{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteMenu'},'-',
					  {iconCls: 'remove',tooltip: '删除',handler: 'deleteSiteMenu'}
			   ]
            }],
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				/*
				store: {
					type: 'menuTypeStore'
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
