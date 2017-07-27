Ext.define('KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeWindow',{
	extend: 'Ext.window.Window',
	xtype: 'materialsReviewExamineeWindow',
	controller: 'materialsReviewExamineeController',
    requires: [
    	'Ext.data.*', 
    	'Ext.grid.*', 
    	'Ext.util.*', 
    	'Ext.grid.filters.Filters',
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeWindowStore',
    	'KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeController'
    ],
	title: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_ADDKS_STD.title","新增考生"),
	width: 1200,
	height: 400,
	modal: true,
	layout:{
		type:'fit'
	},
    initComponent:function() {

        var store = new KitchenSink.view.enrollmentManagement.materialsReview.materialsReviewExamineeWindowStore();

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
                store: store,
                dockedItems:[{
                    xtype:'toolbar',
                    items:[
                        {text:"查询",iconCls:'query',tooltip:"从所有考生中查询",handler:"queryExamineeAdd"}/*,"-",
                        {text:"添加所有考生",iconCls:'add',tooltip:"添加所有考生",handler:"addExamineeAll"}*/]
                }],
                columns: [{
                    dataIndex:'mssqh',
                    hidden:true
                },{
                    text: "姓名",
                    dataIndex: 'name',
                    minWidth:80,
                    filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }

                },{
                    text: "性别",
                    dataIndex: 'sexDesc',
                    minWidth:60,
                    filter: {
                        type: 'list'
                    }
                },{
                    text: "报名表编号",
                    dataIndex: 'appinsId',
                    minWidth: 80,
                    filter: {
                        type: 'list'
                    }
                },{
                    text: "报考班级",
                    dataIndex: 'className',
                    minWidth:150,
                    filter: {
                        type: 'list'
                    }
                },{
                    text: "材料评审批次",
                    dataIndex: 'batchName',
                    minWidth: 110,
                    filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }
                },{
					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.judgeList","评委"),
					dataIndex:'judgeList',
					width:150
				},{
					text:Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.reviewStatusDesc","评审状态"),
					dataIndex:'reviewStatusDesc',
                    minWidth:120,
					filter:{
						type:'list'
					}
				},{
                    text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.negativeList","负面清单"),
                    dataIndex: 'negativeList',
                    minWidth:200,
                    xtype: 'templatecolumn',
                    tpl: Ext.create('Ext.XTemplate','{[this.labels(values)]}',{
                        labels: function(values){
                            var labels = "";
                            var val = values.negativeList;
                            if(val.trim() != ""){
                                var labelArr = val.split(",");
                                for(var i=0;i<labelArr.length;i++){
                                    labels = labels
                                        + '<span style="margin:0px 2px;padding:3px 5px;background:#CCC7C7;border-radius:5px;">'
                                        + labelArr[i]
                                        + '</span>';
                                }
                            }
                            return labels;
                        }
                    }),
                    flex:1
                },{
                    text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.autoLabel","自动标签"),
                    dataIndex: 'autoLabel',
                    minWidth:200,
                    xtype: 'templatecolumn',
                    tpl: Ext.create('Ext.XTemplate','{[this.labels(values)]}',{
                        labels: function(values){
                            var labels = "";
                            var val = values.autoLabel;
                            if(val.trim() != ""){
                                var labelArr = val.split(",");
                                for(var i=0;i<labelArr.length;i++){
                                    labels = labels
                                        + '<span style="margin:0px 2px;padding:3px 5px;background:#CCC7C7;border-radius:5px;">'
                                        + labelArr[i]
                                        + '</span>';
                                }
                            }
                            return labels;
                        }
                    }),
                    flex:1
                },{
                    text: Ext.tzGetResourse("TZ_REVIEW_CL_COM.TZ_CLPS_KS_STD.manualLabel","手工标签"),
                    dataIndex: 'manualLabel',
                    minWidth:200,
                    xtype: 'templatecolumn',
                    tpl: Ext.create('Ext.XTemplate','{[this.labels(values)]}',{
                        labels: function(values){
                            var labels = "";
                            var val = values.manualLabel;
                            if(val.trim() != ""){
                                var labelArr = val.split(",");
                                for(var i=0;i<labelArr.length;i++){
                                    labels = labels
                                        + '<span style="margin:0px 2px;padding: 3px 5px;background:#CCC7C7;border-radius:5px;">'
                                        + labelArr[i]
                                        + '</span>';
                                }
                            }
                            return labels;
                        }
                    }),
                    flex:1
                }],
                bbar: {
                    xtype: 'pagingtoolbar',
                    pageSize: 50,
                    store: store,
                    plugins: new Ext.ux.ProgressBarPager()
                }
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