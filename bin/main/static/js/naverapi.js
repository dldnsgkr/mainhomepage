/**
 * 
 */

 	var currentPath = window.location.href;
    var fragmentIndex = currentPath.indexOf("#");
    var fragment = currentPath.substring(fragmentIndex + 1);
    var params = fragment.split("&");
    console.log(currentPath);
    var callbackUrl = "http://localhost:9000/naverLogin";
    document.cookie = "url=" + encodeURIComponent(currentPath);
   
    
var naverLogin = new naver.LoginWithNaverId(
		{
			clientId: "8ixMHJfHs6rGa6hj_5zP", //내 애플리케이션 정보에 cliendId를 입력해줍니다.
			callbackUrl: callbackUrl, // 내 애플리케이션 API설정의 Callback URL 을 입력해줍니다.
			isPopup: false,
			callbackHandle: true
		}
	);	

naverLogin.init();

function handleClick() {
 naverLogin.getLoginStatus(function (status) {
		if (status) {
			var email = naverLogin.user.getEmail(); // 필수로 설정할것을 받아와 아래처럼 조건문을 줍니다.
    		
			console.log(naverLogin.user); 
    		
            if( email == undefined || email == null) {
				alert("이메일은 필수정보입니다. 정보제공을 동의해주세요.");
				naverLogin.reprompt();
				return;
			}
		} else {
			console.log("callback 처리에 실패하였습니다.");
		}
	});
}

document.getElementById("naverIdLogin_loginButton").addEventListener("click", handleClick);




var testPopUp;
function openPopUp() {
    testPopUp= window.open("https://nid.naver.com/nidlogin.logout", "_blank", "toolbar=yes,scrollbars=yes,resizable=yes,width=1,height=1");
}
function closePopUp(){
    testPopUp.close();
}

function naverLogout() {
	openPopUp();
	setTimeout(function() {
		closePopUp();
		}, 1000);
	
	
}