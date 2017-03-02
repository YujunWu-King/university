Ext.define('KitchenSink.view.common.importExcel.UnifiedImportWindow', {
    extend: 'Ext.window.Window',
    requires: [
        'Ext.layout.container.Card',
        'KitchenSink.view.common.importExcel.UnifiedImportController'
    ],
    xtype: 'unifiedimportwindow',
    layout:'fit',
    ignoreChangesFlag: true,
    viewModel: {
        data: {
            title: '数据导入'
        }
    },
    bind: {
        title: '{title}'
    },
    modal:true,
    defaults: {
        border:false
    },
    defaultListenerScope: true,
    listeners:{
        resize: function(win){
            win.doLayout();
        },
		afterrender:function(){
			//处理在火狐下fieldset下的上传控件按钮宽度不够的问题
			var filebutton = this.down("#orguploadfile").getTrigger('filebutton');;
			filebutton.el.dom.style.width='65px';
		}
    },
    constructor:function(tplId){
    	this.tplId = tplId;
    	this.callParent();
    },
    initComponent: function(){
        var me = this,
        	columnWidths = [.8,.2],
            excelLinkHidden = false,
            excelTplUrl;

        //获取导入模板基本信息
        var tzParams = '{"ComID":"TZ_UNIFIED_IMP_COM","PageID":"TZ_UNIFIED_IMP_STD","OperateType":"QF","comParams":{"tplId":"'+me.tplId+'"}}';

        var tplInfo;
		Ext.tzLoadAsync(tzParams,function(responseData){
			
			tplInfo = responseData;
			
			excelTplUrl = responseData["excelTpl"]||"";
			
			if(excelTplUrl==""){
				//上传Excel和下载Excel模板列宽，是否显示下载Excel链接
				columnWidths = [1,0];
				excelLinkHidden = true;
			}else{
				excelTplUrl = TzUniversityContextPath+excelTplUrl;
			}
			
		});
        
		if(tplInfo==undefined){
			Ext.defer(function(w){
				w.close();
			},100,me,[me]);
		}
		
		//允许调整映射关系？
		var enableAdjustMapping = tplInfo["enableMapping"]=="Y";
		
		//映射关系字段
		var mappingStore = new Ext.data.Store({
			fields:['columnTitle','field'],
			data:[]
		})
		
		//导入模板字段
		var tplFieldsStore = new Ext.data.Store({
			fields:['field','fieldName','columnTitle'],
			autoLoad: true,
			pageSize: 0,
			comID: 'TZ_UNIFIED_IMP_COM',
			pageID: 'TZ_UNIFIED_IMP_STD',
			proxy: Ext.tzListProxy(),
			tzStoreParams:Ext.encode({
				cfgSrhId:"TZ_UNIFIED_IMP_COM.TZ_UNIFIED_IMP_STD.TZ_IMP_TPL_FLD_T",
				condition:{
					"TZ_TPL_ID-operator":"01",
					"TZ_TPL_ID-value":me.tplId
				}
			})
		})
		
        Ext.apply(this,{
            items:[
                {
                    xtype:'panel',
                    layout: 'card',
                    tplId:me.tplId,
                    controller:'unifiedimport',
                    width: 800,
                    enableAdjustMapping:enableAdjustMapping,
                    minHeight:200,                   
                    maxHeight:500,
                    tplFieldsStore:tplFieldsStore,
                    bbar: [
                        {
                            xtype:'tbtext',
                            itemId: 'rowCount',
                            baseCls:Ext.baseCSSPrefix+'panel-header-title-light',
                            defaultTextMsg    : TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00028"),
                            hidden:true
                        },{
                            xtype:'tbseparator',
                            hidden:true
                        },{
                            xtype:'tbtext',
                            itemId: 'msgTip',
                            text :'<span style="color:#b11b3b;font-weight: bold">提示：</span>为避免页面显示过慢，最多提供20列*1000行的数据供预览',
                            hidden:true
                        },
                        '->',
                        {
                            itemId: 'card-prev',
                            iconCls:'prev',
                            text: '上一步',
                            handler: 'showPrevious',
                            hidden: true
                        },
                        {
                            itemId: 'card-next',
                            iconCls:'next',
                            text: '下一步',
                            handler: 'showNext'
                        },
                        {
                            text: '关闭',
                            iconCls:"close",
                            handler: function(btn){
                                btn.findParentByType('window').close();
                            }
                        }
                    ],
                    items: [
                        {
							title:'数据导入',
                            header:false,
                            name:'importExcel',
                            bodyPadding:10,
                            xtype:'form',
                            itemId:'card-0',
                            items:[
                                {
                                    xtype:'form',
                                    name:'uploadExcelForm',
                                    items:[{
                                        xtype:'fieldset',
                                        title: '<span style="font-weight: bold;" class="themeColor">上传Excel文件</span>',
                                        name:'uploadExcel',
                                        collapsible:true,
                                        collapsed:false,
                                        toggleOnTitleClick:true,
                                        checkboxToggle:true,
                                        listeners:{
                                            expand:function( fieldset, eOpts ){
                                                fieldset.findParentByType('window').down('fieldset[name=pasteExcelData]').collapse( );
                                            },
                                            beforecollapse:function( fieldset, eOpts ){
                                                var pasteExcelData=fieldset.findParentByType('window').down('fieldset[name=pasteExcelData]');
                                                if(pasteExcelData.collapsed)pasteExcelData.expand();
                                            }
                                        },
                                        defaults: {
                                            anchor: '100%',
                                            layout: {
                                                type: 'hbox',
                                                defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
                                            }
                                        },
                                        items:[
                                            {
                                                layout:'column',
                                                items:[
                                                    {
                                                        xtype: 'filefield',
                                                        name: 'orguploadfile',
                                                        itemId:'orguploadfile',
                                                        msgTarget: 'side',
                                                        allowBlank: false,
                                                        anchor: '100%',
                                                        buttonText: '浏览...',
                                                        columnWidth:columnWidths[0],
                                                        validator:function(value){
                                                            var excelReg = /\.([xX][lL][sS]){1}$|\.([xX][lL][sS][xX]){1}$/;
                                                            if(!excelReg.test(value)&&value){
                                                                Ext.Msg.alert('提示','文件类型错误,请选择 [xls,xlsx] 格式的Excel文件');
                                                                return '文件类型错误,请选择 [xls,xlsx] 格式的Excel文件';
                                                            }else{
                                                                return true
                                                            }
                                                        }
                                                    },
                                                    {
                                                        xtype:'toolbar',
                                                        name:'downloadExcelTpl',
                                                        hidden:excelLinkHidden,
                                                        padding:0,
                                                        style:'margin-left:20px',
                                                        columnWidth:columnWidths[1],
                                                        items:[
                                                            {
                                                                xtype:'button',
                                                                text:'<span class="themeColor" style="text-decoration: underline">下载Excel模板</span>',
                                                                cls:'themeColor',
                                                                border:false,
                                                                style:{
                                                                    background:'white'
                                                                },
                                                                href:excelTplUrl
                                                            }
                                                        ]
                                                    }
                                                ]
                                            }
                                            ,
                                            {
                                                xtype: 'checkboxfield',
                                                boxLabel: '首行是标题行',
                                                name: 'firstLineTitle_1',
                                                value:"Y",
                                                inputValue: 'Y'
                                            }
                                        ]
                                    }]
                                },
                                {
                                    xtype:'fieldset',
                                    title: '<span style="font-weight: bold;" class="themeColor">粘贴Excel数据</span>',
                                    name:'pasteExcelData',
                                    collapsible:true,
                                    toggleOnTitleClick:true,
                                    checkboxToggle:true,
                                    collapsed:true,
                                    listeners:{
                                        expand:function( fieldset, eOpts ){
                                            fieldset.findParentByType('window').down('fieldset[name=uploadExcel]').collapse();
                                        },
                                        beforecollapse:function( fieldset, eOpts ){
                                            var uploadExcel=fieldset.findParentByType('window').down('fieldset[name=uploadExcel]');
                                            if(uploadExcel.collapsed)uploadExcel.expand();
                                        }
                                    },
                                    defaults: {
                                        anchor: '100%',
                                        layout: {
                                            type: 'hbox',
                                            defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
                                        }
                                    },
                                    items:[
                                        {
                                            xtype: 'textarea',
                                            name: 'excelText',
                                            itemId:'excelText',
                                            msgTarget: 'side',
                                            grow      : true,
                                            growMax :150,
                                            anchor    : '100%',
                                            allowBlank: false
                                        },
                                        {
                                            xtype: 'checkboxfield',
                                            boxLabel: '首行是标题行',
                                            name: 'firstLineTitle_2',
                                            inputValue: 'Y'
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            title:'预览数据',
                            header:false,
                            name:'previewExcelData',
                            itemId:'card-1',
                            xtype:'form',
                            ignoreLabelWidth: true
                        },
                        {
                            title:'调整映射关系',
                            header:false,
                            name:'adjustMapping',
                            itemId:'card-2',
                            xtype:'grid',
                            store:mappingStore,
                            maxHeight:200,
                            plugins: [{
                                  ptype: 'cellediting',
                                  clicksToEdit: 1
                            }],
                            columns:[{
                                text     : '导入模板列标题',
                                flex     : 1,
                                sortable : false,
                                dataIndex: 'columnTitle'
                            },{
                                text     : '目标业务数据字段',
                                flex     : 1,
                                sortable : false,
                                dataIndex: 'field',
                                editor:{
                                	xtype:'combo',
                                	editable:false,
                                	queryMode:'local',
                                	valueField:'field',
                                	displayField:'fieldName',
                                	store:tplFieldsStore,
                                	triggers:{
                                        clear: {
                                            cls: 'x-form-clear-trigger',
                                            handler: function(field){
                                                field.setValue("");
                                            }
                                        }
                                    }
                                },
                                renderer:function(v){
                                    if(v){
                                        var i = tplFieldsStore.find('field',v,0,false,true,false);
                                        if(i>-1){
                                            return tplFieldsStore.getAt(i).get("fieldName");
                                        }else{
                                            return '';
                                        }
                                    }
                                }
                            }]
                        }
                        ,
                        //此处仅作最后一步处理的流程，不作展示
                        {
                            title:'处理数据',
                            header:false,
                            name:'dealExcelData',
                            itemId:'card-3'
                        }
                    ]
                }
            ]
        });
        this.callParent()
    }
});