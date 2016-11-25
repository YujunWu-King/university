Ext.define('KitchenSink.view.siteManage.outsiteManage.outsiteManage', {//站点管理列表页面
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.siteManage.outsiteManage.outsiteController',//调用控制器
		'KitchenSink.view.siteManage.outsiteManage.outsiteModel',//json格式
        'KitchenSink.view.siteManage.outsiteManage.outsiteStore'//json路径
    ],
    xtype: 'outsiteManage',//不能变
	controller: 'outsiteBasicB',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '外部站点管理',
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
            {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeComRegInfos'}]
	},{
		xtype:"toolbar",
		items:[
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addOutSite'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls: 'edit',handler:'editSelectField'}
		]
	}],
    initComponent: function () {   
		var store = new KitchenSink.view.siteManage.outsiteManage.outsiteStore();
        Ext.apply(this, {
            columns: [{ 
                text: '站点编号',
                dataIndex: 'siteId',
                hidden: true,
				width: 100
            },{ 
                text: '站点名称',
                dataIndex: 'sitetemplateName',
				width: 400
            },{
                text: '站点说明',
                sortable: true,
                dataIndex: 'explanation',
                minWidth: 400,
                flex: 1
            },{
               menuDisabled: true,
               sortable: false,
               width:60,
			   xtype: 'actioncolumn',
			   items:[{iconCls: 'edit',tooltip: '编辑',handler:'editField'},
					  {iconCls: 'copy',tooltip: '栏目管理',handler:'editSiteColuById'},
					  {iconCls: 'set',tooltip: '模板管理',handler:'editSiteTempById'},
					  {iconCls: 'preview',tooltip: '内容管理',handler:'editContent'},
					  {iconCls: 'publish',tooltip: '菜单配置',handler:'editSiteMenuById'}
			   ]
            }],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store: store,
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
