Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSelStuModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'msArrInfoGridRowIndex'},
        {name: 'classId'},
        {name: 'batchId'},
        {name: 'appId'},
        {name: 'oprId'},
        {name: 'stuName'},
        {name: 'msCLPSPC'},
        {name: 'msZGFlag'},
        {name: 'city'},
        {name: 'country'},
        {name: 'skype'},
        {name: 'lxEmail'},
        {name: 'timezone'},
        {name: 'timezomeDiff'},
        {name: 'bjDate'},
        {name: 'bjTime'},
        {name: 'localStartDate',type:'date'},
        {name: 'localStartTime'},
        {name: 'localFinishDate',type:'date'},
        {name: 'localFinishTime'},
        {name: 'arrState'},
        {name: 'confState'},
        {name: 'timeSort'}
    ]
});