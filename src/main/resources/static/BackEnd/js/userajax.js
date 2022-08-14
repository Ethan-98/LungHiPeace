// window.onload = function(){
    /*--------------------START後台首頁navbar的USER姓名-------------------*/
    let navbarMiName = document.getElementById("navbarMiName");  // 首頁顯示的姓名
    let navbarMiName1 = document.getElementById("navbarMiName1");  // 大頭貼位置下拉選單的姓名
    let xhrBackNavbar = new XMLHttpRequest();
    xhrBackNavbar.onreadystatechange = function(){
        // alert(xhr.responseText + ", readyState=" + xhr.readyState);
        if (xhrBackNavbar.readyState === 4 && xhrBackNavbar.status === 200) {
            navbarMiName.innerHTML = xhrBackNavbar.responseText ;
            navbarMiName1.innerHTML = xhrBackNavbar.responseText ;
        }
    }
    xhrBackNavbar.open("GET", "/Lung/memberInfo/getCurrentUserMiNameString", true);
    xhrBackNavbar.send();
    /*--------------------END後台首頁navbar的USER姓名-------------------*/

    /*--------------------START測試取得USER資訊-------------------*/
    // TODO 要先開啟Spring Security權限設定，才不會噴錯
    // let xhr1 = new XMLHttpRequest();
    // xhr1.onreadystatechange = function(){
    //     // alert(xhr.responseText + ", readyState=" + xhr.readyState);
    //     if (xhr1.readyState === 4 && xhr1.status === 200) {
    //         console.log(xhr1.responseText)
    //     }
    // }
    // xhr1.open("GET", "/Lung/memberInfo/getCurrentMemerBean", true);
    // xhr1.send();
    /*--------------------END測試取得USER資訊-------------------*/




// }
