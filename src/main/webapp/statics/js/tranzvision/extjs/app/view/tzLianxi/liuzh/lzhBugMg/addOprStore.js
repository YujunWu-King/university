Ext.define('KitchenSink.view.tzLianxi.liuzh.bugMg.addOprStore',{
    extend : "Ext.data.Store",
    alias:'store.addOprStore',
    model:'KitchenSink.view.tzLianxi.liuzh.bugMg.addOprModel',
    proxy : {
        type : "ajax",
        url :  'http://101.200.181.213:8550/psc/ALTZDEV/EMPLOYEE/CRM/s/WEBLIB_GD_LX.TZ_GD_LX.FieldFormula.iScript_bugMg',
        reader : 'json',
        params:{
            oprType:'queryBug',
            bugID:'',
            bugName:'',
            bugStatus:''
        }
    }
});
