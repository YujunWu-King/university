Ext.define('KitchenSink.view.security.menu.menuController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.menuMg',
	requires: [
       'KitchenSink.view.security.menu.menuInfoPanel'
    ],

    addFuncMenu: function() {
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_MENU_COM"]["TZ_AQ_MENUADD_STD"];
			if( pageResSet == "" || typeof(pageResSet) == "undefined" ){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || typeof(className) == "undefined"  ){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_MENUADD_STD，请检查配置。');
				return;
			}
			
			var win = this.lookupReference('securityMenuInfoPanel');
	        
	    if (!win) {
				Ext.syncRequire(className);
				ViewClass = Ext.ClassManager.get(className);
			  //新建类
	      win = new ViewClass();
	     
	      this.getView().add(win);
	      var grid = this.getView();
	      win.on('close',function( panel, eOpts ){
	      	  var confirmBz =  panel.child("form").getForm().findField("confirmBz").getValue();
	      	  if(confirmBz != "Y"){
	      	  	return;
	      	  } 
	      	  grid.store.reload();	  
	      	  var orgId = panel.child("form").getForm().findField("menuOrg").getValue();

	      	  //this.editfuncMenu(orgId);
   	
		        var contentPanel,cmp, className, ViewClass, clsProto;
		        var themeName = Ext.themeName;
		        //是否有访问权限
		        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_MENU_COM"]["TZ_AQ_MENUXX_STD"];
		         if( pageResSet == "" || pageResSet == undefined){
		         Ext.MessageBox.alert('提示', '您没有修改数据的权限');
		         return;
		         }
		         //该功能对应的JS类
		         className = pageResSet["jsClassName"];
		         if(className == "" || className == undefined){
		         Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_MENUXX_STD，请检查配置。');
		         return;
		         }
		        className = 'KitchenSink.view.security.menu.menuEditPanel';
		
		        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		        contentPanel.body.addCls('kitchensink-example');
		
		        if(!Ext.ClassManager.isCreated(className)){
		            Ext.syncRequire(className);
		        }
		        ViewClass = Ext.ClassManager.get(className);
		
		        clsProto = ViewClass.prototype;
		
		        if (clsProto.themes) {
		            clsProto.themeInfo = clsProto.themes[themeName];
		
		            if (themeName === 'gray') {
		                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
		            } else if (themeName !== 'neptune' && themeName !== 'classic') {
		                if (themeName === 'crisp-touch') {
		                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
		                }
		                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
		            }
		            // <debug warn>
		            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
		            if (!clsProto.themeInfo) {
		                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
		                    themeName + '\'. Is this intentional?');
		            }
		            // </debug>
		        }
		
		        cmp = new ViewClass({ menuId: orgId });
		        //操作类型设置为更新
		        cmp.actType = "update";

		        cmp.on('close',function(panel){
		        	try{
				         	 grid.store.reload();
				      }catch(e){
				         	
				      }	  
				    });
		
		        tab = contentPanel.add(cmp);
		
		        contentPanel.setActiveTab(tab);
		
		        Ext.resumeLayouts(true);
		
		        if (cmp.floating) {
		            cmp.show();
		        }
	      });
	        
	    }
      
       win.show();
    },
    editFuncMenu: function() {
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择一条要修改的记录");
            return;
        }else if(checkLen >1){
            Ext.Msg.alert("提示","只能选择一条要修改的记录");
            return;
        }

        var orgId = selList[0].get("orgId");
        this.editfuncMenu(orgId);
    },
	editFuncMenuById: function(view, rowIndex){
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //菜单机构
	   var orgId = selRec.get("orgId");
	   //显示
	   this.editfuncMenu(orgId);
		},
    editfuncMenu: function(orgId){
    	  
    		grid = this.getView();
    	
        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_AQ_MENU_COM"]["TZ_AQ_MENUXX_STD"];
        if( pageResSet == "" || pageResSet == undefined){
	         Ext.MessageBox.alert('提示', '您没有修改数据的权限');
	         return;
	      }
	      //该功能对应的JS类
	      className = pageResSet["jsClassName"];
	      if(className == "" || className == undefined){
	         Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_AQ_MENUXX_STD，请检查配置。');
	         return;
        }
        className = 'KitchenSink.view.security.menu.menuEditPanel';

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        
        

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);

        clsProto = ViewClass.prototype;

        if (clsProto.themes) {
            clsProto.themeInfo = clsProto.themes[themeName];

            if (themeName === 'gray') {
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.classic);
            } else if (themeName !== 'neptune' && themeName !== 'classic') {
                if (themeName === 'crisp-touch') {
                    clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes['neptune-touch']);
                }
                clsProto.themeInfo = Ext.applyIf(clsProto.themeInfo || {}, clsProto.themes.neptune);
            }
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
            // </debug>
        }

        cmp = new ViewClass({ menuId: orgId });
        //操作类型设置为更新
        cmp.actType = "update";

        cmp.on('close',function(panel){
        	try{
		         grid.store.reload();
		      }catch(e){
		      }	  
		    });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    deleteFuncMenuById:function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                var selRec = store.removeAt(rowIndex);
            }
        },this);
    },
    deleteFuncMenu:function(btn){
       //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
				if(btnId == 'yes'){					   
				   var orgMenuStore = this.getView().store;
				   orgMenuStore.remove(selList);
				}
			},this);
	   }
    },
    saveOrgList:function(btn){
        var store = this.getView().getStore();
        if(store.getRemovedRecords().length>0){
            this.submitDeleteOrgMenu();
        }
        if(btn.name=="ensure"){
            this.getView().close();
        }
    },
    closeOrgList:function(btn){
        this.getView().close();
    },
    submitDeleteOrgMenu: function(){
			var comParams = "";
			var editJson = "";
			var store = this.getView().getStore();
			
			//删除json字符串
			var removeJson = "";
			//删除记录
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
			}
			//提交参数
			var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENULIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
	        Ext.tzSubmit(tzParams,function(){
				store.reload();		   
			},"",true,this);
		},
	//可配置搜索
	cfgSearchMenu: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_AQ_MENU_COM.TZ_AQ_MENULIST_STD.TZ_GD_GNCD_VW',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
	//重新计算功能菜单树节点编号;
	refreshFuncMenu: function(bt){
		var tzParams = '{"ComID":"TZ_AQ_MENU_COM","PageID":"TZ_AQ_MENULIST_STD","OperateType":"REFRESHMENU","comParams":{ }}';
    //加载数据
    Ext.tzLoad(tzParams,function(responseData){
    	//菜单那信息数据
			if(responseData.success=="true"){
				 Ext.Msg.alert("提示","功能菜单树节点编号重新计算成功。");
			}else{
				Ext.Msg.alert("提示",responseData.errorMsg);
			} 
			
    });
	}
		
});