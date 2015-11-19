Ext.define('KitchenSink.view.enrollmentManagement.color.colorController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.colorSet',
    /*addColor: function(btn) {
        var colorSortGrid = btn.findParentByType("grid");
        var colorSortCellEditing = colorSortGrid.getPlugin('colorSortCellEditing');
        var colorSortStore =  colorSortGrid.getStore();
        var rowCount = colorSortStore.getCount();
            colorSortCellEditing.cancelEdit();
            var r = Ext.create('KitchenSink.view.enrollmentManagement.color.colorModel', {
                colorSortID:"NEXT",
                colorName: "",
                colorCode: "ff0000"
            });

            colorSortStore.insert(rowCount,r);
            colorSortCellEditing.startEdit(r, 1);
    },*/
    addColor: function(btn) {
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_COLOR_COM"]["TZ_BMGL_COLOR2_STD"];
        if( pageResSet == "" || pageResSet == undefined){
         Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR2_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR2_STD.nmyqx","您没有权限"));
         return;
         }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR2_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR2_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var win = this.lookupReference('colorWindow');
       if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();

            this.getView().add(win);
        }
        win.show();

    },
    deleteColors: function(){
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
				   var colorStore = this.getView().store;
                    colorStore.remove(selList);
				}												  
			},this);   
	   }
	},
    deleteColor: function(view, rowIndex){
        Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.confirm","确认"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.deleteConfirm","您确定要删除所选记录吗?"), function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    saveColors: function(btn){
        //更新操作参数
        var comParams = "";

        //修改json字符串
        var editJson = "";

        //颜色类别信息列表
        var grid = btn.findParentByType("grid");
        //颜色类别信息数据
        var store = grid.getStore();
        //修改记录
        var mfRecs = store.getModifiedRecords();
        var colorSortCellEditing = grid.getPlugin('colorSortCellEditing');
        for(var i=0;i<mfRecs.length;i++){
            /*标签名称不能为空*/
            var colorLength=mfRecs[i].get("colorName").length;
            if(colorLength>30){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colorNameIn30Character","颜色类别名称不能大于30个字符"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            colorSortCellEditing.startEdit(mfRecs[i], 2);
                        }
                    }
                )
                return;
            }else{
                if(mfRecs[i].get("colorSortID")=="NEXT"){
                    if(colorLength<1){
                        Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colorNameIsRequired","颜色类别名称不能为空"),
                            function(e){
                                if(e == "ok"|| e == "OK" || e == "确定"){
                                    colorSortCellEditing.startEdit(mfRecs[i], 2);
                                }
                            }
                        )
                        return;
                    }
                }
            }

            //记录查重
            var colorCode = mfRecs[i].get("colorCode");
            var colorName = mfRecs[i].get("colorName");
            var colorCodeCount =0;
            var colorNameCount =0;

            var recIndex = store.findBy(function(record,id){
                if(record.get("colorCode")==colorCode){
                    colorCodeCount++;
                    if(colorCodeCount>1){
                        return true;
                    }
                };
                if(record.get("colorName")==colorName){
                    colorNameCount++;
                    if(colorNameCount>1){
                        return true;
                    }
                };
            },0);

            if(colorCodeCount>1){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colorRepeated","颜色出现重复"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            colorSortCellEditing.startEdit(mfRecs[i], 1);
                        }
                    }
                )
                return;
            }else if(colorNameCount>1){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colorNameRepeated","颜色类别名称出现重复"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            colorSortCellEditing.startEdit(mfRecs[i], 2);
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
        var tzParams = '{"ComID":"TZ_BMGL_COLOR_COM","PageID":"TZ_BMGL_COLOR_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    ensureColors: function(btn){
        //更新操作参数
        var comParams = "";

        //修改json字符串
        var editJson = "";

        //颜色类别信息列表
        var grid = btn.findParentByType("grid");
        //颜色类别信息数据
        var store = grid.getStore();
        //修改记录
        var mfRecs = store.getModifiedRecords();
        var colorSortCellEditing = grid.getPlugin('colorSortCellEditing');
        for(var i=0;i<mfRecs.length;i++){
            /*标签名称不能为空*/
            var colorLength=mfRecs[i].get("colorName").length;
            if(colorLength>30){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colorNameIn30Character","颜色类别名称不能大于30个字符"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            colorSortCellEditing.startEdit(mfRecs[i], 2);
                        }
                    }
                )
                return;
            }else{
                if(mfRecs[i].get("colorSortID")=="NEXT"){
                    if(colorLength<1){
                        Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colorNameIsRequired","颜色类别名称不能为空"),
                            function(e){
                                if(e == "ok"|| e == "OK" || e == "确定"){
                                    colorSortCellEditing.startEdit(mfRecs[i], 2);
                                }
                            }
                        )
                        return;
                    }
                }
            }

            //记录查重
            var colorCode = mfRecs[i].get("colorCode");
            var colorName = mfRecs[i].get("colorName");
            var colorCodeCount =0;
            var colorNameCount =0;

            var recIndex = store.findBy(function(record,id){
                if(record.get("colorCode")==colorCode){
                    colorCodeCount++;
                    if(colorCodeCount>1){
                        return true;
                    }
                };
                if(record.get("colorName")==colorName){
                    colorNameCount++;
                    if(colorNameCount>1){
                        return true;
                    }
                };
            },0);

            if(colorCodeCount>1){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colorRepeated","颜色出现重复"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            colorSortCellEditing.startEdit(mfRecs[i], 1);
                        }
                    }
                )
                return;
            }else if(colorNameCount>1){
                Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.colorNameRepeated","颜色类别名称出现重复"),
                    function(e){
                        if(e == "ok"|| e == "OK" || e == "确定"){
                            colorSortCellEditing.startEdit(mfRecs[i], 2);
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
        var tzParams = '{"ComID":"TZ_BMGL_COLOR_COM","PageID":"TZ_BMGL_COLOR_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
        //关闭
        grid.close();
    },
    /*关闭颜色定义页*/
    closeColors: function(btn){
        //标签信息列表
        var grid = btn.findParentByType("grid");
        grid.close();
    },
    /*编辑当前颜色*/
    editCurrentColor: function(view,rowIndex) {
        var store = view.findParentByType("grid").store;
        var selRec = store.getAt(rowIndex);
        //ID
        var colorSortID = selRec.get("colorSortID");
        var colorName = selRec.get("colorName");
        var colorCode = selRec.get("colorCode");
        //显示标签定义页面
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_COLOR_COM"]["TZ_BMGL_COLOR2_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR2_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR2_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR2_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR2_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
        var win = this.lookupReference('colorWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();

            this.getView().add(win);
        }
        var tzParams = '{"ComID":"TZ_BMGL_COLOR_COM","PageID":"TZ_BMGL_COLOR2_STD","OperateType":"QF","comParams":{"colorSortID":"'+colorSortID+'"}}';
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){
            var formData = responseData.formData;
            form.setValues(formData);
        });

        win.show();
    },
    /*编辑选中颜色*/
    editColor: function() {
        //选中行
        var selList = this.getView().getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.qxzytyxgdjl","请选择一条要修改的记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR_STD.znxzytyxgdjl","只能选择一条要修改的记录"));
            return;
        }
        var colorSortID = selList[0].get("colorSortID");
        //显示颜色定义页面（同编辑当前颜色）;
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_COLOR_COM"]["TZ_BMGL_COLOR2_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR2_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR2_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR2_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_COLOR_COM.TZ_BMGL_COLOR2_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
        var win = this.lookupReference('colorWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            var form = win.child('form').getForm();

            this.getView().add(win);
        }
        var tzParams = '{"ComID":"TZ_BMGL_COLOR_COM","PageID":"TZ_BMGL_COLOR2_STD","OperateType":"QF","comParams":{"colorSortID":"'+colorSortID+'"}}';
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){
            var formData = responseData.formData;
            form.setValues(formData);
        });

        win.show();
    }
});

