
Ext.define('KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'smsTempletInfo', 
	controller: 'smsTempletInfoMth',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
		'Ext.grid.plugin.Clipboard',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		//'Ext.ux.Ueditor',
		'KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletInfoMth',
		'KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletItemModel',
		'KitchenSink.view.sendEmailAndSMS.smsTempletDef.smsTempletItemStore',
	    'tranzvision.extension.grid.column.Link'
	],

    title: '短信模板定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
	constructor: function (){
        this.callParent();
    },
	initComponent: function(){
		var smsTempletInfoPanel = {
            smsServStore: new KitchenSink.view.common.store.comboxStore({
				recname: 'TZ_SMSSERV_TBL',
				condition:{
				},
				result:'TZ_SMS_SERV_ID,TZ_SMS_SERV_NAME'
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
            smsTempletInfoPanel:smsTempletInfoPanel,
			items: [{
				xtype: 'form',
				reference: 'smsTempletForm',
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
					fieldLabel: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.smstempid","短信模板编号"),
					name: 'smstempid',
					allowBlank: false,
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					]
				},{
					xtype: 'textfield',
					fieldLabel: '',
					name: 'smsContentMode',
					hidden:true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.smstempname","短信模板名称"),
					name: 'smstempname',
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					],
					allowBlank: false
				},{
					xtype: 'combobox',
					fieldLabel: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.smstemporg","机构"),
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
					value:Ext.tzOrgID,
					valueField: 'TZ_JG_ID',
					displayField: 'TZ_JG_NAME',
					//typeAhead: true,
					queryMode: 'local',
					name: 'smstemporg',
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					],
					listeners:{
						select: 'changeOrg',
						afterrender: function(combox){
							//当前登录人机构id
							console.log(combox.value);
							if(combox.value != 'ADMIN')
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
							if (newCard.title == "编辑短信内容")
							{
								var form = tabPanel.findParentByType('form').getForm();
								
								var copyItemsDom = document.getElementsByName("itemsmsCopy");
								
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
								items:[{
                                    xtype:'checkbox',
                                    fieldLabel: Ext.tzGetResourse('TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.isuse','是否启用'),
                                    name: 'isuse',
                                    inputValue:'Y',
                                    uncheckedValue:'N',
                                    afterLabelTextTpl: [
                                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
                                    ]

                                },{
									xtype: 'combobox',
									fieldLabel: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.restempid","模版类型"),
									forceSelection: true,
									editable: false,
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
											var smstemporg = form.getForm().findField("smstemporg").getValue();
										   
											var tzStoreParams ='{"OperateType":"COMBOX","recname":"TZ_TMP_DEFN_TBL","condition":{"TZ_JG_ID":{"value":"'+smstemporg+'","operator":"01","type":"01"},"TZ_USE_FLAG":{"value":"'+'Y'+'","operator":"01","type":"01"}},"result":"TZ_YMB_ID,TZ_YMB_NAME"}';
											
											var arrayData;
											
											arrayData = new Array();
											
											smsTempletInfoPanel.resTmplStore.tzStoreParams=tzStoreParams;
											
											smsTempletInfoPanel.resTmplStore.load({
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
									fieldLabel: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.smstmpdesc","描述"),
									name:'smstmpdesc'
								},{
									xtype: 'combobox',
									fieldLabel: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.tempsmsserv","短信服务"),
									forceSelection: true,
									editable: false,
									
									store: new KitchenSink.view.common.store.comboxStore({
										recname: 'TZ_SMSSERV_TBL',
										condition:{
										},
										result:'TZ_SMS_SERV_ID,TZ_SMS_SERV_NAME'
									}),
									
									valueField: 'TZ_SMS_SERV_ID',
									displayField: 'TZ_SMS_SERV_NAME',
									queryMode: 'local',
									name: 'tempsmsserv',
									/*
									listeners: {
					  
										focus: function (combo,event, eOpts) {

											var form = this.findParentByType("form");
											var smstemporg = form.getForm().findField("smstemporg").getValue();
										   
											var tzStoreParams ='{"OperateType":"COMBOX","recname":"TZ_EMLS_DEF_TBL","condition":{"TZ_JG_ID":{"value":"'+smstemporg+'","operator":"01","type":"01"}},"result":"TZ_EMLSERV_ID,TZ_EML_ADDR100"}';
											
											var arrayData;
											
											arrayData = new Array();
											
											smsTempletInfoPanel.smsServStore.tzStoreParams=tzStoreParams;
											
											smsTempletInfoPanel.smsServStore.load({
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
									},*/
									afterLabelTextTpl: [
										'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
									],
									allowBlank: false
								}]
							}]
						},{
							title:'编辑短信内容',
                            xtype:'form',
							items:[{	
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
									xtype: 'checkbox',
									boxLabel: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.ispersonalize","短信内容不包含个性化动态合并内容"),
									name:'ispersonalize',
									inputValue:'Y',
                                    margin:10,
									uncheckedValue:'N',
									style:{
										marginLeft:'123px'
									}

								},{
									xtype: 'textarea',
									fieldLabel: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.smstmpcontent","短信内容"),
									name:'smstmpcontent',
                                    margin:10
								},{
									xtype: 'textfield',
									fieldLabel: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.copyfield","参数信息项复制"),
									name: 'copyfield',
									hidden:true
								},{
									xtype: 'grid',
                                    height:200,
                                    border: false,
                                    title:'信息项列表',
									name: 'smsTmplItemGrid',
									reference: 'smsTmplItemGrid',
									/*
									selModel: {
										type: 'spreadsheet',
										columnSelect: false  // replaces click-to-sort on header
									},
									plugins: 'clipboard',
									*/
									store: {
												type: 'smsTempletItemStore'
										   },
									columnLines: true,    //显示纵向表格线	
									columns: [{ 
										text: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.parainfoitem","信息项名称"),
										sortable: true,
										dataIndex: 'parainfoitem',
										minWidth: 200,
										flex:1
									},{
										xtype:'linkcolumn',
										text: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.iteminsert","插入"),
										sortable: false,	
										width:70,
										items:[{
											text:"插入",
											handler: "insertSmsitem",
											tooltip:"插入"
										}]
									},{
										text: Ext.tzGetResourse("TZ_SMS_TMPL_MG_COM.TZ_SMS_TMPL_STD.itemsmsCopy","复制"),
										menuDisabled: true,
										sortable: false,
										dataIndex: 'itemcopy',
										width:70,
										renderer:function(){
										   return '<a href="javascript:void(0)" name="itemsmsCopy">复制</a>';
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