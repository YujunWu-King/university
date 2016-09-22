/**
 * Created by caoy on 2016/9/18.
 */
Ext.define('KitchenSink.view.siteManage.outsiteBatch.batchReleaseController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.batchReleaseC',


    //批量发布
    onBatchSave: function(btn) {
        var form = this.getView().child("form").getForm();
        var comSiteParams = form.getValues();

        //发布类型
        var batchType = comSiteParams["batchType"];

        var  objectId="";

        // A:按站点发布   B:按栏目发布
        if (batchType == "A") {
            objectId = comSiteParams["siteId"];
        } else if (batchType == "B") {
            objectId = comSiteParams["coluId"];
        }

        //更新操作参数
        var comParams = "";

        if (batchType == "" || batchType.length < 1) {
            Ext.MessageBox.alert('提示', '您没有选择发布类型，无法发布。');
            return;
        };
        if (objectId == "" || objectId.length < 1) {
            Ext.MessageBox.alert('提示', '您没有选择发布的站点或栏目，无法发布。');
            return;
        };

        //提交参数
        var tzParams = '{"ComID":"TZ_GD_WWZDPLFB_COM","PageID":"TZ_GD_PLFB_STD","OperateType":"batchRelease","comParams":{"batchType":"'
            + batchType+ '","objectId":"'+objectId+'"}}';

        var grid = this.getView().child("grid");
        var store = grid.getStore();

        Ext.tzSubmit(tzParams,function(responseData){
        	 //Ext.Msg.alert("提示",responseData.success);
        	if(responseData.success==true){
        		store.reload();
        	}else{
        	 	  Ext.Msg.alert("提示",responseData.msg);
        	}
        },"",true,this);
    },

    //刷行
    onBatchreshfresh: function(btn) {
        var grid = this.getView().child("grid");
        var store = grid.getStore();
        store.reload();
    },

    //放大镜 查询 外部站点
    pmtSearchSite: function(btn){
        var form = this.getView().child("form").getForm();
        var comSiteParams = form.getValues();
        var siteId = comSiteParams['siteId'];
        Ext.tzShowPromptSearch({
            recname: 'TZ_SITEI_DEFN_T',
            searchDesc: '搜索站点',
            maxRow:20,
            condition:{
                presetFields:{
                    TZ_SITEI_TYPE:{
                        value:'A,B',
                        type: '01',
                        operator:'10'
                    }
                },
                srhConFields:{
                    TZ_SITEI_ID:{
                        desc:'站点ID',
                        operator:'07',
                        type:'01'
                    },
                    TZ_SITEI_NAME:{
                        desc:'站点名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_SITEI_ID: '站点ID',
                TZ_SITEI_NAME: '站点名称'
            },
            multiselect: false,
            callback: function(selection){
                form.findField("siteId").setValue(selection[0].data.TZ_SITEI_ID);
                form.findField("siteName").setValue(selection[0].data.TZ_SITEI_NAME);
            }
        });
    },
    clearPmtSearchSite: function(btn){
        var form = this.getView().child("form").getForm();
        form.findField("siteId").setValue("");
        form.findField("siteName").setValue("");
    },

    //放大镜 查询 栏目
    pmtSearchColu: function(btn){
        var form = this.getView().child("form").getForm();
        Ext.tzShowPromptSearch({
            recname: 'TZ_SITEI_COLU_T',
            searchDesc: '搜索栏目',
            maxRow:20,
            condition:{
                presetFields:{
                    TZ_COLU_LEVEL:{
                        value:'0',
                        type: '02',
                        operator:'04'
                    }
                },
                srhConFields:{
                    TZ_COLU_ID:{
                        desc:'栏目ID',
                        operator:'07',
                        type:'01'
                    },
                    TZ_COLU_NAME:{
                        desc:'栏目名称',
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
            	TZ_COLU_ID: '栏目ID',
            	TZ_COLU_NAME: '栏目名称'
            },
            multiselect: false,
            callback: function(selection){
                form.findField("coluId").setValue(selection[0].data.TZ_COLU_ID);
                form.findField("coluName").setValue(selection[0].data.TZ_COLU_NAME);
            }
        });
    },
    clearPmtSearchColu: function(btn){
        var form = this.getView().child("form").getForm();
        form.findField("coluId").setValue("");
        form.findField("coluName").setValue("");
    }

});