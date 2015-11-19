/**
 * Created by luyan on 2015/11/16.
 */


Ext.define('KitchenSink.view.tzLianxi.LY.DBRWList.dbrwList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.LY.DBRWList.dbrwListController',
        'KitchenSink.view.tzLianxi.LY.DBRWList.dbrwListModel',
        'KitchenSink.view.tzLianxi.LY.DBRWList.dbrwListStore',
        'tranzvision.extension.grid.Exporter'
    ],
    xtype: 'dbrwList',
    controller: 'dbrwListController',
    name:"dbrwListGrid",
    stateful:true,
    stateId:'cookieDbrwListGrid',
    store:{
        type:'dbrwListStore'
    },
    columnLines: true,
    selModel: {
        type: 'checkboxmodel'
    },
    style:"margin:8px",
    multiSelect: true,
    title: '待办任务列表',
    viewConfig: {
        enableTextSelection: true
    },
    tmpDbrwTypeId:'',
    header:false,
    frame: true,
    dockedItems:[{
        xtype:"toolbar",
        dock:"bottom",
        ui:"footer",
        items:['->',
            {minWidth:80,text:"关闭",iconCls:"close",handler:'closeDbrwList'}
        ]},
        {
        xtype:"toolbar",
        items:[
            {text:"查询",tooltip:"查询数据",iconCls: "query",handler:'searchDbrwList'},"-",
            {text:"查看",tooltip:"查看数据",iconCls: 'view',handler:'viewDbrwInfo'},'->',
            {
                xtype:'combobox',
                fieldLabel:'',
                name:'dbrwType',
                queryMode:'remote',
                width:'260',
                editable:false,
                valueField:'TValue',
                displayField:'TSDesc',
                listeners: {
                    afterrender: function(tvType) {
                        var tzParams = '{"ComID":"TZ_DBRW_LIST_COM","PageID":"TZ_DBRW_LIST_STD","OperateType":"QF","comParams":"{}"}';
                        Ext.tzLoad(tzParams,function(responseData){
                            var store = new Ext.data.Store({
                                fields:['TValue','TSDesc','TLDesc'],
                                data:responseData.TransList
                            });
                            tvType.setStore(store);
                            if(store.getCount()>0) {
                                tvType.setValue(store.getAt(0).get("TValue"));
                            }
                        });
                    },
                    change: function (tvType,newValue,oldValue,eOpts) {
                        this.tmpDbrwTypeId=newValue;
                        var store = new KitchenSink.view.tzLianxi.LY.DBRWList.dbrwListStore(newValue);
                        if(newValue==''){
                            tvType.findParentByType("grid").store.load();
                            tvType.findParentByTyee("grid").child("pagingtoolbar").store.load();
                        }else{
                            tvType.findParentByType("grid").setStore(store);
                            tvType.findParentByType("grid").child("pagingtoolbar").setStore(store);
                        }
                    }
                }
            }
        ]
    }],
    initComponent: function () {

        this.title = "待办任务列表";

        //待办任务列表
        var store = new KitchenSink.view.tzLianxi.LY.DBRWList.dbrwListStore();
        Ext.apply(this, {
            plugins:[
                {
                    ptype:'gridfilters',
                    controller:'dbrwListController'
                },
                {
                    ptype:'gridexporter'
                }
            ],
            columns: [{
                text: '任务名称',
                dataIndex: 'taskName',
                width: 200,
                filter:{
                    type:'string',
                    itemDefaults:{
                        emptyText:'Search for...'
                    }
                }
            },{
                text: '优先级',
                dataIndex: 'priority',
                minWidth: 60,
                filter:{
                    type:'list'
                }
            },{
                text: '步骤超时时间',
                dataIndex: 'stepTmOut',
                minWidth: 180
            },{
                text: '步骤超时',
                dataIndex: 'stepTmOutFlag',
                width: 80,
                filter:{
                    type:'list'
                }
            },{
                text: '流程超时时间',
                dataIndex: 'flowTmOut',
                minWidth: 180
            },{
                text:'流程超时',
                dataIndex: 'flowTmOutFlag',
                minWidth: 80,
                filter:{
                    type:'list'
                }
            },{
                text: '任务类型',
                dataIndex: 'taskType',
                minWidth: 80,
                filter:{
                    type:'list'
                }
            },{
                text: '任务产生人',
                dataIndex: 'taskCreateOprid',
                width: 100,
                filter:{
                    type:'list'
                }
            },{
                text: '产生日期',
                dataIndex: 'taskCreateDate',
                minWidth: 120,
                xtype:'datecolumn',
                format:'Y-m-d',
                filter:{
                    type:'date',
                    dateFormat:'Y-m-d'
                }
            },{
                text:'产生时间',
                dataIndex:'taskCreateTime',
                minWidth:120
            },{
                text: '处理时间',
                dataIndex: 'dealDttm',
                minWidth: 180
            },{
                text: '任务状态',
                dataIndex: 'taskStatus',
                minWidth: 80,
                filter:{
                    type:'list'
                }
            },{
                menuDisabled: true,
                sortable: false,
                width:50,
                xtype: 'actioncolumn',
                items:[
                    {iconCls: 'view',tooltip: '查看',handler: 'viewSelDbrwInfo'}
                    /*
                    {iconCls: 'edit',tooltip: '编辑',handler: 'editSelUserAccount'},
                    {iconCls: 'remove',tooltip: '删除',handler: 'deleteUserAccount'}
                    */
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
