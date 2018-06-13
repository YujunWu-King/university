Ext.define('KitchenSink.view.clueManagement.clueManagement.clueContactReportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.clueContactReportController',
    
    addReport:function(btn){
		var panel = btn.findParentByType("panel");
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_CONCT_ADRPT_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CONCT_ADRPT_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        var store = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_XSXS_CYDY_T',
            condition:{
            	TZ_JG_ID:{
                    value: Ext.tzOrgID,
                    operator:'01',
                    type:'01'
                },
                TZ_LABEL_STATUS:{
                    value:'Y',
                    operator:'01',
                    type:'01'
                }},
            result:'TZ_CYDY_ID,TZ_LABEL_NAME'
        });
        
        cmp = new ViewClass(btn.findParentByType("panel").down("timeLine").TZ_LEAD_ID,store,btn.findParentByType("panel").down("timeLine"));
        cmp.on("afterrender",function(panel){
            var leadID = panel.down("form").getForm().findField("leadID").getValue();
            panel.down("form").getForm().findField("leadID").setValue(leadID);
            panel.down("form").getForm().findField("LXBGID").setValue("NEXT");
        });
        cmp.show();
    },
    addReportSave:function(btn){
        //由于新增弹出窗口会使当前页面不能操作，弹出窗口对应的tab即是当前活动的Tab
        var activeTab = Ext.getCmp('tranzvision-framework-content-panel').getActiveTab(),
            data = btn.findParentByType('panel').down("form").getForm().getValues(),
            self = this,
            fileGrid = btn.findParentByType("window").down("form").down("grid"),
            mid = fileGrid.store.removedNode,
            removedNodes=[];
        
        if(mid){
            for(var x =0;x<mid.length;x++){
                delete mid[x].data.id;
                removedNodes.push(mid[x].data);
            }
            btn.findParentByType("window").down("form").down("grid").store.removedNode.length=0;
        }

        var fileEditJson="",
            fileEditRecs = fileGrid.getStore().getModifiedRecords();
        for(var i=0;i<fileEditRecs.length;i++) {
            if(fileEditRecs[i].data.attachmentName.match(/>([\s\S]*?)<\/a>/)){
                fileEditRecs[i].data.attachmentName = fileEditRecs[i].data.attachmentName.match(/>([\s\S]*?)<\/a>/)[1];
            }
            delete fileEditRecs[i].data.id;
            if (fileEditJson == "") {
                fileEditJson = '{"typeFlag":"ATTACH","data":' + Ext.JSON.encode(fileEditRecs[i].data) + '}';
            } else {
                fileEditJson = fileEditJson + ',{"typeFlag":"ATTACH","data":' + Ext.JSON.encode(fileEditRecs[i].data) + '}';
            }
        }

        //表单校验未通过
        var form = btn.findParentByType('panel').down("form").getForm();
        if(!form.isValid()){
            return false;
        }

        var updateParams = '{"typeFlag":"BASIC","data":' + Ext.JSON.encode(data) + '}';
        if(fileEditJson != ""){
            updateParams = updateParams+','+fileEditJson;
        }

        var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_CONNECT_RPT_STD","OperateType":"U","comParams":{"update":[' + updateParams + '],"delete":'+Ext.JSON.encode(removedNodes)+'}}';
        Ext.tzSubmit(tzParams,function(responseData){
        	var reportId = responseData.reportId;
        	
            var leadID = btn.findParentByType("window").down("form").getForm().findField("leadID").getValue();
            if(reportId != "" && reportId != undefined){
                btn.findParentByType("window").down("form").getForm().findField("LXBGID").setValue(reportId);
            }

            //刷新列表
            fileGrid.store.reload();
            
            //线索详情页面和线索详情查看页面操作时，刷新联系报告tabpanel的内容
//            if(activeTab.reference=="clueDetailPanel"||activeTab.reference=="clueDetailViewPanel") {
//                self.loadPoints(leadID, activeTab.down("timeLine"));
//            }
            
            var timeLineObj = btn.findParentByType("window").timeLineObj;
            if(timeLineObj){
            	self.loadPoints(leadID, timeLineObj);
            }

            if(btn.name=="reportEnsureBtn") {
                btn.findParentByType("window").close();
            }

        },"",true,this);
    },
    addReportClose:function(btn){
        btn.findParentByType("window").close();
    },
    addAttach : function(file, value){
        var form = file.findParentByType("form").getForm();
        
        if(value!="") {
        	var upUrl = TzUniversityContextPath + '/UpdServlet?filePath=clueReport';

            var myMask = new Ext.LoadMask({
                msg    : '加载中...',
                target : Ext.getCmp('tranzvision-framework-content-panel')
            });

            myMask.show();

            form.submit({
                url: upUrl,
                success: function (form, action) {
                    var accessPath = action.result.msg.accessPath;
                    if (accessPath.length == (accessPath.lastIndexOf("/") + 1)) {
                        accessPath = accessPath + action.result.msg.sysFileName;
                    } else {
                        accessPath = accessPath + "/" + action.result.msg.sysFileName;
                    }

                    var applyItemGrid = file.findParentByType("grid")
                    var r = Ext.create('KitchenSink.view.clueManagement.clueManagement.MsgAttachmentModel', {
                        "attachmentID": 'NEXT',
                        "attachmentSysName": action.result.msg.sysFileName,
                        "attachmentName": action.result.msg.filename,
                        "attachmentUrl": accessPath
                    });
                    applyItemGrid.store.insert(0, r);
                        
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
    findCmpParent : function(ele){
        //根据当前DOM节点，向上查找最近的包含EXT节点对象的节点并返回该EXT节点对象
        if(ele){
            while(ele.parentNode && !Ext.getCmp(ele.parentNode.id)){
                ele = ele.parentNode;
            }
            return Ext.getCmp(ele.parentNode.id);
        }else{
            return false;
        }
    },
    editData:function(e,com){
        //是否有访问权限
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_XSXS_INFO_COM"]["TZ_CONCT_ADRPT_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert('提示', '您没有修改数据的权限');
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert('提示', '未找到该功能页面对应的JS类，页面ID为：TZ_CONCT_ADRPT_STD，请检查配置。');
            return;
        }
        var contentPanel, cmp, ViewClass, clsProto;

        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');
        contentPanel.body.addCls('kitchensink-example');

        if(!Ext.ClassManager.isCreated(className)){
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        var store = new KitchenSink.view.common.store.comboxStore({
            recname:'TZ_XSXS_CYDY_T',
            condition:{
                TZ_LABEL_STATUS:{
                    value:'Y',
                    operator:'01',
                    type:'01'
                }},
            result:'TZ_CYDY_ID,TZ_LABEL_NAME'
        });
        cmp = new ViewClass(com.TZ_LEAD_ID,store,com);

        var form = this.findCmpParent(e.target).findParentByType("timePoint").down('form');
        cmp.on("afterrender",function(panel){
            var LXBGID = form.getForm().findField("LXBGID").getValue(),
            	title = form.getForm().findField("title").getValue(),
                date = form.getForm().findField("date").getValue(),
                detail = form.getForm().findField("detail").getValue().replace(/<br\/>/g, "\r\n"),
                files = form.getForm().findField("files").getValue().split('</br>');

            var targetForm = panel.down("form").getForm(),
                grid = panel.down("form").down("grid");
            
            targetForm.setValues({
                LXBGID:LXBGID,
                title:title,
                date:date,
                detail:detail
            });
            
            grid.store.tzStoreParams = '{"LXBGID":"'+ LXBGID +'"}';
            grid.store.load();
        });
        cmp.show();
    },
    deleteData:function(e,com){
        var timeLine = this.findCmpParent(e.target).findParentByType("timeLine"),
            targetTimePoint = this.findCmpParent(e.target).findParentByType("timePoint");
        Ext.MessageBox.confirm("提示","确定删除当前联系报告?",function(op){
            if(op=="yes"){
                var leadId = com.findParentByType("panel").TZ_LEAD_ID;
                var LXBGID = targetTimePoint.down("form").getForm().findField("LXBGID").getValue();
                var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_CONNECT_RPT_STD","OperateType":"rm","comParams":{"leadID":"'+leadId+'","LXBGID":"'+LXBGID+'"}}';
                Ext.tzLoad(tzParams,function(responseData){
                    timeLine.remove(targetTimePoint);
					if(timeLine.child("timePoint")==null){
						timeLine.setHidden(true);
					};
                });
            }
        });

    },
    deleteArtAttenments:function(btn,rowIndex){
        //选中行
        var store = btn.findParentByType('grid').getStore();
        var selList = btn.findParentByType('grid').getSelectionModel().getSelection();
        //选中行长度
        var checkLen = selList.length;
        if(rowIndex.toString().match(/^\d+$/)){
            Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
                if(btnId == 'yes'){
                    store.removedNode instanceof Array ? store.removedNode.push(store.getAt(rowIndex)):(store.removedNode =[store.getAt(rowIndex)]);
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
                        store.removedNode = store.removedNode instanceof Array ? store.removedNode.concat(selList):selList;
                        store.remove(selList);
                    }
                },this);
            }

        }
    },
    loadPoints:function(leadID,timeLine){
        var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_CONNECT_RPT_STD","OperateType":"QF","comParams":{"leadID":"'+leadID+'"}}';
        Ext.tzLoad(tzParams,function(responseData){
        	var reports = responseData.reports;
            var items=[];
            reports.sort(function(a,b){
                    return new Date(b.addedDttm) - new Date(a.addedDttm);
                }).forEach(function(v,index){
                    items.push({
                        xtype: 'timePoint',
                        autoLine: true,
                        sinceText: '<span style="font-size:16px;font-weight:bold;">' + v.addedOprName+ '</span><br>' + v.addedDttm,
                        imgSrc: v.imgSrc,
                        content: {
                            xtype: 'form',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            fieldDefaults: {
                                msgTarget: 'side',
                                labelWidth: 110,
                                labelStyle: 'font-weight:bold;color:#35baf6'
                            },
                            items:[{
                                xtype:'container',
                                html:'<div style="float:right"><span data-click="editData" class="edit" style="cursor:pointer;display:inline-block;width:24px;height:24px;" alt="编辑"></span><span data-click="deleteData" class="delete" style="cursor:pointer;display:inline-block;width:24px;height:24px;" alt="删除"></span></div>'
                            },{
                                xtype:'displayfield',
                                name:"LXBGID",
                                value: v.reportID,
                                hidden:true
                            },{
                                xtype:'displayfield',
                                fieldLabel: "主题",
                                name:"title",
                                value: v.title
                            },{
                                xtype:'displayfield',
                                fieldLabel: "日期",
                                name:"date",
                                value: v.date
                            },{
                                xtype:'displayfield',
                                fieldLabel: "详细信息",
                                name:"detail",
                                value: v.detail
                            },{
                                xtype:'displayfield',
                                fieldLabel: "附件",
                                name:"files",
                                hidden: (function(){
                                	return v.files.length == 0 ? true : false;
                                })(),
                                value: (function(){
                                    var result="";
                                    for(var x= v.files.length-1;x>=0;x--){
                                        var src = v.files[x].path.split("/");
                                        result+='<a download data-sys="'+ src[src.length-1]+'" style="line-height:20px;" href="'+ TzUniversityContextPath + v.files[x].path+'">'+v.files[x].name+'</a></br>';
                                    }
                                    return result.replace(/<\/br>$/,"");
                                })()
                            }]
                        }
                    });
                });
            var nowitems = Array.prototype.slice.call(timeLine.items.items);
            if(items.length!==0) {
                timeLine.add(items);
				timeLine.setVisible(true);
            }else{
				timeLine.setHidden(true);
			}
            if(nowitems){
                nowitems.forEach(function(v){
                    timeLine.remove(v);
                });
            }
        });
    },
    //销售线索只读页面
    loadPointsView:function(leadID,timeLine){
        var tzParams = '{"ComID":"TZ_XSXS_INFO_COM","PageID":"TZ_CONNECT_RPT_STD","OperateType":"QF","comParams":{"leadID":"'+leadID+'"}}';
        Ext.tzLoad(tzParams,function(responseData){
            var items=[];
            responseData
                .sort(function(a,b){
                    return new Date(b.addedDttm) - new Date(a.addedDttm);
                })
                .forEach(function(v,index){
                    items.push({
                        xtype: 'timePoint',
                        autoLine: true,
                        sinceText: v.addedDttm,
                        imgSrc: v.imgSrc,
                        content: {
                            xtype: 'form',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            fieldDefaults: {
                                msgTarget: 'side',
                                labelWidth: 110,
                                labelStyle: 'font-weight:bold;color:#35baf6'
                            },
                            items:[/*{
                                xtype:'container',
                                html:'<div style="float:right"><span data-click="editData" style="cursor:pointer;display:inline-block;width:24px;height:24px;background:url(/tranzvision/kitchensink/maple/resources/images/icons/toolbar/edit.png) no-repeat center center" alt="编辑"></span><span data-click="deleteData" style="cursor:pointer;display:inline-block;width:24px;height:24px;background:url(/tranzvision/kitchensink/maple/resources/images/icons/toolbar/minus.png) no-repeat center center" alt="删除"></span></div>'
                            },*/{
                                xtype:'displayfield',
                                name:"LXBGID",
                                value: v.reportID,
                                hidden:true
                            },{
                                xtype:'displayfield',
                                fieldLabel: "主题",
                                name:"title",
                                value: v.title
                            },{
                                xtype:'displayfield',
                                fieldLabel: "日期",
                                name:"date",
                                value: v.date
                            },{
                                xtype:'displayfield',
                                fieldLabel: "详细信息",
                                name:"detail",
                                value: v.detail
                            },{
                                xtype:'displayfield',
                                fieldLabel: "附件",
                                name:"files",
                                value: (function(){
                                    var result="";
                                    for(var x= v.files.length-1;x>=0;x--){
                                        var src = v.files[x].path.split("/");
                                        result+='<a download data-sys="'+ src[src.length-1]+'" href="'+ v.files[x].path+'">'+v.files[x].name+'</a></br>';
                                    }
                                    return result.replace(/<\/br>$/,"");
                                })()
                            }]
                        }
                    });
                });
            var nowitems = Array.prototype.slice.call(timeLine.items.items);
            if(items.length!==0) {
                timeLine.add(items);
				timeLine.setHidden(false);
            }else{
				timeLine.setHidden(true);
			}
            if(nowitems){
                nowitems.forEach(function(v){
                    timeLine.remove(v);
                });
            }
        });
    }
});