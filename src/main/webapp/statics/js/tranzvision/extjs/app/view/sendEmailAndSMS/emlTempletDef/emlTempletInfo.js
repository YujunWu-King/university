
Ext.define('KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'emlTempletInfo', 
	controller: 'emlTempletInfoMth',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
		'Ext.grid.plugin.Clipboard',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		//'Ext.ux.Ueditor',
		'KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletInfoMth',
		'KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletItemModel',
		'KitchenSink.view.sendEmailAndSMS.emlTempletDef.emlTempletItemStore',
	    'tranzvision.extension.grid.column.Link'
	],

    title: '邮件模板定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
	constructor: function (){
        this.callParent();
    },
	initComponent: function(){
		var emlTempletInfoPanel = {
            emlServStore: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_EMLS_DEF_TBL',
				condition:{
					TZ_JG_ID:{
						value:'',
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_EMLSERV_ID,TZ_EML_ADDR100'
			}),
		    resTmplStore: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_TMP_DEFN_TBL',
				condition:{
					TZ_JG_ID:{
						value:'',
						operator:"01",
						type:"01"
					}
				},
				result:'TZ_YMB_ID,TZ_YMB_NAME'
			})
        };
		
		Ext.apply(this,{
            emlTempletInfoPanel:emlTempletInfoPanel,
			items: [{
				xtype: 'form',
				reference: 'emlTempletForm',
				layout: {
					type: 'vbox',       // Arrange child items vertically
					align: 'stretch'    // 控件横向拉伸至容器大小
				},
				actType:"add",
				border: false,
				bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
				
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 120,
					labelStyle: 'font-weight:bold'
				},
				
				items: [{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.emltempid","邮件模板编号"),
					name: 'emltempid',
					maxLength: 15,
					allowBlank: false,
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					]
				},{
					xtype: 'textfield',
					fieldLabel: '',
					name: 'emlContentMode',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.emltempname","邮件模板名称"),
					name: 'emltempname',
					maxLength: 60,
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					],
					allowBlank: false
				},{
					xtype: 'combobox',
					fieldLabel: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.emltemporg","机构"),
					forceSelection: true,
					editable: false,
					store: new KitchenSink.view.common.store.comboxStore({
						recname: 'TZ_JG_BASE_T',
						condition:{
							TZ_JG_EFF_STA:{
								value:"Y",
								operator:"01",
								type:"01"
							}
						},
						result:'TZ_JG_ID,TZ_JG_NAME'
					}),
					value:Ext.tzOrgID,
					valueField: 'TZ_JG_ID',
					displayField: 'TZ_JG_NAME',
					//typeAhead: true,
					queryMode: 'local',
					name: 'emltemporg',
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					],
					listeners:{
						select: 'changeOrg',
						afterrender: function(combox){
							//当前登录人机构id
							if( (combox.value).toUpperCase() != 'ADMIN')
							{
								combox.readOnly = true;
							}
							//console.log(combox.findRecordByValue(value));
						}
					},
					allowBlank: false
				},{
					xtype: 'tabpanel',
                    height:'auto',
					listeners:{
						tabchange: function(tabPanel,newCard){
							if (newCard.title == "编辑邮件内容")
							{
								var form = tabPanel.findParentByType('form').getForm();
								
								var emlContentMode = form.findField("emlContentMode");
								var emlContent = form.findField("emltmpcontent");
								var emlContentHtml = form.findField("emltmpcontentHtml");
								if(emlContentMode.value != 'ueditor')
								{
									emlContentHtml.setValue(emlContent.value);
									emlContentMode.setValue('ueditor');
								};
								
								var copyItemsDom = document.getElementsByName("itememlCopy");
								
								for (var i=0;i<copyItemsDom.length;i++)
								{
									$(copyItemsDom[i]).zclip({
										beforeCopy:function(){
											var itemHtml = this.parentNode.parentNode.parentNode.innerHTML;
											var itemFirstCharPositon = itemHtml.indexOf("["); 
											var itemLastCharPositon = itemHtml.indexOf("]"); 
											var itemPara = itemHtml.slice(itemFirstCharPositon,itemLastCharPositon+1);
											form.findField("copyfield").setValue(itemPara);
										},
										copy:function(){
											return form.findField("copyfield").getValue();
										}
									});
								}
							}									
						}
					},
					frame: true,
					items:[{
							title:'基本信息',
							items:[{
								layout: {
									type: 'vbox',       // Arrange child items vertically
									align: 'stretch'    // 控件横向拉伸至容器大小
								},
                                border: false,
								bodyPadding: 10,
								bodyStyle:'overflow-y:auto;overflow-x:hidden',
								fieldDefaults: {
									msgTarget: 'side',
									labelWidth: 120,
									labelStyle: 'font-weight:bold'
								},
								items:[
                                  {
                                    xtype:'checkbox',
                                    fieldLabel: Ext.tzGetResourse('TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.isuse','是否启用'),
                                    name:'isuse',
                                    inputValue:'Y',
                                    uncheckedValue:'N',
                                    afterLabelTextTpl: [
                                      '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                  ]
                                  },{
                                    xtype:'checkbox',
                                    fieldLabel:Ext.tzGetResourse('TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.ifRpt','是否判重'),
                                    name:'ifRpt',
                                    inputValue:'Y',
                                    uncheckedValue:'N'
                                    },{
									xtype: 'combobox',
									fieldLabel: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.restempid","模版类型"),
									forceSelection: true,
									editable: false,
									/**/
									store: new KitchenSink.view.common.store.comboxStore({
										recname: 'TZ_TMP_DEFN_TBL',
										condition:{
										},
										result:'TZ_YMB_ID,TZ_YMB_NAME'
									}),
									valueField: 'TZ_YMB_ID',
									displayField: 'TZ_YMB_NAME',
									queryMode: 'local',
									name: 'restempid',
									listeners:{
										select: 'changeResTmpl',
										focus: function (combo,event, eOpts) {
											var form = this.findParentByType("form");
											var emltemporg = form.getForm().findField("emltemporg").getValue();
										   
											var tzStoreParams ='{"OperateType":"COMBOX","recname":"TZ_TMP_DEFN_TBL","condition":{"TZ_JG_ID":{"value":"'+emltemporg+'","operator":"01","type":"01"},"TZ_USE_FLAG":{"value":"'+'Y'+'","operator":"01","type":"01"}},"result":"TZ_YMB_ID,TZ_YMB_NAME"}';
											
											var arrayData;
											
											arrayData = new Array();
											
											emlTempletInfoPanel.resTmplStore.tzStoreParams=tzStoreParams;
											
											emlTempletInfoPanel.resTmplStore.load({
												scope: this,
												callback: function(records, operation, success) {
													//console.log(records);
													for(var i=0;i<records.length;i++){
														arrayData.push(records[i].data);
													}
												combo.store.loadData(arrayData);
												}
											} );
										}
									},
									afterLabelTextTpl: [
										'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
									],
									allowBlank: false
								},{
									xtype: 'textarea',
									fieldLabel: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.emltmpdesc","描述"),
									name:'emltmpdesc'
								},{
									xtype: 'combobox',
									fieldLabel: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.tempemailserv","邮箱服务"),
									forceSelection: true,
									editable: false,
									
									store: new KitchenSink.view.common.store.comboxStore({
										recname: 'TZ_EMLS_DEF_TBL',
										condition:{
										},
										result:'TZ_EMLSERV_ID,TZ_EML_ADDR100'
									}),
									
									valueField: 'TZ_EMLSERV_ID',
									displayField: 'TZ_EML_ADDR100',
									queryMode: 'local',
									name: 'tempemailserv',
									listeners: {
					  
										focus: function (combo,event, eOpts) {

											var form = this.findParentByType("form");
											var emltemporg = form.getForm().findField("emltemporg").getValue();
										   
											var tzStoreParams ='{"OperateType":"COMBOX","recname":"TZ_EMLS_DEF_TBL","condition":{"TZ_JG_ID":{"value":"'+emltemporg+'","operator":"01","type":"01"}},"result":"TZ_EMLSERV_ID,TZ_EML_ADDR100"}';
											
											var arrayData;
											
											arrayData = new Array();
											
											emlTempletInfoPanel.emlServStore.tzStoreParams=tzStoreParams;
											
											emlTempletInfoPanel.emlServStore.load({
												scope: this,
												callback: function(records, operation, success) {
													//console.log(records);
													for(var i=0;i<records.length;i++){
														arrayData.push(records[i].data);
													}
												combo.store.loadData(arrayData);
												}
											} );
										 }
									},
									afterLabelTextTpl: [
										'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
									],
									allowBlank: false
								}]
							}]
						},{
							title:'编辑邮件内容',
							items:[{
                                xtype:'form',
								layout: {
									type: 'vbox',       // Arrange child items vertically
									align: 'stretch'    // 控件横向拉伸至容器大小
								},
                                border: false,
								bodyStyle:'overflow-y:auto;overflow-x:hidden',
								fieldDefaults: {
									msgTarget: 'side',
									labelWidth: 120,
									labelStyle: 'font-weight:bold'
								},
								items:[{
									xtype: 'textfield',
									fieldLabel: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.emltmpsubject","邮件主题"),
									maxLength: 254,
									name: 'emltmpsubject',
                                    margin:10
								},{
									xtype: 'checkbox',
									boxLabel: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.ispersonalize","邮件内容不包含个性化动态合并内容"),
									name:'ispersonalize',
                                    margin:10,
									inputValue:'Y',
									uncheckedValue:'N',
									style:{
										marginLeft:'123px'
									}

								},{
									xtype: 'checkbox',
									boxLabel: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.ishtml","以HTML格式发送邮件"),
									name:'ishtml',
                                    margin:10,
									inputValue:'Y',
									checked:true,
									uncheckedValue:'N',
									style:{
										marginLeft:'123px'
									},
									handler:'htmlSwitch',
									afterRender:function()
									{
										var form = this.findParentByType("form").getForm();
										if(this.checked){
											var emlContent = form.findField("emltmpcontent");
											var emlContentHtml = form.findField("emltmpcontentHtml");
											emlContent.setHidden(true);
											emlContentHtml.setHidden(false);
										}else
										{
											var emlContent = form.findField("emltmpcontent");
											var emlContentHtml = form.findField("emltmpcontentHtml");
											emlContent.setHidden(false);
											emlContentHtml.setHidden(true);
										}
									}
								},{
									xtype: 'textarea',
									fieldLabel: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.emltmpcontent","邮件内容"),
									name:'emltmpcontent',
                                    margin:10
								},{
									//xtype: 'htmleditor',
									xtype: 'ueditor',
									fieldLabel: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.emltmpcontentHtml","邮件内容"),
									height: 300,
									zIndex: 900,
									name:'emltmpcontentHtml',
                                    margin:10,
                                    panelXtype: 'emlTempletInfo'  
								}
								,{
									xtype: 'textfield',
									fieldLabel: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.copyfield","参数信息项复制"),
									name: 'copyfield',
									hidden:true
								},
								{
									xtype: 'grid',
                                    height:200,
                                    border: false,
                                    title:'信息项列表',
									name: 'emlTmplItemGrid',
									reference: 'emlTmplItemGrid',
									/*
									selModel: {
										type: 'spreadsheet',
										columnSelect: false  // replaces click-to-sort on header
									},
									plugins: 'clipboard',
									*/
									store: {
												type: 'emlTempletItemStore'
										   },
									columnLines: true,    //显示纵向表格线	
									columns: [{ 
										text: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.parainfoitem","信息项名称"),
										sortable: true,
										dataIndex: 'parainfoitem',
										minWidth: 200,
										flex:1
									},{
										xtype:'linkcolumn',
										text: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.iteminsert","插入"),
										sortable: false,	
										width:70,
										items:[{
											text:"插入",
											handler: "insertemlitem",
											tooltip:"插入"
										}]
									},{
										text: Ext.tzGetResourse("TZ_EML_TMPL_MG_COM.TZ_EML_TMPL_STD.itememlCopy","复制"),
										menuDisabled: true,
										sortable: false,
										dataIndex: 'itemcopy',
										width:70,
										renderer:function(){
										   return '<a href="javascript:void(0)" name="itememlCopy">复制</a>';
										}
									}]
								}]
							}]
						  }
						]
				}]
			}]
		});
		this.callParent();
	},
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