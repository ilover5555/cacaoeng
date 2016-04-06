<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script src="//d1p7wdleee1q2z.cloudfront.net/post/search.min.js"></script>
<script src="../js/student_common.js"></script>
<script>
$(document).ready(function(){
	$('#modifyStudent').click(function(){
		var scrollY = $(window).scrollTop();
		$.ajax({
			url : makeUrl('../modifyStudent.view'),
			method : "GET",
			dataType : "html",
			success:function(msg){
				$('#light').html(msg);
				scheduleCell.showBox(scrollY, common.init(), true, true);
			},
			error: function(request,status,error){
				alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	})
	
})
</script>
<div id="light" class="white_content"></div>
	<div id="fade" class="black_overlay"></div>
<div id="wrap"> 
	<div id="header">
		<div id="top-bg-off"><a href="../main/main.jsp"><img src="../main/images/top_bg_01.jpg" width="1000" height="156" alt="카카오영어 로고배경"></a></div>
		<div class="header-menu">
		  <ul>
			   <li><a href="#"><img src="../main/images/top_menu_01_off.png" width="145" height="46" alt="카카오영어"></a></li>
			   <li><a href="#"><img src="../main/images/top_menu_02_off.png" width="142" height="46" alt="학습방법"></a></li>
			   <li><span class="text-yellow"><a href="#"><img src="../main/images/top_menu_03_off.png" width="143" height="46" alt="교과과정"></a></span></li>
			   <li><a href="#"><img src="../main/images/top_menu_04_off.png" width="143" height="46" alt="수강안내"></a></li>
			   <li><a href="#"><img src="../main/images/top_menu_05_off.png" width="142" height="46" alt="내강의실"></a></li>
			   <li class="text-yellow"><a href="#"><img src="../main/images/top_menu_06_off.png" width="142" height="46" alt="레벨테스트"></a></li>               
			   <li><a href="#"><img src="../main/images/top_menu_07_off.png" width="143" height="46" alt="고객센터"></a></li>
		  </ul> 
		  <div class="sub-menu" id="top-bg-on"> 
				<div class="top_bg_on_logo"><a href="/"><img src="../common/images/spacer.gif" width="1000" height="30" alt="카카오영어"></a></div>
				<table width="1000" id="top-sub-menu" >
					<tr valign="top" align="center">
					  <td width="15"></td>
					  <td width="120">
						  <ul>
							<li><a href="../m1/kakaoeng.jsp">카카오영어는?</a></li>
							<li><a href="../m1/kakaoeng.jsp#sec2">1타강사진</a></li>
							<li><a href="../m1/kakaoeng.jsp#sec3">스마트예약시스템</a></li>
							<li><a href="../m1/kakaoeng.jsp#sec4">검증된교과과정</a></li>
						  </ul>
					  </td>
					  <td width="18"></td>
					 <td width="120">
						<ul>
							<li><a href="../m2/study.jsp">화상영어란?</a></li>
							<li><a href="../m2/study.jsp#sec3">빅블루버튼</a></li>
							<li><a href="../m2/study.jsp#sec4">스카이프</a></li>
							<li><a href="../m2/study.jsp#sec5">모바일지원</a></li>
						</ul>						
					  </td>
					  <td width="23"></td>
					  <td width="120">
						<ul>
							<li><a href="../m3/books.view">커리큘럼</a></li>
							<li><a href="../m3/books.view#books">교재소개</a></li>
							<li><a href="../m3/match.view?sort=TeacherMatch&course=TypeEasy&title=&month=1&period=&duration=">선생님소개</a></li> 
						</ul>
						</td>
					  <td width="23"></td>
					  <td width="120">
						 <ul>
							<li><a href="../m4/studentTuition.view?month=1&v=k">예약방법</a></li>
							<li><a href="../m4/studentTuition.view?month=1&v=k#sec2">수강료안내</a></li>
							<li><a href="../m3/match.view?sort=SmartMatch&course=TypeEasy&title=&month=1&pereiod=2&duration=1#">수강신청</a></li> 
						  </ul>
					</td>
					<td width="23"></td>
					<td width="120">
						<ul>
							<li><a href="../m5/studentBoardList.view">학습게시판</a></li>
							<li><a href="../m5/studentSchedule.view">학습캘린더</a></li>
							<li><a href="#">내수강내역</a></li> 
							<li><a id="modifyStudent" href="#">내정보관리</a></li> 
						  </ul>
						</td>
					  <td width="24"></td>
					  <td width="120">
							<ul>
							<li><a href="../m6/leveltest.jsp">테스트과정</a></li>
							<li><a href="#">테스트신청</a></li> 
						  </ul>
						 </td>
					  <td width="23"></td>
					  <td width="120">
						<ul>
							<li><a href="../m7/notifyBoardList.view">공지사항</a></li>
							<li><a href="../m7/freeBoardList.view">1:1문의</a></li>
							<li><a href="../m7/epilogueBoardList.view">수강후기</a></li> 
						</ul>
					  </td>
					  <td width="13" rowspan="5"></td>
					</tr>           
				</table> 
				<!--table width="1000" id="top-sub-menu" >
					<tr valign="top">
					  <td width="15"></td>
					  <td><table>
						<tr>
						  <td><a href="/m1/kakaoeng.html"><img src="/main/images/top_menu_01_01_off.jpg" width="120" height="23" alt="카카오영어는?"></a></td>
						</tr>
						<tr>
						  <td><a href="/m1/kakaoeng.html#sec2"><img src="/main/images/top_menu_01_02_off.jpg" width="120" height="23" alt="1타강사진"></a></td>
						</tr>
						<tr>
						  <td><a href="/m1/kakaoeng.html#sec3"><img src="/main/images/top_menu_01_03_off.jpg" width="120" height="23" alt="스마트예약시스템"></a></td>
						</tr>
						<tr>
						  <td><a href="/m1/kakaoeng.html#sec4"><img src="/main/images/top_menu_01_04_off.jpg" width="120" height="23" alt="검증된교과과정"></a></td>
						</tr>
					  </table></td>
					  <td width="18"></td>
					  <td><table>
						<tr>
						  <td><a href="/m2/study.html"><img src="/main/images/top_menu_02_01_off.jpg" width="120" height="23" alt="화상영어란?"></a></td>
						</tr>
						<tr>
						  <td><a href="/m2/study.html#sec3"><img src="/main/images/top_menu_02_02_off.jpg" width="120" height="23" alt="빅블루버튼"></a></td>
						</tr>
						<tr>
						  <td><a href="/m2/study.html#sec4"><img src="/main/images/top_menu_02_03_off.jpg" width="120" height="23" alt="스카이프"></a></td>
						</tr>
						<tr>
						  <td><a href="/m2/study.html#sec5"><img src="/main/images/top_menu_02_04_off.jpg" width="120" height="23" alt="모바일지원"></a></td>
						</tr>
					  </table></td>
					  <td width="23"></td>
					  <td><table>
						<tr>
						  <td><a href="/m3/books.html"><img src="/main/images/top_menu_03_01_off.jpg" width="120" height="23" alt="커리큘럼"></a></td>
						</tr>
						<tr>
						  <td><a href="/m3/books.html#books"><img src="/main/images/top_menu_03_02_off.jpg" width="120" height="23" alt="교재소개"></a></td>
						</tr>
						<tr>
						  <td><a href="#"><img src="/main/images/top_menu_03_03_off.jpg" width="120" height="23" alt="선생님소개"></a></td>
						</tr>
						<tr>
						  <td></td>
						</tr>
					  </table></td>
					  <td width="23"></td>
					  <td><table>
						<tr>
						  <td><a href="/m4/course.html"><img src="/main/images/top_menu_04_01_off.jpg" width="120" height="23" alt="예약방법"></a></td>
						</tr>
						<tr>
						  <td><a href="/m4/course.html#sec2"><img src="/main/images/top_menu_04_02_off.jpg" width="120" height="23" alt="수강료안내"></a></td>
						</tr>
						<tr>
						  <td><a href="#"><img src="/main/images/top_menu_04_03_off.jpg" width="120" height="23" alt="수강신청"></a></td>
						</tr>
						<tr>
						  <td></td>
						</tr>
					  </table></td>
					  <td width="23"></td>
					  <td><table>
						<tr>
						  <td><a href="#"><img src="/main/images/top_menu_05_01_off.jpg" width="118" height="23" alt="학습게시판"></a></td>
						</tr>
						<tr>
						  <td><a href="#"><img src="/main/images/top_menu_05_02_off.jpg" width="118" height="23" alt="학습캘린더"></a></td>
						</tr>
						<tr>
						  <td><a href="#"><img src="/main/images/top_menu_05_03_off.jpg" width="118" height="23" alt="내수강내역"></a></td>
						</tr>
						<tr>
						  <td><img src="/main/images/top_menu_05_04_off.jpg" width="118" height="23" alt="내정보관리"></td>
						</tr>
					  </table></td>
					  <td width="24"></td>
					  <td><table>
						<tr>
						  <td><a href="#"><img src="/main/images/top_menu_06_01_off.jpg" width="120" height="23" alt="테스트과정"></a></td>
						</tr>
						<tr>
						  <td><a href="#"><img src="/main/images/top_menu_06_02_off.jpg" width="120" height="23" alt="테스트신청"></a></td>
						</tr>
						<tr>
						  <td></td>
						</tr>
						<tr>
						  <td></td>
						</tr>
					  </table></td>
					  <td width="23"></td>
					  <td><table>
						<tr>
						  <td><a href="#"><img src="/main/images/top_menu_07_01_off.jpg" width="120" height="23" alt="공지사항"></a></td>
						</tr>
						<tr>
						  <td><a href="#"><img src="/main/images/top_menu_07_02_off.jpg" width="120" height="23" alt="1:1문의"></a></td>
						</tr>
						<tr>
						  <td><a href="#"><img src="/main/images/top_menu_07_03_off.jpg" width="120" height="23" alt="수강후기"></a></td>
						</tr>
						<tr>
						  <td></td>
						</tr>
					  </table></td>
					  <td width="13" rowspan="5"></td>
					</tr>           
				</table-->
			</div>  
		</div>	
	</div>
	<div id="container"> 
