/**
 * 
 */

 						$omi = $('#ol_id');
                        $omp = $('#ol_pw');
                        // $omp.css('display','inline-block').css('width',104);
                        $omi_label = $('#ol_idlabel');
                        $omi_label.addClass('ol_idlabel');
                        $omp_label = $('#ol_pwlabel');
                        $omp_label.addClass('ol_pwlabel');

                        $(function() {
                            $omi.focus(function() {
                                $omi_label.css('visibility','hidden');
                            });
                            $omp.focus(function() {
                                $omp_label.css('visibility','hidden');
                            });
                            $omi.blur(function() {
                                $this = $(this);
                                if($this.attr('id') == "ol_id" && $this.attr('value') == "") $omi_label.css('visibility','visible');
                            });
                            $omp.blur(function() {
                                $this = $(this);
                                if($this.attr('id') == "ol_pw" && $this.attr('value') == "") $omp_label.css('visibility','visible');
                            });
                            
                            $("#login_auto_login").click(function(){
                                if ($(this).is(":checked")) {
                                    if(!confirm("자동로그인을 사용하시면 다음부터 회원아이디와 비밀번호를 입력하실 필요가 없습니다.\n\n공공장소에서는 개인정보가 유출될 수 있으니 사용을 자제하여 주십시오.\n\n자동로그인을 사용하시겠습니까? \n\n해당 로그인은 7일간 유지됩니다."))
                                        return false;
                                }
                            });
                        });
                        
                        
                        $('#ol_submit').off('click').on('click', function() { 
                    	  console.log("asdf"); 
                    	  let param = {
                    	    id: $("#ol_id").val(),
                    	    pass: $("#ol_pw").val(),
                    	    autologin: $("#login_auto_login").val()
                    	  };
                    	  let refererUrl = document.referrer; 
                    	  $.ajax({ 
                    	    url: "/logining", 
                    	    type: "POST", 
                    	    data: { 
                    	      "memid": param.id, 
                    	      "mempw": param.pass, 
                    	      "autologin": param.autologin,
                    	      "refererUrl": refererUrl 
                    	    }, 
                    	    dataType: "json", 
                    	    async: false, 
                    	    cache: false, 
                    	    success: function(data,textStatus) { 
                    	      if (data == true) {
                    	        alert("환영합니다 "+param.id+" 님"); 
                    	      } else { 
                    	        alert("아이디나 비밀번호가 틀렸습니다.\n다시 입력해주세요."); 
                    	      } 
                    	    } 
                    	  }); 
                    	});