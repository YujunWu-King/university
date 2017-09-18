Ext.define('KitchenSink.view.tranzApp.tranzAppMng', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.tranzApp.tranzAppController',
		'KitchenSink.view.tranzApp.tranzAppModel',
        'KitchenSink.view.tranzApp.tranzAppStore'
    ],
    xtype: 'tranzAppMng',
	controller: 'tranzAppController',
	store: {
        type: 'tranzAppStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '应用分配管理',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveTanzAppInfos"},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"ensureTanzAppInfos"},
            {minWidth:80,text:"关闭",iconCls:"close",handler:"closeTanzAppInfos"}
        ]
		},{
		xtype:"toolbar",
		items:[
		    {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryTanzApp'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addTranzApp'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editTranzApp'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteTranzApp'}
		]
	}],
    initComponent: function () {  
	   var store = new KitchenSink.view.tranzApp.tranzAppStore();
        Ext.apply(this, {
            columns: [{ 
                text: '机构id',
                dataIndex: 'jgId',
                hidden: true
            },{
                text: '分配应用名称',
                sortable: true,
                dataIndex: 'appName',
                width: 150,
            },{
                text: '分配应用Appid',
                sortable: true,
				dataIndex: 'appId',
				width: 250
            },{
                text: '分配应用appSecret',
                sortable: true,
				dataIndex: 'appSecret',
				width: 300
            },{
                text: '描述',
                sortable: true,
				dataIndex: 'appDesc',
                minWidth: 100,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
               align:'center',
			   width:60,
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editSelTranzApp'},
			   	  {iconCls: 'remove',tooltip: '删除',handler:'deleteSelTranzApp'}
			   ]
            }],
			store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store:store,
                displayInfo: true,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
