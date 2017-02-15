Ext.define('KitchenSink.view.judgesManagement.judgesTypeMg.jugTypeMg', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.judgesManagement.judgesTypeMg.jugTypeMgModel',
        'KitchenSink.view.judgesManagement.judgesTypeMg.jugTypeMgStore',
        'KitchenSink.view.judgesManagement.judgesTypeMg.jugTypeMgController',
    ],
    xtype: 'jugMg',
	controller: 'jugMg',
	reference:"jugMgPanel",
    columnLines: true,
    selModel: {
        type: 'rowmodel'
    },
	style:"margin:8px",
    multiSelect: false,
    title: '评委类型管理',
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
	    //评委类型信息
    	var store = new KitchenSink.view.judgesManagement.judgesTypeMg.jugTypeMgStore();
        var statu = new KitchenSink.view.common.store.appTransStore("TZ_JUGTYP_STAT");
        statu.load();
        Ext.apply(this, {
			store: store,
            columns: [{
                text: '评审类型名称',
                sortable: true,
                dataIndex: 'jugTypeName',
                flx:1,
                width: 305
            },{
                text: '角色名称',
                sortable: true,
                dataIndex: 'rolName',
                width: 305
            },{
                text: '编号',
                sortable: true,
                dataIndex: 'jugTypeId',
                width: 150
            },{
                text:"状态",
                sortable: true,
                dataIndex: 'jugTypeStatus',
                readOnly: true,
                minWidth: 100,
                renderer : function(value, metadata, record) {
                	
                    var index = statu.find('TValue',value);
                    if(index!=-1){
                        return statu.getAt(index).data.TSDesc;
                    }
                    return record.get('');
                }
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
