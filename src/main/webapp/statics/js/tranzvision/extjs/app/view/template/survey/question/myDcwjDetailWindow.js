Ext.define('KitchenSink.view.template.survey.question.myDcwjDetailWindow', {
    extend: 'Ext.panel.Panel',
    xtype: 'myDcwjDetailWindow',
    reference:'myDcwjDetailWindow',
    controller:'wjdcController',
    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'KitchenSink.view.template.survey.question.wjdcController',
        'KitchenSink.view.template.survey.question.wjdcDetailStore'
    ],
    title: '调查详情',
    bodyStyle: 'overflow-y:auto;overflow-x:hidden',
    items: [
        {
            xtype: 'form',
            reference: 'dcxqForm',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
          //  width:750,
            border: false,
          //  bodyPadding: 10,
            style:"margin:8px",
            bodyStyle: 'overflow-y:auto;overflow-x:hidden',
            fieldDefaults: {
                msgTarget: 'side',
                labelWidth: 110,
                labelStyle: 'font-weight:bold'
            },
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: '问卷开始日期',
                    name: 'beginDate',
                    readOnly:true
                }, {
                    xtype: 'textfield',
                    name: 'endDate',
                    fieldLabel:'问卷结束日期',
                    readOnly:true
                },{
                    xtype:'textfield',
                    name:'joinPeople',
                    fieldLabel:'参加人次',
                    readOnly:true
                },{
                    xtype:'textfield',
                    name:'comPeople',
                    fieldLabel:'完成人次',
                    readOnly:true
                },{
                    xtype:'textfield',
                    name:'totalIP',
                    fieldLabel:'独立IP数',
                    readOnly:true
                },{
                    xtype:'textfield',
                    name:'percent',
                   fieldLabel:'完成率',
                    readOnly:true
                }
            ]
        },
        {
            xtype: 'grid',
            title: '问卷实例详情',
          //  width:750,
            height:330,
            frame: true,
            columnLines: true,
            style: "margin:10px",
            //multiSelect: true,
            viewConfig: {
                enableTextSelection: true
            },
            store: {
                type: 'wjdcDetailStore'
            },
            columns: [
                {text:'序号',
                 dataIndex:'order',
                 width:50,
                 hidden:true
                }, {
                    text: '参与调查人IP',
                    dataIndex: 'IPAddress',
                    width: 170
                },
                {
                    text: '完成状态',
                    dataIndex: 'state',
                    minWidth: 100,
                    renderer:function(v){
                        if(v==0){
                            return "已完成";
                        }else{
                            return "未完成";
                        }
                    }
                },
                {
                    text: '开始时间',
                    dataIndex: 'ksSj',
                    minWidth: 170
                },
                {
                    text:'结束时间',
                    dataIndex:'jsSj',
                    minWidth:170
                }, {
                    menuDisabled: true,
                    sortable: false,
                    text: '详细信息',
                    align: 'center',
                    xtype: 'actioncolumn',
                    flex: 1,
                    items:[
                        {iconCls:'view',tooltip:'详细信息',handler:'viewDetail'}
                    ]
                }
            ],
            bbar: {
                xtype: 'pagingtoolbar',
                pageSize: 10,
                listeners:{
                    afterrender: function(pbar){
                        var grid = pbar.findParentByType("grid");
                        pbar.setStore(grid.store);
                    }
                },
                plugins: new Ext.ux.ProgressBarPager()
            }
        }],
    buttons: [
        {
            text: '关闭',
            iconCls: "close",
            handler:'onDetailFormClose'
        }
    ]
});











































//
//import TZ_GD_KJGL_PKG:TZ_GD_COMM_CLS;
//
///************************************************
// ***开发人：WY
// ***开发时间：2015-09-29
// ***功能说明：在线调查-频数报表
// ************************************************/
//class TZ_GD_ZXDC_PSBBWIN_CLS
///*获取调查基本信息*/
//method tzQuery(&comParams As string, &errorCode As number out, &errorDesc As string out) Returns string;
//
///*chart或grid查询入口*/
//method tzQueryList(&comParams As string, &errorCode As number out, &strErrorDesc As string out) Returns string;
///*其它操作数据的方法*/
//method tzOther(&OperateType As string, &comParams As string, &errorCode As number out, &strErrorDesc As string out) Returns string;
//
//private
//
///*获取频数报表grid信息*/
//method tzGetGridList(&comParams As string, &errorCode As number out, &strErrorDesc As string out) Returns string;
//
///*获取频数报表chart信息*/
//method tzGetChartStore(&comParams As string, &errorCode As number out, &strErrorDesc As string out) Returns string;
//
//
//end-class;
//
///************************************************
// ***开发人：WY
// ***开发时间：2015-09-27
// ***功能说明：在线调查-获取调查基本信息
// ************************************************/
//method tzQuery
///+ &comParams as String, +/
///+ &errorCode as Number out, +/
///+ &errorDesc as String out +/
///+ Returns String +/
//
//rem 返回报文;
//Local string &strRtn;
//
//Local string &strOnlinedcId; /*在线调查编号*/
//Local string &strOnlinedcName; /*在线调查名称*/
//Local string &strOnlinedcState; /*在线调查发布状态*/
//
//rem 解析json类;
//Local JavaObject &jsonUtil = CreateJavaObject("com.tranzvision.util.PaseJsonUtil");
//rem 将字符串转换成json对象;
//Local JavaObject &objJson = &jsonUtil.getJson(&comParams);
//
//&strOnlinedcId = &objJson.getString("onlinedcId");
//
//If All(&strOnlinedcId) Then
//SQLExec("SELECT TZ_DC_WJBT,DECODE(TZ_DC_WJ_FB,'0','未发布','1','发布',' ') TZ_DC_WJ_FB FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID=:1", &strOnlinedcId, &strOnlinedcName, &strOnlinedcState);
//
//&strRtn = GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_FORM_HTML, &strOnlinedcId, &strOnlinedcName, &strOnlinedcState);
//
//&strRtn = "{""formData"":" | &strRtn | "}";
//Else
//&errorCode = 1;
//&errorDesc = "参数不正确！";
//
//&strRtn = GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_FORM_HTML, "", "", "");
//End-If;
//
//Return &strRtn;
//
//end-method;
//
//
///************************************************
// ***开发人：王耀
// ***开发时间：2015-07-15
// ***功能说明：进度管理查询入口
// ************************************************/
//method tzOther
///+ &OperateType as String, +/
///+ &comParams as String, +/
///+ &errorCode as Number out, +/
///+ &strErrorDesc as String out +/
///+ Returns String +/
//Local string &strRet = "{}";
//Local string &strResultConten = "";
//Evaluate &OperateType
//When = "Chart"
//    &strResultConten = %This.tzGetChartStore(&comParams, &errorCode, &strErrorDesc);
//Break;
//
//
//End-Evaluate;
//
//Return &strResultConten;
//
//end-method;
//
//
///************************************************
// ***开发人：王耀
// ***开发时间：2015-07-15
// ***功能说明：进度管理查询入口
// ************************************************/
//method tzQueryList
///+ &comParams as String, +/
///+ &errorCode as Number out, +/
///+ &strErrorDesc as String out +/
///+ Returns String +/
//Local string &strRet;
//
//rem 解析json类;
//Local JavaObject &jsonUtil = CreateJavaObject("com.tranzvision.util.PaseJsonUtil");
//rem 将字符串转换成json;
//Local JavaObject &CLASSJson = &jsonUtil.getJson(&comParams);
//rem 获取到type值;
//Local string &type = &CLASSJson.getString("type");
//
//Evaluate &type
//When = "grid"
//    &strRet = %This.tzGetGridList(&comParams, &errorCode, &strErrorDesc);
//When = "chart"
//    &strRet = %This.tzGetChartStore(&comParams, &errorCode, &strErrorDesc);
//End-Evaluate;
//Return &strRet;
//end-method;
//
//
//
//
///************************************************
// ***开发人：WY
// ***开发时间：2015-09-07
// ***功能说明：获取频数报表grid信息
// ************************************************/
//method tzGetGridList
///+ &comParams as String, +/
///+ &errorCode as Number out, +/
///+ &strErrorDesc as String out +/
///+ Returns String +/
//rem 返回值;
//Local string &strRet = "{}";
//
//rem 解析json类;
//Local JavaObject &jsonUtil = CreateJavaObject("com.tranzvision.util.PaseJsonUtil");
//try
//Local string &strOnlineDcId; /*在线调查ID*/
//Local string &strQuestionListIds; /*问题ids*/
//
//Local array of string &arrTableQuestionId; /*表格问题id*/
//Local string &strDcIns; /*问卷实例*/
//Local array of string &arrDcIns = CreateArrayRept("", 0); /*问卷实例s*/
//Local number &numTotal; /*记录数*/
//Local string &strAnswer; /*答案*/
//Local string &strAnswerTemp = "";
//rem 父问题;
//Local string &strQuestionID, &strQuestionDesc, &strQuestionLX, &strComLMC; /*问题编号,问题类型,空间描述*/
//rem 子问题;
//Local string &strChildQuestion, &strChildQuestionDesc;
//rem 返回的html;
//Local string &strAnswernList;
//
//Local Record &rec_TZ_DCWJ_XXXPZ_T = CreateRecord(Record.TZ_DCWJ_XXXPZ_T); /*信息项配置Record*/
//Local Record &rec_TZ_DCWJ_BGZWT_T = CreateRecord(Record.TZ_DCWJ_BGZWT_T); /*表格子问题Record*/
//Local JavaObject &CLASSJson = &jsonUtil.getJson(&comParams);
//&strOnlineDcId = &CLASSJson.getString("onlinedcId");
//&strQuestionListIds = &CLASSJson.getString("wtId");
//
//Local SQL &AppSql = CreateSQL("SELECT TZ_APP_INS_ID FROM PS_TZ_DC_INS_T WHERE TZ_DC_WJ_ID=:1 AND TZ_APP_SUB_STA='S'", &strOnlineDcId);
//While &AppSql.Fetch(&strDcIns)
//&arrDcIns.Push(&strDcIns);
//End-While;
//If &arrDcIns.Len = 0 Then
//&strRet = "{""total"":""0"",""root"":""[]""}";
//Return &strRet;
//End-If;
//
//If All(&strOnlineDcId, &strQuestionListIds) Then
//
//rem 提交问卷的实际考生总数;
//Local number &totalAppIns;
//SQLExec("SELECT count(*) FROM PS_TZ_DC_INS_T WHERE TZ_DC_WJ_ID=:1 AND TZ_APP_SUB_STA='S'",&strOnlineDcId,&totalAppIns);
//rem 父问题ID;
//Local string  &questionListID= &strQuestionListIds[1];
//Local string &percent;
//If &arrQuestionId.Len = 1 Then /*长度为一的话，则为非表格问题，无子问题*/
//rem 父问题答案选项;
//Local string &TZ_XXXKXZ_MC,&TZ_XXXKXZ_MS;
//Local SQL &ZWTanswerXX=CreateSQL("select TZ_XXXKXZ_MC,TZ_XXXKXZ_MS from ps_TZ_DCWJ_XXKXZ_T WHERE TZ_DC_WJ_ID=:1 and TZ_XXX_BH=:2 order by TZ_ORDER ",&strOnlineDcId,&questionListID);
//
//While &ZWTanswerXX.Fetch( &TZ_XXXKXZ_MC,&TZ_XXXKXZ_MS)
//rem 选中当前答案的考生数;
//Local number &answerAppIns;
//SQLExec("SELECT count(*) FROM ps_TZ_DC_DHCC_T A,PS_TZ_DC_INS_T B  WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_DC_WJ_ID=:1 AND B.TZ_APP_SUB_STA='S'AND A.TZ_XXX_BH=:2 AND A.TZ_IS_CHECKED='Y'",&strOnlineDcId,&TZ_XXXKXZ_MC,&answerAppIns);
//&percent=&answerAppIns|"/"|&totalAppIns;
//If None(&strAnswernList) Then
//&strAnswernList = GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_ANS_GRID_HTML, &strOnlineDcId, &questionListID, &strQuestionDesc, " ", " ", &strAnswer, &percent,&percent );
//Else
//&strAnswernList = &strAnswernList | "," | GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_ANS_GRID_HTML, &strOnlineDcId, &questionListID, &strQuestionDesc, " ", " ", &strAnswer, &percent, &percent);
//End-If;
//
//End-while;
///*长度为2的话，即有子问题，在选择题表中，根据信息项编号（问题ID），查找所有的选项*/
//ELSE
//Local string &childQuestionID,&childQuestionDesc;
//&childQuestionID=&strQuestionListIds[2];
//SQLExec("SELECT TZ_QU_NAME FROM PS_TZ_DCWJ_BGZWT_T WHERE TZ_DC_WJ_ID=:1 AND TZ_XXX_BH=:2 AND TZ_QU_CODE=:3", &strOnlineDcId,&questionListID,&childQuestionID,&childQuestionDesc);
//Local number &childAnswerAppIns;
//SQLExec("SELECT count(*) FROM ps_TZ_DC_DHCC_T A,PS_TZ_DC_INS_T B  WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_DC_WJ_ID=:1 AND B.TZ_APP_SUB_STA='S'AND A.TZ_XXX_BH=:2 AND A.TZ_IS_CHECKED='Y'",&strOnlineDcId,&childQuestionID,&childAnswerAppIns);
//&percent=&childAnswerAppIns|"/"|&totalAppIns;
//If None(&strAnswernList) Then
//&strAnswernList = GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_ANS_GRID_HTML, &strOnlineDcId, &strQuestionListIds[1], &strQuestionDesc, &childQuestionID, &strChildQuestionDesc, &strAnswer, &dasdasdasd, "答案fraction");
//Else
//&strAnswernList = &strAnswernList | "," | GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_ANS_GRID_HTML, &strOnlineDcId, &strQuestionListIds[1], &strQuestionDesc, "子问题ID", &strChildQuestionDesc, &strAnswer, &dasdasdasd, "答案fraction");
//End-If;
//end-while;
//
//End-if;
//
//
//
//End-For;
//
//
//
//
//rem   Local string &strRowJson = RTrim(&strRowJson, ",");
//
//
//&strRet = GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_GRID_JSON_HTNL, &numTotal, &strAnswernList);
//Else
//&strRet = "{}";
//&errorCode = 1;
//&strErrorDesc = "参数不正确！";
//End-If;
//catch Exception &e
//&errorCode = 1;
//&strErrorDesc = &e.ToString();
//end-try;
//Return &strRet;
//end-method;
//
//
///************************************************
// ***开发人：WY
// ***开发时间：2015-09-07
// ***功能说明：获取频数报表chart信息
// ************************************************/
//method tzGetChartStore
///+ &comParams as String, +/
///+ &errorCode as Number out, +/
///+ &strErrorDesc as String out +/
///+ Returns String +/
//rem 返回值;
//Local string &strRet = "{}";
//
//rem 解析json类;
//Local JavaObject &jsonUtil = CreateJavaObject("com.tranzvision.util.PaseJsonUtil");
//try
//Local string &strOnlineDcId; /*在线调查ID*/
//Local string &strQuestionListIds; /*问题ids*/
//
//Local array of string &arrTableQuestionId; /*表格问题id*/
//Local string &strDcIns; /*问卷实例*/
//Local array of string &arrDcIns = CreateArrayRept("", 0); /*问卷实例s*/
//Local number &numTotal; /*记录数*/
//Local string &strAnswer; /*答案*/
//Local string &strAnswerTemp = "";
//rem 父问题;
//Local string &strQuestionID, &strQuestionDesc, &strQuestionLX, &strComLMC; /*问题编号,问题类型,空间描述*/
//rem 子问题;
//Local string &strChildQuestion, &strChildQuestionDesc;
//rem 返回的html;
//Local string &strAnswernList;
//
//Local Record &rec_TZ_DCWJ_XXXPZ_T = CreateRecord(Record.TZ_DCWJ_XXXPZ_T); /*信息项配置Record*/
//Local Record &rec_TZ_DCWJ_BGZWT_T = CreateRecord(Record.TZ_DCWJ_BGZWT_T); /*表格子问题Record*/
//Local JavaObject &CLASSJson = &jsonUtil.getJson(&comParams);
//&strOnlineDcId = &CLASSJson.getString("onlinedcId");
//&strQuestionListIds = &CLASSJson.getString("wtId");
//
//Local SQL &AppSql = CreateSQL("SELECT TZ_APP_INS_ID FROM PS_TZ_DC_INS_T WHERE TZ_DC_WJ_ID=:1 AND TZ_APP_SUB_STA='S'", &strOnlineDcId);
//While &AppSql.Fetch(&strDcIns)
//&arrDcIns.Push(&strDcIns);
//End-While;
//If &arrDcIns.Len = 0 Then
//&strRet = "{""total"":""0"",""root"":""[]""}";
//Return &strRet;
//End-If;
//
//If All(&strOnlineDcId, &strQuestionListIds) Then
//Local array of string &arrQuestionId = CreateArrayRept("", 0);
//&arrQuestionId = Split(&strQuestionListIds, ","); /*将ids分解为id*/
//&strQuestionID = &arrQuestionId [1]; /*父问题取第一个字符*/
//SQLExec("SELECT TZ_XXX_MC FROM PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID=:1 and TZ_XXX_BH=:2", &strOnlineDcId, &strQuestionID, &strQuestionDesc);
//Local number &i;
//
//For &i = 1 To &arrDcIns.Len
//
//If &arrQuestionId.Len = 1 Then /*长度为一的话，则为非表格问题，无子问题*/
//
//
//SQLExec("select TZ_XXX_CCLX from PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID=:1 AND TZ_XXX_BH=:2", &strOnlineDcId, &strQuestionID, &strQuestionLX);
///*短存储值类型*/
//If &strQuestionLX = "S" Then
//SQLExec("SELECT TZ_APP_S_TEXT FROM PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID=:1 AND TZ_XXX_BH=:2", &arrDcIns [&i], &strQuestionID, &strAnswer);
//End-If;
///*长存储值类型*/
//If &strQuestionLX = "L" Then
//SQLExec("SELECT TZ_APP_L_TEXT FROM PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID=:1 AND TZ_XXX_BH=:2", &arrDcIns [&i], &strQuestionID, &strAnswer);
//End-If;
///*非表格选择题类型*/
//If &strQuestionLX = "D" Then
//Local SQL &XZTsql = CreateSQL("SELECT TZ_APP_S_TEXT FROM PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID=:1 AND TZ_XXX_BH=:2", &arrDcIns [&i], &strQuestionID);
//While &XZTsql.Fetch(&strAnswerTemp)
//&strAnswer = &strAnswer | "," | &strAnswerTemp;
//End-While;
//&strAnswer = LTrim(&strAnswer, ",");
//End-If;
//Else
//&strChildQuestion = &arrQuestionId [2];
//rem Error &strOnlineDcId | "," | &strChildQuestion | "," | &strQuestionID;
//SQLExec("SELECT TZ_XXXZWT_DMS FROM PS_TZ_DCWJ_BGZWT_T WHERE TZ_DC_WJ_ID=:1 AND TZ_XXX_BH=:2 and TZ_XXXKXZ_MC=:3", &strOnlineDcId, &strQuestionID, &strChildQuestion, &strChildQuestionDesc);
//SQLExec("SELECT TZ_COM_LMC FROM  PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID=:1 AND TZ_XXX_BH=:2", &strOnlineDcId, &strQuestionID, &strComLMC);
//
//Evaluate &strComLMC
//When "TableRadios"
//SQLExec("SELECT TZ_APP_S_TEXT FROM PS_TZ_DCDJ_BGT_T WHERE TZ_APP_INS_ID=:1 AND TZ_XXX_BH=:2 AND TZ_XXXKXZ_WTMC=:3", &arrDcIns [&i], &strQuestionID, &strChildQuestion, &strAnswer);
//Break;
//When "TableCheckBoxes"
//Local SQL &TableDXTsql = CreateSQL("SELECT TZ_APP_S_TEXT FROM PS_TZ_DCDJ_BGT_T WHERE TZ_APP_INS_ID=:1 AND TZ_XXX_BH=:2 AND TZ_XXXKXZ_WTMC=:3", &arrDcIns [&i], &strQuestionID, &strChildQuestion);
//
//While &TableDXTsql.Fetch(&strAnswerTemp)
//&strAnswer = &strAnswer | "," | &strAnswerTemp;
//End-While;
//&strAnswer = LTrim(&strAnswer, ",");
//
//Break;
//When-Other
//
//End-Evaluate;
//
//End-If;
//
//rem  &strRowJson = &strRowJson | "{""id"":""" | &i | """,""问卷编号"":""" | &arrDcIns [&i] | """," | &strQueAnsJson | """样本数量"":""1""" | "},";
//
///*将:问卷ID,问题ID,问题描述,实例ID,子问题描述,答案描述*/
//rem Error &strOnlineDcId | &strQuestionID | &strQuestionDesc | &arrDcIns [&i] | &strChildQuestionDesc | &strAnswer;
///*  If None(&strAnswernList) Then
// &strAnswernList = GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_ANSWER_HTML, &strOnlineDcId, &strQuestionID, &strQuestionDesc, &arrDcIns [&i], &strChildQuestionDesc, &strAnswer);
// Else
// &strAnswernList = &strAnswernList | "," | GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_ANSWER_HTML, &strOnlineDcId, &strQuestionID, &strQuestionDesc, &arrDcIns [&i], &strChildQuestionDesc, &strAnswer);
// End-If;*/
//
//If None(&strAnswernList) Then
//&strAnswernList = GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_ANS_CHART_HTML, "1", 50);
//Else
//&strAnswernList = &strAnswernList | "," | GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_ANS_CHART_HTML, "2", 50);
//End-If;
//
//End-For;
//
//
//
//
//rem   Local string &strRowJson = RTrim(&strRowJson, ",");
//
//&strAnswernList = GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_ANS_CHART_HTML, "2", "12");
//&strAnswernList = &strAnswernList | "," | GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_ANS_CHART_HTML, "3", "25");
//&strAnswernList = &strAnswernList | "," | GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_ANS_CHART_HTML, "5", "30");
//&strRet = GetHTMLText(HTML.TZ_GD_ZXDC_PSBB_GRID_JSON_HTNL, &numTotal, &strAnswernList);
//Else
//&strRet = "{}";
//&errorCode = 1;
//&strErrorDesc = "参数不正确！";
//End-If;
//catch Exception &e
//&errorCode = 1;
//&strErrorDesc = &e.ToString();
//end-try;
//Return &strRet;
//end-method;
//
