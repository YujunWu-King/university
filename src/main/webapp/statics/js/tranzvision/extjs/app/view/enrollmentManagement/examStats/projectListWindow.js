Ext.define('KitchenSink.view.enrollmentManagement.examStats.projectListWindow', {
	extend: 'Ext.window.Window',
	reference: 'projectListWindow',
	requires: ['Ext.data.*',
	           'Ext.grid.*',
	           'Ext.util.*',
	           'Ext.toolbar.Paging',
	           'Ext.ux.ProgressBarPager',
	           'KitchenSink.view.enrollmentManagement.examStats.projectListStore'],
	title: Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.classList", "项目列表"),
	width: 650,
	height: 400,
	layout: 'fit',
	resizable: true,
	modal: true,
	multiSel: '',
	statsId: '',
	rowNum: 0,
	items: [{
		xtype: 'grid',
		columnLines: true,
		multiSelect: true,
		viewConfig: {
			enableTextSelection: true
		},
		store: {
			type: 'projectListStore'
		},
		selModel: {
			type: 'checkboxmodel',
			mode: 'MULTI'
		},
		columns: [{
			text: Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.className", "班级名称"),
			dataIndex: 'className',
			width: 400
		},
		{
			text: Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.classStatus", "开通状态"),
			dataIndex: 'classStatus',
			width: 130,
			renderer: function(v) {
				if (v == 'Y') {
					return '开通';
				} else {
					return '未开通';
				}
			},
			flex: 1
		},
		{
			text: Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.classId", "班级编号"),
			width: 130,
			hidden: true,
			dataIndex: 'classId'
		},
		{
			text: Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.orgId", "机构编号"),
			width: 130,
			hidden: true,
			dataIndex: 'orgId'
		}],
		dockedItems: [{
			xtype: "toolbar",
			items: [{
				text: Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.query", "查询"),
				iconCls: "query",
				handler: "cfgSearchClass"
			}]
		}],
		bbar: {
			xtype: 'pagingtoolbar',
			pageSize: 5,
			listeners: {
				afterrender: function(pbar) {
					var grid = pbar.findParentByType("grid");
					pbar.setStore(grid.store);
				}
			},
			reference: 'orgClassToolBar',
			plugins: new Ext.ux.ProgressBarPager()
		}
	}],
	buttons: [{
		text: Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.ensure", "确定"),
		iconCls: "ensure",
		handler: 'onClassChooseEnsure'
	},
	{
		text: Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_JG_CLASSES_STD.close", "关闭"),
		iconCls: "close",
		handler: 'onClassWinClose'
	}]
});