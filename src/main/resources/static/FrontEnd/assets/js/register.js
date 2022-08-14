const form = document.getElementById("form");
const form_button_submit = document.getElementById('form_button_submit');
const oneClickEnter = document.getElementById('oneClickEnter');

const miNo = document.getElementById('miNo');
const miRole = document.getElementById('miRole');
const miAccount = document.getElementById('miAccount');
const miPassword = document.getElementById('miPassword');
const miPassword2 = document.getElementById('miPassword2');
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

/*先不show送出按紐*/
form_button_submit.style.display = 'none';

/*傳送google recaptcha到後端*/
function verifyCallback(token) {
    // console.log("in verifycallback");
    var formData = new FormData();
    formData.append('token', token);
    // Google Apps Script 部署為網路應用程式後取得的 URL
    var uriGAS = "https://script.google.com/macros/s/AKfycbzwguehfAUBLj1L6jBwvdxJNEUkce650SRhzHfFG_CdfRBgQksbv7hzFVG8FmmzZtUJ/exec";
    fetch(uriGAS, {
        method: "POST",
        body: formData
    }).then(response => response.json())
        .then(result => {
            if(result.success) {
                // 後端驗證成功，success 會是 true
                // 送出按紐show出
                form_button_submit.style.display = '';
            } else {
                // success 為 false 時，代表驗證失敗，error-codes 會告知原因
                Swal.fire({
                    icon: 'error',
                    title: result['error-codes'][0],
                })
            }
        })
        .catch(err => {
            Swal.fire({
                icon: 'error',
                title: err,
            })
        })
}

/*送出資料*/
form_button_submit.addEventListener('click', e => {
    e.preventDefault();
    /* 前台不用驗證權限是誰 */
    // verifyRole();
    if(!checkInputsError()){
        miActive.value = "N";
        miRole.value = "USER";
        form.submit(); /* 如果沒有error，就執行送出*/
    }else{
        onblurCheckAccount();
        oninputCheckPassword();
        onblurCheckPasswordSame();
        oninputCheckName();
        oninputCheckId();
        oninputCheckBirth();
        oninputCheckPhone();
        oninputCheckEmail();
        onblurCheckAddress();
        Swal.fire({
            icon: 'error',
            title: '資料有誤! 請再次確認您的輸入!',
        })
    }
});



/*送出時進行資料確認*/
const isFalse = (element) => element === false;
function checkInputsError() {
    let checkSuccess;
    checkSuccess = [onblurCheckAccount(),
        oninputCheckPassword(),
        onblurCheckPasswordSame(),
        oninputCheckName(),
        oninputCheckId(),
        oninputCheckBirth(),
        oninputCheckPhone(),
        oninputCheckEmail(),
        onblurCheckAddress()]
    // console.log( "表單送出時確認資料: " +checkSuccess);
    return checkSuccess.some(isFalse);  /*翻譯: 陣列中有沒有false?*/
    // some 裡面放方法, 會把 array(eg.checkSuccess) 裡面的東西一個一個放到 some 括號內的方法中
    // array 中有任何滿足 some 中方法(eg.isFalse)的條件, some 就回傳 true(對，陣列中有false)
}


/*一鍵輸入*/
oneClickEnter.addEventListener('click', e => {
    e.preventDefault();
    oneClickToEnter();
});
document.getElementById("oneClickEnterError").addEventListener('click', e => {
    e.preventDefault();
    oneClickToEnterError();
});

//轉換eye -> 顯示密碼
let togglePassword = document.querySelector("#togglePassword");
togglePassword.addEventListener('click', function () {
    // 判斷password 還是text
    let type = miPassword.getAttribute('type') === 'password' ? 'text' : 'password';  //三元運算式，把抓到的type存回type
    miPassword.setAttribute('type', type);  //改變type
    this.classList.toggle('fa-eye-slash'); //轉換眼睛圖示
    miPassword.focus();
});

let togglePassword2 = document.querySelector("#togglePassword2");
togglePassword2.addEventListener('click', function () {
    // 判斷password 還是text
    let type = miPassword2.getAttribute('type') === 'password' ? 'text' : 'password';  //三元運算式，把抓到的type存回type
    miPassword2.setAttribute('type', type);  //改變type
    this.classList.toggle('fa-eye-slash'); //轉換眼睛圖示
    miPassword2.focus();
});


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
    const inputParent = input.parentElement.parentElement;
    const small = inputParent.querySelector('small');
    inputParent.className = 'single-input-item error m-b-30'; <!-- m-b-30是專門for前端版 -->
    small.innerText = message;
}

function setSuccessForAddress(input) {
    const inputParent = input.parentElement.parentElement;
    inputParent.className = 'single-input-item success m-b-30';
}



//驗證帳號
function verifyAccount(){
    // let miNoValue = null;
    // if(miNo!==null){ //是修改 -> 再取user的編號
    //     miNoValue = miNo.value.trim();
    // }
    let miAccountValue = miAccount.value;
    $.ajax({
        type: 'POST',
        url: '/Lung/FrontMember/CheckMemberAccount',
        dataType: "json",
        async: false,
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({"accountToCheck": miAccountValue, "miNo":null}),
        success: function (result) {
            console.log(result);
            if(result.accountCanUse === false){  //取key的方式，拿到 map 中的 value
                setErrorFor(miAccount, '帳號重複，請重新輸入帳號');
            } else {
                setSuccessFor(miAccount);
                return true;
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log(xhr.status);
            console.log(thrownError);
            return false;
        }
    });
}

function onblurCheckAccount(){
    //確認帳號
    let miAccountValue = miAccount.value; /*.trim();*/
    // console.log(mi_accountValue);
    if(miAccountValue === '') {
        setErrorFor(miAccount, '帳號不能為空');
        return false;
    } else {
        // 把varifyAccount移出去做
        verifyAccount();
    }
}



//驗證password的正規表達式
function verifyPassword (miPasswordValue) {
    //最少八個字符，至少一個大寫字母，一個小寫字母和一個數字
    let regex = new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/g)  //!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?
    let verification = regex.test(miPasswordValue);
    return verification;
}

function oninputCheckPassword(){
    //確認密碼
    let miPasswordValue = miPassword.value.trim();
    // console.log("密碼1: " + miPasswordValue)
    if(miPasswordValue === '') {
        setErrorFor(miPassword, '密碼不能為空');
        return false;
    } else {
        let v = verifyPassword(miPasswordValue);
        if(v === false){
            setErrorFor(miPassword, '密碼格式錯誤，請重新輸入');
            return false;
        } else {
            setSuccessFor(miPassword);
            return true;
        }
    }
}

function onblurCheckPasswordSame(){
    let miPasswordValue = miPassword.value.trim();
    let miPassword2Value = miPassword2.value.trim();
    if(miPasswordValue !== miPassword2Value) {
        setErrorFor(miPassword2, '密碼不相同，請重新確認密碼');
        return false;
    } else {
        setSuccessFor(miPassword2);
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
    return mi_birth <= today;
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

function verifyEmailExisted(){
    // let miNoValue = null;
    // if(miNo!==null){ //是修改 -> 再取user的編號
    //     miNoValue = miNo.value.trim();
    // }
    // let xhr = new XMLHttpRequest();
    // xhr.open("POST", "/Lung/FrontMember/CheckMemberEmail", false);
    // xhr.setRequestHeader("Content-Type",
    //     "application/json");
    // // xhr.send("accountToCheck=" + mi_accountValue);  //送出user輸入的值
    // xhr.send(JSON.stringify({ "emailToCheck": miEmailValue, "miNo":miNoValue }));  //送出user輸入的值
    //
    // let emailCanUse = false;
    // xhr.onreadystatechange = function () {
    //     if (xhr.readyState === 4 && xhr.status === 200) {
    //         // console.log("第一步");
    //         let result = JSON.parse(xhr.responseText);
    //         // console.log(result);
    //         //帳號不存在就回傳true，帳號存在回傳false
    //         emailCanUse = result.emailCanUse; //取key的方式，拿到 map 中的 value
    //     } else if (xhr.status !== 200) {
    //         setErrorFor(miEmail, '錯誤訊息：Server Error! 請聯絡系統管理員。');
    //         emailCanUse = false;
    //     }
    //     console.log('emailCanUse:' + emailCanUse)
    //     return emailCanUse;
    // }

    let miEmailValue = miEmail.value.trim();
    $.ajax({
        type: 'POST',
        url: '/Lung/FrontMember/CheckMemberEmail',
        dataType: "json",
        async: false,
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({"emailToCheck": miEmailValue, "miNo":null}),
        success: function (result) {
            console.log(result);
            if(result.emailCanUse === false){  //取key的方式，拿到 map 中的 value
                setErrorFor(miEmail, '此Email已被使用');
            } else {
                setSuccessFor(miEmail);
                return true;
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log(xhr.status);
            console.log(thrownError);
            return false;
        }
    });
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
    }else {
        verifyEmailExisted();
    }
}



/* 執行一鍵輸入*/
function oneClickToEnter(){
    /* 取消變顏色*/
    let errorNodes = document.getElementsByClassName('single-input-item error');
    let successNodes = document.getElementsByClassName('single-input-item success');
    for(let i = 0 ; i<errorNodes.length ; i++){
        errorNodes[i].className = 'single-input-item'
    }
    for(let i = 0 ; i<successNodes.length ; i++){
        successNodes[i].className = 'single-input-item'
    }

    /*塞入值到input框*/
    miAccount.value = 'zoro';/*randomAccount();*/
    miPassword.value = 'Zoro1111';/*randomPassword();*/
    miPassword2.value = 'Zoro1111';/*randomPassword();*/
    miName.value = '索隆';/*randomName();*/
    miId.value = 'a123456789';/*randomId();*/
    miBirth.value = '1977-09-03';/*randomBirth();*/
    miPhone.value = '0987993557';
    miEmail.value = 'jin991824@gmail.com';
    miCity.value = '臺北市';
    miCity.dispatchEvent(new Event('change')); //觸發change事件
    miAddress.value = '羅斯福路三段126之5號';
    // document.getElementById("newcountry1").value = '台中市';
    // document.getElementById("newdistrict1").setAttribute("data-value","大雅區");

    /* 重新把success加上去 */
    let nodes = document.getElementsByClassName('single-input-item');
    for(let i = 0 ; i<nodes.length ; i++){
        nodes[i].className = 'single-input-item success m-b-30'
    }

}
function oneClickToEnterError(){
    /* 取消變顏色*/
    let errorNodes = document.getElementsByClassName('single-input-item error');
    let successNodes = document.getElementsByClassName('single-input-item success');
    for(let i = 0 ; i<errorNodes.length ; i++){
        errorNodes[i].className = 'single-input-item'
    }
    for(let i = 0 ; i<successNodes.length ; i++){
        successNodes[i].className = 'single-input-item'
    }

    /*塞入值到input框*/
    miAccount.value = '';/*randomAccount();*/
    miPassword.value = 'zzzz';/*randomPassword();*/
    miPassword2.value = '1';/*randomPassword();*/
    miName.value = '';/*randomName();*/
    miId.value = 'a123456780';/*randomId();*/
    miBirth.value = '2050-01-05';/*randomBirth();*/
    miPhone.value = '0123456789';
    miEmail.value = 'jin991824@gmail.c';
    miCity.value = '';
    miAddress.value = '';

    /*呼叫所有驗證的方法，這個有return，不用理就好*/
    checkInputsError();

    /* 重新把error加上去 */
    let nodes = document.getElementsByClassName('single-input-item');
    for(let i = 0 ; i<nodes.length ; i++){
        nodes[i].className = 'single-input-item error m-b-30'
    }

}



// function setDistrict(){
//     console.log('onmouseup')
//     let miDistrict22 = document.getElementById('miDistrict')
//     miDistrict22.value = '大安區';
//     console.log("miDistrict.value是" + miDistrict.value);
// }


/*產生隨機帳號*/
function randomAccount(){
    let code="";
    let codeLength = Math.floor(Math.random()*6)+8;//隨機產生驗證碼的長度0-5碼再加8 -> 產生8-13碼
    let random = [0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];//隨機數
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

/* 塞錯誤訊息 */
function setErrorFor(input, message) {
    const inputParent = input.parentElement;
    const small = inputParent.querySelector('small');
    inputParent.className = 'single-input-item error m-b-30';
    small.innerText = message;
}

/* 塞正確訊息 */
function setSuccessFor(input) {
    const inputParent = input.parentElement;
    inputParent.className = 'single-input-item success m-b-30';
}

