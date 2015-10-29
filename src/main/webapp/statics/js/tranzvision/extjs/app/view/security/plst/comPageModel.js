Ext.define('KitchenSink.view.security.plst.comPageModel', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'comID'},
        {name: 'pageID'},
        {name: 'pageName'},
        {name: 'readonly'},
        {name: 'modify',type:"boolean"}
    ]
});
