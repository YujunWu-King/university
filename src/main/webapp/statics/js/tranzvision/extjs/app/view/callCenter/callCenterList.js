Ext.define('KitchenSink.view.callCenter.callCenterList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.callCenter.callCenterModel',
		'KitchenSink.view.callCenter.callCenterStore',
		'KitchenSink.view.callCenter.viewUserController'
    ],
    xtype: 'callCenterList',
    viewConfig: {markDirty: false},
	controller: 'viewUserController',
    columnLines: true,
	style:"margin:8px",
	selModel: {
       	type: 'checkboxmodel'
    },
    multiSelect: false,
    title: '接待单管理',
	header:false,
	frame: true,
    dockedItems:[
		{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchCallList"}
		]},
		{
			xtype:"toolbar",
			dock:"bottom",
			ui:"footer",
			items:['->',
                {minWidth:80,text:"关闭",iconCls:"close",handler:"onCloseRemoveData"}
            ]
		}
	],
    initComponent: function () {
		var store = new KitchenSink.view.callCenter.callCenterStore({
            listeners:{
                load:function(store){
                	
                }
            }
        });
		if(!store.autoLoad){
			store.load();
		}		
		
        Ext.apply(this, {
            columns: [{ 
                //text: '系统变量ID',
				text: '系统编号',
				draggable:false,//姓名不可拖动
                dataIndex: 'receiveId',
				width: 100
            },{
				text: '来电号码',
                sortable: true,				
                dataIndex: 'callPhone',
                width: 150
            },{
				text: '来电时间',
                sortable: true,				
                dataIndex: 'callDTime',
                width: 150
            },{
				text: '处理状态',
                sortable: true,				
                dataIndex: 'dealWithZT',
                width: 150,
                renderer:function(value){
                    if(value=='A'){
                        return '无需处理';
                    }else if(value=='C'){
                    	return '已处理';
                    }else{
                    	return '未处理';
                    }                    
                }
            },{
                text: '备注',
                sortable: true,
                dataIndex: 'callDesc',
                flex:1
            },
            {
               //menuDisabled: true,
			   text:'操作',
               sortable: false,
			   menuDisabled:true,
			   draggable:false,
			   width:80,
               xtype: 'actioncolumn',
			   align:'center',
			   items:[
				 {text: '查看',iconCls: 'view',tooltip: '查看',handler:"viewCallData"}
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
