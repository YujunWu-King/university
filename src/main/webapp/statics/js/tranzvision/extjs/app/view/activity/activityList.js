Ext.define('KitchenSink.view.activity.activityList', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.activity.activityListController',
        'KitchenSink.view.activity.activityListStore'
    ],
    xtype: 'activityMg',
    controller: 'activityMg',
	reference: 'activityListGridPanal',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '活动管理',
    viewConfig: {
        enableTextSelection: true
    },
	plugins: {
		ptype: 'cellediting',
		pluginId: 'artHdListCellEditing'
		//	clicksToEdit: 1
	},
		header:false,
		frame: true,
  //  dockedItems:[{xtype:"toolbar",dock:"bottom",ui:"footer",items:['->',{minWidth:80,text:"保存",iconCls:"save"}]},{
   dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:[
				'->',{minWidth:80,text:"保存",iconCls:"save",name:"save",handler:"onComRegSave"},
				{minWidth:80,text:"确认",iconCls:"ensure",name:"ensure",handler:"onComRegSave"},
				{minWidth:80,text:"关闭",iconCls:"close",handler:"onComRegClose"}
			]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'cfgSearchAct'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addActivity'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls: "edit",handler:'editActivity'},'-',
			{text:"发布",tooltip:"发布选中内容",iconCls:"publish",handler:'releaseSelList'},'-',
			{text:"撤销发布",tooltip:"撤销发布选中内容",iconCls:"revoke",handler:'UndoSelList'},'->',
			{
				xtype:'splitbutton',
				text:'更多操作',
				iconCls:  'list',
				glyph: 61,
				menu:[{
					text:'最近活动',
					handler:'showCurrentActivity'
				},{
					text:'历史活动',
					handler:'showHistoryActivity'
				},{
					text:'全部活动',
					handler:'showAllActivity'
				}]
			}
		]
	}],
    initComponent: function () {   
    		var store = new KitchenSink.view.activity.activityListStore();    										
    		 
        Ext.apply(this, {
            columns: [/*{ 
                text: '站点ID',
                dataIndex: 'siteId',
                hidden: true
            },{ 
                text: '栏目ID',
                dataIndex: 'columnId',
                hidden: true
            },*/{ 
                text: '活动ID',
                dataIndex: 'activityId',
                hidden: true
            },{ 
                text: '活动名称',
                dataIndex: 'activityName',
								minWidth: 125,                
								flex: 1
            },{
			   text: '手机版', 
               menuDisabled: true,
               sortable: false,
			   hidden:true,
			   minWidth:70,
               align:'center',
               xtype: 'actioncolumn',
			  			 items:[
					  		{iconCls: 'view',tooltip: '手机版',handler: 'viewPhoneActivity'}
			   				]
            },{
                text: '活动地点',
                sortable: true,
                dataIndex: 'activityPlace',
                minWidth: 85,
                flex: 1
            },{
                text: '活动开始日期',
                sortable: true,
                dataIndex: 'activityStartDate',
               // formatter: 'date("Y-m-d")',
                width: 115
            },{
                text: '活动结束日期',
                sortable: true,
                dataIndex: 'activityEndDate',
               // formatter: 'date("Y-m-d")',
                width: 115
            },{
                text: '报名开始日期',
                sortable: true,
                dataIndex: 'applyStartDate',
               // formatter: 'date("Y-m-d")',
                width: 115
            },{
                text: '报名结束日期',
                sortable: true,
                dataIndex: 'applyEndDate',
               // formatter: 'date("Y-m-d")',
                width: 115
            },{
                text: '报名人数',
                sortable: true,
                dataIndex: 'applyNum',
                width: 83,
                renderer: function(v) {
	                	return '<a href="javascript:void(0)">'+v+'</a>';
	            	},
		            listeners:{
		            	click:'actApplicants'
		            }
            },{
	            text: '发布/撤销',
	            dataIndex: 'releaseOrUndo',
	            width: 87,
	            align: 'center',
	            groupable: false,
	            renderer: function(v) {
	                if(v == "Y"){
	                	//return '<a href="javascript:void(0)">撤销</a>';
						return '<img src="data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==" class="x-action-col-icon x-action-col-0  revoke" />';
						
	                }else{
	                	//return '<a href="javascript:void(0)">发布</a>';
						return '<img src="data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==" class="x-action-col-icon x-action-col-0  publish" />';
	                }
	            },
	            listeners:{
	            	click:'releaseOrUndo'
	            }
	        },{
	            text: '置顶/撤销',
	            dataIndex: 'topOrUndo',
	            width: 87,
	            align: 'center',
	            groupable: false,
				iconCls: 'top',
	            renderer: function(v) {
	            	if(v != "0"){
	                	//return '<a href="javascript:void(0)" iconCls="revoke">撤销</a>';
						// this.setIconClass('revoke');
						return '<img src="data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==" class="x-action-col-icon x-action-col-0  revoke" />';
						
	                }else{
	                	//return '<a href="javascript:void(0)" iconCls="top">置顶</a>';					
						// this.setIconClass('top');
						return '<img src="data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==" class="x-action-col-icon x-action-col-0  top" />';
	                }
	            },
				getClass:function(v,metadata,r){
                                if (v != "0"){
                                    return 'revoke';
                                }else{
                                    return 'top';
                                }
                            },
	            listeners:{
	            	click:'topOrUndo'
	            }
	        },{
				text: "置顶权重",
				dataIndex: 'artZdSeq',
				width: 70,
				align: 'center',
				editor: {
					xtype: 'numberfield'
				}
			},{
				text: '操作', 
                menuDisabled: true,
                sortable: false,
                align:'center',
                xtype: 'actioncolumn',
				items:[{
					iconCls: 'edit',tooltip: '编辑',handler: 'editSelActivityInfo'},
				/*	{iconCls: 'edit',tooltip: '报名人管理',handler: 'actApplicantsMg'}  */
				]
            }],
            store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});
