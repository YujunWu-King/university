/**
 * Created by WRL on 2015/9/9.
 */
SurveyBuild.extend("PageNav","baseComponent",{
    itemName: "分页符",
    _CommonField:"N",
    _getHtml : function(data,previewmode){
        var c = '';
        if(previewmode){
            if(SurveyBuild.accessType == "P"){
                c += '<div class="maincon-page">';
                c += '	<a href="#"><div class="pagebtn1">< 上一页</div></a>';
                c += '	<a href="#"><div class="pagebtn2">下一页 ></div></a>';
                c += '	<div class="clear"></div>';
                c += '</div>';
            }else{
                c += '<div class="maincon-page">';
                c += '	<a href="#"><div class="pagebtn1">< 上一页</div></a>';
                c += '	<a href="#"><div class="pagebtn2">下一页 ></div></a>';
                c += '	<div class="clear"></div>';
                c += '</div>';
            }
        }else{
            c += '<div class="question-title">';
            c += '	<div class="question-split">';
            c += '		<hr>';
            c += '		<div class="pagename">' + data.itemName + '</div>';
            c += '	</div>';
            c += '</div>';
        }
        return c;
    }
});