Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.auditApplicationFormController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.auditApplicationForm',
    onAuditApplicationFormSave:function(btn){
        var panel = this.getView();
        var form = panel.child("form").getForm();
        var tabpanel = panel.child("tabpanel");
        if (form.isValid()) {
            var appFormStateOriginalValue = form.findField('appFormState').originalValue;
            var tzParams = this.getStuInfoParams(btn);
            if(tzParams!=""){
                Ext.tzSubmit(tzParams,function(responseData){
                    panel.gridRecord.set('submitState',form.findField('appFormState').getRawValue());
                    if(form.findField('appFormState').getValue()=='U'){
                        if(appFormStateOriginalValue!='U'){
                            panel.gridRecord.set('submitDate',Ext.Date.format(new Date(), 'Y-m-d'));
                        }
                    }else{
                        panel.gridRecord.set('submitDate',null);
                    };

                    panel.gridRecord.set('auditState',form.findField('auditState').getRawValue());
                    panel.gridRecord.set('colorType',form.findField('colorType').getValue());
                    panel.gridRecord.commit();

                    if(btn.name=='auditAppFormSaveBtn'){
                        var fileCheckStore = tabpanel.down('grid[name=fileCheckGrid]').getStore();
                        if(fileCheckStore.isLoaded()){
                            fileCheckStore.reload();
                        };
                        var refLetterStore = tabpanel.down('grid[name=refLetterGrid]').getStore();
                        if(refLetterStore.isLoaded()){
                            refLetterStore.reload();
                        };
                        var fileStore = tabpanel.down('grid[name=fileGrid]').getStore();
                        if(fileStore.isLoaded()){
                            fileStore.reload();
                        };
                    }
                    if(btn.name=='auditAppFormEnsureBtn'){
                        panel.close();
                    }

                },"",true,this);
            }
        }
    },
    onAuditApplicationFormClose:function(btn){
        this.getView().close();
    },
    getStuInfoParams: function(btn){
        //主要表单
        var form = this.getView().child('form').getForm();

        //备注信息
        var tabpanel = this.getView().child("tabpanel");
        var remarkForm = tabpanel.down('form[name=remarkForm]').getForm();
        form.findField('remark').setValue(remarkForm.findField('remark').getValue());
        //表单数据
        var formParams = form.getValues();

        //更新操作参数
        var comParams = "";

        //修改json字符串
        var  editJson = '{"typeFlag":"STU","data":'+Ext.JSON.encode(formParams)+'}';

        //报名人联系方式信息;
        var contactInfoForm = tabpanel.down('form[name=contactInfoForm]').getForm();
        var contactInfoParams = contactInfoForm.getValues();
        var appInsID = form.findField("appInsID").getValue();
        editJson = editJson+ ',{"typeFlag":"CONTACT","appInsID":"'+appInsID+'","data":'+Ext.JSON.encode(contactInfoParams)+'}';

        //更多信息表单数据;
        var moreInfoForm = tabpanel.down('form[name=moreInfoForm]').getForm();
        var moreInfoParams = moreInfoForm.getValues();
        var appInsID = form.findField("appInsID").getValue();
        editJson = editJson+ ',{"typeFlag":"MORE","appInsID":"'+appInsID+'","data":'+Ext.JSON.encode(moreInfoParams)+'}';

        /*考生提交资料列表修改数据*/
        var fileCheckGrid = tabpanel.down('grid[name=fileCheckGrid]');
        var fileCheckStore = fileCheckGrid.getStore();
        var fileCheckGridModifiedRecs = fileCheckStore.getModifiedRecords();
        for(var i=0;i<fileCheckGridModifiedRecs.length;i++){
            if(editJson == ""){
                editJson = '{"typeFlag":"FILE","data":'+Ext.JSON.encode(fileCheckGridModifiedRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"typeFlag":"FILE","data":'+Ext.JSON.encode(fileCheckGridModifiedRecs[i].data)+'}';
            }
        }

        /*推荐信列表修改数据*/
        var refLetterGrid = this.getView().down('grid[name=refLetterGrid]');
        var refLetterGridStore = refLetterGrid.getStore();
        var refLetterGridModifiedRecs = refLetterGridStore.getModifiedRecords();
        for(var j=0;j<refLetterGridModifiedRecs.length;j++){
            if(editJson == ""){
                editJson = '{"typeFlag":"REFLETTER","data":'+Ext.JSON.encode(refLetterGridModifiedRecs[j].data)+'}';
            }else{
                editJson = editJson + ',{"typeFlag":"REFLETTER","data":'+Ext.JSON.encode(refLetterGridModifiedRecs[j].data)+'}';
            }
        }

        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
        /*附件删除*/
        //删除json字符串
        var removeJson = "";
        var fileGrid = this.getView().down('grid[name=fileGrid]');
        var fileStore = fileGrid.getStore();
        //删除记录
        var removeRecs = fileStore.getRemovedRecords();
        //console.log(fileStore,removeRecs);
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
        /*附件删除end*/
        //提交参数
        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_AUDIT_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    //发送推荐信未完全提交提醒邮件
    sendRefLetterRemindEmail:function(btn){
        /*创建听众*/
        var form = this.getView().child('form').getForm();
        var oprID  = form.getValues()['oprID'];
        var appInsID  = form.getValues()['appInsID'];
        var params = {
            ComID:"TZ_BMGL_BMBSH_COM",
            PageID:"TZ_BMGL_YJDX_STD",
            OperateType:"U",
            comParams:{add:[{type:'TJX',oprID:oprID,appInsID:appInsID}]}
        };
        Ext.tzLoad(Ext.JSON.encode(params),function(audID){
            Ext.tzSendEmail({
                //发送的邮件模板;
                "EmailTmpName": ["TZ_TJX_CC_EML"],
                //创建的需要发送的听众ID;
                "audienceId": audID,
                //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "N"
            });
        });
    },
    setPassed:function(btn){
        var passed ="B";
        var grid = btn.findParentByType("grid");
        var records = grid.store.getRange();
        for(var i = 0;i<records.length;i++){
            records[i].set("auditState",passed);
        }
    },
    setFailed:function(btn){
        var failed ="C";
        var grid = btn.findParentByType("grid");
        var records = grid.store.getRange();
        for(var i = 0;i<records.length;i++){
            records[i].set("auditState",failed);
        }
    },
    setPending:function(btn){
        var pending ="A";
        var grid = btn.findParentByType("grid");
        var records = grid.store.getRange();
        for(var i = 0;i<records.length;i++){
            records[i].set("auditState",pending);
        }
    },
    setBackMsg:function(grid, rowIndex, colIndex){
        var rec = grid.getStore().getAt(rowIndex);

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_BKMSG_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var win = this.lookupReference('auditAppFormBackMsgWindow');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        var classID = rec.get('classID');
        var fileID = rec.get('fileID');
        win.classID=classID;
        win.fileID=fileID;
        win.rowIndex=rowIndex;
        win.auditApplicationFormPanel=grid.findParentByType("auditApplicationForm").auditApplicationFormPanel;
        //操作类型设置为更新
        var grid = win.child('grid');

        //加载数据
        var tzStoreParams = '{"classID":"'+classID+'","fileID":"'+fileID+'"}';
        grid.store.tzStoreParams = tzStoreParams;
        grid.store.load();
        win.show();

    },
    //常用回复短语，添加最后一行
    addLastBackMsg: function(btn){
        var win = this.lookupReference('auditAppFormBackMsgWindow');
        var classID = win.classID;
        var fileID = win.fileID;

        var profeGrid = btn.findParentByType("grid");
        var cellEditing = profeGrid.getPlugin('dataCellediting');
        var profeStore = profeGrid.getStore();
        var rowCount = profeStore.getCount();

        var model = new KitchenSink.view.enrollmentManagement.applicationForm.backMsgModel({
            classID: classID,
            fileID: fileID,
            msgID: '',
            msgContent:''
        });

        profeStore.insert(rowCount, model);
        cellEditing.startEditByPosition({
            row: rowCount,
            column: 0
        });
    },
    onBackMsgSave: function(btn){
        var win = this.lookupReference('auditAppFormBackMsgWindow');
        var grid = win.child("grid");
        var store = grid.getStore();

        var tzParams = this.getBackMsgParams(btn);
        var comView = this.getView();
        Ext.tzSubmit(tzParams,function(responseData){
            store.reload();
        },"",true,win);

    },
    onBackMsgClose: function(btn){
        var win = btn.findParentByType('window');
        win.close();
    },
    getBackMsgParams: function(btn){
        var win = btn.findParentByType('window');
        var grid = win.child('grid');
        var store = grid.getStore();
        //修改记录
        var mfRecs = store.getModifiedRecords();
        var editJson = "";
        var comParams = "";
        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                editJson = '{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"typeFlag":"PAGE","data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }
        if(editJson != ""){
            if(comParams == ""){
                comParams = '"update":[' + editJson + "]";
            }else{
                comParams = comParams + ',"update":[' + editJson + "]";
            }
        }
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

        /*更新报名表审核页面常用短语下拉框项：如果当前递交资料的常用短语下拉框没有加载（即auditApplicationFormPanel.auditFormFileCheckBackMsgJsonData[rowIndex]为空）则此处不更新*/
        var rowIndex= win.rowIndex;
        var auditApplicationFormPanel= win.auditApplicationFormPanel;
        if(comParams!=""&&auditApplicationFormPanel.auditFormFileCheckBackMsgJsonData[rowIndex]!=undefined){
            var classID = win.classID;
            var fileID = win.fileID;

            var arrayData = new Array();

            store.each(function(rec){
                arrayData.push({TZ_SBMINF_REP:rec.get("msgContent")});
            });
            auditApplicationFormPanel.auditFormFileCheckBackMsgJsonData[rowIndex] = arrayData;
        }

        //提交参数
        var tzParams=""
        tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_BKMSG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //if(comParams!=""){
        //    tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_BKMSG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        //}
        //alert(tzParams);
        return tzParams;
    },
    //考生报名表审核--流程公布结果编辑（LZ添加）
    bmlc_edit:function(grid, rowIndex, colIndex){
        var me = this;
        Ext.tzSetCompResourses("TZ_BMGL_LCJGPUB_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_LCJGPUB_COM"]["TZ_PER_LCJGPUB_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var win = this.lookupReference('perLcjgPubWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass({
                perLcjgCallBack:function(){
                    me.getView().down('grid[name=LcJgGrid]').store.reload();
                }
            });
            me.getView().add(win);
        }
        var record = grid.store.getAt(rowIndex);
        var classID = record.data.classID;
        var bmlc_id = record.data.bmlcID;
        var bmb_id = record.data.bmb_id;
        var form = win.child('form').getForm();
        var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
            recname: 'TZ_CLS_BMLCHF_T',
            condition:{
                TZ_CLASS_ID:{
                    value:classID,
                    operator:"01",
                    type:"01"
                },
                TZ_APPPRO_ID:{
                    value:bmlc_id,
                    operator:"01",
                    type:"01"
                }
            },
            result:'TZ_APPPRO_HF_BH,TZ_CLS_RESULT'
        });
        form.findField("jg_id").setStore(lm_mbStore);

        var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGPUB_STD","OperateType":"QF","comParams":{"bj_id":"'+classID+'","bmlc_id":"'+bmlc_id+'","bmb_id":"'+bmb_id+'"}}';
        Ext.tzLoad(tzParams,function(responseData){
            var formData = responseData.formData;
            form.setValues(formData);
        });

        win.show();
    },
    //查看前台公布内容
    checkFrontCon: function(){
        $('.viewFrontCon').click();
    },
    //常用回复短语删除一行
    deleteBackMsg: function(grid, rowIndex){
        var store = grid.getStore();
        Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.confirm","确认"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.deleteConfirm","您确定要删除所选记录吗?"), function(btnId){
            if(btnId == 'yes'){
                store.removeAt(rowIndex);
            }
        },this);
    },
    uploadRefLetter:function(grid, rowIndex){

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_UPTJX_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var form = this.getView().child("form").getForm();

        var win = this.lookupReference('uploadRefLetterWindow');

        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }

        var winForm = win.child("form").getForm();
        winForm.findField("currentRowIndex").setValue(rowIndex);
        var filePath = grid.getStore().getAt(rowIndex).get("refLetterPurl");
		var stuName = form.findField("stuName").getValue();
        winForm.findField("filePurl").setValue(filePath);
		winForm.findField("stuName").setValue(stuName);
        win.show();

    },
    deleteRefLetter:function(grid, rowIndex){

        var resRefLetterGridStore = grid.getStore();
        var resRefLetterGridRecs = resRefLetterGridStore.getAt(rowIndex);
        resRefLetterGridRecs.set("refLetterUserFile",'');
        resRefLetterGridRecs.set("refLetterSysFile",'');
        resRefLetterGridRecs.set("refLetterAurl",'');

    },
    viewRefLetter:function(grid, rowIndex)
    {
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }

        var form = this.getView().child("form").getForm();
        var appInsID = grid.store.getAt(rowIndex).get('refLetterAppInsId');
        var refLetterID  = grid.store.getAt(rowIndex).get('refLetterId');


        if(appInsID!=""&&refLetterID!=""&&appInsID!="0"&&refLetterID!="0"){
            var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appInsID+'","TZ_REF_LETTER_ID":"'+refLetterID+'","TZ_MANAGER":"Y"}}';
            var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+encodeURIComponent(tzParams);
            var win = new Ext.Window({
                title : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.viewRefLetter","查看推荐信"),
                maximized : true,
                width : Ext.getBody().width,
                height : Ext.getBody().height,
                autoScroll : true,
                border:false,
                bodyBorder : false,
                isTopContainer : true,
                modal : true,
                resizable : false,
                contentEl : Ext.DomHelper.append(document.body, {
                    bodyBorder : false,
                    tag : 'iframe',
                    style : "border:0px none;scrollbar:true",
                    src : viewUrl,
                    height : "100%",
                    width : "100%"
                }),
                buttons: [ {
                    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
                    iconCls:"close",
                    handler: function(){
                        win.close();
                    }
                }]
            });
            win.show();
        }else{
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.canNotFindRefLetter","找不到该推荐信"));
        }
    },
    onUploadRefLetterWindowEnsure: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");

        var form = win.child("form").getForm();

        /*获取页面数据*/

        var filename = form.findField("orguploadfile").getValue();

		var stuName = form.findField("stuName").getValue();

        if(filename != ""){

            var dateStr = Ext.Date.format(new Date(), 'Ymd');
            
            var upUrl = form.findField("filePurl").getValue();
            upUrl = TzUniversityContextPath + '/UpdServlet?filePath=enrollment';
            if(upUrl==""){
                Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.error","错误"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.wdyscfjdlj","未定义上传附件的路径，请与管理员联系"));
                return;
            }else{
				/*
                if(upUrl.length == (upUrl.lastIndexOf("/")+1)){
                    upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+dateStr;
                }else{
                    upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+"/"+dateStr;
                }
				*/
				upUrl = TzUniversityContextPath + '/UpdServlet?filePath=enrollment';
            }

            var myMask = new Ext.LoadMask({
                msg    : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.loading","加载中..."),
                target : Ext.getCmp('tranzvision-framework-content-panel')
            });

            myMask.show();

            form.submit({
                url: upUrl,
                success: function (form, action) {

                    var usefile  = action.result.msg.filename;
					var fix = usefile.substring(usefile.lastIndexOf(".") + 1,usefile.length);

                    var sysfile = action.result.msg.sysFileName;
                    
                    var accessPath = action.result.msg.accessPath;
					//var path = action.result.msg.path;
                    //当前点击行索引
                    var rowindex = form.findField("currentRowIndex").getValue();
                    var resRefLetterGrid = btn.findParentByType("auditApplicationForm").down('grid[name=refLetterGrid]');
                    var resRefLetterGridStore = resRefLetterGrid.getStore();
                    var resRefLetterGridRecs = resRefLetterGridStore.getAt(rowindex);
					var tjrName = resRefLetterGridRecs.get("refLetterPerName");
                    resRefLetterGridRecs.set("refLetterUserFile",stuName + "_Recommendation_" + tjrName + "." + fix);
                    resRefLetterGridRecs.set("refLetterSysFile",sysfile);
                    resRefLetterGridRecs.set("refLetterAurl",accessPath + sysfile);
					resRefLetterGridRecs.set("refLetterPurl",accessPath);
                    //重置表单
                    myMask.hide();
                    form.reset();
                },
                failure: function (form, action) {
                    myMask.hide();
                    Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.error","错误"), action.result.msg);
                }
            });

            win.close();
        }else
        {
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.error","错误"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.noFileSelected","未选择文件"));
            return;
        }
    },
    onUploadRefLetterWindowClose: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        win.close();
    },
    viewApplicationForm:function(btn){
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }

        var form = this.getView().child("form").getForm();
        var classID=form.findField("classID").getValue();
        var oprID=form.findField("oprID").getValue();
        var appInsID = form.findField("appInsID").getValue();

        if(classID!=""&&oprID!=""){
            var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appInsID+'"}}';
            var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+encodeURIComponent(tzParams);
            var win = new Ext.Window({
                title : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.viewApplicationForm","查看报名表"),
                maximized : true,
                width : Ext.getBody().width,
                height : Ext.getBody().height,
                autoScroll : true,
                border:false,
                bodyBorder : false,
                isTopContainer : true,
                modal : true,
                resizable : false,
                contentEl : Ext.DomHelper.append(document.body, {
                    bodyBorder : false,
                    tag : 'iframe',
                    style : "border:0px none;scrollbar:true",
                    src : viewUrl,
                    height : "100%",
                    width : "100%"
                }),
                buttons: [ {
                    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
                    iconCls:"close",
                    handler: function(){
                        win.close();
                    }
                }]
            });
            win.show();
        }else{
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.cantFindAppForm","找不到该报名人的报名表"));
        }
    },
    sendFilePassedEmail: function(btn){

        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_YJDX_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }

        var store = btn.findParentByType('grid').store;
        var modifiedRecs = store.getModifiedRecords();
        if(modifiedRecs.length>0){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.pleaseSaveData","请先保存列表数据之后再发送邮件"));
            return;
        };
        var passed = "B";

        var notAllPassed = store.findBy(function(record){
            if(record.get('auditState')!=passed){
                return true;
            }
        });
        if(notAllPassed>-1){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.canNotSendEmail","该考生资料没有全部通过，无法发送确认报名后续工作提醒邮件！"));
            return;
        };
        if(store.getCount()<1){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.noNeedToSendEmail","没有需要提交的资料，无需发送确认报名后续工作提醒邮件！"));
            return;
        };
        /*创建听众*/
        var form = this.getView().child('form').getForm();
        var oprID  = form.getValues()['oprID'];
        var appInsID  = form.getValues()['appInsID'];
        var params = {
            ComID:"TZ_BMGL_BMBSH_COM",
            PageID:"TZ_BMGL_YJDX_STD",
            OperateType:"U",
            comParams:{add:[{type:'DJZL',oprID:oprID,appInsID:appInsID}]}
        };
        Ext.tzLoad(Ext.JSON.encode(params),function(audID){
            Ext.tzSendEmail({
                //发送的邮件模板;
                "EmailTmpName": ["TZ_BMB_ZL_P_E"],
                //创建的需要发送的听众ID;
                "audienceId": audID,
                //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "N"
            });
        });
    },
    sendFileFailedEmail: function(btn){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_YJDX_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }

        var store = btn.findParentByType('grid').store;
        var modifiedRecs = store.getModifiedRecords();
        if(modifiedRecs.length>0){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.pleaseSaveData","请先保存列表数据之后再发送邮件"));
            return;
        };

        var failed = "C";

        var hadFailed = store.findBy(function(record){
            if(record.get('auditState')==failed){
                return true;
            }
        });
        if(hadFailed==-1){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.noNeedToSendEmail","该考生没有未通过的资料，无需发送需补充资料提醒邮件！"));
            return;
        }
        /*创建听众*/
        var form = this.getView().child('form').getForm();
        var oprID  = form.getValues()['oprID'];
        var appInsID  = form.getValues()['appInsID'];
        var params = {
            ComID:"TZ_BMGL_BMBSH_COM",
            PageID:"TZ_BMGL_YJDX_STD",
            OperateType:"U",
            comParams:{add:[{type:'DJZL',oprID:oprID,appInsID:appInsID}]}
        };
        Ext.tzLoad(Ext.JSON.encode(params),function(audID){
            Ext.tzSendEmail({
                //发送的邮件模板;
                "EmailTmpName": ["TZ_BMB_ZL_F_E"],
                //创建的需要发送的听众ID;
                "audienceId": audID,
                //是否有附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "N"
            });
        });
    },
    uploadFiles:function(btn){


        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_UPLOADFILES_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wjsl","未找到该功能页面对应的JS类，页面ID为：TZ_UPLOADFILES_STD，请检查配置。"));
            return;
        }


        var win = this.lookupReference('uploadFilesWindow');
        //var form = win.child("form").getForm();
        var form = this.getView().child("form").getForm();
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
        win.actType = "add";
        var winForm = win.child("form").getForm();
        //winForm.findField("currentRowIndex").setValue(rowIndex);
        //var filePath = grid.getStore().getAt(rowIndex).get("refLetterPurl");
        var appInsID = form.findField("appInsID").getValue();
        var stuName = form.findField("stuName").getValue();
        //winForm.findField("filePurl").setValue(filePath);
        //winForm.findField("FileName").setValue(filePath);
        winForm.findField("stuName").setValue(stuName);
        winForm.findField("appInsID").setValue(appInsID);
        win.show();

    },
    onUploadFilesWindowClose: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");
        win.close();
    },
    onUploadFilesWindowEnsure: function(btn){
        //获取窗口
        var win = btn.findParentByType("window");

        var form = win.child("form").getForm();

        /*获取页面数据*/
        //附件名称
        var FileName = form.findField("FileName").getValue();
        var strAppId = form.findField("appInsID").getValue();
        var stuName = form.findField("stuName").getValue();
        //文件名称
        //var refLetterFile = form.findField("refLetterFile").getValue();
        var refLetterFile = form.findField("orguploadfile").getValue();

        if(refLetterFile != ""){
            var dateStr = Ext.Date.format(new Date(), 'Ymd');

            //var upUrl = form.findField("filePurl").getValue();
            var upUrl = '/linkfile/FileUpLoad/appFormAttachment/';

            if(upUrl==""){
                Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.error","错误"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wdysclj","未定义上传附件的路径，请与管理员联系"));
                return;
            }else{
				/*
                if(upUrl.length == (upUrl.lastIndexOf("/")+1)){
                    upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+dateStr;
                }else{
                    upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+"/"+dateStr;
                }
				*/
				upUrl = TzUniversityContextPath + '/UpdServlet?filePath=enrollment';
            }

            var myMask = new Ext.LoadMask({
                msg    :  Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.loading","加载中..."),
                target : Ext.getCmp('tranzvision-framework-content-panel')
            });

            myMask.show();

            form.submit({
                url: upUrl,
                success: function (form, action) {
                    var usefile  = action.result.msg.filename;
                    var fix = usefile.substring(usefile.lastIndexOf(".") + 1,usefile.length);
                    var scFileName  =stuName +"_"+ FileName+"."+fix;
                    //2015827742845_1440675728045.png
                    var sysfile = action.result.msg.sysFileName;
                    ///linkfile/FileUpLoad/appFormAttachment/20150827
                    var accessPath = action.result.msg.accessPath;
                    ///export/home/PT852/webserv/ALTZDEV/applications/peoplesoft/PORTAL.war/linkfile/FileUpLoad/appFormAttachment/20150827
                    //var path = action.result.msg.path;
                    
                    
                    if (strAppId!=""){
                    	var tzparamsVar = {
                    		"ComID":"TZ_BMGL_BMBSH_COM",
                    		"PageID":"TZ_UPLOADFILES_STD",
                    		"OperateType":"U",
                    		"comParams":
                    			{ "add":
                    				[{
                    					"strAppId":strAppId,
                    					"stuName":stuName,
                    					"refLetterFile":scFileName,
                    					"FileName":FileName,
                    					"strSysFile":sysfile,
                    					"fileUrl":accessPath
                    				}]
                    			} 
                    	};
                    	var tzParams = Ext.util.JSON.encode(tzparamsVar);
                    	
                        //var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_UPLOADFILES_STD","OperateType":"U","comParams":{"add":[{"strAppId":"'+strAppId+'","stuName":"'+stuName+'","refLetterFile":"'+scFileName+'","FileName":"'+FileName+'","strSysFile":"'+sysfile+'","fileUrl":"'+accessPath+'"}]} }';
                        Ext.tzSubmit(tzParams,function(responseData){
                            //form.reset();
                            //var fileGrid =  btn.findParentByType('grid');
                            var fileGrid = btn.findParentByType("auditApplicationForm").down('grid[name=fileGrid]');
                            var fileStore = fileGrid.getStore();
                            fileStore.reload();
                            win.close();
                        },"",true,this);
                    }
                    myMask.hide();
                },
                failure: function (form, action) {
                    myMask.hide();
                    Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.error","错误"), action.result.msg);
                }
            });

        }else
        {
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.error","错误"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wxzwj","未选择文件"));
            return;
        }
    },
    deleteFiles: function(view,rowIndex){
        //var xxId = grid.getStore().getAt(rowIndex).get("TZ_XXX_BH");
        var xxId = view.findParentByType("grid").getStore().getAt(rowIndex).get("TZ_XXX_BH");
        if (xxId!="BMBFILE") {
            Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.confirm","确认"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.deleteConfirm","您确定要删除所选记录吗?"), function (btnId) {
                if (btnId == 'yes') {
                    var store = view.findParentByType("grid").store;
                    store.removeAt(rowIndex);
                }
            }, this);
        } else{
            Ext.Msg.alert("",Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.deleteerror","不能删除"));
        }
    }
});