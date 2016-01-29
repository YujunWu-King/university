/**
 * Created by WRL on 2015/9/9.
 */
SurveyBuild.extend("TextDescribe", "baseComponent", {
    itemName: "文字说明",
    title: "文字说明",
    StorageType: "L",
    _getHtml: function(data, previewmode) {
        var c = '';
        if (previewmode) {
            if (SurveyBuild.accessType == "P") {
                //PC端访问（P）
                c += '<div class="listcon">';
                c += '  <div class="question" id="'+ data.itemId +'" style="text-align: justify;">';
                c += data.title;
                c += '  </div>';
                c += '</div>';
            } else {
                //移动终端访问（M）
                c += '<div class="listcon">';
                c += '	<div class="question" id="'+ data.itemId +'" style="text-align: justify;">';
                c += data.title;
                c += '	</div>';
                c += '</div>';
            }
        } else {
            c = '<div class="question-answer"></div>';
        }
        return c;
    }
})