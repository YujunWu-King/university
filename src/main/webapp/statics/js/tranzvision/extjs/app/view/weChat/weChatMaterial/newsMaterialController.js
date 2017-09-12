Ext.define('KitchenSink.view.weChat.weChatMaterial.newsMaterialController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.newsMaterialController',
    
    
    //选择封面图片
	chooseCoverPic: function(btn){
		var panel = btn.findParentByType("newsMaterialInfoPanel");
        var wxAppId = panel.wxAppId;
        
        Ext.tzSetCompResourses("TZ_GD_WXMSG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_WXMSG_COM"]["TZ_GD_SCGL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_GD_SCGL_STD，请检查配置。');
            return;
        }

        var contentPanel,cmp, className, ViewClass, clsProto;
        var themeName = Ext.themeName;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');
        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        clsProto = ViewClass.prototype;
        
        var win=this.lookupReference('weChatMsgScWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            
            win = new ViewClass({
            	materialType: 'TP',
            	wxAppId: wxAppId,
            	callback: function(rec){
            		var mediaId = rec.get("mediaId");
            		var src = rec.get("src");
            		
            		var currentRecord = panel.currentRecord;
    				currentRecord.set("thumb_media_id", mediaId);
    				currentRecord.set("thumb_img_url", src);
            	}
            });
            //this.getView().add(win);
        }
       win.show(); 
	},
    
    
    //保存发布图文素材
    onSave: function(btn){
    	var btnName = btn.name;
    	var panel = btn.findParentByType('newsMaterialInfoPanel');
    	var jgId = panel.jgId;
    	var wxAppId = panel.wxAppId;
    	
    	var articesView = panel.down('dataview[reference=newsMaterialArticesView]');
    	var articesViewStore = articesView.getStore();
    	
    	var form = panel.child('form[reference=newsMaterialForm]');
    	var formRec = form.getForm().getValues();
    	
    	var successTip = "保存成功";
    	var operateType = "tzSaveNewsMaterial";
    	if(btnName == "publishBtn"){
    		successTip = "发布成功";
    		operateType = "tzPublishNewsMaterial";
    	}

    	if(form.isValid()){
    		//验证每篇图文消息
    		var articesData = [];
    		var isValid = true;
    		articesViewStore.each(function(rec) {   
    	       var title = rec.get("title");
    	       var thumb_media_id = rec.get("thumb_media_id");
    	       var content = rec.get("content");
    	       var orderNum = rec.get("orderNum");

    	       if(title == ""){    	    	   
    	    	   Ext.Msg.alert("提示","第"+(orderNum+1)+"篇图文消息标题不能为空");
    	    	   chooseMaterial(orderNum);
    	    	   isValid = false;
    	    	   return;
    	       }
    	       if(content == ""){    	    	   
    	    	   Ext.Msg.alert("提示","第"+(orderNum+1)+"篇图文消息内容不能为空");
    	    	   chooseMaterial(orderNum);
    	    	   isValid = false;
    	    	   return;
    	       }
    	       if(thumb_media_id == ""){    	    	   
    	    	   Ext.Msg.alert("提示","请上传第"+(orderNum+1)+"篇图文消息的封面图片");
    	    	   chooseMaterial(orderNum);
    	    	   isValid = false;
    	    	   return;
    	       }
    	       
    	       articesData.push(rec.data);
    		}); 
    		
    		//删除文章
    		var removeData = [];
    		var removeRec = articesViewStore.getRemovedRecords();
    		for(var i=0; i< removeRec.length; i++){
    			removeData.push(removeRec[i].data);
    		}
    		
    		if(isValid){
    			formRec.articesData = articesData;
    			delete formRec.content;
    			delete formRec.title;
    			delete formRec.digest;
    			delete formRec.author;
    			
    			if(panel.actType == "update" && removeData.length > 0){
    				formRec.removeData = removeData;
        		}
    			
        		var tzParamsObj = {
        			ComID: 'TZ_WX_SCGL_COM',
        			PageID: 'TZ_WX_TWSC_STD',
        			OperateType: operateType,
        			comParams: formRec
        		}

        		var tzParams = Ext.JSON.encode(tzParamsObj);
        		Ext.tzSubmit(tzParams,function(respData){
                	if(panel.actType == "add"){
                		panel.actType = "update";
                		
                		var materialID = respData.materialID;
                		panel.materialID = materialID;
                		console.log("新增");
                		//加载文章 
                        articesViewStore.tzStoreParams = '{"jgId":"'+jgId+'","wxAppId":"'+wxAppId+'","materialID":"'+materialID+'"}';
                        articesViewStore.load({
                        	callback: function(){
                        		chooseMaterial(0);
                        	}
                        });
                	}
                	form.getForm().setValues(respData);
                	
                	//发布成功后处理逻辑
                	if(btnName == "publishBtn"){
                        var revokeBtn=panel.down("button[name=revokeBtn]");
                        var addArticesBtn = panel.down("button[name=addArticesBtn]");
                        if(respData.publishSta == "Y"){
                            revokeBtn.setDisabled(false);
                            addArticesBtn.setHidden(true)
                        }else{
                            revokeBtn.setDisabled(true);
                            addArticesBtn.setHidden(false)
                        }
                        
                        articesViewStore.reload();
                	}
                	
                	//确认保存并关闭
                	if(btnName == "ensureBtn"){
                		panel.close();
                	}
                },successTip,true,this);
    		}
    	}
    },

    
    /**
     * 确定保存
     */
    onEnsure: function(btn){
    	this.onSave(btn);
    },

	
	/**
	 * 发布
	 */
	publishToWx: function(btn){
		this.onSave(btn);
	},
	
	/**
	 * 撤销发布
	 */
	revokeFromWx: function(btn){
		var panel = btn.findParentByType('newsMaterialInfoPanel');
		var articesView = panel.down('dataview[reference=newsMaterialArticesView]');
    	var articesViewStore = articesView.getStore();
    	
    	var form = panel.child('form[reference=newsMaterialForm]');
    	var formRec = form.getForm().getValues();
    	
    	if(formRec["mediaId"] == ""){
    		Ext.Msg.alert("提示","当前素材尚未发布");
    		return;
    	}
    	
    	delete formRec.content;
		delete formRec.title;
		delete formRec.digest;
		delete formRec.author;
		
		var tzParamsObj = {
			ComID: 'TZ_WX_SCGL_COM',
			PageID: 'TZ_WX_TWSC_STD',
			OperateType: 'tzRevokeNewsMaterial',
			comParams: formRec
		}

		var tzParams = Ext.JSON.encode(tzParamsObj);
		Ext.tzSubmit(tzParams,function(respData){
			form.getForm().setValues(respData);
			
            var revokeBtn=panel.down("button[name=revokeBtn]");
            var addArticesBtn = panel.down("button[name=addArticesBtn]");
            if(respData.publishSta == "Y"){
                revokeBtn.setDisabled(false);
                addArticesBtn.setHidden(true)
            }else{
                revokeBtn.setDisabled(true);
                addArticesBtn.setHidden(false)
            }
             
            articesViewStore.reload();
        },"撤销成功",true,this);
	}
});
