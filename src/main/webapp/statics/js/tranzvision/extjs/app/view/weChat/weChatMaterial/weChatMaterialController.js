Ext.define('KitchenSink.view.weChat.weChatMaterial.weChatMaterialController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.weChatMaterialController',
    
    //关闭微信窗口
    materialWindowClose:function(){
     this.getView().close();
    },

   //新增图片素材
	addPic:function(btn){
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
            //var formButton =panel.child('form');
           // var btndeletePdf=formButton.down('button[name=deletePdf]');
          //  btndeletePdf.hide();
        });
        var tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
	//新增图文素材
	addPicAndWord:function(){

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet[""][""];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.basicData.resData.resource.resourceSetInfoPanel';
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        clsProto = ViewClass.prototype;


        cmp = new ViewClass();
        cmp.actType="add";
        
//        cmp.on('afterrender',function(panel){
//			var form = panel.child('form').getForm();
//			form.findField('orgaID').setValue('NEXT');
//		});


        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    
	},
	
	//修改素材
	editMaterial:function(){},
	
	//删除素材
	deleteMaterial:function(){
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的素材");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选素材吗?', function(btnId){
                if(btnId == 'yes'){
                    var resSetStore = this.getView().store;
                    resSetStore.remove(selList);
                }
            },this);
        }
    
	},
	
	//图片素材发布
	pIssue:function(){},
	
	//图片素材撤销发布
	pRevoke:function(){},
	
	//图片素材保存
	pSave:function(){},
	
	//图片素材确定
	pEnsure:function(){},
	
	//图片素材关闭
	pClose:function(){
		this.getView().close();
	},
	
	//图文素材发布
	pwIssue:function(){},
	
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
