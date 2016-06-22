Ext.define('KitchenSink.view.onTrialManagement.trialManagement', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.onTrialManagement.trialManagementController',
        'KitchenSink.view.onTrialManagement.trialManagementStore'
    ],
    xtype: 'trialManagement',	
	controller: 'trialManagementController',
	store: {
        type: 'trialManagementStore'
    },
    columnLines: true,    //显示纵向表格线
    selModel: {
        type: 'checkboxmodel'   //复选框选择模式
    },
	style:"margin:8px",
    multiSelect: true,     //设置在行选择模式下支持多选
    title: '试用申请人管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"关闭",iconCls:"close",handler:'closeTrialManagement'}]
		},{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchTrial'},"-",
			{text:"编辑",tooltip:"编辑数据",iconCls:"edit",handler:'editSelTrial'},'->',
			{
				xtype:'splitbutton',
				text:'更多操作',
				iconCls:  'list',
				glyph: 61,
				menu:[{
					text:'发送邮件',
					handler:'sendEmail'
				}]
			}
		]
	}],
    initComponent: function () {
		var store = new KitchenSink.view.onTrialManagement.trialManagementStore();
        Ext.apply(this, {
            columns: [
			{ 
                text: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_MNG_STD.seqNum","序号"),
                sortable: false,
                dataIndex: 'seqNum',
				hidden:true
            },
			{ 
                text: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_MNG_STD.contactName","联系人姓名"),
                sortable: true,
                dataIndex: 'contactName',
				width: 100
            },{
                text: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_MNG_STD.contactPhone","联系人电话"),
                sortable: true,
                dataIndex: 'contactPhone',
                width: 120
            },{
                text: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_MNG_STD.contactEmail","联系人邮箱"),
                sortable: true,
                dataIndex: 'contactEmail',
                width: 200
            },{
                text: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_MNG_STD.orgName","机构名称"),
                sortable: true,
                dataIndex: 'orgName',
                width: 170
            },{
                text: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_MNG_STD.hmsr","来源"),
                sortable: true,
                dataIndex: 'hmsr',
                width: 120
            },{
                text: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_MNG_STD.submitTime","申请提交时间"),
                sortable: true,
                dataIndex: 'submitTime',
                width: 150
            },{
                text: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_MNG_STD.shRst","审核结果"),
                sortable: true,
                dataIndex: 'shRst',
                width: 100
            },{
                text: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_MNG_STD.startTime","试用开始时间"),
                sortable: true,
                dataIndex: 'startTime',
                width: 150
            },{
                text: Ext.tzGetResourse("TZ_TRIAL_MNG_COM.TZ_TRIAL_MNG_STD.endTime","试用结束时间"),
                sortable: true,
                dataIndex: 'endTime',
                width: 150,
                renderer: function(v){
                	if(v != null && v != ''){
                    	var s = v.replace(/-/g,"/");
                    	var date = new Date(s);
                    	
                    	currentDate = new Date();

                    	var days = date.getTime() - currentDate.getTime();
                    	var between = days/ (1000 * 60 * 60 * 24);
      
        				if(between > 0 && between < 3){
        					return '<span style="color:red">'+v+'</span>';
        				}else{
        					return v;
        				}
                    	
                	}else{
                		return "";
                	}
                	
					
				}
            },{
              	menuDisabled: true,
                sortable: false,
                flex:1,
                xtype: 'actioncolumn',
                align:'center',
			    items:[
				  {iconCls: 'edit',tooltip: '编辑',handler:'editTrial'}
			   ]
            }],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 20,
   				store: store,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
		
        this.callParent();
    }
});