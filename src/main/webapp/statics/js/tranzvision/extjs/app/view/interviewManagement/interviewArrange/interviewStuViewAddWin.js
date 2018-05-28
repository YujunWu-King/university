Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewAddWin', {
    extend: 'Ext.window.Window',
    requires: [
    	'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.grid.filters.Filters',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewAddStore',
		'KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewController'
	],
	reference: 'interviewStuViewAddWin',
    xtype: 'interviewStuViewAddWin',
	controller:'interviewStuViewController',
	
	width: 800,
	height: 500,
	minWidth: 600,
	minHeight: 400,
    title: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.selectTimePlan",'选择预约时间安排'),
	//layout: 'fit',
	resizable: false,
	modal: true,
	closeAction: 'hide',
	
	constructor: function(config,option){
		this.classID = config.classID;
		this.batchID = config.batchID;
		this.option = option;
		
		this.callParent();	
	},
	initComponent: function () {		
		var classID = this.classID;
		var batchID = this.batchID;
		
		//预约时间安排Store
		var timePlanStore = new KitchenSink.view.common.store.comboxStore({
			recname:'PS_TZ_MSSJ_ARR_VW',
			condition:{
				TZ_CLASS_ID:{
					value: classID,
					operator:'01',
					type:'01'
				},
				TZ_BATCH_ID:{
					value: batchID,
					operator:'01',
					type:'01'
				}
			},
			result:'TZ_MS_PLAN_SEQ,TZ_MS_DATE,TZ_START_TM,TZ_END_TM'
		});
		timePlanStore.load();
		
		//添加学生store
		var addStuStore = new KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewAddStore({
			classID: classID,
			batchID: batchID
		});
		
        Ext.apply(this,{
		    items: [{
		        xtype: 'form',
		        border: false,
		        //bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				layout: {
			        type: 'vbox',       // Arrange child items vertically
			        align: 'stretch'    // 控件横向拉伸至容器大小
			    },
		        fieldDefaults: {
		            msgTarget: 'side',
		            labelWidth: 120,
					labelSeparator:'',
		            labelStyle: 'font-weight:bold'
		        },
		        items: [{
		            xtype: 'combo',
		            fieldLabel: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.timePlan",'预约时间安排'),
					name: 'timePlan',
					store: timePlanStore,
					queryMode:'local',
					valueField: 'TZ_MS_PLAN_SEQ',
					displayField: 'TZ_MS_DATE',
					allowBlank: false,
					selecOnFocus: true,
					forceSelection: true,
					ignoreChangesFlag: true,
					style: 'margin:10px;',
					tpl: Ext.create('Ext.XTemplate',
							'<tpl for=".">',
								'<div class="x-boundlist-item">',
									'<div style="display: block; overflow:hidden; white-space: nowrap; -o-text-overflow: ellipsis; text-overflow:  ellipsis;">日期：{TZ_MS_DATE}  时间：{TZ_START_TM} - {TZ_END_TM}</div>',
								'</div>',
							'</tpl>'
						),
					displayTpl: Ext.create('Ext.XTemplate',
							'<tpl for=".">',
								'日期：{TZ_MS_DATE}  时间：{TZ_START_TM} - {TZ_END_TM}',
							'</tpl>'
						)
		        },{
		        	xtype: 'grid',
		        	frame: true,
		        	multiSelect: true,
		        	height: 377,
		            viewConfig: {
		                enableTextSelection: true
		            },
		            plugins: [{
                        ptype: 'gridfilters'
		            }],
		        	dockedItems:[{
						xtype:"toolbar",
						items:[
							{text:"查询",tooltip:"查询",iconCls:"query",handler:'searchInterviewStu'}
						]
					}],
					columnLines: true,    //显示纵向表格线
					selModel:{
						type: 'checkboxmodel'
					},
					store: addStuStore,
					columns: [{
	                    text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.rowNum","序号"),
	                    xtype: 'rownumberer',
	                    width:50
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.msApplyID","面试申请号") ,
	                    dataIndex: 'msApplyID',
	                    minWidth:100,
	                    width:110
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.stuName","姓名") ,
	                    dataIndex: 'stuName',
	                    minWidth: 100,
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.mobile","手机") ,
	                    dataIndex: 'mobile',
	                    minWidth: 100,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.email","邮箱") ,
	                    dataIndex: 'email',
	                    minWidth: 100,
	                    flex:1
	                },{
	                	text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.yyStatus","预约状态"),
	                    dataIndex: 'yyStatus',
	                    width: 90,
	                    minWidth: 80,
	                    filter: {
	                        type: 'list'
	                    },
	                    renderer:function(value, metadata, record){
	                    	if (value=="已预约"){
	                    		metadata.style = "color:#66cc66";
							}else{
								metadata.style = "color:#ff0000";
							}
							return value;
	                    }
	                }],
					bbar: {
						xtype: 'pagingtoolbar',
						pageSize: 50,
						store: addStuStore,
						displayInfo: true,
						plugins: new Ext.ux.ProgressBarPager()
					}
		        }]
			}]
        });
        this.callParent();
    },
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'onWindowEnsure'
	},{
		text: '关闭',
		iconCls:"close",
		handler: 'onWindowClose'
	}]
});