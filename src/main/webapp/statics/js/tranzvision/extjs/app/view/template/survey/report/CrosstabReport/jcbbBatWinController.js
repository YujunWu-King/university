Ext.define('KitchenSink.view.template.survey.report.CrosstabReport.jcbbBatWinController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jcbbBatWinController',
    /**
     * 交叉报表批处理window handler
     * LYY  2015-11-06
     */
    //生成报表
    batGenJcbb:function(btn){
        var genJcbbForm = btn.up('form');
        var genJcbbFormRec = genJcbbForm.getForm().getFieldValues();
        var jcbbName=genJcbbFormRec["jcbbName"];
        if(jcbbName==''){
            Ext.Msg.alert("提示","请输入生成报表名称.");
        }else{
            var onlinedcId=genJcbbFormRec["onlinedcId"];
            var wtIds=genJcbbFormRec["wtIds"];
            var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_JCBBLB_STD","OperateType":"BATTB","comParams":{"onlinedcId":"'+onlinedcId+'","jcbbName":"'+jcbbName+'","wtIds":"'+wtIds+'"}}';
            Ext.tzSubmit(tzParams,function(responseData){
                var genJcbbTabPan = genJcbbForm.up('tabpanel');
                genJcbbTabPan.getLayout().setActiveItem(1);

                var viewJcbbGrid = genJcbbForm.up('tabpanel').child('grid');
                viewJcbbGrid.store.load();
            },"处理中...",true,this);
        }
    },

    //查看报表->查询
    viewJcbbQuery:function(btn){
        Ext.tzShowCFGSearch({
            cfgSrhId: 'TZ_ZXDC_WJGL_COM.TZ_ZXDC_JCBBBT_STD.TZ_JCBB_LIST_V',
            condition:{},
            callback: function(seachCfg){
                var store = btn.findParentByType("grid").store;
                store.tzStoreParams = seachCfg;
                store.load();
            }
        });
    },

    //查看报表->删除
    viewJcbbDelete:function(btn){
        var viewJcbbGrid = btn.up('grid');
        //选中行
        var selList = viewJcbbGrid.getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert("提示","请选择要删除的记录");
            return;
        }else{
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    var viewJcbbGridStore =viewJcbbGrid.store;
                    viewJcbbGridStore.remove(selList);
                }
            },this);
        }
    },

    //查看报表->查看
    viewJcbb:function(grid, rowIndex, colIndex){
        var viewJcbbGridRowRecord = grid.store.getAt(rowIndex);
        var jcbbId = viewJcbbGridRowRecord.data.jcbbId;
        var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_JCBBBT_STD","OperateType":"QF","comParams":{"jcbbId":"'+jcbbId+'"}}';
        Ext.tzLoad(tzParams,function(responseData){
            window.open(responseData.VIEWURL,'_blank');
        });
    },

    //查看报表->保存
    viewJcbbSave:function(btn){
        var viewJcbbGrid = btn.up('grid');
        var selectedRecs = viewJcbbGrid.store.getRemovedRecords();
        var selectedJson="";
        if (selectedRecs.length>0){
            for(var i=0;i<selectedRecs.length;i++){
                if(selectedJson == ""){
                    selectedJson = Ext.JSON.encode(selectedRecs[i].data);
                }else{
                    selectedJson = selectedJson + ','+Ext.JSON.encode(selectedRecs[i].data);
                }
            }

            if(selectedJson != ""){
                var comParams = '"delete":[' + selectedJson + "]";
            }
            //提交参数
            var tzParams = '{"ComID":"TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_JCBBBT_STD","OperateType":"U","comParams":{'+comParams+'}}';
            //保存数据
            Ext.tzSubmit(tzParams,function(responseData){
                if(responseData.success='success'){
                    //刷新grid
                    viewJcbbGrid.store.load();
                };
            },"",true,this);
        }else{
            Ext.tzShowToast('保存成功','提示','t','#ffffff');
        };
    },

    //关闭window
    onWindowClose:function(btn){
        btn.up('tabpanel').up('form').up('window').close();
    }
});
