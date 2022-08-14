const form = document.getElementById("form");
const form_button_submit = document.getElementById('form_button_submit');
const oneClickEnter = document.getElementById('oneClickEnter');

const mi_account = document.getElementById('mi_account');
const mi_password = document.getElementById('mi_password');
const mi_name = document.getElementById('mi_name');
const mi_id = document.getElementById('mi_id');
const mi_birth = document.getElementById('mi_birth');
const mi_phone = document.getElementById('mi_phone');
const mi_email = document.getElementById('mi_email');
const city3 = document.getElementById('city3');
const dist3 = document.getElementById('dist3');
const mi_address = document.getElementById('mi_address');
const type = document.getElementById('type');
const ac_name = document.getElementById('ac_name');
const localfileName = document.getElementById('localfileName');
const ac_date = document.getElementById('ac_date');
const ac_participant = document.getElementById('ac_participant');
const ac_venue = document.getElementById('ac_venue');
const ac_quota = document.getElementById('ac_quota');
const ac_waitlist_quota = document.getElementById('ac_waitlist_quota');
const ac_fee = document.getElementById('ac_fee');
const ac_organizer = document.getElementById('ac_organizer');


// trim to remove the whitespaces
const mi_accountValue = mi_account.value.trim();
const mi_passwordValue = mi_password.value.trim();
const mi_nameValue = mi_name.value.trim();
const mi_idValue = mi_id.value.trim();
const mi_birthValue = mi_birth.value.trim();
const mi_phoneValue = mi_phone.value.trim();
const mi_emailValue = mi_email.value.trim();
const mi_addressValue = mi_address.value.trim();




/*送出資料*/
form_button_submit.addEventListener('click', e => {
	e.preventDefault();
	if(!checkInputsError()){
		mi_address.value = city3.value.trim() + dist3.value.trim() + mi_address.value.trim();
		form.submit(); /* 如果沒有error，就執行送出*/
	}
});

/*一鍵輸入*/
oneClickEnter.addEventListener('click', e => {
	e.preventDefault();
	oneClickToEnter();
}); 


/*送出時進行資料確認*/
const isFalse = (element) => element === false;

function checkInputsError() {
	let checkSuccess;

	checkSuccess = [onblurCheckAccount(),
					oninputCheckPassword(),
					oninputCheckName(),
					oninputCheckId(),
					oninputCheckBirth(),
					oninputCheckPhone(),
					oninputCheckEmail(),
					plusAddress()]
	console.log(checkSuccess);
	return checkSuccess.some(isFalse);  /*翻譯: 陣列中有沒有false?*/
	// some 裡面放方法, 會把 array(eg.checkSuccess) 裡面的東西一個一個放到 some 括號內的方法中
	// array 中有任何滿足 some 中方法(eg.isFalse)的條件, some 就回傳 true(對，陣列中有false)
}

//轉換eye -> 顯示密碼
const togglePassword = document.querySelector("#togglePassword");
const password = document.querySelector("#mi_password");
togglePassword.addEventListener('click', function() {
	// 判斷password 還是text
	const type = password.getAttribute('type') === 'password' ? 'text' : 'password';  //三元運算式，把抓到的type存回type
	password.setAttribute('type', type);  //改變type
	this.classList.toggle('fa-eye-slash'); //轉換眼睛圖示
	document.getElementById("mi_password").focus();
});

/* 測試 -> 地址加總 */
function plusAddress(){
	let city3V = document.getElementById('city3').value.trim();
	let dist3V = document.getElementById('dist3').value.trim();
	let mi_addressV = document.getElementById('mi_address').value.trim();
//         document.getElementById('total_address').value = city3V + dist3V + mi_addressV;
//         console.log(document.getElementById('total_address').value)
	console.log('完整地址:' +city3V + dist3V + mi_addressV);

	if(city3V === '') {
		setErrorForAddress(mi_address, '縣市不能為空');
		return false;
		console.log('city3V為空');
	} else if(dist3V === ''){
		setErrorForAddress(mi_address, '鄉鎮市區不能為空');
		return false;
		console.log('dist3V為空');
	} else if(mi_addressV === ''){
		setErrorForAddress(mi_address, '詳細地址不能為空');
		return false;
		console.log('mi_addressV為空');
	} else {
		setSuccessForAddress(mi_address);
		return true;
		console.log('都不為空');
	}

}

function setErrorForAddress(input, message){
	const formGroup = input.parentElement.parentElement;
	const small = formGroup.querySelector('small');
	formGroup.className = 'form-group error';
	small.innerText = message;
}

function setSuccessForAddress(input) {
	const formGroup = input.parentElement.parentElement;
	formGroup.className = 'form-group success';
}





//驗證帳號
function verifyAccount(mi_accountValue){
	let xhr = new XMLHttpRequest();
	xhr.open("POST", "/Lung/Backendmember/CheckMemberAccount", false);
	xhr.setRequestHeader("Content-Type",
		"application/x-www-form-urlencoded");
	xhr.send("accountToCheck=" + mi_accountValue);  //送出user輸入的值
	let accountCanUse = false;
	if (xhr.readyState === 4 && xhr.status === 200) {
		console.log("第一步");
		let result = JSON.parse(xhr.responseText);
		console.log(result);
		//帳號不存在就回傳true，帳號存在回傳false
		accountCanUse = !result.accountExisted; //取key的方式，拿到 map 中的 value
	}else if(xhr.status !== 200){
		setErrorFor(mi_account, '錯誤訊息：Server Error! 請聯絡系統管理員。');
		accountCanUse = false;
	}
	console.log('accountCanUse:' + accountCanUse)
	return accountCanUse;
}

function onblurCheckAccount(){
	//確認帳號
	let mi_accountValue = mi_account.value.trim();
	console.log(mi_accountValue); 
	if(mi_accountValue === '') {
		setErrorFor(mi_account, '帳號不能為空');
		return false;
	} else if(verifyAccount(mi_accountValue) === false){
		setErrorFor(mi_account, '帳號重複，請重新輸入帳號');
	} else {
		setSuccessFor(mi_account);
		return true;
	}
}



//驗證password的正規表達式
function verifyPassword (mi_passwordValue) {
	//最少八個字符，至少一個大寫字母，一個小寫字母和一個數字
	let regex = new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/g)
	verification = regex.test(mi_passwordValue)
	return verification
}
	
function oninputCheckPassword(){
	//確認密碼
	let mi_passwordValue = mi_password.value.trim();
	if(mi_passwordValue === '') {
		setErrorFor(mi_password, '密碼不能為空');
		return false;
	} else if(verifyPassword(mi_passwordValue) === false){
		setErrorFor(mi_password, '密碼格式錯誤，請重新輸入');
		return false;
	} else {
		setSuccessFor(mi_password);
		return true;
	}
}

//驗證姓名
function oninputCheckName(){
	//確認姓名
	let mi_nameValue = mi_name.value.trim();
	if(mi_nameValue === '') {
		setErrorFor(mi_name, '姓名不能為空');
		return false;
	} else {
		setSuccessFor(mi_name);
		return true;
	}
}


	
 //驗證id
function verifyId(id) {
	//建立字母分數陣列(A~Z)
	var city = new Array(1,10,19,28,37,46,55,64,39,73,82, 2,11,20,48,29,38,47,56,65,74,83,21, 3,12,30)
	id = id.trim().toUpperCase();
	//使用「正規表達式」檢驗格式
	if (id.search(/^[A-Z](1|2)\d{8}$/i) == -1) {
	    return false;
	} else {
	    //將字串分割為陣列(IE必需這麼做才不會出錯)
	    id = id.split('');
	    //計算總分
	    var total = city[id[0].charCodeAt(0)-65];
	    for(var i=1; i<=8; i++){
	        total += eval(id[i]) * (9 - i);
	    }
	    //補上檢查碼(最後一碼)
	    total += eval(id[9]);
	    //檢查比對碼(餘數應為0);
	    return ((total%10 == 0 ));
	}
}
 
function oninputCheckId(){
	let mi_idValue = mi_id.value.trim();
	//確認id
	if(mi_idValue === ''){
		setErrorFor(mi_id, '身分證字號不能為空');
		return false;
	}else if(verifyId(mi_idValue) === false) {
		setErrorFor(mi_id, '您的身分證不合法，請重新輸入');
		return false;
	}else {
		setSuccessFor(mi_id);
		return true;
	}
}

 
 //驗證生日
 function verifyBirth(mi_birthValue){
	let today = new Date().getTime();  //獲得自1970年到當前時間之間的秒數
	let mi_birth = new Date(mi_birthValue); //取得user輸入的生日
	//console.log(mi_birth.getTime());  //取得user生日自1970算到現在的秒數
	if(mi_birth>today){
		return false;
	}else{
		return true;
	}

}
 
 function oninputCheckBirth(){
	//確認生日
	let mi_birthValue = mi_birth.value.trim();
	if(mi_birthValue === ''){
		setErrorFor(mi_birth, '生日不能為空');
		return false;
	}else if(verifyBirth(mi_birthValue) === false){
		setErrorFor(mi_birth, '生日不能大於今天');
		return false;
	}else{
		setSuccessFor(mi_birth);
		return true;
	}
}
 
 //驗證電話的正規表達式
function verifyPhone(phone) {
	let regex = new RegExp(/^09[0-9]{8}$/g) //規則:09開頭，後面接著8個0~9的數字
	phone = phone.trim();
	verification = regex.test(phone)  
	return verification
}
 
function oninputCheckPhone(){
	//確認電話
	let mi_phoneValue = mi_phone.value.trim();
	if(mi_phoneValue === ''){
		setErrorFor(mi_phone, '電話不能為空');
		return false;
	}else if(verifyPhone(mi_phoneValue) === false) {
		setErrorFor(mi_phone, '電話號碼格式錯誤，請重新輸入');
		return false;
	}else{
		setSuccessFor(mi_phone);
		return true;
	}
}
 
 
//驗證email的正規表達式
function verifyEmail(email) {
	let regex = new RegExp(/^([A-Za-z0-9_\-\.]+)@([A-Za-z0-9_\-\.]+)\.([A-Za-z]{2,6})$/g)
	email = email.trim();
    verification = regex.test(email)  //規則:任意字符(包括英文數字_-.)無限個  + @ + 任意字符無限個 + . + 二至六位英文字母
	return verification
}
 
function oninputCheckEmail(){
	//確認Email
	let mi_emailValue = mi_email.value.trim();
	if(mi_emailValue === '') {
		setErrorFor(mi_email, 'Email不能為空');
		return false;
	} else if (verifyEmail(mi_emailValue) === false) {
		setErrorFor(mi_email, '無效Email，請重新輸入');
		return false;
	} else {
		setSuccessFor(mi_email);
		return true;
	}
} 
 
// function onblurCheckAddress(){
// 	//確認地址
// 	let city3V = city3.value.trim();
// 	let dist3V = dist3.value.trim();
// 	let mi_addressV = mi_address.value.trim();
// 	if( city3V === '' || dist3V === '' || mi_addressV === '') {
// 		setErrorFor(mi_address, '地址不能為空');
// 		return false;
// 	} else {
// 		setSuccessFor(mi_address);
// 		return true;
// 	}
// }
 
/* 塞錯誤訊息 */ 
function setErrorFor(input, message) {
	const formGroup = input.parentElement;
	const small = formGroup.querySelector('small');
	formGroup.className = 'form-group error';
	small.innerText = message;
}

/* 塞正確訊息 */
function setSuccessFor(input) {
	const formGroup = input.parentElement;
	formGroup.className = 'form-group success';
}
 
 /* 執行一鍵輸入*/
function oneClickToEnter(){
	/* 取消變顏色*/
	// let errorNodes = document.getElementsByClassName('form-group error');
	// let successNodes = document.getElementsByClassName('form-group success');
	// for(let i = 0 ; i<errorNodes.length ; i++){
	// 	errorNodes[i].className = 'form-group'
	// }
	// for(let i = 0 ; i<successNodes.length ; i++){
	// 	successNodes[i].className = 'form-group'
	// }
	/*塞入值到input框*/
	// mi_account.value = randomAccount();
	// mi_password.value = randomPassword();
	// mi_name.value = randomName();
	// mi_id.value = randomId();
	// mi_birth.value = randomBirth();
	// mi_phone.value = '0987993557';
	// mi_email.value = 'email@mail.com';
	// city3.value = '臺北市';
	// // for (let i = 0; i < 50000000000; i++) {}
	// dist3.value = '大安區';
	// mi_address.value = '羅斯福路三段126之5號';
	// type.value = 'USER';
	ac_name.value = '台中市動物保護志工招募';
	localfileName.value = '可愛貓貓.jpg';
	ac_date.value = '2022-01-01';
	ac_participant.value = '關心動物保護議題，卻不知如何投入動物保護第一線嗎？台中動保處今年的志工招募活動開跑了!台中的朋友們可別錯過這個機會，服務內容包含推廣動物保護觀念、協助追蹤動保案件，以及照顧、陪伴園區內的犬貓，服務內容為協助動保處進行動物保護宣導活動、動保案件稽查，動物之家園區介紹導覽、照顧園內犬貓、推廣認領養等等。多元的服務內容，能讓大家發揮所長一同為動物盡一份力量！';
	ac_venue.value = '地點';
	ac_quota.value = '50';
	ac_waitlist_quota.value = '0';
	ac_fee.value = '500';
	ac_organizer.value = '主辦';
	type.value = '0';

	/* 重新把success加上去 */
	// let nodes = document.getElementsByClassName('form-group');
	// for(let i = 0 ; i<nodes.length ; i++){
	// 	nodes[i].className = 'form-group success'
	// }
	
}
 
 /*產生隨機帳號*/
 function randomAccount(){
	let code="";
	let codeLength = Math.floor(Math.random()*6)+8;//隨機產生驗證碼的長度0-5碼再加8 -> 產生8-13碼	
	let random = new Array(0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');//隨機數 
	for(let i = 0; i < codeLength; i++  ) {//迴圈操作 
		let index = Math.floor(Math.random()*36);//取得隨機數的索引（0~35） 
		code  += random[index];//根據索引取得隨機數加到code上 
	} 
	return code;
}

/*產生隨機帳號*/
function randomPassword(){
	let code="";
	let codeLength = Math.floor(Math.random()*6)+5;//隨機產生驗證碼的長度0-5碼再加8 -> 產生5-10碼
	let randomNumber = '0123456789';
	let randomLower = 'abcdefghijklmnopqrstuvwxyz';
	let randomUpper = randomLower.toUpperCase();
	let randomAll = randomNumber + randomLower + randomUpper;

	let resultNumber = randomNumber[Math.floor(Math.random()*10)]; //randomNumber[index]
	let resultLower = randomLower[Math.floor(Math.random()*26)];  //randomLower[index]
	let resultUpper = randomUpper[Math.floor(Math.random()*26)];  //randomUpper[index]

	for(let i = 0; i < codeLength; i++  ) {//迴圈操作
		code  += randomAll[Math.floor(Math.random()*62)]; //隨機從62位數中取得 //根據索引取得隨機數加到code上
	}
	code += resultNumber + resultLower + resultUpper;
	return code;
}

/*產生隨機姓名*/
function randomName(){
	let familyNames = new Array(
	"王", "陳",  "林", "許", "張" , "蔡", "李", "黃", "吳", "蔡", 
	"鄭", "楊", "劉" , "郭", "許", "洪", "邱", "曾", "周", "謝"
	);
	
	let givenNames = new Array(
	"家豪", "志明", "俊傑", "冠宇", "承翰", "志偉", "志成", "國華", "冠霖", "宇翔",
	 "淑娟", "淑芬", "美玲", "雅婷", "美惠", "麗華", "怡君", "美玉", "麗美", "秀玲"
	);
	let i = Math.floor(Math.random()*12); //隨機在0-12中選一個數
	let familyName = familyNames[i];
	let givenName = givenNames[i];
	let name = familyName + givenName;
	return name
}

/*隨機產生身分證*/
function randomId(){
	let city = new Array(
         1,10,19,28,37,46,55,64,39,73,82, 2,11,
        20,48,29,38,47,56,65,74,83,21, 3,12,30
    )
    //建立隨機身份證碼
    let id = new Array();
    id[0] = String.fromCharCode(Math.floor(Math.random() * (26)) + 65);
    id[1] = Math.floor(Math.random() * (2)) + 1;
    for(let i=2; i<9; i++){
        id[i] = Math.floor(Math.random() * (9)) + 0;
    }
    //計算總分
    let total = city[id[0].charCodeAt(0)-65];
    for(let i=1; i<=8; i++){
        total += eval(id[i]) * (9 - i);
    }
    //計算最尾碼
    let total_arr = (total+'').split('');
    let lastChar = eval(10-total_arr[total_arr.length-1]);
    let lastChar_arr = (lastChar+'').split('');
    //補上最後檢查碼
    id[id.length++] = lastChar_arr[lastChar_arr.length-1];
    //回傳結果
    return id.join('');
}

/*隨機產生生日*/
function randomBirth(){
	let code="";
	// 年
	let randomYear = new Array('1971','1973','1975','1977','1979','1981','1983','1985','1987','1989',
		'1991','1993','1995','1997','1999','2000','2001','2002','2003','2004','2005','2006','2007','2008',
		'2009','2010','2011','2012','2013','2014','2015','2016','2017','2018','2019','2020');//隨機月份
	let indexYear = Math.floor(Math.random()*35);//陣列中有35個數 //取得隨機數的索引（0-34）
	code  += randomYear[indexYear];//根據索引取得隨機數加到code上
	// 月
	let randomMonth = new Array('01','02','03','04','05','06','07','08','09','10','11','12');//隨機月份
	let indexMonth = Math.floor(Math.random()*12);//取得隨機數的索引（0-11）
	code  += ("-" + randomMonth[indexMonth]);//根據索引取得隨機數加到code上
	// 日
	let randomDay = new Array('01','02','03','04','05','06','07','08','09','10','11','12',
		'13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31');//隨機日期
	let indexDay = Math.floor(Math.random()*31);//取得隨機數的索引（0-30）
	code  += ( "-" + randomDay[indexDay]);//根據索引取得隨機數加到code上

	return code;
}


/* 只有ADMIN權限的人可以新增/修改他人成為ADMIN */
// document.querySelectorAll("#type option").forEach(opt => {
//
// 	if (opt.value == "ADMIN") {
// 		opt.disabled = true;
// 	}
// });


//返回會員系統
// function back(){
//  		history.back()
// 	}
