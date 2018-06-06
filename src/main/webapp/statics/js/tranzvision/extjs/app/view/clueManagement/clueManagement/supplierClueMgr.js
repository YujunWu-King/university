Ext.define('KitchenSink.view.clueManagement.clueManagement.supplierClueMgr',{
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'tranzvision.extension.grid.Exporter',
        'KitchenSink.view.clueManagement.clueManagement.supplierClueController',
        'KitchenSink.view.clueManagement.clueManagement.supplierClueStore'
    ],
    xtype: 'supplierClueMgr',
    controller: 'supplierClueController',

    title: '供应商线索管理',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    columnLines:true,
    multiSelect:true,
    plugins:[{
        ptype: 'gridfilters'
    }],
    viewConfig: {
        enableTextSelection: true
    },
    selModel: {
        type: 'checkboxmodel'
    },
    header:false,
    frame:true,
    dockedItems:[
        {
            xtype:"toolbar",
            items:[
                {text:"查询",tooltip:"查询",iconCls:"query",handler:"searchClue"},"-",
                {text:"新建线索",tooltip:"新建线索",iconCls:"add",handler:"addClue"}
            ]
        },{
            xtype:"toolbar",
            dock:"bottom",
            ui:"footer",
            items:['->',{minWidth:80,text:"关闭",iconCls:"close",
            	handler: function(btn){
            		btn.findParentByType('supplierClueMgr').close();
            	}
            }]
        }
    ],
    initComponent:function(){
        var me = this;

        var supplierClueStore =new KitchenSink.view.clueManagement.clueManagement.supplierClueStore();


        //状态
        var leadStatusFilterOptions=[];//状态的过滤器
        var leadStatusStore =  new KitchenSink.view.common.store.appTransStore("TZ_LEAD_STATUS");
        leadStatusStore.load(function(records, eOpts, successful) {
            for (var i = 0; i < records.length; i++) {
                leadStatusFilterOptions.push([records[i].data.TSDesc, records[i].data.TSDesc]);
            }
        });

        //创建方式
        var createWayFilterOptions=[];//创建方式的过滤器
        var createWayStore =  new KitchenSink.view.common.store.appTransStore("TZ_RSFCREATE_WAY");
        createWayStore.load(function(records, eOpts, successful) {
            for (var i = 0; i < records.length; i++) {
                createWayFilterOptions.push([records[i].data.TSDesc, records[i].data.TSDesc]);
            }
        });

        //报考状态
        var bkStatusFilterOptions=[];//报考状态的过滤器
        var bkStatusStore =  new KitchenSink.view.common.store.appTransStore("TZ_LEAD_BMB_STATUS");
        bkStatusStore.load(function(records, eOpts, successful) {
            for (var i = 0; i < records.length; i++) {
                bkStatusFilterOptions.push([records[i].data.TSDesc, records[i].data.TSDesc]);
            }
        });

        Ext.apply(this,{
            columns:[{
                    xtype:'rownumberer',
                    width:30
                },
                {
                    text:'客户信息',
                    menuDisabled:true,
                    columns:[
                        {
                            text:'姓名',
                            lockable   : false,
                            dataIndex:'cusName',
                            width:80,
                            filter:{
                                type:'string'
                            },
                            xtype:'linkcolumn',
                            handler:'editSelClueInfo',
                            renderer:function(v) {
                                this.text=v;
                                this.tooltip=v;
                                return;
                            }
                        },{
                            text:'手机',
                            lockable   : false,
                            dataIndex:'cusMobile',
                            width:110,
                            filter:{
                                type:'string'
                            }
                        },{
                            text:'公司',
                            lockable   : false,
                            dataIndex:'comName',
                            width:130,
                            filter:{
                                type:'string'
                            }
                        },{
                            text:'职位',
                            lockable   : false,
                            dataIndex:'cusPos',
                            width:110,
                            filter:{
                                type:'string'
                            }
                        },{
                            text:'报考状态',
                            lockable:false,
                            dataIndex:"bmStateDesc",
                            width:90,
                            filter:{
                                type:'list',
                                options:bkStatusFilterOptions
                            }
                        },{
                            text:'备注',
                            dataIndex:'cusBz',
                            width:120,
                            filter:{
                                type:'string'
                            }
                        }
                    ]
                },{
                    text:'线索信息',
                    menuDisabled:true,
                    columns:[
                        {
                            text:'机构ID',
                            dataIndex:'orgID',
                            hidden:true
                        },{
                            text:'线索状态',
                            lockable:false,
                            dataIndex:"clueStateDesc",
                            width:90,
                            filter:{
                                type:'list',
                                options:leadStatusFilterOptions
                            }
                        },{
                            text:'创建时间',
                            lockable:false,
                            dataIndex:'createDate',
                            width:130,
                            renderer : Ext.util.Format.dateRenderer('Y-m-d H:i'),
                            filter:{
                                type:'date',
                                format:'Y-m-d',
                                active:true
                            }
                        },{
                            text:'创建方式',
                            lockable:false,
                            dataIndex:'createWayDesc',
                            width:90,
                            filter:{
                                type:'list',
                                options:createWayFilterOptions
                            }
                        },{
                            text:'关闭原因',
                            lockable:false,
                            dataIndex:'gbyy',
                            flex:1,
                            width: 120,
                            minWidth: 100,
                            filter:{
                                type:'string'
                            }
                        }
                    ]
                }
            ],
            store:supplierClueStore,
            bbar:{
                xtype:'pagingtoolbar',
                pageSize:20,
                store:supplierClueStore,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});
