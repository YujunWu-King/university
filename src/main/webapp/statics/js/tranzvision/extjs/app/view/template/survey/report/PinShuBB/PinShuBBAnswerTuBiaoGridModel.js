Ext.define('KitchenSink.view.template.survey.report.PinShuBB.PinShuBBAnswerTuBiaoGridModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'questionID'},     //问题编号
        {name: 'questionDesc'}, //问题描述
        {name: 'childQuestionId'},//子问题编号
        {name: 'childQuestionDesc'}, //子问题描述
        {name: 'answer'},//问题答案
        {name: 'answerPercent'},//问题答案百分比
        {name: 'answerFraction'}//问题答案分式

    ]
});
