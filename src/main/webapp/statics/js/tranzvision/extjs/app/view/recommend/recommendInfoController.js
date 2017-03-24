Ext.define('KitchenSink.view.recommend.recommendInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.recommendCon', 

	//可配置搜索
	cfgSearchAct: function(btn){
		Ext.tzShowCFGSearch({
			cfgSrhId: 'TZ_TJR_MANAGER_COM.TZ_TJR_DETAIL_STD.TZ_GD_TJRINF_VW',
			condition:
			{
				"TZ_JG_ID": Ext.tzOrgID
			},
			callback: function(seachCfg){
				var store = btn.findParentByType("grid").store;
				store.tzStoreParams = seachCfg;
				store.load();
			}
		});	
	},
	
	//查看推荐信
	viewLetter: function(grid, rowIndex,colIndex){
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }

        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        var refLetterID=record.get("letterID");
        var appInsID=record.get("appInsID");
        
        if(appInsID!=""){
        	var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appInsID+'","TZ_REF_LETTER_ID":"'+refLetterID+'","TZ_MANAGER":"Y"}}';
            var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+encodeURIComponent(tzParams);
            var mask ;
            var win = new Ext.Window({
                name:'applicationFormWindow',
                title : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.viewRefLetter","查看推荐信"),
                maximized : true,
                controller:'recommendCon',
                refLetterID :refLetterID,
                appInsID : appInsID,
                gridRecord:record,
                width : Ext.getBody().width,
                height : Ext.getBody().height,
                autoScroll : true,
                border:false,
                bodyBorder : false,
                isTopContainer : true,
                modal : true,
                resizable : false,
                items:[
                    new Ext.ux.IFrame({
                        xtype: 'iframepanel',
                        layout: 'fit',
                        style : "border:0px none;scrollbar:true",
                        border: false,
                        src : viewUrl,
                        height : "100%",
                        width : "100%"
                    })
                ],
                buttons: [ {
                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
                        iconCls:"close",
                        handler: function(){
                            win.close();
                        }
                    }]
            })
            win.show();
        }else{
        	Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.canNotFindRefLetter","找不到该推荐信"));
        }
    },

	//查看报名表
    viewApplicationForm: function(grid, rowIndex,colIndex){
        Ext.tzSetCompResourses("TZ_ONLINE_REG_COM");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_ONLINE_REG_COM"]["TZ_ONLINE_APP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }

        var store = grid.getStore();
        var record = store.getAt(rowIndex);
        var classID=record.get("classId");
        var oprID=record.get("oprID");
        var appInsID=record.get("appInsID");
        
        if(appInsID!=""){
            var tzParams='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+appInsID+'"}}';
            var viewUrl =Ext.tzGetGeneralURL()+"?tzParams="+encodeURIComponent(tzParams);
            var mask ;
            var win = new Ext.Window({
                name:'applicationFormWindow',
                title : Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.viewApplicationForm","查看报名表"),
                maximized : true,
                controller:'recommendCon',
                classID :classID,
                oprID :oprID,
                appInsID : appInsID,
                gridRecord:record,
                width : Ext.getBody().width,
                height : Ext.getBody().height,
                autoScroll : true,
                border:false,
                bodyBorder : false,
                isTopContainer : true,
                modal : true,
                resizable : false,
                items:[
                    new Ext.ux.IFrame({
                        xtype: 'iframepanel',
                        layout: 'fit',
                        style : "border:0px none;scrollbar:true",
                        border: false,
                        src : viewUrl,
                        height : "100%",
                        width : "100%"
                    })
                ],
                buttons: [ {
                    text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.audit","审批"),
                    iconCls:"send",
                    handler: "auditApplicationForm"
                },
                    {
                        text: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.close","关闭"),
                        iconCls:"close",
                        handler: function(){
                            win.close();
                        }
                    }]
            })
            win.show();
        }else{
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.cantFindAppForm","找不到该报名人的报名表"));
        }
    },
    
    //发送邮件
    sendEmail: function(btn){
    	var grid = btn.findParentByType("grid");
		var store = grid.getStore();
		var selList = grid.getSelectionModel().getSelection();
		
		//选中行长度
		var checkLen = selList.length;
		if(checkLen == 0){
			Ext.Msg.alert("提示","您没有选中任何记录");
			return ;
		}
		
		var personList = [];
		for(var i=0;i<checkLen;i++){
			var letterIDNum  = selList[i].get('letterID');
			personList.push({"letterIDNum":letterIDNum});
		};
		
		var params = {
		        "ComID":"TZ_TJR_MANAGER_COM",
		         "PageID":"TZ_TJR_DETAIL_STD",
		         "OperateType":"U",
		         "comParams":{"add":[{"personList":personList}]}
		};
		
		Ext.tzLoad(Ext.JSON.encode(params),function(audID){
			 Ext.tzSendEmail({
				//发送的邮件模板;
		        "EmailTmpName": ["TZ_ON_TRIAL_TG","TZ_ON_TRIAL_GQ"],
		         //创建的需要发送的听众ID;
		         "audienceId": audID,
		         //是否有附件: Y 表示可以发送附件,"N"表示无附件;
		         "file": "N"
			 });
		});
    },
    
    //邮件发送史
    sendEmailHistory: function(btn){
    	
    	var me = this;
    	var grid = btn.findParentByType("grid");
		var store = grid.getStore();
		var selList = grid.getSelectionModel().getSelection();
		
		//选中行长度
		var checkLen = selList.length;
		if(checkLen == 0|| checkLen > 1){
			Ext.Msg.alert("提示","必须只能选中一条记录");
			return ;
		}
		
		var email = selList[0].get('email');
		
		//是否有访问权限   
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_TJR_MANAGER_COM"]["TZ_TJR_EMAIL_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert('提示', '您没有修改数据的权限');
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_TJR_EMAIL_STD，请检查配置。');
			return;
		}
		
		var win = me.lookupReference('recommendWindow');
		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			win = new ViewClass();
			win.on('afterrender',function(window){
				var emailGrid = window.child('grid');
				emailGrid.store.tzStoreParams = '{"email":"'+email+'"}';
				emailGrid.store.load();
			})
			win.show();
		}
    },
    
	onComRegClose: function(btn){
		//关闭窗口
		this.getView().close();
	},
	
	//关闭窗口
	onWindowClose: function(btn){
		var win = btn.findParentByType("window");
		win.close();
	}
});
