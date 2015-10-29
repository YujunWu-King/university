Ext.define('KitchenSink.view.sendEmailAndSMS.emailServer.emailServerModel', {
    extend: 'Ext.data.Model',
    fields: [
		{name: 'emailservid'},
        {name: 'emailaddr'},
        {name: 'emailorg'},
		{name: 'emailorgName'},
        {name: 'chnsname'},
        {name: 'emlalias'}
	]
});
