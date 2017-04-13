Ext.define('KitchenSink.view.viewPsStudentListInfo.SetStudenpwWindow', {
	extend: 'Ext.window.Window',
	xtype: 'setStuPw',
	controller: 'viewxscontrol',
	actType: 'add',
	requires: ['Ext.data.*', 
	'Ext.grid.*',
	'Ext.util.*', 
	'Ext.toolbar.Paging',
	'Ext.ux.ProgressBarPager', 
	'Ext.selection.CellModel', 
	'KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListController',
	'KitchenSink.view.viewPsStudentListInfo.SetStudenpwWindowStore',
	
	//'KitchenSink.view.viewPsStudentListInfo.SetStudenpwWindowStore'
	// 'KitchenSink.view.bzScoreMathCalcuter.bzScoreMathDetailStore'
	],
	reference: "setpwwin",
	autoScroll: false,
	actType: 'add',
	bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	title: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_SETPW_STD.setStupw", "指定评委"),
	frame: true,
	height: 400,
	width: 700,

	initComponent: function() {
		var store=new KitchenSink.view.viewPsStudentListInfo.SetStudenpwWindowStore();
		Ext.apply(this, {

			items: [{
				xtype: 'form',
				reference:'cbaform',
				items: [{
					xtype: 'textfield',
					name: 'classId',
					hidden:true

				},{
					xtype: 'textfield',
					name: 'batchId',
					hidden:true

				},{
					xtype: 'textfield',
					name: 'appInsId',
					hidden:true

				}]
			},

			{
				xtype: 'grid',
				columnLines: true,
				style: "margin:0px",
				selModel: {
					type: 'checkboxmodel'
				},

				layout: 'fit',
				minHeight: 300,

				reference: 'setpwksGrid',
				
				columns: [{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_SETPW_STD.classId", "班级编号"),
					dataIndex: 'classId',
					width: 120
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_SETPW_STD.batchIdg", "批次编号"),
					dataIndex: 'batchId',
					width: 120
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_SETPW_STD.judgzhxx", "评委账号"),
					dataIndex: 'judgzhxx',
					width: 120							
				},  {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_SETPW_STD.ksOprIdg", "评委账号"),
					dataIndex: 'judgeID',
					width: 120,
					hidden:true
				},
				{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_SETPW_STD.ksNameg", "评委名称"),
					dataIndex: 'judgeName',
					width: 120
					
				},{

					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_SETPW_STD.judgeGroup", "面试组"),
					dataIndex: 'judgeGroup',
					width: 120,
					flex: 1
					

				}],
				store:store

			}],

			buttons: [{
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_SETPW_STD.save", "保存"),
				handler: 'setpwksSave',
				iconCls: 'save'
			},  {
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_SETPW_STD.close", "关闭"),
				iconCls: 'close',
				handler: 'setpwksClose'
			}]

		});
		this.callParent();
	}

});