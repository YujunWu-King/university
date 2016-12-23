Ext.define('KitchenSink.view.payment.platformModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name:'platformId'},
        {name: 'platformName'},
        {name: 'platformInterface'},
        {name: 'returnUrl'},
        {name: 'dealClass'},
        {name: 'platformState'},
        {name: 'waitTime'},
        {name: 'platformDescribe'}
    ]
});
