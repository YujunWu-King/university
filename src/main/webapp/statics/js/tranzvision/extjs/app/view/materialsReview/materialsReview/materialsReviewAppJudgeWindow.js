
Ext.define('KitchenSink.view.materialsReview.materialsReview.materialsReviewAppJudgeWindow', {
    extend: 'Ext.window.Window',
    xtype: 'materialsReviewAppJussg',
    controller: 'materialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.materialsReview.materialsReview.materialsReviewAppJudgeStore'
    ],
    title: '评委评审',
    width: 800,
    height: 400,
    //  id:'appJudge',
    modal:true,
    layout: {
        type: 'fit'
    },
    constructor:function(action) {
        var me = this;
        this.appGrid = appGrid;
        this.appRowIndex=appRowIndex;
        this.appSelList=appSelList;
console.log(appGrid);
        console.log(appRowIndex);
        console.log(appSelList);
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
                        }

                    ],
                    store: {
                        type: 'materialsReviewAppJudgeStore'
                    },
                    dockedItems:[ {
                        xtype:"toolbar",
                        dock:"bottom",
                        ui:"footer",
                        items: ['->',{
                            text:'确定',
                            iconCls:'save',
                            handler: function (btn) {
                                var win=btn.findParentByType("window");
                                //向后台提交的报名表编号
                                var appInsID='';
                                //为单行考生
                                if(appRowIndex!=""){
                                    appInsID = appRowIndex;}
                                else{
                                    appInsID=appSelList
                                }

                                var form = appGrid.findParentByType('form');

                                var grid = btn.findParentByType('grid'),
                                    store = grid.getStore(),

                                    selectJudge = "",
                                    selectJudgeID = " " ,
                                    NoselectJudgeID = "";
                                var total = 0;
                                var tongzu = 0;
                                if (store.getCount() != 0) {
                                    for (var select = 0; select < store.getCount(); select++) {
                                        var CunZai = store.getAt(select).get('CunZai');

                                        if (CunZai == true) {
                                            var PWZBH = store.getAt(select).get('judgeGroup');
                                            total = total + 1;
                                            for (var select2 = 0; select2 < store.getCount(); select2++) {
                                                if ( store.getAt(select2).get('CunZai') == true) {
                                                    if (store.getAt(select2).get('judgeGroup') == PWZBH) {
                                                        tongzu = tongzu + 1;
                                                    }
                                                }
                                            }
                                            if (selectJudge == "") {
                                                selectJudge = store.getAt(select).get('judgeName');
                                            }
                                            else {
                                                selectJudge = selectJudge + "," + store.getAt(select).get('judgeName');

                                            }}

                                        else {
                                            if (NoselectJudgeID == "") {
                                                NoselectJudgeID = store.getAt(select).get('judgeID');
                                            }
                                            else {
                                                NoselectJudgeID = NoselectJudgeID + "," + store.getAt(select).get('judgeID');

                                            }
                                        }
                                    }
                                    var classID = store.getAt(0).get('classID'),
                                        batchID = store.getAt(0).get('batchID'),
                                        selectJudge = selectJudge;
                                    if (tongzu > total) {
                                        Ext.Msg.alert('注意', '不可为考生指定同组评委,请改正');
                                        return;
                                    }
                                    var tzParams = '{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_APPJUG_STD","OperateType":"AppJudge","comParams":{"type":"AppJudge","classID":"' + classID + '","batchID":"' + batchID + '","appInsID":"' + appInsID + '","selectJudge":"' + selectJudge + '","NOselectJudgeID":"' + NoselectJudgeID + '"}}';

                                    Ext.tzSubmit(tzParams, function (respData) {
                                        appGrid.getStore().reload();
                                        if(respData.Nomal==1){
                                            Ext.Msg.alert ("注意","在您所选的评委中,有评委指定的考生已达上限，未能成功指定评委，修改");
                                        }
                                        else{   Ext.Msg.alert ("提示","指定评委成功");
                                        }
                                        win.close();
                                    },'',true,me);
                                }
                            }
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
                            text: "选择",
                            dataIndex: 'CunZai',
                            minWidth: 100,
                            flex: 1,
                            xtype: 'checkcolumn',
                            ignoreChangesFlag: true

                        },
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