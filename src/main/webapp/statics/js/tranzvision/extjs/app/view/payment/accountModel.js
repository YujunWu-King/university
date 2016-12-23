Ext.define('KitchenSink.view.payment.accountModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name:'accountId'},
        {name: 'accountName'},
        {name: 'accountDescribe'},
        {name:'accountKey'},
        {name: 'accountState'},
        {name:'accountPlatform'}
    ]
});
