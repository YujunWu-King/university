Ext.define('KitchenSink.view.classManage.classManage', {//班级管理列表页面
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.classManage.classController',//调用控制器
        'KitchenSink.view.classManage.classStore'
    ],
    xtype: 'classManage',//不能变
	controller: 'classManage',
    columnLines: true,
	selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.bj_title","班级管理"),
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		items:[
			{text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.query","查询"),iconCls:"query",handler:'query_list'},"-",
			{text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.add","新增"),iconCls:"add",handler:'addClassE'},"-",
			{text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.edit","编辑"),iconCls:"edit",handler:'editClassInfoT'}
		]
	},{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {
                minWidth:80,
                text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.close","关闭"),
                iconCls:"close",
                handler: 'onComRegClose'}]
    }],
    initComponent: function () {   
		var store = new KitchenSink.view.classManage.classStore();
        Ext.apply(this, {
            columns: [{ 
                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.bj_id","班级编号"),
                dataIndex: 'bj_id',
				sortable: true,
				width: 130
            },{ 
                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.bj_name","班级名称"),
				sortable: true,
                dataIndex: 'bj_name',
                minWidth: 150,
				flex: 1
            },{
                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.xm_name","所属项目"),
                sortable: true,
                dataIndex: 'xm_name',
                minWidth: 150,
                flex: 1
            },{
            	text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.rx_time","入学日期"),
                sortable:true,
                dataIndex:'rx_time',
                width:110
            },{
                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.begin_time","开始时间"),
                sortable: true,
                dataIndex: 'begin_time',
                width: 110
            },{
                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.end_time","结束时间"),
                sortable: true,
                dataIndex: 'end_time',
                width: 110
            },{
                text: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.bj_kt_desc","是否开通在线报名"),
                sortable: true,
                dataIndex: 'bj_kt_desc',
                width: 160
            },{
               menuDisabled: true,
               sortable: false,
               width:60,
			   align: 'center',
			   xtype: 'actioncolumn',
			   items:[{iconCls: 'edit',tooltip: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.edit","编辑"),handler:'editClassInfo'}
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