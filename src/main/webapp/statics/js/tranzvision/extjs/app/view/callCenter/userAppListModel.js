Ext.define('KitchenSink.view.callCenter.userAppListModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'appInsId'},
        {name: 'oprid'},
        {name: 'classId'},
        {name: 'batchId'},
        {name: 'className'},
        {name: 'batchName'},
        {name: 'appCreateDtime'},
        {name: 'appBmStatus'},
        {name: 'isOnMaterials'},
        {name: 'materialResult'},
        {name: 'interviewDtime'},
        {name: 'interviewLocation'},
        {name: 'interviewResult'}
	]
});
