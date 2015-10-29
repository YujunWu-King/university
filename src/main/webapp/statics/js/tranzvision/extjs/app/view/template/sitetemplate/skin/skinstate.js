Ext.define('KitchenSink.view.template.sitetemplate.skin.skinstate', {
    extend: 'Ext.data.Store',
    alias: 'store.skinstate',
    fields: ['id','state'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/skin/skinstate.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
