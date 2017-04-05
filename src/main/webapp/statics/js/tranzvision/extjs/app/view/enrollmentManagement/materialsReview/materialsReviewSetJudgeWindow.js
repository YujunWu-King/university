Ext.defind('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewSetJudgeWindow',{
	extend: 'Ext.window.Window',
    xtype: 'materialsReviewSetJudgeWindow',
    controller: 'materialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewSetJudgeWindowStore',
        'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeController'
    ],
    title: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_SETPW_STD.title","指定评委"),
    width: 800,
    height: 400,
    modal:true,
    layout: {
        type: 'fit'
    },
    constructor:function(obj) {
    	this.appRowIndex=obj.appRowIndex;
    	this.appSelList=obj.appSelList;
    	this.callParent();
    },
    initComponent:function() {
    	Ext.apply(this,{
    		items: [{
    			xtype:'textfield',
    			name:'appRowIndex',
    			value:this.appRowIndex,
    			hidden:true
    		},{
    			xtype:'textfield',
    			name:'appSelList',
    			value:this.appSelList,
    			hidden:true
    		},{
                    xtype: 'grid',
                    autoHeight: true,
                    columnLines: true,
                    frame: true,
                    style: 'border:0',
                    plugins: [
                        {
                            ptype: 'gridfilters'
                        }
                    ],
                    store: {
                        type: 'materialsReviewSetJudgeWindowStore'
                    },
                    dockedItems:[ {
                        xtype:"toolbar",
                        dock:"bottom",
                        ui:"footer",
                        items: ['->',{
                            text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.ensure","确定"),
                            iconCls:'ensure',
                            handler: setJudgeEnsure
                        }, {
                            text:  Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_STD.close","关闭"),
                            iconCls: "close",
                            handler: setJudgeClose
                        }]
                    }],
                    columns: [
                        {
                        	dataIndex:'classId',
                        	hidden:true
                        },{
                        	dataIndex:'batchId',
                        	hidden:true
                        },{
                        	dataIndex:'judgeOprid',
                        	hidden:true
                        },{
                            text: "选择",
                            dataIndex: 'selectFlag',
                            minWidth: 100,
                            xtype: 'checkcolumn',
                            ignoreChangesFlag: true
                        },
                        {
                            text: "评委帐号",
                            dataIndex: 'judgeId',
                            minWidth: 100,
                            flex: 1
                        },
                        {
                            text: "评委",
                            dataIndex: 'judgeName',
                            minWidth: 100,
                            flex: 1
                        },
                        {
                            text: "所属评委组",
                            dataIndex: 'judgeGroup',
                            minWidth: 100,
                            flex: 1
                        }
                    ]
                }
            ]
    	});
    	this.callParent()
    }
});