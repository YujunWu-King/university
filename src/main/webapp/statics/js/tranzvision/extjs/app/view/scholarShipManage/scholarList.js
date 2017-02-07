Ext.define('KitchenSink.view.scholarShipManage.scholarList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.scholarShipManage.scholarModel',
        'KitchenSink.view.scholarShipManage.scholarStore',
        'KitchenSink.view.scholarShipManage.scholarController'
    ],
    xtype: 'scholarList',
    controller: 'scholarController',
    reference:'scholarList',
    store: {
        type: 'scholarStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '奖学金管理',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{ 
        xtype:"toolbar", 
        dock:"bottom",
        ui:"footer",
        items:['->',{minWidth:80,text:"关闭",iconCls:"close",handler:"ScholarPanelClose"}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:'查询',iconCls:"query",handler:'queryScholar'},'-',
            {text:"新增",tooltip:"新增",iconCls:"add",handler:'addScholar'},'-',
            {text:'编辑',tooltip:'编辑',iconCls:"edit",handler:'editScholar'},'->'
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.scholarShipManage.scholarStore();
        Ext.apply(this, {
            columns: [
              {
                    text:Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_LIST_STD.TZ_SCHLR_NAME","奖学金名称"),
                    sortable: true,
                    dataIndex: 'TZ_SCHLR_NAME',
                    width: 450
                },{
                    text:Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_LIST_STD.TZ_DC_WJ_KSRQ","开始时间"),
                    sortable: true,
                    dataIndex: 'TZ_DC_WJ_KSRQ',
                    width: 150
                },{
                    text:Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_LIST_STD.TZ_DC_WJ_JSRQ","结束时间"),
                    sortable: true,
                    dataIndex: 'TZ_DC_WJ_JSRQ',
                    width: 150               
                },{ text: Ext.tzGetResourse("TZ_SCHOLAR_COM.TZ_SCHLR_LIST_STD.TZ_STATE","状态"),
                    dataIndex: 'TZ_STATE',
                    width:100,
                    renderer:function(v) {
                    if (v == 'Y') { 
                        return "有效";
                    } else if (v == 'N') {
                        return "无效";
                    }
                   }
                }, {
                    menuDisabled: true,
                    sortable: false,
                    text: '操作',
                    align: 'center',
                    xtype: 'actioncolumn',
                    width:100,
                    items:[
                        {iconCls:'edit',tooltip:'编辑',handler:'editScholarRow'},
                        {iconCls:'people',tooltip:'参与人管理',handler:'viewScholarCyr'}
                    ] 
                }
                ],
                store:store,
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

