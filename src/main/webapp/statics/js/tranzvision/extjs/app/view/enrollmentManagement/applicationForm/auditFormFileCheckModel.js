Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.auditFormFileCheckModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'classID'},
        {name: 'oprID'},
        {name: 'fileID'},
        {name: 'intro'},
        {name: 'remark'},
        {name: 'auditState'},
        {name: 'phrase'},
        {name: 'failedReason'}
	]
});
