Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateWindow', {
    extend: 'Ext.window.Window',
    controller: 'candidateController',
    reference:'candidateWindow',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateWindowStore'
    ],
    title: '新增拟录取人员名单',
    width: 800,
    height: 600,
    modal:true,
    layout: {
        type: 'fit'
    },
    items:[
            {
              xtype: 'form',
               reference: 'bmlcInforForm',
               layout: {
                type: 'vbox',
                align: 'stretch'
               },
               border: false,
               //bodyPadding: 10,
               style:"margin:8px",
               //heigth: 300,
               bodyStyle:'overflow-y:auto;overflow-x:hidden',

               fieldDefaults: {
                msgTarget: 'side',
                labelWidth: 100,
                labelStyle: 'font-weight:bold'
             },
            items:[
                    {
                      xtype: 'textfield',
                      name: 'classID'

                    },
                    {
                      xtype: 'textfield',
                      name: 'batchID'

                    },
                    {
                       xtype: 'grid',
                       autoHeight:true,
                       columnLines: true,
                       frame: true,
                       style:'border:0',
                       /* plugins: [
                                      {
                                   ptype: 'gridfilters',
                                   controller: 'appFormClass'
                                  }

                                  ],*/
                     selModel: {
                            type: 'checkboxmodel'
                    },
                    store: {
                             type:'candidateWindowModel'
                    },
                    dockedItems:[{
                             xtype:'toolbar',
                    items:[{
                             text:"清除筛选条件",tooltip:"清除筛选条件", handler:"clearCondition"
                            }
                    ]
                    }],
                   columns: [
                        {
                            dataIndex: 'classID',
                            hidden: true
                        }, {
                            dataIndex: 'batchID',
                            hidden: true
                        }, {
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
                            dataIndex:'batchName',
                            minWidth:100,
                            flex:1,
                            filter: {
                                type: 'string',
                                itemDefaults: {
                                    emptyText: 'Search for...'
                                }
                            }
                        },{
                            text:"参与批次",
                            dataIndex:'joinedBatchs',
                            minWidth:100,
                            flex:1,
                            filter: {
                                type: 'string',
                                itemDefaults: {
                                    emptyText: 'Search for...'
                                }
                            }
                        },{
                            text:'是否有面试资格',
                            dataIndex:'isInterviewed',
                            minWidth:150,
                            filter: {
                                type: 'list',
                                value:'是'
                            }

                        }
                   ]
             }
            ]
       }
    ],
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