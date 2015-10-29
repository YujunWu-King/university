Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSelStuController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.interviewArrangeSelStuController',
    //清空过滤条件
    onClearFilters:function(btn){
        btn.findParentByType('grid').filters.clearFilters();
    },
    //关闭
    onPanelClose: function(){
        this.getView().close();
    }
});
