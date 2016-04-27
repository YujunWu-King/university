Ext.define('KitchenSink.view.security.user.userInfoPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'userAccInfo', 
    controller: 'userMg',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.security.user.userRoleModel',
		'KitchenSink.view.common.store.comboxStore',
        'KitchenSink.view.security.user.userRoleStore'
	],
	listeners:{
		resize: function(win){
			win.doLayout();
		}
	},
	actType: '',
	userGridStore:'',	//用户Grid Store
    title: '用户定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'userAccountForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 110,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            //fieldLabel: '登录账号',
						fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.usAccNum","登录账号"),
						name: 'usAccNum',
						maxLength: 30,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false,
            validateOnChange: true,
            validateOnBlur: true,
            validator: function(value){
            	if(/.*[\u4e00-\u9fa5]+.*$/.test(value)) 
							{ 
							return "不能包含中文"; 
							} 
							return true; 
            }
        }, {
            xtype: 'textfield',
            //fieldLabel: '用户名称',
			fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.usName","用户名称"),
			maxLength: 150,
			name: 'usName'
        }, {
            xtype: 'textfield',
            //fieldLabel: '手机号码',
			fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.mobile","手机号码"),
				maxLength: 25,
			name: 'mobile'
        }, {
            xtype: 'textfield',
            //fieldLabel: '电子邮箱',
			fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.email","电子邮箱"),
			name: 'email',
				maxLength: 70,
			vtype: 'email'
        }, /*{
            xtype: 'checkbox',
            //fieldLabel: '手机绑定标志',
			fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.mBindFlag","手机绑定标志"),
			name: 'mBindFlag',
			inputValue: 'Y',
			listeners: {
				"afterrender": function(mBindFlag){
			 	    var v = mBindFlag.getValue();
			 	    var bdMobile = mBindFlag.findParentByType("form").getForm().findField("bdMobile");
			 	    if(v == "Y"){
			 	    	bdMobile.allowBlank = false;
						  bdMobile.setReadOnly( false);
			 	    }else{
			 	    	bdMobile.allowBlank = true;
						  bdMobile.setReadOnly( true);
			 	    }
			 	},
				"change": function(checkObj,newValue,oldValue,eOpts){
					//手机号码
					var bdMobile = checkObj.findParentByType("form").getForm().findField("bdMobile");
					var mobile = checkObj.findParentByType("form").getForm().findField("mobile").getValue();
					
					if(newValue){
						bdMobile.allowBlank = false;
						bdMobile.setReadOnly( false);
						bdMobile.setValue(mobile);
					}else{
						bdMobile.allowBlank = true;
						bdMobile.setReadOnly( true);
						bdMobile.setValue("");
					}
				}
			}
    }*/{
			xtype: 'combobox',
			fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.mBindFlag","手机绑定标志"),
			editable:false,
			emptyText:'请选择',
			queryMode: 'remote',
			name: 'mBindFlag',
			valueField: 'TValue',
			displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("TZ_SJBD_BZ"),
			listeners:{
				select : function(combo,record,index) {
					//手机号码
					var bdMobile = combo.findParentByType("form").getForm().findField("bdMobile");
					var mobile = combo.findParentByType("form").getForm().findField("mobile").getValue();

					if(combo.getValue()=="Y"){
						bdMobile.allowBlank = false;
						bdMobile.setReadOnly( false);
						bdMobile.setFieldStyle('background:#FFFFFF');
						if(bdMobile.getValue() == ""){
							bdMobile.setValue(mobile);
						}
					}else{
						bdMobile.allowBlank = true;
						bdMobile.setReadOnly( true);
						bdMobile.setFieldStyle('background:#F4F4F4');
						bdMobile.setValue("");
					}
				}
			}
		},{
        xtype: 'textfield',
        //fieldLabel: '绑定手机号码',
				fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.bdMobile","绑定手机号码"),
				maxLength: 25,
				name: 'bdMobile'
    },/*{
       xtype: 'checkbox',
       //fieldLabel: '邮箱绑定标志',
			 fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.eBindFlag","邮箱绑定标志"),
			 name: 'eBindFlag',
			 inputValue: 'Y',
			 listeners: {
			 	  "afterrender": function(eBindFlag){
			 	    var v = eBindFlag.getValue();
			 	    var bdEmail = eBindFlag.findParentByType("form").getForm().findField("bdEmail");
			 	    if(v == "Y"){
			 	    	bdEmail.allowBlank = false;
						  bdEmail.setReadOnly( false);
			 	    }else{
			 	    	bdEmail.allowBlank = true;
						  bdEmail.setReadOnly( true);
			 	    }
			 		},
				  "change": function(checkObj,newValue,oldValue,eOpts){
						//电子邮箱
						var bdEmail = checkObj.findParentByType("form").getForm().findField("bdEmail");
						var email = checkObj.findParentByType("form").getForm().findField("email").getValue();
						if(newValue){
							bdEmail.allowBlank = false;
							bdEmail.setReadOnly( false);
							bdEmail.setValue(email);
						}else{
							bdEmail.allowBlank = true;
							bdEmail.setReadOnly( true);
							bdEmail.setValue("");
						}
				  }
			 }
    },*/ {
	xtype: 'combobox',
		fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.eBindFlag","邮箱绑定标志"),
		editable:false,
		emptyText:'请选择',
		queryMode: 'remote',
		name: 'eBindFlag',
		valueField: 'TValue',
		displayField: 'TSDesc',
		store: new KitchenSink.view.common.store.appTransStore("TZ_YXBD_BZ"),
			listeners:{
				select : function(combo,record,index) {
					//电子邮箱
					var bdEmail = combo.findParentByType("form").getForm().findField("bdEmail");
					var email = combo.findParentByType("form").getForm().findField("email").getValue();
					if(combo.getValue()=="Y"){
						bdEmail.allowBlank = false;
						bdEmail.setReadOnly( false);
						bdEmail.setFieldStyle('background:#FFFFFF');
						if(bdEmail.getValue() == ''){
							bdEmail.setValue(email);
						}
					}else{
						bdEmail.allowBlank = true;
						bdEmail.setReadOnly( true);
						bdEmail.setFieldStyle('background:#F4F4F4');
						bdEmail.setValue("");
					}
				}
			}
	},{
        xtype: 'textfield',
        //fieldLabel: '电子邮箱',
			  fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.bdEmail","绑定电子邮箱"),
			  name: 'bdEmail',
				maxLength: 70,
			  vtype: 'email'
    },{
    		xtype: 'combobox',
        fieldLabel: Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.jhState","激活状态"),
        editable:false,
        emptyText:'请选择',
        queryMode: 'remote',
	    	name: 'jhState',
	    	valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_JIHUO_ZT")
    },{
    		xtype: 'combobox',
        fieldLabel: Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.jhMethod","激活方式"),
        editable:false,
        emptyText:'请选择',
        queryMode: 'remote',
	    	name: 'jhMethod',
	    	valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_JIHUO_FS")
    },{
    		xtype: 'combobox',
        fieldLabel: Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.rylx","账号类型"),
        editable:false,
        emptyText:'请选择',
        queryMode: 'remote',
	    	name: 'rylx',
	    	valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_RYLX"),
    		afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
        ],
        allowBlank: false,
        listeners:{
            	afterrender: function(combox){
            		//当前登录人机构id
	            	var value = Ext.tzOrgID;
	            	if(value.toUpperCase() == "ADMIN"){
									//combox.setValue("NBYH");
	            	}else{
	            		//combox.setValue("NBYH");
	            		combox.readOnly = true;
	            	}
	            }
	          }
	          
    }, /*{
            xtype: 'checkbox',
            //fieldLabel: '锁定账号',
						fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.acctLock","锁定账号"),
						name: 'acctLock'
    }*/{
			xtype: 'combobox',
			fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.acctLock","锁定账号"),
			editable:false,
			emptyText:'请选择',
			queryMode: 'remote',
			name: 'acctLock',
			valueField: 'TValue',
			displayField: 'TSDesc',
			store: new KitchenSink.view.common.store.appTransStore("ACCTLOCK")
		},
    //{
    //        xtype: 'displayfield',
            //fieldLabel: '激活状态',
		//	fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.jhState","激活状态"),
		//	name: 'jhState'
    //    },
    // {
    //        xtype: 'displayfield',
            //fieldLabel: '激活方式',
		//	fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.jhMethod","激活方式"),
		//	name: 'jhMethod'
    //    }, 
    {
            xtype: 'textfield',
            //fieldLabel: '账号密码',
						fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.password","账号密码"),
						name: 'password',
						inputType: 'password',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            minLength: 6,
            maxLength: 32,
            allowBlank: false
        },{
            xtype: 'textfield',
            //fieldLabel: '账号密码',
						fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.reptPassword","确认密码"),
						name: 'reptPassword',
						inputType: 'password',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            minLength: 6,
            maxLength: 32,
            allowBlank: false,
            validator : function(v){
            	
              var form = this.findParentByType("form").getForm();
              var pwd = form.findField("password").getValue();
              
              if(v != "" && pwd != ""){
            	if(v ==pwd){
            		 return true;
            	}else{
            		return "输入的密码不一致";
            	}
              }else{
          		return true;
          	  }
            }
        }, {
            xtype: 'textfield',
            //fieldLabel: '用户名称',
            fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.orgNo","机构编号"),
            hidden:true,
            name: 'orgNo'
        },{
            xtype: 'combobox',
            //fieldLabel: '机构名称',
						fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.userOrg","机构名称"),
						forceSelection: true,
						editable: false,
						store: new KitchenSink.view.common.store.comboxStore({
							recname: 'TZ_JG_BASE_T',
							/*condition:{
								"TZ_JG_EFF_STA":{
									"value":"Y",
									"operator":"01",
									"type":"01"
								}
							},*/
							condition:{
								TZ_JG_EFF_STA:{
									value:"Y",
									operator:"01",
									type:"01"
								}
							},
							result:'TZ_JG_ID,TZ_JG_NAME'
						}),
            valueField: 'TZ_JG_ID',
            displayField: 'TZ_JG_NAME',
            //typeAhead: true,
            queryMode: 'remote',
						name: 'orgId',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            listeners:{
            	afterrender: function(combox){
            		//当前登录人机构id
	            	var value = Ext.tzOrgID;
	            	if(value.toUpperCase() == "ADMIN"){
	            		combox.readOnly = false ;
	            	}else{
	            		combox.readOnly = true;
	            	}
	            	//console.log(combox.findRecordByValue(value));
	            },
	            change: function(thisComb, newValue, oldValue, eOpts ){
	            	 //用户角色信息列表
									var grid = thisComb.findParentByType('userAccInfo').child('grid');
									//用户账号信息表单
									var form = thisComb.findParentByType('form').getForm();
									//账号
									var usAccNum = form.findField("usAccNum").getValue();

									var tzStoreParams = '{"usAccNum":"'+usAccNum+'","orgId":"'+newValue+'"}';
									grid.store.tzStoreParams = tzStoreParams;			
									grid.store.load();
	            }
            },
            allowBlank: false
        }, {
            xtype: 'textfield',
            //fieldLabel: '用户名称',
            //fieldLabel:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.originOrgId","用户原机构"),
            hidden: true,
            name: 'originOrgId'
        }]
    },{
    	xtype: 'grid', 
		height: 400, 
		title: '角色列表',
		frame: true,
		columnLines: true,
		reference: 'userRoleGrid',
		style:"margin:10px",
		store: {
			type: 'userRoleStore'
		},
		columns: [{ 
			//text: '角色编号',
			text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.roleID","角色编号"),
			dataIndex: 'roleID',
			hidden: true
		},{
			//text: '角色名称',
			text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.roleName","角色名称"),
			dataIndex: 'roleName',
			minWidth: 100,
			flex: 1
		},{
			xtype : 'checkcolumn',
			//text: '选中角色',
			text:Ext.tzGetResourse("TZ_AQ_YHZHGL_COM.TZ_AQ_YHZHXX_STD.isRole","选中角色"),
			dataIndex: 'isRole',
			width: 100
		}]
		//,			
		//bbar: {
		//	xtype: 'pagingtoolbar',
		//	pageSize: 10,
			/*store: {
				type: 'userRoleStore'
			},*/
		/*	listeners:{
				afterrender: function(pbar){
					var grid = pbar.findParentByType("grid");
					pbar.setStore(grid.store);
				}
			},
			displayInfo: true,
			displayMsg: '显示{0}-{1}条，共{2}条',
			beforePageText: '第',
			afterPageText: '页/共{0}页',
			emptyMsg: '没有数据显示',
			plugins: new Ext.ux.ProgressBarPager()
		}	*/
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
