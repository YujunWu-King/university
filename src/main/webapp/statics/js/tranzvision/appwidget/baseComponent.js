SurveyBuild.define("baseComponent",{
	instanceId: "",//控件实例ID
	itemId:"",//信息项ID
	itemName: "",//信息项名称
    title:"",//显示文字（标题）
    emptyText:"",//空值输入框中的提示信息
    orderby: "",//排序字段
    suffix:"",//后缀
	suffixUrl:"",//后缀链接
    defaultval:"",//默认值
    classname:"",//类名称、框架自动赋值
    isRequire:"", //是否必填
	isFirstLetterUpper:"", //是否首字母必填
    preg:"",//固定格式校验
    value:"",//值
    onShowMessage:"",//初始化时提示信息
	onFoucsMessage:"",//获得焦点时提示信息
    rules:{},    //校验规则列表
    boxSize:"3", //控件大小1、2、3
    boxWidth:"",//控件具体宽度
    isDoubleLine:"N",
    isSingleLine:"N",
    _CommonField:"Y",
    wzsm:"",
    "syncOrder": "",
    "syncSep": "",
	linkItems:"",	//关联项
    _init : function(instanceId){},             //控件报文初始化
    _getHtml : function(data){return "";},      //定义控件
    _edit : function(data){return "";},         //控件属性编辑
    _validatorAttr: function(data){return true}, //控件属性验证
    _getDefaultVal: function(data,paramChild){                     //获取默认值
        var isSLine = data.isSingleLine;
        if(isSLine && isSLine == "Y"){
            if(paramChild == "P1" &&  data.defaultval && data.defaultval.indexOf("%BIND") > 0 && "undefined" != typeof UserInfoSet){
                var defExp = data.defaultval.substring(1,data.defaultval.length - 1);
                var field = defExp.split(":")[1];
                if(UserInfoSet.hasOwnProperty(field)){
                    data.children[0]["value"] = UserInfoSet[field];
                }
            }
            if(paramChild == "P2" && data.defaultval && data.defaultval.indexOf("%BIND") > 0 && "undefined" != typeof UserInfoSet){
                var defExp = data.defaultval.substring(1,data.defaultval.length - 1);
                var field = defExp.split(":")[1];
                if(UserInfoSet.hasOwnProperty(field)){
                    data.children[1]["value"] = UserInfoSet[field];
                }
            }
        }else if(data.defaultval && data.defaultval.indexOf("%BIND") > 0 && "undefined" != typeof UserInfoSet){
                var defExp = data.defaultval.substring(1,data.defaultval.length - 1);
                var field = defExp.split(":")[1];
                if(UserInfoSet.hasOwnProperty(field)){
                    data.value = UserInfoSet[field];
                }
        }
    }
}); 
/*数字文本框*/
//isNumSize:"Y",//是否检查数字大小范围验证'Y' Or 'N'
//min:"",最小值
//max:"",最大值

//多行容器，固定多行容器
//minLine:"",最小行
//maxLine:"",最大行

//isCheckStrLen:"N",//是否检查字数范围，'Y' Or 'N'
//minLen:"",最小长度
//maxLen:"",最大长度

//format:"",文本框大小、日期格式、单、多选框排列方式（删除boxSize）

//dateformate:"",日期格式
//minYear:"",//最小年份
//maxYear"",//最大年份

/*多选框*/
//maxSelect:"",多选框最多选择个数
//minSelect:"",多选框最少选择个数
