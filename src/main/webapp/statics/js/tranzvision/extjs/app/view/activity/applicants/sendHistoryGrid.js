Ext.define('KitchenSink.view.activity.applicants.sendHistoryGrid', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.activity.applicants.sendHistoryModel',
		'KitchenSink.view.activity.applicants.sendHistoryStore',
		'KitchenSink.view.activity.applicants.applicantsController',
    ],
    xtype: 'sendHistory',
	controller: 'applicantsMg',
	store: {
        type: 'sendHistoryStore'
    },
    columnLines: true,
	/*
    selModel: {
        type: 'checkboxmodel'
    },
	*/
	style:"margin:8px",
   // multiSelect: true,
    title: '发送历史',
	header:false,
	frame: true,
	sendType: '',
	actID: '',
	
	dockedItems:[
		{
			xtype:"toolbar",
			items:[
				{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"cfgSearchSendHistory"},
			]
		},{
			xtype:"toolbar",
			dock:"bottom",
			ui:"footer",
			items:['->',{minWidth:80,text:"关闭",iconCls:"close",handler:'onFormClose'}]
		}
	],
    initComponent: function () {    
        Ext.apply(this, {
            columns: [{
				xtype: 'rownumberer'	
			},{ 
                text: '状态',
                dataIndex: 'sendStatus',
				width: 120
            },{
                text: '发送人数',
                sortable: true,
                dataIndex: 'sendCount',
                width: 120
            },{
                text: '成功人数',
                sortable: true,
                dataIndex: 'successNum',
                width: 120
            },{
                text: '失败人数',
                sortable: true,
                dataIndex: 'failedNum',
                width: 120
            },{
                text: '发送日期',
                sortable: true,
                dataIndex: 'sendDate',
                minWidth: 160,
                formatter: 'date("Y-m-d H:i:s")',
                flex: 1
            }],
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				listeners:{
					afterrender: function(pbar){
						var grid = pbar.findParentByType("grid");
						pbar.setStore(grid.store);
					}
				},
				/*
				store: {
					type: 'sendHistoryStore'
				},
                displayInfo: true,
				displayMsg: '显示{0}-{1}条，共{2}条',
				beforePageText: '第',
                afterPageText: '页/共{0}页',
				emptyMsg: '没有数据显示',
				*/
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
