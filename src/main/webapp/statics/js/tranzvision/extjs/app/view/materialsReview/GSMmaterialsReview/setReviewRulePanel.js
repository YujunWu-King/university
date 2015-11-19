Ext.define('KitchenSink.view.materialsReview.GSMmaterialsReview.setReviewRulePanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'setGSMmaterialsReviewRule',
    controller: 'GSMmaterialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewJudgeAccountStore',
        'KitchenSink.view.common.store.appTransStore',
        'KitchenSink.view.materialsReview.GSMmaterialsReview.materialsReviewApplicantsStore',
        'tranzvision.extension.grid.Exporter'
    ],
    title: '设置评审信息',
    classID:'',
    batchID:'',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    constructor: function (classID,batchID){
        this.classID=classID;
        this.batchID=batchID;
        this.callParent();
    },
    initComponent:function(){



        var judgeGroupStore2 = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_PWZDY_T',
            condition:{
                TZ_JUGTYP_STAT:{
                    value:'Y',
                    operator:'01',
                    type:'01'
                }},
            result:'TZ_PWZBH,TZ_PWZMS'
        });
        Ext.define('filter', {
            extend: 'Ext.grid.filters.Filters'

        });
        var judgeGroupStore = new Ext.data.Store({
                fields:['group','desc'],
                data:[]
            }),
            judgeStatusStore = new KitchenSink.view.common.store.appTransStore("TZ_JUGACC_STATUS");
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
                bodyStyle:'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 130,
                    labelStyle: 'font-weight:bold',
                    ignoreChangesFlag: true
                },

                items: [
                    {
                        xtype: 'textfield',
                        name: 'classID',
                        hidden:true,
                        ignoreChangesFlag: true
                    },
                    {
                        xtype: 'textfield',
                        name: 'batchID',
                        hidden:true,
                        ignoreChangesFlag: true
                    },{
                        xtype: 'textfield',
                        fieldLabel: "招聘项目名称",
                        name: 'className',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true,
                        ignoreChangesFlag: true
                    }, {
                        xtype: 'textfield',
                        fieldLabel: "批次",
                        name: 'batchName',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true,
                        ignoreChangesFlag: true
                    }, {
                        xtype: 'numberfield',
                        fieldLabel: "总评审人数",
                        name: 'applicantsNumber',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true,
                        hidden:true,
                        ignoreChangesFlag: true
                    },{
                        xtype: 'numberfield',
                        fieldLabel: "已选择评审人数",
                        name: 'materialsReviewApplicantsNumber',
                        fieldStyle:'background:#F4F4F4',
                        ignoreChangesFlag: true,
                        readOnly:true
//                        ,
//                        beforeBodyEl: [
//                                '<li id="{id}-viewGSMApplicantsEl" data-qtip="查看申请人 " data-ref="viewGSMApplicantsEl" class="x-tagfield-item x-tagfield-item-selected" style="cursor:pointer;margin-top:4px;position:absolute">' +
//                                '<div  class="'+
//                                'x-tagfield-item-text" style="padding-right:4px;">查看申请人 </div>' +
//                                '</li>'
//                        ],
//                        childEls: [
//                            'viewGSMApplicantsEl'
//                        ],
//                        listeners: {
//                            change: function (field) {
//                                var GSMmaterialsReviewApplicants = field.getValue();
//                                var length = GSMmaterialsReviewApplicants.toString().length||1;
//                                if (length > 0) {
//                                    var viewApplicantsEl = this.viewGSMApplicantsEl;
//                                    if (viewApplicantsEl) {
//                                        var tpl = Ext.create('Ext.XTemplate',
//                                            'margin-left: {width}px;'
//                                        );
//                                        var data = {
//                                            width: 25 + length * 8
//                                        };
//                                        var marginLeftStyle = tpl.apply(data);
//                                        viewApplicantsEl.applyStyles(marginLeftStyle);
//                                        if (GSMmaterialsReviewApplicants) {
//                                            viewApplicantsEl.addListener("click", "viewGSMApplicationForm");
//                                        }else{
//                                            viewApplicantsEl.addListener("click",function(){
//                                                Ext.Msg.alert("提示","当期项目批次下没有申请人 ");
//                                            });
//                                        }
//
//                                    }
//                                }
//
//                            }
//                        },
//                        ignoreChangesFlag: true

                    },  {
                        xtype: 'textfield',
                        fieldLabel: "评审状态",
                        name: 'reviewStatus',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true,
                        ignoreChangesFlag: true
                    },{
                        layout: {
                            type: 'column'
                        } ,
                        padding:'0 0 8px 0',
                        items:[
                            {
                                xtype: 'datefield',
                                fieldLabel: "开始日期",
                                name: 'startDate',
                                itemId: 'startDate',
                                endDateField: 'endDate',
                                vtype: 'daterange',
                                repeatTriggerClick:true,
                                format: 'Y-m-d',
                                columnWidth:.5,
                                ignoreChangesFlag: true

                            }, {
                                xtype: 'timefield',
                                fieldLabel: "时间",
                                labelWidth:38,
                                style:'margin-left:5px',
                                name: 'startTime',
                                format: 'H:i',
                                columnWidth:.5,
                                ignoreChangesFlag: true
                            }
                        ]},{
                        layout: {
                            type: 'column'
                        } ,
                        padding:'0 0 8px 0',
                        items:[

                            {
                                xtype: 'datefield',
                                fieldLabel: "结束日期",
                                name: 'endDate',
                                itemId: 'endDate',
                                startDateField: 'startDate',
                                vtype: 'daterange',
                                format: 'Y-m-d',
                                repeatTriggerClick:true,
                                columnWidth:.5,
                                ignoreChangesFlag: true
                            },
                            {
                                xtype: 'timefield',
                                fieldLabel: "时间",
                                labelWidth:38,
                                style:'margin-left:5px',
                                name: 'endTime',
                                format: 'H:i',
                                columnWidth:.5,
                                ignoreChangesFlag: true
                            }
                        ]
                    }, {
                        layout: {
                            type: 'column'
                        } ,
                        padding:'0 0 8px 0',
                        items:[{
                            xtype:'displayfield',
                            fieldLabel:'评审状态',
                            ignoreChangesFlag: true
                        },{
                            style:'margin-left:0',
                            xtype: 'button',
                            flagType:'positive',
                            name:'startReview',
                            defaultColor:'',
                            setType:0,
                            text: '开启评审',
                            handler: 'startReview',
                            width: 90
                        },{
                            style:'margin-left:10px',
                            xtype:'button',
                            text:'关闭评审',
                            defaultColor:'',
                            name:'closeReview',
                            flagType:'positive',
                            setType:0,
                            handler:'closeReview',
                            width:90
                        }]
                    },{
                        xtype: 'displayfield',
                        beforeLabelTpl:'',
                        fieldLabel: "注意",
                        value: '开始结束时间仅作为提醒评委使用，不会作为自动开关使用。',
                        labelWidth:38,
                        labelStyle:'font-weight:normal;'
                    },
                    {
                        xtype: 'tabpanel',
                        frame: true,
                        activeTab: 0,
                        plain:false,
                        resizeTabs:true,
                        defaults :{
                            autoScroll: false
                        },

                        listeners:{
                            beforetabchange:function(tabPanel, newCard, oldCard){
                                if (newCard.reference == "GSMmaterialsReviewApplicantsGrid"){
                                    var store = newCard.store;

                                    var classID = this.findParentByType('setGSMmaterialsReviewRule').classID;
                                    var batchID = this.findParentByType('setGSMmaterialsReviewRule').batchID;

                                    var tzParams = '{"ComID":"TZ_REVIEW_GSMCL_COM","PageID":"TZ_GSMCL_APPS_STD",' +
                                        '"OperateType":"QF","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'"}}';
                                    var tzStoreParams ='{"classID":"'+classID+'","batchID":"'+batchID+'"}';
                                    Ext.tzLoad(tzParams, function (respData) {

                                        store.tzStoreParams = tzStoreParams;
                                        store.load({
                                            scope: this,
                                            callback: function(records, operation, success) {
                                            }
                                        });


                                    });
                                    this.doLayout();

                                };

                                var queryType;
                                if (newCard.reference == "GSMmaterialsReviewJudgeForm"){
                                    queryType = "JUDGE";

                                    var classID = this.findParentByType('setGSMmaterialsReviewRule').classID;
                                    var batchID = this.findParentByType('setGSMmaterialsReviewRule').batchID;
                                    var tzStoreParams = '{"classID":"'+classID+'","batchID":"' + batchID + '","queryType":"' + queryType + '"}';

                                    var store = newCard.child("grid").store;
                                    store.tzStoreParams = tzStoreParams;
                                    store.load({
                                        scope: this,
                                        callback: function(records, operation, success) {
                                        }
                                    });
                                    this.doLayout();

                                }
                                if (newCard.reference == "GSMmaterialsReviewDesc"){

                                }
                            }
                        },
                        items:[
                            {
                                title: "材料评审说明",
                                xtype: 'form',
                                reference:'GSMmaterialsReviewDesc',
                                autoHeight:true,
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                border: false,
                                bodyPadding: 10,
                                bodyStyle:'overflow-x:hidden',
                                items: [{
                                    xtype: 'ueditor',
                                    name: 'desc',
                                    model:'normal',
                                    zIndex: 900,
                                    ignoreChangesFlag: true
                                }]
                            },{
                                xtype: 'grid',
                                title:'申请人名单',
                                minHeight: 260,
                                style:'border:0',
                                name:'GSMmaterialsReviewApplicantsGrid',
                                reference:'GSMmaterialsReviewApplicantsGrid',
                                columnLines: true,
                                autoHeight: true,
                                frame:true,
                                selModel: {
                                    selType: 'checkboxmodel'
                                },
                                dockedItems:[/*{
                                 xtype:"toolbar",
                                 dock:"bottom",
                                 ui:"footer",
                                 items:['->',{minWidth:80,text:"保存",iconCls:"save"}]
                                 },*/{
                                    xtype:"toolbar",
                                    items:["-",
                                        // {text:"查询",tooltip:"查询参与本批次材料评审的申请人 ",iconCls:"query",handler:"queryApplicants"},"-",
                                        {text:"新增",tooltip:"添加申请人",iconCls:"add",handler:"addApplicants"},"-",
                                        {text: "编辑",tooltip: "编辑选中申请人信息 ", iconCls:"edit", handler: 'editApplicants'},"-",
                                        {text:"删除",tooltip:"从列表中删除申请人 ",iconCls:"remove",handler:"removeApplicants"},"-",
                                        {text:"导出",tooltip:"导出申请人列表", iconCls:"excel",handler:"exportExcelApplicants"},"-",
                                        {text:"设置评委",tooltip:"为申请人设置评委",iconCls:"people",handler:"setJudgeForStudent"},"->",
                                        {
                                            xtype:'splitbutton',
                                            text:'更多操作',
                                            iconCls:  'list',
                                            glyph: 61,
                                            menu:[
                                                { text: '设置面试资格',handler: 'setOwnQuary'},
                                                {text:"发送邮件",tooltip:"发送面试结果邮件",handler:"sendStudentEmail"}
                                            ]  } ]

                                }],

                                plugins: [{
                                    ptype: 'cellediting'

                                },{
                                    ptype: 'gridfilters'

                                },{
                                    ptype: 'gridexporter'

                                }
                                ],
                                store:{
                                    type:'materialsReviewApplicants',
                                    autoLoad:true
                                },
                                columns: [
                                    {
                                        text: "报名表编号",
                                        dataIndex: 'appInsID',
                                        width:130,
                                        filter: {
                                            type: 'string',
                                            itemDefaults: {
                                                emptyText: 'Search for...'
                                            }
                                        },
                                        align:'center'
                                    }
                                    ,{
                                        text: "姓名",
                                        dataIndex: 'realName',
                                        width:130,
                                        filter: {
                                            type: 'string',
                                            itemDefaults: {
                                                emptyText: 'Search for...'
                                            }
                                        },
                                        align:'center'
                                    },{
                                        text: "性别",
                                        dataIndex: 'gender',
                                        width:70,
                                        filter: {
                                            type: 'list'
                                        },
                                        align:'center'
                                    },{
                                        text: "申请职位",
                                        dataIndex: 'applyPosition',
                                        width:150,
                                        filter: {
                                            type: 'list'
                                        },
                                        align:'center'
                                    },{
                                        text: "志愿顺序",
                                        dataIndex: 'intentOrder',
                                        width:150,
                                        filter: {
                                            type: 'list'
                                        },
                                        align:'center'
                                    },{
                                        text: "志愿系",
                                        dataIndex: 'intentDepart',
                                        flex:1,
                                        filter: {
                                            type: 'list'
                                        },
                                        align:'center'
                                    },{
                                        text: "评委姓名",
                                        dataIndex: 'judgeList',
                                        // minWidth:130,
                                        flex:1,
                                        align:'center',
                                        //hidden:true,
                                        renderer:function(v) {
                                            if (v) {
                                                return '<a class="tz_materialsReview_app" href = "javaScript:void(0)" >' + v + '</a>';
                                            } else {
                                                console.log(this)
                                                this.removeListener('viewJudge',arguments.callee);
                                                return "";
                                            }},
                                        listeners:{
                                            click:'viewJudge'
                                        }
                                    },{
                                        text: "面试资格",
                                        dataIndex: 'interviewQualification',
                                        width:100,
                                        filter: {
                                            type: 'list'
                                        }
                                    },{
                                        menuDisabled: true,
                                        text: "操作",
                                        sortable: false,
                                        width:80,
                                        xtype: 'actioncolumn',
                                        items:[
                                            {iconCls:'edit',tooltip:'编辑申请人信息',handler:'editCurrentApplicant'},
                                            {iconCls: 'remove',tooltip: '移除申请人',  handler: function(view, rowIndex){
                                                if( view.findParentByType("grid").findParentByType("form").getForm().findField('reviewStatus').getValue()=='进行中')
                                                {Ext.Msg.alert('提示','当前评审状态为进行中，不可移除申请人');
                                                    return ;}
                                                Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                                                    if(btnId == 'yes'){
                                                        var store = view.findParentByType("grid").store;
                                                        store.removeAt(rowIndex);
//                                                if (store.getAt(rowIndex).data.judgeList!=0)
//                                                {Ext.Msg.alert('提示','该申请人 已被指定评委，不可删除')
//                                                return;}else{store.removeAt(rowIndex);}

                                                    }
                                                },"",true,this);
                                            } }

                                            // {iconCls: 'people',tooltip: '为申请人 指定评委',handler:'setAppJudge'}
                                        ]
                                    }
                                ]
                            },
                            {
                                title: "材料评委",
                                reference:'GSMmaterialsReviewJudgeForm',
                                xtype: 'form',
                                minHeight: 300,
                                autoHeight:true,
                                style:'margin-left:0;margin-right:0',
                                layout: {
                                    type: 'vbox',
                                    align: 'stretch'
                                },
                                items:[
                                    {
                                        layout: {
                                            type: 'column'
                                        } ,
                                        margin:'10px 0 10px 10px',
                                        items:[
                                            {
                                                xtype: "toolbar",
                                                padding:0,
                                                items: [
                                                    {text: "新增",iconCls:'add', tooltip: "新增评委信息", handler: "addJudge"},"-",
                                                    {text: "删除",iconCls:'remove', tooltip: "删除评委", handler: function(btn)
                                                    { var form=btn.findParentByType("form"),
                                                        selList = form.child('grid').getSelectionModel().getSelection(),
                                                    //选中行长度
                                                        checkLen = selList.length;
                                                        if(form.findParentByType("form").getForm().findField('reviewStatus').getValue()=='进行中')

                                                        { Ext.Msg.alert('提示','当前评审状态为进行中，不可删除评委');
                                                            return ;}
                                                        if (checkLen==0)
                                                        {
                                                            Ext.Msg.alert('提示','请您选择要删除的记录 ');
                                                        }
                                                        else{
                                                            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                                                                if(btnId == 'yes'){
                                                                    var store =  form.child('grid').store;
                                                                    store.remove(selList);
                                                                }
                                                            },this);
                                                        }}},"-",
                                                    {text:"发送邮件",tooltip:"发送邮件",iconCls:"email",handler:"sendEmail"}
                                                ]
                                            }
                                        ]}
                                    , {
                                        xtype: 'grid',
                                        minHeight: 80,
                                        reference:'GSMmaterialsReviewJudgeGrid',
                                        name:'GSMmaterialsReviewJudgeGrid',
                                        columnLines: true,
                                        autoHeight: true,

//                                    dockedItems: [
//                                        {
//                                            xtype: "toolbar",
//                                            items: [
//                                                {text: "新增",iconCls:'add', tooltip: "新增评委", handler: "addJudge"}
//                                            ]
//                                        }
//                                    ],
                                        style:'border-bottom:1px #C1C1C1 solid;border-top:1px #C1C1C1 solid',
                                        selModel: {
                                            type: 'checkboxmodel'
                                        },
                                        plugins: [{
                                            ptype: 'cellediting',
                                            pluginId: 'judgeCellEditing',
                                            listeners:{
                                                beforeedit:function( editor, context, eOpts ){
                                                    if(context.field=="judgeID"&&context.value.length>0&&!context.record.isModified('judgeID')){
                                                        return false;
                                                    }
                                                },
                                                edit:function(editor, context, eOpts){
                                                    var store = context.store;
                                                    var form = store.view.lookupReference("CountForm").getForm();


                                                }
                                            }
                                        }],
                                        store:{
                                            view:this,
                                            type:'GSMmaterialsReviewJudgeAccount'
                                        },
                                        columns: [

                                            {
                                                text: "评委帐号",
                                                dataIndex: 'judgeID',
                                                minWidth: 100,
                                                flex:1
                                            },{
                                                text: "评委姓名",
                                                dataIndex: 'judgeName',
                                                minWidth:100,
                                                flex:1
                                            },{
                                                text: "所属系",
                                                dataIndex: 'judgeDepart',
                                                minWidth:100,
                                                flex:1
                                            },{
                                                text: "评委类型",
                                                dataIndex: 'judgeType',
                                                minWidth:100,
                                                flex:1
                                            },{
                                                text: "手机",
                                                dataIndex: 'judgePhoneNumber',
                                                width:130
                                            },{
                                                text: "邮箱",
                                                dataIndex: 'judgeEmail',
                                                width:130

                                            },{
                                                text: "对应OA账号",
                                                dataIndex: 'judgeOAID',
                                                width:130

                                            },{
                                                text: "所属评委组",
                                                dataIndex: 'judgeGroup',
                                                minWidth:100,
                                                flex:1,
                                                renderer:function(v){
                                              if (v=="A")
                                              {
                                                  return "一般评委组"
                                              }
                                                    if (v=="B")
                                                    {
                                                       return "系主任评委组"
                                                    }
                                                }
                                            },{
                                                menuDisabled: true,
                                                sortable: false,
                                                text:'操作',
                                                width:60,
                                                align:'center',
                                                xtype: 'actioncolumn',
                                                items:[
                                                    {iconCls: 'remove',tooltip: '删除',handler:'removeJudge'}

                                                ]
                                            }
                                        ]
                                    }
                                ],
                                bodyStyle:'overflow-y:auto;overflow-x:hidden'
                            }]
                    }
                ]
            }]

        });
        this.callParent();
    },
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onReviewRuleSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onReviewRuleEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onReviewRuleClose'
    }]
});
