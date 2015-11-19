Ext.define('KitchenSink.view.basicData.resData.hardCode.hardCodeConfig', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.basicData.resData.hardCode.hardCodeController',
		'KitchenSink.view.basicData.resData.hardCode.hardCodeModel',
        'KitchenSink.view.basicData.resData.hardCode.hardCodeStore'
    ],
   xtype: 'hardCode',
	controller: 'hardCode',
	store: {
        type: 'hardCodeStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: 'hardCode管理',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveHardCodeInfos"},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"ensureHardCodeInfos"},
            {minWidth:80,text:"关闭",iconCls:"close",handler:"closeHardCodeInfos"}
        ]
		},{
		xtype:"toolbar",
		items:[
		    {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryHardCode'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addHardCode'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editHardCode'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteHardCode'}
		]
	}],
    initComponent: function () {  
	   var store = new KitchenSink.view.basicData.resData.hardCode.hardCodeStore();
        Ext.apply(this, {
            columns: [{ 
                text: 'hardCode点名称',
                dataIndex: 'hardCodeName',
				width: 230
            },{
                text: '描述',
                sortable: true,
                dataIndex: 'hardCodeDesc',
                width: 320,
                flex:1
            },{
                text: '取值',
                sortable: true,
				dataIndex: 'hardCodeValue',
                minWidth: 100,
				flex:1
            },{
               menuDisabled: true,
               sortable: false,
               align:'center',
			   width:60,
               xtype: 'actioncolumn',
			   items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editSelHardCode'},
			   	  {iconCls: 'remove',tooltip: '删除',handler:'deleteSelHardCode'}
			   ]
            }],
			store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				/*store: {
					type: 'hardCodeStore'
				},*/
				store:store,
                displayInfo: true,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
