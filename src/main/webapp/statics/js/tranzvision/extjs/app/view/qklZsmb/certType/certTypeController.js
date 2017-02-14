Ext.define('KitchenSink.view.qklZsmb.certType.certTypeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.certTypeController',
    searchCertType:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId:'TZ_CERTTYPE_COM.TZ_TYPE_LIST_STD.TZ_CERT_TYPE_VW',
            callback:function(searchCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = searchCfg;
                store.load();
            }
        });
    },
    addCertType: function() {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CERTTYPE_COM"]["TZ_TYPE_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_TYPE_INFO_STD，请检查配置。');
            return;
        }

      
        var win = this.lookupReference('certTypeInfo');
        if (!win) {
			//className = 'KitchenSink.view.security.com.pageRegWindow';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为新增
		win.actType = "add";
		
        win.show();
    },
    eidtCertType:function(){
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要修改的记录");
            return;
        }else if(checkLen > 1){
            Ext.Msg.alert("提示","只能选择一条要修改的记录");
            return;
        }
        var certTypeId = selList[0].get("certTypeId");
        var JgId = selList[0].get("JgId");
        this.editCertTmplByID(JgId,certTypeId);
    },
    editCurrType:function(view,rowIndex){
        var store = view.findParentByType("grid").store;
        var selPlan = store.getAt(rowIndex) ;
        var certTypeId = selPlan.get("certTypeId");
        var JgId = selPlan.get("JgId");
        this.editCertTmplByID(JgId,certTypeId);
    },
    editCertTmplByID:function(JgId,certTypeId){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_CERTTYPE_COM"]["TZ_TYPE_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示','您没有修改数据的权限');
            return;
        }
        //该功能对应的js类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID为：TZ_TYPE_INFO_STD，请检查配置。')
            return;
        }

        var win = this.lookupReference('certTypeInfo');
        if (!win) {
			//className = 'KitchenSink.view.security.com.pageRegWindow';
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为新增
		
		 var form=win.child('form').getForm();
		 form.findField("certTypeId").setReadOnly(true);
         form.findField("certTypeId").addCls("lanage_1");
         var formValue = form.getValues();
         
         //参数
         var tzParams = '{"ComID":"TZ_CERTTYPE_COM","PageID":"TZ_TYPE_INFO_STD","OperateType":"QF","comParams":{"certTypeId":"'+certTypeId+'","JgId":"'+JgId+'"}}';
         //加载数据
         Ext.tzLoad(tzParams,function(responseData){
         	var formData = responseData.formData;
             form.setValues(formData);
             win.actType = "update";
            });
        win.show();
        cmp = new ViewClass();
       
    },
    deleteCurrType: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    saveTypeList:function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var removeJson = "";
        var comParams="";
        var removeRecs = store.getRemovedRecords();

        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }else{
            return;
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_CERTTYPE_COM","PageID":"TZ_TYPE_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
        var comView = this.getView();
        //保存数据
        if(comParams!=""){
            Ext.tzSubmit(tzParams,function(){
                store.commitChanges();
            },"",true,this);
        }
    },
    ensureTypeList:function(btn){
        this.saveTypeList(btn);
        //关闭窗口
        var comView = this.getView();
        comView.close();
    },
    closeTypeList:function(btn){
        //关闭窗口
        this.getView().close();
    },
    onCertTypeSave:function(btn){
    	 var win = btn.findParentByType("window");
		//操作类型，add-添加，edit-编辑
		var actType = win.actType;
		var form = win.child("form").getForm();
		if (form.isValid()) {
			
			//新增
			var comParams="";
			if(actType == "add"){
				comParams = '"add":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}else{
				//修改
				comParams = '"update":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}
			var tzParams = '{"ComID":"TZ_CERTTYPE_COM","PageID":"TZ_TYPE_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData){
				
					form.findField("certTypeId").setReadOnly(true);
                    form.findField("certTypeId").addCls('lanage_1');
                    form.findField("certTypeId").setValue(responseData);
                    var store=win.findParentByType("grid").store;
                    win.actType="update";
                    store.reload();
			},"",true,this);
		}
	},
	onCertTypeEnsure:function(btn){
   	 var win = btn.findParentByType("window");
		//操作类型，add-添加，edit-编辑
		var actType = win.actType;
		var form = win.child("form").getForm();
		if (form.isValid()) {
			
			//新增
			var comParams="";
			if(actType == "add"){
				comParams = '"add":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}else{
				//修改
				comParams = '"update":[{"data":'+Ext.JSON.encode(form.getValues())+'}]';
			}
			var comView = this.getView();
			var tzParams = '{"ComID":"TZ_CERTTYPE_COM","PageID":"TZ_TYPE_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
			Ext.tzSubmit(tzParams,function(responseData){
				
				 /*form.findField("certTypeId").setReadOnly(true);
                 form.findField("certTypeId").addCls('lanage_1');
                 form.findField("certTypeId").setValue(responseData);*/
                 var store=win.findParentByType("grid").store;
                 win.actType="update";
                 store.reload();
                 comView.close();
			},"",true,this);
		}
       // this.onCertTypeSave(btn);
        //关闭窗口
       
    },
    //关闭
    onCertTypeClose: function(){
        this.getView().close();
    }
});