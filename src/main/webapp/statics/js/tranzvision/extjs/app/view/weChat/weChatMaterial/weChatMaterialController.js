Ext.define('KitchenSink.view.weChat.weChatMaterial.weChatMaterialController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.weChatMaterialController',
    
    //查询素材
    queryMaterial:function(btn){
    	var panel=btn.findParentByType("weChatMaterialInfoPanel");
    	var jgId=panel.jgId;
    	var wxAppId=panel.wxAppId;
    	Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_WX_SCGL_COM.TZ_WX_SCGL_STD.TZ_WX_MEDIA_VW', //这里面的组件页面视图需要换成自己的
            condition:
            {
                "TZ_JG_ID":jgId,
                "TZ_WX_APPID":wxAppId
            },
            callback: function(seachCfg){
        	    var picDataView=btn.findParentByType("panel").down("dataview[name=picView]");
                var store = picDataView.store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    //素材窗口保存
    materialWindowSave:function(btn){
    	var panel=btn.findParentByType("weChatMaterialInfoPanel");
	    var picDataView=btn.findParentByType("panel").down("dataview[name=picView]");
        var wxAppId=panel.wxAppId;
        var jgId=panel.jgId;
        var store=picDataView.store;
        var removeJson = "";
        var removeRecs = store.getRemovedRecords();
        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }
        comParams = '"delete":[' + removeJson + "]";
        /*if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }*/
        
        var tzParams = '{"ComID":"TZ_WX_SCGL_COM","PageID":"TZ_WX_SCGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(){
        	 var tzStoreParams = '{"cfgSrhId": "TZ_WX_SCGL_COM.TZ_WX_SCGL_STD.TZ_WX_MEDIA_VW",' +
				'"condition":{"TZ_JG_ID-operator":"01","TZ_JG_ID-value":"' + jgId + '","TZ_WX_APPID-operator":"01","TZ_WX_APPID-value":"' + wxAppId + '"}}';
             store.tzStoreParams = tzStoreParams;
             store.load();
        },"",true,this);
    },
    //素材窗口确定
    materialWindowEnsure:function(btn){
    	var panel=btn.findParentByType("weChatMaterialInfoPanel");
	    var picDataView=btn.findParentByType("panel").down("dataview[name=picView]");
       
        var store=picDataView.store;
        var removeJson = "";
        var removeRecs = store.getRemovedRecords();
        for(var i=0;i<removeRecs.length;i++){
            if(removeJson == ""){
                removeJson = Ext.JSON.encode(removeRecs[i].data);
            }else{
                removeJson = removeJson + ','+Ext.JSON.encode(removeRecs[i].data);
            }
        }
        comParams = '"delete":[' + removeJson + "]";
        var tzParams = '{"ComID":"TZ_WX_SCGL_COM","PageID":"TZ_WX_SCGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(){
        	panel.close();
        },"",true,this);
    },
    //关闭素材窗口
    materialWindowClose:function(){
     this.getView().close();
    },

   //新增图片素材
	addPic:function(btn){
        var panel=btn.findParentByType("weChatMaterialInfoPanel");
        var wxAppId=panel.wxAppId;
        var jgId=panel.jgId;
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_WX_SCGL_COM"]["TZ_WX_TPSC_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_WX_TPSC_STD，请检查配置。');
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
        cmp=new ViewClass();
        cmp.actType = "add";
        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();
            var publishBtn=panel.down("button[name=publishBtn]");
            var revokeBtn=panel.down("button[name=revokeBtn]");
            publishBtn.setDisabled(false);
            revokeBtn.setDisabled(true);
            form.findField("wxAppId").setValue(wxAppId);
            form.findField("jgId").setValue(jgId);
        }); 
        var tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
	//新增图文素材
	addPicAndWord:function(btn){
		var panel=btn.findParentByType("weChatMaterialInfoPanel");
        var wxAppId=panel.wxAppId;
        var jgId=panel.jgId;
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_WX_SCGL_COM"]["TZ_WX_TWSC_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_WX_TWSC_STD，请检查配置。');
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


        cmp = new ViewClass({
        	wxAppId: wxAppId,
        	jgId: jgId,
        	actType : "add"
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
	},
	
	//修改素材
	editMaterial:function(btn){
        var panel=btn.findParentByType("weChatMaterialInfoPanel");
        var picDataView=btn.findParentByType("panel").down("dataview[name=picView]");
        var selList =  picDataView.getSelectionModel().getSelection();
        if(selList.length==0){
            Ext.Msg.alert("提示","请先选择一个素材");
        }else{
            var jgId=panel.jgId;
            var wxAppId=panel.wxAppId;
            var tzSeq=selList[0].data.index;
            var mediaType=selList[0].data.mediaType;
            //图片素材
            if(mediaType=='A'){
                var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_WX_SCGL_COM"]["TZ_WX_TPSC_STD"];
                if( pageResSet == "" || pageResSet == undefined){
                    Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                    return;
                }
                //该功能对应的JS类
                var className = pageResSet["jsClassName"];
                if(className == "" || className == undefined){
                    Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_WX_TPSC_STD，请检查配置。');
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
               
                cmp=new ViewClass();
                cmp.actType = "update";
                cmp.on('afterrender',function(panel){  
                	var form = panel.child('form').getForm();
                    var tzParams = '{"ComID":"TZ_WX_SCGL_COM","PageID":"TZ_WX_TPSC_STD","OperateType":"QF","comParams":{"jgId":"'+jgId+'","wxAppId":"'+wxAppId+'","tzSeq":"'+tzSeq+'"}}';
                    //加载数据
                    Ext.tzLoad(tzParams,function(responseData){
                    	form.setValues(responseData.formData);
                    	var  path=responseData.formData.filePath;
                        var status=responseData.formData.status;
                        var publishBtn=panel.down("button[name=publishBtn]");
                        var revokeBtn=panel.down("button[name=revokeBtn]");
                        if(status=="Y"){
                            publishBtn.setDisabled(true);
                            revokeBtn.setDisabled(false);
                        }else{
                            publishBtn.setDisabled(false);
                            revokeBtn.setDisabled(true);
                        }
                    	panel.down("image[name=titileImage]").setSrc(TzUniversityContextPath+path);
                    });

                });
                var tab = contentPanel.add(cmp);
                contentPanel.setActiveTab(tab);
                Ext.resumeLayouts(true);
                if (cmp.floating) {
                    cmp.show();
                }
            }
            //修改图文素材
            if(mediaType=='B'){
                var jgId=panel.jgId;
                var wxAppId=panel.wxAppId;
                var materialID=selList[0].data.index;
                
                //是否有访问权限
                var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_WX_SCGL_COM"]["TZ_WX_TWSC_STD"];
                if( pageResSet == "" || pageResSet == undefined){
                    Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                    return;
                }
                //该功能对应的JS类
                var className = pageResSet["jsClassName"];
                if(className == "" || className == undefined){
                    Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_WX_TWSC_STD，请检查配置。');
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


                cmp = new ViewClass({
                	wxAppId: wxAppId,
                	jgId: jgId,
                	materialID: materialID,
                	actType : "update"
                });

                tab = contentPanel.add(cmp);

                contentPanel.setActiveTab(tab);

                Ext.resumeLayouts(true);

                if (cmp.floating) {
                    cmp.show();
                }
            }
        }
    },
	
	//删除素材
	deleteMaterial:function(btn){
        //选中行
		var panel=btn.findParentByType("weChatMaterialInfoPanel");
	    var picDataView=btn.findParentByType("panel").down("dataview[name=picView]");
	    var selList =  picDataView.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的素材");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选素材吗?', function(btnId){
                if(btnId == 'yes'){
                    var store = picDataView.store;
                    store.remove(selList);
                }
            },this);
        }
    
	},
	
	//图片素材发布
	pIssue:function(btn){
        var panel=btn.findParentByType("panel");
		var form=panel.down("form").getForm();
        var publishBtn=panel.down("button[name=publishBtn]");
        var revokeBtn=panel.down("button[name=revokeBtn]");
		var tzSeq=form.findField("tzSeq").getValue();
        if(tzSeq==""){
        	Ext.Msg.alert("提示","请保存图片素材后再发布");
            return false;
        }
        var formParams = form.getValues();
        var tzParams = '{"ComID":"TZ_WX_SCGL_COM","PageID":"TZ_WX_TPSC_STD","OperateType":"U","comParams":{"update":[{"type":"publish","data":'+Ext.JSON.encode(formParams)+'}]}}';
        Ext.tzSubmit(tzParams,function(response){
           var status=response.status;
           var tbTime=response.tbTime;
            form.findField("status").setValue(status);
        	form.findField("tbTime").setValue(tbTime);
            publishBtn.setDisabled(true);
            revokeBtn.setDisabled(false);
        },"发布成功",true,this);
	},
	
	//图片素材撤销发布
	pRevoke:function(btn){
        var panel=btn.findParentByType("panel");
        var form=panel.down("form").getForm();
        var publishBtn=panel.down("button[name=publishBtn]");
        var revokeBtn=panel.down("button[name=revokeBtn]");
        /*var tzSeq=form.findField("tzSeq").getValue();
        if(tzSeq==""){
            Ext.Msg.alert("提示","请保存图片素材后再发布");
            return false;
        }*/
        var formParams = form.getValues();
        var tzParams = '{"ComID":"TZ_WX_SCGL_COM","PageID":"TZ_WX_TPSC_STD","OperateType":"U","comParams":{"update":[{"type":"revoke","data":'+Ext.JSON.encode(formParams)+'}]}}';
        Ext.tzSubmit(tzParams,function(response){
        	var status=response.status;
            var tbTime=response.tbTime;
            form.findField("status").setValue(status);
         	form.findField("tbTime").setValue(tbTime);
            publishBtn.setDisabled(false);
            revokeBtn.setDisabled(true);
        },"撤销发布成功",true,this);
	},
	
	//图片素材保存
	pSave:function(btn){
        var form=btn.findParentByType("panel").down("form").getForm();
        if(!form.isValid()){
            return false;
        }
       
        var filePath=form.findField("filePath").getValue();
        if(filePath==""){
        	Ext.Msg.alert("提示","请先上传图片");
        	return false;
        }
        var formParams = form.getValues();
        var tzParams = '{"ComID":"TZ_WX_SCGL_COM","PageID":"TZ_WX_TPSC_STD","OperateType":"U","comParams":{"add":['+Ext.JSON.encode(formParams)+']}}';
        Ext.tzSubmit(tzParams,function(response){
           var tzSeq=response.tzSeq;
           var lastUpdateTime=response.lastUpdateTime;
           if(tzSeq>0){
        	   form.findField("tzSeq").setValue(tzSeq);
        	   form.findField("editTime").setValue(lastUpdateTime);
           }
        },"",true,this);
    },
	//图片素材确定
	pEnsure:function(btn){
        var form=btn.findParentByType("panel").down("form").getForm();
        var jgId=form.findField("jgId").getValue();
        var wxAppId=form.findField("wxAppId").getValue();
        //console.log(btn.findParentByType("panel").findParentByType("weChatMaterialInfoPanel"));
        //console.log(Ext.ComponentQuery.query("panel[reference=weChatMaterialInfoPanel]")[0]);
        //console.log(Ext.ComponentQuery.query("panel[reference=weChatMaterialInfoPanel]")[0].down('dataview[]'));
        if(!form.isValid()){
            return false;
        }

        var filePath=form.findField("filePath").getValue();
        if(filePath==""){
            Ext.Msg.alert("提示","请先上传图片");
            return false;
        }
        var formParams = form.getValues();
        var tzParams = '{"ComID":"TZ_WX_SCGL_COM","PageID":"TZ_WX_TPSC_STD","OperateType":"U","comParams":{"add":['+Ext.JSON.encode(formParams)+']}}';

        Ext.tzSubmit(tzParams,function(response){
            var tzSeq=response.tzSeq;
            var lastUpdateTime=response.lastUpdateTime;
            if(tzSeq>0){
                form.findField("tzSeq").setValue(tzSeq);
                form.findField("editTime").setValue(lastUpdateTime);
                var weChatMaterialInfoPanel=Ext.ComponentQuery.query("panel[reference=weChatMaterialInfoPanel]");
                var dataview=weChatMaterialInfoPanel[0].down("dataview[name=picView]");
                var tzStoreParams = '{"cfgSrhId": "TZ_WX_SCGL_COM.TZ_WX_SCGL_STD.TZ_WX_MEDIA_VW",' +
                    '"condition":{"TZ_JG_ID-operator":"01","TZ_JG_ID-value":"' + jgId + '","TZ_WX_APPID-operator":"01","TZ_WX_APPID-value":"' + wxAppId + '"}}';
                dataview.store.tzStoreParams = tzStoreParams;
                dataview.store.load();
                btn.findParentByType("panel").close();
            }
        },"",true,this);
    },
	
	//图片素材关闭
	pClose:function(){
		this.getView().close();
	},
	
	//图文素材发布
	pwIssue:function(){
		
	},
	
	//图文素材撤销发布
	pwRevoke:function(){},
	
	//图文素材保存
	pwSave:function(){},
	
	//图文素材确定
	pwEnsure:function(){},
	
	//图文素材关闭
	pwClose:function(){
		this.getView().close();
	}
});
