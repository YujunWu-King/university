var SurveyBuild = {
    _sid: 0,
    _data: {},
    _count: 0,
    _qid: [],
    _oid: [],
    _preg: [["^\\d+$", "整数"], ["^\\d+(\\.\\d+)?$", "小数"], ["^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$", "邮箱"], ["^1[3|4|5|8]\\d{9}$", "手机"], ["^[A-Za-z]+$", "字母"], ["^\\w+$", "数字字母下划线"], ["(http://|https://|ftp://)?[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z]{2,4}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&%\\$#\\=~])*$", "网址URL"]],
    is_edit: false,
    _color: ["#FFFFFF", "#5D762A", "#FF0000", "#800080", "#008000", "#855B00", "#000000", "#FFFF00", "#990000", "#FFA500", "#E4E4E4", "#D2691E", "#1EDDFF", "#FFFFB1", "#98FB98", "#BDB76B", "#666666", "#4B0082", "#041690", "#FFB6C1", "#DDA0DD", "#0000FF", "url(../img/mixed.png);", "url(../img/trans.png);"],
    add: function(f, a) {
        if (login.role > 2 && this._count >= $_SURVEY_QUESTION_LIMIT) {
            return upgrade_fixed()
        }
        $("#y").show();
        this.is_edit = true;
        this._count == 0 && $("#question-new").css("display", "none");
        f == "text" || f == "page" || f == "js" || ++this._count;
        var d = "A" + ( + new Date()),
        g = "Q" + this._count;
		
		/*下拉框*/
        if (f == "select") {
            var e = {};
            for (var c = 1; c <= 3; ++c) {
                e[d + "_" + c] = {
                    qid: d,
                    add: 1,
                    code: c,
                    txt: "选项" + c,
                    orderby: c,
                    is_rand: 0
                }
            }
            this._data[d] = {
                add: 1,
                qtype: 1,
                code: g,
                question: "在这里输入你的问题（下拉框）",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                option: e,
                settings: {
                    defaultval: []
                }
            }
        }

		/*单选题*/
        if (f == "S") {
            var e = {};
            for (var c = 1; c <= 3; ++c) {
                e[d + "_" + c] = {
                    qid: d,
                    add: 1,
                    code: c,
                    txt: "选项" + c,
                    orderby: c,
                    is_rand: 0,
                    settings: {
                        other: 0
                    }
                }
            }
            this._data[d] = {
                add: 1,
                qtype: 2,
                code: g,
                question: "在这里输入你的问题（单选题）",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    format: 1,
                    defaultval: []
                },
                option: e
            }
        }

		/*多选框*/
        if (f == "M") {
            var e = {};
            for (var c = 1; c <= 3; ++c) {
                e[d + "_" + c] = {
                    qid: d,
                    add: 1,
                    code: c,
                    txt: "选项" + c,
                    orderby: c,
                    is_rand: 0,
                    settings: {
                        other: 0,
                        exclusive: 0
                    }
                }
            }
            this._data[d] = {
                add: 1,
                qtype: 3,
                code: g,
                question: "在这里输入你的问题（多选框）",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    format: 1,
                    min: 0,
                    max: 0,
                    defaultval: []
                },
                option: e
            }
        }

		/*填空题*/
        if (f == "O") {
            this._data[d] = {
                add: 1,
                qtype: 4,
                code: g,
                question: "在这里输入你的问题（填空题）",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    format: 1,
                    preg: "",
                    min: 0,
                    max: 0,
                    mininum: "",
                    maxinum: "",
                    suffix: "",
                    defaultval: ""
                }
            }
        }

		/*表格单选题*/
        if (f == "GS") {
            var h = {},
            e = {};
            for (var c = 1; c <= 3; ++c) {
                h[d + "_" + c] = {
                    parent_id: d,
                    add: 1,
                    code: c,
                    question: "子问题" + c,
                    orderby: c,
                    is_rand: 0,
                    settings: {
                        mandatory: 1,
                        other: 0,
                        fixedwidth: ""
                    }
                };
                e[d + "_o" + c] = {
                    qid: d,
                    add: 1,
                    code: c,
                    txt: "选项" + c,
                    orderby: c,
                    is_rand: 0,
                    settings: {
                        other: 0,
                        fixedwidth: ""
                    }
                }
            }
            this._data[d] = {
                add: 1,
                qtype: 5,
                code: g,
                question: "在这里输入你的问题",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    fixed: 0,
                    direction: "X",
                    type: "S",
                    mean: 0
                },
                child: h,
                option: e
            }
        }

		/*表格多选题*/
        if (f == "GM") {
            var h = {},
            e = {};
            for (var c = 1; c <= 3; ++c) {
                h[d + "_" + c] = {
                    parent_id: d,
                    add: 1,
                    code: c,
                    question: "子问题" + c,
                    orderby: c,
                    is_rand: 0,
                    settings: {
                        mandatory: 1,
                        other: 0,
                        fixedwidth: ""
                    }
                };
                e[d + "_o" + c] = {
                    qid: d,
                    add: 1,
                    code: c,
                    txt: "选项" + c,
                    orderby: c,
                    is_rand: 0,
                    settings: {
                        other: 0,
                        exclusive: 0,
                        fixedwidth: ""
                    }
                }
            }
            this._data[d] = {
                add: 1,
                qtype: 5,
                code: g,
                question: "在这里输入你的问题",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    fixed: 0,
                    direction: "X",
                    type: "M"
                },
                child: h,
                option: e
            }
        }

		/*表格填空题*/
        if (f == "GO") {
            var h = {},
            e = {};
            for (var c = 1; c <= 3; ++c) {
                h[d + "_" + c] = {
                    parent_id: d,
                    add: 1,
                    code: c,
                    question: "子问题" + c,
                    orderby: c,
                    is_rand: 0,
                    settings: {
                        mandatory: 1,
                        other: 0,
                        fixedwidth: ""
                    }
                };
                e[d + "_o" + c] = {
                    qid: d,
                    add: 1,
                    code: c,
                    txt: "选项" + c,
                    orderby: c,
                    is_rand: 0,
                    settings: {
                        other: 0,
                        fixedwidth: ""
                    }
                }
            }
            this._data[d] = {
                add: 1,
                qtype: 5,
                code: g,
                question: "在这里输入你的问题",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    fixed: 0,
                    direction: "X",
                    type: "O"
                },
                child: h,
                option: e
            }
        }

		/*打分题*/
        if (f == "GG") {
            var h = {},
            e = {};
            h[d + "_1"] = {
                parent_id: d,
                add: 1,
                code: 1,
                question: "选项1",
                orderby: 1,
                is_rand: 0,
                settings: {
                    mandatory: 1,
                    other: 0,
                    fixedwidth: ""
                }
            };
            for (var c = 1; c <= 5; ++c) {
                e[d + "_" + c] = {
                    qid: d,
                    add: 1,
                    code: c,
                    txt: "",
                    orderby: c,
                    is_rand: 0,
                    settings: {
                        other: 0,
                        fixedwidth: ""
                    }
                }
            }
            this._data[d] = {
                add: 1,
                qtype: 5,
                code: g,
                question: "在这里输入你的问题",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    fixed: 0,
                    direction: "X",
                    type: "G",
                    max: 5,
                    mean: 1
                },
                child: h,
                option: e
            }
        }

		/*姓名*/
        if (f == "name") {
            this._data[d] = {
                add: 1,
                qtype: 4,
                code: g,
                question: "您的姓名",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    format: 1,
                    preg: "",
                    type: "name",
                    min: 0,
                    max: 0,
                    mininum: "",
                    maxinum: "",
                    suffix: ""
                }
            }
        }

		/*性别*/
        if (f == "gender") {
            var e = {};
            e[d + "_M"] = {
                qid: d,
                add: 1,
                code: 1,
                txt: "男",
                orderby: 1,
                is_rand: 0,
                settings: {
                    other: 0
                }
            };
            e[d + "_F"] = {
                qid: d,
                add: 1,
                code: 2,
                txt: "女",
                orderby: 2,
                is_rand: 0,
                settings: {
                    other: 0
                }
            };
            this._data[d] = {
                add: 1,
                qtype: 2,
                code: g,
                question: "您的性别",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    format: 10
                },
                option: e
            }
        }

		/*生日*/
        if (f == "age") {
            this._data[d] = {
                add: 1,
                qtype: 14,
                code: g,
                question: "您的生日",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1
            }
        }

		/*城市*/
        if (f == "area") {
            this._data[d] = {
                add: 1,
                qtype: 13,
                code: g,
                question: "您的城市",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1
            }
        }

		/*日期*/
        if (f == "date") {
            this._data[d] = {
                add: 1,
                qtype: 15,
                code: g,
                question: "请选择日期",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1
            }
        }

		/*时间*/
        if (f == "time") {
            this._data[d] = {
                add: 1,
                qtype: 16,
                code: g,
                question: "请选择时间",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1
            }
        }

		/*邮箱*/
        if (f == "email") {
            this._data[d] = {
                add: 1,
                qtype: 4,
                code: g,
                question: "您的邮箱",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    format: 2,
                    preg: "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$",
                    min: 0,
                    max: 0,
                    type: "email",
                    mininum: "",
                    maxinum: "",
                    suffix: ""
                }
            }
        }

		/*手机*/
        if (f == "mobile") {
            this._data[d] = {
                add: 1,
                qtype: 4,
                code: g,
                question: "您的手机",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    format: 1,
                    preg: "^1[3|4|5|8]\\d{9}$",
                    min: 0,
                    max: 0,
                    type: "mobile",
                    mininum: "",
                    maxinum: "",
                    suffix: ""
                }
            }
        }

		/*价格*/
        if (f == "price") {
            this._data[d] = {
                add: 1,
                qtype: 4,
                code: g,
                question: "请输入价格",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    format: 1,
                    preg: "^\\d+(\\.\\d+)?$",
                    min: 0,
                    max: 0,
                    type: "price",
                    mininum: "",
                    maxinum: "",
                    suffix: ""
                }
            }
        }
		/*网址*/
        if (f == "url") {
            this._data[d] = {
                add: 1,
                qtype: 4,
                code: g,
                question: "请输入网址",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    format: 2,
                    preg: "(http://|https://|ftp://)?[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z]{2,4}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&%\\$#\\=~])*$",
                    min: 0,
                    max: 0,
                    type: "url",
                    mininum: "",
                    maxinum: "",
                    suffix: ""
                }
            }
        }
		/*颜色*/
        if (f == "color") {
            var e = {};
            for (var c = 1; c <= 24; ++c) {
                e[d + "_" + c] = {
                    qid: d,
                    add: 1,
                    code: c,
                    txt: this._color[c - 1],
                    orderby: c,
                    is_rand: 0
                }
            }
            this._data[d] = {
                add: 1,
                qtype: 6,
                code: g,
                question: "请选择颜色",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    type: "M"
                },
                option: e
            }
        }
		/*排序题*/
        if (f == "sort") {
            if (login.role > 2) {
                return upgrade_fixed()
            }
            var e = {};
            for (var c = 1; c <= 3; ++c) {
                e[d + "_" + c] = {
                    qid: d,
                    add: 1,
                    code: c,
                    txt: "选项" + c,
                    orderby: c,
                    is_rand: 0
                }
            }
            this._data[d] = {
                add: 1,
                qtype: 8,
                code: g,
                question: "请排序",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    max: 0
                },
                option: e
            }
        }
		/*多项填空题*/
        if (f == "form") {
            if (login.role > 2) {
                return upgrade_fixed()
            }
            var e = {};
            for (var c = 1; c <= 3; ++c) {
                e[d + "_" + c] = {
                    qid: d,
                    add: 1,
                    code: c,
                    txt: "问题" + c,
                    orderby: this._count,
                    is_rand: 0,
                    settings: {
                        mandatory: 1,
                        other: 0,
                        preg: "",
                        suffix: ""
                    }
                }
            }
            this._data[d] = {
                add: 1,
                qtype: 9,
                code: g,
                question: "请填写",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    format: 1,
                    total: ""
                },
                option: e
            }
        }
		/*图片选择题*/
        if (f == "img") {
            if (login.role > 2) {
                return upgrade_fixed()
            }
            this._data[d] = {
                add: 1,
                qtype: 10,
                code: g,
                question: "请选择图片",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    type: "S",
                    format: 3
                },
                option: {}
            }
        }
		/*上传题*/
        if (f == "upload") {
            if (login.role > 2) {
                return upgrade_fixed()
            }
            this._data[d] = {
                add: 1,
                qtype: 11,
                code: g,
                question: "请选择要上传的文件",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    max: 1,
                    format: 2
                }
            }
        }
		/**/
        if (f == "slider") {
            if (login.role > 2) {
                return upgrade_fixed()
            }
            this._data[d] = {
                add: 1,
                qtype: 7,
                code: g,
                question: "请打分",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                settings: {
                    max: 10,
                    start: "",
                    end: "",
                    src: "",
                    position: "H"
                }
            }
        }
		/*联动题*/
        if (f == "relation") {
            if (login.role > 2) {
                return upgrade_fixed()
            }
            var h = {},
            e = {};
            h[d + "_1"] = {
                parent_id: d,
                add: 1,
                code: 1,
                question: "选项1",
                orderby: 1,
                is_rand: 0
            };
            e[d + "_o1"] = {
                qid: d,
                add: 1,
                code: 1,
                txt: "选项1",
                orderby: 1,
                is_rand: 0,
                settings: {
                    other: 0,
                    cid: d + "_1"
                }
            };
            this._data[d] = {
                add: 1,
                qtype: 17,
                code: g,
                question: "在这里输入你的问题",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 1,
                child: h,
                option: e,
                settings: {
                    title: "请选择",
                    subtitle: "请选择"
                }
            }
        }
		/**/
        if (f == "heatmap") {
            if (login.role > 2) {
                return upgrade_fixed()
            }
        }

		/*倒计时*/
        if (f == "timer") {
            if (login.role > 2) {
                return upgrade_fixed()
            }
            this._data[d] = {
                add: 1,
                qtype: 97,
                code: "",
                question: "页面将在<span>3</span>秒后显示下一页按钮",
                is_display: 0,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 0,
                settings: {
                    time: 3,
                    auto: 0
                }
            }
        }
        /*文字说明*/
        if (f == "text") {
            this._data[d] = {
                add: 1,
                qtype: 98,
                code: "",
                question: "您的描述",
                is_display: 1,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 0
            }
        }
        /*脚本题*/
        if (f == "js") {
            if (login.role > 2) {
                return upgrade_fixed()
            }
            this._data[d] = {
                add: 1,
                qtype: 99,
                code: "",
                question: "",
                is_display: 0,
                orderby: this._count,
                is_rand: 0,
                is_mandatory: 0,
                settings: {
                    mobile: 0
                }
            }
        }
        /*分页符*/
        if (f == "page") {
            this._data[d] = {
                add: 1,
                qtype: 100,
                question: "Untitled Page"
            }
        }




        if (a) {
            setTimeout(function() {
                $("#y").animate({
                    top: '250px'
                },
                "slow",
                function() {
                    $("#y").hide();
                    $("#y").css("top", "0");
                })
            },
            650);
            return this._html(d)
        } else {
            $("#question-box").append(this._html(d));
            $("html,body").scrollTop($(document).height());
            /*$("#q" + d).click()*/
            setTimeout(function() {
                $("#y").animate({
                    top: '250px'
                },
                "slow",
                function() {
                    $("#y").hide();
                    $("#y").css("top", "0");
                })
            },
            650);
            f == "page" && this._initTab();
        }
    },
    changeDelChl: function(a) {
        if (a) {
            for (var b in a) {
                if (!a[b].hasOwnProperty("add")) {
                    this._qid.push(a[b].qid)
                }
            }
        }
    },
    changeDelOpt: function(b) {
        if (b) {
            for (var a in b) {
                if (!b[a].hasOwnProperty("add")) {
                    this._oid.push(b[a].oid)
                }
            }
        }
    },
    changeType: function(h, d) {
        this.is_edit = true;
        var j = $(d).val(),
        k = "A" + ( + new Date()),
        a = null,
        g = null;
        if (this._data[h].hasOwnProperty("option")) {
            var a = this._data[h].option;
            delete this._data[h].option
        }
        if (this._data[h].hasOwnProperty("child")) {
            var g = this._data[h].child;
            delete this._data[h].child
        }
        this._data[h].edit = 1;
        if (j == "select") {
            var f = {};
            if (a) {
                for (var e in a) {
                    f[e] = {
                        qid: h,
                        code: a[e]["code"],
                        txt: a[e]["txt"],
                        orderby: a[e]["orderby"],
                        is_rand: a[e]["is_rand"]
                    };
                    if (a[e].hasOwnProperty("add")) {
                        f[e].add = 1
                    } else {
                        f[e].oid = a[e].oid;
                        f[e].edit = 1
                    }
                }
            } else {
                for (var e = 1; e <= 3; ++e) {
                    f[k + "_" + e] = {
                        qid: h,
                        add: 1,
                        code: e,
                        txt: "选项" + e,
                        orderby: e,
                        is_rand: 0
                    }
                }
            }
            this._data[h].qtype = 1;
            this._data[h].option = f;
            delete this._data[h].settings
        } else {
            if (j == "S") {
                var f = {},
                c = {
                    format: 1
                };
                if (a) {
                    for (var e in a) {
                        f[e] = {
                            qid: h,
                            code: a[e]["code"],
                            txt: a[e]["txt"],
                            orderby: a[e]["orderby"],
                            is_rand: a[e]["is_rand"],
                            settings: {
                                other: a[e]["settings"] && a[e].settings.other ? a[e].settings.other: 0
                            }
                        };
                        if (a[e].hasOwnProperty("add")) {
                            f[e].add = 1
                        } else {
                            f[e].oid = a[e].oid;
                            f[e].edit = 1
                        }
                    }
                } else {
                    for (var e = 1; e <= 3; ++e) {
                        f[k + "_" + e] = {
                            qid: h,
                            add: 1,
                            code: e,
                            txt: "选项" + e,
                            orderby: e,
                            is_rand: 0,
                            settings: {
                                other: 0
                            }
                        }
                    }
                }
                if (this._data[h].settings && this._data[h].settings.format) {
                    c.format = this._data[h].settings.format
                }
                this._data[h].qtype = 2;
                this._data[h].option = f;
                this._data[h].settings = c
            } else {
                if (j == "M") {
                    var f = {},
                    c = {
                        format: 1,
                        min: 0,
                        max: 0
                    };
                    if (a) {
                        for (var e in a) {
                            f[e] = {
                                qid: h,
                                code: a[e]["code"],
                                txt: a[e]["txt"],
                                orderby: a[e]["orderby"],
                                is_rand: a[e]["is_rand"],
                                settings: {
                                    other: a[e]["settings"] && a[e].settings.other ? a[e].settings.other: 0,
                                    exclusive: a[e]["settings"] && a[e]["settings"]["exclusive"] ? a[e]["settings"]["exclusive"] : 0
                                }
                            };
                            if (a[e].hasOwnProperty("add")) {
                                f[e].add = 1
                            } else {
                                f[e].oid = a[e].oid;
                                f[e].edit = 1
                            }
                        }
                    } else {
                        for (var e = 1; e <= 3; ++e) {
                            f[k + "_" + e] = {
                                qid: h,
                                add: 1,
                                code: e,
                                txt: "选项" + e,
                                orderby: e,
                                is_rand: 0,
                                settings: {
                                    other: 0,
                                    exclusive: 0
                                }
                            }
                        }
                    }
                    if (this._data[h].settings && this._data[h].settings.format) {
                        c.format = this._data[h].settings.format
                    }
                    this._data[h].qtype = 3;
                    this._data[h].option = f;
                    this._data[h].settings = c
                } else {
                    if (j == "GS") {
                        var b = {},
                        f = {},
                        c = {
                            direction: "X",
                            type: "S",
                            fixed: 0,
                            mean: 0
                        };
                        if (g) {
                            for (var e in g) {
                                b[e] = {
                                    parent_id: h,
                                    code: g[e].code,
                                    question: g[e].question,
                                    orderby: g[e].orderby,
                                    is_rand: g[e].is_rand,
                                    settings: {
                                        mandatory: 1,
                                        other: 0,
                                        fixedwidth: ""
                                    }
                                };
                                if (g[e].hasOwnProperty("add")) {
                                    b[e].add = 1
                                } else {
                                    b[e].edit = 1;
                                    b[e].qid = g[e].qid
                                }
                            }
                        } else {
                            for (var e = 1; e <= 3; ++e) {
                                b[k + "_" + e] = {
                                    add: 1,
                                    parent_id: h,
                                    code: e,
                                    question: "子问题" + e,
                                    orderby: e,
                                    is_rand: 0,
                                    settings: {
                                        mandatory: 1,
                                        other: 0,
                                        fixedwidth: ""
                                    }
                                }
                            }
                        }
                        if (a) {
                            for (var e in a) {
                                f[e] = {
                                    qid: h,
                                    code: a[e]["code"],
                                    txt: a[e]["txt"],
                                    orderby: a[e]["orderby"],
                                    is_rand: a[e]["is_rand"],
                                    settings: {
                                        other: a[e].settings.hasOwnProperty("other") ? a[e].settings.other: 0,
                                        fixedwidth: a[e].settings.hasOwnProperty("fixedwidth") ? a[e].settings.fixedwidth: ""
                                    }
                                };
                                if (a[e].hasOwnProperty("add")) {
                                    f[e].add = 1
                                } else {
                                    f[e].oid = a[e].oid;
                                    f[e].edit = 1
                                }
                            }
                        } else {
                            for (var e = 1; e <= 3; ++e) {
                                f[k + "_" + e] = {
                                    qid: k,
                                    add: 1,
                                    code: e,
                                    txt: "选项" + e,
                                    orderby: e,
                                    is_rand: 0,
                                    settings: {
                                        other: 0,
                                        fixedwidth: ""
                                    }
                                }
                            }
                        }
                        if (this._data[h].settings && this._data[h].settings.fixed) {
                            c.fixed == this._data[h].settings.fixed
                        }
                        this._data[h].qtype = 5;
                        this._data[h].settings = c;
                        this._data[h].child = b;
                        this._data[h].option = f
                    } else {
                        if (j == "GM") {
                            var b = {},
                            f = {},
                            c = {
                                direction: "X",
                                type: "M",
                                fixed: 0
                            };
                            if (g) {
                                for (var e in g) {
                                    b[e] = {
                                        parent_id: h,
                                        code: g[e].code,
                                        question: g[e].question,
                                        orderby: g[e].orderby,
                                        is_rand: g[e].is_rand,
                                        settings: {
                                            mandatory: 1,
                                            other: 0,
                                            fixedwidth: ""
                                        }
                                    };
                                    if (g[e].hasOwnProperty("add")) {
                                        b[e].add = 1
                                    } else {
                                        b[e].edit = 1;
                                        b[e].qid = g[e].qid
                                    }
                                }
                            } else {
                                for (var e = 1; e <= 3; ++e) {
                                    b[k + "_" + e] = {
                                        add: 1,
                                        parent_id: h,
                                        code: e,
                                        question: "子问题" + e,
                                        orderby: e,
                                        is_rand: 0,
                                        settings: {
                                            mandatory: 1,
                                            other: 0,
                                            fixedwidth: ""
                                        }
                                    }
                                }
                            }
                            if (a) {
                                for (var e in a) {
                                    f[e] = {
                                        qid: h,
                                        code: a[e]["code"],
                                        txt: a[e]["txt"],
                                        orderby: a[e]["orderby"],
                                        is_rand: a[e]["is_rand"],
                                        settings: {
                                            other: a[e].settings.hasOwnProperty("other") ? a[e].settings.other: 0,
                                            exclusive: a[e]["settings"] && a[e]["settings"]["exclusive"] ? a[e]["settings"]["exclusive"] : 0,
                                            fixedwidth: a[e].settings.hasOwnProperty("fixedwidth") ? a[e].settings.fixedwidth: ""
                                        }
                                    };
                                    if (a[e].hasOwnProperty("add")) {
                                        f[e].add = 1
                                    } else {
                                        f[e].oid = a[e].oid;
                                        f[e].edit = 1
                                    }
                                }
                            } else {
                                for (var e = 1; e <= 3; ++e) {
                                    f[k + "_" + e] = {
                                        qid: h,
                                        add: 1,
                                        code: e,
                                        txt: "选项" + e,
                                        orderby: e,
                                        is_rand: 0,
                                        settings: {
                                            other: 0,
                                            exclusive: 0,
                                            fixedwidth: ""
                                        }
                                    }
                                }
                            }
                            if (this._data[h].settings && this._data[h].settings.fixed) {
                                c.fixed == this._data[h].settings.fixed
                            }
                            this._data[h].qtype = 5;
                            this._data[h].settings = c;
                            this._data[h].child = b;
                            this._data[h].option = f
                        } else {
                            if (j == "GO") {
                                var b = {},
                                f = {},
                                c = {
                                    direction: "X",
                                    type: "O",
                                    fixed: 0
                                };
                                if (g) {
                                    for (var e in g) {
                                        b[e] = {
                                            parent_id: h,
                                            code: g[e].code,
                                            question: g[e].question,
                                            orderby: g[e].orderby,
                                            is_rand: g[e].is_rand,
                                            settings: {
                                                mandatory: 1,
                                                other: 0,
                                                fixedwidth: ""
                                            }
                                        };
                                        if (g[e].hasOwnProperty("add")) {
                                            b[e].add = 1
                                        } else {
                                            b[e].edit = 1;
                                            b[e].qid = g[e].qid
                                        }
                                    }
                                } else {
                                    for (var e = 1; e <= 3; ++e) {
                                        b[k + "_" + e] = {
                                            add: 1,
                                            parent_id: h,
                                            code: e,
                                            question: "子问题" + e,
                                            orderby: e,
                                            is_rand: 0,
                                            settings: {
                                                mandatory: 1,
                                                other: 0,
                                                fixedwidth: ""
                                            }
                                        }
                                    }
                                }
                                if (a) {
                                    for (var e in a) {
                                        f[e] = {
                                            qid: h,
                                            code: a[e]["code"],
                                            txt: a[e]["txt"],
                                            orderby: a[e]["orderby"],
                                            is_rand: a[e]["is_rand"],
                                            settings: {
                                                other: a[e]["settings"] && a[e].settings.other ? a[e].settings.other: 0,
                                                fixedwidth: a[e].settings.hasOwnProperty("fixedwidth") ? a[e].settings.fixedwidth: ""
                                            }
                                        };
                                        if (a[e].hasOwnProperty("add")) {
                                            f[e].add = 1
                                        } else {
                                            f[e].oid = a[e].oid;
                                            f[e].edit = 1
                                        }
                                    }
                                } else {
                                    for (var e = 1; e <= 3; ++e) {
                                        f[k + "_" + e] = {
                                            qid: h,
                                            add: 1,
                                            code: e,
                                            txt: "选项" + e,
                                            orderby: e,
                                            is_rand: 0,
                                            settings: {
                                                other: 0,
                                                fixedwidth: ""
                                            }
                                        }
                                    }
                                }
                                if (this._data[h].settings && this._data[h].settings.fixed) {
                                    c.fixed == this._data[h].settings.fixed
                                }
                                this._data[h].qtype = 5;
                                this._data[h].settings = c;
                                this._data[h].child = b;
                                this._data[h].option = f
                            }
                        }
                    }
                }
            }
        }
        $(this._html(h)).find(".question-answer").replaceAll("#q" + h + " .question-answer");
        $("#question-edit").html(this._edit(h))
    },
    edit: function(f) {
        if (!this.checkQuestion()) {
            return
        }
        var e = $(f),
        h = e.attr("id").substr(1);
        e.siblings().removeClass("active");
        e.addClass("active");
        $("#build-right").css("height", "auto");
        var a = this._edit(h);
        var b = $(window).scrollTop(),
        d = $(f).offset().top - $("#question-wrap").offset().top - 150;
        $("#question-edit").html(a)
        /*
			$("#question-edit").html(a).effect("slide", {
			direction: "left"
			},
			100);
		*/
        $("#href2").click();
        /*
		$("#question-edit").css("top",
        function() {
            return d > 0 ? d: 0
        }).html(a).effect("slide", {
            direction: "left"
        },
        100);

        var g = $("#question-edit").outerHeight(),
        c = $("#question-edit-box").innerHeight() - 20;
		if (g + d > c) {
            $("#build-right").css("height", g + d + (this._count > 1 ? 10 : 160))
        }
		*/
        SurveyBuild._optionMove()
    },
    _editTabs: function() {
        $("#build-right").css("height", "auto");
        $("#href2").click();
        tabs = "";
        $("#question-box>li").each(function(f) {
            var g = $(this),
            h = g.attr("id").substr(1),
            e = SurveyBuild._data[h].qtype;
            if (e == 100) {
                tabs += '<tr><td width="320"><input id="q_' + h + '" type="text" class="option-txt" value="' + SurveyBuild._data[h].question + '" onkeyup="SurveyBuild.saveTabName(this)" /></td></tr>';
            }
        });

        var a = '<fieldset id="option-box"><table class="table table-bordered data-table"><thead><tr><th>页签名称</th></tr></thead><tbody>' + tabs + "</tbody></table></fieldset>"

        $(window).scrollTop();
        $("#question-edit").html(a)

    },
    _optionMove: function() {
        $("#child-box tbody").sortable({
            cursor: "move",
            items: "tr",
            handle: ".icon-move",
            appendTo: "parent",
            axis: "y",
            revert: true,
            update: function(b, a) {
                var d = {},
                c = a.item.attr("id").split("-")[0];
                a.item.parent().find("tr").each(function(e) {
                    var f = $(this).attr("id").split("-")[1];
                    d[f] = SurveyBuild._data[c]["child"][f];
                    d[f].edit = 1;
                    d[f]["orderby"] = e + 1
                });
                SurveyBuild._data[c].child = d;
                $(SurveyBuild._html(c)).find(".question-answer").replaceAll("#q" + c + " .question-answer")
            }
        });
        /*拖拽元素，更新orderby的值*/
        $("#option-box tbody").sortable({
            cursor: "move",
            items: "tr",
            handle: ".icon-move",
            appendTo: "parent",
            axis: "y",
            revert: true,
            update: function(c, b) {
                var a = {},
                d = b.item.attr("id").split("-")[0];
                b.item.parent().find("tr").each(function(e) {
                    var f = $(this).attr("id").split("-")[1];
                    a[f] = SurveyBuild._data[d].option[f];
                    a[f].edit = 1;
                    a[f]["orderby"] = e + 1
                });
                SurveyBuild._data[d].option = a;
                $(SurveyBuild._html(d)).find(".question-answer").replaceAll("#q" + d + " .question-answer")
            }
        })
    },
    _edit: function(m) {
        var e = "",
        o = "",
        b = "",
        a = "",
        h = "";
        if (this._data[m].qtype > 90) {
            if (this._data[m].qtype == 100) {
                quesiton = ""
            } else {
                if (this._data[m].qtype == 99) {
                    e = '<fieldset style="margin-top:10px;"><legend class="clearfix">编写脚本<label class="inline checkbox pull-right"><input type="checkbox" id="jsmobile" onchange="SurveyBuild.changeMobile(this,\'' + m + "')\" " + (this._data[m].settings.mobile == 1 ? "checked": "") + '/>手机脚本</label></legend><textarea class="question-text" id="' + m + '" onkeyup="SurveyBuild.saveQuestion(this)" style="height:300px">' + this._data[m].question + "</textarea></fieldset>"
                } else {
                    if (this._data[m].qtype == 98) {
                        e = '<fieldset><legend></legend><div id="editor-bar"><button onclick="SurveyBuild.editor(\'' + m + '\')" class="btn btn-primary btn-mini"><i class="icon-font"></i>&nbsp;编辑文字或插入图片</button></div><textarea class="question-text" id="' + m + '" onkeyup="SurveyBuild.saveQuestion(this)" style="height:300px">' + this._data[m].question + "</textarea></fieldset>"
                    } else {
                        if (this._data[m].qtype == 97) {
                            e = '<fieldset><p><br/>倒计时时间：<input type="text" onkeyup="SurveyBuild.saveTimer(\'' + m + '\')" id="timertime' + m + '" class="timertime input-mini" maxlength="3"  oncontextmenu="return false;" ondragenter="return false" onpaste="return false" value="' + this._data[m].settings.time + '"/>秒</p><p><label class="checkbox"><input type="checkbox" id="timerauto' + m + '" onchange="SurveyBuild.saveTimer(\'' + m + '\')" class="timerauto" ' + (this._data[m].settings.auto == 1 ? "checked": "") + "/>自动提交</label></p></fieldset>"
                        }
                    }
                }
            }
            b = '<fieldset style="text-align:center;"><button class="btn btn-small" onclick="SurveyBuild.copy(\'' + m + '\')"><i class="icon-plus"></i>复制</button>&nbsp;&nbsp;<button class="btn btn-small" onclick="SurveyBuild.remove(event,\'' + m + '\')"><i class="icon-trash"></i>删除</button></fieldset>'
        } else {
            e = '<fieldset><legend>题号<input type="text" id="p' + m + '" onkeyup="SurveyBuild.saveQCode(this)" value="' + this._data[m].code + '" class="input-mini qcode" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="8" />由字母、_、数字组成，首位字母</legend><div id="editor-bar"><button onclick="SurveyBuild.editor(\'' + m + '\')" class="btn btn-primary btn-mini"><i class="icon-font"></i>&nbsp;编辑文字或插入图片</button></div><textarea class="question-text" id="' + m + '" onkeyup="SurveyBuild.saveQuestion(this)">' + this._data[m].question + "</textarea></fieldset>";
            b = '<fieldset style="text-align:center;"><button class="btn btn-small btn-danger" onclick="SurveyBuild.questionAdvanced(\'' + m + '\')"><i class="icon-cogs"></i>题目设置</button>&nbsp;&nbsp;<button class="btn btn-small" onclick="SurveyBuild.copy(\'' + m + '\')"><i class="icon-plus"></i>复制</button>&nbsp;&nbsp;<button class="btn btn-small" onclick="SurveyBuild.remove(event,\'' + m + '\')"><i class="icon-trash"></i>删除</button></fieldset>';
            if (this._data[m].qtype <= 3) {
                o = '<fieldset><legend>变更题型</legend><select id="qtype" onchange="SurveyBuild.changeType(\'' + m + "',this)\">";
                o += '<option value="S"' + (this._data[m].qtype == 2 ? " selected": "") + '>单选题</option><option value="M"' + (this._data[m].qtype == 3 ? " selected": "") + '>多选题</option><option value="select"' + (this._data[m].qtype == 1 ? " selected": "") + ">下拉题</option>";
                o += "</select></fieldset>"
            } else {
                if (this._data[m].qtype == 5) {
                    o = '<fieldset><legend>变更题型</legend><select id="qtype" onchange="SurveyBuild.changeType(\'' + m + "',this)\">";
                    o += '<option value="GS"' + (this._data[m].settings.type == "S" ? " selected": "") + '>表格单选题</option><option value="GM"' + (this._data[m].settings.type == "M" ? " selected": "") + '>表格多选题</option><option value="GO"' + (this._data[m].settings.type == "O" ? " selected": "") + ">表格填空题</option>";
                    o += "</select></fieldset>"
                }
            }
        }
        if (this._data[m].hasOwnProperty("option")) {
            var g = this._data[m].option;
            if (this._data[m].qtype == 6) {
                var j = {};
                for (var f in g) {
                    j[g[f]["txt"]] = f
                }
                for (var f in this._color) {
                    if (j.hasOwnProperty(this._color[f])) {
                        a += '<li><label for="' + m + "-" + j[this._color[f]] + '"><b style="background:' + this._color[f] + '"></b><input type="checkbox" id="' + m + "-" + j[this._color[f]] + '" onchange="SurveyBuild.saveColor(this)" checked="checked" value="' + this._color[f] + '"/></label></li>'
                    } else {
                        a += '<li><label for="' + m + "-" + m + "_" + f + '"><b style="background:' + this._color[f] + '"></b><input type="checkbox" id="' + m + "-" + m + "_" + f + '" onchange="SurveyBuild.saveColor(this)" value="' + this._color[f] + '"/></label></li>'
                    }
                }
            } else {
                if (this._data[m].qtype == 17) {
                    for (var l in this._data[m].child) {
                        for (var f in g) {
                            if (g[f].settings.cid == l || "C" + g[f].settings.cid == l) {
                                a += '<tr id="' + m + "-" + f + '"><td><input type="text" class="ocode" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="5" value="' + g[f]["code"] + '" onkeyup="SurveyBuild.saveOCode(this)"/></td><td><input type="text" class="option-txt" value="' + htmlentities(g[f]["txt"]) + '" onkeyup="SurveyBuild.saveTxt(this)" /></td><td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusOption(this);return false;"><i class="icon-plus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusOption(this);return false;"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i></a></td></tr>'
                            }
                        }
                        break
                    }
                } else {
                    if (this._data[m].qtype == 10) {
                        for (var f in g) {
                            a += '<tr id="' + m + "-" + f + '"><td><input type="text" class="ocode" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="5" value="' + g[f]["code"] + '" onkeyup="SurveyBuild.saveOCode(this)"/></td><td><img style="width:60px;margin-right:5px;" src="' + g[f]["settings"]["pic"] + '"/><textarea onkeyup="SurveyBuild.saveTxt(this)" class="option-img-txt">' + g[f]["txt"] + '</textarea></td><td><a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusOption(this);return false;"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i></a></td></tr>'
                        }
                    } else {
                        for (var f in g) {
                            a += '<tr id="' + m + "-" + f + '"><td><input type="text" class="ocode" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="5" value="' + g[f]["code"] + '" onkeyup="SurveyBuild.saveOCode(this)"/></td><td><input type="text" class="option-txt" value="' + htmlentities(g[f]["txt"]) + '" onkeyup="SurveyBuild.saveTxt(this)" /></td><td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusOption(this);return false;"><i class="icon-plus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusOption(this);return false;"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i></a></td></tr>'
                        }
                    }
                }
            }
        }
        if (this._data[m].qtype == 1) {
            h += e + '<div class="question-type clearfix">' + o + '</div><fieldset id="option-box"><table class="table table-bordered data-table"><thead><tr><th>编码</th><th>答案<button class="btn btn-primary btn-mini pull-right" onclick="SurveyBuild.optionBatch(\'' + m + '\',false)">批量编辑选项</button></th><th width="45">操作</th></tr></thead><tbody>' + a + "</tbody></table></fieldset>"
        } else {
            if (this._data[m].qtype == 2) {
                var n = this._data[m].settings.format;
                h += e + '<div class="question-type clearfix">' + o;
                h += '<fieldset><legend>选项排列</legend><select data-id="' + m + '" onchange="SurveyBuild.saveFormat(this)"><option value="1"' + (n == 1 ? " selected": "") + '>垂直1列</option><option value="2"' + (n == 2 ? " selected": "") + '>垂直2列</option><option value="3"' + (n == 3 ? " selected": "") + '>垂直3列</option><option value="4"' + (n == 4 ? " selected": "") + '>垂直4列</option><option value="5"' + (n == 5 ? " selected": "") + '>垂直5列</option><option value="6"' + (n == 6 ? " selected": "") + '>垂直6列</option><option value="7"' + (n == 7 ? " selected": "") + '>垂直7列</option><option value="8"' + (n == 8 ? " selected": "") + '>垂直8列</option><option value="9"' + (n == 9 ? " selected": "") + '>水平</option></select></fieldset></div><fieldset id="option-box"><table class="table table-bordered data-table"><thead><tr><th>编码</th><th>答案<button class="btn btn-primary btn-mini pull-right" onclick="SurveyBuild.optionBatch(\'' + m + '\',false)">批量编辑选项</button></th><th width="45">操作</th></tr></thead><tbody>' + a + "</tbody></table></fieldset>"
            } else {
                if (this._data[m].qtype == 3) {
                    var n = this._data[m].settings.format;
                    h += e + '<div class="question-type clearfix">' + o + '<fieldset><legend>选项排列</legend><select data-id="' + m + '" onchange="SurveyBuild.saveFormat(this)"><option value="1"' + (n == 1 ? " selected": "") + '>垂直1列</option><option value="2"' + (n == 2 ? " selected": "") + '>垂直2列</option><option value="3"' + (n == 3 ? " selected": "") + '>垂直3列</option><option value="4"' + (n == 4 ? " selected": "") + '>垂直4列</option><option value="5"' + (n == 5 ? " selected": "") + '>垂直5列</option><option value="6"' + (n == 6 ? " selected": "") + '>垂直6列</option><option value="7"' + (n == 7 ? " selected": "") + '>垂直7列</option><option value="8"' + (n == 8 ? " selected": "") + '>垂直8列</option><option value="9"' + (n == 9 ? " selected": "") + '>水平</option></select></fieldset></div><fieldset id="option-box"><table class="table table-bordered data-table"><thead><tr><th>编码</th><th>答案<button class="btn btn-primary btn-mini pull-right" onclick="SurveyBuild.optionBatch(\'' + m + '\',false)">批量编辑选项</button></th><th width="45">操作</th></tr></thead><tbody>' + a + "</tbody></table></fieldset>"
                } else {
                    if (this._data[m].qtype == 4) {
                        var n = this._data[m].settings.format;
                        h += e + '<div class="question-type clearfix">' + o + '<fieldset><legend>文本框大小</legend><select data-id="' + m + '" onchange="SurveyBuild.saveFormat(this)"><option value="1"' + (n == 1 ? " selected": "") + '>单行 - 小</option><option value="2"' + (n == 2 ? " selected": "") + '>单行 - 中</option><option value="3"' + (n == 3 ? " selected": "") + ">单行 - 大</option>";
                        if (!this._data[m].settings.hasOwnProperty("type")) {
                            h += '<option value="4"' + (n == 4 ? " selected": "") + '>多行 - 小</option><option value="5"' + (n == 5 ? " selected": "") + '>多行 - 中</option><option value="6"' + (n == 6 ? " selected": "") + ">多行 - 大</option>"
                        }
                        h += "</select></fieldset></div>"
                    } else {
                        if (this._data[m].qtype == 5) {
                            h += e + '<div class="question-type clearfix">' + o + '</div><fieldset id="child-box"><table class="table table-bordered data-table"><thead><tr><th>编码</th><th>' + (this._data[m].settings.type == "G" ? "选项": "子问题") + '<button class="btn btn-primary btn-mini pull-right" onclick="SurveyBuild.optionBatch(\'' + m + "',true)\">批量编辑" + (this._data[m].settings.type == "G" ? "选项": "子问题") + '</button></th><th width="45">操作</th></tr></thead><tbody>';
                            for (var f in this._data[m].child) {
                                h += '<tr id="' + m + "-" + f + '"><td><input type="text" onkeyup="SurveyBuild.saveCCode(this)" maxlength="8" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" class="ccode" value="' + this._data[m].child[f].code + '" /></td><td><input type="text" class="option-txt" value="' + htmlentities(this._data[m].child[f]["question"]) + '" onkeyup="SurveyBuild.saveChild(this)"  /></td><td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusChild(this)"><i class="icon-plus-sign"></i></a>&nbsp;<a href="javascript:void(0);" onclick="SurveyBuild.minusChild(this)" class="text-warning"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info"><i class="icon-move"></i></a></td></tr>'
                            }
                            h += "</tbody></table></fieldset>";
                            if (this._data[m].settings.type != "G") {
                                h += '<fieldset id="option-box"><table class="table table-bordered data-table"><thead><tr><th>编码</th><th>答案<button class="btn btn-primary btn-mini pull-right" onclick="SurveyBuild.optionBatch(\'' + m + '\',false)">批量编辑选项</button></th><th width="45">操作</th></tr></thead><tbody>' + a + "</tbody></table></fieldset>"
                            }
                        } else {
                            if (this._data[m].qtype == 6) {
                                var k = this._data[m].settings.type;
                                h += e + '<div class="question-type clearfix">' + o + '<fieldset><legend>选项类型</legend><select data-id="' + m + '" name="type" onchange="SurveyBuild.saveStype(this)"><option value="S"' + (k == "S" ? " selected": "") + '>单选</option><option value="M"' + (k == "M" ? " selected": "") + ">多选</option></select></fieldset></div>";
                                h += '<fieldset><legend>颜色选择</legend><ul class="inline color-select">' + a + "</ul></fieldset>"
                            } else {
                                if (this._data[m].qtype == 7) {
                                    h += e + '<fieldset><table><tr><td>最大分值：</td><td><input type="text" onkeyup="SurveyBuild.saveSliderMax(this,\'' + m + '\');" onkeypress="return SurveyBuild.keypressNum(event)" value="' + this._data[m].settings.max + '" class="input-small" style="ime-mode: disabled;" maxlength="6" /></td></tr><tr><td>开始：</td><td><input type="text" onkeyup="SurveyBuild.saveSliderStart(this,\'' + m + '\');" value="' + this._data[m].settings.start + '" class="input-small"/></td></tr><tr><td>结束：</td><td><input type="text" onkeyup="SurveyBuild.saveSliderEnd(this,\'' + m + '\');" value="' + this._data[m].settings.end + '" class="input-small"/></td></tr></table></fieldset>'
                                } else {
                                    if (this._data[m].qtype == 8) {
                                        h += e + '<div class="question-type clearfix">' + o + '</div><fieldset id="option-box"><table class="table table-bordered data-table"><thead><tr><th>编码</th><th>答案<button class="btn btn-primary btn-mini pull-right" onclick="SurveyBuild.optionBatch(\'' + m + '\',false)">批量编辑选项</button></th><th width="45">操作</th></tr></thead><tbody>' + a + "</tbody></table></fieldset>"
                                    } else {
                                        if (this._data[m].qtype == 9) {
                                            var n = this._data[m].settings.format;
                                            h += e + '<div class="question-type clearfix">' + o + '<fieldset><legend>选项排列</legend><select data-id="' + m + '" onchange="SurveyBuild.saveFormat(this)"><option value="1"' + (n == 1 ? " selected": "") + '>垂直1列</option><option value="2"' + (n == 2 ? " selected": "") + '>垂直2列</option><option value="3"' + (n == 3 ? " selected": "") + '>垂直3列</option></select></fieldset></div><fieldset id="option-box"><table class="table table-bordered data-table"><thead><tr><th>编码</th><th>答案<button class="btn btn-primary btn-mini pull-right" onclick="SurveyBuild.optionBatch(\'' + m + '\',false)">批量编辑选项</button></th><th width="45">操作</th></tr></thead><tbody>' + a + "</tbody></table></fieldset>"
                                        } else {
                                            if (this._data[m].qtype == 10) {
                                                var n = this._data[m].settings.format,
                                                k = this._data[m].settings.type;
                                                h += e + '<div class="question-type clearfix">' + o + '<fieldset><legend>选项排列</legend><select data-id="' + m + '" onchange="SurveyBuild.saveFormat(this)"><option value="1"' + (n == 1 ? " selected": "") + '>垂直1列</option><option value="2"' + (n == 2 ? " selected": "") + '>垂直2列</option><option value="3"' + (n == 3 ? " selected": "") + '>垂直3列</option><option value="4"' + (n == 4 ? " selected": "") + '>垂直4列</option><option value="5"' + (n == 5 ? " selected": "") + '>垂直5列</option></select></fieldset><fieldset><legend>选项类型</legend><select data-id="' + m + '" name="type" onchange="SurveyBuild.saveStype(this)"><option value="S"' + (k == "S" ? " selected": "") + '>单选</option><option value="M"' + (k == "M" ? " selected": "") + '>多选</option></select></fieldset></div><fieldset id="option-box"><table class="table table-bordered data-table"><thead><tr><th>编码</th><th>图片(描述)</th><th width="45">操作</th></tr></thead><tbody>' + a + '</tbody><tr><td colspan="3"><button type="button" class="btn btn-primary" onclick="SurveyBuild.upload(\'' + m + "')\">选择图片</button></td></tr></table></fieldset>"
                                            } else {
                                                if (this._data[m].qtype == 11) {
                                                    var n = this._data[m].settings.format;
                                                    h += e + '<div class="question-type clearfix">' + o + '<fieldset><legend>文件限制</legend><select data-id="' + m + '" onchange="SurveyBuild.saveFormat(this)"><option value="1"' + (n == 1 ? " selected": "") + '>不限</option><option value="2"' + (n == 2 ? " selected": "") + ">图片</option></select></fieldset></div>"
                                                } else {
                                                    if (this._data[m].qtype == 12) {} else {
                                                        if (this._data[m].qtype == 13 || this._data[m].qtype == 14 || this._data[m].qtype == 15 || this._data[m].qtype == 16) {
                                                            h += e + '<div class="question-type clearfix">' + o + "</div>"
                                                        } else {
                                                            if (this._data[m].qtype == 17) {
                                                                h += e + '<fieldset><form target="relationUpload" action="/ajax/build" enctype="multipart/form-data" method="POST"><input type="hidden" name="op" value="uploadRelation" /><input type="hidden" name="qid" value="' + m + '" /><input type="file" name="file" /> <button type="submit" onclick="loading($(\'#build-box\'));" class="btn btn-primary btn-small">上传</button></form><iframe id="relationUpload" style="display:none;height:0px;" name="relationUpload"></iframe><p>上传格式为xslx 模板<a href="http://www.lediaocha.com/example/relation.xlsx">下载</a></p></fieldset><fieldset>一级问题<input type="text" data-id="' + m + '" value="' + this._data[m].settings.title + '" onkeyup="SurveyBuild.saveTitle(this)"/></fieldset><fieldset id="child-box"><table class="table table-bordered data-table"><thead><tr><th>编码</th><th>一级列表</th><th width="45">操作</th></tr></thead><tbody>';
                                                                var d = "";
                                                                for (var f in this._data[m].child) {
                                                                    h += '<tr id="' + m + "-" + f + '"><td><input type="text" onkeyup="SurveyBuild.saveCCode(this)" maxlength="5" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" class="ocode" value="' + this._data[m].child[f].code + '" /></td><td><input type="text" class="option-txt" value="' + htmlentities(this._data[m].child[f].question) + '" onkeyup="SurveyBuild.saveChild(this)"  /></td><td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusChild(this)"><i class="icon-plus-sign"></i></a>&nbsp;<a href="javascript:void(0);" onclick="SurveyBuild.minusChild(this)" class="text-warning"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info"><i class="icon-move"></i></a></td></tr>';
                                                                    d += '<option id="first' + f + '" value="' + f + '">' + this._data[m].child[f].question + "</option>"
                                                                }
                                                                h += '</tbody></table></fieldset><fieldset>二级问题<input type="text" data-id="' + m + '" value="' + this._data[m].settings.subtitle + '" onkeyup="SurveyBuild.saveSubTitle(this)"/></fieldset>';
                                                                h += '<fieldset>选择一级问题<select style="width:200px" data-id="' + m + '" id="selectTitle" onchange="SurveyBuild.changeTitle(this)">' + d + "</select></fieldset>";
                                                                h += '<fieldset id="option-box"><table class="table table-bordered data-table"><thead><tr><th>编码</th><th>二级列表</th><th width="45">操作</th></tr></thead><tbody id="' + m + '-sub">' + a + "</tbody></table></fieldset>"
                                                            } else {
                                                                if (this._data[m].qtype > 90 && this._data[m].qtype < 100) {
                                                                    h += e
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        h += b;
        return h
    },

    changeMobile: function(a, b) {
        this.is_edit = true;
        this._data[b].edit = 1;
        this._data[b].settings.mobile = $(a).prop("checked") ? 1 : 0
    },
    upload: function(b) {
        var a = '<div class="modal-header"><h4>插入图片</h4></div>';
        a += '<div id="modal-question-upload" class="modal-body"><div class="upload-search"><form target="iframe' + b + '" action="/ajax/build" enctype="multipart/form-data" method="post"><input id="' + b + '-file" type="file" name="file" /><input type="hidden" name="qid" value="' + b + '" /><input type="hidden" name="op" value="upload" /><button type="submit" class="btn btn-primary btn-small" onclick="loading($(\'#modal-question-upload\'))"><i class="icon-upload-alt"></i>上传图片</button></form><iframe id="iframe' + b + '" width="1" scrolling="no" height="1" style="display:none;" name="iframe' + b + '"></iframe></div><div class="upload-gallery clearfix"></div></div>';
        a += '<div class="modal-footer clearfix"><button class="btn pull-left" onclick="$.fancybox.close()"><i class="icon-ban-circle"></i>取消</button><button class="btn btn-warning pull-right" onclick="SurveyBuild.uploadSave(\'' + b + '\')"><i class="icon-ok"></i>确定</button></div>';
        $.fancybox.open({
            content: a,
            afterShow: function() {
                $.post("/ajax/build", {
                    op: "loadGallery"
                },
                function(f) {
                    var e = "";
                    for (var d in SurveyBuild._data[b].option) {
                        var c = $.inArray(SurveyBuild._data[b].option[d].settings.pic, f);
                        if (c != -1) {
                            f.splice(c, 1)
                        }
                    }
                    for (var d in f) {
                        e += '<a class="thumbnail" href="javascript:void(0);"><img src="' + f[d] + '" /></a>'
                    }
                    $("#modal-question-upload>.upload-gallery").html(e)
                },
                "JSON");
                $("#modal-question-upload").on("click", "a.thumbnail",
                function() {
                    $(this).toggleClass("active")
                })
            }
        })
    },
    uploadSave: function(c) {
        this.is_edit = true;
        var b = "A" + ( + new Date()),
        a = 0;
        $("#option-box .ocode").each(function() {
            var d = parseInt($(this).val());
            if (d > a) {
                a = d
            }
        });
        if (this._data[c].option instanceof Array) {
            this._data[c].option = {}
        }
        $("#modal-question-upload a.active").each(function(d) {++a;
            SurveyBuild._data[c].option[b + "_" + d] = {
                qid: b,
                add: 1,
                code: a,
                txt: "",
                orderby: a,
                is_rand: 0,
                settings: {
                    pic: $(this).find("img").attr("src")
                }
            }
        });
        $(this._html(c)).find(".question-answer").replaceAll("#q" + c + " .question-answer");
        $("#question-edit").html(this._edit(c));
        $.fancybox.close()
    },
    copy: function(g) {
        if (login.role > 2 && this._count >= $_SURVEY_QUESTION_LIMIT) {
            return upgrade_fixed()
        }
        this.is_edit = true;
        var a = this._data[g].qtype;
        a > 90 || ++this._count;
        var e = "A" + ( + new Date());
        this._data[e] = cloneObj(this._data[g]);
        this._data[e].code = a > 90 ? "": "Q" + this._count;
        this._data[e].add = 1;
        var c = {};
        if (this._data[e].hasOwnProperty("child")) {
            var h = {},
            b = 0;
            for (var d in this._data[e]["child"]) {++b;
                h[e + "_" + b] = this._data[e]["child"][d];
                h[e + "_" + b].add = 1;
                if (this._data[e]["child"][d].hasOwnProperty("add")) {
                    c[d] = e + "_" + b
                } else {
                    c[this._data[e]["child"][d].qid] = e + "_" + b
                }
            }
            this._data[e].child = h
        }
        if (this._data[e].hasOwnProperty("option")) {
            var f = {},
            b = 0;
            for (var d in this._data[e]["option"]) {++b;
                f[e + "_" + b] = this._data[e].option[d];
                f[e + "_" + b].add = 1;
                if (this._data[g].qtype == 17) {
                    f[e + "_" + b].settings.cid = c[this._data[e].option[d].settings.cid]
                }
            }
            this._data[e].option = f
        }
        $(this._html(e)).insertAfter("#q" + g).effect("highlight", {},
        300,
        function() {
            $("#q" + e).click();
            SurveyBuild.sort()
        });
        return false
    },
    remove: function(a, b) {
        if (confirm("确定删除吗？")) {
            this.is_edit = true;
            if (this._data.hasOwnProperty(b)) {
                this._data[b].qtype > 90 || --this._count;
                $("#q" + b).remove();
                if (!this._data[b].hasOwnProperty("add") && this._data[b].qtype != 100) {
                    this._qid.push(this._data[b].qid)
                }
                delete this._data[b];
                $("#question-edit").empty();
                if ($("#question-box>li").length == 0) {
                    $("#question-new").css("display", "block")
                } else {
                    this.sort()
                }
            } else {
                this._error("非法操作")
            }
        }
        if (a.stopPropagation) {
            a.stopPropagation()
        }
        a.cancelBubble = true;
        return false
    },
    sort: function() {
        this.is_edit = true;
        var b = 0,
        a = 0;
        $("#question-box>li").each(function(c) {
            var e = $(this).attr("id").substr(1),
            d = SurveyBuild._data[e]["qtype"];
            d > 90 || ++b;
            d == 100 || ++a;
            SurveyBuild._data[e].sort = 1;
            SurveyBuild._data[e].orderby = a
        });
        this._initTab();

    },
    _html: function(h) {
        /*console.log(JSON.stringify(this._data[h]));*/
        var c = '<li id="q' + h + '" onclick="SurveyBuild.edit(this)">';
        if (this._data[h].qtype == 1) {
            c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer"><b class="read-select"> - 请选择 - </b><ul class="select-box">';
            for (var e in this._data[h].option) {
                c += '<li id="o' + e + '">' + this._data[h].option[e]["txt"] + "</li>"
            }
            c += "</ul></div>"
        } else {
            if (this._data[h].qtype == 2) {
                c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer"><ul class="format format' + this._data[h].settings.format + '">';
                for (var e in this._data[h].option) {
                    c += '<li id="o' + e + '" class="read-radio">' + this._data[h].option[e]["txt"];
                    if (this._data[h].option[e].settings.other == 1) {
                        c += '<b class="read-input"></b>'
                    }
                    c += "</li>"
                }
                c += "</ul></div>"
            } else {
                if (this._data[h].qtype == 3) {
                    c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer"><ul class="format format' + this._data[h].settings.format + '">';
                    for (var e in this._data[h].option) {
                        c += '<li id="o' + e + '" class="read-check">' + this._data[h].option[e]["txt"];
                        if (this._data[h].option[e].settings.other == 1) {
                            c += '<b class="read-input"></b>'
                        }
                        c += "</li>"
                    }
                    c += "</ul></div>"
                } else {
                    if (this._data[h].qtype == 4) {
                        c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer"><div class="format format' + this._data[h].settings.format + '"><b class="read-input"></b><span class="suffix">' + (this._data[h].settings.hasOwnProperty("suffix") ? this._data[h].settings.suffix: "") + "</span></div></div>"
                    } else {
                        if (this._data[h].qtype == 5) {
                            c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer">';
                            if (this._data[h].settings.type == "G") {
                                c += '<table class="score-box">';
                                var f = "",
                                l = '<table style="border: 1px solid #CCCCCC;background:#fff;"><tr>',
                                g = true,
                                b = 100 / this._data[h].settings.max;
                                for (var e in this._data[h].option) {
                                    l += '<td style="width:' + b + '%;">' + this._data[h].option[e].code + "</td>";
                                    f += '<td id="o' + e + '" style="width:' + b + '%;">' + this._data[h].option[e].txt + "</td>";
                                    if (this._data[h].option[e].txt != "") {
                                        g = false
                                    }
                                }
                                l += "</tr></table>";
                                if (!g) {
                                    c += "<tr><td></td><td><table><tr>" + f + "</tr></table></td></tr>"
                                }
                                for (var e in this._data[h].child) {
                                    c += '<tr><th id="c' + e + '">' + this._data[h].child[e].question + (this._data[h].child[e].settings.other == 1 ? '<b class="read-input"></b>': "") + "</th><td>" + l + "</td></tr>"
                                }
                                c += "</table>"
                            } else {
                                c += '<table class="table table-bordered">';
                                var l = "";
                                switch (this._data[h].settings.type) {
                                case "S":
                                    l = '<img src="../img/read-radio.gif"/>';
                                    break;
                                case "M":
                                    l = '<img src="../img/read-check.gif"/>';
                                    break;
                                case "O":
                                    l = '<b class="read-input"></b>';
                                    break
                                }
                                if (this._data[h].settings.direction == "Y") {
                                    c += "<tr><td></td>";
                                    for (var e in this._data[h].child) {
                                        c += '<th id="c' + e + '"' + (this._data[h].child[e].settings.hasOwnProperty("fixedwidth") ? ' style="width:' + this._data[h].child[e].settings.fixedwidth + 'px"': "") + ">" + this._data[h].child[e]["question"] + (this._data[h].child[e].settings.other == 1 ? '<b class="read-input"></b>': "") + "</th>"
                                    }
                                    c += "</tr>";
                                    for (var e in this._data[h].option) {
                                        c += '<tr><td id="o' + e + '">' + this._data[h].option[e]["txt"] + "</td>";
                                        var a = "";
                                        if (this._data[h].option[e].settings.other == 1) {
                                            a = '<b class="read-input"></b>'
                                        }
                                        for (var d in this._data[h].child) {
                                            c += "<td>" + l + a + "</td>"
                                        }
                                        c += "</tr>"
                                    }
                                } else {
                                    c += "<tr><td></td>";
                                    for (var e in this._data[h].option) {
                                        c += '<td id="o' + e + '"' + (this._data[h].option[e].settings.hasOwnProperty("fixedwidth") ? ' style="width:' + this._data[h].option[e].settings.fixedwidth + 'px"': "") + ">" + this._data[h].option[e]["txt"] + "</td>"
                                    }
                                    c += "</tr>";
                                    for (var e in this._data[h].child) {
                                        c += '<tr><th id="c' + e + '">' + this._data[h].child[e].question + (this._data[h].child[e].settings.other == 1 ? '<b class="read-input"></b>': "") + "</th>";
                                        for (var d in this._data[h].option) {
                                            c += "<td>" + l;
                                            if (this._data[h].option[d].settings.other == 1) {
                                                c += '<b class="read-input"></b>'
                                            }
                                            c += "</td>"
                                        }
                                        c += "</tr>"
                                    }
                                }
                                c += "</table>"
                            }
                            c += "</div>"
                        } else {
                            if (this._data[h].qtype == 6) {
                                c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer"><ul class="inline color-box">';
                                for (var e in this._data[h].option) {
                                    c += '<li id="o' + e + '"><b style="background:' + this._data[h].option[e]["txt"] + '"></b></li>'
                                }
                                c += "</ul></div>"
                            } else {
                                if (this._data[h].qtype == 7) {
                                    c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h]["question"] + '</div></div><div class="question-answer" style="padding-top: 30px;"><div class="slider-start">' + this._data[h].settings.start + '</div><div class="ui-slider ui-slider-horizontal ui-widget ui-widget-content ui-corner-all" style="width:500px;float:left;margin: 4px;"><a href="#" class="ui-slider-handle ui-state-default ui-corner-all" style="left: 20%;"><span class="slider-value">' + (this._data[h].settings.max > 0 ? 0.2 * this._data[h].settings.max: 0) + '</span></a></div><div class="slider-end">' + this._data[h].settings.end + "</div>"
                                } else {
                                    if (this._data[h].qtype == 8) {
                                        c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer clearfix"><div class="sort-left">';
                                        var k = 0;
                                        for (var e in this._data[h].option) {
                                            c += '<p id="o' + e + '">' + this._data[h].option[e]["txt"] + "</p>"; ++k
                                        }
                                        c += '</div><div class="sort-right">';
                                        if (this._data[h].settings.max != 0) {
                                            k = this._data[h].settings.max
                                        }
                                        for (var e = 1; e <= k; ++e) {
                                            c += "<p>" + e + "</p>"
                                        }
                                        c += "</div></div>"
                                    } else {
                                        if (this._data[h].qtype == 9) {
                                            c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer"><ul class="format format' + this._data[h].settings.format + '">';
                                            for (var e in this._data[h].option) {
                                                c += '<li><div id="o' + e + '" class="opentxt">' + this._data[h].option[e]["txt"];
                                                if (this._data[h].option[e].settings.other == 1) {
                                                    c += '<b class="read-input"></b>'
                                                }
                                                c += '</div><b class="read-line"></b><span class="suffix">' + (this._data[h].option[e].settings.hasOwnProperty("suffix") ? this._data[h].option[e].settings.suffix: "") + "</span></li>"
                                            }
                                            c += "</ul></div>"
                                        } else {
                                            if (this._data[h].qtype == 10) {
                                                c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer"><ul class="format format' + this._data[h].settings.format + ' thumbnails">';
                                                for (var e in this._data[h].option) {
                                                    c += '<li id="o' + e + '"><a href="javascript:void(0);" class="thumbnail"><img src="' + this._data[h].option[e]["settings"]["pic"] + '"/></li>'
                                                }
                                                c += "</ul></div>"
                                            } else {
                                                if (this._data[h].qtype == 11) {
                                                    c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer"><button class="btn btn-small"><i class="icon-upload-alt"></i>上传文件</button></div>'
                                                } else {
                                                    if (this._data[h].qtype == 12) {} else {
                                                        if (this._data[h].qtype == 13) {
                                                            c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer"><select name="province"><option value="">省份</option></select>&nbsp;&nbsp;<select name="city"><option value="">城市</option></select></div>'
                                                        } else {
                                                            if (this._data[h].qtype == 14) {
                                                                c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer"><select name="birthyear"><option value=""> 年 </option></select>&nbsp;&nbsp;<select name="birthmonth"><option value=""> 月 </option></select>&nbsp;&nbsp;<select name="birthday"><option value=""> 日 </option></select></div>'
                                                            } else {
                                                                if (this._data[h].qtype == 15 || this._data[h].qtype == 16) {
                                                                    c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer"><input type="text" name="datepicker" /></div>'
                                                                } else {
                                                                    if (this._data[h].qtype == 17) {
                                                                        c += '<div class="question-title"><b class="question-code">' + this._data[h].code + '.</b><div class="question-question">' + this._data[h].question + '</div></div><div class="question-answer"><b class="read-select title">' + this._data[h].settings.title + '</b>&nbsp;&nbsp;<b class="read-select subtitle">' + this._data[h].settings.subtitle + "</b></div>"
                                                                    } else {
                                                                        if (this._data[h].qtype == 97) {
                                                                            c += '<div class="question-title"><pre class="question-question">' + this._data[h].question + "</pre></div>"
                                                                        } else {
                                                                            if (this._data[h].qtype == 98) {
                                                                                c += '<div class="question-title"><div class="question-question">' + this._data[h].question + "</div></div>"
                                                                            } else {
                                                                                if (this._data[h].qtype == 99) {
                                                                                    c += '<div class="question-title"><pre class="question-question">' + this._data[h].question + "</pre></div>"
                                                                                } else {
                                                                                    if (this._data[h].qtype == 100) {
                                                                                        c += '<div class="question-title"><div class="question-split"><hr/><div>分页符</div></div></div>'
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        /*console.log(c);*/
        c += '<div class="question-action"><a class="build-icon-minus" title="删除" onclick="return SurveyBuild.remove(event,\'' + h + '\')"></a><a class="build-icon-copy" title="复制" onclick="SurveyBuild.copy(\'' + h + '\')"></a><i class="build-icon-arrow"></i></div></li>';
        return c
    },
    editor: function(a) {
        this.is_edit = true;
        var b = "K_" + a;
        $.fancybox.open({
            content: '<textarea id="' + b + '">' + SurveyBuild._data[a].question + "</textarea>",
            minWidth: 1002,
            minHeight: 482,
            beforeShow: function() {
                window.editor = KindEditor.create("#" + b, {
                    items: ["source", "fontname", "fontsize", "|", "forecolor", "hilitecolor", "bold", "italic", "underline", "strikethrough", "removeformat", "|", "justifyleft", "justifycenter", "justifyright", "insertorderedlist", "insertunorderedlist", "|", "emoticons", "image", "multiimage", "flash", "link", "wordpaste"],
                    width: "1000px",
                    height: "480px",
                    allowFileManager: true,
                    filterMode: false
                })
            },
            beforeClose: function() {
                var c = editor.html();
                SurveyBuild._data[a].edit = 1;
                SurveyBuild._data[a].question = c;
                $("#" + a).val(c);
                $("#q" + a).find(".question-question").html(c);
                KindEditor.remove("#" + b)
            }
        })
    },
    allRand: function(b, d) {
        var a = d ? "crand": "rand";
        $("." + a).prop("checked", $(b).prop("checked"))
    },
    allMandatory: function(a) {
        $(".mandatory").prop("checked", $(a).prop("checked"))
    },
    _advancedOption: function(g) {
        var b = this._data[g].qtype,
        d = this._data[g].option;
        if (b == 6) {
            var a = '<fieldset><label for="allRand"><input type="checkbox" id="allRand" onchange="SurveyBuild.allRand(this)"> 所有随机</label><ul class="inline color-rand">';
            for (var c in d) {
                a += '<li><label style="background:' + d[c]["txt"] + '" for="rand' + c + '"></label><input type="checkbox" class="rand" id="rand' + c + '" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + " /></li>"
            }
            return a + "</ul></fieldset>"
        }
        var e = '<fieldset><table id="table-advanced-option" class="table table-hover table-bordered"><tr><th>选项</th><th style="width:75px;"><input type="checkbox" onchange="SurveyBuild.allRand(this)"/> 随机&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="选中的选项进行随机，未选中的不变"><i class="icon-question-sign"></i></a></th>';
        if (b == 1) {
            e += '<th style="width:55px;">默认选择</th></tr>';
            for (var c in d) {
                e += "<tr><td>" + d[c]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);" class="defaultval" data-id="' + c + '" value="1"' + (this._data[g].settings.hasOwnProperty("defaultval") && $.inArray(d[c].code, this._data[g].settings.defaultval) > -1 ? " checked": "") + " /></td></tr>"
            }
        } else {
            if (b == 2) {
                e += '<th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th><th style="width:55px;">默认选择</th></tr>';
                for (var c in d) {
                    e += "<tr><td>" + d[c]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="other" data-id="' + c + '" value="1"' + (d[c].settings.other == 1 ? " checked": "") + ' /></td><td><input type="checkbox" onchange="$(\'.defaultval\').not(this).prop(\'checked\',false);" class="defaultval" data-id="' + c + '" value="1"' + (this._data[g].settings.hasOwnProperty("defaultval") && $.inArray(d[c].code, this._data[g].settings.defaultval) > -1 ? " checked": "") + " /></td></tr>"
                }
            } else {
                if (b == 3) {
                    e += '<th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th><th style="width:30px;">排他</th><th style="width:55px;">默认选择</th></tr>';
                    for (var c in d) {
                        e += "<tr><td>" + d[c]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="other" data-id="' + c + '" value="1"' + (d[c].settings.other == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="exclusive" data-id="' + c + '" value="1"' + (d[c]["settings"]["exclusive"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="defaultval" data-id="' + c + '" value="1"' + (this._data[g].settings.hasOwnProperty("defaultval") && $.inArray(d[c].code, this._data[g].settings.defaultval) > -1 ? " checked": "") + " /></td></tr>"
                    }
                } else {
                    if (b == 5) {} else {
                        if (b == 9) {
                            e += '<th style="width:60px;"><input type="checkbox" onchange="SurveyBuild.allMandatory(this)"/> 必填</th><th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th><th style="width:60px;">正则</th><th style="width:40px;">后缀</th></tr>';
                            for (var c in d) {
                                e += "<tr><td>" + d[c]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="mandatory" data-id="' + c + '" value="1"' + (d[c].settings.mandatory == "1" ? " checked": "") + ' /></td><td><input type="checkbox" class="other" data-id="' + c + '" value="1"' + (d[c].settings.other == "1" ? " checked": "") + ' /></td><td><select data-id="' + c + '" name="preg"><option value="">不使用</option>';
                                for (var f in this._preg) {
                                    e += '<option value="' + this._preg[f][0] + '"' + (d[c].settings.preg == this._preg[f][0] ? ' selected="selected"': "") + ">" + this._preg[f][1] + "</option>"
                                }
                                e += '</select></td><td><input type="text" data-id="' + c + '" class="suffix" style="width:30px" value="' + d[c].settings.suffix + '" /></td></tr>'
                            }
                        } else {
                            if (b == 10) {
                                e += "</tr>";
                                for (var c in d) {
                                    e += '<tr><td><img src="' + d[c]["settings"]["pic"] + '" style="width:60px;height:50px;"/>' + d[c]["txt"] + '</td><td style="text-align:center;"><input type="checkbox" class="rand" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + " /></td></tr>"
                                }
                            } else {
                                e += "</tr>";
                                for (var c in d) {
                                    e += "<tr><td>" + d[c]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + c + '" value="1"' + (d[c]["is_rand"] == 1 ? " checked": "") + " /></td></tr>"
                                }
                            }
                        }
                    }
                }
            }
        }
        return e + "</table></fieldset>"
    },
    minSelect: function() {
        var a = $("#min").val();
        $("#max>option").each(function(b) {
            $(this).prop("disabled", b < a)
        })
    },
    maxSelect: function() {
        var a = $("#max").val();
        $("#min>option").each(function(b) {
            $(this).prop("disabled", b > a)
        })
    },
    keypressNum: function(b) {
        var a = b.which;
        return a == 8 || a == 46 || a == 127 || a == 0 || a >= 48 && a <= 57
    },
    questionAdvanced: function(j) {
        var g = this._data[j].option;
        var a = this._data[j].child;
        var b = '<div class="modal-header"><h4>问题题目设置</h4></div><div id="modal-question-advanced" class="modal-body"><fieldset><label class="checkbox inline" for="is_mandatory"><input type="checkbox"' + (this._data[j]["is_mandatory"] == 1 ? " checked": "") + ' value="1" id="is_mandatory"/> 是否必答&nbsp;&nbsp;<a href="#" class="popo" data-content="用户需要回答这个问题，才可以进入到下一个页面。"><i class="icon-question-sign"></i></a></label><label class="checkbox inline" for="is_display"><input type="checkbox"' + (this._data[j]["is_display"] == 1 ? " checked": "") + ' value="1" id="is_display"/> 是否显示&nbsp;&nbsp;<a href="#" class="popo" data-content="勾选该选项后，受访者才能看到此题目，否则隐藏。"><i class="icon-question-sign"></i></a></label>';
        if (this._data[j].qtype == 5 && this._data[j].settings.type != "G") {
            b += '<label class="checkbox inline" for="fixed"><input type="checkbox"' + (this._data[j].settings.fixed == 1 ? " checked": "") + ' value="1" id="fixed"> 固定表头&nbsp;&nbsp;<a href="#" class="popo" data-content="表头会随着滚动条移动"><i class="icon-question-sign"></i></a></label><label class="checkbox inline" for="direction"><input type="checkbox" value="1" id="direction"' + (this._data[j].settings.direction == "Y" ? " checked": "") + " onchange=\"SurveyBuild.changeDirection('" + j + '\')"/> 矩阵翻转&nbsp;&nbsp;<a href="#" class="popo" data-content="转为列矩阵，每列单选、多选"><i class="icon-question-sign"></i></a></label>';
            if (this._data[j].settings.type == "S") {
                b += '<label class="checkbox inline" for="mean"><input type="checkbox" value="1" id="mean"' + (this._data[j].settings.hasOwnProperty("mean") && this._data[j].settings.mean == 1 ? " checked": "") + "/> 报表统计计算平均值</label>"
            }
        }
        b += "</fieldset>";
        if (this._data[j].qtype == 17) {
            b += '<fieldset><table id="table-advanced-child" class="table table-hover table-bordered"><tr><th>一级列表</th><th style="width:75px;"><input type="checkbox" onchange="SurveyBuild.allRand(this,1)" /> 随机&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="选中的选项进行随机，未选中的不变"><i class="icon-question-sign"></i></a></th></tr>';
            for (var f in a) {
                b += "<tr><td>" + a[f]["question"] + '</td><td><input type="checkbox" class="crand" data-id="' + f + '" value="1"' + (a[f]["is_rand"] == 1 ? " checked": "") + " /></td></tr>"
            }
            b += "</table></fieldset>";
            b += '<fieldset><table id="table-advanced-option" class="table table-hover table-bordered"><tr><th>选项</th><th style="width:75px;"><input type="checkbox" onchange="SurveyBuild.allRand(this)"/> 随机&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="选中的选项进行随机，未选中的不变"><i class="icon-question-sign"></i></a></th><th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th></tr>';
            for (var f in g) {
                b += "<tr><td>" + g[f]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + f + '" value="1"' + (g[f]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="other" data-id="' + f + '" value="1"' + (g[f].settings.other == 1 ? " checked": "") + " /></td></tr>"
            }
            b += "</table></fieldset>"
        } else {
            if (this._data[j].qtype == 5) {
                b += '<fieldset><table id="table-advanced-child" class="table table-hover table-bordered"><tr><th>子问题</th><th style="width:75px;"><input type="checkbox" onchange="SurveyBuild.allRand(this,1)" /> 随机&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="选中的选项进行随机，未选中的不变"><i class="icon-question-sign"></i></a></th><th style="width:50px;"><input type="checkbox" onchange="SurveyBuild.allMandatory(this)" /> 必答</th><th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th>';
                if (this._data[j].settings.direction == "Y") {
                    b += '<th style="width:75px">列宽（px）</th>'
                }
                b += "</tr>";
                var d = 1;
                for (var f in a) {
                    b += "<tr><td>" + a[f]["question"] + '</td><td><input type="checkbox" class="crand" data-id="' + f + '" value="1"' + (a[f]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="mandatory" data-id="' + f + '" value="1"' + (a[f].settings.mandatory == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="cother" data-id="' + f + '" value="1"' + (a[f].settings.other == 1 ? " checked": "") + " /></td>";
                    if (this._data[j].settings.direction == "Y") {
                        b += '<td><input type="text" data-id="' + f + '" value="' + (a[f].settings.hasOwnProperty("fixedwidth") ? a[f].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + d + '"/></td>'; ++d
                    }
                    b += "</tr>"
                }
                b += "</table></fieldset>";
                if (this._data[j].settings.type == "G") {
                    b += '<fieldset><p>最高多少分：<select id="max" onchange="SurveyBuild.changeGG(\'' + j + "')\">";
                    for (var f = 2; f <= 10; ++f) {
                        b += '<option value="' + f + '"' + (f == this._data[j].settings.max ? " selected": "") + ">" + f + "</option>"
                    }
                    b += '</select></p><table id="table-advanced-option" class="table table-hover table-bordered"><thead><tr><th style="width:60px;text-align:center;">分值</th><th style="text-align:left;">分值描述（可为空）</th></tr></thead><tbody>';
                    for (var f in g) {
                        b += '<tr><td style="text-align:center">' + g[f].code + '</td><td style="text-align:left"><input type="text" value="' + g[f].txt + '" tabindex="' + d + '"/></td></tr>'; ++d
                    }
                    b += "</tbody></table></fieldset>"
                } else {
                    d = 1;
                    var n = '<fieldset><table id="table-advanced-option" class="table table-hover table-bordered"><tr><th>选项</th><th style="width:75px;"><input type="checkbox" onchange="SurveyBuild.allRand(this)"/> 随机&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="选中的选项进行随机，未选中的不变"><i class="icon-question-sign"></i></a></th>';
                    if (this._data[j].settings.type != "O") {
                        n += '<th style="width:50px;">其它&nbsp;&nbsp;<a href="#" class="popo" data-placement="left" data-content="勾选后该选项后出现文本框。常用于其它，请注明_____"><i class="icon-question-sign"></i></a></th>'
                    }
                    if (this._data[j].settings.type == "M") {
                        n += '<th style="width:30px;">排他</th>';
                        if (this._data[j].settings.direction == "X") {
                            n += '<th style="width:75px;">列宽（px）</th>'
                        }
                        n += "</tr>";
                        for (var f in g) {
                            n += "<tr><td>" + g[f]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + f + '" value="1"' + (g[f]["is_rand"] == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="other" data-id="' + f + '" value="1"' + (g[f].settings.other == 1 ? " checked": "") + ' /></td><td><input type="checkbox" class="exclusive" data-id="' + f + '" value="1"' + (g[f]["settings"]["exclusive"] == 1 ? " checked": "") + " /></td>";
                            if (this._data[j].settings.direction == "X") {
                                n += '<td><input type="text" data-id="' + f + '" value="' + (g[f].settings.hasOwnProperty("fixedwidth") ? g[f].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + d + '"/></td>'; ++d
                            }
                            n += "</tr>"
                        }
                    } else {
                        if (this._data[j].settings.direction == "X") {
                            n += '<th style="width:75px;">列宽（px）</th>'
                        }
                        n += "</tr>";
                        for (var f in g) {
                            n += "<tr><td>" + g[f]["txt"] + '</td><td><input type="checkbox" class="rand" data-id="' + f + '" value="1"' + (g[f]["is_rand"] == 1 ? " checked": "") + " /></td>";
                            if (this._data[j].settings.type != "O") {
                                n += '<td><input type="checkbox" class="other" data-id="' + f + '" value="1"' + (g[f].settings.other == 1 ? " checked": "") + " /></td>"
                            }
                            if (this._data[j].settings.direction == "X") {
                                n += '<td><input type="text" data-id="' + f + '" value="' + (g[f].settings.hasOwnProperty("fixedwidth") ? g[f].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + d + '"/></td>'; ++d
                            }
                            n += "</tr>"
                        }
                    }
                    n += "</table></fieldset>";
                    b += n
                }
            } else {
                if (this._data[j].hasOwnProperty("option")) {
                    b += this._advancedOption(j)
                }
            }
        }
        if (this._data[j].qtype == 3) {
            var h = 0,
            e = "",
            m = "";
            for (var f in g) {++h
            }
            var c = 0,
            l = 0;
            if (this._data[j].settings.min > 0) {
                c = this._data[j].settings.min
            }
            if (this._data[j].settings.max > 0) {
                l = this._data[j].settings.max
            }
            for (var f = 0; f <= h; ++f) {
                e += '<option value="' + f + '"' + (f == c ? " selected": "") + ">" + f + "</option>";
                m += '<option value="' + f + '"' + (f == l ? " selected": "") + ">" + f + "</option>"
            }
            b += '<fieldset>最少选择的答案数<select onchange="SurveyBuild.minSelect()" id="min">' + e + "</select></fieldset>";
            b += '<fieldset>最多选择的答案数<select onchange="SurveyBuild.maxSelect()" id="max">' + m + "</select></fieldset>"
        } else {
            if (this._data[j].qtype == 4 && !this._data[j].settings.hasOwnProperty("type")) {
                b += '<fieldset><p>字数范围</p><p>最少：<input type="text" class="input-mini" id="min" maxlength="5" value="' + this._data[j].settings.min + '">&nbsp;&nbsp;&nbsp;&nbsp;最多：<input type="text" class="input-mini" id="max" maxlength="5" value="' + this._data[j].settings.max + '"></p></fieldset>';
                b += '<fieldset><p>后缀：<input type="text" class="input-mini" id="suffix" value="' + (this._data[j].settings.hasOwnProperty("suffix") ? this._data[j].settings.suffix: "") + '">&nbsp;&nbsp;&nbsp;&nbsp;默认值：<input type="text" id="defaultval" value="' + (this._data[j].settings.hasOwnProperty("defaultval") ? this._data[j].settings.defaultval: "") + '"></p></fieldset>';
                b += '<fieldset><p>使用正则匹配<select id="preg" onchange="SurveyBuild.selectedPreg(this)"><option value="">不使用</option>';
                for (var f in this._preg) {
                    b += '<option value="' + this._preg[f][0] + '"' + (this._data[j].settings.preg == this._preg[f][0] ? ' selected="selected"': "") + ">" + this._preg[f][1] + "</option>"
                }
                b += '</select>&nbsp;&nbsp;<span id="minmaxnum" style="display:' + (this._data[j].settings.preg == "^\\d+$" || this._data[j].settings.preg == "^\\d+(\\.\\d+)?$" ? "inline": "none") + '">最小：<input type="text" class="input-mini" id="mininum" maxlength="9" value="' + (this._data[j].settings.hasOwnProperty("mininum") ? this._data[j].settings.mininum: "") + '">&nbsp;&nbsp;&nbsp;&nbsp;最大：<input type="text" class="input-mini" id="maxinum" maxlength="9" value="' + (this._data[j].settings.hasOwnProperty("maxinum") ? this._data[j].settings.maxinum: "") + '"></span></p></fieldset>'
            } else {
                if (this._data[j].qtype == 8) {
                    var h = 0;
                    for (var f in g) {++h
                    }
                    var k = this._data[j].settings.max != 0 ? this._data[j].settings.max: 0;
                    b += '<fieldset>排序的个数<select id="max"><option value="0">不限制</option>';
                    for (var h; h > 1; --h) {
                        b += '<option value="' + h + '"' + (h == k ? " selected": "") + ">" + h + "</option>"
                    }
                    b += "</select></fieldset>"
                } else {
                    if (this._data[j].qtype == 9) {
                        b += '<fieldset><p>所有答案总和等于：<input type="text" onkeypress="return SurveyBuild.keypressNum(event)" class="input-small" id="total" value="' + this._data[j].settings.total + '" maxlength="11">&nbsp;<span style="color:#f60">注：设置此项后，各项答案的值必须为数字</span></p></fieldset>'
                    } else {
                        if (this._data[j].qtype == 11) {
                            var h = 4,
                            k = "";
                            for (var f = 1; f <= h; ++f) {
                                k += '<option value="' + f + '"' + (f == this._data[j].settings.max ? " selected": "") + ">" + f + "</option>"
                            }
                            b += '<fieldset>要上传的文件数<select id="max">' + k + "</select></fieldset>"
                        }
                    }
                }
            }
        }
        b += '</div><div class="modal-footer clearfix"><button class="btn pull-left" onclick="$.fancybox.close()"><i class="icon-ban-circle"></i>取消</button><button class="btn btn-warning pull-right" onclick="SurveyBuild.saveSettings(\'' + j + '\')"><i class="icon-ok"></i>确定</button></div>';
        $.fancybox.open({
            content: b,
            helpers: {
                overlay: {
                    closeClick: false
                }
            },
            beforeShow: function() {
                $(".popo").popover({
                    trigger: "hover"
                })
            }
        })
    },
    selectedPreg: function(a) {
        var b = $(a).val();
        if (b == "^\\d+$" || b == "^\\d+(\\.\\d+)?$") {
            $("#minmaxnum").css("display", "inline")
        } else {
            $("#minmaxnum input").val("0");
            $("#minmaxnum").css("display", "none")
        }
    },
    changeGG: function(e) {
        var c = parseInt($("#max").val()),
        b = "",
        a = $("#table-advanced-option tr").length - 1;
        if (c > a) {
            for (var d = a + 1; d <= c; ++d) {
                b += '<tr><td style="text-align:center">' + d + '</td><td style="text-align:left"><input type="text" tabindex="' + d + '" value="" /></td></tr>'
            }
            $("#table-advanced-option").find("tbody").append(b)
        } else {
            $("#table-advanced-option tr:gt(" + c + ")").remove()
        }
    },
    changeDirection: function(d) {
        if ($("#direction").prop("checked")) {
            $("#table-advanced-option").find("tr th:last-child,tr td:last-child").remove();
            $("#table-advanced-child").find("tr:first").append('<th style="width:75px">列宽（px）</th>');
            var a = 1,
            e = this._data[d].child;
            for (var b in e) {
                $("#table-advanced-child").find("tr:eq(" + a + ")").append('<td><input type="text" data-id="' + b + '" value="' + (e[b].settings.hasOwnProperty("fixedwidth") ? e[b].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + a + '"/></td>'); ++a
            }
        } else {
            $("#table-advanced-child").find("tr th:last-child,tr td:last-child").remove();
            $("#table-advanced-option").find("tr:first").append('<th style="width:75px">列宽（px）</th>');
            var a = 1,
            c = this._data[d].option;
            for (var b in c) {
                $("#table-advanced-option").find("tr:eq(" + a + ")").append('<td><input type="text" data-id="' + b + '" value="' + (c[b].settings.hasOwnProperty("fixedwidth") ? c[b].settings.fixedwidth: "") + '" onfocus="this.select()" onkeypress="return SurveyBuild.keypressNum(event)" class="input-mini fixedwidth" maxlength="3" tabindex="' + a + '"/></td>'); ++a
            }
        }
    },
    optionBatch: function(e, d) {
        var a = '<div class="modal-header"><h4>批量编辑选项' + (d && this._data[e].settings.type != "G" ? "子问题": "选项") + '<span class="muted">(以回车分隔)</span></h4></div>';
        a += '<div id="modal-question-option" class="modal-body"><textarea id="' + e + '-batch" class="option-text">';
        if (d) {
            var f = this._data[e].child;
            for (var b in f) {
                a += f[b]["question"] + "\n"
            }
        } else {
            var c = this._data[e].option;
            for (var b in c) {
                a += c[b]["txt"] + "\n"
            }
        }
        a += "</textarea></div>";
        a += '<div class="modal-footer clearfix"><button class="btn pull-left" onclick="$.fancybox.close()"><i class="icon-ban-circle"></i>取消</button><button class="btn btn-warning pull-right" onclick="SurveyBuild.optionBatchSave(\'' + e + "'," + d + ')"><i class="icon-ok"></i>确认</button><label class="pull-right checkbox" style="margin-top:15px"><input type="checkbox" id="autocode" checked="checked" />自动编码</label></div>';
        $.fancybox.open({
            content: a,
            helpers: {
                overlay: {
                    closeClick: false
                }
            },
            afterShow: function() {
                var g = $("#" + e + "-batch").val();
                $("#" + e + "-batch").val("").focus().val(g)
            }
        })
    },
    optionBatchSave: function(l, o) {
        this.is_edit = true;
        var m = $.trim($("#" + l + "-batch").val()),
        c = $("#autocode").prop("checked") ? 1 : 0,
        a = m.split("\n"),
        e = a.length,
        n = "A" + ( + new Date()),
        f = 0,
        h = {},
        d = null;
        if (o) {
            var b = this._data[l].child;
            for (var g in b) {
                if (f >= e) {
                    if (!b[g].hasOwnProperty("add")) {
                        this._qid.push(b[g].qid)
                    }
                } else {
                    h[g] = b[g];
                    h[g].edit = 1;
                    h[g].orderby = f + 1;
                    if (c) {
                        h[g].code = f + 1
                    }
                    h[g].question = a[f]
                }++f
            }
            if (e > f) {
                var k = e - f;
                for (var g = 0; g < k; ++g) {
                    h[n + g] = {
                        parent_id: l,
                        add: 1,
                        code: c ? f + 1 : "",
                        question: a[f],
                        orderby: f + 1,
                        is_rand: 0,
                        settings: {
                            mandatory: 1,
                            other: 0,
                            fixedwidth: ""
                        }
                    }; ++f
                }
            }
            this._data[l].child = h
        } else {
            var b = this._data[l].option;
            for (var g in b) {
                if (f >= e) {
                    if (!b[g].hasOwnProperty("add")) {
                        this._oid.push(b[g].oid)
                    }
                } else {
                    h[g] = b[g];
                    h[g].edit = 1;
                    if (c) {
                        h[g].code = f + 1
                    }
                    h[g].txt = a[f]
                }++f
            }
            if (e > f) {
                var k = e - f;
                for (var g = 0; g < k; ++g) {++f;
                    h[n + g] = {
                        qid: l,
                        add: 1,
                        code: c ? f: "",
                        txt: a[f - 1],
                        orderby: f,
                        is_rand: 0
                    };
                    if (this._data[l].qtype == 2) {
                        h[n + g].settings = {
                            other: 0
                        }
                    } else {
                        if (this._data[l].qtype == 3) {
                            h[n + g].settings = {
                                other: 0,
                                exclusive: 0
                            }
                        } else {
                            if (this._data[l].qtype == 5) {
                                if (this._data[l].settings.type == "M") {
                                    h[n + g].settings = {
                                        other: 0,
                                        exclusive: 0,
                                        fixedwidth: ""
                                    }
                                } else {
                                    h[n + g].settings = {
                                        other: 0,
                                        fixedwidth: ""
                                    }
                                }
                            } else {
                                if (this._data[l].qtype == 9) {
                                    h[n + g].settings = {
                                        mandatory: 1,
                                        other: 0,
                                        preg: "",
                                        suffix: ""
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this._data[l].option = h
        }
        $(this._html(l)).find(".question-answer").replaceAll("#q" + l + " .question-answer");
        $("#q" + l).click();
        $.fancybox.close()
    },
    plusChild: function(f) {
        this.is_edit = true;
        var n = $(f).parents("tr").attr("id").split("-"),
        k = n[0],
        h = n[1],
        l = 0,
        o = "A" + ( + new Date()),
        b = {},
        g = 0;
        var d = $("#build-right").height();
        $("#build-right").height(d + 39);
        $(f).parents("tr").after('<tr id="' + k + "-" + o + '"><td><input type="text" value="" class="ccode" oncontextmenu="return false;" maxlength="8" ondragenter="return false" onpaste="return false" onkeyup="SurveyBuild.saveCCode(this)"></td><td><input class="option-txt" value="" onkeyup="SurveyBuild.saveChild(this)" type="text"></td><td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusChild(this)"><i class="icon-plus-sign"></i></a>&nbsp;<a href="javascript:void(0);" onclick="SurveyBuild.minusChild(this)" class="text-warning"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info"><i class="icon-move"></i></a></td></tr>');
        this._data[k].sort = 1;
        var m = "";
        for (var e in this._data[k].child) {++g;
            b[e] = cloneObj(this._data[k].child[e]);
            b[e].orderby = g;
            m += '<option id="first' + e + '" value="' + e + '">' + b[e].question + "</option>";
            if (e == h) {++g;
                b[o] = {
                    add: 1,
                    parent_id: k,
                    code: "",
                    question: "",
                    orderby: g,
                    is_rand: 0,
                    settings: {
                        mandatory: 1,
                        other: 0,
                        fixedwidth: ""
                    }
                };
                m += '<option id="first' + o + '" value="' + o + '" selected></option>'
            }
        }
        this._data[k].child = b;
        if (this._data[k].qtype == 17) {
            $("#selectTitle").html(m);
            var l = 1,
            a = 1;
            for (var e in this._data[k].option) {
                var j = 1 * this._data[k].option[e].code;
                a = 1 + this._data[k].option[e].orderby;
                if (j > l) {
                    l = j
                }
            }
            var p = "o" + o;
            this._data[k].option[p] = {
                qid: k,
                add: 1,
                code: ++l,
                txt: "选项1",
                orderby: a,
                is_rand: 0,
                settings: {
                    other: 0,
                    cid: o
                }
            };
            this._changeTitle(k, o)
        }
        $(this._html(k)).find(".question-answer").replaceAll("#q" + k + " .question-answer")
    },
    minusChild: function(f) {
        this.is_edit = true;
        if ($(f).parents("tr").siblings().length == 0) {
            this._error("至少要有一个子问题")
        } else {
            var e = $(f).parents("tr").attr("id").split("-"),
            g = e[0],
            h = e[1];
            if (this._data[g].child.hasOwnProperty(h)) {
                var b = $("#build-right").height();
                $("#build-right").height(b - 39);
                $(f).parents("tr").remove();
                if (this._data[g].settings.direction == "Y") {
                    var a = $("#q" + g + " tr th#c" + h).index();
                    $("#q" + g + " tr th#c" + h).remove();
                    $("#q" + g + " tr").each(function() {
                        $(this).find("td").eq(a).remove()
                    })
                } else {
                    $("#c" + h).parent().remove()
                }
                if (!this._data[g].child[h].hasOwnProperty("add")) {
                    this._qid.push(this._data[g].child[h].qid)
                }
                delete this._data[g].child[h];
                if (this._data[g].qtype == 17) {
                    $("#first" + h).remove();
                    var d = this._data[g].option;
                    for (var c in d) {
                        if ($option.settings["cid"] == h || "C" + d[c].settings.cid == h) {
                            delete this._data[g].option[c]
                        }
                    }
                }
            } else {
                this._error("非法操作")
            }
        }
    },
    plusOption: function(f) {
        this.is_edit = true;
        var m = $(f).parents("tr").attr("id").split("-"),
        k = m[0],
        b = m[1],
        l = 0,
        n = "A" + ( + new Date()),
        g = {},
        h = 0;
        for (var e in this._data[k].option) {
            var j = 1 * this._data[k].option[e].code;
            if (j > l) {
                l = j
            }
        }
        var a = ++l;
        var d = $("#build-right").height();
        $("#build-right").height(d + 39);
        $(f).parents("tr").after('<tr id="' + k + "-" + n + '"><td><input class="ocode" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="5" value="' + a + '" onkeyup="SurveyBuild.saveOCode(this)" type="text"></td><td><input class="option-txt" value="" onkeyup="SurveyBuild.saveTxt(this)" type="text"></td><td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusOption(this);return false;"><i class="icon-plus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusOption(this);return false;"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i></a></td></tr>');
        this._data[k].sort = 1;
        for (var e in this._data[k].option) {
            h++;
            g[e] = cloneObj(this._data[k].option[e]);
            g[e].orderby = h;
            if (e == b) {++h;
                if (SurveyBuild._data[k]["qtype"] == 2) {
                    g[n] = {
                        qid: k,
                        add: 1,
                        code: a,
                        txt: "",
                        orderby: h,
                        is_rand: 0,
                        settings: {
                            other: 0
                        }
                    }
                } else {
                    if (SurveyBuild._data[k]["qtype"] == 3) {
                        g[n] = {
                            qid: k,
                            add: 1,
                            code: a,
                            txt: "",
                            orderby: h,
                            is_rand: 0,
                            settings: {
                                other: 0,
                                exclusive: 0
                            }
                        }
                    } else {
                        if (SurveyBuild._data[k]["qtype"] == 5) {
                            if (SurveyBuild._data[k]["settings"]["type"] == "M") {
                                g[n] = {
                                    qid: k,
                                    add: 1,
                                    code: a,
                                    txt: "",
                                    orderby: h,
                                    is_rand: 0,
                                    settings: {
                                        other: 0,
                                        exclusive: 0,
                                        fixedwidth: ""
                                    }
                                }
                            } else {
                                g[n] = {
                                    qid: k,
                                    add: 1,
                                    code: a,
                                    txt: "",
                                    orderby: h,
                                    is_rand: 0,
                                    settings: {
                                        other: 0,
                                        fixedwidth: ""
                                    }
                                }
                            }
                        } else {
                            if (SurveyBuild._data[k]["qtype"] == 9) {
                                g[n] = {
                                    qid: k,
                                    add: 1,
                                    code: a,
                                    txt: "",
                                    orderby: h,
                                    is_rand: 0,
                                    settings: {
                                        mandatory: 1,
                                        other: 0,
                                        preg: "",
                                        suffix: ""
                                    }
                                }
                            } else {
                                if (SurveyBuild._data[k]["qtype"] == 17) {
                                    g[n] = {
                                        qid: k,
                                        add: 1,
                                        code: a,
                                        txt: "",
                                        orderby: h,
                                        is_rand: 0,
                                        settings: {
                                            other: 0,
                                            cid: $("#selectTitle").val()
                                        }
                                    }
                                } else {
                                    g[n] = {
                                        qid: k,
                                        add: 1,
                                        code: a,
                                        txt: "",
                                        orderby: h,
                                        is_rand: 0
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this._data[k].option = g;
        $(this._html(k)).find(".question-answer").replaceAll("#q" + k + " .question-answer");
    },
    minusOption: function(e) {
        this.is_edit = true;
        if ($(e).parents("tr").siblings().length == 0) {
            this._error("至少要有一个选项")
        } else {
            var c = $(e).parents("tr").attr("id").split("-"),
            f = c[0],
            d = c[1];
            if (this._data[f].option.hasOwnProperty(d)) {
                var b = $("#build-right").height();
                $("#build-right").height(b - 39);
                $(e).parents("tr").remove();
                if (this._data[f].qtype == "5") {
                    if (this._data[f].settings.direction == "Y") {
                        $("#o" + d).parent().remove()
                    } else {
                        var a = $("#q" + f + " tr td#o" + d).index();
                        $("#q" + f + " tr").each(function() {
                            $(this).find("td").eq(a).remove()
                        })
                    }
                } else {
                    $("#o" + d).remove()
                }
                if (!this._data[f].option[d].hasOwnProperty("add")) {
                    this._oid.push(this._data[f].option[d].oid)
                }
                delete this._data[f].option[d]
            } else {
                this._error("非法操作")
            }
        }
    },
    saveTitle: function(a) {
        this.is_edit = true;
        var c = $(a).attr("data-id"),
        b = $(a).val();
        this._data[c].edit = 1;
        this._data[c].settings.title = b;
        $("#q" + c).find(".title").html(b)
    },
    saveSubTitle: function(a) {
        this.is_edit = true;
        var c = $(a).attr("data-id"),
        b = $(a).val();
        this._data[c].edit = 1;
        this._data[c].settings.subtitle = b;
        $("#q" + c).find(".subtitle").html(b)
    },
    changeTitle: function(a) {
        var c = $(a).attr("data-id"),
        b = $(a).val();
        this._changeTitle(c, b)
    },
    _changeTitle: function(e, d) {
        this.is_edit = true;
        this._data[e].edit = 1;
        var c = this._data[e].option,
        b = "";
        for (var a in c) {
            if (c[a].settings.cid == d || "C" + c[a].settings.cid == d) {
                b += '<tr id="' + e + "-" + a + '"><td><input type="text" class="ocode" onpaste="return false" ondragenter="return false" oncontextmenu="return false;" maxlength="5" value="' + c[a]["code"] + '" onkeyup="SurveyBuild.saveOCode(this)"/></td><td><input type="text" class="option-txt" value="' + htmlentities(c[a]["txt"]) + '" onkeyup="SurveyBuild.saveTxt(this)" /></td><td><a href="javascript:void(0);" class="text-success" onclick="SurveyBuild.plusOption(this);return false;"><i class="icon-plus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-warning" onclick="SurveyBuild.minusOption(this);return false;"><i class="icon-minus-sign"></i></a>&nbsp;<a href="javascript:void(0);" class="text-info option-move"><i class="icon-move"></i></a></td></tr>'
            }
        }
        $("#" + e + "-sub").html(b)
    },
    /*修改问题后，自动保存*/
    saveQuestion: function(a) {
        this.is_edit = true;
        var c = $(a).attr("id"),
        b = $(a).val();
        this._data[c].edit = 1;
        this._data[c].question = b;
        b = b.replace(/<script/ig, "&lt;script").replace(/<\/script>/ig, "&lt;script&gt;");
        $("#q" + c).find(".question-question").html(b)
    },
    saveTabName: function(a) {
        this.is_edit = true;
        var c = $(a).attr("id").substr(2),
        b = $(a).val();
        this._data[c].edit = 1;
        this._data[c].question = b;
        $("#tab_" + c).html(b);
        $("#q" + c).find("span").text(b);
    },
    saveFormat: function(a) {
        this.is_edit = true;
        var c = $(a).attr("data-id"),
        b = $(a).val();
        this._data[c].edit = 1;
        this._data[c].settings.format = b;
        $("#q" + c).find(".format").removeClass().addClass("format format" + b)
    },
    saveSliderMax: function(a, c) {
        this.is_edit = true;
        this._data[c].edit = 1;
        var b = $(a).val();
        this._data[c].settings.max = b;
        $("#q" + c + " .slider-max").text(Math.ceil(b * 0.2))
    },
    saveSliderStart: function(a, c) {
        this.is_edit = true;
        this._data[c].edit = 1;
        var b = $(a).val();
        this._data[c].settings.start = b;
        $("#q" + c + " .slider-start").text(b)
    },
    saveSliderEnd: function(a, c) {
        this.is_edit = true;
        this._data[c].edit = 1;
        var b = $(a).val();
        this._data[c].settings.end = b;
        $("#q" + c + " .slider-end").text(b)
    },
    checkQuestion: function() {
        if ($("#question-box>li.active").length == 0) {
            return true
        }
        var l = $("#question-box>li.active").attr("id").substr(1),
        b = this._data[l].code,
        j = true,
        a,
        m;
        if (this._data[l].qtype >= 90) {
            return true
        }
        if (b == "") {
            j = false;
            a = "p" + l;
            m = "题号不能为空"
        } else {
            if (/[\u4E00-\u9FA5]/i.test(b)) {
                j = false;
                a = "p" + l;
                m = "题号不能输入中文"
            } else {
                if (!/^[a-zA-Z]/i.test(b)) {
                    j = false;
                    a = "p" + l;
                    m = "题号必需以字母开头"
                } else {
                    if (this._data[l].question == "") {
                        j = false;
                        a = l;
                        m = "问题不能为空"
                    } else {
                        for (var g in this._data) {
                            if (g != l && this._data[g].qtype < 90 && this._data[g].code.toString().toUpperCase() == b.toString().toUpperCase()) {
                                j = false;
                                a = "p" + l;
                                m = "题号已存在";
                                break
                            }
                        }
                        if (j && this._data[l].hasOwnProperty("child")) {
                            var e = this._data[l].child,
                            f = null;
                            for (var k in e) {
                                if (e[k].code == "") {
                                    j = false;
                                    m = "编码不能为空"
                                } else {
                                    if (e[k].question == "") {
                                        j = false;
                                        m = this._data[l].settings.type == "G" || this._data[l].qtype == 17 ? "选项不能为空": "子问题不能为空"
                                    } else {
                                        if (/[\u4E00-\u9FA5]/i.test(e[k].code)) {
                                            j = false;
                                            m = "编码不能输入中文"
                                        }
                                    }
                                }
                                for (var n in e) {
                                    if (k != n && e[k].code.toString().toUpperCase() == e[n].code.toString().toUpperCase()) {
                                        j = false;
                                        m = this._data[l].settings.type == "G" || this._data[l].qtype == 17 ? "选项编码重复": "子问题编码重复";
                                        k = n;
                                        break
                                    }
                                }
                                if (!j) {
                                    a = l + "-" + k;
                                    break
                                }
                            }
                        }
                        if (j && this._data[l].hasOwnProperty("option")) {
                            var h = this._data[l].option,
                            f = null;
                            for (var d in h) {
                                if (h[d].code == "") {
                                    j = false;
                                    m = "编码不能为空"
                                } else {
                                    if (this._data[l].qtype != 10 && (this._data[l].qtype != 5 || (this._data[l].qtype == 5 && this._data[l].settings.type != "G")) && h[d].txt == "") {
                                        j = false;
                                        m = "选项不能为空"
                                    } else {
                                        if (/[\u4E00-\u9FA5]/i.test(h[d].code)) {
                                            j = false;
                                            m = "编码不能输入中文"
                                        }
                                    }
                                }
                                for (var n in h) {
                                    if (d != n && h[d].code == h[n].code) {
                                        j = false;
                                        m = "选项编码重复";
                                        d = n;
                                        break
                                    }
                                }
                                if (!j) {
                                    a = l + "-" + d;
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }
        if ($("#" + a).length == 0) {
            j = true
        }
        if (!j) {
            this._fail(a, m)
        }
        return j
    },
    saveQCode: function(a) {
        this.is_edit = true;
        var b = $(a).attr("id").substr(1);
        this._data[b].edit = 1;
        this._data[b].code = $(a).val();
        $("#q" + b + " b.question-code").text(this._data[b].code + ".")
    },
    saveCCode: function(b) {
        this.is_edit = true;
        var a = $(b).parents("tr").attr("id").split("-"),
        c = a[0],
        d = a[1];
        this._data[c].child[d].edit = 1;
        this._data[c].child[d].code = $(b).val()
    },
    saveOCode: function(c) {
        this.is_edit = true;
        var a = $(c).parents("tr").attr("id").split("-"),
        d = a[0],
        b = a[1];
        this._data[d].option[b].edit = 1;
        this._data[d].option[b].code = $(c).val()
    },
    saveTxt: function(c) {
        this.is_edit = true;
        var a = $(c).parents("tr").attr("id").split("-"),
        e = a[0],
        b = a[1],
        d = $(c).val();
        this._data[e].option[b].edit = 1;
        this._data[e].option[b].txt = d;
        d = d.replace(/<script/ig, "&lt;script").replace(/<\/script>/ig, "&lt;script&gt;");
        if (this._data[e].option[b].hasOwnProperty("settings") && this._data[e].option[b].settings.other == 1) {
            d += '<b class="read-input"></b>'
        }
        if (this._data[e].qtype != 10) {
            $("#o" + b).html(d)
        }
    },
    saveChild: function(b) {
        this.is_edit = true;
        var a = $(b).parents("tr").attr("id").split("-"),
        d = a[0],
        e = a[1],
        c = $(b).val();
        this._data[d].child[e].edit = 1;
        this._data[d].child[e].question = c;
        if (this._data[d].qtype == 17) {
            $("#first" + e).text(c)
        }
        c = c.replace(/<script/ig, "&lt;script").replace(/<\/script>/ig, "&lt;script&gt;");
        if (this._data[d].child[e].hasOwnProperty("settings") && this._data[d].child[e].settings.other == 1) {
            c += '<b class="read-input"></b>'
        }
        $("#c" + e).html(c)
    },
    saveTimer: function(d) {
        var c = $("#timertime" + d).val(),
        a = c == "" ? 0 : parseInt(c),
        b;
        this._data[d].settings.time = a;
        if ($("#timerauto" + d).prop("checked")) {
            this._data[d].settings.auto = 1;
            b = "页面将在<span>" + a + "</span>秒后自动提交"
        } else {
            this._data[d].settings.auto = 0;
            b = "页面将在<span>" + a + "</span>秒后显示下一页按钮"
        }
        this._data[d].edit = 1;
        this._data[d].question = b;
        $("#q" + d).find(".question-question").html(b)
    },
    saveColor: function(c) {
        this.is_edit = true;
        var h = $(c).attr("id").split("-"),
        g = h[0],
        b = h[1],
        a = $(c).val(),
        f = 0,
        d = {};
        if ($(c).prop("checked")) {
            var e = $(c).parents("li").prevAll().find(":checked").last();
            var i;
            if (e.length == 0) {
                i = 0
            } else {
                i = e.attr("id").split("-")[1]
            }
            if (i == 0) {++f;
                d[b] = {
                    qid: g,
                    add: 1,
                    code: 0,
                    txt: a,
                    orderby: f,
                    is_rand: 0
                }
            }
            $.each(this._data[g].option,
            function(l, j) {++f;
                j.orderby = f;
                d[l] = j;
                if (l == i) {++f;
                    d[b] = {
                        qid: g,
                        add: 1,
                        code: 0,
                        txt: a,
                        orderby: f,
                        is_rand: 0
                    }
                }
            });
            this._data[g].option = d;
            $(this._html(g)).find(".question-answer").replaceAll("#q" + g + " .question-answer")
        } else {
            $("#o" + b).hide();
            if (!this._data[g].option[b].hasOwnProperty("add")) {
                this._oid.push(this._data[g].option[b].oid)
            }
            delete this._data[g].option[b]
        }
    },
    saveSettings: function(g) {
        this.is_edit = true;
        var m = this._data[g].qtype;
        this._data[g].edit = 1;
        this._data[g].is_mandatory = $("#is_mandatory").prop("checked") ? 1 : 0;
        this._data[g].is_display = $("#is_display").prop("checked") ? 1 : 0;
        $("input.rand").each(function() {
            var i = $(this).attr("data-id");
            SurveyBuild._data[g].option[i].edit = 1;
            SurveyBuild._data[g].option[i].is_rand = $(this).prop("checked") ? 1 : 0
        });
        $("input.exclusive").each(function() {
            var i = $(this).attr("data-id");
            SurveyBuild._data[g].option[i].settings.exclusive = $(this).prop("checked") ? 1 : 0
        });
        if (this._data[g].hasOwnProperty("settings")) {
            if (this._data[g].settings.hasOwnProperty("min")) {
                this._data[g].settings.min = $("#min").val()
            }
            if (this._data[g].settings.hasOwnProperty("max")) {
                var h = $("#max").val(),
                e = parseInt(this._data[g].settings.max);
                h = h ? parseInt(h) : 0;
                this._data[g].settings.max = h;
                if (m == 8) {
                    var a = "";
                    if (h == 0) {
                        for (var c in this._data[g].option) {++h
                        }
                    }
                    for (var c = 1; c <= h; ++c) {
                        a += "<p>" + c + "</p>"
                    }
                    $("#q" + g + " .sort-right").html(a)
                } else {
                    if (m == 5) {
                        var l = "A" + ( + new Date());
                        if (h > e) {
                            var k = h - e;
                            for (var c = 1; c <= k; ++c) {
                                this._data[g].option[l + c] = {
                                    qid: g,
                                    add: 1,
                                    code: e + c,
                                    txt: "",
                                    orderby: e + c,
                                    is_rand: 0,
                                    settings: {
                                        other: 0,
                                        fixedwidth: ""
                                    }
                                }
                            }
                        } else {
                            if (h < e) {
                                var b = 1;
                                for (var c in this._data[g].option) {
                                    if (b > h) {
                                        if (!this._data[g].option[c].hasOwnProperty("add")) {
                                            this._oid.push(this._data[g].option[c].oid)
                                        }
                                        delete this._data[g].option[c]
                                    }++b
                                }
                            }
                        }
                        for (var c in this._data[g].option) {
                            if (!this._data[g].option[c].hasOwnProperty("add")) {
                                this._data[g].option[c].edit = 1
                            }
                            this._data[g].option[c].txt = $("#table-advanced-option input:text:eq(" + (this._data[g].option[c].orderby - 1) + ")").val()
                        }
                    }
                }
            }
        }
        if (m == 4) {
            var n = $("#suffix").val();
            this._data[g].settings.suffix = n;
            this._data[g].settings.preg = $("#preg").val();
            this._data[g].settings.mininum = $("#mininum").val();
            this._data[g].settings.maxinum = $("#maxinum").val();
            this._data[g].settings.defaultval = $("#defaultval").val();
            $("#q" + g + " .suffix").html(n)
        } else {
            if (m == 5) {
                $("input.mandatory").each(function() {
                    var i = $(this).attr("data-id");
                    SurveyBuild._data[g].child[i].settings.mandatory = $(this).prop("checked") ? 1 : 0
                });
                $("input.crand").each(function() {
                    var i = $(this).attr("data-id");
                    SurveyBuild._data[g].child[i].edit = 1;
                    SurveyBuild._data[g].child[i].is_rand = $(this).prop("checked") ? 1 : 0
                });
                $("input.cother").each(function() {
                    var i = $(this).attr("data-id");
                    if ($(this).prop("checked")) {
                        SurveyBuild._data[g]["child"][i].settings.other = 1;
                        $("#c" + i).html(SurveyBuild._data[g]["child"][i].question + '<b class="read-input"></b>')
                    } else {
                        SurveyBuild._data[g]["child"][i].settings.other = 0;
                        $("#c" + i + " .read-input").remove()
                    }
                });
                $("input.other").each(function() {
                    var i = $(this).attr("data-id");
                    if ($(this).prop("checked")) {
                        SurveyBuild._data[g].option[i].settings.other = 1;
                        $("#o" + i).html(SurveyBuild._data[g].option[i]["txt"] + '<b class="read-input"></b>')
                    } else {
                        SurveyBuild._data[g].option[i].settings.other = 0;
                        $("#o" + i + " .read-input").remove()
                    }
                });
                if (this._data[g].settings.type != "G") {
                    this._data[g].settings.fixed = $("#fixed").prop("checked") ? 1 : 0;
                    this._data[g].settings.direction = $("#direction").prop("checked") ? "Y": "X";
                    if (this._data[g].settings.direction == "Y") {
                        $("input.fixedwidth").each(function() {
                            var j = $(this).attr("data-id"),
                            i = $(this).val();
                            SurveyBuild._data[g].child[j].settings.fixedwidth = i
                        })
                    } else {
                        $("input.fixedwidth").each(function() {
                            var i = $(this).attr("data-id"),
                            j = $(this).val();
                            SurveyBuild._data[g].option[i].settings.fixedwidth = j
                        })
                    }
                    if (this._data[g].settings.type == "S") {
                        this._data[g].settings.mean = $("#mean").prop("checked") ? 1 : 0
                    }
                }
                $("#q" + g).html(this._html(g))
            } else {
                if (m == 17) {
                    $("input.crand").each(function() {
                        var i = $(this).attr("data-id");
                        SurveyBuild._data[g].child[i].edit = 1;
                        SurveyBuild._data[g].child[i].is_rand = $(this).prop("checked") ? 1 : 0
                    });
                    $("input.other").each(function() {
                        var i = $(this).attr("data-id");
                        SurveyBuild._data[g].option[i].settings.other = $(this).prop("checked") ? 1 : 0
                    })
                } else {
                    $("input.other").each(function() {
                        var i = $(this).attr("data-id");
                        if ($(this).prop("checked")) {
                            SurveyBuild._data[g].option[i].settings.other = 1;
                            $("#o" + i).html(SurveyBuild._data[g].option[i]["txt"] + '<b class="read-input"></b>')
                        } else {
                            SurveyBuild._data[g].option[i].settings.other = 0;
                            $("#o" + i + " .read-input").remove()
                        }
                    });
                    if (m <= 3) {
                        var d = [];
                        $("input.defaultval:checked").each(function() {
                            var i = $(this).attr("data-id");
                            d.push(SurveyBuild._data[g].option[i].code)
                        });
                        this._data[g].settings.defaultval = d;
                        if (m == 3) {
                            var f = "";
                            if (this._data[g].settings.min != 0 && this._data[g].settings.min == this._data[g].settings.max) {
                                f = '<span class="question-select">[限选' + this._data[g].settings.min + "项]</span>"
                            } else {
                                if (this._data[g].settings.min > 1 && this._data[g].settings.max > 0) {
                                    f = '<span class="question-select">[限选' + this._data[g].settings.min + " - " + this._data[g].settings.max + "项]</span>"
                                } else {
                                    if (this._data[g].settings.min > 1) {
                                        f = '<span class="question-select">[最少选' + this._data[g].settings.min + "项]</span>"
                                    } else {
                                        if (this._data[g].settings.max > 0) {
                                            f = '<span class="question-select">[最多选' + this._data[g].settings.max + "项]</span>"
                                        }
                                    }
                                }
                            }
                            this._data[g].question = this._data[g].question.replace(/<span\s+class="question-select">.*<\/span>/, "");
                            this._data[g].question += f;
                            $("#q" + g + " .question-question").html(this._data[g].question);
                            $("#" + g).val(this._data[g].question)
                        }
                    }
                    if (m == 9) {
                        $("input.mandatory").each(function() {
                            var i = $(this).attr("data-id");
                            SurveyBuild._data[g].option[i].settings.mandatory = $(this).prop("checked") ? 1 : 0
                        });
                        $("select[name=preg]").each(function() {
                            var i = $(this).attr("data-id");
                            SurveyBuild._data[g].option[i].settings.preg = $(this).val()
                        });
                        $("input.suffix").each(function() {
                            var i = $(this).attr("data-id"),
                            j = $(this).val();
                            SurveyBuild._data[g].option[i].settings.suffix = j;
                            $("#o" + i).siblings(".suffix").html(j)
                        });
                        var o = parseFloat($("#total").val());
                        this._data[g].settings.total = isNaN(o) ? "": o
                    }
                }
            }
        }
        $.fancybox.close()
    },
    saveStype: function(a) {
        this.is_edit = true;
        var c = $(a).attr("data-id"),
        b = $(a).val();
        this._data[c].edit = 1;
        this._data[c].settings.type = b
    },
    saveBuild: function() {
        if (!this.checkQuestion()) {
            return
        }
        var a = {},
        b = true,
        d = 100,
        c = 0;
        $("#question-box>li").each(function(f) {
            var g = $(this),
            h = g.attr("id").substr(1),
            e = SurveyBuild._data[h].qtype;
            /*console.log(h);*/
            e == 100 && ++c;
            if (d == 100 && e == 100) {
                SurveyBuild._fail("q" + h, "第" + c + "页不能没有问题", "top");
                b = false;
                return false
            } else {
                d = e;
                a[h] = SurveyBuild._data[h]
            }
        });
        /*console.log(JSON.stringify(a));*/
        if (b) {
            loading($("#build-box"));
            $("#build-save button").text("保存中...");
            $.ajax({
                type: "POST",
                url: "http://202.120.24.169:8050/psc/ACEMDEV/EMPLOYEE/CRM/s/WEBLIB_SURVEY.TZ_EXAM_TPL.FieldFormula.IScript_createQuestionTpl",
                data: {
                    op: "save",
                    sid: SurveyBuild._sid,
                    data: $.toJSON(a),
                    oid: SurveyBuild._oid,
                    qid: SurveyBuild._qid
                },
                dataType: "JSON",
                success: function(f) {
                    loaded($("#build-box"));
                    $("#build-save button").html('保存<span style="font-size:12px;">(建议10分钟一次)</span>');
                    if (f.code == 0) {
                        noteing("保存成功");
                        /*console.log(f.content);*/
                        SurveyBuild.is_edit = false;
                        var e = $("#question-box>li.active").index() - 1;
                        SurveyBuild._data = f.content;
                        $("#question-box>li").remove();
                        SurveyBuild._load(true);
                        if (e >= 0) {
                            var g = $("#question-box>li").eq(e).addClass("active").attr("id").substr(1);
                            $("#question-edit").html(SurveyBuild._edit(g));
                            SurveyBuild._optionMove()
                        }
                    } else {
                        noteing("保存失败", 2)
                    }
                }
            });
            return true
        }
    },
    preview: function(b) {
        /*console.log(this.is_edit);*/
        if (this.is_edit) {
            var a = '<div class="modal-header"><h4>提示</h4></div>';
            a += '<div id="modal-confirm" class="modal-body">当前问卷有未保存的操作，预览前请保存</div>';
            a += "<div class=\"modal-footer clearfix\"><button class=\"btn btn-info pull-left\" onclick=\"$('#modal-confirm').html('问题保存中...');if(SurveyBuild.saveBuild()){$.fancybox.close();window.open('/pc/p/" + b + '\');}"><i class="icon-ok"></i>保存并访问</button><button class="btn btn-warning pull-right" onclick="$.fancybox.close();window.open(\'/pc/p/' + b + '\');"><i class="icon-external-link"></i>继续访问</button></div>';
            $.fancybox.open({
                content: a,
                helpers: {
                    overlay: {
                        closeClick: false
                    }
                }
            })
        } else {
            window.open("/pc/p/" + b)
        }
    },
    init: function(a) {
        $("#question-edit").on("keydown", "input:text",
        function(d) {
            if (d.which == 9) {
                var b = $(":text").length,
                c = $(":text").index(this);
                c = ++c == b ? 0 : c;
                $(":text").eq(c).focus().select();
                return false
            }
        });
        $("#question-type-box li.move").draggable({
            connectToSortable: "#question-box",
            addClasses: false,
            appendTo: "#build-box",
            snapTolerance: 300,
            revert: "invalid",
            helper: function() {
                return $('<div class="draggable-holder">' + $(this).html() + "</div>")
            }
        });
        $("#question-box").sortable({
            cursor: "move",
            placeholder: "place-holder",
            axis: "y",
            opacity: 0.6,
            revert: true,
            cancel: "#question-new",
            receive: function(f, d) {
                var b = -1;
                $("#question-box>li").each(function(e) {
                    if ($(this).attr("data-id")) {
                        b = e;
                        $(this).remove();
                        return
                    }
                });
                var c = SurveyBuild.add(d.item.attr("data-id"), 1);
                if (b > 0) {
                    $(c).insertAfter($("#question-box>li").eq(b - 1)).click()
                } else {
                    $(c).prependTo($("#question-box")).click()
                }
            },
            update: function(c, b) {
                SurveyBuild.sort()
            }
        });
        window.onbeforeunload = function() {
            if (SurveyBuild.is_edit) {
                return "当前页面有未保存内容\n选择“离开页面” 会丢失当前编辑的内容\n选择“留在此面” 可以继续编辑问题然后保存离开"
            }
        };
        $("#question-edit").on("keypress", ".qcode,.ccode",
        function(c) {
            var b = c.which;
            return b == 8 || b == 127 || b == 0 || b == 95 || (b >= 48 && b <= 57) || (b >= 65 && b <= 90) || (b >= 97 && b <= 122)
        });
        $("#question-edit").on("keypress", ".ocode,.timertime",
        function(c) {
            var b = c.which;
            return b == 8 || b == 127 || b == 0 || b >= 48 && b <= 57
        });
        SurveyBuild._sid = a;
        loading($("#question-wrap"));
        $.post("http://202.120.24.169:8050/psc/ACEMDEV/EMPLOYEE/CRM/s/WEBLIB_SURVEY.TZ_EXAM_TPL.FieldFormula.IScript_InitTpl", {
            op: "load",
            sid: a
        },
        function(b) {
            SurveyBuild._data = b.content;
            SurveyBuild._load();
            loaded($("#question-wrap"))
        },
        "JSON")
    },
    _load: function(b) {
        var a = "",
        d = false;
        this._count = 0;
        for (var c in this._data) {
            this._data[c]["qtype"] > 90 || ++this._count;
            $("#question-box").append(this._html(c));
            d = true
        }

        if (!d) {
            $("#question-new").show()
        } else {
            if (!b) {
                /*$("#question-box>li:eq(0)").click()*/
            }
        }
        this._initTab();
    },
    _initTab: function() {
        var tabs = "",
        i = 0,
        targetid = "";
        $("#tabNav").empty();
        $("#question-box>li").each(function(f) {
            var g = $(this),
            h = g.attr("id").substr(1),
            e = SurveyBuild._data[h].qtype;
            if (e == 100) {
                tabs += '<div id="tab_' + h + '" class="' + (i == 0 ? "tabNav_c": "tabNav") + '" onclick="SurveyBuild.pageTo(this.id,\'' + targetid + '\');return false;">' + SurveyBuild._data[h].question + '</div>';
                targetid = h; ++i;
            }
        });
        $("#tabNav").append(tabs);
    },
    pageTo: function(oid, h) {
        if (h.length == 0) {
            window.scrollTo(0, 0);
            $("#" + oid).siblings().removeClass("tabNav_c").addClass("tabNav");
            $("#" + oid).addClass("tabNav_c");
        } else {
            window.scrollTo(0, $("#q" + h).offset().top);
            $("#" + oid).siblings().removeClass("tabNav_c").addClass("tabNav");
            $("#" + oid).addClass("tabNav_c");
        }
        return false;
    },
    _fail: function(d, b, a) {
        var c = $("#" + d);
        if (a == undefined) {
            a = "right"
        }
        $("html,body").animate({
            scrollTop: c.offset().top - 200
        },
        200,
        function() {
            c.popover({
                content: b,
                placement: a
            }).popover("show");
            c.effect("highlight");
            c.focus();
            setTimeout(function() {
                c.popover("destroy")
            },
            1500)
        })
    },
    _error: function(a) {
        alert(a)
    }
};
function upload_call(json) {
    var obj = eval(json);
    if (obj.code > 0) {
        noteing("上传失败 ERROR:" + obj.code, 2)
    } else {
        $("#modal-question-upload .upload-gallery").prepend('<a class="thumbnail" href="javascript:void(0);"><img src="' + obj.url + '" /></a>');
        $("#" + obj.qid + "-file").val("")
    }
    loaded($("#modal-question-upload"))
}
function upload_relation(d, f, a) {
    for (var e in SurveyBuild._data[d].child) {
        SurveyBuild._qid.push(SurveyBuild._data[d].child[e].qid)
    }
    for (var b in SurveyBuild._data[d].option) {
        SurveyBuild._oid.push(SurveyBuild._data[d].option[b].oid)
    }
    SurveyBuild._data[d].child = f;
    SurveyBuild._data[d].option = a;
    loaded($("#build-box"));
    $("#q" + d).click()
};