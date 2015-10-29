
Ext.define("KitchenSink.view.tzLianxi.liuzh.bugMg.bugController", {
    extend: 'Ext.app.ViewController',
    alias: 'controller.bugController',
    viewBug:function(btn){
        var grid = btn.findParentByType('grid');
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要查看的记录");
            return;
        }
        else if(checkLen !=1 ){
            Ext.Msg.alert("提示","请您仅选择一条记录进行查看");
            return;
        }else{

            var BugID=selList[0].get("BugID");
            var contentPanel,className,ViewClass,cmp;
            contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
            contentPanel.body.addCls('kitchensink-example');
            className = 'KitchenSink.view.tzLianxi.liuzh.bugMg.OprBug';
            if(!Ext.ClassManager.isCreated(className)){
                Ext.syncRequire(className);
            }
            ViewClass = Ext.ClassManager.get(className);

            cmp = new ViewClass(
                action=0,
                dis=true);
            cmp.on('afterrender',function(panel){
                //组件注册表单信息;
                var form = panel.child('form').getForm();
                form.findField("BugID").setReadOnly(true);
                //页面注册信息列表
                var grid = panel.child('grid');
                var tzParams = '{"ComID":"LX_LZH_BUGMG_COM","PageID":"LX_BUG_DETAIL_STD","OperateType":"QF","comParams":{"bugID":"'+BugID+'"}}';
                Ext.tzLoad(tzParams,function(responseData){
                    //组件注册信息数据
                    var formData = responseData.formData;
                    form.setValues(formData);
                    //页面注册信息列表数据
                    var bugList = responseData.listData;

                    var tzStoreParams = '{"bugID":"'+BugID+'"}';
                    // grid.store.tzStoreParams = tzStoreParams;
                    // grid.store.load();
                });

            });
            tab = contentPanel.add(cmp);
            contentPanel.setActiveTab(tab);
            Ext.resumeLayouts(true);
            if (cmp.floating) {
                cmp.show();
            }
        }
    },
    addNewBug:function(btn){
        /**/
        var baseBtn = btn;
        var grid =baseBtn.findParentByType("grid");
        var store = grid.store;
        console.log(store);
        var contentPanel,className,ViewClass,cmp;
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        className = 'KitchenSink.view.tzLianxi.liuzh.bugMg.OprBug';
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        cmp = new ViewClass(
            action=1,
            dis=false);
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }

    },
    deleteBug: function(btn){
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
    editBug:function(btn,rowIndex){

        var rec = btn.findParentByType('grid').getStore().getAt(rowIndex);
        var BugID=rec.data.BugID;
        var contentPanel,className,ViewClass,cmp;
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        className = 'KitchenSink.view.tzLianxi.liuzh.bugMg.OprBug';
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        cmp = new ViewClass(
            action=1,
            dis=false);
        cmp.on('afterrender',function(panel){
            //组件注册表单信息;
            var form = panel.child('form').getForm();
            form.findField("BugID").setReadOnly(true);
            //页面注册信息列表
            var grid = panel.child('grid');
            var tzParams = '{"ComID":"LX_LZH_BUGMG_COM","PageID":"LX_BUG_DETAIL_STD","OperateType":"QF","comParams":{"bugID":"'+BugID+'"}}';
            Ext.tzLoad(tzParams,function(responseData){
                //组件注册信息数据
                var formData = responseData.formData;
                form.setValues(formData);
                //页面注册信息列表数据
                var bugList = responseData.listData;

                var tzStoreParams = '{"bugID":"'+BugID+'"}';
               // grid.store.tzStoreParams = tzStoreParams;
               // grid.store.load();
            });

            /* Ext.Ajax.request({
                 url : '/psc/TZDEV/EMPLOYEE/CRM/s/WEBLIB_LX_BUG.LX_BUG.FieldFormula.iScript_bugMg',
                 params:{
                     params:'{"type":"getBug","bugID":"'+BugID+'"}'
                 },
                 success:function(response){
                     var formData = Ext.JSON.decode(response.responseText);
                     var form = panel.child('form').getForm();
                     form.setValues(formData);
                 }
             });
             */
        });
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },

    bugSave: function(btn){
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取组件注册信息参数

            var formParams = form.getValues();
            //console.log(formParams);
            //comParams = '"add":[{"typeFlag":"COM","data":'+Ext.JSON.encode(form.getValues())+'}]';
            var comParams = '"update":[{"formData":'+Ext.JSON.encode(formParams)+'}]';
            //var comParams='"update":'+Ext.JSON.encode(formParams);
            //页面注册信息列表
           var contentPanel = Ext.getCmp('tranzvision-framework-content-panel'), grid;
           for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
               if (contentPanel.items.items[x].title == 'Bug管理') {
                   grid = Ext.getCmp(contentPanel.items.keys[x]);
               }
           }
           //页面注册信息数据
            var store = grid.getStore();
            //修改记录
            var tzParams = '{"ComID":"LX_LZH_BUGMG_COM","PageID":"LX_BUG_LIST_STD","OperateType":"U","comParams":{' + comParams + '}}';
          /*  var bugView = this.getView();
            //页面注册信息列表
            var grid = bugView.child("grid");
            //页面注册信息数据
            var store = grid.getStore();*/
            Ext.tzSubmit(tzParams,function(responseData){
                form.findField("BugID").setReadOnly(true);
                if(store.isLoaded()){
                    store.reload();
                }
            },"",true,this);
        }
    },
    bugEnsure: function(btn){
        //组件注册表单
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取组件注册信息参数

            var formParams = form.getValues();
            //开始附件集;
            //var attachmentGrid = Ext.getCmp('attachmentGrid');
            var attachmentGrid =this.getView().down('grid[name=attachmentGrid]');
            //附件集stroe
            var attachmentGridstore = attachmentGrid.getStore();
            //修改记录
            var mfAttachmentGridRecs = attachmentGridstore.getModifiedRecords();
            var editJson = "";
            for(var i=0;i<mfAttachmentGridRecs.length;i++){
                if(editJson == ""){
                    editJson =Ext.JSON.encode(mfAttachmentGridRecs[i].data);
                }else{
                    editJson = editJson + ','+Ext.JSON.encode(mfAttachmentGridRecs[i].data);
                }
            };


            //删除json字符串
            var removeJson = "";
            //删除记录
            var attachmentGridRemoveRecs = attachmentGridstore.getRemovedRecords();
            for(var i=0;i<attachmentGridRemoveRecs.length;i++){
                if(removeJson == ""){
                    removeJson = Ext.JSON.encode(attachmentGridRemoveRecs[i].data);
                    //removeJson = '{"typeFlag":"ARTATTACHINFO","artId":"'+artId+'","data":'+Ext.JSON.encode(attachmentGridRemoveRecs[i].data)+'}';
                }else{
                    removeJson = removeJson + ','+Ext.JSON.encode(attachmentGridRemoveRecs[i].data);
                   // removeJson = removeJson + ',{"typeFlag":"ARTATTACHINFO","artId":"'+artId+'","data":'+Ext.JSON.encode(attachmentGridRemoveRecs[i].data)+'}';
                }
            }
            //结束附件集;

            /* var attachmentFile = attachmentGridstore.getRange();
             for(var i = 0;i<attachmentFile.length;i++){
                 files.push({
                     fileID : attachmentFile[x].data.attachmentID.replace(/\./g,"_"),
                     fileName : attachmentFile[x].data.attachmentName.match(/\<a.*?\>.*?\<\/a\>/) ? attachmentFile[x].data.attachmentName.match(/\>(.*?)\<\/a/)[1] : filePath[x].data.attachmentName,
                     fileUrl : attachmentFile[x].data.attachmentUrl
                 });
             }*/
            //console.log(formParams);
           // var totalParams=editJson+removeJson+','+Ext.JSON.encode(formParams);
            //var comParams = '"update":[{"data":totalParams}]';

           var comParams = '"update":[{"formData":'+Ext.JSON.encode(formParams)+',"editFile":['+editJson+'],"deleteFile":['+removeJson+']}]';


            console.log(comParams);
            //var comParams='"update":'+Ext.JSON.encode(formParams);
            //页面注册信息列表
            var contentPanel = Ext.getCmp('tranzvision-framework-content-panel'), grid;
            for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
                if (contentPanel.items.items[x].title == 'Bug管理') {
                    grid = Ext.getCmp(contentPanel.items.keys[x]);
                }
            }

            var store = grid.getStore();
            //修改记录
            var tzParams = '{"ComID":"LX_LZH_BUGMG_COM","PageID":"LX_BUG_LIST_STD","OperateType":"U","comParams":{' + comParams + '}}';
            var bugView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                bugView.close();
                store.reload();
            },"",true,this);
        }
    },
   /* baocunBug:function() {
        //var form = this.getView().child("form").getForm();
        var formParams = form.getForm().getValues();
        if (formParams['name'] == '' || formParams['inputDate'] == '' || formParams['inputOprID'] == '' || formParams['responsableOprID'] == ''
            || formParams['espectDate'] == '' || formParams['status'] == '') {
            Ext.Msg.alert('提示', '您输入的信息不完整');
        } else {
            var contentPanel = Ext.getCmp('tranzvision-framework-content-panel'), grid;
            for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
                if (contentPanel.items.items[x].title == 'Bug管理') {
                    grid = Ext.getCmp(contentPanel.items.keys[x]);
                }
            }
            var comParams=formParams;
            var tzParams = '{"ComID":"TZ_AQ_COMREG_COM","PageID":"TZ_AQ_COMREG_STD","OperateType":"U","comParams":{' + comParams + '}}';
            return tzParams;
        }
    },*/
    bugSave222: function(btn){
        //组件注册表单
        //var panel = btn.findParentByType('panel');
        var panel = btn.findParentByType('panel');
        var form = panel.child('form');
        if (form.isValid()) {
            //获取组件注册信息参数
            //var tzParams = this.baocunBug();
            var formParams = form.getForm().getValues();
            if (formParams['name'] == '' || formParams['inputDate'] == '' || formParams['inputOprID'] == '' || formParams['responsableOprID'] == ''
                || formParams['espectDate'] == '' || formParams['status'] == '') {
                Ext.Msg.alert('提示', '您输入的信息不完整');
            } else {
                var contentPanel = Ext.getCmp('tranzvision-framework-content-panel'), grid;
                for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
                    if (contentPanel.items.items[x].title == 'Bug管理') {
                        grid = Ext.getCmp(contentPanel.items.keys[x]);
                    }
                }
                var tzParams = '{"ComID":"TZ_AQ_COMREG_COM","PageID":"TZ_AQ_COMREG_STD","OperateType":"U","comParams":{' + Ext.JSON.encode(formParams) + '}}';
                var tzStoreParams = '{"comID":"'+formParams["comID"]+'"}';
                var pageGrid = this.getView().child("grid");
                Ext.tzSubmit(tzParams,function(){

                    form.findField("bugID").setReadOnly(true);
                    pageGrid.store.tzStoreParams = tzStoreParams;
                    pageGrid.store.reload();
                },"",true,this);
            }
            var bugView = this.getView();
            //页面注册信息列表
            var grid = bugView.child("grid");
            //页面注册信息数据
            var store = grid.getStore();
            Ext.tzSubmit(tzParams,function(responseData){
                // bugView.actType = "update";
                form.findField("bugID").setReadOnly(true);
                if(store.isLoaded()){
                    store.reload();
                }
            },"",true,this);
        }
    },
    bugEnsure222: function(btn){
        //组件注册表单
        var panel = btn.findParentByType('panel');
        var form = panel.child('form');
        //var panel = btn.findParentByType('panel');
        if (form.isValid()) {
            //获取组件注册信息参数
            //var tzParams = this.baocunBug();
            var formParams = form.getForm().getValues();
            if (formParams['name'] == '' || formParams['inputDate'] == '' || formParams['inputOprID'] == '' || formParams['responsableOprID'] == ''
                || formParams['espectDate'] == '' || formParams['status'] == '') {
                Ext.Msg.alert('提示', '您输入的信息不完整');
            } else {
                var contentPanel = Ext.getCmp('tranzvision-framework-content-panel'), grid;
                for (var x = contentPanel.items.items.length - 1; x >= 0; x--) {
                    if (contentPanel.items.items[x].title == 'Bug管理') {
                        grid = Ext.getCmp(contentPanel.items.keys[x]);
                    }
                }
                var comParams=formParams;
                var tzParams = '{"ComID":"TZ_AQ_COMREG_COM","PageID":"TZ_AQ_COMREG_STD","OperateType":"U","comParams":{' + comParams + '}}';
                return tzParams;
            }
            var bugView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                bugView.close();
            },"",true,this);
        }
    },
    onComRegSave: function(btn){
        //组件注册表单
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取组件注册信息参数
            var tzParams = this.getComRegInfoParams();
            var comView = this.getView();
            //页面注册信息列表
            var grid = comView.child("grid");
            //页面注册信息数据
            var store = grid.getStore();
            Ext.tzSubmit(tzParams,function(responseData){
                comView.actType = "update";
                form.findField("comID").setReadOnly(true);
                if(store.isLoaded()){
                    store.reload();
                }
            },"",true,this);
        }
    },
    onComRegEnsure: function(btn){
        //组件注册表单
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            //获取组件注册信息参数
            var tzParams = this.getComRegInfoParams();
            var comView = this.getView();
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                comView.close();
            },"",true,this);
        }
    },
    getComRegInfoParams: function(){
        //组件注册表单
        var form = this.getView().child("form").getForm();
        //组件信息标志
        //更新操作参数
        var comParams = form.getForm().getValues();
      //页面注册信息列表
        var grid = this.getView().child("grid");
        //页面注册信息数据
        var store = grid.getStore();
        //修改记录
        var tzParams = '{"ComID":"TZ_AQ_COMREG_COM","PageID":"TZ_AQ_COMREG_STD","OperateType":"U","comParams":{' + comParams + '}}';
        return tzParams;
    },
    bugCancel:function(btn){
        //关闭窗口
        this.getView().close();
    },
    searchBug:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'LX_LZH_BUGMG_COM.LX_BUG_LIST_STD.TZ_WY_BUG_VW',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams= seachCfg;
                store.load();
            }
        });
    },
    searchBug222:function(btn){
        var parBtn =btn;
        var status=Ext.create('Ext.data.Store',{

            fields:[{name:'condition',name:'conValue'}],
            data:[
                {condition:'新建',conValue:'0'},
                {condition:'已分配',conValue:'1'},
                {condition:'已修复',conValue:'2'},
                {condition:'成功关闭',conValue:'3'},
                {condition:'重新打开',conValue:'4'},
                {condition:'取消',conValue:'5'}
            ],
            autoLoad:true
        });
        var win= Ext.create('Ext.window.Window', {
            modal:true,
            title: '查询Bug' ,
            items: [
                new Ext.form.Panel({
                    xtype:'form',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    border: false,
                    width:450,
                    bodyPadding: 10,
                    height: 'auto',
                    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                    items:[
                        {
                            xtype: 'textfield',
                            fieldLabel: '编号',
                            name: 'bugID',
                            allowBlank:false
                        },{
                            xtype: 'textfield',
                            fieldLabel: '名称',
                            name: 'name',
                            allowBlank:false
                        },{
                            xtype: 'combo',
                            fieldLabel: '处理状态',
                            name: 'status',
                            emptyText: '请选择',
                            queryMode: 'local',
                            editable: false,
                             valueField: 'conValue',
                             displayField: 'condition',
                            store:status,
                            maxeHeight:150,
                            width:300
                        }],
                    buttons:[
                        {text:'搜索',handler: function (btn) {
            var window = btn.findParentByType("window");
            var form = window.child("form").getForm();
            var bugID = form.findField("bugID").getValue();
            var name = form.findField("name").getValue();
            var status = form.findField("status").getValue();
            if(status==null){
                status="";
            }
            var store = parBtn.findParentByType("grid").store;
             var tzParams = '{"ComID":"LX_LZH_BUGMG_COM","PageID":"LX_BUG_DETAIL_STD","OperateType":"QF","comParams":{"bugID":"'+BugID+'"}}';
            store.tzStoreParams='{"type":"queryBug","bugID":"'+bugID+'","name":"'+name+'","status":"'+status+'"}';
            store.load({
                callback:function(response){
                    window.close();
                }
            })
        }},
                        {text:'清空',handler:function(btn){
                            var window = btn.findParentByType("window");
                            var form = window.child("form").getForm();
                           form.reset();
                        }},
                        {text:'取消',handler:function(btn){
                            win.close();
                        }}]
                })]
        });
        win.show();
    },
    /*cancel:function(btn){
        var contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.remove(btn.findParentByType('panel'));
    },*/
    selectOprID: function(btn){
        var fieldName = btn.name;
        var searchDesc,oprID,oprName;
        if(fieldName=='inputOprID'){
            searchDesc="选择录入人";
            oprID="inputOprID";
            oprName="recOprName";
        }else if(fieldName=="responsableOprID"){
            searchDesc="选择责任人";
            oprID="responsableOprID";
            oprName="resOprName";
        }
        var form = this.getView().child("form").getForm();
        Ext.tzShowPromptSearch({
            recname: 'TZ_AQ_YHXX_TBL',
            searchDesc: searchDesc,
            maxRow:20,
            condition:{
                presetFields:{
//                    TZ_JG_ID:{
//                        value: Ext.tzOrgID,
//                        type: '01'
//                    }
                },
                srhConFields:{

                    OPRID:{
                        desc:'人员ID',
                        operator:'07',
                        type:'01'
                    },
                    TZ_REALNAME:{
                        desc:'人员姓名',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                OPRID: '成绩模型ID',
                TZ_REALNAME: '成绩模型描述'
            },
            multiselect: false,
            callback: function(selection){
                form.findField(oprID).setValue(selection[0].data.OPRID);
                form.findField(oprName).setValue(selection[0].data.TZ_REALNAME);
            }
        });
    },
    saveRemoveBug: function(btn){
        //组件注册信息列表
        var grid = btn.findParentByType("grid");
        //组件注册信息数据
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
        if(removeJson != ""){
            comParams = '"delete":[' + removeJson + "]";
        }
        //提交参数
        var tzParams = '{"ComID":"LX_LZH_BUGMG_COM","PageID":"LX_BUG_LIST_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },
    deleteArtAttenment: function(view, rowIndex){
        Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
                var store = view.findParentByType("grid").store;
                store.removeAt(rowIndex);
            }
        },this);
    },
    deleteArtAttenments: function(){
        //选中行
        var attachmentGrid = this.lookupReference('attachmentGrid');
        var selList = attachmentGrid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var store = attachmentGrid.store;
                    store.remove(selList);
                }
            },this);
        }
    }
}) ;
