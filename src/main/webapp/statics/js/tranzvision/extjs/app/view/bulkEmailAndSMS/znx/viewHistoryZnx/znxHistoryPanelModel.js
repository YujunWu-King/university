Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryPanelModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'status'},
        {name: 'sendNum'},
        {name: 'sendSucNum'},
        {name: 'sendFailNum'},
        {name: 'sendDt'},
        {name: 'operator'},
        {name:'znxQfId'},//邮件群发定义ID
        {name: 'taskID'},//同一个群发定义ID下的不同任务ID
		{name: 'sendTime'}
    ]
});
