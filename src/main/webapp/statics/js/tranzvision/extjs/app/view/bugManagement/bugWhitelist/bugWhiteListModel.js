Ext.define('KitchenSink.view.bugManagement.bugWhitelist.bugWhiteListModel', {
    extend: 'Ext.data.Model',
    field:[
        {name:"account"},
        {name:"realName"},
        {name:"isWhite",type:'boolean',defaultValue:false}
    ]
});