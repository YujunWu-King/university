Ext.define('KitchenSink.view.tzLianxi.liuzh.bugMg.addOprStore',{
    extend : "Ext.data.Store",
    alias:'store.addOprStore',
    model:'KitchenSink.view.tzLianxi.liuzh.bugMg.addOprModel',
    proxy : {
        type : "ajax",
        url :  '/tranzvision/kitchensink/app/view/tzLianxi/liuzh/bugMg/areadyIn.json',
        reader : 'json'
    }
});
