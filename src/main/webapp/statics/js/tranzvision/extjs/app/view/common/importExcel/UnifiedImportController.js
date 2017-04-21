Ext.define('KitchenSink.view.common.importExcel.UnifiedImportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.unifiedimport', 

    showNext: function (btn) {
        this.doCardNavigation(1);
    },

    showPrevious: function (btn) {
        this.doCardNavigation(-1);
    },

    doCardNavigation: function (incr) {
        var me = this.getView(),
        	l = me.getLayout(),
        	i = l.activeItem.itemId.split('card-')[1],
        	next = parseInt(i, 10) + incr;
        
        var displayRowCount=me.down('#rowCount'),
    		displayMsgTip=me.down('#msgTip'),
    		tbSeparator = me.down('tbseparator');
        
        /*第一步：导入或者粘贴Excel数据*/
        if( l.activeItem.name == 'previewExcelData'&&incr==-1){
        	displayRowCount.setVisible(false);
            displayMsgTip.setVisible(false);
            tbSeparator.setVisible(false);
        }

        /*第二步：解析并预览数据Excel*/
        if( l.activeItem.name == 'importExcel'&&incr==1){
            if(this.parseData(l)==false){
            	return;
            }else{
            	displayRowCount.setVisible(true);
                displayMsgTip.setVisible(true);
                tbSeparator.setVisible(true);
            }
        }

        /*第三步：调整映射关系*/
        if( l.activeItem.name == 'previewExcelData'&&incr==1&&me.enableAdjustMapping===true ){
        	displayRowCount.setVisible(false);
            displayMsgTip.setVisible(false);
            tbSeparator.setVisible(false);
            
        	this.adjustMapping();
        }
        
        /*第四步：保存数据(如果第三步不需要调整映射关系则直接进入第四步)*/
        if( (l.activeItem.name == 'adjustMapping'&&incr==1)||(l.activeItem.name == 'previewExcelData'&&incr==1&&me.enableAdjustMapping===false)){
            this.submit();
            return;
        }
        
        l.setActiveItem(next);
        me.up('window').getViewModel().set('title', l.activeItem.title);
        me.down('#card-prev').setHidden(next===0);
        me.down('#card-next').setHidden(next===3);
    },
    
    //解析Excel数据
    parseData:function(l){
    	var me = this.getView(),
    		_this = this,
    		displayRowCount=me.down('#rowCount');
    		uploadExcel =  l.activeItem.down('fieldset[name=uploadExcel]'),
    		pasteExcelData = l.activeItem.down('fieldset[name=pasteExcelData]');

	    /*预览数据columns 和 data*/
	    var columns,
	        data = [],
	        dataArray = [],
	        dataWithColumns = [],
	        columnsLength;
	
	    if(!uploadExcel.collapsed){
	        //解析上传Excel文件
	        var filename = me.down("#orguploadfile").getValue();
	        var form = me.down('form[name=uploadExcelForm]').getForm();
	        if(filename&&form.isValid()){
	
	            var filePath = 'tmpFileUpLoad';
	            var updateUrl = TzUniversityContextPath + '/UpdServlet?filePath='+filePath;
	
	            form.submit({
	                url: updateUrl,
	                waitMsg: '正在上传Excel...',
	                success: function (form, action) {
	                    var sysFileName = action.result.msg.sysFileName;
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
	                    
	                    var tzParams = '{"ComID":"TZ_IMPORT_EXCEL_COM","PageID":"TZ_IMP_EXCEL_STD","OperateType":"tzAnalyzeExcel","comParams":{"path":'+Ext.JSON.encode(path)+',"sysFileName":'+Ext.JSON.encode(sysFileName)+'}}';

	                    Ext.tzLoad(tzParams,function(responseData){
	                        if(responseData.error){
	                            Ext.Msg.alert("错误",responseData.error);
	                            return false;
	                        }
	                        
	                        dataWithColumns = responseData;
	                        var firstLineTitle_1 = me.down('checkboxfield[name=firstLineTitle_1]').getValue();
	                        for(var i = 0;i<dataWithColumns.length;i++){
	                        	
	                        	//解析数据：服务端返回的数据格式为[{0:"张三",1:"20岁"}]，需要转成[["张三"],["20岁"]]
                            	var dataTmp = [];
                            	Ext.Object.each(dataWithColumns[i],function(key,value){
                            		dataTmp[_this.parseInt(key)] = value;
                            	});
                            	dataWithColumns[i] = dataTmp;
                            	
	                            if(dataArray.length==1000)break;/*超过1000行的数据不展示*/
	                            columnsLength = (columnsLength==undefined?dataWithColumns[i].length:((columnsLength<dataWithColumns[i].length)?dataWithColumns[i].length:columnsLength));
	
	                            if(i==0&&firstLineTitle_1/*首行为标题行*/){
	                                columns = dataWithColumns[i];
	                            }else{
	                                dataArray.push(dataWithColumns[i]);
	
	                                var jsonData = ""
	                                for(var j=0;j<dataWithColumns[i].length;j++){
	                                    if(j==20)break;/*超过20行的数据不展示*/
	
	                                    var encodeColumnData = Ext.JSON.encode(dataWithColumns[i][j].replace(/</g,'&lt').replace(/>/g,'&gt'));
	                                    if(jsonData==""){
	                                        jsonData ='column_'+j+':'+encodeColumnData;
	                                    }else{
	                                        jsonData =jsonData+',column_'+j+':'+encodeColumnData;
	                                    }
	                                }
	                                data.push(Ext.JSON.decode("{"+jsonData+"}"));
	                            }
	                        }
	                        
	                        me.columnsLength = columnsLength;
	                        me.dataArray=dataArray;
	                        me.columnArray=(columns==undefined?[]:columns);
	                        _this.pieceGrid(columns,columnsLength,data);
	                        displayRowCount.setText(Ext.String.format(displayRowCount.defaultTextMsg, firstLineTitle_1&&dataWithColumns.length==1?0:1,dataArray.length,firstLineTitle_1?dataWithColumns.length-1:dataWithColumns.length));
	
	                        Ext.MessageBox.hide();
	                    });
	
	                },
	                failure: function (form, action) {
	                    Ext.MessageBox.alert("错误", action.result.msg);
	                    return false;
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
	            return false;
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
	            return false;
	        };
	        var firstLineTitle_2 = me.down('checkboxfield[name=firstLineTitle_2]').getValue(),
	            columnsData = excelText.split("\n");//获取每行数据
	
	        var displayCount,allCount;
	        for(var i = 0;i<columnsData.length;i++){
	            if(columnsData[i].replace(/(^\s*)|(\s*$)/g, "")=="") continue;
	
	            //var columnData = columnsData[i].split(/\s+/);//以空格拆分每一列
	            var columnData = columnsData[i].split('\t');//以制表符拆分每一列
	            columnsLength = (columnsLength==undefined?columnData.length:((columnsLength<columnData.length)?columnData.length:columnsLength));
	
	            dataWithColumns.push(columnData);
	            if(i==0&&firstLineTitle_2/*首行为标题行*/){
	                columns = columnData;
	            }else{
	                dataArray.push(columnData);
	
	                if(dataArray.length<=1000){
	                    var jsonData = ""
	                    for(var j=0;j<columnData.length;j++){
	                        var encodeColumnData = Ext.JSON.encode(columnData[j].replace(/</g,'&lt').replace(/>/g,'&gt'));
	                        if(jsonData==""){
	                            jsonData ='column_'+j+':'+encodeColumnData;
	                        }else{
	                            jsonData =jsonData+',column_'+j+':'+encodeColumnData;
	                        }
	                    }
	                    data.push(Ext.JSON.decode("{"+jsonData+"}"));
	                }
	            }
	        }
	        
	        me.dataArray=dataArray;
	        me.columnsLength = columnsLength;
	        me.columnArray=(columns==undefined?[]:columns);
	        _this.pieceGrid(columns,columnsLength,data);
	        displayRowCount.setText(Ext.String.format(displayRowCount.defaultTextMsg, dataArray.length==0?0:1,dataArray.length,dataArray.length));
	    }
	    displayRowCount.setVisible(true);
	    return true;
    },
    
    parseInt:function(key){
 	   var underlineIndex = key.indexOf("_");
 	   if(underlineIndex==-1){
 			return key;
 	   }else{
 			return key.substring(0,underlineIndex)
 	   }
     },
     
    pieceGrid:function(columns,columnsLength,data){
    	var me = this.getView();
        var modelFields = new  Array();
        for(var i = 0;i<columnsLength;i++){
            modelFields.push('column_'+i);
        };

        var gridStore = new Ext.data.Store({
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
            }),
            gridColumns = [];

        //拼装展示列
        gridColumns.push({
            xtype:'rownumberer'
        });
        if(columns!=undefined){
            for(var i = 0;i<columns.length;i++){
                var columnWidth = columns[i].toString().length*15+30;

                var gridColumn = new Ext.grid.Column({
                    header:'<span style="font-size:12px">'+columns[i].toString().replace(/</g,'&lt').replace(/>/g,'&gt')+'</span>',
                    minWidth:columnWidth>100?columnWidth:100,
                    flex:1,
                    sortable:false,
                    menuDisabled: true,
                    dataIndex:'column_'+i,
                    renderer:function(value){
                        value=(value==undefined?"":value);
                        return '<span style="font-size:12px;">' + value + '</span>';
                    }
                })
                gridColumns.push(gridColumn);
            }
        }else{
            for(var i = 0;i<columnsLength;i++){
                var gridColumn = new Ext.grid.Column({
                    header:'<span style="font-size:12px">'+'第'+(i+1)+'列'+'</span>',
                    minWidth:100,
                    flex:1,
                    sortable:false,
                    menuDisabled: true,
                    dataIndex:'column_'+i,
                    renderer:function(value){
                        value=(value==undefined?"":value);
                        return '<span style="font-size:12px;">' + value + '</span>';
                    }
                })
                gridColumns.push(gridColumn);
            }
        };

        var form = me.down('form[name=previewExcelData]');
        var grid = me.down('grid[name=previewExcelDataGrid]');
        if(grid!=undefined)form.remove(grid);
        grid={
            xtype:'grid',
            name : 'previewExcelDataGrid',
            width:'100%',
            minHeight:150,
            maxHeight:250,
            store:gridStore,
            columns:gridColumns
        };

        form.insert(0,grid);
    },
    
    //调整映射关系
    adjustMapping:function(){
    	var me  = this.getView(),
    		mappingGrid = me.down('grid[name=adjustMapping]'),
    		mappingStore = mappingGrid.getStore();
    	
    	var mappingData = [];
    	if(me.columnArray.length>0){
    		Ext.each(me.columnArray,function(column){
    			mappingData.push({
    				columnTitle:column,
    				field:''
    			});
    		})
    	}else{
    		for(var i = 1;i<=me.columnsLength;i++){
    			mappingData.push({
    				columnTitle:'第'+i+'列',
    				field:''
    			});
    		}
    	}
    	
    	mappingStore.loadData(mappingData);

    },
    
    //提交数据
    submit:function(){
    	var _this = this,
    		me = this.getView(),
    		dataGrid = me.down('grid[name=previewExcelDataGrid]'),
    		mappingRange = me.down('grid[name=adjustMapping]').getStore().getRange();

    	//手动调整映射关系是否重复
    	var targetFields = {},
    		targetFieldRepeat = false;    	
    	Ext.each(mappingRange,function(record,index){
    		if(record.get('field')!=undefined&&record.get('field')!=""){
    			if(targetFields[record.get('field')]==undefined){
        			targetFields[record.get('field')] = record.get('columnTitle');
        		}else{
        			targetFieldRepeat = true;
        		}
    		}    		
    	})		
    	if(targetFieldRepeat){
    		Ext.MessageBox.alert("提示", "目标业务数据字段重复，请重新选择。");
    		return
    	}

        //确定最后的模板列标题与字段的映射关系
    	var columnFieldMap = {};
    	var savedFields = [];
    	mappingRange = me.enableAdjustMapping?mappingRange:me.tplFieldsStore.getRange();
    	
        if(me.columnArray.length==0){
        	for(var i = 1;i<=me.columnsLength;i++){
        		me.columnArray.push('第'+i+'列');
    		}
        }
        
    	Ext.each(me.columnArray,function(column,index){
    		Ext.each(mappingRange,function(record){
    			if(_this.trim(column)!=""&&_this.trim(column)==record.get("columnTitle")&&record.get("field")!=""){
    				columnFieldMap[index] = record.get("field");
    				savedFields.push(record.get("field"));
    			}
        	});
    	});
    	
        //校验和提交的数据
    	var ultimateData,
    		savedData = [];
        Ext.each(me.dataArray,function(dataItem,index){
        	var savedDataItem = {};
        	Ext.Object.each(columnFieldMap, function(columnIndex, field) {
        		savedDataItem[field] = dataItem[columnIndex]
        	});
        	
        	savedData.push(savedDataItem);
    	});
       
        ultimateData = {
    			tplId:me.tplId,
    			enableAdjustMapping:me.enableAdjustMapping,
    			fields:savedFields,
    			data:savedData
    	}
        
        var tzValidationParams = Ext.encode({
        	ComID:"TZ_UNIFIED_IMP_COM",
        	PageID:"TZ_UNIFIED_IMP_STD",
        	OperateType:"tzValidate",
        	comParams:ultimateData
        });
        
        var tzSavingParams = Ext.encode({
        	ComID:"TZ_UNIFIED_IMP_COM",
        	PageID:"TZ_UNIFIED_IMP_STD",
        	OperateType:"tzSave",
        	comParams:ultimateData
        });

        //保存数据
        var tzSubmit = function(){
        	Ext.tzSubmit(tzSavingParams,function(){
    			me.up('window').close();
    		},"",true,this);
        }
        
        //校验数据
        Ext.tzLoad(tzValidationParams,function(response){
        	//校验通过
        	if(response!=undefined&&response.result==true){
        		//如果有校验提示信息，则弹出提示，让用户自行选择是否保存
        		if(response.resultMsg!=undefined&&_this.trim(response.resultMsg)!=""){
        			Ext.Msg.confirm("确认",response.resultMsg+"<br/>是否继续导入？",function(btn){
        				if(btn=="yes"){
        					tzSubmit();
        				}
        			});
        		}else{
        			tzSubmit();
        		}
        	}else{
        		//校验不通过则弹出提示，不允许进行保存
        		if(response!=undefined&&response.resultMsg!=undefined&&_this.trim(response.resultMsg)!=""){
        			Ext.Msg.show({
        				title:"提示",
        				icon:Ext.MessageBox.ERROR,
        				msg:"数据校验未通过！<br/>"+response.resultMsg
        			});
        			
        		}else{
        			Ext.Msg.show({
        				title:"提示",
        				icon:Ext.MessageBox.ERROR,
        				msg:"数据校验未通过，请检查导入数据，如果修改数据重试之后仍不成功请联系管理员。"
        			});
        		}
        	}
        	
		});
		
    },
    
    trim:function(str){ 
 　　     	return str.replace(/(^\s*)|(\s*$)/g, "");
 　　 }
});
