function showAlert(orderId) {
    Swal.showLoading()
    fetch('//localhost:8080/Lung/Orders/' + orderId)
        .then(response => {
            if (!response.ok) {
                throw new Error(response.statusText)
            }
            return response.json()
        })
        .catch(error => {
            Swal.showValidationMessage(
                `Request failed: ${error}`
            )
        })
        .then(function (result) {

            console.log(result)
            let data = getData(result);
            let trackingNumber = getTrackingNumber(result);
            Swal.fire({
                title: "訂單明細",
                html: `<table class="table text-center">
					<thead>
						<tr>
							<th scope="col" class="col-sm-6">商品名稱</th>
							<th scope="col" class="col-sm-2">數量</th>
							<th scope="col" class="col-sm-2">小計</th>
						</tr>
					</thead>
					<tbody>
                        ${data}
					</tbody>
					<tfoot>
                        <tr>
							<td class="text-lift">合計</td>
							<td colspan="3" class="text-center" id="total">NT$${result.totalPrice}</td>
						</tr>
                        <tr>
                            <td class="text-lift col-sm-2">地址</td>
                            <td colspan="3" class="text-center col-sm-10">${result.address}</td>
                        </tr>             
                        ${trackingNumber}           
					</tfoot>
				</table>`,
                width: 1500
            })

        });

}

function getData(result) {
    let data = "";
    result.orderItems.forEach(element => {
        data = data +
            `<tr>
			<td class="product-name">${element.productName}</td>
			<td class="product-qty">${element.qty}</td>
			<td class="text-center product-subTotal">NT$${element.subTotal}</td>
		</tr>`
    });
    return data;
}

function getTrackingNumber(result) {
    if (result.trackingNumber == null) {
        return "";
    } else {
        return `<tr>
                   <td class="text-lift col-sm-2">物流單號</td>
                   <td colspan="3" class="text-center col-sm-10">${result.trackingNumber}</td>
             </tr>
             <tr>
                   <td class="text-lift col-sm-2">發貨日期</td>
                   <td colspan="3" class="text-center col-sm-10">${result.shipDate}</td>
             </tr>`
    }
}


function deleteAlert(orderId) {
    Swal.fire({
        title: '確定刪除此訂單?',
        text: "刪除後，相關資訊將會消失!",
        icon: 'warning',
        showCancelButton: true,
        cancelButtonText: '取消',
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: '刪除'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: "DELETE",
                url: '/Lung/Orders/' + orderId,
                success: function (msg) {
                    Swal.fire(
                        '已刪除!',
                        '訂單已成功刪除!',
                        'success'
                    ).then((result) => {
                        if (result.isConfirmed) {
                            location.reload();
                        }
                    })
                },
                error: function (msg) {
                    // console.log(msg.status)
                    Swal.fire({
                        icon: 'error',
                        title: '發生錯誤',
                        text: 'HTTP 狀態碼為 ' + msg.status,
                        footer: '<a href="https://developer.mozilla.org/zh-TW/docs/Web/HTTP/Status"  target="_blank">請聯絡IT人員</a>'
                    })
                }
            });


        }
    })
}

function shipAlert(orderNo) {
    Swal.fire({
        title: '請輸入物流編號',
        input: 'text',
        inputAttributes: {
            autocapitalize: 'off'
        },
        showCancelButton: true,
        cancelButtonText: '返回',
        confirmButtonText: '確認',
        showLoaderOnConfirm: true,
        preConfirm: (trackingNumber) => {

            return fetch('http://localhost:8080/Lung/Order/' + orderNo + '/Ship?trackingNumber=' + trackingNumber,
                {
                    method: "POST"
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(response.statusText)
                    }
                    return response.json()
                })
                .catch(error => {
                    Swal.showValidationMessage(
                        `發貨發生錯誤: ${error}`
                    )
                })
        }
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: `已成功發貨`,
                icon: 'success',
            }).then((result) => {
                if (result.isConfirmed) {
                    location.reload();
                }
            })
        }
    })
}

function refundingAlert(orderNo) {
    Swal.fire({
        title: '是否要通過退款請求?',
        showDenyButton: true,
        showCancelButton: true,
        confirmButtonText: '接受',
        denyButtonText: `拒絕`,
        cancelButtonText: '取消',
    }).then((result) => {
        let baseUrl = 'http://localhost:8080/Lung/Order/' + orderNo + '/Refunding?choose=';
        if (result.isConfirmed) {
            fetch(baseUrl + 'accept',
                {
                    method: "POST"
                }).then(
                Swal.fire('退款請求已同意', '', 'success')
            )
        } else if (result.isDenied) {
            fetch(baseUrl + 'reject',
                {
                    method: "POST"
                }).then(
                Swal.fire('退款請求已拒絕', '', 'success')
            )
        }
    })
}

function refundAlert(orderNo) {
    Swal.fire({
        title: '請確認是否要提出退款請求',
        showCancelButton: true,
        confirmButtonText: '提出',
        cancelButtonText: '放棄',
    }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
            fetch('http://localhost:8080/Lung/Order/' + orderNo + '/Refund',
                {
                    method: "POST"
                }).then(
                Swal.fire('退款請求已提出', '', 'success')
                    .then((result) => {
                        if (result.isConfirmed) {
                            location.reload();
                        }
                    })
            )
        }
    })
}

function cancelAlert(orderNo) {
    Swal.fire({
        title: '確認取消訂單嗎?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '確定',
        cancelButtonText: '返回',
    }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
            fetch('http://localhost:8080/Lung/Order/' + orderNo + '/Cancel',
                {
                    method: "POST"
                }).then(
                Swal.fire('訂單已取消', '', 'success')
                    .then((result) => {
                        if (result.isConfirmed) {
                            location.reload();
                        }
                    })
            )
        }
    })
}

function receiveAlert(orderNo) {
    Swal.fire({
        title: '請確認是否已收到商品',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '確定',
        cancelButtonText: '還沒',
    }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
            fetch('http://localhost:8080/Lung/Order/' + orderNo + '/Receive',
                {
                    method: "POST"
                }).then(
                Swal.fire('謝謝您的惠顧', '', 'success')
                    .then((result) => {
                        if (result.isConfirmed) {
                            location.reload();
                        }
                    })
            )
        }
    })
}

