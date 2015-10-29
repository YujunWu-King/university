Ext.define('KitchenSink.view.interviewManagement.interviewManage.interviewMgrModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classId'},
        {name: 'batchId'},
        {name: 'msPlanSeq'},
        {name: 'appId'},
        {name: 'oprId'},
        {name: 'stuName'},
        {name: 'stuSex'},
        {name: 'msGroupId'},
        {name: 'msDate',type:'date'},
        {name: 'msStartTime'},
        {name: 'msEndTime'},
        {name: 'msArrState'},
        {name: 'msConfigState'},
        {name: 'msJoinState'},
        {name: 'msResult'},
        {name: 'msArrDemo'},
        {name: 'sort'}
    ]
});
