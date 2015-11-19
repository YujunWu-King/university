Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.addJudgeWindow', {
    extend: 'Ext.window.Window',
    controller: 'GSMinterviewReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.GSMinterviewReview.interviewReview.addJudgeWindowStore'
    ],
    title: '新增评委',
    width: 800,
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
            type:'addJudgeWindowStore'
        },
        dockedItems:[{
            xtype:'toolbar',
            items:[{
                text:"清除筛选条件",tooltip:"清除筛选条件", handler:"clearCondition"
            }]
        }],
        columns: [{
                text: "评委账号",
                dataIndex: 'judgeID',
                minWidth: 75,
                flex:1,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }
            },{
                text:'评委姓名',
                dataIndex:'judgeName',
                mindWidth:75,
                flex:1,
                filter:{
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }
            },{
                text:'所属评委组',
                dataIndex:'judgeType',
                flex:1
            }
        ]
    }],
    buttons: [ {
        text: '确定',
        iconCls:"ensure",
        handler: 'addJudgeEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'addJudgeClose'
    }]
});