Ext.define('KitchenSink.view.enrollmentManagement.attributeSet.formattrModel', {
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
