Ext.define('KitchenSink.view.template.survey.testQuestion.testWjdcInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.template.survey.testQuestion.testWjdcModel',
        'KitchenSink.view.template.survey.testQuestion.testWjdcController',
        'KitchenSink.view.template.survey.testQuestion.testWjdcStore',
        'tranzvision.extension.grid.Exporter'
    ],
    xtype: 'testWjdcInfo',
    controller: 'testWjdcController',
    reference:'testWjdcInfo',
    store: {
        type: 'testWjdcStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    plugins:[
        {ptype: 'gridexporter'}
    ],
    style:"margin:8px",
    multiSelect: true,
    title: '测试问卷管理',
    viewConfig: {
        enableTextSelection: true
    },
    header:false,
    frame: true,
    dockedItems:[{ 
        xtype:"toolbar", 
        dock:"bottom",
        ui:"footer",
        items:['->',{minWidth:80,text:"关闭",iconCls:"close",handler:"CsWjdcClose"}
        ]
    },{
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:'查询',iconCls:"query",handler:'findCsWjdc'},'-',
            {text:"新增",tooltip:"新增",iconCls:"add",handler:'addCsWjdc'},'-',
            {text:'编辑',tooltip:'编辑',iconCls:"edit",handler:'editCsWjdc'},'->'
        ]
    }],
    initComponent: function () {
        var store = new KitchenSink.view.template.survey.testQuestion.testWjdcStore();
        Ext.apply(this, {
            columns: [
               /* {
                    text:Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_LIST_STD.TZ_CS_WJ_ID","测试问卷ID"),
                    sortable: true,
                    dataIndex: 'TZ_CS_WJ_ID',
                    hidden   : true
                }, {
                    text:Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_LIST_STD.TZ_DC_WJ_ID","问卷ID"),
                    sortable: true,
                    dataIndex: 'TZ_DC_WJ_ID',
                    hidden   : true
                },*/{
                    text:Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_LIST_STD.TZ_CS_WJ_NAME","问卷名称"),
                    sortable: true,
                    dataIndex: 'TZ_CS_WJ_NAME',
                    flex:1
                },{
                    text:Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_LIST_STD.TZ_DC_WJ_KSRQ","开始日期"),
                    sortable: true,
                    dataIndex: 'TZ_DC_WJ_KSRQ',
                    width: 150
                },{
                    text:Ext.tzGetResourse("TZ_CSWJ_LIST_COM.TZ_CSWJ_LIST_STD.TZ_DC_WJ_JSRQ","结束日期"),
                    sortable: true,
                    dataIndex: 'TZ_DC_WJ_JSRQ',
                    width: 150               
                },{ text: '状态',
                    dataIndex: 'TZ_STATE',
                    width:100,
                    renderer:function(v) {
                    if (v == '0') {
                        return "有效";
                    } else if (v == '1') {
                        return "无效";
                    }
                   }
                }, {
                    menuDisabled: true,
                    sortable: false,
                    text: '操作',
                    align: 'center',
                    xtype: 'actioncolumn',
                    width:120,
                    items:[
                        {iconCls:'edit',tooltip:'编辑',handler:'editTestWjdc'},
                        {iconCls:' view',tooltip:'调查详情',handler:'viewCsWjdcDetail',
                            isDisabled:function(view ,rowIndex ,colIndex ,item,record ){
                                if(record.get('TZ_DC_WJ_ID')==''){
                                    return true;
                                }else{
                                    return false;
                                }
                            } 
                        },
                        {iconCls:' frequency_report',tooltip:'汇总统计',handler:'csWjdcSumStatic',
                            isDisabled:function(view ,rowIndex ,colIndex ,item,record ){
                                if(record.get('TZ_DC_WJ_ID')==''){
                                    return true;
                                }else{
                                    return false;
                                }
                            }
                        }
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

