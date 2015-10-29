Ext.define('KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewApplicantWindow', {
    extend: 'Ext.window.Window',
    controller: 'GSMinterviewReview',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.grid.filters.Filters',
        'Ext.ux.MaximizeTool',
        'KitchenSink.view.GSMinterviewReview.interviewReview.interviewReviewApplicantsWindowStore'
    ],
    title: '系推荐报告',
    width: 800,
    height: 550,
    modal:true,
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype:'form',
        reference: 'GSMCommendationForm',
        layout: {
            type: 'vbox',
            align:'stretch'
        },
        border: false,
        style: 'overflow-y:auto;overflow-x:hidden',
        padding:10,
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 360,
            labelStyle: 'font-weight:bold;text-align:center',
            labelSeparator:''
        },
        items:[{
            xtype:'textfield',
            name:'classID',
            hidden:true
        },{
            xtype:'textfield',
            name:'batchID',
            hidden:true
        },{
            xtype:'textfield',
            name:'appInsID',
            hidden:true
        },{
            fieldLabel:'陈述拟录用的候选人履历简况、对其学术工作的评价</br>（科研成就、教学效果和社会服务）',
            xtype: 'textarea',
            name: 'situation',
            height:100
        },{
            fieldLabel:'与进入最后面试的其他候选人逐一比较</br>（优点和弱势）',
            xtype: 'textarea',
            name: 'comparison',
            height:100
        },{
            fieldLabel:'陈述本系在该研究领域的现状和需求',
            xtype: 'textarea',
            name: 'requirement',
            height:100
        },{
            fieldLabel:'对候选人的发展预期和拟定的教学科研任务，</br>特别是拟安排课堂教学的名称或内容等',
            xtype: 'textarea',
            name: 'task',
            height:100
        }]
    }],
    buttons: [{
        text:'保存',
        iconCls:"save",
        handler:"recommendationSave"
    },{
        text: '确定',
        iconCls:"ensure",
        handler: 'recommendationEnsure'
    }, {
        text: '关闭',
        iconCls:"close",
        handler: 'recommendationClose'
    }]
});