Ext.define('KitchenSink.view.classManage.clsHCGZ.hcgzClassListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hcgzClassListController',
    onWinConfirm:function(btn){
        //alert("onWinConfirm");
        var hcgzClassListForm = btn.up('window').child('form').getForm();
        var hcgzClassListFormRec = hcgzClassListForm.getFieldValues();
        var hcgzId = hcgzClassListFormRec["hcgzId"];

        var hcgzClassListGrid =btn.up('window').child('grid');
        var hcgzClassListGridSelRecs= hcgzClassListGrid.getSelectionModel().getSelection();

        var editJson="";
        var comParams="";
        for(var i=0;i<hcgzClassListGridSelRecs.length;i++){
            if(editJson == ""){
                editJson = Ext.JSON.encode(hcgzClassListGridSelRecs[i].data);
            }else{
                editJson = editJson + ','+Ext.JSON.encode(hcgzClassListGridSelRecs[i].data);
            }
        };

        if(editJson != ""){
            comParams = '"add":[' + editJson + "]";
        }else{
            comParams = '"add":[]';
        }

        var hcgzGZXQClassGrid = btn.up('window').up('panel').child('grid');
        var tzParams = '{"ComID":"TZ_CLS_HCGZ_COM","PageID":"TZ_CLS_HCGZCLS_STD","OperateType":"addClasses","comParams":{"hcgzId":"'+hcgzId+'",'+comParams+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
            if(responseData.success='success'){
                var Params= '{"hcgzId":"'+hcgzId+'"}';
                hcgzGZXQClassGrid.store.tzStoreParams = Params;
                hcgzGZXQClassGrid.store.load();
            };
        },"",true,this);
        this.onWinClose(btn);
    },
    onWinClose:function(btn){
        //alert("onPanelClose");
        btn.up('window').close();
    }
});
