Ext.define('KitchenSink.view.template.survey.report.PinShuBB.PinShuBBQuestionListModel', {
    extend: 'Ext.data.Model',
    fields: [
    {name: 'questionID'},//父问题id
        {name: 'questionDesc'},//父问题描述
        {name: 'childQuestionId'},//子问题id
        {name: 'childQuestionDesc'}//子问题描述
    ]
});
