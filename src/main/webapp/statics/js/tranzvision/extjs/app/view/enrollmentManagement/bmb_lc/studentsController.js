Ext.define('KitchenSink.view.enrollmentManagement.bmb_lc.studentsController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.studentsList', 
    //批量导入发布结果；
    pl_drjg: function(btn) {
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_PCDR_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var win = this.lookupReference('bmlcPldr');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
		var form = win.child('form').getForm();
		//获取班级ID和报名流程ID
		//var yl_grid = this.getView().down('grid[name=studentL]').getStore();
		var yl_grid = this.getView().down('grid[name=studentL]');
		var rec = yl_grid.getStore().getAt(0);
		var bj_id = rec.data.bj_id;
		var bmlc_id = rec.data.bmlc_id;
		var form = win.child('form').getForm();
		form.findField("bj_id").setValue(bj_id);
		form.findField("bmlc_id").setValue(bmlc_id);
        win.show();
    },
	//批量导入（上传附件）
	upload: function(btn) {
		var me=this;
		var _form = this.getView().child("form").getForm();
		var _val=_form.findField('file_name').getValue();
		var fix = _val.substring(_val.lastIndexOf(".") + 1,_val.length);
		if(fix.toLowerCase() == "xls"){
			_form.submit({
				url: TzUniversityContextPath + '/UpdServlet?filePath=/ceshi',
				waitMsg: Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.uploading","上传中...."),
				success: function (_form, action) {
					var message = action.result.msg;
					var path = message.accessPath;
					var sysFileName = message.sysFileName;
					if(path.charAt(path.length - 1) == '/'){
						path = path + sysFileName;
					}else{
						path = path + "/" + sysFileName;
					}
					var _bj_id =_form.findField('bj_id').getValue();
					var _bmlc_id =_form.findField('bmlc_id').getValue();
					var tzParams_1 = '{"bj_id":"' + _bj_id + '","bmlc_id":"' + _bmlc_id + '","path":"' + path + '"}';
					var comParams = '"update":[' + tzParams_1 + "]";
					var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_PCDR_STD","OperateType":"U","comParams":{'+comParams+'}}';
					Ext.tzLoad(tzParams,function(responseData){	
						var formData = responseData.retrun_desc;
						me.getView().close();
						Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"), formData);
					});
				},
				failure: function (form, action) {
					_form.reset();
					Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.error","错误"), action.result.msg);
				}
			});
		}else{
			//重置表单
			_form.reset();
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.qscexcelgsdwj","请上传xls格式的文件"));
		}
    },
	//查看前台公布内容
	sec_frontDeac: function(grid, rowIndex, colIndex){
		Ext.tzSetCompResourses("TZ_BMGL_BMBSH_COM");
		//是否有访问权限
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMGL_AUDIT_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}
		var record = grid.store.getAt(colIndex);
		var _classID = record.data.bj_id;
		var _oprID = record.data.opr_id;
		var tzParams='?tzParams={"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMGL_AUDIT_STD","OperateType":"HTML","comParams":{"oprId":"'+_oprID+'","classId":"' + _classID + '"}}';
		var url = Ext.tzGetGeneralURL() + tzParams;
		window.open(url, '_blank');
    },
	//批量录入发布结果
	pl_lrjg: function(btn) {
		var selList = this.getView().down('grid[name=studentL]').getSelectionModel().getSelection();
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
	   		Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.qzsxzytyxgdjl","请至少选择一条要修改的记录"));
			return;
	   	}
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_PLLR_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var win = this.lookupReference('bmlcPllr');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
        win.show();
    },
	//单独编辑初筛结果
    cs_edit:function(grid, rowIndex, colIndex){
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_HFYSZ2_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var win = this.lookupReference('HfyeditD');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
			var record = grid.store.getAt(rowIndex);
			var classID = record.data.bj_id;
			var bmlc_id = record.data.bmlc_id;
			var bmb_id = record.data.bmb_id;
			var form = win.child('form').getForm();
			var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_CLS_BMLCHF_T',
				condition:{
					TZ_CLASS_ID:{
						value:classID,
						operator:"01",
						type:"01"
					},
					TZ_APPPRO_ID:{
						value:bmlc_id,
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_APPPRO_HF_BH,TZ_CLS_RESULT'
			});
			form.findField("jg_id").setStore(lm_mbStore);
			win.on('afterrender',function(panel){
				var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFYSZ2_STD","OperateType":"QF","comParams":{"bj_id":"'+classID+'","bmlc_id":"'+bmlc_id+'","bmb_id":"'+bmb_id+'"}}';
				Ext.tzLoad(tzParams,function(responseData){
					var formData = responseData.formData;
					form.setValues(formData);
				});
			});
            this.getView().add(win);
        }
        win.show();
    },
	//保存单独公布结果
	SaveHfyeditD: function(btn){
		var form = this.getView().child("form").getForm();
		var comParams = "";
		comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFYSZ2_STD","OperateType":"U","comParams":{'+comParams+'}}';
		var comView = this.getView();
		Ext.tzSubmit(tzParams,function(responseData){

		},"",true,this);
		var win = btn.findParentByType("studentsList");
		win.store.reload();
		//this.getView().close();
	},
	//确定单独公布结果
	EnsureHfyeditD: function(btn){
		var form = this.getView().child("form").getForm();
		var comParams = "";
		comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFYSZ2_STD","OperateType":"U","comParams":{'+comParams+'}}';
		var comView = this.getView();
		Ext.tzSubmit(tzParams,function(responseData){

		},"",true,this);
		var win = btn.findParentByType("studentsList");
		win.store.reload();
		this.getView().close();
	},
	//关闭单独公布结果页面
	CloseHfyeditD: function(){
		this.getView().close();
	},
	//批量编辑初筛结果
    csp_edit:function(btn){
		var selList = this.getView().getSelectionModel().getSelection();
	   	var checkLen = selList.length;
	   	if(checkLen == 0){
	   		Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.qzsxzytyxgdjl","请至少选择一条要修改的记录"));
			return;
	   	}
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_HFYSZ1_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var _bmb_Id="";
		for(var i=0;i<selList.length;i++){
			if(_bmb_Id == ""){
				_bmb_Id = selList[i].get('bmb_id');
			}else{
				_bmb_Id = _bmb_Id + ','+selList[i].get('bmb_id');
			}
		}
		var _bj_id=selList[0].get('bj_id');
		var _bmlc_id=selList[0].get('bmlc_id');
		var win = this.lookupReference('HfyeditP');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
			
			var form = win.child('form').getForm();
			var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_CLS_BMLCHF_T',
				condition:{
					TZ_CLASS_ID:{
						value:_bj_id,
						operator:"01",
						type:"01"
					},
					TZ_APPPRO_ID:{
						value:_bmlc_id,
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_APPPRO_HF_BH,TZ_CLS_RESULT'
			});
			form.findField("jg_id").setStore(lm_mbStore);
			win.on('afterrender',function(panel){
				var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFYSZ1_STD","OperateType":"QF","comParams":{"bj_id":"'+_bj_id+'","bmlc_id":"'+_bmlc_id+'","bmb_id":"'+_bmb_Id+'"}}';
				Ext.tzLoad(tzParams,function(responseData){
					var formData = responseData.formData;
					form.setValues(formData);
				});
			});
            this.getView().add(win);
        }
        win.show();
    },
	//保存批量公布结果
	SaveHfyeditP: function(btn){
		var form = this.getView().child("form").getForm();
		var comParams = "";
		comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFYSZ1_STD","OperateType":"U","comParams":{'+comParams+'}}';
		var comView = this.getView();
		Ext.tzSubmit(tzParams,function(responseData){

		},"",true,this);
		var win = btn.findParentByType("studentsList");
		win.store.reload();
		//this.getView().close();
	},
	//确定批量公布结果
	EnsureHfyeditP: function(btn){
		var form = this.getView().child("form").getForm();
		var comParams = "";
		comParams = '"add":['+Ext.JSON.encode(form.getValues())+']';
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_HFYSZ1_STD","OperateType":"U","comParams":{'+comParams+'}}';
		var comView = this.getView();
		Ext.tzSubmit(tzParams,function(responseData){

		},"",true,this);
		var win = btn.findParentByType("studentsList");
		win.store.reload();
		this.getView().close();
	},
	//关闭批量公布结果页面
	CloseHfyeditP: function(){
		this.getView().close();
	},
	//批量录入发布结果确定按钮
	onEnsureWin: function(btn) {
		var _form=this.getView().child('form').getForm().getValues();
		var _area = _form['plgb_area'];
		var win = btn.findParentByType("studentsList");
		var st_grid=win.down('grid[name=studentL]').getStore();
		var selList=win.down('grid[name=studentL]').getSelectionModel().getSelection();
		for(var i=0;i<selList.length;i++){
			selList[i].set('ms_zg',_area);
		}
		this.getView().close();
    },
	//预览当前人员的发布结果
	name_click: function(grid, rowIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var bj_id = rec.data.bj_id;
		var me=this;
		var bmlc_id = rec.data.bmlc_id;
		var bmlc_desc = rec.data.ms_zg;
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_BJMB_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		edit_str = '{"data":{"bj_id":"'+bj_id+'","bmlc_id":"'+bmlc_id+'"}}';
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_BJMB_STD","OperateType":"QF","comParams":'+edit_str+'}';
		var _mb_desc="";
		var _desc="";
		Ext.tzLoad(tzParams,function(responseData){
			_mb_desc=responseData.formData;
			_desc=_mb_desc.bmlc_mbnr;
			_desc=_desc.replace("【显示公布结果占位符】", bmlc_desc);
			_desc=_desc.replace("<p>", "");
			_desc=_desc.replace("</p>", "");
			var yl_form=me.getView().down('form[name=yl_form]').getForm();
			yl_form.findField('result_yl').setValue(_desc);
			yl_form.findField('row_num').setValue(rowIndex);
		});
	},
	//编辑默认模板
	edit_mb: function() {
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_BJMB_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var win = this.lookupReference('bmLceditMb');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
		var grid = this.getView().down('grid[name=studentL]').getStore();
		var edit_str="";
		grid.each(function(rec){
			edit_str = '{"data":'+Ext.JSON.encode(rec.data)+'}';
		});
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_BJMB_STD","OperateType":"QF","comParams":'+edit_str+'}';
		var form = win.child('form').getForm();
		Ext.tzLoad(tzParams,function(responseData){
			var formData = responseData.formData;
			form.setValues(formData);
		});
        win.show();
    },
	//编辑默认模板
	edit_lrjg: function() {
		var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_BMBSH_COM"]["TZ_BMSH_GBJG_STD"];
		if( pageResSet == "" || pageResSet == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
			return;
		}
		var className = pageResSet["jsClassName"];
		if(className == "" || className == undefined){
			Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
			return;
		}
		var win = this.lookupReference('bmLcLrgb');
        if (!win) {
			Ext.syncRequire(className);
			ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass();
            this.getView().add(win);
        }
		var form = win.child('form').getForm();
		//获取班级ID和报名流程ID
		var yl_form = this.getView().down('form[name=yl_form]').getForm();
		var _rownum=yl_form.getValues();
		var _value = _rownum['row_num'];
		var _grid= this.getView().down('grid[name=studentL]').getStore();
		var record = _grid.getAt(_value);
		var _desc = record.data.ms_zg;
		var form = win.child('form').getForm();
		form.findField("row_num").setValue(_value);
		form.findField("ms_zg").setValue(_desc);
		win.show();
    },
	//确认录入结果页面
	MakeSure: function(btn){
		var _form=this.getView().child('form').getForm().getValues();
		var _rowIndex = _form['row_num'];
		var _ms_zg = _form['ms_zg'];
		_ms_zg=_ms_zg.replace("<p>", "");
		_ms_zg=_ms_zg.replace("</p>", "");
		var win = btn.findParentByType("studentsList");
		var yl_form=win.down('form[name=yl_form]').getForm();
		var st_grid=win.down('grid[name=studentL]').getStore();
		var record = st_grid.getAt(_rowIndex);
		record.set('ms_zg',_ms_zg);
		var _bj_id=record.data.bj_id;
		var _bmlc_id=record.data.bmlc_id;
		edit_str = '{"data":{"bj_id":"'+_bj_id+'","bmlc_id":"'+_bmlc_id+'"}}';
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_BJMB_STD","OperateType":"QF","comParams":'+edit_str+'}';
		var _mb_desc="";
		var _desc="";
		Ext.tzLoad(tzParams,function(responseData){
			_mb_desc=responseData.formData;
			_desc=_mb_desc.bmlc_mbnr;
			_desc=_desc.replace("【显示公布结果占位符】", _ms_zg);
			_desc=_desc.replace("<p>", "");
			_desc=_desc.replace("</p>", "");
			yl_form.findField("result_yl").setValue(_desc);
		});
		this.getView().close();
    },
	//关闭录入结果页面
	MaleClose: function(){
		this.getView().close();
	},
	//关闭整个页面
	onFormClose: function(){
		this.getView().close();
	},
	//关闭批量录入页面
	onCloseWin: function(){
		this.getView().close();
	},
	//关闭批量导入页面
	ClosePldr: function(){
		this.getView().close();
	},
	//保存学生列表信息
	onFormSave: function(){
		var xs_grid=this.getView().down('grid[name=studentL]').getStore();
		var xs_update=xs_grid.getModifiedRecords();
		var edit_str="";
		for(var i=0;i<xs_update.length;i++){
			if(edit_str == ""){
				edit_str = '{"typeFlag":"xs","data":'+Ext.JSON.encode(xs_update[i].data)+'}';
			}else{
				edit_str = edit_str + ',{"typeFlag":"xs","data":'+Ext.JSON.encode(xs_update[i].data)+'}';
	  		}
		}
		var comParams="";
		if(edit_str != ""){
			if(comParams == ""){
				comParams = '"update":[' + edit_str + "]";
			}else{
				comParams = comParams + ',"update":[' + edit_str + "]";
			}
		}
		var tzParams = '{"ComID":"TZ_BMGL_BMBSH_COM","PageID":"TZ_BMSH_FB_STD","OperateType":"U","comParams":{'+comParams+'}}';
		Ext.tzSubmit(tzParams,function(responseData){
			//123
		},"",true,this);
	},
	//关闭学生列表页面
    onPanelClose: function(){
		this.getView().close();
	},
    //测试1
    testLqlc:function(grid, rowIndex, colIndex){
        var me = this;
        var win0 = this.lookupReference('studentsList0');
        //console.log(win0,me);
        Ext.tzSetCompResourses("TZ_BMGL_LCJGPUB_COM");
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_LCJGPUB_COM"]["TZ_PER_LCJGPUB_STD"];

        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }

        var win = this.lookupReference('perLcjgPubWindow');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass({
               perLcjgCallBack:function(){
                }
            });
            var record = grid.store.getAt(rowIndex);
            var classID = record.data.bj_id;
            var bmlc_id = record.data.bmlc_id;
            var bmb_id = record.data.bmb_id;
            var form = win.child('form').getForm();
            var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
                recname: 'TZ_CLS_BMLCHF_T',
                condition:{
                    TZ_CLASS_ID:{
                        value:classID,
                        operator:"01",
                        type:"01"
                    },
                    TZ_APPPRO_ID:{
                        value:bmlc_id,
                        operator:"01",
                        type:"01"
                    }
                },
                result:'TZ_APPPRO_HF_BH,TZ_CLS_RESULT'
            });
            form.findField("jg_id").setStore(lm_mbStore);
            win.on('afterrender',function(panel){
                var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGPUB_STD","OperateType":"QF","comParams":{"bj_id":"'+classID+'","bmlc_id":"'+bmlc_id+'","bmb_id":"'+bmb_id+'"}}';
                Ext.tzLoad(tzParams,function(responseData){
                    var formData = responseData.formData;
                    form.setValues(formData);
                });
            });
            this.getView().add(win);
        }
        win.show();
    },
    //批量录入发布结果
    test_pl: function(btn) {
        var me = this;
        Ext.tzSetCompResourses("TZ_BMGL_LCJGPUB_COM");
        var selList = this.getView().getSelectionModel().getSelection();
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.qzsxzytyxgdjl","请至少选择一条要修改的记录"));
            return;
        }
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_LCJGPUB_COM"]["TZ_PER_LCJGP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var _bmb_Id="";
        for(var i=0;i<selList.length;i++){
            if(_bmb_Id == ""){
                _bmb_Id = selList[i].get('bmb_id');
            }else{
                _bmb_Id = _bmb_Id + ','+selList[i].get('bmb_id');
            }
        }
        var _bj_id=selList[0].get('bj_id');
        var _bmlc_id=selList[0].get('bmlc_id');
        var win = this.lookupReference('perLcjgPubWindowP');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass({
                    perLcjgCallBack:function(){
                    this.store.reload();
                }
            });

            var form = win.child('form').getForm();
            var lm_mbStore = new KitchenSink.view.common.store.comboxStore({
                recname: 'TZ_CLS_BMLCHF_T',
                condition:{
                    TZ_CLASS_ID:{
                        value:_bj_id,
                        operator:"01",
                        type:"01"
                    },
                    TZ_APPPRO_ID:{
                        value:_bmlc_id,
                        operator:"01",
                        type:"01"
                    }
                },
                result:'TZ_APPPRO_HF_BH,TZ_CLS_RESULT'
            });
            form.findField("jg_id").setStore(lm_mbStore);
            win.on('afterrender',function(panel){
                var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGP_STD","OperateType":"QF","comParams":{"bj_id":"'+_bj_id+'","bmlc_id":"'+_bmlc_id+'","bmb_id":"'+_bmb_Id+'"}}';
                Ext.tzLoad(tzParams,function(responseData){
                    var formData = responseData.formData;
                    form.setValues(formData);
                });
            });
            this.getView().add(win);
        }
        win.show();
    },
    //批量发布结果   LYY  2015-09-25
    onPLPublisResult:function(btn){
        var me = this;
        var studentsListGrid = btn.up('grid');
        var selList = studentsListGrid.getSelectionModel().getSelection();
        var studentsListForm = studentsListGrid.up('panel').child('form').getForm();
        var studentsListFormRec = studentsListForm.getFieldValues();
        var classID = studentsListFormRec['classID'];

        Ext.tzSetCompResourses("TZ_BMGL_LCJGPUB_COM");
        var checkLen = selList.length;
        if(checkLen == 0){
            Ext.Msg.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_AUDIT_STD.qzsxzytyxgdjl","请至少选择一条要修改的记录"));
            return;
        }
        var pageResSet = TranzvisionMeikecityAdvanced.Boot.comRegResourseSet["TZ_BMGL_LCJGPUB_COM"]["TZ_PER_LCJGP_STD"];
        if( pageResSet == "" || pageResSet == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"), Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.nmyqx","您没有权限"));
            return;
        }
        var className = pageResSet["jsClassName"];
        if(className == "" || className == undefined){
            Ext.MessageBox.alert(Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.prompt","提示"),Ext.tzGetResourse("TZ_BMGL_BMBSH_COM.TZ_BMGL_STU_STD.wzdgjs","未找到该功能页面对应的JS类，请检查配置。"));
            return;
        }
        var _bmb_Id="";
        for(var i=0;i<selList.length;i++){
            if(_bmb_Id == ""){
                _bmb_Id = selList[i].get('bmb_id');
            }else{
                _bmb_Id = _bmb_Id + ','+selList[i].get('bmb_id');
            }
        }
        var _bj_id=classID;
        var _bmlc_id="";
        var win = this.lookupReference('perLcjgPubWindowP');
        if (!win) {
            Ext.syncRequire(className);
            ViewClass = Ext.ClassManager.get(className);
            win = new ViewClass({
                perLcjgCallBack:function(){
                    studentsListGrid.store.reload();
                }
            });

            var form = win.child('form').getForm();
            /*流程下拉框*/
            var lcStore = new KitchenSink.view.common.store.comboxStore({
                recname: 'TZ_CLS_BMLC_T',
                condition:{
                    TZ_CLASS_ID:{
                        value:_bj_id,
                        operator:"01",
                        type:"01"
                    }
                },
                result:'TZ_APPPRO_ID,TZ_APPPRO_NAME'
            });
            form.findField("ry_lc").setStore(lcStore);

            win.on('afterrender',function(panel){
                var tzParams = '{"ComID":"TZ_BMGL_LCJGPUB_COM","PageID":"TZ_PER_LCJGP_STD","OperateType":"QF","comParams":{"bj_id":"'+_bj_id+'","bmlc_id":"'+_bmlc_id+'","bmb_id":"'+_bmb_Id+'"}}';
                Ext.tzLoad(tzParams,function(responseData){
                    var formData = responseData.formData;
                    form.setValues(formData);
                });
            });
            this.getView().add(win);
        }
        win.show();
    }
});