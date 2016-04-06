<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
<link href="../css/bootstrap.css" rel="stylesheet">
<link href="../css/jquery-ui-1.9.2.custom.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="../css/font-awesome.min.css" />
<link href="../css/common.css" rel="stylesheet">
<script src="../js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="../js/jquery.banner.js"></script>
<script type="text/javascript" src="../js/jquery.preload.min.js"></script> 
<script type="text/javascript" src="../js/jquery.ui.totop.js"></script> 
<link href="../css/ui.totop.css" rel="stylesheet" type="text/css"> 
<script src="../js/jquery-ui.js"></script>
<script src="../js/bootstrap.js"></script>
<script src="../js/common.js"></script>

<link rel="stylesheet" type="text/css" href="../css/match.css" />
<link rel="stylesheet" type="text/css" href="../css/popup.css" />
<script src="../js/scheduleCell.js"></script>

<link href="../css/common.css" rel="stylesheet" type="text/css"> 
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<script language="javascript" type="text/javascript">
function clearText(field)
{
    if (field.defaultValue == field.value) field.value = '';
    else if (field.value == '') field.value = field.defaultValue;
}
</script>

<script>

	function makeUrl(url) {
		var index = url.indexOf('?');
		if (index == -1) {
			url = url + '?stamp=' + Date.now();
		} else {
			url = url + '&stamp=' + Date.now();
		}
		return url;
	}
</script>