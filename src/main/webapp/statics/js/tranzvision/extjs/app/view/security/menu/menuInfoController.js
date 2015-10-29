Ext.define('KitchenSink.view.security.menu.menuInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.menuInfo',

    
    onFormEnsure: function(){
    	  //组件注册表单
        var form = this.getView().child("form").getForm();
        var menuOrg = form.findField("menuOrg").getValue();
        var sourceOrg = form.findField("sourceOrg").getValue();
        if (form.isValid()) {
	    	  var tzParams = this.getOrgInfoParams();
	    	  var orgView = this.getView();
	    	  var tzParamsValidate = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUADD_STD","OperateType":"HTML","comParams":{"menuOrg":"'+menuOrg+'"}}';
				
					//加载数据
					Ext.Ajax.request({
						 async: false,
				     url: Ext.tzGetGeneralURL,
				     params: {
				        "tzParams": tzParamsValidate
				     },
				     async:false,  
						 success: function(response){
						 		var responseText = eval( "(" + response.responseText + ")" );

								//未存在;
								if(responseText.success == "true"){
								   //获取组件注册信息参数
				            form.findField("confirmBz").setValue("Y");
				            
				            Ext.tzSubmit(tzParams,function(responseData){ 
				            },"",true,this);
	        				 orgView.close();
								}
								//已经有该跟节点;
								if(responseText.success == "false"){
								   Ext.MessageBox.confirm('确认', '该机构节点已经被创建,确定要删除重建？', function(btnId){
										 if(btnId == 'yes'){					   
											  //获取组件注册信息参数
					            	form.findField("confirmBz").setValue("Y");
					            if(menuOrg != sourceOrg){
						            Ext.tzSubmit(tzParams,function(responseData){ 
						            },"",true,this);
						          } 
		        				 		orgView.close();  
										 }
									 },this);
								}
								//已经被其他树的节点占用了;
								if(responseText.success == "fail"){
								   Ext.Msg.alert("错误","该机构节点已经在其他树中使用，请与管理员联系！");
								}
						 }
					});
        }
    },
		onFormClose: function(){
			this.getView().close();
		},
    getOrgInfoParams: function(){
        //功能菜单表单
        var form = this.getView().child("form").getForm();
        //功能菜单标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"MENU","data":'+Ext.JSON.encode(form.getValues())+'}]';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            editJson = '{"typeFlag":"MENU","data":'+Ext.JSON.encode(form.getValues())+'}';
        }


        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }

        //提交参数
       var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENUADD_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    }
});