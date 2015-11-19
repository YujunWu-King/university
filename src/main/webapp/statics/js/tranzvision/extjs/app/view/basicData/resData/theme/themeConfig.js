Ext.define('KitchenSink.view.basicData.resData.theme.themeConfig', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.resData.theme.themeController',
		'KitchenSink.view.basicData.resData.theme.themeModel',
        'KitchenSink.view.basicData.resData.theme.themeStore'
    ],
    xtype: 'themeSetMg',
    controller: 'themeSetMg',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '主题配置管理',
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
                {minWidth:80,text:"保存",iconCls:"save",handler:"saveThemeInfos"},
                {minWidth:80,text:'确定',iconCls:"ensure",handler: 'ensureThemeInfo'},
                {minWidth:80,text:'关闭',iconCls:"close",handler: 'closeThemeInfo'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryTheme'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addTheme'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls: "edit",handler:'editTheme'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteTheme'}
		]
	}],
    initComponent: function () {
        var themeStore = new KitchenSink.view.basicData.resData.theme.themeStore();
        Ext.apply(this, {
            store:themeStore,
            columns: [{ 
                text: '主题编号',
                dataIndex: 'themeID',
				width: 210
            },{
                text: '主题名称',
                sortable: true,
                dataIndex: 'themeName',
                width: 260
            },{
                text: '描述',
                sortable: true,
				dataIndex: 'themeDesc',
                //minWidth: 100,
				flex:1
            },{
                text: '有效状态',
                sortable: true,
                dataIndex: 'themeState',
                width: 100
            },{
               menuDisabled: true,
               sortable: false,
			   width:60,
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler: 'editThemeBL'},
			   	  {iconCls: 'remove',tooltip: '删除',handler: 'deleteThemeBL'}
			   ]
            }],
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store: themeStore,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
