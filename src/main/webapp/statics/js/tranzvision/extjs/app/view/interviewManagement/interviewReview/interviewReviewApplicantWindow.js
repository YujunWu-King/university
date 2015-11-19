Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicantWindow', {
    extend: 'Ext.window.Window',
    controller: 'interviewReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicantsWindowStore'
    ],
    title: '新增考生',
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
            type:'interviewReviewApplicantsWindowStore'
        },
        dockedItems:[{
            xtype:'toolbar',
            items:[
                {text:"清除筛选条件",tooltip:"清除筛选条件", handler:"clearCondition"},"-",
                {text:"搜索",iconCls:'query',tooltip:"从所有考生中搜索",handler:"searchFromAll"}
            ]
        }],
        columns: [
            {
                text:'行号',
                xtype:'rownumberer',
                minWidth:35
            },{
                text: "姓名",
                dataIndex: 'realName',
                minWidth: 75,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }

            }, {
                text: "性别",
                dataIndex: 'gender',
                minWidth: 30,
                filter: {
                    type: 'list'
                }
            },{
                text: "报名表编号",
                dataIndex: 'appINSID',
                minWidth: 100,
                flex:1,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                }
            },{
                text:"报考批次",
                dataIndex:'batchID',
                minWidth:100,
                flex:1,
                filter: {
                    type: 'list'
                }
            },{
                text:"参与批次",
                dataIndex:'joinedBatchs',
                minWidth:100,
                flex:1,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...',
                    }
                }
            },{
                text:'是否有面试资格',
                dataIndex:'isInterviewed',
                minWidth:150,
                filter: {
                    type: 'list',
                    options:['是','无','待定']
                }

            }
        ]
    }],
    buttons: [ {
        text: '确定',
        iconCls:"ensure",
        handler: 'addApplicantEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'addApplicantClose'
    }]
});