Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrPreviewController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.interviewArrPreviewController',
	//清除筛选条件
	onClearFilters:function(btn){
		btn.findParentByType('grid').filters.clearFilters();
	},

	//关闭
	onPanelClose:function(){
		this.getView().close();
	}
});