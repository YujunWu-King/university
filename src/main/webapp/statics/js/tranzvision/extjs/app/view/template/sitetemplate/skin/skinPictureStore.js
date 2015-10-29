/**
 * @author ZXW
 */
Ext.define('KitchenSink.view.template.sitetemplate.skin.skinPictureStore', {
    extend: 'Ext.data.Store',
    alias: 'store.skinPictureStore',
    model: 'KitchenSink.view.template.sitetemplate.skin.skinRoleModel',//图片列表json格式
    pageSize: 5,
	comID: 'TZ_GD_ZDMB_COM',
	pageID: 'TZ_GD_ZDPFPIC_STD',
	tzStoreParams: '',
	/*proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/skin/skinRoles.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}*/
	proxy: Ext.tzListProxy()	
});
