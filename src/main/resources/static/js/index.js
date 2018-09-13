$(function () {
    var username=$("#logoutForm").attr("data-username");
    if (username){
        var url="/u/"+username+"/getUser";
        $.ajax({
            url:url,
            contentType : 'application/json',
            type:"get",
            data:{

            },
            success: function(data){
                if (data.avatar) {
                    $("#avatarImg").attr("src",data.avatar);
                }
            },
            error : function() {

            }
        })
    }
})