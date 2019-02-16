(function($, window, undefined) {
$(function () {
    if ($.fn.iCheck !== undefined) {
        $("input[type=checkbox].form-control:not(.noicheck),input[type=radio].form-control:not(.noicheck)").each(function() {
            $(this).iCheck({
                checkboxClass: "icheckbox_" + ($(this).data("style") || "minimal-grey"),
                radioClass: "iradio_" + ($(this).data("style") || "minimal-grey")
            }).on("ifChanged", function() {
                try {
                    $(this).valid()
                } catch (e) {}
            })
        })
    }
    if ($.fn.select2 !== undefined) {
        $("select.form-control:not(.noselect2)").each(function() {
            $(this).select2().on("change", function() {
                try {
                    $(this).valid()
                } catch (e) {}
            })
        })
    }
    if($.fn.datepicker != undefined){
        //Date picker
        $('.datepicker').each(function () {
            $(this).datepicker({
                autoclose: true
            });
        });
    }
});
var bet = {
    /**
     * toastr提示框
     * @param title
     * @param msg
     * @param success
     */
    showMessage: function (message, title,type) {
        toastr = parent.toastr || toastr ;
        if (toastr) {
                toastr.options = {
                closeButton: true,
                positionClass: "toast-bottom-right",
                timeOut: 5000
            };
            toastr[type?type:"info"](message, title);
        }
    },
    showErrorMessage: function (message, title) {
        bet.showMessage(title, message,"error");
    },
    showWarningMessage: function(message) {
        bet.showMessage("警告信息", message, "warning")
    },
    //添加页面
    addTabPage: function (title, url,id,close) {
        if(undefined == close){
            close=true;
        }
        parent.addTabs({title: title,id:id,close: close, url: url})
    },
    //关闭当前页面
    closeTab: function () {
        parent.closeCurrentTab();
    },
    //刷新页面中
    refreshTabTitle:function(title) {
        parent.refreshTabByTitle(title);
    },
    layer: function() {
        try {
            if (top.layer) {
                return top.layer
            }
            if (parent.parent.layer) {
                return parent.parent.layer
            }
            if (parent.layer) {
                return parent.layer
            }
            if(layer){
                return layer
            }
        } catch (e) {}
        if (window.layer) {
            return layer
        }
        return null
    }(),
    //加载中
    loading:function (message, ignoreMessageIfExists) {
        var App = parent.App;
        App.blockUI({
            target: '#tab-content',
            boxed: true,
            message: '加载中......'//,
            // animate: true
        });
    },
    //关闭所有的加载层
    closeLoading:function (timeout, forceClose) {
        var App = parent.App;
        App.unblockUI('#tab-content');//解锁界面
    },
    //询问框
    confirm: function(message, urlOrFun, data, callback, dataType, async, loadingMessage) {
        if (typeof data == "function") {
            loadingMessage = async;
            async = dataType;
            dataType = callback;
            callback = data;
            data = undefined
        }
        var sendAjax = function() {
            bet.loading(loadingMessage == undefined ? "loading....." : loadingMessage);
            $.ajax({
                type: "POST",
                url: urlOrFun,
                data: data,
                dataType: dataType == undefined ? "json" : dataType,
                async: async ==undefined ? true : async,
                error: function(data) {
                    bet.showErrorMessage(data.responseText);
                    bet.closeLoading()
                },
                success: function(data) {
                    if (typeof callback == "function") {
                        callback(data)
                    }
                    bet.closeLoading()
                }
            })
        };
        if (!bet.layer) {
            if (confirm(message)) {
                if (typeof urlOrFun == "function") {
                    urlOrFun()
                } else {
                    sendAjax()
                }
            }
            return
        }
        var options = {
            icon: 3
        };
        if (options.shadeClose == undefined) {
            options.shadeClose = true
        }
        bet.layer.confirm(message, options, function(index) {
            if (typeof urlOrFun == "function") {
                urlOrFun()
            } else {
                sendAjax()
            }
            bet.layer.close(index)
        });
        return false
    },
    /**
     * ajax提交
     * @param url 提交url
     * @param data 数据
     * @param callback 成功后方法
     * @param dataType 提交类型
     * @param async
     * @param message
     */
    ajax: function(url, data, callback, dataType, async, message) {
        bet.loading(message == undefined ? "信息提交中，请稍后..." : message);
        if (typeof data == "function") {
            message = async;
            async = dataType;
            dataType = callback;
            callback = data;
            data = undefined
        }
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            dataType: dataType == undefined ? "json" : dataType,
            async: async ==undefined ? true : async,
            error: function(data) {
                $(".btn").attr("disabled", false);
                bet.showErrorMessage(data.message);
                bet.closeLoading(0, true)
            },
            success: function(data, status, xhr) {
                $(".btn").attr("disabled", false);
                bet.closeLoading();
                if (typeof callback == "function") {
                    callback(data, status, xhr)
                } else {
                    console.log(data)
                }
            }
        })
    },
    //字典翻译
    transDictLabel: function (dictList, val, defVal) {
        var label = "";
        for (var i = 0; i < dictList.length; i++) {
            var dict = dictList[i];
            if (val == dict.dictValue) {
                label = dict.dictLabel;
                break;
            }
        }
        return label == "" ? defVal : label;
    },
    renderSize: function(value) {
        if (null == value || value == "") {
            return "0 Bytes";
        }
        var unitArr = new Array("Bytes","KB","MB","GB","TB","PB","EB","ZB","YB");
        var index = 0, srcsize = parseFloat(value);
        index = Math.floor(Math.log(srcsize) / Math.log(1024));
        var size = srcsize / Math.pow(1024, index);
        //  保留的小数位数
        size = size.toFixed(2);
        return size + unitArr[index];
    },
}
    window.bet = bet;
})(window.jQuery, window);