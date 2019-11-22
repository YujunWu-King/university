Ext.define('KitchenSink.view.enrollProject.userMg.demoList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.userMg.demoController',
        'KitchenSink.view.enrollProject.userMg.demoStore',
        'KitchenSink.view.enrollProject.userMg.demoModel'
    ],
    xtype: 'demoGL',//不能变
    controller: 'demoController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: 'Class查询Demo',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"关闭",iconCls:"close",handler:"onListClose"}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchComList'}

		]
	}],
    initComponent: function () {    
    	var store = new KitchenSink.view.enrollProject.userMg.demoStore();
    	
        Ext.apply(this, {
            columns: [{
                text: '用户ID',
                dataIndex: 'OPRID',
				width: 20,
				hidden:true
            },{
                text: '姓名',
                sortable: true,
                dataIndex: 'TZ_REALNAME',
                width: 75
            },{
                text: '手机',
                sortable: true,
                dataIndex: 'TZ_MOBILE',

                width: 75
            },{
                text: '班级',
                sortable: true,
                dataIndex: 'TZ_CLASS_NAME',
                width: 75
            },{
                text: '批次',
                sortable: true,
                dataIndex: 'TZ_BATCH_NAME',
                width: 95,
                flex:1
            },{
                text: '提交状态',
                sortable: true,
                dataIndex: 'TZ_APP_FORM_STA',
                width: 100
            },{
                text: '提交时间',
                sortable: true,
                dataIndex: 'TZ_APP_SUB_DTTM',
                width: 180
            },{
            	text:'填写比例',
            	sortable: true,
                dataIndex: 'TZ_FILL_PROPORTION',
                width: 180
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
