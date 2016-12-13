Ext.define('KitchenSink.view.template.survey.temp.zxdcMbController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.zxdcMbController',
   /*新增问卷模板*/
    addWjmb:function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_MBGL_COM"]["TZ_ZXDC_XJMB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_XJMB_STD，请检查配置。');
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
        var win = this.lookupReference('newZxdcMbWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();
            this.getView().add(win);
        }
        win.show();
    },
    /*新增页面 关闭按钮*/
   onNewClose:function(btn){
       var win = btn.findParentByType("window");
       win.close();
},
   /*新增页面 确定按钮*/
    onNewEnsure:function(btn){
       // alert("问卷模板 新增页面");
        Ext.tzSetCompResourses("TZ_ZXDC_MBGL_COM");
        //组件注册信息列表
        var grid = btn.findParentByType("zxdcMbListInfo");
        //组件注册信息数据
        var store = grid.getStore();
        var win = this.lookupReference('myZxdcWjmbWindow');
        var activeTab = win.items.items[0].getActiveTab(),
            id = '';
        var tplName = Ext.get(activeTab.id).select('input').elements[0].value,
            tplId = "";
        var count=0;
        if (activeTab.itemId == "add") { //新增
            var form = activeTab.getForm();
        }
        if (activeTab.itemId == "predefine") { //从现有模板复制
            Ext.each(Ext.query(".tplitem"),
                function(i) {
                    if (this.style.backgroundColor == "rgb(173, 216, 230)") {
                            tplId = this.getAttribute("data-id");
                            //return false;
                    }
                });
        } else {
            tplId = "";
        }
        if (tplName) {
            var tzStoreParams = '{"add":[{"id":"' + tplId + '","name":"' + tplName + '"}]}';
            console.log(tplId);
            var tzParams = '{"ComID":"TZ_ZXDC_MBGL_COM","PageID":"TZ_ZXDC_XJMB_STD","OperateType":"U","comParams":' + tzStoreParams + '}';
            Ext.tzSubmit(tzParams,
                function(jsonObject) {
                    store.reload();
                    win.close();
                },"",true,this);
        }
    },
    /*删除问卷模板*/
    deleteWjmb:function(btn){
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var resSetStore =  btn.findParentByType("grid").store;
                    resSetStore.remove(selList);
                }
            },this);
        }
    },
    /*问卷模板列表保存按钮事件*/
    mbInfoSave:function(btn){
        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var removeJson = "";
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
      //  console.log(comParams);
        var tzParams = '{"ComID":"TZ_ZXDC_MBGL_COM","PageID":"TZ_ZXDC_MBGL_STD","OperateType":"U","comParams":{'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    /*可配置搜索，查询问卷模板*/
    findWjmb:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_ZXDC_MBGL_COM.TZ_ZXDC_MBGL_STD.TZ_ZXDC_MB_VW', //这里面的组件页面视图需要换成自己的
            condition:
            {
                "TZ_JG_ID":Ext.tzOrgID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    /*复制问卷模板*/
    copyWjmb: function(view, rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        this.TZ_APP_TPL_ID = selRec.get("TZ_APP_TPL_ID");
        Ext.MessageBox.prompt('复制模板', '请输入另存模板的名称:', this.showResultText, this);
    },
    showResultText: function(id, text) {
       // console.log(this.TZ_APP_TPL_ID,text);
        Ext.tzSetCompResourses("TZ_ZXDC_MBGL_COM");
        if (id == "ok") {
            if (text) {
                //组件注册信息数据
                var store = this.getView().getStore();
                var tzStoreParams = '{"add":[{"id":"' + this.TZ_APP_TPL_ID + '","name":"' + text + '"}]}';
                var tzParams = '{"ComID":"TZ_ZXDC_MBGL_COM" ,"PageID":"TZ_ZXDC_XJMB_STD","OperateType":"U","comParams":' + tzStoreParams + '}';
                Ext.tzSubmit(tzParams,
                    function(data) {
                        store.reload();
                       //如果创建成功，直接跳转到编辑页面
                       var tplId=data.id;
                        //var tzParams = '{"ComID":"TZ_ZXDC_MBGL_COM","PageID":"TZ_ZXDC_EDIT_STD","OperateType":"HTML","comParams":{"ZXDC_TPL_ID":' + tplId + '}}';
                        //var newTab=window.open('about:blank');
                        //newTab.location.href=Ext.tzGetGeneralURL()+'?tzParams='+tzParams;
                       var url = TzUniversityContextPath + "/admission/surveyform/" + tplId;
               			window.open(url, '_blank');
                    },"",true,this);
            } else {
                Ext.MessageBox.alert('提示', '新的模板名称不能为空！');
            }
        }
        return;
    },
    /*设置问卷模板*/
  setWjmb:function(view,rowindex){
      var store = view.findParentByType("grid").store;
      var selRec = store.getAt(rowindex);
      var tplId = selRec.get("TZ_APP_TPL_ID");
      //显示资源集合信息编辑页面
      this.editMbByID(tplId);
    },
    editMbByID:function(tplId){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_MBGL_COM"]["TZ_ZXDC_MBSZ_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_MBSZ_STD，请检查配置。');
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

            // Sometimes we forget to include allowances for other themes, so issue a warning as a reminder.
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        var win = this.lookupReference('myZxdcMbszWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();
            this.getView().add(win);
        }
        var tzParams = '{"ComID":"TZ_ZXDC_MBGL_COM","PageID":"TZ_ZXDC_MBSZ_STD","OperateType":"QF","comParams":{"tplId":"'+tplId+'"}}';
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){
            var formData = Ext.util.JSON.decode(responseData.formData);
            form.setValues(formData);
        });
        win.show();
    },
    /*使用问卷创建一个新的调查  在模板这里不需要有什么逻辑，
    * 点击时，需要打开创建调查（使用问卷模板创建调查）,需要向问卷调查模块传一个
    * 模板ID即可
    * view 代表的是整个页面   rowindex代表id
    * */
    useWjmb:function(view,rowindex){
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowindex);
        var tplName = selRec.get("TZ_APP_TPL_MC");
        Ext.tzSetCompResourses('TZ_ZXDC_WJGL_COM');/*组件之间的跳转，需要哪个组件就把它加载进来*/
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ZXDC_WJGL_COM"]["TZ_ZXDC_XJWJ_STD"];

        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_ZXDC_XJWJ_STD，请检查配置。');
            return;
        }
        var win =this.lookupReference('myDcwjWindow');
        if (!win) {
            Ext.syncRequire(className);
            var ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
            var activeTab =win.items.items[0].setActiveTab('predefine');
            activeTab.items.items[0].setValue(tplName);
            console.log( activeTab.items.items[0].value)
        }else{
            var  activeTab = win.items.items[0].getActiveTab();
            document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = "";
        }
        win.show();
        window.wjdc_pre=null;//目的是让他每次都执行下面的操作
        if (!window.wjdc_pre) {
            window.wjdc_pre = function(el) {
                Ext.each(Ext.query(".tplitem"),
                    function(i) {
                        this.style.backgroundColor = null
                    });
                el.style.backgroundColor = "rgb(173, 216, 230)";
                var activeTab = win.items.items[0].getActiveTab();
                var newName = el.getElementsByClassName("tplname")[0].getAttribute("title")  + "_" + ( + new Date());
                document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = newName;
            }
        }
    },

    /*编辑问卷模板*/
    editWjmb:function(view,rowindex){
     	var selRec = view.getStore().getAt(rowindex);
      	var tplId = selRec.get("TZ_APP_TPL_ID");
//		var tzParams = '{"ComID":"TZ_ZXDC_MBGL_COM","PageID":"TZ_ZXDC_EDIT_STD","OperateType":"HTML","comParams":{"ZXDC_TPL_ID":' + tplId + '}}';
//		var newTab=window.open('about:blank');
//	  	newTab.location.href=Ext.tzGetGeneralURL()+'?tzParams='+tzParams;
        var url = TzUniversityContextPath + "/admission/surveyform/" + tplId;
		window.open(url, '_blank');
    },
    /*问卷模板预览*/
    previewWjmb:function(view,rowIndex){
        var selRec = view.getStore().getAt(rowIndex);
        var tplId = selRec.get("TZ_APP_TPL_ID");
        var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_VIEW_STD","OperateType":"HTML","comParams":{"TYPE":"TPL","SURVEY_ID":"' + tplId + '"}}';
        var newTab=window.open('about:blank');
        //alert(tzParams);
        newTab.location.href=Ext.tzGetGeneralURL()+'?tzParams='+tzParams;
    }, /*新增窗口保存*/
    onNewWjEnsure:function(btn){
        Ext.tzSetCompResourses("TZ_ZXDC_WJGL_COM");
        var win = this.lookupReference('myDcwjWindow');
        var activeTab = win.items.items[0].getActiveTab(),
            id = '';
        var wjbt = Ext.get(activeTab.id).select('input').elements[0].value,
            wjId = "";
        if (activeTab.itemId == "add") { //新增
            var form = activeTab.getForm();
        };
        if (activeTab.itemId == "predefine") { //从现有模板复制
            Ext.each(Ext.query(".tplitem"),
                function(i) {
                    if (this.style.backgroundColor == "rgb(173, 216, 230)") {
                        wjId = this.getAttribute("data-id");  //这里获得的id实际上是模板id
                        return false;
                    }
                });
        } else {
            wjId = "";
        }
        if (wjbt) {
            var tzStoreParams = '{"add":[{"id":"' + wjId + '","name":"' + wjbt + '"}]}';
            var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_XJWJ_STD","OperateType":"U","comParams":' + tzStoreParams + '}';
            Ext.tzSubmit(tzParams,
                function(jsonObject) {
                  win.close();
                },"",true,this);
        }
    },
    onLogicalSet:function(view,rowindex){
      
      var selRec = view.getStore().getAt(rowindex);
      var tplId = selRec.get("TZ_APP_TPL_ID");
	/*
	 // var tzParams = '{"ComID":"TZ_ZXDC_MBGL_COM","PageID":"TZ_ZXDC_LJGZ_STD","OperateType":"HTML","comParams":{"TZ_APP_TPL_ID":' + tplId + '}}';
	  var newTab=window.open('about:blank');
	  //newTab.location.href=Ext.tzGetGeneralURL()+'?tzParams='+tzParams;
	  newTab.location.href=Ext.tzGetGeneralURL()+'?classid=surveyTmpLogic&TZ_APP_TPL_ID='+tplId;
	 */
	 var logicUrl = Ext.tzGetGeneralURL()+'?classid=surveyTmpLogic&EXECUTE=MB&TZ_DC_WJ_ID='+tplId;
			$.layer({
				type: 2,
				title: false,
				fix: true,
				closeBtn: false,
				shadeClose: false,
				icon:2,
				shade : [0.3 , '#000' , true],
				border : [3 , 0.3 , '#000', true],
				offset: ['30%',''],
				area: ['1040px','600px'],
				move : true,
				iframe: {src: logicUrl}
			});
    }
});

