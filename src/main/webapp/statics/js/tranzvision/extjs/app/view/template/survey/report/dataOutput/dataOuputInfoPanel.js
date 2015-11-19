Ext.define('KitchenSink.view.template.survey.report.dataOutput.dataOuputInfoPanel', {
    extend: 'Ext.window.Window',
    xtype: 'dataOutputReport',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.AdvancedVType',
        'tranzvision.extension.exporter.Excel'
    ],
    title: '导出报表',
    width: 600,
    height: 400,
    modal:true,
    layout: {
        type: 'fit'
    },
    initComponent : function(){
        var isFinishedStore = new KitchenSink.view.common.store.appTransStore("TZ_DC_WC_STA"),
            fileTypeStore = new KitchenSink.view.common.store.appTransStore("TZ_DC_SJDC_FILETYP")
        Ext.apply(this,{
            items:[{
                xtype:'form',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                border: false,
                bodyPadding: 10,
                bodyStyle: 'overflow-y:auto;overflow-x:hidden',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 110,
                    labelStyle: 'font-weight:bold'
                },
                items:[{
                    xtype:'textfield',
                    name:'onlinedcID',
                    hidden:true,
                    ignoreChangesFlag: true
                },{
                    xtype:'textfield',
                    name:'onlinedcName',
                    fieldLabel:'标题',
                    readOnly:true,
                    fieldStyle:'background:#F4F4F4',
                    ignoreChangesFlag: true
                },{
                    xtype:'textfield',
                    name:'onlinedcState',
                    fieldLabel:'发布状态',
                    readOnly:true,
                    fieldStyle:'background:#F4F4F4',
                    ignoreChangesFlag: true
                },{
                    xtype:'textfield',
                    name:'startDate',
                    fieldLabel:'开始日期',
                    readOnly:true,
                    fieldStyle:'background:#F4F4F4',
                    ignoreChangesFlag: true
                },{
                    xtype:'textfield',
                    name:'endDate',
                    fieldLabel:'结束日期',
                    readOnly:true,
                    fieldStyle:'background:#F4F4F4',
                    ignoreChangesFlag: true
                },{
                    xtype:'combo',
                    name:'fileType',
                    fieldLabel:'文件类型',
                    store:fileTypeStore,
                    value:'A',
                    displayField:'TSDesc',
                    valueField:'TValue',
                    queryMode:'local',
                    editable:false,
                    ignoreChangesFlag:true,
                    renderer:function(v){
                        var x;
                        if((x = fileTypeStore.find('TValue',v,0,false,false,false))>=0){
                            return fileTypeStore.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }
                },{
                    xtype:'combo',
                    name:'status',
                    fieldLabel:'状态',
                    store:isFinishedStore,
                    value:'0',
                    displayField:'TSDesc',
                    valueField:'TValue',
                    queryMode:'local',
                    editable:false,
                    ignoreChangesFlag:true,
                    renderer:function(v){
                        var x;
                        if((x = isFinishedStore.find('TValue',v,0,false,false,false))>=0){
                            return isFinishedStore.getAt(x).data.TSDesc;
                        }else{
                            return v;
                        }
                    }
                }]
            }]
        });
        this.callParent();
    },
    buttons:[
        {text:'导出',iconCls:'excel',handler:function(btn){
            btn.findParentByType("panel").mask('正在导出...');
            var form = btn.findParentByType("panel").down("form").getForm(),
                onlinedcName = form.findField("onlinedcName").getValue()
                onlinedcID = form.findField("onlinedcID").getValue(),
                filetype = form.findField("fileType").getValue(),
                status = form.findField("status").getValue();
            var tzParams = '{"ComID":"TZ_ZXDC_DCBB_COM","PageID":"TZ_ZXDC_DCBB_STD",' +
                '"OperateType":"QG","comParams":{"onlinedcId":"'+onlinedcID+'","fileType":"'+filetype+'","status":"'+status+'"}}';
            Ext.tzLoad(tzParams,function(respData){
                //设置导出excel的一些基本属性
                var exporter = new tranzvision.extension.exporter.Excel();
                exporter.setAuthor("tranzvision");
                exporter.setCharset("utf-8");
                exporter.setFileName(onlinedcName+".xls");
                exporter.setTitle(onlinedcName);
                exporter.setTitleStyle({
                    alignment:{Horizontal:"Center"}
                });
                var config = exporter.getConfig();
                exporter.workbook = Ext.create('tranzvision.extension.exporter.file.excel.Workbook',{
                    title:              config.title,
                    author:             config.author,
                    windowHeight:       config.windowHeight,
                    windowWidth:        config.windowWidth,
                    protectStructure:   config.protectStructure,
                    protectWindows:     config.protectWindows
                });
                exporter.table = exporter.workbook.addWorksheet({
                    name: config.title
                }).addTable();

                exporter.workbook.addStyle(config.defaultStyle);
                exporter.tableHeaderStyleId = exporter.workbook.addStyle({
                    name: 'Heading 1',
                    alignment: {
                        Horizontal: 'Center',
                        Vertical: 'Center'
                    },
                    borders: [{
                        Position: 'Top',
                        LineStyle: 'Continuous',
                        Weight: 1,
                        Color: '#4F81BD'
                    }],
                    font: {
                        FontName: 'Calibri',
                        Family: 'Swiss',
                        Size: 11,
                        Color: '#1F497D'
                    }
                }).getId();
                exporter.groupHeaderStyleId = exporter.workbook.addStyle(config.groupHeaderStyle).getId();
                exporter.groupFooterStyleId = exporter.workbook.addStyle(config.groupFooterStyle).getId();
                exporter.shortDateStyleId = exporter.workbook.addStyle({name:'Date',format:'Short Date'}).getId();
                exporter.cellStyleId = exporter.workbook.addStyle({
                    borders:[{
                        Position: 'Left',
                        LineStyle: 'Continuous',
                        Weight: 1,
                        Color: '#000'
                    },{
                        Position: 'Right',
                        LineStyle: 'Continuous',
                        Weight: 1,
                        Color: '#000'
                    },{
                        Position: 'Top',
                        LineStyle: 'Continuous',
                        Weight: 1,
                        Color: '#000'
                    },{
                        Position: 'Bottom',
                        LineStyle: 'Continuous',
                        Weight: 1,
                        Color: '#000'
                    }],
                    alignment:{
                        WrapText:true,
                        Horizontal:'Center'
                    }
                }).getId();
                //计算总列数
                //只有表格选择题需要额外的列,同一问卷所有实例信息项是一样的，只在第一个中查找
                var colMerge=respData.XXXBH.length+1, /*  总列数,所有信息项+单独实例ID一列  */
                    tableCellsCount=1,
                    speicalAPP,      /*   记录一个特殊的APP编号，在其他的查询中可以查询这个APP编号   */
                    cellsIndex = [], /*  记录所有列出现位置和所占的单元格数量  */
                    cellsRows = []; /*  记录每个实例的记录需要几行  */
                for(var x in respData.APPINS){
                    for(var y = respData.APPINS[x].length-1;y>=0;y--){
                        if(Object.prototype.toString.call(respData.APPINS[x][y]) === "[object Object]"){
                        //只有表格选择题的数据构造为对象的形式
                            var midCellsCount=1;
                            for(var n in respData.APPINS[x][y]){
                                if(respData.APPINS[x][y].hasOwnProperty(n)){
                                    for(var m in respData.APPINS[x][y][n]){
                                        midCellsCount++;
                                    }
                                }
                                break;
                            }
                            midCellsCount = midCellsCount===0?1:midCellsCount;
                            cellsIndex[y] = cellsIndex[y]?cellsIndex[y]:1;//初始化
                            cellsIndex[y] = Math.max(cellsIndex[y],midCellsCount);
                           tableCellsCount=Math.max(tableCellsCount,midCellsCount);
                        }else{
                            cellsIndex[y] = 1;
                        }
                    }
                    speicalAPP=speicalAPP?speicalAPP:x;
                }
                colMerge+= tableCellsCount-1;
                //colMerge
                //exporter.buildHeader();
                //标题
                exporter.addTitle(config, colMerge+1);
                //表头
                var header = exporter.table.addRow({
                        height: 20.25,
                        autoFitHeight: 1,
                        styleId: exporter.tableHeaderStyleId
                    });
                //先添加APPINS列
                header.addCell({value:"APPINS"});
                //再添加各个信息项列
                for(var x=0;x<respData.XXXBH.length;x++){
                    header.addCell({
                        value:respData.XXXBH[x],
                        mergeAcross:cellsIndex[x]-1
                    });
                }
                //表格数据
                var len = respData.APPINS[speicalAPP].length,
                    rowNum=3,cellNum=1,/*  记录行号和列号  */
                    rowCellsIndex = [];/* 记录进行纵向合并单元格的列，在之后的构建行中需要跳过这些列 */
                 for(var x=0;x<colMerge;x++){
                    rowCellsIndex.push(0);
                }
                //开始解析数据并构造Excel表格
                for(var x in respData.APPINS){
                    //确定该属性不是原型链中的属性
                    if(respData.APPINS.hasOwnProperty(x) && x !== 'length'){
                        var tableRowsCount = 1,
                            subRowsCount = 0,
                            DHCCRowCount = 0;
                        //查询当前实例共需多少行
                        for(var y=0;y<len;y++){
                            //表格选择题需要行数
                            if(Object.prototype.toString.call(respData.APPINS[x][y])==="[object Object]"){
                                subRowsCount=0;
                                for(var z in respData.APPINS[x][y]){
                                    if(respData.APPINS[x][y].hasOwnProperty(z)){
                                        subRowsCount++;
                                    }
                                }
                            }
                            //多选选择需要行数
                            if(Object.prototype.toString.call(respData.APPINS[x][y])==="[object Array]"){
                                    DHCCRowCount = Math.max(DHCCRowCount,respData.APPINS[x][y].length);
                            }
                            //表格选择题需要额外一行显示表头
                            tableRowsCount = Math.max(tableRowsCount,subRowsCount+1);
                        }
                        tableRowsCount = Math.max(tableRowsCount,DHCCRowCount);
                        //开始表格构建,共需构建tableRowsCount行
                        for(var n=0;n<tableRowsCount;n++){
                            //添加一行
                            var thisRow = exporter.table.addRow({
                                index:rowNum,
                            });
                            rowNum++;
                            //先添加APPINS列，第一列
                            if(rowCellsIndex[0]===0){
                                thisRow.addCell({
                                    value:x,
                                    mergeDown:tableRowsCount-1,
                                    styleId:exporter.cellStyleId
                                });
                                rowCellsIndex[0]= tableRowsCount-1;
                            }else{
                                rowCellsIndex[0]--;
                            }
                            //根据信息项添加之后所有的信息项列
                            for(var m=0;m<len;m++){
                                var former = 0;
                                for(var f=0;f<m;f++){
                                    former+=cellsIndex[f];
                                }
                                switch (Object.prototype.toString.call(respData.APPINS[x][m])){
                                    case "[object Array]":
                                    //多项
                                    var sub = respData.DHCC[respData.XXXBH[m]][respData.APPINS[x][m][n]-1];
                                    thisRow.addCell({
                                        index:former+2,
                                        value:sub?sub:"",
                                        styleId:exporter.cellStyleId
                                    });  
                                    break;
                                    case "[object String]":
                                    //问答
                                    if(rowCellsIndex[m+1]===0){
                                        thisRow.addCell({
                                            value:respData.APPINS[x][m],
                                            mergeDown:tableRowsCount-1,
                                            styleId:exporter.cellStyleId
                                        });
                                     rowCellsIndex[m+1]= tableRowsCount-1;
                                    }else{
                                        rowCellsIndex[m+1]--;
                                    }
                                    break;
                                    case "[object Object]":
                                    var moreCell;
                                    //表格选择
                                    if(n===0){
                                        moreCell = 1;
                                        //表格选择第一行用来构建表头
                                        //第一列显示子选项（表头的子选项为空）
                                        thisRow.addCell({
                                            index:former+2,
                                            value:"",
                                            styleId:exporter.cellStyleId
                                        });
                                        
                                        if(JSON.stringify(respData.APPINS[x][m])!=='{}'){
                                            for(var l in respData.APPINS[x][m]){
                                                if(respData.APPINS[x][m].hasOwnProperty(l)){
                                                    for(var k in respData.APPINS[x][m][l]){x
                                                        thisRow.addCell({
                                                            index:former+2+moreCell,
                                                            value:k,
                                                            styleId:exporter.cellStyleId
                                                        });
                                                    
                                                        moreCell++;
                                                    }
                                                        if(moreCell<cellsIndex[m]){
                                                            //未达到列数，用空列补足
                                                            for(var a=0;a<cellsIndex[m]-moreCell;a++){
                                                                thisRow.addCell({
                                                                    index:former+2+moreCell+a,
                                                                    value:"",
                                                                    styleId:exporter.cellStyleId
                                                                });
                                                            }
                                                        }
                                                    
                                                }
                                                break;
                                                }
                                        }else{
                                            for(var l=0;l<cellsIndex[m]-1;l++){
                                                thisRow.addCell({
                                                    index:former+2+moreCell,
                                                    value:"",
                                                    styleId:exporter.cellStyleId
                                                });
                                                moreCell++;
                                            }
                                        }
                                    }else{
                                        //子选项
                                        var thisSub;
                                        moreCell = 1;
                                        for(var l in respData.APPINS[x][m]){
                                            thisRow.addCell({
                                                index:former+2,
                                                value:l,
                                                styleId:exporter.cellStyleId
                                            });
                                            thisSub = l;
                                            break;
                                        }
                                        if(JSON.stringify(respData.APPINS[x][m])!=='{}'){
                                            //表格列
                                            var moreCell = 1;
                                            for(var k in respData.APPINS[x][m][thisSub]){
                                                thisRow.addCell({
                                                    index:former+2+moreCell,
                                                    value:respData.APPINS[x][m][thisSub][k],
                                                    styleId:exporter.cellStyleId
                                                });
                                                moreCell++;
                                            }
                                            //补足列
                                            if(moreCell<cellsIndex[m]){
                                                //未达到列数，用空列补足
                                                for(var a=0;a<cellsIndex[m]-moreCell;a++){
                                                    thisRow.addCell({
                                                        index:former+2+moreCell+a,
                                                            alue:"",
                                                        styleId:exporter.cellStyleId
                                                    });
                                                }
                                            }
                                        }else{
                                            //表头列也需要补足，所以index从m+1开始
                                            for(var l=0;l<cellsIndex[m];l++){
                                                thisRow.addCell({
                                                    index:former+1+moreCell,
                                                    value:"",
                                                    styleId:exporter.cellStyleId
                                                });
                                                moreCell++;
                                            }
                                        }
                                        try{
                                            //删除已经使用过得属性
                                            //表格选择题没有数据时删除不影响其他程序
                                            delete respData.APPINS[x][m][thisSub];
                                        }catch(e){
                                            //do nothing
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    
                }
                
                tranzvision.extension.exporter.File.saveAs(exporter.workbook.render(), exporter.getFileName(), exporter.getCharset());
                btn.findParentByType("panel").unmask();
            });
        }},
        {text:'关闭',iconCls:'close',handler:function(btn){
        btn.findParentByType('window').close();
    }}]
});