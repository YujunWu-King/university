Ext.define('KitchenSink.view.tzLianxi.ytt.bugCk.bugMg', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.tzLianxi.ytt.bugCk.bugStore',
        'KitchenSink.view.tzLianxi.ytt.bugCk.bugController'
    ],
    xtype:'bugMg',
    controller:'bugController',
    stateful: true,
    collapsible: true,
    height: 350,
    title: 'Bug管理',
    viewConfig: {
        enableTextSelection: true
    },


    initComponent: function () {
        var me = this,
            bugStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_BUG_STATUS"),
            store = new KitchenSink.view.tzLianxi.ytt.bugCk.bugStore();

        Ext.apply(me,{
            dockedItems:[
                {
                    xtype:"toolbar",
                    items: [
                        {text:'查询',tooltip:'检索Bug数据',iconCls:"query",handler:'searchBug'}
                    ]
                }
            ],
//            selModel: {
//                type: 'checkboxmodel'
//            },
            multiSelect: true,
            columns:[{
                width:1
            },{
                text:'编号',
                width:150,
                dataIndex:'bugID',
                listeners:{
                    click: 'viewBug'
                },
                renderer:function(v){
                    return '<a href="javascript:void(0)" >'+v+'</a>';
                }
            },{
                text:'说明',
                minWidth:100,
                flex:1,
                dataIndex:'bugName'
            },{
                text:'责任人',
                width:100,
                dataIndex:'resOpr'
            },{
                text:'期望解决日期',
                width:150,
                dataIndex:'expDate'
            },{
                text:'状态',
                width:120,
                dataIndex:'bugStatus',
                renderer:function(v){
                    switch(v){
                        case '0':
                            return "新建";
                        case '1':
                            return "已分配";
                        case '2':
                            return "已修复";
                        case '3':
                            return "关闭";
                        case '4':
                            return "重新打开";
                        case '5':
                            return "取消";
                        default :
                            break;
                    }
                }
            }
            ],
            store:store,
            bbar: {
                xtype: 'pagingtoolbar',
                store:store,
                pageSize: 10,
                plugins: new Ext.ux.ProgressBarPager()
            }
        });
        this.callParent();
    }
});


