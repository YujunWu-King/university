Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.interviewReview', {
    extend: 'Ext.panel.Panel',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewStore',
        'KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewController',
        'KitchenSink.view.common.store.comboxStore',
        'tranzvision.extension.grid.column.Link'
    ],
    xtype: 'GSMinterviewManagment',
    columnLines: true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    controller: 'GSMinterviewReview',
    style: "margin:8px",
    title: '拟录取管理',
    viewConfig: {
        enableTextSelection: true
    },
    layout: {
        type: 'fit'
    },
    header: false,
    frame: true,
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onClassBatchSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onClassBatchEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onClassBatchClose'
    }],
    initComponent: function () {
        var store = new KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewStore(),
            self = this;
            Ext.apply(this, {
                items:[{
                    xtype: 'grid',
                    selModel: {
                        type: 'checkboxmodel'
                    },
                    multiSelect: true,
                    dockedItems: [/*{
                     xtype:"toolbar",
                     dock:"bottom",
                     ui:"footer",
                     items:['->',{minWidth:80,text:"保存",iconCls:"save"}]
                     },*/{
                        xtype: "toolbar",
                        items: [
                            {text: "查询", tooltip: "查询数据", iconCls: "query", handler: "queryClassBatch"}, '-',
                            {text: '新增', tooltip: "新增批次", iconCls: 'add', handler: 'addClassBatch'}, '-',
                            {text: '删除', tooltop: "删除批次", iconCls: 'remove', handler: 'deleteClassBatch'}
                        ]
                    }],
                    columns: [{
                        text: '班级编号',
                        dataIndex: 'classID',
                        hidden: true
                    }, {
                        text: '招聘项目',
                        dataIndex: 'className',
                        minWidth: 180,
                        sortable: false,
                        flex: 1
                    }, {
                        text: '批次编号',
                        dataIndex: 'batchID',
                        hidden: true
                    }, {
                        text: '批次',
                        dataIndex: 'batchName',
                        minWidth: 120,
                        sortable: false,
                        flex: 1
                    },{
                        header: '设置招聘领导小组',
                        xtype:'linkcolumn',
                        sortable: false,
                        flex:2,
                        value:'设置招聘领导小组',
                        /*renderer: function () {
                            return '<a class="tz-gsmmsps-lzh-cls" data-type="LD" class="tz-gsmmsps-lzh-cls" href="javaScript:void(0)">设置招聘领导小组</a>';
                        },*/
                        minWidth: 150,
                        handler:"viewLDTeam"
                    }, {
                        header: '设置学术委员会成员',
                        xtype:'linkcolumn',
                        sortable: false,
                        flex:2,
                        value:'设置学术委员会成员',
                        minWidth: 150,
                        handler:'viewXSTeam'

                    }, {
                        header: '拟录取人员名单管理',
                        xtype:'linkcolumn',
                        sortable: false,
                        flex:2,
                        value:'拟录取人员名单管理',
                        minWidth: 150,
                        handler:'viewCandidateApplicants'
                    }],
                    store: store,
                    bbar: {
                        xtype: 'pagingtoolbar',
                        pageSize: 10,
                        store: store,
                        plugins: new Ext.ux.ProgressBarPager()
                    }
                }]
            });

            this.callParent();
        /*store.load({
                callback: function () {
                    var links = document.querySelectorAll('.tz-gsmmsps-lzh-cls');
                    for (var x = links.length - 1; x >= 0; x--) {
                        if (links[x].addEventListener) {
                            links[x].addEventListener('click', function (e) {
                                e.stopPropagation();
                                var type = e.target.getAttribute('data-type');
                                self.controller.setRulesByType(type, e.target);
                            }, false);
                        } else {
                            //兼容IE
                            links[x].attachEvent("onclick", function () {
                                var e = window.event;
                                e.stopPropagation();
                                var type = e.target.getAttribute("data-type");
                                self.controller.setRulesByType(type, e.target);
                            })
                        }
                    }
                }
            })*/
    }
});

