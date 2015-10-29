Ext.define('KitchenSink.view.interviewManagement.interviewManage.msResImpController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.msResImpController',


    //校验提示
    dataCKTip:function(view, rowIndex){
        var wingridstore = view.findParentByType("grid").store;
        var record = wingridstore.getAt(rowIndex);
        var ckResult = record.data.checkRes;
        if(ckResult=="-1"||ckResult=="" || ckResult==" " ||ckResult==undefined ){
        }else{
            Ext.MessageBox.alert('提示', ckResult);
        }
    },

    //删除当前行
    deleteCurrMsResImpRec:function(view, rowIndex){
        var wingridstore = view.findParentByType("grid").store;
        wingridstore.removeAt(rowIndex);
        //更新总条数
        var winform = view.findParentByType("grid").findParentByType('window').down('form').getForm();
        var winformrec=winform.getFieldValues();
        var classID = winformrec["classID"];
        var batchID = winformrec["batchID"];
        var strtmp = "共 ";
        strtmp = strtmp+"<span style='color:red;font-size: 22px'>"+wingridstore.getCount()+"</span>";
        strtmp = strtmp+" 条数据";
        var formpara={classID:classID,batchID:batchID,ImpRecsCount:strtmp};
        winform.setValues(formpara);
    },

    //保存
    onWinSave:function(btn){
        //alert(btn.getXType());
        var win = btn.findParentByType("window");

        var winformrec = win.child("form").getForm().getFieldValues();
        var classID = winformrec["classID"];
        var batchID = winformrec["batchID"];

        var winGridStore = win.child("grid").store;
        var winGridRecs = winGridStore.getRange();

        var comParams="";
        var strJson="";

        if(winGridRecs.length<=0){
            Ext.MessageBox.alert('提示', '没有导入数据！');
            return;
        }else{
            for(var i=0;i<winGridRecs.length;i++){
                //查重
                var checkRepeatappID = winGridRecs[i].get("appId");
                var checkRepeatappIDCount =0;

                var recIndex = winGridStore.findBy(function(record,id){
                    if(record.get("appId")==checkRepeatappID){
                        checkRepeatappIDCount++;
                        if(checkRepeatappIDCount>1){
                            return true;
                        }
                    };
                },0);

                var msResImpGridCellEditing = win.child("grid").getPlugin('msResImpGridCellEditing');

                if(checkRepeatappID==""||checkRepeatappID==" "){
                    Ext.MessageBox.alert('提示', '报名表编号不能为空！',
                        function(e){
                            if(e == "ok"|| e == "OK" || e == "确定"){
                                msResImpGridCellEditing.startEdit(winGridRecs[i], 1);
                            }
                        }
                    );
                    return;
                }

                if(checkRepeatappIDCount>1){
                    Ext.MessageBox.alert('提示', '报名表编号重复！',
                        function(e){
                            if(e == "ok"|| e == "OK" || e == "确定"){
                                msResImpGridCellEditing.startEdit(winGridRecs[i], 1);
                            }
                        }
                    )
                    return;
                };

                //是否编辑过
                if(winGridRecs[i].isModified('appId')||winGridRecs[i].isModified('stuName')||winGridRecs[i].isModified('msJoinState')||winGridRecs[i].isModified('msResult')||winGridRecs[i].isModified('msArrDemo')) {
                    //alert(winGridRecs[i].get("appId"));
                    winGridRecs[i].data.checkRes="-1";
                }

                if(strJson == ""){
                    strJson = '{"data":'+Ext.JSON.encode(winGridRecs[i].data)+'}';
                }else{
                    strJson = strJson + ',{"data":'+Ext.JSON.encode(winGridRecs[i].data)+'}';
                }
            }

            if(strJson != "") {
                comParams = '"imprecords":[' + strJson + "]";
            }
        }

        var tzParams = '{"ComID":"TZ_MS_MGR_COM","PageID":"TZ_MS_MSRESIMP_STD","OperateType":"msResImp","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'",'+comParams+'}}';

        Ext.tzLoad(tzParams,function(responseData){
            //alert(responseData['root']);
            //winGridStore.reload();
            if (responseData['rowcountckres']=="" ||responseData['rowcountckres']==" " ){

            }else{
                Ext.MessageBox.alert('提示',responseData['rowcountckres'] );
            }
            winGridStore.loadData(responseData['root']);
            if (responseData['impres']=="0" ) {
                Ext.tzShowToast('导入成功！','提示','t','#ffffff');
            }
        });
    },

    //确定
    onWinConfirm:function(btn){
        var win = btn.findParentByType("window");

        var winformrec = win.child("form").getForm().getFieldValues();
        var classID = winformrec["classID"];
        var batchID = winformrec["batchID"];

        var winGridStore = win.child("grid").store;
        var winGridRecs = winGridStore.getRange();

        var comParams="";
        var strJson="";

        if(winGridRecs.length<=0){
            Ext.MessageBox.alert('提示', '没有导入数据！');
            return;
        }else{
            for(var i=0;i<winGridRecs.length;i++){
                //查重
                var checkRepeatappID = winGridRecs[i].get("appId");
                var checkRepeatappIDCount =0;

                var recIndex = winGridStore.findBy(function(record,id){
                    if(record.get("appId")==checkRepeatappID){
                        checkRepeatappIDCount++;
                        if(checkRepeatappIDCount>1){
                            return true;
                        }
                    };
                },0);

                var msResImpGridCellEditing = win.child("grid").getPlugin('msResImpGridCellEditing');

                if(checkRepeatappID==""||checkRepeatappID==" "){
                    Ext.MessageBox.alert('提示', '报名表编号不能为空！',
                        function(e){
                            if(e == "ok"|| e == "OK" || e == "确定"){
                                msResImpGridCellEditing.startEdit(winGridRecs[i], 1);
                            }
                        }
                    );
                    return;
                }

                if(checkRepeatappIDCount>1){
                    Ext.MessageBox.alert('提示', '报名表编号重复！',
                        function(e){
                            if(e == "ok"|| e == "OK" || e == "确定"){
                                msResImpGridCellEditing.startEdit(winGridRecs[i], 1);
                            }
                        }
                    )
                    return;
                };

                //是否编辑过
                if(winGridRecs[i].isModified('appId')||winGridRecs[i].isModified('stuName')||winGridRecs[i].isModified('msJoinState')||winGridRecs[i].isModified('msResult')||winGridRecs[i].isModified('msArrDemo')) {
                    //alert(winGridRecs[i].get("appId"));
                    winGridRecs[i].data.checkRes="-1";
                }

                if(strJson == ""){
                    strJson = '{"data":'+Ext.JSON.encode(winGridRecs[i].data)+'}';
                }else{
                    strJson = strJson + ',{"data":'+Ext.JSON.encode(winGridRecs[i].data)+'}';
                }
            }

            if(strJson != "") {
                comParams = '"imprecords":[' + strJson + "]";
            }
        }

        var tzParams = '{"ComID":"TZ_MS_MGR_COM","PageID":"TZ_MS_MSRESIMP_STD","OperateType":"msResImp","comParams":{"classID":"'+classID+'","batchID":"'+batchID+'",'+comParams+'}}';
        //导入结果   0-导入成功  1-导入失败
        var impres="";
        //继续操作   N-否；Y-是
        var continueFlag="N";
        Ext.tzLoad(tzParams,function(responseData){
            //alert(responseData['root']);
            //winGridStore.reload();
            if (responseData['rowcountckres']=="" ||responseData['rowcountckres']==" " ){

            }else{
                Ext.MessageBox.alert('提示',responseData['rowcountckres'] );
            }
            winGridStore.loadData(responseData['root']);
            if (responseData['impres']=="0" ) {
                impres=responseData['impres'];
                Ext.tzShowToast('导入成功！','提示','t','#ffffff');

                //刷新grid
                var interviewMgrPanel=Ext.ComponentQuery.query("panel[reference=interviewMgrPanel]");
                var panelclassID,panelbatchId,panelstore;
                var Params= '{"classID":"'+classID+'","batchID":"'+batchID+'"}';
                if(interviewMgrPanel.length>0){
                    for(var i=0;i<interviewMgrPanel.length;i++){
                        panelclassID=interviewMgrPanel[i].down('form').getForm().getFieldValues()['classID'];
                        panelbatchId=interviewMgrPanel[i].down('form').getForm().getFieldValues()['batchID'];
                        if(panelclassID==classID && panelbatchId==batchID){
                            panelstore=interviewMgrPanel[i].down('grid').store;
                            panelstore.tzStoreParams = Params;
                            panelstore.reload();
                        }
                    }
                }

                win.close();
            }else{
                Ext.MessageBox.confirm('提示', '数据尚未导入成功，是否确定要离开当前页面?', function(btnId){
                    if(btnId == 'yes'){
                        win.close();
                    }else{

                    }
                },this);
            }
        });
    },

    //关闭
    onWinClose: function(){
        this.getView().close();
    }
})
