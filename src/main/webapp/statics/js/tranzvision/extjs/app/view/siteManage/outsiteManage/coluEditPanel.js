Ext.define('KitchenSink.view.siteManage.outsiteManage.coluEditPanel',{
	extend : 'Ext.panel.Panel',
	xtype : 'coluEdit',
	controller : 'coluEdit',
	requires : ['Ext.data.*',
	            'Ext.util.*',
	            'KitchenSink.view.siteManage.outsiteManage.coluEditController',
	            'KitchenSink.view.siteManage.outsiteManage.coluTreeStore',
	            'Ext.data.TreeStore' ],
	title : '站点栏目管理',
	width : 640,
	layout : 'border',
	viewModel : true,
	actType : 'update',
	thisColuId : 'NEXT',

	initComponent : function() {
		me = this;
		this.items = [{
			title : '站点栏目树',
			region : 'west',
			xtype : 'treepanel',
			width : 300,
			split : true,
			collapsible : true,
			autoScroll : true,
			lines : true,
			rootVisible : true,
			store : new KitchenSink.view.siteManage.outsiteManage.coluTreeStore({
				siteId : me.siteId
			}),
			listeners : {
				afterrender : function(thisTree) {
				},
				itemclick : function(view, record,item, index, e, eOpts) {
					var form = view.findParentByType("coluEdit").child("form").getForm();
					form.setValues({
						coluId : record.data.id,
						coluName : record.data.text,
						coluPath : record.data.coluPath,
						coluState : record.data.coluState,
						coluTempletId : record.data.coluTempletId,
						contentTypeId : record.data.contentTypeId,
						coluUrl : record.data.coluUrl,
						coluType : record.data.coluType,
						NodeType : record.data.NodeType,
						operateNode : record.data.operateNode,
						rootNode : record.data.rootNode,
						siteId : record.data.siteId,
						coluTempletName : record.data.coluTempletName,
						contentTypeName : record.data.contentTypeName,
						coluAbout : record.data.coluAbout
					});
					form.findField("coluId").setReadOnly(true);
					form.findField("coluId").addCls('lanage_1'); 
					form.findField("coluPath").setReadOnly(true);
					form.findField("coluPath").addCls('lanage_1'); 
					// 如果是跳转类型，那么 模版和类型按钮隐藏
					if (record.data.coluType == "C") {
						form.findField("coluTempletId").hide();
						form.findField("coluTempletName").hide();
						form.findField("contentTypeId").hide();
						form.findField("contentTypeName").hide();
						form.findField("coluUrl").show();
					} else {
						form.findField("coluTempletId").show();
						form.findField("coluTempletName").show();
						form.findField("contentTypeId").show();
						form.findField("contentTypeName").show();
						form.findField("coluUrl").hide();
					}
					view.findParentByType("coluEdit").actType = "update";
				}
			}
		},{
			region : 'center',
			frame : true,
			xtype : 'form',
			reference : 'coluEditForm',
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
					handler : 'inserColuItem'
				},"-",{
					text : "插入子节点",
					iconCls : 'childnode',
					tooltip : "插入子节点",
					handler : 'inserChildColuItem'
				},"-",{
					text : "删除",
					iconCls : 'remove',
					tooltip : "删除",
					handler : 'removeColuItem'
				}]
			},{
				xtype : 'textfield',
				fieldLabel : '栏目编号',
				name : 'coluId',
				//allowBlank : false,
				value : 'NEXT'
			},{
				// 站点ID
				xtype : 'textfield',
				name : 'siteId',
				hidden : true
			},{
				xtype : 'textfield',
				fieldLabel : '栏目名称',
				name : 'coluName',
				beforeLabelTextTpl : [ '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>' ],
				allowBlank : false
			},{
				xtype : 'textfield',
				fieldLabel : '栏目路径',
				name : 'coluPath'
				//beforeLabelTextTpl : [ '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>' ],
				//allowBlank : false
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
				name : 'coluState'
			},{
				xtype : 'combobox',
				fieldLabel : '栏目类型',
				forceSelection : true,
				allowBlank : false,
				valueField : 'TValue',
				displayField : 'TSDesc',
				//allowBlank : false,
				store : new KitchenSink.view.common.store.appTransStore("TZ_ZDLM_LX"),
				typeAhead : true,
				queryMode : 'local',
				name : 'coluType',
				listeners : {
					select : function(combo,record,index){	
						form= combo.findParentByType("form").getForm();
						if (combo.getValue() == "C") {
							form.findField("coluTempletId").hide();
							form.findField("coluTempletName").hide();
							form.findField("contentTypeId").hide();
							form.findField("contentTypeName").hide();
							form.findField("coluUrl").show();
						} else {
							form.findField("coluTempletId").show();
							form.findField("coluTempletName").show();
							form.findField("contentTypeId").show();
							form.findField("contentTypeName").show();
							form.findField("coluUrl").hide();
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
					fieldLabel : '内容模板',
					name : 'coluTempletId',
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
					name : 'coluTempletName'
				}]
			},{
				layout : {
					type : 'column'
				},
				items : [{
					columnWidth : .55,
					xtype : 'textfield',
					fieldLabel : '内容类型',
					name : 'contentTypeId',
					editable : false,
					triggers : {
						clear : {
							cls : 'x-form-clear-trigger',
							handler : 'clearPmtSearchCtype'
						},
						search : {
							cls : 'x-form-search-trigger',
							handler : "pmtSearchCtype"
						}
					}
				},{
					columnWidth : .45,
					xtype : 'displayfield',
					hideLabel : true,
					style : 'margin-left:5px',
					name : 'contentTypeName'
				}]
			},{
				xtype : 'textfield',
				fieldLabel : '外部链接',
				name : 'coluUrl',
				//allowBlank : false
			},{
	            xtype: 'textarea',
	            fieldLabel : '栏目说明',
				name: 'coluAbout',
				height: 95
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
		}];
		this.callParent();
	},
	listeners : {
		afterrender : function(panel) {
			var thisTree = panel.child("treepanel");
			var treeStore = thisTree.getStore();
			var rootNode = treeStore.getRoot();
			thisTree.getSelectionModel().select(rootNode);
			var form = panel.child("form").getForm();
			form.setValues({
				coluId : rootNode.data.id,
				coluName : rootNode.data.text,
				coluPath : rootNode.data.coluPath,
				coluState : rootNode.data.coluState,
				coluTempletId : rootNode.data.coluTempletId,
				contentTypeId : rootNode.data.contentTypeId,
				NodeType : rootNode.data.NodeType,
				coluUrl : rootNode.data.coluUrl,
				coluType : rootNode.data.coluType,
				operateNode : rootNode.data.operateNode,
				rootNode : rootNode.data.rootNode,
				siteId : rootNode.data.siteId,
				coluTempletName : rootNode.data.coluTempletName,
				contentTypeName : rootNode.data.contentTypeName,
				coluAbout : rootNode.data.coluAbout
			});
			form.findField("coluId").setReadOnly(true);
			form.findField("coluId").addCls('lanage_1'); 
			form.findField("coluPath").setReadOnly(true);
			form.findField("coluPath").addCls('lanage_1');
			
			// 如果是跳转类型，那么 模版和类型按钮隐藏
			if (rootNode.data.coluType == "C") {
				form.findField("coluTempletId").hide();
				form.findField("coluTempletName").hide();
				form.findField("contentTypeId").hide();
				form.findField("contentTypeName").hide();
				form.findField("coluUrl").show();
			} else {
				form.findField("coluTempletId").show();
				form.findField("coluTempletName").show();
				form.findField("contentTypeId").show();
				form.findField("contentTypeName").show();
				form.findField("coluUrl").hide();
			}
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
