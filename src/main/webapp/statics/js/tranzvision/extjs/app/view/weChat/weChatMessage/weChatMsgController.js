Ext.define('KitchenSink.view.weChat.weChatMessage.weChatMsgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.weChatMsgController',
    
  //从素材库中选择图片
  ChoosePic:function(btn){
        var tabpanel=btn.findParentByType("tabpanel");
        var msgForm=tabpanel.up('form[name=msgForm]').getForm();
        var wxAppdId=msgForm.findField("appId").getValue();
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WXMSG_COM"]["TZ_GD_SCGL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_SCGL_STD，请检查配置。');
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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        var win=this.lookupReference('weChatMsgScWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass(tabpanel);
            win.materialType='TuPian';
            win.wxAppId=wxAppdId;
            this.getView().add(win);
        }
       win.on('afterrender',function(panel){
           var picDataView = panel.down('dataview[name=picView]');
           var tzStoreParams = '{"wxAppId":"'+wxAppdId+'","mediaType":"TP"}';
           picDataView.store.tzStoreParams = tzStoreParams;
           picDataView.store.load();
       });
        win.show(); 
        //from2.findField("tpMediaId").setValue("222");
       // tabpanel.down('image[name=titileImage]').setHidden(false);
        //tabpanel.down('button[name=deletePicBtn]').setHidden(false);
       // tabpanel.down('image[name=titileImage]').setSrc(TzUniversityContextPath + "/statics/js/tranzvision/extjs/app/view/template/bmb/images/forms.png");

    },
    //从素材库中选择图文
    ChooseTw:function(btn){
        var tabpanel=btn.findParentByType("tabpanel");
        var msgForm=tabpanel.up('form[name=msgForm]').getForm();
        var wxAppdId=msgForm.findField("appId").getValue();
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WXMSG_COM"]["TZ_GD_SCGL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_SCGL_STD，请检查配置。');
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
            // <debug warn>
            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        var win=this.lookupReference('weChatMsgScWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass(tabpanel);
            win.materialType='TuWen';
            win.wxAppId=wxAppdId;
            this.getView().add(win);
        }
        win.on('afterrender',function(panel){
            var picDataView = panel.down('dataview[name=picView]');
            var tzStoreParams = '{"wxAppId":"'+wxAppdId+'","mediaType":"TW"}';
            picDataView.store.tzStoreParams = tzStoreParams;
            picDataView.store.load();
        });
        win.show();
    },
    //确定
    chooseScEnsure:function(btn){
        var win=btn.findParentByType("window");
        var materialType=win.materialType;
        var tabpanel=win.tabpanel;
        var picDataView = win.down('dataview[name=picView]');
        //获的选择的图片
        var selList =  picDataView.getSelectionModel().getSelection();
        if(selList.length==0){
        	Ext.Msg.alert("提示","请先选择一个素材");
        }else{
	        var medidId=selList[0].data.mediaId;
	        var src=selList[0].data.src;
	        var caption=selList[0].data.caption;
	        //选择图片
	        if(materialType=="TuPian"){
	        	var from2=tabpanel.down('form[name=form2]').getForm();
	        	from2.findField("tpMediaId").setValue(medidId);
	        	tabpanel.down('image[name=titileImage]').setHidden(false);
	        	tabpanel.down('button[name=deletePicBtn]').setHidden(false);
	        	tabpanel.down('button[name=chooseScBtn]').setHidden(true);
	        	tabpanel.down('image[name=titileImage]').setSrc(TzUniversityContextPath + src);
	        	win.close();
	        }else{
		        //选择图文
		        if(materialType=="TuWen"){
		            var from3=tabpanel.down('form[name=form3]').getForm();
		            from3.findField("twTitle").setHidden(false);
		            from3.findField("twMediaId").setValue(medidId);
		            from3.findField("twTitle").setValue(caption);
		            tabpanel.down('button[name=deleteTwBtn]').setHidden(false);
		            tabpanel.down('button[name=chooseTwBtn]').setHidden(true);
		            tabpanel.down('image[name=twImage]').setHidden(false);
		            tabpanel.down('image[name=twImage]').setSrc(TzUniversityContextPath +src);
		            win.close();
		        }else{
		        	win.close();
		        }
	        }
        }
    },
    //发送微信消息
    sendWxMsg:function(btn){
    	var form=btn.findParentByType("panel").down("form").getForm();
    	if(!form.isValid()){
            return false;
        }
    	var sendMode=form.findField("sendMode").getValue();
    	var openIds=form.findField("openIds").getValue();
    	var wechatTag=form.findField("wechatTag").getValue();
    	if(sendMode=="A"){
    		if(openIds==""){
    			Ext.Msg.alert("提示","用户列表不能为空!");
        		return false;
    		}
    	}else{
			if(wechatTag==""){
				Ext.Msg.alert("提示","请先选择标签!");
        		return false;			
			 }
    	}
        var formParams = form.getValues();
        var tabPanel=form.findField("weChatTabPanel");
        var tabPanel=btn.findParentByType("panel").down("form").items.items[4];
        var activeForm=tabPanel.getActiveTab().config.name;
        var sendType="";
        if(activeForm=="form1"){
        	//文字消息
        	sendType="C";
        	var wordMessage=form.findField("wordMessage").getValue();
        	if(wordMessage==""){
        		Ext.Msg.alert("提示","请先输入文字消息!");
        		return false;
        	}
        }
        if(activeForm=="form2"){
        	//图片消息
        	sendType="A";
        	var tpMediaId=form.findField("tpMediaId").getValue();
        	if(tpMediaId==""){
        		Ext.Msg.alert("提示","请先选择图片素材!");
        		return false;
        	}
        }
        if(activeForm=="form3"){
        	//图文消息
        	sendType="B";
        	var twMediaId=form.findField("twMediaId").getValue();
        	if(twMediaId==""){
        		Ext.Msg.alert("提示","请先选择图文素材!");
        		return false;
        	}
        }
        var tzParams = '{"ComID":"TZ_GD_WXMSG_COM","PageID":"TZ_GD_WXMSG_STD","OperateType":"U","comParams":{"add":[{"sendType":"'+sendType+'","data":'+Ext.JSON.encode(formParams)+'}]}}';
        //console.log(tzParams);
        var msg="";
        Ext.tzSubmit(tzParams,function(response){
        if(response.errcode==0){
           form.findField("sendStatus").setValue("Y");
           Ext.Msg.alert("提示","发送成功");
        }else{
           form.findField("sendStatus").setValue("N");
           Ext.Msg.alert("提示","发送失败");
         }
        	
        },false,true,this);
    },
    //查看发送历史
    viewSendHis:function(btn){
    	var form=btn.findParentByType("panel").down("form").getForm();
    	var wxAppId=form.findField("appId").getValue();
    	var orgId=Ext.tzOrgID;
    	//console.log(wxAppId,orgId);
    	Ext.tzSetCompResourses("TZ_GD_WXSERVICE_COM");
    	//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WXSERVICE_COM"]["TZ_GD_LOGLIST_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_LOGLIST_STD，请检查配置。');
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
	        cmp.actType = "update";

	        cmp.on('afterrender',function(panel){
	            //许可权表单信息;
	            var form = panel.child('form').getForm();
	            form.findField("jgId").setReadOnly(true);
	            form.findField("jgId").setFieldStyle('background:#F4F4F4');
	            form.findField("appId").setReadOnly(true);
	            form.findField("appId").setFieldStyle('background:#F4F4F4');
	            //授权组件列表
	            var grid = panel.child('grid');
	            //参数
	            var tzParams = '{"ComID":"TZ_GD_WXSERVICE_COM","PageID":"TZ_GD_LOGLIST_STD","OperateType":"QF","comParams":{"orgId":"'+orgId+'","wxAppId":"'+wxAppId+'"}}';
	            //加载数据
	            Ext.tzLoad(tzParams,function(responseData){
	            	 //资源集合信息数据
	                var formData = responseData.formData;
	                form.setValues(formData);
	                //资源集合信息列表数据
	                var roleList = responseData.listData;

	                var tzStoreParams = '{"cfgSrhId": "TZ_GD_WXSERVICE_COM.TZ_GD_LOGLIST_STD.TZ_WXMSG_LOG_T","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+orgId+'","TZ_WX_APPID-operator": "01","TZ_WX_APPID-value": "'+wxAppId+'"}}';
	                grid.store.tzStoreParams = tzStoreParams;
	                grid.store.load();     
	            });   

	        });

	        tab = contentPanel.add(cmp);

	        contentPanel.setActiveTab(tab);

	        Ext.resumeLayouts(true);

	        if (cmp.floating) {
	            cmp.show();
	        }
    },
    //关闭微信窗口
    closeWxPanel:function(){
     this.getView().close();
    }
});
