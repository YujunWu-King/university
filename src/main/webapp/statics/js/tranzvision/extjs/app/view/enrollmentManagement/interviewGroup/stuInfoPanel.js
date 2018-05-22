//面试现场分组  考生列表 
Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.stuInfoPanel', {
	extend: 'Ext.panel.Panel',
	xtype: 'stuInfoPanel',
	controller: 'appFormInterview',
	requires: ['Ext.data.*', 
	           'Ext.grid.*', 
	           'Ext.util.*',
	           'Ext.toolbar.Paging',
	           'Ext.ux.ProgressBarPager',
	           'Ext.selection.CellModel',
	           'KitchenSink.view.enrollmentManagement.interviewGroup.stuStore',
	           'KitchenSink.view.enrollmentManagement.interviewGroup.interviewController'
	           ],
	autoScroll: false,
	reference:"viewmspsxsList_mspsview",
	bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	title: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.MSPSKSMD", "面试评审考生名单"),
	frame: true,
	listeners: {
		resize: function(panel, width, height, oldWidth, oldHeight, eOpts) {
			var buttonHeight = 42; /*button height plus panel body padding*/
			var formHeight = 30;
			var formPadding = 20;
			var grid = panel.child('grid[name=appseastudentInfo]');
			grid.setHeight(height - formHeight - buttonHeight - formPadding);
		}
	},
	initComponent: function() {
		this.cellEditing = new Ext.grid.plugin.CellEditing({
			clicksToEdit: 1
		});
		var store = new KitchenSink.view.enrollmentManagement.interviewGroup.stuStore();


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


				},{
						xtype: 'textfield',
						name: 'msJxNo',
						hidden:true
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
					//fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.classId", "班级编号"),
					labelWidth: 110,
					name: 'classId',
					//allowBlank: false,
					//fieldStyle:'background:#F4F4F4',
    				//readOnly:true,
    				hidden:true

				}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.batchId", "批次编号"),
					labelWidth: 110,
					name: 'batchId',
					//allowBlank: false,
					//fieldStyle:'background:#F4F4F4',
    				//readOnly:true,
    				hidden:true

				}]
			}, {
				xtype: 'grid',
				title: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.ksmgrid", "考生名单"),
				columnLines: true,
				name: 'appseastudentInfo',
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
				reference: 'mspsksGrid',
				frame: true,
				dockedItems: [{
					xtype: "toolbar",
					items: [
						{
						text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.search", "查询"),
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.search", "查询"),
						iconCls: "query",
						handler: 'queryStudents'
					},{
						text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.PYFZ", "批量分组"),
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.PYFZ", "批量分组"),
						iconCls: "query",
						handler: 'PYFZ'
					}]
				}],
				columns: [{
					text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.OPRID","OPRID"),
                    dataIndex: 'ksOprId',
                    hidden:true
					
				},
				{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.ksNameg", "考生姓名"),
					dataIndex: 'ksName',
					width: 120
					
				}, {

					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.mshIdg", "面试申请号"),
					dataIndex: 'mshId',
					width: 120
					//flex: 1
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.appInsIdg", "报名表编号"),
					dataIndex: 'appInsId',
					width: 120
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.genderg", "性别"),
					dataIndex: 'gender',
					width: 100,
					renderer: function(v) {
						if (v == 'M') {
							return "男";
						}  else if (v == 'F') {
							return "女";
						} else {
							return "";
						}
					}
					
				},{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.pwGroup", "评委组"),
					dataIndex: 'pwgroup_name',
					width: 100
				
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.judgeGroup", "面试组"),
					dataIndex: 'group_name',
					width: 100
				
				}, {
					text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.submtatesd","面试序号"),
					dataIndex: 'order',
					width: 80
					
				},{
					text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_COM.grouptime","分组时间"),
					dataIndex: 'group_date',
					width: 200
					
				},{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.handle", "操作"),
					menuDisabled: true,
					sortable: false,
					width: 50,
					align: 'center',
					xtype: 'actioncolumn',
					items: [{iconCls: 'set',tooltip: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_CLASS_STD.clmspsSets","面试分组"),handler:'openInterviewGroupWindow'}]
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
				handler: 'closeviewListStu'
			}]
		});
		this.callParent();
	},
	
	onRemoveClick: function(grid, rowIndex) {
		grid.getStore().removeAt(rowIndex);
	}
});