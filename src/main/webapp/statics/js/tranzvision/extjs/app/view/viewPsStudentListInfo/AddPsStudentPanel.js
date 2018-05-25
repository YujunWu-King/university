Ext.define('KitchenSink.view.viewPsStudentListInfo.AddPsStudentPanel', {
	extend: 'Ext.window.Window',
	xtype: 'addPsStu',
	controller: 'viewxscontrol',
	actType: 'add',
	requires: ['Ext.data.*', 
	'Ext.grid.*',
	'Ext.util.*', 
	'Ext.toolbar.Paging',
	'Ext.ux.ProgressBarPager', 
	'Ext.selection.CellModel', 
	'KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListController',
	'KitchenSink.view.viewPsStudentListInfo.AddPsStudentPanelStore'
	],
	reference: "addpsStuinfo",
	autoScroll: false,
	actType: 'add',
	bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	title: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.addks", "新增考生"),
	frame: true,
	height: 400,
	width:1000,

	initComponent: function() {
		var store=new KitchenSink.view.viewPsStudentListInfo.AddPsStudentPanelStore();
		Ext.apply(this, {

			items: [

			{
				xtype: 'grid',
				columnLines: true,
				style: "margin:0px",
				selModel: {
					type: 'checkboxmodel'
				},

				layout: 'fit',
				minHeight: 300,

				reference: 'afterBzscoreGrid',
				dockedItems: [{
					xtype: "toolbar",
					items: [
						{
						text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.query", "查询"),
						tooltip: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.querydata", "查询数据"),
						handler: 'searchksList',
						iconCls: "query"
					}, "-"
					]
				}],
				columns: [ {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.ksOprIdg", "考生编号"),
					dataIndex: 'ksOprId',
					width: 90,
					hidden:true
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.ksNameg", "考生姓名"),
					dataIndex: 'ksName',
					width: 90
					
				},{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.genderg", "性别"),
					dataIndex: 'gender',
					width: 60,
					renderer:function(v){
					if (v=='M'){
						return "男"
					}else{
						return "女"
					}
					}
					
				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.appInsIdg", "报名表编号"),
					dataIndex: 'appInsId',
					width: 100
					
				},{

					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.mshIdg", "面试申请号"),
					dataIndex: 'mshId',
					width: 100
					
					

				}, {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.classId", "班级编号"),
					dataIndex: 'classId',
					width: 90,
					hidden:true
					
				},{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.batchIdg", "批次编号"),
					dataIndex: 'batchId',
					width: 90,
					hidden:true
					
				},
				{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.className", "班级"),
					dataIndex: 'className',
					width: 240
					
				},{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.batchName", "批次"),
					dataIndex: 'batchName',
					width: 90
					
				},
					
					//{
					//text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.judgesList", "评委组"),
					//dataIndex: 'pwList',
					//width: 150
					
					
				//},
				//	{
				//	text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.MasreState", "评审状态"),
				//	dataIndex: 'reviewStatusDesc',
				//	flex: 1
				//},
				{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.judges", "评委"),
					dataIndex: 'judges',
					width: 90,
					hidden:true
					
				},{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.judgeStatus", "评审状态"),
					dataIndex: 'judgeStatus',
					width: 90,
					hidden:true
					
				}/* {
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.judgeGroupg", "面试评审组"),
					dataIndex: 'judgeGroup',
					width: 110,
					editor: {
						allowBlank: false
					}
				}, *//*{
					text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.passStateg", "录取状态"),
					dataIndex: 'passState',
					width: 90,
					editor: {
						xtype: 'combobox',
						valueField: 'TValue',
						displayField: 'TSDesc',
						store: new KitchenSink.view.common.store.appTransStore("TZ_KSLU_ZT")
					},
					renderer: function(v) {
						if (v == 'A') {
							return "条件录取";
						} else if (v == 'B') {
							return "等候";
						} else {
							return "不录取";
						}
					}
				}*/],
				 store:store,
				    bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 2000,
                    store: store,
                    displayInfo: true,
                    displayMsg:"显示{0}-{1}条，共{2}条",
                    beforePageText:"第",
                    afterPageText:"页/共{0}页",
                    emptyMsg: "没有数据显示",
                    plugins: new Ext.ux.ProgressBarPager()
                }

			}],

			buttons: [{
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.sure", "确定"),
				handler: 'addksSave',
				iconCls: 'ensure'
			},  {
				text: Ext.tzGetResourse("TZ_REVIEW_MS_COM.TZ_MSPS_ADDKS_STD.close", "关闭"),
				iconCls: 'close',
				handler: 'addksClose'
			}]

		});
		this.callParent();
	}

});