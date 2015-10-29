Ext.define('KitchenSink.view.security.user.orgStore', {
    extend: 'Ext.data.Store',
    alias: 'store.orgStore',
    fields: ['orgId','orgName'],
	autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/security/user/orgs.json',
				reader: {
					type: 'json',
					rootProperty: 'root'
				}
			}	
});
