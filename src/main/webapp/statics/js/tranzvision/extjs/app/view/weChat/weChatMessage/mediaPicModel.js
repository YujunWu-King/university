Ext.define('KitchenSink.view.weChat.weChatMessage.mediaPicModel', {
    extend: 'Ext.data.Model',
    fields: [
        { name:'index',type:'number'},
        { name:'mediaId', type:'string' },
        { name:'src', type:'string' },
        { name:'caption', type:'string' }
    ]
});
