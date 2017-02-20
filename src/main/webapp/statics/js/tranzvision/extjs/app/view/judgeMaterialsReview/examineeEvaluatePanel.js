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
		'Ext.ux.IFrame',
		'tranzvision.extension.grid.column.Link',
		'KitchenSink.view.judgeMaterialsReview.examineeEvaluateController',
		'KitchenSink.view.judgeMaterialsReview.examineeListStore'
	],
	layout:'fit',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	frame:true,
	controller:'examineeEvaluateController',
	construct:function(obj) {
		this.classId = obj.classId;
		this.applyBatchId = obj.applyBatchId;
		this.bmbId = obj.bmbId;
		this.bmbUrl = obj.bmbUrl;
		this.scoreItemFields = obj.scoreItemFields;
		this.callParent();
	},
	dockedItems:[{
		xtype:'toolbar',
		items:[{
			xtype:'button',
			text:'返回评审主页面',
			handler:'returnMaterialsReview'
		},{
			xtype:'displayfield',
			name:'batchInfo',
			value:''
		}]
	}],
	initComponent:function() {
		
		//考生列表store
		var examineeListStore = new KitchenSink.view.judgeMaterialsReview.examineeListStore();
		
		//打分页form的items
		var scoreFormItems = [];
		var basicItems = [{
        	xtype:'textfield',
        	name:'classId',
        	hidden:true
        },{
        	xtype:'textfield',
        	name:'applyBatchId',
        	hidden:true
        },{
        	xtype:'textfield',
        	name:'applyBatchName',
        	hidden:true
        },{
        	xtype:'textfield',
        	name:'bmbId',
        	hidden:true
        },{
        	xtype:'textfield',
        	name:'type',
        	hidden:true
        },{
            xtype:'fieldcontainer',
            layout:'hbox',
            items:[{
                xtype: 'displayfield',
                name: 'interviewApplyId',
                fieldLabel: '面试申请号',
                labelWidth: 120,
                labelStyle: 'font-weight:bold',
                flex: 1,
                margin: '0 20 0 0'
            }, {
                xtype: 'displayfield',
                name: 'name',
                fieldLabel: '姓名',
                labelWidth: 120,
                labelStyle: 'font-weight:bold',
                labelWidth: 50,
                flex: 1,
                margin: '0 20 0 0'
            }, {
                xtype: 'container',
                html: '<a href="'+this.bmbUrl+'" target="_blank">新开窗口看考生材料</a>',
                flex: 1,
                margin: '5 0 0 0'
            }]
        }];
		
		for(var i=0;i<basicItems.length;i++) {
			scoreFormItems.push(basicItems[i]);
		}
		
		for(var j=0;j<this.scoreItemFields.length;j++) {
			scoreFormItems.push(this.scoreItemFields[j]);
		}
		
		
		var buttonItems = [{
            xtype: 'fieldcontainer',
            layout: 'hbox',
            margin: 10,
            items: [{
                xtype: 'button',
                text: '保存',
                width: 100,
                height: 30,
                margin: 10,
                handler: 'saveExamineeEvaluate'
            }, {
                xtype: 'button',
                text: '保存并获取下一个考生',
                width: 160,
                height: 30,
                margin: 10,
                handler: 'saveAndGetNext'
            }, {
                xtype: 'button',
                text: '返回评审界面',
                width: 100,
                height: 30,
                margin: 10,
                handler: 'returnMaterialsReview'
            }]
        }];
		
		for(var k=0;k<buttonItems.length;k++) {
			scoreFormItems.push(buttonItems[k]);
		}
		
		
		
		
		Ext.apply(this,{
			items:[{
				xtype:'panel',
				layout:'border',
				bodyBorder:false,
				name:'materialScore',
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
						store:examineeListStore,
						columns:[{
							dataIndex:'classId',
							hidden:true
						},{
							dataIndex:'applyBatchId',
							hidden:true
						},{
                            xtype:'linkcolumn',
							text: '姓名',
                            dataIndex: 'examineeName',
                            width: 85,
                            flex: 1,
                            handler:'changeExaminee',
                            renderer:function(v) {
                            	this.text = v;
                            	return;
                            }
                        },{
							text: '排名',
                            dataIndex: 'examineeRank',
                            width: 50
						},{
							text:'总分',
							dataIndex:'examineeTotalScore',
							width:60
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
					items:[
						new Ext.ux.IFrame({
			                xtype: 'iframepanel',
			                layout: 'fit',
			                style : "border:0px none;scrollbar:true",
			                border: false,
			                src : this.bmbUrl,
			                height : "100%",
			                width : "100%"
			    		})
					]
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
                            msgTarget: 'side'
                        },
                        items: scoreFormItems
                    }]
				}]
				
			}]
		});
		
		this.callParent();
	}
});