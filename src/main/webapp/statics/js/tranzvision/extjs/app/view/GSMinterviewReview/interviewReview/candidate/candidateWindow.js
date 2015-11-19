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
                    fieldLabel:'申请人姓名',
                    name: 'realName',
                    ignoreChangesFlag: true,
                    timeSet:'timeSet',
                    listeners:{'change':function(aa,newValue){
                        var self = this;
                        clearTimeout(this.timeSet);
                        this.timeSet = setTimeout(function(){
                            var formParaes=self.up('form').getForm().getValues();
                            var classID=formParaes['classID'];
                            var batchID=formParaes['batchID'];;
                            var realName=newValue;

                            var comParas='{"classID":"'+classID+'","batchID":"'+batchID+'","realName":"'+realName+'"}';
                            var store=self.up('form').child('grid').getStore();
                            store.tzStoreParams=comParas;
                            store.reload();
                        },500);
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
                    bodyStyle:'overflow-y:auto;overflow-x:hidden',
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
                            text:"研究领域",
                            dataIndex:'joinedBatchs',
                            minWidth:150,
                            flex:1
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