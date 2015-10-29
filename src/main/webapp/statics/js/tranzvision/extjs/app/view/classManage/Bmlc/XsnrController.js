Ext.define('KitchenSink.view.classManage.Bmlc.XsnrController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.BmlcXsnr', 
	onFormSave: function(btn){
		var form = this.getView().child("form").getForm();
		contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		var templateGrid = contentPanel.child("ClassInfo").child("form").child("tabpanel").getActiveTab().child('grid');
	 	var selRec = templateGrid.store.getAt(form.getValues()['h_no']);
   	 	var bj_id = selRec.set("bmlc_desc",form.getValues()['bmlc_desc']);
	},
	onFormEnsure: function(){
		this.onFormSave("but_ensure");
		this.getView().close();
	},
	onFormClose: function(){
		this.getView().close();
	}
});