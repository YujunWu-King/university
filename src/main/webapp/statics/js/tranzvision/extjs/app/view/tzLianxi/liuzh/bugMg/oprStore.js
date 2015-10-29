Ext.define("KitchenSink.view.tzLianxi.liuzh.bugMg.oprStore",{
    extend : "Ext.data.Store",
    alias:'store.oprStore',
    model : "KitchenSink.view.tzLianxi.liuzh.bugMg.oprStruct",
    autoLoad:true,
    pageSize:10,
    proxy: {
        type: 'ajax',
        extraParams:{
            params:'{"type":"queryUser","oprID":"","name":"","manager":"wy"}'
        },
        url : '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_BUGMG.TZ_LZH_FUNCTION.FieldFormula.IScript_bugMg',

        reader: {
            type: 'json',
            totalProperty: 'content.total',
            rootProperty: ' content.root'
        }
    }
});