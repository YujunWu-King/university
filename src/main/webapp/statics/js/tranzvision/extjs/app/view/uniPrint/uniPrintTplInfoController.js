Ext.define('KitchenSink.view.uniPrint.uniPrintTplInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uniPrintTplInfoController',
    //数据导入模板查询
    searchImpTpl:function(field) {
        var me = this,
            view = me.getView(),
            form = view.child("form").getForm(),
            grid = view.down("grid[name=fieldGrid]"),
            gridStore = grid.getStore();

        var actType = view.actType;
        var jgId = form.findField("TZ_JG_ID").getValue();
        var dymbId = form.findField("TZ_DYMB_ID").getValue();

        if(actType=="add") {
            Ext.Msg.alert("提示", "请先保存打印模板信息。");
            return;
        }

        Ext.tzShowPromptSearch({
            recname: 'TZ_IMP_TPL_DFN_VW',
            searchDesc: '搜索导入模板',
            maxRow:20,
            condition:{
                srhConFields:{
                    TZ_TPL_ID:{
                        desc:'导入模板编号',
                        operator:'07',
                        type:'01'
                    },
                    TZ_TPL_NAME:{
                        desc:'导入模板名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_TPL_ID:'导入模板编号',
                TZ_TPL_NAME:'导入模板名称'
            },
            multiselect: false,
            callback: function(selection){
                var drmbIdField = form.findField("TZ_DYMB_DRMB_ID");
                var drmbIdValue = drmbIdField.getValue();

                var selectValue = selection[0].data.TZ_TPL_ID;

                if(drmbIdValue!="" && drmbIdValue!=selectValue){
                    Ext.MessageBox.confirm('确认', '更换导入模板会重新加载字段，您确定要选择所选模板吗?', function(btnId){
                        if(btnId == 'yes'){
                            me.loadTplField(drmbIdField,selectValue,gridStore,jgId,dymbId);
                        }
                    },this);
                } else {
                    me.loadTplField(drmbIdField,selectValue,gridStore,jgId,dymbId);
                }
            }
        });
    },
    //加载数据导入模板中的字段到grid中
    loadTplField:function(field,selectvalue,gridStore,jgId,dymbId) {
        field.setValue(selectvalue);
        var tzParams = {
            "OperateType":"COMBOX",
            "recname":"TZ_IMP_TPL_FLD_VW",
            "condition":{
                "TZ_TPL_ID":{
                    "value":selectvalue,
                    "operator":"01",
                    "type":"01"
                }
            },
            "result":"TZ_FIELD,TZ_FIELD_NAME"
        };
        Ext.tzLoad(Ext.encode(tzParams),function(responseData){
            var columns = responseData["TZ_IMP_TPL_FLD_VW"];

            gridStore.removeAll();
            //默认加载图片字段,ID 固定 -begin;
            var record_tmp = new KitchenSink.view.uniPrint.uniPrintTplFieldModel({
                TZ_JG_ID:jgId,
                TZ_DYMB_ID:dymbId,
                TZ_DYMB_FIELD_ID:"TZ_DY_IMG",
                TZ_DYMB_FIELD_SM:"照片",
                TZ_DYMB_FIELD_QY:""
            });
            gridStore.add(record_tmp);
            //默认加载图片字段,ID 固定 -end;

            Ext.each(columns,function(column){
                var record = new KitchenSink.view.uniPrint.uniPrintTplFieldModel({
                    TZ_JG_ID:jgId,
                    TZ_DYMB_ID:dymbId,
                    TZ_DYMB_FIELD_ID:column["TZ_FIELD"],
                    TZ_DYMB_FIELD_SM:column["TZ_FIELD_NAME"],
                    TZ_DYMB_FIELD_QY:"1"
                });
                gridStore.add(record);
            });
        });
    },
    //上传PDF模板
    uploadPDF: function (file, value, eOpts ){
        if(value != ""){
            var form = file.findParentByType("form").getForm();
            var panel = file.findParentByType("uniPrintTplInfo");

            if(panel.actType == "update"){
                var dymbId =panel.child("form").getForm().findField("TZ_DYMB_ID").getValue();
                // 获取后缀
                var fix = value.substring(value.lastIndexOf(".") + 1,value.length);
                if(fix.toLowerCase() == "pdf" ){
                    form.submit({
                        url: TzUniversityContextPath + '/UpPdfPServlet?tplid='+dymbId,
                        waitMsg: '正在上传，请耐心等待....',
                        success: function (form, action) {
                            var message = action.result.msg;
                            var path = message.accessPath;
                            var filename = message.filename;
                            var sysFileName = message.sysFileName;
                            if(path.charAt(path.length - 1) == '/'){
                                path = path + sysFileName;
                            }else{
                                path = path + "/" + sysFileName;
                            }
                            form.findField("TZ_DYMB_PDF_URL").setValue(path);
                            form.findField("TZ_DYMB_PDF_NAME").setValue(filename);

                            var url = TzUniversityContextPath + "/DownPdfPServlet?templateID="+dymbId;
                            form.findField("downfileName").setValue("<a href='"+url+"' target='_blank'>"+filename+"</a>");
                            form.findField("pdfuploadfile").setVisible(false);

                            var btndeletePdf=panel.child("form").down('button[name=pdfDeleteBtn]');
                            btndeletePdf.show();
                        },
                        failure: function (form, action) {
                            Ext.MessageBox.alert("错误", action.result.msg);
                        }
                    });
                } else {
                    Ext.MessageBox.alert("提示", "请上传pdf格式的文件。");
                }
            } else {
                Ext.MessageBox.alert("提示", "请先保存。");
            }
        }
    },
    //删除PDF模板
    deletePDF:function(btn,value, eOpts) {
        var form = btn.findParentByType("form").getForm();
        var comParams  = '"update":[{"type":"TPL","data":'+Ext.JSON.encode(form.getValues())+'}]';
        var tzParams = '{"ComID":"TZ_DYMB_COM","PageID":"TZ_DYMB_INF_STD","OperateType":"U","comParams":{'
            + comParams + '}}';

        var panel = btn.findParentByType("uniPrintTplInfo");
        var grid = panel.down("grid");

        Ext.Ajax.request({
            url:Ext.tzGetGeneralURL(),
            async:false,
            params: {
                tzParams: tzParams
            },
            waitTitle : '请等待' ,
            waitMsg: '正在删除中',
            success: function(response){
                var btndeletePdf=panel.child("form").down('button[name=pdfDeleteBtn]');
                btndeletePdf.hide();

                form.findField("pdfuploadfile").setVisible(true);
                form.findField("downfileName").setValue("");
                form.findField("TZ_DYMB_PDF_URL").setValue("");
                form.findField("TZ_DYMB_PDF_NAME").setValue("");
            }
        });
    },
    //grid-新增
    addField: function (btn) {
        var me = this,
            view = me.getView();

        var actType = view.actType;
        var form = view.child("form").getForm();
        var jgId = form.findField("TZ_JG_ID").getValue();
        var dymbId = form.findField("TZ_DYMB_ID").getValue();
        var impTplId = form.findField("TZ_DYMB_DRMB_ID").getValue();

        if(actType=="add") {
            Ext.Msg.alert("提示", "请先保存打印模板信息再新增。");
            return;
        }


        if(impTplId=="" ||impTplId.length<1) {
            Ext.MessageBox.alert('提示', '您没有配置数据导入模板，无法新增模板字段。');
            return;
        }

        var r = new KitchenSink.view.uniPrint.uniPrintTplFieldModel(
            {
                TZ_JG_ID : jgId,
                TZ_DYMB_ID : dymbId,
                TZ_DYMB_FIELD_ID : "",
                TZ_DYMB_FIELD_SM : "",
                TZ_DYMB_FIELD_QY : '1',
                TZ_DYMB_FIELD_PDF : ''
            });


        var grid = btn.findParentByType("grid");
        var store = grid.store;
        var rowCount = store.getCount();
        var gridCellEditing = grid.getPlugin();
        store.insert(rowCount,r);
        gridCellEditing.startEditByPosition({
            row: rowCount,
            column: 1
        });


        /*
        Ext.tzShowPromptSearch({
            recname : 'TZ_IMP_TPL_FLD_VW',
            searchDesc : '新增模板字段',
            maxRow : 20,
            TZ_EFFEXP_ZT : "",
            condition : {
                presetFields : {
                    TZ_TPL_ID : {
                        value : impTplId,
                        type : '01'
                    }
                },
                srhConFields : {
                    TZ_FIELD : {
                        desc : '字段ID',
                        operator : '07',
                        type : '01'
                    },
                    TZ_FIELD_NAME : {
                        desc : '字段名称',
                        operator : '07',
                        type : '01'
                    }
                }
            },
            srhresult : {
                TZ_FIELD : '字段ID',
                TZ_FIELD_NAME : '字段名称'
            },
            multiselect : true,
            callback : function(selection) {
                var store = btn.findParentByType('grid').store;
                for (var i = 0; i < selection.length; i++) {
                    var fieldID = selection[i].data.TZ_FIELD;
                    var fieldName = selection[i].data.TZ_FIELD_NAME;

                    if (store.find('TZ_DYMB_FIELD_ID', fieldID, 0, false, false, true) == -1) {
                        var model = new KitchenSink.view.uniPrint.uniPrintTplFieldModel(
                            {
                                TZ_JG_ID : jgId,
                                TZ_DYMB_ID : dymbId,
                                TZ_DYMB_FIELD_ID : fieldID,
                                TZ_DYMB_FIELD_SM : fieldName,
                                TZ_DYMB_FIELD_QY : '1',
                                TZ_DYMB_FIELD_PDF : ''
                            });
                        store.add(model);
                    }
                };
            }
        })
        */
    },
    //grid-删除
    removeField:function(btn) {
        var me = this,
            view = me.getView();
        var grid = view.down("grid[name=fieldGrid]"),
            store = grid.store;

        var selList = grid.getSelectionModel().getSelection();
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else {
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    store.remove(selList);
                }
            },this);
        }
    },
    //grid-自动匹配
    autoMatchField:function(btn) {
        var me = this,
            view = me.getView();

        var form = view.child("form").getForm();
        var jgId = form.findField("TZ_JG_ID").getValue();
        var dymbId = form.findField("TZ_DYMB_ID").getValue();
        var impTplId = form.findField("TZ_DYMB_DRMB_ID").getValue();
        var fileName = form.findField("TZ_DYMB_PDF_NAME").getValue();
        var filePath = form.findField("TZ_DYMB_PDF_URL").getValue();

        var actType = view.actType;

        if(actType=="add") {
            Ext.Msg.alert("提示", "请先保存。");
            return;
        }
        if (impTplId == "" || impTplId.length < 1) {
            Ext.MessageBox.alert('提示', '您没有配置数据导入模板，无法新增模板字段。');
            return;
        }
        if (fileName == "" || fileName.length < 1) {
            Ext.MessageBox.alert('提示', '您没有上传pdf文档，无法加载PDF模板信息项。');
            return;
        }
        if (filePath == "" || filePath.length < 1) {
            Ext.MessageBox.alert('提示', '您没有上传pdf文档，无法加载PDF模板信息项。');
            return;
        }

        var grid = btn.findParentByType("grid");
        var store = grid.getStore();
        var count = store.getCount();
        if (count > 0) {
            var storDates = this.getGridInfoParams(store);
            var tzParams = '{"ComID":"TZ_DYMB_COM","PageID":"TZ_DYMB_INF_STD","OperateType":"tzAnalysisMatch","comParams":{"jgId":"'
                + jgId
                + '","dymbId":"'
                + dymbId
                + '","filePath":"'
                + filePath
                + '","storDates":['
                + storDates + ']}}';


            // 加载数据
            Ext.tzLoad(tzParams, function(responseData) {
                var fieldList = responseData.root;
                if (fieldList == null || fieldList.length == 0) {
                    Ext.Msg.alert("提示", "PDF模版文件里面没有可以匹配的项。");
                    return;
                }
                for(var i=0;i<count;i++) {
                    var jgId = store.getAt(i).get("TZ_JG_ID");
                    var dymbId = store.getAt(i).get("TZ_DYMB_ID");
                    var fieldId = store.getAt(i).get("TZ_DYMB_FIELD_ID");

                    for (var j=0;j<fieldList.length;j++) {
                        var jgId_return = fieldList[j].jgId;
                        var dymbId_return = fieldList[j].dymbId;
                        var fieldId_return = fieldList[j].fieldID;
                        var fieldPdf_return = fieldList[j].fieldPdf;

                        if(jgId==jgId_return && dymbId==dymbId_return&&fieldId==fieldId_return) {
                            store.getAt(i).set("TZ_DYMB_FIELD_PDF",fieldPdf_return);
                        }
                    }
                }
            });

        } else {
            Ext.MessageBox.alert('提示', '数据映射关系列表中没有数据，无法加载PDF模板信息项。');
            return;
        }
    },
    /* 加载grid的信息 构造提交后台的参数 */
    getGridInfoParams : function() {
        // 修改json字符串
        var editJson = "";
        // 字段列表
        var grid = this.getView().down("grid");
        var store = grid.getStore();
        var count = store.getCount();

        for (var i = 0; i < count; i++) {
            if (editJson == "") {
                editJson = '{"data":' + Ext.JSON.encode(store.getAt(i).data)
                    + '}';
            } else {
                editJson = editJson + ',' + '{"data":'
                    + Ext.JSON.encode(store.getAt(i).data) + '}';
            }
        }
        return editJson;
    },
    //grid-查询PDF模板对应字段
    searchPdfField:function(btn) {
        var form = btn.findParentByType("form").getForm();
        var jgId = form.findField("TZ_JG_ID").getValue();
        var dymbId = form.findField("TZ_DYMB_ID").getValue();

        var grid = btn.findParentByType("grid");
        var selectRow = grid.getSelectionModel().getSelection();

        Ext.tzShowPromptSearch({
            recname : 'TZ_PDF_MBXX_T',
            searchDesc : '选择PDF模板字段',
            maxRow : 20,
            TZ_EFFEXP_ZT : "",
            condition : {
                presetFields : {
                    TZ_JG_ID : {
                        value : jgId,
                        type : '01'
                    },
                    TZ_MB_ID : {
                        value : dymbId,
                        type : '01'
                    }
                },
                srhConFields : {
                    TZ_PDF_FIELD : {
                        desc : 'PDF模板字段',
                        operator : '07',
                        type : '01'
                    }
                }
            },
            srhresult : {
                TZ_PDF_FIELD : 'PDF模板字段'
            },
            callback : function(selection) {
                var selectValue = selection[0].data.TZ_PDF_FIELD;
                selectRow[0].set("TZ_DYMB_FIELD_PDF",selectValue);
            }
        })
    },
    //清除字段
    clearField:function(field) {
        field.setValue("");
    },
    //保存和确定
    saveTplInfo:function(btn) {
        var me = this,
            view = me.getView();
        var form = view.child("form").getForm();
        if(form.isValid()) {
            var tzParams = me.getPrintTplInfoParams();

            var actType = view.actType;
            var tplId = form.findField("TZ_DYMB_ID").getValue();
            if(actType=="update" && (tplId=="" || tplId=="NEXT" || typeof(tplId) == "undefined" )) {
                Ext.Msg.alert("提示","保存出错");
            } else {
                Ext.tzSubmit(tzParams, function (responseData) {
                    if(actType=="add") {
                        view.actType="update";
                        form.findField("TZ_DYMB_ID").setValue(responseData.dymbId);
                    }

                    view.commitChanges(view);

                    if(btn.name=="ensure") {
                        view.close();
                    }

                },"",true,this);
            }
        }
    },
    //获取保存参数
    getPrintTplInfoParams:function() {
        var me = this,
            view = me.getView();
        var form = view.child("form").getForm();

        var actType = view.actType;

        var comParams="";

        if(actType=="add") {
            comParams = '"add":[{"type":"TPL","data":'+Ext.JSON.encode(form.getValues())+'}]';
        }

        var editJson="";
        if(actType=="update") {
            editJson = '{"type":"TPL","data":'+Ext.JSON.encode(form.getValues())+'}';
        }

        //数据映射关系
        var fieldGrid = view.down("grid[name=fieldGrid]");
        var fieldGridStore = fieldGrid.getStore();

        //修改记录
        var modifyRecords = fieldGridStore.getModifiedRecords();
        for(var i=0;i<modifyRecords.length;i++) {
            if(editJson=="") {
                editJson = '{"type":"FIELD","data":'+Ext.JSON.encode(modifyRecords[i].data)+'}';
            } else {
                editJson = editJson + ',' + '{"type":"FIELD","data":'+Ext.JSON.encode(modifyRecords[i].data)+'}';
            }
        }

        //删除记录
        var removeJson = "";
        var removeRecords = fieldGridStore.getRemovedRecords();
        for(var i=0;i<removeRecords.length;i++) {
            if(removeJson=="") {
                removeJson = Ext.JSON.encode(removeRecords[i].data);
            } else {
                removeJson = removeJson + ',' + Ext.JSON.encode(removeRecords[i].data);
            }
        }


        if(editJson!="") {
            if(comParams=="") {
                comParams = '"update":['+editJson+']';
            } else {
                comParams = comParams + ',' + '"update":['+editJson+']';
            }
        }

        if(removeJson!="") {
            if(comParams=="") {
                comParams = '"delete":['+removeJson+']';
            } else {
                comParams = comParams + ',' + '"delete":['+removeJson+']';
            }
        }

        var tzParams='{"ComID":"TZ_DYMB_COM","PageID":"TZ_DYMB_INF_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },
    //关闭
    closeTplInfo:function(btn) {
        btn.findParentByType("uniPrintTplInfo").close();
    }
});
