//面试异常数据处理-班级批次列表  默认首页
Ext.define('KitchenSink.view.enrollmentManagement.interviewErrorData.interviewList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.enrollmentManagement.interviewErrorData.interviewListStore',
        'KitchenSink.view.enrollmentManagement.interviewErrorData.interviewErrorDataController',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'appFormList',
    columnLines: true,
    controller: 'errorDataController',
    name:'appFormListInterview',
    /*style:"margin:8px",*/
    multiSelect: true,
    title: "面试信息列表",
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[
        {
        xtype:"toolbar",
        items:[
            {
                text:"查询",
                iconCls:"query",
                handler:'queryClassList'
            }
        ]},
        {
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {
                minWidth:80,
                text:"关闭",
                iconCls:"close",
                handler: 'onSyClose'
            }]
        }
    ],
    initComponent: function () {
		var store = new KitchenSink.view.enrollmentManagement.interviewErrorData.interviewListStore();
		store.load();
        Ext.apply(this, {
            columns: [{
                xtype: 'rownumberer',
                width:50
            },{
                text:"报考方向",
                minWidth:120,
                width:150,
                dataIndex:'className',
                flex:1
            },{
                text:"申请批次",
                dataIndex: 'batchName',
                width:300
            },{
                text: "操作",
                menuDisabled: true,
                sortable: false,
                width: 50,
                align: 'center',
                xtype: 'actioncolumn',
                items: [{iconCls: 'view',tooltip:'查看',handler:'errorDataShow'}]
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

