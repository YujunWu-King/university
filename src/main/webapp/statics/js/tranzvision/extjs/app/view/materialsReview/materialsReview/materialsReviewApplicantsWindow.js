Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsWindow', {
    extend: 'Ext.window.Window',
    xtype: 'materialsReviewApplicantsWindow',
    controller: 'materialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.materialsReview.materialsReview.materialsReviewApplicantsWindowStore'
    ],
    title: '新增考生',
    width: 900,
    height: 400,
    modal:true,
    layout: {
        type: 'fit'
    },
    constructor: function (className){
        this.className=className;
        this.callParent();
    },
    initComponent:function(){
        var className =this.className;
        Ext.apply(this,{
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
                    type:'materialsReviewApplicantsWindowStore'
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
                    },{
                        text: "姓名",
                        dataIndex: 'realName',
                        minWidth: 100,
                        flex: 1,
                        filter: {
                            type: 'string',
                            itemDefaults: {
                                emptyText: 'Search for...'
                            }
                        }

                    },{
                        text: "性别",
                        dataIndex: 'gender',
                        minWidth: 15,
                        width:50,
                        filter: {
                            type: 'list'
                        }
                    },{
                        text: "报名表编号",
                        dataIndex: 'appInsID',
                        minWidth: 100,
                        filter: {
                            type: 'list'
                        }
                    },{
                        text: "报考班级",
                        dataIndex: 'className',
                        minWidth: 120,
                        width:140,
                        flex: 1,
                        filter: {
                            type: 'list',
                            value:className
                        }

                    },{
                        text: "报考批次",
                        dataIndex: 'batchName',
                        minWidth: 90,
                        width:100,
                        flex: 1,
                        filter: {
                            type: 'string',
                            itemDefaults: {
                                emptyText: 'Search for...'
                            }
                        }

                    },
                    {
                        text: "所属材料评审批次",
                        dataIndex: 'czPCName',
                        minWidth: 170,
                        renderer:function(v){
                          console.log(v);
                           var arrEMLTmpls=v.split(","),
                                resultHTML = "";
                           console.log(arrEMLTmpls);
                            for(var x=0 ;x<arrEMLTmpls.length;x++){
                                resultHTML += "<span>"+arrEMLTmpls[x]+"</span></br>";
                            }
                            console.log(resultHTML)
                            return resultHTML?resultHTML.replace(/\<\/br\>$/,''):resultHTML;
                        },
                        filter: {
                            type: 'string',
                            itemDefaults: {
                                emptyText: 'Search for...'
                            }
                        }
                    },
                    {
                        text: "初审状态",
                        dataIndex: 'FirstTrialStatus',
                        minWidth: 80,
                        filter: {
                            type: 'list',
                            value:'已通过'
                        }


                    }

                ]
            }]
        }),
            this.callParent();
    },

    buttons: [{
        text: '确定',
        iconCls:"ensure",
        handler: 'addApplicantEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'addApplicantClose'
    }]
});