Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExaminee',{
	extend: 'Ext.panel.Panel',
	xtype: 'materialsReviewExaminee',
	controller: 'materialsReview',
    requires: [
    	'Ext.data.*', 
    	'Ext.grid.*', 
    	'Ext.util.*', 
    	'Ext.toolbar.Paging', 
    	'Ext.ux.ProgressBarPager', 
    	'Ext.ux.MaximizeTool',
		'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeWindow',
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeStore',
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeController'
    ],
	title: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.title","材料评审考生名单"),
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
	actType:'',
    constructor: function(obj) {
        this.classId = obj.classId;
        this.batchId = obj.batchId;
        this.callParent();
    },
    initComponent:function() {
    	
    	//考生名单store
    	var examineeStore = new KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeStore();
    
    	Ext.apply(this,{
    		items:[{
    			xtype:'form',
    			reference:'materialsReviewExamineeForm',
    			layout:{
    				type:'vbox',
    				align:'stretch'
    			},
    			border:false,
    			bodyPadding:10,
    			bodyStyle:'overflow-y:auto;overflow-x:hidden',
    			fieldDefaults:{
    				msgTarget:'side',
    				labelWidth:110,
    				labelStyle:'font-weight:bold'
    			},
    			items:[{
    				xtype:'textfield',
    				name:'classId',
    				hidden:true
    			},{
    				xtype:'textfield',
    				name:'batchId',
    				hidden:true
    			},{
    				xtype:'textfield',
    				name:'dqpsLunc',
    				hidden:true
    			},{
    				xtype:'textfield',
    				name:'dqpsStatus',
    				hidden:true
    			},{
    				xtype:'textfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.className","报考班级"),
    				name:'className',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true
    			},{
    				xtype:'textfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.batchName","批次"),
    				name:'batchName',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true
    			},{
    				xtype:'numberfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.bkksNum","报考考生数量"),
    				name:'bkksNum',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true
    			},{
    				xtype:'numberfield',
    				fieldLabel:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.clpsksNum","材料评审考生"),
    				name:'clpsksNum',
    				fieldStyle:'background:#F4F4F4',
    				readOnly:true,
					value:0
    			},{
    				xtype:'grid',
    				title:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.ksGridTitle","考生名单"),
    				minHeight:260,
    				name:'materialsReviewExamineeGrid',
    				columnLines:true,
    				autoHeight:true,
    				frame:true,
    				selModel:{
    					selType:'checkboxmodel'
    				},
    				dockedItems:[{
    					xtype:'toolbar',
    					items:[{
    						text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.query","查询"),
    						tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.query","查询"),
    						iconCls:'query',
    						handler:'queryExaminee'
    					},"-",{
    						text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.add","新增"),
    						tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.add","新增"),
    						iconCls:'add',
    						handler:'addExaminee'
    					},"-",{
    						text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.remove","删除"),
    						tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.remove","删除"),
    						iconCls:'remove',
    						handler:'removeExaminee'
    					},"-",{
    						text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.people","指定评委"),
    						tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.people","指定评委"),
    						iconCls:'people',
    						handler:'setJudgeForExaminee'
    					},"->",{
    						xtype:'splitbutton',
							text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.more","更多操作"),
							iconCls:'list',
							glyph:61,
							menu:[{
								text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.exportExcel","导出选中考生评议数据"),
    							name:'excel',
    							handler:'exportExcel'
							}]
    					}]
    				}],
    				plugins:[{
    					ptype:'gridfilters'
    				}],
    				columns:[{
    					xtype:'rownumberer',
    					width:50,
    					align:'center'
    				},{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.name","考生姓名"),
    					dataIndex:'name',
    					width:100,
    					filter:{
    						type:'string'
    					}
    				},{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.mssqh","面试申请号"),
    					dataIndex:'mssqh',
    					width:100,
    					filter:{
    						type:'string'
    					}
    				},{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.appinsId","报名表编号"),
    					dataIndex:'appinsId',
    					width:100,
    					filter:{
    						type:'string'
    					}
    				},{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.sex","性别"),
    					dataIndex:'sexDesc',
    					width:100,
    					filter:{
    						type:'list'
    					}
    				},{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.judgeList","评委"),
    					dataIndex:'judgeList',
    					width:120,
    					flex:1
    				},{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.judgeTotal","评委总分"),
    					dataIndex:'judgeTotal',
    					width:120
    				},{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.reviewStatusDesc","评审状态"),
    					dataIndex:'reviewStatusDesc',
    					width:120,
    					filter:{
    						type:'list'
    					}
    				},{
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.interviewStatusDesc","面试资格"),
    					dataIndex:'interviewStatusDesc',
    					width:120,
    					filter:{
    						type:'list'
    					}
    				},{
    					menuDisabled:true,
    					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.operate","操作"),
    					sortable:false,
    					width:80,
    					xtype:'actioncolumn',
    					items:[{
    						iconCls:'people',tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.people","指定评委"),handler:'setJudgeForOne'
    					},{
    						iconCls:'remove',tooltip:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.remove","删除"),handler:'removeExaminee'
    					}]
    				}],
    				store:examineeStore
    			}]
    		}]		
    	});
    	this.callParent();
    },
    buttons: [{
        text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.save","保存"),
        iconCls:"save",
        handler: 'onExamineeSave'
    }, {
        text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.ensure","确定"),
        iconCls:"ensure",
        handler: 'onExamineeEnsure'
    }, {
        text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.close","关闭"),
        iconCls:"close",
        handler: 'onExamineeClose'
    }]
});