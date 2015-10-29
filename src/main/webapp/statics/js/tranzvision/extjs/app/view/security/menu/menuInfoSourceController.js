Ext.define('KitchenSink.view.security.menu.menuInfoSourceController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.menuInfoSource',
    onSourceFormEnsure: function(){
    	  //组件注册表单
    	  var orgView = this.getView();
        var form = orgView.child("form").getForm();
        var menuOrg = form.findField("menuOrg").getValue();
        var sourceOrg = form.findField("sourceOrg").getValue();
        
        
        if (form.isValid()) {
        	if(menuOrg == sourceOrg){
        		form.findField("confirmBz").setValue("");
        		orgView.close(); 
        	}else{
        		var tzParams = this.getOrgSourceInfoParams();

						form.findField("confirmBz").setValue("Y");
						            
						Ext.tzSubmit(tzParams,function(responseData){ 
						},"",true,this);
						orgView.close(); 
        	} 

        }
    },
    getOrgSourceInfoParams: function(){
        //功能菜单表单
        var form = this.getView().child("form").getForm();
        //功能菜单标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
        }

        //提交参数
       var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUADD_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
		onSourceFormClose: function(){
			this.getView().close();
		}
});