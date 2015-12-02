Ext.define('KitchenSink.view.sendEmailAndSMS.resTempletDef.resTempletInfoMth', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.resTempletInfoMth', 
	onFormSave: function(){
		var comView = this.getView();
		var form = this.getView().child("form").getForm();
		if (form.isValid()) {
			var tzParams = this.getResTmplInfoParams();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";
				form.findField("restempid").setReadOnly(true);
				form.findField("restemporg").setReadOnly(true);
				form.findField("restempid").addCls("lanage_1");
				form.findField("restemporg").addCls("lanage_1");

				form.setValues({"restempid":responseData.restempid});
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("resTempletDef").store.reload();
			},"",true,this);
		}
	},
	onFormEnsure: function(){
		//获取窗口
		var comView = this.getView();
		//信息表单
		var form = comView.child("form").getForm();
		if (form.isValid()) {
			/*保存页面信息*/
			var tzParams = this.getResTmplInfoParams();	
			Ext.tzSubmit(tzParams,function(responseData){
				//重置表单
				form.reset();
				//关闭窗口
				comView.close();
				comView.actType = "update";	
				var contentPanel;
				contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
				contentPanel.child("resTempletDef").store.reload();
			},"",true,this);

		}	
	},
	onFormClose: function(){
		this.getView().close();
	},
	addParaRow: function(){
	
		if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存函件元模版定义，再新增元模版参数定义。");
			return;
		}
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_RES_TMPL_MG_COM"]["TZ_RESTPL_PARA_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RESTPL_PARA_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('resTempletParaInfoWindow');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		//操作类型设置为新增
		win.actType = "add";
		//元模版定义表单
		var resTmplFormParams = this.getView().child("form").getForm().getValues();
		//元模版id
		var restempid = resTmplFormParams["restempid"];
		var restemporg = resTmplFormParams["restemporg"];

        //参数定义表单
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({restempid:restempid});
		form.setValues({restemporg:restemporg});
		form.findField("paraid").setReadOnly(false);
		
        win.show();
	},
	deleteParaRow: function(grid,rowIndex){
		Ext.MessageBox.confirm('确认', '确定删除该记录?', function(btn){
            if(btn!='yes') {
                return;
            }						            			
            var parastore=grid.getStore();
			var sm = grid.getSelectionModel();
			sm.select(rowIndex);
            parastore.remove(sm.getSelection());
        },this);
	},
	deleteSelParaRow: function(btn){
	   //选中行
	   var paraGrid = btn.findParentByType("grid");
	   var selList = paraGrid.getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btn){
				if(btn == 'yes'){					   
				   var store = paraGrid.store;
				   store.remove(selList);
				}												  
			},this);   
	   }
	},
	editSelParaRow: function(btn) {
		//选中行
	    var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
	    //选中行长度
	    var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	    }
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_RES_TMPL_MG_COM"]["TZ_RESTPL_PARA_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RESTPL_PARA_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('resTempletParaInfoWindow');
        
        if (!win) {
			
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		
		//元模版定义表单
		var resTmplFormParams = this.getView().child("form").getForm().getValues();
		//元模版id
		var restempid = selList[0].get("restempid");
		//机构id
		var restemporg = selList[0].get("restemporg");
		//参数id
		var paraid = selList[0].get("paraid");
		var tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RESTPL_PARA_STD","OperateType":"QF","comParams":{"restempid":"'+restempid+'","restemporg":"'+restemporg+'","paraid":"'+paraid+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();
		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData);
			form.findField("paraid").setReadOnly(true);
			form.findField("paraid").addCls("lanage_1");
		});
        win.show(); 
			
    },
	editParaRow: function(view,rowIndex) {

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_RES_TMPL_MG_COM"]["TZ_RESTPL_PARA_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RESTPL_PARA_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('resTempletParaInfoWindow');
        
        if (!win) {
			
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		
		var restempid = selRec.get("restempid");
		var restemporg = selRec.get("restemporg");
		var paraid = selRec.get("paraid");
		//参数
		var tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RESTPL_PARA_STD","OperateType":"QF","comParams":{"restempid":"'+restempid+'","restemporg":"'+restemporg+'","paraid":"'+paraid+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();
		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData);
			form.findField("paraid").setReadOnly(true);
			form.findField("paraid").addCls("lanage_1");
		});
        win.show(); 
    },
	addContentRow: function(btn){
		if(this.getView().actType == "add"){
			Ext.MessageBox.alert("提示","请先保存函件元模版定义，再新增元模版内容参数定义。");
			return;
		}
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_RES_TMPL_MG_COM"]["TZ_RESTPL_CONT_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RESTPL_CONT_STD，请检查配置。');
			return;
		}
		var win = this.lookupReference('resTempletContentInfoWindow');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		//操作类型设置为新增
		win.actType = "add";
		//元模版定义表单
		var resTmplFormParams = this.getView().child("form").getForm().getValues();
		//元模版id
		var restempid = resTmplFormParams["restempid"];
		var restemporg = resTmplFormParams["restemporg"];

        //参数定义表单
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({restempid:restempid});
		form.setValues({restemporg:restemporg});
		form.findField("keyname").setReadOnly(false);
		form.findField("paraid").setReadOnly(false);
        win.show();
	},

	editSelContentRow: function(btn) {
		//选中行
	    var selList = btn.findParentByType("grid").getSelectionModel().getSelection();
	    //选中行长度
	    var checkLen = selList.length;
	    if(checkLen == 0){
			Ext.Msg.alert("提示","请选择一条要修改的记录");   
			return;
	    }else if(checkLen >1){
		   Ext.Msg.alert("提示","只能选择一条要修改的记录");   
		   return;
	    }
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_RES_TMPL_MG_COM"]["TZ_RESTPL_CONT_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RESTPL_CONT_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('resTempletContentInfoWindow');
        
        if (!win) {
			
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		
		//元模版定义表单
		var resTmplFormParams = this.getView().child("form").getForm().getValues();
		//元模版id
		var restempid = selList[0].get("restempid");
		//机构id
		var restemporg = selList[0].get("restemporg");
		//参数id
		var keyname = selList[0].get("keyname");
		
		var tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RESTPL_CONT_STD","OperateType":"QF","comParams":{"restempid":"'+restempid+'","restemporg":"'+restemporg+'","keyname":"'+keyname+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();
		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData);
			form.findField("keyname").setReadOnly(true);
			form.findField("keyname").addCls("lanage_1");
			form.findField("paraid").setReadOnly(false);
		});
        win.show(); 
			
    },
	editContentRow: function(view,rowIndex) {

		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_RES_TMPL_MG_COM"]["TZ_RESTPL_CONT_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_RESTPL_CONT_STD，请检查配置。');
			return;
		}
		
		var win = this.lookupReference('resTempletParaInfoWindow');
        
        if (!win) {
			
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		
		var store = view.findParentByType("grid").store;
		var selRec = store.getAt(rowIndex);
		
		var restempid = selRec.get("restempid");
		var restemporg = selRec.get("restemporg");
		var keyname = selRec.get("keyname");
		//参数
		var tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RESTPL_CONT_STD","OperateType":"QF","comParams":{"restempid":"'+restempid+'","restemporg":"'+restemporg+'","keyname":"'+keyname+'"}}';
		//页面注册信息表单
		var form = win.child("form").getForm();
		Ext.tzLoad(tzParams,function(responseData){
			form.setValues(responseData);
			form.findField("keyname").setReadOnly(true);
			form.findField("keyname").addCls("lanage_1");
			form.findField("paraid").setReadOnly(false);
		});
        win.show(); 
    },
	deleteContentRow: function(grid,rowIndex){
		Ext.MessageBox.confirm('确认', '确定删除该记录?', function(btn){
            if(btn!='yes') {
                return;
            }						            			    
            //rowEditing.cancelEdit();
			//var grid=this.lookupReference('tmpcontgrid')
            var contstore=grid.getStore();
			var sm = grid.getSelectionModel();
			sm.select(rowIndex);
            contstore.remove(sm.getSelection());
        },this);  
	},
	deleteSelContentRow: function(btn){
	   //选中行
	   var contentGrid = btn.findParentByType("grid");
	   var selList = contentGrid.getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert("提示","请选择要删除的记录");   
			return;
	   }else{
			Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btn){
				if(btn == 'yes'){					   
				   var store = contentGrid.store;
				   store.remove(selList);
				}												  
			},this);   
	   }
	},
	getResTmplInfoParams: function(){
		//组件注册表单
		var form = this.getView().child("form").getForm();
		//组件信息标志
		var actType = this.getView().actType;
		//更新操作参数
		var comParams = "";

		//新增
		if(actType == "add"){
			comParams = '"add":[{"typeFlag":"RESTMPLINFO","data":'+Ext.JSON.encode(form.getValues())+'}]';
		}
		//修改json字符串
	
		var editJson = "";
		if(actType == "update"){
			editJson = '{"typeFlag":"RESTMPLINFO","data":'+Ext.JSON.encode(form.getValues())+'}';
		}
		
		/*元模版参数列表*/
		//var attachmentGrid = Ext.getCmp('attachmentGrid');
		
		
		var restempid =  form.findField("restempid").getValue();
		
		var restemporg =  form.findField("restemporg").getValue();
		
		/*
		for(var j =0;j<resTmplParaGridstore.getCount();j++){
			//var mfResTmplParaGridRecs = resTmplParaGridstore.getModifiedRecords(); 
			var mfResTmplParaGridRecs = resTmplParaGridstore.getAt(j); 
			if(editJson == ""){
					editJson = '{"typeFlag":"RESTMPLPARA","restempid":"'+restempid+'","restemporg":"'+restemporg+'","data":'+Ext.JSON.encode(mfResTmplParaGridRecs.data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"RESTMPLPARA","restempid":"'+restempid+'","restemporg":"'+restemporg+'","data":'+Ext.JSON.encode(mfResTmplParaGridRecs.data)+'}';
			}
		}
		*/
		
		/*模板内容采集规则列表*/
		
		/*
		
		
		for(var j =0;j<resTmplContentGridstore.getCount();j++){
			//var mfResTmplContentGridRecs = resTmplContentGridstore.getModifiedRecords(); 
			var mfResTmplContentGridRecs = resTmplContentGridstore.getAt(j);
			
			if(editJson == ""){
				editJson = '{"typeFlag":"RESTMPLCONTENT","restempid":"'+restempid+'","restemporg":"'+restemporg+'","data":'+Ext.JSON.encode(mfResTmplContentGridRecs.data)+'}';
			}else{
				editJson = editJson + ',{"typeFlag":"RESTMPLCONTENT","restempid":"'+restempid+'","restemporg":"'+restemporg+'","data":'+Ext.JSON.encode(mfResTmplContentGridRecs.data)+'}';
			}
		}*/
		
		if(editJson != ""){
			if(comParams == ""){
				comParams = '"update":[' + editJson + "]";
			}else{
				comParams = comParams + ',"update":[' + editJson + "]";
			}
		}
		
		//删除参数json字符串
		var removeJson = "";
		//删除记录
		/*模板参数列表*/
		var resTmplParaGrid =this.getView().down('grid[name=resTmplRaraGrid]');
	
		var resTmplParaGridstore = resTmplParaGrid.getStore();
		var rmResTmplParaGridRecs = resTmplParaGridstore.getRemovedRecords();
		for(var i=0;i<rmResTmplParaGridRecs.length;i++){
			if(removeJson == ""){
				removeJson = '{"typeFlag":"RESTMPLPARA","restempid":"'+restempid+'","restemporg":"'+restemporg+'","data":'+Ext.JSON.encode(rmResTmplParaGridRecs[i].data)+'}';
			}else{
				removeJson = removeJson + ',{"typeFlag":"RESTMPLPARA","restempid":"'+restempid+'","restemporg":"'+restemporg+'","data":'+Ext.JSON.encode(rmResTmplParaGridRecs[i].data)+'}';
			}
		}
		
		var resTmplContentGrid =this.getView().down('grid[name=resTmplContentGrid]');
		var resTmplContentGridstore = resTmplContentGrid.getStore();
		var rmResTmplContentGridRecs = resTmplContentGridstore.getRemovedRecords();
		for(var i=0;i<rmResTmplContentGridRecs.length;i++){
			if(removeJson == ""){
				removeJson = '{"typeFlag":"RESTMPLCONTENT","restempid":"'+restempid+'","restemporg":"'+restemporg+'","data":'+Ext.JSON.encode(rmResTmplContentGridRecs[i].data)+'}';
			}else{
				removeJson = removeJson + ',{"typeFlag":"RESTMPLCONTENT","restempid":"'+restempid+'","restemporg":"'+restemporg+'","data":'+Ext.JSON.encode(rmResTmplContentGridRecs[i].data)+'}';
			}
		}
		//结束参数列表;
		if(removeJson != ""){
			if(comParams == ""){
				comParams = '"delete":[' + removeJson + "]";
			}else{
				comParams = comParams + ',"delete":[' + removeJson + "]";
			}
		}
		
		//comParams = comParams + ',"storeTest":[' + resTmplContentGridstore + "]";
		
		//提交参数
		var tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RES_TMPL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
	
	//邮件服务器搜索
	pmtSearchEmlServ: function(btn){
		var form = this.getView().child("form").getForm();
		var orgid = form.findField('restemporg').value;
		Ext.tzShowPromptSearch({
			recname: 'TZ_EMLS_DEF_VW',
			searchDesc: '搜索邮箱服务配置',
			maxRow:20,
			condition:{
				presetFields:{
					TZ_JG_ID_1:{
						value: orgid,
						type: '01'	
					}	
				},
				srhConFields:{
					TZ_JG_ID:{
						desc:'机构编号',
						operator:'01',
						type:'01'
					},
					TZ_EML_ADDR100:{
						desc:'电子邮箱',
						operator:'01',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TZ_EMLSERV_ID: '邮箱服务器编号',
				TZ_JG_ID:'所属机构',
				TZ_EML_ADDR100: '电子邮箱'	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("tempemailserv").setValue(selection[0].data.TZ_EMLSERV_ID);
				form.findField("emailaddr").setValue(selection[0].data.TZ_EML_ADDR100);
			}
		});	
	},
	//短信服务器搜索
	pmtSearchSmsServ: function(btn){
		var form = this.getView().child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_SMSSERV_TBL',
			searchDesc: '搜索短信网关配置',
			maxRow:20,
			condition:{
				/*
				presetFields:{
					TZ_EMLSERV_ID:{
						//value: Ext.tzOrgID,
						value:"",
						type: '01'	
					}	
				},
				*/
				srhConFields:{
					TZ_SMS_SERV_NAME:{
						desc:'短信服务器名称',
						operator:'01',
						type:'01'
					}	
				}	
			},
			srhresult:{
				TZ_SMS_SERV_ID: '短信服务器编号',
				TZ_SMS_SERV_NAME:'短信服务器名称'
			},
			multiselect: false,
			callback: function(selection){
				form.findField("tempsmsserv").setValue(selection[0].data.TZ_SMS_SERV_ID);
				form.findField("smssevname").setValue(selection[0].data.TZ_SMS_SERV_NAME);
			}
		});	
	},
	
	//模版参数搜索
	pmtSearchResPara: function(btn){
		//var form = this.getView().child("form").getForm();
		var form = btn.findParentByType('form').getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_EX_PARA_TBL',
			searchDesc: '搜索模版参数',
			maxRow:20,
			condition:{

				srhConFields:{
					TZ_PARA_ID:{
						desc:'参数编号',
						operator:'01',
						type:'01'
					},	
					TZ_PARA_CNAME:{
						desc:'参数名称',
						operator:'07',
						type:'01'
					}
				}	
			},
			srhresult:{
				TZ_PARA_ID: '参数编号',
				TZ_PARA_CNAME:'参数名称'
			},
			multiselect: false,
			callback: function(selection){
				//console.log(form);
				//console.log(selection[0].data.TZ_PARA_ID);
				form.findField("paraid").setValue(selection[0].data.TZ_PARA_ID);
				form.findField("paraname").setValue(selection[0].data.TZ_PARA_CNAME);
				/*
				var store = grid.store;
				var selRec = store.getAt(rowIndex);
				selRec.set("paraid",selection[0].data.TZ_PARA_ID);
				selRec.set("paraname",selection[0].data.TZ_PARA_CNAME);
				*/
			}
		});	
	},
	//搜索系统变量
	pmtSearchResSysVar: function(btn){
		var form = btn.findParentByType('form').getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_SYSVAR_T',
			searchDesc: '搜索模版参数',
			maxRow:20,
			condition:{
				presetFields:{
					TZ_EFFFLG:{
						//value: Ext.tzOrgID,
						value:"Y",
						type: '01'	
					}	
				},
				srhConFields:{
					TZ_SYSVARID:{
						desc:'系统变量编号',
						operator:'01',
						type:'01'
					},	
					TZ_SYSVARNAME:{
						desc:'系统变量名称',
						operator:'07',
						type:'01'
					}
				}	
			},
			srhresult:{
				TZ_SYSVARID: '参数编号',
				TZ_SYSVARNAME:'参数名称'
			},
			multiselect: false,
			callback: function(selection){
				form.findField("systvar").setValue(selection[0].data.TZ_SYSVARID);
				form.findField("systvarname").setValue(selection[0].data.TZ_SYSVARNAME);
				
			}
		});	
	},
	//搜索记录关键字
	pmtSearchRecKey: function(btn){
		var form = this.getView().child("form").getForm();
		var recName = form.findField("recname").value;
		if(!recName) recName = 'NAN';
		Ext.tzShowPromptSearch({
			recname: 'TZ_RECFIELD_VW',
			searchDesc: '搜索记录关键字',
			maxRow:20,
			condition:{
				presetFields:{
					TZ_DSREC_NAME:{
						//value: Ext.tzOrgID,
						value:recName,
						type: '01'	
					}	
				},
				srhConFields:{
					FIELDNAME:{
						desc:'字段名',
						operator:'01',
						type:'01'
					},	
					LONGNAME:{
						desc:'字段描述',
						operator:'01',
						type:'01'
					}
				}	
			},
			srhresult:{
				FIELDNAME: '字段名',
				LONGNAME:'字段描述'
			},
			multiselect: false,
			callback: function(selection){
				var winForm = btn.findParentByType('form').getForm();
				winForm.findField("keyname").setValue(selection[0].data.FIELDNAME);
				
			}
		});	
	},
		//搜索关键字对应的参数
	pmtSearchCpara: function(btn){
		var form = this.getView().child("form").getForm();
		var restempid = form.findField("restempid").value;
		var restemporg = form.findField("restemporg").value;
		if(!restempid) restempid = 'NAN';
		if(!restemporg) restemporg = 'NAN';
		Ext.tzShowPromptSearch({
			recname: 'TZ_TMP_PARA_VW',
			searchDesc: '搜索模版对应取值参数',
			maxRow:20,
			condition:{
				presetFields:{
					TZ_JG_ID:{
						//value: Ext.tzOrgID,
						value:restemporg,
						type: '01'	
					},
					TZ_YMB_ID:{
						value:restempid,
						type: '01'	
					}					
				},
				srhConFields:{
					TZ_PARA_ID:{
						desc:'参数名称',
						operator:'01',
						type:'01'
					},	
					TZ_PARA_CNAME:{
						desc:'参数描述',
						operator:'01',
						type:'01'
					}
				}	
			},
			srhresult:{
				TZ_PARA_ID: '参数编号',
				TZ_PARA_CNAME:'参数名称'
			},
			multiselect: false,
			callback: function(selection){
				var winForm = btn.findParentByType('form').getForm();
				winForm.findField("paraid").setValue(selection[0].data.TZ_PARA_ID);
				winForm.findField("paraname").setValue(selection[0].data.TZ_PARA_CNAME);
				
			}
		});	
	},
	pmtSearchResAppClass: function(grid,rowIndex){
		var form = this.getView().child("form").getForm();
		var restempid = form.findField("restempid").value;
		var restemporg = form.findField("restemporg").value;
		if(!restempid) restempid = 'NAN';
		if(!restemporg) restemporg = 'NAN';
		Ext.tzShowPromptSearch({
			recname: 'TZ_TMP_PARA_VW',
			searchDesc: '搜索模版对应取值参数',
			maxRow:20,
			condition:{
				presetFields:{
					TZ_JG_ID:{
						//value: Ext.tzOrgID,
						value:restemporg,
						type: '01'	
					},
					TZ_YMB_ID:{
						value:restempid,
						type: '01'	
					}					
				},
				srhConFields:{
					TZ_PARA_ID:{
						desc:'参数名称',
						operator:'01',
						type:'01'
					},	
					TZ_PARA_CNAME:{
						desc:'参数描述',
						operator:'01',
						type:'01'
					}
				}	
			},
			srhresult:{
				TZ_PARA_ID: '参数编号',
				TZ_PARA_CNAME:'参数名称'
			},
			multiselect: false,
			callback: function(selection){
				form.findField("contclassid").setValue(selection[0].data.TZ_PARA_ID);
				form.findField("contclassName").setValue(selection[0].data.TZ_PARA_CNAME);
			}
		});	
	},
	//应用程序类搜索
	pmtSearchResAppClass: function(btn){
		var form = this.getView().child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_APPCLS_TBL',
			searchDesc: '搜索应用程序类',
			maxRow:20,
			condition:{
				/*
				presetFields:{
					TZ_EMLSERV_ID:{
						//value: Ext.tzOrgID,
						value:"",
						type: '01'	
					}	
				},
				*/
				srhConFields:{
					TZ_APPCLS_ID:{
						desc:'应用程序类ID',
						operator:'01',
						type:'01'
					},
					TZ_APPCLS_NAME:{
						desc:'应用程序类名称',
						operator:'01',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TZ_APPCLS_ID: '应用程序类ID',
				TZ_APPCLS_NAME:'应用程序类名称'
			},
			multiselect: false,
			callback: function(selection){
				form.findField("contclassid").setValue(selection[0].data.TZ_APPCLS_ID);
				form.findField("contclassName").setValue(selection[0].data.TZ_APPCLS_NAME);
			}
		});	
	},
	onResTmplParaInfoSave: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//元模版参数信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面注册信息*/
			this.saveResTmplParaInfo(win,"save");
		}
	},
	onResTmplParaInfoEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//元模版参数信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存元模版参数信息表单*/
			this.saveResTmplParaInfo(win,"ensure");
		}	
	},
	saveResTmplParaInfo: function(win,op){
		//页面注册信息表单
		var form = win.child("form").getForm();
		//表单数据
		var formParams = form.getValues();
		
		//提交参数
		var tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RESTPL_PARA_STD","OperateType":"U","comParams":{"'+win.actType+'":['+Ext.JSON.encode(formParams)+']}}';
		var tzStoreParams = '{"restempid":"'+formParams["restempid"]+'","restemporg":"'+formParams["restemporg"]+'","listtype":"'+'PARA'+'"}';
		var paraGrid = this.getView().down('grid[name=resTmplRaraGrid]');
		Ext.tzSubmit(tzParams,function(){
			win.actType = "update";
			form.findField("paraid").setReadOnly(true);
			paraGrid.store.tzStoreParams = tzStoreParams;
			paraGrid.store.reload();
			if(op == "ensure")
			{
				//重置表单
				form.reset();
				//关闭窗口
				win.close();
			}
	    },"",true,this);
	},
	onResTmplParaClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	},
	onResTmplContentInfoSave: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//元模版参数信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存页面注册信息*/
			this.saveResTmplContentInfo(win,"save");
		}
	},
	onResTmplContentInfoEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//元模版参数信息表单
		var form = win.child("form").getForm();
		if (form.isValid()) {
			/*保存元模版参数信息表单*/
			this.saveResTmplContentInfo(win,"ensure");
		}	
	},
	saveResTmplContentInfo: function(win,op){
		//页面注册信息表单
		var form = win.child("form").getForm();
		//表单数据
		var formParams = form.getValues();
		
		//提交参数
		var tzParams = '{"ComID":"TZ_RES_TMPL_MG_COM","PageID":"TZ_RESTPL_CONT_STD","OperateType":"U","comParams":{"'+win.actType+'":['+Ext.JSON.encode(formParams)+']}}';
		var tzStoreParams = '{"restempid":"'+formParams["restempid"]+'","restemporg":"'+formParams["restemporg"]+'","listtype":"'+'CONTENT'+'"}';
		var contentParaGrid = this.getView().down('grid[name=resTmplContentGrid]');
		Ext.tzSubmit(tzParams,function(){
			win.actType = "update";
			form.findField("paraid").setReadOnly(true);
			contentParaGrid.store.tzStoreParams = tzStoreParams;
			contentParaGrid.store.reload();
			if(op == "ensure")
			{
				//重置表单
				form.reset();
				//关闭窗口
				win.close();
			}
	    },"",true,this);
	},
	onResTmplContentClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//页面注册信息表单
		var form = win.child("form").getForm();
		win.close();
	}
}); 