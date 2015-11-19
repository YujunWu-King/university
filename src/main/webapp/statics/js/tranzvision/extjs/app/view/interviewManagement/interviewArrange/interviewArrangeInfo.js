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
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeModel',
		'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeStore',
		'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeController'
	],
    title: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.panelTitle",'面试日程安排'),
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	reference: 'interviewArrangeInfoPanel',
	listeners:{
		resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
			var buttonHeight = 44;/*button height plus panel body padding*/
			var grid = panel.child('form').child('grid');
			if(grid) grid.setHeight( height -buttonHeight -100);
		}
	},
	initComponent: function (){
		//预约状态
		var msArrStateStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_ARR_STATE");
		msArrStateStore.load();

		//确认状态
		var msConfStateStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_CONF_STA");
		msConfStateStore.load();

		//类别
		var orgColorSortStore = new KitchenSink.view.common.store.comboxStore({
			recname:'TZ_ORG_COLOR_V',
			condition:{
				TZ_JG_ID:{
					value:Ext.tzOrgID,
					operator:'01',
					type:'01'
				}},
			result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
		});
		orgColorSortStore.load();

		Ext.util.CSS.createStyleSheet(" .x-grid-cell.msArrangeArrStateYesStyle {color: #66cc66;}","msArrangeArrStateYesStyle");
		Ext.util.CSS.createStyleSheet(" .x-grid-cell.msArrangeArrStateNoStyle {color: #ff0000;}","msArrangeArrStateNoStyle");
		Ext.util.CSS.createStyleSheet(" .x-grid-cell.msArragneConfigStateYesStyle {color: #66cc66;}","msArragneConfigStateYesStyle");
		Ext.util.CSS.createStyleSheet(" .x-grid-cell.msArragneConfigStateNcStyle {color: #ff0000;}","msArragneConfigStateNcStyle");
		Ext.util.CSS.createStyleSheet(" .x-grid-cell.msArragneConfigStateNoStyle {color: #ffa000;}","msArragneConfigStateNoStyle");

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
				 xtype: 'textfield',
				 fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.batchStartTime",'面试开始时间'),
				 name: 'batchStartTime',
				 hidden:true
				 },{
				 xtype: 'textfield',
				 fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.batchEndTime",'面试结束时间'),
				 name: 'batchEndTime',
				 hidden:true
				 },{
					xtype: 'grid',
					//minHeight: 340,
					frame: true,
					name: 'msjh_grid',
					reference: 'interviewArrangeDetailGrid',
					store: {
						type: 'interviewArrangeStore'
					},

					dockedItems:[{
						xtype:"toolbar",
						items:[
							{text:"自动生成面试安排计划",tooltip:"自动生成面试安排计划",iconCls:"",handler:'SetInterviewTime'},'-',
							{text:"建议时间内自动安排考生",tooltip:"建议时间内自动安排考生",iconCls:"",handler:'msJYSJAutoArrStus'},'-',
							{text:"发送面试时间确认邮件",tooltip:"发送面试时间确认邮件",iconCls:"email",handler:'sendInterviewArrConfirmEmail'},'-',
							{text:"设置参与本批次面试的考生",tooltip:"设置参与本批次面试的考生",iconCls:"set",handler:'setInterviewApplicant'},'->',
							{
								xtype:'splitbutton',
								text:'更多操作',
								iconCls:  'list',
								glyph: 61,
								menu:[
									{
										text:'批量清除考生安排',
										iconCls:"remove",
										handler:'ms_cleanAp'
									},{
										text:'清除选中时间安排',
										iconCls:"remove",
										handler:'ms_cleanTimeArr'
									},{
										text:'清除所有时间安排',
										iconCls:"reset",
										handler:'ms_cleanAllTimeArr'
									},{
										text:'考生安排情况一览表',
										iconCls:"preview",
										handler:'ms_msArrPreview'
									},{
										text:'面试建议时间定义',
										iconCls:"set",
										handler:'ms_jytime'
									},{
										text:"发布",
										iconCls:"publish",
										handler:'releaseSelList'},
									{
										text:"撤销发布",
										iconCls:"revoke",
										handler:'UndoSelList'}
								]
							}
						]
					}],
					plugins: [{
						ptype:'rowexpander',
						rowBodyTpl : new Ext.XTemplate(
							'<div class="x-grid-group-title" style="margin-left:30px">',
							'<table class="x-grid3-row-table" cellspacing="0" cellpadding="0" border="0" >',
							'<tpl for="moreInfo">',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">所在城市 ：</td><td style="font-weight: normal;max-width:800px;">{city}</td>',
							'</tr>',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">所在国家 ：</td><td style="font-weight: normal;max-width:800px;">{country}</td>',
							'</tr>',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">所属时区 ：</td><td style="font-weight: normal;max-width:800px;">{timezone}</td>',
							'</tr>',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">时差（同北京） ：</td><td style="font-weight: normal;max-width:800px;">{timezoneDiff}</td>',
							'</tr>',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">当地开始日期 ：</td><td style="font-weight: normal;max-width:800px;">{localStartDate}</td>',
							'</tr>',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">当地结束日期 ：</td><td style="font-weight: normal;max-width:800px;">{localFinishDate}</td>',
							'</tr>',
							'<tr style="line-height:30px;">',
							'<td  class="x-grid3-hd x-grid3-cell x-grid3-td-11" style="padding-right: 20px;">邮箱 ：</td><td style="font-weight: normal;max-width:800px;">{lxEmail}</td>',
							'</tr>',
							'</tpl>',
							'</table>',
							'</div>',{}),
						lazyRender : true,
						enableCaching : false
					},{
						ptype: 'cellediting',
						pluginId: 'msArrCellEditingPlugin',
						clicksToEdit: 1
					}],
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
						editor:{
							xtype:"datefield",
							format:"Y-m-d"
						},
						width: 105
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msGroupId", '组号'),
						sortable: true,
						dataIndex: 'msGroupId',
						editor:{
							xtype:'textfield',
							allowBlank:false
						},
						width: 60
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msGroupSn",  '序号'),
						sortable: true,
						dataIndex: 'msGroupSn',
						width: 72
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.bjMsStartTime",'开始时间'),
						xtype:'datecolumn',
						format:'H:i',
						sortable: true,
						dataIndex: 'bjMsStartTime',
						editor:{
							xtype: 'timefield',
							increment:5,
							editable:false,
							allowBlank: false,
							format:'H:i'
						},
						width: 90
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.bjMsEndTime", '结束时间'),
						sortable: true,
						dataIndex: 'bjMsEndTime',
						xtype:'datecolumn',
						format:'H:i',
						editor:{
							xtype: 'timefield',
							increment:5,
							editable:false,
							allowBlank: false,
							format:'H:i'
						},
						width: 90
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msXxBz", '备注'),
						dataIndex: 'msXxBz',
						editor:{
							xtype:'textfield'
						},
						width: 100
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.localStartTime",'当地开始时间'),
						dataIndex: 'localStartTime',
						width: 125
					},{
						text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.localFinishTime", '当地结束时间'),
						dataIndex: 'localFinishTime',
						width: 125
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.skypeId",  'Skype账号'),
						dataIndex: 'skypeId',
						width: 100
					},{
					 menuDisabled: true,
					 sortable: false,
					 width:40,
					 header:' ',
					 xtype: 'actioncolumn',
					 dataIndex: 'transferSkype',
					 items:[
					 {	iconCls: 'skype',tooltip: 'Skype',handler:'transferSkype'}
					 ]
					 },{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msOprName",  '姓名'),
						dataIndex: 'msOprName',
						width: 100
					},{
						menuDisabled: true,
						sortable: false,
						xtype:'actioncolumn',
						width: 35,
						header:' ',
						items:[
							{iconCls: 'query',tooltip: '搜索',handler:'selInterviwStu'}
						]
					},{
						menuDisabled: true,
						sortable: false,
						width:40,
						header:'清除',
						items:[	],
						xtype: 'actioncolumn',
						dataIndex: 'removeMsArrInfo',
						renderer:function(){
							return "<input class='msArrGridClearBtn' type='button' value='清除'/>";
						}
					},{
						text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msOrderState", '预约状态'),
						sortable: true,
						dataIndex: 'msOrderState',
						minWidth: 100,
						renderer : function(value, metadata, record) {
							if (value=="B"){
								metadata.tdCls = 'msArrangeArrStateYesStyle';
							}else{
								metadata.tdCls = 'msArrangeArrStateNoStyle';
							}
							//alert("render"+value);
							var index = msArrStateStore.find('TValue',value);
							if(index!=-1){
								return msArrStateStore.getAt(index).data.TSDesc;
							}
							return record.get('msZGFlag');
						}
					},{
						text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.msConfirmState", '确认状态'),
						sortable: true,
						dataIndex: 'msConfirmState',
						minWidth: 100,
						editor:{
							xtype: 'combobox',
							//emptyText:"请选择",
							name: 'msConfirmStateCb',
							queryMode: 'remote',
							valueField: 'TValue',
							displayField: 'TSDesc',
							editable: false,
							store:msConfStateStore
							//allowBlank:false
						},
						renderer : function(value, metadata, record) {
							if (value=="C"){
								metadata.tdCls = 'msArragneConfigStateYesStyle';
							}else if (value=="NA"){
								metadata.tdCls = 'msArragneConfigStateNcStyle';
							}else{
								metadata.tdCls = 'msArragneConfigStateNoStyle';
							}
							//alert("render"+value);
							var index = msConfStateStore.find('TValue',value);
							if(index!=-1){
								return msConfStateStore.getAt(index).data.TSDesc;
							}
							return record.get('msZGFlag');
						}
					},{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.sort", "类别") ,
						sortable: true,
						dataIndex: 'sort',
						minWidth: 140,
						flex:1,
						renderer:function(v){
							if(v){
								var rec = orgColorSortStore.find('TZ_COLOR_SORT_ID',v,0,true,true,false);
								if(rec>-1){
									return "<div  class='x-colorpicker-field-swatch-inner' style='width:30px;height:50%;background-color: #"+orgColorSortStore.getAt(rec).get("TZ_COLOR_CODE")+"'></div><div style='margin-left:40px;'>"+orgColorSortStore.getAt(rec).get("TZ_COLOR_NAME")+"</div>";
								}else{
									return "";
								}
							}
						},
						editor: {
							xtype: 'combo',
							queryMode:'local',
							valueField: 'TZ_COLOR_SORT_ID',
							displayField: 'TZ_COLOR_NAME',
							triggerAction: 'all',
							editable : false,
							triggers:{
								clear: {
									cls: 'x-form-clear-trigger',
									handler: function(field){
										field.setValue("");
									}
								}
							},
							store:orgColorSortStore,
							tpl: Ext.create('Ext.XTemplate',
								'<tpl for=".">',
								'<div class="x-boundlist-item"><div class="x-colorpicker-field-swatch-inner" style="margin-top:6px;width:30px;height:50%;background-color: #{TZ_COLOR_CODE}"></div><div style="margin-left:40px;display: block;  overflow:  hidden; white-space: nowrap; -o-text-overflow: ellipsis; text-overflow:  ellipsis;"> {TZ_COLOR_NAME}</div></div>',
								'</tpl>'
							),
							displayTpl: Ext.create('Ext.XTemplate',
								'<tpl for=".">',
								'{TZ_COLOR_NAME}',
								'</tpl>'
							),
							listeners: {
								focus: function (combo,event, eOpts) {
									var selList = this.findParentByType("grid").getView().getSelectionModel().getSelection();
									var colorSortID =selList[0].raw.sort;
									if(colorSortID.length<=0){
										combo.setValue("");
									}
								}
							}
						}
					},{
						xtype: 'actioncolumn',
						header:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.releaseOrUndo","发布/撤销") ,
						//sortable: false,
						minWidth:100,
						width:100,
						items:[
							{
								iconCls: '',
								tooltip: '',
								handler:'releaseOrUndo',
								getClass: function(v, metadata , r,rowIndex ,colIndex ,store ){
									if (store.getAt(rowIndex).get("msOprId")=='') {
										return '';
									}else{
										if(store.getAt(rowIndex).get("releaseOrUndo")=='Y'){
											metadata['tdAttr'] = "data-qtip=撤销";
											return 'revoke';
										}else{
											metadata['tdAttr'] = "data-qtip=发布";
											return 'publish';
										}
									}
								}
							}
						]
					},{
						menuDisabled: true,
						sortable: false,
						header:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_CAL_ARR_STD.option", '操作'),
						width:40,
						xtype: 'actioncolumn',
						items:[
							{	iconCls: 'add',tooltip: '添加',handler:'addMsCalRow'},
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
						displayMsg: '显示{0}-{1}条，共{2}条',
						beforePageText: '第',
						afterPageText: '页/共{0}页',
						emptyMsg: '没有数据显示',
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
