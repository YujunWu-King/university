Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.bmLceditController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.bmLceditMb',
	//保存
	onFormSave: function(){
		var form = this.getView().child('form').getForm();
		var nr=form.findField("bmlc_mbnr").getValue();
		var Cts = "bblText";
		if(nr.indexOf("【显示公布结果占位符】") > 0 ){
			var comSiteParams = form.getValues();
			var comParams = '"update":[' + Ext.JSON.encode(comSiteParams) + "]";
			//edit_str = '{"data":'+Ext.JSON.encode(comSiteParams)+'}';
			var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_BJMB_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(tzParams){
			
			},"",true,this);
		}else{
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.bjmbzbxyzwf","编辑模板中必须有“【显示公布结果占位符】”"));
		}
	},
	onFormEnsure: function(btn){
		this.onFormSave("but_ensure");
		this.getView().close();
	},
	onFormClose: function(){
		this.getView().close();
	}
});