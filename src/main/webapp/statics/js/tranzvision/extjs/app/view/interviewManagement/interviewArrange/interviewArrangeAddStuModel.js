Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeAddStuModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classId'},
        {name: 'appId'},
        {name: 'stuName'},
        {name: 'className'},
        {name: 'msCLPSPC'},
        {name: 'msZGFlag'},
        {name: 'lxEmail'},
        {name: 'label'},
        {name: 'sort'},
        {name: 'earlyDecision',type:'date'}
    ]
});