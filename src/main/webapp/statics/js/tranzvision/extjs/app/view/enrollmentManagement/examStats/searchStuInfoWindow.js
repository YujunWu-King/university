Ext.define('KitchenSink.view.enrollmentManagement.examStats.searchStuInfoWindow', {
	extend: 'Ext.window.Window',
	reference: 'searchStuInfoWindow',
	requires: ['Ext.data.*',
	           'Ext.grid.*',
	           'Ext.util.*',
	           'Ext.toolbar.Paging',
	           'Ext.ux.ProgressBarPager'],
	title: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00082"),
	/*查询*/
	width: 500,
	minWidth: 650,
	minHeight: 200,
	resizable: true,
	modal: true,
	closeAction: 'destroy',
	ignoreChangesFlag: true,	//让框架程序不要提示用户保存的属性设置
	bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	statsId: '',
	username: '',
	isMulti: '',
	items: [{
		xtype: 'form',
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		ignoreLabelWidth: true,
		bodyStyle: 'overflow-y:auto;overflow-x:hidden',
		fieldDefaults: {
			msgTarget: 'side',
			labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'textfield',
			fieldLabel: '姓名',
			name: 'username',
			value: this.username,
			labelWidth: 150
		},
		{
			xtype: 'combo',
			fieldLabel: Ext.tzGetResourse("TZ_EXAM_COUNT_COM.TZ_EXAM_COUNT_STD.isMulti", "是否报考多项目"),
			name: 'isMulti',
			labelWidth: 150,
			editable: false,
			queryMode: 'local',
			value: this.isMulti,
			valueField: 'TValue',
			displayField: 'TDesc',
			store: {
				fields: ['TValue', 'TDesc'],
				data: [{
					"TValue": "Y",
					"TDesc": "是"
				},
				{
					"TValue": "N",
					"TDesc": "否"
				}]
			}
		}],
		buttonAlign: 'right'
	}],
	buttons: [{
		/*搜索*/
		text: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00086"),
		iconCls: "search",
		handler: "searchCfg"
	},
	{
		/*清除*/
		text: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00087"),
		iconCls: "clean",
		handler: function(btn) {
			//搜索信息表单
			var form = btn.findParentByType("window").child("form").getForm();
			//重置表单
			form.reset();
		}
	},
	{
		/*关闭*/
		text: TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00047"),
		iconCls: "close",
		handler: function(btn) {
			//获取窗口
			var win = btn.findParentByType("window");
			//修改密码信息表单
			var form = win.child("form").getForm();
			//关闭窗口
			win.close();
		}
	}]
});