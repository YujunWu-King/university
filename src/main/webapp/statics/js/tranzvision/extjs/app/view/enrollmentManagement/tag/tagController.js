Ext.define('KitchenSink.view.enrollmentManagement.tag.tagController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.tagSet',
    // zxw 151023 新增标签定义时弹出窗口
    addTag: function(btn) {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_TAG_COM"]["TZ_BMGL_TAG_BJ_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_BJ_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_BJ_STD.nmyqx","您没有权限"));
            return;
        }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_BJ_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_BJ_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('tagDefineWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();

            this.getView().add(win);
        }
        win.show();
    },
    addTagRear: function(view,rowIndex) {
        var tagGrid = view.findParentByType("grid");
        var tagCellEditing = tagGrid.getPlugin('tagCellEditing');
        var tagStore =  tagGrid.getStore();
        var rowCount = rowIndex+1;
        tagCellEditing.cancelEdit();
        var r = Ext.create('KitchenSink.view.enrollmentManagement.tag.tagModel', {
            tagID:"NEXT",
            tagName: "",
            tagDesc: ""
        });

        tagStore.insert(rowCount,r);
        tagCellEditing.startEdit(r, 1);
    },
    deleteTags: function(){
	   //选中行
	   var selList = this.getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
           Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.youSelectedNothing","您没有选中任何记录"));
			return;
	   }else{
			Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.confirm","确认"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.deleteConfirm","您确定要删除所选记录吗?"), function(btnId){
				if(btnId == 'yes'){					   
				   var tagStore = this.getView().store;
                    tagStore.remove(selList);
				}												  
			},this);   
	   }
	},
    deleteTag: function(view, rowIndex){
        Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.confirm","确认"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.deleteConfirm","您确定要删除所选记录吗?"), function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    saveTags: function(btn){
        //更新操作参数
        var comParams = "";

        //修改json字符串
        var editJson = "";

        //标签信息列表
        var grid = btn.findParentByType("grid");

        //标签信息数据
        var store = grid.getStore();

        //修改记录
        var mfRecs = store.getModifiedRecords();
        var tagCellEditing = grid.getPlugin('tagCellEditing');

        for(var i=0;i<mfRecs.length;i++){
            /*标签名称不能为空*/
            var tagLength=mfRecs[i].get("tagName").length;
                 if(tagLength>30){
                    Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.tagNameIn30Character ","标签名称不能大于30个字符"),
                        function(e){
                            if(e == "ok"|| e == "OK" || e == "确定"){
                            tagCellEditing.startEdit(mfRecs[i], 1);
                            }
                        }
                    )
                return;
                }else{
                     if(mfRecs[i].get("tagID")=="NEXT"){
                         if(tagLength<1){
                             continue;
                         }
                     }
                 }

            //记录查重
            var tagName = mfRecs[i].get("tagName");
            var tagNameCount =0;
            var recIndex = store.findBy(function(record,id){
                if(record.get("tagName")==tagName){
                    tagNameCount++;
                    if(tagNameCount>1){
                        return true;
                    }
                }
            },0);

            if(tagNameCount>1){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.tagNameRepeated","标签名称出现重复"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            tagCellEditing.startEdit(mfRecs[i], 1);
                        }
                    }
                )
                return;
            }

            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }

        if(editJson != "")comParams = '"update":[' + editJson + "]";

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
            if(comParams == ""){
                comParams = '"delete":[' + removeJson + "]";
            }else{
                comParams = comParams + ',"delete":[' + removeJson + "]";
            }
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_BMGL_TAG_COM","PageID":"TZ_BMGL_TAG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据

        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    ensureTags: function(btn){
        //更新操作参数
        var comParams = "";

        //修改json字符串
        var editJson = "";

        //标签信息列表
        var grid = btn.findParentByType("grid");

        //标签信息数据
        var store = grid.getStore();

        //修改记录
        var mfRecs = store.getModifiedRecords();
        var tagCellEditing = grid.getPlugin('tagCellEditing');

        for(var i=0;i<mfRecs.length;i++){
            /*标签名称不能为空*/
            var tagLength=mfRecs[i].get("tagName").length;
            if(tagLength>30){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.tagNameIn30Character ","标签名称不能大于30个字符"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            tagCellEditing.startEdit(mfRecs[i], 1);
                        }
                    }
                )
                return;
            }else{
                if(mfRecs[i].get("tagID")=="NEXT"){
                    if(tagLength<1){
                        continue;
                    }
                }
            }

            //记录查重
            var tagName = mfRecs[i].get("tagName");
            var tagNameCount =0;
            var recIndex = store.findBy(function(record,id){
                if(record.get("tagName")==tagName){
                    tagNameCount++;
                    if(tagNameCount>1){
                        return true;
                    }
                }
            },0);

            if(tagNameCount>1){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.tagNameRepeated","标签名称出现重复"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            tagCellEditing.startEdit(mfRecs[i], 1);
                        }
                    }
                )
                return;
            }

            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }

        if(editJson != "")comParams = '"update":[' + editJson + "]";

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
            if(comParams == ""){
                comParams = '"delete":[' + removeJson + "]";
            }else{
                comParams = comParams + ',"delete":[' + removeJson + "]";
            }
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_BMGL_TAG_COM","PageID":"TZ_BMGL_TAG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据

        Ext.tzSubmit(tzParams,function(){
           // store.reload();
        },"",true,this);
        //关闭
        grid.close();
    },
    /*关闭标签定义页*/
    closeTags: function(btn){
        //标签信息列表
        var grid = btn.findParentByType("grid");
        grid.close();
    },
    /*编辑选中标签*/
    editTag: function() {
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.qxzytyxgdjl","请选择一条要修改的记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_STD.znxzytyxgdjl","只能选择一条要修改的记录"));
            return;
        }
        var tagId = selList[0].get("tagId");
        //显示标签定义页面;
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_TAG_COM"]["TZ_BMGL_TAG_BJ_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_BJ_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_BJ_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_BJ_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_BJ_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            // </debug>
        }


        var win = this.lookupReference('tagDefineWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();

            this.getView().add(win);
        }
        var tzParams = '{"ComID":"TZ_BMGL_TAG_COM","PageID":"TZ_BMGL_TAG_BJ_STD","OperateType":"QF","comParams":{"tagId":"'+tagId+'"}}';
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){
            var formData = responseData.formData;
            form.setValues(formData);
        });

        win.show();
    },

    /*编辑当前标签*/
    editCurrentTag: function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //ID
        var tagId = selRec.get("tagId");
        var tagName = selRec.get("tagName");
        //显示标签定义页面
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_TAG_COM"]["TZ_BMGL_TAG_BJ_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_BJ_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_BJ_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_BJ_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_TAG_COM.TZ_BMGL_TAG_BJ_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            // </debug>
        }


        var win = this.lookupReference('tagDefineWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();

            this.getView().add(win);
        }
        var tzParams = '{"ComID":"TZ_BMGL_TAG_COM","PageID":"TZ_BMGL_TAG_BJ_STD","OperateType":"QF","comParams":{"tagId":"'+tagId+'"}}';
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){
            var formData = responseData.formData;
            form.setValues(formData);
        });

        win.show();
    }
});

