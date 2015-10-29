Ext.define('KitchenSink.view.interviewManagement.interviewArrange.MsChoiceSt', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.interviewManagement.interviewArrange.MsChoiceStController',//调用控制器
		'KitchenSink.view.interviewManagement.interviewArrange.classModel',//json格式
        'KitchenSink.view.interviewManagement.interviewArrange.classStore'//json路径
    ],
    xtype: 'MsChoiceSt',
	controller: 'MsChoiceStController',
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: '考生选择',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls:"query",handler:'query_list'},"-",
			{text:"新增",tooltip:"新增数据",iconCls:"add",handler:'addClassE'}
		]
	}],
    initComponent: function () {   
		var store = new KitchenSink.view.classManage.classStore();
        Ext.apply(this, {
            columns: [{ 
                text: '班级编号',
                dataIndex: 'bj_id',
				hidden:true
            },{ 
                text: '批次编号',
                dataIndex: 'batchID',
				hidden: true
            },{
                text: '报名表编号',
                sortable: true,
                dataIndex: 'app_ins_id',
                minWidth: 150,
                flex: 1
            },{
                text: '人员ID',
                dataIndex: 'oprid',
				hidden: true
            },{ 
                text: '考生姓名',
                dataIndex: 'ks_name',
				minWidth: 150
            },{ 
                text: '所在城市',
                dataIndex: 'ks_city',
				minWidth: 150
            },{ 
                text: '所在国家',
                dataIndex: 'ks_country',
				minWidth: 150
            },{ 
                text: '所属时区',
                dataIndex: 'ks_sq',
				minWidth: 150
            },{ 
                text: '时差（同北京）',
                dataIndex: 'ks_sc',
				minWidth: 150
            },{
                text: '当地开始日期',
                dataIndex: 'ddbegin_date',
                minWidth: 110
            },{
                text: '当地开始时间',
                dataIndex: 'ddbegin_time',
                minWidth: 110
            },{
                text: '当地结束日期',
                dataIndex: 'ddend_date',
                minWidth: 110
            },{
                text: '当地结束时间',
                dataIndex: 'ddend_time',
                minWidth: 110
            },{
                text: '预约状态',
                dataIndex: 'yy_state',
                minWidth: 110
            },{
                text: '时间类型',
                dataIndex: 'sj_type',
                minWidth: 110
            }],
			store: store,
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
				store: store,
                displayInfo: true,
				displayMsg: '显示{0}-{1}条，共{2}条',
				beforePageText: '第',
                afterPageText: '页/共{0}页',
				emptyMsg: '没有数据显示',
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});