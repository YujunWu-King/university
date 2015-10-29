Ext.define('KitchenSink.view.enrollProject.userMg.userMgList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.userMg.userMgController',
		'KitchenSink.view.enrollProject.userMg.userMgModel',
        'KitchenSink.view.enrollProject.userMg.userMgStore'
    ],
    xtype: 'userMgGL',//不能变
    controller: 'userMgController',
	/*store: {
        type: 'userMgStore'
    },*/
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '会员用户管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:"saveUserMgInfos"}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryUser'},"-",
			{text:"查看",tooltip:"查看数据",iconCls: 'view',handler:'viewUser'},'->',
			{
				xtype:'splitbutton',
				text:'更多操作',
				iconCls:  'list',
				glyph: 61,
				menu:[{
					text:'重置密码',
					handler:'resetPassword'
				},{
					text:'关闭账号',
					handler:'deleteUser'
				}]
			}
		]
	}],
    initComponent: function () {    
    	var store = new KitchenSink.view.enrollProject.userMg.userMgStore();
        Ext.apply(this, {
            columns: [{ 
                text: '用户ID',
                dataIndex: 'OPRID',
				width: 20,
				hidden:true
            },{
                text: '姓名',
                sortable: true,
                dataIndex: 'userName',
                width: 91
            },{
                text: '性别',
                sortable: true,
				dataIndex: 'userSex',
                width: 62
            },{
                text: '电子邮箱',
                sortable: true,
                dataIndex: 'userEmail',
                width: 251
            },{
                text: '手机',
                sortable: true,
                dataIndex: 'userPhone',
                width: 120
            },{
                text: '账号激活状态',
                sortable: true,
                dataIndex: 'jihuoZt',
                width: 120
            },{
                text: '创建日期时间',
                sortable: true,
                dataIndex: 'zcTime',
                width: 150
            },{
                text: '账号锁定状态',
                sortable: true,
                dataIndex: 'acctlock',
                width: 120,
            },{
			   menuText: '操作',	
               menuDisabled: true,
               sortable: false,
			   width:50,
			   align: 'center',
               xtype: 'actioncolumn',
			   			 items:[
			   			  {iconCls: 'view',tooltip: '查看'}
			   			]
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
				/*store: {
					type: 'userMgStore'
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
