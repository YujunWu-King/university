Ext.define('KitchenSink.view.weChat.weChatMaterial.materialModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name:'appId',type:'string'},
        { name:'index',type:'number'},
        { name:'mediaId', type:'string'},
        { name:'src', type:'string' },
        { name:'caption', type:'string'}
    ]
});
