Ext.define('KitchenSink.view.bugManagement.bugCk.bugCk', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.bugManagement.bugCk.bugCkStore',
        'KitchenSink.view.bugManagement.bugCk.bugCkController'
    ],
    xtype:'bugCk',
    style:"margin:8px",
    frame:true,
    columnLines: true,
    name:'bugCkGrid',
    controller:'bugCkController',
    stateful: true,
    stateId:'cookieBugCkGrid',
    collapsible: true,
    height: 350,
    title: '任务查看',
    viewConfig: {
        enableTextSelection: true
    },
    initComponent: function () {
        var me = this,
        bugStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_BUG_STATUS"),
        store = new KitchenSink.view.bugManagement.bugCk.bugCkStore({
            listeners:{
                beforeload:function(store , operation){
//                    if(store.isLoaded()){
//                        var filters = me.filters;
//                        if(filters){
//                            filters.clearFilters();
//                        }
//                    }
                },
                load:function(store){
//                    console.log(store);
                    if(store.loadCount==1){
                        var statusFilter = new Ext.util.Filter({
                            filterFn: function(item) {
                                return item.get('bugStatus') !='关闭'&&item.get('bugStatus') !='取消';
                            }
                        });
                      store.addFilter(statusFilter);
//                      store.filter(statusFilter);
//                      store.setFilters(statusFilter);
//                        console.log(store.getFilters());
                    }
                }
            }
        });

        Ext.apply(me,{
            plugins: [
                {
                    ptype: 'gridfilters',
                    controller: 'bugController'
                }
            ],
            multiSelect: true,
            columns:[{
                text:'编号',
                width:70,
                dataIndex:'bugID',
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
//                ,hidden:true
            },
            {
                text:'说明',
                minWidth:300,
                flex:1,
                dataIndex:'bugName',
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
            },{
                text:'状态',
                width:100,
                dataIndex:'bugStatus',
                filter: {
                    type: 'list'
/*                    ,
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
                width:70,
                dataIndex:'bugPrior',
                filter: {
                    type: 'list'
                }
            },{
                text:'进度百分比',
                width:100,
                dataIndex:'bugPercent',
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
                    width: 110,
                    dataIndex: 'bugUpdateDate',
                    xtype: 'datecolumn',
                    format: 'Y-m-d',
                    filter: {
                        type: 'date',
                        dateFormat: 'Y-m-d '
                    }
                },{
                    text: '进度更新时间',
                    width: 110,
                    dataIndex: 'bugUpdateTime'
                },
                {
                text:'录入人',
                width:100,
                dataIndex:'recOpr',
                filter: {
                    type: 'list'
                }
            },{
                text:'录入日期',
                width:150,
                dataIndex:'recDate',
                xtype: 'datecolumn',
                format:'Y-m-d',
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
                width:100,
                dataIndex:'resOpr',
                filter: {
                    type: 'list'
                }
            },{
                text:'期望解决日期',
                width:150,
                dataIndex:'expDate',
                xtype: 'datecolumn',
                format:'Y-m-d',
                filter: {
                    type: 'date',
                    dateFormat: 'Y-m-d'
                }
            }, {
                text:'测试状态',
                width:120,
                dataIndex:'bugMigration',
                filter: {
                    type: 'list'
                }
            },{
                    text: '操作',
                    width: 60,
                    xtype: 'actioncolumn',
                    align: 'center',
                    items: [
                        {iconCls: 'view', tooltip: '查看', handler: 'viewBug', action: '0'}
                    ]
            }],
            store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                store:store,
                pageSize: 800,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});


