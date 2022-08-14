const form = document.getElementById("form");
const form_button_submit = document.getElementById('form_button_submit');
const oneClickEnter = document.getElementById('oneClickEnter');

const miNo = document.getElementById('miNo');
let miActive = document.getElementById('miActive');
const miRole = document.getElementById('miRole');
const miAccount = document.getElementById('miAccount');
const miPassword = document.getElementById('miPassword');
const miName = document.getElementById('miName');
const miGender = document.getElementById('miGender');
const miId = document.getElementById('miId');
const miBirth = document.getElementById('miBirth');
const miPhone = document.getElementById('miPhone');
const miEmail = document.getElementById('miEmail');
const miCity = document.getElementById('miCity');
const miDistrict = document.getElementById('miDistrict');
const miAddress = document.getElementById('miAddress');
const check_roleADMIN = document.getElementById('check-roleADMIN');


/* 修改時，一進頁面就看到目前member的權限 */
if(thisMemberRole!==null){
	for(let i=0; i<thisMemberRole.length ; i++){
		console.log("thisMemberRole: " + thisMemberRole);
		let thisRole = thisMemberRole.split(';'); // 切分後長度是1-4
		for (let j = 0; j < thisRole.length; ++j) {
			document.getElementById('check-role'+ allRole[j]).checked = true;
		}
	}
}

/*送出資料*/
form_button_submit.addEventListener('click', e => {
	e.preventDefault();
	/* 送出時驗證權限 */
	verifyRole();
	if(!checkInputsError()){
		/* 串接地址 */
		// miAddress.value = miCity.value.trim() + miDistrict.value.trim() + miAddress.value.trim();
		/* 串接所有權限角色 */
		let checkedMiRoleV =""; /*miRole.value;*/
		miActive.value = "Y";
		for(let i=0; i < allRole.length ; i++){
			console.log('check-role'+ allRole[i])
			let role = document.getElementById('check-role'+ allRole[i]);
			// console.log("role.checked:"+ role.checked);
			if(role.checked === true){
				checkedMiRoleV += role.value + ";" ;
			}
		}
		if(checkedMiRoleV.length > 0){
			console.log("原"+checkedMiRoleV);
			if( checkedMiRoleV[checkedMiRoleV.length-1] ===';'){
				checkedMiRoleV = checkedMiRoleV.slice(0, -1);
				// console.log("slice後"+checkedMiRoleV);
				miRole.value = checkedMiRoleV;
			}
		}
		form.submit(); /* 如果沒有error，就執行送出*/
	}else{
		onblurCheckAccount();
		oninputCheckPassword();
		oninputCheckName();
		oninputCheckId();
		oninputCheckBirth();
		oninputCheckPhone();
		oninputCheckEmail();
		onblurCheckAddress();
	}
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
					onblurCheckAddress(),
					verifyRole()]
	console.log( "表單送出時確認資料: " +checkSuccess);
	return checkSuccess.some(isFalse);  /*翻譯: 陣列中有沒有false?*/
	// some 裡面放方法, 會把 array(eg.checkSuccess) 裡面的東西一個一個放到 some 括號內的方法中
	// array 中有任何滿足 some 中方法(eg.isFalse)的條件, some 就回傳 true(對，陣列中有false)
}

/* 權限綁定連動 -> 勾高階自動會選低階 */
for(let i=0 ; i< allRole.length   ; i++){  // 0 USER / 1 ACTIVE / 2 EMPLOYEE / 3 ADMIN
	let role = document.getElementById('check-role'+ allRole[i]);  // 建立4個listener，監聽即時的change事件
	role.addEventListener('change', e => {
		if(role.checked === true){  // 如果change事件是因為這個權限被checked
			for( let j =0 ; j<i ; j++){  //用for迴圈把這個權限 [以下] 的權限角色也一起checked
				document.getElementById('check-role'+ allRole[j]).checked = true;
			}
		}else{  // 如果change事件是因為這個權限被 [取消] checked
			for( let j = allRole.length-1 ; j>i ; j--){  //用for迴圈把這個權限 [以上] 的權限角色一起取消checked
				document.getElementById('check-role'+ allRole[j]).checked = false;
			}
		}
	});
}

/*一鍵輸入*/
oneClickEnter.addEventListener('click', e => {
	e.preventDefault();
	oneClickToEnter();
});

//轉換eye -> 顯示密碼
// const togglePassword = document.querySelector("#togglePassword");
document.getElementById("togglePassword").addEventListener('click', function() {
	// 判斷password 還是text
	const type = miPassword.getAttribute('type') === 'password' ? 'text' : 'password';  //三元運算式，把抓到的type存回type
	miPassword.setAttribute('type', type);  //改變type
	this.classList.toggle('fa-eye-slash'); //轉換眼睛圖示
	miPassword.focus();
});

/* 驗證 權限欄(miRole) 是否為空 -> 送出時驗證*/
function verifyRole(){
	/* 因為有寫權限連動，選高階的權限角色時，低階也會一起被checked，所以這裡可以直接驗證最低階的是否也沒被選擇 */
	let roleUnChecked = (document.getElementById("check-roleUSER").checked === false) ;
	if(roleUnChecked) {
		setErrorForAddress(document.getElementById('smallofRole'), '請至少設定一種權限');
		return false;
	}
}

/* 測試 -> 地址加總 */
function onblurCheckAddress(){
	let miCityV = miCity.value.trim();
	let miDistrictV = miDistrict.value.trim();
	let miAddressV = miAddress.value.trim();
//         document.getElementById('total_address').value = city3V + dist3V + mi_addressV;
// 	console.log('完整地址:' +city3V + dist3V + mi_addressV);

	if(miCityV === '') {
		setErrorForAddress(miAddress, '縣市不能為空');
		return false;
		// console.log('miCityV為空');
	} else if(miDistrictV === ''){
		setErrorForAddress(miAddress, '鄉鎮市區不能為空');
		return false;
		// console.log('miDistrictV為空');
	} else if(miAddressV === ''){
		setErrorForAddress(miAddress, '詳細地址不能為空');
		return false;
		// console.log('miAddressV為空');
	} else {
		setSuccessForAddress(miAddress);
		return true;
		// console.log('都不為空');
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
function verifyAccount(miAccountValue){
	let miNoValue = null;
	if(miNo!==null){ //是修改 -> 再取user的編號
		miNoValue = miNo.value.trim();
	}
	let xhr = new XMLHttpRequest();
	xhr.open("POST", "/Lung/Backendmember/CheckMemberAccount", false);
	xhr.setRequestHeader("Content-Type",
		"application/json");
	// xhr.send("accountToCheck=" + mi_accountValue);  //送出user輸入的值
	xhr.send(JSON.stringify({ "accountToCheck": miAccountValue, "miNo":miNoValue }));  //送出user輸入的值

	let accountCanUse = false;
	if (xhr.readyState === 4 && xhr.status === 200) {
		// console.log("第一步");
		let result = JSON.parse(xhr.responseText);
		// console.log(result);
		//帳號不存在就回傳true，帳號存在回傳false
		accountCanUse = result.accountCanUse; //取key的方式，拿到 map 中的 value
	}else if(xhr.status !== 200){
		setErrorFor(miAccount, '錯誤訊息：Server Error! 請聯絡系統管理員。');
		accountCanUse = false;
	}
	console.log('accountCanUse:' + accountCanUse)
	return accountCanUse;
}

function onblurCheckAccount(){
	//確認帳號
	let miAccountValue = miAccount.value.trim();
	// console.log(mi_accountValue);
	if(miAccountValue === '') {
		setErrorFor(miAccount, '帳號不能為空');
		return false;
	} else if(verifyAccount(miAccountValue) === false){
		setErrorFor(miAccount, '帳號重複，請重新輸入帳號');
	} else {
		setSuccessFor(miAccount);
		return true;
	}
}



//驗證password的正規表達式
function verifyPassword (miPasswordValue) {
	//最少八個字符，至少一個大寫字母，一個小寫字母和一個數字
	let regex = new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]{8,}$/g)
	let verification = regex.test(miPasswordValue);
	return verification;
}

function oninputCheckPassword(){
	//確認密碼
	let miPasswordValue = miPassword.value.trim();
	if(miPasswordValue === '') {
		setErrorFor(miPassword, '密碼不能為空');
		return false;
	} else if(verifyPassword(miPasswordValue) === false){
		setErrorFor(miPassword, '密碼格式錯誤，請重新輸入');
		return false;
	} else {
		setSuccessFor(miPassword);
		return true;
	}
}

//驗證姓名
function oninputCheckName(){
	//確認姓名
	let miNameValue = miName.value.trim();
	if(miNameValue === '') {
		setErrorFor(miName, '姓名不能為空');
		return false;
	} else {
		setSuccessFor(miName);
		return true;
	}
}



 //驗證id
function verifyId(id) {
	//建立字母分數陣列(A~Z)
	let city = [1,10,19,28,37,46,55,64,39,73,82, 2,11,20,48,29,38,47,56,65,74,83,21, 3,12,30]
	id = id.trim().toUpperCase();
	//使用「正規表達式」檢驗格式
	if (id.search(/^[A-Z](1|2)\d{8}$/i) === -1) {
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
	    return ((total%10 === 0 ));
	}
}

function oninputCheckId(){
	let miIdValue = miId.value.trim();
	//確認id
	if(miIdValue === ''){
		setErrorFor(miId, '身分證字號不能為空');
		return false;
	}else if(verifyId(miIdValue) === false) {
		setErrorFor(miId, '您的身分證不合法，請重新輸入');
		return false;
	}else {
		setSuccessFor(miId);
		return true;
	}
}


 //驗證生日
 function verifyBirth(miBirthValue){
	let today = new Date().getTime();  //獲得自1970年到當前時間之間的秒數
	let mi_birth = new Date(miBirthValue); //取得user輸入的生日
	//console.log(mi_birth.getTime());  //取得user生日自1970算到現在的秒數
	if(mi_birth>today){
		return false;
	}else{
		return true;
	}
}

 function oninputCheckBirth(){
	//確認生日
	let miBirthValue = miBirth.value.trim();
	if(miBirthValue === ''){
		setErrorFor(miBirth, '生日不能為空');
		return false;
	}else if(verifyBirth(miBirthValue) === false){
		setErrorFor(miBirth, '生日不能大於今天');
		return false;
	}else{
		setSuccessFor(miBirth);
		return true;
	}
}

 //驗證電話的正規表達式
function verifyPhone(phone) {
	let regex = new RegExp(/^09[0-9]{8}$/g) //規則:09開頭，後面接著8個0~9的數字
	return regex.test(phone)
}

function oninputCheckPhone(){
	//確認電話
	let miPhoneValue = miPhone.value.trim();
	if(miPhoneValue === ''){
		setErrorFor(miPhone, '電話不能為空');
		return false;
	}else if(verifyPhone(miPhoneValue) === false) {
		setErrorFor(miPhone, '電話號碼格式錯誤，請重新輸入');
		return false;
	}else{
		setSuccessFor(miPhone);
		return true;
	}
}


//驗證email的正規表達式
function verifyEmail(email) {
	let regex = new RegExp(/^([A-Za-z0-9_\-\.]+)@([A-Za-z0-9_\-\.]+)\.([A-Za-z]{2,6})$/g)
	return regex.test(email)  //規則:任意字符(包括英文數字_-.)無限個  + @ + 任意字符無限個 + . + 二至六位英文字母
}

function oninputCheckEmail(){
	//確認Email
	let miEmailValue = miEmail.value.trim();
	if(miEmailValue === '') {
		setErrorFor(miEmail, 'Email不能為空');
		return false;
	} else if (verifyEmail(miEmailValue) === false) {
		setErrorFor(miEmail, '無效Email，請重新輸入');
		return false;
	} else {
		setSuccessFor(miEmail);
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
	console.log('onmousedown')
	let errorNodes = document.getElementsByClassName('form-group error');
	let successNodes = document.getElementsByClassName('form-group success');
	for(let i = 0 ; i<errorNodes.length ; i++){
		errorNodes[i].className = 'form-group'
	}
	for(let i = 0 ; i<successNodes.length ; i++){
		successNodes[i].className = 'form-group'
	}
	/*塞入值到input框*/
	miAccount.value = randomAccount();
	miPassword.value = randomPassword();
	miName.value = randomName();
	miId.value = randomId();
	miBirth.value = randomBirth();
	miPhone.value = '0987993557';
	miEmail.value = 'email@mail.com';
	miCity.value = '臺北市';
	miCity.dispatchEvent(new Event('change')); //觸發change事件

	miAddress.value = '羅斯福路三段126之5號';
	document.getElementById("check-roleADMIN").checked = true;
	document.getElementById("check-roleEMPLOYEE").checked = true;
	document.getElementById("check-roleACTIVE").checked = true;
	document.getElementById("check-roleUSER").checked = true;

	// document.getElementById("newcountry1").value = '台中市';
	// document.getElementById("newdistrict1").setAttribute("data-value","大雅區");

	// $(".city-selector-set").each(function() {
	// 	$(this).attr("data-value", "大雅區");
	// });

	/* 重新把success加上去 */
	let nodes = document.getElementsByClassName('form-group');
	for(let i = 0 ; i<nodes.length ; i++){
		nodes[i].className = 'form-group success'
	}

}

function setDistrict(){
	console.log('onmouseup')
	let miDistrict22 = document.getElementById('miDistrict')
	miDistrict22.value = '大安區';
	console.log("miDistrict.value是" + miDistrict.value);
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

// 顯示選取的照片
function previewHeadshot(event) {
	/*
    files = ['src']
    法一:
        const file = files[0] -> file = 'src'
    法二:
        const [file] = files -> file = 'src'
    */
	const [file] = document.getElementById("productImage").files
	if (file) {
		document.getElementById("blah").src = URL.createObjectURL(file)
	}
}


/* 只有ADMIN權限的人可以新增/修改他人成為ADMIN */
// document.querySelectorAll("#type option").forEach(opt => {
//
// 	if (opt.value == "ADMIN") {
// 		opt.disabled = true;
// 	}
// });


