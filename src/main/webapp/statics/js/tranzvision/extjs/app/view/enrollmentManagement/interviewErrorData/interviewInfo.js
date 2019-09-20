//面试异常数据处理  详细信息
Ext.define('KitchenSink.view.enrollmentManagement.interviewErrorData.interviewInfo', {
	extend: 'Ext.panel.Panel',
	xtype: 'msInfoPanel',
	controller: 'errorDataController',
	actType: 'add',
	requires: ['Ext.data.*', 
	           'Ext.grid.*', 
	           'Ext.util.*',
	           'Ext.toolbar.Paging',
	           'Ext.ux.ProgressBarPager',
	           'Ext.selection.CellModel',
	           'KitchenSink.view.enrollmentManagement.interviewErrorData.interviewInfoStore',
	           'KitchenSink.view.enrollmentManagement.interviewErrorData.interviewErrorDataController'
	           ],
	autoScroll: false,
	reference:"msInfoPanel",
	bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	title: "异常面试信息",
	frame: true,
	listeners: {
		resize: function(panel, width, height, oldWidth, oldHeight, eOpts) {
			var buttonHeight = 42; /*button height plus panel body padding*/
			var formHeight = 30;
			var formPadding = 20;
			var grid = panel.child('grid[name=ycmsInfo]');
			grid.setHeight(height - formHeight - buttonHeight - formPadding);
		}
	},
	initComponent: function() {
		this.cellEditing = new Ext.grid.plugin.CellEditing({
			clicksToEdit: 1
		});
		var store = new KitchenSink.view.enrollmentManagement.interviewErrorData.interviewInfoStore();
		
		Ext.apply(this, {
			items: [{
				xtype: 'form',
				frame: true,
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 100,
					labelStyle: 'font-weight:bold'
				},
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				margin: '8px',
				style: 'border:0px',

				items: [
				{
					xtype: 'textfield',
					name: 'jgid',
					readOnly:true,
					hidden:true
				},
				{
					xtype: 'textfield',
					fieldLabel: "报考方向",
					name: 'className',
					labelWidth: 110,
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
					readOnly:true
				}, {
					xtype: 'textfield',
					fieldLabel: "批次名称",
					name: 'batchName',
					labelWidth: 110,
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true


				},{
					xtype: 'textfield',
					fieldLabel: "班级编号",
					labelWidth: 110,
					name: 'classID',
    				hidden:true

				}, {
					xtype: 'textfield',
					fieldLabel: "批次编号",
					labelWidth: 110,
					name: 'batchID',
    				hidden:true

				}]
			}, {
				xtype: 'grid',
				title: "异常信息列表",
				columnLines: true,
				name: 'ycmsInfo',
				style: "margin:0px",
				selModel: {
					type: 'checkboxmodel'
				},
				viewConfig: {
					enableTextSelection: true
				},
				plugins: [this.cellEditing],
				header: false,
				height: 489,
				reference: 'ycmsGrid',
				frame: true,
				dockedItems: [{
					xtype: "toolbar",
					items: [
						{
							text: "批量删除",
							tooltip:  "批量删除",
							iconCls: "delete",
							handler: 'deleteSelected'
						}]
				}],
				columns: [{
					text: "姓名",
                    dataIndex: 'stuName',
					minwidth: 100,
					align:'center',
					flex:1
				},{
					text: "面试申请号",
                    dataIndex: 'mshID',
					minwidth: 150,
					align:'center',
					flex:1
				},{
					text: "评委组",
                    dataIndex: 'pwGroupName',
					width: 100,
					align:'center'
				},{
					text: "评委",
					dataIndex: 'pwName',
					minwidth: 100,
					align:'center',
					flex:1
				},{
					text: "面试组",
					dataIndex: 'groupName',
					width: 100,
					align:'center'
				},
				{
					text:"面试序号",
					dataIndex: 'msOrder',
					width: 100,
					align:'center'

				}, {
					text:"分组时间",
					dataIndex: 'groupDate',
					align:'center',
					width: 220
				},{
					text: "操作",
					menuDisabled: true,
					sortable: false,
					width: 50,
					align: 'center',
					xtype: 'actioncolumn',
					items: [{iconCls: 'delete',tooltip: "删除",handler:'delErrorData'}]
				}],
				store: store,
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 10,
                    store: store,
                    displayInfo: true,
                    displayMsg:"显示{0}-{1}条，共{2}条",
                    beforePageText:"第",
                    afterPageText:"页/共{0}页",
                    emptyMsg: "没有数据显示",
                    plugins: new Ext.ux.ProgressBarPager()
				}
			}],
			buttons: [
				{
					text: '保存',
					iconCls:"save",
					handler: 'onErrorDataSave'
				},{
					text: '确定',
					iconCls:"ensure",
					handler: 'onErrorDataEnsure'
				}, {
					text: "关闭",
					iconCls: 'close',
					handler: 'onErrorDataClose'
				}
			]
		});
		this.callParent();
	}
});