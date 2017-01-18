Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewAddWin', {
    extend: 'Ext.window.Window',
    requires: [
	    'Ext.data.*',
        'Ext.util.*',
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
		        	height: 354,
		            viewConfig: {
		                enableTextSelection: true
		            },
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
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.appId","报名表编号") ,
	                    dataIndex: 'appId',
	                    minWidth:125,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.stuName","姓名") ,
	                    dataIndex: 'stuName',
	                    minWidth: 100,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.email","邮箱") ,
	                    dataIndex: 'email',
	                    minWidth: 100,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.mobile","手机") ,
	                    dataIndex: 'mobile',
	                    minWidth: 100,
	                    flex:1
	                },/*{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.msCLPSPC","材料评审批次") ,
	                    dataIndex: 'msCLPSPC',
	                    minWidth: 125,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.msZGFlag","初筛结果"),
	                    dataIndex: 'msZGFlag',
	                    renderer : function(value, metadata, record) {
	                        var index = mszgFlagStore.find('TValue',value);
	                        if(index!=-1){
	                            return mszgFlagStore.getAt(index).data.TSDesc;
	                        }
	                        return record.get('msZGFlag');
	                    },
	                    minWidth:120,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.city","所在城市") ,
	                    sortable: true,
	                    dataIndex: 'city',
	                    minWidth: 105,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.country","所在国家") ,
	                    sortable: true,
	                    dataIndex: 'country',
	                    minWidth: 105,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.timezone","所属时区") ,
	                    filter: {
	                        type: 'string'
	                    },
	                    sortable: true,
	                    dataIndex: 'timezone',
	                    minWidth: 107,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.timezomeDiff","时差（同北京）") ,
	                    filter: {
	                        type: 'string'
	                    },
	                    sortable: true,
	                    dataIndex: 'timezomeDiff',
	                    minWidth: 125,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.bjTime","北京时间") ,
	                    sortable: true,
	                    dataIndex: 'bjTime',
	                    minWidth: 110,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.localStartDate","当地开始日期") ,
	                    xtype:'datecolumn',
	                    format:'Y-m-d',
	                    filter: {
	                        type: 'date',
	                        dateFormat: 'Y-m-d'
	                    },
	                    sortable: true,
	                    dataIndex: 'localStartDate',
	                    minWidth: 125,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.localStartTime","当地开始时间") ,
	                    sortable: true,
	                    dataIndex: 'localStartTime',
	                    minWidth: 125,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.localFinishDate","当地结束日期") ,
	                    xtype:'datecolumn',
	                    format:'Y-m-d',
	                    filter: {
	                        type: 'date',
	                        dateFormat: 'Y-m-d'
	                    },
	                    sortable: true,
	                    dataIndex: 'localFinishDate',
	                    minWidth: 125,
	                    flex:1
	                },{
	                    text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MSKS_VIEW_STD.localFinishTime","当地结束时间") ,
	                    filter: {
	                        type: 'string'
	                    },
	                    sortable: true,
	                    dataIndex: 'localFinishTime',
	                    minWidth: 125,
	                    flex:1
	                }*/],
					bbar: {
						xtype: 'pagingtoolbar',
						pageSize: 20,
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
		text: '确定',
		iconCls:"ensure",
		handler: 'onWindowEnsure'
	},{
		text: '关闭',
		iconCls:"close",
		handler: 'onWindowClose'
	}]
});