Ext.define('KitchenSink.view.template.sitetemplate.picStore', {
    extend: 'Ext.data.Store',
    alias: 'store.picStore',
    model: 'KitchenSink.view.template.sitetemplate.picModel',
	autoLoad: true,
	proxy: {
		type: 'ajax',
		//url : '/tranzvision/kitchensink/app/view/template/sitetemplate/pics.json',
		url: '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_ATT_D.TZ_GD_ATT_FILE.FieldFormula.Iscript_getAtt',
		reader: {
			type: 'json',
			rootProperty: 'picList'
		}
	}
});