/**
 * Created by tzhjl on 2017/1/22.
 */

Ext.define('KitchenSink.view.autoScoringRulerManagement.autoScoringRulerList', {
	extend: 'Ext.grid.Panel',
	requires: ['Ext.data.*', 
	'Ext.grid.*',
	'Ext.util.*', 
	'Ext.toolbar.Paging',
	'Ext.ux.ProgressBarPager', 
	'KitchenSink.view.autoScoringRulerManagement.autoScoringRulerListController',
	'KitchenSink.view.autoScoringRulerManagement.autoScoringRulerListModel',
	'KitchenSink.view.autoScoringRulerManagement.autoScoringRulerListStore'
	],
	xtype: 'zsbfjgMgList',
	title: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.zfbfjgmg", "自动打分规则管理"),
	controller: 'autScoRMgController',
	viewConfig: {
		markDirty: false
	},
	columnLines: true,
	style: "margin:8px",
	selModel: {
		type: 'checkboxmodel'
	},
	multiSelect: false,
	header: false,
	frame: true,
	dockedItems: [{
		xtype: "toolbar",
		items: [{
			text: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.query", "查询"),
			tooltip: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.querydata", "查询数据"),
			iconCls: "query",
			handler: 'searchzsMgList'
		}, "-",
		{
			text: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.add", "新增"),
			tooltip: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.adddata", "新增数据"),
			iconCls: "add",
			handler: 'addResSet'
		}, "-",
		{
			text: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.edit", "编辑"),
			tooltip: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.editdata", "编辑数据"),
			iconCls: "edit",
			handler: 'editResSet'
		}, "-",
		{
			text: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.remove", "删除"),
			tooltip: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.removedata", "删除选中的数据"),
			iconCls: "remove",
			handler: 'deleteResSets'
		}


		]
	}, {
		xtype: "toolbar",
		dock: "bottom",
		ui: "footer",
		items: ['->',
		{
			minWidth: 80,
			text: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.save", "保存"),
			iconCls: "save",
			handler: 'onSaveRemoveData'
		}, {
			minWidth: 80,
			text: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.ensure", "确定"),
			iconCls: "ensure",
			handler: 'ensureonSaveRemoveData'
		}, {
			minWidth: 80,
			text: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.close", "关闭"),
			iconCls: "close",
			handler: 'closeResSets'
		}]
	}],

	initComponent: function() {
		var store = new KitchenSink.view.autoScoringRulerManagement.autoScoringRulerListStore();
		Ext.apply(this, {


			columns: [{
				text: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.zsJGID", "打分规则ID"),
				dataIndex: 'zhjgID',
				width: 200
			},
			 {
				text: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.zhjgName", "打分规则名称"),
				dataIndex: 'zhjgName',
				minWidth: 350,
				flex: 1
			},{
				text: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.zhjgLinkedIn", "打分规则"),
				dataIndex: 'zhjgNameID',
				minWidth: 350,
				flex: 1
			}, {
				menuDisabled: true,
				sortable: false,
				width: 60,
				align: 'center',
				xtype: 'actioncolumn',
				items: [{
					iconCls: 'edit',
					tooltip: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.edit", "编辑"),
					handler: 'editCurrResSet'
				}, {
					iconCls: 'remove',
					tooltip: Ext.tzGetResourse("TZ_ZDCS_DFGZ_COM.TZ_DFGZ_LIST_STD.edit", "删除"),
					handler: 'deleteCurrResSet'
				}]
			}],

			store: store,
			bbar: {
				xtype: 'pagingtoolbar',
				pageSize: 10,
				store: store,
				displayInfo: true,
				displayMsg: "显示{0}-{1}条，共{2}条",
				beforePageText: "第",
				afterPageText: "页/共{0}页",
				emptyMsg: "没有数据显示",
				plugins: new Ext.ux.ProgressBarPager()
			}


		});
		this.callParent();
	}

});