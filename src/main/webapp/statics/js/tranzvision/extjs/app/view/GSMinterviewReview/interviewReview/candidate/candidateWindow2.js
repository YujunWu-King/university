Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.candidate.candidateWindow', {
    extend: 'Ext.window.Window',
    xtype: 'candidateWindow',
    controller: 'candidateController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.AdvancedVType',
        'Ext.ux.ProgressBarPager',
        'Ext.toolbar.Paging'
    ],
    title: '批次管理',
    width: 600,
    height: 400,
    modal:true,
    layout: {
        type: 'fit'
    },
    constructor: function (obj){
        Ext.apply(this, {
            items: [{
                xtype: 'form',
                reference: 'interviewReviewRuleForm',
                baseData:obj,
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',

                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },
                items: [{
                    xtype:'textfield',
                    name:'classID',
                    hidden: true,
                    ignoreChangesFlag: true
                },{
                    xtype: 'textfield',
                    fieldLabel: '招聘项目',
                    name: 'className',
                    allowBlank:false,
                    triggers: {
                        search: {
                            cls: 'x-form-search-trigger',
                            handler:'selectClass'
                        }
                    },
                    ignoreChangesFlag: true
                }, {
                    xtype: 'textfield',
                    name: 'batchName',
                    fieldLabel: '拟录取批次',
                    value:'请先选择班级',
                    fieldStyle:'color:#BFBFBF',
                    regex:/./,
                    readOnly:true,
                    ignoreChangesFlag: true
                }, {
                    xtype: 'datefield',
                    fieldLabel: "开始日期",
                    name: 'startDate',
                    itemId: 'startDate',
                    endDateField: ['endDate', 'reportDate'],
                    vtype: 'daterange',
                    repeatTriggerClick: true,
                    format: 'Y-m-d',
                    ignoreChangesFlag: true
                }, {
                    xtype: 'datefield',
                    fieldLabel: "结束日期",
                    name: 'endDate',
                    itemId: 'endDate',
                    startDateField: 'startDate',
                    vtype: 'daterange',
                    format: 'Y-m-d',
                    repeatTriggerClick: true,
                    ignoreChangesFlag: true
                }]
            }]
        });
        this.callParent();
    },
    buttons:[
        {text:'确定',iconCls:'ensure',handler:'newClassEnsure'},
        {text:'关闭',iconCls:'close',handler:function(btn){
            btn.findParentByType('window').close();
        }}]

});