Ext.define('KitchenSink.view.viewPsStudentListInfo.ViewPsStudentList', {
	extend: 'Ext.panel.Panel',
	xtype: 'viewmspsxsList',
	controller: 'viewxscontrol',
	actType: 'add',
	requires: ['Ext.data.*', 
	           'Ext.grid.*', 
	           'Ext.util.*',
	           'Ext.toolbar.Paging',
	           'Ext.ux.ProgressBarPager',
	           'KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListController',
	           'Ext.selection.CellModel',
	           'KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListModel', 
	           'KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListStore'
	           ],
	autoScroll: false,
	actType: 'add',
	bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	title: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.MSPSKSMD", "面试评审考生名单"),
	frame: true,
	listeners: {
		resize: function(panel, width, height, oldWidth, oldHeight, eOpts) {
			var buttonHeight = 42; /*button height plus panel body padding*/
			var formHeight = 30;
			var formPadding = 20;
			var grid = panel.child('grid[name=appFormApplicants]');
			grid.setHeight(height - formHeight - buttonHeight - formPadding);
		}
	},
	initComponent: function() {
		this.cellEditing = new Ext.grid.plugin.CellEditing({
			clicksToEdit: 1
		});
		var store = new KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListStore();


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
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.classIdname", "报考班级"),
					name: 'className',
					labelWidth: 110,
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true
					//value: '105'

				}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_RULE_STD.batchIdname", "批次"),
					name: 'batchName',
					labelWidth: 110,
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true
					//value: '2'

				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.classId", "班级编号"),
					labelWidth: 110,
					name: 'classId',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true,
    				hidden:true

				}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.batchId", "批次编号"),
					labelWidth: 110,
					name: 'batchId',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true,
    				hidden:true

				}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.ksNum", "报考考生数量"),
					labelWidth: 110,
					name: 'ksNum',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true

				}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.reviewClpsKsNum", "材料审批考生"),
					labelWidth: 110,
					name: 'reviewClpsKsNum',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true

				}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.reviewKsNum", "参与面试考生"),
					labelWidth: 110,
					name: 'reviewKsNum',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true

				}]
			}, {
				xtype: 'grid',
				title: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.ksmgrid", "考生名单"),
				columnLines: true,
				name: 'appFormApplicants',
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
					items: [{
						text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.add", "新增"),
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.adddata", "新增"),
						iconCls: "add",
						handler: 'onAddMsPsXs'
					}, "-",
					/*{
						text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.add", "新增"),
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.adddata", "新增"),
						iconCls: "add",
						handler: this.onAddClick
					}, "-",*/
					{
						text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.delete", "删除"),
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.editdata", "删除"),
						iconCls: "delete",
						handler: 'deleteResSets'
					}, "-",
					{
						text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.focuspw", "指定评委"),
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.removedata", "指定评委"),
						iconCls: "people",
						handler: 'setStuPw'
					}, "-",
					{
						xtype: 'splitbutton',
						text: '批量设置录取状态',
						iconCls: 'set',
						menu: [{
							text: '条件录取',
						    handler: 'setluztequleA'
							
						}, {
							text: '等候',							
							handler: 'setluztequleB'
						}, {
							text: '不录取',
							handler: 'setluztequleC'
						}]
					}, '->',
					{
						xtype: 'splitbutton',
						text: '更多操作',
						iconCls: 'list',
						glyph: 61,
						menu: [/*{
							text: '导出选中考生评议数据',
							handler: 'exportSelStuInfom'
						},*/{
							text: '导出选中考生评议数据',
							handler: 'exportSelStuInfom'
						},{
							text: '计算选中考生标准成绩',
							handler: 'matchStudenSocre'
						}]
					}]
				}],
				columns: [{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.classId", "班级编号"),
					dataIndex: 'classId',
					width: 100
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.batchIdg", "批次编号"),
					dataIndex: 'batchId',
					width: 100
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.ksOprIdg", "考生编号"),
					dataIndex: 'ksOprId',
					width: 100
					
				}, 
				{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.ksNameg", "考生姓名"),
					dataIndex: 'ksName',
					width: 100
					
				}, {

					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.mshIdg", "面试申请号"),
					dataIndex: 'mshId',
					width: 110
					
					

				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.appInsIdg", "报名表编号"),
					dataIndex: 'appInsId',
					width: 110
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.genderg", "性别"),
					dataIndex: 'gender',
					width: 60,
					renderer: function(v) {
						if (v == 'M') {
							return "男";
						}  else {
							return "女";
						}
					}
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.judgeGroupg", "面试评审组"),
					dataIndex: 'judgeGroup',
					width: 110
				
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.pcg", "偏差"),
					dataIndex: 'pc',
					width: 60
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.passStateg", "录取状态"),
					dataIndex: 'passState',
					name:'passState',
					width: 100,
					editor: {
						xtype: 'combobox',
						valueField: 'TValue',
                        displayField: 'TSDesc',
                        store: new KitchenSink.view.common.store.appTransStore("TZ_KSLU_ZT"),
                        checkChange: function() {
                        	
                            var me = this,
                            newVal, oldVal;
                            if (!me.suspendCheckChange && !me.destroying && !me.destroyed) {
                            newVal = me.getValue();
                            oldVal = me.lastValue;
                
                          if (me.didValueChange(newVal, oldVal)) {
                             me.lastValue = newVal;
                             
                             me.fireEvent('change', me, newVal, oldVal);
                             me.onChange(newVal, oldVal);
                             var selList = me.findParentByType('grid').getSelectionModel().getSelection();
                             console.log("newVal:"+newVal+"oldVal:"+oldVal+"me.selList;"+ Ext.JSON.encode(selList[0].data));
                            }
                           }
                            
                        }
                       
					},
					renderer: function(v) {
						console.log(v);
						if (v == 'A') {
							return "条件录取";
						} else if (v == 'B') {
							return "等候";
						} else {
							return "不录取";
						}
					}
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.handle", "操作"),
					menuDisabled: true,
					sortable: false,
					width: 110,
					align: 'center',
					xtype: 'actioncolumn',
					flex: 1,
					items: [/*{
						iconCls: 'edit',
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.edit", "编辑"),
						handler: this.onAddClick
					},*/ {
						iconCls: 'people',
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.edit", "指定评委"),
						handler: 'setStuOnepw'
					}, {
						iconCls: 'remove',
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.delete", "删除"),
						handler: 'deleteCurrResSet'
					}]
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
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.save", "保存"),
				handler: 'onSaveRemoveKs',
				iconCls: 'save'
			}, {
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.sure", "确定"),
				handler: 'ensureonschoolSave',
				iconCls: 'ensure'
			}, {
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.close", "关闭"),
				iconCls: 'close',
				handler: 'closeviewList'
			}]
		});
		this.callParent();
	},
	onAddClick: function(btn) {
		// Create a model instance
		var rec = new KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListModel({
			classId: '',
			batchId: '',
			ksOprId: null,
			ksName: '',
			mshId: '',
			appInsId: ''
		});

		hbfsAllRecs = btn.findParentByType("grid[reference=mspsksGrid]").store.getRange();
		btn.findParentByType("grid[reference=mspsksGrid]").getStore().insert(hbfsAllRecs.length, rec);
		// this.cellEditing.startEdit(rec, 0);
	},
	onRemoveClick: function(grid, rowIndex) {
		grid.getStore().removeAt(rowIndex);
	}
});