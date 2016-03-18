    Ext.define('KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicants', {
    extend: 'Ext.panel.Panel',
    xtype: 'interviewReviewApplicants',
    controller: 'interviewReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.interviewManagement.interviewReview.interviewReviewApplicantsStore',
        'tranzvision.extension.grid.Exporter'
    ],
    title: '面试评审考生名单',
    classID:'',
    batchID:'',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    constructor:function(transValue){
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'interviewReviewApplicantsForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },

                items: [
                    {
                        xtype: 'textfield',
                        name: 'classID',
                        hidden:true
                    },
                    {
                        xtype: 'textfield',
                        name: 'batchID',
                        hidden:true
                    },
                    {
                        xtype:'textfield',
                        name:'interviewStage',
                        hidden:true
                    }
                    ,
                    {
                        xtype: 'textfield',
                        fieldLabel: "报考班级",
                        name: 'className',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    }, {
                        xtype: 'textfield',
                        fieldLabel: "批次",
                        name: 'batchName',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    }, {
                        xtype: 'numberfield',
                        fieldLabel: "报考考生数量",
                        name: 'applicantsNum',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    }, {
                        xtype: 'numberfield',
                        fieldLabel: "面试评审考生",
                        name: 'reviewNum',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true,
                        ignoreChangesFlag:true
                    },
                    {
                        xtype: 'grid',
                        title:'考生名单',
                        minHeight: 260,
                        name:'interviewReviewStudentGrid',
                        columnLines: true,
                        autoHeight: true,
                        frame:true,
                        selModel: {
                            type: 'checkboxmodel'
                        },
                        plugins:[
                            {
                                ptype: 'gridexporter'
                            }
                        ],
                        dockedItems:[/*{
                         xtype:"toolbar",
                         dock:"bottom",
                         ui:"footer",
                         items:['->',{minWidth:80,text:"保存",iconCls:"save"}]
                         },*/{
                            xtype:"toolbar",
                            items:[
                                {text:"新增",tooltip:"添加考生参与本批次面试评审",iconCls:"add",
                                  handler:"addApplicantsFromBM" },"-",
                                {text:"删除",tooltip:"从列表中移除选中的考生",iconCls:"remove",
                                   handler:"removeApplicants" },"-",
                                {text:"设置评委组",tooltip:"为考生设置评委组",
                                    handler:"setJudgeGroup" }
                                //{text:"增加五金考生",tooltip:"增加五金考生",handler:'addApplicantsFromWJ'}
                            ]
                        }],
                        store:{
                            type:'interviewReviewApplicants'
                        },
                        columns: [
                            {
                                text:'行号',
                                xtype:'rownumberer',
                                minWidth:35
                            },{
                                text: "姓名",
                                dataIndex: 'realName',
                                minWidth:100,
                                flex:1
                            },{
                                text: "报名表编号",
                                dataIndex: 'appINSID',
                                minWidth:130
                            }
                            ,{
                                text: "性别",
                                dataIndex: 'gender',
                                minWidth:50,
                                renderer:function(v){
                                    var x;
                                    var genderStore = transValue.get("TZ_GENDER");
                                    if((x = genderStore.find('TValue',v))>=0){
                                        return genderStore.getAt(x).data.TSDesc;
                                    }else{
                                        return v;
                                    }
                                }
                            },{
                                text: "面试评委组",
                                dataIndex: 'judgeGroup',
                                name:'judgeGroup',
                                width:200,
                                flex:1
                            },{
                                text: "录取状态",
                                dataIndex: 'LUQUZT',
                                name:'reviewStatus',
                                mindWidth:100,
                                renderer:function(v,metadata,record){
                                    var x;
                                    var admitStore = transValue.get("TZ_LUQU_ZT");
                                    if((x = admitStore.find('TValue',v))>=0){
                                        return admitStore.getAt(x).data.TSDesc;
                                    }else{
                                        return v;
                                    }
                                }
                            },{
                                text:"评委间偏差",
                                dataIndex:'PWJPC',
                                minWidth:100
                            },{
                                text:"备注",
                                dataIndex:'remark',
                                minWidth:150,
                                flex:1,
                            },{
                                text:"操作",
                                menuDisabled: true,
                                sortable: false,
                                align:'center',
                                minWidth:75,
                                xtype: 'actioncolumn',
                                items:[
                                    {iconCls:'edit',tooltip:'编辑考生',handler:'editApplicant'},
                                    {iconCls: 'remove',tooltip: '移除考生', handler:"removeApplicant"}

                                ]
                            }
                        ]
                    }
                ]
            }]

        });
        //test
        this.callParent();
    },
    buttons: [{
        text: '保存',
        iconCls:"save",
        //handler: 'onApplicantsSave'
        handler:function(btn){
    var store = btn.findParentByType('panel').down('grid[name=interviewReviewStudentGrid]').store,
        newData = store.getNewRecords(),
        removedData = store.getRemovedRecords(),
        updateData = store.getModifiedRecords(),
        comParas,JSONData={},
        classID = btn.findParentByType('panel').child('form').getForm().findField('classID').getValue(),
        batchID = btn.findParentByType('panel').child('form').getForm().findField('batchID').getValue();
    //新增数据
            if(newData.length !== 0) {
        JSONData.add = [];
        for (var x = newData.length - 1; x >= 0; x--) {
            JSONData.add[x] = {};
            JSONData.add[x].appINSID = newData[x].data.appINSID;
            JSONData.add[x].classID = classID;
            JSONData.add[x].batchID = batchID;
        }
    }
    //删除数据
    if(removedData.length !== 0){
        JSONData.deleted = [];
        for(var x =removedData.length-1;x>=0;x--){
            JSONData.deleted[x] = {};
            JSONData.deleted[x].appINSID = removedData[x].data.appINSID;
            JSONData.deleted[x].classID = classID;
            JSONData.deleted[x].batchID = batchID;
        }
    }
    //更新数据

    if(updateData.length !== 0){
        JSONData.update =[];
        for(var x = updateData.length-1;x>=0;x--){
            JSONData.update[x] ={};
            delete updateData[x].data.id;
            JSONData.update[x].classID = classID;
            JSONData.update[x].batchID = batchID;
            JSONData.update[x].appINSID = updateData[x].data.appINSID;
            JSONData.update[x].judgeList = updateData[x].data.judgeList;
            JSONData.update[x].remark = updateData[x].data.remark;
            JSONData.update[x].reviewStatus = updateData[x].data.LUQUZT;
            JSONData.update[x].judgeGroup = updateData[x].data.judgeGroup?updateData[x].data.judgeGroup:'';
        }
    }
            comParas=Ext.JSON.encode(JSONData);
            //表单中的数据
            comParas=Ext.JSON.encode(JSONData);
    //提交参数
    var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"U","comParams":' + comParas + '}';
    Ext.tzSubmit(tzParams,function(responseData){
        var thisValue = responseData.reviewCount||btn.findParentByType('panel').child('form').getForm().findField('reviewNum').getValue();
        btn.findParentByType('panel').child('form').getForm().findField('reviewNum').setValue(thisValue);
        var grid = btn.findParentByType('panel').down("grid[name=interviewReviewStudentGrid]");
        var store = grid.getStore();
        if(tzParams.indexOf("add")>-1||tzParams.indexOf("delete")>-1||tzParams.indexOf("update")){
            store.reload();
        }
    },"",true,this);
}
    }, {
        text: '确定',
        iconCls:"ensure",
       //handler: 'onApplicantsEnsure'
        handler:function(btn) {
            var store = btn.findParentByType('panel').down('grid[name=interviewReviewStudentGrid]').store,
                newData = store.getNewRecords(),
                removedData = store.getRemovedRecords(),
                updateData = store.getModifiedRecords(),
                comParas,JSONData={},
                classID = btn.findParentByType('panel').child('form').getForm().findField('classID').getValue(),
                batchID = btn.findParentByType('panel').child('form').getForm().findField('batchID').getValue();
            if(newData.length !== 0) {
                JSONData.add = [];
                for (var x = newData.length - 1; x >= 0; x--) {
                    JSONData.add[x] = {};
                    JSONData.add[x].appINSID = newData[x].data.appINSID;
                    JSONData.add[x].classID = classID;
                    JSONData.add[x].batchID = batchID;
                }
            }
            if(removedData.length !== 0){
                JSONData.deleted = [];
                for(var x =removedData.length-1;x>=0;x--){
                    JSONData.deleted[x] = {};
                    JSONData.deleted[x].appINSID = removedData[x].data.appINSID;
                    JSONData.deleted[x].classID = classID;
                    JSONData.deleted[x].batchID = batchID;
                }
            }
            //更新数据
            if(updateData.length !== 0){
                JSONData.update =[];
                for(var x = updateData.length-1;x>=0;x--){
                    JSONData.update[x] ={};
                    delete updateData[x].data.id;
                    JSONData.update[x].classID = classID;
                    JSONData.update[x].batchID = batchID;
                    JSONData.update[x].appINSID = updateData[x].data.appINSID;
                    JSONData.update[x].judgeList = updateData[x].data.judgeList;
                    JSONData.update[x].remark = updateData[x].data.remark;
                    JSONData.update[x].reviewStatus = updateData[x].data.LUQUZT;
                    JSONData.update[x].judgeGroup = updateData[x].data.judgeGroup?updateData[x].data.judgeGroup:'';
                }
            }
            comParas=Ext.JSON.encode(JSONData);
            //提交参数
            var tzParams = '{"ComID":"TZ_REVIEW_MS_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"U","comParams":' + comParas + '}';
            if(tzParams.indexOf("add")>-1||tzParams.indexOf("delete")>-1||tzParams.indexOf("update")>-1){
                Ext.tzSubmit(tzParams,function(responseData){
                    var grid = btn.findParentByType('panel').down("grid[name=interviewReviewStudentGrid]");
                    var store = grid.getStore();
                    store.commitChanges();
                    btn.findParentByType('panel').close();
                },"",true,this);
            }else{
                btn.findParentByType('panel').close();
            }
        }
    }, {
        text: '关闭',
        iconCls:"close",
        //handler: 'onApplicantsClose'
        handler:function(btn){
            btn.findParentByType('panel').close();
        }
    }]

});
