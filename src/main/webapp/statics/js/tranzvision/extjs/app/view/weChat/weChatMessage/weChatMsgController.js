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
        var formParams = form.getValues();
        var tabPanel=form.findField("weChatTabPanel");
        var tabPanel=btn.findParentByType("panel").down("form").items.items[4];
        var activeForm=tabPanel.getActiveTab().config.name;
        var sendType="";
        if(activeForm=="form1"){
        	//文字消息
        	sendType="C";
        }
        if(activeForm=="form2"){
        	//图片消息
        	sendType="A";
        }
        if(activeForm=="form3"){
        	//图文消息
        	sendType="B";
        }
        var tzParams = '{"ComID":"TZ_GD_WXMSG_COM","PageID":"TZ_GD_WXMSG_STD","OperateType":"U","comParams":{"add":[{"sendType":"'+sendType+'","data":'+Ext.JSON.encode(formParams)+'}]}}';
        //console.log(tzParams);
        var msg="";
        Ext.tzSubmit(tzParams,function(response){
        if(response.errcode==0){
           form.findField("sendStatus").setValue("Y");
           msg="发送成功";
        }else{
           form.findField("sendStatus").setValue("N");
           msg="发送失败";
         }
        	
        },msg,true,this);
    },
    viewSendHis:function(btn){
    	alert("查看发送历史");
    },
    //关闭微信窗口
    closeWxPanel:function(){
     this.getView().close();
    }
});
