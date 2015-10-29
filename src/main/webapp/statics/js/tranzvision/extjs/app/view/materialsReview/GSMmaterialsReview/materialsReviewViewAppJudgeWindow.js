Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewViewAppJudgeWindow', {
    extend: 'Ext.window.Window',
    xtype: 'GSMmaterialsReviewAppJugDetail',
    reference: 'GSMmaterialsReviewAppJugDetail',
    controller: 'GSMmaterialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewViewAppJudgeStore'
    ],
    title: '查看申请人评审得分',
    width: 600,
    height: 400,
    modal:true,
    layout: {
        type: 'fit'
    },
    scoreType:"" ,
    constructor: function(scoreType){
        this.scoreType = scoreType;
        this.callParent();
    },
    initComponent:function(){
        var scoreType = this.scoreType;
        var scoreItems = [ {
            text: "评委姓名",
            dataIndex: 'judgeRealName',
            minWidth: 100

        }, {
            text: "报名表编号",
            dataIndex: 'appInsID',
            minWidth: 100
        }
            , {
                text: "申请人姓名",
                dataIndex: 'studentRealName',
                width: 100,
                flex: 1
            }];
        if(scoreType.length==1){
            tItems={
                text: scoreType[0],
                name: scoreType[0],
                align: 'center',
                dataIndex: 'score',
                flex:1,
                renderer: function (v, metaData) {
                    //console.log(v);
                    var headText = metaData.column.name;
                    // console.log(headText);
                    var arr = Ext.JSON.decode(v),
                        resultHTML = "";
                    for (var x = 0; x < arr.length; x++) {
                        // console.log(arr[x].name, headText)
                        if (arr[x].name == headText) {

                            resultHTML = arr[x].value;
                        }
                        else {

                        }
                    }
                    //console.log(resultHTML)

                    return resultHTML;
                }
            }
            scoreItems.push(tItems);
        }
        else {
            for (var i = 0; i < scoreType.length; i++) {
                var tItems = {
                    text: scoreType[i],
                    name: scoreType[i],
                    align: 'center',
                    dataIndex: 'score',
                    renderer: function (v, metaData) {
                        //console.log(v);
                        var headText = metaData.column.name;
                        // console.log(headText);
                        var arr = Ext.JSON.decode(v),
                            resultHTML = "";
                        for (var x = 0; x < arr.length; x++) {
                            // console.log(arr[x].name, headText)
                            if (arr[x].name == headText) {



                                resultHTML = arr[x].value;
                            }
                            else {

                            }
                        }
                        //console.log(resultHTML)

                        return "<span title='"+resultHTML+"'>"+resultHTML+"</span>";
                    }
                };
                scoreItems.push(tItems);
            }
        }


        Ext.apply(this,{
            items: [{
                xtype: 'grid',
                autoHeight:true,
                //xtype: 'grouped-header-grid',
                columnLines: true,
                frame: true,
                style:'border:0',
                store: {
                    type:'GSMmaterialsReviewViewAppJudgeStore'
                },
                columns: scoreItems
//                     {
//                       text:'评委评审得分   ',
//                       // dataIndex:'score',
//                        minWidth:250,
//                        columns:scoreItems
//
//                    }

//        listeners: {
//            afterRender: function(grid) {
//                grid.store.load();
//            }
//        }
            }]
        })
        this.callParent();
    }

});