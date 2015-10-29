Ext.define("KitchenSink.view.tzLianxi.liuzh.lzhBugMg.oprStore",{
    extend : "Ext.data.Store",
    alias:'store.oprStore',
    model : "KitchenSink.view.tzLianxi.liuzh.lzhBugMg.oprStruct",
    autoLoad:true,
    pageSize:10,
    proxy: {
        type: 'ajax',
        extraParams:{
            params:'{"type":"queryUser","oprID":"","name":"","manager":"lzh"}'
        },
        url : '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',

        reader: {
            type: 'json',
            totalProperty: 'content.total',
            rootProperty: ' content.root'
        }
    }
});