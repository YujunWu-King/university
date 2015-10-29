/**
 * @author ZXW
 */
Ext.define('KitchenSink.view.siteManage.siteManage.skin.skinPictureStore', {
    extend: 'Ext.data.Store',
    alias: 'store.skinPictureStore',
    model: 'KitchenSink.view.template.sitetemplate.skin.skinRoleModel',//图片列表json格式
    pageSize: 5,
	//autoLoad: true,
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/skin/skinRoles.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}
});
