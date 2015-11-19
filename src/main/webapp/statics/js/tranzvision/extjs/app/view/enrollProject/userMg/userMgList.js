Ext.define('KitchenSink.view.enrollProject.userMg.userMgList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollProject.userMg.userMgController',
        'KitchenSink.view.enrollProject.userMg.userMgStore'
    ],
    xtype: 'userMgGL',//不能变
    controller: 'userMgController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '申请用户管理',
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
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'queryUser'},"-",
			{text:"查看",tooltip:"查看数据",iconCls: 'view',handler:'viewUserByBtn'},'->',
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
				},{
					text:'邮件发送历史',
					iconCls: 'mail',
					handler:'viewMailHistory'	
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
                width: 81
            },{
                text: '性别',
                sortable: true,
				dataIndex: 'userSex',
                width: 60
            },{
                text: '电子邮箱',
                sortable: true,
                dataIndex: 'userEmail',
                width: 230
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
                minWidth: 110,
				flex:1,
            },{
			   xtype: 'actioncolumn',
			   text: '操作',	
               menuDisabled: true,
			   menuText: '操作',
               sortable: false,
			   with:50,
			   align: 'center',
			   			 items:[
			   			  {text: '查看',iconCls: 'view',tooltip: '查看',handler:'viewUserByRow'}
			   			]
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
