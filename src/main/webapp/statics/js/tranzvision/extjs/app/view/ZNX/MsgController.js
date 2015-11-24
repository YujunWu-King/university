Ext.define('KitchenSink.view.ZNX.MsgController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.MsgController',

    init: function() {
        this.setCurrentView('RecList');
    },

    queryReclist:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_ZNX_COM.TZ_ZNX_INBOX_STD.TZ_RECLIST_VW',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },
    querySentlist:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_ZNX_COM.TZ_ZNX_SENT_STD.TZ_SENTLIST_VW',
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },

    deleteMsg: function(btn){
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId) {
                if (btnId == 'yes') {
                    var store = btn.findParentByType("grid").store;
                    store.remove(selList);
                    var newstore = btn.findParentByType("grid").store;
                    var removeJson = "";
                    var comParams = "";
                    var removeRecs = newstore.getRemovedRecords();
                    console.log(store);
                    for (var i = 0; i < removeRecs.length; i++) {
                        if (removeJson == "") {
                            removeJson = Ext.JSON.encode(removeRecs[i].data);
                        } else {
                            removeJson = removeJson + ',' + Ext.JSON.encode(removeRecs[i].data);
                        }
                    }
                    if (removeJson != "") {
                        comParams = '"delete":[' + removeJson + "]";
                    } else {
                        return;
                    }
                    //提交参数
                    var tzParams = '{"ComID":"TZ_ZNX_COM","PageID":"TZ_ZNX_INBOX_STD","OperateType":"U","comParams":{' + comParams + '}}';
                    console.log(tzParams);
                    //var comView = this.getView();
                    //保存数据
                    if (comParams != "") {
                        Ext.tzSubmit(tzParams, function () {
                            store.commitChanges();
                            store.reload;
                           // console.log(store);
                        }, "", true, this);
                    }
                }
            })
        }
    },
    SenterdeleteMsg: function(btn){
        //选中行
        var selList =  btn.findParentByType("grid").getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId) {
                if (btnId == 'yes') {
                    var store = btn.findParentByType("grid").store;
                    store.remove(selList);
                    var newstore = btn.findParentByType("grid").store;
                    var removeJson = "";
                    var comParams = "";
                    var removeRecs = newstore.getRemovedRecords();
                    console.log(store);
                    for (var i = 0; i < removeRecs.length; i++) {
                        if (removeJson == "") {
                            removeJson = Ext.JSON.encode(removeRecs[i].data);
                        } else {
                            removeJson = removeJson + ',' + Ext.JSON.encode(removeRecs[i].data);
                        }
                    }
                    if (removeJson != "") {
                        comParams = '"delete":[' + removeJson + "]";
                    } else {
                        return;
                    }
                    //提交参数
                    var tzParams = '{"ComID":"TZ_ZNX_COM","PageID":"TZ_ZNX_SENT_STD","OperateType":"U","comParams":{' + comParams + '}}';
                    console.log(tzParams);
                    //var comView = this.getView();
                    //保存数据
                    if (comParams != "") {
                        Ext.tzSubmit(tzParams, function () {
                            store.commitChanges();
                            store.reload;
                            //console.log(store);
                        }, "", true, this);
                    }
                }
            })
        }
    },

    onMsgClose: function(bt) {
        this.getView().close();
    },

    getMsgInfoParams:function(){
        var form = this.getView().child("form").getForm();
        //表单数据
        var formParams = form.getValues();
        var actType = this.getView().actType;
        //更新操作参数
        var comParams = "";
        //新增
        if(actType == "add"){
            comParams = '"add":[{"typeFlag":"NEWMSG","data":'+Ext.JSON.encode(formParams)+'}]';
        }
        //提交参数
        var tzParams = '{"ComID":"TZ_ZNX_COM","PageID":"TZ_ZNX_NEWMSG_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
    },

    onMsgSend:function(btn) {
        var form = this.getView().child("form").getForm();
        if (form.isValid()) {
            var tzParams = this.getMsgInfoParams();
            var comView = this.getView();
           console.log(comView)
            Ext.tzSubmit(tzParams,function(responseData){
                //关闭窗口
                comView.close();
            },"",true,this);

          /*  var MsgID_new = form.findField('MsgID').getValues();
            var obj = this;
            var tzParams = '{"ComID":"TZ_ZNX_COM","PageID":"TZ_ZNX_NEWMSG_STD","displayOnly": true,"OperateType":"QF","comParams":{"MsgID":"' + MsgID_new + '"}}';
            Ext.tzLoad(tzParams, function (respData) {
                var formData = respData.formData;
                delete formData.files;
                form.setValues({MsgID: formData.MsgID});
                var file_grid = btn.findParentByType('panel').down('grid[name=attachmentGrid]');
                var file_store = file_grid.getStore();
                file_store.tzStoreParams = '{"MsgID":"' + formData.MsgID + '","queryType":"FILE"}';
                file_store.load();

                obj.updateRecord(grid, flg, formData);
//                var store=grid.getStore();
//                store.reload();
            });*/

        }
    },

    onMsgReply:function(btn){
        var tabel=btn.findParentByType("RecMsgInfo");
        var tabel=btn.findParentByType("RecMsgInfo");
        var MsgID=tabel.down('#MsgID').data.MsgID;
        var MsgReply=tabel.down('#MsgReply').data.MsgReply;
        console.lod(MsgReply)

        var tzParams = '{"ComID":"TZ_ZNX_COM","PageID":"TZ_ZNX_RECINFO_STD","OperateType":"Next","comParams":{"MsgID":"'+MsgID+'"}}';
        console.log(tzParams)

    },


    addZNXAttach : function(file, value, attachmentType){
        attachmentType = 'ATTACHMENT';
        var form = file.findParentByType("form").getForm();
        if(value != ""){
            //如果是附件则存在在附件的url中，如果是图片在存放在图片的url中;
            var dateStr = Ext.Date.format(new Date(), 'Ymd');
            var upUrl = "";
            if(attachmentType=="ATTACHMENT"){
                upUrl = "/linkfile/FileUpLoad/attachment/msg/";
                if(upUrl==""){
                    Ext.Msg.alert("错误","未定义上传附件的路径，请与管理员联系");
                    return;
                }else{
					/*
                    if(upUrl.length == (upUrl.lastIndexOf("/")+1)){
                        upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+dateStr;
                    }else{
                        upUrl = TzUniversityContextPath + '/UpdServlet?filePath='+upUrl+"/"+dateStr;
                    }
					*/
					upUrl = TzUniversityContextPath + '/UpdServlet?filePath=znx';
                }
            }
            var myMask = new Ext.LoadMask({
                msg    : '加载中...',
                target : Ext.getCmp('content-panel')
            });

            myMask.show();
            form.submit({
                url: upUrl,
                //waitMsg: '图片正在上传，请耐心等待....',
                success: function (form, action) {
                    var tzParams;
                    var picViewCom;

                    tzParams = '{"attachmentType":"' + attachmentType + '","data":' + Ext.JSON.encode(action.result.msg) + '}';
                    console.log(tzParams)
                    tzParams = '{"ComID":"TZ_ZNX_COM","PageID":"TZ_ZNX_NEWMSG_STD","OperateType":"ATTACH","comParams":' + tzParams +'}';
                    console.log(tzParams)
                    Ext.Ajax.request({
                        url: Ext.tzGetGeneralURL,
                        params: {
                            tzParams: tzParams
                        },
                        success: function(response){
                            var responseText = Ext.JSON.decode(response.responseText);
                            if(responseText.success == 0){
                                var accessPath = action.result.msg.accessPath;
                                var sltPath = action.result.msg.accessPath;
                                if(accessPath.length == (accessPath.lastIndexOf("/")+1)){
                                    accessPath = accessPath + action.result.msg.sysFileName;
                                    //sltPath = sltPath + "MINI_"+action.result.msg.sysFileName;
                                    sltPath = sltPath + responseText.minPicSysFileName;
                                }else{
                                    accessPath = accessPath + "/" + action.result.msg.sysFileName;
                                    //sltPath = sltPath+ "/" + "MINI_"+action.result.msg.sysFileName;
                                    sltPath = sltPath+ "/" + responseText.minPicSysFileName;
                                }

                                if(attachmentType=="ATTACHMENT"){
                                    //var applyItemGrid = this.lookupReference('attachmentGrid');
                                    var applyItemGrid = file.findParentByType("grid")
                                    var r = Ext.create('KitchenSink.view.activity.attachmentModel', {
//                                        "bugID":bugID,
                                        "attachmentID": 'NEXT',
                                        "attachmentSysName": action.result.msg.sysFileName,
                                        "attachmentName": "<a href='"+accessPath+"' target='_blank'>"+action.result.msg.filename+"</a>",
                                        "attachmentUrl": accessPath
                                    });
                                    applyItemGrid.store.insert(0,r);
                                }
                            }else{
                                Ext.Msg.alert("提示", responseText.message);
                            }
                        },
                        failure: function (response) {
                            Ext.MessageBox.alert("错误", "上传失败");
                        }
                    });

                    //重置表单
                    myMask.hide();
                    form.reset();
                },
                failure: function (form, action) {
                    myMask.hide();
                    Ext.MessageBox.alert("错误", action.result.msg);
                }
            });
        }
    },

    deleteArtAttenments: function(btn,rowIndex){
        //选中行
        var store = btn.findParentByType('grid').getStore();
        var selList = btn.findParentByType('grid').getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(rowIndex.toString().match(/^\d+$/)){
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    store.removeAt(rowIndex);
                }
            },this);
        }else {
            if(checkLen == 0){
                Ext.Msg.alert("提示","请选择要删除的记录");
                return;
            }else{
                Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                    if(btnId == 'yes'){
                        store.remove(selList);
                    }
                },this);
            }

        }
    },


    onBackBtnClick: function() {
        this.setCurrentView('RecList');
    },

    onMenuClick: function(menu, item){
        if(item) {
            this.setCurrentView(item.routeId, item.params);
        }
    },

    setCurrentView: function(view, params) {
        var contentPanel = this.getView().down('#ZNXContentPanel');
       // console.log("test======>"+this.getView().down('#ZNXContentPanel'))
        if(!contentPanel|| view === ''|| (contentPanel.down() && contentPanel.down().xtype === view)){
            return false;
        }
        if (params && params.openWindow) {
            var cfg = Ext.apply({
                xtype: 'Window',
                items: [
                    Ext.apply({
                        xtype: view
                    }, params.targetCfg)
                ]
            }, params.windowCfg);
            Ext.create(cfg);
        } else {
            Ext.suspendLayouts();
            contentPanel.removeAll(true);
            contentPanel.add(
                Ext.apply({
                    xtype: view
                }, params)
            );
            Ext.resumeLayouts(true);
        }
    },

    setCurrentViewFromGrid: function(view, params,grid) {
        var contentPanel = this.getView().down('#ZNXContentPanel');
        if(!contentPanel){
            contentPanel=grid.up('container[itemId=ZNXContentPanel]');
        }
      //  console.log("test======>"+this.getView().down('#ZNXContentPanel'))
        if(!contentPanel|| view === ''|| (contentPanel.down() && contentPanel.down().xtype === view)){
            return false;
        }
        if (params && params.openWindow) {
            var cfg = Ext.apply({
                xtype: 'Window',
                items: [
                    Ext.apply({
                        xtype: view
                    }, params.targetCfg)
                ]
            }, params.windowCfg);
            Ext.create(cfg);
        } else {
            Ext.suspendLayouts();
            contentPanel.removeAll(true);
            contentPanel.add(
                Ext.apply({
                    xtype: view
                }, params)
            );
            Ext.resumeLayouts(true);
        }
    },
    onGridCellItemClick: function(grid,view, cellIndex, record){
        if(cellIndex > 1){
            this.setCurrentViewFromGrid('RecMsgInfo',{record: record},grid);
        } else if (cellIndex === 1) {
            //Invert selection
            record.set('MsgFlag', !record.get('MsgFlag'));
        }
    },
    getNextMsg:function(btn)
    {
        var tabel=btn.findParentByType("RecMsgInfo");
        var MsgID=tabel.down('#MsgID').data.MsgID;

        var tzParams = '{"ComID":"TZ_ZNX_COM","PageID":"TZ_ZNX_RECINFO_STD","OperateType":"Next","comParams":{"MsgID":"'+MsgID+'"}}';
        //加载数据
        Ext.tzLoad(tzParams,function(responseData){
            var formData = responseData.formData;
            console.log(formData)
            tabel.down('#mailBody').setData(responseData.formData)
            tabel.down('#emailSubjectContainer').setData(responseData.formData)
            tabel.down('#MsgID').setData(responseData.formData)
        });
        if(MsgID == ''){
            Ext.Msg.alert("提示","已查看至最后一条信息");
            return;
        }
    },


    deleteCurrentZNXMsg:function(btn)
    {
        var tabel=btn.findParentByType("RecMsgInfo");
        var MsgID=tabel.down('#MsgID').data.MsgID;
        console.log(MsgID)
        var tzParams = '{"ComID":"TZ_ZNX_COM","PageID":"TZ_ZNX_RECINFO_STD","OperateType":"U","comParams":{"MsgID":"'+MsgID+'"}}';
        console.log(tzParams)
        //加载数据
        Ext.tzSubmit(tzParams, function () {
            //store.commitChanges();
           // store.reload;

        }, "", true, this);
        this.setCurrentView('RecList');
    },
   beforeMsgRender: function(view) {
       var record = view.record ? view.record : {};
       var MsgID=record.get('MsgID')
      var tzParams = '{"ComID":"TZ_ZNX_COM","PageID":"TZ_ZNX_RECINFO_STD","OperateType":"QF","comParams":{"MsgID":"'+MsgID+'"}}';
       //加载数据

       Ext.tzLoad(tzParams,function(responseData){
           var formData = responseData.formData;
           view.down('#mailBody').setData(responseData.formData)
           view.down('#MsgID').setData(responseData.formData)


       });

      // view.down('#mailBody').setData(record.get('MsgText'));
       //console.log(record.data);
       view.down('#emailSubjectContainer').setData(record.data? record.data: {});

    }

});

