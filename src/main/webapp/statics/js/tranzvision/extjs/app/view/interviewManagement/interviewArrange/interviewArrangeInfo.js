Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'interviewArrangeInfo', 
	controller: 'interviewArrangeController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewAudienceStore',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeModel',
		'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeStore',
		'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeController'
	],
    title: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.panelTitle",'面试日程安排'),
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	reference: 'interviewArrangeInfoPanel',
	listeners:{
		resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
			var buttonHeight = 36;/*button height plus panel body padding*/
			var grid = panel.child('form').child('grid');
			if(grid) grid.setHeight( height -buttonHeight -100);
		}
	},
	initComponent: function (){
		//开启状态
		var msOpenStateStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_OPEN_STATE");
		msOpenStateStore.load();

		Ext.apply(this,{
			items: [{
				xtype: 'form',
				reference: 'interviewArrangeForm',
				layout: {
					type: 'vbox',       // Arrange child items vertically
					align: 'stretch'    // 控件横向拉伸至容器大小
				},
				border: false,
				bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',

				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 120,
					labelStyle: 'font-weight:bold'
				},
				items: [{
					xtype: 'textfield',
					fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.classID"," 报考班级ID"),
					name: 'classID',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.className",'报考班级') ,
					name: 'className',
					fieldStyle:'background:#F4F4F4',
					readOnly:true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.batchID", '面试批次ID'),
					name: 'batchID',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.batchName", '面试批次'),
					name: 'batchName',
					fieldStyle:'background:#F4F4F4',
					readOnly:true
				},{
					xtype: 'textfield',
					fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.clearAllTimeArr",'清除所有时间安排'),
					name: 'clearAllTimeArr',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.batchStartDate", '面试开始日期'),
					name: 'batchStartDate',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.batchEndDate", '面试结束日期'),
					name: 'batchEndDate',
					hidden:true
				},{
					layout: {
						type: 'column'
					},
					bodyStyle:'padding:0 0 10px 0',
					items: [{
						columnWidth: .5,
						xtype: 'datefield',
			            fieldLabel: '预约开放日期',
						format: 'Y-m-d',
						name: 'openDate',
						style:'margin-right:20px'
					},{
						columnWidth: .5,
						xtype: 'timefield',
			            fieldLabel: '预约开放时间',
						increment:5,
						editable:false,
						format:'H:i',
						name: 'openTime',
						style:'margin-left:20px'
					}]
				},{
					layout: {
						type: 'column'
					},
					bodyStyle:'padding:0 0 10px 0',
					items: [{
						columnWidth: .5,
						xtype: 'datefield',
			            fieldLabel: '预约关闭日期',
						format: 'Y-m-d',
						name: 'closeDate',
						style:'margin-right:20px'
					},{
						columnWidth: .5,
						xtype: 'timefield',
			            fieldLabel: '预约关闭时间',
						increment:5,
						editable:false,
						format:'H:i',
						name: 'closeTime',
						style:'margin-left:20px'
					}]
				},{
					layout: {
						type: 'column'
					},
					bodyStyle:'padding:0 0 10px 0',
					items: [{
						columnWidth: .5,
						xtype: 'combo',
			            fieldLabel: '状态',
						name: 'openStatus',
						valueField: 'TValue',
						displayField: 'TLDesc',
						queryMode: 'local',
						store: msOpenStateStore,
						style:'margin-right:20px'
					},{
						columnWidth: .5,
						xtype: 'checkbox',
			            fieldLabel: '前台显示',
						name: 'frontView',
						boxLabel: '预约时间结束后，仍显示在学生前台',
						inputValue: 'Y',
						style:'margin-left:20px'
					}]
				},{
					xtype: 'ueditor',
		            fieldLabel: '说明',
		            height: 200,
		            zIndex: 900,
					name: 'descr'
				},{
					xtype: 'grid',
					frame: true,
					name: 'msjh_grid',
					reference: 'interviewArrangeDetailGrid',
					store: {
						type: 'interviewArrangeStore'
					},

					dockedItems:[{
						xtype:"toolbar",
						items:[
							/*{text:"自动生成面试安排计划",tooltip:"自动生成面试安排计划",iconCls:"",handler:'SetInterviewTime'},'-',*/
							{text:"新增面试安排",tooltip:"新增面试安排",iconCls:"add",handler:'addInterviewTime'},'-',
							{text:"设置参与本批次面试的考生",tooltip:"设置参与本批次面试的考生",iconCls:"set",handler:'setInterviewApplicant'},'-',
							{text:"查看预约考生",tooltip:"查看预约考生",iconCls:"view",handler:'viewArrangeStuList'},'->',
							{
								xtype:'splitbutton',
								text:'更多操作',
								iconCls:  'list',
								glyph: 61,
								menu:[{
										text:'清除选中时间安排',
										iconCls:"remove",
										handler:'ms_cleanTimeArr'
									},{
										text:'清除所有时间安排',
										iconCls:"reset",
										handler:'ms_cleanAllTimeArr'
									},{
										text:"发布选中记录",
										iconCls:"publish",
										handler:'releaseSelList'
									},{
										text:"撤销选中记录",
										iconCls:"revoke",
										handler:'UndoSelList'
									},{
										text:"导出选中记录到Excel",
										iconCls:"excel",
										handler:'exportToExcel'
									}]
							}
						]
					}],
					/*
					plugins: [{
						ptype: 'cellediting',
						pluginId: 'msArrCellEditingPlugin',
						clicksToEdit: 1
					}],
					*/
					columnLines: true,    //显示纵向表格线
					selModel:{
						type: 'checkboxmodel'
					},
					columns: [{
						text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msDate", '面试日期'),
						xtype:'datecolumn',
						format:'Y-m-d',
						sortable: true,
						dataIndex: 'msDate',
						/*
						editor:{
							xtype:"datefield",
							format:"Y-m-d"
						},*/
						width: 120
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.maxPerson", '最多预约人数'),
						sortable: true,
						dataIndex: 'maxPerson',
						/*
						editor:{
							xtype:'numberfield',
							allowBlank:false,
							minValue: 1
						},*/
						width: 120
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.bjMsStartTime",'开始时间'),
						xtype:'datecolumn',
						format:'H:i',
						sortable: true,
						dataIndex: 'bjMsStartTime',
						/*
						editor:{
							xtype: 'timefield',
							increment:5,
							editable:false,
							allowBlank: false,
							format:'H:i'
						},*/
						width: 100
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.bjMsEndTime", '结束时间'),
						sortable: true,
						dataIndex: 'bjMsEndTime',
						xtype:'datecolumn',
						format:'H:i',
						/*
						editor:{
							xtype: 'timefield',
							increment:5,
							editable:false,
							allowBlank: false,
							format:'H:i'
						},*/
						width: 100
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msLocation", '面试地点'),
						dataIndex: 'msLocation',
						/*
						editor:{
							xtype:'textfield'
						},*/
						minWidth: 120,
						width: 120,
						flex: 1
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msXxBz", '备注'),
						dataIndex: 'msXxBz',
						/*
						editor:{
							xtype:'textfield'
						},*/
						minWidth: 120,
						width: 120,
						flex: 1
					},{
						xtype: 'actioncolumn',
						header:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.releaseOrUndo","发布/撤销") ,
						minWidth:100,
						width:100,
						items:[
							{
								iconCls: '',
								tooltip: '',
								handler:'releaseOrUndo',
								getClass: function(v, metadata , r,rowIndex ,colIndex ,store ){
									if(store.getAt(rowIndex).get("releaseOrUndo")=='Y'){
										metadata['tdAttr'] = "data-qtip=撤销";
										return 'revoke';
									}else{
										metadata['tdAttr'] = "data-qtip=发布";
										return 'publish';
									}
								}
							}
						]
					},{
						menuDisabled: true,
						sortable: false,
						header:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.option", '操作'),
						width:60,
						xtype: 'actioncolumn',
						items:[
							/*{	iconCls: 'add',tooltip: '添加',handler:'addMsCalRow'},*/
							{	iconCls: 'edit',tooltip: '编辑',handler:'editMsCalRow'},
							{	iconCls: 'remove',tooltip: '删除',handler:'deleteMsCalRow'}
						]
					}],
					bbar: {
						xtype: 'pagingtoolbar',
						pageSize: 10,
						listeners:{
							afterrender: function(pbar){
								var grid = pbar.findParentByType("grid");
								pbar.setStore(grid.store);
							}
						},
						displayInfo: true,
						plugins: new Ext.ux.ProgressBarPager()
					}
				}]
			}]
		});
		this.callParent();
	},

    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
