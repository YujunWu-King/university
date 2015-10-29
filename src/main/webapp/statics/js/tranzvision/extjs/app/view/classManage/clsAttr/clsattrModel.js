Ext.define('KitchenSink.view.classManage.clsAttr.clsattrModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'attrSeq'},
        {name: 'attrValue'},
        {name: 'attrDesc'},
        {name: 'attrType'},
		{name: 'attrTypeDesc'},
        {name: 'attrEnabled'},
		{name: 'attrEnabledDesc'}
	]
});
