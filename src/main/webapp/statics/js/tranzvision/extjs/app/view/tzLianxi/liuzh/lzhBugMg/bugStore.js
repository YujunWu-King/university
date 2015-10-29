Ext.define('KitchenSink.view.tzLianxi.liuzh.lzhBugMg.bugStore',{
    extend:'Ext.data.Store',
    model:'KitchenSink.view.tzLianxi.liuzh.lzhBugMg.bugModel',
    alias:"store.bugStore",
    autoLoad: true,
    pageSize:10,
    proxy: {
        type: 'ajax',
        extraParams:{
            params:'{"type":"queryBug","bugID":"","name":"","status":"","manager":"lzh"}'
        },
        url :'/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',

        reader: {
            type: 'json',
            totalProperty: 'content.total',
            rootProperty: ' content.root'
        }
    }
});
