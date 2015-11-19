Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrPreview', {
    extend: 'Ext.panel.Panel',
    xtype: 'interviewArrPreview',
	controller: 'interviewArrPreviewController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
		'Ext.grid.filters.Filters',
        'KitchenSink.view.interviewManagement.interviewArrange.interviewArrPreviewModel',
		'KitchenSink.view.interviewManagement.interviewArrange.interviewArrPreviewStore',
		'KitchenSink.view.interviewManagement.interviewArrange.interviewArrPreviewController'
	],
	title: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.panelTitle","考生面试安排情况一览表"),
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	style:"margin:8px",
	listeners:{
		resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
			var buttonHeight = 44;/*button height plus panel body padding*/
			var grid = panel.child('grid');
			if(grid)grid.setHeight( height -buttonHeight -8);
		}
	},
	initComponent: function (){
		var me = this;
		//报名表提交状态
		var appFormStateStore=new KitchenSink.view.common.store.appTransStore("TZ_APPFORM_STATE");
		appFormStateStore.load();
		//为filter添加值
		var appFormStateSortFilterOptions=[];
		appFormStateStore.addListener("load",function(store, records, successful, eOpts){
			for(var i=0;i<records.length;i++){
				appFormStateSortFilterOptions.push([records[i].data.TValue,records[i].data.TSDesc]);
			};
		});
		//报名表初审状态
		var auditStateStore = new KitchenSink.view.common.store.appTransStore("TZ_AUDIT_STATE");
		auditStateStore.load();
		//为filter添加值
		var auditStateSortFilterOptions=[];
		auditStateStore.addListener("load",function(store, records, successful, eOpts){
			for(var i=0;i<records.length;i++){
				auditStateSortFilterOptions.push([records[i].data.TSDesc,records[i].data.TSDesc]);
			};
		});
		//材料评审状态（有无面试资格）
		var mszgFlagStore = new KitchenSink.view.common.store.appTransStore("TZ_MSHI_ZGFLG");
		mszgFlagStore.load();
		//为filter添加值
		var mszgFlagSortFilterOptions=[];
		mszgFlagStore.addListener("load",function(store, records, successful, eOpts){
			for(var i=0;i<records.length;i++){
				mszgFlagSortFilterOptions.push([records[i].data.TSDesc,records[i].data.TSDesc]);
			};
		});
		//gridStore添加filterchange监听
		var msArrPreviewGridStore = new KitchenSink.view.interviewManagement.interviewArrange.interviewArrPreviewStore({
			listeners:{
				filterchange:function( store, filters, eOpts ){
					var clearFiltersBtn=me.lookupReference('msArrPreviewClearFiltersBtn');
					if(filters.length>0){
						clearFiltersBtn.setDisabled( false );
					}else{
						clearFiltersBtn.setDisabled( true );
					}
				}
			}
		});
		Ext.apply(this,{
			items: [{
				xtype: 'grid',
				height: 420,
				frame: true,
				name: 'interviewArrPreviewGrid',
				reference: 'interviewArrPreviewGrid',
				store: msArrPreviewGridStore,
				columnLines: true,    //显示纵向表格线
				plugins:[
					{
						ptype: 'gridfilters'
					}
				],
				dockedItems:[{
					xtype:"toolbar",
					items:[	{
							text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.tbarClearFilters","清除筛选条件"),
							tooltip:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.tbarClearFiltersTip","清除筛选条件"),
							iconCls:"reset",
							reference:'msArrPreviewClearFiltersBtn',
							handler:'onClearFilters',
							disabled:true
					}]
				}],
				columns: [{
					text: Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.rowNum","序号"),
					xtype: 'rownumberer',
					width:50
				},{
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.appId","报名表编号") ,
					dataIndex: 'appId',
					filter: {
						type: 'number'
					},
					minWidth:125,
					flex:1
				},{
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.stuName","姓名") ,
					dataIndex: 'stuName',
					filter: {
						type: 'string'
					},
					minWidth: 100,
					flex:1
				},{
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.appFormSta","报名表提交") ,
					dataIndex: 'appFormSta',
					filter: {
						type: 'list',
						options: appFormStateSortFilterOptions
					},
					renderer : function(value, metadata, record) {
						//alert("render"+value);
						var index = appFormStateStore.find('TValue',value);
						if(index!=-1){
							return appFormStateStore.getAt(index).data.TSDesc;
						}
						return record.get('appFormSta');
					},
					minWidth:110,
					flex:1
				},{
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.msAppFormAuditSta","报名表评审状态"),
					dataIndex: 'msAppFormAuditSta',
					filter: {
						type: 'list',
						options: auditStateSortFilterOptions
					},
					renderer : function(value, metadata, record) {
						//alert("render"+value);
						var index = auditStateStore.find('TValue',value);
						if(index!=-1){
							return auditStateStore.getAt(index).data.TSDesc;
						}
						return record.get('msAppFormAuditSta');
					},
					minWidth:150,
					flex:1
				},{
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.msZGFlag","初筛结果"),
					dataIndex: 'msZGFlag',
					filter: {
						type: 'list',
						options: mszgFlagSortFilterOptions
					},
					renderer : function(value, metadata, record) {
						//alert("render"+value);
						var index = mszgFlagStore.find('TValue',value);
						if(index!=-1){
							return mszgFlagStore.getAt(index).data.TSDesc;
						}
						return record.get('msZGFlag');
					},
					minWidth:120,
					flex:1
				},{
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.batchName","面试批次") ,
					sortable: true,
					dataIndex: 'batchName',
					filter: {
						type: 'string'
					},
					minWidth: 150,
					flex:1
				},{
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.msDate","面试日期") ,
					xtype:'datecolumn',
					format:'Y-m-d',
					filter: {
						type: 'date',
						dateFormat: 'Y-m-d'
					},
					sortable: true,
					dataIndex: 'msDate',
					minWidth: 105,
					flex:1
				},{
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.msStartTime","开始时间") ,
					sortable: true,
					dataIndex: 'msStartTime',
					minWidth: 100,
					flex:1
				},{
					text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.msEndTime","结束时间") ,
					sortable: true,
					dataIndex: 'msEndTime',
					minWidth: 100,
					flex:1
				}]
			}]
		});
		this.callParent();
	},
	buttons: [{
		text:Ext.tzGetResourse("TZ_MS_ARR_MG_COM.TZ_MS_ARR_PRE_STD.butClose","关闭") ,
		iconCls:"close",
		handler:'onPanelClose'
	}]
});
