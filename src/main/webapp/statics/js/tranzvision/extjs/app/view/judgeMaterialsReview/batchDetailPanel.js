Ext.define('KitchenSink.view.judgeMaterialsReview.batchDetailPanel',{
	extend:'Ext.panel.Panel',
	xtype:'batchDetail',
	requires:[
		'Ext.data.*',
		'Ext.grid.*',
		'Ext.util.*',
		'Ext.layout.container.Border',
		'Ext.toolbar.Paging',
		'Ext.ux.ProgressBarPager',
		'KitchenSink.view.judgeMaterialsReview.examineeEvaluateController'
	],
	layout:'fit',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	frame:true,
	controller:'examineeEvaluateController',
	construct:function(obj) {
		this.applyBatchName = obj.applyBatchName;
		this.callParent();
	},
	dockedItems:[{
		xtype:'toolbar',
		items:[{
			text:'进行评审',
			handler:'materialsReviewTest'
		}]
	}]
});