    Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.candidateApplicants', {
    extend: 'Ext.panel.Panel',
    xtype: 'candidateApplicants',
    controller:'candidateController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'Ext.ux.MaximizeTool',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateStore',
        'KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateModel',
        'tranzvision.extension.grid.Exporter'
    ],
    title: '拟录取考生名单',
    classID:'',
    batchID:'',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent:function(){
        var genderStore = new KitchenSink.view.common.store.appTransStore("TZ_GENDER"),
            admitStore = new KitchenSink.view.common.store.appTransStore("TZ_LUQU_ZT"),
            ZGStore = new KitchenSink.view.common.store.appTransStore("TZ_CLPS_MSZG"),
            TZ_MSPS_STAGE=new KitchenSink.view.common.store.appTransStore("TZ_MSPS_STAGE");
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'interviewReviewApplicantsForm',
                name:"interviewReviewApplicantsForm",
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    //labelWidth: 110,
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
                    }
                    ,
                    {
                        xtype: 'textfield',
                        fieldLabel: "报考项目",
                        name: 'className',
                        fieldStyle:'background:#F4F4F4',
                        ignoreChangesFlag:true,
                        readOnly:true
                    }, {
                        xtype: 'textfield',
                        fieldLabel: "批次",
                        hidden:true,
                        name: 'batchName',
                        fieldStyle:'background:#F4F4F4',
                        ignoreChangesFlag:true,
                        readOnly:true
                    }, {
                        xtype: 'numberfield',
                        fieldLabel: "总报名数",
                        name: 'applicantsNum',
                        fieldStyle:'background:#F4F4F4',
                        ignoreChangesFlag:true,
                        readOnly:true
                    }, {
                        xtype: 'numberfield',
                        fieldLabel: "初筛通过人数",
                        name: 'reviewNum',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true,
                        ignoreChangesFlag:true
                    },
                    {
                        items:[


                            {
                                //style:'margin:0px 8px 8px 0px',
                                layout: {
                                    type: 'column',
                                    align: 'stretch'
                                },

                                items:[{
                                    xtype:'displayfield',
                                    width: 200,

                                    fieldLabel:"招聘领导小组",
                                    column:3

                                },{
                                    style:'margin-right:20px',

                                    xtype: 'button',
                                    flagType:'positive',
                                    defaultColor:'',
                                    name:'startup',
                                    setType:0,
                                    text: '启动',
                                    handler: 'startInterview',
                                    ignoreChangesFlag:true,
                                    width: 60,
                                    column:3
                                },{
                                    style:'margin-left:0px',
                                    xtype:'button',
                                    text:'关闭',
                                    name:'finish',
                                    flagType:'positive',
                                    defaultColor:'',
                                    setType:0,
                                    handler:'endInterview',
                                    ignoreChangesFlag:true,
                                    width:60,
                                    column:3
                                },{
                                    xtype:'displayfield',
                                    style:'margin-left:2em',
                                    fieldLabel:"当前评议状态",
                                    width: 150,
                                    name:'interviewStatus',
                                    ignoreChangesFlag: true,
                                    column:3
                                }]

                            },
                            {
                                //style:'margin:0px 8px 8px 0px',
                                layout: {
                                    type: 'column',
                                    align: 'stretch'
                                },

                                items:[{
                                    xtype:'displayfield',
                                    width: 200,

                                    fieldLabel:"评审委员会",
                                    column:3

                                },{
                                    style:'margin-right:20px',

                                    xtype: 'button',
                                    flagType:'positive',
                                    defaultColor:'',
                                    name:'startup2',
                                    setType:0,
                                    text: '启动',
                                    handler: 'startInterview2',
                                    width: 60,
                                    column:3
                                },{
                                    style:'margin-left:0px',
                                    xtype:'button',
                                    text:'关闭',
                                    name:'finish2',
                                    flagType:'positive',
                                    defaultColor:'',
                                    setType:0,
                                    handler:'endInterview2',
                                    width:60,
                                    column:3
                                },{
                                    xtype:'displayfield',
                                    style:'margin-left:2em',
                                    fieldLabel:"当前评议状态",
                                    width: 150,
                                    name:'interviewStatus2',
                                    ignoreChangesFlag: true,
                                    column:3,
                                    flex:1
                                }]

                            },

                            {
                                xtype: 'grid',
                                title:'考生名单',
                                minHeight: 260,
                                name:'candidateStudentGrid',
                                columnLines: true,
                                autoHeight: true,
                                frame:true,
                                selModel: {
                                    type: 'checkboxmodel'
                                },
                               /* plugins:[
                                    {
                                        ptype: 'gridexporter'
                                    }
                                ],*/

                                plugins: {
                                    ptype: 'cellediting',
                                    pluginId: 'dataCellediting',
                                    clicksToEdit: 1
                                },
                                dockedItems:[/*{
                                 xtype:"toolbar",
                                 dock:"bottom",
                                 ui:"footer",
                                 items:['->',{minWidth:80,text:"保存",iconCls:"save"}]
                                 },*/{
                                    xtype:"toolbar",
                                    items:[
                                        {text:"新增",tooltip:"添加考生参与本批次面试评审",iconCls:"add",
                                            handler:"addApplicantsBMR" },"-",
                                        {text:"删除",tooltip:"从列表中移除选中的考生",iconCls:"remove",
                                            handler:"removeApplicants" },
                                        {text:"发送录取邮件",tooltip:"发送录取邮件",iconCls:"email",handler:"sendEmail"}

                                    ]
                                }],
                                store:{
                                    type:'candidateStore'
                                },
                                columns: [
                                    {
                                        text: "班级ID",
                                        dataIndex: 'classID',
                                        hidden:true
                                    },{
                                        text: "批次ID",
                                        dataIndex: 'batchID',
                                        hidden:true
                                    },{
                                        text: "姓名",
                                        dataIndex: 'realName',
                                        minWidth:80,
                                        flex:1
                                    },{
                                        text: "报名表编号",
                                        dataIndex: 'appINSID',
                                        minWidth:100
                                    }
                                    ,{
                                        text: "性别",
                                        dataIndex: 'gender',
                                        minWidth:30,
                                        renderer:function(v){
                                            var x;
                                            if((x = genderStore.find('TValue',v))>=0){
                                                x=+x;
                                                return genderStore.getAt(x).data.TSDesc;
                                            }else{
                                                return v;
                                            }
                                        }
                                    },{
                                        text: "研究领域",
                                        dataIndex: 'reasercherArea',
                                        width:100,

                                        align:'center'
                                    },
                                    {
                                        text: "申请职位",
                                        dataIndex: 'applyPosition',
                                        width:100,

                                        align:'center'
                                    },{
                                        text: "拟录取系所",
                                        dataIndex: 'nlqxs',
                                        width:120,
                                        filter: {
                                            type: 'list'
                                        },
                                        align:'center'
                                    },{
                                        header: '系推荐报告',
                                        xtype:'linkcolumn',
                                        sortable: false,
                                        flex:2,
                                        value:'录入',
                                        minWidth: 120,
                                        handler:'viewCandidateTJBG'
                                    },{
                                        text: "招聘领导小组审议通过率",
                                        dataIndex: 'zpldxzsytgl',
                                        name:'zpldxzsytgl',
                                        width:150

                                    },{
                                        header: '招聘领导小组审议',
                                        xtype:'linkcolumn',
                                        sortable: false,
                                        flex:2,
                                        value:'招聘领导小组审议',
                                        minWidth: 120,
                                        handler:'ViewTeamReview'
                                    },{
                                        text: "学术委员会审议通过率",
                                        dataIndex: 'xswyhsytgl',
                                        name:'xswyhsytgl',
                                        width:150

                                    },{
                                        header: '学术委员会审议',
                                        xtype:'linkcolumn',
                                        sortable: false,
                                        flex:2,
                                        value:'学术委员会审议',
                                        minWidth: 120,
                                        handler:'ViewXxwyReview'
                                    },{
                                        text: "录取状态",
                                        dataIndex: 'LUQUZT',
                                        name:'LUQUZT',

                                        mindWidth:100,

                                        renderer:function(v){
                                            var x;
                                            if((x = admitStore.find('TValue',v))>=0){
                                                return admitStore.getAt(x).data.TSDesc;
                                            }else{
                                                return v;
                                            }
                                        }
                                    },{
                                        header: '发布',
                                        xtype:'linkcolumn',
                                        sortable: false,
                                        flex:2,
                                        value:'发布',
                                        minWidth: 100,
                                        handler:'publishResult'
                                    },{
                                        text:"操作",
                                        menuDisabled: true,
                                        sortable: false,
                                        align:'center',
                                        minWidth:75,
                                        xtype: 'actioncolumn',
                                        items:[
                                            {iconCls:'edit',tooltip:'编辑考生',handler:'editApplicant22'},
                                            {iconCls: 'remove',tooltip: '移除考生', handler: function(view, rowIndex){
                                                var store = view.findParentByType("grid").store;

                                                var interviewStatus=this.up("form[name=interviewReviewApplicantsForm]").getForm().findField('interviewStatus').value;
                                                var interviewStatus2=this.up("form[name=interviewReviewApplicantsForm]").getForm().findField('interviewStatus2').value;
                                                var enduring = TZ_MSPS_STAGE.getAt(TZ_MSPS_STAGE.find('TValue','A')).data.TSDesc
                                                 //var interviewStatus2=btn.findParentByType('form').getForm().findField('interviewStatus2').value;
                                                 //alert(interviewStatus+"=="+interviewStatus2);
                                                if (interviewStatus==enduring || interviewStatus2==enduring){
                                                    Ext.MessageBox.alert("提示","评审正在进行中，不能删除考生");
                                                    return;
                                                }else{
                                                    Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function (btnId) {
                                                        if (btnId == 'yes') {
                                                            //alert(store.getAt(rowIndex));
                                                            store.removeAt(rowIndex);

                                                        }
                                                    }, this);
                                                }                                                    

                                            }}

                                        ]
                                    }
                                ]
                            }
                        ]
                    }]
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
    var store = btn.findParentByType('panel').down('grid[name=candidateStudentGrid]').store,
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
        JSONData.delete = [];
        for(var x =removedData.length-1;x>=0;x--){
            JSONData.delete[x] = {};
            JSONData.delete[x].appINSID = removedData[x].data.appINSID;
            JSONData.delete[x].classID = classID;
            JSONData.delete[x].batchID = batchID;
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
        }
    }
            comParas=Ext.JSON.encode(JSONData);
            //表单中的数据
            comParas=Ext.JSON.encode(JSONData);
    //提交参数
    var tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"U","comParams":' + comParas + '}';
    Ext.tzSubmit(tzParams,function(responseData){

    	if(responseData.msg === "reviewing"){
               Ext.MessageBox.alert("提示","已经处于评审状态的考生不能删除");
           }
        var thisValue = responseData.reviewCount||btn.findParentByType('panel').child('form').getForm().findField('reviewNum').getValue();
        btn.findParentByType('panel').child('form').getForm().findField('reviewNum').setValue(thisValue);
        var grid = btn.findParentByType('panel').down("grid[name=candidateStudentGrid]");
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

           // alert( btn.findParentByType('panel').down('grid[name=candidateStudentGrid]'));
            var store = btn.findParentByType('panel').down('grid[name=candidateStudentGrid]').store,
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
                JSONData.delete = [];
                for(var x =removedData.length-1;x>=0;x--){
                    JSONData.delete[x] = {};
                    JSONData.delete[x].appINSID = removedData[x].data.appINSID;
                    JSONData.delete[x].classID = classID;
                    JSONData.delete[x].batchID = batchID;
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
                }
            }
            comParas=Ext.JSON.encode(JSONData);
            //提交参数
            var tzParams = '{"ComID":"TZ_GSM_CANDIDATE_COM","PageID":"TZ_MSPS_APPS_STD","OperateType":"U","comParams":' + comParas + '}';
            Ext.tzSubmit(tzParams,function(responseData){
                var grid = btn.findParentByType('panel').down("grid[name=candidateStudentGrid]");
                var store = grid.getStore();
                if(tzParams.indexOf("add")>-1||tzParams.indexOf("delete")>-1){
                    store.reload();
                }
                btn.findParentByType('panel').close();
            },"",true,this);
        }
    }, {
        text: '关闭',
        iconCls:"close",
        //handler: 'onApplicantsClose'
        handler:function(btn){
            btn.findParentByType('panel').close();
            //this.up('panel').close();
            //this.getView().close();
        }
    }]

});
