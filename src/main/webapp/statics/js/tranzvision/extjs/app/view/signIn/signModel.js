Ext.define('KitchenSink.view.signIn.signModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'id'},
        {name: 'openid'},
        {name: 'ibeaconName'},
        {name: 'signTime'},
        {name: 'signAccuracy'},
        {name: 'nickName'}
    ]
});
