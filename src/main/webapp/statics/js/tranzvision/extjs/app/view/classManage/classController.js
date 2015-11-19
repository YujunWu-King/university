Ext.define('KitchenSink.view.classManage.classController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.classManage', 
	requires: [
       'KitchenSink.view.classManage.ClassInfo'
    ],
	//可配置搜索
	query_list: function(btn){     //searchComList为各自搜索按钮的handler event;
        Ext.tzShowCFGSearch({           
           cfgSrhId: 'TZ_GD_BJGL_COM.TZ_GD_BJCX_STD.TZ_GD_BJGL_VW',
           condition:
            {
                "TZ_JG_ID": Ext.tzOrgID           //设置搜索字段的默认值，没有可以不设置condition;
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });    
    },
    //添加班级；
    addSite: function(btn) {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxbcbjjbxx","请先保存班级基本信息后"));
			return;
		}
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJGL_COM"]["TZ_GD_BJJB_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        cmp = new ViewClass();
        //操作类型设置为更新
		cmp.actType = "add";
		cmp.class_id = "NEXT";
        cmp.on('afterrender',function(){
			var form = cmp.child("form").getForm();
			form.reset();
		});
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
	//添加班级---精简版；
    addClassE: function(btn) {
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJGL_COM"]["TZ_BJ_ADD_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var _bj_id=this.getView().class_id;
		var _lc_id=this.getView().bmlc_id;
		var win = this.lookupReference('addClass');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
			var form = win.child('form').getForm();
			//form.findField("bj_id").setValue(_bj_id);
			//form.findField("bmlc_id").setValue(_lc_id);
            this.getView().add(win);
        }
        win.show();
    },
	//选择班级所属项目
	xm_idChoice1: function(btn){
		var form = this.getView().child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_PRJ_INF_T',
			searchDesc:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ssssxm","搜索所属项目") ,
			maxRow:20,
			condition:{
				presetFields:{
					TZ_JG_ID:{
						value: Ext.tzOrgID,
						type: '01'	
					},
					TZ_IS_OPEN:{
						value: 'Y',
						type: '01'	
					}
				},
				srhConFields:{
					TZ_PRJ_ID:{
						desc:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xmflbh","项目分类编号"),
						operator:'01',
						type:'01'	
					},
					TZ_PRJ_NAME:{
						desc:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xmflmc","分类名称"),
						operator:'01',
						type:'01'		
					}	
				}	
			},
			srhresult:{
				TZ_PRJ_ID: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xmflbh","项目分类编号"),
				TZ_PRJ_NAME: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xmflmc","分类名称")
			},
			multiselect: false,
			callback: function(selection){
				form.findField("xm_id").setValue(selection[0].data.TZ_PRJ_ID);
				form.findField("xm_id_desc").setValue(selection[0].data.TZ_PRJ_NAME);
			}
		});	
	},
	//关闭新增班级页面
	CloseClassEasy: function(){
		this.getView().close();
	},
	//保存并关闭新增班级页面
	AddClassEasy: function(btn){
		var me=this;
		var form = this.getView().child("form").getForm();
		if (form.isValid())
		{
			var actType = this.getView().actType;
			var comParams = "";
			if(actType == "add"){
				comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
			}
			var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_ADD_STD","OperateType":"U","comParams":{'+comParams+'}}';
			var comView = this.getView();
			Ext.tzSubmit(tzParams,function(responseData){
				comView.actType = "update";
				if(actType == "add" && btn != "but_ensure"){
				}
				var win = btn.findParentByType("classManage");
				win.store.reload();
				me.getView().close();
			},"",true,this);
		}
	},
	//编辑班级（title）
	editClassInfoT:function(){
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzytyxgdjl","请选择一条要修改的记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.znxzytyxgdjl","只能选择一条要修改的记录"));
            return;
        }
        //班级ID
        var bj_id= selList[0].get("bj_id");
        this.editSiteIntoByID(bj_id);
    },
	//编辑班级（列表）
    editClassInfo: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
   	 	var bj_id = selRec.get("bj_id");
     	this.editSiteIntoByID(bj_id);
    },
    editSiteIntoByID:function(bj_id){
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJGL_COM"]["TZ_GD_BJJB_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        //cmp = new ViewClass({"class_id": "2"});
		cmp = new ViewClass(bj_id);
        //操作类型设置为更新
		cmp.actType = "update";
		//cmp.class_id = bj_id;
		cmp.on('afterrender',function(panel){
			//组件注册表单信息;
			var form = panel.child('form').getForm();
			//页面注册信息列表
			var tabpanel = panel.child("form").child("tabpanel");
			var grid = tabpanel.getActiveTab();
			//加载报名流程模板
			var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_APPPRO_TMP_T',
				condition:{
					TZ_APPPRO_STATUS:{
						value:'Y',
						operator:"01",
						type:"01"
					},
					TZ_JG_ID:{
						value:Ext.tzOrgID,
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_APPPRO_TMP_ID,TZ_APPPRO_TMP_NAME'
			});
			var _d=panel.down('combobox[name=bmlc_mb]');
			_d.setStore(lm_mbStore);
			//加载报名流程模板
			var lm_mbStore1 = new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_SBMINF_TMP_T',
				condition:{
					TZ_SBMINF_STATUS:{
						value:'Y',
						operator:"01",
						type:"01"
					},
					TZ_JG_ID:{
						value:Ext.tzOrgID,
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_SBMINF_TMP_ID,TZ_SBMINF_TMP_NAME'
			});
			var _dd=panel.down('combobox[name=djzl_mx]');
			_dd.setStore(lm_mbStore1);
			//tabpanel.findField("lm_mb").setStore(lm_mbStore);
			//参数
			var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_GD_BJJB_STD","OperateType":"QF","comParams":{"bj_id":"'+bj_id+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var formData = responseData.formData;
				form.setValues(formData);

				//加载班级信息中的grid数据
				//专业方向页面重新加载
				var zy_grid = panel.down('grid[name=zyfx_save]');
				Params= '{"bj_id":"'+bj_id+'","queryID":"2"}';
				zy_grid.store.tzStoreParams = Params;
				zy_grid.store.reload();
				//批次管理页面重新加载
				var pc_grid = panel.down('grid[name=pcgl_save]');
				Params= '{"bj_id":"'+bj_id+'","queryID":"3"}';
				pc_grid.store.tzStoreParams = Params;
				pc_grid.store.reload();
				//管理人员页面重新加载
				var gl_grid = panel.down('grid[name=glry_save]');
				Params= '{"bj_id":"'+bj_id+'","queryID":"4"}';
				gl_grid.store.tzStoreParams = Params;
				gl_grid.store.reload();
				//报名流程页面重新加载
				var _bm="";
				var bm_grid = panel.down('grid[name=applyItemGrid]');
				Params= '{"bj_id":"'+bj_id+'","queryID":"5","lc_id":"'+_bm+'"}';
				bm_grid.store.tzStoreParams = Params;
				bm_grid.store.reload();
				//递交资料页面重新加载
				var _zl="";
				var zl_grid = panel.down('grid[name=applyItemGrid1]');
				Params= '{"bj_id":"'+bj_id+'","queryID":"6","zl_id":"'+_zl+'"}';
				zl_grid.store.tzStoreParams = Params;
				zl_grid.store.reload();
				//互斥规则页面数据加载
				var hcgz_grid = panel.down('grid[name=hcgzGrid]');
				Params= '{"bj_id":"'+bj_id+'","queryID":"7"}';
				hcgz_grid.store.tzStoreParams = Params;
				hcgz_grid.store.reload();
			});
		});
        tab = contentPanel.add(cmp);     
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
	onComRegClose: function(btn){
		//关闭
		var grid = btn.findParentByType("grid");
		grid.close();
	}
});