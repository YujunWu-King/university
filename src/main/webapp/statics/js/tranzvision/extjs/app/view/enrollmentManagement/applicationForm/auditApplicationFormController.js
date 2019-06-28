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
                    	//取消显示材料及流程结果
                        /*var fileCheckStore = tabpanel.down('grid[name=fileCheckGrid]').getStore();
                        if(fileCheckStore.isLoaded()){
                            fileCheckStore.reload();
                        };*/
                        var refLetterStore = tabpanel.down('grid[name=refLetterGrid]').getStore();
                        if(refLetterStore.isLoaded()){
                            refLetterStore.reload();
                        };
                        var fileStore = tabpanel.down('grid[name=fileGrid]').getStore();
                        if(fileStore.isLoaded()){
                            fileStore.reload();
                        };
                        var fileStore = tabpanel.down('grid[name=fileGrid2]').getStore();
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
    //申请用户信息页面跳转过来
    onAuditApplicationFormSave2:function(btn){
        var panel = this.getView();
        var form = panel.child("form").getForm();
        var tabpanel = panel.child("tabpanel");
        if (form.isValid()) {
            var appFormStateOriginalValue = form.findField('appFormState').originalValue;
            var tzParams = this.getStuInfoParams2(btn);
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
                    	//取消显示材料及流程结果
                        /*var fileCheckStore = tabpanel.down('grid[name=fileCheckGrid]').getStore();
                        if(fileCheckStore.isLoaded()){
                            fileCheckStore.reload();
                        };*/
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
        /*var fileCheckGrid = tabpanel.down('grid[name=fileCheckGrid]');
        var fileCheckStore = fileCheckGrid.getStore();
        var fileCheckGridModifiedRecs = fileCheckStore.getModifiedRecords();
        for(var i=0;i<fileCheckGridModifiedRecs.length;i++){
            if(editJson == ""){
                editJson = '{"typeFlag":"FILE","data":'+Ext.JSON.encode(fileCheckGridModifiedRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"typeFlag":"FILE","data":'+Ext.JSON.encode(fileCheckGridModifiedRecs[i].data)+'}';
            }
        }*/

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
        
        /*复试资料删除*/
        //删除json字符串
        var removeJson2 = "";
        var fileGrid2 = this.getView().down('grid[name=fileGrid2]');
        var fileStore2 = fileGrid2.getStore();
        //删除记录
        var removeRecs2 = fileStore2.getRemovedRecords();
        //console.log(fileStore,removeRecs);
        for(var i=0;i<removeRecs2.length;i++){
            if(removeJson2 == ""){
                removeJson2 = Ext.JSON.encode(removeRecs2[i].data);
            }else{
                removeJson2 = removeJson2 + ','+Ext.JSON.encode(removeRecs2[i].data);
            }
        }
        if(removeJson2 != ""){
            if(comParams == ""){
                comParams = '"delete":[' + removeJson2 + "]";
            }else{
                comParams = comParams + ',"delete":[' + removeJson2 + "]";
            }
        }
        /*复试资料删除end*/
        //提交参数
        var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_AUDIT_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    getStuInfoParams2: function(btn){
        //主要表单
        var form = this.getView().child('form').getForm();
        
        //表单数据
        var formParams = form.getValues();

        //更新操作参数
        var comParams = "";

        //修改json字符串
        var  editJson = '{"typeFlag":"STU","data":'+Ext.JSON.encode(formParams)+'}';
    
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
            var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appInsID+'","isEdit":"Y"}}';
            var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+encodeURIComponent(tzParams);
            var win = new Ext.Window({
                title : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.viewApplicationForm","查看报名表"),
                maximized : true,
                width : Ext.getBody().width,
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
        winForm.findField("fileType").setValue(0);
        winForm.findField("FileName").setValue('');
        winForm.findField("FileName").setVisible(true);
        win.show();

    },
    uploadFiles2:function(btn){
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
        winForm.findField("fileType").setValue(1);
        winForm.findField("FileName").setValue(1);
        winForm.findField("FileName").setVisible(false);
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
        var fileType = form.findField("fileType").getValue();
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
                    
                    var PageID = "TZ_UPLOADFILES_STD";
                    if(fileType == 1){
                    	PageID = "TZ_UPLOADFSZL_STD";
                    	scFileName = usefile;
                    }
                    if (strAppId!=""){
                    	var tzparamsVar = {
                    		"ComID":"TZ_BMGL_BMBSH_COM",
                    		"PageID":PageID,
                    		"OperateType":"U",
                    		"comParams":
                    			{ "add":
                    				[{
                    					"strAppId":strAppId,
                    					"stuName":stuName,
                    					"refLetterFile":scFileName,
                    					"FileName":scFileName,
                    					"strSysFile":sysfile,
                    					"fileUrl":accessPath,
                    					"fileType":fileType
                    				}]
                    			} 
                    	};
                    	var tzParams = Ext.util.JSON.encode(tzparamsVar);
                    	
                        //var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_UPLOADFILES_STD","OperateType":"U","comParams":{"add":[{"strAppId":"'+strAppId+'","stuName":"'+stuName+'","refLetterFile":"'+scFileName+'","FileName":"'+FileName+'","strSysFile":"'+sysfile+'","fileUrl":"'+accessPath+'"}]} }';
                        Ext.tzSubmit(tzParams,function(responseData){
                            //form.reset();
                            //var fileGrid =  btn.findParentByType('grid');
                        	var fileGrid;
                        	if(fileType == 1){
                        		fileGrid = btn.findParentByType("auditApplicationForm").down('grid[name=fileGrid2]');
                        	}else{
                        		fileGrid = btn.findParentByType("auditApplicationForm").down('grid[name=fileGrid]');
                        	}
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
    },
    deleteFiles2: function(view,rowIndex){
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.confirm","确认"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.deleteConfirm","您确定要删除所选记录吗?"), function (btnId) {
			if (btnId == 'yes') {
				var store = view.findParentByType("grid").store;
				store.removeAt(rowIndex);
			}
		}, this);
    	
    },
	resetTjxPassword: function(grid,rowIndex){
	
		var form = this.getView().child("form").getForm();
        var appTjxInsID = grid.store.getAt(rowIndex).get('refLetterAppInsId');
        var refLetterID  = grid.store.getAt(rowIndex).get('refLetterId');
		var refLetterPwd = grid.store.getAt(rowIndex).get('refLetterPwd');

		 if (appTjxInsID!=""&&appTjxInsID!="0") {
			 var win = this.lookupReference('setTjxPasswordWindow');
			 if (!win) {
					className = 'KitchenSink.view.enrollmentManagement.applicationForm.setTjxPassword';
						Ext.syncRequire(className);
						ViewClass = Ext.ClassManager.get(className);
						//新建类
					win = new ViewClass();
					
					this.getView().add(win);
			  }
			  win.appTjxInsId = appTjxInsID;
			  win.show();
		 }else{
			Ext.Msg.alert("提示","当前推荐人未填写推荐信。");   
			return;
		 }
		 
	},
	onSetPwdClose: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		//重置密码信息表单
		var form = win.child("form").getForm();
		//重置表单
			form.reset();
		//关闭窗口
		win.close();
	},
	onSetPwdEnsure: function(btn){
		//获取窗口
		var win = btn.findParentByType("window");
		var appInsID = win.appTjxInsId;

		//重置密码信息表单
		if(appInsID!=""&&appInsID!="0"){
			var form = win.child("form").getForm();
			if (!form.isValid()) {//表单校验未通过
				return false;
			}
				
			//表单数据
			var formParams = form.getValues();
			//密码
			var password = formParams["password"];
			//密码参数
			var pwdParams = '"password":"'+password+'"';
			var appInsIdParams = '"appInsID":"'+appInsID+'"';
			//提交参数
			var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_AUDIT_STD","OperateType":"PWD","comParams":{'+pwdParams+","+appInsIdParams+'}}';
			form.reset();
			Ext.tzSubmit(tzParams,function(){
				//重置表单
				//form.reset();
				//关闭窗口
				win.close();						   
			},"重置密码成功",true,this);
		}else{
			Ext.Msg.alert("提示","重置密码失败。");   
			return;
		}
	},
	//查看招生线索
    clueDetailForm:function(btn){
  
    	var form = this.getView().child("form").getForm();
    	var clueID = form.findField('clueID').getValue(); 
        this.editClueInfoByID(clueID);
    },
    //根据线索ID进行编辑
    editClueInfoByID:function(clueId) {
        Ext.tzSetCompResourses("TZ_XSXS_INFO_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_XSXS_DETAIL_STD"];
        if (pageResSet == "" || pageResSet == undefined) {
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if (className == "" || className == undefined) {
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_XSXS_DETAIL_STD，请检查配置。');
            return;
        }

        var contentPanel, cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        if (!Ext.ClassManager.isCreated(className)) {
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);

        //获取线索信息中的下拉框值
        var backReasonId, backReasonName, closeReasonId, closeReasonName, colorTypeId, colorTypeName, colorTypeCode;
        var backReasonFlag, closeReasonFlag, colorTypeFlag;
        var i, j, m, n;
        var backReasonData = [],
            closeReasonData = [],
            colorSortData = [],
            customerNameData = [],
            companyNameData = [];
        var tzDropDownParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"tzGetDropDownInfo","comParams":{"clueId":"' + clueId + '"}}';
        Ext.tzLoad(tzDropDownParams, function (respData) {
            backReasonId = respData.backReasonId;
            backReasonName = respData.backReasonName;
            closeReasonId = respData.closeReasonId;
            closeReasonName = respData.closeReasonName;
            colorTypeId = respData.colorTypeId;
            colorTypeName = respData.colorTypeName;
            colorTypeCode = respData.colorTypeCode;


            var myMask = new Ext.LoadMask(
                {
                    msg    : TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00022"),
                    target : Ext.getCmp('tranzvision-framework-content-panel')
                });

            myMask.show();


            //退回原因
            var validBackReasonStore = new KitchenSink.view.common.store.comboxStore({
                recname: 'TZ_XS_THYY_V',
                condition: {
                    TZ_JG_ID: {
                        value: Ext.tzOrgID,
                        operator: '01',
                        type: '01'
                    }
                },
                result: 'TZ_THYY_ID,TZ_LABEL_NAME',
                listeners: {
                    load: function (store, records, successful, eOpts) {
                        for (i = 0; i < records.length; i++) {
                            backReasonData.push(records[i].data);
                            if (backReasonId.length == 0 || records[i].data["TZ_THYY_ID"] == backReasonId) {
                                backReasonFlag = "Y";
                            }
                        }
                        if (backReasonFlag == "Y") {

                        } else {
                            backReasonData.push({
                                TZ_THYY_ID: backReasonId,
                                TZ_LABEL_NAME: backReasonName
                            });
                        }

                        //关闭原因
                        var validCloseReasonStore = new KitchenSink.view.common.store.comboxStore({
                            recname: 'TZ_XS_GBYY_V',
                            condition: {
                                TZ_JG_ID: {
                                    value: Ext.tzOrgID,
                                    operator: '01',
                                    type: '01'
                                }
                            },
                            result: 'TZ_GBYY_ID,TZ_LABEL_NAME',
                            listeners: {
                                load: function (store, records, successful, eOpts) {
                                    for (j = 0; j < records.length; j++) {
                                        closeReasonData.push(records[j].data);
                                        if (closeReasonId.length == 0 || records[j].data["TZ_GBYY_ID"] == closeReasonId) {
                                            closeReasonFlag = "Y";
                                        }
                                    }
                                    if (closeReasonFlag == "Y") {

                                    } else {
                                        closeReasonData.push({
                                            TZ_GBYY_ID: closeReasonId,
                                            TZ_LABEL_NAME: closeReasonName
                                        });
                                    }

                                    //类别
                                    var validColorSortStore = new KitchenSink.view.common.store.comboxStore({
                                        recname: 'TZ_XS_XSLB_V',
                                        condition: {
                                            TZ_JG_ID: {
                                                value: Ext.tzOrgID,
                                                operator: '01',
                                                type: '01'
                                            }
                                        },
                                        result: 'TZ_COLOUR_SORT_ID,TZ_COLOUR_NAME,TZ_COLOUR_CODE',
                                        listeners: {
                                            load: function (store, records, successful, eOpts) {
                                                for (k = 0; k < records.length; k++) {
                                                    colorSortData.push(records[k].data);
                                                    if (colorTypeId.length == 0 || records[k].data["TZ_COLOUR_SORT_ID"] == colorTypeId) {
                                                        colorTypeFlag = "Y";
                                                    }
                                                }
                                                if (colorTypeFlag == "Y") {

                                                } else {
                                                    colorSortData.push({
                                                        TZ_COLOUR_SORT_ID: colorTypeId,
                                                        TZ_COLOUR_NAME: colorTypeName,
                                                        TZ_COLOUR_CODE: colorTypeCode
                                                    });
                                                }


                                                //姓名
                                                var customerNameStore = new KitchenSink.view.common.store.comboxStore({
                                                    pageSize: 0,
                                                    recname: 'TZ_XS_CUSNM_V',
                                                    condition: {
                                                        TZ_JG_ID: {
                                                            value: Ext.tzOrgID,
                                                            operator: '01',
                                                            type: '01'
                                                        }
                                                    },
                                                    result: 'TZ_KH_OPRID,TZ_REALNAME,TZ_DESCR_254',
                                                    listeners: {
                                                        load: function (store, records, successful, eOpts) {
                                                            for (m = 0; m < records.length; m++) {
                                                                customerNameData.push(records[m].data);
                                                            }


                                                            //公司
                                                            var companyNameStore = new KitchenSink.view.common.store.comboxStore({
                                                                pageSize: 0,
                                                                recname: 'TZ_XS_COMNM_V',
                                                                condition: {
                                                                    TZ_JG_ID: {
                                                                        value: Ext.tzOrgID,
                                                                        operator: '01',
                                                                        type: '01'
                                                                    }
                                                                },
                                                                result: 'TZ_COMP_CNAME',
                                                                listeners: {
                                                                    load: function (store, records, successful, eOpts) {
                                                                        for (n = 0; n < records.length; n++) {
                                                                            companyNameData.push(records[n].data);
                                                                        }

                                                                        //线索标签
                                                                        var clueTagStore= new KitchenSink.view.common.store.comboxStore({
                                                                            recname:'TZ_LABEL_DFN_T',
                                                                            condition:{
                                                                            	TZ_JG_ID:{
                                                                                    value: Ext.tzOrgID,
                                                                                    operator:'01',
                                                                                    type:'01'
                                                                                },
                                                                                TZ_LABEL_STATUS:{
                                                                                    value: 'Y',
                                                                                    operator:'01',
                                                                                    type:'01'
                                                                                }
                                                                            },
                                                                            result:'TZ_LABEL_ID,TZ_LABEL_NAME'
                                                                        });
                                                                        clueTagStore.load({
                                                                        	callback: function(){
                                                                        		
                                                                        		var otherZrrStore= new KitchenSink.view.common.store.comboxStore({
                                                                                    recname:'TZ_XS_QTZRR_V',
                                                                                    condition:{
                                                                                    	TZ_LEAD_ID:{
                                                                                            value: clueId,
                                                                                            operator:'01',
                                                                                            type:'01'
                                                                                        }
                                                                                    },
                                                                                    result:'TZ_ZRR_OPRID,TZ_REALNAME'
                                                                                });
                                                                        		otherZrrStore.load({
                                                                                	callback: function(){
                                                                                		myMask.hide();

                                                                                        cmp = new ViewClass({
                                                                                            clueID: clueId,
                                                                                            backReasonData: backReasonData,
                                                                                            closeReasonData: closeReasonData,
                                                                                            colorSortData: colorSortData,
                                                                                            customerNameData: customerNameData,
                                                                                            companyNameData: companyNameData,
                                                                                            clueTagStore: clueTagStore,
                                                                                            otherZrrStore: otherZrrStore,
                                                                                            zrrEditFalg: 'Y'
                                                                                        });

                                                                                        cmp.on('afterrender', function (panel) {
                                                                                            var form = panel.child('form').getForm();
                                                                                            var tabpanel = panel.child('tabpanel');
                                                                                            var store = tabpanel.child("grid").store;
                                                                                            var glBmbBut = panel.down("button[name=glBmbBut]");
                                                                                            //参数
                                                                                            var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_XSXS_DETAIL_STD","OperateType":"QF","comParams":{"clueId":"' + clueId + '"}}';
                                                                                            Ext.tzLoad(tzParams, function (respData) {
                                                                                                var formData = respData.formData;
                                                                                                //基本信息
                                                                                                form.setValues(formData);

                                                                                                //根据显示状态显示相应的其他字段
                                                                                                var clueState = form.findField('clueState').getValue();

                                                                                                form.findField('backReasonId').setVisible(false);
                                                                                                form.findField('closeReasonId').setVisible(false);
                                                                                                form.findField('contactDate').setVisible(false);
                                                                                                form.findField('backReasonId').allowBlank = true;
                                                                                                form.findField('closeReasonId').allowBlank = true;
                                                                                                form.findField('contactDate').allowBlank = true;

                                                                                                //退回原因
                                                                                                if (clueState == "F") {
                                                                                                    form.findField('backReasonId').setVisible(true);
                                                                                                    form.findField('backReasonId').allowBlank = false;
                                                                                                }
                                                                                                //关闭原因
                                                                                                if (clueState == "G") {
                                                                                                    form.findField('closeReasonId').setVisible(true);
                                                                                                    form.findField('closeReasonId').allowBlank = false;
                                                                                                }
                                                                                                //建议跟进日期
                                                                                                if (clueState == "D") {
                                                                                                    form.findField('contactDate').setVisible(true);
                                                                                                    form.findField('contactDate').allowBlank = false;
                                                                                                }


                                                                                                //线索状态为关闭或者报考状态不是未报名，关联报名表按钮隐藏
                                                                                                var bkStatus = form.findField("bkStatus").getValue();
                                                                                                if (clueState == "G" || bkStatus != "A") {
                                                                                                    glBmbBut.setVisible(false);
                                                                                                }

                                                                                                //加载报名表信息
                                                                                                var clueId = form.findField("clueId").getValue();
                                                                                                store.tzStoreParams = '{"clueId":"' + clueId + '"}';
                                                                                                store.load();
                                                                                            });
                                                                                        });

                                                                                        var tab = contentPanel.add(cmp);
                                                                                        contentPanel.setActiveTab(tab);
                                                                                        Ext.resumeLayouts(true);
                                                                                        if (cmp.floating) {
                                                                                            cmp.show();
                                                                                        }
                                                                                	}
                                                                                });
                                                                        	}
                                                                        });
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        });
    }
});