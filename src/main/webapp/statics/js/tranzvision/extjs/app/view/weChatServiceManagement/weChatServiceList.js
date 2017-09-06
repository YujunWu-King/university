Ext.define('KitchenSink.view.weChatServiceManagement.weChatServiceList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.weChatServiceManagement.weChatServiceController',
		'KitchenSink.view.weChatServiceManagement.weChatServiceModel',
        'KitchenSink.view.weChatServiceManagement.weChatServiceStore'
    ],
   xtype: 'weChatService',
	controller: 'weChatServiceController',
	reference:"weChatList",
	store: {
        type: 'weChatServiceStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '微信服务号管理',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:"saveList"},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:"ensureList"},
            {minWidth:80,text:"关闭",iconCls:"close",handler:"closeList"}
        ]
		},{
		xtype:"toolbar",
		items:[
		    {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'query'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'add'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'edit'},"-",
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteMul'}
		]
	}],
    initComponent: function () {  
	   var store = new KitchenSink.view.weChatServiceManagement.weChatServiceStore();
        Ext.apply(this, {
            columns: [{ 
                text: '机构ID',
                dataIndex: 'orgId',
				width: 100,
				hidden: true
            },{
                text: '微信服务号名称',
                sortable: true,
                dataIndex: 'wxName',
                width: 320,
                flex:1
            },{
                text: '服务号ID',
                sortable: true,
				dataIndex: 'wxId',
                minWidth: 100,
				flex:1
            },{
                text: '服务号Secret',
                sortable: true,
				dataIndex: 'wxSecret',
                minWidth: 100,
				flex:1
            },{
                text: '指定参数',
                sortable: true,
				dataIndex: 'wxParam',
                minWidth: 100,
				flex:1
            },{
                text: '有效状态',
                sortable: true,
				dataIndex: 'wxState',
                minWidth: 80
            },{
                text: '添加人',
                sortable: true,
				dataIndex: 'wxAddOprid',
                minWidth: 80
            },{
				text: '添加时间',
				dataIndex: 'wxAddTime',
				width: 125
			},/*{
				text: '添加日期',
				dataIndex: 'wxAddDate',
				//hidden: true
			},*/{
               menuDisabled: true,
               sortable: false,
               align:'center',
			   width:60,
               xtype: 'actioncolumn',
			   items:[
                   {iconCls: 'edit',tooltip: '编辑',handler:'editSel'},
			   	   {iconCls: 'remove',tooltip: '删除',handler:'deleteSel'},
                   {iconCls: 'people',tooltip: '用户管理',handler:'userManage'},
                   {iconCls: 'refresh',tooltip: '全量获取用户',handler:'getUserAll'},
                   {iconCls: 'preview',tooltip: '查看发送日志',handler:'getLog'}
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
