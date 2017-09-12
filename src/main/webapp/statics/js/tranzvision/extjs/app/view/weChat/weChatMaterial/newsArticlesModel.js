Ext.define('KitchenSink.view.weChat.weChatMaterial.newsArticlesModel', {
    extend: 'Ext.data.Model',
    fields: [
    	{name: 'media_id'},
        {name: 'title'},
        {name: 'thumb_media_id'},
        {name: 'author'},
        {name: 'digest'},
        {name: 'show_cover_pic'},
        {name: 'content'},
        {name: 'content_source_url'},
        {name: 'orderNum'}
	]
});
