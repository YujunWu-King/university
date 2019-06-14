Ext.define('KitchenSink.view.classManage.classInfoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ClassInfo',
	//选择班级所属项目
	xm_idChoice: function(btn){
		var form = this.getView().child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_PRJ_INF_T',
			searchDesc: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ssssxm","搜索所属项目"),
			maxRow:20,
			condition:{
				presetFields:{
					TZ_JG_ID:{
						value: Ext.tzOrgID,
						type: '01'	
					},
					TZ_IS_OPEN:{
						value: 'Y',
						type: '01'	
					}
				},
				srhConFields:{
					TZ_PRJ_ID:{
						desc:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xmflbh","项目分类编号"),
						operator:'01',
						type:'01'	
					},
					TZ_PRJ_NAME:{
						desc:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xmflmc","分类名称"),
						operator:'01',
						type:'01'		
					}
				}
			},
			srhresult:{
				TZ_PRJ_ID:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xmflbh","项目分类编号"),
				TZ_PRJ_NAME:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xmflmc","分类名称")	
			},
			multiselect: false,
			callback: function(selection){
				form.findField("xm_id").setValue(selection[0].data.TZ_PRJ_ID);
				form.findField("xm_id_desc").setValue(selection[0].data.TZ_PRJ_NAME);
			}
		});	
	},
	//选择材料/面试评审成绩模型
    choiceScoreModal: function(btn){
        var fieldName = btn.name;
        var searchDesc,modal,modal_desc;
        if(fieldName=='clps_cj_modal'){
            searchDesc=Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ssclpscjmx","搜索材料评审成绩模型");
            modal="clps_cj_modal";
            modal_desc="clps_cj_modal_desc";
        }else if(fieldName=="msps_cj_modal"){
            searchDesc=Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ssmspscjmx","搜索面试评审成绩模型");
            modal="msps_cj_modal";
            modal_desc="msps_cj_modal_desc";
        }else if(fieldName=="csmb_id"){
            searchDesc=Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ssmspscjmx","搜索自动初筛成绩模型");
            modal="csmb_id";
            modal_desc="csmb_desc";
        }
        var form = this.getView().child("form").getForm();
        Ext.tzShowPromptSearch({
            recname: 'TZ_RS_MODAL_TBL',
            searchDesc: searchDesc,
            maxRow:20,
            condition:{
                presetFields:{
                    TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    },
                    TZ_MODAL_FLAG:{
                        value: 'Y',
                        type: '01'
                    }
                },
                srhConFields:{
                    TZ_SCORE_MODAL_ID:{
                        desc:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.cjmxid","成绩模型ID"),
                        operator:'07',
                        type:'01'
                    },
                    TZ_MODAL_NAME:{
                        desc:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.cjmxms","成绩模型描述"),
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
                TZ_SCORE_MODAL_ID: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.cjmxid","成绩模型ID"),
                TZ_MODAL_NAME:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.cjmxms","成绩模型描述")
            },
            multiselect: false,
            callback: function(selection){
                form.findField(modal).setValue(selection[0].data.TZ_SCORE_MODAL_ID);
                form.findField(modal_desc).setValue(selection[0].data.TZ_MODAL_NAME);
            }
        });
    },
    
  //选择标签组 20170118 ZXW
    choiceBqzModal: function(btn){
        var fieldName = btn.name;
        var searchDesc,modal,modal_desc;
        if(fieldName=='zdbq_id'){
            searchDesc=Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bqz","搜索自动标签组");
            modal="zdbq_id";
            modal_desc="zdbq_desc";
        }else if(fieldName=="fmqd_id"){
            searchDesc=Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bqz","搜索负面清单标签组");
            modal="fmqd_id";
            modal_desc="fmqd_desc";
        }
        var form = this.getView().child("form").getForm();
        Ext.tzShowPromptSearch({
            recname: 'TZ_BIAOQZ_T',
            searchDesc: searchDesc,
            maxRow:20,
            condition:{
                presetFields:{
                    TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    }
                },
                srhConFields:{
                	TZ_BIAOQZ_ID:{
                        desc:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bqzid","标签组编号"),
                        operator:'07',
                        type:'01'
                    },
                    TZ_BIAOQZ_NAME:{
                        desc:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bqzname","标签组名称"),
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
            	TZ_BIAOQZ_ID: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bqzid","标签组编号"),
            	TZ_BIAOQZ_NAME:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bqzname","标签组名称")
            },
            multiselect: false,
            callback: function(selection){
                form.findField(modal).setValue(selection[0].data.TZ_BIAOQZ_ID);
                form.findField(modal_desc).setValue(selection[0].data.TZ_BIAOQZ_NAME);
            }
        });
    },
    //选择在线调查
    choiceSurveyModal: function(btn){
        var fieldName = btn.name;

        var form = this.getView().child("form").getForm();
        Ext.tzShowPromptSearch({
            recname: 'PS_TZ_DC_WJ_DY_T',
            searchDesc: "搜索在线调查",
            maxRow:20,
            condition:{
                presetFields:{
                    TZ_JG_ID:{
                        value: Ext.tzOrgID,
                        type: '01'
                    }
                },
                srhConFields:{
                	TZ_DC_WJ_ID:{
                        desc:"在线调查ID",
                        operator:'07',
                        type:'01'
                    },
                    TZ_DC_WJBT:{
                        desc:"在线调查名称",
                        operator:'07',
                        type:'01'
                    }
                }
            },
            srhresult:{
            	TZ_DC_WJ_ID: "在线调查ID",
            	TZ_DC_WJBT: "在线调查名称"
            },
            multiselect: false,
            callback: function(selection){
                form.findField("surveyId").setValue(selection[0].data.TZ_DC_WJ_ID);
                form.findField("surveyName").setValue(selection[0].data.TZ_DC_WJBT);
            }
        });
    },
	//选择班级在线报名表模板
	bmb_mbChoice: function(field){
        var fieldName = field.name;
        var searchDesc,modal,modal_desc;
        if(fieldName=='bmb_mb'){
            searchDesc=Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.ssbmbmb","搜索报名表模板");
            modal="bmb_mb";
            modal_desc="bmb_mb_desc";
        }else if(fieldName=="psbmb_mb"){
            searchDesc=Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.sspsybmbmb","搜索评审用报名表模版");
            modal="psbmb_mb";
            modal_desc="psbmb_mb_desc";
        }
		var form = this.getView().child("form").getForm();
		Ext.tzShowPromptSearch({
			recname: 'TZ_APPTPL_DY_T',
			searchDesc: searchDesc,
			maxRow:20,
			condition:{
				presetFields:{
					TZ_JG_ID:{
						value: Ext.tzOrgID,
						type: '01'	
					},
					TZ_EFFEXP_ZT:{
						value: 'Y',
						type: '01'	
					}
				},
				srhConFields:{
					TZ_APP_TPL_ID:{
						desc:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmbmbid","报名表模板ID"),
						operator:'01',
						type:'01'
					},
					TZ_APP_TPL_MC:{
						desc:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmbmbmc","报名表模板名称"),
						operator:'01',
						type:'01'
					}
				}
			},
			srhresult:{
				TZ_APP_TPL_ID:Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmbmbid","报名表模板ID"),
				TZ_APP_TPL_MC: Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bmbmbmc","报名表模板名称")
			},
			multiselect: false,
			callback: function(selection){
                form.findField(modal).setValue(selection[0].data.TZ_APP_TPL_ID);
                form.findField(modal_desc).setValue(selection[0].data.TZ_APP_TPL_MC);
			}
		});
	},

	//删除专业方向(title)
	deleteZyfxT: function(){
	   //选中行
	   //var selList = this.getView().getSelectionModel().getSelection();
	   var selList = this.getView().down('grid[name=zyfx_save]').getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzyscdjl","请选择要删除的记录"));   
			return;
	   }else{
			Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗"), function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.getView().down('grid[name=zyfx_save]').store;
				   store.remove(selList);
                    var rowCount = store.getCount( );
                    var records = store.getRange();
                    var num=0;
                    for (var i = 0; i < records.length; i++) {
                        num+=1;
                        var record = records[i];
                        record.set('fx_xh',num);
                    }
				}												  
			},this);   
	   }
	},
	//编辑专业方向（title）
	editZyfxT:function(){
		var selList = this.getView().down('grid[name=zyfx_save]').getSelectionModel().getSelection();
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzytyxgdjl","请选择一条要修改的记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.znxzytyxgdjl","只能选择一条要修改的记录"));
            return;
        }
        var bj_id= selList[0].get("bj_id");
		var fx_id= selList[0].get("fx_id");
        this.editZyfxByID(bj_id,fx_id);
    },
	//编辑专业方向（list）
    editZyfx: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
   	 	var bj_id = selRec.get("bj_id");
		var fx_id = selRec.get("fx_id");
     	this.editZyfxByID(bj_id,fx_id);
    },
	editZyfxByID: function(bj_id,fx_id){
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJGL_COM"]["TZ_BJ_ZYFX_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
        var store = this.getView().down('grid[name=zyfx_save]').store;
        if(store.getRemovedRecords().length>0||store.getModifiedRecords().length>0){
            this.onFormSave(btn);
        };
		var win = this.lookupReference('ZyfxDetail');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
		win.actType = "update";
		var comSiteParams = this.getView().child("form").getForm().getValues();
		var bj_id = comSiteParams["bj_id"];
		var form = win.child('form').getForm();
		var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_ZYFX_STD","OperateType":"QF","comParams":{"bj_id":"'+bj_id+'","fx_id":"'+fx_id+'"}}';
		Ext.tzLoad(tzParams,function(responseData){
			var formData = responseData.formData;
			form.setValues(formData);
			form.setValues({bj_id:bj_id});
			form.findField('fx_id').setReadOnly(true);
		});
        win.show();
	},
	//添加专业方向
    addZyfx: function(btn) {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxbcbjjbxx","请先保存班级基本信息"));
			return;
		};
        var store = this.getView().down('grid[name=zyfx_save]').store;
        if(store.getRemovedRecords().length>0||store.getModifiedRecords().length>0){
            this.onFormSave(btn);
        };
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJGL_COM"]["TZ_BJ_ZYFX_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var win = this.lookupReference('ZyfxDetail');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
		win.actType = "add";
		var comSiteParams = this.getView().child("form").getForm().getValues();
		var bj_id = comSiteParams["bj_id"];
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({bj_id:bj_id});
        win.show();
    },
	//编辑批次管理(title)
	editPcglT:function(){
		var selList = this.getView().down('grid[name=pcgl_save]').getSelectionModel().getSelection();
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzytyxgdjl","请选择一条要修改的记录"));
            return;
        }else if(checkLen >1){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.znxzytyxgdjl","只能选择一条要修改的记录"));
            return;
        }
        var bj_id= selList[0].get("bj_id");
		var pc_id= selList[0].get("pc_id");
        this.editPcglByID(bj_id,pc_id);
    },
	//编辑批次管理
    editPcgl: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
   	 	var bj_id = selRec.get("bj_id");
		var pc_id = selRec.get("pc_id");
     	this.editPcglByID(bj_id,pc_id);
    },
	editPcglByID: function(bj_id,pc_id){
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJGL_COM"]["TZ_BJ_PCGL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
        var store = this.getView().down('grid[name=pcgl_save]').store;
        if(store.getRemovedRecords().length>0||store.getModifiedRecords().length>0){
            this.onFormSave(btn);
        };
		var win = this.lookupReference('PcglDetail');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
		win.actType = "update";
		//var comSiteParams = this.getView().child("form").getForm().getValues();
		//var bj_id = comSiteParams["bj_id"];
		var form = win.child('form').getForm();
		var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_PCGL_STD","OperateType":"QF","comParams":{"bj_id":"'+bj_id+'","pc_id":"'+pc_id+'"}}';
		Ext.tzLoad(tzParams,function(responseData){
			var formData = responseData.formData;
			form.setValues(formData);
			form.setValues({bj_id:bj_id});
		});
        win.show();
	},
	//添加批次管理
    addPcgl: function(btn) {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxbcbjjbxx","请先保存班级基本信息"));
			return;
		};
        var store = this.getView().down('grid[name=pcgl_save]').store;
        if(store.getRemovedRecords().length>0||store.getModifiedRecords().length>0){
            this.onFormSave(btn);
        };
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJGL_COM"]["TZ_BJ_PCGL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var win = this.lookupReference('PcglDetail');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
		win.actType = "add";
		var comSiteParams = this.getView().child("form").getForm().getValues();
		var bj_id = comSiteParams["bj_id"];
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({bj_id:bj_id});
        win.show();
    },
	//删除专业方向
    deleteZyfx: function(view, rowIndex){
    	Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
                var rowCount = store.getCount( );
                var records = store.getRange();
                var num=0;
                for (var i = 0; i < records.length; i++) {
                    num+=1;
                    var record = records[i];
                    record.set('fx_xh',num);
                }
			}												  
		},this);
    },
	//删除管理人员
    deleteGlry: function(view, rowIndex){
    	Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
	//删除批次管理
    deletePcgl: function(view, rowIndex){
    	Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
	//删除批次管理(title)
	deletePcglT: function(){
	   //选中行
	   //var selList = this.getView().getSelectionModel().getSelection();
	   var selList = this.getView().down('grid[name=pcgl_save]').getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzyscdjl","请选择要删除的记录"));   
			return;
	   }else{
			Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.getView().down('grid[name=pcgl_save]').store;
				   store.remove(selList);
				}												  
			},this);   
	   }
	},
	//删除管理人员(title)
	deleteGlryT: function(){
	   //选中行
	   //var selList = this.getView().getSelectionModel().getSelection();
	   var selList = this.getView().down('grid[name=glry_save]').getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzyscdjl","请选择要删除的记录"));   
			return;
	   }else{
			Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.getView().down('grid[name=glry_save]').store;
				   store.remove(selList);
				}												  
			},this);   
	   }
	},
	//添加管理人员(list)
	addGlry: function(view, rowIndex){
		var win = this.lookupReference('userWindows');
        if (!win) {
            win = new KitchenSink.view.classManage.userWindows();
			win.multiSel = 'SINGLE';
			win.rowNum = rowIndex;
            this.getView().add(win);
        }
        win.show();	
	},
	//添加管理人员（title）
	addGlryT: function(btn){
		var win = this.lookupReference('userWindows');
        if (!win) {
            win = new KitchenSink.view.classManage.userWindows();
            this.getView().add(win);
        }
        win.show();
	},
	//确认添加人员，需要验证不能添加重复的人员到人员管理中
	onUserChoose: function(btn){
		var win = btn.findParentByType('window');
		var multiSel = win.multiSel;
		var rowNum = win.rowNum;
		var grid = win.child('grid');
		var selList = grid.getView().getSelectionModel().getSelection();
		if (selList.length == 0){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzyhjl","请选择用户记录"));
			return;	
		} else {
			if (multiSel == 'SINGLE' && selList.length > 1){
				Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.znxzytjl","只能选择一条记录"));
				return;			
			}else{
				var isRept = false;
				var ManagerGrid = this.getView().down('tabpanel').getActiveTab();
				for(var i=0;i<selList.length; i++){
					if (ManagerGrid.getStore().find("ry_id",selList[i].data.userOprid,0,false,true,true) != -1){
						isRept = true;
						break;
					}
				}
				if (!isRept){
					for(var i=0;i<selList.length; i++){
						if (multiSel == 'SINGLE'){
							var row = rowNum + i + 1;
						} else {
							var row = ManagerGrid.getStore().getCount();
						}
						var _bj_id=this.getView().child("form").getForm();
						var _form_v=_bj_id.getValues();
						var _bj=_form_v["bj_id"];
						var model = new KitchenSink.view.classManage.userModel({
							bj_id:_bj,
							ry_id: selList[i].data.userOprid, 
							gl_name: selList[i].data.userName,
							gl_phone: selList[i].data.userPhone,
							gl_email: selList[i].data.userEmail
						});	
						ManagerGrid.getStore().insert(row, model);
					}
					win.close();
				} else {
					Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.xztjdryycz","选择添加的人员已存在"));
				}
			}
		}
	},
	//关闭添加管理人员页面
	onUserClose: function(btn){
		var win = btn.findParentByType('window');
		win.close();
	},
	//报名流程设置-回复语设置
	bmlcMrnrXs:function(grid, rowIndex, colIndex){
		Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_HFY_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
            return;
        }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        cmp = new ViewClass();
        var record = grid.store.getAt(colIndex);
        var classID = record.data.bj_id;
		var lc_id = record.data.bmlc_id;
		cmp.class_id=classID;
		cmp.bmlc_id=lc_id;
        cmp.on('afterrender',function(panel){
            var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFY_STD","OperateType":"QG","comParams":{"bj_id":"'+classID+'","bmlc_id":"'+lc_id+'"}}';
            /*Ext.tzLoad(tzParams,function(respData){
                var formData = respData.formData;
                form.setValues(formData);
            });*/
			var tzStoreParams = '{"bj_id":"'+classID+'","bmlc_id":"'+lc_id+'"}';
            //panel.child('panel').child('grid').store.tzStoreParams = tzStoreParams;
			panel.store.tzStoreParams = tzStoreParams;
        });
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
	bmlcMrnrXs2: function(grid, rowIndex, colIndex) {
		Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_HFY_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
		//var store = view.findParentByType("grid").store;
	 	var selRec = grid.store.getAt(colIndex);
   	 	var bj_id = selRec.get("bj_id");
		var bmlc_id = selRec.get("bmlc_id");
        cmp = new ViewClass(bj_id,bmlc_id);
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    bmlcMrnrXs1: function(view,t,rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
		var bmlc_desc = selRec.get("bmlc_desc");
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJGL_COM"]["TZ_BJ_LCXS_STD"];
		var className = pageResSet["jsClassName"];
		var win = this.lookupReference('BmlcXsnr1');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
		win.actType = "update";
		var form = win.child('form').getForm();
		form.setValues({bmlc_desc:bmlc_desc});
		form.setValues({h_no:rowIndex});
		win.show();
    },
	//递交资料设置-常用短信设置
	DjzlDyhf:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(colIndex);
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJGL_COM"]["TZ_BJ_ZLDX_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var win = this.lookupReference('DjzlDxNr');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
		win.bj_id =rec.data.bj_id; 
		//rec.get('bj_id');
		win.djzl_id =rec.data.djzl_id;
		//rec.get('djzl_id');
		if(win.djzl_id==""){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxbcdjzlxx","请先保存递交资料信息"));
			return;
		}
		win.actType = "update";
		var grid = win.child('grid');
		/*var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_ZLDX_STD","OperateType":"QF","comParams":{"bj_id":"'+win.smtDtTmpID+'","djzl_id":"'+win.smtDtID+'"}}';
		Ext.tzLoad(tzParams,function(responseData){
			var roleList = responseData.listData;	
			var tzStoreParams = '{"bj_id":"'+win.smtDtTmpID+'","djzl_id":"'+win.smtDtID+'"}';
			grid.store.tzStoreParams = tzStoreParams;
			grid.store.load();					
		});*/
		
		//var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_ZLDX_STD","OperateType":"QG","comParams":{"bj_id":"'+win.smtDtTmpID+'","djzl_id":"'+win.smtDtID+'"}}';
		var tzStoreParams = '{"bj_id":"'+win.bj_id+'","djzl_id":"'+win.djzl_id+'"}';
		grid.store.tzStoreParams = tzStoreParams;
		grid.store.load();
		win.show();
	},
	//递交资料设置--常用回复短语，添加最后一行
	addLastBackMsg: function(){
		var win = this.lookupReference('DjzlDxNr');
		var bj_id = win.bj_id;
	    var djzl_id = win.djzl_id;
		var profeGrid = this.lookupReference('DjzlDxNrGrid');
		var cellEditing = profeGrid.getPlugin('dataCellediting');
		var profeStore = profeGrid.getStore();
		var rowCount = profeStore.getCount();
		var model = new KitchenSink.view.classManage.DjzlDxNrModel({
            bj_id: bj_id,
            djzl_id: djzl_id,
            hfdx_id: '',
			hfdx_desc:''
        });

		profeStore.insert(rowCount, model);
		cellEditing.startEditByPosition({
            row: rowCount,
            column: 0
        });
	},
	//递交资料设置--常用回复短语添加一行
	addBackMsg: function(view, rowIndex){
		var win = this.lookupReference('DjzlDxNr');
		var bj_id = win.bj_id;
	    var djzl_id = win.djzl_id;
		var rec = new KitchenSink.view.classManage.DjzlDxNrModel({
            bj_id: bj_id,
            djzl_id: djzl_id,
            hfdx_id: '',
			hfdx_desc:''
        });
		var grid = this.lookupReference('DjzlDxNrGrid');
		var cellEditing = grid.getPlugin('dataCellediting');
        view.getStore().insert(rowIndex+1, rec);
        cellEditing.startEditByPosition({
            row: rowIndex+1,
            column: 0
        });
	},
	//递交资料设置---常用回复短语删除一行
	deleteBackMsg: function(grid, rowIndex){
		var store = grid.getStore();
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					      
			   store.removeAt(rowIndex);
			}												  
		},this);  
	},
	//递交资料设置---常用回复短语删除(title)
	deleteBackMsgT: function(){
	   //选中行
	   //var selList = this.getView().down('grid[name=zyfx_save]').getSelectionModel().getSelection();
	   var selList = this.lookupReference('DjzlDxNrGrid').getView().getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzyscdjl","请选择要删除的记录"));   
			return;
	   }else{
			Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.lookupReference('DjzlDxNrGrid').getView().store;
				   store.remove(selList);
				}												  
			},this);   
	   }
	},
	//递交资料设置---常用短信保存
	onBackMsgSave: function(btn){
		var win = this.lookupReference('DjzlDxNr');
		var bj_id = win.bj_id;
	    var djzl_id = win.djzl_id;

		//var djzl_grid=this.getView().down('grid[name=applyItemGrid1]').getStore();
		var grid = win.child("grid").getStore();
		//var grid = win.child("grid");
		var xs_grid=grid.getRemovedRecords();
		var remove_str="";
		var edit_str="";
		var comParams="";
		for(var i=0;i<xs_grid.length;i++){
			if(remove_str == ""){
				remove_str = '{"typeFlag":"xs","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(xs_grid[i].data)+'}';
			}else{
				remove_str = remove_str + ',{"typeFlag":"xs","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(xs_grid[i].data)+'}';
	  		}
		}
		var xs_edit=grid.getModifiedRecords();
		for(var i=0;i<xs_edit.length;i++){
			if(edit_str == ""){
				edit_str = '{"typeFlag":"xs","data":'+Ext.JSON.encode(xs_edit[i].data)+'}';
			}else{
				edit_str = edit_str + ',{"typeFlag":"xs","data":'+Ext.JSON.encode(xs_edit[i].data)+'}';
			}
		}

		if(remove_str != ""){
			if(comParams == ""){
				comParams = '"delete":[' + remove_str + "]";
			}else{
				comParams = comParams + ',"delete":[' + remove_str + "]";
			}
		}
		if(edit_str != ""){
			if(comParams == ""){
				comParams = '"update":[' + edit_str + "]";
			}else{
				comParams = comParams + ',"update":[' + edit_str + "]";
			}
		}
		//结束报名信息项;
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_ZLDX_STD","OperateType":"U","comParams":{'+comParams+'}}';
		
		//var store = grid.getStore();
		//var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_ZLDX_STD","OperateType":"U","comParams":{"bj_id":"'+bj_id+'","djzl_id":"'+djzl_id+'"}}';
		//var tzParams = this.getBackMsgParams(btn);
		var comView = this.getView();
		Ext.tzSubmit(tzParams,function(responseData){
			var tzStoreParams = '{"bj_id":"'+bj_id+'","djzl_id":"'+djzl_id+'"}';
			grid.tzStoreParams = tzStoreParams;
			comView.actType = "update";	
			grid.reload();
		});
	},
	//递交资料设置---常用短信确定
	onBackMsgSure: function(btn){
		var win = this.lookupReference('DjzlDxNr');
		var bj_id = win.bj_id;
	    var djzl_id = win.djzl_id;

		//var djzl_grid=this.getView().down('grid[name=applyItemGrid1]').getStore();
		var grid = win.child("grid").getStore();
		//var grid = win.child("grid");
		var xs_grid=grid.getRemovedRecords();
		var remove_str="";
		var edit_str="";
		var comParams="";
		for(var i=0;i<xs_grid.length;i++){
			if(remove_str == ""){
				remove_str = '{"typeFlag":"xs","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(xs_grid[i].data)+'}';
			}else{
				remove_str = remove_str + ',{"typeFlag":"xs","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(xs_grid[i].data)+'}';
	  		}
		}
		var xs_edit=grid.getModifiedRecords();
		for(var i=0;i<xs_edit.length;i++){
			if(edit_str == ""){
				edit_str = '{"typeFlag":"xs","data":'+Ext.JSON.encode(xs_edit[i].data)+'}';
			}else{
				edit_str = edit_str + ',{"typeFlag":"xs","data":'+Ext.JSON.encode(xs_edit[i].data)+'}';
			}
		}

		if(remove_str != ""){
			if(comParams == ""){
				comParams = '"delete":[' + remove_str + "]";
			}else{
				comParams = comParams + ',"delete":[' + remove_str + "]";
			}
		}
		if(edit_str != ""){
			if(comParams == ""){
				comParams = '"update":[' + edit_str + "]";
			}else{
				comParams = comParams + ',"update":[' + edit_str + "]";
			}
		}
		//结束报名信息项;
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_ZLDX_STD","OperateType":"U","comParams":{'+comParams+'}}';
		
		//var store = grid.getStore();
		//var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_BJ_ZLDX_STD","OperateType":"U","comParams":{"bj_id":"'+bj_id+'","djzl_id":"'+djzl_id+'"}}';
		//var tzParams = this.getBackMsgParams(btn);
		var comView = this.getView();
		Ext.tzSubmit(tzParams,function(responseData){
			var tzStoreParams = '{"bj_id":"'+bj_id+'","djzl_id":"'+djzl_id+'"}';
			grid.tzStoreParams = tzStoreParams;
			comView.actType = "update";	
			grid.reload();
			btn.findParentByType('window').close();
		});
	},
	//递交资料设置---常用短信页面关闭
	onBackMsgClose: function(btn){
		var win = btn.findParentByType('window');
		win.close();
	},
	//新增报名流程
	addBmlcT: function(){
        var className, ViewClass, cmp;
        //该功能对应的JS类
        className = 'KitchenSink.view.classManage.enrollmentProcedure.enrollmentProcedureWindow';

        if (!Ext.ClassManager.isCreated(className)) {
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        cmp = new ViewClass();
        cmp.actType='add',
        this.getView().add(cmp);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
	},
	//删除报名流程(title)
	deleteBmlcT: function(){
	   //选中行
	   //var selList = this.getView().getSelectionModel().getSelection();
	   var selList = this.getView().down('grid[name=applyItemGrid]').getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzyscdjl","请选择要删除的记录"));   
			return;
	   }else{
			Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.getView().down('grid[name=applyItemGrid]').store;
				   store.remove(selList);
                    var rowCount = store.getCount( );
                    var records = store.getRange();
                    var num=0;
                    for (var i = 0; i < records.length; i++) {
                        num+=1;
                        var record = records[i];
                        record.set('bmlc_xh',num);
                    }
				}												  
			},this);   
	   }
	},
	//跳转到班级个性化设置页面
	tz_bjgxh: function(btn) {
		Ext.tzSetCompResourses("TZ_GD_BJSX_COM");
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJSX_COM"]["TZ_GD_BJSX_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        cmp = new ViewClass();
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
	//新增递交资料模板
	addDjzl_mb: function() {
		var me = this;
		Ext.tzSetCompResourses("TZ_GD_SMTDTMDL_COM");
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_NEWDJ_STD"];
		if (pageResSet == "" || pageResSet == undefined) {
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if (className == "" || className == undefined) {
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var win = this.lookupReference('myBmbRegWindow');
		if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
			win = new ViewClass();
			this.getView().add(win);
		}else{
			var activeTab = win.items.items[0].getActiveTab();
			document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = "";
		}
		win.show();
		if (!window.mybmb_cj) {
			window.mybmb_cj = function(el) {
				Ext.each(Ext.query(".tplitem"),
					function(i) {
						this.style.backgroundColor = null
					});
				el.style.backgroundColor = "rgb(173, 216, 230)";
				var activeTab = win.items.items[0].getActiveTab();
				var newName = el.getElementsByClassName("tplname")[0].getAttribute("title")  + "_" + ( + new Date());
				document.getElementById(Ext.get(activeTab.id).query('input')[0].id).value = newName;
			}
		}
	},
	/*新增递交资料模板页面，确定*/
	onNewEnsure_1: function(btn) {
		//组件注册信息列表
		//var grid = btn.findParentByType("submitDataModelMg");
		//var grid = btn.findParentByType("ClassInfo");
		//组件注册信息数据
		//var store = grid.getStore();

		var win = this.lookupReference('myBmbRegWindow');
		var activeTab = win.items.items[0].getActiveTab(),
			id = '';
		var tplName = Ext.get(activeTab.id).select('input').elements[0].value,
			tplId = "";
		if (activeTab.itemId == "predefine") {
			Ext.each(Ext.query(".tplitem"),
				function(i) {
					if (this.style.backgroundColor == "rgb(173, 216, 230)") {
						tplId = this.getAttribute("data-id");
						return false;
					}
				});
		} else {
			tplId = "";
		}
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_SMTDTMDL_COM"]["TZ_GD_SMTDTSET_STD"];
		var className = pageResSet["jsClassName"];
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
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
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
		}
		win.close();
		if (tplId&&tplName){
			cmp = new ViewClass();
			cmp.actType = "update";
			cmp.on('afterrender',function(panel){
			var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_SMTDTSET_STD","OperateType":"QF","comParams":{"smtDtTmpID":"'+tplId+'","smtDtName":"'+tplName+'","addOne":"new"}}';
			//var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_SMTDTSET_STD","OperateType":"QF","comParams":{"smtDtTmpID":"'+tplId+'","smtDtName":"'+tplName+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var form = cmp.child('form').getForm();
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				var roleList = responseData.listData;
				var tzStoreParams = '{"smtDtTmpID":"'+tplId+'","smtDtName":"'+tplName+'","addOne":"new"}';
				//var tzStoreParams = '{"smtDtTmpID":"'+tplId+'","smtDtName":"'+tplName+'"}';
				var grid_1 = cmp.child('grid');
				grid_1.store.tzStoreParams = tzStoreParams;
				grid_1.store.load();
			});
			});
			tab = contentPanel.add(cmp);
			contentPanel.setActiveTab(tab);
			Ext.resumeLayouts(true);
			if (cmp.floating) {
				cmp.show();
			}
		}else if (tplName) {
			cmp = new ViewClass();
			cmp.actType = "add";
			var form = cmp.child('form').getForm();
			form.findField('smtDtName').setValue(tplName);
			tab = contentPanel.add(cmp);
			contentPanel.setActiveTab(tab);
			Ext.resumeLayouts(true);
			if (cmp.floating) {
				cmp.show();
			}
		}
	},
	/*新增报名模板页面，确定*/
	onNewEnsure: function(btn) {
		//组件注册信息列表
		//var grid = btn.findParentByType("submitDataModelMg");
		//var grid = btn.findParentByType("ClassInfo");
		//组件注册信息数据
		//var store = grid.getStore();

		var win = this.lookupReference('myBmbRegWindow1');
		var activeTab = win.items.items[0].getActiveTab(),
			id = '';
		var tplName = Ext.get(activeTab.id).select('input').elements[0].value,
			tplId = "";
		if (activeTab.itemId == "predefine") {
			Ext.each(Ext.query(".tplitem"),
				function(i) {
					if (this.style.backgroundColor == "rgb(173, 216, 230)") {
						tplId = this.getAttribute("data-id");
						return false;
					}
				});
		} else {
			tplId = "";
		}
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_PM_BMLCMB_STD"];
		var className = pageResSet["jsClassName"];
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
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
			if (!clsProto.themeInfo) {
				Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
					themeName + '\'. Is this intentional?');
			}
		}
		win.close();
		if (tplId&&tplName){
			cmp = new ViewClass();
			cmp.actType = "update";
			cmp.on('afterrender',function(panel){
			var tzParams = '{"ComID":"TZ_PM_BMLCMBGL_COM","PageID":"TZ_PM_BMLCMBGL_STD","OperateType":"QF","comParams":{"TZ_APPPRO_TMP_ID":"'+tplId+'"}}';
			//var tzParams = '{"ComID":"TZ_GD_SMTDTMDL_COM","PageID":"TZ_GD_SMTDTSET_STD","OperateType":"QF","comParams":{"smtDtTmpID":"'+tplId+'","smtDtName":"'+tplName+'"}}';
			//加载数据
			Ext.tzLoad(tzParams,function(responseData){
				//组件注册信息数据
				var form = cmp.child('form').getForm();
				var formData = responseData.formData;
				form.setValues(formData);
				//页面注册信息列表数据
				var roleList = responseData.listData;
				var tzStoreParams = '{"TZ_APPPRO_TMP_ID":"'+tplId+'","TZ_APPPRO_TMP_NAME":"'+tplName+'"}';
				//var tzStoreParams = '{"smtDtTmpID":"'+tplId+'","smtDtName":"'+tplName+'","addOne":"new"}';
				//var tzStoreParams = '{"smtDtTmpID":"'+tplId+'","smtDtName":"'+tplName+'"}';
				var grid_1 = cmp.child('grid');
				grid_1.store.tzStoreParams = tzStoreParams;
				grid_1.store.load();
			});
			});
			tab = contentPanel.add(cmp);
			contentPanel.setActiveTab(tab);
			Ext.resumeLayouts(true);
			if (cmp.floating) {
				cmp.show();
			}
		}else if (tplName) {
			cmp = new ViewClass();
			cmp.actType = "add";
			var form = cmp.child('form').getForm();
			form.findField('TZ_APPPRO_TMP_NAME').setValue(tplName);
			//form.findField('smtDtName').setValue(tplName);
			tab = contentPanel.add(cmp);
			contentPanel.setActiveTab(tab);
			Ext.resumeLayouts(true);
			if (cmp.floating) {
				cmp.show();
			}
		}
	},
	onNewClose: function(btn) {
		var win = btn.findParentByType("window");
		win.close();
	},
	//刘哲添加，新增递交资料选择页面------结束

	//新增递交资料
	addDjzl: function(){
    var className, ViewClass, cmp ;
    //该功能对应的JS类
    className = 'KitchenSink.view.classManage.submitMaterial.submitMaterialWindow';

    if (!Ext.ClassManager.isCreated(className)) {
        Ext.syncRequire(className);
    }
    ViewClass = Ext.ClassManager.get(className);
    cmp = new ViewClass();
    cmp.actType='add',
        this.getView().add(cmp);

    Ext.resumeLayouts(true);
    if (cmp.floating) {
        cmp.show();
    }
},
	//报名流程删除一行
	deleteBmlc: function(grid, rowIndex){
		var store = grid.getStore();
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					      
			   store.removeAt(rowIndex);
                var rowCount = store.getCount( );
                var records = store.getRange(rowIndex, rowCount);
                var num=rowIndex;
                for (var i = 0; i < records.length; i++) {
                    num+=1;
                    var record = records[i];
                    record.set('bmlc_xh',num);
                }
			}												  
		},this); 

	},
	//递交资料删除一行
	deleteDjzl: function(grid, rowIndex){
		var store = grid.getStore();
		Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					      
			   store.removeAt(rowIndex);
                var rowCount = grid.getStore().getCount( );
                var records = grid.getStore().getRange(rowIndex, rowCount);
                var num=rowIndex;
                for (var i = 0; i < records.length; i++) {
                    num+=1;
                    var record = records[i];
                    record.set('djzl_xh',num);
                }
			}												  
		},this);

	},
	//删除递交资料(title)
	deleteDjzlT: function(){
	   //选中行
	   //var selList = this.getView().getSelectionModel().getSelection();
	   var selList = this.getView().down('grid[name=applyItemGrid1]').getSelectionModel().getSelection();
	   //选中行长度
	   var checkLen = selList.length;
	   if(checkLen == 0){
			Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzyscdjl","请选择要删除的记录"));   
			return;
	   }else{
			Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
				if(btnId == 'yes'){					   
				   var store = this.getView().down('grid[name=applyItemGrid1]').store;
				   store.remove(selList);
                    var rowCount = store.getCount( );
                    var records = store.getRange();
                    var num=0;
                    for (var i = 0; i < records.length; i++) {
                        num+=1;
                        var record = records[i];
                        record.set('djzl_xh',num);
                    }
				}												  
			},this);   
	   }
	},
    //删除站点模板集合
    deleteSiteTemplate: function(view, rowIndex){
    	Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
	//添加站点栏目集合；
    addColumn: function() {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxbczdmbxx","请先保存站点模板基本信息"));
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDGL_COM"]["TZ_GD_ZDLM_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		
		var win = this.lookupReference('siteTemplate');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		//操作类型设置为新增
		win.actType = "add";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
        //模板信息表单
		var form = win.child("form").getForm();
		form.reset();
		form.setValues({siteId:siteId});
        win.show();
    },
    //删除站点栏目集合
    deleteSiteColumn: function(view, rowIndex){
    	Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
    //添加站点区域集合；
    addArea: function() {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxbczdmbxx","请先保存站点模板基本信息"));
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDGL_COM"]["TZ_ZD_QYSZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		
		var win = this.lookupReference('siteareaInfoPanel');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		//操作类型设置为新增
		win.actType = "add";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
        //模板信息表单
		var form = win.child("form").getForm();
		//参数
		var tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_ZD_QYSZ_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'","areaid":""}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			var areatypeid = form.findField("areatypeid");
			areatypeid.setStore(new Ext.data.Store({
				fields: ['TValue', 'TSDesc', 'TLDesc'],
				data:responseData.TZ_AREA_TYPE_ID
			}));
			var arealm = form.findField("arealm");
			arealm.setStore(new Ext.data.Store({
				fields: ['TValue', 'TSDesc', 'TLDesc'],
				data:responseData.TZ_COLU_ID
			}));
		});
		form.reset();
		form.setValues({siteId:siteId});
		
        win.show();
    },
   	//编辑站点区域集合
    editSiteArea: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	//皮肤
   	 	var areaid = selRec.get("areaid");
     	//显示皮肤设置编辑页面
     	this.editAreaSkinByID(areaid);
    },
    editAreaSkinByID: function(areaid){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDGL_COM"]["TZ_ZD_QYSZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		
		var win = this.lookupReference('siteareaInfoPanel');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
		//模板集合表单信息;
		var form = win.child('form').getForm();
		//参数
		var tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_ZD_QYSZ_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'","areaid":"'+areaid+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			var areatypeid = form.findField("areatypeid");
			areatypeid.setStore(new Ext.data.Store({
				fields: ['TValue', 'TSDesc', 'TLDesc'],
				data:responseData.TZ_AREA_TYPE_ID
			}));
			var arealm = form.findField("arealm");
			arealm.setStore(new Ext.data.Store({
				fields: ['TValue', 'TSDesc', 'TLDesc'],
				data:responseData.TZ_COLU_ID
			}));
			//皮肤设置数据
			var formData = responseData.formData;
			form.setValues(formData);
			form.setValues({siteId:siteId});
		});
		
        win.show();
	},
    //删除站点区域集合
    deleteSiteArea: function(view, rowIndex){
    	Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
    //区域类型管理；
    addTypeArea: function() {
    	if(this.getView().actType == "add"){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxbczdmbxx","请先保存站点模板基本信息"));
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDGL_COM"]["TZ_ZD_QYLXGL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   		contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.template.sitetemplate.area.typeManges.areaTypeManagement';
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
        cmp = new ViewClass();
        var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];

        cmp.on('afterrender',function(panel){
			panel.siteId = siteId;
        	var tzStoreParams = '{"siteId":"'+siteId+'"}';
			panel.store.tzStoreParams = tzStoreParams;
			panel.store.load();
		});
        tab = contentPanel.add(cmp);
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
	//编辑站点菜单集合
    editSiteMenu: function(view, rowIndex){
    	var store = view.findParentByType("grid").store;
	 	var selRec = store.getAt(rowIndex);
	 	//皮肤
   	 	var menuid = selRec.get("menuid");
     	//显示菜单设置编辑页面
     	this.editMenuSkinByID(menuid);
    },
    editMenuSkinByID: function(menuid){
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDGL_COM"]["TZ_ZD_CDSZ_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		
		var win = this.lookupReference('sitemenuInfoPanel');
        
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
		    //新建类
            win = new ViewClass();
            this.getView().add(win);
        }
		
		//操作类型设置为更新
		win.actType = "update";
		//站点模板基本信息
		var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];
		//模板集合表单信息;
		var form = win.child('form').getForm();
		//参数
		var tzParams = '{"ComID":"TZ_GD_ZDGL_COM","PageID":"TZ_ZD_CDSZ_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'","menuid":"'+menuid+'"}}';
		//加载数据
		Ext.tzLoad(tzParams,function(responseData){
			var areatypeid = form.findField("menutypeid");
			areatypeid.setStore(new Ext.data.Store({
				fields: ['TValue', 'TSDesc', 'TLDesc'],
				data:responseData.TZ_MENU_TYPE_ID
			}));
			var arealm = form.findField("menulm");
			arealm.setStore(new Ext.data.Store({
				fields: ['TValue', 'TSDesc', 'TLDesc'],
				data:responseData.TZ_MENU_COLUMN
			}));
			//菜单设置数据
			var formData = responseData.formData;
			form.setValues(formData);
			form.setValues({siteId:siteId});
		});
		
        win.show();
	},
    //删除站点菜单集合
    deleteSiteMenu: function(view, rowIndex){
    	Ext.MessageBox.confirm(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.confirm","确认"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nqdyscsxjlm","您确定要删除所选记录吗？"), function(btnId){
			if(btnId == 'yes'){					   
			   var store = view.findParentByType("grid").store;
			   store.removeAt(rowIndex);
			}												  
		},this);
    },
    //菜单类型管理；
    addTypeMenu: function() {
		if(this.getView().actType == "add"){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxbczdmbxx","请先保存站点模板基本信息"));
			return;
		}
		
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_ZDGL_COM"]["TZ_ZD_CDLXGL_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
			return;
		}
		//该功能对应的JS类
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		
		var contentPanel,cmp, className, ViewClass, clsProto;
		var themeName = Ext.themeName;
		
        contentPanel = Ext.getCmp('tranzvision-framework-content-panel');			
   		contentPanel.body.addCls('kitchensink-example');

        //className = 'KitchenSink.view.template.sitetemplate.area.typeManges.areaTypeManagement';
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
        cmp = new ViewClass();
        var comSiteParams = this.getView().child("form").getForm().getValues();
		//站点模板id
		var siteId = comSiteParams["siteId"];

        cmp.on('afterrender',function(panel){
        	panel.siteId = siteId;
        	var tzStoreParams = '{"siteId":"'+siteId+'"}';
			panel.store.tzStoreParams = tzStoreParams;
			panel.store.load();
		});
        tab = contentPanel.add(cmp);
		
        contentPanel.setActiveTab(tab);

        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
	onFormSave: function(btn){
		//组件注册表单
		var me=this;
		var form = this.getView().child("form").getForm();
		var comSiteParams = form.getValues();
		var bj_id = comSiteParams["bj_id"];
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getActivityInfoParams();
			var comView = this.getView();
			var actType = comView.actType;
			var activityId = form.findField("bj_id").getValue();
			if(actType=="update" && (activityId=="" || typeof(activityId) == "undefined" )){
					Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bccc","保存出错"));
			}else{
				Ext.tzSubmit(tzParams,function(responseData){
					if(actType=="add"){
						comView.actType = "update";	
				  		form.findField("bj_id").setValue(responseData.bj_id);
					}
					//专业方向页面重新加载
					var zy_grid = btn.findParentByType('ClassInfo').down('grid[name=zyfx_save]');
					Params= '{"bj_id":"'+bj_id+'","queryID":"2"}';
					zy_grid.store.tzStoreParams = Params;
					zy_grid.store.reload();
					//批次管理页面重新加载
					var pc_grid = btn.findParentByType('ClassInfo').down('grid[name=pcgl_save]');
					Params= '{"bj_id":"'+bj_id+'","queryID":"3"}';
					pc_grid.store.tzStoreParams = Params;
					pc_grid.store.reload();
					//管理人员页面重新加载
					var gl_grid = btn.findParentByType('ClassInfo').down('grid[name=glry_save]');
					Params= '{"bj_id":"'+bj_id+'","queryID":"4"}';
					gl_grid.store.tzStoreParams = Params;
					gl_grid.store.reload();
					//报名流程页面重新加载
					var _bm="";
					var bm_grid = btn.findParentByType('ClassInfo').down('grid[name=applyItemGrid]');
					Params= '{"bj_id":"'+bj_id+'","queryID":"5","lc_id":"'+_bm+'"}';
					bm_grid.store.tzStoreParams = Params;
					bm_grid.store.reload();
					//递交资料页面重新加载
					/*var _zl="";
					var zl_grid = btn.findParentByType('ClassInfo').down('grid[name=applyItemGrid1]');
					Params= '{"bj_id":"'+bj_id+'","queryID":"6","zl_id":"'+_zl+'"}';
					zl_grid.store.tzStoreParams = Params;
					zl_grid.store.reload();*/
				},"",true,this);
			}
		}
	},
	getActivityInfoParams: function(){
		//班级管理基本信息
		var form = this.getView().child("form").getForm();
		//var form=this.getView().down('form[name=form_1]').getForm();
		var actType = this.getView().actType;
		var comParams = "";
		var comSiteParams = form.getValues();
		var attachGrid = this.getView().down('checkbox[name=bj_xs]');
		comSiteParams["bj_xs"]=attachGrid.getValue();
		//拼接基本信息json
		if(actType == "add"){
			comParams = '"add":[' + Ext.JSON.encode(comSiteParams) + ']';
		}
		//修改信息
		var edit_str="";
		//删除信息
		var remove_str="";
		//班级ID
		var bj_id = comSiteParams["bj_id"];
		if(actType == "update"){
			edit_str = '{"typeFlag":"bj_jbxx","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(comSiteParams)+'}';
		}
		//专业方向
		var zyfx_grid=this.getView().down('grid[name=zyfx_save]').getStore();
		var zyfx_remove=zyfx_grid.getRemovedRecords();
		for(var i=0;i<zyfx_remove.length;i++){
			if(remove_str == ""){
				remove_str = '{"typeFlag":"zyfx","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(zyfx_remove[i].data)+'}';
			}else{
				remove_str = remove_str + ',{"typeFlag":"zyfx","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(zyfx_remove[i].data)+'}';
	  		}
		}
		var zyjf_edit=zyfx_grid.getModifiedRecords();
		for(var i=0;i<zyjf_edit.length;i++){
			if(edit_str == ""){
				edit_str = '{"typeFlag":"zyfx","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(zyjf_edit[i].data)+'}';
			}else{
				edit_str = edit_str + ',{"typeFlag":"zyfx","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(zyjf_edit[i].data)+'}';
			}
		}
		//批次管理
		var pcgl_grid=this.getView().down('grid[name=pcgl_save]').getStore();
		var pcgl_remove=pcgl_grid.getRemovedRecords();
		for(var i=0;i<pcgl_remove.length;i++){
			if(remove_str == ""){
				remove_str = '{"typeFlag":"pcgl","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(pcgl_remove[i].data)+'}';
			}else{
				remove_str = remove_str + ',{"typeFlag":"pcgl","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(pcgl_remove[i].data)+'}';
	  		}
		}
		//管理人员
		var glry_grid=this.getView().down('grid[name=glry_save]').getStore();
		var glry_remove=glry_grid.getRemovedRecords();
		for(var i=0;i<glry_remove.length;i++){
			if(remove_str == ""){
				remove_str = '{"typeFlag":"glry","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(glry_remove[i].data)+'}';
			}else{
				remove_str = remove_str + ',{"typeFlag":"glry","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(glry_remove[i].data)+'}';
	  		}
		}
		var glry_edit=glry_grid.getModifiedRecords();
		for(var i=0;i<glry_edit.length;i++){
			if(edit_str == ""){
				edit_str = '{"typeFlag":"glry","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(glry_edit[i].data)+'}';
			}else{
				edit_str = edit_str + ',{"typeFlag":"glry","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(glry_edit[i].data)+'}';
	  		}
		}
		//报名流程
		var bmlc_grid=this.getView().down('grid[name=applyItemGrid]').getStore();
		bmlc_grid.each(function(rec){
			if(edit_str == ""){
				edit_str = '{"typeFlag":"bmlc","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(rec.data)+'}';
			}else{
				edit_str = edit_str + ',{"typeFlag":"bmlc","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(rec.data)+'}';
			}
		});
		var bmlc_remove=bmlc_grid.getRemovedRecords();
		for(var i=0;i<bmlc_remove.length;i++){
			if(remove_str == ""){
				remove_str = '{"typeFlag":"bmlc","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(bmlc_remove[i].data)+'}';
			}else{
				remove_str = remove_str + ',{"typeFlag":"bmlc","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(bmlc_remove[i].data)+'}';
	  		}
		}
		//递交资料-隐藏
		/*var djzl_grid=this.getView().down('grid[name=applyItemGrid1]').getStore();
		djzl_grid.each(function(rec){
			if(edit_str == ""){
				edit_str = '{"typeFlag":"djzl","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(rec.data)+'}';
			}else{
				edit_str = edit_str + ',{"typeFlag":"djzl","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(rec.data)+'}';
			}
		});
		var djzl_remove=djzl_grid.getRemovedRecords();
		for(var i=0;i<djzl_remove.length;i++){
			if(remove_str == ""){
				remove_str = '{"typeFlag":"djzl","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(djzl_remove[i].data)+'}';
			}else{
				remove_str = remove_str + ',{"typeFlag":"djzl","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(djzl_remove[i].data)+'}';
	  		}
		}*/
		//更多信息
		var form2=this.getView().down('form[name=form_2]').getForm();
		var comParams1 = "";
		var comSiteParams1 = form2.getValues();
		if(edit_str == ""){
			edit_str = '{"typeFlag":"gdxx","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(comSiteParams1)+'}';
		}else{
			edit_str = edit_str+',{"typeFlag":"gdxx","bj_id":"'+bj_id+'","data":'+Ext.JSON.encode(comSiteParams1)+'}';
		}
		
		if(remove_str != ""){
			if(comParams == ""){
				comParams = '"delete":[' + remove_str + "]";
			}else{
				comParams = comParams + ',"delete":[' + remove_str + "]";
			}
		}
		if(edit_str != ""){
			if(comParams == ""){
				comParams = '"update":[' + edit_str + "]";
			}else{
				comParams = comParams + ',"update":[' + edit_str + "]";
			}
		}
		//结束报名信息项;
		//提交参数
		var tzParams = '{"ComID":"TZ_GD_BJGL_COM","PageID":"TZ_GD_BJJB_STD","OperateType":"U","comParams":{'+comParams+'}}';
        return tzParams;
	},
    onFormEnsure: function(btn){
		//组件注册表单
		var me=this;
		var form = this.getView().child("form").getForm();
		var comSiteParams = form.getValues();
		var bj_id = comSiteParams["bj_id"];
		if (form.isValid()) {
			//获取组件注册信息参数
			var tzParams = this.getActivityInfoParams();
			var comView = this.getView();
			var actType = comView.actType;
			var activityId = form.findField("bj_id").getValue();
			if(actType=="update" && (activityId=="" || typeof(activityId) == "undefined" )){
					Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.bccc","保存出错"));
			}else{
				Ext.tzSubmit(tzParams,function(responseData){
					if(actType=="add"){
						comView.actType = "update";	
				  		form.findField("bj_id").setValue(responseData.bj_id);
					}
					//专业方向页面重新加载
					var zy_grid = btn.findParentByType('ClassInfo').down('grid[name=zyfx_save]');
					Params= '{"bj_id":"'+bj_id+'","queryID":"2"}';
					zy_grid.store.tzStoreParams = Params;
					zy_grid.store.reload();
					//批次管理页面重新加载
					var pc_grid = btn.findParentByType('ClassInfo').down('grid[name=pcgl_save]');
					Params= '{"bj_id":"'+bj_id+'","queryID":"3"}';
					pc_grid.store.tzStoreParams = Params;
					pc_grid.store.reload();
					//管理人员页面重新加载
					var gl_grid = btn.findParentByType('ClassInfo').down('grid[name=glry_save]');
					Params= '{"bj_id":"'+bj_id+'","queryID":"4"}';
					gl_grid.store.tzStoreParams = Params;
					gl_grid.store.reload();
					//报名流程页面重新加载
					var _bm="";
					var bm_grid = btn.findParentByType('ClassInfo').down('grid[name=applyItemGrid]');
					Params= '{"bj_id":"'+bj_id+'","queryID":"5","lc_id":"'+_bm+'"}';
					bm_grid.store.tzStoreParams = Params;
					bm_grid.store.reload();
					//递交资料页面重新加载
					/*var _zl="";
					var zl_grid = btn.findParentByType('ClassInfo').down('grid[name=applyItemGrid1]');
					Params= '{"bj_id":"'+bj_id+'","queryID":"6","zl_id":"'+_zl+'"}';
					zl_grid.store.tzStoreParams = Params;
					zl_grid.store.reload();*/
					me.getView().close();
				},"",true,this);
			}
		}
	},
	onFormClose: function(){
		this.getView().close();
	},
    enrollmentProcedureSet:function(){
        Ext.tzSetCompResourses("TZ_PM_BMLCMBGL_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_PM_BMLCMBGL_COM"]["TZ_PM_BMLCMBGL_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        cmp = new ViewClass();
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    dynamicFormSet:function(){
        Ext.tzSetCompResourses("TZ_GD_BJSX_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_GD_BJSX_COM"]["TZ_GD_BJSX_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"), Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.nmyqx","您没有权限"));
            return;
        }
        //该功能对应的JS类
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
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
            if (!clsProto.themeInfo) {
                Ext.log.warn ( 'Example \'' + className + '\' lacks a theme specification for the selected theme: \'' +
                    themeName + '\'. Is this intentional?');
            }
        }
        cmp = new ViewClass();
        tab = contentPanel.add(cmp);
        contentPanel.setActiveTab(tab);
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    editBmlc:function(obj , rowIndex , colIndex , item , e , record , row){
        var contentPanel, className, ViewClass, cmp ,bmlc_name;
        var editRecord;
        if(Ext.ComponentQuery.is(obj,"button")){
            var selList = obj.findParentByType("grid").getSelectionModel().getSelection();
            if (selList.length === 0 ) {
                Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzytyxgdjl","请选择一条要修改的记录"));
                return;
            }
            if (selList.length > 1) {
                Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.znxzytjl","只能选择一条记录"));
                return;
            }
            editRecord= selList[0];
            bmlc_name = selList[0].data.bmlc_name;
        }else{
            editRecord= record;
            bmlc_name = record.get('bmlc_name');
        }
        //该功能对应的JS类
        className = 'KitchenSink.view.classManage.enrollmentProcedure.enrollmentProcedureWindow';

        if (!Ext.ClassManager.isCreated(className)) {
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        cmp = new ViewClass({editRecord:editRecord});
        cmp.editRecord=editRecord;
        cmp.actType='update',
        this.getView().add(cmp);

        cmp.on('afterrender', function (panel) {
            var form = panel.child('form').getForm();
            form.setValues({
                bmlc_name:bmlc_name
            });
        });
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    },
    editDjzl:function(obj , rowIndex , colIndex , item , e , record , row){
        var contentPanel, className, ViewClass, cmp ,content,remark;
        var editRecord;
        if(Ext.ComponentQuery.is(obj,"button")){
            var selList = obj.findParentByType("grid").getSelectionModel().getSelection();
            if (selList.length === 0 ) {
                Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.qxzytyxgdjl","请选择一条要修改的记录"));
                return;
            }
            if (selList.length > 1) {
                Ext.Msg.alert(Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.prompt","提示"),Ext.tzGetResourse("TZ_GD_BJGL_COM.TZ_GD_BJJB_STD.znxzytjl","只能选择一条记录"));
                return;
            }
            editRecord= selList[0];
            content = selList[0].data.djzl_name;
            remark = selList[0].data.djzl_bz;
        }else{
            editRecord= record;
            content = record.get('djzl_name');
            remark = record.get('djzl_bz');
        }
        //该功能对应的JS类
        className = 'KitchenSink.view.classManage.submitMaterial.submitMaterialWindow';

        if (!Ext.ClassManager.isCreated(className)) {
            Ext.syncRequire(className);
        }
        ViewClass = Ext.ClassManager.get(className);
        cmp = new ViewClass({editRecord:editRecord});
        cmp.editRecord=editRecord;
        cmp.actType='update',
            this.getView().add(cmp);

        cmp.on('afterrender', function (panel) {
            var form = panel.child('form').getForm();
            form.setValues({
                djzl_name:content,
                djzl_bz:remark
            });
        });
        Ext.resumeLayouts(true);
        if (cmp.floating) {
            cmp.show();
        }
    }
});