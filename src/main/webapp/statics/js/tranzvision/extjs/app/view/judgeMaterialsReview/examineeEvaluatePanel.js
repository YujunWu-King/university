Ext.define('KitchenSink.view.judgeMaterialsReview.examineeEvaluatePanel',{
	extend:'Ext.panel.Panel',
	xtype:'examineeEvaluate',
	requires:[
		'Ext.data.*',
		'Ext.grid.*',
		'Ext.util.*',
		'Ext.layout.container.Border',
		'Ext.toolbar.Paging',
		'Ext.ux.ProgressBarPager',
		'tranzvision.extension.grid.column.Link',
		'KitchenSink.view.judgeMaterialsReview.examineeEvaluateController'
	],
	layout:'fit',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	frame:true,
	controller:'examineeEvaluateController',
	construct:function(obj) {
		this.classId = obj.classId;
		this.applyBatchId = obj.applyBatchId;
		this.applyBatchName = obj.applyBatchName;
		this.bmbId = obj.bmbId;
		this.callParent();
	},
	dockedItems:[{
		xtype:'toolbar',
		items:[{
			text:'返回评审主页面',
			handler:'returnMaterialsReview'
		},{
			xtype:'displayfield',
			name:'applyBatchName',
			value:this.applyBatchName
		}]
	}],
	initComponent:function() {
		
		Ext.apply(this,{
			items:[{
				xtype:'panel',
				layout:'border',
				bodyBorder:false,
				items:[{
					title:'考生列表',
					region:'west',
					floatable:false,
					collapsible:true,
					collapsed:true,
					split:true,
					margin:'5 0 0 0',
					width:310,
					minWidth:310,
					maxWidth:380,
					layout:'fit',
					items:[{
						xtype:'grid',
						title:'请给以下考生打分',
						columnsLines:true,
						name:'examineeListGrid',
						store:'',
						columns:[{
                            xtype:'linkcolumn',
							text: '姓名',
                            dataIndex: 'examineeName',
                            width: 85,
                            flex: 1,
                            handler:'changeExaminee'
                        },{
							text: '排名',
                            dataIndex: 'examineeRank',
                            width: 50
						},{
							text:'总分',
							dataIndex:'examineeTotalScore',
							width:50
						},{
							text:'面试申请号',
							dataIndex:'examineeInterviewId',
							width:90
						}]
					}]	
				},{
					name:'examineeBmbArea',
					region:'center',
					margin:'5 0 0 0',
					html:''
				},{
					title:'【打分区】',
					region:'east',
					floatable:false,
					collapsible:true,
					collapsed:true,
					split:true,
					margin: '5 0 0 0',
                    width: 720,
                    minWidth: 720,
                    maxWidth: 800,
                    layout: 'fit',
                    scrollable: true,
                    items:[{
                    	xtype: 'form',
                    	name:'scoreForm',
                        bodyPadding:10,
                        fieldDefaults: {
                            msgTarget: 'side',
                            labelWidth: 120,
                            labelStyle: 'font-weight:bold'
                        },
                        items: [{
                            xtype:'fieldcontainer',
                            layout:'hbox',
                            items:[{
                                xtype: 'displayfield',
                                name: 'interviewApplyId',
                                fieldLabel: '面试申请号',
                                flex: 1,
                                margin: '0 20 0 0'
                            }, {
                                xtype: 'displayfield',
                                name: 'name',
                                labelWidth: 50,
                                flex: 1,
                                margin: '0 20 0 0'
                            }, {
                                xtype: 'container',
                                html: '',
                                flex: 1,
                                margin: '5 0 0 0'
                            }]
                        }]
                    }, {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        margin: 10,
                        items: [{
                            xtype: 'button',
                            text: '保存',
                            handler: 'saveExamineeEvaluate'
                        }, {
                            xtype: 'button',
                            text: '保存并获取下一个考生',
                            handler: 'saveAndGetNext'
                        }, {
                            xtype: 'button',
                            text: '返回评审界面',
                            handler: 'returnMaterialsReview',
                            margin: '0 0 0 10'
                        }]
                    }]
				}]
				
			}]
		});
		
		this.callParent();
	}
});