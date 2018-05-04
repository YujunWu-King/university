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

				}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.ksNum", "报考考生数量"),
					labelWidth: 110,
					name: 'ksNum',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true

				/*}, {
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.reviewClpsKsNum", "材料审批考生"),
					labelWidth: 110,
					name: 'reviewClpsKsNum',
					allowBlank: false,
					fieldStyle:'background:#F4F4F4',
    				readOnly:true */

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
						handler: 'searchMsksList'
					}, "-",{
						text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.add", "新增"),
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.adddata", "新增"),
						iconCls: "add",
						handler: 'onAddMsPsXs'
					}, "-",
			
					{
						text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.delete", "删除"),
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.editdata", "删除"),
						iconCls: "delete",
						handler: 'deleteResSets'
					}, /*"-",
					{
						text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.focuspw", "指定评委"),
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.removedata", "指定评委"),
						iconCls: "people",
						handler: 'setStuPw'
					}, *//*"-",
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
					},*/ '->',
					{
						xtype: 'splitbutton',
						text: '更多操作',
						iconCls: 'list',
						glyph: 61,
						menu: /*[{
							text: '导入面试考生',
							handler: 'importMsStuInfom'
						},{
							text: '导出选中考生评议数据',
							handler: 'exportSelStuInfom'
						},{
							text: '计算选中考生标准成绩',
							handler: 'matchStudenSocre'
						}]
						*/
						   [{
							text: '导入面试考生',
							iconCls: 'excel',
							handler: 'importMsStuInfom'
						},{
							text:Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.exportExcel","导出考生评议数据"),
    						iconCls: 'excel',
    						menu:[{
    								text:Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.exportSelectedExcel","导出选中考生评议数据"),
        							handler:'exportSelectedExcel'
    							},{
    								text:Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.exportSearchExcel","导出查询结果考生评议数据"),
        							handler:'exportSearchExcel'
    							}]
							},{
								text:Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.download","查看历史导出并下载"),
								iconCls:'download',
    							handler:'downloadHisExcel'
							},{
							text: '计算搜索结果中考生标准成绩',
							handler: 'matchStudenSocre'
						     }]
						
						
					}]
				}],
				columns: [{
					//text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.classId", "班级编号"),
					dataIndex: 'classId',
					//width: 100,
					hidden:true
					
				}, {
					//text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.batchIdg", "批次编号"),
					dataIndex: 'batchId',
					width: 100,
					hidden:true
					
				}, {
					//text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.ksOprIdg", "考生编号"),
					dataIndex: 'ksOprId',
					//width: 100,
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
						}  else {
							return "女";
						}
					}
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.judgeGroup", "面试组"),
					dataIndex: 'group_name',
					width: 100
				
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.pcg", "偏差"),
					dataIndex: 'pc',
					width: 80
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.pg", "面试序号"),
					dataIndex: 'order',
					width: 80
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.pc", "安排时间"),
					dataIndex: 'group_date',
					width: 140
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.passStateg", "录取状态"),
					dataIndex: 'passState',
					name:'passState',
					width: 100,
/*					editor: {
						xtype: 'combobox',
						valueField: 'TValue',
                        displayField: 'TSDesc',
                        store: new KitchenSink.view.common.store.appTransStore("TZ_KSLU_ZT")                 
                       
					},*/
					renderer: function(v) {
						//console.log(v);
						if (v == 'LQ') {
							return "录取";
						} else if (v == 'B') {
							return "等候";
						} else  if(v == 'DD'){
							return "待定";
						}else  if(v == 'JJ'){
							return "拒绝";
						}else  if(v == 'DB'){
							return "递补";
						}else{
						    return " ";
						}
					}
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.handle", "操作"),
					menuDisabled: true,
					sortable: false,
					width: 50,
					align: 'center',
					xtype: 'actioncolumn',
					items: [/*{
						iconCls: 'edit',
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.edit", "编辑"),
						handler: this.onAddClick
					},*/ /*{
						iconCls: 'people',
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.edit", "指定评委"),
						handler: 'setStuOnepw'
					},*/ {
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
				handler: 'ensureonRemoveKs',
				iconCls: 'ensure'
			}, {
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_KS_STD.close", "关闭"),
				iconCls: 'close',
				handler: 'closeviewList'
			}]
		});
		this.callParent();
	},
	
	onRemoveClick: function(grid, rowIndex) {
		grid.getStore().removeAt(rowIndex);
	}
});