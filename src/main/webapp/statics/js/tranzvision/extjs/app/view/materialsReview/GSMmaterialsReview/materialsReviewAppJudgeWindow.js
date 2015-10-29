Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewAppJudgeWindow', {
    extend: 'Ext.window.Window',
    xtype: 'GSMmaterialsReviewAppJussg',
    controller: 'GSMmaterialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewAppJudgeStore'
    ],
    title: '为申请人指定评委',
    width: 800,
    height: 400,
    id:'GSMappJudge',
    modal:true,
    layout: {
        type: 'fit'
    },
    constructor:function(action) {
        this.appGrid = appGrid;
        this.appRowIndex=appRowIndex;
        Ext.apply(this, {
        items: [
            {
                xtype: 'grid',
                autoHeight: true,
                columnLines: true,
                frame: true,

                style: 'border:0',
                plugins: [
                    {
                        ptype: 'gridfilters'
// ,
//                controller: 'appFormClass'
                    }

                ],
                selModel: {
                    type: 'checkboxmodel'
                },
                store: {
                    type: 'GSMmaterialsReviewAppJudgeStore'
                },
                dockedItems:[ {
                xtype:"toolbar",
                dock:"bottom",
                ui:"footer",
                items: ['->',{
                    text:'确定',
                    iconCls:'save',
                    handler: function (btn) {
                        var  record = appGrid.getStore().getAt(appRowIndex);
                        var form=appGrid.findParentByType('form');
                        console.log(form);
                        var judgeList=record.data.judgeList;
                        var grid=btn.findParentByType('grid'),
                         store =grid .getStore(),
                        select=  grid.getSelectionModel().getSelection(),
                        selectJudge="";
                        if(select.length=='0')
                        {
                            btn.findParentByType("window").close();
                            return;
                        }
                        for (var i=0;i<select.length;i++){
                            var judgeName=select[i].data.judgeName;
                                if (selectJudge==""){
                                    selectJudge=judgeName;
                                }else{
                                    selectJudge=selectJudge+","+judgeName;
                                }
                        }
                        var classID=select[0].data.classID,
                            batchID = select[0].data.batchID,
                            appInsID=record.data.appInsID,
                            selectJudge=selectJudge;
//                       var tzStoreParams = '',
//                                JSONData={};
//                                JSONData.classID = select[0].data.classID;
//                            JSONData.batchID = select[0].data.batchID;
//                           JSONData.selectJudge=selectJudge;

                    var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_APPJUG_STD","OperateType":"AppJudge","comParams":{"type":"AppJudge","classID":"' + classID + '","batchID":"' + batchID + '","appInsID":"' + appInsID + '","selectJudge":"' + selectJudge + '"}}';
                   console.log(tzParams);
                        Ext.tzSubmit(tzParams, function (respData) {
                            var msg=respData.Nomal;
                            Ext.Msg.alert("注意",msg);
                         appGrid.getStore().reload();
        });
                        btn.findParentByType("window").close();}
//                  if(record.data.judgeList.indexOf(judgeName) == -1){
//                            if (judgeList==""){
//                                judgeList=judgeName;
//                            }
//                            else{
//                                judgeList=judgeList+ "," +judgeName;
//                            }
//                        }
//
//
//                        record.set('judgeList',judgeList);

                }, {
                    text: '关闭',
                    iconCls: "close",
                    handler: function(btn){
                        btn.findParentByType("window").close();
                    }
                }]
            }],
                columns: [

                    {
                        text: "评委帐号",
                        dataIndex: 'judgeID',
                        minWidth: 100,
                        flex: 1
                    },
                    {
                        text: "评委",
                        dataIndex: 'judgeName',
                        minWidth: 100,
                        flex: 1
                    },{
                        text: "评委类型",
                        dataIndex: 'judgeType',
                        minWidth: 100,
                        flex: 1
                    },
                    {
                        text: "所属评委组",
                        dataIndex: 'judgeGroup',
                        minWidth: 100,
                        flex: 1
                    }
                ]

            }
        ]
        });
        this.callParent();
    }
});