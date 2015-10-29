Ext.define('KitchenSink.view.bugManagement.bugMg.bugMg', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'Ext.grid.feature.Grouping',
        'KitchenSink.view.bugManagement.bugMg.bugMgStore',
        'KitchenSink.view.bugManagement.bugMg.bugMgController',
        'tranzvision.extension.grid.Exporter'
    ],
    xtype:'bugMg',
    columnLines: true,
    style:"margin:8px",
    frame:true,
    name:'bugMgGrid',
    controller:'bugMgController',
    stateful: true,
    stateId:'cookieBugMgGrid',
    multiSelect: true,
    title: '任务管理',
    viewConfig: {
        enableTextSelection: true
    },
    features: [{
        ftype: 'grouping',
        groupHeaderTpl: '{columnName}: {name} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',
        hideGroupedHeader: true,
        startCollapsed: true,
        id: 'moduleGrouping'
    }],
    initComponent: function () {
        var me = this,
            bugMgStore = new KitchenSink.view.bugManagement.bugMg.bugMgStore({
                listeners:{
                    load:function(store,records){
//                        me.groupingFeature.collapseAll();/*load的时候折叠*/
                    }
                }});
        bugMgStore.sort('bugID','DESC');/*store中加sortinfo只对谷歌浏览器有效，火狐和其他无效，因此在这里加排序方式，addsorted才有效（只针对本地排序）*/
        Ext.apply(me,{
            dockedItems:[
                {
                    overflowHandler: 'Menu',
                    xtype:"toolbar",
                    items: [
                        {text: "查询",tooltip: "查询", iconCls:"query", handler: 'searchBug'},"-",
                        {text: "新增",tooltip: "新增", iconCls:"add", handler: 'addBug'},"-",
                        {text: "编辑",tooltip: "编辑", iconCls:"edit", handler: 'editBug'},"-",
                        {text: "删除",tooltip: "删除", iconCls:"remove", handler: 'deleteBugs'}
                        ,'->',
                        {
                            xtype:'splitbutton',
                            text:"更多操作",
                            iconCls:  'list',
                            glyph: 61,
                            menu:[
                                {
                            text:"导出列表数据",
                                iconCls:"excel",
                                handler:function(btn){
                                    var grid = btn.findParentByType('grid');
                                    grid.saveDocumentAs({
                                        type: 'excel',
                                        title: '任务管理列表信息',
                                        fileName: '任务管理列表信息.xls'
                                    });
                                }
                            },
                                {
                                text:"批量发送邮件",
                                iconCls:"email",
                                menu:[{
                                    text:"发送任务分配邮件",
                                    iconCls:"email",
                                    name:'sendBugAssignEmails',
                                    handler:'sendBugAssignEmails'
                                },
                                    {
                                        text:"发送复测通知邮件",
                                        iconCls:"email",
                                        name:'sendBugRetestEmails',
                                        handler:'sendBugRetestEmails'
                                    }]

                            },
                                {
                                text:"预设任务查询",
                                iconCls:"query",
                                menu:[
                                    {
                                        text:"未分配的记录",
                                        iconCls:"query",
                                        handler:'queryUnassigned'
                                    },{
                                        text:"未更新进度的记录",
                                        iconCls:"query",
                                        handler:'queryNotUpdated'
                                    }
                                ]
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype:"toolbar",
                    dock:"bottom",
                    ui:"footer",
                    items: ['->',{
                        text:'保存',
                        iconCls:'save',
                        handler:"bugMgSave"
                    }]
                }
            ],
            plugins:[
                {
                    ptype: 'gridfilters',
                    controller: 'bugMgController'
                },
                {
                    ptype: 'gridexporter'
                }],
            selModel: {
                type: 'checkboxmodel'
            },
            columns:[
                {
                    text:'编号',
                    width:80,
                    dataIndex:'bugID' ,
                    filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }
                },
                {
                    text:'功能模块',
                    width:150,
                    dataIndex:'module' ,
                    filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    }
                },
                {
                    text:'说明',
                    minWidth:200,
                    flex:1,
                    dataIndex:'bugName' ,
                    filter: {
                        type: 'string',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    },
                    renderer:function(v){
                        return '<a href="javascript:void(0)" >'+v+'</a>';
                    }
                    ,
                    listeners:{
                        click:'editBugByLink'
                    }
                },
                {
                    text:'状态',
                    width:110,
                    dataIndex:'bugStatus' ,
                    filter: {
                        type: 'list'
                        /*,
                        value:[
                            '新建',
                            '已分配',
                            '已完成(待审核)',
                            '重新打开',
                            '暂时搁置'
                        ],
                        active:true*/
                    }
                },{
                    text:'类型',
                    width:100,
                    dataIndex:'bugType',
                    filter: {
                        type: 'list'
                    }
                },{
                    text:'优先级',
                    width:80,
                    dataIndex:'bugPrior',
                    filter: {
                        type: 'list'
                    }
                },{
                    text:'进度百分比',
                    width:100,
                    dataIndex:'bugPercent' ,
                    filter: {
                        type: 'number',
                        itemDefaults: {
                            emptyText: 'Search for...'
                        }
                    },
                    renderer:function(v){
                        return v+'%';
                    }
                },{
                    text: '进度更新日期',
                    width: 120,
                    dataIndex: 'bugUpdateDate',
                    xtype: 'datecolumn',
                    format: 'Y-m-d',
                    filter: {
                        type: 'date',
                        dateFormat: 'Y-m-d '
                    }
                },{
                    text: '进度更新时间',
                    width: 120,
                    dataIndex: 'bugUpdateTime'
/*                },{
                    text: '最后更新',
                    width: 160,
                    dataIndex: 'bugUpdateDTTM'
                    ,
                    xtype: 'datecolumn',
//                    format: 'h:i AM',
//                    format: 'Y-m-d H:i:s',
                    format: 'Y-m-d H:i',
                    filter: {
                        type: 'date',
                        dateFormat: 'Y-m-d H:i'
                    },
                    hidden:true*/
                },{
                    text:'录入人',
                    width:80,
                    dataIndex:'recOpr',
                    filter: {
                        type: 'list'
                    }
                },{
                    text:'录入日期',
                    width:120,
                    dataIndex:'recDate',
                    xtype: 'datecolumn',
                    format:'Y-m-d' ,
                    filter: {
                        type: 'date',
                        dateFormat: 'Y-m-d'
                    }
                },{
                    text:'预计结束日期',
                    width:120,
                    dataIndex:'estDate',
                    xtype: 'datecolumn',
                    format:'Y-m-d',
                    filter: {
                        type: 'date',
                        dateFormat: 'Y-m-d'
                    }
                },{
                    text:'责任人',
                    width:80,
                    dataIndex:'resOpr' ,
                    filter: {
                        type: 'list'
                    }
                },{
                    text:'期望解决日期',
                    width:120,
                    dataIndex:'expDate',
                    xtype: 'datecolumn',
                    format:'Y-m-d',
                    filter: {
                        type: 'date',
                        dateFormat: 'Y-m-d'
                    }
                },{
                    text:'测试状态',
                    width:120,
                    dataIndex:'bugMigration' ,
                    filter: {
                        type: 'list'
                    }
                },{
                    text:'操作',
                    width:60,
                    xtype: 'actioncolumn',
                    align:'center',
                    items: [
                        {iconCls: 'edit', tooltip: '修改',handler:'editBug',action:'0'},
                        {iconCls: 'remove', tooltip: '删除',handler:'deleteBug'}
                    ]
                }],
            store:bugMgStore,
            bbar: {
                xtype: 'pagingtoolbar',
                store: bugMgStore,
                pageSize: 300,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
//        me.groupingFeature = me.getView().getFeature('moduleGrouping');
    }
});


