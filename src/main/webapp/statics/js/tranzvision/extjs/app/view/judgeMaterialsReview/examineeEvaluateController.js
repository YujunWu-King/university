Ext.define('KitchenSink.view.judgeMaterialsReview.examineeEvaluateController',{
	extend:'Ext.app.ViewController',
	alias:'controller.examineeEvaluateController',
	//进行评审
	materialsReviewTest:function() {
		//是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PW_CLPS_COM"]["TZ_CLPS_DF_STD"];
        if(pageResSet=="" || pageResSet==undefined) {
            Ext.MessageBox.alert('提示','您没有访问或修改数据的权限');
        }
        //改功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className==""||className==undefined){
            Ext.MessageBox.alert('提示','未找到该功能页面对应的JS类，页面ID：TZ_CLPS_DF_STD，请检查配置。');
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
        
        var classId,applyBatchId,applyBatchName,bmbId;
        classId='124';
        applyBatchId='46';
        applyBatchName='2018年第三批次在职项目（P班）';
        bmbId='222';
        
        cmp = new ViewClass({
        	classId:classId,
        	applyBatchId:applyBatchId,
        	applyBatchName:applyBatchName,
        	bmbId:bmbId
        });
        
        cmp.on('afterrender',function(){
        	var examineeList = cmp.down("examineeListGrid");
        	var examineeBmbArea = cmp.down("examineeBmbArea");
        	
        	var tzParams = '{"ComID":"TZ_PW_CLPS_COM","PageID":"TZ_CLPS_DF_STD","OperateType":"U","comParams":{"classId":"'+classId+'","applyBatchId":"'+applyBatchId+'","bmbId":"'+bmbId+'"}}';
        	Ext.tzLoad(tzParams,function(responseData){
        		//考生列表
        	
        		//报名表区域
        		var bmbUrl='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+bmb+'"}}';
        		var bmbArea = '<iframe src="'+bmbUrl+'" frameborder="0" width="100%" height="100%" style="display:inline;"></iframe>'; 
        		examineeBmbArea.setHtml(bmbArea);
        		
        		//打分区域
        	});
        });
        
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if(cmp.floating){
        	cmp.show();
        }
        
	},
	//返回评审主页面
	returnMaterialsReview:function() {
		
	},
	//考生列表-点击考生姓名
	changeExaminee:function() {
		
	},
	//保存
	saveExamineeEvaluate:function() {
		
	},
	//保存并获取下一个考生
	saveAndGetNext:function() {
		
	}
});