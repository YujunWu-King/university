Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeWindow',{
	extend: 'Ext.window.Window',
	xtype: 'materialsReviewExamineeWindow',
	controller: 'materialsReview',
    requires: [
    	'Ext.data.*', 
    	'Ext.grid.*', 
    	'Ext.util.*', 
    	'Ext.grid.filters.Filters',
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeWindowStore',
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeController'
    ],
	title: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_ADDKS_STD.title","新增考生"),
	width: 800,
	height: 400,
	modal: true,
	layout:{
		type:'fit'
	},
    initComponent:function() {
    	Ext.apply(this,{
    		items:[{
    			xtype:'grid',
    			autoHeight:true,
    			columnLines:true,
    			frame: true,
                style:'border:0',
                plugins: [
                    {
                        ptype: 'gridfilters'
                    }
                ],
                selModel: {
                    type: 'checkboxmodel'
                },
                store: {
                    type:'materialsReviewExamineeWindowStore'
                },
                dockedItems:[{
                    xtype:'toolbar',
                    items:[{text:"查询",iconCls:'query',tooltip:"从所有考生中查询",handler:"queryExamineeAdd"}]
                }],
                columns: [{
                    text: "姓名",
                    dataIndex: 'name',
                    minWidth: 100,
                    width:120,
                    filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }

                },{
                    text: "性别",
                    dataIndex: 'sexDesc',
                    minWidth: 15,
                    width:60,
                    filter: {
                        type: 'list'
                    }
                },{
                    text: "报名表编号",
                    dataIndex: 'appinsId',
                    minWidth: 100,
                    filter: {
                        type: 'list'
                    }
                },{
                    text: "报考班级",
                    dataIndex: 'className',
                    minWidth: 120,
                    width:90,
                    flex: 1,
                    filter: {
                        type: 'list'
                    }
                },{
                    text: "材料评审批次",
                    dataIndex: 'batchName',
                    minWidth: 100,
                    flex: 1,
                    filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }
                },{
					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.judgeList","评委"),
					dataIndex:'judgeList',
					width:120,
					flex:1
				},{
					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.reviewStatusDesc","评审状态"),
					dataIndex:'reviewStatusDesc',
					width:120,
					filter:{
						type:'list'
					}
				}]
    		}]
    	});
    	this.callParent();
    },
    buttons:[{
    	text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.ensure","确定"),
        iconCls: "ensure",
        handler: 'addExamineeEnsure'
    },{
    	text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.close","关闭"),
        iconCls: "close",
        handler: 'addExamineeClose'
    }]
});