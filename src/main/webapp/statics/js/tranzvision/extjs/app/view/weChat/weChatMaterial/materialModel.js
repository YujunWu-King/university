Ext.define('KitchenSink.view.weChat.weChatMaterial.materialModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name:'jgId',type:'string'},
        {name:'wxAppId',type:'string'},
        {name:'index',type:'number'},
        {name:'caption', type:'string'},
        {name:'mediaId', type:'string'},
        {name:'mediaType',type:'string'},
        {name:'src', type:'string' },
        {name:'state',type:'string'},
        {name:'publishFlag',type:'string'}
    ]
});
