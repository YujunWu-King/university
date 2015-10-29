Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewApplicants', {
    extend: 'Ext.panel.Panel',
    xtype: 'GSMsetInterviewReviewApplicants',
    controller: 'GSMinterviewReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewApplicantsStore'
    ],
    title: '学术委员会成员管理',
    columnLines: true,
    viewConfig: {
        enableTextSelection: true
    },
    header: false,
    frame: true,
    style: "margin:8px",
    bodyStyle:'overflow-y:auto;overflow-x:hidden',
    initComponent:function(){
        var store = new KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewApplicantsStore(),
            judgeGroup = new KitchenSink.view.common.store.appTransStore("TZ_GSM_JUG_GRP");
        judgeGroup.load();
        Ext.apply(this,{
            items: [{
                xtype: 'form',
                reference: 'GSMinterviewReviewRuleForm',
                bodyStyle:'overflow-y:auto;overflow-x:hidden',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },

                items: [{
                    xtype: 'form',
                    reference:'GSMinterviewReviewDesc',
                    maxHeight:400,
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    border: false,
                    bodyPadding: 10,
                    bodyStyle:'overflow-x:hidden',
                    defaults:{
                        labelAlign:'top'
                    },
                    items: [
                        {
                            xtype:'displayfield',
                            name:'classID',
                            hidden:true
                        },{
                            xtype:'displayfield',
                            name:'batchID',
                            hidden:true
                        },{
                            xtype: 'textfield',
                            fieldLabel: "项目名称",
                            labelAlign:'left,',
                            name: 'startDate',/*  不修改后端数据结构，而是使用取startDate作为项目名称  */
                            fieldStyle:'background:#F4F4F4',
                            readOnly:true
                        },{
                            xtype: 'textfield',
                            fieldLabel: "批次名称",
                            name: 'startTime',/*  不修改后端数据结构，而是使用取startTime作为批次名  */
                            labelAlign:'left,',
                            fieldStyle:'background:#F4F4F4',
                            readOnly:true
                        },{
                            xtype: 'ueditor',
                            name: 'desc',
                            model:'normal',
                            maxHeight:'200px',
                            fieldLabel:'学术委员会评审说明',
                            zIndex:990
                        }
                    ]
                },{
                    xtype: 'grid',
                    minHeight: 200,
                    reference:'GSMinterviewReviewJudgeGrid',
                    columnLines: true,
                    autoHeight: true,
                    selModel: {
                        type: 'checkboxmodel'
                    },
                    bodyStyle:'overflow-y:auto;overflow-x:hidden',
                    dockedItems: [
                        {
                            xtype: "toolbar",
                            items: [
                                {text: "新增",iconCls:"add", tooltip: "新增评委", handler: "addXSJudge"},
                                {text:"发送邮件",tooltip:"发送邮件",iconCls:"email",handler:"sendEmail"}
                            ]
                        }
                    ],
                    store:store,
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
                            text: "手机",
                            dataIndex: 'judgeMobile',
                            minWidth:100,
                            flex:1
                        }
                        ,{
                            text: "邮箱",
                            dataIndex: 'judgeEmail',
                            minWidth:200,
                            flex:2
                        },
                        {
                            text: "对应OA账号",
                            dataIndex: 'judgeOAAccount',
                            minWidth:100,
                            flex:1
                        },{
                            text: "所属评委组",
                            dataIndex:'judgeGroup',
                            minWidth:100,
                            renderer:function(v){
                                var x;
                                if((x = judgeGroup.find('TValue',v))>=0){
                                    return judgeGroup.getAt(x).data.TSDesc;
                                }else{
                                    return v;
                                }
                            },
                            flex:1
                        }, {
                            text:"操作",
                            align:'center',
                            menuDisabled: true,
                            sortable: false,
                            minWidth:75,
                            xtype: 'actioncolumn',
                            items:[
                                {iconCls: 'remove',tooltip: '删除',handler:'removeJudge'}
                            ]
                        }]
                }]
            }]

        });
        this.callParent();
    },
    buttons: [{
        text: '保存',
        iconCls:"save",
        handler: 'onReviewXSSave'
    }, {
        text: '确定',
        iconCls:"ensure",
        handler: 'onReviewXSEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'onReviewXSClose'
    }]
});
