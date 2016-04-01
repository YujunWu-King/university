Ext.define('KitchenSink.view.classManage.clsAttr.setDropdownModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'attrValue'},
        {name: 'attrDropDownId'},
        {name: 'attrDropDownDesc'},
        {name: 'attrDDEnabled'},
		{name: 'attrDDEnabledDesc'},
		{name: 'attrOrder'}
	]
});
