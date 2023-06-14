/**
 * 
 */
	var currentPath = window.location.href;
    var fragmentIndex = currentPath.indexOf("#");
    var fragment = currentPath.substring(fragmentIndex + 1);
    var params = fragment.split("&");
    var accessToken;

    for (var i = 0; i < params.length; i++) {
      var keyValue = params[i].split("=");
      var key = keyValue[0];
      var value = keyValue[1];
      
      if (key === "access_token") {
        accessToken = value;
        break;
      }
    }
    console.log(accessToken);
    document.cookie = "accessToken=" + encodeURIComponent(accessToken);
    
     setTimeout(function() {
    if (accessToken !== undefined) {
            console.log('나 왔다감');
            // AJAX 요청으로 서버에 전달
            $.ajax({
                type: "GET",
                url: "/naverinfo",
                data: { access_token: encodeURIComponent(accessToken)},
                success: function(response) {
                    // 서버로부터의 응답 처리
                    var yourObject = JSON.parse(response);
                    console.log(yourObject.path);
                    window.location.href = yourObject.path;
                    completeLoading();
                },
                error: function(error) {
                    
                }
            });
        }
    }, 1000); // 2초 (5000밀리초)
    
    
   