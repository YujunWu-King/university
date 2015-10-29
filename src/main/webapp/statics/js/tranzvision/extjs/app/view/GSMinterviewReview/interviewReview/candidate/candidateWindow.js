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
    width: 600,
    height: 400,
    modal:true,
    layout: {
        type: 'fit'
    },
    items:[
            {
              xtype: 'form',
               reference: 'bmrForm',
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
                      fieldLabel:'班级ID',
                      name: 'classID',
                      hidden:true
                    },
                    {
                      xtype: 'textfield',
                      fieldLabel:'批次ID',
                      name: 'batchID',
                      hidden:true
                    },
                   {
                    xtype: 'textfield',
                    fieldLabel:'考生姓名',
                    name: 'realName',
                    ignoreChangesFlag: true,
                    listeners:{'change':function(aa,newValue){
                       // alert(newValue);拼装请求的参数

                        //提交参数
                        //alert(this.up('form').getXType());
                        var formParaes=this.up('form').getForm().getValues();
                        var classID=formParaes['classID'];
                        var batchID=formParaes['batchID'];;
                        var realName=newValue;

                        var comParas='{"classID":"'+classID+'","batchID":"'+batchID+'","realName":"'+realName+'"}';
                        var tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_APPLY_STD","OperateType":"U","comParams":' + comParas + '}';
                        //alert(this.getParentbyty('window').getXType());
                        //alert(this.up('form').child('grid').getXType());
                        var store=this.up('form').child('grid').getStore();
                        store.tzStoreParams=comParas;
                        store.reload();
                        //Ext.tzSubmit(tzParams,function(responseData){
                           /* var thisValue = responseData.reviewCount||btn.findParentByType('panel').child('form').getForm().findField('reviewNum').getValue();
                            btn.findParentByType('panel').child('form').getForm().findField('reviewNum').setValue(thisValue);
                            var grid = btn.findParentByType('panel').down("grid[name=interviewReviewStudentGrid]");
                            var store = grid.getStore();
                            if(tzParams.indexOf("add")>-1||tzParams.indexOf("delete")>-1||tzParams.indexOf("update")){
                                store.reload();
                            }*/
                       // },"",true,this);

                    }}

                },
                {
                    xtype: 'grid',
                    autoHeight:true,
                    reference:'candidateBRMapplyGrid',
                    name:'candidateBRMapplyGrid',
                    columnLines: true,
                    frame: true,
                    height:250,
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
                        xtype:'toolbar'

                    }],
                    columns: [
                        {
                            text: "班级ID",
                            dataIndex: 'classID',
                            hidden: true
                        }, {
                            text: "批次ID",
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
                            hidden:true,
                            minWidth:100,
                            flex:1,
                            filter: {
                                type: 'string',
                                itemDefaults: {
                                    emptyText: 'Search for...'
                                }
                            }
                        },{
                            text:"研究领域",
                            dataIndex:'joinedBatchs',
                            minWidth:150,
                            flex:1
                        },{
                            text:'是否有面试资格',
                            dataIndex:'isInterviewed',
                            minWidth:150,
                            hidden:true
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