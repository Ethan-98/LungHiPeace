// window.onload = function () {
    //先不show取消按鈕
    // let account_form_cancel = document.getElementById('account_form_cancel');
    // account_form_cancel.style.display = 'none';

    /*--------------------START塞value到會員表單中-------------------*/
    // let miName = document.getElementById('miName');
    // let miAccount = document.getElementById('miAccount');
    // let miGender = document.getElementById('miGender');
    // let miBirth = document.getElementById('miBirth');
    // let miId = document.getElementById('miId');
    // let miPhone = document.getElementById('miPhone');
    // let miEmail = document.getElementById('miEmail');
    // let miCity = document.getElementById('miCity');
    // let miDistrict = document.getElementById('miDistrict');
    // let miAddress = document.getElementById('miAddress');

const miNo = document.getElementById('miNo');
const miAccount = document.getElementById('miAccount');
const miPassword = document.getElementById('miPassword');
const miPassword1 = document.getElementById('miPassword1');
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

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) {
            let result = JSON.parse(xhr.responseText);
            console.log(result);
            miName.value = result.miName;
            miAccount.value = result.miAccount;
            miGender.value = result.miGender;
            miBirth.value = result.miBirth;
            miId.value = result.miId;
            miPhone.value = result.miPhone;
            miEmail.value = result.miEmail;
            miCity.value = result.miCity;
            miDistrict.value = result.miDistrict;
            miAddress.value = result.miAddress;

        }
    };
    xhr.open("GET", "/Lung/FrontMember/getMemberforUpdate", false);
    xhr.send();

    /*--------------------END塞value到會員表單中-------------------*/

// }
/* 點sidebar的會員基本資料 */
document.getElementById('memberCenterBasic').click();

document.getElementById('form_button_cancel').addEventListener('click', function () {
    if(document.getElementById('form_button_update').innerText === "儲存"){

        document.getElementById('miName').disabled = true;
        document.getElementById('miAccount').disabled = true;
        document.getElementById('miGender').disabled = true;
        document.getElementById('miBirth').disabled = true;
        document.getElementById('miId').disabled = true;
        document.getElementById('miPhone').disabled = true;
        document.getElementById('miEmail').disabled = true;
        document.getElementById('miCity').disabled = true;
        document.getElementById('miDistrict').disabled = true;
        document.getElementById('miAddress').disabled = true;

        document.getElementById('form_button_update').innerText = "修改";
    }
});

function updateHeadshot(){
    let data = new FormData();
    if ((document.getElementById("productImage").value) === '') {
        Swal.fire({
            icon: 'success',
            title: '會員資料修改成功囉!',
        }).then(() => {
            location.reload(); /* 讓頁面重整 */
        })
        return;
    }
    data.append('headshot', document.getElementById("productImage").files[0]);
    $.ajax({
        type: "POST",
        url: "/Lung/FrontMember/saveHeadshotforUpdate",
        data: data,
        processData: false,
        contentType: false,
        success: function () {
            Swal.fire({
                icon: 'success',
                title: '會員資料修改成功囉!',
            }).then(() => {
                location.reload(); /* 讓頁面重整 */
            })
        }
    });

}


document.getElementById('form_button_update').addEventListener('click', function () {
    // 按鈕是修改時
    if(document.getElementById('form_button_update').innerText === "修改"){
        //一開始是disabled -> 所以當按下"修改"時，變成不是disabled(可以編輯)
        document.getElementById('miName').disabled = false;
        // document.getElementById('miAccount').disabled = false; /*帳號不可以改*/
        document.getElementById('miGender').disabled = false;
        document.getElementById('miBirth').disabled = false;
        // document.getElementById('miId').disabled = false;
        document.getElementById('miPhone').disabled = false;
        // document.getElementById('miEmail').disabled = false; /*信箱不可以改*/
        document.getElementById('miCity').disabled = false;
        document.getElementById('miDistrict').disabled = false;
        document.getElementById('miAddress').disabled = false;
        //btn變成儲存功能
        document.getElementById('form_button_update').innerText = "儲存";
    }else{
        /* 如果沒有error，就執行送出*/
        if(!checkInputsError()){
            let miNameV = document.getElementById('miName').value.trim();
            let miAccountV = document.getElementById('miAccount').value.trim();
            let miGenderV = document.getElementById('miGender').value.trim();
            let miBirthV = document.getElementById('miBirth').value.trim();
            let miIdV = document.getElementById('miId').value.trim();
            let miPhoneV = document.getElementById('miPhone').value.trim();
            let miEmailV = document.getElementById('miEmail').value.trim();
            let miCityV = document.getElementById('miCity').value.trim();
            let miDistrictV = document.getElementById('miDistrict').value.trim();
            let miAddressV = document.getElementById('miAddress').value.trim();

            let xhr1 = new XMLHttpRequest();
            let memberToSave = {
                "miName": miNameV,
                "miAccount": miAccountV,
                "miGender": miGenderV,
                "miBirth": miBirthV,
                "miId": miIdV,
                "miPhone": miPhoneV,
                "miEmail": miEmailV,
                "miCity": miCityV,
                "miDistrict": miDistrictV,
                "miAddress": miAddressV
            }
            let jsonString = JSON.stringify(memberToSave);
            console.log(jsonString);
            xhr1.open("POST", "/Lung/FrontMember/saveMemberforUpdate", true);
            xhr1.setRequestHeader("Content-type", "application/json");
            xhr1.send(jsonString);
            xhr1.onreadystatechange = function() {
                // 向伺服器提出的請求已經收到回應
                if (xhr1.readyState === 4 && xhr1.status === 200) {
                    console.log("修改會員資料xhr1.responseText:" + xhr1.responseText);
                    let obj = JSON.parse(xhr1.responseText);
                    // console.log("obj: "+obj.success);
                    if(obj.success === 'success'){
                        document.getElementById('miName').disabled = true;
                        document.getElementById('miGender').disabled = true;
                        document.getElementById('miBirth').disabled = true;
                        document.getElementById('miPhone').disabled = true;
                        document.getElementById('miCity').disabled = true;
                        document.getElementById('miDistrict').disabled = true;
                        document.getElementById('miAddress').disabled = true;
                        document.getElementById('form_button_update').innerText = "修改";
                        //修改圖片
                        updateHeadshot();

                    }
                }
            }
        }else{
            Swal.fire({
                icon: 'error',
                title: '會員修改失敗！',
            })
        }
    }
});

/* 確定會員舊密碼是否相同 */
function onblurCheckOldPassword(){
// 舊密碼相同才可以送出更新密碼的請求
    let inputPassword = document.getElementById('miPassword').value.trim();
    let xhr1 = new XMLHttpRequest();
    let passwordToCheck = {"passwordToCheck":inputPassword}
    let jsonString = JSON.stringify(passwordToCheck);
    // console.log(jsonString);
    xhr1.onreadystatechange = function() {
        if (xhr1.readyState == 4 && xhr1.status == 200) {
            // console.log("密碼是否和db中舊密碼相同" + xhr1.responseText);
            let obj = JSON.parse(xhr1.responseText);

            // console.log("obj: "+obj.checkPasswordResult);
            if(obj.checkPasswordResult === 'fail'){
                setErrorFor(miPassword, '您輸入的密碼與舊密碼不相符喔!');
                return false;
            }else{
                setSuccessFor(miPassword);
                return true;
            }
        }
    }
    xhr1.open("POST", "/Lung/FrontMember/checkPassword", true);
    // xhr1.setRequestHeader("Content-type", "application/json");
    xhr1.send(jsonString);
}


const sleep = ms => new Promise(r => setTimeout(r, ms));
/* 修改會員密碼 */
/*document.getElementById('btnUpdatePassword').addEventListener('click', function () {
    // TODO ------if(onblurCheckOldPassword() === true){
        let xhr1 = new XMLHttpRequest();
        let pwdToSave = {
            "newPassword": document.getElementById('miPassword2').value.trim()
        }
        let jsonString = JSON.stringify(pwdToSave);
        console.log(jsonString);

        xhr1.onreadystatechange = async function () {
            // 向伺服器提出的請求已經收到回應
            if (xhr1.readyState === 4 && xhr1.status === 200) {
                console.log("修改會員密碼 responseText:" + xhr1.responseText);
                let obj = JSON.parse(xhr1.responseText);

                // console.log("obj: " + obj.success);
                if (obj.success === 'success') {
                    Swal.fire({
                        icon: 'success',
                        title: '修改密碼成功囉!',
                    })
                    await sleep(800);
                }
            }
        }
        xhr1.open("POST", "/Lung/FrontMember/savePasswordforUpdate", true);
        // xhr1.setRequestHeader("Content-type", "application/json");
        xhr1.send(jsonString);
    // }
})*/

document.getElementById('btnUpdatePassword').addEventListener('click', function () {
    if(miPassword.parentElement.className === 'single-input-item success m-b-30' &&
        miPassword1.parentElement.className === 'single-input-item success m-b-30' &&
        miPassword2.parentElement.className === 'single-input-item success m-b-30')
    {
        $.ajax({
            type: 'POST',
            url: '/Lung/FrontMember/savePasswordforUpdate',
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify({"newPassword": document.getElementById('miPassword2').value.trim()}),
            success: function (result) {
                if (result.success === 'success') {
                    Swal.fire({
                        icon: 'success',
                        title: '修改密碼成功囉!',
                    })
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log(xhr.status);
                console.log(thrownError);
            }
        });
    }else{
        Swal.fire({
            icon: 'error',
            title: '修改密碼失敗！',
        })
    }

})


/*------------------Common js-----------------------*/
// const form_button_submit = document.getElementById('form_button_submit');
// const oneClickEnter = document.getElementById('oneClickEnter');

/*送出時進行資料確認*/
const isFalse = (element) => element === false;
function checkInputsError() {
    let checkSuccess;
    checkSuccess = [
        // oninputCheckPassword(),
        oninputCheckName(),
        oninputCheckBirth(),
        oninputCheckPhone(),
        onblurCheckAddress()]
    // console.log( "表單送出時確認資料: " +checkSuccess);
    return checkSuccess.some(isFalse);  /*翻譯: 陣列中有沒有false?*/
    // some 裡面放方法, 會把 array(eg.checkSuccess) 裡面的東西一個一個放到 some 括號內的方法中
    // array 中有任何滿足 some 中方法(eg.isFalse)的條件, some 就回傳 true(對，陣列中有false)
}


/*一鍵輸入*/
// oneClickEnter.addEventListener('click', e => {
//     e.preventDefault();
//     oneClickToEnter();
// });

//轉換eye -> 顯示密碼
let togglePassword = document.querySelector("#togglePassword");
togglePassword.addEventListener('click', function () {
    let miPassword = document.getElementById('miPassword');
    // 判斷password 還是text
    let type = miPassword.getAttribute('type') === 'password' ? 'text' : 'password';  //三元運算式，把抓到的type存回type
    miPassword.setAttribute('type', type);  //改變type
    this.classList.toggle('fa-eye-slash'); //轉換眼睛圖示
    miPassword.focus();
});

let togglePassword1 = document.querySelector("#togglePassword1");
togglePassword1.addEventListener('click', function () {
    let miPassword1 = document.getElementById('miPassword1');
    let type = miPassword1.getAttribute('type') === 'password' ? 'text' : 'password';  //三元運算式，把抓到的type存回type
    miPassword1.setAttribute('type', type);  //改變type
    this.classList.toggle('fa-eye-slash'); //轉換眼睛圖示
    miPassword1.focus();
});
let togglePassword2 = document.querySelector("#togglePassword2");
togglePassword2.addEventListener('click', function () {
    let miPassword2 = document.getElementById('miPassword2');
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
    let regex = new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/g)  //!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?
    let verification = regex.test(miPasswordValue);
    return verification;
}

function oninputCheckPassword1(){
    let miPassword1 = document.getElementById('miPassword1');
    //確認密碼
    let miPassword1Value = miPassword1.value.trim();
    // console.log("密碼1: " + miPasswordValue)
    if(miPassword1Value === '') {
        setErrorFor(miPassword1, '密碼不能為空');
        return false;
    } else {
        let v = verifyPassword(miPassword1Value);
        if(v === false){
            setErrorFor(miPassword1, '密碼格式錯誤，請重新輸入');
            return false;
        } else {
            setSuccessFor(miPassword1);
            return true;
        }
    }
}

function onblurCheckPassword1SamePassword2(){
    let miPassword1 = document.getElementById('miPassword1');
    let miPassword2 = document.getElementById('miPassword2');
    let miPassword1Value = miPassword1.value.trim();
    let miPassword2Value = miPassword2.value.trim();
    if(miPassword1Value !== miPassword2Value) {
        setErrorFor(miPassword2, '新密碼必須兩次都輸入一樣的喔!請重新確認!');
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

function verifyEmailExisted(miEmailValue){
    let miNoValue = null;
    if(miNo!==null){ //是修改 -> 再取user的編號
        miNoValue = miNo.value.trim();
    }
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/Lung/FrontMember/CheckMemberEmail", false);
    xhr.setRequestHeader("Content-Type",
        "application/json");
    // xhr.send("accountToCheck=" + mi_accountValue);  //送出user輸入的值
    xhr.send(JSON.stringify({ "emailToCheck": miEmailValue, "miNo":miNoValue }));  //送出user輸入的值

    let emailCanUse = false;
    if (xhr.readyState === 4 && xhr.status === 200) {
        // console.log("第一步");
        let result = JSON.parse(xhr.responseText);
        // console.log(result);
        //帳號不存在就回傳true，帳號存在回傳false
        emailCanUse = result.emailCanUse; //取key的方式，拿到 map 中的 value
    }else if(xhr.status !== 200){
        setErrorFor(miEmail, '錯誤訊息：Server Error! 請聯絡系統管理員。');
        emailCanUse = false;
    }
    console.log('emailCanUse:' + emailCanUse)
    return emailCanUse;
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
    }else if(verifyEmailExisted(miEmailValue) === false){
        setErrorFor(miEmail, '此Email已被使用');
        return false;
    }else {
        setSuccessFor(miEmail);
        return true;
    }
}



/* 執行一鍵輸入*/
// function oneClickToEnter(){
//     /* 取消變顏色*/
//     console.log('onmousedown')
//     let errorNodes = document.getElementsByClassName('form-group error');
//     let successNodes = document.getElementsByClassName('form-group success');
//     for(let i = 0 ; i<errorNodes.length ; i++){
//         errorNodes[i].className = 'form-group'
//     }
//     for(let i = 0 ; i<successNodes.length ; i++){
//         successNodes[i].className = 'form-group'
//     }
//     /*塞入值到input框*/
//     miAccount.value = randomAccount();
//     miPassword.value = randomPassword();
//     miName.value = randomName();
//     miId.value = randomId();
//     miBirth.value = randomBirth();
//     miPhone.value = '0987993557';
//     miEmail.value = 'email@mail.com';
//     miCity.value = '臺北市';
//     miCity.dispatchEvent(new Event('change')); //觸發change事件
//
//     miAddress.value = '羅斯福路三段126之5號';
//     document.getElementById("check-roleADMIN").checked = true;
//     document.getElementById("check-roleEMPLOYEE").checked = true;
//     document.getElementById("check-roleACTIVE").checked = true;
//     document.getElementById("check-roleUSER").checked = true;
//
//     document.getElementById("newcountry1").value = '台中市';
//     document.getElementById("newdistrict1").setAttribute("data-value","大雅區");
//
//     // $(".city-selector-set").each(function() {
//     // 	$(this).attr("data-value", "大雅區");
//     // });
//
//     /* 重新把success加上去 */
//     let nodes = document.getElementsByClassName('form-group');
//     for(let i = 0 ; i<nodes.length ; i++){
//         nodes[i].className = 'form-group success'
//     }
//
// }

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





