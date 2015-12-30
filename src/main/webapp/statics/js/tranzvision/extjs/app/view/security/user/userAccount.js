Ext.define('KitchenSink.view.security.user.userAccount', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.security.user.userController',
		'KitchenSink.view.security.user.userModel',
        'KitchenSink.view.security.user.userStore'
    ],
    xtype: 'userMg',
	controller: 'userMg',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '用户账号管理',
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
			{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:'deleteUserAccounts'},'->',
			{
				xtype:'splitbutton',
				text:'更多操作',
				iconCls:  'list',
				glyph: 61,
				menu:[{
					text:'重置密码',
					handler:'resetPassword'
				},{
					text:'锁定账号',
					handler:'lockUserAccount'
				}]
			}
		]
	}],
    initComponent: function () {
    	if((Ext.tzOrgID).toUpperCase() == "ADMIN"){
    		this.title = "用户账号管理";
    	}else{
    		this.title = "内部用户管理";
    	}
    	
	    //用户账号信息
	    var store = new KitchenSink.view.security.user.userStore();
        var acctLock = new KitchenSink.view.common.store.appTransStore("ACCTLOCK");
        acctLock.load();
        Ext.apply(this, {
			store: store,
            columns: [{ 
                //text: '机构',
				text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.jgId","机构"),
                dataIndex: 'orgId',
				width: 85
            },{ 
                //text: '登录账号',
				text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.usAccNum","登录账号"),
                dataIndex: 'usAccNum',
				minWidth: 100,
                flex: 1
            },{
                //text: '用户名称',
				text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.usName","用户名称"),
                sortable: true,
                dataIndex: 'usName',
                minWidth: 100,
                flex: 1
            },{
                //text: '手机号码',
				text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.mobile","手机号码"),
                sortable: true,
                dataIndex: 'mobile',
                width: 120
            },{
                //text: '电子邮箱',
				text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.email","电子邮箱"),
                sortable: true,
                dataIndex: 'email',
                minWidth: 150,
				flex: 1
            },/*{
            	xtype: 'checkcolumn',
            	disabled: true,
                //text: '锁定',
							text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.acctLock","锁定"),
                sortable: true,
                dataIndex: 'acctLock',
                width: 60
            }*/
                {
                    text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.acctLock","锁定"),
                    sortable: true,
                    dataIndex: 'acctLock',
                    minWidth: 100,
                    renderer : function(value, metadata, record) {
                    	
                        var index = acctLock.find('TValue',value);
                        if(index!=-1){
                            return acctLock.getAt(index).data.TSDesc;
                        }
                        return record.get('');
                    }
                }
            /*,{
            	xtype: 'checkcolumn',
            	disabled: false,
                //text: '手机绑定标志',
				text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.mBindFlag","手机绑定标志"),
                sortable: true,
                dataIndex: 'mBindFlag',
                listeners: {
                	"beforecheckchange": function(){
                		return false;
                	}
                }, 
                width: 120
            },{
            	xtype: 'checkcolumn',
                //text: '邮箱绑定标志',
				text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.eBindFlag","邮箱绑定标志"),
                sortable: true,
                dataIndex: 'eBindFlag',
                listeners: {
                	"beforecheckchange": function(){
                		return false;
                	}
                }, 
                width: 120
            }*/
            ,{
                //text: '账号类型',
				text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.rylx","账号类型"),
                sortable: true,
                dataIndex: 'rylx',
                minWidth: 110,
                flex: 1
            }
            ,{
                //text: '激活状态',
				text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.jhState","激活状态"),
                sortable: true,
                dataIndex: 'jhState',
                width: 85
            },{
                //text: '激活方式',
				text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHGL_STD.jhMethod","激活方式"),
                sortable: true,
                dataIndex: 'jhMethod',
                minWidth: 100,
                flex: 1
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
