//查看预约考生
Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewList', {
    extend: 'Ext.panel.Panel',
    xtype: 'interviewStuViewList', 
	controller: 'interviewStuViewController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewModel',
		'KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewStore',
		'KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewController'
	],
    title: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.panelTitle",'查看预约考生'),
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	reference: 'interviewStuView',
	listeners:{
		resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
			var buttonHeight = 44;/*button height plus panel body padding*/
			var grid = panel.child('form').child('grid');
			if(grid) grid.setHeight( height -buttonHeight -100);
		}
	},
	initComponent: function (){
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
		
		Ext.apply(this,{
			items: [{
				xtype: 'form',
				reference: 'interviewStuViewForm',
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
					name: 'classID',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.className",'报考班级') ,
					name: 'className',
					fieldStyle:'background:#F4F4F4',
					readOnly:true
				},{
					xtype: 'textfield',
					name: 'batchID',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.batchName", '面试批次'),
					name: 'batchName',
					fieldStyle:'background:#F4F4F4',
					readOnly:true
				},{
					xtype: 'grid',
					frame: true,
					name: 'interviewAppoStuGrid',
					store: {
						type: 'interviewStuViewStore'
					},
					features: [{
				        ftype: 'grouping',
				        groupHeaderTpl: Ext.create('Ext.XTemplate',
			        	    '{columnName}: ',
			        	    '{children:this.formatMsDatetime}',
			        	    ' ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
			        	    {
				        		formatMsDatetime: function(children) {
				        			var msDate = children[0].data["msDate"]
				        			var msStartTime = children[0].data["msStartTime"]
				        			var msEndTime = children[0].data["msEndTime"]
				        			
				        			var dateStr = msDate+" "+msStartTime+" 至 "+msDate+" "+msEndTime;
			        	            return Ext.String.trim(dateStr);
			        	        }
			        	    }
			        	),
				        enableGroupingMenu: false,
				        hideGroupedHeader: true, 
				        startCollapsed: false,
				        id: 'moduleGrouping'
				    }],
					dockedItems:[{
						xtype:"toolbar",
						items:[
							{text:"新增",tooltip:"新增",iconCls:"add",handler:'addInterviewAppoStu'},'-',
							{text:"删除",tooltip:"删除",iconCls:"remove",handler:'delInterviewAppoStubat'},'->',
							/*{
								xtype:'splitbutton',
								text:'更多操作',
								iconCls: 'list',
								glyph: 61,
								menu:[{
										text:"撤销选中记录",
										iconCls:"revoke",
										handler:'UndoSelList'
									},{
										text:"导出选中记录到Excel",
										iconCls:"excel",
										handler:'exportToExcel'
									}]
							}*/
						]
					}],
					plugins: [/*{
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
					},*/{
						ptype: 'cellediting',
						pluginId: 'msAppoCellEditingPlugin',
						clicksToEdit: 1
					}],
					columnLines: true,    //显示纵向表格线
					selModel:{
						type: 'checkboxmodel'
					},
					columns: [{
						text: '面试时间',
						dataIndex: 'msPlanSeq',
						groupable: true
					},{
						text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.name", '姓名'),
						dataIndex: 'name',
						width: 120
					},{
						text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.emial", '邮箱'),
						dataIndex: 'email',
						width: 160
					},{
						text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.mobile", '手机'),
						dataIndex: 'mobile',
						width: 140
					},/*{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.skypeAcc", 'Skype账号'),
						dataIndex: 'skypeAcc',
						width: 120
					},{
						 menuDisabled: true,
						 sortable: false,
						 width:40,
						 header:' ',
						 xtype: 'actioncolumn',
						 dataIndex: 'transferSkype',
						 items:[
							 {iconCls: 'skype',tooltip: 'Skype',handler:'transferSkype'}
						 ]
					},*/{
						text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.sort", "类别") ,
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
						menuDisabled: true,
						sortable: false,
						header:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.active", '操作'),
						width:60,
						xtype: 'actioncolumn',
						items:[
							{	iconCls: 'remove',tooltip: '删除',handler:'delInterviewAppoStuRow'}
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
