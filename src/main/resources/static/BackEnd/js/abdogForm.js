const form = document.getElementById("form");
const form_button_submit = document.getElementById('form_button_submit');
const oneClickEnter = document.getElementById('oneClickEnter');
const ab_area = document.getElementById('ab_area');
const ab_name = document.getElementById('ab_name');
const ab_phone = document.getElementById('ab_phone');
const ab_email = document.getElementById('abe_mail');
const ab_remark = document.getElementById('ab_remark');

function oneClickEnter() {
	const count = Math.floor(Math.random() * 10 + 1);
	if (count > 1) {
		ab_area.value = "台北市";
		ab_name.value = "台北市大安區認養中心";
		ab_phone.value = "0912345678";
		ab_email.value = "apple.con.am";
		ab_remark.value = "它的眼睛圓圓的，小小的鼻子下有一張三角嘴，小嘴兩邊有一個漂亮的八字胡";
	}


	if (count == 2) {
		document.form4.abarea.value = "台中市";
		document.form4.abname.value = "台中巿西區認養中心";
		document.form4.abphone.value = "0912345678";
		document.form4.abemail.value = "apple.con.am";
		document.form4.abremark.value = "它的眼睛圓圓的，小小的鼻子下有一張三角嘴，小嘴兩邊有一個漂亮的八字胡";
	}


	if (count == 3) {
		document.form4.abarea.value = "台中市";
		document.form4.abname.value = "台中巿東區認養中心";
		document.form4.abphone.value = "0912345678";
		document.form4.abemail.value = "apple.con.am";
		document.form4.abremark.value = "它的眼睛圓圓的，小小的鼻子下有一張三角嘴，小嘴兩邊有一個漂亮的八字胡";
	}


	if (count == 4) {
		document.form4.abarea.value = "新北市";
		document.form4.abname.value = "新北市清水認養中心";
		document.form4.abphone.value = "0912345678";
		document.form4.abemail.value = "apple.con.am";
		document.form4.abremark.value = "眼睛圓圓的，小小的鼻子下有一張三角嘴，小嘴兩邊有一個漂亮的八字胡";
	}


	if (count == 5) {
		document.form4.abarea.value = "台北市";
		document.form4.abname.value = "台北市松山區認養中心";
		document.form4.abphone.value = "0912345678";
		document.form4.abemail.value = "apple.con.am";
		document.form4.abremark.value = "圓圓的腦袋上豎起兩只尖尖的耳朵，顯得特別神氣。";
	}


	if (count == 6) {
		document.form4.abarea.value = "新北市";
		document.form4.abname.value = "汐止洪國認養中心";
		document.form4.abphone.value = "0912345678";
		document.form4.abemail.value = "apple.con.am";
		document.form4.abremark.value = "小貓鼻子下面有一張人字形的嘴巴，兩旁有6根白色的胡須，常常一扇一扇的，挺神氣。";
	}


	if (count == 7) {
		document.form4.abarea.value = "台中市";
		document.form4.abname.value = "台中市泰北寵物認養中心";
		document.form4.abphone.value = "0912345678";
		document.form4.abemail.value = "apple.con.am";
		document.form4.abremark.value = "小貓“咪咪”的那一雙大耳朵，一天到晚都直豎著，哪個地方有聲音，馬上往那邊轉，活像一架有特殊性能的雷達";
	}

	if (count == 8) {
		document.form4.abarea.value = "台東市";
		document.form4.abname.value = "台東市民寵物流浪中心";
		document.form4.abphone.value = "0912345678";
		document.form4.abemail.value = "apple.con.am";
		document.form4.abremark.value = "它一身的白毛像雪似的，中間夾著數塊墨色的細毛，黑白相間，白的顯得越白，而黑的越發顯得黑了";
	}
	if (count == 9) {
		document.form4.abarea.value = "彰化縣";
		document.form4.abname.value = "彰化縣貓狗認養中心";
		document.form4.abphone.value = "0912345678";
		document.form4.abemail.value = "apple.con.am";
		document.form4.abremark.value = "它見什么都好奇，就用爪子先輕輕地碰一碰，看看沒有危險再用爪子滾來滾去，從這個屋子滾到那個屋子";
	}

	if (count == 10) {
		document.form4.abarea.value = "花蓮縣";
		document.form4.abname.value = "花蓮縣貓狗祝福認養中心";
		document.form4.abphone.value = "0912345678";
		document.form4.abemail.value = "apple.con.am";
		document.form4.abremark.value = "這只花貓的全身是白底黑斑，遠看上去，像一團雪白的棉花點上了幾滴墨汁";
	}



}