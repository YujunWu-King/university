Ext.define('KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.resTempletController', 
	requires: [
       'KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletInfo'
    ],
    addResTemplet: function() {
			//是否有访问权限
			
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_RES_TMPL_MG_COM"]["TZ_RES_TMPL_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RES_TMPL_STD，请检查配置。');
				return;
			}
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
       		contentPanel.body.addCls('kitchensink-example');

            //className = 'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfo';
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

            cmp = new ViewClass();
			
			cmp.actType = "add";
			
			
			cmp.on('afterrender',function(panel){
				//邮箱信息配置数据表单

				//组件注册表单信息;
				var form = panel.child('form').getForm();
				
				var tzParams = '{"restemporg":"' + Ext.tzOrgID + '"}';
				tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RES_TMPL_MG_STD","OperateType":"QF","comParams":' + tzParams +'}';

				Ext.Ajax.request({
					url: Ext.tzGetGeneralURL,
					params: {
						tzParams: tzParams
					},
					success: function(response){
					
						var responseText = Ext.util.JSON.decode(response.responseText);

						var tempemailserv = responseText.comContent.tempemailserv;
						var tempsmsserv = responseText.comContent.tempsmsserv;
						var emailaddr = responseText.comContent.emailaddr;
						var smssevname = responseText.comContent.smssevname;
						
						var initValues = {
							"tempemailserv":tempemailserv,
							"emailaddr":emailaddr,
							"tempsmsserv":tempsmsserv,
							"smssevname":smssevname
						}
						form.setValues(initValues);
					},
					failure: function (response) {
						//Ext.MessageBox.alert("错误", "错误");
					}
				});
				
				form.setValues({"isneed":"Y"});
				/*
				form.findField("restemporg").on('change',
					function(combox,newValue,oldValue){
					
							var tzParams = '{"restemporg":"' + newValue + '"}';
							tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RES_TMPL_MG_STD","OperateType":"QF","comParams":' + tzParams +'}';

							Ext.Ajax.request({
								url: Ext.tzGetGeneralURL,
								params: {
									tzParams: tzParams
								},
								success: function(response){
								
									var responseText = Ext.util.JSON.decode(response.responseText);

									var tempemailserv = responseText.comContent.tempemailserv;
									var tempsmsserv = responseText.comContent.tempsmsserv;
									var emailaddr = responseText.comContent.emailaddr;
									var smssevname = responseText.comContent.smssevname;
								
									form.findField("tempemailserv").setValue(tempemailserv);
									form.findField("emailaddr").setValue(emailaddr);
									form.findField("tempsmsserv").setValue(tempsmsserv);
									form.findField("smssevname").setValue(smssevname);
									
								},
								failure: function (response) {
									//Ext.MessageBox.alert("错误", "错误");
								}
							});
				})	*/
			});
			
            tab = contentPanel.add(cmp);  

			tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
			
            contentPanel.setActiveTab(tab);
	
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
    },
    editResTemplet: function(view, rowIndex) {
		 var store = view.findParentByType("grid").store;
		 var selRec = store.getAt(rowIndex);
		 //组件ID
	   	 var restempid = selRec.get("restempid");
		 var restemporg = selRec.get("restemporg");
	     //显示元模板信息编辑页面
	     this.editResTmplInfoByKey(restempid,restemporg);
			
    },
	editSelResTemplet: function() {
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
        //元模版编号
        var restempid = selList[0].get("restempid");
		var restemporg = selList[0].get("restemporg");
        //显示邮箱服务器编辑页面
        this.editResTmplInfoByKey(restempid,restemporg);
    },
	editResTmplInfoByKey: function(restempid,restemporg) {
	
			//是否有访问权限
			var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_RES_TMPL_MG_COM"]["TZ_RES_TMPL_STD"];
			if( pageResSet == "" || pageResSet == undefined){
				Ext.MessageBox.alert('提示', '您没有修改数据的权限');
				return;
			}
			//该功能对应的JS类
			var className = pageResSet["jsClassName"];
			if(className == "" || className == undefined){
				Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RES_TMPL_STD，请检查配置。');
				return;
			}
			
			var contentPanel,cmp, className, ViewClass, clsProto;
			var themeName = Ext.themeName;
			
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
       		contentPanel.body.addCls('kitchensink-example');

         //   className = 'KitchenSink.view.sendEmailAndSMS.emailServer.emailServerInfo';
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

            cmp = new ViewClass();
			//操作类型设置为更新
			cmp.actType = "update";
            
			cmp.on('afterrender',function(panel){
				//邮箱信息配置数据表单
				//var form = this.lookupReference('emailServerForm').getForm();
				
				//组件注册表单信息;
				var form = panel.child('form').getForm();
				form.findField("restempid").setReadOnly(true);
				form.findField("restemporg").setReadOnly(true);
				form.findField("restempid").addCls("lanage_1");
				form.findField("restemporg").addCls("lanage_1");
				
				var tzParamsJson = {
						"ComID":"TZ_RES_TMPL_MG_COM",
						"PageID":"TZ_RES_TMPL_STD",
						"OperateType":"QF",
						"comParams":{
							"restempid": restempid,
							"restemporg": restemporg
						}
				} 
				//参数
				var tzParams = Ext.JSON.encode(tzParamsJson);
				//var tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RES_TMPL_STD","OperateType":"QF","comParams":{"restempid":"'+restempid+'","restemporg":"'+restemporg+'"}}';
				
				/*
				Ext.Ajax.request({
					url: '/tranzvision/kitchensink/app/view/sendEmailAndSMS/emailServer/emailServerInfo.json',
					params: {
						"emailaddr":"44577@qq.com",   
						"orgid":"EMBA"
					},
					waitTitle : '请等待' ,  
					waitMsg: '正在加载中', 
					success: function(response){
						var resText = response.responseText;
						var responseData = Ext.JSON.decode(resText);
						//邮箱信息配置数据
						form.setValues(responseData);
	
					}
				});	
				*/
				
				Ext.tzLoad(tzParams,function(responseData){
					
					//console.log(responseData);
					//var formData = responseData.formData;
					form.setValues(responseData);	
					//参数信息数据
					var	resTmplRaraGrid = panel.down('grid[name=resTmplRaraGrid]');
					var tzStoreParamsParaJson = {
							"restempid": restempid,
							"restemporg": restemporg,
							"listtype": "PARA"
					}
					//var tzStoreParamsPara = '{"restempid":"'+restempid+'","restemporg":"'+restemporg+'","listtype":"'+'PARA'+'"}';
					var tzStoreParamsPara = Ext.JSON.encode(tzStoreParamsParaJson);
					resTmplRaraGrid.store.tzStoreParams = tzStoreParamsPara;
					resTmplRaraGrid.store.load();
					/**/
					//内容采集规则数据
					//var	resTmplContentGrid = panel.down('grid[name=resTmplContentGrid]');
					//var tzStoreParamsContent = '{"restempid":"'+restempid+'","restemporg":"'+restemporg+'","listtype":"'+'CONTENT'+'"}';
					//resTmplContentGrid.store.tzStoreParams = tzStoreParamsContent;
					//resTmplContentGrid.store.load();
					
				});
			});
			
            tab = contentPanel.add(cmp);     
			tab.on(Ext.tzTabOn(tab,this.getView(),cmp));
            contentPanel.setActiveTab(tab);
	
            Ext.resumeLayouts(true);

            if (cmp.floating) {
                cmp.show();
            }
    },
	searchComList: function(btn){     //searchComList为各自搜索按钮的handler event;

        Ext.tzShowCFGSearch({            

            cfgSrhId: 'TZ_RES_TMPL_MG_COM.TZ_RES_TMPL_MG_STD.TZ_TMP_DEFN_VW',           
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });    
    },
	deleteSelResTemplet: function(){
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
				   var resTempletStore = this.getView().store;
                    resTempletStore.remove(selList);
				}												  
			},this);   
	   }
	},
    deleteResTemplet: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
	saveResTemplet: function(btn){
        //资源集合列表
        var grid = btn.findParentByType("grid");
        //资源集合信息数据
        var store = grid.getStore();
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
        }else{
            return;
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RES_TMPL_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
	ensureResTemplet: function(btn){
        //资源集合列表
        var grid = btn.findParentByType("grid");
        //资源集合信息数据
        var store = grid.getStore();
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
			//提交参数
			var tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RES_TMPL_MG_STD","OperateType":"U","comParams":{'+comParams+'}}';
			//保存数据
			Ext.tzSubmit(tzParams,function(){
				grid.close();
			},"",true,this);
        }else{
            grid.close();
        }  
    },
	closeResTemplet: function(btn){
        
        var grid = btn.findParentByType("grid");
        
        grid.close();
		
    }
});


