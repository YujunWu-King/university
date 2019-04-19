Ext.define('KitchenSink.view.projectAdjust.projectAdjust', {//班级管理列表页面
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.projectAdjust.projectAdjustController',//调用控制器
        'KitchenSink.view.projectAdjust.projectAdjustStore'
    ],
    xtype: 'projectAdjust',//不能变
	controller: 'projectAdjustController',
    columnLines: true,
	selModel: {
        type: 'checkboxmodel'
    },
	style:"margin:8px",
    multiSelect: true,
    title: "申请项目调整",
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[{
		xtype:"toolbar",
		items:[
			{text:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.query","查询"),iconCls:"query",handler:'query_list'}
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
		var store = new KitchenSink.view.projectAdjust.projectAdjustStore();
        Ext.apply(this, {
            columns: [{ 
                text: "申请编号",
                dataIndex: 'tz_proadjust_id',
				sortable: true,
				hidden:true,
				width:80
            },{ 
                text: "oprid",
				sortable: true,
                dataIndex: 'tz_oprid',
                width:100
            },{ 
                text: "考生姓名",
				sortable: true,
                dataIndex: 'TZ_REALNAME',
                width:100
            },{
                text: "申请班级",
                sortable: true,
                dataIndex: 'TZ_CLASS_NAME',
                flex: 1
            },{
            	text:"报名表编号",
                sortable:true,
                dataIndex:'appinsId',
                width:110
            },{
                text: "面试申请号",
                sortable: true,
                dataIndex: 'applicationId',
                flex: 1
            },{
                text: "报名表提交状态",
                sortable: true,
                dataIndex: 'submitState',
                flex: 1,
                renderer:function(v){
                	if(v == "U"){
                		return "已提交";
                	}else if(v == "S"){
                		return "新建";
                	}else if(v == "OUT"){
                		return "撤销";
                	}else if(v == "BACK"){
                		return "退回修改";
                	}else{
                		return "";
                	}
                }
            },{
                text: "申请时间",
                sortable: true,
                dataIndex: 'apply_date',
                flex: 1
            },{
                text: "审核状态",
                sortable: true,
                dataIndex: 'state',
                flex: 1,
                renderer:function(v){
                	if(v == 2){
                		return "拒绝";
                	}else if(v == 1){
                		return "同意";
                	}else{
                		return "未审核";
                	}
                }
            },{
               menuDisabled: true,
               sortable: false,
               width:60,
			   align: 'center',
			   xtype: 'actioncolumn',
			   items:[{iconCls: 'edit',tooltip:"审核",handler:'audit'}
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