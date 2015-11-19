Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewApplicantsWindow', {
    extend: 'Ext.window.Window',
    xtype: 'GSMmaterialsReviewApplicantsWindow',
    controller: 'GSMmaterialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewApplicantsWindowStore'
    ],
    title: '新增申请人',
    width: 1000,
    height: 450,
    modal:true,
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent:function(){
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'materialsReviewRuleForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                //bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 130,
                    labelStyle: 'font-weight:bold'
                },
                items:[ {
                    xtype: 'textfield',
                    name: 'classID',
                    hidden:true
                },
                    {
                        xtype: 'textfield',
                        name: 'batchID',
                        hidden:true
                    },{
                        layout: {
                            type: 'column'
                        } ,
                        padding:'0 0 8px 0',
                        items:[
                            {
                                xtype: 'displayfield',
                                fieldLabel: "招聘项目名称"
                            }, {
                                xtype: 'displayfield',
                                name: 'className'
                            }
                        ]}, {
                        xtype: 'grid',
                        autoHeight:true,
                        columnLines: true,
                        frame: true,
                        minHeight: 260,
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
                            type:'GSMmaterialsReviewApplicantsWindowStore'
                        },
                        dockedItems:[{
                            xtype:'toolbar',
                            items:[
                                //{text:"查询",tooltip:"添加申请人参与本批次材料评审",iconCls:"query",handler:"queryAddApplicants"},"-",
                                {text:"清除筛选条件",tooltip:"清除筛选条件", handler:"dumpTerm"
                                }]
                        }],
                        columns: [
                            {
                                text: "序号",
                                xtype: 'rownumberer',
                                width: 50,
                                align:'center'
                            }, {
                                text: "报名表编号",
                                dataIndex: 'appInsID',
                                minWidth: 100,
                                filter: {
                                    type: 'list'
                                }
                            },{
                                text: "姓名",
                                dataIndex: 'realName',
                                width: 100,
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
                                width: 70,
                                filter: {
                                    type: 'list'
                                }
                            },
                            {
                                text: "申请职位",
                                dataIndex: 'applyPosition',
                                minWidth: 90,
                                flex:2,
                                filter: {
                                    type: 'list'
                                }
                            },
                            {
                                text: "提交状态",
                                dataIndex: 'appSubYN',
                                minWidth: 100,
                                filter: {
                                    type: 'list'
                                }
                            },
                            {
                                text: "审核状态",
                                dataIndex: 'assedStatus',
                                minWidth: 10,
                                filter: {
                                    type: 'list',
                                    value:'已通过'
                                }

                            },
                            {
                                text: "标签",
                                dataIndex: 'label',
                                minWidth: 140,
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
                            }

                        ]
                    }]
            }]
        });
        this.callParent();
    },
    buttons: [
// {
//    text: '保存',
//    iconCls:"save",
//    handler: 'addApplicantSave'
//},
        {
            text: '确定',
            iconCls:"ensure",
            handler: 'addApplicantEnsure'
        }, {
            text: '关闭',
            iconCls:"close",
            handler: 'addApplicantClose'
        }]
});