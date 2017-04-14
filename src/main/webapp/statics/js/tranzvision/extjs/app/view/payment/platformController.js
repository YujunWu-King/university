Ext.define('KitchenSink.view.payment.platformController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.platformController',
    requires:['KitchenSink.view.payment.platformEditForm'],
    /*查询支付账户*/
    selectPlatform:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_ZFPTGL_COM.TZ_ZFPTGL_STD.TZ_ZFPTGL_VM', //
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
                //alert("查询支付平台结束");
            }
        });
    },
    /*增加支付账户*/
    addPlatform:function(btn){
    	//alert("增加支付账户执行");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZFPTGL_COM"]["TZ_ZFPTGL_EDIT_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        var className = pageResSet["jsClassName"];
        //alert("className:"+className);
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZFZHGL_EDIT_STD，请检查配置。');
            return;
        }

        ViewClass = Ext.ClassManager.get(className);
        cmp=new ViewClass();
        //cmp.actType = "update";
        //alert("cmp:"+cmp);
        cmp.show();
        this.getView().add(cmp);
    	//alert("增加支付频台执行完毕");
    },
    savePlatform:function(btn){
    	//alert("执行增加支付平台保存！");
    	var win = btn.findParentByType("window");
        var form=win.down("form").getForm();
        if(!form.isValid() ){
            return false;
        }
        var formParams = form.getValues();
        var tzParams = '{"ComID":"TZ_ZFPTGL_COM","PageID":"TZ_ZFPTGL_STD","OperateType":"U","comParams":{"update":['+Ext.JSON.encode(formParams)+']}}';
        Ext.tzSubmit(tzParams,function(){
            
           // form.setValues(response.formData);
        	//增加完毕数据 父窗体grid数据重新加载
        	//alert("父窗体："+win.findParentByType("grid"));
			if(win.parentGridStore!=null&&win.parentGridStore!=""){
				//win.findParentByType("grid").getStore().reload();
			}
        },"",true,this);
    },
    //新增窗口确定
    ensurePlatform:function(btn){
    	this.savePlatform(btn);
    	this.closeForm(btn);
    },
    /*关闭新增窗口*/
    closeForm:function(btn){
    	var win=btn.findParentByType("window");
        var grid=win.findParentByType("grid");
        var store=grid.getStore();
        store.reload();
        win.close();
    },
    deleteOnePlatform:function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    //删除账户,从界面中删除，必须保存才能从数据库中删除
    deletePlatform:function(btn,rowIndex){
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
        	selList=btn.getSelectionModel().getSelected();
            if(selList.length==0){
            	 Ext.Msg.alert("提示","请选择要删除的记录");
                 return;
            }
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var resSetStore =  btn.findParentByType("grid").store;
                    resSetStore.remove(selList);
                }
            },this);
        }
    },
    //支付账户管理显示界面 关闭
    platformInfoWindowClose:function(btn){
    	var panel=btn.findParentByType("panel");
    	panel.close();
    },
    //支付账户管理显示界面 保存
    platformInfoWindowSave:function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var removeJson = "";
        var removeRecs = store.getRemovedRecords();
        for(var i=0;i<removeRecs.length;i++){
        	console.log("account del:")
        	console.dir(removeRecs[i]);
            if(removeJson == ""){
            	
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
            console.log("here");
            console.log("removeJson:"+removeJson);
        }
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }else{
            return;
        }
        //console.log(comParams);
        var tzParams = '{"ComID":"TZ_ZFPTGL_COM","PageID":"TZ_ZFPTGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"保存成功",true,this);
    },
  //编辑支付平台
    editPlatform:function(obj){
    	//alert("执行编辑支付平台！");
    	//拿到当前编辑按钮 直属父类grid
    	var grid=obj.findParentByType("grid");
		//获取当前选中行
    	var selList = grid.getSelectionModel().getSelection();
    	
    	var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	    }
		var platformId= selList[0].get("platformId");
	   
	    //取得进行编辑的窗体
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZFPTGL_COM"]["TZ_ZFPTGL_EDIT_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        var className = pageResSet["jsClassName"];
        //alert("className:"+className);
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZFPTGL_EDIT_STD，请检查配置。');
            return;
        }
    	var view=this.getView();
    	//加载form中的数据
		var tzParams = '{"ComID":"TZ_ZFPTGL_COM","PageID":"TZ_ZFPTGL_EDIT_STD","OperateType":"QF","comParams":{"platformId":"'+platformId+'"}}';
		Ext.tzLoad(tzParams,function(response){
		     //将查询数据结果 放入form中
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			var win = new ViewClass();
			view.add(win);
			var form = win.child("form").getForm();
			form.setValues(response.formData);
			win.show();
		});
		}
});

