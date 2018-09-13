//    调用编辑器
var testEditor;
var username=$("#catalogName").attr("data-username");
$(function() {
    testEditor = editormd("test-editormd", {
        width   : "100%",
        height  : 700,
        syncScrolling : "single",
        path    : "../../editormd/lib/"
    });
    $("#submitBt").on('click', function(){
        var title=$("input[name='title']").val();
        var content=$("textarea[name='content']").val();
        title = $.trim(title);
        content = $.trim(content);
        if (title!=null&&title!=""){
            if (content!=null&&content!=""){
                getCatalog();
                $('#myModal').modal('show');
            } else {
                bootoast({
                    message: "文章内容不能为空",
                    type: 'danger',
                    position:'bottom',
                    timeout:2
                });
            }
        } else {
            bootoast({
                message: "文章标题不能为空",
                type: 'danger',
                position:'bottom',
                timeout:2
            });
        }
    })

    $("#catalogBt").on('click', function(){
        var catalogName=$("#catalogName").val();
        catalogName = $.trim(catalogName);
        if (catalogName!=null&&catalogName!=""){
            // 获取 CSRF Token
            var csrfToken = $("meta[name='_csrf']").attr("content");
            var csrfHeader = $("meta[name='_csrf_header']").attr("content");

            $.ajax({
                url: '/catalogs/add',
                type: 'POST',
                contentType: "application/json; charset=utf-8",
                data:JSON.stringify({"username":username, "catalog":{"id":null, "name":catalogName}}),
                beforeSend: function(request) {
                    request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
                },
                success: function(data){
                    if (data.success) {
                        bootoast({
                            message: data.message,
                            type: 'success',
                            position:'bottom',
                            timeout:2
                        });
                        // 成功后，刷新列表
                        $('#myModal-addCatalog').modal('hide');
                        getCatalog();

                    } else {
                        bootoast({
                            message: data.message,
                            type: 'danger',
                            position:'bottom',
                            timeout:2
                        });
                    }
                },
                error : function() {
                    bootoast({
                        message: "发生错误",
                        type: 'danger',
                        position:'bottom',
                        timeout:2
                    });
                }
            })
        } else {
            bootoast({
                message: "文章分类不能为空",
                type: 'danger',
                position:'bottom',
                timeout:2
            });
        }
    })
    $("#blogSubmitBt").on('click', function(){
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/u/'+ username + '/blogsEdit',
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            data:JSON.stringify({"id":null,
                "title": $('#title').val(),
                "summary": $('#summary').val(),
                "content": $('#content').val(),
                "catalog":{"id": $("input[name='radioCatalog']:checked").val()},
                "tags":$("#tags").val()
            }),
            beforeSend: function(request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function(data){
                if (data.success) {
                    bootoast({
                        message: data.message,
                        type: 'success',
                        position:'bottom',
                        timeout:2
                    });
                    // 成功后，重定向
                    window.location = data.body;
                } else {
                    bootoast({
                        message: data.message,
                        type: 'danger',
                        position:'bottom',
                        timeout:2
                    });
                }

            },
            error : function() {
                bootoast({
                    message: "发生错误",
                    type: 'danger',
                    position:'bottom',
                    timeout:2
                });
            }
        })
    })
});

function getCatalog() {
    $.ajax({
        url: '/catalogs/getCatalog',
        type: 'GET',
        data:{"username":username},
        success: function(data){
            $("#showlistCatalog").html(data);
        },
        error : function() {
            bootoast({
                message: "发生错误",
                type: 'danger',
                position:'bottom',
                timeout:2
            });
        }
    });
}