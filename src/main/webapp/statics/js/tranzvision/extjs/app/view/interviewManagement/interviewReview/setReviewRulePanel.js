Ext.define('KitchenSink.view.interviewManagement.interviewReview.setReviewRulePanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'setInterviewReviewRule',
    controller: 'interviewReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'KitchenSink.view.interviewManagement.interviewReview.interviewReviewJudgeAccountStore'
    ],
    title: '设置评审规则',
    classID:'',
    batchID:'',
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    constructor:function(transValue){
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'interviewReviewRuleForm',
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
                    },{
                        xtype:"textfield",
                        name:'interviewStage',
                        hidden:true,
                        ignoreChangesFlag:true
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
                    }, {
                        xtype: 'numberfield',
                        fieldLabel: "面试评审考生",
                        name: 'interviewReviewApplicantsNumber',
                        fieldStyle:'background:#F4F4F4',
                        ignoreChangesFlag: true,
                        readOnly:true,
                        beforeBodyEl: [
                            '<li id="{id}-viewAppFormEl" data-qtip="查看考生" data-ref="viewAppFormEl" class="x-tagfield-item x-tagfield-item-selected" style="cursor:pointer;margin-top:4px;position:absolute">' +
                            '<div  class="'+
                            'x-tagfield-item-text" style="padding-right:4px;">查看考生</div>' +
                            '</li>'
                        ],
                        childEls: [
                            'viewAppFormEl'
                        ],
                        listeners: {
                            change: function (field) {
                                var appInsID = field.getValue();
                                if(appInsID ===0 || appInsID) {
                                    var length = appInsID.toString().length || 1;
                                    if (length > 0) {
                                        var viewAppFormEl = this.viewAppFormEl;
                                        if (viewAppFormEl) {
                                            var tpl = Ext.create('Ext.XTemplate',
                                                'margin-left: {width}px;'
                                            );
                                            var data = {
                                                width: 25 + length * 8
                                            };
                                            var marginLeftStyle = tpl.apply(data);
                                            viewAppFormEl.applyStyles(marginLeftStyle);
                                            if (appInsID) {
                                                viewAppFormEl.addListener("click", "viewApplicationForm");
                                            } else {
                                                viewAppFormEl.addListener("click", function () {
                                                    Ext.Msg.alert("提示", "当期班级和批次没有考生");
                                                });
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }, {
                        xtype: 'textfield',
                        fieldLabel: "当前评审状态",
                        name: 'reviewStatus',
                        fieldStyle:'background:#F4F4F4',
                        readOnly:true,
                        ignoreChangesFlag:true,
                        listeners:{
                            change:function(field,v){
                                var x;
                                var reviewStage = transValue.get("TZ_MSPS_STAGE");
                                if((x = reviewStage.find('TValue',v))>=0){
                                    field.setValue(reviewStage.getAt(x).data.TSDesc);
                                }
                                field.findParentByType('form').getForm().findField('interviewStage').setValue(v);
                            }
                        }
                    },{
                        layout: {
                            type: 'column'
                        } ,
                        padding:'0 0 8px 0',
                        items:[{
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
                        items:[{
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
                                if (newCard.reference == "interviewReviewJudgeForm"){
                                    queryType = "JUDGE";
                                }
                                if (newCard.reference == "interviewReviewDesc"){

                                }else{
                                    if(!newCard.child("grid").store.isLoaded()){
                                        var classID = this.findParentByType('setInterviewReviewRule').classID;
                                        var batchID = this.findParentByType('setInterviewReviewRule').batchID;
                                        var tzStoreParams = '{"classID":"'+classID+'","batchID":"' + batchID + '","queryType":"' + queryType + '"}';

                                        var store = newCard.child("grid").store;
                                        store.tzStoreParams = tzStoreParams;
                                        store.load({
                                            scope: this,
                                            callback: function(records, operation, success) {
                                            }
                                        });
                                        //this.doLayout();
                                    }
                                }
                            }
                        },
                        items:[
                            {
                                title: "面试评审说明",
                                xtype: 'form',
                                reference:'interviewReviewDesc',
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
                                title: "面试评委",
                                reference:'interviewReviewJudgeForm',
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
                                        padding:'10px 0 5px 10px',
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
                                                        var form = field.findParentByType("setInterviewReviewRule").lookupReference("CountForm").getForm();
                                                        var reviewApplicantsCount = form.findField("interviewReviewApplicantsNumber").getValue();
                                                        var reviewCountAll = reviewApplicantsCount* parseInt(newValue);
                                                        form.findField("reviewCountAll").setValue(reviewCountAll);
                                                    }
                                                }
                                            },{
                                                xtype: 'displayfield',
                                                value: "个评委审批",
                                                hideLabel: true
                                            },
                                        ]},{
                                        layout: {
                                            type: 'column'
                                        } ,
                                        padding:'0px 0 5px 75px',
                                        items:[
                                            {
                                                xtype: 'displayfield',
                                                value: "一共",
                                                hideLabel: true
                                            },
                                            {
                                                xtype: 'numberfield',
                                                fieldLabel: "",
                                                name: 'reviewTeamCount',
                                                allowDecimals:false,
                                                minValue:0,
                                                style:'margin-left:5px;margin-right:5px;',
                                                negativeText:'评委组数量不能为负数',
                                                nanText:'{0}不是有效的数字',
                                                hideLabel: true,
                                                width:130,
                                            },{
                                                xtype: 'displayfield',
                                                value: "个评委组",
                                                hideLabel: true
                                            }
                                        ]},
                                      {
                                        xtype: 'grid',
                                        minHeight: 80,
                                        reference:'interviewReviewJudgeGrid',
                                        columnLines: true,
                                        autoHeight: true,
                                        style:'border-bottom:1px #C1C1C1 solid;border-top:1px #C1C1C1 solid',
                                        selModel: {
                                            type: 'checkboxmodel'
                                        },
                                          dockedItems: [
                                              {
                                                  xtype: "toolbar",
                                                  items: [
                                                      {text: "新增",iconCls:"save", tooltip: "新增评委", handler: "addJudge"},
                                                      {text:"发送邮件",tooltip:"发送邮件",iconCls:"email",handler:"sendEmail"}
                                                  ]
                                              }
                                          ],
                                        store:{
                                            view:this,
                                            type:'interviewReviewJudgeAccount'
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
                                            },{
                                                text: "账户状态",
                                                dataIndex: 'judgeStatus',
                                                width:100,
                                                value:'A',/*默认有效*/
                                                renderer:function(v){
                                                    var x;
                                                    var jugaccStatusStore = transValue.get("TZ_JUGACC_STATUS");
                                                    if((x = jugaccStatusStore.find('TValue',v))>=0){
                                                        return jugaccStatusStore.getAt(x).data.TSDesc;
                                                    }else{
                                                        return v;
                                                    }
                                                }
                                            }
                                            ,{
                                                text: "所属评委组",
                                                dataIndex: 'judgeGroup',
                                                minWidth:100,
                                                flex:1
                                            },{
                                                text:"操作",
                                                align:'center',
                                                menuDisabled: true,
                                                sortable: false,
                                                minWidth:75,
                                                xtype: 'actioncolumn',
                                                items:[
                                                    {iconCls:'edit',tooltip:'编辑',handler:'editJudge'},
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
                                                name:'interviewReviewApplicantsNumber',
                                                fieldLabel:'面试评审考生人数',
                                                ignoreChangesFlag: true
                                            },{
                                                xtype:'displayfield',
                                                name: 'reviewCountAll',
                                                fieldLabel:'要求评审人次',
                                                ignoreChangesFlag: true
                                            }]
                                        }]
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
