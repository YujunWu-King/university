Ext.define('KitchenSink.view.template.sitetemplate.siteTemplateInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'siteTemplateInfo', 
	controller: 'siteChildInfo',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.sitetemplate.skinStore',
        'KitchenSink.view.template.sitetemplate.templateStore',
        'KitchenSink.view.template.sitetemplate.columnStore',
        'KitchenSink.view.template.sitetemplate.areaStore',
        'KitchenSink.view.template.sitetemplate.menuStore',
        'KitchenSink.view.template.sitetemplate.siteChildsController'
	],
    title: '站点模板基本信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'siteAccountForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        //frame: true,
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		buttonAlign: 'center',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 170,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            readOnly:true,
			fieldStyle:'background:#F4F4F4',
            fieldLabel: '站点模板编号',
			name: 'siteId',
			value: 'NEXT'
        }, {
            xtype: 'combobox',
            fieldLabel: '启用状态',
            emptyText:'请选择',
            queryMode: 'remote',
            editable:false,
			name: 'enabled',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_SITEM_ENABLE")
    		/*
			listeners: {
			  	afterrender: function(tvType){
					Ext.tzLoad('{"OperateType":"TV","fieldName":"TZ_SITEM_ENABLE"}',function(response){
						tvType.setStore(new Ext.data.Store({		
							fields: ['TValue', 'TSDesc', 'TLDesc'],
							data:response.TZ_SITEM_ENABLE
						}));
					});
				}
			}*/
        },{
			xtype: 'combobox',
            fieldLabel: '站点语言',
            editable:false,
            emptyText:'请选择',
            queryMode: 'remote',
			name: 'siteLanguage',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_GD_LANGUAGE")
		},{
            xtype: 'textfield',
            fieldLabel: '站点模板名称',
			name: 'siteName'
        },{  
            xtype: 'textareafield',  
            grow: true,
            name: 'siteIntroduce',  
            fieldLabel: '站点模板说明',
            preventScrollbars : false
        },{
            xtype: 'textfield',
            fieldLabel: '图片存放的服务器文件夹',
			name: 'picPrefix'
        },{
            xtype: 'textfield',
            fieldLabel: '附件存放的服务器文件夹',
			name: 'attPrefix'
        },{
            xtype: 'textfield',
            fieldLabel: '视频存放的服务器文件夹',
			name: 'viewPrefix'
        },{
            xtype: 'textfield',
            fieldLabel: '站点首页处理程序',
			name: 'siteHomeProgram'
        },{
            xtype: 'textfield',
            fieldLabel: '站点登录页处理程序',
			name: 'siteLoginProgram'
        },{
            xtype: 'textfield',
            fieldLabel: '站点注册页处理程序',
			name: 'siteRegistProgram'
        },{
			xtype : 'tabpanel',
			activeTab: 0,
        	plain:false,
        	frame: true,
        	resizeTabs:true,
        	autoHeight:true,
        	defaults :{
            	autoScroll: false,
            	//margin:5
        	},
        	listeners:{
        		tabchange:function(tp,p){
        			var queryID;
        			var siteId = tp.findParentByType("form").getForm().findField('siteId').getValue();
        			//tp.findParentByType("form").getForm().findField('siteLoginProgram').setHidden(true);
        			//tp.findParentByType("form").getForm().findField('siteRegistProgram').setHidden(true);
        			tp.findParentByType("form").getForm().findField('skinAddress').setHidden(true);
        			if(p.title == "皮肤设置"){
        				queryID = "1";
        				//tp.findParentByType("form").getForm().findField('siteLoginProgram').setHidden(false);
        				//tp.findParentByType("form").getForm().findField('siteRegistProgram').setHidden(false);
        				tp.findParentByType("form").getForm().findField('skinAddress').setHidden(false);
        			}
        			if(p.title == "站点模板集合"){
        				queryID = "2";
        			}
        			if(p.title == "站点栏目集合"){
        				queryID = "3";
        			}
        			if(p.title == "站点区域集合"){
        				queryID = "4";
        			}
        			if(p.title == "站点菜单集合"){
        				queryID = "5";
        			}
        			this.doLayout();
					if (queryID == "1" || queryID == "2" || queryID == "3" || queryID == "4" || queryID == "5")
					{
						var tzStoreParams = '{"siteId":"'+siteId+'","queryID":"' + queryID + '"}';
						p.store.tzStoreParams = tzStoreParams;
						p.store.load();
					}
        			
                }
            },
			items : [{
				//id:'skin',
				title:'皮肤设置',
				xtype: 'grid',
				//frame: true,
				columnLines: true,
				minHeight: 250,
				reference: 'skinGrid',
				store: {
					type: 'skinStore'
				},
				columns: [{
					text: '皮肤编号',
					dataIndex: 'skinId',
					width: 200
				},{
					text: '皮肤名称',
					dataIndex: 'skinName',
					minWidth: 5,
					flex: 1
				},{
					text: '状态',
					dataIndex: 'skinStatus',
					width: 200
				},{
					text: '操作',
	               	menuDisabled: true,
	               	sortable: false,
				   	width:100,
	               	xtype: 'actioncolumn',
				   	items:[
				   		{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteSkin'},
				   		'-',
						{iconCls: 'remove',tooltip: '删除',handler: 'deleteSiteSkin'}
				   	]
	            }],
	            tbar: [{
					xtype:"toolbar",
                    items: [{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addSkin'}]
                }],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 10,
					/*
					reference: 'skinToolBar',
					store: {
						type: 'skinStore'
					},
					*/
					listeners:{
						afterrender: function(pbar){
							var grid = pbar.findParentByType("grid");
							pbar.setStore(grid.store);
						}
					},
					displayInfo: true,
					displayMsg: '显示{0}-{1}条，共{2}条',
					beforePageText: '第',
					afterPageText: '页/共{0}页',
					emptyMsg: '没有数据显示',
					plugins: new Ext.ux.ProgressBarPager()
				}
			},{
				//id:'template',
				title:'站点模板集合',
				xtype: 'grid',
				columnLines: true,
				minHeight: 250,
				reference: 'templateGrid',
				store: {
					type: 'templateStore'
				},
				columns: [{ 
					text: '模板编号',
					dataIndex: 'templateId',
					width: 200
				},{
					text: '模板名称',
					dataIndex: 'templateName',
					minWidth: 5,
					flex: 1
				},{
					text: '模板类型',
					dataIndex: 'templateType',
					width: 200
				},{
					text: '操作',
	               	menuDisabled: true,
	               	sortable: false,
				   	width:100,
	               	xtype: 'actioncolumn',
				   	items:[
				   		{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteTemplate'},
				   		'-',
						{iconCls: 'remove',tooltip: '删除',handler: 'deleteSiteTemplate'}
				   	]
	            }],
	            tbar: [{
					xtype:"toolbar",
                    items: [{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addTemplate'}]
                }],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 10,
					/*
					reference: 'templateToolBar',
					store: {
						type: 'templateStore'
					},
					*/
					listeners:{
						afterrender: function(pbar){
							var grid = pbar.findParentByType("grid");
							pbar.setStore(grid.store);
						}
					},
					displayInfo: true,
					displayMsg: '显示{0}-{1}条，共{2}条',
					beforePageText: '第',
					afterPageText: '页/共{0}页',
					emptyMsg: '没有数据显示',
					plugins: new Ext.ux.ProgressBarPager()
				}
			},{
				//id:'column',
				title:'站点栏目集合',
				xtype: 'grid',
				columnLines: true,
				minHeight: 250,
				reference: 'columnGrid',
				store: {
					type: 'columnStore'
				},
				columns: [{ 
					text: '栏目编号',
					dataIndex: 'lm_id',
					width: '150'
				},{
					text: '栏目名称',
					dataIndex: 'lm_name',
					flex: 1
				},{
					text: '栏目类型',
					dataIndex: 'lm_lx',
					width: '150'
				},{
					text: '内容类型',
					dataIndex: 'lm_nrlx',
					width: '150'
				},{
					text: '操作',
	               	menuDisabled: true,
	               	sortable: false,
				   	width:100,
	               	xtype: 'actioncolumn',
				   	items:[
				   		{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteColumn'},
				   		'-',
						{iconCls: 'remove',tooltip: '删除',handler: 'deleteSiteColumn'}
				   	]
	            }],
	            tbar: [{
					xtype:"toolbar",
                    items: [{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addColumn'}]
                }],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 10,
					/*
					reference: 'columnToolBar',
					store: {
						type: 'columnStore'
					},
					*/
					listeners:{
						afterrender: function(pbar){
							var grid = pbar.findParentByType("grid");
							pbar.setStore(grid.store);
						}
					},
					displayInfo: true,
					displayMsg: '显示{0}-{1}条，共{2}条',
					beforePageText: '第',
					afterPageText: '页/共{0}页',
					emptyMsg: '没有数据显示',
					plugins: new Ext.ux.ProgressBarPager()
				}
			},{
				//id:'area',
				title:'站点区域集合',
				xtype: 'grid',
				columnLines: true,
				minHeight: 250,
				reference: 'areaGrid',
				store: {
					type: 'areaStore'
				},
				columns: [{ 
					text: '区域编号',
					dataIndex: 'areaid',
					width: '150'
				},{
					text: '区域名称',
					dataIndex: 'areaname',
					flex: 1
				},{
					text: '区域类型',
					dataIndex: 'areatypeid',
					width: '150'
				},{
					text: '区域位置',
					dataIndex: 'areaposition',
					width: '150'
				},{
					text: '操作',
	               	menuDisabled: true,
	               	sortable: false,
				   	width:100,
	               	xtype: 'actioncolumn',
				   	items:[
				   		{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteArea'},
				   		'-',
						{iconCls: 'remove',tooltip: '删除',handler: 'deleteSiteArea'}
				   	]
	            }],
	            tbar: [{
					xtype:"toolbar",
                    items: [{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addArea'},'-',
                    		{text:"区域类型管理",tooltip:"区域类型管理",iconCls:"add",handler:'addTypeArea'}]
                }],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 10,
					/*
					reference: 'areaToolBar',
					store: {
						type: 'areaStore'
					},
					*/
					listeners:{
						afterrender: function(pbar){
							var grid = pbar.findParentByType("grid");
							pbar.setStore(grid.store);
						}
					},
					displayInfo: true,
					displayMsg: '显示{0}-{1}条，共{2}条',
					beforePageText: '第',
					afterPageText: '页/共{0}页',
					emptyMsg: '没有数据显示',
					plugins: new Ext.ux.ProgressBarPager()
				}
			},{
				//id:'menu',
				title:'站点菜单集合',
				xtype: 'grid',
				columnLines: true,
				minHeight: 250,
				reference: 'menuGrid',
				store: {
					type: 'menuStore'
				},
				columns: [{ 
					text: '菜单编号',
					dataIndex: 'menuid',
					width: 200
				},{
					text: '菜单名称',
					dataIndex: 'menuname',
					minWidth: 5,
					flex: 1
				},{
					text: '菜单功能类型',
					dataIndex: 'menutypename',
					width: 200
				},{
					text: '操作',
	               	menuDisabled: true,
	               	sortable: false,
				   	width:100,
	               	xtype: 'actioncolumn',
				   	items:[
				   		{iconCls: 'edit',tooltip: '编辑',handler: 'editSiteMenu'},
				   		'-',
						{iconCls: 'remove',tooltip: '删除',handler: 'deleteSiteMenu'}
				   	]
	            }],
	            tbar: [{
					xtype:"toolbar",
                    items: [{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addMenu'},'-',
                    		{text:"菜单类型管理",tooltip:"菜单类型管理",iconCls:"add",handler:'addTypeMenu'}]
                }],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 10,
					/*
					reference: 'menuToolBar',
					store: {
						type: 'menuStore'
					},
					*/
					listeners:{
						afterrender: function(pbar){
							var grid = pbar.findParentByType("grid");
							pbar.setStore(grid.store);
						}
					},
					displayInfo: true,
					displayMsg: '显示{0}-{1}条，共{2}条',
					beforePageText: '第',
					afterPageText: '页/共{0}页',
					emptyMsg: '没有数据显示',
					plugins: new Ext.ux.ProgressBarPager()
				}
			},{
				title:'首页源代码',
				xtype: 'form',
				layout: {
				 type: 'vbox',
				 align: 'stretch'
				},
				minHeight: 350,
				border: false,
				bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				buttonAlign: 'center',
				fieldDefaults: {
				msgTarget: 'side',
					labelWidth: 170,
					labelStyle: 'font-weight:bold'
				},
				items: [{
				 xtype: 'textarea',
				 height: 350,
                 fieldLabel: '初始化源代码',
			     name: 'indexInitCode'
				}]
			},{
				title:'登录页源代码',
				xtype: 'form',
				layout: {
				 type: 'vbox',
				 align: 'stretch'
				},
				border: false,
				bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				buttonAlign: 'center',
				fieldDefaults: {
				msgTarget: 'side',
					labelWidth: 170,
					labelStyle: 'font-weight:bold'
				},
				items: [{
				 xtype: 'textarea',
				 height: 350,
                 fieldLabel: '初始化源代码',
			     name: 'loginInitCode'
				}]
			}]
		},{
            xtype: 'textfield',
            fieldLabel: '皮肤存放地址',
            margin:'20 0 0 0',
			name: 'skinAddress'
        }]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});