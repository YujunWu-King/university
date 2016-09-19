Ext.define('KitchenSink.view.siteManage.outsiteManage.menuEditPanel',{
	extend : 'Ext.panel.Panel',
	xtype : 'menuEdit',
	controller : 'menuEdit',
	requires : ['Ext.data.*',
	            'Ext.util.*',
	            'KitchenSink.view.siteManage.outsiteManage.menuEditController',
	            'KitchenSink.view.siteManage.outsiteManage.menuTreeStore',
	            'KitchenSink.view.siteManage.outsiteManage.menuStore',
	            'Ext.data.TreeStore' ],
	title : '站点菜单管理',
	width : 640,
	layout : 'border',
	viewModel : true,
	actType : 'update',
	thisMenuId : 'NEXT',

	initComponent : function() {
		me = this;
		this.items = [{
			title : '站点菜单树',
			region : 'west',
			xtype : 'treepanel',
			width : 300,
			split : true,
			collapsible : true,
			autoScroll : true,
			lines : true,
			rootVisible : true,
			store : new KitchenSink.view.siteManage.outsiteManage.menuTreeStore({
				siteId : me.siteId
			}),
			listeners : {
				afterrender : function(thisTree) {
				},
				itemclick : function(view, record,item, index, e, eOpts) {
					var form = view.findParentByType("menuEdit").child("form").getForm();
					form.setValues({
						menuId : record.data.id,
						menuName : record.data.text,
						menuPath : record.data.menuPath,
						menuState : record.data.menuState,
						menuTempletId : record.data.menuTempletId,
						menuTempletName : record.data.menuTempletName,
						menuPageName : record.data.menuPageName,
						menuType : record.data.menuType,
						menuXH : record.data.menuXH,
						isDefault : record.data.isDefault,
						defaultPage : record.data.defaultPage,
						NodeType : record.data.NodeType,
						operateNode : record.data.operateNode,
						rootNode : record.data.rootNode,
						siteId : me.siteId
					});
					form.findField("menuId").setReadOnly(true);
					form.findField("menuId").addCls('lanage_1'); 
					form.findField("menuPath").setReadOnly(true);
					form.findField("menuPath").addCls('lanage_1'); 
					form.findField("menuType").setReadOnly(true);
					form.findField("menuType").addCls('lanage_1'); 
					// 如果是BOOK类型隐藏menuPageName隐藏是否默认页面，显示路径
					// A:PAGE  B:BOOK
					if (record.data.menuType == "B") {
						//form.findField("menuPageName").hide();
						form.findField("isDefault").hide();
						form.findField("menuPath").show();
						form.findField("defaultPage").show();
					} else {
						//form.findField("menuPageName").show();
						form.findField("isDefault").show();
						form.findField("menuPath").hide();
						form.findField("defaultPage").hide();
					}
					view.findParentByType("menuEdit").actType = "update";
					
					/*var grid = view.findParentByType("menuEdit").child('grid');
					var tzStoreParams = '{"menuId":"' + record.data.id + '"}';
					grid.store.tzStoreParams = tzStoreParams;
					grid.store.load(); */
				}
			}
		},{
			/*xtype: 'panel',
            region: 'center', 
            frame: true,
            title: '菜单',
            reference: 'menuPanel',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            border: false,
            scrollable:true,
            //collapsible : true,
			autoScroll : true,
            bodyStyle: 'overflow-y:auto;overflow-x:hidden',

            fieldDefaults: {
                msgTarget: 'side',
                labelWidth:120,
                labelStyle: 'font-weight:bold'
            },*/
			//items: [{ 
				region : 'center',
				frame : true,
				xtype : 'form',
				reference : 'menuEditForm',
				layout : {
					type : 'vbox',
					align : 'stretch'
				},
				border : false,
				bodyPadding : 10,
				bodyStyle : 'overflow-y:auto;overflow-x:hidden',
				fieldDefaults : {
					msgTarget : 'side',
					labelWidth : 120,
					labelStyle : 'font-weight:bold'
				},
				items : [{
					xtype : "toolbar",
					items : [{
						text : "插入同级节点",
						iconCls : 'siblingnode',
						tooltip : "插入同级节点",
						handler : 'inserMenuItem'
					},"-",{
						text : "插入子节点",
						iconCls : 'childnode',
						tooltip : "插入子节点",
						handler : 'inserChildMenuItem'
					},"-",{
						text : "删除",
						iconCls : 'remove',
						tooltip : "删除",
						handler : 'removeMenuItem'
					}]
				},{
					xtype : 'textfield',
					fieldLabel : '菜单编号',
					name : 'menuId',
					//allowBlank : false,
					value : 'NEXT'
				},{
					// 站点ID
					xtype : 'textfield',
					name : 'siteId',
					hidden : true
				},{
					xtype : 'textfield',
					fieldLabel : '菜单名称',
					name : 'menuName',
					beforeLabelTextTpl : [ '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>' ],
					allowBlank : false
				},{
					xtype : 'textfield',
					fieldLabel : '顺序',
					name : 'menuXH',
					beforeLabelTextTpl : [ '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>' ],
					allowBlank : false
				},{
					xtype : 'combobox',
					fieldLabel : '有效状态',
					forceSelection : true,
					allowBlank : false,
					valueField : 'TValue',
					displayField : 'TSDesc',
					store : new KitchenSink.view.common.store.appTransStore("TZ_YXX"),
					typeAhead : true,
					queryMode : 'local',
					name : 'menuState'
				},{
					xtype : 'combobox',
					fieldLabel : '菜单类型',
					forceSelection : true,
					allowBlank : false,
					valueField : 'TValue',
					displayField : 'TSDesc',
					//allowBlank : false,
					store : new KitchenSink.view.common.store.appTransStore("TZ_ZDCD_LX"),
					typeAhead : true,
					queryMode : 'local',
					name : 'menuType',
					listeners : {
						select : function(combo,record,index){	
							form= combo.findParentByType("form").getForm();
							// 如果是BOOK类型隐藏menuPageName隐藏是否默认页面，显示路径
							if (combo.getValue() == "B") {
								//form.findField("menuPageName").hide();
								form.findField("isDefault").hide();
								form.findField("menuPath").show();
								form.findField("defaultPage").show();		
							} else {
								//form.findField("menuPageName").show();
								form.findField("isDefault").show();
								form.findField("menuPath").hide();
								form.findField("defaultPage").hide();
							}
						}
					}
				},{
					layout : {
						type : 'column'
					},
					items : [{
						columnWidth : .55,
						xtype : 'textfield',
						fieldLabel : '菜单模板',
						name : 'menuTempletId',
						editable : false,
						triggers : {
							clear : {
								cls : 'x-form-clear-trigger',
								handler : 'clearPmtSearchTemplet'
							},
							search : {
								cls : 'x-form-search-trigger',
								handler : "pmtSearchTemplet"
							}
						}
					},{
						columnWidth : .45,
						xtype : 'displayfield',
						hideLabel : true,
						style : 'margin-left:5px',
						name : 'menuTempletName'
					}]
				},{
					xtype : 'textfield',
					fieldLabel : '页面名称',
					name : 'menuPageName',
					//allowBlank : false
				},{
					xtype : 'textfield',
					fieldLabel : '菜单路径',
					name : 'menuPath'
						//beforeLabelTextTpl : [ '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>' ],
						//allowBlank : false
				},{
					xtype: 'checkboxfield',
					fieldLabel  : '是否默认首页',
					name : 'isDefault',
					inputValue: 'Y'
				},{
					xtype: 'displayfield',
					fieldLabel  : '默认首页',
					name : 'defaultPage'
				},{
					// 插入同级节点还是子节点,Y:表示同级节点，'N'表示子节点;
					xtype : 'textfield',
					name : 'NodeType',
					hidden : true
				},{
					// 插入同级节点或子节点是在哪个节点上操作的;
					xtype : 'textfield',
					name : 'operateNode',
					hidden : true
				}, {
					// 跟节点;
					xtype : 'textfield',
					name : 'rootNode',
					hidden : true
				}],
				listeners : {
					afterrender : function(thisForm) {
					}
				}
			}/*,{
				xtype: 'grid',
				title: '子菜单列表',
				frame: true,
				columnLines: true,
				multiSelect: true,
				height:350,
				flex: 1,
				reference: 'menuEditGrid',
				style:"margin:0 10px 10px 10px",
				store: {
					type: 'menuStore'
				},
				columns: [{
	        		text: '菜单编号',
					dataIndex: 'menuId',
					hidden: true,
					width: 100
				},{
					text: '菜单名称',
					dataIndex: 'menuName',
					minWidth: 5,
					flex: 1
				},{
					text: '菜单类型',
					dataIndex: 'menuType',
					width: 100
				},{
					text: '菜单循序',
					dataIndex: 'menuXH',
					width: 100
				}],
				bbar: {
					xtype: 'pagingtoolbar',
					pageSize: 5,
					listeners:{
						afterrender: function(pbar){
							var grid = pbar.findParentByType("grid");
							pbar.setStore(grid.store);
						}
					},
					plugins: new Ext.ux.ProgressBarPager()
				}
			}
		]}*/
		];
		this.callParent();
	},
	listeners : {
		afterrender : function(panel) {
			var thisTree = panel.child("treepanel");
			var treeStore = thisTree.getStore();
			var rootNode = treeStore.getRoot();
			thisTree.getSelectionModel().select(rootNode);

			
			//var refs = me.getReferences(),
			//	menuPanel = refs.menuPanel;
			//var form =menuPanel.child("form").getForm();
			var form = panel.child("form").getForm();
			form.setValues({
				menuId : rootNode.data.id,
				menuName : rootNode.data.text,
				menuPath : rootNode.data.menuPath,
				menuState : rootNode.data.menuState,
				menuTempletId : rootNode.data.menuTempletId,
				menuPageName : rootNode.data.menuPageName,
				menuType : rootNode.data.menuType,
				isDefault : rootNode.data.isDefault,
				menuXH : rootNode.data.menuXH,
				defaultPage : rootNode.data.defaultPage,
				NodeType : rootNode.data.NodeType,
				operateNode : rootNode.data.operateNode,
				rootNode : rootNode.data.rootNode,
				siteId : me.siteId,
				menuTempletName : rootNode.data.menuTempletName
			});
			form.findField("menuId").setReadOnly(true);
			form.findField("menuId").addCls('lanage_1'); 

			form.findField("menuPath").setReadOnly(true);
			form.findField("menuPath").addCls('lanage_1');
			form.findField("menuType").setReadOnly(true);
			form.findField("menuType").addCls('lanage_1'); 
			
			// 如果是BOOK类型隐藏menuPageName隐藏是否默认页面，显示路径
			if (rootNode.data.menuType == "B") {
				//form.findField("menuPageName").hide();
				form.findField("isDefault").hide();
				form.findField("menuPath").show();
				form.findField("defaultPage").show();
			} else {
				//form.findField("menuPageName").show();
				form.findField("isDefault").show();
				form.findField("menuPath").hide();
				form.findField("defaultPage").hide();
			}
			
			/*var grid = panel.child("grid");
			//var grid =menuPanel.child("grid");
			var tzStoreParams = '{"menuId":"' + rootNode.data.id + '"}';
			grid.store.tzStoreParams = tzStoreParams;
			grid.store.load(); */
		}
	},
	buttons : [{
		text : '保存',
		iconCls : "save",
		handler : 'onFormSave'
	}, {
		text : '确定',
		iconCls : "ensure",
		handler : 'onFormEnsure'
	}, {
		text : '关闭',
		iconCls : "close",
		handler : 'onFormClose'
	}],
	constructor : function(config) {
		// 机构主菜单ID;
		this.siteId = config.siteId;
		this.callParent();
	}
});
