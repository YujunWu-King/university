Ext.define('KitchenSink.view.judgesManagement.judgesGroupMg.jugMsGroupMg', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.judgesManagement.judgesGroupMg.jugGroupMgModel',
        'KitchenSink.view.judgesManagement.judgesGroupMg.jugMsGroupMgStore',
        'KitchenSink.view.judgesManagement.judgesGroupMg.jugMsGroupMgController'
    ],
    xtype: 'jugMsMg',
	controller: 'jugMsMg',
	reference:"jugMsMgPanel",
    columnLines: true,
    selModel: {
        type: 'rowmodel'
    },
	style:"margin:8px",
    multiSelect: false,
    title: '面试评审评委组管理',
    viewConfig: {
        enableTextSelection: false
    },
	header:false,
	frame: true,
    dockedItems:[{xtype:"toolbar",dock:"bottom",ui:"footer",items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveList'},{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureList'},{minWidth:80,text:"关闭",iconCls:"close",handler:'closeList'}]},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchDataList'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addDataList'},'->'
		]
	}],
    initComponent: function () {
	    //面试评审评委组
    	var store = new KitchenSink.view.judgesManagement.judgesGroupMg.jugMsGroupMgStore();
        Ext.apply(this, {
			store: store,
            columns: [{
                text: '面试评审组ID',
                sortable: true,
                dataIndex: 'jugGroupId',
                width: 305
            },{
                text: '面试评审组名称',
                sortable: true,
                dataIndex: 'jugGroupName',
                width: 305,
                flex: 1
            },{
                text: '面试秘书',
                sortable: true,
                dataIndex: 'roleName',
                width: 305,
                flex: 1
            },{
               menuDisabled: true,
               sortable: false,
			   			 width:50,
               xtype: 'actioncolumn',
			   			 items:[
					  		{iconCls: 'edit',tooltip: '编辑',handler: 'editSelData'},
					  		{iconCls: 'remove',tooltip: '删除',handler: 'deleteSelData'}
			   			]
            }
			],
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
