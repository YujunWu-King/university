Ext.define('KitchenSink.view.interviewManagement.classManage.classManagement', {
    extend: 'Ext.grid.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.interviewManagement.classManage.classModel',
        'KitchenSink.view.interviewManagement.classManage.classStore',
        'KitchenSink.view.interviewManagement.classManage.classController',
        'tranzvision.extension.grid.column.Link',
		'KitchenSink.view.interviewManagement.classBatchChoose.msBatchModel',
        'KitchenSink.view.interviewManagement.classBatchChoose.msBatchStore'
    ],
    xtype: 'classManagement',
    columnLines: true,
//    selModel: {
//        type: 'checkboxmodel'
//    },
    controller: 'iterviewMgClass',
	style:"margin:8px",
    multiSelect: true,
    title: '班级管理',
    viewConfig: {
        enableTextSelection: true
    },
	header:false,
	frame: true,
    dockedItems:[/*{
		xtype:"toolbar",
		dock:"bottom",
		ui:"footer",
		items:['->',{minWidth:80,text:"保存",iconCls:"save",handler:'saveResSets'}]
		},*/{
		xtype:"toolbar",
		items:[
			{text:"查询",tooltip:"查询数据",iconCls:"query",handler:'queryClassInfo'}
        ]
	}],
    initComponent: function () {
        var store = new KitchenSink.view.interviewManagement.classManage.classStore();
        Ext.apply(this, {
            columns: [{
                text: '序号',
                xtype: 'rownumberer',
                width:50
            }/*,{
                text: '班级编号',
                dataIndex: 'classID',
                hidden:true
            }*/,{
                text: '班级名称',
				dataIndex: 'className',
                minwidth:250,
                flex:2
            },{
                text: '所属项目',
                dataIndex: 'projectName',
                minwidth:110,
                flex:1
            },{
                text: '项目类别',
                dataIndex: 'projectType',
                minwidth:110,
                flex:1
            },{
                text: '报名人数',
                dataIndex: 'applicantsNumber',
                minwidth:110,
                flex:1
            },{
                text:Ext.tzGetResourse("TZ_MS_MGR_COM.TZ_MS_MGR_CLS_STD.noauditNumber","未审核人数"),
                dataIndex: 'noauditNumber',
                width:140,
                renderer: function(val){
                    if(val>0){
                        return '<span  style="color: #ED0048;" >'+val+'</span>';
                    }else{
                        return val;
                    }
                }
            },{
                xtype:'linkcolumn',
                text:"面试时间安排",
                width:130,
				//flex:1,
                sortable:false,
                items:[{
                    text:"面试时间安排",
                    handler: "interviewArrange",
                    tooltip:"面试时间安排"
                }]
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

