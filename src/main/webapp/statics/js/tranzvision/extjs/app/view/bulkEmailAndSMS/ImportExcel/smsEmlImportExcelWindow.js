/*=============================================================
+ 功能描述：短信邮件群发Excel导入
+ 开发人：张浪
+ 开发时间：2016-01-11
+=============================================================*/
Ext.define('KitchenSink.view.bulkEmailAndSMS.ImportExcel.smsEmlImportExcelWindow', {
    extend: 'Ext.window.Window',
    requires: [
		'Ext.grid.*',
    	'Ext.data.*',
        'Ext.layout.container.Card',
		'Ext.ux.data.PagingMemoryProxy'
    ],
    xtype: 'smsEmlImportExcelWindow',
    layout:'fit',
    ignoreChangesFlag: true,
    viewModel: {
        data: {
            title: '导入Excel'
        }
    },
    bind: {
        title: '{title}'
    },
    constructor: function (config) {
		this.sendPicId = config.sendPicId;
        this.sendType = config.sendType;//导入发送类型，SMS-短信发送导入，EML-邮件发送导入
		this.callback = config.callback;
		this.callParent();
    },
    modal:true,
	savedFlag: false,
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
			//var filebutton = this.down("#excelFile").getTrigger('filebutton');
			var filebutton = this.down("#orguploadfile").getTrigger('filebutton');
			filebutton.el.dom.style.width='65px';
		}
    },
    initComponent: function(){
        var me = this;
		
        Ext.apply(this,{
            items:[
                {
                    xtype:'panel',
                    layout: 'card',
                    width: 800,
                    minHeight:200,
                    maxHeight:450,
                    buttons: [
                        {
                            xtype:'tbtext',
                            itemId: 'rowCount',
                            baseCls:Ext.baseCSSPrefix+'panel-header-title-light',
                            defaultTextMsg    : TranzvisionMeikecityAdvanced.Boot.getMessage("TZGD_FWINIT_00028"),
                            hidden:true
                        },
                        '->',
                        {
                            itemId: 'card-prev',
                            iconCls:'prev',
                            text: '上一步',
                            handler: 'showPrevious',
                            disabled: true
                        },
                        {
                            itemId: 'card-next',
                            iconCls:'next',
                            text: '下一步',
                            handler: 'showNext'
                        },{
							itemId: 'card-save',	
							iconCls:"save",
							text: '保存',
							hidden: true,
							handler: 'saveExcelDate',
						},{
							itemId: 'card-ensure',	
							iconCls:"ensure",
							text: '确定',
							hidden: true,
							handler: 'ensureSaveDate',
						},{
                            text: '关闭',
                            iconCls:"close",
                            handler: function(btn){
                                btn.findParentByType('window').close();
                            }
                        }
                    ],
                    items: [

                        {title:'导入Excel',
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
                                        collapsed:false,/*默认展开*/
                                        toggleOnTitleClick:true,
                                        checkboxToggle:true,
                                        listeners:{
                                            expand:function( fieldset, eOpts ){
                                                fieldset.findParentByType('smsEmlImportExcelWindow').down('fieldset[name=pasteExcelData]').collapse( );
                                            },
                                            beforecollapse:function( fieldset, eOpts ){
                                                var pasteExcelData=fieldset.findParentByType('smsEmlImportExcelWindow').down('fieldset[name=pasteExcelData]');
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
                                                        //name: 'excelFile',
                                                        name: 'orguploadfile',
                                                        //itemId:'excelFile',
                                                        itemId:'orguploadfile',
                                                        msgTarget: 'side',
                                                        allowBlank: false,
                                                        anchor: '100%',
                                                        buttonText: '浏览...',
                                                        columnWidth:1,
                                                        validator:function(value){
                                                            var excelReg = /\.([xX][lL][sS]){1}$|\.([xX][lL][sS][xX]){1}$/;
                                                            if(!excelReg.test(value)&&value){
                                                                Ext.Msg.alert('提示','文件类型错误,请选择 [xls,xlsx] 格式的Excel文件');
                                                                return '文件类型错误,请选择 [xls,xlsx] 格式的Excel文件';
                                                            }else{
                                                                return true
                                                            }
                                                        }
                                                    }
                                                ]
                                            }
                                            ,
                                            {
                                                xtype: 'checkboxfield',
                                                boxLabel: '首行是标题行',
                                                name: 'firstLineTitle_1',
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
                                    collapsed:true,/*默认折叠*/
                                    listeners:{
                                        expand:function( fieldset, eOpts ){
                                            fieldset.findParentByType('smsEmlImportExcelWindow').down('fieldset[name=uploadExcel]').collapse( );
                                        },
                                        beforecollapse:function( fieldset, eOpts ){
                                            var uploadExcel=fieldset.findParentByType('smsEmlImportExcelWindow').down('fieldset[name=uploadExcel]');
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
							title:'标题行解析(仅解析Excel前20列)',
							header:false,
							name:'titleExcelDate',
							itemId:'card-1',
							xtype:'form',
							ignoreLabelWidth: true
						},
                        {
                            title:'预览导入数据',
                            header:false,
                            name:'previewExcelData',
                            itemId:'card-2',
                            xtype:'form',
                            ignoreLabelWidth: true
                        }
                    ]
                }
            ]
        });
        this.callParent()
    },
    showNext: function (btn) {
        this.doCardNavigation(1);
    },

    showPrevious: function (btn) {
        this.doCardNavigation(-1);
    },
	
    doCardNavigation: function (incr) {
        var me = this;
        var l = me.child('panel').getLayout();
        var i = l.activeItem.itemId.split('card-')[1];
        var next = parseInt(i, 10) + incr;
        var displayRowCount=me.down('#rowCount');

        /*第一步：导入或者粘贴Excel数据*/
        if( l.activeItem.name == 'previewExcelData'&&incr==-1){
            displayRowCount.setVisible(false);
        }

        /*第二步：解析并设置Excel标题行*/
        if( l.activeItem.name == 'importExcel'){
            var uploadExcel =  l.activeItem.down('fieldset[name=uploadExcel]'),
                pasteExcelData = l.activeItem.down('fieldset[name=pasteExcelData]');


            /*预览数据columns 和 data*/
            var columns,
                data = [],
                dataArray = [],
                dataWithColumns = [],
                columnsLength;

            if(!uploadExcel.collapsed){
                //解析上传Excel文件
                //var filename = me.down("#excelFile").getValue();
            	var filename = me.down("#orguploadfile").getValue();
            	
                var form = me.down('form[name=uploadExcelForm]').getForm();
                if(filename&&form.isValid()){
                    //var dateStr = Ext.Date.format(new Date(), 'Ymd');
                    //var filePath = '/linkfile/FileUpLoad'
                	var filePath = 'smsAndEmailTmpExcel';
                    var updateUrl =TzUniversityContextPath+'/UpdServlet';

                    form.submit({
                        url: updateUrl,
                        waitMsg: '正在上传Excel...',
                        params: {
                            filePath: filePath
                        },
                        success: function (form, action) {
                            var sysFileName = action.result.msg.sysFileName;
                            //var path = action.result.msg.path;
                            var path = action.result.msg.accessPath;
                            
                            /*后台解析Excsel*/
                            Ext.MessageBox.show({
                                msg: '解析数据中，请稍候...',
                                progress: true,
                                progressText:'解析中...',
                                width: 300,
                                wait: {
                                    interval: 50
                                }
                            });

                            var tzParams = '{"ComID":"TZ_QF_EXCIMP_COM","PageID":"TZ_QF_EXCIMP_STD","OperateType":"tzAnalysisExcel","comParams":{"path":'+Ext.JSON.encode(path)+',"sysFileName":'+Ext.JSON.encode(sysFileName)+'}}';

                            Ext.tzLoad(tzParams,function(responseData){
                                if(responseData.error){
                                    Ext.Msg.alert("错误",responseData.error);
                                    return;
                                }
                                dataWithColumns = responseData;
                                var firstLineTitle_1 = me.down('checkboxfield[name=firstLineTitle_1]').getValue();
								//首行为标题行
								if(firstLineTitle_1){
									columns = dataWithColumns[0];
									dataWithColumns = dataWithColumns.slice(1);	
								}
								columnsLength = dataWithColumns[i].length;
                                me.dataWithColumns=dataWithColumns;
								
								//显示标题行设置
								me.headerSetGrid(me,columns,columnsLength);
                                Ext.MessageBox.hide();
                            });

                        },
                        failure: function (form, action) {
                            Ext.MessageBox.alert("错误", action.result.msg);
                            return;
                        }
                    });
                }else
                {
                    Ext.MessageBox.show({
                        title: '提示',
                        msg: '未选择Excel文件!',
                        buttons: Ext.Msg.OK,
                        scope: this,
                        icon:  Ext.Msg.WARNING});
                    return;
                }
            }else{
                //解析粘贴Excel数据
                var excelText = me.down('#excelText').getValue();
                if(!excelText||excelText.replace(/(^\s*)|(\s*$)/g, "")==""){
                    Ext.MessageBox.show({
                        title: '提示',
                        msg: '请粘贴Excel数据!',
                        buttons: Ext.Msg.OK,
                        scope: this,
                        icon:  Ext.Msg.WARNING});
                    return;
                };
                var firstLineTitle_2 = me.down('checkboxfield[name=firstLineTitle_2]').getValue(),
                    columnsData = excelText.split("\n");//获取每行数据

                var displayCount,allCount;
                for(var i = 0;i<columnsData.length;i++){
                    if(columnsData[i].replace(/(^\s*)|(\s*$)/g, "")=="") continue;

                    //var columnData = columnsData[i].split(/\s+/);//以空格拆分每一列
                    var columnData = columnsData[i].split('\t');//以制表符拆分每一列
                    columnsLength = (columnsLength==undefined?columnData.length:((columnsLength<columnData.length)?columnData.length:columnsLength));
                    if(i==0&&firstLineTitle_2/*首行为标题行*/){
                        columns = columnData;
                    }else{
                    	//去除所有前后空白;
                    	for(var j = 0; j < columnData.length; j++){
                    		columnData[j] = columnData[j].replace(/(^\s*)|(\s*$)/g, "")
                    	}
						dataWithColumns.push(columnData);	
					}
                }
                me.dataWithColumns=dataWithColumns;
                //me.dataArray=dataArray;
                me.columnArray=(columns==undefined?[]:columns);
				
				me.headerSetGrid(me,columns,columnsLength);
               // me.pieceGrid(me,columns,columnsLength,data);
                //displayRowCount.setText(Ext.String.format(displayRowCount.defaultTextMsg, dataArray.length==0?0:1,dataArray.length,dataArray.length));
            }
            displayRowCount.setVisible(true);
        }

        /*第三步：解析数据*/
        if( l.activeItem.name == 'titleExcelDate'&&incr==1){
			var sendType = me.sendType;
			var headerParams = "",
				headerColumnsDate=[],
				isSetMailOrPhone = false,
				isTitleRept = false,
				isSetRept = false,
				isTitleBlank = false;
			var grid = me.down('grid[name=titleExcelDateGrid]');
			
			var headerReptArr = [],
				setReptArr = [];
			var rowIndex = grid.getStore().getCount();
			for(var i = 0; i< rowIndex ; i++){ 
				var record = grid.getStore().getAt(i);
				var headerTitle = record.data["title"];
				var headerSet = record.data["set"];
				if(headerTitle == "" || headerTitle == null) isTitleBlank = true;
				if(headerReptArr.indexOf(headerTitle)== -1){
					if(headerTitle != "" && headerTitle != null) headerReptArr.push(headerTitle);	
				}else{
					isTitleRept = true;
				}
				if(setReptArr.indexOf(headerSet) == -1){
					if(headerSet != "" && headerSet != null) setReptArr.push(headerSet);	
				}else{
					isSetRept = true;
				}
				if(record.data["set"] == "B") isSetMailOrPhone = true;/*设置邮箱或手机标识*/
				if(headerParams == ""){
					headerParams = Ext.JSON.encode(record.data);
				}else{
					headerParams = headerParams + ',' +  Ext.JSON.encode(record.data);
				} 
			}
			if(isTitleBlank){
				Ext.Msg.alert("提示","标题行名称不能为空");
				return;		
			}
			if(isTitleRept){
				Ext.Msg.alert("提示","标题行名称不能重复");
				return;	
			}
			if(isSetRept){
				Ext.Msg.alert("提示","设置项不能重复");
				return;		
			}
			if(!isSetMailOrPhone){
				if(sendType == "SMS"){
					Ext.Msg.alert("提示","请设置手机");
				}else{
					Ext.Msg.alert("提示","请设置邮箱");
				}
				return;	
			}
			jsonData = '{"ComID":"TZ_QF_EXCIMP_COM","PageID":"TZ_QF_EXCIMP_STD","OperateType":"tzSetExcelHeader","comParams":{"sendPcId":"'+ me.sendPicId +'","headerData":['+ headerParams +']}}';//Json数据
			Ext.MessageBox.show({
					msg: '解析数据中，请稍候...',
					progress: true,
					progressText:'解析中...',
					width: 300,
					wait: {
						interval: 50
					}
				});
				
			Ext.tzLoad(jsonData,function(respData){
				me.headerColumnsDate=respData;
				
				me.previewExcelDate(me,me.headerColumnsDate,me.dataWithColumns);
				
				Ext.MessageBox.hide();
				me.down('#card-prev').setHidden(true);
        		me.down('#card-next').setHidden(true);
				me.down('#card-save').setHidden(false);
        		me.down('#card-ensure').setHidden(false);
			});
        }
		
        l.setActiveItem(next);
        me.getViewModel().set('title', l.activeItem.title);
        me.down('#card-prev').setDisabled(next===0);
        me.down('#card-next').setDisabled(next===2);
    },
	
	headerSetGrid: function(me,columns,columnsLength){
		var sendType = me.sendType;
		var headTypeStore;//导入Excel标题行设置store
		if(sendType == "SMS"){
			headTypeStore = new KitchenSink.view.common.store.appTransStore("TZ_SMS_HEAD_TYPE");
		}else{
			headTypeStore = new KitchenSink.view.common.store.appTransStore("TZ_EML_HEAD_TYPE");
		}
		var modelFields = new  Array("title","set");
		var columnsData = [],
			colJson="",i;
		if(columns != undefined){
			var field_num = 0;
			for(var i=0;i<columns.length;i++){
				var titleDesc = columns[i];
				if(titleDesc.toString().trim() == ""){
					field_num = field_num + 1;
					titleDesc = "字段" + field_num;	
				}
				colJson = '"title":"'+ titleDesc +'","set":""';	
				columnsData.push(Ext.JSON.decode("{"+colJson+"}"))		
			}
		}else{
			for(var i=0;i<columnsLength;i++){
				colJson = '"title":"字段'+(i+1)+'","set":""';	
				columnsData.push(Ext.JSON.decode("{"+colJson+"}"))	
			}
		}

		var gridStore = new Ext.data.Store({
                model:new Ext.data.Model({fields: modelFields }),
                data:{root:columnsData},
                autoLoad: true,
                proxy: {
                    type: 'memory',
                    reader: {
                        type: 'json',
                        rootProperty: 'root'
                    }
                }
            }),
			gridColumns = [];
		gridColumns = [{
				xtype:'rownumberer',
				text:'序号',
				width:60
		},{
				text:'标题行名称',
				dataIndex:'title',
				minWidth:150,
				flex:1,
				editor:{
					xtype:'textfield'	
				}
		},{
				text:'设置',
				dataIndex:'set',
				width:120,
				flex:1,
				editor:{
					xtype: 'combo', 
					forceSelection: true,
					valueField: 'TValue',
					displayField: 'TSDesc',
					typeAhead: true,
					mode:"remote",
					store: headTypeStore
				},
				renderer:function(value,metadata,record){
					var index = headTypeStore.find('TValue',value);   
					if(index!=-1){   
						   return headTypeStore.getAt(index).data.TSDesc;   
					}   
					return record.get('set');  
				}
		}];
		var form = me.down('form[name=titleExcelDate]');
        var grid = me.down('grid[name=titleExcelDateGrid]');
        if(grid!=undefined)form.remove(grid);
        grid={
            xtype:'grid',
            name : 'titleExcelDateGrid',
            width:'100%',
            minHeight:200,
            maxHeight:300,
            store:gridStore,
            columns:gridColumns,
			plugins: [
				Ext.create('Ext.grid.plugin.CellEditing',{
					clicksToEdit: 1
				})
			],
        };

        form.insert(0,grid);
	},
	
	previewExcelDate: function(me,columns,dataWithColumns){
		var data = [];
		var columnLen = columns.length;
		var modelFields = new  Array();
		for(var i=0;i<columnLen;i++){
			modelFields.push(columns[i]["storeField"]);	
		}
		
		for(var i = 0;i<dataWithColumns.length;i++){
			var jsonData = ""
			for(var j=0;j<dataWithColumns[i].length;j++){

				var encodeColumnData = Ext.JSON.encode(dataWithColumns[i][j].replace(/</g,'&lt').replace(/>/g,'&gt'));
				if(jsonData==""){
					jsonData = modelFields[j] +':'+encodeColumnData;
				}else{
					jsonData =jsonData+','+modelFields[j]+':'+encodeColumnData;
				}
			}
			jsonData = jsonData + ',' + 'rowNumber:'+ (i);
			data.push(Ext.JSON.decode("{"+jsonData+"}"));
		}

		var store = new Ext.data.Store({
                model:new Ext.data.Model({fields: modelFields }),
                data:{root:data},
                autoLoad: true,
				proxy: {
                    type: 'memory',
                    reader: {
                        type: 'json',
                        rootProperty: 'root'
                    }
                }
		});
		
		var gridStore = new Ext.data.Store({
                model:new Ext.data.Model({fields: modelFields }),
                //data:{root:data},
                autoLoad: true,
				pageSize:100,
        		proxy: new Ext.data.MemoryProxy({ data:data,reader:{type:'json'},enablePaging:true })
            }),
			gridColumns = [];
		gridStore.load({params:{start:0,limit:100}})	
		
		//拼装展示列
        gridColumns.push({
			text:'序号',
			//locked:true,
			width:60,
            xtype:'rownumberer'
        });	
		for(var i = 0;i<columns.length;i++){
			var columnWidth = columns[i]["header"].toString().length*15+30;
			var locked = false;
			//if(i<15) locked = true;
			var gridColumn = new Ext.grid.Column({
				header:'<span style="font-size:14px;color:'+ ((columns[i]["sendType"] != "" && columns[i]["sendType"] != null) ? "#FF0000":"") +'">'+columns[i]["header"].toString().replace(/</g,'&lt').replace(/>/g,'&gt')+'</span>',
				minWidth:columnWidth>100?columnWidth:100,
				flex:1,
				sortable:false,
				//locked:locked,
				//lockable:false,
				menuDisabled: true,
				dataIndex:columns[i]["storeField"],
				renderer:function(value){
					value=(value==undefined?"":value);
					return '<span style="font-size:14px;">' + value + '</span>';
				}
			})
			gridColumns.push(gridColumn);
		}
		gridColumns.push({
			xtype: 'actioncolumn',
			width:60,
			header:'操作',
			//locked:true,
			//lockable:false,
			//enableLocking:true,
			menuDisabled: true,
			sortable: false,
			items:[
				{
					iconCls: 'remove',
					tooltip: '删除',
					handler:'removeCurrentDate'
				}
			]	
		});
		
		var form = me.down('form[name=previewExcelData]');
        var grid = me.down('grid[name=previewExcelDataGrid]');
        if(grid!=undefined)form.remove(grid);
        grid={
            xtype:'grid',
            name : 'previewExcelDataGrid',
            width:'100%',
			frame: true,
			columnLines: true,
			header:false,
            minHeight:200,
            maxHeight:350,
			SaveStore:store,
            store:gridStore,
            columns:gridColumns,
			bbar: {
				xtype: 'pagingtoolbar',
				pageSize: 100,
				store: gridStore,
				displayInfo: true,
				plugins: new Ext.ux.ProgressBarPager()
			}
        };

        form.insert(0,grid);
	},

	removeCurrentDate: function(view, rowIndex, colIndex){
		var me = this;
		Ext.MessageBox.confirm('确认', '您确定要删除所选记录吗?', function(btnId){
            if(btnId == 'yes'){
				var grid = view.findParentByType("grid");

				var store = grid.getStore();
				var selRec = store.getAt(rowIndex);
				var rowNumber = selRec.get("rowNumber");
				store.remove(selRec);
				
				var saveStore = grid.SaveStore;
				var index = saveStore.find("rowNumber",rowNumber);
				saveStore.removeAt(index);
				
				me.savedFlag = false;
            }
        },this);	
	},
	
	saveExcelDate: function(btn){
		var me = this;
		if(!me.savedFlag){
			var win = btn.findParentByType("smsEmlImportExcelWindow");
			var grid = win.down('grid[name=previewExcelDataGrid]');
	
			var dataItems = grid.SaveStore.data.items;
			var dataJson = "";
			for(var i=0;i<dataItems.length;i++){
				if(dataJson == ""){
					dataJson = Ext.JSON.encode(dataItems[i].data);	
				}else{
					dataJson = dataJson + ',' + Ext.JSON.encode(dataItems[i].data);
				}
			}
			/*参数信息项*/
			var ParamItemArr = me.headerColumnsDate;
			var paramItemZwfArr = [];
			for(var i=0;i<ParamItemArr.length;i++){
				var itemNameZwf = "["+ ParamItemArr[i]["header"] +"]";
				paramItemZwfArr.push({"parainfoitem":itemNameZwf});
			}
			
			paramsJson = '{"ComID":"TZ_QF_EXCIMP_COM","PageID":"TZ_QF_EXCIMP_STD","OperateType":"tzSaveExcelData","comParams":{"sendPcId":"'+ me.sendPicId +'","sendType":"'+ me.sendType +'","data":['+ dataJson +']}}';//Json数据
			Ext.tzLoad(paramsJson,function(respData){
				var receverArr = respData.recever;
				var subject = respData.subject;
				var content = respData.content;
				
				if(me.callback!=undefined) me.callback(receverArr,subject,content,paramItemZwfArr);
				if(btn.itemId == "card-save"){
					Ext.tzShowToast('保存成功！','提示','t','#ffffff');	
				}
				me.savedFlag = true;
			});
		}
	},
	
	ensureSaveDate: function(btn){
		var me = this;
		me.saveExcelDate(btn);
		me.close();	
	}
});