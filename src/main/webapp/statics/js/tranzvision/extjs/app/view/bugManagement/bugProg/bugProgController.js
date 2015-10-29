Ext.define('KitchenSink.view.bugManagement.bugProg.bugProgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.bugProgController',
    onClose: function(btn){
        //关闭窗口
        this.getView().close();
    }
});