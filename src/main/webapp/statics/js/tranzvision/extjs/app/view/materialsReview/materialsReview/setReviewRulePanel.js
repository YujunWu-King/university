Ext.define('KitchenSink.view.materialsReview.materialsReview.setReviewRulePanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'setMaterialsReviewRule',
    controller: 'materialsReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.materialsReview.materialsReview.materialsReviewJudgeAccountStore',
        'KitchenSink.view.common.store.appTransStore'
    ],
    title: '设置评审规则',
    classID:'',
    batchID:'',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent:function(){
        var KSPWPSEHNZT = new KitchenSink.view.common.store.appTransStore("TZ_CLPW_STATUS");
        KSPWPSEHNZT.load();
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
                    },{
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
                        name: 'applicantsNumber',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true
                    },{
                        xtype: 'numberfield',
                        fieldLabel: "材料评审考生",
                        name: 'materialsReviewApplicantsNumber',
                        fieldStyle:'background:#F4F4F4',
                        ignoreChangesFlag: true,
                        readOnly:true,
                        beforeBodyEl: [
                                '<li id="{id}-viewApplicantsEl" data-qtip="查看考生" data-ref="viewApplicantsEl" class="x-tagfield-item x-tagfield-item-selected" style="cursor:pointer;margin-top:4px;position:absolute">' +
                                '<div  class="'+
                                'x-tagfield-item-text" style="padding-right:4px;">查看考生</div>' +
                                '</li>'
                        ],
                        childEls: [
                            'viewApplicantsEl'
                        ],
                        listeners: {
                            change: function (field) {
                                var materialsReviewApplicants = field.getValue();
                                    var length = materialsReviewApplicants.toString().length||1;
                                   if (length > 0) {
                                        var viewApplicantsEl = this.viewApplicantsEl;
                                        if (viewApplicantsEl) {
                                            var tpl = Ext.create('Ext.XTemplate',
                                              'margin-left: {width}px;'
                                            );
                                            var data = {
                                                width: 25 + length * 8
                                            };
                                            var marginLeftStyle = tpl.apply(data);
                                            viewApplicantsEl.applyStyles(marginLeftStyle);
                                            if (materialsReviewApplicants) {
                                                viewApplicantsEl.addListener("click", "viewApplicationForm");
                                            }else{
                                                viewApplicantsEl.addListener("click",function(){
                                                    Ext.Msg.alert("提示","当期班级批次下没有考生");
                                                });
                                            }

                                        }
                                    }
                            }
                        }

                    },  {
                        xtype: 'textfield',
                        fieldLabel: "当前评审状态",
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
                                columnWidth:.5

                            }, {
                                xtype: 'timefield',
                                fieldLabel: "时间",
                                labelWidth:38,
                                style:'margin-left:5px',
                                name: 'startTime',
                                format: 'H:i',
                                columnWidth:.5
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
                                columnWidth:.5
                            },
                            {
                                xtype: 'timefield',
                                fieldLabel: "时间",
                                labelWidth:38,
                                style:'margin-left:5px',
                                name: 'endTime',
                                format: 'H:i',
                                columnWidth:.5
                            }
                        ]
                    }, {
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
                                var queryType;
                                if (newCard.reference == "materialsReviewJudgeForm"){
                                    queryType = "JUDGE";
                                }
                                if (newCard.reference == "materialsReviewDesc"){

                                }else{
                                    if(!newCard.child("grid").store.isLoaded()){
                                        var classID = this.findParentByType('setMaterialsReviewRule').classID;
                                        var batchID = this.findParentByType('setMaterialsReviewRule').batchID;
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
                                }
                            }
                        },
                        items:[
                            {
                                title: "材料评审说明",
                                xtype: 'form',
                                reference:'materialsReviewDesc',
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
                                    zIndex: 900
                                }]
                            },
                            {
                                title: "材料评委",
                                reference:'materialsReviewJudgeForm',
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
                                        margin:'10px 0 0 10px',
                                        items:[
                                            {
                                                xtype: 'displayfield',
                                                value: "每位考生要求被",
                                                hideLabel: true
                                            },
                                            {
                                                xtype: 'numberfield',
                                                fieldLabel: "",
                                                name: 'reviewCount',
                                                allowDecimals:false,
                                                minValue:0,
                                                style:'margin-left:5px;margin-right:5px;',
                                                negativeText:'评委数量不能为负数',
                                                nanText:'{0}不是有效的数字',
                                                hideLabel: true,
                                                width:130,
                                                listeners:{
                                                    change:function(field, newValue, oldValue, eOpts ){
                                                        var form = field.findParentByType("setMaterialsReviewRule").lookupReference("CountForm").getForm();
                                                        var reviewApplicantsCount = form.findField("materialsReviewApplicantsNumber").getValue();
                                                        var reviewCountAll = reviewApplicantsCount* parseInt(newValue);
                                                        form.findField("reviewCountAll").setValue(reviewCountAll);
                                                    }
                                                }
                                            },
                                            {
                                                xtype: 'displayfield',
                                                value: "个评委审批",
                                                hideLabel: true,
                                                style:'margin-right:10px;'
                                            },
                                            {
                                                xtype: "toolbar",
                                                padding:0,
                                                items: [
                                                    {text: "新增评委",iconCls:'add', tooltip: "新增评委信息", handler: "addJudge"},"-",
                                                    {text: "编辑",tooltip: "编辑评委信息", iconCls:"edit", handler: 'editJudge'},
                                                    {text:"发送邮件",tooltip:"发送邮件",iconCls:"email",handler:"sendJudgeEmail",hidden:true}
                                                ]
                                            }
                                        ]}
                                    , {
                                        xtype: 'grid',
                                        minHeight: 80,
                                        reference:'materialsReviewJudgeGrid',
                                        columnLines: true,
                                        autoHeight: true,
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

                                                    if(context.field=="upper"){
                                                        var upperCount=0;
                                                        for(var i=0;i<store.getCount();i++){
                                                            var record = store.getAt(i);
                                                            upperCount += record.get("upper");
                                                        };
                                                        form.findField("upper").setValue(upperCount);
                                                    }
                                                    if(context.field=="lower"){
                                                        var lowerCount=0;
                                                        for(var i=0;i<store.getCount();i++){
                                                            var record = store.getAt(i);
                                                            lowerCount += record.get("lower");
                                                        };
                                                        form.findField("lower").setValue(lowerCount);
                                                    }
                                                }
                                            }
                                        }],
                                        store:{
                                            view:this,
                                            type:'materialsReviewJudgeAccount',
                                            listeners:{
                                                datachanged: function(store,meta,epts){
                                                    var form = store.view.lookupReference("CountForm").getForm();
                                                    var upperCount=0;
                                                    var lowerCount=0;
                                                    for(var i=0;i<store.getCount();i++){
                                                        var record = store.getAt(i);
                                                        upperCount += record.get("upper");
                                                        lowerCount += record.get("lower");
                                                    };
                                                    form.findField("upper").setValue(upperCount);
                                                    form.findField("lower").setValue(lowerCount);
                                                }
                                            }
                                        },
                                        columns: [
                                            {
                                                text: "评委帐号",
                                                dataIndex: 'judgeID',
                                                minWidth: 100,
                                                flex:1
                                            },{
                                                text: "评委",
                                                dataIndex: 'judgeName',
                                                minWidth:100,
                                                flex:1
                                            }
                                            ,{
                                                text: "所属评委组",
                                                dataIndex: 'judgeGroup',
                                                minWidth:100,
                                                flex:1
                                            },{
                                                text: "评审考生人数",
                                                dataIndex: 'lower',
                                                width:150
                                            },
                                            {
                                                text: "评委状态",
                                                dataIndex: 'judgeStatus',
                                                width:150,
                                                renderer: function (v,grid,record) {
                                                    var x;
                                                    v = v?v:'N';
                                                    if((x = KSPWPSEHNZT.find('TValue',v))>=0){
                                                        return KSPWPSEHNZT.getAt(x).data.TSDesc;
                                                    }else{
                                                        return v;
                                                    }
                                                }
                                            }
//                                            ,{
//                                                text: "评审考生上限",
//                                                dataIndex: 'upper',
//                                                width:130
//
//                                            }
                                            ,{
                                                menuDisabled: true,
                                                sortable: false,
                                                text:'操作',
                                                width:60,
                                                align:'center',
                                                xtype: 'actioncolumn',
                                                items:[
                                                    {iconCls: 'edit',tooltip: '编辑',handler:'editCurrentJudge'},
                                                    {iconCls: 'remove',tooltip: '删除',handler:'removeJudge'}
                                                ]
                                            }
                                        ]
                                    },
                                    {
                                        xtype:'panel',
                                        ui: 'navigation',
                                        tabPosition: 'left',
                                        tabBar: {
                                            border: false
                                        },
                                        items: [{
                                            title: '人数统计',
                                            glyph: 85,
                                            xtype:'form',
                                            reference:'CountForm',
                                            autoHeight:true,
                                            style:'margin-left:0;margin-right:0',
                                            layout: {
                                                type: 'vbox',
                                                align: 'stretch'
                                            },
                                            fieldDefaults: {
                                                msgTarget: 'side',
                                                labelWidth: 220,
                                                labelStyle: 'font-weight:normal',
                                                padding:'0 0 0 10px',
                                                height:25
                                            },
                                            items:[{
                                                xtype:'displayfield',
                                                name:'materialsReviewApplicantsNumber',
                                                fieldLabel:'材料评审考生人数'
                                            },{
                                                xtype:'displayfield',
                                                name: 'reviewCountAll',
                                                fieldLabel:'要求评审人次'
                                            },{
                                                xtype:'displayfield',
                                                name: 'lower',
                                                fieldLabel:'当前选择评委评议总人次'
                                            },{
                                                xtype:'displayfield',
                                                name: 'upper',
                                                fieldLabel:'当前选择评委评议上限人次',
                                                hidden:true
                                            }
                                            ]
                                        }]
                                    }
                                ],
                                bodyStyle:'overflow-y:auto;overflow-x:hidden'
                            }]
                    }
                ]
            }]

        })
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
