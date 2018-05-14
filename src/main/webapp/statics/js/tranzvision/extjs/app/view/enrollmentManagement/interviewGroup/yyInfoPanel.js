//面试现场分组  预约列表 
Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.yyInfoPanel', {
	extend: 'Ext.panel.Panel',
	xtype: 'yyInfoPanel',
	controller: 'appFormInterview',
	actType: 'add',
	requires: ['Ext.data.*', 
	           'Ext.grid.*', 
	           'Ext.util.*',
	           'Ext.toolbar.Paging',
	           'Ext.ux.ProgressBarPager',
	           'Ext.selection.CellModel',
	           'KitchenSink.view.enrollmentManagement.interviewGroup.yyStore',
	           'KitchenSink.view.enrollmentManagement.interviewGroup.interviewController'
	           ],
	autoScroll: false,
	reference:"yyInfoPanel",
	bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	title: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.YYFZ", "预约分组"),
	frame: true,
	listeners: {
		resize: function(panel, width, height, oldWidth, oldHeight, eOpts) {
			var buttonHeight = 42; /*button height plus panel body padding*/
			var formHeight = 30;
			var formPadding = 20;
			var grid = panel.child('grid[name=yyInfo]');
			grid.setHeight(height - formHeight - buttonHeight - formPadding);
		}
	},
	initComponent: function() {
		this.cellEditing = new Ext.grid.plugin.CellEditing({
			clicksToEdit: 1
		});
		var store = new KitchenSink.view.enrollmentManagement.interviewGroup.yyStore();
		//console.log(store);
		
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
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.classIdname", "报考班级"),
					name: 'className',
					labelWidth: 110,
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true


				}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.batchIdname", "批次"),
					name: 'batchName',
					labelWidth: 110,
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true


				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.classId", "班级编号"),
					labelWidth: 110,
					name: 'classId',
    				hidden:true

				}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.batchId", "批次编号"),
					labelWidth: 110,
					name: 'batchId',
    				hidden:true

				}]
			}, {
				xtype: 'grid',
				title: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.yygrid", "预约列表"),
				columnLines: true,
				name: 'yyInfo',
				style: "margin:0px",
				selModel: {
					type: 'checkboxmodel'
				},
				viewConfig: {
					enableTextSelection: true
				},
				plugins: [this.cellEditing],
				header: false,

				// layout:'fit',
				height: 489,
				reference: 'yyGrid',
				frame: true,
				/*dockedItems: [{
					xtype: "toolbar",
					items: [
						{
						text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.search", "查询"),
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.search", "查询"),
						iconCls: "query",
						handler: 'queryStudents'
					}]
				}], */
				columns: [{
					text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.TZ_MS_PLAN_SEQ","面试日程安排计划编号"),
                    dataIndex: 'msJxNo',
                    hidden:true
					
				},{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.classId", "班级编号"),
                    dataIndex: 'classID',
                    hidden:true
					
				},{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.batchId", "批次编号"),
                    dataIndex: 'batchID',
                    hidden:true
					
				},{
					text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msDate", '面试日期'),
					xtype:'datecolumn',
					format:'Y-m-d',
					sortable: true,
					dataIndex: 'msDate',
					width: 100
				},
				{
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.maxPerson", '最多预约人数'),
					sortable: true,
					dataIndex: 'maxPerson',
					width: 110,
					align:'center'
					
				}, {
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.appoPerson", '已预约人数'),
					sortable: true,
					dataIndex: 'appoPerson',
					width: 100,
					align:'center'
				}, {
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.bjMsStartTime",'开始时间'),
					//xtype:'datecolumn',
					//format:'H:i',
					sortable: true,
					dataIndex: 'bjMsStartTime',
					width: 90
				}, {
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.bjMsEndTime", '结束时间'),
					sortable: true,
					dataIndex: 'bjMsEndTime',
					//xtype:'datecolumn',
					//format:'H:i',
					width: 90
					
				},{
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msLocation", '面试地点'),
					dataIndex: 'msLocation',
					minWidth: 120,
					width: 120,
					flex: 1
				
				}, {
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msXxBz", '备注'),
					dataIndex: 'msXxBz',
					minWidth: 120,
					width: 120,
					flex: 1
				},{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.handle", "操作"),
					menuDisabled: true,
					sortable: false,
					width: 50,
					align: 'center',
					xtype: 'actioncolumn',
					items: [{iconCls: 'set',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.LOOK","查看"),handler:'openStu'}]
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
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.close", "关闭"),
				iconCls: 'close',
				handler: 'closeviewList'
			}]
		});
		this.callParent();
	}
});