Ext.define('KitchenSink.view.blackListManage.blackMg.blackMg', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.blackListManage.blackMg.blackMgController',
		'KitchenSink.view.blackListManage.blackMg.blackMgStore',
		'KitchenSink.view.blackListManage.blackMg.blackMgModel'
    ],
    xtype: 'blackMg',
	controller: 'blackMg',
	reference:"blackMgPanel",
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '黑名单管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{xtype:"toolbar",dock:"bottom",ui:"footer",items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveUserInfos'},{minWidth:80,text:"确定",iconCls:"ensure",handler:'ensureUserInfos'},{minWidth:80,text:"关闭",iconCls:"close",handler:'closeUserInfos'}]},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchUserList'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addUserAccount'},"-",
		//	{text:"查看",tooltip:"查看数据",iconCls: 'view',handler:'viewUserAccount'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls: 'edit',handler:'editUserAccount'},"-",
			{text:"移除",tooltip:"移除选中的数据",iconCls:"remove",handler:'deleteUserAccounts'},'->',
		]
	}],
    initComponent: function () {
	    //用户账号信息
    	var store = new KitchenSink.view.blackListManage.blackMg.blackMgStore();
        var acctLock = new KitchenSink.view.common.store.appTransStore("ACCTLOCK");
        acctLock.load();
        Ext.apply(this, {
			store: store,
            columns: [{ 
                text: '用户ID',
                dataIndex: 'OPRID',
				width: 20,
				hidden:true
            },/*{
                text: '编号',
                sortable: true,
                dataIndex: 'OPRID',
                width: 105
            },*/{
                text: '面试申请号',
                sortable: true,
                dataIndex: 'msSqh',
                width: 210,
            },{
                text: '姓名',
                sortable: true,
                dataIndex: 'userName',
                width: 100
            },{
                text: '性别',
                sortable: true,
				dataIndex: 'userSex',
                width: 60,
                hidden:true
            },{
                text: '手机',
                sortable: true,
                dataIndex: 'userPhone',
                width: 120
            },{
                text: '电子邮箱',
                sortable: true,
                dataIndex: 'userEmail',
                width: 250
            },{
                text:Ext.tzGetResourse("TZ_BLACK_LIST_COM.TZ_BLACK_LIST_STD.acctLock","锁定"),
                sortable: true,
                hidden:true,
                dataIndex: 'acctLock',
                minWidth: 100,
                renderer : function(value, metadata, record) {
                	
                    var index = acctLock.find('TValue',value);
                    if(index!=-1){
                        return acctLock.getAt(index).data.TSDesc;
                    }
                    return record.get('');
                }
            },{
                //text: '激活状态',
				text:Ext.tzGetResourse("TZ_BLACK_LIST_COM.TZ_BLACK_LIST_STD.jhState","激活状态"),
                sortable: true,
                dataIndex: 'jhState',
                width: 85,
                hidden:true
            },{
                text: '账号激活状态',
                sortable: true,
                dataIndex: 'jihuoZt',
                width: 120,
                hidden:true
            },{
                text: '创建日期时间',
                sortable: true,
                dataIndex: 'zcTime',
                width: 150,
                hidden:true
            },{
                text: '账号锁定状态',
                sortable: true,
                dataIndex: 'acctlock',
                minWidth: 110,
				flex:1,
				hidden:true
            },{
                text: '备注',
                sortable: true,
                dataIndex: 'beizhu',
                width: 120,
                flex:1
            },{
               menuDisabled: true,
               sortable: false,
			   			 width:50,
               xtype: 'actioncolumn',
			   			 items:[
			   			  /*{iconCls: 'view',tooltip: '查看'},*/
					  		{iconCls: 'edit',tooltip: '编辑',handler: 'editSelUserAccount'},
					  		{iconCls: 'remove',tooltip: '删除',handler: 'deleteUserAccount'}
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
