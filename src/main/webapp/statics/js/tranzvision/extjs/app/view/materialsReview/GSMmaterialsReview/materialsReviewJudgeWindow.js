Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewJudgeWindow', {
    extend: 'Ext.window.Window',
    xtype: 'GSMmaterialsReviewApplicantsWindow',
    controller: 'GSMmaterialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewJudgeWindowStore'
    ],
    title: '用户账号列表',
    width: 1000,
    height: 400,
    modal:true,
    layout: {
        type: 'fit'
    },
    items: [{
        xtype: 'grid',
        autoHeight:true,
        columnLines: true,
        frame: true,
        style:'border:0',
        plugins: [
            {
                ptype: 'gridfilters',
                controller: 'appFormClass'
            }

        ],
        selModel: {
            type: 'checkboxmodel'
        },
        store: {
            type:'GSMmaterialsReviewJudgeWindowStore'
        },
        dockedItems:[{
            xtype:'toolbar',
            items:[{
                text:"清除筛选条件",tooltip:"清除筛选条件", handler:"dumpTerm"
            }]
        }],
        columns: [
            {
                text: "序号",
                xtype: 'rownumberer',
                width: 50,
                align:'center'
            }, {
                text: "评委帐号",
                dataIndex: 'judgeID',
                minWidth: 100,
                flex:1,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }
            },{
                text: "评委姓名",
                dataIndex: 'judgeName',
                minWidth:100,
                flex:1,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }
            },{
                text: "所属系",
                dataIndex: 'judgeDepart',
                minWidth:100,
                flex:1,
                filter: {
                    type: 'list'
                }
            },{
                text: "评委类型",
                dataIndex: 'judgeType',
                minWidth:100,
                flex:1,
                filter: {
                    type: 'list'
                }
            },{
                text: "手机",
                dataIndex: 'judgePhoneNumber',
                width:130,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }
            },{
                text: "邮箱",
                dataIndex: 'judgeEmail',
                width:130,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }

            },{
                text: "对应OA账号",
                dataIndex: 'judgeOAID',
                width:130,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }

            }
        ]
    }],
    buttons: [
//        {
//        text: '保存',
//        iconCls:"save",
//        handler: 'addJudgeSave'
//    },
        {
        text: '确定',
        iconCls:"ensure",
        handler: 'addJudgeEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'addJudgeClose'
    }]
});