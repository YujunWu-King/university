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
    width: 1300,
    height: 400,
    modal:true,
    layout: {
        type: 'fit'
    },
    constructor: function (classID,batchID,className){
        this.classID=classID;
        this.batchID=batchID;
        this.className=className;
        this.callParent();
    },
    initComponent:function(){
        var classID=this.classID,
             batchID=this.batchID,
            className =this.className;
        console.log("initComponent")
        var columns=[
            {
                text: "序号",
                xtype: 'rownumberer',
                width: 50,
                align:'center'
            },{
                text: "姓名",
                dataIndex: 'realName',
                minWidth: 100,
                width:120,
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
                width:60,
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
                width:90,
                flex: 1,
                filter: {
                    type: 'list'
                }
            },{
                text: "报考批次",
                dataIndex: 'batchName',
                minWidth: 90,
                width:100,
                hidden:true,
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
                minWidth: 200,
                flex: 1,
//                        renderer:function(v){
//
//                            var arrEMLTmpls=v.split(","),
//                                resultHTML = "";
//                            console.log(arrEMLTmpls);
//                            for(var x=0 ;x<arrEMLTmpls.length;x++){
//                                resultHTML += "<span>"+arrEMLTmpls[x]+"</span></br>";
//                            }
//                            console.log(resultHTML)
//                            return resultHTML?resultHTML.replace(/\<\/br\>$/,''):resultHTML;
//                        },
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


            },{
                text: "提交时间",
                dataIndex: 'submitTime',
                minWidth: 80,
                width:150,
                filter: {
                    type: 'string',
                    itemDefaults: {
                        emptyText: 'Search for...'
                    }
                },
                renderer:function(v){
console.log('time');
                    var arrEMLTmpls=v.split(" ")
                    return arrEMLTmpls[0];

                }


            }
        ];
        var tzAddAppColsParams ='{"ComID":"TZ_REVIEW_CL_COM","PageID":"TZ_CLPS_ADDAPP_STD",' +
            '"OperateType":"getAddStudentColumns","comParams":{"type":"getAddStudentColumns", "classID":"'+classID+'","batchID":"'+batchID+'"}}';
        var applicationCoulmn;
        Ext.tzLoadAsync(tzAddAppColsParams,function(respData){
            console.log(respData);
            //返回的字符串中包括到处报名表中配置的所有列;
            var addStudentColums=respData.studentColums;
            if (addStudentColums!=""){
                var transScoreValue=addStudentColums.split(",");
                for (var tt=0;tt<transScoreValue.length;tt++){
                    applicationCoulmn={
                        text: transScoreValue[tt],
                        name: transScoreValue[tt],
                        align: 'center',
                        width: 100,
                        dataIndex: transScoreValue[tt],
                        filter: {
                            type: 'string',
                            itemDefaults: {
                                emptyText: 'Search for...'
                            }
                        }

                    }
                    columns.splice(columns.length-1,0,applicationCoulmn);

                }
            }

        });

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
                    items:[ {text:"清除筛选条件",tooltip:"清除筛选条件", handler:"dumpTerm"},"-",
                        {text:"搜索",iconCls:'query',tooltip:"从所有考生中搜索",handler:"queryApplicants"}]
                }],
                columns: columns
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