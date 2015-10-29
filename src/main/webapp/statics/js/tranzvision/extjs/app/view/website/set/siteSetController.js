Ext.define('KitchenSink.view.website.set.userController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.siteSetMg', 
	requires: [
       'KitchenSink.view.security.user.userInfoPanel'
    ],
    addUserAccount: function() {
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
       		contentPanel.body.addCls('kitchensink-example');

            className = 'KitchenSink.view.security.user.userInfoPanel';
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

            cmp = new ViewClass();
			
            cmp.on('afterrender',function(){
				//用户角色信息列表
				var grid = this.lookupReference('userRoleGrid');
				var gridBar = this.lookupReference('userRoleToolBar');
				grid.store.load();
				gridBar.store.load();				
			});
			
            tab = contentPanel.add(cmp);     
			
            contentPanel.setActiveTab(tab);
	
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
    },
    editUserAccount: function() {
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
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
       		contentPanel.body.addCls('kitchensink-example');

            className = 'KitchenSink.view.security.user.userInfoPanel';
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

            cmp = new ViewClass();
            
			cmp.on('afterrender',function(){
				//用户账号信息表单
				var form = this.lookupReference('userAccountForm').getForm();
				//用户角色信息列表
				var grid = this.lookupReference('userRoleGrid');
				var gridBar = this.lookupReference('userRoleToolBar');
				
				Ext.Ajax.request({
					url: '/tranzvision/kitchensink/app/view/security/user/userInfo.json',
					params: {
						usAccNum: 1
					},
					waitTitle : '请等待' ,  
					waitMsg: '正在加载中', 
					success: function(response){
						var resText = response.responseText;
						var responseData = Ext.JSON.decode(resText);
						//用户账号信息数据
						var formData = responseData.formData;
						form.setValues(formData);
						//用户角色列表数据
						var roleList = responseData.listData;
	
						grid.store.loadData(roleList.root);
	
					}
				});	
				/*var store = new KitchenSink.view.security.user.userRoleStore();
				store.load({
					scope: this,
					callback: function(records, operation, success) {
						console.log(records);
					}
				});*/
			});
			
            tab = contentPanel.add(cmp);     
			
            contentPanel.setActiveTab(tab);
	
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
    },
	deleteUserAccount: function(){
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
				   var userStore = this.getView().store;
				   userStore.remove(selList);
				}												  
			},this);   
	   }
	}
});