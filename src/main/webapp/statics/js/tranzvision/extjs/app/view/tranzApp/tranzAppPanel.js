Ext.define('KitchenSink.view.tranzApp.tranzAppPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'tranzAppPanel', 
	controller: 'tranzAppController',
	actType:'',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
	   'KitchenSink.view.tranzApp.tranzAppController',
	   'KitchenSink.view.tranzApp.tranzUserAppStore'
	],
    title: '应用分配定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'tranzAppForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 130,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TRANZAPP_STD.jgId","机构id"),
			name: 'jgId',
			hidden: true
        },{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TRANZAPP_STD.appName","应用分配名称"),
			name: 'appName',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            maxLength: 100,
            allowBlank: false
        },{
        	layout: {
				type: 'column'
			},
			bodyStyle:'padding:0 0 5px 0',
        	items:[
				{
					columnWidth: 1,
				    xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TRANZAPP_STD.appId","应用分配appId"),
					name: 'appId',
					readOnly: true,
					cls: 'lanage_1',
					afterLabelTextTpl: [
				        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
				    ],
				    maxLength: 30,
					allowBlank: false
				},{
					xtype:'button',
					text:'<span style="text-decoration:underline;color:blue;">生成</span>',
					textAlign: 'left',
					border:false,
					width: 100,
					style:{
						background: 'white',
						boxShadow:'none'
					},
					handler: function(fld){
						var panel = fld.findParentByType("tranzAppPanel");
						if(panel.actType == "add"){
							var form = fld.findParentByType("form").getForm();
							
							var tzParams = '{"ComID":"TZ_GD_TRANZAPP_COM","PageID":"TZ_GD_TRANZAPP_STD","OperateType":"APPID","comParams":{}}';
							
							//加载数据
							Ext.tzLoad(tzParams,function(responseData){
								form.findField("appId").setValue(responseData.RANDOM);
							});
						}else{
							Ext.MessageBox.alert('提示', '保存后不可修改');
						}
						
					}
				}  
        	] 
        },{
        	layout: {
				type: 'column'
			},
			bodyStyle:'padding:0 0 5px 0',
        	items:[
				{
					columnWidth: 1,
				    xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TRANZAPP_STD.appSecret","应用分配appSecret"),
					name: 'appSecret',
					afterLabelTextTpl: [
				        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
				    ],
					maxLength: 60,
					allowBlank: false
				},{
					xtype:'button',
					text:'<span style="text-decoration:underline;color:blue;">生成/重置</span>',
					textAlign: 'left',
					border:false,
					width: 100,
					style:{
						background: 'white',
						boxShadow:'none'
					},
					handler: function(fld){
						var form = fld.findParentByType("form").getForm();
						
						var tzParams = '{"ComID":"TZ_GD_TRANZAPP_COM","PageID":"TZ_GD_TRANZAPP_STD","OperateType":"APPSECRET","comParams":{}}';
						
						//加载数据
						Ext.tzLoad(tzParams,function(responseData){
							form.findField("appSecret").setValue(responseData.RANDOM);
						});
					}
				}  
        	]
        }, {
            xtype: 'textarea',
			fieldLabel: Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TRANZAPP_STD.appDesc","备注"),
			name: 'appDesc'
        }]
    },{
		xtype: 'grid',
		title: '登录用户关系列表',
		frame: true,
		columnLines: true,
		height: 350,
		reference: 'tranzAppUserGrid',
		style: "margin:10px",
		multiSelect: true,
		selModel: {
			type: 'checkboxmodel'
		},
		plugins: [
			Ext.create('Ext.grid.plugin.CellEditing',{
				clicksToEdit: 1
			})
		],
		columns: [{ 
            text: '机构id',
            dataIndex: 'jgId',
            hidden: true
        },{ 
            text: 'appid',
            dataIndex: 'appId',
            hidden: true
        },{
			text: Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TRANZAPP_STD.otherUserName","用户名"),
			dataIndex: 'otherUserName',
			width:240
		},{
			text: Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TRANZAPP_STD.userName","姓名"),
			dataIndex: 'userName',
			width:240
		},{
			text: Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TRANZAPP_STD.accountId","指定登录用户"),
			dataIndex: 'accountId',
			minWidth: 100,
			flex: 1
		},
		//将原来的checkbox 改为下拉框
		{
			text:Ext.tzGetResourse("TZ_GD_TRANZAPP_COM.TZ_GD_TRANZAPP_STD.isEnable","是否启用"),
			sortable: true,
			dataIndex: 'isEnable',
			minWidth: 100,
			align:'center',
			renderer : function(value, metadata, record) {
				console.log(value);
				if (value=="Y"){
					return "是";
				}else if(value=="N"){
					return "否";
				}
			}
		},
		{
			menuDisabled: true,
			sortable: false,
			width:60,
			align:'center',
			xtype: 'actioncolumn',
			items:[
				{iconCls: 'edit',tooltip: '编辑',handler: 'editSelTranzAppUser'},
				{iconCls: 'remove',tooltip: '删除',handler: 'deleteSelTranzAppUser'}
			]
			}
		],
		store: {
			type: 'tranzUserAppStore'
		},
		dockedItems:[{
			xtype:"toolbar",
			items:[
				{text:"新增",tooltip:"新增数据",iconCls:"add",handler:"addTanzUserInfo"},"-",
				{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:"editTranzAppUser"},"-",
				{text:"删除",tooltip:"删除选中的数据",iconCls:"remove",handler:"deleteTranzAppUser"}
			]
		}],
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 5,
			/*store: {
			 type: 'tranzUserAppStore'
			 },*/
			listeners:{
				afterrender: function(pbar){
					var grid = pbar.findParentByType("grid");
					pbar.setStore(grid.store);
				}
			},
			plugins: new Ext.ux.ProgressBarPager()
		}
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onTranAppSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onTranAppSure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onTanzAppClose'
	}]
});
 
