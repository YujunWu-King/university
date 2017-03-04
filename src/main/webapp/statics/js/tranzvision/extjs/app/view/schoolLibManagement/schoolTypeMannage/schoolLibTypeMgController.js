/**
 * Created by tzhjl on 2017/1/12.
 */
Ext.define('KitchenSink.view.schoolLibManagement.schoolTypeMannage.schoolLibTypeMgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.schoolMgTypeConter',
    //院校库添加
    addResSet: function() {
        //是否有访问权限   
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SCH_TYPE_COM"]["TZ_SCTYE_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SCTYE_INFO_STD，请检查配置。');
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

        cmp.on('afterrender',function(panel){
            var form = panel.child('form').getForm();
            form.findField('typeID').setValue('NEXT');
        });


        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },


    //详情页关闭按钮
    onSchoolClose:function(){
        this.getView().close();

    },
    //院校库列表页编辑
    editResSet: function() {
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
        //资源集合ID
        var resSetID = selList[0].get("typeID");
        //显示资源集合信息编辑页面
        this.editResSetInfoByID(resSetID);
    },
    editCurrResSet: function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //资源集合ID
        var typeID = selRec.get("typeID");
        //显示资源集合信息编辑页面
        this.editResSetInfoByID(typeID);
    },
    ////院校库列表页编辑数据操作
    editResSetInfoByID: function(typeID){
        //是否有访问权限.
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_SCH_TYPE_COM"]["TZ_SCTYE_INFO_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_SCTYE_INFO_STD，请检查配置。');
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


        cmp = new ViewClass();
        //操作类型设置为更新
        cmp.actType = "update";

        cmp.on('afterrender',function(panel){
            //资源集合表单信息;
            var form = panel.child('form').getForm();
            form.findField("typeID").setReadOnly(true);
            form.findField("typeID").addCls("lanage_1");
            //资源信息列表

            //参数
            var tzParams = '{"ComID":"TZ_SCH_TYPE_COM","PageID":"TZ_SCTYE_INFO_STD","OperateType":"QF","comParams":{"typeID":"'+typeID+'"}}';
            //加载数据
            Ext.tzLoad(tzParams,function(responseData){
                //资源集合信息数据
                var formData = responseData;
                form.setValues(formData);
                //资源集合信息列表数据


            });

        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },

    //院校库列表页删除按钮
    deleteResSets: function(){
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
                    var resSetStore = this.getView().store;
                    resSetStore.remove(selList);
                }
            },this);
        }
    },
    deleteCurrResSet: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },

    //院校库列表保存删除
    onSaveRemoveData: function(btn){
        //院校库变量列表
        var grid = btn.findParentByType("grid");
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
        var comParams = "";
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }
        //提交参数.
        var tzParams = '{"ComID":"TZ_SCH_TYPE_COM","PageID":"TZ_SCTYE_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(responseData){
            store.reload();
        },"保存成功！",true,this);
    },

    //院校库列表页确定按钮
    ensureonSaveRemoveData:function(btn){
        this.onSaveRemoveData(btn);
        //关闭窗口
        var comView = this.getView();
        comView.close();
    },


    //院校库详情页按钮
    onschoolSave: function(btn){
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获得院校库表单信息
            var tzParams = this.getResSetInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
            	form.findField("typeID").setValue(responseData);
                comView.actType = "update";
            },"",true,this);
        }
    },
    
    
     //院校库详情页确定按钮
    ensureonschoolSave:function(btn){
    	var form = this.getView().child("form").getForm();

        if (form.isValid()) {
            //获得院校库表单信息
            var tzParams = this.getResSetInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
		    contentPanel.child("schoolMgTypeList").store.reload();
                comView.close();
            },"",true,this);
        }
    
    },

    closeResSets:function(btn){
        //关闭窗口
        var comView = this.getView();
        comView.close();
    },


    getResSetInfoParams: function(){
        //院校库表单
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();

        //组件信息标志
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":['+Ext.JSON.encode(formParams)+']';
        }
        //修改json字符串
        var editJson = "";
        if(actType == "update"){
            comParams = '"update":['+Ext.JSON.encode(formParams)+']';
        }

        //提交参数
        var tzParams = '{"ComID":"TZ_SCH_TYPE_COM","PageID":"TZ_SCTYE_INFO_STD","OperateType":"U","comParams":{'+comParams+'}}';
        console.log(tzParams);
        return tzParams;
    },
     //可配置搜索
	searchschMgList: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_SCH_TYPE_COM.TZ_SCTYE_LIST_STD.TZ_SCHOOL_TYPE_VW',
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	}
    
});

