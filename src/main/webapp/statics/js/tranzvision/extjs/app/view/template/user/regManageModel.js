Ext.define('KitchenSink.view.template.user.regManageModel', {
    extend: 'Ext.data.Model',
    fields: [
            {name: "siteId"}, 
            {name: "regId"},
            {name: "regName"},
            {name: "regEnName"},
            {name: "order"},
            {name: "isEnable"},
            {name: "isRequired"},
            {name: "isSysField"},
            {name: "regFieldType"},
            {name: "defaultValue"},
            {name: "isChange"},
            {name: "isShowWzsy"}
	]
});