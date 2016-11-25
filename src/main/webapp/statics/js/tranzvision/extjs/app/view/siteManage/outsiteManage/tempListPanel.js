Ext.define('KitchenSink.view.siteManage.outsiteManage.tempListPanel', {//站点模板管理列表页面
	extend: 'Ext.panel.Panel',
	xtype: 'tempListPanel',
	controller: 'tempController',
	requires: ['Ext.data.*',
	           'Ext.grid.*',
	           'Ext.util.*',
	           'Ext.toolbar.Paging',
	           'Ext.ux.ProgressBarPager',
	           'KitchenSink.view.siteManage.outsiteManage.tempStore',
	           'KitchenSink.view.siteManage.outsiteManage.tempController'
	],
	title: '站点模板管理',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',// 默认新增

	initComponent:function(){
		Ext.apply(this,{
			items: [{
				xtype: 'form',
				reference: 'tempListPanel',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				bodyPadding: 10,
				// heigth: 600,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 110,
					labelStyle: 'font-weight:bold'
				},
				items: [{
					xtype: 'hidden',
					fieldLabel: '站点编号',
					name: 'siteId',
					allowBlank:false
				},{
					xtype: 'displayfield',
					fieldLabel: '站点名称',
					name: 'siteName',
					allowBlank:false
				}]
			},{
				xtype: 'grid',
				title: '站点模板列表',
				frame: true,
				columnLines: true,
				multiSelect: true,
				height:350,
				selModel: {
					type: 'checkboxmodel'
				},
				dockedItems:{
					xtype:"toolbar",
					items:[
					       {text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addOutTemp'},"-",
					       {text:"编辑",tooltip:"编辑数据",iconCls: 'edit',handler:'editSelectField'},"-",
					       {text:"删除",tooltip:"删除数据",iconCls: 'remove',handler:'removeSelectField'}
					]
				},
				reference: 'templateGrid',
				style:"margin:0 10px 10px 10px",
				store: {
					type: 'tempStore'
				},
				columns: [{
	        		text: '站点编号',
					dataIndex: 'siteId',
					hidden: true,
					width: 200
				},{
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
					       {iconCls: 'edit',tooltip: '编辑',handler: 'editField'},
					       '-',
					       {iconCls: 'remove',tooltip: '删除',handler: 'removeField'}
					]
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
			}]
		})
	    this.callParent();
	},
	buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'saveTempList'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'ensureTempList'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'closeTempList'
	}]
});