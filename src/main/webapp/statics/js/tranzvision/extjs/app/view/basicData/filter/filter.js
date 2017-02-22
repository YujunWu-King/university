Ext.define('KitchenSink.view.basicData.filter.filter', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.filter.filterController',
        'KitchenSink.view.basicData.filter.filterStore'
    ],
    xtype: 'filter',
    controller: 'filter',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '可配置搜索管理',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveFilterInfos"},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"ensureFilterInfos"},
            {minWidth:80,text:"关闭",iconCls:"close",handler:"closeFilterInfos"}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryFilter'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addFilter'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editFilter'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteFilter'}
			,"-",{text:"测试邮件发送",tooltip:"测试邮件发送",iconCls:"email",handler:"sendEmail"}
			/*,"-",{text:"图片上传",tooltip:"图片上传处理",iconCls:"add",handler:"picClipping"}
			,"-",
			{text:"导入Excel",tooltip:"导入Excel",iconCls:"import",handler:"importExcel"}*/,"-",
			{text:"测试短信发送",tooltip:"测试短信发送",iconCls:"sms",handler:"sendSms"}
		]
	}],
    initComponent: function () {    
    	var store = new KitchenSink.view.basicData.filter.filterStore();
        Ext.apply(this, {
            columns: [{ 
                text: '组件编号',
                dataIndex: 'ComID',
								minWidth: 100,
								flex: 1
            },{
                text: '组件名称',
                sortable: true,
                dataIndex: 'comMc',
                minWidth: 100,
                flex: 1
            },{ 
                text: '页面编号',
                dataIndex: 'PageID',
								minWidth: 100,
								flex: 1
            },{
                text: '页面名称',
                sortable: true,
                dataIndex: 'pageMc',
                minWidth: 100,
                flex: 1
            },{
                text: '视图名称',
                sortable: true,
								dataIndex: 'ViewMc',
                minWidth: 100,
								flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   			 width:50,
               xtype: 'actioncolumn',
			   			 items:[
				  				{iconCls: 'edit',tooltip: '编辑',handler: 'editFilterBL'},
				  				// '-',
			   	  			{iconCls: 'remove',tooltip: '删除',handler: 'deleteFilterBL'}
			   				]
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                /*
				store: {
					type: 'filterStore'
				},*/
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
