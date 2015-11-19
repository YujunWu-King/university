Ext.define('KitchenSink.view.interviewManagement.interviewManage.interviewMgrController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.interviewMgrController',
    //考生面试情况一览
    msArrPreview:function(btn){
        //是否有访问权限
        Ext.tzSetCompResourses("TZ_MS_ARR_MG_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_ARR_MG_COM"]["TZ_MS_ARR_PRE_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_ARR_PRE_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

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
        var panelform = btn.up('grid').up('panel').child('form').getForm();

        var panelformrec = panelform.getFieldValues();
        var classID = panelformrec["classID"];
        //alert(classID);

        cmp = new ViewClass();
        cmp.on('afterrender',function(panel){
            var prePanelGrid = panel.child('grid');
            var tzParams = '{"classID":"'+classID+'"}';
            prePanelGrid.store.tzStoreParams = tzParams;
            prePanelGrid.store.load();
        });

        tab = contentPanel.add(cmp);

        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);

        if (cmp.floating) {
            cmp.show();
        }
    },
    //查询----通过列filter实现
    /*
    searchComList: function(btn){     //searchComList为各自搜索按钮的handler event;
        //alert("---");
        var clsformrec = this.getView().down('form').getForm().getFieldValues();
        var classID = clsformrec["classID"];
        var batchID = clsformrec["batchID"];
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_MS_MGR_COM.TZ_MS_IVWMGR_STD.TZ_MS_ITWMGRL_V',
            condition:
            {
                "TZ_CLASS_ID": classID,          //设置搜索字段的默认值，没有可以不设置condition;
                "TZ_BATCH_ID": batchID
            },
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    */

    //清空过滤条件
    onClearFilters:function(btn){
        btn.findParentByType('grid').filters.clearFilters();
    },

    //发送邮件
    sendEmail:function(){

        var clsformrec = this.getView().down('form').getForm().getFieldValues();
        var classID = clsformrec["classID"];
        var batchID = clsformrec["batchID"];

        //面试信息列表
        var stuListGrid = this.getView().down("grid");

        //面试信息选中行数据
        var stuListRecs = stuListGrid.getSelectionModel().getSelection();

        //提交参数
        var comParams="";

        //编辑JSON
        var editJson="";

        if(stuListRecs.length==0){
            var config = {
                title: "提示",
                msg: "请选择需要发送邮件的记录！",
                width: 300,
                closable: true,
                buttons: Ext.MessageBox.OK
                //icon:Ext.MessageBox.INFO
                //fn: function (btn, txt) {
                //
                //}
            };
            Ext.MessageBox.show(config);
            return;
        }else{
            for(var i=0;i<stuListRecs.length;i++){
                if(i>0){
                    if(stuListRecs[i].data.msResult!=stuListRecs[i-1].data.msResult){
                        Ext.MessageBox.show({
                            title: "提示",
                            msg: "所选记录的面试结果不一致，请检查！",
                            width: 300,
                            closable: true,
                            buttons: Ext.MessageBox.OK
                            //icon:Ext.MessageBox.INFO
                        });
                        return;
                    }
                }

                if(editJson == ""){
                    editJson = '{"data":'+Ext.JSON.encode(stuListRecs[i].data)+'}';
                }else{
                    editJson = editJson + ',{"data":'+Ext.JSON.encode(stuListRecs[i].data)+'}';
                }
            }

            if(editJson != "") {
                comParams = '"selected":[' + editJson + "]";
            }
        }

        var tzParams = '{"ComID":"TZ_MS_MGR_COM","PageID":"TZ_MS_IVWMGR_STD","OperateType":"getEmailInfo","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'",'+comParams+'}}';

        Ext.tzLoad(tzParams,function(responseData){
            //alert(responseData['EmailTmpName']);
            var emailTmpName = responseData['EmailTmpName'];
            var arrEMLTmpls = new Array();
            arrEMLTmpls=emailTmpName.split(",");

            var audienceId = responseData['audienceId'];

            Ext.tzSendEmail({
                //发送的邮件模板;
                "EmailTmpName":arrEMLTmpls,
                //创建的需要发送的听众ID;
                "audienceId": audienceId,
                //是否可以发送附件: Y 表示可以发送附件,"N"表示无附件;
                "file": "Y"
            });
        });
    },

    //发布面试结果--单元格点击事件发布
    /*
    pubItwRes:function(grid, rowIndex, colIndex){
        alert("---");
        var strColIndex = (colIndex-2)+"";
        //alert(grid.store.getAt(rowIndex).get(strColIndex));
        var classId = grid.store.getAt(rowIndex).data.classId;
        var appId = grid.store.getAt(rowIndex).data.appId;
        var bmlcId = grid.store.getAt(rowIndex).get(strColIndex);
        var me = this;
        Ext.tzSetCompResourses("TZ_BMGL_LCJGPUB_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_LCJGPUB_COM"]["TZ_PER_LCJGPUB_STD"];

        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，请检查配置。');
            return;
        }

        var win = this.lookupReference('perLcjgPubWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass({
                //perLcjgCallBack:me.testLqlc2
                perLcjgCallBack:function(){
                    //Ext.MessageBox.alert('测试',"这是一个回调函数");
                    //me.msResPubGridRefresh(grid);
                    grid.store.reload();
                }
            });
            var record = grid.store.getAt(rowIndex);
            //alert(record+"__"+classId+"__"+bmlcId+"__"+appId);
            var classID = classId;
            var bmlc_id = bmlcId;
            var bmb_id = appId;
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
            win.on('afterrender',function(panel){
                var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGPUB_STD","OperateType":"QF","comParams":{"bj_id":"'+classID+'","bmlc_id":"'+bmlc_id+'","bmb_id":"'+bmb_id+'"}}';
                //"callback"'+this.testLqlc2()+'"
                Ext.tzLoad(tzParams,function(responseData){
                    var formData = responseData.formData;
                    form.setValues(formData);
                });
            });
            this.getView().add(win);
        }
        win.show();
    },
    //回调刷新grid.store
    msResPubGridRefresh:function(grid){
        //Ext.MessageBox.alert('测试',"exe");
        grid.store.reload();
    },
    */

    //批量发布面试结果
    pubItwResPl:function(btn){
        var me = this;
        var msResGrid = btn.up('grid');
        var selList = msResGrid.getSelectionModel().getSelection();
        Ext.tzSetCompResourses("TZ_BMGL_LCJGPUB_COM");
        //var selList = this.getView().getSelectionModel().getSelection();
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请至少选择一条要修改的记录");
            return;
        }
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_LCJGPUB_COM"]["TZ_PER_LCJGP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，请检查配置。');
            return;
        }
        var _bmb_Id="";
        for(var i=0;i<selList.length;i++){
            if(_bmb_Id == ""){
                _bmb_Id = selList[i].get('appId');
            }else{
                _bmb_Id = _bmb_Id + ','+selList[i].get('appId');
            }
        }
        var _bj_id=selList[0].get('classId');
        var _bmlc_id="";
        var win = this.lookupReference('perLcjgPubWindowP');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass({
                perLcjgCallBack:function(){
                    //Ext.MessageBox.alert('测试',"这是一个回调函数");
                    msResGrid.store.reload();
                }
            });

            var form = win.child('form').getForm();
         /*   var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
                recname: 'TZ_CLS_BMLCHF_T',
                condition:{
                    TZ_CLASS_ID:{
                        value:_bj_id,
                        operator:"01",
                        type:"01"
                    },
                    TZ_APPPRO_ID:{
                        value:_bmlc_id,
                        operator:"01",
                        type:"01"
                    }
                },
                result:'TZ_APPPRO_HF_BH,TZ_CLS_RESULT'
            });
            form.findField("jg_id").setStore(lm_mbStore);*/
            /*流程下拉框*/
            var lcStore = new KitchenSink.view.common.store.comboxStore({
                recname: 'TZ_CLS_BMLC_T',
                condition:{
                    TZ_CLASS_ID:{
                        value:_bj_id,
                        operator:"01",
                        type:"01"
                    }
                },
                result:'TZ_APPPRO_ID,TZ_APPPRO_NAME'
            });
            form.findField("ry_lc").setStore(lcStore);

            win.on('afterrender',function(panel){
                var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGP_STD","OperateType":"QF","comParams":{"bj_id":"'+_bj_id+'","bmlc_id":"'+_bmlc_id+'","bmb_id":"'+_bmb_Id+'"}}';
                Ext.tzLoad(tzParams,function(responseData){
                    var formData = responseData.formData;
                    form.setValues(formData);
                });
            });
            this.getView().add(win);
        }
        win.show();
    },

    //导入面试结果集成调用
    impItwRes:function(btn){
        //alert("import");
        var clsformrec = this.getView().down('form').getForm().getFieldValues();
        var classID = clsformrec["classID"];
        var batchID = clsformrec["batchID"];

        Ext.tzImport({
            /*importType 导入类型：A-传Excel；B-粘贴Excel数据*/
            importType : 'B',
			/*导入模版编号*/
			tplResId:'TZ_GD_MSRES_IMP',
            /*businessHandler  预览导入的数据之后点击下一步执行的函数，根据业务的需求自由编写，columnArray为解析Excel后的标题行数组（如果未勾选首行是标题行columnArray=[]）
             * dataArray为解析后的Excel二维数组数据（勾选了首行是标题行则dataArray不包含首行数据；）
             */
            businessHandler : function(columnArray,dataArray){
                //是否有访问权限
                var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_MS_MGR_COM"]["TZ_MS_MSRESIMP_STD"];
                if( pageResSet == "" || pageResSet == undefined){
                    Ext.MessageBox.alert('提示', '您没有修改数据的权限');
                    return;
                }

                //该功能对应的JS类
                var className = pageResSet["jsClassName"];
                if(className == "" || className == undefined){
                    Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_MS_MSRESIMP_STD，请检查配置。');
                    return;
                }

                var win = this.lookupReference('msResImpWin');

                if (!win) {
                    //className = 'KitchenSink.view.activity.applyOptionsWindow';
                    Ext.syncRequire(className);
                    ViewClass = Ext.ClassManager.get(className);
                    //新建类
                    win = new ViewClass();
                    //this.getView().add(win);
                }

                var windowgrid=win.child('grid');

                if(dataArray.length>0){
                    for(var i=0;i<dataArray.length;i++){
                        dataArray[i][5]="-1";
                    }
                }
                windowgrid.store.loadData(dataArray);

                var winform = win.child("form").getForm();
                var strtmp = "共 ";
                strtmp = strtmp+"<span style='color:red;font-size: 22px'>"+dataArray.length+"</span>";
                strtmp = strtmp+" 条数据";
                var formpara={classID:classID,batchID:batchID,ImpRecsCount:strtmp};
                winform.setValues(formpara);
                win.show();
            }
        });
    },

    //面试结果导出
    msResExport:function(btn){
        //alert("----");
        var grid = btn.findParentByType('grid');
        var gridColumns=grid.columns;
        var exportGridColumns=new Array();
        var exportGridStoreFields=new Array();
        var arr1Rec;
        var arr2Recs=new Array();
        for(var i=0;i<gridColumns.length;i++){
            if(gridColumns[i].dataIndex==""||gridColumns[i].dataIndex==null){

            }else{
                if(gridColumns[i].dataIndex.indexOf("#")==-1){
                    exportGridStoreFields.push(gridColumns[i].dataIndex);

                    exportGridColumns.push({
                        text:gridColumns[i].text,
                        dataIndex: gridColumns[i].dataIndex
                    });
                }
            };
        };
        var index=-1;
        //是否参加
        var msResExpJoinStateStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_JOIN_STATE");
        //msResExpJoinStateStore.load();
        //面试状态
        var msResExpResultStore  = new KitchenSink.view.common.store.appTransStore("TZ_MS_RESULT");
        //msResExpResultStore.load();
        //预约状态
        var msResExpArrStateStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_ARR_STATE");
        //msResExpArrStateStore.load();
        //确认状态
        var msResExpConfStateStore = new KitchenSink.view.common.store.appTransStore("TZ_MS_CONF_STA");
        //msResExpConfStateStore.load();
        //类别
        var orgColorSortStore = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_ORG_COLOR_V',
            condition:{
                TZ_JG_ID:{
                    value:Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                }},
            result:'TZ_COLOR_SORT_ID,TZ_COLOR_NAME,TZ_COLOR_CODE'
        });

        var arrMsJoinState=new Array();
        var arrMsResult=new Array();
        var arrMsArrState=new Array();
        var arrMsConfState=new Array();
        var arrColorSort=new Array();

        var jsonTemp;
        var strTemp1="";
        var strTemp2="";
        var gridRecs = grid.store.getRange();

        msResExpJoinStateStore.load({
            callback: function(records, options, success){
                for(var i=0;i<records.length;i++){
                    //alert(records[i].data.TValue+"--"+records[i].data.TSDesc);
                    strTemp1=records[i].data.TValue;
                    strTemp2=records[i].data.TSDesc;
                    jsonTemp={value:strTemp1,desc:strTemp2};
                    arrMsJoinState.push(jsonTemp);
                    //alert(arrMsJoinState[i]["value"]+"__"+arrMsJoinState[i]["desc"]);
                }

                msResExpResultStore.load({
                    callback: function(records, options, success){
                        for(var i=0;i<records.length;i++){
                            strTemp1=records[i].data.TValue;
                            strTemp2=records[i].data.TSDesc;
                            jsonTemp={value:strTemp1,desc:strTemp2};
                            arrMsResult.push(jsonTemp);
                            //alert(arrMsResult[i]["value"]+"__"+arrMsResult[i]["desc"]);
                        }

                        msResExpArrStateStore.load({
                            callback: function(records, options, success){
                                for(var i=0;i<records.length;i++){
                                    strTemp1=records[i].data.TValue;
                                    strTemp2=records[i].data.TSDesc;
                                    jsonTemp={value:strTemp1,desc:strTemp2};
                                    arrMsArrState.push(jsonTemp);
                                    //alert(arrMsArrState[i]["value"]+"__"+arrMsArrState[i]["desc"]);
                                }

                                msResExpConfStateStore.load({
                                    callback: function(records, options, success){
                                        for(var i=0;i<records.length;i++){
                                            strTemp1=records[i].data.TValue;
                                            strTemp2=records[i].data.TSDesc;
                                            jsonTemp={value:strTemp1,desc:strTemp2};
                                            arrMsConfState.push(jsonTemp);
                                            //alert(arrMsConfState[i]["value"]+"__"+arrMsConfState[i]["desc"]);
                                        }
                                        orgColorSortStore.load({
                                            callback: function(records, options, success){
                                                for(var i=0;i<records.length;i++){
                                                    strTemp1=records[i].data.TZ_COLOR_SORT_ID;
                                                    strTemp2=records[i].data.TZ_COLOR_NAME;
                                                    jsonTemp={value:strTemp1,desc:strTemp2};
                                                    arrColorSort.push(jsonTemp);
                                                    //alert(arrMsConfState[i]["value"]+"__"+arrMsConfState[i]["desc"]);
                                                };

                                                for(var i=0;i<gridRecs.length;i++){
                                                    //alert(gridRecs[0].data[exportGridColumns[i].dataIndex]+"--"+i+"--"+gridColumns.length);
                                                    arr1Rec=new Array();
                                                    for(var j=0;j<exportGridColumns.length;j++){
                                                        //if(j<13){
                                                            switch(exportGridColumns[j].dataIndex)
                                                            {
                                                                case "msDate":
                                                                    arr1Rec[j]=Ext.util.Format.date(gridRecs[i].data[exportGridColumns[j].dataIndex], 'Y-m-d');
                                                                    break;
                                                                case "msArrState":
                                                                    for(var k=0;k<arrMsArrState.length;k++){
                                                                        //arrMsJoinState[0]["value"]+"__"+arrMsJoinState[0]["desc"]
                                                                        if (gridRecs[i].data[exportGridColumns[j].dataIndex]==arrMsArrState[k]["value"]) {
                                                                            arr1Rec[j]=arrMsArrState[k]["desc"];
                                                                            break;
                                                                        }else{
                                                                            arr1Rec[j]="";
                                                                        };
                                                                    };
                                                                    break;
                                                                case "msConfigState":
                                                                    for(var m=0;m<arrMsConfState.length;m++){
                                                                        //arrMsJoinState[0]["value"]+"__"+arrMsJoinState[0]["desc"]
                                                                        if (gridRecs[i].data[exportGridColumns[j].dataIndex]==arrMsConfState[m]["value"]) {
                                                                            arr1Rec[j]=arrMsConfState[m]["desc"];
                                                                            break;
                                                                        }else{
                                                                            arr1Rec[j]="";
                                                                        };
                                                                    };
                                                                    break;
                                                                case "msJoinState":
                                                                    for(var n=0;n<arrMsJoinState.length;n++){
                                                                        //arrMsJoinState[0]["value"]+"__"+arrMsJoinState[0]["desc"]
                                                                        if (gridRecs[i].data[exportGridColumns[j].dataIndex]==arrMsJoinState[n]["value"]) {
                                                                            arr1Rec[j]=arrMsJoinState[n]["desc"];
                                                                            break;
                                                                        }else{
                                                                            arr1Rec[j]="";
                                                                        };
                                                                    };
                                                                    break;
                                                                case "msResult":
                                                                    for(var k=0;k<arrMsResult.length;k++){
                                                                        //arrMsJoinState[0]["value"]+"__"+arrMsJoinState[0]["desc"]
                                                                        if (gridRecs[i].data[exportGridColumns[j].dataIndex]==arrMsResult[k]["value"]) {
                                                                            arr1Rec[j]=arrMsResult[k]["desc"];
                                                                            break;
                                                                        }else{
                                                                            arr1Rec[j]="";
                                                                        };
                                                                    };
                                                                    break;
                                                                case "sort":
                                                                    for(var l=0;l<arrColorSort.length;l++){
                                                                        if (gridRecs[i].data[exportGridColumns[j].dataIndex]==arrColorSort[l]["value"]) {
                                                                            arr1Rec[j]=arrColorSort[l]["desc"];
                                                                            break;
                                                                        }else{
                                                                            arr1Rec[j]="";
                                                                        };
                                                                    };
                                                                    break;
                                                                default:
                                                                    arr1Rec[j]=gridRecs[i].data[exportGridColumns[j].dataIndex];
                                                            }
                                                        //}else{
                                                        //    arr1Rec[j]=gridRecs[i].data[exportGridColumns[j].dataIndex].substring(gridRecs[i].data[exportGridColumns[j].dataIndex].indexOf(",")+1,gridRecs[i].data[exportGridColumns[j].dataIndex].length);
                                                        //}
                                                    }
                                                    arr2Recs.push(arr1Rec);
                                                }

                                                var exportGridStore = Ext.create('Ext.data.Store', {
                                                    fields: exportGridStoreFields,
                                                    data: arr2Recs
                                                });
                                                var exportGrid = Ext.create("Ext.grid.Panel",{
                                                    plugins:[{ptype: 'gridexporter'}],
                                                    columns:exportGridColumns,
                                                    store:exportGridStore
                                                });
                                                exportGrid.saveDocumentAs({
                                                    type: 'excel',
                                                    title: '面试结果信息',
                                                    fileName: '面试结果信息.xls'
                                                });

                                                exportGrid.close();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    },

    //保存
    onPanelSave: function(){
        //面试信息列表
        var grid = this.getView().down("grid");

        //面试信息数据
        var store = grid.getStore();

        //提交参数
        var tzParams = this.getItwMgrUpdSubmitParams();

        //保存数据
        Ext.tzSubmit(tzParams,function(){
            store.reload();
        },"",true,this);
    },

    //确定
    onPanelConfirm: function(){
        //面试信息列表
        var grid = this.getView().down("grid");
        //面试信息数据
        var store = grid.getStore();
        //提交参数
        var tzParams = this.getItwMgrUpdSubmitParams();
        var comView = this.getView();
        if(tzParams!=""){
            Ext.tzSubmit(tzParams,function(){
                comView.close();
            },"",true,this);
        }else{
            comView.close();
        }
    },

    //关闭
    onPanelClose: function(){
        this.getView().close();
    },

    //获取提交参数
    getItwMgrUpdSubmitParams:function(){
        //更新操作参数
        var comParams = "";

        //修改json字符串
        var editJson = "";

        //面试信息列表
        var grid = this.getView().down("grid");

        //面试信息数据
        var store = grid.getStore();

        //修改记录
        var mfRecs = store.getModifiedRecords();

        for(var i=0;i<mfRecs.length;i++){
            if(editJson == ""){
                editJson = '{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }else{
                editJson = editJson + ',{"data":'+Ext.JSON.encode(mfRecs[i].data)+'}';
            }
        }

        if(editJson != "")comParams = '"update":[' + editJson + "]";

        var tzParams = '{"ComID":"TZ_MS_MGR_COM","PageID":"TZ_MS_IVWMGR_STD","OperateType":"U","comParams":{'+comParams+'}}';

        return tzParams;
    }

});
