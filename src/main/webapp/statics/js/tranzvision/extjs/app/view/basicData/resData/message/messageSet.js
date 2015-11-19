Ext.define('KitchenSink.view.basicData.resData.message.messageSet', {
    extend: 'Ext.grid.Panel',
    xtype: 'msgSetMg',
    controller: 'msgController',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.resData.message.msgController',
		'KitchenSink.view.basicData.resData.message.messageModel',
        'KitchenSink.view.basicData.resData.message.messageStore'
    ],
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '消息集合信息列表',
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
            {minWidth:80,text:"保存",iconCls:"save",handler:'msgSave'},
            {minWidth:80,text:"确定",iconCls:"ensure",handler:'msgEnsure'},
            {minWidth:80,text:"关闭",iconCls:"close",handler:'msgClose'}
        ]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls:"query",handler:'msgQuery'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'msgAdd'},"-",
            {text:"编辑",tooltip:"编辑选中的消息集合",iconCls:"edit",handler:'msgEdit'},"-",
			{text:"删除",tooltip:"删除选中的消息集合",iconCls:"remove",handler:'msgRemove'}
		]
	}],
    initComponent: function () {
    	var store = new KitchenSink.view.basicData.resData.message.messageStore();
        Ext.apply(this, {
            columns: [{
                text: '消息集合编号',
                dataIndex: 'msgSetID',
				width: 340
            },{
                text: '消息集合名称',
				dataIndex: 'msgSetDesc',
                minWidth: 160,
				flex:1
            },{
                menuDisabled: true,
                sortable: false,
                width:60,
                align:'center',
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'edit',tooltip: '编辑',handler:'currMsgEdit'},
                    {iconCls: 'remove',tooltip: '删除',handler:'currMsgDelete'}
                ]
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 15,
				store:store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});
