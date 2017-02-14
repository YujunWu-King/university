Ext.define('KitchenSink.view.ksShujuDr.ksShujuDrController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ksShujuDrController',
    
    import:function(view,rowIndex){
        var store = view.findParentByType("grid").store;
        var select = store.getAt(rowIndex) ;
        var tplId = select.get("tplId");
        Ext.tzUnifiedImport(tplId);
    }
   
});
